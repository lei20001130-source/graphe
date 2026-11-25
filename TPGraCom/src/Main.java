import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;


public class Main {

  public static void main(String args[]){

    try{


      File file= new File(args[0]); // The first argument is the name of the file containing the graph.
      int nbGt=0;
      for (int z = 1; z<args.length;z+=2){
        if (args[z]=="--gt"){
          if (args[z+1]=="ll"){
            nbGt=1;
          }else if(args[z+1]=="lm"){
            nbGt=2;
          }else if(args[z+1]=="mm"){
            nbGt=3;
          }else if(args[z+1]=="ml"){
            nbGt=4;
          }
        }
      }
      
    // Unless really necessary, you should choose one representation OR the other, below

      
    //If we choose the representation by adjacency matrix
      Scanner sc = new Scanner(file);      
      AdjMatrix graphM = new AdjMatrix(sc); // The graph is now represented as an adjacency matrix
      
      // As an example, we show how to compute and display the degrees of the vertices
      graphM.computeDegrees(); //calls the function associated with a representation by adjacency matrix

      graphM.BFS(0);
      graphM.DFS(0);

      
   // If we choose the representation by adjacency lists
      sc = new Scanner(file);
      AdjList graphL = new AdjList(sc); // The graph is now represented as an adjacency list, with weighted or unweighted nodes
      
      //Again, we show as an example how to compute the degrees
      graphL.computeDegrees(); // calls the function associated with a representation by adjacency lists

      graphL.BFS(0);
      graphL.DFS(0);

      if(nbGt!=0){
        if (nbGt==1){
          AdjList gtList=graphL.GtToList(graphL);
          System.err.println("On a nouvelle GT list viene de adjlist");
        }else if(nbGt==2){
          AdjMatrix gtMatrix=graphL.GtToMatrix(graphL);
          System.err.println("On a nouvelle GT matrix viene de adjlist");
        }else if(nbGt==4){
          AdjList gtList=graphM.GtToList(graphM);
          System.err.println("On a nouvelle GT list viene de adjmatrix");
        }else if(nbGt==3){
          AdjMatrix gtMatrix=graphM.GtToMatrix(graphM);
          System.err.println("On a nouvelle GT matrix viene de adjmatrix");
        }
      }

      graphL.DFSNum(0);


      
      sc.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
