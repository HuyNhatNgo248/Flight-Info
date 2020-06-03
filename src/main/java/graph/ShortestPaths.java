/*
 * Author: Huy Nhat Ngo
 * Date: 6/3/2020
 * Purpose: this class implements Dijkstra’s Single-Source Shortest Paths algorithm
 * and modified Breath-First Search to find the smallest hop from origin to destination
 */


package graph;

import heap.Heap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Provides an implementation of Dijkstra's single-source shortest paths
 * algorithm.
 * Sample usage:
 * Graph g = // create your graph
 * ShortestPaths sp = new ShortestPaths();
 * Node a = g.getNode("A");
 * sp.compute(a);
 * Node b = g.getNode("B");
 * LinkedList<Node> abPath = sp.getShortestPath(b);
 * double abPathLength = sp.getShortestPathLength(b);
 */
public class ShortestPaths {
    // stores auxiliary data associated with each node for the shortest
    // paths computation:
    private HashMap<Node, PathData> paths;

    /**
     * Compute the shortest path to all nodes from origin using Dijkstra's
     * algorithm. Fill in the paths field, which associates each Node with its
     * PathData record, storing total distance from the source, and the
     * backpointer to the previous node on the shortest path.
     * Precondition: origin is a node in the Graph.
     */
    public void compute(Node origin) {
        paths = new HashMap<Node, PathData>();
        // TODO 1: implement Dijkstra's algorithm to fill paths with
        // shortest-path data for each Node reachable from origin.
        Heap<Node, Double> frontier = new Heap<>();
        paths.put(origin, new PathData(0, null));
        frontier.add(origin, 0.0);
        while (frontier.size() != 0) {
            Node f = frontier.poll();
            for (HashMap.Entry<Node, Double> entry : f.getNeighbors().entrySet()) {
                //neighbor's node
                Node w = entry.getKey();
                //weight of the edge to the neighbor's node
                if (!paths.containsKey(w)) {
                    paths.put(w, new PathData(paths.get(f).distance + f.getNeighbors().get(w), f));
                    frontier.add(w, paths.get(w).distance);
                } else if (paths.get(f).distance + f.getNeighbors().get(w) < paths.get(w).distance) {
                    paths.get(w).distance = paths.get(f).distance + f.getNeighbors().get(w);
                    paths.get(w).previous = f;
                    frontier.changePriority(w, paths.get(w).distance);
                }
            }
        }
    }

    /**
     * Returns the length  the shortest path from the origin to destination.
     * If no path exists, return Double.POSITIVE_INFINITY.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called.
     */
    public double shortestPathLength(Node destination) {
        // TODO 2 - implement this method to fetch the shortest path length
        // from the paths data computed by Dijkstra's algorithm.
        if (paths.containsKey(destination))
            return paths.get(destination).distance;
        else
            return Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a LinkedList of the nodes along the shortest path from origin
     * to destination. This path includes the origin and destination. If origin
     * and destination are the same node, it is included only once.
     * If no path to it exists, return null.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called.
     */
    public LinkedList<Node> shortestPath(Node destination) {
        // TODO 3 - implement this method to reconstruct sequence of Nodes
        // along the shortest path from the origin to destination using the
        // paths data computed by Dijkstra's algorithm.
        if (!paths.containsKey(destination)) {
            return null;
        } else {
            LinkedList<Node> lst = new LinkedList<>();
            lst.add(destination);
            Node counter = paths.get(destination).previous;
            while (counter != null) {
                lst.add(counter);
                counter = paths.get(counter).previous;
            }
            if (lst.size() == 1)
                return lst;
            else if (lst.get(0).equals(lst.getLast()))
                lst.removeLast();

            Collections.reverse(lst);
            return lst;
        }
    }



    /**
     * Inner class representing data used by Dijkstra's algorithm in the
     * process of computing shortest paths from a given source node.
     */
    class PathData {
        double distance; // distance of the shortest path from source
        Node previous; // previous node in the path from the source

        /**
         * constructor: initialize distance and previous node
         */
        public PathData(double dist, Node prev) {
            distance = dist;
            previous = prev;
        }
    }


    /**
     * Static helper method to open and parse a file containing graph
     * information. Can parse either a basic file or a DB1B CSV file with
     * flight data. See GraphParser, BasicParser, and DB1BParser for more.
     */
    protected static Graph parseGraph(String fileType, String fileName) throws
            FileNotFoundException {
        // create an appropriate parser for the given file type
        GraphParser parser;
        if (fileType.equals("basic")) {
            parser = new BasicParser();
        } else if (fileType.equals("db1b")) {
            parser = new DB1BParser();
        } else {
            throw new IllegalArgumentException(
                    "Unsupported file type: " + fileType);
        }

        // open the given file
        parser.open(new File(fileName));

        // parse the file and return the graph
        return parser.parse();
    }



    public static void main(String[] args) {
        // read command line args
        String fileType = args[0];
        String fileName = args[1];
        String origCode = args[2];

        String destCode = null;
        if (args.length == 4) {
            destCode = args[3];
        }

        // parse a graph with the given type and filename
        Graph graph;
        try {
            graph = parseGraph(fileType, fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open file " + fileName);
            return;
        }
        graph.report();


        // TODO 4: create a ShortestPaths object, use it to compute shortest
        // paths data from the origin node given by origCode.
        ShortestPaths sh = new ShortestPaths();
        Node origin = graph.getNode(origCode);
        sh.compute(origin);


        // TODO 5:
        // If destCode was not given, print each reachable node followed by the
        // length of the shortest path to it from the origin.
        if (args.length == 3) {
            System.out.println("Shortest paths from " + origCode + ": ");
            for (HashMap.Entry<Node, PathData> entry : sh.paths.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue().distance);
            }
        }

        // TODO 6:
        // If destCode was given, print the nodes in the path from
        // origCode to destCode, followed by the total path length
        // If no path exists, print a message saying so.
        if (args.length == 4) {
            Node dest = graph.getNode(destCode);
            if (sh.shortestPath(dest) == null) {
                System.out.println("no path exists");
            } else {
                LinkedList<Node> lst = sh.shortestPath(dest);
                for (Node entry : lst) {
                    System.out.print(entry + " ");
                }
                System.out.println(sh.shortestPathLength(dest));
            }
        }
    }
}



