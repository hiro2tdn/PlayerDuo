package jp.gr.java_conf.duo.fragment;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.activity.MainActivity;
import jp.gr.java_conf.duo.adapter.RootMenuAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.root_menu, container, false);
        final MainActivity activity = (MainActivity) super.getActivity();

        // ページャ
        RootMenuAdapter mRootMenuAdapter = new RootMenuAdapter(super.getChildFragmentManager(), activity);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mRootMenuAdapter);

        // ページャタブ
        PagerTabStrip strip = (PagerTabStrip) view.findViewById(R.id.pager_title_strip);
        strip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        strip.setTextSpacing(50);
        strip.setNonPrimaryAlpha(0.3f);

        return view;
    }
}
