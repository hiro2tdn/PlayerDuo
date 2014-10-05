package jp.gr.java_conf.duo.adapter;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.domain.Album;
import jp.gr.java_conf.duo.image.ImageCache;
import jp.gr.java_conf.duo.image.ImageGetTask;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/* アルバム配列アダプタ */
public class ArrayAlbumAdapter extends ArrayAdapter<Album> {

    public ArrayAlbumAdapter(Context context, List<Album> albumList) {
        super(context, 0, albumList);
    }

    private static class ViewHolder {
        TextView albumNameTextView;
        TextView artistNameTextView;
        ImageView albumArtImageView;

        public ViewHolder(View view) {
            albumNameTextView = (TextView) view.findViewById(R.id.album_name);
            artistNameTextView = (TextView) view.findViewById(R.id.artist_name);
            albumArtImageView = (ImageView) view.findViewById(R.id.album_art);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_album, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Album album = getItem(position);
        holder.albumNameTextView.setText(album.getAlbum());
        holder.artistNameTextView.setText(album.getArtist());
        holder.albumArtImageView.setImageResource(R.drawable.dummy_album_art_blank);

        String path = album.getAlbumArt();
        if (path == null) {
            path = String.valueOf(R.drawable.dummy_album_art);
            // キャッシュからイメージを取得する
            Bitmap bitmap = ImageCache.getImage(path);
            // キャッシュにイメージが無い場合はイメージを作成してキャッシュに登録する
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(super.getContext().getResources(), R.drawable.dummy_album_art);
                ImageCache.setImage(path, bitmap);
            }
        }

        holder.albumArtImageView.setTag(path);
        ImageGetTask task = new ImageGetTask(holder.albumArtImageView);
        task.execute(path);

        return convertView;
    }
}
