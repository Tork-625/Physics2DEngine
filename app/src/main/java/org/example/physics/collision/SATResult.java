package org.example.physics.collision;


import org.example.physics.utils.Vector2D;

public class SATResult {
	
	public boolean isColliding;
	public Vector2D collisionNormal;
	public float penetrationDepth;
	
	public Vector2D contactPoint1;
	public Vector2D contactPoint2;
	public int noOfContacts;
	
	public SATResult(boolean isColliding, Vector2D collisionNormal, float penetrationDepth, Vector2D contactPoint1, Vector2D contactPoint2, int noOfContacts) {
		this.isColliding = isColliding;
		this.collisionNormal = collisionNormal;
		this.penetrationDepth = penetrationDepth;
		
		this.contactPoint1 = contactPoint1;
		this.contactPoint2 = contactPoint2;
		this.noOfContacts = noOfContacts;
	}
}
