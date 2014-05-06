package com.geos.gestor.cerobaches;

import org.json.JSONException;
import org.json.JSONObject;

import com.geos.gestor.cerobaches.fragments.MainFragment;
import com.geos.gestor.cerobaches.interfaces.MainListener;
import com.geos.gestor.cerobaches.libs.BachesComunicador;
import com.geos.gestor.cerobaches.libs.Comunicador.ResponseListener;
import com.geos.gestor.cerobaches.libs.Datos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements MainListener  {

	private ProgressDialog dialog;
	private Datos datos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void logout(){
		BachesComunicador sendData = BachesComunicador.getInstance();
		
		sendData.logout(new ResponseListener() {
			
			public void success(String val) {
				try{
					JSONObject json = new JSONObject(val);
					String state = json.getString("State");
					
					if(state.equals("Logout")){
						goLogin();
					}
				} catch (JSONException e){
					e.printStackTrace();
				}
			}
			
			public void onfinal() {
				// TODO Auto-generated method stub
				
			}
			
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

	@Override
	public void onClickOrden(int position) {
		// TODO Auto-generated method stub
		Intent intent = new Intent().setClass(MainActivity.this, OrdenActivity.class);
		intent.putExtra("position", position);
		
		startActivity(intent);
	}

}
