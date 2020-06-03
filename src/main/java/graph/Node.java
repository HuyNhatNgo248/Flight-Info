/*
 * Author: Huy Nhat Ngo
 * Date: 6/3/2020
 * Purpose: add more data fields, including visited, hop, bpE and their getters and setters
 * to support computeEnhancements in ShortestPaths.java (enhancements)
 */
package graph;

import java.util.HashMap;

/**
 * A Node class for a adjacency-list representation of a graph.  Nodes are
 * identified by a unique String identifier and edges are stored as a Neighbor
 * map that associates each neighboring node with the weight of the edge to
 * that node. It is the responsibility of the user of this class to avoid
 * making multiple Nodes with the same unique identifier.
 */
public class Node {

    private final String id; // unique identifier for this node

    // for each node v that has an edge from this to v, neighbors maps
    //  v -> the weight of the edge
    private HashMap<Node, Double> neighbors;
    private boolean visited = false;
    private int hop; //hop value
    private Node bpE; //back pointer for Enhancements

    /**
     * Constructor: create node with the given id
     */
    public Node(String id) {
        this.id = id;
        neighbors = new HashMap<Node, Double>();
    }

    /**
     * Return the edge's count to the node
     */
    public int getHop() {
        return hop;
    }

    /**
     * Update the edge's count to the node
     */
    public void setHop(int hop) {
        this.hop = hop;
    }

    /**
     * Return the back pointer
     */
    public Node getBpE() {
        return bpE;
    }

    /**
     * Update the back pointer
     */
    public void setBpE(Node bpE) {
        this.bpE = bpE;
    }

    /**
     * Return this node's unique identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Return true if the node is visited, false otherwise
     */
    public boolean getVisited() {
        return visited;
    }

    /**
     * set the value of node's visited field to true or
     * false depending on whether the node is visited or not
     */
    public void setVisited (boolean value) {
        visited = value;
    }

    /**
     * Return the map that associates each neighbor with the weight of the
     * edge to that neighbor.
     */
    public HashMap<Node, Double> getNeighbors() {
        return neighbors;
    }

    /**
     * Add an edge to neighbor with the given weight. If such an edge already
     * existed, upate its weight.
     */
    public void addNeighbor(Node neighbor, double weight) {
        neighbors.put(neighbor, weight);
    }

    /**
     * returns the Node's unique identifier
     */
    @Override
    public String toString() {
        return id;
    }

    /**
     * equals: two nodes are equal if their unique ids are equal
     */
    @Override
    public boolean equals(Object ob) {
        if (ob == null || !(ob instanceof Node)) {
            return false;
        }
        return id.equals(((Node) ob).id);
    }

    /**
     * Hashes the unique id
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
