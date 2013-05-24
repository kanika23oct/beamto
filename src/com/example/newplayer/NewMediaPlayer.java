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
		
		mediaPlayer.pause();
		mediaPlayer.setOnCompletionListener(this);
		Bundle b = getIntent().getExtras();
		if (b != null) {
			ArrayList<HashMap<String, String>> selectedSongs =  (ArrayList<HashMap<String, String>>)getIntent().getSerializableExtra("SelectedSongList");
			 HashMap<String, String> song = selectedSongs .get(0);
			String url = song.get("songUrl");
			String name = song.get("songName");
			String albumName = song.get("albumName");
			if(name !=null)
				songTitleLabel.setText(albumName+" - "+name);
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
