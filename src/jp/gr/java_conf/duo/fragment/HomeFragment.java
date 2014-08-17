package jp.gr.java_conf.duo.fragment;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.activity.MainActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

/* ホーム画面フラグメント */
public class HomeFragment extends Fragment {

    private MainActivity activity = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.home, container, false);
        activity = (MainActivity) super.getActivity();

        // ボタンの動作設定
        view.findViewById(R.id.btn_scan_sdcard).setOnClickListener(mScanClickListener);

        return view;
    }

    /* Scan SD Cardボタンクリック時の処理 */
    private OnClickListener mScanClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String _url = "file://" + Environment.getExternalStorageDirectory();
            Uri _uri = Uri.parse(_url);
            activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, _uri));
        }
    };
}
