/*
 * This file is part of the search package.
 *
 * Copyright (C) 2012, Eric Fritz
 * Copyright (C) 2012, Reed Johnson
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), to deal in the Software without 
 * restriction, including without limitation the rights to use, copy, modify, merge, publish, 
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions: 
 * 
 * The above copyright notice and this permission notice shall be included in all copies or 
 * substantial portions of the Software. 
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT.  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */

package edu.uwm.ai.search;

/**
 * TODO: Implement priority queue of locally inconsistent vertices.
 * 
 */
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.lang.Math;

/**
 * @author Eric Fritz
 * @author Reed Johnson
 */
public class DStarSearch extends BaseSearchAlgorithm
{	
	/*
	 * D* Searches from the goal to the start vertex, not from start to goal.
	 * To this end, the g values and rhs values are estimates to the goal, not the start.
	 * The constructor method for the search algorithm acts as the initialize function for D* Lite
	 */
	private Map<Integer,DNode> vertexHash;
	private PriorityQueue<DNode> openList;
	private int k_m;
	//The start and goal nodes, assigned and referenced within getPath
	private DNode start_n, goal_n;
	
	public DStarSearch(World w)
	{
		super(w);
		vertexHash = new HashMap<Integer, DNode>();
		openList = new PriorityQueue<DNode>();
		start_n = new DNode(new Point(0,0), 0, Integer.MAX_VALUE);
		goal_n = new DNode(new Point(0,0), Integer.MAX_VALUE, Integer.MAX_VALUE);
		k_m = 0;
		
		//Place the start and goal in the list of known/discovered vertices.
		vertexHash.put(vertexHashCode(start_n), start_n);
		vertexHash.put(vertexHashCode(goal_n),goal_n);
		
		
		
	}
	/**
	 * Extension of point class which provides the extra information needed
	 * to properly implement the D* search algorithm. 
	 * @author Reed Johnson
	 * @author Eric Fritz
	 * @date 11.12.2012 (Last modified by Reed Johnson)
	 *
	 */
	private class DNode extends Point implements Comparable{
		
		private int rhs;
		private int g_val;
		//k1 and k2 are the two parts of the node's key. The key is used to compare different nodes in priority queue
		private int k1, k2;
		
		public DNode(Point p, int rhs, int g_val){
			super(p.getX(),p.getY());
			this.g_val = g_val;
			this.rhs = rhs;
		}
		
		public DNode(DNode d)
		{
			super(d.getX(), d.getY());
			this.g_val = d.getG();
			this.rhs = d.getRhs();
			this.k1 = d.getK1();
			this.k2 = d.getK2();
		}
		
		
		
		/**
		 * 
		 * @return The right-hand side value
		 */
		public int getRhs(){
			return rhs;
		}
		
		public void setRhs(int new_rhs)
		{
			this.rhs = new_rhs;
		}
		
		/**
		 * Returns the DNode's g value
		 * @return g_val The DNode's g value as an integer
		 * @date 11.12.2012
		 */
		public int getG()
		{
			return g_val;
		}
		
		/**
		 * Sets the DNode's g value to an integer specified by the parameter new_g
		 * @param new_g An integer value representing the new g value for the Node
		 * @date 11.12.2012
		 */
		public void setG(int new_g)
		{
			this.g_val = new_g;
		}
		
		/**
		 * Set the Node's first key value. When automatically calculated, this is equal to
		 *  min(g, rhs) + h(start, this) + k_m
		 * @param new_k1
		 */
		public void setK1(int new_k1)
		{
			this.k1 = new_k1;
		}
		
		/**
		 * Returns the DNode's first key. This is the key given by min(g, rhs) + h(start, this) + k_m
		 * @return k1 The integer value representing the DNode's first key 
		 * @date 11.12.2012 
		 */
		public int getK1()
		{
			return this.k1;
		}
		
		/**
		 * Sets the Node's second key value. When automatically calculated, this is the min of g and rhs values.
		 * @param new_k2
		 * @date 11.12.2012 (Last modified by Reed Johnson)
		 */
		public void setK2(int new_k2)
		{
			this.k2 = new_k2;
		}
		
		/**
		 * Returns the DNode's first key. This is the key given by min(g, rhs) + h(start, this) + k_m
		 * @return k1 The integer value representing the DNode's first key 
		 * @date 11.12.2012 
		 */
		public int getK2()
		{
			return this.k2;
		}
		
		/**
		 * Sets the Node's (x,y) location to that of the point passed in
		 * @param new_point A Point object that holds the new x,y location.
		 * @date 11.12.2012 (Last modified by Reed Johnson)
		 */
		public void setPoint(Point new_point)
		{
			this.setX(new_point.getX());
			this.setY(new_point.getY());
		}
		
		/**
		 * Calculates the int-pair of keys that is used to order each DNode in a priority queue.
		 * @date 11.12.2012 (Last modified by Reed Johnson)
		 */
		public void calculateKey()
		{
			this.k1 = Math.min(this.g_val, this.rhs) + heuristic(start_n, this) + k_m;
			this.k2 = Math.min(this.g_val, this.rhs);
		}
		
		@Override
		public boolean equals(Object o)
		{
			if (o == this) {
				return true;
			}

			if (o == null || !(o instanceof Point)) {
				return false;
			}

			Point other = (Point) o;
			return other.getX() == this.getX() && other.getY() == this.getY();
		}

		/**
		 * Orders DNode objects based on their key values as described in the D* Lite algorithm
		 * @Author Reed Johnson
		 * @Author Eric Fritz
		 * @Date 11.12.2012 (Last modified by Reed Johnson)
		 */
		@Override
		public int compareTo(Object arg0) {
			DNode other = (DNode) arg0;
			//If my first key is less than other first key, I am less than the other
			if(this.k1 < other.k1 )
			{
				return -1;
			}
			//Else if my first key is the same as other's first key
			else if(this.k1 == other.k1)
			{
				//If my second key is less than other's second key, then I am less than other.
				if (this.k2 < other.k2)
				{
					return -1;
				}
				else if(this.k2 == other.k2)
				{
					return 0;
				}
				else
				{
					return 1;
				}
			}
			//Else my first key is greater than other's first key, and I am greater than other.
			else
			{
				return 1;
			}
		}
	}
	
	private int heuristic(Point p, Point goal)
	{
		return Math.abs(p.getX() - goal.getX()) + Math.abs(p.getY() - goal.getY());
	}
	
	//private void calculate
	
	@Override
	public SearchResult search(Point initial, Point goal)
	{
		//Update the start and goal nodes to the new initial and goal positions
		start_n.setPoint(initial);
		goal_n.setPoint(goal);
		//Goal's key values are updated each time SearchResult is called. The assumption in the original D* Lite paper was
		//that the goal did not move, but the environment changed. In our implementation, the goal also moves.
		goal_n.setK1(heuristic(start_n, goal_n));
		goal_n.setK2(0);
		
		openList.add(goal_n);
		start_n.calculateKey();
		DNode u = openList.peek();
		while((u.compareTo(start_n) == -1) || start_n.getRhs() > start_n.getG())
		{
			
		}
		
		
		
		
		
		return new SearchResult(new LinkedList<Point>(), 0);
	}

	@Override
	public String toString()
	{
		return "D*";
	}
	
	/**
	 * Simple function that returns an integer value to place the DNode d into the hash table of vertices
	 * @param d An initialized DNode object.
	 * @return An integer representing the code to associate with that node
	 */
	private int vertexHashCode(DNode d)
	{
		return d.getX() + 1492*d.getY();
	}
}
