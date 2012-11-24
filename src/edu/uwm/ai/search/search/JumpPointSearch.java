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
public class JumpPointSearch extends BaseSearchAlgorithm
{
	private World w;
	private Heuristic h;

	public JumpPointSearch(World w, Heuristic h)
	{
		super(w);

		this.w = w;
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
				return new SearchResult(fill(backtrace(pred, current)), cost);
			}

			for (Node successor : getSuccessors(current, pred.get(current), pred, goal)) {
				if (!hasKey(pred, successor)) {
					successor.setCost(h.heuristic(successor, goal));

					pred.put(successor, current);
					successors.add(successor);
				}
			}
		}

		return new SearchResult(new ArrayList<Point>(), cost);
	}

	List<Point> fill(List<Node> path)
	{
		List<Point> nodes = new ArrayList<Point>();

		for (int i = 1; i < path.size(); i++) {
			Node a = path.get(i - 1);
			Node b = path.get(i - 0);

			int dx = (b.getX() - a.getX()) / Math.max(Math.abs(b.getX() - a.getX()), 1);
			int dy = (b.getY() - a.getY()) / Math.max(Math.abs(b.getY() - a.getY()), 1);

			Point p = a;

			do {
				nodes.add(p);
				p = new Point(p.getX() + dx, p.getY() + dy);
			} while (!p.equals(b));
		}

		nodes.add(path.get(path.size() - 1));

		return nodes;
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

	List<Node> getSuccessors(Node p, Node px, Map<Node, Node> predecessors, Point goal)
	{
		List<Node> nodes = new ArrayList<Node>();

		for (Node n : doGetSuccessors(p, px, predecessors)) {
			Point jump = jump(n.getX(), n.getY(), p.getX(), p.getY(), goal);

			if (jump != null) {
				int dx = Math.abs(jump.getX() - p.getX());
				int dy = Math.abs(jump.getY() - p.getY());

				if (dx != 0 && dy != 0) {
					nodes.add(new Node(jump, 0, dx * Math.sqrt(2)));
				} else {
					nodes.add(new Node(jump, 0, dx + dy));
				}
			}
		}

		return nodes;
	}

	Point jump(int x, int y, int px, int py, Point goal)
	{
		int dx = x - px;
		int dy = y - py;

		if (!w.isValidPosition(x, y)) {
			return null;
		}

		if (goal.getX() == x && goal.getY() == y) {
			return new Point(x, y);
		}

		if (dx != 0 && dy != 0) {
			if ((w.isValidPosition(x - dx, y + dy) && !w.isValidPosition(x - dx, y)) || (w.isValidPosition(x + dx, y - dy) && !w.isValidPosition(x, y - dy))) {
				return new Point(x, y);
			}
		} else {
			if (dx != 0) {
				if ((w.isValidPosition(x + dx, y + 1) && !w.isValidPosition(x, y + 1)) || (w.isValidPosition(x + dx, y - 1) && !w.isValidPosition(x, y - 1))) {
					return new Point(x, y);
				}
			} else {
				if ((w.isValidPosition(x + 1, y + dy) && !w.isValidPosition(x + 1, y)) || (w.isValidPosition(x - 1, y + dy) && !w.isValidPosition(x - 1, y))) {
					return new Point(x, y);
				}
			}
		}

		if (dx != 0 && dy != 0) {
			Point jx = jump(x + dx, y, x, y, goal);
			Point jy = jump(x, y + dy, x, y, goal);

			if (jx != null || jy != null) {
				return new Point(x, y);
			}
		}

		if (w.isValidPosition(x + dx, y) || w.isValidPosition(x, y + dy)) {
			return jump(x + dx, y + dy, x, y, goal);
		} else {
			return null;
		}
	}

	List<Node> doGetSuccessors(Node p, Node px, Map<Node, Node> predecessors)
	{
		List<Node> successors = new ArrayList<Node>(8);

		if (px != null) {
			int dx = (p.getX() - px.getX()) / Math.max(Math.abs(p.getX() - px.getX()), 1);
			int dy = (p.getY() - px.getY()) / Math.max(Math.abs(p.getY() - px.getY()), 1);

			if (dx != 0 && dy != 0) {
				successors.add(new Node(p.getX() + +0, p.getY() + dy, 0, 1));
				successors.add(new Node(p.getX() + dx, p.getY() + +0, 0, 1));
				successors.add(new Node(p.getX() + dx, p.getY() + dy, 0, diagCost));
				successors.add(new Node(p.getX() - dx, p.getY() + dy, 0, diagCost));
				successors.add(new Node(p.getX() + dx, p.getY() - dy, 0, diagCost));
			} else {
				if (dx == 0) {
					if (w.isValidPosition(p.getX(), p.getY() + dy)) {
						successors.add(new Node(p.getX() + 0, p.getY() + dy, 0, 1));
						successors.add(new Node(p.getX() + 1, p.getY() + dy, 0, diagCost));
						successors.add(new Node(p.getX() - 1, p.getY() + dy, 0, diagCost));
					}
				} else {
					if (w.isValidPosition(p.getX() + dx, p.getY())) {
						successors.add(new Node(p.getX() + dx, p.getY() + 0, 0, 1));
						successors.add(new Node(p.getX() + dx, p.getY() + 1, 0, diagCost));
						successors.add(new Node(p.getX() + dx, p.getY() - 1, 0, diagCost));
					}
				}
			}
		} else {
			successors.add(new Node(p.getX() + 0, p.getY() - 1, 0, 1));
			successors.add(new Node(p.getX() + 0, p.getY() + 1, 0, 1));
			successors.add(new Node(p.getX() - 1, p.getY() + 0, 0, 1));
			successors.add(new Node(p.getX() + 1, p.getY() + 0, 0, 1));
			successors.add(new Node(p.getX() + 1, p.getY() + 1, 0, diagCost));
			successors.add(new Node(p.getX() + 1, p.getY() - 1, 0, diagCost));
			successors.add(new Node(p.getX() - 1, p.getY() + 1, 0, diagCost));
			successors.add(new Node(p.getX() - 1, p.getY() - 1, 0, diagCost));
		}

		return pruneInvalid(successors, p);
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
		return "JPS";
	}
}
