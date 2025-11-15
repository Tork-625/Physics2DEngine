package org.example.app;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImBoolean;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import org.example.physics.settings.Config;
import org.example.custom_objects.Dynamite;
import org.example.custom_objects.DynamiteManager;
import org.example.physics.engine.World;


public class UIManager {

    private final Window window;
    private final World world;
    private final DynamiteManager dynamiteManager;
    private final ImBoolean gravityEnabled;
    private final AppState appState;
    private int lastSolverIterations;

    public UIManager(
            Window window,
            World world,
            DynamiteManager dynamiteManager,
            ImBoolean gravityEnabled,
            AppState appState,
            int lastSolverIterations
    ) {
        this.window = window;
        this.world = world;
        this.dynamiteManager = dynamiteManager;
        this.gravityEnabled = gravityEnabled;
        this.appState = appState;
        this.lastSolverIterations = lastSolverIterations;
    }


    public void drawUI() {
        int screenWidth = appState.screenWidth;
        int screenHeight = appState.screenHeight;
        int topBarHeight = appState.topBarHeight;
        int leftPanelWidth = appState.leftPanelWidth;
        int rightPanelWidth = appState.rightPanelWidth;

        drawTopBar(screenWidth, screenHeight, topBarHeight);
        drawLeftPanel(screenWidth, screenHeight, leftPanelWidth, topBarHeight);
        drawRightPanel(screenWidth, screenHeight, rightPanelWidth, topBarHeight);

    }

    public void drawTopBar(int screenWidth, int screenHeight, int topBarHeight){
        ImGui.setNextWindowPos(0, 0);
        ImGui.setNextWindowSize(screenWidth, topBarHeight);
        ImGui.pushStyleColor(ImGuiCol.WindowBg, 0.12f, 0.14f, 0.16f, 1f);
        ImGui.begin("TopBar", ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | 
                    ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoTitleBar | 
                    ImGuiWindowFlags.NoScrollbar);
        
        ImGui.setCursorPosY((topBarHeight - ImGui.getTextLineHeight()) / 2);
        ImGui.text("Physics-2D Sandbox");
        ImGui.sameLine(screenWidth - 30);
        ImGui.setCursorPosY((topBarHeight - 20) / 2);
        ImGui.pushStyleVar(imgui.flag.ImGuiStyleVar.FramePadding, 2f, 2f);
        if (ImGui.button("X##close", 20, 20)) {
            glfwSetWindowShouldClose(window.getWindow(), true);
        }
        ImGui.popStyleVar();
        
        ImGui.end();
        ImGui.popStyleColor();
    };


    public void drawLeftPanel(int screenWidth, int screenHeight, int leftPanelWidth, int topBarHeight){
        ImGui.setNextWindowPos(0, topBarHeight);
        ImGui.setNextWindowSize(leftPanelWidth, screenHeight - topBarHeight);
        ImGui.pushStyleColor(ImGuiCol.WindowBg, 0.16f, 0.18f, 0.20f, 1f);

        ImGui.begin(
            "Shapes",
            ImGuiWindowFlags.NoResize |
            ImGuiWindowFlags.NoMove |
            ImGuiWindowFlags.NoCollapse
        );

        if (ImGui.radioButton("Square", appState.selectedShape.equals("square"))) {
            appState.selectedShape = "square";
        }
        ImGui.dummy(0, 6);

        if (ImGui.radioButton("Circle", appState.selectedShape.equals("circle"))) {
            appState.selectedShape = "circle";
        }
        ImGui.dummy(0, 6);

        if (ImGui.radioButton("Rectangle", appState.selectedShape.equals("rectangle"))) {
            appState.selectedShape = "rectangle";
        }
        ImGui.dummy(0, 6);

        if (ImGui.radioButton("Triangle", appState.selectedShape.equals("triangle"))) {
            appState.selectedShape = "triangle";
        }
        ImGui.dummy(0, 6);

        if (ImGui.radioButton("Hexagon", appState.selectedShape.equals("hexagon"))) {
            appState.selectedShape = "hexagon";
        }
        ImGui.dummy(0, 6);

        if (ImGui.radioButton("Heart", appState.selectedShape.equals("heart"))) {
            appState.selectedShape = "heart";
        }
        ImGui.dummy(0, 6);

        if (ImGui.radioButton("Dynamite", appState.selectedShape.equals("dynamite"))) {
            appState.selectedShape = "dynamite";
        }
        ImGui.dummy(0, 6);

        if (ImGui.radioButton("Star", appState.selectedShape.equals("star"))) {
            appState.selectedShape = "star";
        }
        ImGui.dummy(0, 6);

        if (ImGui.radioButton("Stroke", appState.selectedShape.equals("stroke"))) {
            appState.selectedShape = "stroke";
        }
        ImGui.dummy(0, 6);
        ImGui.end();
        ImGui.popStyleColor();

    };
    public void drawRightPanel(int screenWidth, int screenHeight, int rightPanelWidth, int topBarHeight){
        ImGui.setNextWindowPos(screenWidth - rightPanelWidth, topBarHeight);
        ImGui.setNextWindowSize(rightPanelWidth, screenHeight - topBarHeight);
        ImGui.pushStyleColor(ImGuiCol.WindowBg, 0.14f, 0.20f, 0.23f, 1f);
        ImGui.begin("Control Panel", ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoCollapse);
        ImGui.separator();

        // ====== GRAVITY ======
        ImGui.textColored(0.7f, 0.85f, 1f, 1f, "Gravity");

        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 4, 4);

