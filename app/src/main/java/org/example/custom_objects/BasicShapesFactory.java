package org.example.custom_objects;

import org.example.physics.objects.Circle;
import org.example.physics.objects.Polygon;
import org.example.physics.utils.Vector2D;
import org.example.physics.engine.ObjectManager;

public class BasicShapesFactory {


    private static final ObjectManager objectManager = ObjectManager.getInstance();

    private static final float DEFAULT_MASS = 1f;
    private static final float DEFAULT_RESTITUTION = 0.8f;

    public static void createCircle(float x, float y, float scale) {
        Circle c = new Circle(
                new Vector2D(x, y),
                scale,
                DEFAULT_MASS,
                true,
                DEFAULT_RESTITUTION
        );
        objectManager.add(c);
    }


    public static void createSquare(float x, float y, float scale) {
        float s = scale / 2f;
        Vector2D[] verts = {
                new Vector2D(-s, -s),
                new Vector2D(s, -s),
                new Vector2D(s, s),
                new Vector2D(-s, s)
        };

        Polygon p = new Polygon(
                new Vector2D(x, y),
                verts,
                0,
                DEFAULT_MASS,
                true,
                DEFAULT_RESTITUTION
        );
        objectManager.add(p);
    }


    public static void createRectangle(float x, float y, float scale) {
        float w = scale;
        float h = scale / 2f;

        Vector2D[] verts = {
                new Vector2D(-w/2, -h/2),
                new Vector2D(w/2, -h/2),
                new Vector2D(w/2, h/2),
                new Vector2D(-w/2, h/2)
        };

        Polygon p = new Polygon(
                new Vector2D(x, y),
                verts,
                0,
                DEFAULT_MASS,
                true,
                DEFAULT_RESTITUTION
        );
        objectManager.add(p);
    }


    public static void createTriangle(float x, float y, float scale) {
        float s = scale / 2f;

        Vector2D[] verts = {
                new Vector2D(0, -s),
                new Vector2D(s, s),
                new Vector2D(-s, s)
        };

        Polygon p = new Polygon(
                new Vector2D(x, y),
                verts,
                0,
                DEFAULT_MASS,
                true,
                DEFAULT_RESTITUTION
        );
        objectManager.add(p);
    }


    public static void createHexagon(float x, float y, float scale) {
        float r = scale / 2f;
        Vector2D[] verts = new Vector2D[6];

        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i);
            verts[i] = new Vector2D(
                    (float)(Math.cos(angle) * r),
                    (float)(Math.sin(angle) * r)
            );
        }

        Polygon p = new Polygon(
                new Vector2D(x, y),
                verts,
                0,
                DEFAULT_MASS,
                true,
                DEFAULT_RESTITUTION
        );
        objectManager.add(p);
    }
}
