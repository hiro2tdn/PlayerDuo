package jp.gr.java_conf.duo.fragment;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.activity.MainActivity;
import jp.gr.java_conf.duo.adapter.RootAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* ルートフラグメント */
public class RootFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.root, container, false);
        MainActivity activity = (MainActivity) super.getActivity();

        // ページャ
        RootAdapter mRootMenuAdapter = new RootAdapter(super.getChildFragmentManager(), activity);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mRootMenuAdapter);

        return view;
    }
}
