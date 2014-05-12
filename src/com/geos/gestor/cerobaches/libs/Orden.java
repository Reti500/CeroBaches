package com.geos.gestor.cerobaches.libs;

import android.os.Parcel;
import android.os.Parcelable;

public class Orden implements Parcelable{
	private String idSolicitud;				// get set
	private String fecha;					// get set
	private String estatus;					// get set
	private String descripcion;				// get set
	private String calle;					// get set
	private String colonia;					// get set
	private String numInterior;				// get set
	private String numExterior;				// get set
	private String cp;						// get set
	private String municipio;				// get set
	private String entidad;					// get set
	private String calleCruce1;				// get set
	private String calleCruce2;				// get set
	private String latitud;					// get set
	private String longitud;				// get set
	private String estatusId;				// get set
	private String fechaLinux;				// get set
	private String referencia;				// get set
	
	
	// ---------------------------------------------------------------------
	// CONTRUCTORES
	// ---------------------------------------------------------------------
	
	public Orden(){
		super();
	}
	
	public Orden(Parcel in){
		this.calle = in.readString();
		this.calleCruce1 = in.readString();
		this.calleCruce2 = in.readString();
		this.colonia = in.readString();
		this.cp = in.readString();
		this.descripcion = in.readString();
		this.entidad = in.readString();
		this.estatus = in.readString();
		this.fecha = in.readString();
		this.idSolicitud = in.readString();
		this.latitud = in.readString();
		this.longitud = in.readString();
		this.municipio = in.readString();
		this.numExterior = in.readString();
		this.numInterior = in.readString();
		this.estatusId = in.readString();
		this.fechaLinux = in.readString();
		this.referencia = in.readString();
	}
	
//	public Orden(String fecha){
//		super();
//		
//		this.fecha = fecha;
//	}
	
//	public Orden(String estatus, String direccion, String fecha){
//		super();
//		
//		this.estatus = estatus;
//		this.fecha = fecha;
//	}
	
	// ---------------------------------------------------------------------
	// SET
	// ---------------------------------------------------------------------
	public void setIdSolicitud(String id){
		this.idSolicitud = id;
	}
	
	public void setFecha(String fecha){
		this.fecha = fecha;
	}
	
	public void setDescripcion(String des){
		this.descripcion = des;
	}
	
	public void setEstatus(String e){
		this.estatus = e;
	}
	
	public void setCalle(String calle){
		this.calle = calle;
	}
	
	public void setColonia(String colonia){
		this.colonia = colonia;
	}
	
	public void setNumInterio(String numInterior){
		this.numInterior = numInterior;
	}
	
	public void setNumExterior(String numExterior){
		this.numExterior = numExterior;
	}
	
	public void setCP(String cp){
		this.cp = cp;
	}
	
	public void setMunicipio(String municipio){
		this.municipio = municipio;
	}
	
	public void setEntidad(String entidad){
		this.entidad = entidad;
	}
	
	public void setCalleCruce1(String calleCruce1){
		this.calleCruce1 = calleCruce1;
	}
	
	public void setCalleCruce2(String calleCruce2){
		this.calleCruce2 = calleCruce2;
	}
	
	public void setLatitud(String latitud){
		this.latitud = latitud;
	}
	
	public void setLongitud(String longitud){
		this.longitud = longitud;
	}
	
	public void setEstatusId(String estatusId){
		this.estatusId = estatusId;
	}
	
	public void setFechaLinux(String fechaLinux){
		this.fechaLinux = fechaLinux;
	}
	
	public void setReferencia(String referencia){
		this.referencia = referencia;
	}
	
	// ---------------------------------------------------------------------
	// GET
	// ---------------------------------------------------------------------
	
	public String getIdSolicitud(){
		return idSolicitud;
	}
	
	public String getFecha(){
		return fecha;
	}
	
	public String getEstatus(){
		return estatus;
	}
	
	public String getDescripcion(){
		return descripcion;
	}
	
	public String getCalle(){
		return calle;
	}
	
	public String getColonia(){
		return colonia;
	}
	
	public String getNumInterior(){
		return numInterior;
	}
	
	public String getNumExterior(){
		return numExterior;
	}
	
	public String getCP(){
		return cp;
	}
	
	public String getMunicipio(){
		return municipio;
	}
	
	public String getEntidad(){
		return entidad;
	}
	
	public String getCalleCruce1(){
		return calleCruce1;
	}
	
	public String getCalleCruce2(){
		return calleCruce2;
	}
	
	public String getLatitud(){
		return latitud;
	}
	
	public String getLongitud(){
		return longitud;
	}
	
	public String getEstatusId(){
		return estatusId;
	}
	
	public String getFechaLinux(){
		return fechaLinux;
	}
	
	public String getReferencia(){
		return referencia;
	}
	
	// ---------------------------------------------------------------------
	// Parcelable
	// ---------------------------------------------------------------------

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(this.calle);
		dest.writeString(this.calleCruce1);
		dest.writeString(this.calleCruce2);
		dest.writeString(this.colonia);
		dest.writeString(this.cp);
		dest.writeString(this.descripcion);
		dest.writeString(this.entidad);
		dest.writeString(this.estatus);
		dest.writeString(this.fecha);
		dest.writeString(this.idSolicitud);
		dest.writeString(this.latitud);
		dest.writeString(this.longitud);
		dest.writeString(this.municipio);
		dest.writeString(this.numExterior);
		dest.writeString(this.numInterior);
		dest.writeString(this.estatusId);
		dest.writeString(this.fechaLinux);
		dest.writeString(this.referencia);
	}
	
	public static final Orden.Creator<Orden> CREATOR = new Orden.Creator<Orden>() {
        public Orden createFromParcel(Parcel in) {
            return new Orden(in);
        }
  
        public Orden[] newArray(int size) {
            return new Orden[size];
        }
    };
	
	// ---------------------------------------------------------------------
}
