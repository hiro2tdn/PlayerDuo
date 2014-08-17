package jp.gr.java_conf.duo.fragment;

import jp.gr.java_conf.duo.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* ルートメニューフラグメント */
public class RootMenuFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.root_menu, container, false);

        // ページャ
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(super.getChildFragmentManager());
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // ページャタブ
        PagerTabStrip strip = (PagerTabStrip) view.findViewById(R.id.pager_title_strip);
        strip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        strip.setTextSpacing(50);
        strip.setNonPrimaryAlpha(0.3f);

        return view;
    }

    /* フラグメントページャアダプタ */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new ListArtistFragment();
                break;
            case 2:
                fragment = new ListAlbumFragment();
                break;
            case 3:
                fragment = new ListTrackFragment();
                break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
            case 0:
                return getString(R.string.home);
            case 1:
                return getString(R.string.artist);
            case 2:
                return getString(R.string.album);
            case 3:
                return getString(R.string.track);
            }
            return null;
        }
    }
}
