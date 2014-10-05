package jp.gr.java_conf.duo.activity;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.adapter.ArrayAlbumAdapter;
import jp.gr.java_conf.duo.domain.Album;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

/* アルバム配列フラグメント */
public class ArrayAlbumFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.list_album, container, false);
        final MainActivity activity = (MainActivity) super.getActivity();

        // リストの設定
        List<Album> albumList = Album.getItemsByArtistId(activity, activity.artistId);
        ArrayAlbumAdapter adapter = new ArrayAlbumAdapter(activity, albumList);
        ListView albumListView = (ListView) view.findViewById(R.id.list_album);
        albumListView.setAdapter(adapter);

        // リストクリック時の動作設定
        albumListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // クリックした情報をActivityに保存
                ListView lv = (ListView) parent;
                Album album = (Album) lv.getItemAtPosition(position);
                activity.albumId = album.getId();

                // ページ内容を再描画
                activity.pagerAdapter.notifyDataSetChanged();

                // PLAYアクティビティへ値の受け渡し・起動
                Intent intent = new Intent(activity, PlayActivity.class);
                intent.putExtra(MainActivity.CONST_ARTIST_ID, activity.artistId); // アーティストID
                intent.putExtra(MainActivity.CONST_ALBUM_ID, activity.albumId); // アルバムID
                intent.putExtra(MainActivity.CONST_POSITION, 0); // ポジション
                startActivity(intent);
            }
        });

        // リストロングクリック時の動作設定
        albumListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // クリックした情報をActivityに保存
                ListView lv = (ListView) parent;
                Album album = (Album) lv.getItemAtPosition(position);
                activity.albumId = album.getId();

                // ページ内容を再描画
                activity.pagerAdapter.notifyDataSetChanged();

                // トラックページを表示
                activity.viewPager.setCurrentItem(2);

                return true;
            }
        });

        return view;
    }
}
