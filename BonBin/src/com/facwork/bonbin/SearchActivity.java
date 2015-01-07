package com.facwork.bonbin;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.facwork.bonbin.adapter.HewanAdapter;
import com.facwork.bonbin.data.Hewan;
import com.facwork.bonbin.data.Informasi;
import com.facwork.bonbin.database.Constants.Extra;
import com.facwork.bonbin.database.DatabaseHelper;

public class SearchActivity extends Activity implements TextWatcher,
		OnItemClickListener {
	// private EditText search;
	private ListView lv;
	private DatabaseHelper dbHelper;
	private HewanAdapter adapter;
	Context context;

	ArrayList<Hewan> daftarHewan = new ArrayList<Hewan>();
	//ArrayList<Informasi> info = new ArrayList<Informasi>();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		context = this;

		// search = (EditText) findViewById(R.id.search);
		lv = (ListView) findViewById(R.id.lv_data);
		lv.setEmptyView(findViewById(R.id.empty));

		// getSupportLoaderManager().initLoader(0, null, this);

		dbHelper = DatabaseHelper.getInstance(this);
		setData();

		lv.setOnItemClickListener(this);
		lv.setTextFilterEnabled(true);

	}

	@Override
	public void afterTextChanged(Editable arg0) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
		adapter.getFilter().filter(s.toString());

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		// TODO Auto-generated method stub

		Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
		intent.putExtra(Extra.ID, adapter.getItem(position).getID());
		intent.putExtra(Extra.TITLE, adapter.getItem(position).getTitle());
		intent.putExtra(Extra.LAT, adapter.getItem(position).getLat());
		intent.putExtra(Extra.LNG, adapter.getItem(position).getLng());
		intent.putExtra(Extra.IMAGES, adapter.getItem(position).getGambar());
		intent.putExtra(Extra.DESCRIPTION, adapter.getItem(position)
				.getDeskripsi());
		intent.putExtra(Extra.JARAK, adapter.getItem(position).getJarak());

		startActivity(intent);
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
	}

	private void setData() {
		// search.addTextChangedListener(this);
		//info = (ArrayList<Informasi>) dbHelper.getInformasi();
		daftarHewan = dbHelper.getAllHewan();
		// System.out.println(dbHelper.getHewan("Singa").toString());

		adapter = new HewanAdapter(getApplicationContext(),
				R.layout.item_list_image, daftarHewan);
		lv.setAdapter(adapter);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.item_search)
				.getActionView();

		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(true);

		SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {
				// this is your adapter that will be filtered
				adapter.getFilter().filter(newText);
				System.out.println("on text chnge text: " + newText);
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				// this is your adapter that will be filtered
				adapter.getFilter().filter(query);
				System.out.println("on query submit: " + query);
				return true;
			}
		};
		searchView.setOnQueryTextListener(textChangeListener);

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.item_maps) {
			startActivity(new Intent(SearchActivity.this, PetaActivity.class));
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
		}
		if (item.getItemId() == R.id.action_price) {
			
		
			
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.layout_dialog);
			
			TextView text = (TextView) dialog.findViewById(R.id.informasi);
			//text.setText(info.get(0).getinfo());
			
			Button ok = (Button) dialog.findViewById(R.id.ok);
			ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			dialog.show();

		}

		return super.onOptionsItemSelected(item);

	}
}
