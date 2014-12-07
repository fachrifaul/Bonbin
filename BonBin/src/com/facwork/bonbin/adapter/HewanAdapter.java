package com.facwork.bonbin.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facwork.bonbin.R;
import com.facwork.bonbin.data.Hewan;
import com.facwork.bonbin.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class HewanAdapter extends ArrayAdapter<Hewan> {
	Context context;
	boolean isMixareInstalled;
	private ArrayList<Hewan> arrayList;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	private static class ViewHolder {
		TextView text_judul, text_lat, text_long, text_jarak;
		ImageView image;
		// ImageView maps, ar;
	}

	public HewanAdapter(Context context, int textViewResourceId,
			ArrayList<Hewan> items) {
		super(context, textViewResourceId, items);
		this.arrayList = items;
		this.context = context;
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	public ArrayList<Hewan> getArrayList() {
		return arrayList;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.load)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(90)).build();

		ViewHolder holder = null;
		final Hewan rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_list_image, null);
			holder = new ViewHolder();
			holder.text_judul = (TextView) convertView
					.findViewById(R.id.text_bonbin);
			holder.text_lat = (TextView) convertView
					.findViewById(R.id.text_lat);
			holder.text_long = (TextView) convertView
					.findViewById(R.id.text_long);
			holder.text_jarak = (TextView) convertView
					.findViewById(R.id.txt_jarak);
			holder.image = (ImageView) convertView.findViewById(R.id.icon_list);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.text_judul.setText(rowItem.getTitle());
		holder.text_lat.setText(rowItem.getLat());
		holder.text_long.setText(rowItem.getLng());
		double jarak = Utils.RoundDecimal(rowItem.getJarak(), 2);
		holder.text_jarak.setText("" + jarak + " Km");

		imageLoader.displayImage(rowItem.getGambar(), holder.image, options);

		return convertView;
	}

}
