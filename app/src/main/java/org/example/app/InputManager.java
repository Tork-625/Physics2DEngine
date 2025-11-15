package org.example.app;

import org.example.physics.engine.KeyHandler;
import org.example.physics.engine.SketchHandler;
import org.example.custom_objects.*;
import org.example.physics.engine.ObjectManager;
import org.example.physics.utils.Vector2D;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static org.lwjgl.glfw.GLFW.*;

public class InputManager {

    private long window;
    private int leftPanelWidth;
    private int rightPanelWidth;
    private int topBarHeight;
    private int screenWidth;
    private int screenHeight;
    private float globalScale;

    private final AppState appState;
    private KeyHandler keyHandler;
    private SketchHandler sketchHandler;
    private ObjectManager objectManager = ObjectManager.getInstance();

    public InputManager(
            long window,
            int leftPanelWidth,
            int rightPanelWidth,
            int topBarHeight,
            int screenWidth,
            int screenHeight,
            float globalScale,
            KeyHandler keyHandler,
            SketchHandler sketchHandler,
            AppState appState
    ) {
        this.window = window;
        this.leftPanelWidth = leftPanelWidth;
        this.rightPanelWidth = rightPanelWidth;
        this.topBarHeight = topBarHeight;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.globalScale = globalScale;
        this.keyHandler = keyHandler;
        this.sketchHandler = sketchHandler;
        this.appState = appState;
    }

    public void initCallbacks() {

        glfwSetKeyCallback(window, (w, key, sc, action, mods) -> {
            if (action == GLFW_PRESS || action == GLFW_REPEAT) {
                keyHandler.keyPressed(
                        new KeyEvent(new java.awt.Canvas(), 0, System.currentTimeMillis(), 0,
                                key, KeyEvent.CHAR_UNDEFINED)
                );
            } else if (action == GLFW_RELEASE) {
                keyHandler.keyReleased(
                        new KeyEvent(new java.awt.Canvas(), 0, System.currentTimeMillis(), 0,
                                key, KeyEvent.CHAR_UNDEFINED)
                );
            }
        });

        glfwSetMouseButtonCallback(window, (w, button, action, mods) -> {
            double[] xpos = new double[1];
            double[] ypos = new double[1];
            glfwGetCursorPos(window, xpos, ypos);

            int mx = (int) xpos[0] - leftPanelWidth;
            int my = (int) ypos[0] - topBarHeight;

            if (mx < 0 || mx > (screenWidth - leftPanelWidth - rightPanelWidth)) return;
            if (my < 0) return;

            MouseEvent me = new MouseEvent(new java.awt.Canvas(), 0, System.currentTimeMillis(), 0, mx, my, 1, false);

            if (button == GLFW_MOUSE_BUTTON_LEFT) {
                if (action == GLFW_PRESS) {
                    if ("stroke".equals(appState.selectedShape)) {
                        sketchHandler.mousePressed(me);
                    } else {
                        spawnShape(mx, my);
                    }
                } else if (action == GLFW_RELEASE) {
                    if ("stroke".equals(appState.selectedShape)) {
                        sketchHandler.mouseReleased(me);
                    }
                }
            }
        });

        glfwSetCursorPosCallback(window, (w, xpos, ypos) -> {
            if (!"stroke".equals(appState.selectedShape)) return;

            int mx = (int) xpos - leftPanelWidth;
            int my = (int) ypos - topBarHeight;

            if (mx < 0 || mx > (screenWidth - leftPanelWidth - rightPanelWidth)) return;
            if (my < 0) return;

            MouseEvent me = new MouseEvent(new java.awt.Canvas(), 0, System.currentTimeMillis(), 0, mx, my, 1, false);

            sketchHandler.mouseDragged(me);
            sketchHandler.mouseMoved(me);
        });
    }

    private void spawnShape(float x, float y) {
        float scale = 50f;

        switch (appState.selectedShape) {
            case "square" -> BasicShapesFactory.createSquare(x, y, scale * globalScale);
            case "circle" -> BasicShapesFactory.createCircle(x, y, scale * 0.5f * globalScale);
            case "rectangle" -> BasicShapesFactory.createRectangle(x, y, scale * 1.5f * globalScale);
            case "triangle" -> BasicShapesFactory.createTriangle(x, y, scale * globalScale);
            case "hexagon" -> BasicShapesFactory.createHexagon(x, y, scale * globalScale);
            case "heart" -> objectManager.add(new Heart(x, y, scale * 0.5f * globalScale));
            case "star" -> objectManager.add(new Star(x, y, scale * 0.006f * globalScale));
            case "dynamite" -> objectManager.add(new Dynamite(new Vector2D(x, y), scale * 0.5f * globalScale));
        }
    }
}
