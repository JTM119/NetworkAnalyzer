//Edge.java


//Below is the syntax highlighted version of Edge.java from §4.3 Minimum Spanning Trees.



/******************************************************************************
 *  Compilation:  javac Edge.java
 *  Execution:    java Edge
 *
 *  Immutable weighted edge.
 *
 ******************************************************************************/

/**
 *  The {@code Edge} class represents a weighted edge in an 
 *  {@link EdgeWeightedGraph}. Each edge consists of two integers
 *  (naming the two vertices) and a real-value weight. The data type
 *  provides methods for accessing the two endpoints of the edge and
 *  the weight. The natural order for this data type is by
 *  ascending order of weight.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Edge implements Comparable<Edge> { 

    private final int v;
    private final int w;
    private final double length;
    private final double latency;
    //If it's copper it will be a 1, if it's optic it will be 0
    private final int speed;
    private final int bandwidth;
    //Also need to include a variable for bandwidth, and a variable for type of cable

    /**
     * Initializes an edge between vertices {@code v} and {@code w} of
     * the given {@code length}.
     *
     * @param  v one vertex
     * @param  w the other vertex
     * @param  length the length of this edge
     * @throws IllegalArgumentException if either {@code v} or {@code w} 
     *         is a negative integer
     * @throws IllegalArgumentException if {@code weight} is {@code NaN}
     */
    public Edge(int v, int w, double length, String type, int bandwidth) {
        if (v < 0) throw new IllegalArgumentException("vertex index must be a nonnegative integer");
        if (w < 0) throw new IllegalArgumentException("vertex index must be a nonnegative integer");
        if (Double.isNaN(length)) throw new IllegalArgumentException("Length is NaN");
        if (type.equals("optical")) this.speed = 200000000;
        else if (type.equals("copper")) this.speed = 230000000;
        else throw new IllegalArgumentException("Invalid cable type");
        this.v = v;
        this.w = w;
        this.length = length;
        this.latency = length/(double)speed;
        this.bandwidth = bandwidth;
    }

    /**
     * Returns the length of this edge.
     *
     * @return the length of this edge
     */
    public double length() {
        return length;
    }


    /**
     * Returns the latency of this edge.
     *
     * @return the latency of this edge
     */
    public double latency(){
        return latency;
    }


    /**
     * Returns the speed of this edge.
     *
     * @return the speed of this edge
     */
    public double speed(){
        return speed;
    }

    public int bandwidth(){
        return bandwidth;
    }

    /**
     * Returns either endpoint of this edge.
     *
     * @return either endpoint of this edge
     */
    public int either() {
        return v;
    }

    /**
     * Returns the endpoint of this edge that is different from the given vertex.
     *
     * @param  vertex one endpoint of this edge
     * @return the other endpoint of this edge
     * @throws IllegalArgumentException if the vertex is not one of the
     *         endpoints of this edge
     */
    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else {
            System.out.println(vertex + ", " + v + ", " + w);
            throw new IllegalArgumentException("Illegal endpoint");
        }
    }

    /**
     * Compares two edges by latency.
     * Note that {@code compareTo()} is not consistent with {@code equals()},
     * which uses the reference equality implementation inherited from {@code Object}.
     *
     * @param  that the other edge
     * @return a negative integer, zero, or positive integer depending on whether
     *         the weight of this is less than, equal to, or greater than the
     *         argument edge
     */
    @Override
    public int compareTo(Edge that) {
        return Double.compare(this.latency, that.latency);
    }

    /**
     * Returns a string representation of this edge.
     *
     * @return a string representation of this edge
     */
    public String toString() {
        return String.format("%d-%d " + latency, v, w);
    }


}


//Copyright © 2000–2017, Robert Sedgewick and Kevin Wayne. 
//Last updated: Fri Oct 20 12:50:46 EDT 2017.