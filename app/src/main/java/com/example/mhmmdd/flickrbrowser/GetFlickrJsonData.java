package com.example.mhmmdd.flickrbrowser;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammed on 4.12.2017.
 */

class GetFlickrJsonData implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFlickrJsonData";

    private List<Photo> photoList;
    private String baseURL;
    private String language;
    private boolean matchAll;

    private final OnDataAvailable callback;

    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    public GetFlickrJsonData(List<Photo> photoList, String language, boolean matchAll, OnDataAvailable callback) {
        this.photoList = photoList;
        this.language = language;
        this.matchAll = matchAll;
        this.callback = callback;
    }

    void executeOnSomeThread(String searchCriteria) {
        Log.d(TAG, "executeOnSomeThread: starts");
        String destinationURL = createURL(searchCriteria, language, matchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationURL);
        Log.d(TAG, "executeOnSomeThread: ends");
    }

    private String createURL(String searchCriteria, String language, boolean matchAll) {
        Log.d(TAG, "createURL: starts");

        return Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", language)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }


    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts. Status = " + status);

        if(status == DownloadStatus.OK) {
            photoList = new ArrayList<>();
        }

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            for(int i = 0; i < itemsArray.length(); i++) {
                JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                String title = jsonPhoto.getString("title");
                String author = jsonPhoto.getString("author");
                String authorId = jsonPhoto.getString("author_id");
                String tags = jsonPhoto.getString("tags");

                JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                String photoURL = jsonMedia.getString("m");

                String link = photoURL.replaceFirst("_m.", "_b.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
