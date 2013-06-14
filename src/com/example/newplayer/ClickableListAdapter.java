package com.example.newplayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ClickableListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	private static TextView songTitleLabel;
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String SOURCE_URL = "source_url";
	private static final String TAG_ALBUM_IMAGE = "coverart_small";
	String albumName = "";
	private static Resources resources;
	StringBuffer albumUrl;
	private  String imageURL="";

	public ClickableListAdapter(Context context,
			ArrayList<HashMap<String, String>> songList) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.songsList = songList;
		resources = context.getResources();
		albumUrl = new StringBuffer(resources.getString(R.string.songsListURL));
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
	
/*	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        final HashMap<String, String> song = songsList.get(position);
        imageURL = song.get(TAG_ALBUM_IMAGE);
		View v = null;
		final ImageView imageView = new ImageView(context);	
		final Thread startAlbum = new Thread() {
			public void run() {
			
				imageView.setImageBitmap(AlbumList.LoadImagetoGridView(imageURL));
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
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		        imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
		        
	  return imageView;	
	}*/

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final HashMap<String, String> song = songsList.get(position);
		
		
		// songTitleLabel = (TextView) playerView .findViewById(R.id.songTitle);
		View v = null;
		if (convertView != null)
			v = convertView;
		else
			v = inflater.inflate(R.layout.playlist_item, parent, false);
		
		View playerView = inflater.inflate(R.layout.player, parent, false);
		songTitleLabel = (TextView) playerView.findViewById(R.id.songTitle);
		TextView itemAlbumName = (TextView) v.findViewById(R.id.albumTitle);
		itemAlbumName.setText(song.get("name"));
		ImageButton albumPlayButton = (ImageButton) v
				.findViewById(R.id.btnAlbumPlay);
		
		albumPlayButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				int albumIndex = Integer.parseInt(song.get("id"));
				albumName = (song.get("name"));
				albumUrl.append(albumIndex + "/songs.json");
				imageURL = song.get(TAG_ALBUM_IMAGE);
				System.out.println("@@@@@@@@@ "+imageURL);
				final Thread startAlbum = new Thread() {
					public void run() {
						JSONParser jParser = new JSONParser();

						try {
							String jsonString = jParser
									.readJsonFromUrl(albumUrl.toString());
							JSONObject jsonObject = new JSONObject(jsonString);
							JSONArray songs = jsonObject.getJSONArray("songs");
							for (int i = 0; i < songs.length(); i++) {
								HashMap<String, String> song = new HashMap<String, String>();
								JSONObject songDetails = songs.getJSONObject(i);
								String id = songDetails.getString(TAG_ID);
								String name = songDetails.getString(TAG_NAME);
								String jsonSongURL = "http://dev.beamto.us/songs/"
										+ id + ".json";
								String jsonStringSong = jParser
										.readJsonFromUrl(jsonSongURL);
								JSONObject jsonObjectSong = new JSONObject(
										jsonStringSong);
								String songUrl = jsonObjectSong
										.getString("url");
								
								if(songUrl.startsWith("https://"))
								{
									songUrl = songUrl.replaceFirst("https://", "http://");
								}
								System.out.println(songUrl);
								song.put("songUrl", songUrl);
								song.put(TAG_ID, id);
								song.put("songName", name);
								song.put("albumName", albumName);
								System.out.println("###### "+ imageURL );
								song.put(TAG_ALBUM_IMAGE, imageURL);
								NewMediaPlayer.selectedSongs.add(song);
								System.out.println("Adding songs");
							}
							synchronized (this) {
								this.notifyAll();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
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
				//	NewMediaPlayer.artistImage.setImageDrawable(AlbumList.LoadImageFromWebOperations("http://s3.amazonaws.com/beamtous-staging-assets/bumpfoot/allel-lost-world-ep-bump183/coverart_small.jpg"));
					
					albumUrl = new StringBuffer(resources.getString(R.string.songsListURL));

					if (!NewMediaPlayer.mediaPlayer.isPlaying()) {
						HashMap<String, String> playingSong = NewMediaPlayer.selectedSongs
								.get(0);
						System.out.println("* Adding the songs for album");
						if (playingSong != null) {
							System.out.println("** Playing the song for album");
							String url = playingSong.get("songUrl");
							String songName = playingSong.get("songName");
							//AlbumList.LoadImageFromWebOperations(playingSong.get(TAG_ALBUM_IMAGE));
							 if (songName != null)
							 NewMediaPlayer.songTitleLabel.setText(albumName + " - " +
							 songName);
							
							NewMediaPlayer.playSong(url);
						}
					}
				}
			}
		});
		return v;
	}
}
