package org.example.renderer;

import org.example.physics.objects.Circle;
import org.example.physics.objects.Polygon;
import org.example.physics.renderer.Renderer;
import org.example.physics.utils.Vector2D;

import static org.lwjgl.opengl.GL11.*;

public class NinjaStarRenderer implements Renderer {

    private static final NinjaStarRenderer instance = new NinjaStarRenderer();
    public static NinjaStarRenderer get() { return instance; }

    @Override
    public void drawCircle(Circle c) {
        // Ninja stars don’t do circles.
    }

    @Override
    public void drawPolygon(Polygon p) {
        if (p == null) return;
        Vector2D[] verts = p.getWorldVertices();
        if (verts == null || verts.length < 3) return;

        // darker steel
        glColor3f(0.45f, 0.45f, 0.50f);
        glBegin(GL_POLYGON);
        for (Vector2D v : verts) glVertex2f(v.x, v.y);
        glEnd();

        // outline
        glColor3f(0.05f, 0.05f, 0.05f);
        glLineWidth(1.6f);
        glBegin(GL_LINE_LOOP);
        for (Vector2D v : verts) glVertex2f(v.x, v.y);
        glEnd();

        drawShine(p, verts);
    }

    private void drawShine(Polygon p, Vector2D[] verts) {
        float pulse = (float)(Math.sin(System.currentTimeMillis() * 0.003) * 0.07 + 0.15);

        // subtle metallic highlight
        float r = 0.60f + pulse;
        float g = 0.60f + pulse;
        float b = 0.65f + pulse;

        glColor3f(r, g, b);

        if (verts.length >= 3) {
            glBegin(GL_TRIANGLES);
            glVertex2f(verts[0].x, verts[0].y);
            glVertex2f(verts[1].x, verts[1].y);
            glVertex2f(
                (verts[0].x + verts[2].x) / 2f,
                (verts[0].y + verts[2].y) / 2f
            );
            glEnd();
        }
    }
}
