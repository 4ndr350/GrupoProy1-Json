package PaquetesTiquetes;

public class PaqueteDeluxe extends Paquete{
	public String beneficio;
	
	public PaqueteDeluxe(String beneficio) {
		super();
		this.precioPaq = 10000.0;
		this.transferible = false;
		this.beneficio = beneficio;
	}

}
