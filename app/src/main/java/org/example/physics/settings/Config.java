package org.example.physics.settings;

import org.example.app.Engine;

public class Config {
	
	public static final int FPS = 600;											// Frames Per Second
	
	public static float g = 500;												// Gravitational Constant
	
	public static final int epsilon = 2;										// RDP Algorithm's Epsilon
		
	public static int strokeThickness = Math.round(5 * Engine.globalScale);		// Stroke Polygon Thickness
	
	public static final float penetrationCorrectionFactor = 0.6f;				// Separates the objects instantly by this factor if they penetrate within each other
	
	public static final float verySmallDistance = 0.2f;							// Any Thing below this distance is considered to be in contact with polygon (in a polygon -polygon collision)
	
	public static final int movingForce = 700;									// The force that gets applied on the first added object to the world on pressing W,S,A,D on the Keyboard.
	
	public static final float staticFriction = 0.8f;							// Static friction coefficient
	public static final float dynamicFriction = 0.6f;							// Dynamic friction coefficient

	public static final int defaultSolverIterations = 35;        				// Value never changes
	public static int solverIterations = defaultSolverIterations;        		// Perform the collision Resolution Step these many times per frame

	public static final float linearThreshold_X = 0.07f;  						// linear Velocity in X Direction below this number will be clamped to 0.
	public static final float linearThreshold_Y = 0.07f;  						// linear Velocity in Y Direction below this number will be clamped to 0.
	public static final float angularThreshold = 1e-3f; 						// Angular Velocity below this number will be clamped to 0.

}
