
public class TwoArrays {

	private int[] indegree;
	private int[] outdegree;
	
	public TwoArrays(int[] tab1, int[] tab2) {
		indegree=tab1;
		outdegree=tab2;		
	}
	
	public int[] in() {
		return indegree;
	}
	
	public int[] out() {
		return outdegree;
	}
	
}
