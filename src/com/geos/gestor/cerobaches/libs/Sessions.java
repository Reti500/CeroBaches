package com.geos.gestor.cerobaches.libs;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.geos.gestor.cerobaches.libs.SendToServer.SendParams;
import com.geos.gestor.cerobaches.libs.SendToServer.SendToServerListener;

public class Sessions {
	public static Sessions instance;
	
	private boolean state;
	
	public Sessions(){
		super();
		
		this.state = false;
	}
	
	public static final Sessions getInstance(){
		if(instance == null){
			instance = new Sessions();
		}
		
		return instance;
	}
	
	public void active(){
		this.state= true;
	}
	
	public void inactive(){
		this.state = false;
	}
	
	public boolean isActive(){
		return state;
	}
	
	public void login(Context context, String username, String password, SendToServerListener listener){
		SendToServer sendData = SendToServer.getInstance();
		
		ArrayList<SendParams> params = sendData.createParams(
				new SendParams("username", username),
				new SendParams("password", password)
		);
		
		sendData.sendByPost(context, params, SendToServer.LOGIN_URL, listener);
			
//			@Override
//			public void success(String val) {
//				// TODO Auto-generated method stub
//				
//				ParseJson serialize = new ParseJson();
//				String[] vals = {"State", "Message", "Data"};
//				HashMap<String, String> map = serialize.parse(val, vals);
//				
//				if(map.get("State").equals(Datos.RESPONSE_OK)){
//					state = true;
//				}else{
//					state = false;
//				}
//			}
//			
//			@Override
//			public void onfinal() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void error(String msg) {
				// TODO Auto-generated method stub
//				Toast.makeText(context, "Error de red", Toast.LENGTH_LONG).show();
	}
}
