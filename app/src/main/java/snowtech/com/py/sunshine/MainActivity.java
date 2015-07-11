package snowtech.com.py.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }

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
