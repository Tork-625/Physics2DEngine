package org.example.renderer;

import org.example.physics.objects.Circle;
import org.example.physics.objects.Polygon;
import org.example.physics.renderer.Renderer;
import org.example.physics.utils.Vector2D;

import static org.lwjgl.opengl.GL11.*;

public class DefaultRenderer implements Renderer {

    private static final DefaultRenderer instance = new DefaultRenderer();
    public static DefaultRenderer getInstance() { return instance; }

    @Override
    public void drawCircle(Circle c) {

        int segments = 64;

        // Fill
        glColor3f(1f, 1f, 1f);
        glBegin(GL_TRIANGLE_FAN);
        glVertex2f(c.position.x, c.position.y);
        for(int i = 0; i <= segments; i++){
            double angle = Math.toRadians((360.0 / segments) * i);
            float x = c.position.x + (float)Math.cos(angle) * c.radius;
            float y = c.position.y + (float)Math.sin(angle) * c.radius;
            glVertex2f(x, y);
        }
        glEnd();

        // Outline
        glColor3f(0f, 0f, 0f);
        glLineWidth(1.5f);
        glBegin(GL_LINE_LOOP);
        for(int i = 0; i <= segments; i++){
            double angle = Math.toRadians((360.0 / segments) * i);
            float x = c.position.x + (float)Math.cos(angle) * c.radius;
            float y = c.position.y + (float)Math.sin(angle) * c.radius;
            glVertex2f(x, y);
        }
        glEnd();

        // Rotation indicator line (black, thin)
        if (c.worldVertices != null && c.worldVertices.length >= 2) {
            glColor3f(0f, 0f, 0f);
            glLineWidth(1f);
            glBegin(GL_LINES);
            glVertex2f(c.worldVertices[0].x, c.worldVertices[0].y);
            glVertex2f(c.worldVertices[1].x, c.worldVertices[1].y);
            glEnd();
        }

        glColor3f(1f, 1f, 1f);
    }


    @Override
    public void drawPolygon(Polygon p) {

        Vector2D[] verts = p.getWorldVertices();
        if (verts == null || verts.length < 3) return;

        // Fill
        glColor3f(1f, 1f, 1f);
        glBegin(GL_POLYGON);
        for (Vector2D v : verts) {
            glVertex2f(v.x, v.y);
        }
        glEnd();

        // Outline
        glColor3f(0f, 0f, 0f);
        glLineWidth(1.5f);
        glBegin(GL_LINE_LOOP);
        for (Vector2D v : verts) {
            glVertex2f(v.x, v.y);
        }
        glEnd();

        glColor3f(1f, 1f, 1f);
    }
}
