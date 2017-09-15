package Fractal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This program will show a 2D representation of Apollonian spheres.
 * 
 * @author Arianna Burch, Edgar Romero Fuentes, Janine Thomas
 */
public class ApGasket extends Application {
	private double d = 2.; //dimension
	private int iteration = 0; //number of iterations
	Map<Integer, ArrayList<OurCircle>> m = new HashMap<>(); //key = iteration, value = list of circles made in iteration
	private Color[] set = new Color[3]; //For color scheme
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
		cOut.setStroke(set[1]); cOut.setFill(Color.TRANSPARENT);
		cIn.setRadius(r.get(0)); cIn.setCenterX(gp.get(0)); cIn.setCenterY(gp.get(1));
		cIn.setStroke(set[1]); cIn.setFill(Color.WHITE);	
		
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
	 * Will return radius of circle
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
		double nRad = 1. / ((-b - Math.sqrt(Math.pow(b, 2) - (4*a*c)))/(2*a));

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
		double By = -(2. / d) * (sum2), 		  Bx = -(2. / d) * (sum2x);
		double Cy = (sum3 - ((1./d) * sum4)) - 2, Cx = (sum3x - ((1./d) * sum4x)) - 2;
		
		//Quadratic formula to find positive and negative x/y values
		double xp = (-Bx + Math.sqrt(Math.pow(Bx, 2) - (4*A*Cx))) / (2*A);
		double xn = (-Bx - Math.sqrt(Math.pow(Bx, 2) - (4*A*Cx))) / (2*A);
		double yp = (-By + Math.sqrt(Math.pow(By, 2) - (4*A*Cy))) / (2*A); 
		double yn = (-By - Math.sqrt(Math.pow(By, 2) - (4*A*Cy))) / (2*A);
		
		ArrayList<Double> gp = new ArrayList<>();
		double[] xs = {xp, xn}, ys = {yp, yn};
		double x = 0, y = 0;
		
