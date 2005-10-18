/*
 * $Id: LogicalSchemeGUI.java,v 1.6 2005/10/18 08:51:15 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * @version $Revision: 1.6 $, $Date: 2005/10/18 08:51:15 $
 * @author $Author: max $
 * @module filter
 */
public class LogicalSchemeGUI implements LogicalSchemeView {
	
	private final JDialog frame;
	
	LogicalSchemeController controller;

	public LogicalSchemeGUI(LogicalScheme logicalScheme, JFrame parentFrame) {
		this.controller = new LogicalSchemeController(logicalScheme, this);
		this.frame = new JDialog(parentFrame, true);
		logicalScheme.save();
		createFrame();		
	}

	private void createFrame() {
		
		JPanel panel = new JPanel(new GridBagLayout());
		final JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.getViewport().add(this.controller.getLogicalSchemeUI());
		
		GraphicsEnvironment localGraphicsEnvironment = 
			GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle maximumWindowBounds = 
			localGraphicsEnvironment.getMaximumWindowBounds();
		jScrollPane.setPreferredSize(new Dimension(maximumWindowBounds.width / 3, 
			maximumWindowBounds.height / 3));

		JButton andButton = new JButton(LangModelFilter.getString("filter.logicalscheme.button.and"));
		andButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogicalSchemeGUI.this.controller.addAndItem();
			}
		});
		
		JButton orButton = new JButton(LangModelFilter.getString("filter.logicalscheme.button.or"));
		orButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogicalSchemeGUI.this.controller.addOrItem();
			}
		});

		JButton deleteButton = new JButton(LangModelFilter.getString("filter.logicalscheme.button.delete"));
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogicalSchemeGUI.this.controller.deleteItem();				
			}
		});

		JButton arrangeButton = new JButton(LangModelFilter.getString("filter.logicalscheme.button.arrange"));
		arrangeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogicalSchemeGUI.this.controller.arrange(jScrollPane.getWidth(), jScrollPane.getHeight());
			}
		});

		
		JButton okButton = new JButton(LangModelFilter.getString("filter.logicalscheme.button.ok"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogicalSchemeGUI.this.controller.confirm();				
			}
		});

		// TODO add listener
		final JButton cancelButton = new JButton(LangModelFilter.getString("filter.logicalscheme.button.cancel"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogicalSchemeGUI.this.controller.cancel();
			}
		});

		Box box1 = new Box(BoxLayout.X_AXIS);
		box1.add(andButton);
		box1.add(orButton);
		box1.add(Box.createGlue());

		Box box2 = new Box(BoxLayout.X_AXIS);
		box2.add(deleteButton);
		box2.add(Box.createGlue());
		box2.add(arrangeButton);
		box2.add(Box.createGlue());
		box2.add(cancelButton);
		box2.add(okButton);

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;

		gbc.gridwidth = GridBagConstraints.REMAINDER;

		panel.add(box1, gbc);
		gbc.weighty = 1.0;
		gbc.gridheight = GridBagConstraints.RELATIVE;
		panel.add(jScrollPane, gbc);
		gbc.weighty = 0.0;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		panel.add(box2, gbc);

		this.frame.getContentPane().add(panel);
		this.frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.frame.addComponentListener(new ComponentAdapter() {			
			@Override
			public void componentHidden(ComponentEvent e) {
				cancelButton.doClick();
			}
		});
		this.frame.pack();
		Dimension dimension = new Dimension(this.frame.getSize());
		panel.setMinimumSize(dimension);
		panel.setPreferredSize(dimension);
		this.frame.setSize((int)(dimension.getWidth()*1.2),(int)(dimension.getHeight()*1.2));
		this.frame.setVisible(true);
	}
	public void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this.frame,
			    message,
			    "Error",
			    JOptionPane.ERROR_MESSAGE);
	}
	public void dispose() {
		this.frame.dispose();		
	}
}
