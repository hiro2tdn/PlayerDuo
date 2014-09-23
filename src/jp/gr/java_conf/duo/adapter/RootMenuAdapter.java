package jp.gr.java_conf.duo.adapter;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.fragment.HomeFragment;
import jp.gr.java_conf.duo.fragment.ListAlbumFragment;
import jp.gr.java_conf.duo.fragment.ListArtistFragment;
import jp.gr.java_conf.duo.fragment.ListTrackFragment;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/* ルートメニューアダプタ */
public class RootMenuAdapter extends FragmentPagerAdapter {

    private String titles[];
    private Fragment frags[];

    public RootMenuAdapter(FragmentManager fm, Context context) {
        super(fm);

        Resources resources = context.getResources();
        titles = resources.getStringArray(R.array.titles);

        frags = new Fragment[titles.length];
        frags[0] = new ListArtistFragment();
        frags[1] = new ListAlbumFragment();
        frags[2] = new ListTrackFragment();
        frags[3] = new HomeFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return frags[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

}
