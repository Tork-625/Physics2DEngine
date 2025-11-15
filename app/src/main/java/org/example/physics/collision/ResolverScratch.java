package org.example.physics.collision;

import org.example.physics.utils.Vector2D;

public class ResolverScratch {
	
    public Vector2D normal = new Vector2D(0,0);
    
    public float e  = 0f;
	
    public Vector2D rA = new Vector2D(0,0);
    public Vector2D rB = new Vector2D(0,0);
    
    public Vector2D tA = new Vector2D(0,0);
    public Vector2D tB = new Vector2D(0,0);
    
    public Vector2D vA = new Vector2D(0,0);
    public Vector2D vB = new Vector2D(0,0);
    
    public Vector2D relVel = new Vector2D(0,0);
    
    public float contactVel = 0f;
    
    public float rAn = 0f;
    public float rBn = 0f;
    public float denom = 0f;
    public float j = 0f;
    
    public Vector2D impulse = new Vector2D(0,0);
    
    public Vector2D tangent = new Vector2D(0,0);
    public Vector2D nProj = new Vector2D(0,0);

    public float rAt = 0f;
    public float rBt = 0f;
    
    public float jt = 0f;

    public Vector2D frictionImpulse = new Vector2D(0,0);
    
    public Vector2D contact = new Vector2D(0,0);
    
	
	public void reset() {
	    normal.set(0,0);
	    rA.set(0,0);
	    rB.set(0,0);
	    vA.set(0,0);
	    vB.set(0,0);
	    relVel.set(0,0);
	    contactVel = 0f;
	    rAn = 0f;
	    rBn = 0f;
	    denom = 0f;
	    j = 0f;
	    impulse.set(0,0);
	    tangent.set(0,0);
	    nProj.set(0,0);
	    rAt = 0f;
	    rBt = 0f;
	    jt = 0f;
	    frictionImpulse.set(0,0);
	    contact.set(0,0);	
	 }
}
