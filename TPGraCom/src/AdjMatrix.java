
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class AdjMatrix {

  private int n;
  private boolean directed; //false if undirected, true if directed
  private boolean weighted; // false if unweighted, true otherwise
  private float[][] adjmat;
  

  public AdjMatrix(Scanner sc){
			AdjMatrix temp=Tools.fromFiletoAdjMatrix(sc); // reads the file pointed to by sc and saves the graph into the current adjacency matrix
			this.n=temp.n;
			this.directed=temp.directed;
			this.weighted=temp.weighted;
			this.adjmat=temp.adjmat;
  }

  public AdjMatrix(int n, boolean directed, boolean weighted, float[][] adjmat) {
		this.n=n;
		this.directed=directed;
		this.weighted=weighted;
		this.adjmat=adjmat;
	}
  
  
  public void computeDegrees() {
  
  if (!this.getDirected()) { //if the graph is undirected 
	  int[] degree = this.degree();  
	  System.out.println("(Matrix) Degrees for vertices from 1 to " + degree.length + " for the given undirected graph");  
	  Tools.printArray(degree); 
  }
  else { //if the graph is directed 
	  TwoArrays pair=this.degrees();  
	  int[] indegree =pair.in();  
      int[] outdegree =pair.out();
      System.out.println("(Matrix)Indegrees for vertices from 1 to " + indegree.length + " for the given digraph");  
      Tools.printArray(indegree);
      System.out.println("(Matrix)Outdegrees for vertices from 1 to " + indegree.length + " for the given digraph");  
      Tools.printArray(outdegree);
      
	  }	
  }
  
//method to be applied only when directed=false, i.e. the graph is undirected
  public int[] degree(){
	int[] tmp=new int[this.n];
    for(int i=0;i<this.n;i++) 
    	tmp[i]=0;
    for(int i=0;i<this.n;i++)
      for(int j=0;j<this.n;j++)
        if(this.adjmat[i][j] != 0)
          tmp[i] = tmp[i] + 1 ;
    return tmp;   

  }

//method to be applied only when type=1, i.e. the graph is directed
  public TwoArrays degrees(){
	  int[] tmp1=new int[this.n]; //indegrees
	  int[] tmp2=new int[this.n]; //outdegrees
	  for(int i=0;i<this.n;i++) { 
	    tmp1[i]=0;
	    tmp2[i]=0;
	  }
    for(int i=0;i<this.n;i++)
      for(int j=0;j<this.n;j++)
        if(this.adjmat[i][j]!=0){
          tmp2[i]= tmp2[i] +1;
          tmp1[j]=tmp1[j]+1;
        }
    return(new TwoArrays(tmp1,tmp2));
        
  }

  public boolean getDirected() {
	return this.directed;
  }

  public boolean getWeighted() {
		return this.weighted;
  }

  public void BFS(int i) {
    boolean[] visited = new boolean[this.n];

    Queue<Integer> queue = new LinkedList<>();

    visited[i] = true;
    queue.offer(i);

    while (!queue.isEmpty()) {
        int u = queue.poll();

        System.out.print((u + 1) + " ");

        for (int v = 0; v < this.n; v++) {
            if (this.adjmat[u][v] != 0 && !visited[v]) {
                visited[v] = true;
                queue.offer(v);
            }
        }
    }

    System.err.println();
  }

  public void DFS(int i) {
    boolean[] visited = new boolean[this.n];

    RunDfs(i, visited);

    System.err.println();
  }

  public void RunDfs(int i, boolean[] visited) {
    visited[i] = true;
    System.out.print((i + 1) + " ");

    for (int v = 0; v < this.n; v++) {
        if (this.adjmat[i][v] != 0 && !visited[v]) {
            RunDfs(v, visited);
        }
    }
  }

  public AdjList GtToList(AdjMatrix MatrixNature){

    LinkedList<PairIntFloat>[] newList = new LinkedList[MatrixNature.n];

    for (int u = 0;u<MatrixNature.n;u++){
			newList[u] = new LinkedList<>();
		}

    for (int i = 0; i<MatrixNature.n; i++){
      for (int j = 0; j<MatrixNature.n;j++){
        if (MatrixNature.adjmat[i][j]!=0){
          if (MatrixNature.weighted){
            newList[j].add(new PairIntFloat(i, MatrixNature.adjmat[i][j]));
          }else{
            newList[j].add(new PairIntFloat(i,0));
          }
        }
      }
    }


    return new AdjList(MatrixNature.n, MatrixNature.directed, MatrixNature.weighted, newList);
  }

  // Worst-case time: O(n * n).
  public AdjMatrix GtToMatrix(AdjMatrix MatrixNature) {
    float[][] newMatrix = new float[MatrixNature.n][MatrixNature.n];

    for (int i = 0; i < MatrixNature.n; i++) {
        for (int j = 0; j < MatrixNature.n; j++) {
            newMatrix[j][i] = MatrixNature.adjmat[i][j];
        }
    }

    return new AdjMatrix(MatrixNature.n, MatrixNature.directed, MatrixNature.weighted, newMatrix);
  }

  public void testChemain(int[] pointe){
    if (this.directed){
      System.err.println("Cette graphe est oriente.");
    }
    
    else {
      if (pointe.length==0){
        System.err.println("Cette chemin est vide.");
        return ;
      }
      for (int i=0;i<pointe.length-1;i++){
        if (this.adjmat[pointe[i]-1][pointe[i+1]-1] ==0){
          System.err.println("Cette chemin n'exist pas.");
          return ;
        }
      }
      System.err.println("Cette chemin existe");
    }
  }
  

}