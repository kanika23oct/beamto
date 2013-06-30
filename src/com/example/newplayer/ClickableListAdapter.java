package com.example.newplayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ClickableListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	List<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	private static TextView songTitleLabel;
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String SOURCE_URL = "source_url";
	private static final String TAG_ALBUM_IMAGE = "coverart_small";
	String albumName = "";
	private static Resources resources;
	StringBuffer albumUrl;
	private String imageURL = "";

	public ClickableListAdapter(Context context,
			List<HashMap<String, String>> songList) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.songsList = songList;
		resources = context.getResources();
		//albumUrl = new StringBuffer(resources.getString(R.string.songsListURL));
	}

	public void addToList(HashMap<String, String> song) {
		songsList.add(song);
	}

	@Override
	public int getCount() {
		return songsList.size();
	}

	@Override
	public Object getItem(int position) {
		return songsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final HashMap<String, String> song = songsList.get(position);
		imageURL = song.get(TAG_ALBUM_IMAGE);
		View v = null;
		if (convertView != null)
			v = convertView;
		else
			v = inflater.inflate(R.layout.gridlayout_item, parent, false);
		final ImageView imageView = (ImageView) v.findViewById(R.id.icon_image);
		final Thread startAlbum = new Thread() {
			public void run() {

				imageView.setImageBitmap(AlbumList
						.LoadImagetoGridView(imageURL));

				synchronized (this) {
					this.notifyAll();
				}
			}
		};

		synchronized (startAlbum) {
			startAlbum.start();
			try {
				startAlbum.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		imageView.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				albumUrl = new StringBuffer(resources.getString(R.string.songsListURL));
				String albumIndex = song.get("id");
				albumName = (song.get("name"));
			//	albumUrl.append(albumIndex + "/songs.json");
				imageURL = song.get(TAG_ALBUM_IMAGE);
				SongsList albumList = new SongsList(imageURL, albumUrl,
						albumName,albumIndex,resources);
				Thread threadAlbumList = new Thread(albumList);
				threadAlbumList.start();

				synchronized (albumList) {
					try {
						albumList.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (!NewMediaPlayer.mediaPlayer.isPlaying()) {
					HashMap<String, String> playingSong = NewMediaPlayer.selectedSongs
							.get(0);
					if (playingSong != null) {
						System.out.println("** Playing the song for album");
						String url = playingSong.get("songUrl");
						String songName = playingSong.get("songName");
						if (songName != null)
							{
							 NewMediaPlayer.songTitleLabel.setText(albumName
									+ " - " + songName);
							 NewMediaPlayer.songTitle.setText(albumName+"-"+songName);
							}
						NewMediaPlayer.playSong(url);
					}
				}
			}
		});
		
		TextView itemAlbumName = (TextView) v.findViewById(R.id.icon_text);
		itemAlbumName.setText(song.get("name"));
		itemAlbumName.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				HashMap<String, String> song = songsList.get(position);
				String songIndex = song.get("id");
				String albumName = (song.get("name"));
				Intent in = new Intent(context, SongListActivity.class);
				in.putExtra("albumIndex", songIndex);
				in.putExtra("albumName", albumName);
				in.putExtra("AlbumImage", song.get("coverart_small"));
				context.startActivity(in);
			}
		});

		return v;
	}

}
