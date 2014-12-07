package com.facwork.bonbin.webservice;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facwork.bonbin.data.HewanModel;
import com.facwork.bonbin.database.HewanController;
import com.facwork.bonbin.utils.Utils;

import android.content.Context;
import android.util.Log;

public class GetAllHewan {

	private static final String SERVER_URL = Utils.URL + "getbonbin.php";
	Context context;

	public GetAllHewan(Context mContext) {
		this.context = mContext;
	}

	public boolean getAllPoi() {

		boolean result = false;
		JSONObject j = new JSONObject();
		HewanController rc = new HewanController(context);
		try {
			HttpResponse re = HTTPPost.doPost(SERVER_URL, j);
			String temp = EntityUtils.toString(re.getEntity());

			JSONArray jArr = new JSONArray(temp);

			int counter = jArr.length();

			if (counter != 0) {
				result = true;
				rc.removeall();
				Log.i("coba", "hapus databases");
				for (int i = 0; i < counter; i++) {
					JSONObject jo = jArr.getJSONObject(i);

					HewanModel rm = new HewanModel();
					rm.setId(jo.getInt("id"));
					rm.setName(jo.getString("title"));
					rm.setLat(jo.getString("lat"));
					rm.setLon(jo.getString("lng"));

					rm.setDesc(jo.getString("descr").toString());

					// rm.setImgUrl(Utils.URL+jo.getString("img_url"));
					rm.setImgUrl(Utils.URL + jo.getString("img"));

					rc.insert(rm);

					Log.i("coba", "Yang di ambil: Nama : " + rm.getName());

				}
			} else {
				result = false;
			}

		} catch (ClientProtocolException e) {
			Log.e("request error", e.getMessage());
		} catch (IOException e) {
			Log.e("requset error", e.getMessage());
		} catch (JSONException e) {
			Log.e("json error", e.getMessage());
		}
		return result;

	}
}
