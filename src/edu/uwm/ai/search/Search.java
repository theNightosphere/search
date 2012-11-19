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

	private World world;
	private PlayerEntity player;

	private List<SearchEntity> entities = new ArrayList<SearchEntity>();

	@Override
	public void setup()
	{
		world = new World(this, 48, 32);
		player = new PlayerEntity(this, world, world.getRandomFreePoint(), color(0, 0, 0));

		entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(0, 127, 255), player, new BreadthFirstSearch(world)));
		entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(255, 0, 127), player, new IterativeDeepening(world)));
		entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(127, 0, 255), player, new AStarSearch(world, new ManhattanDistance())));

		size(displayWidth, displayHeight + (world.getBlockHeight() * entities.size()));
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

		world.draw();
		player.draw();

		for (SearchEntity e : entities) {
			e.draw();
		}

		fill(255);
		textAlign(LEFT);
		textFont(createFont("Courier New", 12));

		int offset = 0;
		for (SearchEntity e : entities) {
			fill(e.getColor());
			text(e.getResults(), 20, displayHeight + ++offset * world.getBlockHeight() - 4);
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
