package us.beamto.newplayer.ui.activites;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import us.beamto.newplayer.R;


import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayerFooterLayout extends  NewMediaPlayerActivity implements Observer {

	private ImageButton btnPlay;
	private ImageButton btnForward;
	private ImageButton btnBackward;
	private ImageButton btnPrevious;
	private ImageButton btnNext;
	private LinearLayout footerLayout;
	
	public PlayerFooterLayout(LinearLayout layout){
		Subscriber.getInstance().addObserver(this);
		footerLayout = (LinearLayout) layout.findViewById(R.id.player_footer_bg);
		btnPlay = (ImageButton) footerLayout.findViewById(R.id.btnPlay);
		btnForward = (ImageButton) footerLayout.findViewById(R.id.btnForward);
		btnBackward = (ImageButton) footerLayout.findViewById(R.id.btnBackward);
		btnPrevious = (ImageButton) footerLayout.findViewById(R.id.btnPrevious);
		btnNext = (ImageButton) footerLayout.findViewById(R.id.btnNext);
	}
	
	public void eventSubscriber() {

		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (getSelectedSongList().size() > 1) {
					if (currentIndex == (getSelectedSongList().size() - 1)) {
						currentIndex = 0;
					} else {
						currentIndex = currentIndex + 1;
					}
					HashMap<String, String> song = getSelectedSongList().get(currentIndex);
					url = song.get("songUrl");
					name = song.get("songName");
					albumName = song.get("albumName");
					if (name != null) {
						Subscriber.getInstance().message("SET_TITLE;"+albumName + " - " + name);
					}
					playSong(url);

				}
			}
		});

		btnPrevious.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (getSelectedSongList().size() > 1) {
					if (currentIndex == 0)
						currentIndex = getSelectedSongList().size() - 1;
					else
						currentIndex = currentIndex - 1;
					HashMap<String, String> song = getSelectedSongList()
							.get(currentIndex);
					url = song.get("songUrl");
					name = song.get("songName");
					albumName = song.get("albumName");
					if (name != null) {
						Subscriber.getInstance().message("SET_TITLE;"+albumName + " - " + name);
					}
					playSong(url);

				}
			}
		});

		btnBackward.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (getSelectedSongList().size() > 1) {
					currentIndex = 0;
					HashMap<String, String> song = getSelectedSongList().get(0);
					url = song.get("songUrl");
					name = song.get("songName");
					albumName = song.get("albumName");
					if (name != null) {
						Subscriber.getInstance().message("SET_TITLE;"+albumName + " - " + name);
					}
					playSong(url);

				}
			}
		});
		
		btnForward.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (getSelectedSongList().size() > 1) {
					HashMap<String, String> song = getSelectedSongList()
							.get(getSelectedSongList().size() - 1);
					currentIndex = getSelectedSongList().size() - 1;

					url = song.get("songUrl");
					name = song.get("songName");
					albumName = song.get("albumName");
					if (name != null) {
						Subscriber.getInstance().message("SET_TITLE;"+albumName + " - " + name);
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
					if (getSelectedSongList().size() > 0) {
						HashMap<String, String> song = getSelectedSongList()
								.get(currentIndex);

						url = song.get("songUrl");
						name = song.get("songName");
						albumName = song.get("albumName");
						if (name != null) {
							Subscriber.getInstance().message("SET_TITLE;"+albumName + " - " + name);
						}
						btnPlay.setImageResource(R.drawable.btn_pause);
						mediaPlayer.start();
					}
				} else {
					mediaPlayer.pause();
					btnPlay.setImageResource(R.drawable.btn_play);
					// btnPlayList.setImageResource(R.drawable.btn_start);
				}
			}
		});
		
	}
	
	public void setPlayButton(int resourceId)
	{
		btnPlay.setImageResource(resourceId);	
	}

	@Override
	public void update(Observable observable, Object message) {
		String data = (String) message;
		if(data.contains("CHANGE_PLAY_BUTTON")){
		//	System.out.println("*** "+data.split(";")[1]);
			int resourceId = Integer.parseInt(data.split(";")[1]);
			this.setPlayButton(resourceId);	
		}
		
	}
	
}
