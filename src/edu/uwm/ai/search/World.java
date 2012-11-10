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

import processing.core.PApplet;

/**
 * @author Eric Fritz
 * @author ReedJohnson
 */
public class World
{
	private PApplet parent;

	private final int w;
	private final int h;
	private final boolean[] obstacles;

	public World(PApplet parent, int w, int h)
	{
		this.parent = parent;

		this.w = w;
		this.h = h;
		this.obstacles = new boolean[w * h];

		generateWorld();
	}

	public int getWidth()
	{
		return w;
	}

	public int getHeight()
	{
		return h;
	}

	public boolean isValidPosition(Point p)
	{
		return isValidPosition(p.getX(), p.getY());
	}

	public boolean isValidPosition(int i, int j)
	{
		return i >= 0 && j >= 0 && i < w && j < h && !hasObstacle(i, j);
	}

	public boolean hasObstacle(Point p)
	{
		return hasObstacle(p.getX(), p.getY());
	}

	public boolean hasObstacle(int i, int j)
	{
		return obstacles[getIndex(i, j)];
	}

	public void setObstacle(int i, int j)
	{
		obstacles[getIndex(i, j)] = true;
	}

	public int getBlockWidth()
	{
		return Search.displayWidth / w;
	}

	public int getBlockHeight()
	{
		return Search.displayHeight / h;
	}

	public void draw()
	{
		parent.fill(200, 200, 200);
		parent.stroke(0);

		int dimW = getBlockWidth();
		int dimH = getBlockHeight();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (hasObstacle(i, j)) {
					parent.rect(i * dimW, j * dimH, dimW, dimH);
				}
			}
		}
	}

	private int getIndex(int i, int j)
	{
		return j * h + i;
	}

	private void generateWorld()
	{
		//
		// TODO - generate with better consistency

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (Math.random() < .1) {
					setObstacle(i, j);
				}
			}
		}
	}
}
