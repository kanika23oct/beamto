package us.beamto.newplayer.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import us.beamto.newplayer.R;
import us.beamto.newplayer.ui.activites.NewMediaPlayerActivity;

public class SampleListFragment extends ListFragment {
	Context context;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		super.onActivityCreated(savedInstanceState);
		String[] birds = getResources().getStringArray(R.array.planets_array);
		ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1, birds);
		setListAdapter(colorAdapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		context = (Context) NewMediaPlayerActivity.getActivity();
		switch ((position+1)) {
		case 1:
			Intent in = new Intent(context, NewMediaPlayerActivity.class);
			in.putExtra("Album_Url", "" + (position+1));
			startActivity(in);
			break;

		case 2:
			Intent in1 = new Intent(context, NewMediaPlayerActivity.class);
			in1.putExtra("Album_Url", "" + (position+1));
			startActivity(in1);
			break;

		default:
			Intent in2 = new Intent(context, NewMediaPlayerActivity.class);
			in2.putExtra("Album_Url", "" + 1);
			startActivity(in2);
			break;
		}
	}

	private class SampleItem {
		public String tag;
		public int iconRes;

		public SampleItem(String tag, int iconRes) {
			this.tag = tag;
			this.iconRes = iconRes;
		}
	}

}