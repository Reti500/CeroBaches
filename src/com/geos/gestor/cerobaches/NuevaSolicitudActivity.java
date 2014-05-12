package com.geos.gestor.cerobaches;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.geos.gestor.cerobaches.fragments.NuevaSolicitudFragment;
import com.geos.gestor.cerobaches.interfaces.NuevaSolicitudListener;
import com.geos.gestor.cerobaches.libs.Datos;
import com.geos.gestor.cerobaches.libs.Files;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class NuevaSolicitudActivity extends FragmentActivity implements NuevaSolicitudListener{

	private Files files;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nueva_solicitud);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		files = new Files();
		
		((NuevaSolicitudFragment) getSupportFragmentManager()
				.findFragmentById(R.id.nueva_solicitud_fragment)).setListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nueva_solicitud, menu);
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
	public void onClickCrearSolicitud(String image_name, String image_url) {
		// TODO Auto-generated method stub
		
		createAlert("Nueva Solicitud", "Se creo la solicitud!!");
		
		if(Datos.isNetworkAvailable(NuevaSolicitudActivity.this)){
			
		}else{
			addJsonImage(image_name, files.BACHES_PHOTOS_DIRECTORY+image_name, image_url);
		}
	}
	
	public void addJsonImage(String image_name, String path, String image_url){
		String jsonImage = "";
		JSONObject jsonFinal = new JSONObject();
		JSONArray arrayFinal = new JSONArray();
		JSONObject image_json = new JSONObject();
		
		if(files.existsFile(Datos.IMAGENES_FILE_NAME, files.BACHES_CACHE_DIRECTORY)){
			jsonImage = files.readFile(Datos.IMAGENES_FILE_NAME, files.BACHES_CACHE_DIRECTORY);
			
			try{
				JSONObject json = new JSONObject(jsonImage);
				arrayFinal = json.getJSONArray("images");
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		
		try{
			image_json.put("ImageName", image_name);
			image_json.put("ImageUrl", image_url);
			image_json.put("ImagePath", path);
			
			arrayFinal.put(image_json);
			jsonFinal.put("images", arrayFinal);
			
			files.saveFile(Datos.IMAGENES_FILE_NAME, jsonFinal.toString(), files.BACHES_CACHE_DIRECTORY);
		}catch(JSONException e){
			e.printStackTrace();
		}
	}

	public void createAlert(String title, String message){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				NuevaSolicitudActivity.this);
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				NuevaSolicitudActivity.this.finish();
			}
		});
		
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
}
