package org.example.app;

import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWVidMode;

public class Window{

    private int width;
    private int height;
    private String title = "2D-Physics-Engine";

    private long window;
    private long monitor;

    public void init(){
        if(!glfwInit()) throw new RuntimeException("GLFW initialization failed.");

        monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vid = glfwGetVideoMode(monitor);
        width = vid.width();
        height = vid.height();

        //////////////////// FOR TESTING, LATER TO BE REMOVED ////////////
        //width = 800;
        //height = 400;
        /////////////////////////////////////////////////////

        // Window hints for borderless fullscreen
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);  // Remove window borders
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);  // Prevent resizing
        
        window = glfwCreateWindow(width, height, title, 0, 0);
        if(window == 0) throw new RuntimeException("Failed to create window.");

        glfwSetWindowPos(window, 0, 0);
        
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        GL.createCapabilities();
        glfwShowWindow(window);
    }

    public long getWindow(){
        return window;
    }

    public boolean shouldClose(){
        return glfwWindowShouldClose(window);
    }

    public void swapBuffers(){
        glfwSwapBuffers(window);
    }

    public void destroy(){
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public void pollEvents(){
        glfwPollEvents();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}