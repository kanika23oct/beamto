package us.beamto.newplayer.ui.activites;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import org.json.JSONArray;

import us.beamto.newplayer.R;
import us.beamto.newplayer.R.drawable;
import us.beamto.newplayer.R.id;
import us.beamto.newplayer.R.layout;
import us.beamto.newplayer.R.menu;
import us.beamto.newplayer.R.string;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;

import us.beamto.newplayer.api.LoadAlbumPage;

import us.beamto.newplayer.common.BuildValues;
import us.beamto.newplayer.common.LoadAlbumBroadcastReceiver;
import us.beamto.newplayer.common.PhoneStateChange;
import us.beamto.newplayer.common.Utilities;
import us.beamto.newplayer.common.VariablesList;
import us.beamto.newplayer.service.LoadAlbumService;
import us.beamto.newplayer.service.LoadSongsService;
import us.beamto.newplayer.service.LoadTrendingAlbumsService;
import us.beamto.newplayer.ui.adapters.ClickableListAdapter;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.view.View;

@SuppressLint({ "NewApi", "ValidFragment" })
public class NewMediaPlayerActivity extends BaseActivity implements
		OnCompletionListener, OnScrollListener, OnClickListener,
		MediaPlayer.OnPreparedListener {

	private LoadAlbumBroadcastReceiver broadcastReceiver;
	private static Activity activity;
	public static MediaPlayer mediaPlayer;
	protected static ProgressDialog progDialog;
	private Utilities utils;
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> newList;
	public static SlidingDrawer slidingDrawer;
	boolean mLoading = false;

	boolean mLastPage = false;
	private ClickableListAdapter adaptor;
	private int numberOfPages = 1;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	private static ArrayList<HashMap<String, String>> selectedSongs = new ArrayList<HashMap<String, String>>();
	private static HashMap<String, JSONArray> albumJsonString = new HashMap<String, JSONArray>();

	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();
	public static PlayerTimerDisplay timerDisplay;
	public ThumbnailImage artistImage;
	public PlayerCenterPart centerPart;
	public PlayerFooterLayout footerLayout;
	public SlidingWindow slidingWindow;

	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds
	boolean isShuffle = false;
	boolean isRepeat = false;
	private String albumURL;
	private String songURL;

	String name;
	String url;
	String albumName;
	private String currentSongName;
	static int currentIndex;
	private String[] mPlanetTitles;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private DrawerLayout drawerLayout;
	private static View gridView;
	private String albumApi;

	public NewMediaPlayerActivity() {
		super(R.string.app_name);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_new_media_player);
		// drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		RelativeLayout contentLayout = (RelativeLayout) findViewById(R.id.content_frame);

		gridView = (GridView) contentLayout.findViewById(R.id.grid_view_albums);
		LinearLayout mainLayout = (LinearLayout) contentLayout
				.findViewById(R.id.content);
		final RelativeLayout windowLayout = (RelativeLayout) contentLayout
				.findViewById(R.id.windowLayout);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;

		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		activity = this;

		// drawerLayout.setDrawerListener(mDrawerToggle);

		artistImage = new ThumbnailImage(mainLayout, density,
				metrics.heightPixels, metrics.widthPixels);
		centerPart = new PlayerCenterPart(mainLayout);
		timerDisplay = new PlayerTimerDisplay(mainLayout);
		footerLayout = new PlayerFooterLayout(mainLayout);
		// navigationLayout = new NavigationLayout(dwawerLayout);
		slidingWindow = new SlidingWindow(this, windowLayout);

		centerPart.eventSubscriber();
		slidingWindow.eventSubscriber();
		footerLayout.eventSubscriber();

		mediaPlayer = new MediaPlayer();
		slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
		utils = new Utilities();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher).cacheOnDisc()
				.cacheInMemory()
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();

		mediaPlayer.pause();
		slidingDrawer.setVisibility(View.INVISIBLE);

		// Listeners
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnPreparedListener(this);
		Bundle b = getIntent().getExtras();
		if (b != null) {
			albumApi = (String) getIntent().getSerializableExtra("Album_Url");
			albumApi = albumApi.trim();
			selectedSongs = (ArrayList<HashMap<String, String>>) getIntent()
					.getSerializableExtra("SelectedSongList");
			if (selectedSongs != null) {
				currentIndex = 0;
				HashMap<String, String> song = selectedSongs.get(0);
				url = song.get("songUrl");
				name = song.get("songName");
				albumName = song.get("albumName");
				playSong(url);
			} else {
				selectedSongs = new ArrayList<HashMap<String, String>>();
			}
		} else {
			albumApi = "1";
		}
		adaptor = new ClickableListAdapter(this, songsList);
		final GridView view = (GridView) findViewById(R.id.grid_view_albums);
		view.setAdapter(adaptor);
		view.setOnScrollListener(this);
		// view.setSmoothScrollbarEnabled(true);

		albumURL = Utilities.albumURL(Integer.parseInt(albumApi));
		System.out.println("Album Trending URL: "+albumURL);
		/*
		 * albumURL = BuildValues.BASE_URL + VariablesList.ALBUMS_URL;
		 * 
		 * songURL = BuildValues.BASE_URL + VariablesList.SONGS_LIST_URL;//
		 * getString(R.string.songsListURL); if (songsList.size() == 0) { if
		 * (numberOfPages == 1) { setLoading(true); new
		 * LoadAlbumPage().execute(albumURL, "1", songURL);
		 * 
		 * } }
		 */
		if (albumURL.equalsIgnoreCase("Trending")) {
			setLoading(true);
			Intent service = new Intent(this, LoadTrendingAlbumsService.class);
			service.putExtra("Page-Number", "1");
			this.startService(service);
			
		} else {
			setLoading(true);
			Intent service = new Intent(this, LoadAlbumService.class);
			service.putExtra("Page-Number", "1");
			this.startService(service);
		}

		broadcastReceiver = new LoadAlbumBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(
				"us.beamto.newplayer.service.LoadAlbumService");
		intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(broadcastReceiver, intentFilter);

		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				slidingDrawer.setVisibility(View.VISIBLE);
				if (mediaPlayer.isPlaying()) {
					// footerLayout.setPlayButton(R.drawable.btn_pause);
					Subscriber.getInstance().message(
							"CHANGE_PLAY_BUTTON;" + R.drawable.btn_pause);
				} else {
					// footerLayout.setPlayButton(R.drawable.btn_play);
					Subscriber.getInstance().message(
							"CHANGE_PLAY_BUTTON;" + R.drawable.btn_play);
				}

				Subscriber.getInstance().message(
						"CHANGE_PLAYER_VISIBILTY;" + View.INVISIBLE);
				// slidingWindow.setPlayButtonVisibilty(View.INVISIBLE);

				Subscriber.getInstance().message(
						"CHANGE_CURRENTLIST_VISIBILTY;" + View.VISIBLE);
				slidingWindow.setCurrentPlaylistVisibilty(View.VISIBLE);
				view.setVisibility(View.INVISIBLE);
				windowLayout.setBackgroundResource(R.drawable.bg_player_header);

			}
		});

		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {
				if (selectedSongs.size() > 0) {
					slidingWindow.setPlayButtonVisibilty(View.VISIBLE);

				}
				view.setVisibility(View.VISIBLE);
				slidingWindow.setCurrentPlaylistVisibilty(View.INVISIBLE);
				windowLayout.setBackgroundColor(Color.rgb(211, 211, 211));
				if (mediaPlayer.isPlaying()) {
					// slidingWindow.setPlayButton(R.drawable.btn_stop);
					Subscriber.getInstance().message(
							"CHANGE_PLAYER_BUTTON;" + R.drawable.btn_stop);
				} else {
					Subscriber.getInstance().message(
							"CHANGE_PLAYER_BUTTON;" + R.drawable.btn_start);
					// slidingWindow.setPlayButton(R.drawable.btn_start);
				}
			}

		});

	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@SuppressWarnings("deprecation")
	public void playSong(String url) {

		try {
			if (url != null) {
				HashMap<String, String> song = selectedSongs.get(currentIndex);
				String imageURL = song.get("coverart_small");
				onCreateDialog(1);

				if (!slidingDrawer.isOpened()) {
					Subscriber.getInstance().message(
							"CHANGE_PLAYER_VISIBILTY;" + View.VISIBLE);
					// slidingWindow.setPlayButtonVisibilty(View.VISIBLE);
				}
				mediaPlayer.reset();
				mediaPlayer.setDataSource(url);
				mediaPlayer.prepareAsync();
				Subscriber.getInstance().message("UPLOAD_IMAGE;" + imageURL);

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

	@Override
	public void onBackPressed() {
		if (slidingDrawer.isOpened())
			slidingDrawer.close();
		else {
			mediaPlayer.pause();
			System.gc();
			clearData();
			this.finish();

		}
	//	 unregisterReceiver(broadcastReceiver);
		return;
	}

	@Override
	 protected void onDestroy() {
	  super.onDestroy();
	  //un-register BroadcastReceiver
	  unregisterReceiver(broadcastReceiver);
	 }
	
	private void clearData() {
		selectedSongs.clear();
	}

	public ClickableListAdapter getClickableListAdapter() {
		return adaptor;
	}

	public void setNewList(ArrayList<HashMap<String, String>> setNewList) {
		newList = setNewList;
	}

	public void setLastPage(boolean lastPage) {
		mLastPage = lastPage;
	}

	public void setSongTitle(String title) {
		slidingWindow.setSongTitle(title);
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

	public void AddToList(int numberOfPages) {

		String page = "" + numberOfPages;
		//new LoadAlbumPage().execute(albumURL, page, songURL);
		if (albumURL.equalsIgnoreCase("Trending")) {
			setLoading(true);
			Intent service = new Intent(this, LoadTrendingAlbumsService.class);
			service.putExtra("Page-Number", page);
			this.startService(service);
			
		} else {
			setLoading(true);
			Intent service = new Intent(this, LoadAlbumService.class);
			service.putExtra("Page-Number", page);
			this.startService(service);
		}
		
		LoadAlbumBroadcastReceiver broadcastReceiver = new LoadAlbumBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(
				"us.beamto.newplayer.service.LoadAlbumService");
		intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(broadcastReceiver, intentFilter);

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

	public HashMap<String, JSONArray> getAlbumJsonArray() {
		return albumJsonString;
	}

	public void setAlbumJsonArray(String key, JSONArray value) {
		albumJsonString.put(key, value);
	}

	public static NewMediaPlayerActivity getActivity() {
		return (NewMediaPlayerActivity) activity;
	}

	public void appendToAdaptor() {
		for (int i = 0; i < newList.size(); i++) {
			adaptor.addToList(newList.get(i));
		}
		System.out.println("Total Album Count :" + adaptor.getCount());
	}

	public void setCurrentSongName(String songName) {
		this.currentSongName = songName;
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	public void setCurrentIndex(int index) {
		currentIndex = index;
	}

	public Integer getCurrentIndex() {
		return currentIndex;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCompletion(android.media.MediaPlayer arg0) {
		HashMap<String, String> song = null;
		if (selectedSongs != null && selectedSongs.size() >= 1) {
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
				Subscriber.getInstance().message(
						"SET_TITLE;" + albumName + " - " + name);
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
			showDialog(0);
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
		Subscriber.getInstance().message(
				"CHANGE_PLAYER_BUTTON;" + R.drawable.btn_stop);
		Subscriber.getInstance().message(
				"CHANGE_PLAY_BUTTON;" + R.drawable.btn_pause);
		// slidingWindow.setPlayButton(R.drawable.btn_stop);
		// footerLayout.setPlayButton(R.drawable.btn_pause);

		// set Progress bar values
		timerDisplay.setProgressBarValues(0, 100);
		timerDisplay.updateProgressBar();

	}

	private void selectItem(int position) {
		// update the main content by replacing fragments

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

}
