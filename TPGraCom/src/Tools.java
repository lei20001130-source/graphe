import java.util.LinkedList;
import java.util.Scanner;

public class Tools {

	public static void printArray(int[] tab) { 
	    for(int i=0;i<tab.length;i++) {
	    	System.out.print(" "+tab[i]);
	    }
	    System.out.println();
}
	
	public static AdjList fromFiletoAdjList(Scanner sc) { // creates a graph represented with an adjacency list, from the file pointed to by sc
		String[] firstline = sc.nextLine().split(" ");
	    int n = Integer.parseInt(firstline[0]);
	    System.out.println(n);
	    boolean directed;
	    if (firstline[1].equals("undirected"))
	    	directed = false;
	    else
	    	directed=true;
	    System.out.println("Directed= "+directed);
	    boolean weighted;
	    if (firstline[2].equals("unweighted"))
	    	weighted = false;
	    else
	    	weighted=true;
	    System.out.println("Weighted= "+weighted);
	    
	    LinkedList<PairIntFloat>[] adjlist=new LinkedList[n];
	    for (int i=0;i<n;i++)
    		adjlist[i]=new LinkedList<>();
	    
	  	
		
	    for(int k=0;k<n;k++){
	    	String[] line = sc.nextLine().split(" : ");
  		  	int i = Integer.parseInt(line[0]); //the vertex "source"
  		  	if (!weighted) {
  		  		if ((line.length>1) && (line[1].charAt(0)!=' ')) {
  		  			String[] successors= line[1].split(", "); 
  		  			for (int h=0;h<successors.length;h++) {
  		  				PairIntFloat pair=new PairIntFloat(Integer.parseInt(successors[h])-1,0f);
  		  				adjlist[i-1].add(pair);				
  		  			}
  		  		}
  		  	}
  		  	else {
  		  		line = line[1].split(" // "); 
  		  		if ((line.length==2)&&(line[1].charAt(0)!=' ')){// if there really are somme successors, then we must have something different from " " after "// "
  		  			String[] successors= line[0].split(", ");
  			  	    String[] theirweights= line[1].split(", ");
  				  	for (int h=0;h<successors.length;h++) {
  				  		PairIntFloat pair=new PairIntFloat(Integer.parseInt(successors[h])-1,Float.parseFloat(theirweights[h]));
  				  		adjlist[i-1].add(pair);			  		
  				  	}
  		  		}
  		  	}
	    }
	    return(new AdjList(n, directed, weighted, adjlist));
	}
	




	public static AdjMatrix fromFiletoAdjMatrix(Scanner sc) { // creates a graph represented with an adjacency matrix, from the file pointed to by sc
		String[] firstline = sc.nextLine().split(" ");
		int n = Integer.parseInt(firstline[0]);
		System.out.println("Number of vertices "+n);
		boolean directed;
		if (firstline[1].equals("undirected"))
			directed = false;
		else
			directed=true;
		System.out.println("Directed= "+directed);
		boolean weighted;
		if (firstline[2].equals("unweighted"))
			weighted = false;
		else
			weighted=true;
		System.out.println("Weighted= "+weighted);

		float[][] adjmat = new float[n][n];
		for (int i=0;i<n; i++)
			for(int j=0; j<n; j++)
				adjmat[i][j]=0; // replace 0 with something else if the weights can be 0

		for(int k=0;k<n;k++){
			String[] line = sc.nextLine().split(" : ");
			int i = Integer.parseInt(line[0]); //the vertex "source"
			if (!weighted) {
				if ((line.length>1) && (line[1].charAt(0)!=' ')) { 
					String[] successors= line[1].split(", ");
					for (int h=0;h<successors.length;h++){
						adjmat[i-1][Integer.parseInt(successors[h])-1]=1;
					}
				}
			}
			else {
				line = line[1].split(" // "); 
				if ((line.length==2)&&(line[1].charAt(0)!=' ')){// if there really are somme successors, then we must have something different from " " after "// "
					String[] successors= line[0].split(", ");
			  	    String[] theirweights= line[1].split(", ");
				  	for (int h=0;h<successors.length;h++)
				   		adjmat[i-1][Integer.parseInt(successors[h])-1]=Float.parseFloat(theirweights[h]);
				}
			}
		}	
		return(new AdjMatrix(n, directed, weighted, adjmat));
	}
}
