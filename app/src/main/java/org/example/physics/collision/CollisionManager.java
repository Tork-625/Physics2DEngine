package org.example.physics.collision;

import java.util.ArrayList;
import java.util.List;

import org.example.physics.objects.PhysicsObject;
import org.example.physics.settings.Config;
import org.example.physics.utils.Vector2D;

public class CollisionManager {
	
	public static List<CollisionManifold> manifolds = new ArrayList<>();
	public static SATResult satResult = new SATResult(false, new Vector2D(0,0),0, new Vector2D(0,0), new Vector2D(0,0), 0);
	public static SATScratch satScratch = new SATScratch();
	public static float [][] contactPoints = new float[3][2];
	public static ResolverScratch resolverScratch = new ResolverScratch();

	public static void handleCollision(List<PhysicsObject> objectList, double timePerFrame) {
		
		int objectListSize = objectList.size();

		for(int i = 0; i < objectListSize - 1; i++) {
			PhysicsObject parentA = objectList.get(i);
			List<PhysicsObject> childrenA = objectList.get(i).getChildren();
			int childrenASize = childrenA.size();
			
			for(int j = i+1; j < objectListSize; j++) {
				PhysicsObject parentB = objectList.get(j);
				List<PhysicsObject> childrenB = objectList.get(j).getChildren();
				int childrenBSize = childrenB.size();
				
				for(int k = 0; k < childrenASize; k++) {
					for(int l = 0; l < childrenBSize; l++) {
						
						PhysicsObject bodyA = childrenA.get(k);
						PhysicsObject bodyB = childrenB.get(l);
						
						satScratch.reset();
						CollisionDetector.SAT(bodyA, bodyB, satResult, satScratch);

						if(satResult.isColliding) {		
							
							//Simple Positional Separation
							float totalInverseMass = parentA.inverseMass + parentB.inverseMass;
							if(totalInverseMass == 0) {continue;}
							
							float correctionX = satResult.collisionNormal.x * (satResult.penetrationDepth / totalInverseMass) * Config.penetrationCorrectionFactor;
							float correctionY = satResult.collisionNormal.y * (satResult.penetrationDepth / totalInverseMass) * Config.penetrationCorrectionFactor;
							
							if(parentA.active && parentB.active) {
								parentA.position.x -= correctionX * parentA.inverseMass; parentA.position.y -= correctionY * parentA.inverseMass;
								parentB.position.x += correctionX * parentB.inverseMass; parentB.position.y += correctionY * parentB.inverseMass;
							}
							else if(parentA.active && !parentB.active) {
								parentA.position.x -= satResult.collisionNormal.x * satResult.penetrationDepth * Config.penetrationCorrectionFactor;
								parentA.position.y -= satResult.collisionNormal.y * satResult.penetrationDepth * Config.penetrationCorrectionFactor;
							}
							else if(!parentA.active && parentB.active) {
								parentB.position.x += satResult.collisionNormal.x * satResult.penetrationDepth * Config.penetrationCorrectionFactor;
								parentB.position.y += satResult.collisionNormal.y * satResult.penetrationDepth * Config.penetrationCorrectionFactor;
							}
							
							manifolds.add(new CollisionManifold(bodyA, bodyB, satResult.collisionNormal, satResult.penetrationDepth, satResult.contactPoint1, satResult.contactPoint2, satResult.noOfContacts, parentA, parentB));

						}
					}
				}
			}
		}

		 
		// Sorting Contacts
		manifolds.sort((a, b) -> {
			float aY = (a.contactCount == 2) 
					? (a.contactPoint1[1] + a.contactPoint2[1]) * 0.5f 
					: a.contactPoint1[1];

			float bY = (b.contactCount == 2) 
					? (b.contactPoint1[1] + b.contactPoint2[1]) * 0.5f 
					: b.contactPoint1[1];
			return Float.compare(aY, bY);
		});


		// Resolving Collisions
		for(int iterations = 0; iterations < Config.solverIterations; iterations++){
			for (CollisionManifold manifold : manifolds) {
				resolverScratch.reset();
		    	CollisionResolver.resolveCollisions(manifold, resolverScratch);
			}
		}
		manifolds.clear();
	}
}
