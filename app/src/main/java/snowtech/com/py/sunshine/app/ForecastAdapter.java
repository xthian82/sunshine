package snowtech.com.py.sunshine.app;

//import android.widget.CursorAdapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import snowtech.com.py.sunshine.app.data.WeatherContract;

/**
 * Created by cristhian on 11/7/15.
 */
public class ForecastAdapter extends CursorAdapter {
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_forecast, parent, false);

        //list_item_date_textview (fecha)
        long date = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);

        //list_item_cast_textview
        String cast = cursor.getString(ForecastFragment.COL_WEATHER_DESC);

        double max = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        double min = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        boolean isMetric = Utility.isMetric(context);

        //View root = parent.getRootView();

        ImageView imgV  = (ImageView)view.findViewById(R.id.list_item_icon);
        TextView tvDate =  (TextView)view.findViewById(R.id.list_item_date_textview);
        TextView tvDesc =  (TextView)view.findViewById(R.id.list_item_cast_textview);
        TextView tvMaxT =  (TextView)view.findViewById(R.id.list_item_max_textview);
        TextView tvMinT =  (TextView)view.findViewById(R.id.list_item_min_textview);

        imgV.setImageResource(R.drawable.ic_launcher);
        tvDate.setText(Utility.getFriendlyDayString(context, date));
        tvDesc.setText(cast);
        tvMaxT.setText(Utility.formatTemperature(max, isMetric));
        tvMinT.setText(Utility.formatTemperature(min, isMetric));

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //TextView tv = (TextView)view;
        //tv.setText(convertCursorRowToUXFormat(cursor));
    }

    //Prepare the weather high/lows for presentation.
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
        return highLowStr;
    }

    //This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
    //string.
    private String convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor
        int idx_max_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
        int idx_min_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP);
        int idx_date = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
        int idx_short_desc = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);

        String highAndLow = formatHighLows(
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }
}
