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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
		final Search s = new Search();
		s.init();

		setLayout(new FlowLayout());
		setTitle("Heuristic Search Comparison");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout());

		JButton button1 = new JButton("world 1");
		panel1.add(button1);
		JButton button2 = new JButton("world 2");
		panel1.add(button2);
		JButton button3 = new JButton("world 3");
		panel1.add(button3);
		JButton button4 = new JButton("world 4");
		panel1.add(button4);

		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout());
		panel2.add(s);

		getContentPane().add(panel1);
		getContentPane().add(panel2);

		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				s.setupWorld(1);
			}
		});

		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				s.setupWorld(2);
			}
		});

		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				s.setupWorld(3);
			}
		});
		
		button4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				s.setupWorld(4);
			}
		});
	}

	public static void main(String[] args)
	{
		SearchController sc = new SearchController();
		sc.setLocationRelativeTo(null);
		sc.setVisible(true);

		sc.setMinimumSize(new Dimension(Search.displayWidth, Search.displayHeight + 125));
	}
}
