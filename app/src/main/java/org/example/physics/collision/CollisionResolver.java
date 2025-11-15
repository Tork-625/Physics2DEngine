package org.example.physics.collision;

import org.example.physics.settings.Config;

public class CollisionResolver {
	
	private static final float sf = Config.staticFriction;
	private static final float df = Config.dynamicFriction;

    public static void resolveCollisions(CollisionManifold manifold, ResolverScratch scratch) {

        // Damping 10% penetrationDepth Helped stabilizing Stacking of objects
        /////////////////////////////////////////////////////////////////////
        manifold.penetrationDepth *= 0.9;                                  //
        /////////////////////////////////////////////////////////////////////

    	scratch.normal.set(manifold.collisionNormal[0], manifold.collisionNormal[1]);

    	scratch.e = Math.min(manifold.parentA.restitution, manifold.parentB.restitution);

        for (int i = 0; i < manifold.contactCount; i++) {
        	
        	if(i == 0) {
        		scratch.contact.set(manifold.contactPoint1[0], manifold.contactPoint1[1]);
        	}
        	else {
        		scratch.contact.set(manifold.contactPoint2[0], manifold.contactPoint2[1]);
        	}

            // Rotation offsets from centers to contact
        	scratch.rA.set(scratch.contact.x - manifold.parentA.position.x, scratch.contact.y - manifold.parentA.position.y);
        	scratch.rB.set(scratch.contact.x - manifold.parentB.position.x, scratch.contact.y - manifold.parentB.position.y);

            // Calculating relative velocity at contact
        	scratch.vA.set(manifold.parentA.velocity.x, manifold.parentA.velocity.y);
        	scratch.tA.set(-manifold.parentA.angularVelocity * scratch.rA.y, manifold.parentA.angularVelocity * scratch.rA.x);
        	scratch.vA.add(scratch.tA);

        	scratch.vB.set(manifold.parentB.velocity.x, manifold.parentB.velocity.y);
            scratch.tB.set(-manifold.parentB.angularVelocity * scratch.rB.y, manifold.parentB.angularVelocity * scratch.rB.x);
            scratch.vB.add(scratch.tB);

            scratch.relVel.set(scratch.vB.x - scratch.vA.x, scratch.vB.y - scratch.vA.y);

            // Normal impulse 
            scratch.contactVel = scratch.relVel.dot(scratch.normal);
            if (scratch.contactVel > 0) continue; // separating

            scratch.rAn = scratch.rA.cross(scratch.normal);
            scratch.rBn = scratch.rB.cross(scratch.normal);
            scratch.denom = manifold.parentA.inverseMass + manifold.parentB.inverseMass +
            				scratch.rAn * scratch.rAn * manifold.parentA.inverseInertia + scratch.rBn * scratch.rBn * manifold.parentB.inverseInertia;

            scratch.j = -(1 + scratch.e) * scratch.contactVel / scratch.denom;
            scratch.impulse.set(scratch.normal.x * scratch.j, scratch.normal.y * scratch.j);

            // Applying normal impulse
            if (manifold.parentA.active) {
            	manifold.parentA.velocity.x -= scratch.impulse.x * manifold.parentA.inverseMass;
            	manifold.parentA.velocity.y -= scratch.impulse.y * manifold.parentA.inverseMass;
            	manifold.parentA.angularVelocity -= (scratch.rA.x * scratch.impulse.y - scratch.rA.y * scratch.impulse.x) * manifold.parentA.inverseInertia;
            }
            if (manifold.parentB.active) {
            	manifold.parentB.velocity.x += scratch.impulse.x * manifold.parentB.inverseMass;
            	manifold.parentB.velocity.y += scratch.impulse.y * manifold.parentB.inverseMass;
            	manifold.parentB.angularVelocity += (scratch.rB.x * scratch.impulse.y - scratch.rB.y * scratch.impulse.x) * manifold.parentB.inverseInertia;
            }

            // Friction impulse (static + dynamic)
            scratch.vA.set(manifold.parentA.velocity.x, manifold.parentA.velocity.y);
            scratch.tA.set(-manifold.parentA.angularVelocity * scratch.rA.y, manifold.parentA.angularVelocity * scratch.rA.x);
            scratch.vA.add(scratch.tA);
            
            scratch.vB.set(manifold.parentB.velocity.x, manifold.parentB.velocity.y);
            scratch.tB.set(-manifold.parentB.angularVelocity * scratch.rB.y, manifold.parentB.angularVelocity * scratch.rB.x);
            scratch.vB.add(scratch.tB);

            scratch.relVel.set(scratch.vB.x - scratch.vA.x, scratch.vB.y - scratch.vA.y);

            // Tangent vector
            scratch.tangent.set(scratch.relVel.x, scratch.relVel.y);
            scratch.nProj.set(scratch.normal.x, scratch.normal.y);
            scratch.nProj.scale(scratch.relVel.dot(scratch.normal));
            scratch.tangent.subtract(scratch.nProj);
            if (scratch.tangent.length() > 1e-8f) scratch.tangent.normalizeSafely();

            scratch.rAt = scratch.rA.x * scratch.tangent.y - scratch.rA.y * scratch.tangent.x;
            scratch.rBt = scratch.rB.x * scratch.tangent.y - scratch.rB.y * scratch.tangent.x;
            scratch.denom = manifold.parentA.inverseMass + manifold.parentB.inverseMass +
            				scratch.rAt * scratch.rAt * manifold.parentA.inverseInertia + scratch.rBt * scratch.rBt * manifold.parentB.inverseInertia;

            scratch.jt = -scratch.relVel.dot(scratch.tangent) / scratch.denom;

            
            if (Math.abs(scratch.jt) < scratch.j * sf) {
                // Static friction: stick
            } else {
                // Dynamic friction: slide
            	scratch.jt = (scratch.jt > 0 ? 1 : -1) * scratch.j * df;
            }

            scratch.frictionImpulse.set(scratch.tangent.x * scratch.jt, scratch.tangent.y * scratch.jt);

            // Applying friction
            if (manifold.parentA.active) {
            	manifold.parentA.velocity.x -= scratch.frictionImpulse.x * manifold.parentA.inverseMass;
            	manifold.parentA.velocity.y -= scratch.frictionImpulse.y * manifold.parentA.inverseMass;
            	manifold.parentA.angularVelocity -= (scratch.rA.x * scratch.frictionImpulse.y - scratch.rA.y * scratch.frictionImpulse.x) * manifold.parentA.inverseInertia;
            }
            if (manifold.parentB.active) {
            	manifold.parentB.velocity.x += scratch.frictionImpulse.x * manifold.parentB.inverseMass;
            	manifold.parentB.velocity.y += scratch.frictionImpulse.y * manifold.parentB.inverseMass;
            	manifold.parentB.angularVelocity += (scratch.rB.x * scratch.frictionImpulse.y - scratch.rB.y * scratch.frictionImpulse.x) * manifold.parentB.inverseInertia;
            }
        }
    }
}