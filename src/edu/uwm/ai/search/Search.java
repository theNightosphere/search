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

import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;

/**
 * @author Eric Fritz
 * @author Reed Johnson
 */
public class Search extends PApplet
{
	private static final long serialVersionUID = 1L;

	public static final int displayWidth = 720;
	public static final int displayHeight = 480;

	World w = new World(this, 48, 32);
	PlayerEntity player = new PlayerEntity(this, w, new Point(5, 5), color(0, 0, 0));

	List<SearchEntity> entities = Arrays.asList(new SearchEntity[] { new SearchEntity(this, w, new Point(1, 1), color(255, 255, 25), player, new AStarSearch(w)), new SearchEntity(this, w, new Point(4, 1), color(25, 255, 255), player, new IterativeDeepening(w)), new SearchEntity(this, w, new Point(3, 3), color(255, 25, 255), player, new DStarSearch(w)) });

	@Override
	public void setup()
	{
		size(displayWidth, displayHeight + (w.getBlockHeight() * entities.size()));
	}

	@Override
	public void keyPressed()
	{
		player.update(keyCode);
	}

	@Override
	public void draw()
	{
		update();

		background(255);

		w.draw();

		noStroke();
		player.draw();

		for (SearchEntity e : entities) {
			e.draw();
		}

		noStroke();
		fill(0, 0, 0);
		rect(0, displayHeight, displayWidth, w.getBlockHeight() * 3);

		fill(255);
		textAlign(LEFT);
		textFont(createFont("Courier New", 12));

		int offset = 0;
		for (SearchEntity e : entities) {
			fill(e.getColor());
			text(e.getResults(), 20, displayHeight + ++offset * w.getBlockHeight() - 4);
		}
	}

	long last = System.currentTimeMillis();

	void update()
	{
		long now = System.currentTimeMillis();

		if (now - 250 > last) {
			last = now;

			for (SearchEntity e : entities) {
				e.update();
			}
		}
	}

	public static void main(String args[])
	{
		PApplet.main(new String[] { "--present", "edu.uwm.ai.search.Search" });
	}
}
