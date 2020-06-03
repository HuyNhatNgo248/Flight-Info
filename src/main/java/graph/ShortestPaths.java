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

/*
 * 1/ Main method behavior: upon running the main method, call executeCommand to
 * prompt the user to select one of the two options: (1) find shortest path (base assignment)
 * (2) find fewest hops (enhancements) to execute either djikstra for (1) or BFS for (2)
 * If user selects (2), precondition requires total number of command line arguments are 4 (including dest)
 * Then display the number of nodes and edges (hops) from origin to destination
 *
 * 2/ I decided to use BFS but modified the algorithm in a way that keeps track of
 * the shortest amount of edges (hops) and back pointer node so the algorithm can track
 * back to its origin. I create another hashmap named updateHop that takes in a stringID-Node pair.
 * updateHop is similar to paths hashmap in Djikstra except it doesn't create PathData object. I provide
 * 3 more fields in Node: visited hop, bpE to keep track of whether a node is visited and to
 * update the hop and back pointer fields when queue has duplicate Nodes that potentially requires less hop to reach.
 */

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
    //enhancements
    //store string-node pair value
    //use to update hop value for computeEnhancements method
    HashMap<String, Node> updateHop = new HashMap<>();
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
     * Returns a LinkedList of the nodes along the smallest hop from origin
     * to destination. This path includes the origin and destination. If origin
     * and destination are the same node, it is included only once.
     * If no path to it exists, return null.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called.
     */
    public LinkedList<Node> shortestHop(Node destination) {
        if (!updateHop.containsValue(destination)) {
            return null;
        } else {
            LinkedList<Node> lst = new LinkedList<>();
            lst.add(destination);
            Node counter = updateHop.get(destination.getId()).getBpE();
            while (counter != null) {
                lst.add(counter);
                counter = updateHop.get(counter.getId()).getBpE();
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
     * Compute the shortest hop value (shortest number of edges from origin to dest node)
     * Instantiate updateHop to keep track of hop value and back pointer.
     * When the added Node to the queue also happens to be the visited node,
     * call updateHop to update hop value and back pointer
     * otherwise, it's a unique node, add to the updateHop hashmap
     * Once executed, updateNode holds all unique Node and its smallest hop value from origin
     * Pre: origin is a node in the graph
     */
    public void computeEnhancements(Node origin) {
        Queue<Node> queue = new ArrayDeque<>();
        updateHop = new HashMap<>();
        queue.add(origin);
        origin.setBpE(null);
        origin.setHop(0);

        updateHop.put(origin.getId(), new Node(origin.getId()));

        while (!queue.isEmpty()) {
            Node u = queue.remove();
            if (!u.getVisited()) {
                u.setVisited(true);
                for (HashMap.Entry<Node, Double> entry : u.getNeighbors().entrySet()) {
                    Node w = entry.getKey();
                    w.setHop(u.getHop() + 1);
                    w.setBpE(u);
                    queue.add(w);
                    if (!updateHop.containsKey(w.getId())) {
                        updateHop.put(w.getId(), new Node(w.getId()));
                        updateHop.get(w.getId()).setBpE(u);
                        updateHop.get(w.getId()).setHop(w.getHop());
                    } else {
                        Node tmp = updateHop.get(w.getId());
                        if (tmp.getHop() > w.getHop()) {
                            tmp.setHop(w.getHop());
                            tmp.setBpE(w.getBpE());
                        }
                    }
                }
            }
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

    /**
     * Enhancements (helper method)
     * ask the user to select a shortest path or shortest length option
     * to compute the appropriate algorithm
     * Pre: origCode is non empty, sh and graph aren't null
     */
    private static int executeCommand(String origCode, ShortestPaths sh, Graph graph) {
        Scanner input = new Scanner(System.in);
        //prompt the user to enter a selection
        //1: find shortest path
        //2: find fewest hops
        System.out.print("Plese select a number: (1) find shortest path, " +
                "(2) find fewest hops: ");
        int i = input.nextInt();
        if (i == 1) {
            sh.compute(graph.getNode(origCode));
            return 1;
        } else if (i == 2) {
            sh.computeEnhancements(graph.getNode(origCode));
            return 2;
        } else
            return 0;
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
        int i = executeCommand(origCode, sh, graph);

        if (i == 1) {
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
        } else if (i == 2) {
            //display enhancements
            Node dest = graph.getNode(destCode);
            if (sh.shortestHop(dest) == null) {
                System.out.println("no path exists");
            } else {
                LinkedList<Node> lst = sh.shortestHop(dest);
                for (Node entry : lst) {
                    System.out.print(entry + " ");
                }
                System.out.println();
                System.out.println("Total edges: " + (lst.size() - 1));
            }
        }
    }
}


