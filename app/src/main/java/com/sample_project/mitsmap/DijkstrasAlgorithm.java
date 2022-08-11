package com.sample_project.mitsmap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.Arrays;

public class DijkstrasAlgorithm extends Activity {

    // A Java program for Dijkstra's
// single source shortest path
// algorithm. The program is for
// adjacency matrix representation
// of the graph.
        private static final int NO_PARENT = -1;
        static String result_path="";


    // Function that implements Dijkstra's
        // single source shortest path
        // algorithm for a graph represented
        // using adjacency matrix
        // representation
    static void dijkstra(int[][] adjacencyMatrix,
                         int startVertex, int destinationVertex)
        {

            Log.i("LOG_ALGO","inside algo ; start= "+startVertex+" dest= "+destinationVertex);
            int nVertices = adjacencyMatrix[0].length;
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

            System.out.print("\n" + startVertex + " -> "+shortestDistances+"->"+parents+"->"+destinationVertex);

            printSolution(startVertex, shortestDistances, parents,destinationVertex);
        }

        // A utility function to print
        // the constructed distances
        // array and shortest paths
        private static void printSolution(int startVertex,
                                          int[] distances,
                                          int[] parents,int dest)
        {
           // StringBuilder sb_result=new StringBuilder("");

            int nVertices = distances.length;
            System.out.print("Vertex\t Distance\tPath");
            String path_result="";
            for (int vertexIndex = 0;
                 vertexIndex < nVertices;
                 vertexIndex++)
            {
                if (vertexIndex != startVertex)
                {

//                        System.out.print("\n" + startVertex + " -> ");
//                        System.out.print(vertexIndex + " \t\t ");
//                        System.out.print(distances[vertexIndex] + "\t\t");
//                      printPath(vertexIndex, parents);
                    //Log.i("LOG_NEW","y="+y+"\n");
                      if(vertexIndex==dest){
                          System.out.print("\n" + startVertex + " -> ");
                          System.out.print(vertexIndex + " \t\t ");
                          System.out.print(distances[vertexIndex] + "\t\t");
                          printPath(vertexIndex, parents);
                      }
                     //  path_result=path_result+startVertex+"->"+vertexIndex+" have distance="+distances[vertexIndex]+";\n";

                        //Log.i("LOG_PATH",path_result);


                }

            }
           stringPathtoIntegerPath();


        }

    private static void stringPathtoIntegerPath() {
        Path_Result myPath=new Path_Result();

        String path_vertex_string=myPath.getPath();
        Log.i("MY_PATH",path_vertex_string);
        String[] pathIntegerString=path_vertex_string.split("(\\s+)");
        int[] path_vertex=new int[pathIntegerString.length];
        for(int i=0; i<path_vertex.length; i++) {
            path_vertex[i] = Integer.parseInt(pathIntegerString[i]);
        }
//        System.out.println(Arrays.toString(path_vertex));
        Log.i("KEY_INTEGER_SET_PATH", Arrays.toString(path_vertex));
        myPath.setIntegerPath(path_vertex);

       //myPath.refreshPath();
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
            Log.i("LOG_CURRENT","Current= "+(currentVertex));
            printPath(parents[currentVertex], parents);
          //  Log.i("LOG_CURRENT",currentVertex+" ");

            //result_path=result_path+x;
           Path_Result myPath=new Path_Result();
            myPath.setPath(currentVertex+"\t");
           System.out.print(currentVertex + " ");
        //    System.out.print(result_path + " is result path \n");;

        }



    }



