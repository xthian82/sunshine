package snowtech.com.py.sunshine.app;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import snowtech.com.py.sunshine.app.sync.SunshineSyncAdapter;


public class MainActivity extends ActionBarActivity implements ForecastFragment.Callback {
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private String mLocation;
    private String TAG = MainActivity.class.getSimpleName();
    private boolean mTwoPane;
    private ForecastFragment forecastFragment;

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
        if (preferedLoc != null && preferedLoc.equals(mLocation) == false) {

            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            if ( null != ff ) {
                ff.onLocationChanged();
            }
            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if (df != null) df.onLocationChanged(preferedLoc);
            mLocation = preferedLoc;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocation = Utility.getPreferredLocation(this);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.weather_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        forecastFragment = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
        forecastFragment.setUseTodayLayout(!mTwoPane);
        SunshineSyncAdapter.initializeSyncAdapter(this);
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

    @Override
    public void onItemSelected(Uri dateUri) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, dateUri);
            DetailFragment df = new DetailFragment();
            df.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, df, DETAILFRAGMENT_TAG)
                    .commit();
        } else  {
            Intent detailIntent = new Intent(this, DetailActivity.class);

            detailIntent.setData(dateUri);
            startActivity(detailIntent);
        }
    }
}
