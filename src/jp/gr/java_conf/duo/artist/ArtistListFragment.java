package jp.gr.java_conf.duo.artist;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.album.AlbumActivity;
import jp.gr.java_conf.duo.main.MainActivity;
import jp.gr.java_conf.duo.play.PlayActivity;
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

/* アーティストリストフラグメント */
public class ArtistListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.list_artist, container, false);
        final Context context = getActivity();

        // アーティストリストを設定する
        List<Artist> artistList = Artist.getItems(context);
        ListAdapter listAdapter = new ArtistArrayAdapter(context, artistList);
        ListView listView = (ListView) view.findViewById(R.id.list_artist);
        listView.setAdapter(listAdapter);

        // リストクリック時の動作設定
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // クリックした情報を取得する
                ListView listView = (ListView) parent;
                Artist artist = (Artist) listView.getItemAtPosition(position);

                // Albumアクティビティへ値の受け渡し・起動
                Intent intent = new Intent(context, AlbumActivity.class);
                intent.putExtra(MainActivity.CONST_ARTIST_ID, artist.getId()); // アーティストID
                startActivity(intent);
            }
        });

        // リストロングクリック時の動作設定
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // クリックした情報を取得する
                ListView listView = (ListView) parent;
                Artist artist = (Artist) listView.getItemAtPosition(position);

                // Playアクティビティへ値の受け渡し・起動
                Intent intent = new Intent(context, PlayActivity.class);
                intent.putExtra(MainActivity.CONST_ARTIST_ID, artist.getId()); // アーティストID
                startActivity(intent);

                return true;
            }
        });

        return view;
    }
}
