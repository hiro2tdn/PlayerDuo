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

/* トラックフラグメント */
public class TracksFragment extends Fragment {

    private long albumId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.tracks, container, false);
        MainActivity activity = (MainActivity) super.getActivity();

        // アクティビティからID取得、または、OSによる停止時の値を復元
        if (savedInstanceState == null) {
            albumId = activity.getAlbumId();
        } else  {
            albumId = savedInstanceState.getLong("ALBUM_ID");
        }

        // トラックリスト
        List<Track> trackList;
        if (albumId == 0) {
            trackList = Track.getItems(activity);
        } else {
            trackList = Track.getItemsByAlbumId(getActivity(), albumId);
        }

        ListTrackAdapter adapter = new ListTrackAdapter(activity, trackList);
        ListView trackListView = (ListView) view.findViewById(R.id.list);
        trackListView.setAdapter(adapter);
        trackListView.setOnItemClickListener(activity.trackClickListener);

        return view;
    }

    /* OSによる停止時の処理 */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 現在のインスタンス変数を保存
        outState.putLong("ALBUM_ID", albumId);
    }
}
