package org.example.physics.collision;

import org.example.physics.utils.Vector2D;

public class SATScratch {
	public Vector2D temp1 = new Vector2D(0,0);
	public Vector2D temp2 = new Vector2D(0,0);
	public float projection;
	public float bodyA_max, bodyB_max, bodyA_min, bodyB_min;
	public float penetrationDepth;
	public Vector2D collisionNormal = new Vector2D(0,0);
	public boolean isColliding;
	
	public Vector2D contactPoint1 = new Vector2D(0,0);
	public Vector2D contactPoint2 = new Vector2D(0,0);
	public int noOfContacts;
	
	public Vector2D aabb_MAX_A = new Vector2D(-Float.MAX_VALUE, -Float.MAX_VALUE);
	public Vector2D aabb_MAX_B = new Vector2D(-Float.MAX_VALUE, -Float.MAX_VALUE);
	public Vector2D aabb_MIN_A = new Vector2D(Float.MAX_VALUE, Float.MAX_VALUE);
	public Vector2D aabb_MIN_B = new Vector2D(Float.MAX_VALUE, Float.MAX_VALUE);
	
	public void reset() {
		penetrationDepth = Float.MAX_VALUE;
		isColliding = false;
		collisionNormal.set(0,0);
		temp1.set(0,0);
		temp2.set(0,0);
		
		contactPoint1.set(0,0);
		contactPoint2.set(0,0);
		noOfContacts = 0;
		
		aabb_MAX_A.set(-Float.MAX_VALUE, -Float.MAX_VALUE);
		aabb_MAX_B.set(-Float.MAX_VALUE, -Float.MAX_VALUE);
		aabb_MIN_A.set(Float.MAX_VALUE, Float.MAX_VALUE);
		aabb_MIN_B.set(Float.MAX_VALUE, Float.MAX_VALUE);;
	}
}
