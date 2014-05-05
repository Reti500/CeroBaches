package com.geos.gestor.cerobaches.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.geos.gestor.cerobaches.R;
import com.geos.gestor.cerobaches.interfaces.MainListener;
import com.geos.gestor.cerobaches.libs.BachesComunicador;
import com.geos.gestor.cerobaches.libs.Comunicador.ResponseListener;
import com.geos.gestor.cerobaches.libs.Datos;
import com.geos.gestor.cerobaches.libs.Files;
import com.geos.gestor.cerobaches.libs.Functions;
import com.geos.gestor.cerobaches.libs.Orden;
import com.geos.gestor.cerobaches.libs.OrdenAdapter;

public class MainFragment extends Fragment {
	// Context
	Context context;
	
	//Private constants
	private static final String ORDENES_FILE_NAME = "ordenes.txt";
	
	// Lista
	private ListView lista_ordenes;
	
	// Dialog
	private ProgressDialog dialog;
	
	//Files
	private Files files;
	
	// Functions
	private Functions functions;
	
	// Listener
	private MainListener listener;
	
	// Datos
	private Datos datos;
	
	public MainFragment(){
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		context = rootView.getContext();
		files = new Files();
		functions = new Functions();
		datos = Datos.getInstance();
		
		files.createDirectories();
		
		datos.data_orden = new ArrayList<Orden>();
		
		lista_ordenes = (ListView) rootView.findViewById(R.id.frag_main_list_ordenes);
		datos.adapter = new OrdenAdapter(context, R.layout.list_item_row, datos.data_orden);
		
		lista_ordenes.setAdapter(datos.adapter);
		lista_ordenes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(listener != null){
					listener.onClickOrden(position);
				}
			}
		});
		
		dialog = ProgressDialog.show(context, "Please wait ...", "Cargando ordenes ...", true);
		
		if(files.existsFile(ORDENES_FILE_NAME, files.BACHES_CACHE_DIRECTORY)){
			cargarOrdenesEnCache();
		}else{
			cargarOrdenesDeTrabajo();
		}
		
		return rootView;
	}
	
	public void cargarOrdenesDeTrabajo() {
		final Handler run = new Handler();
		run.post(new Runnable() {

			@Override
			public void run() {
				
				
				BachesComunicador sendData = BachesComunicador.getInstance();

				sendData.getSolicitudes(new ResponseListener() {

					@Override
					public void success(String val) {
						files.saveFile(ORDENES_FILE_NAME, val, files.BACHES_CACHE_DIRECTORY);
						
						listarOrdenes(val);
//						
//						updateList();
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
		});
	}
	
	public void cargarOrdenesEnCache(){
		if(datos.data_orden.size() <= 0){
			listarOrdenes(files.readFile(ORDENES_FILE_NAME, files.BACHES_CACHE_DIRECTORY));
		}
	}
	
	public void updateList(){
		datos.adapter.notifyDataSetChanged();
		if(dialog.isShowing())
			dialog.dismiss();
	}
	
	public void setListener(MainListener listener){
		this.listener = listener;
	}
	
	public void listarOrdenes(String val){
		try {
			JSONObject json = new JSONObject(val);
			JSONArray solicitudes = json.getJSONArray("solicitudes");

			for (int i = 0; i < solicitudes.length(); i++) {
				JSONObject sol = solicitudes.getJSONObject(i);

				String id = sol.isNull("idSolicitud") != true ? sol
						.getString("idSolicitud") : "";
				String descripcion = sol.isNull("descripcion") != true ? sol
						.getString("descripcion") : "";
				String direccion = sol.isNull("calle") != true ? sol
						.getString("calle") : "";
				String fecha = sol.isNull("fechaCreacion") != true ? functions
						.linuxToGTM(sol.getString("fechaCreacion"))
						: "";
				String estatus = sol.isNull("EstatusSolicitud") != true ? sol
						.getString("EstatusSolicitud") : "";
				String calle = sol.isNull("calle") != true ? sol
						.getString("calle") : "";
				String colonia = sol.isNull("colonia") != true ? sol
						.getString("colonia") : "";
				String numInterior = sol.isNull("numInterior") != true ? sol
						.getString("numInterior") : "";
				String numExterior = sol.isNull("numExterior") != true ? sol
						.getString("numExterior") : "";
				String cp = sol.isNull("cp") != true ? sol
						.getString("cp") : "";
				String municipio = sol.isNull("Municipio") != true ? sol
						.getString("Municipio") : "";
				String entidad = sol.isNull("Entidad") != true ? sol
						.getString("Entidad") : "";
				String calleCruce1 = sol.isNull("calleCruce1") != true ? sol
						.getString("calleCruce1") : "";
				String calleCruce2 = sol.isNull("calleCruce2") != true ? sol
						.getString("calleCruce2") : "";
				String latitud = sol.isNull("latitud") != true ? sol
						.getString("latitud") : "";
				String longitud = sol.isNull("longitud") != true ? sol
						.getString("longitud") : "";
				String EstatusId = sol.isNull("EstatusId") != true ? sol
						.getString("EstatusId") : "";
				String fechaLinux = sol.isNull("fechaCreacion") != true ? functions
						.linuxFormat(sol.getString("fechaCreacion")) : "";
				String referencia = sol.isNull("referencia") != true ?
						sol.getString("referencia") : "";

				Orden or = new Orden(estatus, direccion, fecha);
				or.setIdSolicitud(id);
				or.setDescripcion(descripcion);
				or.setCalle(calle);
				or.setColonia(colonia);
				or.setNumInterio(numInterior);
				or.setNumExterior(numExterior);
				or.setCP(cp);
				or.setMunicipio(municipio);
				or.setEntidad(entidad);
				or.setCalleCruce1(calleCruce1);
				or.setCalleCruce2(calleCruce2);
				or.setLatitud(latitud);
				or.setLongitud(longitud);
				or.setEstatusId(EstatusId);
				or.setFechaLinux(fechaLinux);
				or.setReferencia(referencia);

				datos.data_orden.add(or);
			}

//			txtHeader.setText(pref.getString("name", "nada"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		updateList();
//		dialog.dismiss();
	}
}
