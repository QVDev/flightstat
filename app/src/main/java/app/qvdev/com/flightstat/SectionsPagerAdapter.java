package app.qvdev.com.flightstat;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final int TOTAL_PAGES = 2;
    private static final int POSITION_CORRECTION = 1;

    private final Context mContext;

    SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return BaseFragment.newInstance(position + POSITION_CORRECTION);
    }

    @Override
    public int getCount() {
        return TOTAL_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.specific_flight);
            case 1:
                return mContext.getString(R.string.airport_flights);
            default:
                return null;
        }
    }
}
