package snowtech.com.py.sunshine.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;


public class DetailActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment( ))
                    .commit();
        }
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

    /*
    public static class PlaceholderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

        private ShareActionProvider mShareActionProvider;

        public PlaceholderFragment() {
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

            TextView tv = (TextView)getView().findViewById(R.id.text_view_detail);

            String date = Utility.formatDate(data.getLong(COL_WEATHER_DATE));
            String weatherDes = data.getString(COL_WEATHER_DESC);
            boolean isMetric = Utility.isMetric(getActivity());
            String maxTemp = Utility.formatTemperature(getActivity().getBaseContext(), data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
            String minTemp = Utility.formatTemperature(getActivity().getBaseContext(), data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

            mForecastText = String.format("%s - %s - %s/%s", date, weatherDes, maxTemp, minTemp);

            tv.setText(mForecastText);

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(getDefaultIntent());
            }

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }*/
}
