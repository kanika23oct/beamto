package com.example.newplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;

import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.view.View;

@SuppressLint("NewApi")
public class NewMediaPlayer extends Activity implements OnCompletionListener,
		SeekBar.OnSeekBarChangeListener, OnScrollListener {
	public static MediaPlayer mediaPlayer = new MediaPlayer();
	public static ImageView artistImage;
	private static ImageButton btnPlay;
	private static ImageButton btnForward;
	private static ImageButton btnBackward;
	private static ImageButton btnPrevious;
	public static TextView songTitleLabel;
	private static ImageButton btnNext;
	private static ImageButton btnShuffle;
	private static ImageButton btnRepeat;
	private static SeekBar songProgressBar;
	private static TextView songCurrentDurationLabel;
	private static TextView songTotalDurationLabel;
	public static ProgressBar progressBar;
	 ProgressDialog progDialog;
	private static Utilities utils;
	AssetManager am;
	private static ArrayList<HashMap<String, String>> songsList;
	private static ArrayList<HashMap<String, String>> newList;
	public static ImageButton btnPlayList;
	private static ImageButton currentPlayList;
	public static TextView songTitle;
	private static SlidingDrawer slidingDrawer;
	int mVisibleThreashold = 6;
	boolean mLoading = false;
	static boolean mLastPage = false;
	static ClickableListAdapter adaptor;
	int numberOfPages = 1;

	public static ArrayList<HashMap<String, String>> selectedSongs = new ArrayList<HashMap<String, String>>();
	public static HashMap<String,JSONArray> albumJsonString = new HashMap<String,JSONArray>();
	// Handler to update UI timer, progress bar etc,.
	private static Handler mHandler = new Handler();;
	private static int seekForwardTime = 5000; // 5000 milliseconds
	private static int seekBackwardTime = 5000; // 5000 milliseconds
	private boolean isShuffle = false;
	private boolean isRepeat = false;

	String name;
	String url;
	String albumName;
	static int currentIndex;

	
	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnForward = (ImageButton) findViewById(R.id.btnForward);
		btnBackward = (ImageButton) findViewById(R.id.btnBackward);
		songTitleLabel = (TextView) findViewById(R.id.songTitle);
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
		btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
		btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
		artistImage = (ImageView) findViewById(R.id.songThumbnail);
		btnPlayList = (ImageButton) findViewById(R.id.playList);
		songTitle = (TextView) findViewById(R.id.playListTitle);
		slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
		final RelativeLayout layout = (RelativeLayout) findViewById(R.id.buttonPlayList);
		currentPlayList = (ImageButton) findViewById(R.id.currentPlayList);
		 showDialog(0);
		
		utils = new Utilities();

		
		
		mediaPlayer.pause();
		btnPlayList.setVisibility(View.INVISIBLE);

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

		am = this.getAssets();
		
		
		
		
		
		LoadAlbumPage albumPage = new LoadAlbumPage(getResources().getString(
				R.string.albumsURL), "1",getString(R.string.songsListURL));
		Thread threadAlbum = new Thread(albumPage);
		threadAlbum.start();
		synchronized (albumPage) {

			try {
				
				
				albumPage.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
        adaptor = new ClickableListAdapter(this, songsList);
		final GridView view = (GridView) findViewById(R.id.grid_view_albums);
		view.setAdapter(adaptor);
		view.setOnScrollListener(this);
		progDialog.dismiss();
		
		
		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				btnPlayList.setVisibility(View.INVISIBLE);
				songTitle.setVisibility(View.INVISIBLE);
				view.setVisibility(View.INVISIBLE);
				layout.setBackgroundColor(Color.WHITE);

			}
		});

		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {
				btnPlayList.setVisibility(View.VISIBLE);
				songTitle.setVisibility(View.VISIBLE);
				view.setVisibility(View.VISIBLE);
				layout.setBackgroundColor(Color.rgb(211, 211, 211));
				if (mediaPlayer.isPlaying()) {
					btnPlayList.setImageResource(R.drawable.btn_stop);
				} else {
					btnPlayList.setImageResource(R.drawable.btn_start);
				}
			}

		});

		final Context context = this;
		currentPlayList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.out.println("***** Hello");
				Intent in = new Intent(context, PlayList.class);
				context.startActivity(in);
			}
		});

		btnPlayList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!mediaPlayer.isPlaying()) {
					// mediaPlayer.start();
					// Changing button image to pause button
					if (selectedSongs.size() > 0) {
						HashMap<String, String> song = selectedSongs
								.get(currentIndex);

						url = song.get("songUrl");
						name = song.get("songName");
						albumName = song.get("albumName");
						String imageURL = song.get("coverart_small");
						AlbumList.LoadImageFromWebOperations(imageURL);
						if (name != null) {
							songTitleLabel.setText(albumName + " - " + name);
							songTitle.setText(albumName + " - " + name);
						}
						mediaPlayer.start();
						btnPlay.setImageResource(R.drawable.btn_pause);
						btnPlayList.setImageResource(R.drawable.btn_stop);
					}
				} else {
					mediaPlayer.pause();
					btnPlay.setImageResource(R.drawable.btn_play);
					btnPlayList.setImageResource(R.drawable.btn_start);
				}
			}
		});

		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (selectedSongs.size() > 1) {
					if (currentIndex == (selectedSongs.size() - 1)) {
						currentIndex = 0;
					} else {
						currentIndex = currentIndex + 1;
					}
					HashMap<String, String> song = selectedSongs
							.get(currentIndex);
					url = song.get("songUrl");
					name = song.get("songName");
					albumName = song.get("albumName");
					if (name != null) {
						songTitleLabel.setText(albumName + " - " + name);
						songTitle.setText(albumName + " - " + name);
					}
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
					if (name != null) {
						songTitleLabel.setText(albumName + " - " + name);
						songTitle.setText(albumName + " - " + name);
					}
					playSong(url);

				}
			}
		});

		btnBackward.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (selectedSongs.size() > 1) {
					currentIndex = 0;
					HashMap<String, String> song = selectedSongs.get(0);
					url = song.get("songUrl");
					name = song.get("songName");
					albumName = song.get("albumName");
					if (name != null) {
						songTitleLabel.setText(albumName + " - " + name);
						songTitle.setText(albumName + " - " + name);
					}
					playSong(url);

				}
			}
		});

		btnForward.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (selectedSongs.size() > 1) {
					HashMap<String, String> song = selectedSongs
							.get(selectedSongs.size() - 1);
					currentIndex = selectedSongs.size() - 1;

					url = song.get("songUrl");
					name = song.get("songName");
					albumName = song.get("albumName");
					if (name != null) {
						songTitleLabel.setText(albumName + " - " + name);
						songTitle.setText(albumName + " - " + name);
					}
					playSong(url);

				}
			}
		});

		btnPlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!mediaPlayer.isPlaying()) {
					// mediaPlayer.start();
					// Changing button image to pause button
					if (selectedSongs.size() > 0) {
						HashMap<String, String> song = selectedSongs
								.get(currentIndex);

						url = song.get("songUrl");
						name = song.get("songName");
						albumName = song.get("albumName");
						String imageURL = song.get("coverart_small");
						AlbumList.LoadImageFromWebOperations(imageURL);
						if (name != null) {
							songTitleLabel.setText(albumName + " - " + name);
							songTitle.setText(albumName + " - " + name);
						}
						mediaPlayer.start();
						btnPlay.setImageResource(R.drawable.btn_pause);
						// btnPlayList.setImageResource(R.drawable.btn_stop);
					}
				} else {
					mediaPlayer.pause();
					btnPlay.setImageResource(R.drawable.btn_play);
					// btnPlayList.setImageResource(R.drawable.btn_start);
				}
			}
		});

		btnRepeat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isRepeat) {
					isRepeat = false;
					Toast.makeText(getApplicationContext(), "Repeat is OFF",
							Toast.LENGTH_SHORT).show();
					btnRepeat.setImageResource(R.drawable.btn_repeat);
				} else {
					// make repeat to true
					isRepeat = true;
					Toast.makeText(getApplicationContext(), "Repeat is ON",
							Toast.LENGTH_SHORT).show();
					// make shuffle to false
					isShuffle = false;
					btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
				}
			}
		});

		btnShuffle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isShuffle) {
					isShuffle = false;
					Toast.makeText(getApplicationContext(), "Shuffle is OFF",
							Toast.LENGTH_SHORT).show();
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
				} else {
					// make repeat to true
					isShuffle = true;
					Toast.makeText(getApplicationContext(), "Shuffle is ON",
							Toast.LENGTH_SHORT).show();
					// make shuffle to false
					isRepeat = false;
					btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
					btnRepeat.setImageResource(R.drawable.btn_repeat);
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.media_player, menu);
		return true;
	}

	public static void setSongList(
			ArrayList<HashMap<String, String>> setSongsList) {
		songsList = setSongsList;
	}

	public static void setNewList(ArrayList<HashMap<String, String>> setNewList) {
		newList = setNewList;
	}

	public static void setLastPage(boolean lastPage) {
		mLastPage = lastPage;
	}

	public static void playSong(String url) {

		try {
			if (url != null) {
				HashMap<String, String> song = selectedSongs.get(currentIndex);
				String imageURL = song.get("coverart_small");
				AlbumList.LoadImageFromWebOperations(imageURL);
				mediaPlayer.reset();
				mediaPlayer.setDataSource(url);
				mediaPlayer.prepare();
				mediaPlayer.start();
				// Changing Button Image to pause image
				btnPlay.setImageResource(R.drawable.btn_pause);
				btnPlayList.setImageResource(R.drawable.btn_stop);
				// set Progress bar values
				songProgressBar.setProgress(0);
				songProgressBar.setMax(100);
				btnPlayList.setVisibility(View.VISIBLE);
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
	public static void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}

	/**
	 * Background Runnable thread
	 * */
	private static Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long totalDuration = mediaPlayer.getDuration();
			long currentDuration = mediaPlayer.getCurrentPosition();

			// Displaying Total Duration time
			songTotalDurationLabel.setText(""
					+ utils.milliSecondsToTimer(totalDuration));
			// Displaying time completed playing
			songCurrentDurationLabel.setText(""
					+ utils.milliSecondsToTimer(currentDuration));

			// Updating progress bar
			int progress = (int) (utils.getProgressPercentage(currentDuration,
					totalDuration));
			// Log.d("Progress", ""+progress);
			songProgressBar.setProgress(progress);

			// Running this thread after 100 milliseconds
			mHandler.postDelayed(this, 100);
		}
	};

	/**
	 * 
	 * */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromTouch) {

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
	 * When user stops moving the progress handler
	 * */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mediaPlayer.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(),
				totalDuration);

		// forward or backward to certain seconds
		mediaPlayer.seekTo(currentPosition);

		// update timer progress again
		updateProgressBar();
	}

	@Override
	public void onBackPressed() {
		if (slidingDrawer.isOpened())
			slidingDrawer.close();
		else {
			mediaPlayer.pause();
			this.finish();

		}
		return;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int lastInScreen = firstVisibleItem + visibleItemCount;
		System.out.println("**** total count :"+totalItemCount);
		System.out.println(" %% FV " + firstVisibleItem);
		System.out.println(" %% LS " + lastInScreen);

		if (!mLastPage && !(mLoading) && ((totalItemCount-lastInScreen)<=10)) {
			 showDialog(0);
			numberOfPages++;
			AddToList(numberOfPages);
			mLoading = false;
			progDialog.dismiss();
		}
	}

	public void AddToList(int numberOfPages)  {

		
	  
		
		System.out.println("***** Page :" + numberOfPages);
		mLoading = true;
		final String pages = "" + numberOfPages;
		LoadAlbumPage albumPage = new LoadAlbumPage(getResources().getString(
				R.string.albumsURL), pages,getString(R.string.songsListURL));
		Thread threadAlbums = new Thread(albumPage);
		threadAlbums.start();
		adaptor.notifyDataSetChanged();
		mLoading = false;
		
		
	}

	public static void appendToAdaptor() {
		for (int i = 0; i < newList.size(); i++) {
			adaptor.addToList(newList.get(i));
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompletion(android.media.MediaPlayer arg0) {
		HashMap<String, String> song = null;
		if (selectedSongs.size() >= 1) {
			if (isRepeat) {
				song = selectedSongs.get(currentIndex);
			} else if (isShuffle) {
				// shuffle is on - play a random song
				Random rand = new Random();
				currentIndex = rand.nextInt((selectedSongs.size() - 1) - 0 + 1) + 0;
				song = selectedSongs.get(currentIndex);
			} else if (currentIndex < (selectedSongs.size() - 1)) {
				currentIndex = currentIndex + 1;
				song = selectedSongs.get(currentIndex);

			} else {
				// play first song
				song = selectedSongs.get(0);

				currentIndex = 0;
			}
			if (song != null) {
				url = song.get("songUrl");
				name = song.get("songName");
				albumName = song.get("albumName");
				if (name != null)
					songTitleLabel.setText(albumName + " - " + name);
				playSong(url);
			}
		}
	}

	// Method to create a progress bar dialog of either spinner or horizontal type
    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
        case 0:                      // Spinner
            progDialog = new ProgressDialog(this);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDialog.setMessage("Loading...");
            return progDialog;
        case 1:                      // Horizontal
        	 progDialog = new ProgressDialog(this);
             progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             progDialog.setMessage("Loading...");
            return progDialog;
        default:
            return null;
        }
    }
}
