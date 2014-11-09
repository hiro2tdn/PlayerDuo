package jp.gr.java_conf.duo.track;

import java.util.List;

import jp.gr.java_conf.duo.R;
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
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * トラックリストフラグメント
 */
public class TrackListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.list_track, container, false);
        final Context context = getActivity();

        // アルバムIDを取得する
        long albumId = 0;
        if (getArguments() != null) {
            albumId = getArguments().getLong(MainActivity.CONST_ALBUM_ID, 0);
        }

        // トラックリストを設定する
        List<Track> trackList = Track.getItemsByAlbumId(context, albumId);
        ListAdapter listAdapter = new TrackArrayAdapter(context, trackList);
        ListView listView = (ListView) view.findViewById(R.id.list_track);
        listView.setAdapter(listAdapter);

        // リストクリック時の動作設定
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // クリックした情報を取得する
                ListView listView = (ListView) parent;
                Track track = (Track) listView.getItemAtPosition(position);

                // Playアクティビティへ値の受け渡し・起動
                Intent intent = new Intent(context, PlayActivity.class);
                intent.putExtra(MainActivity.CONST_ARTIST_ID, track.getArtistId()); // アーティストID
                intent.putExtra(MainActivity.CONST_ALBUM_ID, track.getAlbumId()); // アルバムID

                // トラックリストを再取得して再取得後のポジションを設定する
                List<Track> trackList = Track.getItemsByAlbumId(context, track.getAlbumId());
                int positionNew = 0;
                for (Track trackNew : trackList) {
                    if (track.getId() == trackNew.getId()) {
                        intent.putExtra(MainActivity.CONST_POSITION, positionNew); // ポジション
                        break;
                    }
                    positionNew++;
                }

                startActivity(intent);
            }
        });

        return view;
    }
}
