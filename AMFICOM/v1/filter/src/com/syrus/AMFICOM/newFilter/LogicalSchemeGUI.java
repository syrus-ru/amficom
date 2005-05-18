/*
 * $Id: LogicalSchemeGUI.java,v 1.4 2005/05/18 12:42:50 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/18 12:42:50 $
 * @author $Author: bass $
 * @module filter_v1
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

		JButton andButton = new JButton("AND");
		andButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogicalSchemeGUI.this.controller.addAndItem();
			}
		});
		
		JButton orButton = new JButton("OR");
		orButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogicalSchemeGUI.this.controller.addOrItem();
			}
		});

		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogicalSchemeGUI.this.controller.deleteItem();				
			}
		});

		JButton arrangeButton = new JButton("arrange");
		arrangeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogicalSchemeGUI.this.controller.arrange(jScrollPane.getWidth(), jScrollPane.getHeight());
			}
		});

		
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogicalSchemeGUI.this.controller.confirm();				
			}
		});

		// TODO add listener
		JButton cancelButton = new JButton("Cancel");
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
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
