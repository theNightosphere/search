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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uwm.ai.search.World;
import edu.uwm.ai.search.util.Point;

/**
 * @author Eric Fritz
 * @author Reed Johnson
 */
public class BreadthFirstSearch extends BaseSearchAlgorithm
{
	private World w;

	public BreadthFirstSearch(World w)
	{
		this.w = w;
	}

	@Override
	public SearchResult search(Point initial, final Point goal)
	{
		Map<Point, Point> pred = new HashMap<Point, Point>();
		List<Point> successors = new ArrayList<Point>();

		if (initial.equals(goal)) {
			return new SearchResult(new ArrayList<Point>(), 0);
		}

		int cost = 0;
		for (int depthLimit = 1; depthLimit < 100; depthLimit++) {
			pred.clear();
			successors.clear();

			successors.add(initial);
			pred.put(initial, null);

			while (!successors.isEmpty()) {
				cost++;
				Point current = successors.remove(0);

				for (Point successor : w.getSuccessors(current)) {
					if (!hasKey(pred, successor)) {
						pred.put(successor, current);
						successors.add(successor);
					}

					if (successor.equals(goal)) {
						return new SearchResult(backtrace(pred, successor), cost);
					}
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

	@Override
	public String toString()
	{
		return "BFS";
	}
}
