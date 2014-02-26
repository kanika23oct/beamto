package us.beamto.newplayer.ui.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import us.beamto.newplayer.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import us.beamto.newplayer.common.VariablesList;

public class VideoAdaptor extends BaseAdapter{
	private Context context;
	private ArrayList<HashMap<String, String>> videoPlayLists = new ArrayList<HashMap<String, String>> ();
	private LayoutInflater inflater;
	private static Resources resources;
	ImageView imageView;
	DisplayImageOptions options;
	ImageLoader imageLoader;
    String imageURL = "";
	
	public VideoAdaptor(Context context,ArrayList<HashMap<String, String>> videoPlayList)
	{
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.videoPlayLists = videoPlayList;
		resources = context.getResources();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher).cacheOnDisc()
				.cacheInMemory().build();
	}

	@Override
	public int getCount() {
		return videoPlayLists.size();
	}

	@Override
	public Object getItem(int position) {
		return videoPlayLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		HashMap<String,String> videoPlayList = videoPlayLists.get(position);
		View v = null;
		if(convertView != null)
			v = convertView;
		else
			v = inflater.inflate(R.layout.li_clickable_list, parent, false);
		
		ImageView imageView = (ImageView)v.findViewById(R.id.icon_image);
		imageURL = videoPlayList.get(VariablesList.TAG_ALBUM_IMAGE);
		imageLoader.displayImage(imageURL, imageView,options);
		
		
		return v;
	}

}
