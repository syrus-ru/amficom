/*
 * $Id: FilterGUI.java,v 1.1 2005/03/15 16:11:44 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.syrus.AMFICOM.general.ConditionWrapper;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/15 16:11:44 $
 * @author $Author: max $
 * @module misc
 */
public class FilterGUI extends JFrame {
	
	private static final int HORISONTAL_GAP = 5;
	private static final int VERTICAL_GAP = 5;
	
	private static final String FILTERED_LABEL 			= "Filtered list";
	private static final String KEYS_LABEL 				= "Choose type of condition";
	private static final String ADD_BUTTON 				= "Add condition";
	private static final String REMOVE_BUTTON 			= "Remove condition";
	private static final String STRING_CONDITION_LABEL 	= "Please, inter a string";
	private static final String NUMBER_LABEL 			= "Please, inter a number";
	public static final String EQUALS_LABEL 			= "Equals";
	public static final String OR_LABEL 				= "Or";
	public static final String FROM_LABEL 				= "From";
	public static final String TO_LABEL 				= "To";
	private static final String INCLUDE_BOUNDS_LABEL 	= "Include boundary";
	
	private static final String NUMBER_CARD	= "number";
	private static final String STRING_CARD	= "string";
	private static final String LIST_CARD	= "list";
	
	JPanel panel;
	JPanel conditionPanel;
	JComboBox keysCombo;
	Filter filter;
	
	JList conditionList;
	public JList linkedConditionList;
	
	public JTextField conditionTextField = new JTextField();
	public JTextField equalsField 	= 		new JTextField(5);
	public JTextField fromField 	= 		new JTextField(5);
	public JTextField toField 		= 		new JTextField(5);
	public JCheckBox boundaryCheckBox = 	new JCheckBox();
	
	FilterGUI(ConditionWrapper wrapper) {

		super();
		this.filter = new Filter(wrapper, this);
		createFrame();
	
	}
	
