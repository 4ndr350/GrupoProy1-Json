package Calculadora;

import java.util.Date;
import java.util.Map;

import ZonasMaster.Evento;
import ZonasMaster.Localidad;

public class CalculadoraRendimiento {
	public static Map<Localidad, Double> ventasLocalidades;
	public static Map<Evento, Double> ventasEventos;
	public static Map<Date, Double> gananciasXfecha;
	public static Double gananciasGenerales;
	
	public Double calcularRendimientoEvento(Evento evento) {
		return ventasEventos.get(evento);
	}

}
