package org.example.physics.objects;

import java.util.ArrayList;
import java.util.List;

import org.example.physics.utils.Vector2D;

public class CompoundObject extends PhysicsObject{
	
	public List<PhysicsObject> children;
	private List<Vector2D> localPositionOffsets;
	
	public boolean isColliding = false;

	public CompoundObject(float angle, boolean active, float restitution) {
		super(new Vector2D(0,0), angle, 0, active, 0, restitution);

		children = new ArrayList<>();
		localPositionOffsets = new ArrayList<>();

	}
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	public void update(double timePerFrame) {
		super.update(timePerFrame);
		
		float rad = (float) Math.toRadians(this.angle);
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);
		
		for(int i = 0; i < children.size(); i++) { 					// [REMOVED THE NULL CHECK FOR ONCE IN A MILLION ERROR] -> if(children.get(i) != null && localPositionOffsets != null) {
			Vector2D offset = localPositionOffsets.get(i);
			float rotatedOffsetX = offset.x * cos - offset.y * sin;
			float rotatedOffsetY = offset.x * sin + offset.y * cos;
			
			children.get(i).position.x = this.position.x + rotatedOffsetX;
			children.get(i).position.y = this.position.y + rotatedOffsetY;
			
			children.get(i).angle = this.angle;
			
			//children.get(i).update(timePerFrame);
			children.get(i).getWorldVertices();
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void draw() {
		for (int i = 0; i < children.size(); i++) {
		    children.get(i).draw();
		}
	}

	
	@Override
	public void defineLocalVertices() {
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	/** IMPORTANT: Whenever you add a new child object,
	make sure to call these three methods immediately after:
	calculateMass();
	calculateCenterOfMass();
	calculateLocalPositionOffsets();
	calculateInertia();**/
	public void addChild(PhysicsObject child) {
		children.add(child);
		child.parent = this; 

		calculateMass();
		calculateCenterOfMass();
		calculateLocalPositionOffsets();
		calculateInertia();
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	public void removeChild(PhysicsObject child) {
		int childIndex = children.indexOf(child);
		if(childIndex != -1) {
			children.remove(childIndex);
			localPositionOffsets.remove(childIndex);
		}
		calculateMass();
		calculateCenterOfMass();
		calculateLocalPositionOffsets();
		calculateInertia();
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	public void calculateLocalPositionOffsets() {
		localPositionOffsets.clear();
		for(PhysicsObject child: children) {
			localPositionOffsets.add(new Vector2D( child.position.x - this.position.x, child.position.y - this.position.y));
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////

	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void calculateMass() {	
		float totalMass = 0;
		
		for(PhysicsObject child: children) {
			totalMass += child.mass;
		}
		
		mass = totalMass;
		if(mass == 0) {inverseMass = 0;}
		else {inverseMass = 1f/mass;}
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	public void calculateCenterOfMass() {
		float comX = 0;
		float comY = 0;
		
		for(PhysicsObject child: children) {
			comX += child.position.x * child.mass;
			comY += child.position.y * child.mass;
		}
		
		if(mass == 0) {
			return;
		}
		
		position.x = comX / mass;
		position.y = comY / mass;
	
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public List<PhysicsObject> getChildren() {
		return children;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////

	

	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void calculateInertia() {
	    inertia = 0;
	    
	    for (PhysicsObject child : children) {

	        float childInertia = child.inertia;
	        float distanceSquared = child.position.distanceSq(this.position);
	        inertia += childInertia + child.mass * distanceSquared;
	    }
	    
	    inverseInertia = (inertia != 0) ? 1f / inertia : 0f;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////
}
