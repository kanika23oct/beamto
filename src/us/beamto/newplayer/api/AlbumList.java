package us.beamto.newplayer.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.*;

import us.beamto.newplayer.common.VariablesList;
import us.beamto.newplayer.ui.activites.NewMediaPlayerActivity;

public class AlbumList {

	String albumUrl;

	public AlbumList(String albumUrl) {
		this.albumUrl = albumUrl;
	}

	private ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();

	public ArrayList<HashMap<String, String>> songList(String url,
			String parameter, String value) {
		try {
			JSONParser jParser = new JSONParser();
			String jsonString = jParser.readJsonFromUrl(url.toString(),
					parameter, value);
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray songs = jsonObject
					.getJSONArray(VariablesList.JSON_ALBUM_OBJECT);
			System.out.println("**** Size: " + songs.length());
			for (int i = 0; i < songs.length(); i++) {
				HashMap<String, String> album = new HashMap<String, String>();
				JSONObject albumDetails = songs.getJSONObject(i);
				String id = albumDetails.getString(VariablesList.TAG_ID);
				String slug = albumDetails.getString(VariablesList.TAG_SLUG);
				String name = albumDetails.getString(VariablesList.TAG_NAME);
				String labelName = albumDetails
						.getString(VariablesList.TAG_LABEL_NAME);
				String albumImage = albumDetails
						.getString(VariablesList.TAG_ALBUM_IMAGE);
				String artistName = albumDetails
						.getString(VariablesList.TAG_ARTIST_NAME);

				String songJsonString = jParser.readJsonFromUrl(
						albumUrl.toString(),
						VariablesList.ALBUM_JSON_PARAMETER, slug);
					JSONObject songjsonObject = new JSONObject(songJsonString);
						NewMediaPlayerActivity
							.getActivity()
							.setAlbumJsonArray(
									id + ";" + name,
									songjsonObject
											.getJSONArray(VariablesList.JSON_SONG_OBJECT));
					album.put(VariablesList.TAG_ID, id);
					album.put(VariablesList.TAG_SLUG, slug);
					album.put(VariablesList.TAG_NAME, name);
					album.put(VariablesList.TAG_LABEL_NAME, labelName);
					album.put(VariablesList.TAG_ALBUM_IMAGE, albumImage);
					album.put(VariablesList.TAG_ARTIST_NAME, artistName);
					String size = ""
							+ songjsonObject.getJSONArray(
									VariablesList.JSON_SONG_OBJECT).length();
					album.put(VariablesList.NUMBER_OF_SONGS, size);
					albumList.add(album);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return albumList;
	}

	public static Bitmap LoadImagetoGridView(String url) {
		String image = url;
		Bitmap mIcon11 = null;
		InputStream in;
		try {
			in = new java.net.URL(image).openStream();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;

			mIcon11 = BitmapFactory.decodeStream(in, null, options);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mIcon11;
	}

}
