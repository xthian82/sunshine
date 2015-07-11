package snowtech.com.py.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import snowtech.com.py.sunshine.app.FetchWeatherTask;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    private ArrayAdapter<String> mForecastAdapter;
    private ListView listView;
    //private Location location;

    public ForecastFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        } else if (id == R.id.action_map_location) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            String location = sharedPreferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
            Uri uri = Uri.parse("geo:0,0?").buildUpon()
                    .appendQueryParameter("q", location)
                    .build();
            intent.setData(uri);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateWeather() {
        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask(getActivity().getBaseContext(), mForecastAdapter);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String location = sharedPreferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        fetchWeatherTask.execute(location);
            /*
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());
            fetchWeatherTask.execute(latitude, longitude); //"2420,py");
            */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mForecastAdapter = new ArrayAdapter<String>(
                getActivity(), //Context
                R.layout.list_item_forecast, //ID of list item layout
                R.id.list_item_forecast_textview, //Id of text view
                new ArrayList<String>()); //data list

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data = mForecastAdapter.getItem(i);
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtra(Intent.EXTRA_TEXT, data);
                startActivity(detailIntent);
            }
        });

        /*LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location locationAddress) {
                location = locationAddress;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener); */

        return rootView;
    }

    /*

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
        private final String TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... param) {
            if (param.length < 1) return null;

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            final String QUERY = "q";
            final String LAT = "lat";
            final String LON = "lon";
            final String UNITS = "units";
            final String COUNT = "cnt";
            final int numDays = 7;

            try {
                Uri uri = Uri.parse("http://api.openweathermap.org/data/2.5/forecast/daily?").buildUpon()
                        .appendQueryParameter(QUERY, param[0])
                        //.appendQueryParameter(LAT, param[0])
                        //.appendQueryParameter(LON, param[1])
                        .appendQueryParameter(UNITS, "metric")
                        .appendQueryParameter(COUNT, String.valueOf(numDays))
                        .build();

                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(uri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getWeatherDataFromJson(forecastJsonStr, 7);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(String[] result) {
            if (result != null) {
                mForecastAdapter.clear();

                for (String r : result) {
                    mForecastAdapter.add(r);
                }
            }
        }

        //The date/time conversion code is going to be moved outside the asynctask later,
        //so for convenience we're breaking it out into its own method now.
        //
        private String getReadableDateString(long time){
            // Because the API returns a unix timestamp (measured in seconds),
            // it must be converted to milliseconds in order to be converted to valid date.
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(time);
        }

        //Prepare the weather high/lows for presentation.
        private String formatHighLows(double high, double low) {
            // For presentation, assume the user doesn't care about tenths of a degree.

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String degree = " ˚C";

            String unitType = sharedPreferences.getString(getString(R.string.pref_time_units_key), getString(R.string.pref_time_units_metrics));

            if (unitType.equals(getString(R.string.pref_time_units_imperial))) {
                high = (high * 1.8) + 32;
                low = (low * 1.8) + 32;
                degree = " ˚F";
            }

            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);

            String highLowStr = roundedHigh + "/" + roundedLow + degree;
            return highLowStr;
        }


//          Take the String representing the complete forecast in JSON Format and
//          pull out the data we need to construct the Strings needed for the wireframes.
//
//          Fortunately parsing is easy:  constructor takes the JSON string and converts it
//          into an Object hierarchy for us.

        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_DESCRIPTION = "main";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            String[] resultStrs = new String[numDays];
            for(int i = 0; i < weatherArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String day;
                String description;
                String highAndLow;

                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                long dateTime;
                // Cheating to convert this to UTC time, which is what we want anyhow
                dateTime = dayTime.setJulianDay(julianStartDay+i);
                day = getReadableDateString(dateTime);

                // description is in a child array called "weather", which is 1 element long.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);

                highAndLow = formatHighLows(high, low);
                resultStrs[i] = day + " - " + description + " - " + highAndLow;
            }

            for (String s : resultStrs) {
                Log.v(TAG, "Forecast entry: " + s);
            }
            return resultStrs;

        }
    }*/
}
