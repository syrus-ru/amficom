/*
 * $Id: LogicalConditionUI.java,v 1.16 2005/04/14 13:23:05 bob Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;

import com.syrus.AMFICOM.logic.AbstractItem;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.LogicalItem;
import com.syrus.AMFICOM.logic.LogicalSchemeUI;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.AMFICOM.logic.Populatable;
import com.syrus.AMFICOM.logic.ServiceItem;

/**
 * @version $Revision: 1.16 $, $Date: 2005/04/14 13:23:05 $
 * @author $Author: bob $
 * @module filter_v1
 */
public class LogicalConditionUI {

	private Item			rootItem;

	private LogicalSchemeUI	logicalSchemeUI;

	public LogicalConditionUI() {
		// nothing
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Filter");
		LogicalConditionUI lsUI = new LogicalConditionUI();
		frame.getContentPane().add(lsUI.getSplitPane());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(false);
		frame.setVisible(true);

	}

	public JPanel getPanel() {
		JPanel panel = new JPanel(new GridBagLayout());

		Dimension dimension = new Dimension(600, 400);
		panel.setMinimumSize(dimension);
		panel.setPreferredSize(dimension);

		final Item rootItem2 = this.getRootItem();
		this.logicalSchemeUI = new LogicalSchemeUI(rootItem2);

		final LogicalSchemeUI logicalSchemeUI1 = this.logicalSchemeUI;

		final JScrollPane jScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
														ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.setAutoscrolls(true);
		jScrollPane.getViewport().add(this.logicalSchemeUI);

		JButton andButton = new JButton("AND");
		andButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				logicalSchemeUI1.addItem(new LogicalItem(LogicalItem.AND));
			}
		});

		JButton orButton = new JButton("OR");
		orButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				logicalSchemeUI1.addItem(new LogicalItem(LogicalItem.OR));
			}
		});
		
		JButton populateButton = new JButton("Populate");

		populateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (rootItem2 instanceof Populatable) {
					Populatable populatable = (Populatable) rootItem2;
					if (!populatable.isPopulated()) {
						populatable.populate();
					}
				}
			}
		});
		

		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				logicalSchemeUI1.deleteSelectedItems();
			}
		});

		JButton arrangeButton = new JButton("arrange");
		arrangeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				logicalSchemeUI1.setSize(jScrollPane.getWidth(), jScrollPane.getHeight());
				logicalSchemeUI1.arrange();
			}
		});

		// TODO add listener
		JButton okButton = new JButton("Ok");
		// TODO add listener
		JButton cancelButton = new JButton("Cancel");

		Box box1 = new Box(BoxLayout.X_AXIS);
		box1.add(andButton);
		box1.add(orButton);
		box1.add(Box.createGlue());
		box1.add(populateButton);

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

		// this.add(this.getTree(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;

		panel.add(box1, gbc);
		gbc.weighty = 1.0;
		gbc.gridheight = GridBagConstraints.RELATIVE;
		panel.add(jScrollPane, gbc);
		gbc.weighty = 0.0;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		panel.add(box2, gbc);

		return panel;
	}

	public JSplitPane getSplitPane() {
		JPanel panel = this.getPanel();
		LogicalTreeUI logicalTreeUI = new LogicalTreeUI(this.getRootItem());
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, logicalTreeUI.getPanel(), panel);
		if (this.logicalSchemeUI != null) {
			logicalTreeUI.addSelectionListener(this.logicalSchemeUI);
			this.logicalSchemeUI.addSelectionListener(logicalTreeUI);
//			this.logicalSchemeUI.addAddDeleteItemListener(logicalTreeUI);
		}
		splitPane.setOneTouchExpandable(false);
		splitPane.setResizeWeight(0.2);
//		this.populateRootItem();
		return splitPane;
	}

	

	private Item getRootItem() {
		if (this.rootItem == null) {
			this.rootItem = new ServiceItem();		
			
			((ServiceItem)this.rootItem).setChildrenFactory(new ChildrenFactory(){
				public void populate(Item item) {
					final LogicalItem result = new LogicalItem(LogicalItem.ROOT);
					
					new Thread() {
						int i = 0;
						public void run() {
							while (true) {		
								String oldName = result.getName();
								result.setName("Result" + i++);
								String newName = result.getName();
								result.propertyChange(new PropertyChangeEvent(this, AbstractItem.OBJECT_NAME_PROPERTY, oldName, newName));
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}.start();

					item.addChild(result);

					LogicalItem andOperator0 = new LogicalItem(LogicalItem.AND);

					LogicalItem andOperator1 = new LogicalItem(LogicalItem.AND);
					andOperator0.addChild(andOperator1);
					LogicalItem andOperator2 = new LogicalItem(LogicalItem.AND);
					LogicalItem andOperator3 = new LogicalItem(LogicalItem.AND);
					andOperator1.addChild(andOperator2);
					andOperator1.addChild(andOperator3);
					
					//LogicalItem condition1 = new LogicalItem();
					//LogicalItem condition2 = new LogicalItem(LogicalItem.CONDITION, "TectCondition2");
					//andOperator0.addChild(condition1);
					//andOperator0.addChild(condition2);
					result.addChild(andOperator0);

					LogicalItem andOperator4 = new LogicalItem(LogicalItem.AND);
					andOperator0.addChild(andOperator4);
					LogicalItem andOperator5 = new LogicalItem(LogicalItem.AND);
					LogicalItem andOperator6 = new LogicalItem(LogicalItem.AND);
					LogicalItem andOperator7 = new LogicalItem(LogicalItem.AND);
					andOperator4.addChild(andOperator5);
					andOperator4.addChild(andOperator6);
					andOperator4.addChild(andOperator7);

					
				}
			}
					
					);
		}
		return this.rootItem;
	}

}
