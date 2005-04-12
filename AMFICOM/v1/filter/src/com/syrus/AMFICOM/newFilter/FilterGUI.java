/*
 * $Id: FilterGUI.java,v 1.8 2005/04/12 13:31:04 max Exp $
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
import java.util.Date;

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

/**
 * @version $Revision: 1.8 $, $Date: 2005/04/12 13:31:04 $
 * @author $Author: max $
 * @module misc
 */
public class FilterGUI extends JFrame implements FilterView {
	
	private static final long	serialVersionUID	= 3257291314054968631L;
	private static final int HORISONTAL_GAP = 5;
	private static final int VERTICAL_GAP = 5;
	
	private static final String FILTERED_LABEL 			= "Filtered list";
	private static final String KEYS_LABEL 				= "Choose type of condition";
	private static final String ADD_BUTTON 				= "Add condition";
	private static final String REMOVE_BUTTON 			= "Remove condition";
	private static final String	LOGIC_SCHEME_BUTTON		= "Logic scheme";
	private static final String STRING_CONDITION_LABEL 	= "Please, inter a string";
	private static final String NUMBER_LABEL 			= "Please, inter a number";
	private static final String EQUALS_LABEL 			= "Equals";
	private static final String OR_LABEL 				= "Or";
	private static final String FROM_LABEL 				= "From";
	private static final String TO_LABEL 				= "To";
	private static final String INCLUDE_BOUNDS_LABEL 	= "Include boundary";
	private static final String	CLEAR_DATE				= "Clear";
	private static final String	SET_START_DATE			= "..";
	private static final String	SET_END_DATE			= "..";
	
	private static final String NUMBER_CARD	= "number";
	private static final String STRING_CARD	= "string";
	private static final String LIST_CARD	= "list";
	private static final String	DATE_CARD	= "date";
	
	private Filter filter;
	
	private JPanel mainPanel;
	private JPanel conditionPanel;
	private JComboBox keysCombo;
		
	private JList filteredList;
	private JList conditions;
	private JList linkedConditionList;
	
	private JTextField conditionTextField = new JTextField();
	private JTextField equalsField 	= 		new JTextField(5);
	private JTextField fromField 	= 		new JTextField(5);
	private JTextField toField 		= 		new JTextField(5);
	private JCheckBox boundaryCheckBox = 	new JCheckBox();
	
	private JButton addButton;  
	private JButton removeButton; 
	private JButton createSchemeButton;
	private JButton startClear;
	private JButton startDayButton;
	private JButton endClear;
	private JButton endDayButton;
	
	private TimeSpinner startTimeSpinner;
	private DateSpinner startDateSpinner;
	private TimeSpinner endTimeSpinner;
	private DateSpinner endDateSpinner;
	
	
	
	private FilterController controller;
		
	public FilterGUI(Filter filter) {
		this.filter = filter;
		this.controller = new FilterController(filter, this);
		createFrame();
	}
	
