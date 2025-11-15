package org.example.custom_objects;

import org.example.physics.utils.Vector2D;
import org.example.renderer.PlainRenderer;
import org.example.physics.objects.CompoundObject;
import org.example.physics.objects.Polygon;

public class Heart extends CompoundObject {

    PlainRenderer plainRenderer = PlainRenderer.getInstance();

    public Heart(float x, float y, float scale) {
        super(0, true, 0.5f);

        Vector2D v1 = new Vector2D(-0.609802f * scale, -0.782136f * scale);
        Vector2D v2 = new Vector2D(0.609802f * scale, -0.782136f * scale);
        Vector2D v3 = new Vector2D(0f, 1.000000f * scale);
        Vector2D v4 = new Vector2D(0f, -0.488238f * scale);
        Vector2D v5 = new Vector2D(-0.881792f * scale, 0f);
        Vector2D v6 = new Vector2D(0.881792f * scale, 0f);
        Vector2D v7 = new Vector2D(-0.996759f * scale, -0.317546f * scale);
        Vector2D v8 = new Vector2D(0.996759f * scale, -0.317546f * scale);
        Vector2D v9 = new Vector2D(0.360963f * scale, -0.726319f * scale);
        Vector2D v10 = new Vector2D(-0.360963f * scale, -0.726319f * scale);
        Vector2D v11 = new Vector2D(-0.847275f * scale, -0.691485f * scale);
        Vector2D v12 = new Vector2D(0.847275f * scale, -0.691485f * scale);
        Vector2D v13 = new Vector2D(0f, -0.317546f * scale);

        Vector2D[] allVertices = new Vector2D[]{
            v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13
        };

        int[][] faces = {
            {2,4,6,7,5},      
            {11,7,12,3,8,1},   
            {0,9,3,12,6,10}    
        };

        for (int[] face : faces) {
            Vector2D[] polygonVerts = new Vector2D[face.length];
            for (int i = 0; i < face.length; i++) {
                polygonVerts[i] = allVertices[face[i]];
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
                plainRenderer
        ));
    }
}
