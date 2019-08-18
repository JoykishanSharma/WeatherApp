package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityEditText;
    TextView weatherForecast;
    RelativeLayout relativeLayout;


    public void checkWeather(View view){

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(cityEditText.getWindowToken(), 0);

        String ecodedCityName = null;

        try {

            ecodedCityName = URLEncoder.encode(cityEditText.getText().toString(), "UTF-8");

            DownloadTask task = new DownloadTask();
            task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + ecodedCityName + "&appid=0f5417a710a99da9a4b0bd9023bfafeb");

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not check weather",Toast.LENGTH_LONG);

        }




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = findViewById(R.id.cityEditText);
        weatherForecast = findViewById(R.id.weatherForecast);
        relativeLayout = findViewById(R.id.relativeLayout);

    }

    public  class DownloadTask extends AsyncTask<String, Void, String>{



        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1){

                    char current = (char)data;
                    result += current;
                    data = reader.read();

                }
                return result;

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(),"Could not check weather",Toast.LENGTH_LONG);

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                String message = "";

                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");

                JSONArray jsonArray = new JSONArray(weatherInfo);

                for (int i=0; i<jsonArray.length(); i++){

                    JSONObject jsonPart = jsonArray.getJSONObject(i);

                    String description = "";

                    description = jsonPart.getString("description");

                    if(description != ""){

                        message += description;

                    }

                }
                if(message != ""){

                    Resources res = getResources();

                    switch (message) {
                        case "broken clouds":
                        case "scattered clouds": {
                            Drawable drawable = res.getDrawable(R.drawable.brokenclouds);
                            relativeLayout.setBackground(drawable);
                            break;
                        }
                        case "clear sky": {
                            Drawable drawable = res.getDrawable(R.drawable.clearsky);
                            relativeLayout.setBackground(drawable);
                            break;
                        }
                        case "haze": {
                            Drawable drawable = res.getDrawable(R.drawable.haze);
                            relativeLayout.setBackground(drawable);
                            break;
                        }
                        case "light rain":
                        case "light rainmist":
                        case "moderate rain": {
                            Drawable drawable = res.getDrawable(R.drawable.rain);
                            relativeLayout.setBackground(drawable);
                            break;
                        }
                        case "thunderstorm": {
                            Drawable drawable = res.getDrawable(R.drawable.lighting);
                            relativeLayout.setBackground(drawable);
                            break;
                        }
                        case "heavy intensity rain":
                        case "light intensity drizzleshower rain":
                        case "thunderstorm with heavy rain": {
                            Drawable drawable = res.getDrawable(R.drawable.heavyrain);
                            relativeLayout.setBackground(drawable);
                            break;
                        }
                        case "mist": {
                            Drawable drawable = res.getDrawable(R.drawable.mist);
                            relativeLayout.setBackground(drawable);
                            break;
                        }
                        case "overcast clouds": {
                            Drawable drawable = res.getDrawable(R.drawable.overcastclouds);
                            relativeLayout.setBackground(drawable);
                            break;
                        }
                        case "light intensity shower rain": {
                            Drawable drawable = res.getDrawable(R.drawable.thunderstormrain);
                            relativeLayout.setBackground(drawable);
                            break;
                        }
                        case "shower rain":
                        case "drizzle": {
                            Drawable drawable = res.getDrawable(R.drawable.drizzle);
                            relativeLayout.setBackground(drawable);
                            break;
                        }
                        default: {
                            Drawable drawable = res.getDrawable(R.drawable.background);
                            relativeLayout.setBackground(drawable);
                            break;
                        }
                    }

                    weatherForecast.setText("Description : " + message);

                }else {

                    Toast.makeText(getApplicationContext(),"Could not check weather",Toast.LENGTH_LONG);

                }

            } catch (JSONException e) {

                Toast.makeText(getApplicationContext(),"Could not check weather",Toast.LENGTH_LONG);

            }

        }
    }


}
