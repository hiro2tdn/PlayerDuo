package jp.gr.java_conf.duo.activity;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.adapter.ArrayArtistAdapter;
import jp.gr.java_conf.duo.domain.Artist;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/* アーティスト配列フラグメント */
public class ArrayArtistFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.list_artist, container, false);
        final MainActivity activity = (MainActivity) super.getActivity();

        // アーティストリストを設定する
        List<Artist> artistList = Artist.getItems(activity);
        ArrayArtistAdapter adapter = new ArrayArtistAdapter(activity, artistList);
        ListView artistListView = (ListView) view.findViewById(R.id.list_artist);
        artistListView.setAdapter(adapter);

        // リストクリック時の動作設定
        artistListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // クリックした情報を取得する
                ListView lv = (ListView) parent;
                Artist artist = (Artist) lv.getItemAtPosition(position);

                // PLAYアクティビティへ値の受け渡し・起動
                Intent intent = new Intent(activity, PlayActivity.class);
                intent.putExtra(MainActivity.CONST_ARTIST_ID, artist.getId()); // アーティストID
                intent.putExtra(MainActivity.CONST_ALBUM_ID, 0); // アルバムID
                intent.putExtra(MainActivity.CONST_POSITION, 0); // ポジション
                startActivity(intent);
            }
        });

        return view;
    }
}
