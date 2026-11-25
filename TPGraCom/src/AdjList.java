
import java.util.*;


public class AdjList {

	private int n;
	private boolean directed; //false if undirected, true if directed
	private boolean weighted; // false if unweighted, true otherwise
	private LinkedList<PairIntFloat>[] adjlist;// adjacency list with successor number and weight (convention here: no weight=0 weight)

	public AdjList(Scanner sc){ // reads the file pointed to by sc and saves the graph into the current adjacency list
		AdjList temp=Tools.fromFiletoAdjList(sc);
		this.n=temp.n;
		this.directed=temp.directed;
		this.weighted=temp.weighted;
		this.adjlist=temp.adjlist;
	}

	public AdjList(int n, boolean directed, boolean weighted, LinkedList<PairIntFloat>[] adjlist) {
		this.n=n;
		this.directed=directed;
		this.weighted=weighted;
		this.adjlist=adjlist;
	}

	public void computeDegrees() {
		if (!this.getDirected()) { // if the graph is undirected, whether weighted or not
			int[] degree = this.degree();
			System.out.println("(List) Degrees for vertices from 1 to " + degree.length + " for the given undirected graph");
			Tools.printArray(degree);
		}
		else { //if the graph is directed directed, whether weighted or not
			TwoArrays pair=this.degrees();
			int[] indegree = pair.in();
			int[] outdegree = pair.out();
			System.out.println("(List) Indegrees for vertices from 1 to " + indegree.length + " for the given digraph");
			Tools.printArray(indegree);
			System.out.println("(List) Outdegrees for vertices from 1 to " + indegree.length + " for the given digraph");
			Tools.printArray(outdegree);
		}
	}

	//method to be applied only when directed=false
	public int[] degree(){
		int[] tmp=new int[this.n];
		for(int i=0;i<this.n;i++)
			tmp[i]=0;
		for(int i=0;i<this.n;i++) {
			for(PairIntFloat pair:adjlist[i])
				tmp[i]=tmp[i]+1;
		}
		return(tmp);
	}

	//method to be applied only when directed=true
	public TwoArrays degrees(){
		int[] tmp1=new int[this.n]; //indegrees
		int[] tmp2=new int[this.n]; //outdegrees
		for(int i=0;i<this.n;i++) {
			tmp1[i]=0;
			tmp2[i]=0;
		}
		for(int i=0;i<this.n;i++) {
			for(PairIntFloat pair:adjlist[i]) {
				tmp2[i]=tmp2[i]+1;
				tmp1[pair.getA()]=tmp1[pair.getA()]+1;
			}
		}
		return(new TwoArrays(tmp1,tmp2));
	}

	public boolean getDirected() {
		return this.directed;
	}
	public boolean getWeighted() {
		return this.weighted;
	}

	// 广度优先搜索。从下标 i 出发，按距离起点的边数从少到多打印所有可达顶点。
	// 这里 i 用 0 到 n-1 的内部下标，例如 BFS(0) 表示从题目中的顶点 1 出发。
	public void BFS(int i) {
		boolean[] visited = new boolean[this.n]; // Java 将每个位置初始化为 false
		Queue<Integer> queue = new LinkedList<>(); // 队首取出，队尾加入

		visited[i] = true; // 从发现顶点的那一刻就标记，避免它被多次加入队列
		queue.offer(i);

		while (!queue.isEmpty()) {
			int u = queue.poll(); // 取出并移除队首顶点，这一轮检查 u 的邻居
			System.out.print((u + 1) + " "); // 对外显示的顶点编号从 1 开始

			// adjlist[u] 存放从 u 出发能一步到达的所有邻居，以及相应的边权。
			for (PairIntFloat pair : this.adjlist[u]) {
				int v = pair.getA(); // v 是邻居的下标，BFS 在这里用不到边权
				if (!visited[v]) {
					visited[v] = true;
					queue.offer(v); // 新发现的邻居排在已经等待的顶点后面
				}
			}
		}

		System.err.println(); // 原代码把换行写到标准错误流，顶点则打印在标准输出流
	}

	// 深度优先遍历整张图。当前方法体没有使用形参 i，第一棵 DFS 树总从下标 0 开始（非空图）。
	public void DFS(int i) {
		boolean[] visited = new boolean[this.n];

		for (int u = 0; u < this.n; u++) {
			if (!visited[u]) {
				// 上一轮递归到不了的顶点也要访问。从 u 开始一棵新的 DFS 树。
				RunDfs(u, visited);
			}
		}
		System.out.println();
	}

