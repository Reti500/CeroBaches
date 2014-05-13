package com.geos.gestor.cerobaches;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.geos.gestor.cerobaches.fragments.MainFragment;
import com.geos.gestor.cerobaches.interfaces.MainListener;
import com.geos.gestor.cerobaches.libs.BachesComunicador;
import com.geos.gestor.cerobaches.libs.Comunicador.ResponseListener;
import com.geos.gestor.cerobaches.libs.Datos;
import com.geos.gestor.cerobaches.libs.Files;
import com.geos.gestor.cerobaches.libs.ParseJson;
import com.geos.gestor.cerobaches.libs.SendToServer;
import com.geos.gestor.cerobaches.libs.SendToServer.SendParams;
import com.geos.gestor.cerobaches.libs.SendToServer.SendToServerListener;
import com.geos.gestor.cerobaches.libs.Sessions;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements MainListener  {

	private ProgressDialog dialog;
	private Datos datos;
	private MainFragment main_frag;
	private Sessions s;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.main_fragment, new MainFragment()).commit();
		}
		
		datos = Datos.getInstance();
		s = Sessions.getInstance();
		
		main_frag = (MainFragment)getSupportFragmentManager().
				findFragmentById(R.id.main_fragment);
		main_frag.setListener(this);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		datos.adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		switch(id){
			case R.id.menu_logout:
				final Handler run = new Handler();
				run.post(new Runnable() {
					
					@Override
					public void run() {
						dialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Saliendo ...", true);
						
						logout();
					}
				});
				break;
				
			case R.id.nueva_solicitud:
				goNuevaSolicitud();
				break;
				
			case R.id.actualizar:
				if(Datos.isNetworkAvailable(MainActivity.this)){
					actualizarDatos();
				}
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void logout(){
		SendToServer sendData = SendToServer.getInstance();
		
		sendData.sendByGet(MainActivity.this, null, SendToServer.LOGOUT_URL, 
				new SendToServerListener() {
			
			@Override
			public void success(String val) {
				// TODO Auto-generated method stub
//				try{
//					JSONObject json = new JSONObject(val);
//					String state = json.getString("State");
//					
//					if(state.equals("Logout")){
//						goLogin();
//					}
//				} catch (JSONException e){
//					e.printStackTrace();
//				}
				ParseJson serialize = new ParseJson();
				String[] vals = {"State", "Message", "Data"};
				HashMap<String, String> map = serialize.parse(val, vals);
				
				if(map.get("State").equals(Datos.RESPONSE_OK)){
					Files files = new Files();
					files.getFile("ordenes.txt", files.BACHES_CACHE_DIRECTORY).delete();
					goLogin();
				}
			}
			
			@Override
			public void onfinal() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void error(String msg) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void goLogin(){
		SharedPreferences pref = getSharedPreferences("userPref", Context.MODE_PRIVATE);
		Editor editor = pref.edit();
	
		editor.putBoolean("savedUserData", false);
		editor.putString("username", "");
		editor.putString("password", "");
		editor.putString("name", "");
		editor.commit();
		
		dialog.dismiss();
		
		Intent i = new Intent().setClass(MainActivity.this, LoginActivity.class);
		
		startActivity(i);
		finish();
	}
	
	public void goNuevaSolicitud(){
		Intent i = new Intent().setClass(MainActivity.this, NuevaSolicitudActivity.class);
		startActivity(i);
	}

	@Override
	public void onClickOrden(int position) {
		// TODO Auto-generated method stub
		Intent intent = new Intent().setClass(MainActivity.this, OrdenActivity.class);
		intent.putExtra("position", position);
		
		startActivity(intent);
	}
	
	public void actualizarDatos(){
//		dialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Actualizando datos ...", true);
//		
//		final Handler run = new Handler();
//		run.post(new Runnable() {
//
//			@Override
//			public void run() {
//				uploadData();
//			}
//		});
		if(s.isActive()){
			uploadData();
		}else{
			SharedPreferences preferences = getSharedPreferences("userPref", Context.MODE_PRIVATE);
			String username = preferences.getString("username", "");
			String password = preferences.getString("password", "");
			s.login(MainActivity.this, username, password,
					new SendToServerListener() {
						
						@Override
						public void success(String val) {
							// TODO Auto-generated method stub
							ParseJson serialize = new ParseJson();
							String[] vals = {"State", "Message", "Data"};
							HashMap<String, String> map = serialize.parse(val, vals);
							
							if(map.get("State").equals(Datos.RESPONSE_OK)){
								s.active();
								uploadData();
							}
						}
						
						@Override
						public void onfinal() {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void error(String msg) {
							// TODO Auto-generated method stub
							
						}
					});
		}
	}
	
	public void uploadData(){
		Files files = new Files();
		String json = files.readFile(Datos.REPORTES_INICIO_FILE_NAME, files.BACHES_CACHE_DIRECTORY);
		
		if(json != null){
			SendToServer sendData = SendToServer.getInstance();
			
	//		BachesComunicador sendData = BachesComunicador.getInstance();
			ArrayList<SendParams> params = sendData.createParams(
					new SendParams("myjson", json));
			
			sendData.sendByPost(MainActivity.this, params, SendToServer.JSON_SOLICITUDES_URL,
					new SendToServerListener() {
	//		sendData.cambiarEstatusAll(json, new ResponseListener() {
				
				@Override
				public void success(String val) {
					// TODO Auto-generated method stub
					ParseJson serialize = new ParseJson();
					String[] vals = {"State", "Message", "Data"};
					HashMap<String, String> map = serialize.parse(val, vals);
					
					if(map.get("State").equals(Datos.RESPONSE_OK)){
						Files files = new Files();
						files.getFile(Datos.REPORTES_INICIO_FILE_NAME, files.BACHES_CACHE_DIRECTORY).delete();
						
						datos.adapter.clear();
						main_frag.cargarOrdenesDeTrabajo();
						uploadImages();
					}
				}
				
				@Override
				public void onfinal() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void error(String msg) {
					// TODO Auto-generated method stub
					
				}
			});
		}else{
			uploadImages();
		}
	}
	
	public void uploadImages(){
		Files files = new Files();
		String strjson = files.readFile(Datos.IMAGENES_FILE_NAME, files.BACHES_CACHE_DIRECTORY);
		
		if(strjson != null){
			try{
				JSONObject json = new JSONObject(strjson);
				JSONArray images = json.getJSONArray("images");
				
				if(images.length() > 0){
					ArrayList<JSONObject> imagesjson = new ArrayList<JSONObject>();
					ArrayList<File> imagesFiles = new ArrayList<File>();
					
					for(int i=0; i<images.length(); i++){
						JSONObject img = images.getJSONObject(i);
						File ext_dir = Environment.getExternalStorageDirectory();
						File file = new File(ext_dir.getAbsolutePath() 
								+ img.getString("ImagePath"));
						
						imagesjson.add(img);
						imagesFiles.add(file);
					}
					
					sendImage(imagesjson, imagesFiles, 0);
					
				}
			}catch(JSONException e){
				
			}
		}
	}
	
	public void sendImage(final ArrayList<JSONObject> img_json,
			final ArrayList<File> files, final int pos){
		SendToServer sendData = SendToServer.getInstance();
		
		if(pos < img_json.size()){
			try {
				JSONObject img = img_json.get(pos);
				final File file = files.get(pos);
				
				ArrayList<SendParams> params;
			
				params = sendData.createParams(
						new SendParams("image_name", img.getString("ImageUrl")),
						new SendParams("id_solicitud", img.getString("ImageID")),
						new SendParams("id_estatus", img.getString("ImageEstatus")),
						new SendParams("image", file));
			
				sendData.sendByPost(null, params, SendToServer.UPLOAD_IMAGE_URL,
						new SendToServerListener() {
							
							@Override
							public void success(String val) {
								// TODO Auto-generated method stub
								ParseJson serialize = new ParseJson();
								String[] vals = {"State", "Message", "Data"};
								HashMap<String, String> map = serialize.parse(val, vals);
								
								if(map.get("State").equals(Datos.RESPONSE_OK)){
									file.delete();
								}
								
								int new_pos = pos + 1;
								sendImage(img_json, files, new_pos);
							}
							
							@Override
							public void onfinal() {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void error(String msg) {
								// TODO Auto-generated method stub
								Log.i("SEND IMAGE", "NO :(");
							}
						});
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else{
			Files archivos = new Files();
//			String strjson = archivos.readFile(Datos.IMAGENES_FILE_NAME, archivos.BACHES_CACHE_DIRECTORY);
			archivos.getFile(Datos.IMAGENES_FILE_NAME, archivos.BACHES_CACHE_DIRECTORY).delete();
		}
	}
}
