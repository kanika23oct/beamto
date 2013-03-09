package com.example.newplayer;

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

}
