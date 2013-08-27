package com.example.beamto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.beamto.R;
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
	String albumUrl;
	private String imageURL = "";
	ImageView imageView;
	DisplayImageOptions options;
	ImageLoader imageLoader;

	@SuppressWarnings("deprecation")
	public ClickableListAdapter(Context context,
			List<HashMap<String, String>> songList) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.songsList = songList;
		resources = context.getResources();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher).cacheOnDisc()
				.cacheInMemory().build();

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
				click(position);
			}
		});

		final TextView itemAlbumName = (TextView) v
				.findViewById(R.id.icon_text);
		itemAlbumName.setText(song.get("name"));
		itemAlbumName.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				click(position);
			}
		});
		
		final TextView itemArtistName = (TextView) v
				.findViewById(R.id.album_artist);
		itemArtistName.setText(song.get(VariablesList.TAG_ARTIST_NAME));
		itemArtistName.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				click(position);
			}
		});
		
		final TextView itemTotalSongs = (TextView) v
				.findViewById(R.id.total_songs);
		itemTotalSongs.setText(song.get(VariablesList.NUMBER_OF_SONGS)+ " songs");
		itemTotalSongs.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				click(position);
			}
		});

		return v;
	}

	public void click(int position){
		HashMap<String, String> song = songsList.get(position);
		String songIndex = song.get("id");
		String albumName = (song.get("name"));
		String artistName = song.get(VariablesList.TAG_ARTIST_NAME);
		String totalSongs = song.get(VariablesList.NUMBER_OF_SONGS);
		System.out.println("%%%%%%% total Songs :"+ totalSongs);
		Intent in = new Intent(context, SongListActivity.class);
		in.putExtra("albumIndex", songIndex);
		in.putExtra("albumName", albumName);
		in.putExtra("albumImage",
				song.get(VariablesList.TAG_ALBUM_IMAGE));
		in.putExtra("artistName", artistName);
		in.putExtra("totalSongs", totalSongs);
		context.startActivity(in);
		
	}
}
