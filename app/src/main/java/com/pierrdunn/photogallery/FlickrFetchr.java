package com.pierrdunn.photogallery;

import android.graphics.LinearGradient;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by pierrdunn on 09.03.18.
 * Сетевая поддержка (Flickr)
 */

public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";
    private static final String API_KEY = "e2ddc6feeca22e9c12f7fe8b0a401617";

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

    //Построение URL запроса
    public List<GalleryItem> fetchItems(){

        List<GalleryItem> items = new ArrayList<>();

        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            //разбор json ответа
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (IOException ioe){
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return items;
    }

    //Разбор распарщенных фотографий
    private void parseItems(List<GalleryItem> items, JSONObject jsonObject)
        throws IOException, JSONException{

        JSONObject photosJsonObject = jsonObject.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

        for (int i = 0; i < photoJsonArray.length(); i++){
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();
            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));

            if (!photoJsonObject.has("url_s"))
                continue;

            item.setUrl(photoJsonObject.getString("url_s"));
            items.add(item);
        }

    }
}
