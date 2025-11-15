package org.example.physics.engine; 
 
import org.example.physics.objects.Polygon;
import org.example.physics.renderer.Renderer;
import org.example.physics.utils.Vector2D;
import org.example.renderer.DefaultRenderer;
import org.example.physics.settings.Config;
import org.example.app.Engine;
 
public class World { 
 
    public ObjectManager objectManager; 
    public Renderer defaultRenderer = DefaultRenderer.getInstance(); 
 
    public Polygon topBar, bottomBar, leftBar, rightBar; 
    public Polygon tnt; 

    private Polygon[] towerBlocks;

    private int screenHeight;
    private int screenWidth;
 
    public World(int screenWidth, int screenHeight) { 
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        objectManager = ObjectManager.getInstance(); 
 
        float barThickness = 30f * Engine.globalScale; 
 
        topBar = new Polygon( 
            new Vector2D(screenWidth / 2, barThickness / 2),  
            new Vector2D[] { 
                new Vector2D(0, 0),  
                new Vector2D(screenWidth, 0),  
                new Vector2D(screenWidth, barThickness),  
                new Vector2D(0, barThickness) 
            },  
            0, 1, false, 0.6f 
        ); 
 
        bottomBar = new Polygon( 
            new Vector2D(screenWidth / 2, screenHeight - barThickness / 2),  
            new Vector2D[] { 
                new Vector2D(0, screenHeight - barThickness),  
                new Vector2D(screenWidth, screenHeight - barThickness), 
                new Vector2D(screenWidth, screenHeight),  
                new Vector2D(0, screenHeight) 
            },  
            0, 1, false, 0.6f  
        ); 
 
        leftBar = new Polygon( 
            new Vector2D(barThickness / 2, screenHeight / 2),  
            new Vector2D[] { 
                new Vector2D(0, 0),  
                new Vector2D(barThickness, 0),  
                new Vector2D(barThickness, screenHeight),  
                new Vector2D(0, screenHeight) 
            },  
            0, 1, false, 0.6f 
        ); 
 
        rightBar = new Polygon( 
            new Vector2D(screenWidth - barThickness / 2, screenHeight / 2),  
            new Vector2D[] { 
                new Vector2D(screenWidth - barThickness, 0),  
                new Vector2D(screenWidth, 0),  
                new Vector2D(screenWidth, screenHeight),  
                new Vector2D(screenWidth - barThickness, screenHeight) 
            },  
            0, 1, false, 0.6f 
        ); 
 

    } 
 
private void buildTower(int screenWidth, int screenHeight) {
    float blockWidth = 60f * Engine.globalScale;
    float blockHeight = 25f * Engine.globalScale;
    float centerX = screenWidth / 2f;
    float groundY = screenHeight - 50f * Engine.globalScale + 20f;

    towerBlocks = new Polygon[40]; // added 4 for side pillars
    int idx = 0;
    float currentY = groundY;

    // --- original tower layers ---
    // LAYER 1 - Wide Foundation (6 horizontal blocks)
    currentY -= blockHeight / 2;
    for (int i = -3; i < 3; i++) {
        towerBlocks[idx++] = createBlock(centerX + blockWidth * i + blockWidth / 2, currentY, blockWidth, blockHeight);
    }

    // LAYER 2 - Vertical supports (5 vertical blocks)
    currentY -= blockHeight / 2 + blockWidth / 2;
    for (int i = -2; i <= 2; i++) {
        towerBlocks[idx++] = createBlock(centerX + blockWidth * i, currentY, blockHeight, blockWidth);
    }

    // LAYER 3 - Platform (5 horizontal blocks)
    currentY -= blockWidth / 2 + blockHeight / 2;
    for (int i = -2; i <= 2; i++) {
        towerBlocks[idx++] = createBlock(centerX + blockWidth * i, currentY, blockWidth, blockHeight);
    }

    // LAYER 4 - Middle pillars (4 vertical blocks)
    currentY -= blockHeight / 2 + blockWidth / 2;
    for (float i = -1.5f; i <= 1.5f; i += 1.0f) {
        towerBlocks[idx++] = createBlock(centerX + blockWidth * i, currentY, blockHeight, blockWidth);
    }

    // LAYER 5 - Second platform (4 horizontal blocks)
    currentY -= blockWidth / 2 + blockHeight / 2;
    for (float i = -1.5f; i <= 1.5f; i += 1.0f) {
        towerBlocks[idx++] = createBlock(centerX + blockWidth * i, currentY, blockWidth, blockHeight);
    }

    // LAYER 6 - Intermediate pillars (3 vertical blocks)
    currentY -= blockHeight / 2 + blockWidth * 0.45f;
    for (int i = -1; i <= 1; i++) {
        towerBlocks[idx++] = createBlock(centerX + blockWidth * i, currentY, blockHeight, blockWidth * 0.9f);
    }

    // LAYER 7 - Third platform (3 horizontal blocks)
    currentY -= blockWidth * 0.45f + blockHeight / 2;
    for (int i = -1; i <= 1; i++) {
        towerBlocks[idx++] = createBlock(centerX + blockWidth * i, currentY, blockWidth, blockHeight);
    }

    // LAYER 8 - Upper pillars (3 vertical blocks)
    currentY -= blockHeight / 2 + blockWidth * 0.4f;
    for (float i = -0.5f; i <= 0.5f; i += 1f) {
        towerBlocks[idx++] = createBlock(centerX + blockWidth * i, currentY, blockHeight, blockWidth * 0.8f);
    }

    // LAYER 9 - Fourth platform (2 horizontal blocks)
    currentY -= blockWidth * 0.4f + blockHeight / 2;
    towerBlocks[idx++] = createBlock(centerX - blockWidth * 0.5f, currentY, blockWidth, blockHeight);
    towerBlocks[idx++] = createBlock(centerX + blockWidth * 0.5f, currentY, blockWidth, blockHeight);

    // LAYER 10 - Tower neck (2 vertical blocks)
    currentY -= blockHeight / 2 + blockWidth * 0.35f;
    towerBlocks[idx++] = createBlock(centerX - blockWidth * 0.35f, currentY, blockHeight * 0.9f, blockWidth * 0.7f);
    towerBlocks[idx++] = createBlock(centerX + blockWidth * 0.35f, currentY, blockHeight * 0.9f, blockWidth * 0.7f);

    // LAYER 11 - Crown base (2 horizontal blocks)
    currentY -= blockWidth * 0.35f + blockHeight / 2;
    towerBlocks[idx++] = createBlock(centerX - blockWidth * 0.4f, currentY, blockWidth * 0.8f, blockHeight);
    towerBlocks[idx++] = createBlock(centerX + blockWidth * 0.4f, currentY, blockWidth * 0.8f, blockHeight);

    // LAYER 12 - Dome base (1 block)
    currentY -= blockHeight / 2 + blockHeight / 3;
    towerBlocks[idx++] = createBlock(centerX, currentY, blockWidth * 1.2f, blockHeight * 0.8f);

    // LAYER 13 - Large central dome (1 cone)
    currentY -= blockHeight * 0.08f + blockHeight * 1.2f;
    towerBlocks[idx++] = createTriangleDome(centerX, currentY, blockWidth * 1.5f, blockHeight * 2.4f);
}


    
    private Polygon createBlock(float x, float y, float width, float height) {
        return new Polygon(
            new Vector2D(x, y),
            new Vector2D[]{
                new Vector2D(-width / 2, -height / 2),
                new Vector2D(width / 2, -height / 2),
                new Vector2D(width / 2, height / 2),
                new Vector2D(-width / 2, height / 2)
            },
            0, 1, true, 0.6f 
        );
    }
    
