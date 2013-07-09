package com.example.newplayer;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadAlbumPage implements Runnable {
	String url = "";
	String pageNumber = "";

	public LoadAlbumPage(String url, String pageNumber) {
		this.url = url;
		this.pageNumber = pageNumber;
	}

	@Override
	public void run() {
		ArrayList<HashMap<String, String>> list;
		if (pageNumber.equals("1")) {
		list = new AlbumList().songList(url,
				VariablesList.JSON_PAGE_OBJECT, pageNumber);
		  
			NewMediaPlayer.setSongList(list);
		} else {
			list = new AlbumList().songList(url,
					VariablesList.JSON_PAGE_OBJECT, pageNumber);
			
			NewMediaPlayer.setNewList(new AlbumList().songList(url,
					VariablesList.JSON_PAGE_OBJECT, pageNumber));
			NewMediaPlayer.appendToAdaptor();
		}
		if(list.size() ==0){
			System.out.println("***** Albums Finish");
			NewMediaPlayer.setLastPage(true);
		}
		synchronized (this) {
			this.notifyAll();
		}

	}

}
