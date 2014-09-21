package jp.gr.java_conf.duo.fragment;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.activity.MainActivity;
import jp.gr.java_conf.duo.adapter.ListArtistAdapter;
import jp.gr.java_conf.duo.domain.Artist;
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

/* アーティストリストフラグメント */
public class ListArtistFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.list_artist, container, false);
        final MainActivity activity = (MainActivity) super.getActivity();

        // アーティストリストの取得
        List<Artist> artistList = Artist.getItems(activity);
        ListArtistAdapter adapter = new ListArtistAdapter(activity, artistList);

        // アーティストリストの設定
        ListView artistListView = (ListView) view.findViewById(R.id.list);
        artistListView.setAdapter(adapter);
        artistListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                activity.setArtistId(((Artist) lv.getItemAtPosition(position)).getId());

                // Fragmentを作成
                FragmentManager fm = activity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.root, new ListAlbumFragment(), MainActivity.FLAGMENT_TAGS[1]);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }
}
