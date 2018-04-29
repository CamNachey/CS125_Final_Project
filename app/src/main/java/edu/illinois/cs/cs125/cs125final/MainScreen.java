package edu.illinois.cs.cs125.cs125final;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class MainScreen extends AppCompatActivity {

    String constant = "https://maps.googleapis.com/maps/api/place/textsearch/json?rankby=distance&location=40.1020,-88.2272&opennow&type=restaurant&query=";
    String key = "AIzaSyCAx56k6PKMB3vo8jYkiD_pMa7m8TKtEhw";
    String price;
    String foodType;
    RadioButton zero;
    RadioButton one;
    RadioButton two;
    RadioButton three;
    RadioButton fastFood;
    RadioButton mexican;
    RadioButton pizza;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Button button = findViewById(R.id.button4);
        fastFood = (RadioButton) findViewById(R.id.fastFood);
        mexican = (RadioButton) findViewById(R.id.mexican);
        pizza = (RadioButton) findViewById(R.id.pizza);
        zero = (RadioButton) findViewById(R.id.zero);
        one = (RadioButton) findViewById(R.id.one);
        two = (RadioButton) findViewById(R.id.two);
        three = (RadioButton) findViewById(R.id.three);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJson();
            }
        });
    }

    private void getJson() {
        if (zero.isChecked()) {
            price = "4";
        }
        if (one.isChecked()) {
            price = "4";
        }
        if (two.isChecked()) {
            price = "3";
        }
        if (three.isChecked()) {
            price = "3";
        }
        if (fastFood.isChecked()) {
            foodType = "fast+food";
        }
        if (mexican.isChecked()) {
            foodType = "mexican+food";
        }
        if (pizza.isChecked()) {
            foodType = "pizza";
        }
        String input = constant + foodType + "&maxprice=" + price + "&key=" + key;
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("query", input);
        this.startActivity(intent);
    }
}
