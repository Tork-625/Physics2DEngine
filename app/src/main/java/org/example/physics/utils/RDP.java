package org.example.physics.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.example.physics.settings.Config;

public class RDP {
	
	public static int epsilon = Config.epsilon;
	public static int epsilonSq = epsilon * epsilon;
	
	
	public static List<int[]> simplify (List<int[]> points){
		int length = points.size();
		
		if (length < 3) {return points;}
		
		boolean[] keep = new boolean[length];
		keep[0] = true;
		keep[length - 1] = true;
		
		Stack<int[]> stack = new Stack<>();
		stack.push(new int[] {0,length - 1});
		List<int[]> result = rdp(points, stack, keep);
		
		return result;
	}
	
	public static List<int[]> rdp (List<int []> points, Stack<int []> stack, boolean[] keep){
		
		List<int[]> result = new ArrayList<>();
		
		while(!stack.isEmpty()) {
			
			double maxDistance = 0;
			int index = -1;
			
			int[] range = stack.pop();
			int start = range[0];
			int end = range[1];
			
			for(int i = start +1; i < end; i++) {
				double distance = perpendicularDistanceSq(points.get(start), points.get(end), points.get(i));
				
				if(maxDistance < distance) {
					maxDistance = distance;
					index = i;
				}
			}
			
			if(maxDistance > epsilonSq && index != -1) {
				keep[index] = true;
				stack.push(new int[] {start, index});
				stack.push(new int[] {index, end});
			}
			
			}
		
		for(int i = 0; i < points.size(); i++) {
			if(keep[i]) {result.add(points.get(i));}
		}
		
		return result;
	}
	
	
	

    private static double perpendicularDistanceSq(int[] a, int[] b, int[] p) {
        double dx = b[0] - a[0];
        double dy = b[1] - a[1];
        if (dx == 0 && dy == 0) {
            double px = p[0] - a[0];
            double py = p[1] - a[1];
            return px * px + py * py;
        }
        double t = ((p[0] - a[0]) * dx + (p[1] - a[1]) * dy) / (dx * dx + dy * dy);
        double projX = a[0] + t * dx;
        double projY = a[1] + t * dy;
        double diffX = p[0] - projX;
        double diffY = p[1] - projY;
        return diffX * diffX + diffY * diffY;
    }
}