	// 递归访问下标 i。所有递归调用共用同一个 visited 数组。
	public void RunDfs(int i, boolean[] visited) {
		visited[i] = true;
		System.err.print((i + 1) + " "); // 在进入顶点时打印，所以输出的是发现顺序

		for (PairIntFloat pair : this.adjlist[i]) {
			int v = pair.getA();
			if (!visited[v]) {
				// 先走完 v 这条分支。递归返回后，才检查 i 的下一个邻居。
				RunDfs(v, visited);
			}
		}
	}

	// 构造转置图 Gᵀ 的邻接表。原图的每条 u → v（权重 w）对应新图的 v → u（权重 w）。
	public AdjList GtToList(AdjList listNature) {
		// 数组里先是 n 个 null，每个顶点都需要一条独立的空链表来接收邻居。
		LinkedList<PairIntFloat>[] newList = new LinkedList[listNature.n];
		for (int u = 0; u < listNature.n; u++) {
			newList[u] = new LinkedList<>();
		}

		// 原代码的第二个外层循环少了 u++，非空图会重复处理下标 0。这里补上自增。
		for (int u = 0; u < listNature.n; u++) {
			// 原代码读取 this.adjlist[u]。这里读取传入的 listNature，它才是求转置的图。
			for (PairIntFloat pair : listNature.adjlist[u]) {
				// pair.getA() 是原边的终点 v。把原起点 u 加入新图中 v 的邻接表。
				newList[pair.getA()].add(new PairIntFloat(u, pair.getB()));
			}
		}

		AdjList GTlist = new AdjList(listNature.n, listNature.directed, listNature.weighted, newList);
		return GTlist;
	}

	// 同样构造 Gᵀ，这次结果用矩阵保存。行表示出发顶点，列表示到达顶点。
	public AdjMatrix GtToMatrix(AdjList listNature) {
		float[][] newMatrix = new float[listNature.n][listNature.n]; // 初始元素都是 0

		for (int i = 0; i < listNature.n; i++) {
			for (PairIntFloat pair : listNature.adjlist[i]) {
				int v = pair.getA(); // 原图中存在 i → v，新图中要填写第 v 行、第 i 列
				if (listNature.weighted) {
					newMatrix[v][i] = pair.getB(); // 有权图复制原边权重
				} else {
					newMatrix[v][i] = 1; // 无权图用 1 表示有边
				}
			}
		}
		return new AdjMatrix(listNature.n, listNature.directed, listNature.weighted, newMatrix);
	}

	// 检查给定的顶点序列能否在无向图中依次走通。pointe 中写的是 1 到 n 的题目编号。
	public void testChemain(int[] pointe) {
		if (this.directed) {
			System.err.println("Cette graphe est oriente. "); // 有向图只打印提示
		} else {
			if (pointe.length == 0) {
				System.err.println("Cette chemin est vide.");
				return;
			}

			// 每次检查相邻的两个编号 pointe[i] 和 pointe[i+1]，所以 i 最大到 length-2。
			for (int i = 0; i < pointe.length - 1; i++) {
				boolean exit = false; // 表示这一步需要的边是否已经找到
				for (PairIntFloat pair : this.adjlist[pointe[i] - 1]) {
					// 用编号减 1 找到当前顶点的链表，再把邻居下标加 1 与下一编号比较。
					if (pair.getA() + 1 == pointe[i + 1]) {
						exit = true;
						break; // 只退出查邻居的内层循环，接下来还要检查下一步
					}
				}
				if (!exit) {
					System.err.println("Cette chemin n'exite pas.");
					return; // 任意一步走不通，整个序列就走不通
				}
			}
			// 此处假定输入编号有效。长度为 1 时直接到这里。序列也允许重复顶点。
			System.err.println("Cette chemin exite.");
		}
	}

	// 带编号的 DFS。从指定下标 i 开始，再补访其余顶点，最后打印时间并给边分类。
	public void DFSNum(int i) {
		boolean[] visited = new boolean[this.n];
		int[] start = new int[this.n];  // start[u] 记录第一次进入 u 的时刻，即 d[u]
		int[] finish = new int[this.n]; // finish[u] 记录 u 的所有邻居处理完毕的时刻，即 f[u]
		int[] parent = new int[this.n]; // parent[v] 是 DFS 首次到达 v 时的上一个顶点下标

		for (int u = 0; u < this.n; u++) {
			parent[u] = -1; // 顶点 0 是有效下标，因此用 -1 表示没有父亲（DFS 树的根）
		}

		// a 是整个遍历共用的逻辑时钟。第一次从 0 计数，进入第一个顶点记为 1。
		int a = RunDfsNum(i, 0, visited, start, finish, parent, this);
		for (int u = 0; u < this.n; u++) {
			if (!visited[u]) {
				// 从新树根接着计时，保证不同 DFS 树的时间戳也互不相同。
				a = RunDfsNum(u, a, visited, start, finish, parent, this);
			}
		}

		for (int u = 0; u < this.n; u++) {
			System.out.println(
				"Sommet " + (u + 1)
				+ " : d = " + start[u]
				+ ", f = " + finish[u]
			);
		}
		RunDfsFinish(this, start, finish, parent); // 已有完整时间戳，可逐边判断类型
	}

