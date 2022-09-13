package com.sample_project.mitsmap;

import android.app.Activity;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;


public class ShortestPath extends Activity
{
    static  int[] lift_points;
    //static String value="";
    private static final int NO_PARENT = -1;
    private static Path_Result shortest_lift_path;
    //Path_Result shortest_lift_path;

    // Function that implements Dijkstra's
    // single source shortest path
    // algorithm for a graph represented
    // using adjacency matrix
    // representation
    static int[] dijkstra(int[][] adjacencyMatrix, int startVertex, int[] floor_dest)
    {
        shortest_lift_path=new Path_Result();
        int nVertices = adjacencyMatrix[0].length;
        lift_points=floor_dest;
        Log.i("shpath_listpoint",""+ Arrays.toString(lift_points));
        // shortestDistances[i] will hold the
        // shortest distance from src to i
        int[] shortestDistances = new int[nVertices];

        // added[i] will true if vertex i is
        // included / in shortest path tree
        // or shortest distance from src to
        // i is finalized
        boolean[] added = new boolean[nVertices];

        // Initialize all distances as
        // INFINITE and added[] as false
        for (int vertexIndex = 0; vertexIndex < nVertices;
             vertexIndex++)
        {
            shortestDistances[vertexIndex] = Integer.MAX_VALUE;
            added[vertexIndex] = false;
        }

        // Distance of source vertex from
        // itself is always 0
        shortestDistances[startVertex] = 0;

        // Parent array to store shortest
        // path tree
        int[] parents = new int[nVertices];

        // The starting vertex does not
        // have a parent
        parents[startVertex] = NO_PARENT;

        // Find shortest path for all
        // vertices
        for (int i = 1; i < nVertices; i++)
        {

            // Pick the minimum distance vertex
            // from the set of vertices not yet
            // processed. nearestVertex is
            // always equal to startNode in
            // first iteration.
            int nearestVertex = -1;
            int shortestDistance = Integer.MAX_VALUE;
            for (int vertexIndex = 0;
                 vertexIndex < nVertices;
                 vertexIndex++)
            {
                if (!added[vertexIndex] &&
                        shortestDistances[vertexIndex] <
                                shortestDistance)
                {
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                }
            }

            // Mark the picked vertex as
            // processed
            added[nearestVertex] = true;

            // Update dist value of the
            // adjacent vertices of the
            // picked vertex.
            for (int vertexIndex = 0;
                 vertexIndex < nVertices;
                 vertexIndex++)
            {
                int edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex];

                if (edgeDistance > 0
                        && ((shortestDistance + edgeDistance) <
                        shortestDistances[vertexIndex]))
                {
                    parents[vertexIndex] = nearestVertex;
                    shortestDistances[vertexIndex] = shortestDistance +
                            edgeDistance;
                }
            }
        }

        printSolution(startVertex, shortestDistances, parents);
        return shortest_lift_path.getIntegerPath();
    }

    // A utility function to print
    // the constructed distances
    // array and shortest paths
    private static void printSolution(int startVertex,
                                      int[] distances,
                                      int[] parents)
    {
        int nVertices = distances.length;
        System.out.print("Vertex\t Distance\tPath");
// 		 System.out.println("The shortest Distance from source 0th node to lift nodes are: ");
        int res=999,lift_value=0;
        HashMap<String, String> data = new HashMap<String, String>();


        System.out.println("\nThe path from source to shortest lift" + res+" with "+ lift_value);
        for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++)
        {
            if (vertexIndex != startVertex)
            {
                System.out.print("\n" + startVertex + " -> ");
                System.out.print(vertexIndex + " \t\t ");
                System.out.print(distances[vertexIndex] + "\t\t");
                for(int k=0;k<lift_points.length;k++){

                    if(vertexIndex==lift_points[k]){

                        res = Math.min(res, distances[vertexIndex]);
                        if(res==distances[vertexIndex]){
                            lift_value=vertexIndex;
                        }



                    }
                }

            }
        }
        //	System.out.println("\nTo " + vertexIndex + " the shortest distance is: " + distances[vertexIndex]+"---->"+res+"--"+lift_value);
        printPath(lift_value, parents);
        stringPathtoIntegerPath();
    }
    private static void stringPathtoIntegerPath() {
        //Path_Result shortest_lift_path=new Path_Result();
        Log.i("shpath_list5","inside stringPath");
        String path_vertex_string=shortest_lift_path.getPath();
        Log.i("shpath_list6_STR_PATH",path_vertex_string);
        String[] pathIntegerString=path_vertex_string.split("(\\s+)");
        int[] path_vertex=new int[pathIntegerString.length];
        for(int i=0; i<path_vertex.length; i++) {
            path_vertex[i] = Integer.parseInt(pathIntegerString[i]);
        }
//        System.out.println(Arrays.toString(path_vertex));
        Log.i("shpathlist7_int_PATH", Arrays.toString(path_vertex));
        shortest_lift_path.setIntegerPath(path_vertex);

        // myPath.refreshPath();
    }
    // Function to print shortest path
    // from source to currentVertex
    // using parents array
    private static void printPath(int currentVertex,
                                  int[] parents)
    {

        // Base case : Source node has
        // been processed
        if (currentVertex == NO_PARENT)
        {
            return;
        }
        printPath(parents[currentVertex], parents);
        //	System.out.print(currentVertex + " ");

        shortest_lift_path.setPath(currentVertex+"\t");

    }

    public static void resetSelectedPoints() {
        shortest_lift_path=null;
    }
}