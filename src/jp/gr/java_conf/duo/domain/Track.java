package jp.gr.java_conf.duo.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    private long id;         // コンテントプロバイダに登録されたID
    private String path;     // 実データのPATH
    private String title;    // トラックタイトル
    private String album;    // アルバムタイトル
    private String artist;   // アーティスト名
    private long albumId;    // アルバムID
    private long artistId;   // アーティストID
    private long duration;   // 再生時間(ミリ秒)
    private int trackNo;     // アルバムのトラックナンバ
    private String lyrics;   // 歌詞
    private Uri uri;         // URI

    /* トラック取得時に取得する情報 */
    public static final String[] COLUMNS = {
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.ARTIST_ID,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.TRACK};

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

	public String getStrDuration(){
        long dm = duration / 60000;
        long ds = (duration - (dm * 60000)) / 1000;
        return String.format(Locale.getDefault(), "%d:%02d", dm, ds);
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

    public String getLyrics() {
        if (lyrics == null) {
            // TODO ファイルタイプの判定を入れたい
            try {
                AudioFile f = AudioFileIO.read(new File(path));
                Mp4Tag mp4tag = (Mp4Tag) f.getTag();
                lyrics = mp4tag.getFirst(Mp4FieldKey.LYRICS);
                lyrics = lyrics.replaceAll("\r", "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    /* 全トラック取得 */
    public static List<Track> getItems(Context activity) {
        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Track.COLUMNS,
                null,
                null,
                null);

        List<Track> tracks = new ArrayList<Track>();
        while (cursor.moveToNext()) {
            /* １秒未満は無視する */
            if (cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) < 1000) {
                continue;
            }
            tracks.add(new Track(cursor));
        }

        cursor.close();
        return tracks;
    }

    /* 指定されたアーティストのトラック取得 */
    public static List<Track> getItemsByArtistId(Context activity, long artistId) {
        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Track.COLUMNS,
                MediaStore.Audio.Media.ARTIST_ID + "= ?",
                new String[] { String.valueOf(artistId) },
                null);

        List<Track> tracks = new ArrayList<Track>();
        while (cursor.moveToNext()) {
            tracks.add(new Track(cursor));
        }

        cursor.close();
        return tracks;
    }

    /* 指定されたアルバムのトラック取得 */
    public static List<Track> getItemsByAlbumId(Context activity, long albumId) {
        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Track.COLUMNS,
                MediaStore.Audio.Media.ALBUM_ID + "= ?",
                new String[] { String.valueOf(albumId) },
                null);

        List<Track> tracks = new ArrayList<Track>();
        while (cursor.moveToNext()) {
            tracks.add(new Track(cursor));
        }

        cursor.close();
        return tracks;
    }

    /* 指定されたトラック取得 */
    public static Track getItemsByTrackId(Context activity, long id) {
        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Track.COLUMNS,
                MediaStore.Audio.Media._ID + "= ?",
                new String[] { String.valueOf(id) },
                null);

        Track track = new Track(cursor);
        cursor.close();
        return track;
    }
}
