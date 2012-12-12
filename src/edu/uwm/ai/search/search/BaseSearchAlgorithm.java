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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.uwm.ai.search.World;
import edu.uwm.ai.search.util.Node;
import edu.uwm.ai.search.util.Point;

/**
 * @author Eric Fritz
 * @author Reed Johnson
 */
abstract public class BaseSearchAlgorithm implements SearchAlgorithm
{
	public static double diagCost = 2;

	private World w;

	public BaseSearchAlgorithm(World w)
	{
		this.w = w;
	}

	List<Point> backtrace(Map<Point, Point> predecessors, Point p)
	{
		List<Point> path = new LinkedList<Point>();

		while (p != null) {
			path.add(0, p);
			p = predecessors.get(p);
		}

		return path;
	}

	List<Node> getSuccessors(Point p)
	{
		List<Node> successors = new ArrayList<Node>(8);
		successors.add(new Node(p.getX() + 0, p.getY() - 1, 0, 1));
		successors.add(new Node(p.getX() + 0, p.getY() + 1, 0, 1));
		successors.add(new Node(p.getX() - 1, p.getY() + 0, 0, 1));
		successors.add(new Node(p.getX() + 1, p.getY() + 0, 0, 1));
		successors.add(new Node(p.getX() + 1, p.getY() + 1, 0, diagCost));
		successors.add(new Node(p.getX() + 1, p.getY() - 1, 0, diagCost));
		successors.add(new Node(p.getX() - 1, p.getY() + 1, 0, diagCost));
		successors.add(new Node(p.getX() - 1, p.getY() - 1, 0, diagCost));

		return pruneInvalid(successors, p);
	}

	List<Node> pruneInvalid(List<Node> nodes, Point p)
	{
		List<Node> newNodes = new ArrayList<Node>();

		for (Node n : nodes) {
			if (w.isValidPosition(n) && w.isAccessableThrough(n, p)) {
				newNodes.add(n);
			}
		}

		return newNodes;
	}
}
