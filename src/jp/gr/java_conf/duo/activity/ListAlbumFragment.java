package jp.gr.java_conf.duo.activity;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.adapter.ListAlbumAdapter;
import jp.gr.java_conf.duo.domain.Album;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/* アルバムリストフラグメント */
public class ListAlbumFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.list_album, container, false);
        final MainActivity activity = (MainActivity) super.getActivity();

        // リストの設定
        List<Album> albumList = Album.getItemsByArtistId(activity, activity.artistId);
        ListAlbumAdapter adapter = new ListAlbumAdapter(activity, albumList);
        ListView albumListView = (ListView) view.findViewById(R.id.list_album);
        albumListView.setAdapter(adapter);

        // リスト押下時の動作設定
        albumListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                Album album = (Album) lv.getItemAtPosition(position);
                activity.albumId = album.getId();

                // ページ内容を再描画
                activity.pagerAdapter.notifyDataSetChanged();
                // トラックページを表示
                activity.viewPager.setCurrentItem(2);
            }
        });

        return view;
    }
}
