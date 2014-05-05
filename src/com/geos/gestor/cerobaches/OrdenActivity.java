package com.geos.gestor.cerobaches;

import com.geos.gestor.cerobaches.fragments.OrdenFragment;
import com.geos.gestor.cerobaches.interfaces.OrdenListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class OrdenActivity extends FragmentActivity implements OrdenListener {

	private OrdenFragment frag_orden;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orden);

		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		int orden_position = getIntent().getExtras().getInt("position");
		
		frag_orden = (OrdenFragment) getSupportFragmentManager().findFragmentById(R.id.orden_fragment);
		frag_orden.setOrdenPosition(orden_position);
		frag_orden.setListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.orden, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClickReporte(int position) {
		// TODO Auto-generated method stub
		Intent i = new Intent().setClass(OrdenActivity.this, IniciarReporteActivity.class);
		
		i.putExtra("orden_position", position);
		startActivity(i);
	}

}
