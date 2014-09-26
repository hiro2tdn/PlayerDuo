package jp.gr.java_conf.duo.adapter;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.activity.ListAlbumFragment;
import jp.gr.java_conf.duo.activity.ListArtistFragment;
import jp.gr.java_conf.duo.activity.ListTrackFragment;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/* メインページャアダプタ */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private String titles[];

    public MainPagerAdapter(FragmentManager fm, Resources resources) {
        super(fm);
        titles = resources.getStringArray(R.array.titles);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
        case 0:
            fragment = new ListArtistFragment();
            break;
        case 1:
            fragment = new ListAlbumFragment();
            break;
        case 2:
            fragment = new ListTrackFragment();
            break;
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
