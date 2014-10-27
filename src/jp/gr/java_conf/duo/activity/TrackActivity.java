package jp.gr.java_conf.duo.activity;

import jp.gr.java_conf.duo.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/* アルバムアクティビティ */
public class TrackActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // 呼出元アクティビティから値の受取
        Intent intent = getIntent();
        long albumId = intent.getLongExtra(MainActivity.CONST_ALBUM_ID, 0);

        Bundle bundle = new Bundle();
        bundle.putLong(MainActivity.CONST_ALBUM_ID, albumId);

        Fragment fragment = new ArrayTrackFragment();
        fragment.setArguments(bundle);
        ft.add(R.id.container, fragment);
        ft.commit();
    }

}
