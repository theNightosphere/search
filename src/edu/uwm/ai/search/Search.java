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
import edu.uwm.ai.search.search.FloodFillSearch;
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
	private int mIndex, pMapUpdateCost, tMapUpdateCost;
	private double pMapUpdateTime, tMapUpdateTime;

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

	/**
	 * Initializes the world based on the index passed as a parameter. The index corresponds to the 'World' buttons
	 * on the JFrame containing the PApplet
	 * @param index An integer value corresponding to which demonstration to display
	 */
	public void setupWorld(int index)
	{
		switch(index)
		{
		case 1:
			world = new World(this, 48, 32);
			break;
		case 2:
		case 3:
		case 4:
			world = new World(this, 48 * 2, 32 * 2);
			break;
			
		}
		player = new PlayerEntity(this, world, world.getRandomFreePoint(), color(0, 0, 0));
		mIndex = index;
		
		pMapUpdateTime = tMapUpdateTime = 0;

		chasing = false;
		entities = new ArrayList<SearchEntity>();
		switch(index)
		{
		//Displays first demonstration, BFS vs ID vs A* vs A* with JPS
		case 1:
			entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(0, 127, 255), player, new BreadthFirstSearch(world)));
			entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(255, 0, 127), player, new IterativeDeepening(world)));
			entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(127, 0, 255), player, new AStarSearch(world, new ManhattanDistance())));
			entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(255, 127, 0), player, new JumpPointSearch(world, new ManhattanDistance())));
			break;
		//Displays second demonstration, A* vs A* with JPS in a large, open grid.
		case 2:
			entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(127, 0, 255), player, new AStarSearch(world, new ManhattanDistance())));
			entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(255, 127, 0), player, new JumpPointSearch(world, new ManhattanDistance())));
			break;
		//Displays third demonstration, showing path finding time for large groups of entities using JPS
		case 3:
			//entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(127, 0, 255), player, new AStarSearch(world, new ManhattanDistance())));
			for(int i = 0; i < 20; i++)
			{
				entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(255, 127, 0), player, new JumpPointSearch(world, new ManhattanDistance())));
			}
			break;
		//Displays fourth demonstration, showing path finding time for large groups using flood fill.
		case 4:
			for(int i = 0; i < 20; i++)
			{
				entities.add(new SearchEntity(this, world, world.getRandomFreePoint(), color(127, 255, 0), player,
						                      new FloodFillSearch(world, new ManhattanDistance())));
			}
		}
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
		
		//Accumulators
		int totalSearches = 0;
		int totalPCost = 0;
		int totalTCost = 0;
		double totalPTime = 0.0;
		double totalTTime = 0.0;
		String displayString = "";
		switch(mIndex)
		{
		case 1:
		case 2:
			for (SearchEntity e : entities) {
				fill(0);
				// fill(e.getColor());
				text(e.getResults(), 20, displayHeight - offset++ * 12);
			}
			break;
		case 3:
			for (SearchEntity e : entities) 
			{
				fill(0);
				totalPTime += e.getPTime();
				totalTTime += e.getTTime();
				totalPCost += e.getPCost();
				totalTCost += e.getTCost();
				totalSearches += e.getTotalSearches();
				
			}
			displayString = String.format("[%3s] %8.2fms, %8.2fms total, %8.2fms avg, %6d nodes expanded, %6d total, %6d avg", 
			          "Flood Fill", totalPTime, totalTTime, 
			          totalSearches == 0 ? 0 : (totalTTime / totalSearches), totalPCost,
				      totalTCost, totalSearches == 0 ? 0 : (totalTCost / totalSearches));
			text(displayString, 20, displayHeight - offset);
			break;
		case 4:
			for (SearchEntity e : entities) 
			{
				fill(0);
				totalPTime += e.getPTime();
				totalTTime += e.getTTime();
				totalSearches += e.getTotalSearches();
				
			}
			totalPTime += pMapUpdateTime;
			totalTTime += tMapUpdateTime; 
			displayString = String.format("[%3s] %8.2fms, %8.2fms total, %8.2fms avg, %6d nodes expanded, %6d total, %6d avg", 
			          entities.get(0).getAlgorithm().toString(), totalPTime, totalTTime, 
			          totalSearches == 0 ? 0 : (totalTTime / totalSearches), pMapUpdateCost,
				      tMapUpdateCost, totalSearches == 0 ? 0 : (tMapUpdateCost / totalSearches));
			text(displayString, 20, displayHeight - offset);
			break;
		}
	}

	long last = System.currentTimeMillis();

	void update()
	{
		long now = System.currentTimeMillis();

		if (now - 250 > last && chasing) {
			last = now;
			switch(mIndex)
			{
			//Displays 1 through 3 deal with search slightly differently than case 4, which updates the map.
			case 1:
			case 2:
			case 3:
				for (SearchEntity e : entities) {
					e.update();
				}
				break;
			case 4:
				//The lookup table must be updated separately, but should only be done once.
				//Timing of the update is also important as its the main workhorse of the flood fill.
				if(entities.get(0).hasEntityMoved())
				{
					long st = System.nanoTime();
					pMapUpdateCost = world.updateWorldMap(player);
					tMapUpdateCost += pMapUpdateCost;
					pMapUpdateTime = (System.nanoTime() - st) / 1e6;
					tMapUpdateTime += pMapUpdateTime;
				}
				//Flood-Fill entities will update their paths if they need to. 
				for (SearchEntity e : entities)
				{
					e.update();
				}
			}
		}
	}
}
