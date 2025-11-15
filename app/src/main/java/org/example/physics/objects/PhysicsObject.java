package org.example.physics.objects;

import java.util.List;

import org.example.physics.settings.Config;
import org.example.physics.utils.Vector2D;
import org.example.physics.utils.Color;

/** Base Class for 2D Physics objects.*/

public abstract class PhysicsObject {
	
	public Vector2D position;
	public float angle, massDensity, angularVelocity;
	public boolean active;
	
	public Vector2D force;
	public Vector2D velocity;
	
	public Vector2D gravitationalForce = new Vector2D(0f , Config.g);
	
	public Vector2D [] worldVertices;
	public int [] xPoints;
	public int [] yPoints;
	public Vector2D[] localVertices;
	
	public float mass;
	public float inverseMass;
	
	public float inertia, inverseInertia;
	
	public float restitution;

	public float linearThreshold_X = Config.linearThreshold_X;  
	public float linearThreshold_Y = Config.linearThreshold_Y;  
	public float angularThreshold = Config.angularThreshold; 

	public Color color;
	public PhysicsObject parent = null;
	
	///////////////// Remove Later ///////////////
	public boolean isColliding = false;
	//////////////////////////////////////////////
	
	public PhysicsObject(Vector2D position, float angle, float massDensity, boolean active, int numVertices, float restitution) {
		this.position = position;
		this.angle = angle;
		this.massDensity = massDensity;
		this.active = active;
		
		worldVertices = new Vector2D[numVertices];
		xPoints = new int[numVertices];
		yPoints = new int[numVertices];
		localVertices = new Vector2D[numVertices];
		
		this.force = new Vector2D(0,0);
		this.velocity = new Vector2D(0,0);
		this.angularVelocity = 0;
		this.inertia = 0;
		this.inverseInertia = 0;
		
		this.restitution = restitution;
		
		for(int i = 0; i < worldVertices.length; i++) {
			worldVertices[i] = new Vector2D(0,0);
			xPoints[i] = 0;
			yPoints[i] = 0;
			localVertices[i] = new Vector2D(0,0);
		}

		getWorldVertices();
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void addForce(Vector2D newForce) {
		force.add(newForce);
	}
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void update(double timePerFrame) { 
		if (active != true) {return;}

		gravitationalForce.set(0,Config.g);
		
		// Adding all the Forces:-
    	force.x += gravitationalForce.x * mass;
		force.y += gravitationalForce.y * mass;

		
		// Calculating Acceleration due to force:-
		force.divide(mass); 
		
		
		// Calculation Velocity:-
		velocity.x += force.x * timePerFrame * 0.7;
		velocity.y += force.y * timePerFrame * 0.7;

		// Clamping Linear Velocity:-
		if (Math.abs(velocity.x) < linearThreshold_X) velocity.x = 0f;
		if (Math.abs(velocity.y) < linearThreshold_Y) velocity.y = 0f;

		//System.out.print("\nVel_x: " + velocity.x);
		//System.out.print("\nVel_y: " + velocity.y);

		// Calculation Position:-
		position.x += velocity.x * timePerFrame;
		position.y += velocity.y * timePerFrame;
		
		// Clamping Angular Velocity:-
		if (Math.abs(angularVelocity) < angularThreshold) angularVelocity = 0f;

		// Calculating angle:- (BTW the angular velocity every frame is calculated during the collision Resolution)
	    angle += Math.toDegrees(angularVelocity * timePerFrame * 0.9);
		//System.out.print("\n"+angularVelocity);
	    
		
		// Removing all the Forces:-
		force.x = 0f;
		force.y = 0f;
		
		getWorldVertices();
	}
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public abstract void draw();
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public Vector2D getCenter() {
		
		// Fall back for circles:-
		if (worldVertices == null || worldVertices.length == 0) {
			return position;
		}
		
		float cX = 0f;
		float cY = 0f;
		
		for(Vector2D vert : worldVertices){
			cX += vert.x;
			cY += vert.y;
		}
		
		return new Vector2D(cX/worldVertices.length, cY/worldVertices.length);
	}
	///////////////////////////////////////////////////////////////////////////////////////////////

	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public Vector2D[] getWorldVertices() {
		
		float cos = (float) Math.cos(Math.toRadians(angle));
		float sin = (float) Math.sin(Math.toRadians(angle));
		
		for(int i = 0; i < worldVertices.length; i++) {
			
			float rot_x = (cos * localVertices[i].x ) - (sin * localVertices[i].y);
			float rot_y = (sin * localVertices[i].x ) + (cos * localVertices[i].y);
			
			worldVertices[i].set(rot_x + position.x, rot_y + position.y);
			xPoints[i] = (int) worldVertices[i].x;
			yPoints[i] = (int) worldVertices[i].y;
		}
		
		return worldVertices;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public abstract void defineLocalVertices();
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public abstract void calculateMass();
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public abstract List<PhysicsObject> getChildren();
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public abstract void calculateInertia();
	///////////////////////////////////////////////////////////////////////////////////////////////
	
}
