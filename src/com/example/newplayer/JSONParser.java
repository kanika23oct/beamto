package com.example.newplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;
import java.io.Reader;
import java.nio.charset.Charset;

import android.util.Log;

public class JSONParser {

	static InputStream is = null;
	// static JSONArray jObj = null;
	static String json = "";

	// constructor
	public JSONParser() {

	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public String readJsonFromUrl(String url) throws IOException, JSONException {
		//InputStream is = new URL(url).openStream();
		 HttpClient httpclient = new DefaultHttpClient();
		 HttpPost httppost = new HttpPost(url);
		 HttpResponse response = httpclient.execute(httppost);
		 is =  response .getEntity().getContent();
		
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json.toString();
		} finally {
			is.close();
		}
	}

}