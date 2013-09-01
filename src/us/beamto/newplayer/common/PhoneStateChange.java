package us.beamto.newplayer.common;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import us.beamto.newplayer.ui.activites.NewMediaPlayerActivity;

public class PhoneStateChange extends PhoneStateListener{
	private boolean pausedForPhoneCall = false;
	
	 @Override
	    public void onCallStateChanged(int state, String incomingNumber) {

	        switch (state) {
	            case TelephonyManager.CALL_STATE_IDLE:
	            	NewMediaPlayerActivity.mediaPlayer.start();
	                return;
	            case TelephonyManager.CALL_STATE_OFFHOOK: 
	            	NewMediaPlayerActivity.mediaPlayer.pause();   
	                return;
	            case TelephonyManager.CALL_STATE_RINGING: 
	            	NewMediaPlayerActivity.mediaPlayer.pause();     
	                return;
	        }
	    }
	 
}
