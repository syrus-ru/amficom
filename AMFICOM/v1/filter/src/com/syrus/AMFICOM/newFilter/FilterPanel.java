/*-
 * $Id: FilterPanel.java,v 1.1 2005/04/15 16:38:25 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.newFilter;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;



/**
 * @version $Revision: 1.1 $, $Date: 2005/04/15 16:38:25 $
 * @author $Author: max $
 * @module filter_v1
 */
public class FilterPanel extends JScrollPane implements FilterView {
	
	private static final long	serialVersionUID	= 3257291314054968631L;
	private static final int HORISONTAL_GAP = 5;
	private static final int VERTICAL_GAP = 5;
	
	private static final String	CREATED_CONDITON_LABEL	= "Result condition";
	private static final String KEYS_LABEL 				= "Criterion";
	private static final String ADD_BUTTON 				= "Add";
	private static final String CHANGE_BUTTON 				= "Change";
	private static final String REMOVE_BUTTON 			= "Remove";
	private static final String	LOGIC_SCHEME_BUTTON		= ">>";
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
	
	private JTextField conditionTextField = 		new JTextField();
	private JTextField equalsField = 				new JTextField(5);
	private JTextField fromField =	 				new JTextField(5);
	private JTextField toField = 					new JTextField(5);
	private JTextField resultConditionTextField	=	new JTextField();
	
	
	private JCheckBox boundaryCheckBox = 			new JCheckBox();
	
	private JButton addButton;
	private JButton changeButton;
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
	private Object[]	conditionNames;
	private JFrame parentFrame;
		
	public FilterPanel(Filter filter, JFrame parentFrame) {
		this.filter = filter;
		this.parentFrame = parentFrame;
		this.controller = new FilterController(filter, this);
		this.setBorder(null);
		createFrame();
	}
	
