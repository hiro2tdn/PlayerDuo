package jp.gr.java_conf.duo.album;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.main.MainActivity;
import jp.gr.java_conf.duo.play.PlayActivity;
import jp.gr.java_conf.duo.track.TrackActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

/* アルバムリストフラグメント */
public class AlbumListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.list_album, container, false);
        final Context context = getActivity();

        // アーティストIDを取得する
        long artistId = 0;
        if (getArguments() != null) {
            artistId = getArguments().getLong(MainActivity.CONST_ARTIST_ID, 0);
        }

        // アルバムリストを設定する
        List<Album> albumList = Album.getItemsByArtistId(context, artistId);
        ListAdapter listAdapter = new AlbumArrayAdapter(context, albumList);
        ListView listView = (ListView) view.findViewById(R.id.list_album);
        listView.setAdapter(listAdapter);

        // リストクリック時の動作設定
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // クリックした情報を取得する
                ListView listView = (ListView) parent;
                Album album = (Album) listView.getItemAtPosition(position);

                // Albumアクティビティへ値の受け渡し・起動
                Intent intent = new Intent(context, TrackActivity.class);
                intent.putExtra(MainActivity.CONST_ALBUM_ID, album.getId()); // アルバムID
                startActivity(intent);
            }
        });

        // リストロングクリック時の動作設定
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // クリックした情報を取得する
                ListView listView = (ListView) parent;
                Album album = (Album) listView.getItemAtPosition(position);

                // Playアクティビティへ値の受け渡し・起動
                Intent intent = new Intent(context, PlayActivity.class);
                intent.putExtra(MainActivity.CONST_ALBUM_ID, album.getId()); // アルバムID
                startActivity(intent);

                return true;
            }
        });

        return view;
    }
}
