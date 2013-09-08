package us.beamto.newplayer.ui.activites;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;


import us.beamto.newplayer.R;
import us.beamto.newplayer.R.drawable;
import us.beamto.newplayer.R.id;
import us.beamto.newplayer.R.layout;
import us.beamto.newplayer.R.menu;
import us.beamto.newplayer.R.string;
import us.beamto.newplayer.api.LoadAlbumPage;
import us.beamto.newplayer.common.BuildValues;
import us.beamto.newplayer.common.PhoneStateChange;
import us.beamto.newplayer.common.Utilities;
import us.beamto.newplayer.common.VariablesList;
import us.beamto.newplayer.ui.adapters.ClickableListAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;

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
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
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
public class NewMediaPlayerActivity extends Activity implements OnCompletionListener,
		SeekBar.OnSeekBarChangeListener, OnScrollListener, OnClickListener,
		MediaPlayer.OnPreparedListener {
	public static MediaPlayer mediaPlayer = new MediaPlayer();
	public static ImageView artistImage;
	private static ImageButton btnPlay;
	private static ImageButton btnForward;
	private static ImageButton btnBackward;
	private static ImageButton btnPrevious;
	private static ImageButton btnNext;
	private static ImageButton btnShuffle;
	private static ImageButton btnRepeat;
	private static SeekBar songProgressBar;
	private static TextView songCurrentDurationLabel;
	private static TextView songTotalDurationLabel;
	public static ProgressBar progressBar;
	private static Activity activity;
	static ProgressDialog progDialog;
	private static Utilities utils;
	private static ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	private static ArrayList<HashMap<String, String>> newList;
	public static ImageButton btnPlayList;
	private static ImageButton currentPlayList;
	public static TextView songTitle;
	public static SlidingDrawer slidingDrawer;
	int mVisibleThreashold = 6;
	static boolean mLoading = false;
	static boolean mLastPage = false;
	static ClickableListAdapter adaptor;
	static int numberOfPages = 1;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	String currentSongName;

	private ArrayList<HashMap<String, String>> selectedSongs = new ArrayList<HashMap<String, String>>();
	public static HashMap<String, JSONArray> albumJsonString = new HashMap<String, JSONArray>();
	// Handler to update UI timer, progress bar etc,.
	private static Handler mHandler = new Handler();;
	private static int seekForwardTime = 5000; // 5000 milliseconds
	private static int seekBackwardTime = 5000; // 5000 milliseconds
	private boolean isShuffle = false;
	private boolean isRepeat = false;
	public static String albumURL;
	public static String songURL;

	String name;
	String url;
	String albumName;
	static int currentIndex;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_new_media_player);
		activity = this;
		mediaPlayer.setOnPreparedListener(this);

		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnForward = (ImageButton) findViewById(R.id.btnForward);
		btnBackward = (ImageButton) findViewById(R.id.btnBackward);
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
		btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
		btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
		artistImage = (ImageView) findViewById(R.id.songThumbnail);
		btnPlayList = (ImageButton) findViewById(R.id.playList);
		songTitle = (TextView) findViewById(R.id.songTitle);
		slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
		slidingDrawer.setVisibility(View.INVISIBLE);
		final RelativeLayout layout = (RelativeLayout) findViewById(R.id.buttonPlayList);
		currentPlayList = (ImageButton) findViewById(R.id.currentPlayList);
		currentPlayList.setVisibility(View.INVISIBLE);

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher).cacheOnDisc()
				.cacheInMemory().imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();

		utils = new Utilities();

		mediaPlayer.pause();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		float density = metrics.density;
		
		btnPlayList.setVisibility(View.INVISIBLE);
		artistImage.getLayoutParams().height = (int) (density*240);
		artistImage.getLayoutParams().width = (int) (density*300);

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
				setCurrentSongName(albumName + " - " + name);
			playSong(url);
		}

		adaptor = new ClickableListAdapter(this, songsList);
		final GridView view = (GridView) findViewById(R.id.grid_view_albums);
		view.setAdapter(adaptor);
		view.setOnScrollListener(this);
		// view.setSmoothScrollbarEnabled(true);
		albumURL = BuildValues.BASE_URL + VariablesList.ALBUMS_URL;
		songURL = BuildValues.BASE_URL + VariablesList.SONGS_LIST_URL;
		if (songsList.size() == 0) {
			if (numberOfPages == 1) {
				setLoading(true);
				new LoadAlbumPage().execute(albumURL, "1", songURL);

			}
		}

		PhoneStateChange listener = new PhoneStateChange();
		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if(tManager != null)
            tManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        
		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				slidingDrawer.setVisibility(View.VISIBLE);
				btnPlayList.setVisibility(View.INVISIBLE);
				currentPlayList.setVisibility(View.VISIBLE);
				view.setVisibility(View.INVISIBLE);
				layout.setBackgroundResource(R.drawable.bg_player_header);

			}
		});

		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {
				if (selectedSongs.size() > 0 ) {
					btnPlayList.setVisibility(View.VISIBLE);

				}
				view.setVisibility(View.VISIBLE);
				currentPlayList.setVisibility(View.INVISIBLE);
				layout.setBackgroundColor(Color.rgb(211, 211, 211));
				if (mediaPlayer.isPlaying()) {
					slidingDrawer.setVisibility(View.VISIBLE);
					btnPlayList.setImageResource(R.drawable.btn_stop);
				} else {
					btnPlayList.setImageResource(R.drawable.btn_start);
				}
			}

		});

		final Context context = this;
		currentPlayList.setClickable(true);
		currentPlayList.setOnClickListener(this);

		currentPlayList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent in = new Intent(context, PlayListActivity.class);
				context.startActivity(in);
			}
		});

		btnPlayList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!mediaPlayer.isPlaying()) {
					// Changing button image to pause button
					if (selectedSongs.size() > 0) {
						HashMap<String, String> song = selectedSongs
								.get(currentIndex);

						url = song.get("songUrl");
						name = song.get("songName");
						albumName = song.get("albumName");
						String imageURL = song.get("coverart_small");
						imageLoader
								.displayImage(imageURL, artistImage, options);
						if (name != null) {
							setCurrentSongName(albumName + " - " + name);
						}
						mediaPlayer.start();
						btnPlay.setImageResource(R.drawable.btn_pause);

						btnPlayList.setImageResource(R.drawable.btn_stop);
						if (slidingDrawer.isOpened()) {
							btnPlayList.setVisibility(View.INVISIBLE);
						}
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
						setCurrentSongName(albumName + " - " + name);
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
						setCurrentSongName(albumName + " - " + name);
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
						setCurrentSongName(albumName + " - " + name);
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
						setCurrentSongName(albumName + " - " + name);
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
						imageLoader
								.displayImage(imageURL, artistImage, options);
						if (name != null) {
							setCurrentSongName(albumName + " - " + name);
						}
						mediaPlayer.start();
						btnPlay.setImageResource(R.drawable.btn_pause);

						btnPlayList.setImageResource(R.drawable.btn_stop);
						if (slidingDrawer.isOpened()) {
							btnPlayList.setVisibility(View.INVISIBLE);
						}
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

	public static ClickableListAdapter getClickableListAdapter() {
		return adaptor;
	}

	public static void setSongList(
			ArrayList<HashMap<String, String>> setSongsList) {
		songsList = setSongsList;
	}

	public static void setNewList(ArrayList<HashMap<String, String>> setNewList) {
		newList = setNewList;
	}

	public void setCurrentSongName(String songName) {
		this.currentSongName = songName;
	}

	public static void setLastPage(boolean lastPage) {
		mLastPage = lastPage;
	}
	public ImageLoader getImageLoader(){
		return imageLoader;
	}

	public static void setCurrentIndex(int index) {
		currentIndex = index;
	}
	
	public static Integer getCurrentIndex() {
		return currentIndex ;
	}
	
	
	@SuppressWarnings("deprecation")
	public void setLoading(boolean loading) {
		mLoading = loading;
		if (mLoading == false) {
			progDialog.dismiss();
		} else if (mLoading == true) {
			// if(!progDialog.i)
			showDialog(0);
		}
	}

	@SuppressWarnings("deprecation")
	public void playSong(String url) {

		try {
			if (url != null) {
				HashMap<String, String> song = selectedSongs.get(currentIndex);
				String imageURL = song.get("coverart_small");
				songTitle.setText("");
				if (slidingDrawer.isOpened()) {
					btnPlayList.setVisibility(View.INVISIBLE);
					showDialog(1);
				}
				
				mediaPlayer.reset();
				mediaPlayer.setDataSource(url);
				mediaPlayer.prepareAsync();
				imageLoader.displayImage(imageURL, artistImage, options);
				

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
			if (mediaPlayer.isPlaying()) {
				long totalDuration = mediaPlayer.getDuration();
				long currentDuration = mediaPlayer.getCurrentPosition();

				// Displaying Total Duration time
				songTotalDurationLabel.setText(""
						+ utils.milliSecondsToTimer(totalDuration));
				// Displaying time completed playing
				songCurrentDurationLabel.setText(""
						+ utils.milliSecondsToTimer(currentDuration));

				// Updating progress bar
				int progress = (int) (utils.getProgressPercentage(
						currentDuration, totalDuration));
				// Log.d("Progress", ""+progress);
				songProgressBar.setProgress(progress);

				// Running this thread after 100 milliseconds
				mHandler.postDelayed(this, 100);
			}
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
			mediaPlayer.reset();
			System.gc();
			clearData();
			this.finish();

		}
		return;
	}

	private void clearData() {
		selectedSongs.clear();
	}

	public static void AddToList(int numberOfPages) {

		String page = "" + numberOfPages;
		new LoadAlbumPage().execute(albumURL, page, songURL);

	}

	public void addToSelectedList(HashMap<String, String> song) {

		selectedSongs.add(song);

	}

	public HashMap<String, String> getSelectedSong(int position) {

		return selectedSongs.get(position);
	}

	public ArrayList<HashMap<String, String>> getSelectedSongList() {

		return selectedSongs;
	}

	public static NewMediaPlayerActivity getActivity() {
		return (NewMediaPlayerActivity) activity;
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
					setCurrentSongName(albumName + " - " + name);
				playSong(url);
			}
		}
	}

	// Method to create a progress bar dialog of either spinner or horizontal
	// type

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: // Spinner
			progDialog = new ProgressDialog(activity);
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setMessage("Loading...Please wait");
			progDialog.setCancelable(true);
			return progDialog;
			
		case 1: // Spinner
			progDialog = new ProgressDialog(activity);
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setMessage("Playing...Please wait");
			progDialog.setCancelable(true);
			return progDialog;
		default:
			return null;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		int lastInScreen = firstVisibleItem + visibleItemCount;
		if (!mLastPage && !(mLoading) && (totalItemCount == lastInScreen)
				&& lastInScreen > 0) {
			setLoading(true);
			numberOfPages++;
			AddToList(numberOfPages);
			// mLoading = false;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.currentPlayList:
			Intent in = new Intent(this, PlayListActivity.class);
			startActivity(in);

		}

	}

	@Override
	public void onPrepared(MediaPlayer mediaPlayer) {
		mediaPlayer.start();
		progDialog.dismiss();
		songTitle.setVisibility(View.VISIBLE);
		btnPlay.setImageResource(R.drawable.btn_pause);
		songProgressBar.setProgress(0);
		songProgressBar.setMax(100);
		songTitle.setText(currentSongName);
		btnPlayList.setImageResource(R.drawable.btn_stop);
		if (slidingDrawer.isOpened()) {
			btnPlayList.setVisibility(View.INVISIBLE);
		}
		else
		{
			btnPlayList.setVisibility(View.VISIBLE);
		}

		// Updating progress bar

		updateProgressBar();

	}

}
