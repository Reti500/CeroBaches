package com.geos.gestor.cerobaches;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.geos.gestor.cerobaches.fragments.IniciarReporteFragment;
import com.geos.gestor.cerobaches.interfaces.IniciarReporteListener;
import com.geos.gestor.cerobaches.libs.CreateOrdenJson;
import com.geos.gestor.cerobaches.libs.Datos;
import com.geos.gestor.cerobaches.libs.Files;
import com.geos.gestor.cerobaches.libs.Orden;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class IniciarReporteActivity extends FragmentActivity implements IniciarReporteListener {
	
	private int orden_position;
	private Files files;
	private Datos datos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_iniciar_reporte);

		files = new Files();
		datos = Datos.getInstance();
		
		orden_position = getIntent().getExtras().getInt("orden_position");
		
		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		((IniciarReporteFragment) getSupportFragmentManager().
				findFragmentById(R.id.iniciar_reporte_fragment)).setListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.iniciar_reporte, menu);
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
	public void onClickIniciarReporte(String new_status, String coment, Bitmap img) {
		// TODO Auto-generated method stub
		
		Orden or = datos.adapter.getOrden(orden_position);
		String image_name = datos.getOrdenName(or, Datos.TIPO_IMAGEN_INICIO, "jpg");
		String image_url = datos.createOrdenName(or, Datos.TIPO_IMAGEN_INICIO, "jpg");
		String ordenId = or.getIdSolicitud();
		
		files.saveImage(image_name, files.BACHES_PHOTOS_DIRECTORY, img);
		
		or.setEstatus("Inicio de labores");
		or.setEstatusId(new_status);
		
		addJsonReporte(ordenId, new_status, coment);
		addJsonImage(image_name, files.BACHES_PHOTOS_DIRECTORY+image_name, image_url);
		
		String ordenJson = CreateOrdenJson.createFromAdapter(datos.adapter);
		files.saveFile("ordenes.txt", ordenJson, files.BACHES_CACHE_DIRECTORY);
		
		createAlert("Reporte", "Se ha iniciado el reporte!!");
	}
	
	public void addJsonReporte(String ordenId, String status, String coment){
		String jsonReportes = "";
		JSONObject jsonFinal = new JSONObject();
		JSONArray arrayFinal = new JSONArray();
		JSONObject reporte_json = new JSONObject();
		
		if(files.existsFile(Datos.REPORTES_INICIO_FILE_NAME, files.BACHES_CACHE_DIRECTORY)){
			jsonReportes = files.readFile(Datos.REPORTES_INICIO_FILE_NAME, files.BACHES_CACHE_DIRECTORY);
			
			try{
				JSONObject json = new JSONObject(jsonReportes);
				arrayFinal = json.getJSONArray("reportes");
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		
		try{
			reporte_json.put("id", ordenId);
			reporte_json.put("idEstatus", status);
			reporte_json.put("comentario", coment);
			
			arrayFinal.put(reporte_json);
			jsonFinal.put("reportes", arrayFinal);
			
			files.saveFile(Datos.REPORTES_INICIO_FILE_NAME, jsonFinal.toString(), files.BACHES_CACHE_DIRECTORY);
		}catch(JSONException e){
			e.printStackTrace();
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
				IniciarReporteActivity.this);
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				IniciarReporteActivity.this.finish();
			}
		});
		
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
}
