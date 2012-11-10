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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eric Fritz
 * @author Reed Johnson
 */
public class AStarSearch extends BaseSearchAlgorithm
{
	public AStarSearch(World w)
	{
		super(w);
	}

	@Override
	public SearchResult getPath(Point initial, Point goal)
	{
		Map<Point, Point> pred = new HashMap<Point, Point>();
		List<Point> successors = new ArrayList<Point>();

		successors.add(initial);
		pred.put(initial, null);

		int cost = 0;
		while (!successors.isEmpty()) {
			cost++;
			Point current = successors.remove(0);

			if (current.equals(goal)) {
				return new SearchResult(backtrace(pred, current), cost);
			}

			for (Point successor : getSuccessors(current)) {
				if (!hasKey(pred, successor)) {
					pred.put(successor, current);
					addSuccessor(pred, successors, successor, goal);
				}
			}
		}

		return new SearchResult(new ArrayList<Point>(), cost);
	}

	private boolean hasKey(Map<Point, Point> m, Point p)
	{
		for (Point a : m.keySet()) {
			if (a.equals(p)) {
				return true;
			}
		}

		return false;
	}

	private void addSuccessor(Map<Point, Point> pred, List<Point> successors, Point successor, Point goal)
	{
		double h = heuristic(successor, goal) + backtrace(pred, successor).size();

		int i = 0;
		while (i < successors.size() && h > heuristic(successors.get(i), goal) + backtrace(pred, successors.get(i)).size()) {
			i++;
		}

		successors.add(i, successor);
	}

	private double heuristic(Point p, Point goal)
	{
		return Math.abs(p.getX() - goal.getX()) + Math.abs(p.getY() - goal.getY());
	}

	@Override
	public String toString()
	{
		return "A*";
	}
}
