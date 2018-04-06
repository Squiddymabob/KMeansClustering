/**
 * 
 */
package com.ew00162.kmeans;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Emily
 *
 * The goal of clustering is to automatically find groups that are similar and 'cluster' them
 * These groups are 'clusters'
 *
 * K-Means has two main parameters - a dataset and a positive integer K
 * K represents the number of clusters to be extracted from the dataset
 * 
 * STEP 1: Pick a number of clusters K
 * STEP 2: Make K points at randomised locations - these will be the centroids
 * STEP 3: Calculate Euclidean distance from each point to all centroids
 * STEP 4: Assign each point to its nearest centroid
 * STEP 5: Establish new centroids by averaging the locations of all points in a cluster
 * STEP 6: Repeat from STEP 4 until changes made are irrelevant
 */
public class KMeansClustering {

	// STEP 1
	// Pick a number of clusters K
	private int K = 0;
	
	// Max X and Y bounds of graph
	// Max lat and long for GPS
	private double maxY = 0;
	private double maxX = 0;
	
	// Origins
	private double originX = 0;
	private double originY = 0;
	
	// List of points
	private ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();
	
	// HashMap to map centroids to points
	private HashMap<Point2D.Double, Point2D.Double> clusters = new HashMap<Point2D.Double, Point2D.Double>();
	
	/**
	 * @return the clusters
	 */
	public HashMap<Point2D.Double, Point2D.Double> getClusters() {
		return clusters;
	}
	
	/**
	 * @return the points
	 */
	public ArrayList<Point2D.Double> getPoints() {
		return points;
	}
	
	// List of centroids
	// centroids.size() is K
	private ArrayList<Point2D.Double> centroids = new ArrayList<Point2D.Double>();
	
	/**
	 * @return the centroids
	 */
	public ArrayList<Point2D.Double> getCentroids() {
		return centroids;
	}

	// List of nearest centroids
	// index(i) of this is the nearest centroid for index(i) of points
	private ArrayList<Point2D.Double> nearestCentroids = new ArrayList<Point2D.Double>();

	/**
	 * @return the nearestCentroids
	 */
	public ArrayList<Point2D.Double> getNearestCentroids() {
		return nearestCentroids;
	}


	// Centroids changed?
	private boolean changed = false;
		
	/**
	 * Constructor
	 * @param k
	 * @param maxY
	 * @param maxX
	 * @param points
	 */
	public KMeansClustering(int k, double maxY, double maxX, ArrayList<Point2D.Double> points) {
		super();
		// STEP 1
		K = k;
		this.maxY = maxY;
		this.maxX = maxX;
		this.points = points;
	}
	
	/**
	 * STEP 2
	 * Make K points at randomised locations
	 * These are the centroids
	 * @param K
	 * @param maxY
	 * @param maxX
	 */
	public void createCentroids() {
		
		// Create K points
		for (int i = K; i > 0; i--) {
			
			// Create random X and Y values for the new point
			// Values will be inside max range of graph
			//Random r = new Random();
			double randomX = ThreadLocalRandom.current().nextDouble(originX, maxX);
			double randomY = ThreadLocalRandom.current().nextDouble(originY, maxY);
			
			// Create new point
			Point2D.Double newPoint = new Point2D.Double(randomX,randomY);
			
			// Add new point to the list of points
			centroids.add(newPoint);
			
		}
		
	}
	
	/**
	 * STEP 3
	 * Calculate Euclidean distance
	 * @param pointA
	 * @param pointB
	 * @return distance
	 */
	public double euclideanDistance(Point2D.Double pointA, Point2D.Double pointB) {
		
		double distance = 0.0;
		
		// d(p,q) = sqrt(((p1 - q1)^2) + ((p2 - q2)^2))
		distance = Math.sqrt(Math.pow((pointA.getX() - pointB.getX()),2) + Math.pow((pointA.getY() - pointB.getY()),2));
		
		return distance;	
	}
	
