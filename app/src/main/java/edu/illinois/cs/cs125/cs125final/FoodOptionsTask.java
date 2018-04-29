package edu.illinois.cs.cs125.cs125final;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import edu.illinois.cs.cs125.cs125final.MainScreen;
import edu.illinois.cs.cs125.cs125final.MapsActivity;

public class FoodOptionsTask extends AsyncTask<String, Void, JSONObject> implements OnMapReadyCallback {

    private double[] latitudes;
    private double[] longitudes;
    private String[] markerNames;
    private MapsActivity activity;
    private int length;

    public FoodOptionsTask(MapsActivity activity){
        this.activity = activity;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        HttpURLConnection connection = null;
        URL url = null;
        try {
            url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            return new JSONObject(slurp(connection.getInputStream()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //https://stackoverflow.com/questions/31202840/creating-a-jsonobject-from-inputstreamreader/31203092
    public static String slurp(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        super.onPostExecute(json);

        try {
            length = json.getJSONArray("results").length();
            if (length >= 10) {
                length  = 10;
            }
            latitudes = new double[length];
            longitudes = new double[length];
            markerNames = new String[length];
            for (int i = 0; i < length; i++) {
                latitudes[i] = json.getJSONArray("results").getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                longitudes[i] = json.getJSONArray("results").getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                markerNames[i] = json.getJSONArray("results").getJSONObject(i).getString("name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) activity.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        for (int i = 0; i < length; i++) {
            LatLng location = new LatLng(latitudes[i], longitudes[i]);
            googleMap.addMarker(new MarkerOptions().position(location).title(markerNames[i]));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        }
        LatLng location = new LatLng(latitudes[0], longitudes[0]);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }
}
