package procesador.domain;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class DetectorDeContornosActivos {

		private Imagen original;
		private Imagen resultado;
		private int ancho;
		private int alto;
		private Color colorObjeto;
		private int [][] imagenFi=null;
		private List<Point> Lint = null;
		private List<Point> Lext = null;
		private int p1X;
		private int p1Y;
		private int p2X;
		private int p2Y;
			
		public Imagen deteccionDeContornosActivos(Imagen original,Point puntoInicial,Point puntoFinal,Integer error){
			this.original=original;
			this.ancho = original.getAncho();
			this.alto = original.getAlto();
			this.resultado = clonarImagen(this.original);
			this.imagenFi = inicializarMatrizFi();
			this.Lint = new ArrayList<Point>();
			this.Lext = new ArrayList<Point>();
									
			Integer iteraciones=calcularMaximasIteraciones();
			
			if (aplicarContornoInicial(resultado,puntoInicial,puntoFinal,error)){
				
				ejecutarPrimerCiclo(iteraciones,error);
				
				//ejecutarSegundoCiclo();		
				
				pintarBordesContornosActivos();
			
			}else{
			
				System.out.println("Error al aplicar contorno inicial");
			
			}
			
			return resultado;
		}
		
		private void ejecutarPrimerCiclo(Integer iteraciones,Integer error) {
			
			Point puntoX=null;
			Point puntoY=null;
			Color colorX=null;
			int red,blue,green;
						
			for (int i=0;i<1000;i++){
				//Paso1
				ListIterator<Point> iteradorLext = this.Lext.listIterator();
				while (iteradorLext.hasNext()){
					puntoX=iteradorLext.next();
					red=this.resultado.getRed((int)puntoX.getX(),(int)puntoX.getY());
					green=this.resultado.getGreen((int)puntoX.getX(),(int)puntoX.getY());
					blue=this.resultado.getBlue((int)puntoX.getX(),(int)puntoX.getY());
					colorX=new Color(red,green,blue);
					if (funcionFuerzaVelocidad_D(colorX,this.colorObjeto,error)>0){
						puntoY=new Point();
						puntoY.setLocation((int)puntoX.getX(),(int)puntoX.getY());
						this.Lint.add(puntoY);
						this.imagenFi[(int)puntoX.getX()][(int)puntoX.getY()]=1;
						iteradorLext.remove();
						validarPuntosN4conFi3(puntoY);
					}
				}
				
				//Paso2
				ListIterator<Point> iteradorLint = this.Lint.listIterator();
				while (iteradorLint.hasNext()){
					puntoX=iteradorLint.next();
					red=this.resultado.getRed((int)puntoX.getX(),(int)puntoX.getY());
					green=this.resultado.getGreen((int)puntoX.getX(),(int)puntoX.getY());
					blue=this.resultado.getBlue((int)puntoX.getX(),(int)puntoX.getY());
					colorX=new Color(red,green,blue);
					if (funcionFuerzaVelocidad_D(colorX,this.colorObjeto,error)>0){
						this.imagenFi[(int)puntoX.getX()][(int)puntoX.getY()]=-3;
						iteradorLint.remove();
					}
				}
				
				//Paso3
				ListIterator<Point> iterador2Lint = this.Lint.listIterator();
				while (iterador2Lint.hasNext()){
					puntoX=iterador2Lint.next();
					red=this.resultado.getRed((int)puntoX.getX(),(int)puntoX.getY());
					green=this.resultado.getGreen((int)puntoX.getX(),(int)puntoX.getY());
					blue=this.resultado.getBlue((int)puntoX.getX(),(int)puntoX.getY());
					colorX=new Color(red,green,blue);
					if (funcionFuerzaVelocidad_D(colorX,this.colorObjeto,error)<0){
						puntoY=new Point();
						puntoY.setLocation((int)puntoX.getX(),(int)puntoX.getY());
						this.Lext.add(puntoY);
						this.imagenFi[(int)puntoX.getX()][(int)puntoX.getY()]=-1;
						iterador2Lint.remove();
						validarPuntosN4conFiMenos3(puntoY);
					}
				}
							
				//Paso4
				ListIterator<Point> iterador2Lext = this.Lext.listIterator();
				while (iterador2Lext.hasNext()){
					puntoX=iterador2Lext.next();
					red=this.resultado.getRed((int)puntoX.getX(),(int)puntoX.getY());
					green=this.resultado.getGreen((int)puntoX.getX(),(int)puntoX.getY());
					blue=this.resultado.getBlue((int)puntoX.getX(),(int)puntoX.getY());
					colorX=new Color(red,green,blue);
					if (funcionFuerzaVelocidad_D(colorX,this.colorObjeto,error)<0){
						this.imagenFi[(int)puntoX.getX()][(int)puntoX.getY()]=3;
						iterador2Lext.remove();
					}
				}
				
			}
		}

		private void validarPuntosN4conFiMenos3(Point puntoY) {
			int valorX=(int)puntoY.getX();
			int valorY=(int)puntoY.getY();
			Point puntoN4=null;
			
			if ((valorX-1>=0)&&(valorX+1<ancho)&&(valorY-1>=0)&&(valorY+1<alto)){
				if (imagenFi[valorX-1][valorY-1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY-1);
					this.Lint.add(puntoN4);
					imagenFi[valorX-1][valorY-1]=-1;
				}
				if (imagenFi[valorX][valorY-1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY-1);
					this.Lint.add(puntoN4);
					imagenFi[valorX][valorY-1]=-1;
				}
				if (imagenFi[valorX+1][valorY-1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY-1);
					this.Lint.add(puntoN4);
					imagenFi[valorX+1][valorY-1]=-1;
				}
				if (imagenFi[valorX-1][valorY]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY);
					this.Lint.add(puntoN4);
					imagenFi[valorX-1][valorY]=-1;
				}
				if (imagenFi[valorX+1][valorY]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY);
					this.Lint.add(puntoN4);
					imagenFi[valorX+1][valorY]=-1;
				}
				if (imagenFi[valorX-1][valorY+1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY+1);
					this.Lint.add(puntoN4);
					imagenFi[valorX-1][valorY+1]=-1;
				}
				if (imagenFi[valorX][valorY+1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY+1);
					this.Lint.add(puntoN4);
					imagenFi[valorX][valorY+1]=-1;
				}
				if (imagenFi[valorX+1][valorY+1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY+1);
					this.Lint.add(puntoN4);
					imagenFi[valorX+1][valorY+1]=-1;
				}
			}
			if ((valorX==0)&&(valorY>0)&&(valorY<alto-1)){
				if (imagenFi[valorX][valorY-1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY-1);
					this.Lint.add(puntoN4);
					imagenFi[valorX][valorY-1]=-1;
				}
				if (imagenFi[valorX+1][valorY-1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY-1);
					this.Lint.add(puntoN4);
					imagenFi[valorX+1][valorY-1]=-1;
				}
				if (imagenFi[valorX+1][valorY]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY);
					this.Lint.add(puntoN4);
					imagenFi[valorX+1][valorY]=-1;
				}
				if (imagenFi[valorX][valorY+1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY+1);
					this.Lint.add(puntoN4);
					imagenFi[valorX][valorY+1]=-1;
				}
				if (imagenFi[valorX+1][valorY+1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY+1);
					this.Lint.add(puntoN4);
					imagenFi[valorX+1][valorY+1]=-1;
				}
			}
			
			if ((valorX==ancho-1)&&(valorY>0)&&(valorY<alto-1)){
				
				if (imagenFi[valorX-1][valorY-1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY-1);
					this.Lint.add(puntoN4);
					imagenFi[valorX-1][valorY-1]=-1;
				}
				if (imagenFi[valorX][valorY-1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY-1);
					this.Lint.add(puntoN4);
					imagenFi[valorX][valorY-1]=-1;
				}
				if (imagenFi[valorX-1][valorY]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY);
					this.Lint.add(puntoN4);
					imagenFi[valorX-1][valorY]=-1;
				}
				if (imagenFi[valorX-1][valorY+1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY+1);
					this.Lint.add(puntoN4);
					imagenFi[valorX-1][valorY+1]=-1;
				}
				if (imagenFi[valorX][valorY+1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY+1);
					this.Lint.add(puntoN4);
					imagenFi[valorX][valorY+1]=-1;
				}
			}
			if ((valorY==0)&&(valorX>0)&&(valorX<ancho-1)){

				if (imagenFi[valorX-1][valorY]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY);
					this.Lint.add(puntoN4);
					imagenFi[valorX-1][valorY]=-1;
				}
				if (imagenFi[valorX+1][valorY]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY);
					this.Lint.add(puntoN4);
					imagenFi[valorX+1][valorY]=-1;
				}
				if (imagenFi[valorX-1][valorY+1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY+1);
					this.Lint.add(puntoN4);
					imagenFi[valorX-1][valorY+1]=-1;
				}
				if (imagenFi[valorX][valorY+1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY+1);
					this.Lint.add(puntoN4);
					imagenFi[valorX][valorY+1]=-1;
				}
				if (imagenFi[valorX+1][valorY+1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY+1);
					this.Lint.add(puntoN4);
					imagenFi[valorX+1][valorY+1]=-1;
				}
			}
			
			if ((valorY==alto-1)&&(valorX>0)&&(valorX<ancho-1)){
				if (imagenFi[valorX-1][valorY-1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY-1);
					this.Lint.add(puntoN4);
					imagenFi[valorX-1][valorY-1]=-1;
				}
				if (imagenFi[valorX][valorY-1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY-1);
					this.Lint.add(puntoN4);
					imagenFi[valorX][valorY-1]=-1;
				}
				if (imagenFi[valorX+1][valorY-1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY-1);
					this.Lint.add(puntoN4);
					imagenFi[valorX+1][valorY-1]=-1;
				}
				if (imagenFi[valorX-1][valorY]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY);
					this.Lint.add(puntoN4);
					imagenFi[valorX-1][valorY]=-1;
				}
				if (imagenFi[valorX+1][valorY]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY);
					this.Lint.add(puntoN4);
					imagenFi[valorX+1][valorY]=-1;
				}
			}
			
			if ((valorX==0)&&(valorY==0)){
				if (imagenFi[valorX+1][valorY]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY);
					this.Lint.add(puntoN4);
					imagenFi[valorX+1][valorY]=-1;
				}
				if (imagenFi[valorX][valorY+1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY+1);
					this.Lint.add(puntoN4);
					imagenFi[valorX][valorY+1]=-1;
				}
				if (imagenFi[valorX+1][valorY+1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY+1);
					this.Lint.add(puntoN4);
					imagenFi[valorX+1][valorY+1]=-1;
				}
			}
			if ((valorX==0)&&(valorY==alto-1)){
							
				if (imagenFi[valorX][valorY-1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY-1);
					this.Lint.add(puntoN4);
					imagenFi[valorX][valorY-1]=-1;
				}
				if (imagenFi[valorX+1][valorY-1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY-1);
					this.Lint.add(puntoN4);
					imagenFi[valorX+1][valorY-1]=-1;
				}
				if (imagenFi[valorX+1][valorY]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY);
					this.Lint.add(puntoN4);
					imagenFi[valorX+1][valorY]=-1;
				}
			}
			if ((valorX==ancho-1)&&(valorY==0)){
				
				if (imagenFi[valorX-1][valorY]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY);
					this.Lint.add(puntoN4);
					imagenFi[valorX-1][valorY]=-1;
				}
				if (imagenFi[valorX-1][valorY+1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY+1);
					this.Lint.add(puntoN4);
					imagenFi[valorX-1][valorY+1]=-1;
				}
				if (imagenFi[valorX][valorY+1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY+1);
					this.Lint.add(puntoN4);
					imagenFi[valorX][valorY+1]=-1;
				}
			}
			if ((valorX==ancho-1)&&(valorY==alto-1)){
				if (imagenFi[valorX-1][valorY-1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY-1);
					this.Lint.add(puntoN4);
					imagenFi[valorX-1][valorY-1]=-1;
				}
				if (imagenFi[valorX][valorY-1]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY-1);
					this.Lint.add(puntoN4);
					imagenFi[valorX][valorY-1]=-1;
				}
				if (imagenFi[valorX-1][valorY]==-3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY);
					this.Lint.add(puntoN4);
					imagenFi[valorX-1][valorY]=-1;
				}
			}
		}


		private void validarPuntosN4conFi3(Point puntoY) {
			int valorX=(int)puntoY.getX();
			int valorY=(int)puntoY.getY();
			Point puntoN4=null;
			
			if ((valorX-1>=0)&&(valorX+1<ancho)&&(valorY-1>=0)&&(valorY+1<alto)){
				if (imagenFi[valorX-1][valorY-1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY-1);
					this.Lext.add(puntoN4);
					imagenFi[valorX-1][valorY-1]=1;
				}
				if (imagenFi[valorX][valorY-1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY-1);
					this.Lext.add(puntoN4);
					imagenFi[valorX][valorY-1]=1;
				}
				if (imagenFi[valorX+1][valorY-1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY-1);
					this.Lext.add(puntoN4);
					imagenFi[valorX+1][valorY-1]=1;
				}
				if (imagenFi[valorX-1][valorY]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY);
					this.Lext.add(puntoN4);
					imagenFi[valorX-1][valorY]=1;
				}
				if (imagenFi[valorX+1][valorY]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY);
					this.Lext.add(puntoN4);
					imagenFi[valorX+1][valorY]=1;
				}
				if (imagenFi[valorX-1][valorY+1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY+1);
					this.Lext.add(puntoN4);
					imagenFi[valorX-1][valorY+1]=1;
				}
				if (imagenFi[valorX][valorY+1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY+1);
					this.Lext.add(puntoN4);
					imagenFi[valorX][valorY+1]=1;
				}
				if (imagenFi[valorX+1][valorY+1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY+1);
					this.Lext.add(puntoN4);
					imagenFi[valorX+1][valorY+1]=1;
				}
			}
			if ((valorX==0)&&(valorY>0)&&(valorY<alto-1)){
				if (imagenFi[valorX][valorY-1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY-1);
					this.Lext.add(puntoN4);
					imagenFi[valorX][valorY-1]=1;
				}
				if (imagenFi[valorX+1][valorY-1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY-1);
					this.Lext.add(puntoN4);
					imagenFi[valorX+1][valorY-1]=1;
				}
				if (imagenFi[valorX+1][valorY]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY);
					this.Lext.add(puntoN4);
					imagenFi[valorX+1][valorY]=1;
				}
				if (imagenFi[valorX][valorY+1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY+1);
					this.Lext.add(puntoN4);
					imagenFi[valorX][valorY+1]=1;
				}
				if (imagenFi[valorX+1][valorY+1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY+1);
					this.Lext.add(puntoN4);
					imagenFi[valorX+1][valorY+1]=1;
				}
			}
			
			if ((valorX==ancho-1)&&(valorY>0)&&(valorY<alto-1)){
				
				if (imagenFi[valorX-1][valorY-1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY-1);
					this.Lext.add(puntoN4);
					imagenFi[valorX-1][valorY-1]=1;
				}
				if (imagenFi[valorX][valorY-1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY-1);
					this.Lext.add(puntoN4);
					imagenFi[valorX][valorY-1]=1;
				}
				if (imagenFi[valorX-1][valorY]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY);
					this.Lext.add(puntoN4);
					imagenFi[valorX-1][valorY]=1;
				}
				if (imagenFi[valorX-1][valorY+1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY+1);
					this.Lext.add(puntoN4);
					imagenFi[valorX-1][valorY+1]=1;
				}
				if (imagenFi[valorX][valorY+1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY+1);
					this.Lext.add(puntoN4);
					imagenFi[valorX][valorY+1]=1;
				}
			}
			if ((valorY==0)&&(valorX>0)&&(valorX<ancho-1)){

				if (imagenFi[valorX-1][valorY]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY);
					this.Lext.add(puntoN4);
					imagenFi[valorX-1][valorY]=1;
				}
				if (imagenFi[valorX+1][valorY]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY);
					this.Lext.add(puntoN4);
					imagenFi[valorX+1][valorY]=1;
				}
				if (imagenFi[valorX-1][valorY+1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY+1);
					this.Lext.add(puntoN4);
					imagenFi[valorX-1][valorY+1]=1;
				}
				if (imagenFi[valorX][valorY+1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY+1);
					this.Lext.add(puntoN4);
					imagenFi[valorX][valorY+1]=1;
				}
				if (imagenFi[valorX+1][valorY+1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY+1);
					this.Lext.add(puntoN4);
					imagenFi[valorX+1][valorY+1]=1;
				}
			}
			
			if ((valorY==alto-1)&&(valorX>0)&&(valorX<ancho-1)){
				if (imagenFi[valorX-1][valorY-1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY-1);
					this.Lext.add(puntoN4);
					imagenFi[valorX-1][valorY-1]=1;
				}
				if (imagenFi[valorX][valorY-1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY-1);
					this.Lext.add(puntoN4);
					imagenFi[valorX][valorY-1]=1;
				}
				if (imagenFi[valorX+1][valorY-1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY-1);
					this.Lext.add(puntoN4);
					imagenFi[valorX+1][valorY-1]=1;
				}
				if (imagenFi[valorX-1][valorY]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY);
					this.Lext.add(puntoN4);
					imagenFi[valorX-1][valorY]=1;
				}
				if (imagenFi[valorX+1][valorY]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY);
					this.Lext.add(puntoN4);
					imagenFi[valorX+1][valorY]=1;
				}
			}
			
			if ((valorX==0)&&(valorY==0)){
				if (imagenFi[valorX+1][valorY]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY);
					this.Lext.add(puntoN4);
					imagenFi[valorX+1][valorY]=1;
				}
				if (imagenFi[valorX][valorY+1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY+1);
					this.Lext.add(puntoN4);
					imagenFi[valorX][valorY+1]=1;
				}
				if (imagenFi[valorX+1][valorY+1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY+1);
					this.Lext.add(puntoN4);
					imagenFi[valorX+1][valorY+1]=1;
				}
			}
			if ((valorX==0)&&(valorY==alto-1)){
							
				if (imagenFi[valorX][valorY-1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY-1);
					this.Lext.add(puntoN4);
					imagenFi[valorX][valorY-1]=1;
				}
				if (imagenFi[valorX+1][valorY-1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY-1);
					this.Lext.add(puntoN4);
					imagenFi[valorX+1][valorY-1]=1;
				}
				if (imagenFi[valorX+1][valorY]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX+1,valorY);
					this.Lext.add(puntoN4);
					imagenFi[valorX+1][valorY]=1;
				}
			}
			if ((valorX==ancho-1)&&(valorY==0)){
				
				if (imagenFi[valorX-1][valorY]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY);
					this.Lext.add(puntoN4);
					imagenFi[valorX-1][valorY]=1;
				}
				if (imagenFi[valorX-1][valorY+1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY+1);
					this.Lext.add(puntoN4);
					imagenFi[valorX-1][valorY+1]=1;
				}
				if (imagenFi[valorX][valorY+1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY+1);
					this.Lext.add(puntoN4);
					imagenFi[valorX][valorY+1]=1;
				}
			}
			if ((valorX==ancho-1)&&(valorY==alto-1)){
				if (imagenFi[valorX-1][valorY-1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY-1);
					this.Lext.add(puntoN4);
					imagenFi[valorX-1][valorY-1]=1;
				}
				if (imagenFi[valorX][valorY-1]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX,valorY-1);
					this.Lext.add(puntoN4);
					imagenFi[valorX][valorY-1]=1;
				}
				if (imagenFi[valorX-1][valorY]==3){
					puntoN4=new Point();
					puntoN4.setLocation(valorX-1,valorY);
					this.Lext.add(puntoN4);
					imagenFi[valorX-1][valorY]=1;
				}
			}
		}

		private int calcularMaximasIteraciones() {
			int iteraciones=this.ancho;
			if (this.alto>this.ancho) {
				iteraciones=this.alto;
			}
			return iteraciones;
		}

		private void pintarBordesContornosActivos() {
			
			Color azul=new Color(0,51,255);
			Color amarillo=new Color(255,255,51);
			
			for (int i=0;i<ancho;i++){
				for (int j=0;j<alto;j++){
					System.out.print(imagenFi[i][j]+" ");
					switch(imagenFi[i][j]){
					case -1: resultado.setRGB(i,j,amarillo.getRGB());
						break;
					case 1: resultado.setRGB(i,j,azul.getRGB());
						break;
					case 3: resultado.setRGB(i,j,amarillo.getRGB());
						break;
					}
				}
				System.out.println("");
			}
		}

		private boolean aplicarContornoInicial(Imagen resultado,Point puntoInicial,Point puntoFinal,Integer error){
			boolean respuesta=true;
			this.p1X=(int) puntoInicial.getX();
			this.p1Y=(int) puntoInicial.getY();
			this.p2X=(int) puntoFinal.getX();
			this.p2Y=(int) puntoFinal.getY();
			
			if (validarBordes()){
				// muestro por consola los puntos y el error seleccionados
				System.out.println("Punto Inicial: X "+p1X+" Y "+p1Y);
				System.out.println("Punto Final: X "+p2X+" Y "+p2Y);
				System.out.println("Error: "+error);
				
				cargarListaBordesExternosEimagenFi();
				cargarListaBordesInternosEimagenFi();
				cargarPuntosInternosEnImagenFi();
				calcularColorPromedio();
				
			}else{
				respuesta=false;
			}
			return respuesta;
		}
		
		private void calcularColorPromedio() {
			int contadorPixeles=0;
			int red=0;
			int green=0;
			int blue=0;
			
			for (int i=p1X;i<=p2X;i++){
				for (int j=p1Y;j<=p2Y;j++){
					contadorPixeles++;
					red+=resultado.getRed(i,j);
					green+=resultado.getGreen(i,j);
					blue+=resultado.getBlue(i,j);
				}
			}
			if (contadorPixeles!=0){
				red=red/contadorPixeles;
				green=green/contadorPixeles;
				blue=blue/contadorPixeles;
				this.colorObjeto=new Color(red,green,blue);
			}else{
				this.colorObjeto=new Color(0,0,0);
			}
		}

		private void cargarPuntosInternosEnImagenFi() {
			
			for (int i=p1X+1;i<=p2X-1;i++){
				for (int j=p1Y+1;j<=p2Y-1;j++){
					this.imagenFi[i][j]=3;
				}
			}
		}

		private void cargarListaBordesInternosEimagenFi() {
			Point punto1=null;
			Point punto2=null;
			//agrego puntos de bordes internos superior e inferior
			for (int i=p1X;i<=p2X;i++){
				punto1=new Point();
				punto2=new Point();
				punto1.setLocation(i,p1Y);
				this.imagenFi[i][p1Y]=1;
				punto2.setLocation(i,p2Y);
				this.imagenFi[i][p2Y]=1;
				this.Lint.add(punto1);
				this.Lint.add(punto2);
			}
			//agrego puntos de bordes internos izquierdo y derecho
			for (int j=p1Y+1;j<=p2Y-1;j++){
				punto1=new Point();
				punto2=new Point();
				punto1.setLocation(p1X,j);
				this.imagenFi[p1X][j]=1;
				punto2.setLocation(p2X,j);
				this.imagenFi[p2X][j]=1;
				this.Lint.add(punto1);
				this.Lint.add(punto2);
			}
		}

		private void cargarListaBordesExternosEimagenFi(){
			Point punto1=null;
			Point punto2=null;
			
			//agrego puntos de bordes externos superior e inferior
			for (int i=p1X-1;i<=p2X+1;i++){
				punto1=new Point();
				punto2=new Point();
				punto1.setLocation(i,p1Y-1);
				this.imagenFi[i][p1Y-1]=-1;
				punto2.setLocation(i,p2Y+1);
				this.imagenFi[i][p2Y+1]=-1;
				this.Lext.add(punto1);
				this.Lext.add(punto2);
			}
			//agrego puntos de bordes externos izquierdo y derecho
			for (int j=p1Y;j<=p2Y;j++){
				punto1=new Point();
				punto2=new Point();
				punto1.setLocation(p1X-1,j);
				this.imagenFi[p1X-1][j]=-1;
				punto2.setLocation(p2X+1,j);
				this.imagenFi[p2X+1][j]=-1;
				this.Lext.add(punto1);
				this.Lext.add(punto2);
			}
		}		
		
		private boolean validarBordes() {
			boolean valido=true;
			if (p1X<p2X){
				if (p1X==0) p1X++;
				if (p2X==ancho-1) p2X--;
			}else {
				valido=false;
				System.out.println("Region Inicial - Componente X erronea");
			}
			if (p1Y<p2Y){
				if (p1Y==0) p1Y++;
				if (p2Y==alto-1) p2Y--;
			}else {
				valido=false;
				System.out.println("Region Inicial - Componente Y erronea");
			}
			return valido;
		}

		private int funcionFuerzaVelocidad_D(Color colorX,Color colorObjeto,Integer error){
			int respuesta=1;
			int difRed=colorX.getRed() - colorObjeto.getRed();
			int difGreen=colorX.getGreen() - colorObjeto.getGreen();
			int difBlue=colorX.getBlue() - colorObjeto.getBlue();
			
			double norma=Math.sqrt(Math.pow(difRed,2)+Math.pow(difGreen,2)+Math.pow(difBlue,2));
			
			if (norma>error){
				respuesta=-1;
			}
			
			return respuesta;
		}
		
		private int[][] inicializarMatrizFi(){
			
			int [][] imagenFi = new int[ancho][alto];
			
			for (int i=0;i<ancho;i++){
				for (int j=0;j<alto;j++){
					imagenFi[i][j]=-3;
				}
			}
			
			return imagenFi;
		}
		
		private Imagen clonarImagen(Imagen original){
			
			Imagen resultado = new Imagen(original.getWidth(),original.getHeight());
			
			for (int i=0;i<original.getWidth();i++){
				for (int j=0;j<original.getHeight();j++){
					resultado.setRGB(i,j,original.getRGB(i,j));
				}
			}
			return resultado;
		}
}