		//Test each combination of (x, y) values to find correct location
		BiPredicate<Double, Double> distance = (d1, d2) -> (Math.abs(d1-d2) <= 1.0E-4);
		for(int i = 0; i < xs.length; i++) { //Each x value
			for(int j = 0; j < ys.length; j++) { //Each y value
				for(int k = 0; k < c.size(); k++) { //Each parent circle
					double goalDis = Math.abs(r + c.get(k).getRadius());
					double dis = Math.sqrt(Math.pow((xs[i]-c.get(k).getX()), 2) + Math.pow((ys[j]-c.get(k).getY()), 2));
					if (distance.test(goalDis, dis)) {
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
	public void iterations(int iter) {
		for(int i = 0; i <= iter; i++) {
			if (i == 0) { //Initial three circles
				final Circle c1 = new Circle(300, 350, 200.);
				final Circle c2 = new Circle(700, 350, 200.);
				final Circle c3 = new Circle(500, 696.4102, 200.);
				c1.setStroke(set[0]); c1.setFill(Color.WHITE);
				c2.setStroke(set[0]); c2.setFill(Color.WHITE);
				c3.setStroke(set[0]); c3.setFill(Color.WHITE);
				
				pane.getChildren().add(c1);
				pane.getChildren().add(c2);
				pane.getChildren().add(c3);
				
				//Initial circles become OurCircle and added to arraylist of current iteration circles
				OurCircle C1 = new OurCircle(c1.getRadius(), c1.getCenterX(), c1.getCenterY());
				OurCircle C2 = new OurCircle(c2.getRadius(), c2.getCenterX(), c2.getCenterY());
				OurCircle C3 = new OurCircle(c3.getRadius(), c3.getCenterX(), c3.getCenterY());
				ArrayList<OurCircle> cir = new ArrayList<>();
				cir.add(C1); cir.add(C2); cir.add(C3);
				m.put(0, cir);
				continue;
			}
			if (i == 1) { //Special case
				inOutCircles(m.get(i-1));
				continue;
			}
			
			ArrayList<OurCircle> o = m.get(i - 1); //Array of prev iteration circles
			ArrayList<OurCircle> next = new ArrayList<>(); //Array for this iteration circles made
			
			for(int q = 0; q < o.size(); q++) {
				ArrayList<OurCircle> temp = new ArrayList<>();
				OurCircle prevC = o.get(q); //Previous circle
				temp = prevC.getParent(); //Prev circle's parent circles
				
				for(int k = 1; k < temp.size() - 1; k++) {
					for(int l = k + 1; l < temp.size(); l++) {
						ArrayList<OurCircle> newT = new ArrayList<>(); //Array for new circle parents
						ArrayList<Double> rad = new ArrayList<>(); //Radius
						ArrayList<Double> p = new ArrayList<>(); //Center Point
						
						//Add parent circles to arraylist, find radius and center point
						newT.add(temp.get(0)); newT.add(temp.get(k)); newT.add(temp.get(l));
						rad = summRad(newT, i);
						p = summLoc(rad.get(0), newT);
					
						//Create new circle and add to screen
						final Circle d = new Circle(p.get(0), p.get(1), rad.get(0));
						d.setStroke(set[i%3]); d.setFill(Color.WHITE);
						pane.getChildren().add(d);
					
						//Add circle to current iteration circles
						OurCircle D = new OurCircle(rad.get(0), p.get(0), p.get(1));
						newT.add(0, D); D.setParent(newT);
						next.add(D);
					}
				}
			}
			//Add list of created circles to current iteration
			m.put(i, next);
		}
	}
	
	/**
	 * Will use user input to determine number of iterations
	 * and color scheme.
	 * 
	 * @param iter number of iterations
	 * @param colorScheme list of three colors
	 * @param u field for user input
	 * @param b button initiate calculations
	 * @param t label for iterations
	 */
	private void getChoices(String iter, ChoiceBox<String> colorScheme, TextField u, Button b, Text t) {
		//Clear what is on screen and keep user nodes
		pane.getChildren().clear();
		pane.getChildren().addAll(t, u, colorScheme, b);
		
		//Get number of iterations
	    try { 
	    	iteration = Integer.parseInt(iter); 
	    } catch(NumberFormatException e) { 
	    	iteration = 0; 
	    } catch(NullPointerException e) {
	    	iteration = 0;
	    }
	    
	    //Get color scheme choice
	    char l = colorScheme.getValue().charAt(0);
	    if (l == 'R') {
	    	set[0] = Color.INDIANRED; set[1] = Color.BURLYWOOD; set[2] = Color.ORANGE;
	    }
	    else if (l == 'G') {
	    	set[0] = Color.LIMEGREEN; set[1] = Color.DEEPSKYBLUE; set[2] = Color.MEDIUMPURPLE;
	    }
	    else if (l == 'T') {
	    	set[0] = Color.KHAKI; set[1] = Color.LIGHTPINK; set[2] = Color.CORNFLOWERBLUE;
	    }
	    else {
	    	set[0] = Color.BLACK; set[1] = Color.BLACK; set[2] = Color.BLACK;
	    }
	    
	    //Calculate iterations
		iterations(iteration);
	}
	
	@Override
	public void start(Stage primaryStage) {		
		//Label for text box
		Text chooseI = new Text("Iterations:");
		chooseI.setTranslateX(100); chooseI.setTranslateY(920);
		
		//For user to enter number of iterations
		TextField userE = new TextField();
		userE.setPrefColumnCount(5);
		userE.setTranslateX(175); userE.setTranslateY(900);
		
		//To choose color scheme
		ChoiceBox<String> col = new ChoiceBox<>();
		col.getItems().addAll("Red, brown, orange", "Green, blue, purple",  "Tan, pink, blue", "Black and white");
		col.setTranslateX(100); col.setTranslateY(950);
		
		//Button to initiate calculations
		final Button btn = new Button();
		btn.setText("Go!");
		btn.setTranslateX(300); btn.setTranslateY(950);
		
		//Add items to screen
		pane.getChildren().addAll(chooseI, userE, col, btn);
		pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		btn.setOnAction(e -> getChoices(userE.getText(), col, userE, btn, chooseI));
		
		//Draw 2D Representation
		Scene scene = new Scene(pane, 1000, 1000);
		primaryStage.setTitle("Apollonian Circles!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}	