	private void createFrame() {
		
		//TODO: make gbc global variable
		GridBagLayout gbl = 			new GridBagLayout();
		GridBagConstraints gbc = 		new GridBagConstraints();
		CardLayout cardLayout = 		new CardLayout();
		FlowLayout flowLayout = 		new FlowLayout(FlowLayout.LEFT, HORISONTAL_GAP, VERTICAL_GAP);
		
		JLabel filteredLabel = 			new JLabel(FILTERED_LABEL);
		JLabel keysLabel = 				new JLabel(KEYS_LABEL);
		JLabel numberLabel 	= 			new JLabel(NUMBER_LABEL);
		JLabel equalsLabel 	= 			new JLabel(EQUALS_LABEL);
		JLabel orLabel 		= 			new JLabel(OR_LABEL);
		JLabel fromLabel 	= 			new JLabel(FROM_LABEL);
		JLabel toLabel 		= 			new JLabel(TO_LABEL);
		JLabel boundaryLabel = 			new JLabel(INCLUDE_BOUNDS_LABEL);
		JLabel stringConditionLabel = 	new JLabel(STRING_CONDITION_LABEL);
		
		JButton addButton = 			new JButton(ADD_BUTTON);
		JButton removeButton = 			new JButton(REMOVE_BUTTON);
		
		this.panel = 					new JPanel(gbl);
		this.conditionPanel = 			new JPanel(cardLayout);
		JPanel edditPanel = 			new JPanel(gbl);
		JPanel createPanel = 			new JPanel(gbl);
		JPanel filterPanel = 			new JPanel(gbl);
		JPanel createConditionPanel =	new JPanel(gbl);
		JPanel numberPanel =			new JPanel(gbl);
		JPanel stringPanel =			new JPanel(gbl);
		JPanel linkedPanel =			new JPanel(gbl);
		JPanel firstSubPanel = 			new JPanel(flowLayout);
		JPanel secondSubPanel = 		new JPanel(flowLayout);
		JPanel thirdSubPanel = 			new JPanel(flowLayout);
		
		JSplitPane firstSplit = 		new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, createPanel, edditPanel);
		JSplitPane secondSplit = 		new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, filterPanel, createConditionPanel);
		
		
		
		this.keysCombo = 				new JComboBox(this.filter.getKeys());
		
		JList filteredList = 			new JList(this.filter.getFilteredList());
		this.conditionList = 			new JList();
		this.linkedConditionList = 		new JList(this.filter.getLinkedConditionList());
		
		JScrollPane filterScroller = 	new JScrollPane(filteredList);
		JScrollPane conditionScroller =	new JScrollPane(conditionList);
		JScrollPane linkedConditionListScroller = new JScrollPane(linkedConditionList,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		boundaryCheckBox.setSelected(true);
		
		filterScroller.setPreferredSize(new Dimension(0,0));
		conditionScroller.setPreferredSize(new Dimension(0,0));
		linkedConditionListScroller.setPreferredSize(new Dimension(0,0));
		
		firstSplit.setResizeWeight(0.7);
		secondSplit.setResizeWeight(0.4);
		
		this.keysCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeKey();
			}
		});
		
		this.keysCombo.addPopupMenuListener(new PopupMenuListener() {
			
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				filter.saveParameters();				
			}
			public void popupMenuCanceled(PopupMenuEvent e) {
				//Do nothing				
			}
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				//Do nothing				
			}
		});
		
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filter.addCondition();			
			}
		});
		
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filter.removeCondition();
			}			
		});
		
		gbc.insets = new Insets(VERTICAL_GAP, HORISONTAL_GAP, VERTICAL_GAP, HORISONTAL_GAP);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		filterPanel.add(filteredLabel, gbc);
				
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;
		filterPanel.add(filterScroller, gbc);
		
		gbc.weighty = 0.0;
		createConditionPanel.add(keysLabel, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		createConditionPanel.add(this.keysCombo, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;
		createConditionPanel.add(this.conditionPanel, gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.weighty = 0.0;
		createConditionPanel.add(addButton, gbc);
						
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		edditPanel.add(conditionScroller, gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.weighty = 0.0;
		edditPanel.add(removeButton, gbc);
		
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		createPanel.add(secondSplit, gbc);
		
		this.panel.add(firstSplit, gbc);
				
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		stringPanel.add(stringConditionLabel, gbc);
		
		gbc.gridwidth = 1;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		stringPanel.add(conditionTextField, gbc);
				
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		linkedPanel.add(linkedConditionListScroller, gbc);
				
		gbc.weighty = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		numberPanel.add(numberLabel, gbc);
				
		firstSubPanel.add(equalsLabel);
		firstSubPanel.add(equalsField);
		numberPanel.add(firstSubPanel, gbc);
		
		numberPanel.add(orLabel, gbc);
					
		
		secondSubPanel.add(fromLabel);
		secondSubPanel.add(fromField);
		secondSubPanel.add(toLabel);
		secondSubPanel.add(toField);
		numberPanel.add(secondSubPanel, gbc);
		
		thirdSubPanel.add(boundaryLabel);
		thirdSubPanel.add(boundaryCheckBox);
		gbc.weighty = 1;
		numberPanel.add(thirdSubPanel, gbc);
		
		this.conditionPanel.add(numberPanel, NUMBER_CARD);
		this.conditionPanel.add(linkedPanel, LIST_CARD);
		this.conditionPanel.add(stringPanel, STRING_CARD);
		
		changeKey();
		
		this.setSize(600,400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(this.panel);
		this.setVisible(true);
		JOptionPane.showMessageDialog(this,
			    "qweqweqweqwe",
			    "wazapp",
			    JOptionPane.ERROR_MESSAGE);
	}
		
	public void drawNumberCondition(NumberCondition numberCondition) {
		this.equalsField.setText(numberCondition.getEquals());
		this.fromField.setText(numberCondition.getFrom());
		this.toField.setText(numberCondition.getTo());
		this.boundaryCheckBox.setSelected(numberCondition.isIncludeBounds());

		CardLayout cardLayout = (CardLayout) this.conditionPanel.getLayout();
		cardLayout.show(this.conditionPanel, NUMBER_CARD);
	}
	
	public void drawStringCondition(StringCondition stringCondition) {
		this.conditionTextField.setText(stringCondition.getString());
		
		CardLayout cardLayout = (CardLayout) this.conditionPanel.getLayout();
		cardLayout.show(this.conditionPanel, STRING_CARD);
	}
	
	public void drawListCondition() {
		CardLayout cardLayout = (CardLayout) this.conditionPanel.getLayout();
		cardLayout.show(this.conditionPanel, LIST_CARD);
	}
	
	private void changeKey() {
		this.filter.changeKey(getKey());
	}
	
	public String getKey() {
		return (String) this.keysCombo.getSelectedItem();		
	}

	public void errorMessage(String string) {
		JOptionPane.showMessageDialog(this,
			    string,
			    "Inane error",
			    JOptionPane.ERROR_MESSAGE);		
	}	
}
