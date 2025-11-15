package org.example.custom_objects;

import org.example.physics.utils.Vector2D;
import org.example.renderer.NinjaStarRenderer;
import org.example.renderer.PlainRenderer;
import org.example.physics.objects.CompoundObject;
import org.example.physics.objects.Polygon;

public class Star extends CompoundObject {

    PlainRenderer plainRenderer = PlainRenderer.getInstance();

    public Star(float x, float y, float scale) {
        super(0, true, 0.5f);

        Vector2D v1 = new Vector2D(20.810032f * scale, -20.810032f * scale);
        Vector2D v2 = new Vector2D(-20.810032f * scale, -20.810032f * scale);
        Vector2D v3 = new Vector2D(20.810032f * scale, 20.810032f * scale);
        Vector2D v4 = new Vector2D(-20.810032f * scale, 20.810032f * scale);
        Vector2D v5 = new Vector2D(0f, -109.462852f * scale);
        Vector2D v6 = new Vector2D(0f, 109.462852f * scale);
        Vector2D v7 = new Vector2D(109.462852f * scale, 0f);
        Vector2D v8 = new Vector2D(-109.462852f * scale, 0f);

        Vector2D[] allVertices = new Vector2D[]{v1, v2, v3, v4, v5, v6, v7, v8};

        int[][] faceIndices = {
            {3, 2, 0, 1}, 
            {7, 3, 1},   
            {4, 1, 0},    
            {6, 0, 2},  
            {5, 2, 3}    
        };

        for (int[] indices : faceIndices) {
            Vector2D[] polygonVerts = new Vector2D[indices.length];
            for (int i = 0; i < indices.length; i++) {
                polygonVerts[i] = allVertices[indices[i]];
            }
            addChildPolygonFromVertices(polygonVerts, x, y);
        }
    }

    private void addChildPolygonFromVertices(Vector2D[] verts, float globalX, float globalY) {
        float cx = 0, cy = 0;
        for (Vector2D v : verts) {
            cx += v.x;
            cy += v.y;
        }
        cx /= verts.length;
        cy /= verts.length;

        Vector2D[] localVerts = new Vector2D[verts.length];
        for (int i = 0; i < verts.length; i++) {
            Vector2D v = verts[verts.length - 1 - i];
            localVerts[i] = new Vector2D(v.x - cx, v.y - cy);
        }

        addChild(new Polygon(
                new Vector2D(globalX + cx, globalY + cy),
                localVerts,
                0,
                2,
                true,
                restitution,
                NinjaStarRenderer.get()
        ));
    }
}
