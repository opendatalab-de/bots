package de.grundid.api.telegram.weather;

import de.grundid.api.telegram.AbstractBotInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by lukas on 19.05.16.
 */
@Service
public class WeatherBotService extends AbstractBotInitService {

    @Autowired
    public WeatherBotService(@Value("${telegram.cowoHnWeatherBot.apiKey}") String apiKey) {
        super(apiKey, "https://bots.grundid.de/bot/cowoWeather");
    }
}
