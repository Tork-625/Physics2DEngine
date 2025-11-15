package org.example.physics.engine;

import org.example.physics.objects.PhysicsObject;
import java.util.ArrayList;
import java.util.List;

import org.example.physics.collision.CollisionManager;

public class ObjectManager {
	
	public List<PhysicsObject> objectList;
	public static ObjectManager instance;
	
	public static List<PhysicsObject> toAdd;
	public static List<PhysicsObject> toRemove;
	
	private ObjectManager() {
		objectList = new ArrayList<>();
		toAdd = new ArrayList<>();
		toRemove = new ArrayList<>();
	}
	
	public static ObjectManager getInstance() {
		if(instance == null) {
			instance = new ObjectManager();
		}
		return instance;
	}
	
	public void add(PhysicsObject obj) {
		toAdd.add(obj);
	}
	
	public void draw() {
		for(int i = 0; i < objectList.size(); i++){
			objectList.get(i).draw();
		}
	}
	
	public void update(double timePerFrame) {
		if(!toAdd.isEmpty()) {
			objectList.addAll(toAdd);
			toAdd.clear();
		}
		
		if(!toRemove.isEmpty()) {
			objectList.removeAll(toRemove);
			toRemove.clear();
		}
		
		for(PhysicsObject obj: objectList){
			obj.update(timePerFrame);
		}
	}
	
	////////////////////////////////////////////
	public void remove(PhysicsObject obj) {
		toRemove.add(obj);
	}
	public void remove(int idx) {
		toRemove.add(objectList.get(idx));
	}
	////////////////////////////////////////////
	
	public void clear() {
		objectList.clear();
	}

	public void handleCollisions(double timePerFrame) {
		CollisionManager.handleCollision(objectList, timePerFrame);
	}

	public List<PhysicsObject> getObjects() {
    	return objectList;
	}
	
}
