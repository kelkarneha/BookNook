package com.example.android.booknook.asynctaskdemo;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2/14/2017.
 */

public class GetBookData extends AsyncTask<String, Void, String> {
    private String rawData;
    private String LogTag = GetBookData.class.getSimpleName();

    protected String doInBackground (String... params){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        if(params == null){
            return null;
        }

        try{
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if(inputStream == null){
                return null;
            }

            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine())!=null){
                buffer.append(line + "\n");
            }

            rawData = buffer.toString();

        }catch(IOException e){
            Log.e(LogTag, "Error", e);
            return null;
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if (reader != null){
                try{
                    reader.close();
                }catch(final IOException e){
                    Log.e(LogTag, "Error closing stream", e);
                }
            }
        }
        return rawData;
    }
    protected void onPostExecute (String rawData) {
        if (rawData != null) {
            Log.v(LogTag, rawData);
        }
    }
}
