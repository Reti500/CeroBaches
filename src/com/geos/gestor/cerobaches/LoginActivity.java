package com.geos.gestor.cerobaches;

import com.geos.gestor.cerobaches.fragments.LoginFragment;
import com.geos.gestor.cerobaches.interfaces.LoginListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class LoginActivity extends FragmentActivity implements LoginListener {
	
	private LoginFragment fragmentLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.login_fragment, new LoginFragment()).commit();
		}
		
		SharedPreferences preferences = getSharedPreferences("userPref", Context.MODE_PRIVATE);
		if(preferences.getBoolean("savedUserData", false)){
			goMain();
		}else{
			fragmentLogin = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.login_fragment);
			fragmentLogin.setListener(this);
		}
	}

	@Override
	public void goMain() {
		// TODO Auto-generated method stub
		Intent i = new Intent().setClass(LoginActivity.this, MainActivity.class);
		startActivity(i);
		finish();
	}
}
