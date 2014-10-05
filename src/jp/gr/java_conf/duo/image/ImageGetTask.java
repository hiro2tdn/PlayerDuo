package jp.gr.java_conf.duo.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageGetTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;
    private String tag;

    public ImageGetTask(ImageView imageView) {
        this.imageView = imageView;
        this.tag = imageView.getTag().toString();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String path = params[0];
        // キャッシュからイメージを取得する
        Bitmap bitmap = ImageCache.getImage(path);
        // キャッシュにイメージが無い場合はイメージを作成してキャッシュに登録する
        if (bitmap == null) {
            bitmap = decodeBitmap(path);
            ImageCache.setImage(path, bitmap);
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (tag.equals(imageView.getTag())) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }

    /* 48px * 48px の画像を作成する */
    public static Bitmap decodeBitmap(String path) {
        // 作成する画像のサイズ
        int w = 144;
        int h = 144;

        // Out Of Memory対策で画像をメモリに展開しないで情報だけ読み取る
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // メモリに展開する際の縮小比率を設定する
        int scaleW = (int) Math.floor(options.outWidth / w);
        int scaleH = (int) Math.floor(options.outHeight / h);
        options.inSampleSize = Math.min(scaleW, scaleH);

        // 縮小画像をメモリに展開する
        options.inJustDecodeBounds = false;
        Bitmap bm1 = BitmapFactory.decodeFile(path, options);

        if (bm1 != null) {
            // 縮小画像のサイズを取得する
            int width = bm1.getWidth();
            int height = bm1.getHeight();

            // 48px * 48pxにするための縮小比率を設定する
            float sx = w / Float.valueOf(width);
            float sy = h / Float.valueOf(height);
            Matrix matrix = new Matrix();
            matrix.postScale(sx, sy);

            // 縮小画像を作成する
            Bitmap bm2 = Bitmap.createBitmap(bm1, 0, 0, width, height, matrix, true);

            return bm2;
        }

        return null;
    }
}
