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

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * The JFrame that contains the program.
 * 
 * @author Reed Johnson
 * @author Eric Fritz
 * @date 11.19.2012
 */
@SuppressWarnings("serial")
public class SearchController extends JFrame
{
	public SearchController()
	{
		setTitle("Heuristic Search Comparison");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Search s = new Search();
		s.init();
		add(s);

	}

	public static void main(String[] args)
	{
		SearchController sc = new SearchController();
		sc.pack();
		sc.setMinimumSize(new Dimension(Search.displayWidth + 15, Search.displayHeight + 100));
		sc.setLocationRelativeTo(null);
		sc.setVisible(true);

	}
}