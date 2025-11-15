package org.example.physics.utils;

/**Creates a 2D Vector (taking in x and y coordinates) and contains several Functions to manipulate them.*/

public class Vector2D {
	
	public float x;
	public float y;
	
	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void add(Vector2D other) {
		x += other.x;
		y += other.y;
	}
	
	public void subtract(Vector2D other) {
		x -= other.x;
		y -= other.y;
	}
	
	public void divide(float scalar) {
		x /= scalar;
		y /= scalar;
	}
	
	
	public void scale(float scalar) {
		x *= scalar;
		y *= scalar;
	}
	
	public float length() {
		return (float) Math.sqrt(x*x + y*y);
	}
	
	public void normalize() {
		float len = (float) Math.sqrt(x*x + y*y);
		x /= len;
		y /= len;
	}
	
	public void normalizeSafely() {
		float len = (float) Math.sqrt(x*x + y*y);
		
		if(len < 0.01) {
			x = 1; y = 0;
		}
		else{
		x /= len;
		y /= len;}
	}
	
	public void multiply(Vector2D other) {
		x *= other.x;
		y *= other.y;
	}
	
	public float dot(Vector2D other) {
		return (x * other.x + y * other.y);
	}
	
	public float cross(Vector2D other) {
		return (x * other.y - y * other.x);
	}
	
	public float distance(Vector2D other) {
		float dx = x - other.x;
		float dy = y - other.y;
		return (float) Math.sqrt(dx*dx + dy*dy);
	}
	
	public float distanceSq(Vector2D other) {
		float dx = x - other.x;
		float dy = y - other.y;
		return dx*dx + dy*dy;
	}
	
	public void perpendicular() {		
		float tmp = x;
		x = -y;
		y = tmp;
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
