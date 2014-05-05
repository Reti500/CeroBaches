package com.geos.gestor.cerobaches.fragments;

import com.geos.gestor.cerobaches.R;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class IniciarReporteFragment extends Fragment{
	
	private static int TAKE_PICTURE = 1;
	
	private EditText comentario;
	private ImageButton tomarFoto;
	private Button aceptar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_iniciar_reporte,
				container, false);
		
		comentario = (EditText) rootView.findViewById(R.id.iniciar_reporte_edit_comentarios);
		tomarFoto = (ImageButton) rootView.findViewById(R.id.iniciar_reporte_image_foto);
		aceptar = (Button) rootView.findViewById(R.id.iniciar_reporte_button_aceptar);
		
		tomarFoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				
				startActivityForResult(i, TAKE_PICTURE);
			}
		});
		
		return rootView;
	}
}
