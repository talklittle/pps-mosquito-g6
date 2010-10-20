package mosquito.g6;

import java.awt.geom.Point2D;

import mosquito.sim.Light;

public class HelperLight {
	private double x, y, d, t, s;
	private int phase = -1;
	private HelperLight base = null;
	
	public HelperLight(double x, double y, double d, double t, double s) {
		this.x = x;
		this.y = y;
		this.d = d;
		this.t = t;
		this.s = s;
	}
	
	public Light getLight() {
		return new Light(x, y, d, t, s);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}

	public double getT() {
		return t;
	}

	public void setT(double t) {
		this.t = t;
	}

	public double getS() {
		return s;
	}

	public void setS(double s) {
		this.s = s;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

	public HelperLight getBase() {
		return base;
	}

	public void setBase(HelperLight base) {
		this.base = base;
	}
	
	public Point2D getPoint(){
		return new Point2D.Double(x, y);
	}
	
}
