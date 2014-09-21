package jp.gr.java_conf.duo.fragment;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.activity.MainActivity;
import jp.gr.java_conf.duo.adapter.ListAlbumAdapter;
import jp.gr.java_conf.duo.domain.Album;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/* アルバムリストフラグメント */
public class ListAlbumFragment extends Fragment {

    private static final String BUNDLE_ARTIST_ID = "ARTIST_ID";

    private long artistId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.list_album, container, false);
        final MainActivity activity = (MainActivity) super.getActivity();

        // OSによる停止前の状態があるか
        if (savedInstanceState != null) {
            // OSによる停止時の状態を復元
            artistId = savedInstanceState.getLong(BUNDLE_ARTIST_ID);
        } else {
            // アクティビティからアルバムID取得
            artistId = activity.getArtistId();
        }

        // アルバムリストの取得
        List<Album> albumList = Album.getItemsByArtistId(activity, artistId);
        ListAlbumAdapter adapter = new ListAlbumAdapter(activity, albumList);

        // アルバムリストの動作設定
        ListView albumListView = (ListView) view.findViewById(R.id.list);
        albumListView.setAdapter(adapter);
        albumListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                activity.setAlbumId(((Album) lv.getItemAtPosition(position)).getId());

                // Fragmentを作成
                FragmentManager fm = activity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.root, new ListTrackFragment(), MainActivity.FLAGMENT_TAGS[2]);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // アーティストリストに戻るのでアーティストの選択を初期化
        MainActivity activity = (MainActivity) super.getActivity();
        activity.setArtistId(0);
    }

    /* OSによる停止時の処理 */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 現在の状態を保存
        outState.putLong(BUNDLE_ARTIST_ID, artistId);
    }
}
