package jp.gr.java_conf.duo.album;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.artist.Artist;
import jp.gr.java_conf.duo.main.MainActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

/**
 * アルバムアクティビティ
 */
public class AlbumActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // 呼出元アクティビティから値の受取
        Intent intent = getIntent();
        long artistId = intent.getLongExtra(MainActivity.CONST_ARTIST_ID, 0);

        Bundle bundle = new Bundle();
        bundle.putLong(MainActivity.CONST_ARTIST_ID, artistId);

        // アーティストの表示
        Artist artist = Artist.getItemByArtistId(this, artistId);
        ((TextView) findViewById(R.id.artist_name)).setText(artist.getArtist());
        ((TextView) findViewById(R.id.album_num)).setText(getResources().getQuantityString(R.plurals.album_num, artist.getAlbumNum(), artist.getAlbumNum()));

        // アルバムリストの表示
        Fragment fragment = new AlbumListFragment();
        fragment.setArguments(bundle);
        ft.add(R.id.container, fragment);
        ft.commit();
    }

}
