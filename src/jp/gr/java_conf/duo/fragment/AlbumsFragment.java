package jp.gr.java_conf.duo.fragment;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.activity.MainActivity;
import jp.gr.java_conf.duo.adapter.ListAlbumAdapter;
import jp.gr.java_conf.duo.domain.Album;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/* アルバムフラグメント */
public class AlbumsFragment extends Fragment {

    private long artistId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.albums, container, false);
        MainActivity activity = (MainActivity) super.getActivity();

        // アクティビティからID取得、または、OSによる停止時の値を復元
        if (savedInstanceState == null) {
            artistId = activity.getArtistId();
        } else  {
            artistId = savedInstanceState.getLong("ARTIST_ID");
        }

        // アルバムリスト
        List<Album> albumList;
        if (artistId == 0) {
            albumList = Album.getItems(activity);
        } else {
            albumList = Album.getItemsByArtistId(getActivity(), artistId);
        }

        ListAlbumAdapter adapter = new ListAlbumAdapter(activity, albumList);
        ListView albumListView = (ListView) view.findViewById(R.id.list);
        albumListView.setAdapter(adapter);
        albumListView.setOnItemClickListener(activity.albumClickListener);

        return view;
    }

    /* OSによる停止時の処理 */
    @Override
    public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);

      // 現在のインスタンス変数を保存
      outState.putLong("ARTIST_ID", artistId);
    }
}
