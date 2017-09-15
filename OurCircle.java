package Fractal;

import java.util.ArrayList;

/**
 * Creates a circle with a radius, center point,
 * and arraylist of circles that created it
 * 
 * @author Arianna Burch, Edgar Romero Fuentes, Janine Thomas
 *
 */
public class OurCircle {
	private double radius;
	private double x;
	private double y;
	private ArrayList<OurCircle> parent;
	
	public OurCircle(double rad, double X, double Y) {
		this.radius = rad;
		this.x = X;
		this.y = Y;
	}
	
	public OurCircle() {
		
	}
	
	public void setRadius(double r) {
		this.radius = r;
	}
	
	public void setX(double X) {
		this.x = X;
	}
	
	public void setY(double Y) {
		this.y = Y;
	}
	
	public void setParent(ArrayList<OurCircle> c) {
		this.parent = c;
	}	
	
	public double getRadius() {
		return radius;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public ArrayList<OurCircle> getParent() {
		return parent;
	}
}