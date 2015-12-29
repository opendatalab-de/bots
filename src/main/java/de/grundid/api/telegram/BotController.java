package de.grundid.api.telegram;

import de.grundid.api.utils.ByDistanceComparator;
import de.grundid.api.utils.Utils;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.telegram.api.methods.SendMessage;
import org.telegram.api.objects.Location;
import org.telegram.api.objects.Update;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

@RestController
public class BotController {

    private DecimalFormat decimalFormat = new DecimalFormat("#,##0.00",
            DecimalFormatSymbols.getInstance(Locale.GERMANY));
    private static Logger log = LoggerFactory.getLogger(BotController.class);
    @Autowired
    private RestOperations restOperations;

    @RequestMapping(value = "/bot/recycling", method = RequestMethod.POST)
    public ResponseEntity<?> post(@RequestBody Update update) {
        logInfos(update);
        Location location = update.getMessage().getLocation();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        if (location != null) {
            Calendar now = Calendar.getInstance();
            int month = now.get(Calendar.MONTH) + 1;
            FeatureCollection featureCollection = restOperations.getForObject(
                    "https://api.grundid.de/recycling?month=" + month, FeatureCollection.class);
            List<Feature> features = new ArrayList<>(featureCollection.getFeatures());
            Collections
                    .sort(features, new ByDistanceComparator(location.getLatitude(), location.getLongitude()));
            StringBuilder response = new StringBuilder();
            for (Feature feature : features.subList(0, Math.min(3, features.size()))) {
                Point point = (Point)feature.getGeometry();
                Map<String, Object> properties = feature.getProperties();
                Map<String, String> address = (Map<String, String>)properties.get("address");
                response.append("[").append(properties.get("name")).append("](");
                response.append("http://maps.google.com/maps?q=").append(point.getCoordinates().getLatitude())
                        .append(",")
                        .append(point.getCoordinates().getLongitude()).append(")\n");
                response.append(address.get("street")).append("\n");
                response.append(address.get("zip")).append(" ");
                response.append(address.get("city")).append("\n");
                double distanceMeters = Utils.distFrom(location.getLatitude(), location.getLongitude(),
                        point.getCoordinates().getLatitude(),
                        point.getCoordinates().getLongitude());
                response.append("Entfernung: ").append(decimalFormat.format(distanceMeters / 1000)).append("km");
                response.append("\n\n");
            }
            sendMessage.setText(response.toString());
            sendMessage.enableMarkdown(true);
        }
        else {
            if (StringUtils.hasText(update.getMessage().getText())) {
                sendMessage.setText(
                        "Schick mir bitte deine Position, dann kann ich dir Recyclinghöfe in deiner Nähe schicken.");
            }
        }
        return ResponseEntity.ok(sendMessage);
    }

    private void logInfos(@RequestBody Update update) {
        log.info("bot text: " + update.getMessage().getText());
        log.info("bot location: " + update.getMessage().getLocation());
        if (update.getMessage().getLocation() != null) {
            log.info("bot lat: " + update.getMessage().getLocation().getLatitude() + " lon: " + update.getMessage()
                    .getLocation().getLongitude());
        }
        log.info("bot from: " + update.getMessage().getFrom().getUserName());
    }
}
