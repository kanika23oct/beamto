package us.beamto.newplayer.ui.activites;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import us.beamto.newplayer.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlidingWindow extends NewMediaPlayerActivity implements Observer {
	private ImageButton btnPlayList;
	private ImageButton currentPlayList;
	private TextView songTitle;
	Activity activity;

	public SlidingWindow(Activity activity, RelativeLayout slidingWindowlayout) {
		Subscriber.getInstance().addObserver(this);
		this.activity = activity;

		btnPlayList = (ImageButton) slidingWindowlayout
				.findViewById(R.id.playList);
		songTitle = (TextView) slidingWindowlayout.findViewById(R.id.songTitle);
		currentPlayList = (ImageButton) slidingWindowlayout
				.findViewById(R.id.currentPlayList);
		btnPlayList.setVisibility(View.INVISIBLE);
		currentPlayList.setVisibility(View.INVISIBLE);
	}

	public void eventSubscriber() {
		currentPlayList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent in = new Intent(activity, PlayListActivity.class);
				activity.startActivity(in);
			}
		});

		btnPlayList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!mediaPlayer.isPlaying()) {

					// Changing button image to pause button
					if (getSelectedSongList().size() > 0) {
						HashMap<String, String> song = getSelectedSong(currentIndex);

						url = song.get("songUrl");
						name = song.get("songName");
						albumName = song.get("albumName");
						if (name != null) {
							songTitle.setText(albumName + " - " + name);
						}
						mediaPlayer.start();
						btnPlayList.setImageResource(R.drawable.btn_stop);
					}
				} else {
					mediaPlayer.pause();
					btnPlayList.setImageResource(R.drawable.btn_start);
				}
			}
		});
	}

	@Override
	public void update(Observable observable, Object message) {
		String data = (String) message;
		if (data.contains("SET_TITLE")) {
			String title = data.split(";")[1];
			songTitle.setText(title);
		} else if (data.contains("CHANGE_PLAYER_BUTTON")) {
             int resourceId = Integer.parseInt(data.split(";")[1]);
             btnPlayList.setImageResource(resourceId);
		}else if (data.contains("CHANGE_PLAYER_VISIBILTY")) {
            int resourceId = Integer.parseInt(data.split(";")[1]);
            btnPlayList.setVisibility(resourceId);
		}else if (data.contains("CHANGE_CURRENTLIST_VISIBILTY")) {
            int resourceId = Integer.parseInt(data.split(";")[1]);
            currentPlayList.setVisibility(resourceId);
		}
	}

	public void setSongTitle(String title) {
		songTitle.setText(title);
	}

	public void setPlayButton(int resourceId) {
		btnPlayList.setImageResource(resourceId);
	}

	public void setPlayButtonVisibilty(int isVisible) {
		btnPlayList.setVisibility(isVisible);
	}

	public void setCurrentPlaylistVisibilty(int isVisible) {
		currentPlayList.setVisibility(isVisible);
	}

}
