package us.beamto.newplayer.ui.activites;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import org.json.JSONArray;

import us.beamto.newplayer.R;
import us.beamto.newplayer.R.drawable;
import us.beamto.newplayer.R.id;
import us.beamto.newplayer.R.layout;
import us.beamto.newplayer.R.menu;
import us.beamto.newplayer.R.string;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

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
import android.view.Menu;
import android.view.MenuItem;
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

public class NewMediaPlayerActivity extends Activity implements
		OnCompletionListener, 
		OnScrollListener, OnClickListener, MediaPlayer.OnPreparedListener {
	
	    
	    
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
	public NavigationLayout navigationLayout;
	
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

	  
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_new_media_player);
	//	showActionBar();
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		gridView = (GridView)findViewById(R.id.grid_view_albums);
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.content);
		final RelativeLayout windowLayout = (RelativeLayout) findViewById(R.id.windowLayout);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;
		
		 LayoutInflater inflator = (LayoutInflater) this
		            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		activity = this;
	//	playActionBar = new PlayerActionBar(inflator);
		
		mPlanetTitles = drawerLayout.getResources().getStringArray(R.array.planets_array);
    	mDrawerList = (ListView) drawerLayout.findViewById(R.id.left_drawer);
    	mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                 R.layout.drawer_list_item, mPlanetTitles));
    	 mTitle = mDrawerTitle = getTitle();
    	
	
    	
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.img_btn_playlist,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
         //       grid.setVisibility(View.VISIBLE);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
         //       grid.setVisibility(View.INVISIBLE);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        
        drawerLayout.setDrawerListener(mDrawerToggle);
        
		
		artistImage = new ThumbnailImage(mainLayout, density, metrics.heightPixels, metrics.widthPixels);
		centerPart = new PlayerCenterPart(mainLayout);
		timerDisplay = new PlayerTimerDisplay(mainLayout);
		footerLayout = new PlayerFooterLayout(mainLayout);
	//	navigationLayout = new NavigationLayout(dwawerLayout);
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
			selectedSongs = (ArrayList<HashMap<String, String>>) getIntent()
					.getSerializableExtra("SelectedSongList");
			currentIndex = 0;
			HashMap<String, String> song = selectedSongs.get(0);
			url = song.get("songUrl");
			name = song.get("songName");
			albumName = song.get("albumName");
			playSong(url);
		}

		adaptor = new ClickableListAdapter(this, songsList);
		final GridView view = (GridView) findViewById(R.id.grid_view_albums);
		view.setAdapter(adaptor);
		view.setOnScrollListener(this);
		// view.setSmoothScrollbarEnabled(true);
		albumURL = BuildValues.BASE_URL + VariablesList.ALBUMS_URL;//getResources().getString(R.string.albumsURL);
		songURL = BuildValues.BASE_URL + VariablesList.SONGS_LIST_URL;//getString(R.string.songsListURL);
		if (songsList.size() == 0) {
			if (numberOfPages == 1) {
				setLoading(true);
				new LoadAlbumPage().execute(albumURL, "1", songURL);

			}
		}

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
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
       
        
        return super.onOptionsItemSelected(item);
    }
    
	private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = inflator.inflate(R.layout.media_player_menu, null);
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(false);
    actionBar.setDisplayShowHomeEnabled (false);
    actionBar.setDisplayShowCustomEnabled(true);
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setCustomView(v);
}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.media_player, menu);
		return true;
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
		return;
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
        return currentIndex ;
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
				Subscriber.getInstance().message("SET_TITLE;"+albumName + " - " + name);
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

	 /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new PlanetFragment();
        
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        drawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class PlanetFragment extends Fragment {
       
        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
           
            return gridView;
        }
    }
    

}