	private void createFrame() {
		
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
		
		this.addButton = 				new JButton(ADD_BUTTON);
		this.removeButton = 			new JButton(REMOVE_BUTTON);
		this.createSchemeButton = 		new JButton(LOGIC_SCHEME_BUTTON);
		this.startClear = 				new JButton(CLEAR_DATE);
		this.startDayButton = 			new JButton(SET_START_DATE);		
		this.endClear = 				new JButton(CLEAR_DATE);
		this.endDayButton =				new JButton(SET_END_DATE);
		
		this.mainPanel = 				new JPanel(gbl);
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
		JPanel datePanel = 				new JPanel(gbl);
		
		
		JSplitPane firstSplit = 		new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, createPanel, edditPanel);
		JSplitPane secondSplit = 		new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, filterPanel, createConditionPanel);
		
		this.keysCombo = 				new JComboBox(this.filter.getKeyNames());
		
		this.filteredList = 			new JList();
		this.conditions = 				new JList();
		this.linkedConditionList = 		new JList();
		
		this.startDateSpinner = 		new DateSpinner();
		this.startTimeSpinner = 		new TimeSpinner();
		this.endDateSpinner = 			new DateSpinner();
		this.endTimeSpinner = 			new TimeSpinner();
		
		JScrollPane filterScroller = 	new JScrollPane(this.filteredList);
		JScrollPane conditionScroller =	new JScrollPane(this.conditions);
		JScrollPane linkedConditionListScroller = new JScrollPane(this.linkedConditionList);
		
		this.boundaryCheckBox.setSelected(true);
		
		filterScroller.setPreferredSize(new Dimension(0,0));
		conditionScroller.setPreferredSize(new Dimension(0,0));
		linkedConditionListScroller.setPreferredSize(new Dimension(0,0));
		
		firstSplit.setResizeWeight(0.7);
		secondSplit.setResizeWeight(0.4);
		
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
		createConditionPanel.add(this.addButton, gbc);
						
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		edditPanel.add(conditionScroller, gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.weighty = 0.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		edditPanel.add(this.removeButton, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		edditPanel.add(this.createSchemeButton, gbc);
		
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		createPanel.add(secondSplit, gbc);
		
		this.mainPanel.add(firstSplit, gbc);
				
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		stringPanel.add(stringConditionLabel, gbc);
		
		gbc.gridwidth = 1;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		stringPanel.add(this.conditionTextField, gbc);
				
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		linkedPanel.add(linkedConditionListScroller, gbc);
				
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		numberPanel.add(numberLabel, gbc);
				
		firstSubPanel.add(equalsLabel);
		firstSubPanel.add(this.equalsField);
		numberPanel.add(firstSubPanel, gbc);
		
		numberPanel.add(orLabel, gbc);
					
		secondSubPanel.add(fromLabel);
		secondSubPanel.add(this.fromField);
		secondSubPanel.add(toLabel);
		secondSubPanel.add(this.toField);
		numberPanel.add(secondSubPanel, gbc);
		
		thirdSubPanel.add(boundaryLabel);
		thirdSubPanel.add(this.boundaryCheckBox);
		gbc.weighty = 1;
		numberPanel.add(thirdSubPanel, gbc);
		
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = 1;
		datePanel.add(fromLabel, gbc);
		datePanel.add(this.startDateSpinner, gbc);
		datePanel.add(this.startDayButton, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		datePanel.add(this.startTimeSpinner, gbc);
		
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		datePanel.add(toLabel, gbc);
		datePanel.add(this.endDateSpinner, gbc);
		datePanel.add(this.endDayButton, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		datePanel.add(this.endTimeSpinner, gbc);
		
		this.conditionPanel.add(numberPanel, NUMBER_CARD);
		this.conditionPanel.add(linkedPanel, LIST_CARD);
		this.conditionPanel.add(stringPanel, STRING_CARD);
		this.conditionPanel.add(datePanel, DATE_CARD);
		
		this.keysCombo.addActionListener(this.controller);
		this.keysCombo.addPopupMenuListener(this.controller);
		this.keysCombo.setSelectedIndex(0);
		
		this.createSchemeButton.addActionListener(this.controller);
		this.addButton.addActionListener(this.controller);
		this.removeButton.addActionListener(this.controller);
		
		this.getContentPane().add(this.mainPanel);
		//this.setSize(600,600);
		this.pack();
		this.mainPanel.setMinimumSize(this.getSize());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setVisible(true);
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
	
	public void drawLinkedCondition(ListCondition listCondition) {
		this.linkedConditionList.setListData(listCondition.getSelectedNames());
		this.linkedConditionList.setSelectedIndices(listCondition.getSelectedIndices());
		
		CardLayout cardLayout = (CardLayout) this.conditionPanel.getLayout();
		cardLayout.show(this.conditionPanel, LIST_CARD);
	}
	
	public void drawDateCondition(DateCondition dateCondition) {
		this.startDateSpinner.setValue(dateCondition.getStartDate());
		this.startTimeSpinner.setValue(dateCondition.getStartDate());
		this.endDateSpinner.setValue(dateCondition.getStartDate());
		this.endTimeSpinner.setValue(dateCondition.getStartDate());	
		CardLayout cardLayout = (CardLayout) this.conditionPanel.getLayout();
		cardLayout.show(this.conditionPanel, DATE_CARD);
	}
	
	public void showErrorMessage(String string) {
		JOptionPane.showMessageDialog(this,
			    string,
			    "Inane error",
			    JOptionPane.ERROR_MESSAGE);		
	}
	
	public Object changeKeyRef() {
		return this.keysCombo;
	}

	public Object removeConditionRef() {
		return this.removeButton;
	}

	public Object addConditionRef() {
		return this.addButton;
	}
	
	public Object createLogicalSchemeRef() {
		return this.createSchemeButton;
	}

	public int getSelectedKeyIndex() {
		return this.keysCombo.getSelectedIndex();		
	}

	public String[] getSelectedConditionNames() {
		Object[] objectNames = this.conditions.getSelectedValues();
		String[] stringNames = new String[objectNames.length];
		for (int i = 0; i < objectNames.length; i++) {
			stringNames[i] = (String) objectNames[i];
			
		}
		return stringNames;
	}
	
	public void createLogicalSchemeView(LogicalScheme logicalScheme) {
		new LogicalSchemeGUI(logicalScheme, this);
	}
	
	public void setNumberCondition(NumberCondition intCondition) {
		intCondition.setEquals(this.equalsField.getText());
		intCondition.setFrom(this.fromField.getText());
		intCondition.setTo(this.toField.getText());
		intCondition.setIncludeBounds(this.boundaryCheckBox.isSelected());
	}

	public void setStringCondition(StringCondition stringCondition) {
		stringCondition.setString(this.conditionTextField.getText());		
	}

	public void setListCondition(ListCondition listCondition) {
		listCondition.setSelectedIndices(this.linkedConditionList.getSelectedIndices());		
	}
	
	public void setDateCondition(DateCondition dateCondition) {
		long startYearMonthDay = ((Date)this.startDateSpinner.getValue()).getTime(); 
		long startTime = ((Date)this.startTimeSpinner.getValue()).getTime();
		long endYearMonthDay = ((Date)this.endDateSpinner.getValue()).getTime(); 
		long endTime = ((Date)this.endTimeSpinner.getValue()).getTime();
		Date start = new Date(startYearMonthDay + startTime);
		Date end = new Date(endYearMonthDay + endTime);
		dateCondition.setStartDate(start);
		dateCondition.setEndDate(end);		
	}

	public void refreshCreatedConditions(Object[] conditionNames) {
		this.conditions.setListData(conditionNames);
	}

	public void refreshFilteredEntities(String[] filteredNames) {
		this.filteredList.setListData(filteredNames);		
	}
}
