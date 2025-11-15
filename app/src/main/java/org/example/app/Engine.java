package org.example.app;

import org.example.physics.engine.KeyHandler;
import org.example.physics.engine.SketchHandler;
import org.example.physics.engine.World;
import org.example.physics.settings.Config;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import static org.lwjgl.opengl.GL11.*;

import imgui.type.ImBoolean;
import org.example.custom_objects.DynamiteManager;


public class Engine {

    private Window window;
    private World world;
    private SketchHandler sketchHandler;
    private KeyHandler keyHandler;

    private ImGuiImplGlfw imguiGlfw = new ImGuiImplGlfw();
    private ImGuiImplGl3 imguiGl3 = new ImGuiImplGl3();

    private int screenWidth;
    private int screenHeight;

    private final int FPS = Config.FPS;

    private int topBarHeight = 30;
    private int leftPanelWidth = 150;
    private int rightPanelWidth = 300;

    private ImBoolean gravityEnabled = new ImBoolean(true);

    DynamiteManager dynamiteManager;

    private SketchPreviewRenderer sketchPreviewRenderer;

    public float VIRTUAL_HEIGHT = 720;
    public float VIRTUAL_WIDTH =  1366;

    public static float globalScale;

    private UIManager ui;

    public void start() {
        window = new Window();
        window.init();
        screenWidth = window.getWidth();
        screenHeight = window.getHeight();

        AppState appState = new AppState();
        appState.screenWidth = screenWidth;
        appState.screenHeight = screenHeight;

        float scaleX = screenWidth / VIRTUAL_WIDTH;
        float scaleY = screenHeight / VIRTUAL_HEIGHT;
        globalScale = Math.min(scaleX, scaleY);

        Config.strokeThickness = Math.round(5 * globalScale);

        topBarHeight = Math.round(30 * globalScale);
        leftPanelWidth = Math.round(150 * globalScale);
        rightPanelWidth = Math.round(300 * globalScale);

        glViewport(0, 0, screenWidth, screenHeight);
        glOrtho(0, screenWidth, screenHeight, 0, -1, 1);
        glClearColor(0.2f, 0.2f, 0.2f, 1.0f);

        initImGui();

        world = new World(screenWidth - leftPanelWidth - rightPanelWidth, screenHeight - topBarHeight);
        world.loadObjects();

        sketchHandler = new SketchHandler();
        keyHandler = new KeyHandler();

        dynamiteManager = new DynamiteManager();


        this.ui = new UIManager(
        window,
        world,
        dynamiteManager,
        gravityEnabled,
        appState,
        Config.defaultSolverIterations
        );

        InputManager inputManager = new InputManager(
                window.getWindow(),
                leftPanelWidth,
                rightPanelWidth,
                topBarHeight,
                screenWidth,
                screenHeight,
                globalScale,
                keyHandler,
                sketchHandler,
                appState
        );

        sketchPreviewRenderer = new SketchPreviewRenderer(
            sketchHandler,
            leftPanelWidth,
            topBarHeight
        );

        inputManager.initCallbacks();
        loop();
        cleanUp();
    }

    private void initImGui() {
        ImGui.createContext();
        ImGui.getIO().addConfigFlags(ImGuiConfigFlags.DockingEnable);
        ImGui.getIO().addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        ImGui.getIO().setIniFilename(null);
        imguiGlfw.init(window.getWindow(), true);
        imguiGl3.init("#version 130");
    }


    private void loop() {
        long lastTime = System.nanoTime();
        double nsPerFrame = 1_000_000_000.0 / FPS;
        double delta = 0;

        while (!window.shouldClose()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerFrame;
            lastTime = now;

            while (delta >= 1) {
                world.update(1.0 / FPS);
                world.handleCollisions(1.0 / FPS);
                delta--;
            }

            glClear(GL_COLOR_BUFFER_BIT);

            int w = screenWidth - leftPanelWidth - rightPanelWidth;
            int h = screenHeight - topBarHeight;

            glViewport(leftPanelWidth, 0, w, h);
            glMatrixMode(GL_PROJECTION);
            glPushMatrix();
            glLoadIdentity();
            glOrtho(0, w, h, 0, -1, 1);

            glMatrixMode(GL_MODELVIEW);
            glPushMatrix();
            glLoadIdentity();

            world.draw();

            glMatrixMode(GL_MODELVIEW);
            glPopMatrix();
            glMatrixMode(GL_PROJECTION);
            glPopMatrix();

            glViewport(0, 0, screenWidth, screenHeight);
            sketchPreviewRenderer.draw();

            imguiGlfw.newFrame();
            ImGui.newFrame();

            ui.drawUI();

            ImGui.render();
            imguiGl3.renderDrawData(ImGui.getDrawData());

            window.swapBuffers();
            window.pollEvents();
        }
    }

    private void cleanUp() {
        imguiGl3.dispose();
        imguiGlfw.dispose();
        ImGui.destroyContext();
        window.destroy();
    }
}
