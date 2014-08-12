package jp.gr.java_conf.duo.fragment;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.activity.MainActivity;
import jp.gr.java_conf.duo.adapter.ListTrackAdapter;
import jp.gr.java_conf.duo.domain.Track;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/* 全トラックフラグメント */
public class AllTracksFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.all_tracks, container, false);
        MainActivity activity = (MainActivity) super.getActivity();

        // トラックリスト
        List<Track> tracks = Track.getItems(activity);
        ListTrackAdapter adapter = new ListTrackAdapter(activity, tracks);
        ListView trackList = (ListView) view.findViewById(R.id.list);
        trackList.setAdapter(adapter);
        trackList.setOnItemClickListener(activity.trackClickListener);

        return view;
    }
}
