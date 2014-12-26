package com.facwork.bonbin;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facwork.bonbin.database.Constants.Extra;
import com.facwork.bonbin.utils.Route;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class DetailActivity extends Activity {
	String id, title, lng, lat, gambar, deskripsi;
	double jarak;
	TextView titleHewan, latHewan, lngHewan, descriptionHewan, jarakHewan;
	ImageView imageurlV;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	Button goPeta;
	Context ctx;
	Route route;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_pager_image);
		
		ctx = this;
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				ctx).threadPriority(Thread.NORM_PRIORITY - 2)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);

		final ProgressBar spinner = (ProgressBar) findViewById(R.id.loading);
		//final ScrollView viewimagepath = (ScrollView) findViewById(R.id.scrollView1);

		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		Bundle bundle = getIntent().getExtras();
		id = bundle.getString(Extra.ID);
		title = bundle.getString(Extra.TITLE);
		lng = bundle.getString(Extra.LNG);
		lat = bundle.getString(Extra.LAT);
		gambar = bundle.getString(Extra.IMAGES);
		deskripsi = bundle.getString(Extra.DESCRIPTION);
		jarak = bundle.getDouble(Extra.JARAK);

		route = new Route(ctx, "", lat, lng);
		goPeta = (Button) findViewById(R.id.go_peta);
		goPeta.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				route.goToMap();
			}
		});

		// Im = (ImageView) findViewById(R.id.iv_detail);
		titleHewan = (TextView) findViewById(R.id.txt_title);
		latHewan = (TextView) findViewById(R.id.txt_lat);
		lngHewan = (TextView) findViewById(R.id.txt_lng);
		descriptionHewan = (TextView) findViewById(R.id.txt_description);
		jarakHewan = (TextView) findViewById(R.id.txt_jarak);
		imageurlV = (ImageView) findViewById(R.id.picture);
		// Im.setImageResource(msg_im);
		titleHewan.setText(title);
		latHewan.setText(lat);
		lngHewan.setText(lng);
		descriptionHewan.setText(deskripsi);
		jarakHewan.setText("" + jarak + " Km");

		Typeface faceTitle = Typeface.createFromAsset(getAssets(),
				"fonts/Lato-Bold.ttf");
		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/Lato-Regular.ttf");
		titleHewan.setTypeface(faceTitle);
		latHewan.setTypeface(face);
		lngHewan.setTypeface(face);
		descriptionHewan.setTypeface(face);
		jarakHewan.setTypeface(face);

		imageLoader.displayImage(gambar, imageurlV, options,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						spinner.setVisibility(View.VISIBLE);
						imageurlV.setVisibility(View.GONE);

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						String message = null;
						switch (failReason.getType()) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case DECODING_ERROR:
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
						}
						Toast.makeText(getApplicationContext(), message,
								Toast.LENGTH_SHORT).show();

						spinner.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						spinner.setVisibility(View.GONE);
						imageurlV.setVisibility(View.VISIBLE);

					}
				});

		View customNav = LayoutInflater.from(this).inflate(R.layout.back, null);

		ImageView backhome = (ImageView) customNav.findViewById(R.id.back);
		backhome.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
			}
		});

		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(
				Color.TRANSPARENT));
		actionBar.setCustomView(customNav);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

	}

	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
	}

	protected void onDestroy() {
		super.onDestroy();
		imageLoader.stop();
	}

	protected void onPause() {
		super.onPause();
		imageLoader.pause();
	}

	protected void onResume() {
		super.onResume();
		imageLoader.resume();
	}
}
