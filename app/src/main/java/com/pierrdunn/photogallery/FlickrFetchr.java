package com.pierrdunn.photogallery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by pierrdunn on 09.03.18.
 * Сетевая поддержка (Flickr)
 */

public class FlickrFetchr {

    //Получает низкоуровневые данные по URL и
    //возвращает их в виде массива байтов
    public byte[] getUrlBytes(String urlSpec) throws IOException{
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpsURLConnection) url
                .openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK)
                throw new IOException(connection.getResponseMessage()
                + ": with " + urlSpec);

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0)
                out.write(buffer, 0, bytesRead);

            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    //Преобразует getUrlBytes в String
    public String getUrlString(String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }

}
