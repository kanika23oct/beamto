package com.example.newplayer;

import java.io.IOException;

import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ImageButton;
import android.view.View;

public class NewMediaPlayer extends Activity implements OnCompletionListener {
	private MediaPlayer mediaPlayer;
	private ImageButton btnPlay;
	private ImageButton btnForward;
	private ImageButton btnBackward;
	private ImageButton btnPrevious;
	private ImageButton btnPause;
	private ImageButton btnPlaylist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnForward = (ImageButton) findViewById(R.id.btnForward);
		btnBackward = (ImageButton) findViewById(R.id.btnBackward);
		btnPause = (ImageButton) findViewById(R.id.btnBackward);
		btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(this);
		Bundle b = getIntent().getExtras();
		if (b != null) {
			String url = getIntent().getExtras().getString("songUrl");
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if (resultCode == 100) {
		String url = data.getExtras().getString("songUrl");
		// play selected song
		playSong(url);
		// }

	}

	@Override
	public void onCompletion(android.media.MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	public void playSong(String url) {
		
		try {
			if (url != null) {
				mediaPlayer.pause();
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
