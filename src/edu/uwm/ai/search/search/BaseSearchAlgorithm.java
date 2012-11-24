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

		Node n1 = new Node(p.getX() + 0, p.getY() - 1, 0, 1);
		Node n2 = new Node(p.getX() + 0, p.getY() + 1, 0, 1);
		Node n3 = new Node(p.getX() - 1, p.getY() + 0, 0, 1);
		Node n4 = new Node(p.getX() + 1, p.getY() + 0, 0, 1);

		double diagCost = Math.sqrt(2);
		Node n5 = new Node(p.getX() + 1, p.getY() + 1, 0, diagCost);
		Node n6 = new Node(p.getX() + 1, p.getY() - 1, 0, diagCost);
		Node n7 = new Node(p.getX() - 1, p.getY() + 1, 0, diagCost);
		Node n8 = new Node(p.getX() - 1, p.getY() - 1, 0, diagCost);

		if (w.isValidPosition(n1))
			successors.add(n1);
		if (w.isValidPosition(n2))
			successors.add(n2);
		if (w.isValidPosition(n3))
			successors.add(n3);
		if (w.isValidPosition(n4))
			successors.add(n4);

		if (w.isValidPosition(n5) && w.isAccessableThrough(n5, p))
			successors.add(n5);
		if (w.isValidPosition(n6) && w.isAccessableThrough(n6, p))
			successors.add(n6);
		if (w.isValidPosition(n7) && w.isAccessableThrough(n7, p))
			successors.add(n7);
		if (w.isValidPosition(n8) && w.isAccessableThrough(n8, p))
			successors.add(n8);

		return successors;
	}
}
