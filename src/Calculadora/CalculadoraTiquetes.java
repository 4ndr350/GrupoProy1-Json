package Calculadora;

public class CalculadoraTiquetes {
	public Double cargoServicio;
	public Double cuotaAdit;
	
	public double calcularCostoFinal(Double precioBol) {
		return (precioBol*(cargoServicio+cuotaAdit+1));
	}
	
	

}
