package com.geos.gestor.cerobaches;

import java.io.File;
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
import com.geos.gestor.cerobaches.libs.SendToServer.SendToServerListener;
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
		
		((MainFragment)getSupportFragmentManager().
				findFragmentById(R.id.main_fragment)).setListener(this);
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
				actualizarDatos();
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
		dialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Actualizando datos ...", true);
		
		final Handler run = new Handler();
		run.post(new Runnable() {

			@Override
			public void run() {
				uploadData();
			}
		});
	}
	
	public void uploadData(){
		Files files = new Files();
		BachesComunicador sendData = BachesComunicador.getInstance();
		String json = files.readFile(Datos.REPORTES_INICIO_FILE_NAME, files.BACHES_CACHE_DIRECTORY);
		
		sendData.cambiarEstatusAll(json, new ResponseListener() {
			
			@Override
			public void success(String val) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				uploadImages();
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
	
	public void uploadImages(){
		Files files = new Files();
		String strjson = files.readFile(Datos.IMAGENES_FILE_NAME, files.BACHES_CACHE_DIRECTORY);
		
		try{
			JSONObject json = new JSONObject(strjson);
			JSONArray images = json.getJSONArray("images");
			
			if(images.length() > 0){
				JSONObject img = images.getJSONObject(0);
				
//				new UploadImageS3().execute(img.getString("ImagePath"), img.getString("ImageName"));
				File ext_dir = Environment.getExternalStorageDirectory();
				File file = new File(ext_dir.getAbsolutePath() 
						+ img.getString("ImagePath"));
				
				if(file.exists()){
					Log.i("URI -> ", file.toURI().toString());
//					UploadServer server = new UploadServer(file, "http://www.newstellerapp.com/api/v1/noticias/1");
//					server.start();
				}
				
			}
		}catch(JSONException e){
			
		}
	}
}
