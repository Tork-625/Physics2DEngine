package org.example.physics.objects;

import java.util.List;

import org.example.physics.renderer.Renderer;
import org.example.physics.utils.Vector2D;
import org.example.renderer.DefaultRenderer;

public class Polygon extends PhysicsObject{
	
	public Vector2D [] vertexList;

	public Renderer renderer;

	public Polygon(Vector2D position, Vector2D[] vertexList, float angle, float massDensity, boolean active, float restitution, Renderer renderer) {
		super(position, angle, massDensity, active, vertexList.length, restitution);
		
		this.vertexList = vertexList;
		this.renderer = renderer;
		
		defineLocalVertices();
		getWorldVertices();
		calculateMass();
		calculateInertia();
	}

	public Polygon(Vector2D position, Vector2D[] vertexList, float angle, float massDensity, boolean active, float restitution) {
		super(position, angle, massDensity, active, vertexList.length, restitution);

		this.vertexList = vertexList;
		
		defineLocalVertices();
		getWorldVertices();
		calculateMass();
		calculateInertia();
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void draw() {
		if(renderer != null) {
			renderer.drawPolygon(this);
		} else {
			DefaultRenderer.getInstance().drawPolygon(this);
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void defineLocalVertices() {
		Vector2D centroid = computeCentroid(vertexList);
		
		for(int i = 0; i < vertexList.length; i++) {
			localVertices[i].x = vertexList[i].x - centroid.x;
			localVertices[i].y = vertexList[i].y - centroid.y;
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public Vector2D computeCentroid(Vector2D [] list) {
		float cX = 0;
		float cY = 0;
		for(Vector2D vert: list) {
			cX += vert.x;
			cY += vert.y;
		}
		
		return new Vector2D(cX/list.length, cY/list.length);
	}




	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void calculateMass() {
		float area = 0;
		for(int i = 0; i < vertexList.length; i++) {
			int next = (i + 1) % vertexList.length;
			area += localVertices[i].x * localVertices[next].y - localVertices[next].x * localVertices[i].y;
		}
		area = Math.abs(area * 0.5f);
		mass = massDensity * area;
		if(mass == 0) {inverseMass = 0;}
		else {inverseMass = 1f/mass;}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	public List<PhysicsObject> getChildren() {
		return List.of(this);
	}
	//////////////////////////////////////////////////////////////////////////////////////////

	

	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void calculateInertia() {
	    float numerator = 0f;
	    float denominator = 0f;

	    for (int i = 0; i < localVertices.length; i++) {
	        Vector2D v0 = localVertices[i];
	        Vector2D v1 = localVertices[(i + 1) % localVertices.length];

	        float cross = Math.abs(v0.x * v1.y - v1.x * v0.y);
	        numerator += cross * (v0.x * v0.x + v0.x * v1.x + v1.x * v1.x + v0.y * v0.y + v0.y * v1.y + v1.y * v1.y);
	        denominator += cross;
	    }

	    if (denominator == 0f) {
	        inertia = 0f;
	        inverseInertia = 0f;
	        return;
	    }

	    // Polygon moment of inertia about centroid
	    inertia = (mass / 6f) * (numerator / denominator);
	    inverseInertia = (inertia != 0f) ? 1f / inertia : 0f;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
}
