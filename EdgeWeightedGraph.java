//EdgeWeightedGraph.java


//Below is the syntax highlighted version of EdgeWeightedGraph.java from §4.3 Minimum Spanning Trees.

import java.util.*;
import java.io.*;    


/**
 *  The {@code EdgeWeightedGraph} class represents an edge-weighted
 *  graph of vertices named 0 through <em>V</em> – 1, where each
 *  undirected edge is of type {@link Edge} and has a real-valued weight.
 *  It supports the following two primary operations: add an edge to the graph,
 *  iterate over all of the edges incident to a vertex. It also provides
 *  methods for returning the number of vertices <em>V</em> and the number
 *  of edges <em>E</em>. Parallel edges and self-loops are permitted.
 *  By convention, a self-loop <em>v</em>-<em>v</em> appears in the
 *  adjacency list of <em>v</em> twice and contributes two to the degree
 *  of <em>v</em>.
 *  <p>
 *  This implementation uses an adjacency-lists representation, which 
 *  is a vertex-indexed array of {@link Bag} objects.
 *  All operations take constant time (in the worst case) except
 *  iterating over the edges incident to a given vertex, which takes
 *  time proportional to the number of such edges.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class EdgeWeightedGraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int E;
    private EdgeNode[] graph;
    
    /**
     * Initializes an empty edge-weighted graph with {@code V} vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public EdgeWeightedGraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        //Here is where I will need to change it so that it uses  an adjacency list instead of a bag
        graph = new EdgeNode[V];

    }


    /**
     *Determines whether the graph would stay connected if any two vertices in the graph fail
    */
    public void remainConnected(){
       for(int v = 0; v < V; v++){
            for(int w = 0; w < V; w++){
                boolean[] marked = new boolean[V];
                marked[v] = true;
                marked[w] = true;
                for(int i = 0; i < V; i++){
                    if(i == v || i == w) continue;
                    EdgeNode current = graph[i];
                    while(current != null){
                        if(current.getData().other(i) != v && current.getData().other(i) != w){
                            marked[current.getData().other(i)] = true;
                            marked[i] = true;
                            //System.out.println(i + "  " + current.getData().other(i));
                        }
                        current = current.nextNode();
                    }
                }

                for(int i = 0; i < V; i ++){
                    if(marked[i] == false){
                        System.out.println("The network would not remain connected");
                        return;
                    }
                }
            }
        }
        System.out.println("The network survived the failures of any two vertices");
    }


    
    /**
     * Prints the edges that comprise the graph with the minimum spanning tree
     */
    public void lowestLatencySpanningTree(){
        double weight = 0;
        EdgeWeightedGraph mst = new EdgeWeightedGraph(V);
        MinPQ<Edge> pq = new MinPQ<Edge>();
         for(int i = 0; i < V; i++){
            EdgeNode current = graph[i];
            while(current != null){
                pq.insert(current.getData());
                current = current.nextNode();
            }
        }

        // run greedy algorithm
       // UF uf = new UF(G.V());
       int[] connectivity = new int[V];
       for(int i = 0; i < V; i ++){
            connectivity[i] = i;
       } 
       int count = 0;
        while (!pq.isEmpty() && count < V - 1) {
            Edge e = pq.delMin();
            int v = e.either();
            int w = e.other(v);
            //If the components are not connected
            if (connectivity[v] != connectivity[w]) { // v-w does not create a cycle
                connect(v, w, connectivity);  // merge v and w components
                mst.addEdge(e);  // add edge e to mst
                weight += e.latency();
                count ++;
            }
        }

        System.out.println(mst);
        System.out.println("There is an average latency of " +(weight/(double)(V-1)));

    }

    //Connects components within the given array
    /**
     *Connects components v and w if they are not already connected
     *@param v the first vertex to connect
     *@param w the second vertex to connect
     *@param connected an array used to determine what vertices are connected in the graph
     */
    public void connect(int v, int w, int[] connected){
        int vCon = connected[v];
        int wCon = connected[w];

        for(int i = 0; i < V; i++){
            if(connected[i] == vCon){
                connected[i] = wCon;
            }
        }

    }

    
    /**
     * Prints whether or not the graph is copper only connected
     */
    public void copperOnly(){
        boolean copper = true;
        for(int i = 0; i < V; i++){
            EdgeNode current = graph[i];
            while(current != null){
                if(current.getData().speed() == 200000000) {
                    copper = false;
                    break;
                }
                current = current.nextNode();
            }

        }

        if(copper) {
            System.out.println("The graph is copper only connected");
        }else{
            System.out.println("The graph is not copper only connected");
        }

    }

    //This will use Dijkstra's Algorithm to find the lowest latency path between the two points
    //It will then print the edges it used to get there
    /**
     *Finds the lowest total latency path between two vertices
     *@param v the starting vertex
     *@param w the edning vertex
     */
    public void lowestLatencyPath(int v, int w){
        validateVertex(v);
        validateVertex(w);
        boolean[] marked = new boolean[V];
        double[] distance = new double[V];
        Edge[] via = new Edge[V];
        //Create an array of distances to contain the values of 
        for(int i = 0; i < V; i++){
            distance[i] = Integer.MAX_VALUE;
        }
        //Create a priority queue to contain the edges 
        MinPQ edges = new MinPQ<Edge>();
        int current = v;
        distance[v] = 0;
        marked[v] = true;
        via[v] = null;
        //Run through the current nodes neighboring vertices until you see w
        while(current != w){
            EdgeNode currNode = graph[current];
            marked[current] = true;
            while(currNode != null){  
                //calculate the distance to this vertex through the current node
                double distanceTo = distance[current] + currNode.getData().latency();

                //If the new distance is shorter than the previous one and the node has not already been visited update the distance 
                //and add the edge to the PQ
                if(distanceTo < distance[currNode.getData().other(current)] && (marked[currNode.getData().other(current)] == false )){
                    distance[currNode.getData().other(current)] = distanceTo;
                    //Update the via array to be from the current vertex
                    via[currNode.getData().other(current)] = currNode.getData();
                    //If this edge is a quicker route to the new vertex 
                    edges.insert(currNode.getData());
                } 
                //Get the next edge
                currNode = currNode.nextNode();               
            }
            //Remove the lowest latency edge in the PQ that does not connect vertices already visited and visit the next vertex
            boolean mark = false;
            while(!mark){
                Edge nextEdge = (Edge)edges.delMin();
                current = nextEdge.either();
                if(marked[current] == true){
                    current = nextEdge.other(current);
                }
                if(marked[current] == false){
                    mark = true;
                }
            }
        }

        //When it gets here the next node will be the target Node
        int minBand = Integer.MAX_VALUE;       
        current = w;
        int count = 0;
        Edge[] path = new Edge[V];
        while(current != v){
            minBand = Math.min(minBand, via[current].bandwidth());
            //System.out.println(via[current]);
            path[count] = via[current];
            count++;
            current = via[current].other(current);
        }

        for(int i = count; i >= 0; i--){
            if(path[i] != null) System.out.println(path[i]);
        }
        System.out.println("The bandwidth along this path is " + minBand);

    }


    /**
     * Returns the number of vertices in this edge-weighted graph.
     *
     * @return the number of vertices in this edge-weighted graph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-weighted graph.
     *
     * @return the number of edges in this edge-weighted graph
     */
    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Adds the undirected edge {@code e} to this edge-weighted graph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless both endpoints are between {@code 0} and {@code V-1}
     */
    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        validateVertex(v);
        validateVertex(w);
        //Tbe lastNode of each vertex so that this edge can be added to the list
        EdgeNode lastNodeV = findLastLink(v, w);
        EdgeNode lastNodeW = findLastLink(w, v); 
        //adds the edge to the list
        lastNodeV.setData(e);
        lastNodeW.setData(e);
        //adj[v].add(e);
        //adj[w].add(e);
        E++;
    }

    //Traverses to the end of the list of edges of each node, and returns the next open spot to insert a node.
    //Returns null if the edge is already in the list
    
    public EdgeNode findLastLink(int v, int w){
        EdgeNode currNode = graph[v];
        if(currNode == null){
            currNode = new EdgeNode();
            graph[v] = currNode;
            return currNode;
        }
        //System.out.println("Making it here");
        while(currNode.nextNode() != null){
            //If the edge is already in the list return null
            if(currNode.getData().other(v) == w) return null;
            
            currNode = currNode.nextNode();
        }

        currNode.setNext(new EdgeNode());
        return currNode.nextNode();
    }


   
    /**
     * Returns the degree of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the degree of vertex {@code v}               
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int degree(int v) {
        validateVertex(v);
        EdgeNode currNode = graph[v];
        int count = 0;
        while(currNode != null){
            count++;
            currNode = currNode.nextNode();
        }

        return count;
    }


    /**
     * Returns a string representation of the edge-weighted graph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            //for (int i = 0; i < V; i++) {
                EdgeNode currNode = graph[v];
                while(currNode != null){
                    s.append(currNode.getData() + "  ");
                    currNode = currNode.nextNode();
                }
            //}
            s.append(NEWLINE);
        }
        return s.toString();
    }


}


