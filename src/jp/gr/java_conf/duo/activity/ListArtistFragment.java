package jp.gr.java_conf.duo.activity;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.adapter.ListArtistAdapter;
import jp.gr.java_conf.duo.domain.Artist;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/* アーティストリストフラグメント */
public class ListArtistFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.list_artist, container, false);
        final MainActivity activity = (MainActivity) super.getActivity();

        // リストの設定
        List<Artist> artistList = Artist.getItems(activity);
        ListArtistAdapter adapter = new ListArtistAdapter(activity, artistList);
        ListView artistListView = (ListView) view.findViewById(R.id.list_artist);
        artistListView.setAdapter(adapter);

        // リスト押下時の動作設定
        artistListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                Artist artist = (Artist) lv.getItemAtPosition(position);
                activity.artistId = artist.getId();
                activity.albumId = 0;

                // ページ内容を再描画
                activity.pagerAdapter.notifyDataSetChanged();
                // アルバムページを表示
                activity.viewPager.setCurrentItem(1);
            }
        });

        return view;
    }
}
