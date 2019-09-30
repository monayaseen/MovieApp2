package com.example.movieappstage2;


import android.net.Uri;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class JSON {
    public static URL buildUrl(String[] query) {
        Uri builtUri = Uri.parse(Const.MOVIEDB_BASE_URL).buildUpon()
                .appendPath(query[0])
                .appendQueryParameter(Const.API_KEY_QUERY_PARAM, Const.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildMovieIdUrl(String id, String query) {
        Uri builtUri = Uri.parse(Const.MOVIEDB_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(query)
                .appendQueryParameter(Const.API_KEY_QUERY_PARAM, Const.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
