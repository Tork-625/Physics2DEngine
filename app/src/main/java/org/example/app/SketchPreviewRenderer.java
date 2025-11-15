package org.example.app;

import static org.lwjgl.opengl.GL11.*;

import org.example.physics.engine.SketchHandler;

public class SketchPreviewRenderer {

    private final SketchHandler sketchHandler;
    private final int leftPanelWidth;
    private final int topBarHeight;

    public SketchPreviewRenderer(SketchHandler sketchHandler, int leftPanelWidth, int topBarHeight) {
        this.sketchHandler = sketchHandler;
        this.leftPanelWidth = leftPanelWidth;
        this.topBarHeight = topBarHeight;
    }

    public void draw() {
        if (!sketchHandler.canDrawNow) return;

        glColor3f(1f, 1f, 1f);
        glLineWidth(4f);

        glBegin(GL_LINE_STRIP);
        for (var p : sketchHandler.points) {
            float x = p[0] + leftPanelWidth;
            float y = p[1] + topBarHeight;
            glVertex2f(x, y);
        }
        glEnd();
    }
}
