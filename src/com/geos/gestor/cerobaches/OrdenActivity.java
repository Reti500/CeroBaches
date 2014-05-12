package com.geos.gestor.cerobaches;

import com.geos.gestor.cerobaches.fragments.OrdenFragment;
import com.geos.gestor.cerobaches.interfaces.OrdenListener;
import com.geos.gestor.cerobaches.libs.Datos;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class OrdenActivity extends FragmentActivity implements OrdenListener {

	private OrdenFragment frag_orden;
	private int orden_position;
	private Datos datos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orden);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		datos = Datos.getInstance();
		
		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		orden_position = getIntent().getExtras().getInt("position");
		
		frag_orden = (OrdenFragment) getSupportFragmentManager().findFragmentById(R.id.orden_fragment);
		frag_orden.setOrdenPosition(orden_position);
		frag_orden.setListener(this);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		frag_orden.setOrdenPosition(orden_position);
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
	public void onClickReporte() {
		// TODO Auto-generated method stub
		int estatus = Integer.parseInt(datos.adapter.getOrden(orden_position).getEstatusId());
		
		if(estatus == 7){
			goIniciarRepote();
		}else if(estatus == 8){
			goConcluirReposte();
		}else{
			Toast.makeText(this, "Reporte finalizado", Toast.LENGTH_SHORT).show();
		}
	}

	public void goIniciarRepote(){
		Intent i = new Intent().setClass(OrdenActivity.this, IniciarReporteActivity.class);
		i.putExtra("orden_position", orden_position);
		startActivity(i);
	}
	
	public void goConcluirReposte(){
		Intent i = new Intent().setClass(OrdenActivity.this, ConcluirReporteActivity.class);
		i.putExtra("orden_position", orden_position);
		startActivity(i);
	}
}
