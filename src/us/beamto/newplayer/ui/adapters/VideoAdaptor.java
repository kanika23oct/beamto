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
import android.widget.TextView;

import us.beamto.newplayer.common.VariablesList;
import us.beamto.newplayer.ui.activites.NewMediaPlayerActivity;

public class VideoAdaptor extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String, String>> videoPlayLists = new ArrayList<HashMap<String, String>>();
	private LayoutInflater inflater;
	private static Resources resources;
	ImageView imageView;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	String imageURL = "";

	public VideoAdaptor(Context context,
			ArrayList<HashMap<String, String>> videoPlayList) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.videoPlayLists = videoPlayList;
		resources = context.getResources();
		imageLoader = NewMediaPlayerActivity.getActivity().getImageLoader();
	}

	public void addToList(HashMap<String, String> video) {
		System.out.println("%%%%% Added a video to adaptor %%%%");
		videoPlayLists.add(video);
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
		System.out.println("***** Playing the video *******");
		HashMap<String, String> videoPlayList = videoPlayLists.get(position);
		View v = null;
		if (convertView != null)
			v = convertView;
		else
			v = inflater.inflate(R.layout.li_clickable_list, parent, false);

		ImageView imageView = (ImageView) v.findViewById(R.id.icon_image);
		imageURL = videoPlayList.get(VariablesList.TAG_ALBUM_IMAGE);
		imageLoader.displayImage(imageURL, imageView, options);

		TextView textView = (TextView) v.findViewById(R.id.icon_text);
		textView.setText(videoPlayList.get(VariablesList.TAG_NAME));

		final TextView itemArtistName = (TextView) v
				.findViewById(R.id.album_artist);
		itemArtistName
				.setText(videoPlayList.get(VariablesList.TAG_USERNAME));

		final TextView itemTotalSongs = (TextView) v
				.findViewById(R.id.total_songs);
		itemTotalSongs.setText(videoPlayList.get(VariablesList.NUMBER_OF_SONGS)
				+ " songs");
		return v;
	}

}