        // ON button
        if (ImGui.radioButton("ON", gravityEnabled.get())) {
            Config.solverIterations = lastSolverIterations;
            gravityEnabled.set(true);      
            world.toggleGravity(true);    
        }

        // OFF button
        ImGui.sameLine();
        if (ImGui.radioButton("OFF", !gravityEnabled.get())) {
            lastSolverIterations = Config.solverIterations;
            gravityEnabled.set(false);  
            world.toggleGravity(false);    
        }
        ImGui.popStyleVar();
        ImGui.separator();

        // ====== EXPLOSION STRENGTH ======
        ImGui.textColored(1f, 0.9f, 0.5f, 1f, "Explosion Strength");
        float[] forceMag = { Dynamite.forceMagnitude };
        if (ImGui.sliderFloat("##explosion_strength", forceMag, 500f, 10000f, "%.0f")) {
            Dynamite.forceMagnitude = forceMag[0];
        }

        // space
        ImGui.dummy(0, 12);

        // ====== FRAGMENT COUNT ======
        ImGui.textColored(1f, 0.9f, 0.5f, 1f, "Fragment Count");
        int[] fragCount = { Dynamite.fragmentCount };
        if (ImGui.sliderInt("##frag_count", fragCount, 2, 10)) { 
            Dynamite.fragmentCount = Math.max(2, fragCount[0]);
        }

        // space
        ImGui.dummy(0, 12);

        // ====== FUSE TIME ======
        ImGui.textColored(1f, 0.9f, 0.5f, 1f, "Fuse Time (ms)");
        float[] fuseTime = { DynamiteManager.interval };
        if (ImGui.sliderFloat("##fuse_time", fuseTime, 0f, 1000f, "%.0f")) {
            DynamiteManager.interval = fuseTime[0];
        }

        // space
        ImGui.dummy(0, 12);

        // ====== DETONATE BUTTON ======
        ImGui.pushStyleColor(ImGuiCol.Button, 0.7f, 0.2f, 0.2f, 1f);       
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.85f, 0.3f, 0.3f, 1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 1f, 0.4f, 0.4f, 1f);
        if (ImGui.button("Detonate", rightPanelWidth - 30, 0)) {
            dynamiteManager.detonateAll();
        }
        ImGui.popStyleColor(3);

        // space + separation
        ImGui.dummy(0, 10);
        ImGui.separator();
        ImGui.dummy(0, 10);


        // ====== CLEAR SCREEN ======
        ImGui.pushStyleColor(ImGuiCol.Button, 0.25f, 0.33f, 0.45f, 1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.30f, 0.43f, 0.60f, 1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.35f, 0.50f, 0.70f, 1f);
        if (ImGui.button("Clear Screen", rightPanelWidth - 30, 0)) {
            world.clearNonBoundObjects();
        }
        ImGui.popStyleColor(3);

        // ====== RESET BUTTON ======
        ImGui.pushStyleColor(ImGuiCol.Button, 0.35f, 0.25f, 0.25f, 1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.45f, 0.30f, 0.30f, 1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.55f, 0.35f, 0.35f, 1f);
        if (ImGui.button("Reset", rightPanelWidth - 30, 0)) {
            world.clear();
            world.loadObjects();
        }
        ImGui.popStyleColor(3);

        // thicker divider
        ImGui.dummy(0, 10);
        ImGui.separator();
        ImGui.dummy(0, 10);

        // ====== PERFORMANCE PARAMETERS ======
        ImGui.textColored(0.7f, 0.85f, 1f, 1f, "PERFORMANCE PARAMETERS");

        // space
        ImGui.dummy(0, 6);

        // ====== SOLVER ITERATIONS ======
        if (!gravityEnabled.get()) {
            ImGui.textColored(1f, 0.9f, 0.5f, 1f, "Solver Iterations: 1 (Gravity Off)");
            Config.solverIterations = 1;
        } else {
            ImGui.textColored(1f, 0.9f, 0.5f, 1f, "Solver Iterations"); 
            int[] solverIt = { Config.solverIterations };
            ImGui.sliderInt("##solverSlider", solverIt, 1, 200); 
            Config.solverIterations = solverIt[0];
        }

        // space
        ImGui.dummy(0, 12);

        // END
        ImGui.end();
        ImGui.popStyleColor();
    };

    
}