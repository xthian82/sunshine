package snowtech.com.py.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import snowtech.com.py.sunshine.app.data.WeatherContract;

/**
 * Created by cristhian on 18/7/15.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static String mForecastText;
    private TextView tvDate;
    private TextView tvMaxT;
    private TextView tvMinT;
    private TextView tvDesc;
    private TextView tvHumi;
    private TextView tvWind;
    private TextView tvPres;
    private ImageView imgV;
    private TextView tvDatF;

    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_DEGREES
    };

    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_WEATHER_HUMIDITY = 5;
    static final int COL_WEATHER_WIND_SPEED = 6;
    static final int COL_WEATHER_PRESSURE = 7;
    static final int COL_WEATHER_WEATH_ID = 8;
    static final int COL_WEATHER_DEGREE = 9;
    private ShareActionProvider mShareActionProvider;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_TEXT, mForecastText + "#sunshineApp");
        intent.setType("text/plain");

        return intent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

         getLoaderManager().initLoader(1, null, this);
         tvDate = (TextView)rootView.findViewById(R.id.date_text_detail);
         tvMaxT = (TextView)rootView.findViewById(R.id.max_text_detail);
         tvMinT = (TextView)rootView.findViewById(R.id.min_text_detail);
         tvDesc = (TextView)rootView.findViewById(R.id.desc_text_detail);
         tvHumi = (TextView)rootView.findViewById(R.id.humidity_text_detail);
         tvWind = (TextView)rootView.findViewById(R.id.wind_text_detail);
         tvPres = (TextView)rootView.findViewById(R.id.pressure_text_detail);
         tvDatF = (TextView)rootView.findViewById(R.id.datefriend_text_detail);
         imgV = (ImageView)rootView.findViewById(R.id.img_detail);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detail_fragment, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if (mForecastText != null)
            mShareActionProvider.setShareIntent(getDefaultIntent());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();

        if (intent == null) return null;

        return new CursorLoader(getActivity(), intent.getData(),
                FORECAST_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (!data.moveToFirst()) return;

//        tvDate = (TextView)getView().findViewById(R.id.date_text_detail);
//        tvMaxT = (TextView)getView().findViewById(R.id.max_text_detail);
//        tvMinT = (TextView)getView().findViewById(R.id.min_text_detail);
//        tvDesc = (TextView)getView().findViewById(R.id.desc_text_detail);
//        tvHumi = (TextView)getView().findViewById(R.id.humidity_text_detail);
//        tvWind = (TextView)getView().findViewById(R.id.wind_text_detail);
//        tvPres = (TextView)getView().findViewById(R.id.pressure_text_detail);
//        tvDatF = (TextView)getView().findViewById(R.id.datefriend_text_detail);
//        imgV = (ImageView)getView().findViewById(R.id.img_detail);

        long date = data.getLong(COL_WEATHER_DATE);
        String friendlyDateText = Utility.getDayName(getActivity(), date);
        String dateText = Utility.getFormattedMonthDay(getActivity(), date);
        String weatherDes = data.getString(COL_WEATHER_DESC);
        boolean isMetric = Utility.isMetric(getActivity());
        String maxTemp = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
        String minTemp = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);
        int id = data.getInt(COL_WEATHER_WEATH_ID);
        float humidity = data.getFloat(COL_WEATHER_HUMIDITY);
        float windSpeed = data.getFloat(COL_WEATHER_WIND_SPEED);
        float windDir = data.getFloat(COL_WEATHER_DEGREE);
        float pressure = data.getFloat(COL_WEATHER_PRESSURE);

        mForecastText = String.format("%s - %s - %s/%s", date, weatherDes, maxTemp, minTemp);

        tvDate.setText(dateText);
        tvDatF.setText(friendlyDateText);
        tvMaxT.setText(maxTemp);
        tvMinT.setText(minTemp);
        tvDesc.setText(weatherDes);
        tvHumi.setText(Utility.formatContext(getActivity(), humidity, Utility.HUMIDITY_RES));
        tvPres.setText(Utility.formatContext(getActivity(), pressure, Utility.PRESSURE_RES));
        tvWind.setText(Utility.formatWind(getActivity(), windSpeed, windDir));

        imgV.setImageResource(R.drawable.ic_launcher);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(getDefaultIntent());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
