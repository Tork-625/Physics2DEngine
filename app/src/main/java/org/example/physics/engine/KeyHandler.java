package org.example.physics.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.example.physics.settings.Config;


import org.example.custom_objects.DynamiteManager;

public class KeyHandler implements KeyListener{
	
	ObjectManager objectManager = ObjectManager.getInstance();
	
	public static int movingForce = Config.movingForce;

	DynamiteManager dynamiteManager = new DynamiteManager();

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if(keyCode == KeyEvent.VK_W){
			if(!objectManager.objectList.isEmpty()) {
				objectManager.objectList.get(objectManager.objectList.size()-1).velocity.y -= movingForce;
			}
		}
		if(keyCode == KeyEvent.VK_A) {
			if(!objectManager.objectList.isEmpty()) {
				objectManager.objectList.get(objectManager.objectList.size()-1).velocity.x -= movingForce;
			}
		}
		if(keyCode == KeyEvent.VK_S) {
			if(!objectManager.objectList.isEmpty()) {
				objectManager.objectList.get(objectManager.objectList.size()-1).velocity.y += movingForce;
			}
		}
		if(keyCode == KeyEvent.VK_D) {
			if(!objectManager.objectList.isEmpty()) {
				objectManager.objectList.get(objectManager.objectList.size()-1).velocity.x += movingForce;
			}
		}
		if (keyCode == KeyEvent.VK_SPACE) {
			if (!objectManager.objectList.isEmpty()) {
				dynamiteManager.detonateAll();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
