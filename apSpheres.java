import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * This program will show a 2D representation of Apollonian spheres.
 * 
 * @author Janine Thomas
 */
public class AppSpheres extends Application {
	private double d = 2.; //dimension
	private int iteration = 6; //number of iterations
	//key is the iteration, value is the list of circles made in iteration
	Map<Integer, ArrayList<OurCircle>> m = new HashMap<>(); 
	Pane pane = new Pane();
	
	/**
	 * For finding the inner and outer circles
	 * (iteration 1)
	 * 
	 * @param C list of initial three circles
	 */
	private void inOutCircles(ArrayList<OurCircle> C) {
		//Radius for circles
		ArrayList<Double> r = summRad(C, 1);
		
		//Will return center point for inner/outer circles
		ArrayList<Double> gp = summLoc(r.get(0), C);
		
		//Create inner/outer circles
		final Circle cOut = new Circle();
		final Circle cIn = new Circle();
		cOut.setRadius(-1 * r.get(1)); cOut.setCenterX(gp.get(0)); cOut.setCenterY(gp.get(1));
		cOut.setStroke(Color.BLACK); cOut.setFill(Color.TRANSPARENT);
		cIn.setRadius(r.get(0)); cIn.setCenterX(gp.get(0)); cIn.setCenterY(gp.get(1));
		cIn.setStroke(Color.BLACK); cIn.setFill(Color.WHITE);
		
		System.out.printf("Radius: %f, X: %f, Y: %f%n", cIn.getRadius(), cIn.getCenterX(), cIn.getCenterY());
		System.out.printf("Radius: %f, X: %f, Y: %f%n", cOut.getRadius(), cOut.getCenterX(), cOut.getCenterY());	
		
		//Add to pane
		pane.getChildren().add(cOut);
		pane.getChildren().add(cIn);
		
		//Creates OurCircle version of circles to pass into map and use for further iterations
		OurCircle ci = new OurCircle(r.get(0), cIn.getCenterX(), cIn.getCenterY());
		OurCircle co = new OurCircle(r.get(1), cIn.getCenterX(), cIn.getCenterY());
		
		ArrayList<OurCircle> inC = new ArrayList<>(); 
		inC.add(ci); inC.add(C.get(0)); inC.add(C.get(1)); inC.add(C.get(2));
		ci.setParent(inC);
		
		ArrayList<OurCircle> outC = new ArrayList<>(); 
		outC.add(co); outC.add(C.get(0)); outC.add(C.get(1)); outC.add(C.get(2));
		co.setParent(outC);

		ArrayList<OurCircle> some = new ArrayList<>(); some.add(ci); some.add(co);
		m.put(1, some);
	}
	
