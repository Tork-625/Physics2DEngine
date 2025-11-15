package org.example.physics.collision;

import org.example.physics.objects.PhysicsObject;
import org.example.physics.utils.Vector2D;

public class CollisionManifold {
	public PhysicsObject bodyA;
	public PhysicsObject bodyB;
	
	public PhysicsObject parentA;
	public PhysicsObject parentB;
	
	public float[] collisionNormal = new float[2];
	public float penetrationDepth;
	
	public float[] contactPoint1 = new float[2];
	public float[] contactPoint2 = new float[2];
	
	public int contactCount;
	
	
	public CollisionManifold(PhysicsObject bodyA, PhysicsObject bodyB, Vector2D collisionNormal, float penetrationDepth, Vector2D contactPoint1, Vector2D contactPoint2, int contactCount, PhysicsObject parentA, PhysicsObject parentB) {
		this.bodyA = bodyA;
		this.bodyB = bodyB;
		
		this.parentA = parentA;
		this.parentB = parentB;
		
		this.collisionNormal[0] = collisionNormal.x;
		this.collisionNormal[1] = collisionNormal.y;
		
		this.penetrationDepth = penetrationDepth;
		
		this.contactPoint1[0] = contactPoint1.x;
		this.contactPoint1[1] = contactPoint1.y;
		
		this.contactPoint2[0] = contactPoint2.x;
		this.contactPoint2[1] = contactPoint2.y;
		
		this.contactCount = contactCount;
	}
}
