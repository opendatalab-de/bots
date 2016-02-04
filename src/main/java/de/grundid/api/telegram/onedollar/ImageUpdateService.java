package de.grundid.api.telegram.onedollar;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.api.methods.Constants;
import org.telegram.api.methods.SendPhoto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class ImageUpdateService {

    private static Logger log = LoggerFactory.getLogger(ImageUpdateService.class);
    @Value("${telegram.onedollarBot.apiKey}")
    private String apiKey;
    private RestTemplate restTemplate = new RestTemplate();

    private Integer sendImageChatid;


    @Scheduled(fixedDelay = 5000)
    public void updateImages() throws IOException {

        if (sendImageChatid != null) {
            FormHttpMessageConverter converter = new FormHttpMessageConverter();

            InputStream image = OneDollarBotController.class.getResourceAsStream("/hochzeitsplanerplus-qrcode.png");

            ByteArrayOutputStream outImage = new ByteArrayOutputStream();
            IOUtils.copy(image, outImage);


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            HttpHeaders httpHeaders = new HttpHeaders();

            try {
                HttpOutputMessage outputMessage = new HttpOutputMessage() {
                    @Override
                    public OutputStream getBody() throws IOException {
                        return outputStream;
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        return httpHeaders;
                    }
                };
                MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
                map.add("method", "sendPhoto");
                map.add("chat_id", "" + sendImageChatid);
                map.add("caption", "Imagebeschreibung");
                map.add("photo", outImage.toByteArray());

                converter.write(map, MediaType.MULTIPART_FORM_DATA, outputMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }

            HttpEntity<OutputStream> entity = new HttpEntity<>(outputStream, httpHeaders);

            ResponseEntity<String> responseEntity = restTemplate.exchange(Constants.BASEURL + apiKey + "/" + SendPhoto.PATH, HttpMethod.POST, entity, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("Send Message OK: " + responseEntity.getBody());
            } else {
                log.error("Error setting hook: " + responseEntity.getBody());
            }
        }
        sendImageChatid = null;
    }


    public Integer getSendImageChatid() {
        return sendImageChatid;
    }

    public void setSendImageChatid(Integer sendImageChatid) {
        this.sendImageChatid = sendImageChatid;
    }
}