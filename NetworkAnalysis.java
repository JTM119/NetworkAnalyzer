import java.io.*;
import java.util.Scanner;

public class NetworkAnalysis{
	
	public static EdgeWeightedGraph graph; 
	public static boolean done = false;
	public static void main(String[] args){
		//create an empty graph

		//Read the file and insert it into the graph
		readGraphFile(args[0]);
		//System.out.println(graph);
		while(!done){
			printInterface();
			int input = acceptInput();
			if(input > 0 && input < 6) handleInput(input);
			else System.out.println("Invalid input, try again");
		}
	}


	public static void readGraphFile(String fileName){
		
		//Create a buffer to read in the graph
		try{
			FileReader reader = new FileReader(fileName);
			BufferedReader fileReader = new BufferedReader(reader);	
			String entry = fileReader.readLine();
			//Read the first line to find the number of vertices
			int vertices = Integer.parseInt(entry);
			
			graph = new EdgeWeightedGraph(vertices);

			entry= fileReader.readLine();
			while(entry != null){
				//Each data element is separated by a space so I just need to read in the space

					//Get v
					int oldSubstring = entry.indexOf(' ');
					String vString = entry.substring(0, oldSubstring);
					int v = Integer.parseInt(vString);
					
					//Get w
					int nextSubstring = entry.indexOf(' ', oldSubstring + 1);
					String wString = entry.substring(oldSubstring + 1, nextSubstring);
					int w = Integer.parseInt(wString);
					
					//Get the type of cable
					oldSubstring = nextSubstring;
					nextSubstring = entry.indexOf(' ', oldSubstring + 1);
					String cableType = entry.substring(oldSubstring + 1, nextSubstring);
					
					//Get the Bandwidth
					oldSubstring = nextSubstring;
					nextSubstring = entry.indexOf(' ', oldSubstring + 1);
					String bandwidthString = entry.substring(oldSubstring + 1, nextSubstring);
					int bandwidth = Integer.parseInt(bandwidthString);
					
					//Get the length
					oldSubstring = nextSubstring;
					String lengthString = entry.substring(oldSubstring + 1, entry.length());
					double length = Double.parseDouble(lengthString);
					
					//create the edge and add it to the graph
					Edge newEdge = new Edge(v, w, length, cableType, bandwidth);
					graph.addEdge(newEdge);

				entry = fileReader.readLine();
			}
			fileReader.close();

		}catch( FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException er){
			er.printStackTrace();
		}

	}

	public static void printInterface(){
		System.out.println();
		for(int i = 0; i < 20; i++)System.out.print("-");
		System.out.println();
		//System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Enter 1 to find the lowest latency path between two vertices in the graph");
		
		System.out.println("Enter 2 to determine whether or not the graph is copper only connected");
		
		System.out.println("Enter 3 to find the lowest average latency spanning tree");
		
		System.out.println("Enter 4 to determine whether or not the network will remain connected if any two points fail");
		
		System.out.println("Enter 5 to quit the program");
		System.out.println();
		System.out.println();
		for(int i = 0; i < 20; i++)System.out.print("-");
		System.out.println();
		System.out.println();
		System.out.println("Please enter the number corresponding to the operation you wish to perform");

	}

	public static int acceptInput(){
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
			try{
				int option = Integer.parseInt(input);
				return option;
			}catch(Exception e){
				//System.out.println("Inavlid input, try again");
				return 0;
			}
		
		
	}

	public static void handleInput(int input){
		Scanner in = new Scanner(System.in);
		int v;
		int w;
		switch(input){
			case 1 : 
				try{
					System.out.println("Please enter the starting vertex");
					v = in.nextInt();
					System.out.println("Please enter the target vertex");
					w = in.nextInt();
					System.out.println();

					graph.lowestLatencyPath(v, w);

				}catch(Exception e){
					e.printStackTrace();
					System.out.println("Invalid input");
				}

				break;
			case 2:
				graph.copperOnly();
				break;	
			case 3:
				graph.lowestLatencySpanningTree();
				break;
			case 4 :

				graph.remainConnected();
				break;
			case 5 : 
				done = true;
				break;
			default :
				break; 
		}
	}
}
	