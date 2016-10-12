package oscarf.androidgps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Coordenadas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordenadas);

        Button buttonCurrentPos = (Button)findViewById(R.id.buttonPosActual);
        Button buttonShowMap = (Button)findViewById(R.id.buttonShow);

        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putFloat("SAVED_LONG",(float)location.getLongitude());
                editor.putFloat("SAVED_LAT",(float)location.getLatitude());
                editor.apply();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        buttonCurrentPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText latText = (EditText)findViewById(R.id.editTextLat);
                EditText longText = (EditText)findViewById(R.id.editTextLong);

                float savedLat = sharedPref.getFloat("SAVED_LAT", 40);
                float savedLon = sharedPref.getFloat("SAVED_LONG", -3);
                String lat = savedLat+"";
                String lon = savedLon+"";
                latText.setText(lat);
                longText.setText(lon);
            }
        });

        buttonShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open new activity
                Intent intent = new Intent(Coordenadas.this, MapActivity.class);

                EditText latText = (EditText)findViewById(R.id.editTextLat);
                EditText longText = (EditText)findViewById(R.id.editTextLong);

                double newLat = Double.parseDouble(latText.getText().toString());
                double newLon = Double.parseDouble(longText.getText().toString());

                if(latText.getText().toString().equals("") || newLat > 90 || newLat < -90){
                    intent.putExtra("lat",40.0);
                }
                else{
                    intent.putExtra("lat",newLat);
                }
                if(latText.getText().toString().equals("") || newLon > 180 || newLon < -180){
                    intent.putExtra("lon",40.0);
                }
                else{
                    intent.putExtra("lon",newLon);
                }

                startActivity(intent);
            }
        });

    }
}