	/**
	 * STEP 3 and STEP 4
	 * Calculate Euclidean distance from each point to all centroids
	 * Assign each point to its nearest centroid
	 * @param point
	 * @return nearestCentroid
	 */
	public Point2D.Double calculateNearestCentroid(Point2D.Double point){
		
		Point2D.Double nearestCentroid = new Point2D.Double(0.0,0.0);
		double distance = 0.0;
		
		for (int i = 0; i < K; i++) {
			
			if (i == 0) {
				distance = euclideanDistance(point, centroids.get(i));
				nearestCentroid = centroids.get(i);
			}
			
			if (euclideanDistance(point, centroids.get(i)) < distance) {
			
				distance = euclideanDistance(point, centroids.get(i));
				nearestCentroid = centroids.get(i);
			}
			
		}
		
		return nearestCentroid;	
	}


	public void createClusters() {
		
		changed = false;
		
		// 'null' value point
		Point2D.Double nullPoint = new Point2D.Double(originX - 1, originY -1);
		
		// Empty nearestCentroids so that they can be set again
		nearestCentroids.clear();
		
		// Parallel arrays so that nearestCentroids(i) is the nearest centroid to points(i)
		for (int i = 0; i < points.size(); i++) {	
			nearestCentroids.add(calculateNearestCentroid(points.get(i)));
		}

		// Array of K clusters, one for each centroid, of max size = number of points
		Point2D.Double[][] arr = new Point2D.Double[points.size()][K];
		
		// For the nearest centroid of each point, if it is equal to a centroid, add the point to that centroid's cluster
		for(int i = 0; i < arr.length; i++) {
			
		    for(int j = 0; j < K; j++) {
		    	
		    	if (nearestCentroids.get(i) == centroids.get(j)) {    	
		    		arr[i][j] = points.get(i);
		    	}
		    	else {
		    		arr[i][j] = nullPoint;
		    	}
		    	
		    	// TESTING
			    //System.out.println(arr[i][j]);
		    	
		    }
		    
		}
		
		// Averaging the clusters to find the new centroids
		double sumX = 0.0;
		double sumY = 0.0;
		int count = 0;
		
		for (int l = 0; l < K; l++) {	
			for (int m = 0; m < arr.length; m++) {
				if (!(arr[m][l].equals(nullPoint))){ 
					sumX += arr[m][l].getX();
					sumY += arr[m][l].getY();
					count++;
				}
			}	
			
			if (!(count == 0) && !(centroids.get(l).getX() == sumX/count) && !(centroids.get(l).getY() == sumY/count)) {

				Point2D.Double newCentroid = new Point2D.Double((sumX / count),(sumY / count));
				
				centroids.set(l, newCentroid);
				
				changed = true;
				
				// TESTING
				//System.out.println("NEW CENTROID: " + newCentroid);
			}
			
		}
		
		
		// Call self unless no changes to centroids
		if (changed == true) {
		createClusters();
		}
		
	}
	

	/**
	 * @param centroid
	 * @return the points in a cluster
	 */
	public ArrayList<Point2D.Double> getClusterValuesForCentroid(Point2D.Double centroid) {
		
		ArrayList<Point2D.Double> clusterValues = new ArrayList<Point2D.Double>();
		
		// Assign to HashMap clusters
		// Index(i) of nearestCentroids is the nearest centroid for index(i) of points		
		for(int i = 0; i < points.size(); i++) {
			
			if (nearestCentroids.get(i).equals(centroid)) {
			// Now, each point is associated to its nearest centroid
			clusterValues.add(points.get(i));
			}
		}
			
		return clusterValues;
	}
	
	/**
	 * For each centroid, print all points in that centroid's cluster
	 */
	public void printAllClusters() {
		
		for (int i = 0; i < centroids.size(); i++) {
			System.out.println("CLUSTER FOR CENTROID " + centroids.get(i) + ": " + getClusterValuesForCentroid(centroids.get(i)));
		}
		
	}
	
	
}
