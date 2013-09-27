package us.beamto.newplayer.ui.activites;

import us.beamto.newplayer.R;
import us.beamto.newplayer.common.Utilities;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayerTimerDisplay extends NewMediaPlayerActivity implements
		SeekBar.OnSeekBarChangeListener {
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	private SeekBar songProgressBar;
	LinearLayout timerLayout;
	private Utilities utils;
	private Handler mHandler = new Handler();

	public PlayerTimerDisplay(LinearLayout layout) {
		timerLayout = (LinearLayout) layout.findViewById(R.id.timerDisplay);
		songCurrentDurationLabel = (TextView) timerLayout
				.findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) timerLayout
				.findViewById(R.id.songTotalDurationLabel);
		songProgressBar = (SeekBar) layout.findViewById(R.id.songProgressBar);
		songProgressBar.setOnSeekBarChangeListener(this);
		utils = new Utilities();
	}

	/**
	 * Background Runnable thread
	 * */
	Runnable mUpdateTimeTask = new Runnable() {
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

	public void setProgressBarValues(int start, int max) {
		songProgressBar.setProgress(start);
		songProgressBar.setMax(max);
	}

	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}

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

}
