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
    private static final String F_ALBUM = "fAlbum";

    private MainActivity activity = null;
    private long artistId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.list_album, container, false);
        activity = (MainActivity) super.getActivity();

        // アクティビティからアーティストID取得、または、OSによる停止時の値を復元
        if (savedInstanceState == null) {
            artistId = activity.getArtistId();
        } else  {
            artistId = savedInstanceState.getLong(BUNDLE_ARTIST_ID);
        }

        // アルバムリストの取得
        List<Album> albumList;
        if (artistId == 0) {
            albumList = Album.getItems(activity);
        } else {
            albumList = Album.getItemsByArtistId(getActivity(), artistId);
        }

        ListAlbumAdapter adapter = new ListAlbumAdapter(activity, albumList);
        ListView albumListView = (ListView) view.findViewById(R.id.list);
        albumListView.setAdapter(adapter); // リスト設定
        albumListView.setOnItemClickListener(mAlbumClickListener);   // リスト押下動作設定

        return view;
    }

    /* OSによる停止時の処理 */
    @Override
    public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);

      // 現在のインスタンス変数を保存
      outState.putLong(BUNDLE_ARTIST_ID, artistId);
    }

    /* アルバムクリック時の処理 */
    private OnItemClickListener mAlbumClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            activity.setAlbumId(((Album) lv.getItemAtPosition(position)).getId());

            // Fragmentを作成
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.root, new ListTrackFragment(), F_ALBUM);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(null);
            ft.commit();
        }
    };
}
