package org.example.renderer;

import org.example.physics.objects.Circle;
import org.example.physics.objects.PhysicsObject;
import org.example.physics.objects.Polygon;
import org.example.physics.renderer.Renderer;
import org.example.physics.utils.Vector2D;
import org.example.physics.utils.Color;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class PlainRenderer implements Renderer {

    private static final PlainRenderer instance = new PlainRenderer();
    public static PlainRenderer getInstance() { return instance; }

    private static final float[][] COLORS = {
            {0.70f, 1.00f, 0.70f},
            {1.00f, 0.50f, 0.50f},
            {0.60f, 0.80f, 1.00f},
            {1.00f, 0.70f, 0.40f},
            {1.00f, 1.00f, 0.60f},
            {0.80f, 0.60f, 1.00f}
    };

    private final Random rand = new Random();

    private Color pickColor() {
        float[] c = COLORS[rand.nextInt(COLORS.length)];
        return new Color(c[0], c[1], c[2]);
    }

    private PhysicsObject rootOf(PhysicsObject o) {
        PhysicsObject r = o;
        while (r.parent != null) {
            r = r.parent;
        }
        return r;
    }

    private void applyColor(PhysicsObject o) {
        PhysicsObject r = rootOf(o);

        if (r.color == null) {
            r.color = pickColor();
        }

        glColor3f(r.color.r, r.color.g, r.color.b);
    }

    @Override
    public void drawCircle(Circle c) {
        applyColor(c);

        int segments = 64;

        // fill
        glBegin(GL_TRIANGLE_FAN);
        glVertex2f(c.position.x, c.position.y);
        for (int i = 0; i <= segments; i++) {
            double angle = Math.toRadians((360.0 / segments) * i);
            float x = c.position.x + (float)Math.cos(angle) * c.radius;
            float y = c.position.y + (float)Math.sin(angle) * c.radius;
            glVertex2f(x, y);
        }
        glEnd();

        // outline for parentless circle
        if (c.parent == null) {
            glColor3f(0f, 0f, 0f);
            glLineWidth(1.5f);

            glBegin(GL_LINE_LOOP);
            for (int i = 0; i <= segments; i++) {
                double angle = Math.toRadians((360.0 / segments) * i);
                float x = c.position.x + (float)Math.cos(angle) * c.radius;
                float y = c.position.y + (float)Math.sin(angle) * c.radius;
                glVertex2f(x, y);
            }
            glEnd();
        }
    }

    @Override
    public void drawPolygon(Polygon p) {
        applyColor(p);

        Vector2D[] verts = p.getWorldVertices();
        if (verts == null || verts.length < 3) return;

        // fill
        glBegin(GL_POLYGON);
        for (Vector2D v : verts) {
            glVertex2f(v.x, v.y);
        }
        glEnd();

        // outline for parentless polygon
        if (p.parent == null) {
            glColor3f(0f, 0f, 0f);
            glLineWidth(1.5f);

            glBegin(GL_LINE_LOOP);
            for (Vector2D v : verts) {
                glVertex2f(v.x, v.y);
            }
            glEnd();
        }
    }
}
