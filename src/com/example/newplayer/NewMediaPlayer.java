package com.example.newplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;

public class NewMediaPlayer extends Activity implements OnCompletionListener {
	private static MediaPlayer mediaPlayer = new MediaPlayer();
	private ImageButton btnPlay;
	private ImageButton btnForward;
	private ImageButton btnBackward;
	private ImageButton btnPrevious;
	private ImageButton btnPause;
	private ImageButton btnPlaylist;
	private TextView songTitleLabel;
	private ImageButton btnNext;
	ArrayList<HashMap<String, String>> selectedSongs;
	String name;
	String url;
	String albumName;
	int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnForward = (ImageButton) findViewById(R.id.btnForward);
		btnBackward = (ImageButton) findViewById(R.id.btnBackward);
		btnPause = (ImageButton) findViewById(R.id.btnBackward);
		btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
		songTitleLabel = (TextView) findViewById(R.id.songTitle);
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
		btnNext = (ImageButton) findViewById(R.id.btnNext);

		mediaPlayer.pause();
		mediaPlayer.setOnCompletionListener(this);
		Bundle b = getIntent().getExtras();
		if (b != null) {
			selectedSongs = (ArrayList<HashMap<String, String>>) getIntent()
					.getSerializableExtra("SelectedSongList");
			currentIndex = 0;
			HashMap<String, String> song = selectedSongs.get(0);
			url = song.get("songUrl");
			name = song.get("songName");
			albumName = song.get("albumName");
			if (name != null)
				songTitleLabel.setText(albumName + " - " + name);
			playSong(url);
		}

		btnPlaylist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(getApplicationContext(),
						PlayListActivity.class);
				startActivityForResult(i, 100);
			}
		});

		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (selectedSongs.size() > 1) {
					if (currentIndex == (selectedSongs.size() - 1))
						currentIndex = 0;
					else
						currentIndex = currentIndex + 1;
					HashMap<String, String> song = selectedSongs
							.get(currentIndex);
					url = song.get("songUrl");
					name = song.get("songName");
					albumName = song.get("albumName");
					if (name != null)
						songTitleLabel.setText(albumName + " - " + name);
					playSong(url);

				}
			}
		});

		btnPrevious.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (selectedSongs.size() > 1) {
					if (currentIndex == 0)
						currentIndex =selectedSongs.size() - 1;
					else
						currentIndex = currentIndex - 1;
					HashMap<String, String> song = selectedSongs
							.get(currentIndex);
					url = song.get("songUrl");
					name = song.get("songName");
					albumName = song.get("albumName");
					if (name != null)
						songTitleLabel.setText(albumName + " - " + name);
					playSong(url);

				}
			}
		});

		btnBackward.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (selectedSongs.size() > 1) {
					HashMap<String, String> song = selectedSongs.get(0);
					url = song.get("songUrl");
					name = song.get("songName");
					albumName = song.get("albumName");
					if (name != null)
						songTitleLabel.setText(albumName + " - " + name);
					playSong(url);

				}
			}
		});

		btnForward.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (selectedSongs.size() > 1) {
					int size = selectedSongs.size();
					HashMap<String, String> song = selectedSongs
							.get(selectedSongs.size() - 1);
					url = song.get("songUrl");
					name = song.get("songName");
					albumName = song.get("albumName");
					if (name != null)
						songTitleLabel.setText(albumName + " - " + name);
					playSong(url);

				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.media_player, menu);
		return true;
	}

	@Override
	public void onCompletion(android.media.MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	public void playSong(String url) {

		try {
			if (url != null) {
				mediaPlayer.reset();
				mediaPlayer.setDataSource(url);
				mediaPlayer.prepare();
				mediaPlayer.start();
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
