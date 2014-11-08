package jp.gr.java_conf.duo.track;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Track {

    private long id; // コンテントプロバイダに登録されたID
    private String path; // 実データのパス
    private String title; // タイトル
    private String album; // アルバム名
    private String artist; // アーティスト名
    private long albumId; // アルバムID
    private long artistId; // アーティストID
    private long duration; // 再生時間(ミリ秒)
    private int trackNo; // トラック番号
    private Uri uri; // URI
    private String lyric; // 歌詞

    private static String[] COLUMNS = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TRACK };

    public Track(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
        path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
        artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
        artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
        duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        trackNo = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK));
        uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
    }

    /* トラック取得 */
    private static List<Track> getMyItems(Context context, String selection, long id) {
        String mSelection = null;
        String[] selectionArgs = null;

        if (id != 0) {
            mSelection = selection;
            selectionArgs = new String[] { String.valueOf(id) };
        }

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                COLUMNS,
                mSelection,
                selectionArgs,
                "TITLE ASC");

        List<Track> tracks = new ArrayList<Track>();
        while (cursor.moveToNext()) {
            tracks.add(new Track(cursor));
        }

        cursor.close();
        return tracks;
    }

    /* 全トラック取得 */
    public static List<Track> getItems(Context context) {
        return getMyItems(context, null, 0);
    }

    /* 指定されたアーティストのトラック取得 */
    public static List<Track> getItemsByArtistId(Context context, long artistId) {
        String selection = MediaStore.Audio.Media.ARTIST_ID + "= ?";
        return getMyItems(context, selection, artistId);
    }

    /* 指定されたアルバムのトラック取得 */
    public static List<Track> getItemsByAlbumId(Context context, long albumId) {
        String selection = MediaStore.Audio.Media.ALBUM_ID + "= ?";
        return getMyItems(context, selection, albumId);
    }

    /* 指定されたトラック取得 */
    public static Track getItemByTrackId(Context context, long trackId) {
        String selection = MediaStore.Audio.Media._ID + "= ?";
        return getMyItems(context, selection, trackId).get(0);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(int trackNo) {
        this.trackNo = trackNo;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getLyric() {
        if (lyric == null) {
            lyric = "";
            try {
                // MP4限定で歌詞取得
                AudioFile f = AudioFileIO.read(new File(path));
                Mp4Tag mp4tag = (Mp4Tag) f.getTag();
                lyric = mp4tag.getFirst(Mp4FieldKey.LYRICS).replaceAll("\r", "\n");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }
}