	private void createFrame() {
		
		GridBagLayout gbl = 			new GridBagLayout();
		GridBagConstraints gbc = 		new GridBagConstraints();
		CardLayout cardLayout = 		new CardLayout();
		FlowLayout flowLayout = 		new FlowLayout(FlowLayout.LEFT, HORISONTAL_GAP, VERTICAL_GAP);
		
		JLabel resultConditionLabel = 	new JLabel(CREATED_CONDITON_LABEL);
		JLabel keysLabel = 				new JLabel(KEYS_LABEL);
		JLabel numberLabel 	= 			new JLabel(NUMBER_LABEL);
		JLabel equalsLabel 	= 			new JLabel(EQUALS_LABEL);
		JLabel orLabel 		= 			new JLabel(OR_LABEL);
		JLabel fromLabel 	= 			new JLabel(FROM_LABEL);
		JLabel toLabel 		= 			new JLabel(TO_LABEL);
		JLabel boundaryLabel = 			new JLabel(INCLUDE_BOUNDS_LABEL);
		JLabel stringConditionLabel = 	new JLabel(STRING_CONDITION_LABEL);
		
		this.addButton = 				new JButton(ADD_BUTTON);
		this.changeButton = 			new JButton(CHANGE_BUTTON);
		this.removeButton = 			new JButton(REMOVE_BUTTON);
		this.createSchemeButton = 		new JButton(LOGIC_SCHEME_BUTTON);
		this.startClear = 				new JButton(CLEAR_DATE);
		this.startDayButton = 			new JButton(SET_START_DATE);		
		this.endClear = 				new JButton(CLEAR_DATE);
		this.endDayButton =				new JButton(SET_END_DATE);
		
		this.mainPanel = 				new JPanel(gbl);
		this.conditionPanel = 			new JPanel(cardLayout);
		JPanel typePanel = 				new JPanel();
		JPanel filterPanel = 			new JPanel(gbl);
		JPanel numberPanel =			new JPanel(gbl);
		JPanel stringPanel =			new JPanel(gbl);
		JPanel linkedPanel =			new JPanel(gbl);
		JPanel firstSubPanel = 			new JPanel(flowLayout);
		JPanel secondSubPanel = 		new JPanel(flowLayout);
		JPanel thirdSubPanel = 			new JPanel(flowLayout);
		JPanel datePanel = 				new JPanel(gbl);
		
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
		
		//TODO: all setIcons move to Enviroment.java
		JRadioButton eqRadioButton = new JRadioButton();
		eqRadioButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
				getImage("images/filter_equal.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		JRadioButton rangeRadioButton = new JRadioButton();
		rangeRadioButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
				getImage("images/filter_diapazon.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		JRadioButton timeRadioButton = new JRadioButton();
		timeRadioButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
				getImage("images/filter_time.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		JRadioButton subRadioButton = new JRadioButton();
		subRadioButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
				getImage("images/filter_substring.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		JRadioButton listRadioButton = new JRadioButton();
		listRadioButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
				getImage("images/filter_list.gif").getScaledInstance(16, 16,Image.SCALE_SMOOTH)));
		
		JRadioButton emptyRadioButton = new JRadioButton();

		ButtonGroup radio;
		
		JScrollPane linkedConditionListScroller = new JScrollPane(this.linkedConditionList);
		
		typePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		typePanel.setLayout(new GridLayout(0,1));
		typePanel.add(eqRadioButton, null);
		typePanel.add(rangeRadioButton, null);
		typePanel.add(timeRadioButton, null);
		typePanel.add(subRadioButton, null);
		typePanel.add(listRadioButton, null);
		
		this.boundaryCheckBox.setSelected(true);
		
		filterScroller.setPreferredSize(new Dimension(0,0));
		conditionScroller.setPreferredSize(new Dimension(0,0));
		linkedConditionListScroller.setPreferredSize(new Dimension(0,0));
		
		radio = new ButtonGroup();
		
		radio.add(eqRadioButton);
		radio.add(rangeRadioButton);
		radio.add(timeRadioButton);
		radio.add(subRadioButton);
		radio.add(listRadioButton);
		radio.add(emptyRadioButton);

		filterPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.
			RAISED));
		
		conditionPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.
				RAISED));
		Insets none = new Insets( 0, 0, 0, 0);
		Insets right = new Insets( 0, 0, 0, HORISONTAL_GAP);
		Insets smallRight = new Insets( 0, 0, 0, HORISONTAL_GAP/2);
		Insets topLeftRight = new Insets(VERTICAL_GAP, HORISONTAL_GAP, 0, HORISONTAL_GAP);
		Insets both = new Insets(VERTICAL_GAP, HORISONTAL_GAP, VERTICAL_GAP, HORISONTAL_GAP);
		
		JPanel keysLine = new JPanel(gbl);
		JPanel typeLine = new JPanel(gbl);
		JPanel buttonLine = new JPanel(gbl);
		JPanel textConditionAndSchemeLine = new JPanel(gbl);
		//keysLine
		gbc.insets = right;
		gbc.weightx = 0;
		keysLine.add(keysLabel, gbc);
		gbc.insets = none;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		keysLine.add(keysCombo, gbc);
		//typeLine
		gbc.insets = smallRight;
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.VERTICAL;
		typeLine.add(typePanel,gbc);
		gbc.insets = none;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.BOTH;
		typeLine.add(conditionPanel,gbc);
		//buttonLine
		gbc.gridwidth = 1;
		gbc.insets = right;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		buttonLine.add(addButton,gbc);
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		buttonLine.add(changeButton,gbc);
		gbc.insets = none;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		buttonLine.add(removeButton,gbc);
		//textConditionAndSchemeLine
		gbc.insets = right;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		textConditionAndSchemeLine.add(resultConditionTextField, gbc);
		gbc.insets = none;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		textConditionAndSchemeLine.add(createSchemeButton, gbc);
		
		//main panel
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = topLeftRight;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.mainPanel.add(keysLine, gbc);
		gbc.weighty = 1;
		this.mainPanel.add(typeLine, gbc);
		gbc.weighty = 0;
		this.mainPanel.add(buttonLine, gbc);
		gbc.weighty = 1;
		this.mainPanel.add(conditionScroller, gbc);
		gbc.weighty = 0;
		this.mainPanel.add(resultConditionLabel, gbc);
		gbc.insets = both;
		this.mainPanel.add(textConditionAndSchemeLine, gbc);
		
		//condition panel
		gbc.insets = both;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		stringPanel.add(stringConditionLabel, gbc);
		stringPanel.setPreferredSize(new Dimension(0,0));
		
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
		this.conditionPanel.setPreferredSize(new Dimension(0,0));
		
		this.keysCombo.addActionListener(this.controller);
		this.keysCombo.addPopupMenuListener(this.controller);
		this.keysCombo.setSelectedIndex(0);
		
		this.createSchemeButton.addActionListener(this.controller);
		this.addButton.addActionListener(this.controller);
		this.removeButton.addActionListener(this.controller);
				
		this.getViewport().add(this.mainPanel);
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
		int[] indices = this.conditions.getSelectedIndices();
		String[] selectedNames = new String[indices.length];
		for (int i = 0; i < indices.length; i++) {
			int index = indices[i];
			selectedNames[i] = (String) this.conditionNames[index];
		}
		return selectedNames; 
	}
	
	public void createLogicalSchemeView(LogicalScheme logicalScheme) {
		new LogicalSchemeGUI(logicalScheme, this.parentFrame);
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
		this.conditionNames = conditionNames;
		String[] names = new String[conditionNames.length]; 
		for (int i = 0; i < conditionNames.length; i++) {
			names[i] = "Condition " + (i+1) + " : " + conditionNames[i];
		}
		this.conditions.setListData(names);
	}

	public void refreshFilteredEntities(String[] filteredNames) {
		this.filteredList.setListData(filteredNames);		
	}

	public void refreshResultConditionString(String stringCondition) {
		this.resultConditionTextField.setText(stringCondition);
	}

	public void enableChangeDisableAdd(boolean b) {
		this.addButton.setEnabled(!b);
		this.changeButton.setEnabled(b);
	}
}

