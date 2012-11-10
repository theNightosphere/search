package edu.uwm.ai.search;
import processing.core.PApplet;

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

/**
 * @author Eric Fritz
 * @author Reed Johnson
 */
abstract public class Entity
{
	private PApplet parent;

	private World w;
	private Point p;
	private int c;

	public Entity(PApplet parent, World w, Point p, int c)
	{
		this.parent = parent;

		this.w = w;
		this.p = p;
		this.c = c;
	}

	public World getWorld()
	{
		return w;
	}

	public Point getPoint()
	{
		return p;
	}

	public int getColor()
	{
		return c;
	}

	public void move(Point p)
	{
		move(p.getX(), p.getY());
	}

	public void move(int distX, int distY)
	{
		moveTo(p.getX() + distX, p.getY() + distY);
	}

	public void moveTo(Point p)
	{
		moveTo(p.getX(), p.getY());
	}

	public void moveTo(int newX, int newY)
	{
		if (w.isValidPosition(newX, newY)) {
			p.setX(newX);
			p.setY(newY);
		}
	}

	public void draw()
	{
		parent.fill(c);
		parent.noStroke();

		int dimW = w.getBlockWidth();
		int dimH = w.getBlockHeight();

		int halfW = dimW / 2;
		int halfH = dimH / 2;

		parent.ellipse(p.getX() * dimW + halfW, p.getY() * dimH + halfH, dimW, dimH);
	}
}
