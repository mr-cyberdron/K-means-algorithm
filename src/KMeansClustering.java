import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeansClustering {
    public static class Point {
        public double x;
        public double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Cluster {
        public Point centroid;
        public List<Point> points;

        public Cluster(Point centroid) {
            this.centroid = centroid;
            this.points = new ArrayList<>();
        }

        public void addPoint(Point point) {
            points.add(point);
        }

        public void clearPoints() {
            points.clear();
        }
    }

    public static List<Cluster> kmeans(List<Point> points, int k, int maxIterations) {
        List<Cluster> clusters = initializeClusters(points, k);
        List<Point> previousCentroids = new ArrayList<>(k);

        int iteration = 0;
        while (iteration < maxIterations) {
            // Assign points to the nearest cluster
            assignPointsToClusters(points, clusters);

            // Update centroids
            updateCentroids(clusters);

            // Check for convergence
            if (centroidsConverged(clusters, previousCentroids)) {
                break;
            }

            previousCentroids.clear();
            for (Cluster cluster : clusters) {
                previousCentroids.add(new Point(cluster.centroid.x, cluster.centroid.y));
                cluster.clearPoints();
            }

            iteration++;
        }
        assignPointsToClusters(points, clusters);
        return clusters;
    }

    private static List<Cluster> initializeClusters(List<Point> points, int k) {
        List<Cluster> clusters = new ArrayList<>(k);
        Random random = new Random();

        for (int i = 0; i < k; i++) {
            int randomIndex = random.nextInt(points.size());
            double x_start_val = points.get(randomIndex).x+(points.get(randomIndex).x*0.01);
            double y_start_val = points.get(randomIndex).y+(points.get(randomIndex).y*0.01);
            Point centroid = new Point(x_start_val,y_start_val);
            Cluster cluster = new Cluster(centroid);
            clusters.add(cluster);
        }

        return clusters;
    }

    private static void assignPointsToClusters(List<Point> points, List<Cluster> clusters) {
        for (Point point : points) {
            double minDistance = Double.MAX_VALUE;
            Cluster closestCluster = clusters.get(0);

            for (Cluster cluster : clusters) {
                double distance = calculateDistance(point, cluster.centroid);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestCluster = cluster;
                }
            }

            closestCluster.addPoint(point);
        }
    }

    private static double calculateDistance(Point p1, Point p2) {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static void updateCentroids(List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            double sumX = 0.0;
            double sumY = 0.0;

            for (Point point : cluster.points) {
                sumX += point.x;
                sumY += point.y;
            }

            int numPoints = cluster.points.size();
            if (numPoints > 0) {
                double centroidX = sumX / numPoints;
                double centroidY = sumY / numPoints;
                cluster.centroid.x = centroidX;
                cluster.centroid.y = centroidY;
            }
        }
    }

    private static boolean centroidsConverged(List<Cluster> currentClusters, List<Point> previousCentroids) {
        if (currentClusters.size() != previousCentroids.size()) {
            return false;
        }

        for (int i = 0; i < currentClusters.size(); i++) {
            Point currentCentroid = currentClusters.get(i).centroid;
            Point previousCentroid = previousCentroids.get(i);

            if (currentCentroid.x != previousCentroid.x || currentCentroid.y != previousCentroid.y) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1.0, 2.0));
        points.add(new Point(2.0, 1.0));
        points.add(new Point(2.0, 4.0));
        points.add(new Point(3.0, 5.0));
        points.add(new Point(4.0, 2.0));
        points.add(new Point(10.0, 8.0));
        points.add(new Point(12.0, 6.0));
        points.add(new Point(11.0, 9.0));

        int k = 2;
        int maxIterations = 20;

        List<Cluster> clusters = kmeans(points, k, maxIterations);

        System.out.println("Final centroids:");
        for (Cluster cluster:clusters) {
            System.out.println("(" + cluster.centroid.x + ", " + cluster.centroid.y + ")");
        }
    }
}