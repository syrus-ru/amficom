/*
 * $Id: LogicalConditionUITemp.java,v 1.2 2005/03/23 15:04:49 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.newFilter;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.logic.LogicalItem;
import com.syrus.AMFICOM.logic.LogicalSchemeUI;

/**
 * @version $Revision: 1.2 $, $Date: 2005/03/23 15:04:49 $
 * @author $Author: bass $
 * @module filter_v1
 */
public class LogicalConditionUITemp {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3760566377651844662L;

	//private LogicalItem				rootItem;
	//private LogicalItem				savedRootItem;

	//private Collection				rootItems;

	private LogicalSchemeUI		logicalSchemeUI;
	
	private LogicalConditionScheme logicalConditionScheme;
	
	JDialog frame;

	protected static final String	CONDITION_DELETE_ERROR	= "Can't delete condition";

	protected static final String	ROOT_DELETE_ERROR	= "Can't delete root element";

	public LogicalConditionUITemp(LogicalConditionScheme logicalConditionScheme, JFrame parentFrame) {
		this.logicalConditionScheme = logicalConditionScheme;
		frame = new JDialog(parentFrame, true);
		frame.getContentPane().add(getPanel());
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

		
		
		this.logicalSchemeUI = new LogicalSchemeUI(logicalConditionScheme.getRootItem());

		final LogicalSchemeUI logicalSchemeUI = this.logicalSchemeUI;

		final JScrollPane jScrollPane = new JScrollPane();
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
				
				Collection selectedItems = logicalSchemeUI.getSelectedItems();
				for (Iterator it = selectedItems.iterator(); it.hasNext();) {
					LogicalItem selectedItem = (LogicalItem) it.next();
					if(selectedItem.getObject().equals(LogicalItem.CONDITION)) {
						showErrorMessage(CONDITION_DELETE_ERROR);
						return;
					} else if(selectedItem.getObject().equals(LogicalItem.ROOT)) {
						showErrorMessage(ROOT_DELETE_ERROR);
						return;
					}
				}
				logicalSchemeUI.deleteSelectedItems();
			}
		});

		JButton arrangeButton = new JButton("arrange");
		arrangeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				logicalSchemeUI.setSize(jScrollPane.getWidth(), jScrollPane.getHeight());
				logicalSchemeUI.arrange();
			}
		});

		
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(logicalConditionScheme.getValidation(LogicalConditionUITemp.this))
					frame.dispose();
			}
		});

		// TODO add listener
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logicalConditionScheme.restore();
				frame.dispose();
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
	
	public void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(frame,
			    message,
			    "Error",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	/*public JSplitPane getSplitPane() {
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
	}*/

	/*private List getItems() {
		
		this.items = new LinkedList();
		filter.getCreatedConditions();
		
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
	}*/

}
