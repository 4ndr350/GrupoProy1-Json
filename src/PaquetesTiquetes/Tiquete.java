package PaquetesTiquetes;

import java.util.Date;

public class Tiquete {
	public String idTiquete;
	public String asiento;
	public String tipo;
	public Date fecha;
	public String hora;
	

	public Tiquete(String idTiquete, String asiento, String tipo, Date fecha,
			String hora) {
		super();
		this.idTiquete = idTiquete;
		this.asiento = asiento;
		this.tipo = tipo;
		this.fecha = fecha;
		this.hora = hora;
	}


	public Tiquete(String idTiquete, String tipo, Date fecha, String hora) {
		super();
		this.idTiquete = idTiquete;
		this.tipo = tipo;
		this.fecha = fecha;
		this.hora = hora;
		this.asiento = null;
	}
	
	

}
