package org.example.custom_objects;

import java.util.List;
import org.example.physics.objects.PhysicsObject;
import org.example.physics.engine.ObjectManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class DynamiteManager {

    private ObjectManager objectManager = ObjectManager.getInstance();
    public static float interval = 150;

    private final List<Timer> pendingTimers = Collections.synchronizedList(new ArrayList<>());

    public void detonateAll() {
        List<PhysicsObject> list = objectManager.objectList;
        int delay = 0;

        for (int i = 0; i < list.size(); i++) {
            PhysicsObject obj = list.get(i);
            if (obj instanceof Dynamite) {
                int currentDelay = delay;

                Timer t = new java.util.Timer();
                pendingTimers.add(t);

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if (!objectManager.objectList.contains(obj)) {
                            pendingTimers.remove(t);
                            t.cancel();
                            return;
                        }
                        ((Dynamite) obj).detonate();
                        pendingTimers.remove(t);
                        t.cancel();
                    }
                };

                t.schedule(task, currentDelay);
                delay += interval;
            }
        }
    }

    public void cancelPendingDetonations() {
        synchronized (pendingTimers) {
            for (Timer t : pendingTimers) {
                try {
                    t.cancel(); 
                } catch (Throwable ignored) {}
            }
            pendingTimers.clear();
        }
    }
}
