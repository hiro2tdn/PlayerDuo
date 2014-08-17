package jp.gr.java_conf.duo.fragment;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.activity.MainActivity;
import jp.gr.java_conf.duo.activity.PlayActivity;
import jp.gr.java_conf.duo.adapter.ListTrackAdapter;
import jp.gr.java_conf.duo.domain.Track;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/* トラックリストフラグメント */
public class ListTrackFragment extends Fragment {

    public static final String EXTRA_TRACK_ID = "TRACK_ID";

    private static final String BUNDLE_ALBUM_ID = "ALBUM_ID";

    private MainActivity activity = null;
    private long albumId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.list_track, container, false);
        activity = (MainActivity) super.getActivity();

        // アクティビティからアルバムID取得、または、OSによる停止時の値を復元
        if (savedInstanceState == null) {
            albumId = activity.getAlbumId();
        } else  {
            albumId = savedInstanceState.getLong(BUNDLE_ALBUM_ID);
        }

        // トラックリストの取得
        List<Track> trackList;
        if (albumId == 0) {
            trackList = Track.getItems(activity);
        } else {
            trackList = Track.getItemsByAlbumId(getActivity(), albumId);
        }

        ListTrackAdapter adapter = new ListTrackAdapter(activity, trackList);
        ListView trackListView = (ListView) view.findViewById(R.id.list);
        trackListView.setAdapter(adapter); // リスト設定
        trackListView.setOnItemClickListener(mTrackClickListener);   // リスト押下動作設定

        return view;
    }

    /* OSによる停止時の処理 */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 現在のインスタンス変数を保存
        outState.putLong(BUNDLE_ALBUM_ID, albumId);
    }

    /* トラッククリック時の処理 */
    private OnItemClickListener mTrackClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            long trackId = ((Track) lv.getItemAtPosition(position)).getId();

            Intent intent = new Intent(activity, PlayActivity.class);
            // PLAYアクティビティへ値の受け渡し
            intent.putExtra(EXTRA_TRACK_ID, trackId);   // トラックID
            // PLAYアクティビティ起動
            startActivity(intent);
        }
    };
}
