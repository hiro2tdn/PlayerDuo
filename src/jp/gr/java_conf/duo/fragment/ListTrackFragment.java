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

    private static final String BUNDLE_ALBUM_ID = "ALBUM_ID";

    private long albumId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.list_track, container, false);
        final MainActivity activity = (MainActivity) super.getActivity();

        // OSによる停止前の状態があるか
        if (savedInstanceState == null) {
            // アクティビティからアルバムID取得
            albumId = activity.albumId;
        } else {
            // OSによる停止時の状態を復元
            albumId = savedInstanceState.getLong(BUNDLE_ALBUM_ID);
        }

        // リストの取得
        List<Track> trackList = Track.getItemsByAlbumId(activity, albumId);
        ListTrackAdapter adapter = new ListTrackAdapter(activity, trackList);

        // リストの設定
        ListView trackListView = (ListView) view.findViewById(R.id.list_track);
        trackListView.setAdapter(adapter);

        // リスト押下時の動作設定
        trackListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, PlayActivity.class);
                // PLAYアクティビティへ値の受け渡し
                intent.putExtra(MainActivity.EXTRA_POSITION, position); // ポジション
                intent.putExtra(MainActivity.EXTRA_ALBUM_ID, albumId);  // アルバムID
                // PLAYアクティビティ起動
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // アルバムリストに戻るのでアルバムの選択を初期化
        MainActivity activity = (MainActivity) super.getActivity();
        activity.albumId= 0;
    }

    /* OSによる停止時の処理 */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 現在の状態を保存
        outState.putLong(BUNDLE_ALBUM_ID, albumId);
    }
}
