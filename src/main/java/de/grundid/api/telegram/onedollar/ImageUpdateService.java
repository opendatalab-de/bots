package de.grundid.api.telegram.onedollar;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.api.methods.Constants;
import org.telegram.api.methods.SendPhoto;

import java.io.*;

@Service
public class ImageUpdateService {

    private static Logger log = LoggerFactory.getLogger(ImageUpdateService.class);
    @Value("${telegram.onedollarBot.apiKey}") private String apiKey;
    private RestTemplate restTemplate = new RestTemplate();
    private Integer sendImageChatid;
    @Value("${datastoreDir}") private String datastoreDir;

    public void updateImages() throws IOException {
        if (sendImageChatid != null) {
            String url = Constants.BASEURL + apiKey + "/" + SendPhoto.PATH;
            System.out.println(url);
            InputStream image = OneDollarBotController.class.getResourceAsStream("/hochzeitsplanerplus-qrcode.png");

/*            ByteArrayOutputStream outImage = new ByteArrayOutputStream();
            IOUtils.copy(image, outImage); */
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
                HttpPost httppost = new HttpPost(url);
                HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("chat_id",
                        new StringBody("" + sendImageChatid, ContentType.TEXT_PLAIN.withCharset("utf8"))).addPart(
                        "caption",
                        new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN.withCharset("utf8")))
                        //.addPart("photo", new ByteArrayBody(outImage.toByteArray(), ContentType.create("image/png"), "image.png"))
                        .addPart("photo", new InputStreamBody(image, ContentType.create("image/png"), "image.png"))
                        .build();
                httppost.setEntity(reqEntity);
                System.out.println("executing request " + httppost.getRequestLine());
                // httppost.getEntity().writeTo(System.out);
                CloseableHttpResponse response = httpclient.execute(httppost);
                try {
                    System.out.println("----------------------------------------");
                    System.out.println(response.getStatusLine());
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        System.out.println("Response content length: " + resEntity.getContentLength());
                        resEntity.writeTo(System.out);
                    }
                    EntityUtils.consume(resEntity);
                }
                finally {
                    response.close();
                }
            }
            finally {
                httpclient.close();
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

    public void sendPhoto(Integer chatId, InputStream image) {
        HttpPost httppost = new HttpPost("");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .addPart("chat_id", new StringBody("" + chatId, ContentType.TEXT_PLAIN.withCharset("utf8"))).addPart(
                        "caption",
                        new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN.withCharset("utf8")))
                        //.addPart("photo", new ByteArrayBody(outImage.toByteArray(), ContentType.create("image/png"), "image.png"))
                .addPart("photo", new InputStreamBody(image, ContentType.create("image/png"), "image.png"));
        builder.build();
        System.out.println("executing request " + httppost.getRequestLine());
        // httppost.getEntity().writeTo(System.out);
    }

    public HttpOutputMessage createSendPhoto(String fileName, String caption, Integer chatId) throws IOException {
        FormHttpMessageConverter converter = new FormHttpMessageConverter();
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
            map.add("chat_id", "" + chatId);
            if (caption != null)
                map.add("caption", caption);
            map.add("photo", new FileSystemResource(datastoreDir + File.separator + fileName));
            converter.write(map, MediaType.MULTIPART_FORM_DATA, outputMessage);
            return outputMessage;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}