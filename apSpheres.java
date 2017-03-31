import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class AppSpheres extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		double d = 2.;	//dimension
		
		//Initial three circles
		final Circle c1 = new Circle();
		final Circle c2 = new Circle();
		final Circle c3 = new Circle();
		
		//Location and radius of initial three circles
		c1.setCenterX(450); c1.setCenterY(500); c1.setRadius(50);
		c2.setCenterX(550); c2.setCenterY(500); c2.setRadius(50);
		c3.setCenterX(500); c3.setCenterY(586.6025); c3.setRadius(50);
		c1.setStroke(Color.BLACK); c1.setFill(Color.WHITE);
		c2.setStroke(Color.BLACK); c2.setFill(Color.WHITE);
		c3.setStroke(Color.BLACK); c3.setFill(Color.WHITE);
		
		//Array of circles
		Circle[] circles = {c1, c2, c3};
		
		//Summation of all curvatures
		double sum = 0;
		for(int i = 0; i < d + 1; i++) {
			sum +=  1 / circles[i].getRadius();
		}
		
		//Summation of all curvatures squared
		double sum1 = 0;
		for(int j = 0; j < d + 1; j++) {
			sum1 += Math.pow(1 / circles[j].getRadius(), 2);
		}
		
		//A, B, and C
		double a = (d-1)/d;
		double b = - (2./d) * sum;
		double c = sum1 - ((1. / d) * Math.pow(sum, 2));
		
		//Radius using quad formula for inner/outer circle
		double radOut = 1.0 / ((-b + Math.sqrt(Math.pow(b, 2) - (4*a*c)))/(2*a));
		double radIn = 1.0 / ((-b - Math.sqrt(Math.pow(b, 2) - (4*a*c)))/(2*a));
		
		//Create inner/outer circles
		final Circle cOut = new Circle();
		final Circle cIn = new Circle();
		cOut.setRadius(radOut); cIn.setRadius(radIn);
		System.out.printf("Outer Circle: %f, Inner Circle: %f", radOut, radIn);
		
		//Draw to screen
		Pane pane = new Pane();
		pane.getChildren().add(c1);
		pane.getChildren().add(c2);
		pane.getChildren().add(c3);
//		pane.getChildren().add(cOut);
//		pane.getChildren().add(cIn);
		Scene scene = new Scene(pane, 1000, 1000);
		primaryStage.setTitle("Circle!");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}
	
	//Work in progress
	public void iterations(double d, Circle[] cir, double findRad) {
		double cur = 1 / findRad;
		double A = (1 - (1 / d));
	}
}