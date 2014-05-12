package com.geos.gestor.cerobaches.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.geos.gestor.cerobaches.R;
import com.geos.gestor.cerobaches.interfaces.LoginListener;
import com.geos.gestor.cerobaches.libs.Datos;
import com.geos.gestor.cerobaches.libs.SendToServer.SendParams;
import com.geos.gestor.cerobaches.libs.ParseJson;
import com.geos.gestor.cerobaches.libs.SendToServer;
import com.geos.gestor.cerobaches.libs.SendToServer.SendToServerListener;

public class LoginFragment extends Fragment {
	// Context
	private Context context;
	
	// UI
	private EditText username;
	private EditText password;
	private Button login;
	
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
				login();
			}
		});
		
		return rootView;
	}
		
	public void login(){
		SendToServer sendData = SendToServer.getInstance();
		
		ArrayList<SendParams> params = sendData.createParams(
				new SendParams("username", username.getText().toString()),
				new SendParams("password", password.getText().toString())
		);
		
		sendData.sendByPost(context, params, SendToServer.LOGIN_URL, 
				new SendToServerListener() {
			
			@Override
			public void success(String val) {
				// TODO Auto-generated method stub
				
				ParseJson serialize = new ParseJson();
				String[] vals = {"State", "Message", "Data"};
				HashMap<String, String> map = serialize.parse(val, vals);
				
				if(map.get("State").equals(Datos.RESPONSE_OK)){
					SharedPreferences preferences = context.getSharedPreferences("userPref", Context.MODE_PRIVATE);
					Editor editor = preferences.edit();
					
					editor.putBoolean("savedUserData", true);
					editor.putString("username", username.getText().toString());
					editor.putString("password", password.getText().toString());
					editor.commit();
					
					if(listener != null){
						listener.goMain();
					}
				}else{
					Toast.makeText(context, "Error en el usuario o contraseña", Toast.LENGTH_LONG).show();
					password.setText("");
				}
			}
			
			@Override
			public void onfinal() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void error(String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "Error de red", Toast.LENGTH_LONG).show();
			}
		});
	}
		
	public void setListener(LoginListener listener){
		this.listener = listener;
	}
}
