package com.sample_project.mitsmap;

public class Adjacency_VARIABLE {
    public static int[][] get_adjacency_matrix(int floor ) {
        int[][] myadjacencyMatrix=null;

        final int[][] adjacencyMatrix_floor1 = {
                {0, 1, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 1, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 1, 1, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 1},
                {0, 0, 0, 1, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0}

        };
        final int[][] adjacencyMatrix_floor2={
                {0, 1, 0, 0, 0, 0},
                {1, 0, 1, 0, 0, 0},
                {0, 1, 0, 1, 1, 0},
                {0, 0, 1, 0, 0, 0},
                {0, 0, 1, 0, 0, 1},
                {0, 0, 0, 0, 1, 0}


        };
        switch(floor){
            case 1: myadjacencyMatrix= adjacencyMatrix_floor1;
            break;
            case 2: myadjacencyMatrix=  adjacencyMatrix_floor2;
            break;


        }
      return  myadjacencyMatrix;
    }
}
