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

package edu.uwm.ai.search.search;

import java.util.ArrayList;
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

/**
 * @author Eric Fritz
 * @author Reed Johnson
 */
public class AStarSearch extends BaseSearchAlgorithm
{
	private Heuristic h;

	public AStarSearch(World w, Heuristic h)
	{
		super(w);

		this.h = h;
	}

	@Override
	public SearchResult search(Point initial, final Point goal)
	{
		final Map<Node, Node> pred = new HashMap<Node, Node>();

		PriorityQueue<Node> successors = new PriorityQueue<Node>(16, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2)
			{
				double h1 = o1.getCost();
				double h2 = o2.getCost();

				for (Node n : backtrace(pred, o1)) {
					h1 += n.getPathCost();
				}

				for (Node n : backtrace(pred, o2)) {
					h2 += n.getPathCost();
				}

				return (int) (h1 - h2);
			}
		});

		Node init = new Node(initial, h.heuristic(initial, goal), 0);

		successors.add(init);
		pred.put(init, null);

		int cost = 0;
		while (!successors.isEmpty()) {
			cost++;
			Node current = successors.poll();

			if (current.equals(goal)) {
				return new SearchResult(new ArrayList<Point>(backtrace(pred, current)), cost);
			}

			for (Node successor : getSuccessors(current)) {
				if (!hasKey(pred, successor)) {
					successor.setCost(h.heuristic(successor, goal));

					pred.put(successor, current);
					successors.add(successor);
				}
			}
		}

		return new SearchResult(new ArrayList<Point>(), cost);
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

	private boolean hasKey(Map<Node, Node> m, Node p)
	{
		for (Node a : m.keySet()) {
			if (a.equals(p)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString()
	{
		return "A*";
	}
}
