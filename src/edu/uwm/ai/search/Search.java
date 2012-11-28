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
import edu.uwm.ai.search.agent.PlayerEntity;
import edu.uwm.ai.search.agent.SearchEntity;
import edu.uwm.ai.search.heuristic.ManhattanDistance;
import edu.uwm.ai.search.search.AStarSearch;
import edu.uwm.ai.search.search.BreadthFirstSearch;
import edu.uwm.ai.search.search.IterativeDeepening;
import edu.uwm.ai.search.search.JumpPointSearch;

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
	private boolean chasing = true;

	private List<SearchEntity> entities = new ArrayList<SearchEntity>();

	@Override
	public void setup()
	{
		size(displayWidth, displayHeight);
		setupWorld(1);
	}

	public int getDisplayWidth()
	{
		return width;
	}

	public int getDisplayHeight()
	{
		return displayHeight;
	}

	public void setupWorld(int index)
	{
		world = new World(this, 48 * index, 32 * index);
		player = new PlayerEntity(this, world, world.getRandomFreePoint(), color(0, 0, 0));

		chasing = false;
		entities = new ArrayList<SearchEntity>();

		if (index == 1) {
			entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(0, 127, 255), player, new BreadthFirstSearch(world)));
			entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(255, 0, 127), player, new IterativeDeepening(world)));
		}

		entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(127, 0, 255), player, new AStarSearch(world, new ManhattanDistance())));
		entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(255, 127, 0), player, new JumpPointSearch(world, new ManhattanDistance())));
	}

	@Override
	public void keyPressed()
	{
		if (keyCode == 32) {
			chasing = !chasing;
		}

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
			fill(0);
			// fill(e.getColor());
			text(e.getResults(), 20, displayHeight - offset++ * 12);
		}
	}

	long last = System.currentTimeMillis();

	void update()
	{
		long now = System.currentTimeMillis();

		if (now - 250 > last && chasing) {
			last = now;

			for (SearchEntity e : entities) {
				e.update();
			}
		}
	}
}
