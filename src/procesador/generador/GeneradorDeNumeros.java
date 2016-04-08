package procesador.generador;

public class GeneradorDeNumeros{
	
	public int generadorExponencial(double lambda){
		int valor = 0;
		double u = Math.random();
		if (u==0){
			valor = 0;
		}else{
			valor = (int)(-1*(Math.log(u))/lambda);
		}
		return valor;
	}
	
	public int generadorGaussiano(double media, double desviacion){
		int valor = 0;
		double u1 = Math.random();
		double u2 = Math.random();
		valor = (int) (Math.sqrt(-2*Math.log(u1))*Math.cos(2*Math.PI*(u2)));
		return (int) (valor*desviacion+media);
	}
	
	public int generadorRayleigh(double fi){
		int valor = 0;
		double u = Math.random();
		valor = (int) (fi*Math.sqrt(-2*Math.log(1-u)));
		return valor;
	}
}
