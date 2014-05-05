package com.geos.gestor.cerobaches.libs;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class BachesComunicador extends Comunicador {
	
	public final static String URL_BASE = "http://rest.bs.geos-it.mx/";
	
	private static BachesComunicador instance;
	
	protected BachesComunicador() {
		super();
	}
	
	public final static BachesComunicador getInstance() {
		if (instance == null) {
			instance = new BachesComunicador(); 
		}
		return instance;
	}
	
	/*
	 * Sesiones
	 */
	
	public final void login (String user, String password, ResponseListener listener) {
		ArrayList<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add( new BasicNameValuePair("UserName", user ));
		paramsList.add( new BasicNameValuePair("Password", password ));
		post(build_url (URL_BASE, "Account", "LogOn"), null, paramsList, listener);
	}
	
	public final void logout (ResponseListener listener) {
		get(build_url (URL_BASE, "Account", "LogOff"), null, null, listener);
	}
	
	// Obterner la informacion del usuario
	public final void getUser(ResponseListener listener) {
		HashMap<String, String> params = new HashMap<String, String>(2);
		get(build_url (URL_BASE, "Account", "GetUserInfo"), null, params, listener);
	}
	
	// Obtiene las orenes de trabajo
	public final void getSolicitudes(ResponseListener listener){
		HashMap<String, String> params = new HashMap<String, String>(2);
		get(build_url (URL_BASE, "Solicitud"), null, params, listener);
	}
	
	// Crea una solicitud
	public final void createSolicitud(String calle, String colonia,
			String municipio, String entidad, String cp,
			String latitud, String longitud, String descripcion, String descipcionUbicacion,
			ResponseListener listener){
		ArrayList<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add( new BasicNameValuePair("calle", calle ));
		paramsList.add( new BasicNameValuePair("colonia", colonia ));
		paramsList.add( new BasicNameValuePair("municipio", municipio));
		paramsList.add( new BasicNameValuePair("entidad", entidad ));
		paramsList.add( new BasicNameValuePair("cp", cp ));
		paramsList.add( new BasicNameValuePair("latitud", latitud ));
		paramsList.add( new BasicNameValuePair("longitud", longitud ));
		paramsList.add( new BasicNameValuePair("descripcion", descripcion ));
		paramsList.add( new BasicNameValuePair("descripcionUbicacion", descipcionUbicacion));
		post(build_url (URL_BASE, "Solicitud", "Create"), null, paramsList, listener);
	}
	
	// Cambia el estatus de una solicitud
	public final void cambiarEstatus(String id, String idEstatus, String comentario, ResponseListener listener){
		ArrayList<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add( new BasicNameValuePair("id", id ));
		paramsList.add( new BasicNameValuePair("idEstatus", idEstatus ));
		paramsList.add( new BasicNameValuePair("comentario", comentario ));
		post(build_url (URL_BASE, "Solicitud", "ChangeEstatus"), null, paramsList, listener);
	}
	
	// Guardar info de la imagen
	public final void uploadImagen(String id, String idEstatus, String url, String nombre, ResponseListener listener){
		ArrayList<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add( new BasicNameValuePair("idSolicitud", id ));
		paramsList.add( new BasicNameValuePair("idEstatus", idEstatus ));
		paramsList.add( new BasicNameValuePair("url", url ));
		paramsList.add( new BasicNameValuePair("nombre", nombre ));
		post(build_url (URL_BASE, "Solicitud", "UpLoadImage"), null, paramsList, listener);
	}
	
	// Obtener el historial de una solicitud
	public final void historial(String idSolicitud, ResponseListener listener){
		HashMap<String, String> params = new HashMap<String, String>(2);
		params.put("idSolicitud", idSolicitud);
		get(build_url (URL_BASE, "Historial"), null, params, listener);
	}
}

