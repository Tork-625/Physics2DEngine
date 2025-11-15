package org.example.renderer;

import org.example.physics.objects.Polygon;
import org.example.physics.renderer.Renderer;
import org.example.physics.objects.Circle;
import org.example.physics.utils.Vector2D;

import static org.lwjgl.opengl.GL11.*;

public class DynamiteRenderer implements Renderer {

    private static final DynamiteRenderer instance = new DynamiteRenderer();
    public static DynamiteRenderer get() { return instance; }

    @Override
    public void drawCircle(Circle c) {
        // dynamite ≠ donut
    }

    @Override
    public void drawPolygon(Polygon p) {
        if (p == null) return;

        Vector2D[] verts = p.getWorldVertices();
        if (verts == null || verts.length < 3) return;

        // pale red body
        glColor3f(1f, 0.6f, 0.6f);
        glBegin(GL_POLYGON);
        for (Vector2D v : verts) glVertex2f(v.x, v.y);
        glEnd();

        // outline
        glColor3f(0f, 0f, 0f);
        glLineWidth(1.5f);
        glBegin(GL_LINE_LOOP);
        for (Vector2D v : verts) glVertex2f(v.x, v.y);
        glEnd();
    }
}
