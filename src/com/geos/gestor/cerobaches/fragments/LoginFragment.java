package com.geos.gestor.cerobaches.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.geos.gestor.cerobaches.R;
import com.geos.gestor.cerobaches.interfaces.LoginListener;
import com.geos.gestor.cerobaches.libs.BachesComunicador;
import com.geos.gestor.cerobaches.libs.Comunicador.ResponseListener;

public class LoginFragment extends Fragment {
	// Context
	private Context context;
	
	// UI
	private EditText username;
	private EditText password;
	private Button login;
	
	// ProgressDialog
	private ProgressDialog dialog;
	
	// Listener
	private LoginListener listener;
	
	public LoginFragment(){
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_login,
				container, false);
		
		context = rootView.getContext();
		
		username = (EditText) rootView.findViewById(R.id.login_edit_user_name);
		password = (EditText) rootView.findViewById(R.id.login_edit_password);
		login = (Button) rootView.findViewById(R.id.login_button_login);
		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				dialog = ProgressDialog.show(context, "Please wait ...", "Iniciando sesion", true);
				
				final Handler run = new Handler();
				run.post(new Runnable() {

					@Override
					public void run() {
						login();
					}
				});
			}
		});
		
		return rootView;
	}
		
	public void login(){
		BachesComunicador sendData = BachesComunicador.getInstance();
		
		sendData.login(username.getText().toString(), 
				password.getText().toString(), new ResponseListener() {
					
			@Override
			public void success(String val) {
				// TODO Auto-generated method stub
				try{
					JSONObject json = new JSONObject(val);
					String state = json.getString("State");
					String name = json.isNull("UserName") != true ? json.getString("UserName") : "";
					
					if(state.equals("Logged")){
						SharedPreferences preferences = context.getSharedPreferences("userPref", Context.MODE_PRIVATE);
						Editor editor = preferences.edit();
						
						editor.putBoolean("savedUserData", true);
						editor.putString("username", username.getText().toString());
						editor.putString("password", password.getText().toString());
						editor.putString("name", name);
						editor.commit();
						
						if(listener != null){
							listener.goMain();
						}
					}else{
						Toast.makeText(context, "Error en el usuario o contraseña", Toast.LENGTH_LONG).show();
						password.setText("");
					}
				} catch (JSONException e){
					e.printStackTrace();
				}
				dialog.dismiss();
				
				Log.i("LOGIN", "Sesion creada!");
			}
					
			@Override
			public void onfinal() {
				// TODO Auto-generated method stub
				
			}
					
			@Override
			public void error(String msg) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Log.e("LOGIN ERROR", "Error al iniciar sesion");
			}
		});
	}
		
	public void setListener(LoginListener listener){
		this.listener = listener;
	}
}
