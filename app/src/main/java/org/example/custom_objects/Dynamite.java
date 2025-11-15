package org.example.custom_objects;

import org.example.physics.objects.Polygon;
import org.example.physics.objects.Circle;
import org.example.physics.engine.ObjectManager;
import org.example.physics.utils.Vector2D;
import org.example.renderer.DynamiteRenderer;
import org.example.renderer.PlainRenderer;
import org.example.app.Engine;

public class Dynamite extends Polygon {

    private ObjectManager objectManager = ObjectManager.getInstance();
    PlainRenderer plainRenderer = PlainRenderer.getInstance();
    public static int fragmentCount = 6;
    public static float forceMagnitude = 4000;

    public Dynamite(Vector2D position, float size) {
        super(
            position,
            new Vector2D[] { 
                new Vector2D(-size/2, -size/2),
                new Vector2D(size/2, -size/2),
                new Vector2D(size/2, size/2),
                new Vector2D(-size/2, size/2)
            },
            0, 
            1f, 
            true,
            0.7f,
            DynamiteRenderer.get()
        );
    }

    public void detonate() {
        objectManager.remove(this); 

        for (int i = 0; i < fragmentCount; i++) {
            float angle = (float) Math.toRadians(i * (360f / fragmentCount));
            Vector2D dir = new Vector2D((float)Math.cos(angle), (float)Math.sin(angle));

            Circle fragment = new Circle(
                new Vector2D(this.position.x, this.position.y),
                8f* Engine.globalScale, 
                1f,
                true,
                0.7f,
                plainRenderer
            );

            dir.scale(forceMagnitude);
            fragment.velocity.x += dir.x;
            fragment.velocity.y += dir.y;

            objectManager.add(fragment);
        }
    }
}
