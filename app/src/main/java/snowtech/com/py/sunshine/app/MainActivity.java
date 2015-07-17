package snowtech.com.py.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
    private static final String FORECASTFRAGMENT_TAG = "ForecastFragment";
    private String mLocation;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, " -- onStart -- ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, " -- onPause -- ");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, " -- onStop -- ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, " -- onDestroy -- ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, " -- onResume -- ");
        String preferedLoc = Utility.getPreferredLocation(this);
        if (preferedLoc.equals(mLocation) == false) {
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);
            ff.onLocationChanged(new Location(preferedLoc));
            mLocation = preferedLoc;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment(), FORECASTFRAGMENT_TAG)
                    .commit();
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        mLocation = sharedPreferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

        Log.d(TAG, " -- onCreate -- ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
