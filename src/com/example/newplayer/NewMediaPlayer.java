package com.example.newplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View;

public class NewMediaPlayer extends Activity implements OnCompletionListener,SeekBar.OnSeekBarChangeListener {
	private static MediaPlayer mediaPlayer = new MediaPlayer();
	private ImageButton btnPlay;
	private ImageButton btnForward;
	private ImageButton btnBackward;
	private ImageButton btnPrevious;
	private ImageButton btnPause;
	private ImageButton btnPlaylist;
	private TextView songTitleLabel;
	private ImageButton btnNext;
	private SeekBar songProgressBar;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	private Utilities utils;
	ArrayList<HashMap<String, String>> selectedSongs;
	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds

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
		btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
		songTitleLabel = (TextView) findViewById(R.id.songTitle);
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
        
		utils = new Utilities();


		mediaPlayer.pause();
		// Listeners
        songProgressBar.setOnSeekBarChangeListener(this); // Important
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
						currentIndex = selectedSongs.size() - 1;
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

		btnPlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!mediaPlayer.isPlaying())
				{
					mediaPlayer.start();
                      // Changing button image to pause button
                      btnPlay.setImageResource(R.drawable.btn_pause);
				}
				else
				{
					mediaPlayer.pause();
					btnPlay.setImageResource(R.drawable.btn_play);
				}
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.media_player, menu);
		return true;
	}


	public void playSong(String url) {

		try {
			if (url != null) {
				mediaPlayer.reset();
				mediaPlayer.setDataSource(url);
				mediaPlayer.prepare();
				mediaPlayer.start();

				// Changing Button Image to pause image
				btnPlay.setImageResource(R.drawable.btn_pause);
				// set Progress bar values
				songProgressBar.setProgress(0);
				songProgressBar.setMax(100);
				
				// Updating progress bar
				updateProgressBar();		
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
	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);        
    }	
	
	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		   public void run() {
			   long totalDuration = mediaPlayer.getDuration();
			   long currentDuration = mediaPlayer.getCurrentPosition();
			  
			   // Displaying Total Duration time
			   songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
			   // Displaying time completed playing
			   songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));
			   
			   // Updating progress bar
			   int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
			   //Log.d("Progress", ""+progress);
			   songProgressBar.setProgress(progress);
			   
			   // Running this thread after 100 milliseconds
		       mHandler.postDelayed(this, 100);
		   }
		};
		
	/**
	 * 
	 * */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
		
	}

	/**
	 * When user starts moving the progress handler
	 * */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// remove message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
		
    }
	
	/**
	 * When user stops moving the progress hanlder
	 * */
	@Override
    public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mediaPlayer.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
		
		// forward or backward to certain seconds
		mediaPlayer.seekTo(currentPosition);
		
		// update timer progress again
		updateProgressBar();
    }

	

	@Override
	public void onCompletion(android.media.MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

}
