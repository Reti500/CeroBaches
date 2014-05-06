package com.geos.gestor.cerobaches.libs;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class CreateOrdenJson {

	public CreateOrdenJson(){
		super();
	}
	
	public static String createFromArrayList(ArrayList<Orden> ordenes_lsit){
		try{
			JSONObject jsonFinal = new JSONObject();
			JSONArray jsonArrayFinal = new JSONArray();
			
			for(int i=0; i<ordenes_lsit.size(); i++){
				Orden or = ordenes_lsit.get(i);
				JSONObject jsonOrden = new JSONObject();
				
				jsonOrden.put("idSolicitud", or.getIdSolicitud());
				jsonOrden.put("descripcion", or.getDescripcion());
				jsonOrden.put("calle", or.getCalle());
				jsonOrden.put("colonia", or.getColonia());
				jsonOrden.put("fechaCreacion", or.getFechaLinux());
				jsonOrden.put("EstatusSolicitud", or.getEstatus());
				jsonOrden.put("numeroInterior", or.getNumInterior());
				jsonOrden.put("numeroExterior", or.getNumExterior());
				jsonOrden.put("cp", or.getCP());
				jsonOrden.put("Municipio", or.getMunicipio());
				jsonOrden.put("Entidad", or.getEntidad());
				jsonOrden.put("calleCruce1", or.getCalleCruce1());
				jsonOrden.put("calleCruce2", or.getCalleCruce2());
				jsonOrden.put("latitud", or.getLatitud());
				jsonOrden.put("longitud", or.getLongitud());
				jsonOrden.put("EstatusId", or.getEstatusId());
				
				jsonArrayFinal.put(jsonOrden);
			}
			
			jsonFinal.put("solicitudes", jsonArrayFinal);
			return jsonFinal.toString();
		}catch(JSONException ex) {
	        ex.printStackTrace();
	        return "";
	    }
	}
	
	public static String createFromAdapter(OrdenAdapter adaptador){
		try{
			JSONObject jsonFinal = new JSONObject();
			JSONArray jsonArrayFinal = new JSONArray();
			Log.i("Numero de ordenes", ""+adaptador.getCount());
			for(int i=0; i<adaptador.getCount(); i++){
				Orden or = adaptador.getItem(i);
				JSONObject jsonOrden = new JSONObject();
				
				jsonOrden.put("idSolicitud", or.getIdSolicitud());
				jsonOrden.put("descripcion", or.getDescripcion());
				jsonOrden.put("calle", or.getCalle());
				jsonOrden.put("colonia", or.getColonia());
				jsonOrden.put("fechaCreacion", or.getFechaLinux());
				jsonOrden.put("EstatusSolicitud", or.getEstatus());
				jsonOrden.put("numeroInterior", or.getNumInterior());
				jsonOrden.put("numeroExterior", or.getNumExterior());
				jsonOrden.put("cp", or.getCP());
				jsonOrden.put("Municipio", or.getMunicipio());
				jsonOrden.put("Entidad", or.getEntidad());
				jsonOrden.put("calleCruce1", or.getCalleCruce1());
				jsonOrden.put("calleCruce2", or.getCalleCruce2());
				jsonOrden.put("latitud", or.getLatitud());
				jsonOrden.put("longitud", or.getLongitud());
				jsonOrden.put("EstatusId", or.getEstatusId());
				
				jsonArrayFinal.put(jsonOrden);
			}
			
			jsonFinal.put("solicitudes", jsonArrayFinal);
			return jsonFinal.toString();
		}catch(JSONException ex) {
	        ex.printStackTrace();
	        return "";
	    }
	}
}
