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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

	public World getWorld()
	{
		return w;
	}

	List<Point> getSuccessors(Point p)
	{
		List<Point> successors = new ArrayList<Point>();

		Point p1 = new Point(p.getX() + 0, p.getY() - 1);
		Point p2 = new Point(p.getX() + 0, p.getY() + 1);
		Point p3 = new Point(p.getX() - 1, p.getY() + 0);
		Point p4 = new Point(p.getX() + 1, p.getY() + 0);

		if (w.isValidPosition(p1))
			successors.add(p1);
		if (w.isValidPosition(p2))
			successors.add(p2);
		if (w.isValidPosition(p3))
			successors.add(p3);
		if (w.isValidPosition(p4))
			successors.add(p4);

		return successors;
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
}
