package edu.uwm.ai.search.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.uwm.ai.search.World;
import edu.uwm.ai.search.heuristic.Heuristic;
import edu.uwm.ai.search.util.Node;
import edu.uwm.ai.search.util.Point;

public class FloodFillSearch extends BaseSearchAlgorithm
{

	private World w;
	//private Heuristic h;
	
	public FloodFillSearch(World w, Heuristic h)
	{
		super(w);
		
		this.w = w;
		//this.h = h;
	}
	@Override
	public SearchResult search(Point initial, Point goal)
	{
		final Map<Node, Node> pred = new HashMap<Node, Node>();
		PriorityQueue<Node> successors = new PriorityQueue<Node>(16, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2)
			{
				double h1 = w.getCostOfSquare(o1);
				double h2 = w.getCostOfSquare(o2);
				//Compare nodes based on the number of moves from the goal.
				if (h1 < h2)
				{
					return -1;
				}
				else if (h1 == h2)
				{
					return 0;
				}
				else
				{
					return 1;
				}
				
			}
		});
		Node init = new Node(initial, w.getCostOfSquare(initial), 0);
		successors.add(init);
		pred.put(init, null);
		
		while(!successors.isEmpty())
		{
			Node current = successors.poll();
			//If I'm looking at the goal, return the search path
			if (current.equals(goal)) {
				return new SearchResult(new ArrayList<Point>(backtrace(pred, current)),
						w.getCostOfSquare(initial) - w.getCostOfSquare(current.getNode()));
			}
			
			//Go through all successors, if they aren't in the list of predecessors, update its cost and add it.
			List<Node> succNodes = getSuccessors(current);
			Node trueSuccessor = Collections.min(succNodes, new Comparator<Node>() {
				@Override
				public int compare(Node o1, Node o2)
				{
					int c1 = w.getCostOfSquare(o1);
					int c2 = w.getCostOfSquare(o2);
					//Compare nodes based on the number of moves from the goal.
					if (c1 < c2)
					{
						return -1;
					}
					else if (c1 == c2)
					{
						double h1 = o1.getCost();
						double h2 = o2.getCost();
						for (Node n : backtrace(pred, o1)) {
							h1 += n.getPathCost();
						}

						for (Node n : backtrace(pred, o2)) {
							h2 += n.getPathCost();
						}
						if(h1 < h2)
						{
							return -1;
						}
						else if(h1 == h2)
						{
							return 0;
						}
						else
						{
							return 1;
						}
					}
					else
					{
						return 1;
					}
					
				}
			});
			//Add the lowest-step-cost successor to the map of successors-to-predecessors.
			pred.put(trueSuccessor, current);
			successors.add(trueSuccessor);
		}
		
		return new SearchResult(new ArrayList<Point>(), 0);
	}
	
	List<Node> backtrace(Map<Node, Node> predecessors, Node p)
	{
		List<Node> path = new LinkedList<Node>();

		while (p != null) {
			path.add(0, p);
			p = predecessors.get(p);
		}

		return path;
	}
	
	@Override
	public String toString()
	{
		return "Flood Fill";
	}

}
