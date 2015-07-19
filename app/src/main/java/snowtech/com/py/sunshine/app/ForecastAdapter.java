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

/**
 * Created by cristhian on 11/7/15.
 */
public class ForecastAdapter extends CursorAdapter {
    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;
    public ForecastAdapter(Context context, Cursor c, int flags) {

        super(context, c, flags);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        if (viewType == VIEW_TYPE_TODAY)
            layoutId = R.layout.list_item_forecast_today;
        else
            layoutId = R.layout.list_item_forecast;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //TextView tv = (TextView)view;
        //tv.setText(convertCursorRowToUXFormat(cursor));
        long date = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);

        //list_item_cast_textview
        String cast = cursor.getString(ForecastFragment.COL_WEATHER_DESC);

        int conditionId = cursor.getInt(ForecastFragment.COL_CON_WEATH_ID);
        double max = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        double min = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        boolean isMetric = Utility.isMetric(context);

        ViewHolder viewHolder = (ViewHolder)view.getTag();
        int viewType = getItemViewType(cursor.getPosition());


        if (viewType == VIEW_TYPE_TODAY)
            viewHolder.imgV.setImageResource(Utility.getArtResourceForWeatherCondition(conditionId));
        else
            viewHolder.imgV.setImageResource(Utility.getIconResourceForWeatherCondition(conditionId));

        viewHolder.tvDate.setText(Utility.getFriendlyDayString(context, date));
        viewHolder.tvDesc.setText(cast);
        viewHolder.tvMaxT.setText(Utility.formatTemperature(context, max, isMetric));
        viewHolder.tvMinT.setText(Utility.formatTemperature(context, min, isMetric));
    }

    //Prepare the weather high/lows for presentation.
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(mContext, high, isMetric) + "/" + Utility.formatTemperature(mContext, low, isMetric);
        return highLowStr;
    }

    //This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
    //string.
    private String convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor

        String highAndLow = formatHighLows(
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    static class ViewHolder {
        public final ImageView imgV;
        public final TextView tvDate;
        public final TextView tvDesc;
        public final TextView tvMaxT;
        public final TextView tvMinT;

        public ViewHolder(View view) {
            imgV  = (ImageView)view.findViewById(R.id.list_item_icon);
            tvDate = (TextView)view.findViewById(R.id.list_item_date_textview);
            tvDesc = (TextView)view.findViewById(R.id.list_item_cast_textview);
            tvMaxT = (TextView)view.findViewById(R.id.list_item_max_textview);
            tvMinT = (TextView)view.findViewById(R.id.list_item_min_textview);
        }
    }
}