	/**
	 * Summation formulas to calculate the quadratic formula
	 * variables. Quadratic formula will return radius of
	 * circle.
	 * 
	 * @param C ArrayList of parent circles
	 * @param iter current iteration
	 * @return radii ArrayList of radius for circle (will 
	 * 		   only be size 2 for first iteration, otherwise
	 * 		   it's size 1)
	 */
	private ArrayList<Double> summRad(ArrayList<OurCircle> C, int iter) {
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
		
		//Radius using quadratic formula
		double pRad = 1. / ((-b + Math.sqrt(Math.pow(b, 2) - (4*a*c)))/(2*a));
		double nRad = (1. / ((-b - Math.sqrt(Math.pow(b, 2) - (4*a*c)))/(2*a)));

		ArrayList<Double> radii = new ArrayList<>();
		radii.add(pRad); 
		
		//Add negative radius to radii for iteration 1 otherwise just use 
		//positive radius
		if(iter == 1) {
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
	 * @param r radius of new circle
	 * @param c list of parent circles
	 * @return gp center point for circle (x, y)
	 */
	private ArrayList<Double> summLoc(double r, ArrayList<OurCircle> c) {
		//Summation for y values
		double sum2 = 0;
		for(int k = 0; k < d + 1; k++) {
			sum2 += (1. / r) * (1. / c.get(k).getRadius()) * (c.get(k).getY());
		}
		
		double sum3 = 0;
		for(int l = 0; l < d + 1; l++) {
			sum3 += (Math.pow(1. / c.get(l).getRadius(), 2) * Math.pow(c.get(l).getY(), 2));
		}
		
		double sum4 = 0;
		for(int m = 0; m < d + 1; m++) {
			for(int n = 0; n < d + 1; n++) {
				sum4 += (1. / c.get(m).getRadius()) * (1. / c.get(n).getRadius())
					* (c.get(m).getY()) * (c.get(n).getY());
			}
		}
		
		//Summation for x values
		double sum2x = 0;
		for(int k = 0; k < d + 1; k++) {
			sum2x += (1. / r) * (1. / c.get(k).getRadius()) * (c.get(k).getX());
		}
		
		double sum3x = 0;
		for(int l = 0; l < d + 1; l++) {
			sum3x += (Math.pow(1. / c.get(l).getRadius(), 2) * Math.pow(c.get(l).getX(), 2));
		}
	
		double sum4x = 0;
		for(int m = 0; m < d + 1; m++) {
			for(int n = 0; n < d + 1; n++) {
				sum4x += (1. / c.get(m).getRadius()) * (1. / c.get(n).getRadius())
					* (c.get(m).getX()) * (c.get(n).getX());
			}
		}
		
		//A, B, and C
		double A = (1 - (1./d)) * Math.pow((1. / r), 2);
		double By = -(2. / d) * (sum2); 		  double Bx = -(2. / d) * (sum2x);
		double Cy = (sum3 - ((1./d) * sum4)) - 2; double Cx = (sum3x - ((1./d) * sum4x)) - 2;
		
		//Quadratic formula to find positive and negative x/y values
		double yp = (-By + Math.sqrt(Math.pow(By, 2) - (4*A*Cy))) / (2*A); 
		double yn = (-By - Math.sqrt(Math.pow(By, 2) - (4*A*Cy))) / (2*A);
		double xp = (-Bx + Math.sqrt(Math.pow(Bx, 2) - (4*A*Cx))) / (2*A);
		double xn = (-Bx - Math.sqrt(Math.pow(Bx, 2) - (4*A*Cx))) / (2*A);
		
		double[] xs = {xp, xn}; double[] ys = {yp, yn};
		ArrayList<Double> gp = new ArrayList<>();
		double x = 0, y = 0;
		
		//Test each combination of (x, y) values to find correct location
		for(int i = 0; i < xs.length; i++) { //Each x value
			for(int j = 0; j < ys.length; j++) { //Each y value
				for(int k = 0; k < c.size(); k++) { //Each parent circle
					double goalDis = Math.abs(r + c.get(k).getRadius());
					double dis = Math.sqrt(Math.pow((xs[i]-c.get(k).getX()), 2) + Math.pow((ys[j]-c.get(k).getY()), 2));
					if (Math.abs(goalDis-dis) <= 1.0E-4) {
						x = xs[i]; y = ys[j];
						break;
					}
				}
			}
		}
		
		gp.add(x); gp.add(y);
		return gp;
	}
	
	/**
	 * Loops through each iteration adding all the circles
	 * to the pane and each iteration to the map.
	 * 
	 * @param iter number of iterations
	 * @param c initial 3 circles
	 */
	public void iterations(int iter, ArrayList<OurCircle> c) {
		for(int i = 1; i <= iter; i++) {
			if (i == 1) {
				inOutCircles(c);
				iteration++;
				continue;
			}
			ArrayList<OurCircle> o = m.get(i - 1); //Array of prev iteration circles
			ArrayList<OurCircle> next = new ArrayList<>(); //Array for this iteration circles made
			for(int m = 0; m < o.size(); m++) {
				ArrayList<OurCircle> temp = new ArrayList<>(); //Array of prev circle's parents and itself
				OurCircle prevC = o.get(m);
				temp = prevC.getParent();
				for(int k = 1; k < temp.size() - 1; k++) {
					for(int l = k + 1; l < temp.size(); l++) {
						ArrayList<OurCircle> newT = new ArrayList<>(); //Array for new circle parents
						ArrayList<Double> rad = new ArrayList<>(); //Radius
						ArrayList<Double> p = new ArrayList<>(); //Center Point
						newT.add(temp.get(0)); newT.add(temp.get(k)); newT.add(temp.get(l));
						rad = summRad(newT, i);
						p = summLoc(rad.get(0), newT);
					
						final Circle d = new Circle(p.get(0), p.get(1), rad.get(0));
						d.setStroke(Color.BLACK); d.setFill(Color.WHITE);
						pane.getChildren().add(d);
						System.out.printf("Radius: %f, X: %f, Y: %f%n", d.getRadius(), d.getCenterX(), d.getCenterY());
					
						OurCircle D = new OurCircle(rad.get(0), p.get(0), p.get(1));
						newT.add(0, D); D.setParent(newT);
						next.add(D);
					}
				}
			}
			
			m.put(i, next);
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		//Initial three circles
		final Circle c1 = new Circle();
		final Circle c2 = new Circle();
		final Circle c3 = new Circle();
		
		//Location and radius of initial three circles
		c1.setCenterX(300); c1.setCenterY(350); c1.setRadius(200.);
		c2.setCenterX(700); c2.setCenterY(350); c2.setRadius(200.);
		c3.setCenterX(500); c3.setCenterY(696.4102); c3.setRadius(200.);
		c1.setStroke(Color.BLACK); c1.setFill(Color.WHITE);
		c2.setStroke(Color.BLACK); c2.setFill(Color.WHITE);
		c3.setStroke(Color.BLACK); c3.setFill(Color.WHITE);
		
		pane.getChildren().add(c1);
		pane.getChildren().add(c2);
		pane.getChildren().add(c3);
		
		//Array of initial circles
		OurCircle C1 = new OurCircle(c1.getRadius(), c1.getCenterX(), c1.getCenterY());
		OurCircle C2 = new OurCircle(c2.getRadius(), c2.getCenterX(), c2.getCenterY());
		OurCircle C3 = new OurCircle(c3.getRadius(), c3.getCenterX(), c3.getCenterY());
		ArrayList<OurCircle> c = new ArrayList<>();
		c.add(C1); c.add(C2); c.add(C3);
		m.put(0, c);
		
		//iterations
		iterations(iteration, c);
		
		//Draw 2D Representation
		Scene scene = new Scene(pane, 1000, 1000);
		primaryStage.setTitle("Circle!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}	
