/*
 * $Id: LogicalConditionUI.java,v 1.4 2005/03/10 15:17:48 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;

import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.logic.LogicalItem;
import com.syrus.AMFICOM.logic.LogicalSchemeUI;
import com.syrus.AMFICOM.logic.LogicalTreeUI;

/**
 * @version $Revision: 1.4 $, $Date: 2005/03/10 15:17:48 $
 * @author $Author: bob $
 * @module filter_v1
 */
public class LogicalConditionUI {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3760566377651844662L;

	private List				items;

	private List				rootItems;

	private LogicalSchemeUI		logicalSchemeUI;

	public LogicalConditionUI() {
		// nothing
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Filter");
		LogicalConditionUI logicalSchemeUI = new LogicalConditionUI();
		frame.getContentPane().add(logicalSchemeUI.getSplitPane());
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

		this.logicalSchemeUI = new LogicalSchemeUI(this.getItems());

		final LogicalSchemeUI logicalSchemeUI = this.logicalSchemeUI;

		final JScrollPane jScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
														ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.setAutoscrolls(true);
		jScrollPane.getViewport().add(this.logicalSchemeUI);

		JButton andButton = new JButton("AND");
		andButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				logicalSchemeUI.addItem(new LogicalItem(LogicalItem.AND));
			}
		});

		JButton orButton = new JButton("OR");
		orButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				logicalSchemeUI.addItem(new LogicalItem(LogicalItem.OR));
			}
		});

		JButton deleteButton = new JButton("delete");
		deleteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				logicalSchemeUI.deleteSelectedItem();
			}
		});

		JButton arrangeButton = new JButton("arrange");
		arrangeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				logicalSchemeUI.setSize(jScrollPane.getWidth(), jScrollPane.getHeight());
				logicalSchemeUI.arrange();
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
		LogicalTreeUI logicalTreeUI = new LogicalTreeUI(this.rootItems);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, logicalTreeUI.getPanel(), panel);
		if (this.logicalSchemeUI != null) {
			logicalTreeUI.addSelectionListener(this.logicalSchemeUI);
			this.logicalSchemeUI.addSelectionListener(logicalTreeUI);
			this.logicalSchemeUI.addAddDeleteItemListener(logicalTreeUI);
		}
		splitPane.setOneTouchExpandable(false);
		splitPane.setResizeWeight(0.2);
		return splitPane;
	}

	private List getItems() {
		if (this.items == null) {
			this.items = new LinkedList();
			LogicalItem result = new LogicalItem(LogicalItem.ROOT);
			if (this.rootItems == null)
				this.rootItems = new LinkedList();
			this.rootItems.add(result);

			LogicalItem andOperator0 = new LogicalItem(LogicalItem.AND);
			
			LogicalItem andOperator1 = new LogicalItem(LogicalItem.AND);
			andOperator0.addChild(andOperator1);
			LogicalItem andOperator2 = new LogicalItem(LogicalItem.AND);
			LogicalItem andOperator3 = new LogicalItem(LogicalItem.AND);
			andOperator1.addChild(andOperator2);
			andOperator1.addChild(andOperator3);
			LogicalItem condition1 = new LogicalItem(new EquivalentCondition(ObjectEntities.TEST_ENTITY_CODE));
			LogicalItem condition2 = new LogicalItem(new EquivalentCondition(ObjectEntities.TEST_ENTITY_CODE));
			andOperator0.addChild(condition1);
			andOperator0.addChild(condition2);
			result.addChild(andOperator0);
			
			LogicalItem andOperator4 = new LogicalItem(LogicalItem.AND);
			andOperator0.addChild(andOperator4);
			LogicalItem andOperator5 = new LogicalItem(LogicalItem.AND);
			LogicalItem andOperator6 = new LogicalItem(LogicalItem.AND);
			LogicalItem andOperator7 = new LogicalItem(LogicalItem.AND);
			andOperator4.addChild(andOperator5);
			andOperator4.addChild(andOperator6);
			andOperator4.addChild(andOperator7);
			
			this.items.add(result);
			this.items.add(andOperator0);
			this.items.add(andOperator1);
			this.items.add(andOperator2);
			this.items.add(andOperator3);
			this.items.add(andOperator4);
			this.items.add(andOperator5);
			this.items.add(andOperator6);
			this.items.add(andOperator7);
			this.items.add(condition1);
			this.items.add(condition2);
		}
		return this.items;
	}

}
