package org.example.physics.engine;

import java.util.List;
import org.example.physics.objects.Circle;
import org.example.physics.objects.CompoundObject;
import org.example.physics.objects.Polygon;
import org.example.physics.settings.Config;
import org.example.physics.utils.Vector2D;
import org.example.renderer.DefaultRenderer;

public class StrokePolygonator {
	
	public static final int thickness = Config.strokeThickness;
	static ObjectManager objectManager = ObjectManager.getInstance();
	
	private static Vector2D temp = new Vector2D(0,0);
	private static Vector2D v1 = new Vector2D(0,0);
	private static Vector2D v2 = new Vector2D(0,0);
	private static Vector2D [] singlePolygonPoints;
	
	private static Vector2D [] polygonatedPointsTop;
	private static Vector2D [] polygonatedPointsBottom;
	
	private static Vector2D center;

	private static final DefaultRenderer defaultRenderer = DefaultRenderer.getInstance();


	public static void polygonate(List<int []> points) {

	    if (points == null || points.isEmpty()) return;
		
		int length = points.size();
		
		if(points.size() == 1) {
			//Circle circle = new Circle(new Vector2D(points.get(0)[0], points.get(0)[1]), thickness*5, 2, true, 0.9f, defaultRenderer); 
			//objectManager.add(circle);
			return;
		}

		
		polygonatedPointsTop = new Vector2D[length];
		polygonatedPointsBottom = new Vector2D[length];
		
		//--First Point
		v1.x = points.get(0)[0];
		v1.y = points.get(0)[1];	
		v2.x = points.get(1)[0];
		v2.y = points.get(1)[1];	
		temp.x = v2.x - v1.x; 
		temp.y = v2.y - v1.y;
		temp.normalize();
		temp.perpendicular();
		temp.scale(thickness);
		polygonatedPointsTop[0] = new Vector2D(v1.x + temp.x, v1.y + temp.y);
		polygonatedPointsBottom[0] = new Vector2D(v1.x - temp.x, v1.y - temp.y);

		//--Last Point
		v1.x = points.get(length - 1)[0];
		v1.y = points.get(length - 1)[1];
		v2.x = points.get(length - 2)[0];
		v2.y = points.get(length - 2)[1];
		temp.x = v1.x - v2.x;
		temp.y = v1.y - v2.y;
		temp.normalize();
		temp.perpendicular();
		temp.scale(thickness);
		polygonatedPointsTop[length - 1] = new Vector2D(v1.x + temp.x, v1.y + temp.y);
		polygonatedPointsBottom[length - 1] = new Vector2D(v1.x - temp.x, v1.y - temp.y);

		
		
		//--Middle Points
		for(int i = 1; i < length -1; i++) {
		    v1.x = points.get(i)[0] - points.get(i-1)[0];
		    v1.y = points.get(i)[1] - points.get(i-1)[1];
		    v1.normalize();
		    
		    v2.x = points.get(i+1)[0] - points.get(i)[0];
		    v2.y = points.get(i+1)[1] - points.get(i)[1];
		    v2.normalize();
		    v2.add(v1);
		    float bisectorLen = v2.length();
		    v1.perpendicular();
		    if(bisectorLen < 0.01f) {
		    	v1.x *= thickness;
		    	v1.y *= thickness;
		    } else {
		    	v2.normalize();
		        float cross = v2.cross(v1);
		        
		        if(cross < 0.01f) {
		        	v1.x *= thickness;
		        	v1.y *= thickness;
		        } else {
		            float scale = thickness / cross;
		            if(scale > thickness * 4) {
		                scale = thickness * 4;
		            }
		            v1.x = -v2.y * scale;
		            v1.y = v2.x * scale;
		        }
		    }
		    polygonatedPointsTop[i]    = new Vector2D(points.get(i)[0] + v1.x,
		                                              points.get(i)[1] + v1.y);
		    polygonatedPointsBottom[i] = new Vector2D(points.get(i)[0] - v1.x,
		                                              points.get(i)[1] - v1.y);
		}
		
		CompoundObject compoundStroke = new CompoundObject(0, true, 0.9f);
		for(int i = 0; i < length - 1; i++) {
			singlePolygonPoints = new Vector2D[4];
			singlePolygonPoints[0] = polygonatedPointsTop[i];
			singlePolygonPoints[1] = polygonatedPointsTop[i + 1];
			singlePolygonPoints[2] = polygonatedPointsBottom[i + 1];
			singlePolygonPoints[3] = polygonatedPointsBottom[i];
			center = new Vector2D((singlePolygonPoints[0].x + singlePolygonPoints[1].x + singlePolygonPoints[2].x + singlePolygonPoints[3].x) * 0.25f,
								  (singlePolygonPoints[0].y + singlePolygonPoints[1].y + singlePolygonPoints[2].y + singlePolygonPoints[3].y) * 0.25f);
			Polygon polygon = new Polygon(center, singlePolygonPoints, 0, 2, true, 0.9f, defaultRenderer);
			compoundStroke.addChild(polygon);
		}
		objectManager.add(compoundStroke);
	}
}
