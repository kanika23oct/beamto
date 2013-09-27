package us.beamto.newplayer.common;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import us.beamto.newplayer.ui.activites.NewMediaPlayerActivity;

public class PhoneStateChange extends PhoneStateListener{
	private static boolean pausedForPhoneCall = false;
	
	 @Override
	    public void onCallStateChanged(int state, String incomingNumber) {

	        switch (state) {
	            case TelephonyManager.CALL_STATE_IDLE:
	              if(pausedForPhoneCall)	
	            	{
	            	  NewMediaPlayerActivity.mediaPlayer.start();
	            	  pausedForPhoneCall = false;
	            	}
	                return;
	            case TelephonyManager.CALL_STATE_OFFHOOK: 
	            	NewMediaPlayerActivity.mediaPlayer.pause();   
	                return;
	            case TelephonyManager.CALL_STATE_RINGING: 
	            	pausedForPhoneCall = true;
	            	NewMediaPlayerActivity.mediaPlayer.pause();     
	                return;
	        }
	    }
	 
}
