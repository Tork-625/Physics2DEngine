package org.example.physics.collision;

import org.example.physics.objects.Circle;
import org.example.physics.objects.PhysicsObject;
import org.example.physics.objects.Polygon;
import org.example.physics.settings.Config;
import org.example.physics.utils.Vector2D;


public class CollisionDetector {
	
	
	public static SATResult SAT(PhysicsObject bodyA, PhysicsObject bodyB, SATResult satResult, SATScratch satScratch) {
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		computeAABB(bodyA, satScratch.aabb_MAX_A, satScratch.aabb_MIN_A);
		computeAABB(bodyB, satScratch.aabb_MAX_B, satScratch.aabb_MIN_B);
		
		if(		satScratch.aabb_MAX_A.x < satScratch.aabb_MIN_B.x ||
				satScratch.aabb_MIN_A.x > satScratch.aabb_MAX_B.x ||
			    satScratch.aabb_MAX_A.y < satScratch.aabb_MIN_B.y ||
			    satScratch.aabb_MIN_A.y > satScratch.aabb_MAX_B.y	) {
			
			satResult.isColliding = false;
			return satResult;
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if(bodyA instanceof Circle && bodyB instanceof Circle) {
			satScratch.temp1.set(bodyA.position.x, bodyA.position.y);
			satScratch.temp2.set(bodyB.position.x, bodyB.position.y);
			
			float radiusA = ((Circle) bodyA).radius;
			float radiusB = ((Circle) bodyB).radius;
			float sum = radiusA + radiusB;
			
			float distance = satScratch.temp2.distance(satScratch.temp1);
			
			if(sum > distance) {
				satResult.isColliding = true;
				satResult.penetrationDepth = sum - distance;
				satScratch.temp2.subtract(satScratch.temp1);
				satScratch.temp2.normalizeSafely();
				satResult.collisionNormal.set(satScratch.temp2.x, satScratch.temp2.y); 
				
				/////////// CONTACT POINT CALCULATION ///////////
				 satScratch.temp2.scale(radiusA);
				 satScratch.temp2.add(satScratch.temp1);
				 satResult.contactPoint1.set(satScratch.temp2.x, satScratch.temp2.y);
				 satResult.noOfContacts = 1;
				/////////////////////////////////////////////////
				return satResult;
			}
			else{satResult.isColliding = false; return satResult;}
			
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		else if((bodyA instanceof Circle && bodyB instanceof Polygon) || (bodyA instanceof Polygon && bodyB instanceof Circle)) {
			PhysicsObject circleBody = (bodyA instanceof Circle)? bodyA: bodyB;
			PhysicsObject polygonBody = (bodyA instanceof Polygon)? bodyA: bodyB;
			
			float radius = ((Circle) circleBody).radius;
			
			for(int i = 0; i < polygonBody.worldVertices.length; i++) {
				int j = (i + 1) % polygonBody.worldVertices.length;
				
				satScratch.temp1.set(polygonBody.worldVertices[j].x - polygonBody.worldVertices[i].x,
	                     			 polygonBody.worldVertices[j].y - polygonBody.worldVertices[i].y);
				satScratch.temp1.perpendicular();
				satScratch.temp1.normalizeSafely();
				
				satScratch.bodyA_max = -Float.MAX_VALUE;	//polygon
				satScratch.bodyA_min = Float.MAX_VALUE;		//polygon
				satScratch.bodyB_min = Float.MAX_VALUE;		//circle
				satScratch.bodyB_max = -Float.MAX_VALUE;	//circle

				for(Vector2D vert: polygonBody.worldVertices) {
					satScratch.projection = vert.dot(satScratch.temp1);
					if(satScratch.bodyA_max < satScratch.projection) {satScratch.bodyA_max = satScratch.projection;}
					if(satScratch.bodyA_min > satScratch.projection) {satScratch.bodyA_min = satScratch.projection;}
				}

				satScratch.projection = circleBody.position.dot(satScratch.temp1);
				satScratch.bodyB_max = satScratch.projection + radius;
				satScratch.bodyB_min = satScratch.projection - radius;
				
				if(satScratch.bodyA_min > satScratch.bodyB_max || satScratch.bodyB_min > satScratch.bodyA_max) {satResult.isColliding = false; return satResult;}
				
				float depth = Math.min(satScratch.bodyA_max-satScratch.bodyB_min,satScratch.bodyB_max-satScratch.bodyA_min);
				if(satScratch.penetrationDepth > depth) {
					satScratch.penetrationDepth = depth;
					satScratch.collisionNormal.set(satScratch.temp1.x, satScratch.temp1.y);
				}
			}
			
			float minDistance = Float.MAX_VALUE;
			int idx = -1;
			float distance = 0;
			for(int i = 0; i < polygonBody.worldVertices.length; i++) {
				distance = polygonBody.worldVertices[i].distanceSq(circleBody.position); 
				if(minDistance > distance) {
					minDistance = distance;
					idx = i;
				}
			}
			
		if (idx == -1) {
			idx = 0;
		}

			satScratch.temp1.set(circleBody.position.x - polygonBody.worldVertices[idx].x, circleBody.position.y - polygonBody.worldVertices[idx].y);
			satScratch.temp1.normalizeSafely();
			
			
			satScratch.bodyA_max = -Float.MAX_VALUE;	//polygon
			satScratch.bodyA_min = Float.MAX_VALUE;		//polygon
			satScratch.bodyB_min = Float.MAX_VALUE;		//circle
			satScratch.bodyB_max = -Float.MAX_VALUE;	//circle
			
			for(int i = 0; i < polygonBody.worldVertices.length; i++) {
				satScratch.projection = polygonBody.worldVertices[i].dot(satScratch.temp1);
				if(satScratch.bodyA_max < satScratch.projection) {satScratch.bodyA_max = satScratch.projection;}
				if(satScratch.bodyA_min > satScratch.projection) {satScratch.bodyA_min = satScratch.projection;}
			}
			
			satScratch.projection = circleBody.position.dot(satScratch.temp1);
			satScratch.bodyB_max = satScratch.projection + radius;
			satScratch.bodyB_min = satScratch.projection - radius;
			
			if(satScratch.bodyA_min > satScratch.bodyB_max || satScratch.bodyB_min > satScratch.bodyA_max) {satResult.isColliding = false; return satResult;}
			
			float depth = Math.min(satScratch.bodyA_max-satScratch.bodyB_min,satScratch.bodyB_max-satScratch.bodyA_min);
			if(satScratch.penetrationDepth > depth) {
				satScratch.penetrationDepth = depth;
				satScratch.collisionNormal.set(satScratch.temp1.x, satScratch.temp1.y);
			}
			
			
			satScratch.temp1.set(circleBody.position.x, circleBody.position.y);
			satScratch.temp2.set(polygonBody.position.x, polygonBody.position.y);
			satScratch.temp1.subtract(satScratch.temp2); 

			if(satScratch.temp1.dot(satScratch.collisionNormal) < 0) {
			    satScratch.collisionNormal.scale(-1);
			}
			
			/////////// CONTACT POINT CALCULATION ///////////
			satScratch.temp2.set(satScratch.collisionNormal.x, satScratch.collisionNormal.y);
			satScratch.temp2.scale(-radius);
			satScratch.temp1.set(circleBody.position.x, circleBody.position.y);
			satScratch.temp1.add(satScratch.temp2);
			satResult.contactPoint1.set(satScratch.temp1.x, satScratch.temp1.y);
			satResult.noOfContacts = 1;
			/////////////////////////////////////////////////
			
			//If polygon was bodyA, flip the normal to point from A to B
			if(bodyA instanceof Circle) {
			    satScratch.collisionNormal.scale(-1);
			}
		
			
			satResult.isColliding = true;
			satResult.collisionNormal.set(satScratch.collisionNormal.x, satScratch.collisionNormal.y);
			satResult.penetrationDepth = satScratch.penetrationDepth;
			return satResult;
			
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		else {
			for(int objects = 0; objects < 2; objects++) {
				PhysicsObject body = (objects == 0)? bodyA : bodyB;
				
				for(int i = 0; i < body.worldVertices.length; i++) {
					int j = (i+1) % body.worldVertices.length;
					
					satScratch.temp1.set(body.worldVertices[i].x, body.worldVertices[i].y);
					satScratch.temp2.set(body.worldVertices[j].x, body.worldVertices[j].y);
					
					satScratch.temp1.subtract(satScratch.temp2);
					satScratch.temp1.perpendicular();
					satScratch.temp1.normalizeSafely();
					
					satScratch.bodyA_max = -Float.MAX_VALUE;	
					satScratch.bodyA_min = Float.MAX_VALUE;		
					satScratch.bodyB_min = Float.MAX_VALUE;	
					satScratch.bodyB_max = -Float.MAX_VALUE;
					
					for(Vector2D vert: bodyA.worldVertices) {
						satScratch.projection = vert.dot(satScratch.temp1);
						if(satScratch.bodyA_max < satScratch.projection) {satScratch.bodyA_max = satScratch.projection;}
						if(satScratch.bodyA_min > satScratch.projection) {satScratch.bodyA_min = satScratch.projection;}
					}
					for(Vector2D vert: bodyB.worldVertices) {
						satScratch.projection = vert.dot(satScratch.temp1);
						if(satScratch.bodyB_max < satScratch.projection) {satScratch.bodyB_max = satScratch.projection;}
						if(satScratch.bodyB_min > satScratch.projection) {satScratch.bodyB_min = satScratch.projection;}
					}
					
					if(satScratch.bodyA_min > satScratch.bodyB_max || satScratch.bodyB_min > satScratch.bodyA_max) {satResult.isColliding = false; return satResult;}
					else {satScratch.isColliding = true;}
					
					float depth = Math.min(satScratch.bodyA_max-satScratch.bodyB_min,satScratch.bodyB_max-satScratch.bodyA_min);
					if(satScratch.penetrationDepth > depth) {
						satScratch.penetrationDepth = depth;
						satScratch.collisionNormal.set(satScratch.temp1.x, satScratch.temp1.y);
					}
					
				}
			}
			
			float minDistanceSq = Float.MAX_VALUE;
			boolean foundSecondContact = false;
			
			for(int i = 0; i < bodyA.worldVertices.length; i++) {
				int j = (i+1) % bodyA.worldVertices.length;
				
				for(int k = 0; k < bodyB.worldVertices.length; k++) {
					float distanceSq = pointSegmentDistanceSquared(bodyB.worldVertices[k].x, bodyB.worldVertices[k].y,
																   bodyA.worldVertices[i].x, bodyA.worldVertices[i].y,
																   bodyA.worldVertices[j].x, bodyA.worldVertices[j].y);
					
					if(areValuesNearlyEqual(minDistanceSq, distanceSq) == true) {
						if(arePointsNearlyEqual(satScratch.contactPoint1.x, satScratch.contactPoint1.y, bodyB.worldVertices[k].x, bodyB.worldVertices[k].y) == false) {
							satScratch.contactPoint2.x = bodyB.worldVertices[k].x;
							satScratch.contactPoint2.y = bodyB.worldVertices[k].y;
							foundSecondContact = true;
						}
					}
					else if(distanceSq < minDistanceSq) {
						minDistanceSq = distanceSq;
						satScratch.contactPoint1.x = bodyB.worldVertices[k].x;
						satScratch.contactPoint1.y = bodyB.worldVertices[k].y;
					}
				}
			}
			
			
			for(int i = 0; i < bodyB.worldVertices.length; i++) {
				int j = (i+1) % bodyB.worldVertices.length;
				
				for(int k = 0; k < bodyA.worldVertices.length; k++) {
					float distanceSq = pointSegmentDistanceSquared(bodyA.worldVertices[k].x, bodyA.worldVertices[k].y,
																   bodyB.worldVertices[i].x, bodyB.worldVertices[i].y,
																   bodyB.worldVertices[j].x, bodyB.worldVertices[j].y);
					
					if(areValuesNearlyEqual(minDistanceSq, distanceSq) == true) {
						if(arePointsNearlyEqual(satScratch.contactPoint1.x, satScratch.contactPoint1.y, bodyA.worldVertices[k].x, bodyA.worldVertices[k].y) == false) {
							satScratch.contactPoint2.x = bodyA.worldVertices[k].x;
							satScratch.contactPoint2.y = bodyA.worldVertices[k].y;
							foundSecondContact = true;
						}
					}
					else if(distanceSq < minDistanceSq) {
						minDistanceSq = distanceSq;
						satScratch.contactPoint1.x = bodyA.worldVertices[k].x;
						satScratch.contactPoint1.y = bodyA.worldVertices[k].y;
					}
				}
			}
			
			satResult.contactPoint1.set(satScratch.contactPoint1.x, satScratch.contactPoint1.y);
			satResult.contactPoint2.set(satScratch.contactPoint2.x, satScratch.contactPoint2.y);
			satResult.noOfContacts = foundSecondContact? 2:1;

			satScratch.collisionNormal.normalizeSafely();
			
			satScratch.temp1.set(bodyA.position.x, bodyA.position.y);
			satScratch.temp2.set(bodyB.position.x, bodyB.position.y);
			satScratch.temp1.subtract(satScratch.temp2);
			
			if(satScratch.temp1.dot(satScratch.collisionNormal) > 0) {
				satScratch.collisionNormal.scale(-1);
			}
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		satResult.isColliding = satScratch.isColliding;
		satResult.collisionNormal.set(satScratch.collisionNormal.x, satScratch.collisionNormal.y);
		satResult.penetrationDepth = satScratch.penetrationDepth;
		return satResult;
	}
	
	
	
	
	public static float pointSegmentDistanceSquared(float pX, float pY, float aX, float aY, float bX, float bY) {
		
		float apX = pX - aX;
		float apY = pY - aY;
		
		float abX = bX - aX;
		float abY = bY - aY;
		
		float proj = apX * abX + apY * abY;
		float lengthSq = abX * abX + abY * abY;
		proj /= lengthSq;
		proj = Math.min(1, Math.max(0, proj));
		
		abX = abX * proj;
		abY = abY * proj;
		
		abX += aX;
		abY += aY;
		
		abX = abX - pX;
		abY = abY - pY;
		return (abX * abX + abY * abY);
	}
	
	public static boolean arePointsNearlyEqual(float aX, float aY, float bX, float bY) {
		float distance = (aX - bX) * (aX - bX) + (aY - bY) * (aY - bY);
		if(distance > Config.verySmallDistance) {
			return false;
		}
		return true;
	}
	
	public static boolean areValuesNearlyEqual(float x, float y) {
		if(Math.abs(x - y) > Config.verySmallDistance) {
			return false;
		}
		return true;
	}
	
	public static void computeAABB(PhysicsObject body, Vector2D max, Vector2D min) {
		if(body instanceof Circle) {
		    Circle c = (Circle) body;
			min.set(c.position.x - c.radius, c.position.y - c.radius);
			max.set(c.position.x + c.radius, c.position.y + c.radius);
		}
		else if(body instanceof Polygon) {
			for(Vector2D vertex: body.worldVertices) {
				if(vertex.x > max.x) {max.x = vertex.x;}
				if(vertex.x < min.x) {min.x = vertex.x;}
				if(vertex.y > max.y) {max.y = vertex.y;}
				if(vertex.y < min.y) {min.y = vertex.y;}
			}
		}
	}
	
}
