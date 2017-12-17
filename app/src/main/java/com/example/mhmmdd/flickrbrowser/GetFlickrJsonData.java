package com.example.mhmmdd.flickrbrowser;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammed on 4.12.2017.
 */

class GetFlickrJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFlickrJsonData";

    private List<Photo> photoList;
    private String baseURL;
    private String language;
    private boolean matchAll;

    private final OnDataAvailable callback;
    private boolean runningOnSameTherad = false;

    public GetFlickrJsonData(OnDataAvailable callBack, String baseURL, String language, boolean matchAll) {
        Log.d(TAG, "GetFlickrJsonData: called");
        this.callback = callBack;
        this.baseURL = baseURL;
        this.language = language;
        this.matchAll = matchAll;
    }

    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute starts");

        if(callback != null) {
            callback.onDataAvailable(photoList, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute ends");
    }

    @Override
    protected List<Photo> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground starts");
        String destinationUri = createUri(strings[0], language, matchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground ends");
        return photoList;
    }

    void executeOnSomeThread(String searchCriteria) {
        Log.d(TAG, "executeOnSomeThread: starts");
        runningOnSameTherad = true;
        String destinationURL = createUri(searchCriteria, language, matchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationURL);
        Log.d(TAG, "executeOnSomeThread: ends");
    }

    private String createUri(String searchCriteria, String language, boolean matchAll) {
        Log.d(TAG, "createUri starts");

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

                Photo photo = new Photo(title, author, authorId, link, tags, photoURL);
                photoList.add(photo);

                Log.d(TAG, "onDownloadComplete: " + photo.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "onDownloadComplete: Error processing JSON data " + e.getMessage());
            status = DownloadStatus.FAILED_OR_EMPTY;
        }

        if (runningOnSameTherad && callback != null) {
            callback.onDataAvailable(photoList, status);
        }
	    Log.d(TAG, "onDownloadComplete: ends");
    }

}
