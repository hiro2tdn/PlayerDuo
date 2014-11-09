package jp.gr.java_conf.duo.album;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.main.MainActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * アルバムアクティビティ
 */
public class AlbumActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // 呼出元アクティビティから値の受取
        Intent intent = getIntent();
        long artistId = intent.getLongExtra(MainActivity.CONST_ARTIST_ID, 0);

        Bundle bundle = new Bundle();
        bundle.putLong(MainActivity.CONST_ARTIST_ID, artistId);

        Fragment fragment = new AlbumListFragment();
        fragment.setArguments(bundle);
        ft.add(R.id.container, fragment);
        ft.commit();
    }

}
