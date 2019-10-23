import java.util.*;
import javax.swing.*;

public class Puzzle {
	Scanner sc = new Scanner(System.in); 
	Node goal = new Node();
	Node start = new Node();
	int counter = 0;
	
	boolean flag = false;
	
	int temp1;
	ArrayList<Integer> arrayList = new ArrayList<Integer>();
	
	
	public void display(int a[][]){
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				System.out.print(a[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	public void initStart(){
		
		System.out.println("Enter start state");
		System.out.println("Enter 0 for blank tile");
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				
					start.state[i][j] = sc.nextInt();

				if(start.state[i][j] == 0){
					start.x = i;
					start.y = j;
					
				}
				
			}
			
			
		}
		
		display(start.state);
		
		
	}
	
	public void initGoal(){
			
			System.out.println("Enter goal state");
			System.out.println("Enter 0 for blank tile");
			for(int i=0;i<3;i++){
				for(int j=0;j<3;j++){
					goal.state[i][j] = Integer.parseInt(JOptionPane.showInputDialog("Enter value for ["+i+"]["+j+"]"));
				}
			}
			
			display(goal.state);
			
			
	}
	
	//expanding goal
	public static Node expand(int state[][], int x, int y, int newX, int newY, int cost){
		Node node = new Node();

		for(int i =0;i<3;i++){
			for(int j =0;j<3;j++){
				node.state[i][j] = state[i][j];
			}
		}
		
		// exchanging new and old position of blank tile
		int temp = node.state[newX][newY];
		node.state[newX][newY] = node.state[x][y];
		node.state[x][y] = temp;
		
		node.x = newX;
		node.y = newY;
		return node;
	}
	
	public int computeMisplaced(int a[][], int b[][]){
		
		int h = 0;
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if(a[i][j]!=b[i][j])
					h++;
			}
		}
		
		return h;
		
	}
	
	public boolean legal(int a, int b){
		return (a>=0 && a<3) && (b>=0 && b<3);
	}
	
	
	public boolean duplicate(int man, int mis, int initial[][]){
		temp1 = computeUnique(man,mis,initial);
			if(arrayList.contains(temp1))
				return false;
			else
				return true;
	}
	
	
	public int computeUnique(int man, int mis, int initial[][]){
		int sum =0;
		for(int i =0;i<3;i++){
			for(int j =0;j<3;j++){
				sum += 2*Math.pow(i+1,  3) * 2*Math.pow(j+1,  2) * initial[i][j];
			}
		}
		return sum * man * mis;
	}
	
	public int computeManhattan(int initial[][], int fin[][]){
		int count = 0;
		int tileVal  = 0;
		for(int i =0;i<3;i++){
			for(int j =0;j<3;j++){
				tileVal = initial[i][j];
				count+= java.lang.Math.abs(tileVal/3 - i) + java.lang.Math.abs(tileVal%3 -j);
			}
		}
		return count;
	}

	
	public void solve(){
		
		PriorityQueue<Node> queue = new PriorityQueue<Node>(11, new Comparator<Node>() {

			public int compare(Node o1, Node o2) {
				
				Integer f1 = o1.fn;
				Integer f2 = o2.fn;
				
				return f1.compareTo(f2);
				
			}

		});
		
		
		Node initNode = expand(start.state, start.x, start.y, start.x, start.y, 0);
		// 0 is the cost i.e level or gn
		initNode.gn = 0;
		initNode.hn = computeMisplaced(start.state, goal.state);
		initNode.fn = initNode.gn + initNode.hn;
		queue.add(initNode);
		
		int rowop[] = {1, 0, -1, 0};
		int colop[] = {0, 1, 0, -1};
		
		while(!queue.isEmpty() && counter<=100){
			
			Node min = queue.poll();
			System.out.println("About to expand this node ---- Level is : "+ min.gn);
			display(min.state);
			counter++;
			
			if(min.hn == 0){
				System.out.println("Goal state reached after "+(counter-1)+" moves");
				System.out.println("Found at level : "+min.gn);
				System.out.println("Goal state is: ");
				display(min.state);
				flag = true;
				return;
				
			}
			
			for(int i=0;i<4;i++){
				
				if(legal(min.x+rowop[i], min.y+colop[i])){
					
					Node child = expand(min.state,min.x,min.y,min.x+rowop[i],min.y+colop[i],min.gn+1);
					
					child.manhattan = computeManhattan(child.state,goal.state);
					child.hn = computeMisplaced(child.state,goal.state);
					child.fn = child.gn + child.hn;
					
					
					if(rowop[i] == 1 && colop[i] == 0)
						System.out.println("Operation: DOWN =>"+child.fn);
					else if(rowop[i] == -1 &&  colop[i] ==0)
						System.out.println("Operation: UP =>"+child.fn);
					else if(rowop[i] == 0 &&  colop[i] == -1)
						System.out.println("Operation : LEFT =>"+child.fn);
					else
						System.out.println("Operation: RIGHT =>"+child.fn);
					
					display(child.state);
					
					if(duplicate(child.manhattan, child.hn, child.state)){
						queue.add(child);
					}
					
				}
				
			}
			
			
			
		}
		
		if(flag == false){
			System.out.println("Goal state cannot be reached");
		}
		
		
		
	}
	
		
	public static void main(String[] args) {
		
		Puzzle puzzle = new Puzzle();
		puzzle.initStart();
		puzzle.initGoal();
		puzzle.solve();

	}

}
