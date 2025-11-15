package org.example.physics.renderer;

import org.example.physics.objects.Circle;
import org.example.physics.objects.Polygon;

public interface Renderer {
    void drawCircle(Circle c);
    void drawPolygon(Polygon p); 
}
