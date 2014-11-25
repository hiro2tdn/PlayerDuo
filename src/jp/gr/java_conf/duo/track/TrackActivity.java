package jp.gr.java_conf.duo.track;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.album.Album;
import jp.gr.java_conf.duo.image.ImageCache;
import jp.gr.java_conf.duo.image.ImageGetTask;
import jp.gr.java_conf.duo.main.MainActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * トラックアクティビティ
 */
public class TrackActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // 呼出元アクティビティから値の受取
        Intent intent = getIntent();
        long albumId = intent.getLongExtra(MainActivity.CONST_ALBUM_ID, 0);

        Bundle bundle = new Bundle();
        bundle.putLong(MainActivity.CONST_ALBUM_ID, albumId);

        // アルバムの表示
        Album album = Album.getItemByAlbumId(this, albumId);
        ((TextView) findViewById(R.id.album_name)).setText(album.getAlbum());
        ((TextView) findViewById(R.id.artist_name)).setText(album.getArtist());
        ImageView albumArtImageView = (ImageView) findViewById(R.id.album_art);
        albumArtImageView.setImageResource(R.drawable.dummy_album_art_blank);

        String path = album.getAlbumArt();
        if (path == null) {
            path = String.valueOf(R.drawable.dummy_album_art);
            // キャッシュからイメージを取得する
            Bitmap bitmap = ImageCache.getImage(path);
            // キャッシュにイメージが無い場合はイメージを作成してキャッシュに登録する
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dummy_album_art);
                ImageCache.setImage(path, bitmap);
            }
        }

        albumArtImageView.setTag(path);
        ImageGetTask task = new ImageGetTask(albumArtImageView);
        task.execute(path);

        // トラックリストの表示
        Fragment fragment = new TrackListFragment();
        fragment.setArguments(bundle);
        ft.add(R.id.container, fragment);
        ft.commit();
    }

}
