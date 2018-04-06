/**
 * 
 */
package com.ew00162.kmeans;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Emily
 *
 */
public class RunKMeansClustering {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ArrayList<Point2D.Double> testset = new ArrayList<Point2D.Double>();
		
		// Create new points
		Point2D.Double newPoint1 = new Point2D.Double(8.2,11.1);
		Point2D.Double newPoint2 = new Point2D.Double(8.3,11.2);
		Point2D.Double newPoint3 = new Point2D.Double(6.0,1.3);
		Point2D.Double newPoint4 = new Point2D.Double(12.4,15.3);
		Point2D.Double newPoint5 = new Point2D.Double(8.2,11.1);
		Point2D.Double newPoint6 = new Point2D.Double(8.9,7.2);
		Point2D.Double newPoint7 = new Point2D.Double(12.3,15.9);
		Point2D.Double newPoint8 = new Point2D.Double(3.5,5.3);
		Point2D.Double newPoint9 = new Point2D.Double(1.6,0.2);
		Point2D.Double newPoint10 = new Point2D.Double(4.4,4.7);
		
		/*
		// Create points
		for (int i = 20; i > 0; i--) {
					
		// Create random X and Y values for the new point
		// Values will be inside max range of graph
		//Random r = new Random();
		double randomX = ThreadLocalRandom.current().nextDouble(0, 16);
		double randomY = ThreadLocalRandom.current().nextDouble(0, 16);
					
		// Create new point
		Point2D.Double newPoint = new Point2D.Double(randomX,randomY);	
		
		testset.add(newPoint);
		}
		*/
					
		// Add new points to the list of points
		testset.add(newPoint1);
		testset.add(newPoint2);
		testset.add(newPoint3);
		testset.add(newPoint4);
		testset.add(newPoint5);
		testset.add(newPoint6);
		testset.add(newPoint7);
		testset.add(newPoint8);
		testset.add(newPoint9);
		testset.add(newPoint10);
		
		// Create new K Means
		KMeansClustering testmeans = new KMeansClustering(3, 16, 16, testset);
		
		// Initialise centroids
		testmeans.createCentroids();
		System.out.println("INITIAL CENTROIDS: " + testmeans.getCentroids());
		
		// TEST
		//System.out.println(testmeans.euclideanDistance(newPoint1, newPoint2));
		
		testmeans.createClusters();
		
		
		//System.out.println("NEW CLUSTERS");
		
		//testmeans.createClusters();
		
		
		System.out.println("FINAL CENTROIDS: " + testmeans.getCentroids());
		
		
		
		System.out.println("POINTS: " + testmeans.getPoints());
		System.out.println("NEAREST CENTROIDS: " + testmeans.getNearestCentroids());
		
		//System.out.println("CLUSTERS: " + testmeans.getClusters());
		System.out.println("---");
		testmeans.printAllClusters();
		
	}

}
