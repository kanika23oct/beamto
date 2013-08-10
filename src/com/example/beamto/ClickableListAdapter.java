package com.example.beamto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.newplayer.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class ClickableListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	List<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	String albumName = "";
	private static Resources resources;
	StringBuffer albumUrl;
	private String imageURL = "";
	ImageView imageView;
	public static HashMap<String,Bitmap> imageMap = new HashMap<String,Bitmap>();
	DisplayImageOptions options;
	ImageLoader imageLoader;
	

	@SuppressWarnings("deprecation")
	public ClickableListAdapter(Context context,
			List<HashMap<String, String>> songList) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.songsList = songList;
		resources = context.getResources();
		imageLoader= ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_launcher)
			    .showImageForEmptyUri(R.drawable.ic_launcher)
			    .cacheOnDisc()
			    .cacheInMemory()
			    .build();
		
		
	}

	public void addToList(HashMap<String, String> song) {
		songsList.add(song);
	}
	
	public void setImage(String url,Bitmap image) {
		imageMap.put(url, image);
	}
	
	public Bitmap getImage(String url) {
		return imageMap.get(url);
	}
	
	public void clearImage(){
		imageMap.clear();
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
		imageURL = song.get(VariablesList.TAG_ALBUM_IMAGE);
		View v = null;
		
		if (convertView != null) {
			v = convertView;
		} else {
			v = inflater.inflate(R.layout.gridlayout_item, parent, false);
		}
		
      	ImageView imageView = (ImageView) v.findViewById(R.id.icon_image);
		imageLoader.displayImage(imageURL, imageView, options);
		imageView.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				albumUrl = new StringBuffer(resources
						.getString(R.string.songsListURL));
				String albumIndex = song.get("id");
				albumName = (song.get("name"));
				imageURL = song.get(VariablesList.TAG_ALBUM_IMAGE);

				SongsList albumList = new SongsList(imageURL, albumUrl,
						albumName, albumIndex, resources);
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
						String url = playingSong.get("songUrl");
						String songName = playingSong.get("songName");
						if (songName != null) {
							NewMediaPlayer.songTitleLabel.setText(albumName
									+ " - " + songName);
							NewMediaPlayer.songTitle.setText(albumName + "-"
									+ songName);
						}
						NewMediaPlayer.playSong(url);
					}
				}
			}
		});

	
		final TextView itemAlbumName = (TextView) v
				.findViewById(R.id.icon_text);
		itemAlbumName.setText(song.get("name"));
		itemAlbumName.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				HashMap<String, String> song = songsList.get(position);
				String songIndex = song.get("id");
				String albumName = (song.get("name"));
				Intent in = new Intent(context, SongListActivity.class);
				in.putExtra("albumIndex", songIndex);
				in.putExtra("albumName", albumName);
				in.putExtra("AlbumImage",
						song.get(VariablesList.TAG_ALBUM_IMAGE));
				context.startActivity(in);
			}
		});

		return v;
	}

}
