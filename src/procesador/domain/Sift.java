package procesador.domain;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.BasicMatcher;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.MatchingUtilities;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.model.fit.RANSAC;

public class Sift {

	private static int iteraciones = 1500;
	private static Double limite = 5.0;
	private static Double porcentaje = 0.5;
	private static int limiteMatches = 8;

	public static void aplicar(File img1, File img2) throws Exception {
		ingresarValores();
		
		MBFImage query = ImageUtilities.readMBF(img1);
		MBFImage target = ImageUtilities.readMBF(img2);

		DoGSIFTEngine engine = new DoGSIFTEngine();
		
		//encuentra los puntos caracteristicos de la imagen 1
		LocalFeatureList<Keypoint> queryKeypoints  = engine.findFeatures(query.flatten());
		//encuentra los puntos caracteristicos de la imagen 2
		LocalFeatureList<Keypoint> targetKeypoints = engine.findFeatures(target.flatten());
		
		LocalFeatureMatcher<Keypoint> matcher = new BasicMatcher<Keypoint>(75);
		
		matcher.setModelFeatures(queryKeypoints);
		matcher.findMatches(targetKeypoints);

		RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(
				limite, iteraciones, new RANSAC.PercentageInliersStoppingCondition(porcentaje));
		matcher = new ConsistentLocalFeatureMatcher2d<Keypoint>(
				new FastBasicKeypointMatcher<Keypoint>(limiteMatches), modelFitter);

		matcher.setModelFeatures(queryKeypoints);
		matcher.findMatches(targetKeypoints);

		MBFImage consistentMatches = MatchingUtilities.drawMatches(query,
				target, matcher.getMatches(), RGBColour.BLUE);
		
		JOptionPane.showMessageDialog(null, 
				"Descriptores de la imagen " + img1.getName() + ": " +  String.valueOf(queryKeypoints.size()) + "\n" +
				"Descriptores de la imagen " + img2.getName() + ": " +  String.valueOf(targetKeypoints.size()) + "\n" +	
				"Coincidencias entre descriptores: " + String.valueOf(matcher.getMatches().size()));
		DisplayUtilities.display(consistentMatches);
	}

	private static void ingresarValores() {
			JTextField cantidadIteraciones = new JTextField();  //1500
			JTextField porcentajeParada = new JTextField();     //0.5
			JTextField limiteComparacion = new JTextField();    //8
			JTextField limiteEstimador = new JTextField();      //0.5
			Object[] message = {
					"Iteraciones:", cantidadIteraciones,
					"porcentaje:", porcentajeParada,
					"limite de comparaciones:", limiteComparacion,
					"limite estimador:", limiteEstimador,				
			};

			int option = JOptionPane.showConfirmDialog(null, message, "Ingrese los Valores", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION)
			{
				iteraciones = Integer.valueOf(cantidadIteraciones.getText());
				limite = Double.valueOf(limiteEstimador.getText());
				porcentaje = Double.valueOf(porcentajeParada.getText());
				limiteMatches = Integer.valueOf(limiteComparacion.getText());}
	}

}

