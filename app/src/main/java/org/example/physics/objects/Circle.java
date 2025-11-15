package org.example.physics.objects;

import java.util.List;

import org.example.physics.renderer.Renderer;
import org.example.physics.utils.Vector2D;
import org.example.renderer.DefaultRenderer;

// [NOTE] We've put the 2 vertices into the list just for visualization purposes, so later they can be removed it not needed.

public class Circle extends PhysicsObject{

	public static int numVertices = 2;
	public float radius;
	public float massDensity;

	public Renderer renderer;
	
	public Circle(Vector2D position,float radius, float massDensity, boolean active, float restitution, Renderer renderer) {
		super(position, 0, massDensity, active, numVertices, restitution);
		
		this.radius = radius;
		this.massDensity = massDensity;
		this.renderer = renderer;
		
		defineLocalVertices();
		getWorldVertices();
		calculateMass();
		calculateInertia();
	}

	public Circle(Vector2D position,float radius, float massDensity, boolean active, float restitution) {
		super(position, 0, massDensity, active, numVertices, restitution);
		
		this.radius = radius;
		this.massDensity = massDensity;
		
		defineLocalVertices();
		getWorldVertices();
		calculateMass();
		calculateInertia();
	}
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void draw() {
		if(renderer != null) {
			renderer.drawCircle(this);
		} else {
			DefaultRenderer.getInstance().drawCircle(this);
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void defineLocalVertices() {
		localVertices[0] = new Vector2D(0,0);
		localVertices[1] = new Vector2D(-radius,0);
	}
	//////////////////////////////////////////////////////////////////////////////////////////

	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void calculateMass() {
		float area = (float)Math.PI * this.radius * this.radius;
		mass = area * massDensity;
		if(mass == 0) {inverseMass = 0;}
		else {inverseMass = 1f/mass;}
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	public List<PhysicsObject> getChildren() {
		return List.of(this);
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void calculateInertia() {
		inertia = 0.5f * mass * radius * radius;
		inverseInertia = (inertia != 0)? 1f/inertia : 0f;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////
}
