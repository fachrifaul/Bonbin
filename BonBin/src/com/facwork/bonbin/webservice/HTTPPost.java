package com.facwork.bonbin.webservice;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class HTTPPost {

	public static HttpResponse doPost(String url, JSONObject c)
			throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("Content-Type", "application/json");
		StringEntity s = new StringEntity(c.toString());
		httppost.setEntity(s);
		HttpResponse response;
		response = httpclient.execute(httppost);
		return response;
	}

}
