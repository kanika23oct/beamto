package us.beamto.newplayer.ui.activites;

import java.util.Observable;
import java.util.Observer;

import us.beamto.newplayer.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class ThumbnailImage implements Observer {
	ImageView artistImage;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	public static String UPLOAD_IMAGE = "upload_image";

	@SuppressWarnings("deprecation")
	public ThumbnailImage(LinearLayout layout, float density, int screenHeight, int screenWeight) {
		Subscriber.getInstance().addObserver(this);
		LinearLayout imageLayout = (LinearLayout) layout
				.findViewById(R.id.songThumbnailLayout);
		artistImage = (ImageView) imageLayout.findViewById(R.id.songThumbnail);
		imageLoader = ImageLoader.getInstance();
			options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher).cacheOnDisc()
				.cacheInMemory()
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();
			Context context = NewMediaPlayerActivity.getActivity();
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context.getApplicationContext()).defaultDisplayImageOptions(options).build();
			imageLoader.init(config);
		
			 artistImage.getLayoutParams().height = (int)(screenHeight/2.46);
             artistImage.getLayoutParams().width = (int)(screenWeight/1.5);
	
	}

	public void eventSubscriber(String imageURL) {
		imageLoader.displayImage(imageURL, artistImage, options);
	}

	public void update(Observable observer, Object message) {
		String imageURL = (String) message;

		if (imageURL.contains("UPLOAD_IMAGE")) {
			String url = imageURL.split(";")[1];
			this.eventSubscriber(url);

		}
	}

}
