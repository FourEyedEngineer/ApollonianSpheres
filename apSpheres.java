import java.util.ArrayList;
import java.util.Collection;
import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class AppSpheres extends Application{
	private double d = 2.; //dimension
	private int iteration = 1;
	private Collection<ArrayList<Circle>> itC = new ArrayList<ArrayList<Circle>>(iteration+1); 
	private Collection<ArrayList<ArrayList<Circle>>> parentC = new ArrayList<ArrayList<ArrayList<Circle>>>(iteration+1); 
	
	Pane pane = new Pane();
	
	/**
	 * For finding the inner and outer circles
	 * (iteration 1)
	 * 
	 * @param C list of initial three circles
	 */
	private void inOutCircles(ArrayList<Circle> C) {
		//Radius for circles
		ArrayList<Double> rad = summRad(C);
		
		//Will return center point for inner/outer circles
		ArrayList<Double> gp = summLoc(rad.get(0), C.get(1).getRadius(), C);
		
		//Create inner/outer circles
		final Circle cOut = new Circle();
		final Circle cIn = new Circle();
		
		cOut.setRadius(-1 * rad.get(1)); cOut.setCenterX(gp.get(0)); cOut.setCenterY(gp.get(1));
		cOut.setStroke(Color.BLACK); cOut.setFill(Color.TRANSPARENT);
		cIn.setRadius(rad.get(0)); cIn.setCenterX(gp.get(0)); cIn.setCenterY(gp.get(1));
		cIn.setStroke(Color.BLACK); cIn.setFill(Color.WHITE);
		
		//Add to pane
		pane.getChildren().add(cOut);
		pane.getChildren().add(cIn);
	}
	
	/**
	 * 
	 * @param C
	 * @return
	 */
	private ArrayList<Double> summRad(ArrayList<Circle> C) {
		//Summation of the first three curvatures
		double sum = 0;
		for(int i = 0; i < d + 1; i++) {
			sum +=  1. / C.get(i).getRadius();
		}
		
		//Summation of the first three curvatures squared
		double sum1 = 0;
		for(int j = 0; j < d + 1; j++) {
			sum1 += Math.pow(1. / C.get(j).getRadius(), 2);
		}
		
		//A, B, and C
		double a = (d-1)/d;
		double b = - (2./d) * sum;
		double c = sum1 - ((1. / d) * Math.pow(sum, 2));
		
		//Radius using quad formula for inner/outer circle
		double pRad = 1. / ((-b + Math.sqrt(Math.pow(b, 2) - (4*a*c)))/(2*a));
		double nRad = (1. / ((-b - Math.sqrt(Math.pow(b, 2) - (4*a*c)))/(2*a)));

		ArrayList<Double> radii = new ArrayList<>();
		radii.add(pRad); 
		
		if(iteration == 1) {
			radii.add(nRad);
			return radii;
		}
		else {
			return radii;
		}
	}
	
	/**
	 * Will find center point of circle
	 * 
	 * @param r radius of circle
	 * @param rO radius of parent circle
	 * @param c list of parent circles
	 * @return list (x, y), center point for circle
	 */
	private ArrayList<Double> summLoc(double r, double rO, ArrayList<Circle> c) {
		double sum2 = 0;
		for(int k = 0; k < d + 1; k++) {
			sum2 += (1. / r) * (1. / c.get(k).getRadius()) * (c.get(k).getCenterY());
		}
		
		double sum2x = 0;
		for(int k = 0; k < d + 1; k++) {
			sum2x += (1. / r) * (1. / c.get(k).getRadius()) * (c.get(k).getCenterX());
		}
		
		//Summation 2.0
		double sum3 = 0;
		for(int l = 0; l < d + 1; l++) {
			sum3 += (Math.pow(1. / c.get(l).getRadius(), 2) * Math.pow(c.get(l).getCenterY(), 2));
		}
		
		double sum3x = 0;
		for(int l = 0; l < d + 1; l++) {
			sum3x += (Math.pow(1. / c.get(l).getRadius(), 2) * Math.pow(c.get(l).getCenterX(), 2));
		}
		
		//Summation 2.1
		double sum4 = 0;
		for(int m = 0; m < d + 1; m++) {
			for(int n = 0; n < d + 1; n++) {
				sum4 += (1. / c.get(m).getRadius()) * (1. / c.get(n).getRadius())
					* (c.get(m).getCenterY()) * (c.get(n).getCenterY());
			}
		}
		
		double sum4x = 0;
		for(int m = 0; m < d + 1; m++) {
			for(int n = 0; n < d + 1; n++) {
				sum4x += (1. / c.get(m).getRadius()) * (1. / c.get(n).getRadius())
					* (c.get(m).getCenterX()) * (c.get(n).getCenterX());
			}
		}
		
		double A = (1 - (1. / d)) * Math.pow((1. / r), 2);
		double By = -(2. / d) * (sum2); 		  double Bx = -(2. / d) * (sum2x);
		double Cy = (sum3 - ((1./d) * sum4)) - 2; double Cx = (sum3x - ((1./d) * sum4x)) - 2;
		
		double yp = (-By + Math.sqrt(Math.pow(By, 2) - (4*A*Cy))) / (2*A); 
		double yn = (-By - Math.sqrt(Math.pow(By, 2) - (4*A*Cy))) / (2*A);
		
		double xp = (-Bx + Math.sqrt(Math.pow(Bx, 2) - (4*A*Cx))) / (2*A);
		double xn = (-Bx - Math.sqrt(Math.pow(Bx, 2) - (4*A*Cx))) / (2*A);
		
		double[] p = {xp, xn, yp, yn};
		ArrayList<Double> gp = new ArrayList<>();
		
		double goalDis = r + rO;
		boolean works = false;
		for(int i = 0; i < 2; i++) {
			for(int j = 2; j < 4; j++) {
				double dis = Math.sqrt(Math.pow((p[i]-c.get(1).getCenterX()), 2) + Math.pow((p[j]-c.get(1).getCenterY()), 2));
				if (Math.abs(goalDis-dis) <= 1.0E-5) {
					works = true;
					gp.add(p[i]); gp.add(p[j]);
					break;
				}
			}
			if (works) break;
		}
		
		return gp;
	}
	
	//WORK IN PROGRESS
	public void iterations(int iter, ArrayList<Circle> c) {
		for(int i = 1; i < iter; i++) {
			if (i == 1) {
				inOutCircles(c);
			}
			
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		//Initial three circles
		final Circle c1 = new Circle();
		final Circle c2 = new Circle();
		final Circle c3 = new Circle();
		
		//Location and radius of initial three circles
		c1.setCenterX(450); c1.setCenterY(500); c1.setRadius(50.);
		c2.setCenterX(550); c2.setCenterY(500); c2.setRadius(50.);
		c3.setCenterX(500); c3.setCenterY(586.6025); c3.setRadius(50.);
		c1.setStroke(Color.BLACK); c1.setFill(Color.WHITE);
		c2.setStroke(Color.BLACK); c2.setFill(Color.WHITE);
		c3.setStroke(Color.BLACK); c3.setFill(Color.WHITE);
		
		//Array of circles
		ArrayList<Circle> circles = new ArrayList<>();
		circles.add(c1); circles.add(c2); circles.add(c3);
		itC.add(circles);
		
		//Add to pane
		pane.getChildren().add(c1);
		pane.getChildren().add(c2);
		pane.getChildren().add(c3);
		
		//iterations(iteration, circles....);
		inOutCircles(circles);
		
		//Draw 2D Rep
		Scene scene = new Scene(pane, 1000, 1000);
		primaryStage.setTitle("Circle!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}	