	// 递归处理 i 及其在本次 DFS 中的后代，返回整棵子树处理完时的时钟值。
	public int RunDfsNum(
			int i,
			int a,
			boolean[] visited,
			int[] start,
			int[] finish,
			int[] parent,
			AdjList listNature) {
		visited[i] = true;
		a = a + 1;
		start[i] = a; // 进入 i，先记录发现时间

		for (PairIntFloat pair : listNature.adjlist[i]) {
			int v = pair.getA();
			if (!visited[v]) {
				parent[v] = i; // 第一次通过 i → v 发现 v，因此 i 是 v 在 DFS 树中的父亲
				// 各层递归共用数组。int 参数 a 按值传递，要用返回值接住子树走完后的时间。
				a = RunDfsNum(v, a, visited, start, finish, parent, listNature);
			}
		}

		a = a + 1;
		finish[i] = a; // 所有递归分支都已返回，现在才离开 i
		return a;
	}

	// 使用同一次 DFS 的 parent、start、finish，对每条边分类。
	public void RunDfsFinish(AdjList graph, int[] start, int[] finish, int[] parent) {
		for (int u = 0; u < graph.n; u++) {
			for (PairIntFloat pair : graph.adjlist[u]) {
				int v = pair.getA();
				if (!graph.directed && u > v) {
					continue; // 无向边通常在两端各存一次，跳过端点编号较大的一侧
				}

				String type;
				// 树边要先判断。父子的时间区间也相互嵌套，只看区间会把有向树边判成前向边。
				if (parent[v] == u || (!graph.directed && parent[u] == v)) {
					type = "arc tree"; // DFS 首次发现一个新顶点时走过的边
				} else if (!graph.directed) {
					type = "arc back"; // 无向简单图中，DFS 的非树边连接祖先与后代
				} else if (start[v] <= start[u] && finish[u] <= finish[v]) {
					type = "arc back"; // u → v 指向祖先。等号使自环 u → u 也归入此类
				} else if (start[u] < start[v] && finish[v] < finish[u]) {
					type = "arc forward"; // u → v 指向后代，树边已经在第一项处理过
				} else {
					type = "arc cross"; // u 与 v 没有祖先关系，两个时间区间互不相交
				}

				String arrow = graph.directed ? " -> " : " -- ";
				System.out.println((u + 1) + arrow + (v + 1) + " : " + type);
			}
		}
	}

	// 判断图中是否有环。先给整张图做 DFS 编号，再扫描边寻找能回到祖先的边。
	public boolean hasCycle() {
		boolean[] visited = new boolean[this.n];
		int[] start = new int[this.n];
		int[] finish = new int[this.n];
		int[] parent = new int[this.n];

		for (int u = 0; u < this.n; u++) {
			parent[u] = -1;
		}
		int a = 0;
		for (int u = 0; u < this.n; u++) {
			if (!visited[u]) {
				a = RunDfsNum(u, a, visited, start, finish, parent, this);
			}
		}

		for (int u = 0; u < this.n; u++) {
			for (PairIntFloat pair : this.adjlist[u]) {
				int v = pair.getA();
				if (u == v) {
					return true; // 自环单独处理，这个分支直接返回，不调用 printCycle
				}
				if (!this.directed && parent[u] == v) {
					// 无向图中，孩子看到父亲时，可能只是从另一端又看到了同一条树边。
					// 此写法按端点跳过父边，适用于无平行边的无向图。
					continue;
				}
				if (start[v] < start[u] && finish[u] < finish[v]) {
					// v 是 u 的严格祖先。树上已有 v 到 u 的路径，再沿 u → v 就能闭合。
					printCycle(u, v, parent);
					return true; // 只需要找到一个环即可
				}
			}
		}
		return false; // 所有顶点和边都已检查
	}

	// 已知 u → v 是一条指向祖先的回边。还原树上的 v … u，再输出回到 v 的最后一步。
	private void printCycle(int u, int v, int[] parent) {
		Stack<Integer> cycle = new Stack<>();
		int x = u;
		while (x != v) {
			cycle.push(x); // 从后代往上回溯，每经过一个顶点就入栈
			x = parent[x];
		}
		cycle.push(v); // 祖先最后入栈，因而最先出栈

		System.out.print("Cycle : ");
		while (!cycle.isEmpty()) {
			System.out.print((cycle.pop() + 1) + "=>"); // 出栈顺序为祖先到后代
		}
		System.out.println(v + 1); // 沿 u → v 回到起点，环闭合
	}
}

