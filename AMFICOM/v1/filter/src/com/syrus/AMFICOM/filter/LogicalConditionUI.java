/*
* $Id: LogicalConditionUI.java,v 1.2 2005/02/22 09:33:46 bob Exp $
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
import javax.swing.ScrollPaneConstants;

import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.logic.LogicalItem;
import com.syrus.AMFICOM.logic.LogicalSchemeUI;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/22 09:33:46 $
 * @author $Author: bob $
 * @module filter_v1
 */
public class LogicalConditionUI extends JPanel {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3760566377651844662L;

	public LogicalConditionUI() {
		List items = new LinkedList();
		LogicalItem result = new LogicalItem(LogicalItem.ROOT);
		
		LogicalItem andOperator1 = new LogicalItem(LogicalItem.AND);
		LogicalItem andOperator2 = new LogicalItem(LogicalItem.AND);
		
		andOperator1.addChild(andOperator2);
		LogicalItem andOperator3 = new LogicalItem(LogicalItem.AND);
		LogicalItem andOperator4 = new LogicalItem(LogicalItem.AND);
		andOperator2.addChild(andOperator3);
		andOperator2.addChild(andOperator4);
		LogicalItem condition1 = new LogicalItem(new EquivalentCondition(ObjectEntities.TEST_ENTITY_CODE));
		LogicalItem condition2 = new LogicalItem(new EquivalentCondition(ObjectEntities.TEST_ENTITY_CODE));
		andOperator1.addChild(condition1);
		andOperator1.addChild(condition2);
		result.addChild(andOperator1);
		items.add(result);
		items.add(andOperator1);
		items.add(andOperator2);
		items.add(andOperator3);
		items.add(andOperator4);
		items.add(condition1);
		items.add(condition2);
		
		LogicalItem result2 = new LogicalItem(LogicalItem.ROOT);
		
		LogicalItem andOperator21 = new LogicalItem(LogicalItem.AND);
		
		result2.addChild(andOperator21);
		items.add(result2);
		items.add(andOperator21);

		
//		LogicalItem result = new LogicalItem(LogicalItem.ROOT);
//		LogicalItem andOperator1 = new LogicalItem(LogicalItem.AND);
//		LogicalItem andOperator2 = new LogicalItem(LogicalItem.AND);
//		result.addChild(andOperator1);
//		andOperator1.addChild(andOperator2);
//		
////		ViewItem result = new ViewItem(1, 0, "Result", "Result", 0, 0);
////		ViewItem andOperator1 = new ViewItem(Integer.MAX_VALUE, 1, "AND", "AND", 0, 0);
////		result.addChild(andOperator1);
//
//		items.add(result);
//		items.add(andOperator1);
//		items.add(andOperator2);

		Dimension dimension = new Dimension(600, 400);
		this.setMinimumSize(dimension);
		this.setPreferredSize(dimension);
		
		final LogicalSchemeUI logicalSchemeUI = new LogicalSchemeUI(items);	

		this.setLayout(new GridBagLayout());

		final JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane.setAutoscrolls(true);
		jScrollPane.getViewport().add(logicalSchemeUI);

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

		Box box = new Box(BoxLayout.X_AXIS);
		box.add(andButton);
		box.add(orButton);
		box.add(Box.createHorizontalStrut(andButton.getWidth()));
		box.add(deleteButton);
		box.add(Box.createGlue());
		box.add(arrangeButton);
		box.add(Box.createGlue());
		box.add(okButton);
		box.add(cancelButton);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = GridBagConstraints.RELATIVE;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		this.add(jScrollPane, gbc);
		// this.add(this.objectPanel, BorderLayout.NORTH);
		// gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		this.add(box, gbc);		
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Filter");
		LogicalConditionUI logicalSchemeUI = new LogicalConditionUI();
		frame.getContentPane().add(logicalSchemeUI);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(false);
		frame.setVisible(true);

	}
}