    private Polygon createTriangleDome(float x, float y, float base, float height) {
        return new Polygon(
            new Vector2D(x, y),
            new Vector2D[]{
                new Vector2D(0, -height / 2),
                new Vector2D(base / 2, height / 2),
                new Vector2D(-base / 2, height / 2)
            },
            0, 1, true, 0.4f
        );
    }
 
    public void loadObjects() { 
        objectManager.add(topBar); 
        objectManager.add(bottomBar); 
        objectManager.add(leftBar); 
        objectManager.add(rightBar); 
 
        buildTower(screenWidth, screenHeight);

        for (Polygon block : towerBlocks) {
            objectManager.add(block);
        }
 
    } 
 
    public void update(double timePerFrame) { 
        objectManager.update(timePerFrame); 
    } 
 
    public void draw() { 
        objectManager.draw(); 
    } 
 
    public void handleCollisions(double timePerFrame) { 
        objectManager.handleCollisions(timePerFrame); 
    } 

    public void clearNonBoundObjects() { 
        for(int i = 4; i < objectManager.objectList.size(); i++)
            objectManager.remove(i); 
    } 

    public void clear() { 
        objectManager.clear(); 
    } 

    public void toggleGravity(boolean state) {
        Config.g = state ? 500 : 0;
        if (!state) Config.solverIterations = 1; 
    }
}
