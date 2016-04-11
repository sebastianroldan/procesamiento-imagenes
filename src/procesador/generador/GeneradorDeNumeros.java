package procesador.generador;

public class GeneradorDeNumeros{
	
public int generadorAleatorioExponencial(double lambda){
		
		int valor = 0;
		double u = Math.random();
		
		if (u==0){
			valor = 0;
		}else{
			valor = (int)Math.round((-1*(Math.log(u))/lambda));
		}
		//System.out.println("X: "+u+" Y: "+valor);
		
		return valor;
	}
	
	public int generadorAleatorioGaussiano(double media, double desvio){
		int valor = 0;
		double u1 = Math.random();
		double u2 = Math.random();
		valor = (int) (Math.sqrt(-2*Math.log(u1))*Math.cos(2*Math.PI*(u2)));
		//System.out.println("Valor: "+valor);
		return (int) (valor*desvio+media);
	}
	
	public int generadorAleatorioRayleigh(double fi){
		int valor = 0;
		double u = Math.random();
		valor = (int) (fi*Math.sqrt(-2*Math.log(1-u)));
		//System.out.println("X: "+u+" Y: "+valor);
		return valor;
	}
}
