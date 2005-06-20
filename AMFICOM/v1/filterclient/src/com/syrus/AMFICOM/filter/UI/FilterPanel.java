/*-
 * $Id: FilterPanel.java,v 1.3 2005/06/20 09:33:34 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.filter.UI;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.newFilter.DateCondition;
import com.syrus.AMFICOM.newFilter.Filter;
import com.syrus.AMFICOM.newFilter.FilterController;
import com.syrus.AMFICOM.newFilter.FilterView;
import com.syrus.AMFICOM.newFilter.ListCondition;
import com.syrus.AMFICOM.newFilter.LogicalScheme;
import com.syrus.AMFICOM.newFilter.LogicalSchemeGUI;
import com.syrus.AMFICOM.newFilter.NumberCondition;
import com.syrus.AMFICOM.newFilter.StringCondition;




/**
 * @version $Revision: 1.3 $, $Date: 2005/06/20 09:33:34 $
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
		
	//private JList filteredList;
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
		
	public FilterPanel(Filter filter) {
		this.filter = filter;
		this.parentFrame = Environment.getActiveWindow();
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
		
		//this.filteredList = 			new JList();
		this.conditions = 				new JList();
		this.linkedConditionList = 		new JList();
		
		this.startDateSpinner = 		new DateSpinner();
		this.startTimeSpinner = 		new TimeSpinner();
		this.endDateSpinner = 			new DateSpinner();
		this.endTimeSpinner = 			new TimeSpinner();
		
		//JScrollPane filterScroller = 	new JScrollPane(this.filteredList);
		JScrollPane conditionScroller =	new JScrollPane(this.conditions);
		
		this.removeButton.setEnabled(false);
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
		
		//filterScroller.setPreferredSize(new Dimension(0,0));
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
		
		this.conditionPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.
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
		keysLine.add(this.keysCombo, gbc);
		//typeLine
		gbc.insets = smallRight;
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.VERTICAL;
		typeLine.add(typePanel,gbc);
		gbc.insets = none;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.BOTH;
		typeLine.add(this.conditionPanel,gbc);
		//buttonLine
		gbc.gridwidth = 1;
		gbc.insets = right;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		buttonLine.add(this.addButton,gbc);
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		buttonLine.add(this.changeButton,gbc);
		gbc.insets = none;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		buttonLine.add(this.removeButton,gbc);
		//textConditionAndSchemeLine
		gbc.insets = right;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		textConditionAndSchemeLine.add(this.resultConditionTextField, gbc);
		gbc.insets = none;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		textConditionAndSchemeLine.add(this.createSchemeButton, gbc);
		
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
		this.conditions.addListSelectionListener(this.controller);
		this.startDayButton.addActionListener(this.controller);
		this.endDayButton.addActionListener(this.controller);
		
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
		this.endDateSpinner.setValue(dateCondition.getEndDate());
		this.endTimeSpinner.setValue(dateCondition.getEndDate());	
		CardLayout cardLayout = (CardLayout) this.conditionPanel.getLayout();
		cardLayout.show(this.conditionPanel, DATE_CARD);
	}
	
	public void showErrorMessage(String string) {
		JOptionPane.showMessageDialog(this,
			    string,
			    "",
			    JOptionPane.INFORMATION_MESSAGE);		
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
	
	public Object createdConditionListRef() {
		return this.conditions;
	}
	
	public Object startDayButtonRef() {
		return this.startDayButton;		
	}
	
	public Object endDayButtonRef() {
		return this.endDayButton;		
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
		
		Calendar startYearMonthDay = Calendar.getInstance();
		startYearMonthDay.setTime((Date)this.startDateSpinner.getValue());
		Calendar startTime = Calendar.getInstance();
		startTime.setTime((Date)this.startTimeSpinner.getValue());
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.YEAR, startYearMonthDay.get(Calendar.YEAR));
		startDate.set(Calendar.MONTH, startYearMonthDay.get(Calendar.MONTH));
		startDate.set(Calendar.DAY_OF_MONTH, startYearMonthDay.get(Calendar.DAY_OF_MONTH));
		startDate.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
		startDate.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
		startDate.set(Calendar.SECOND, startTime.get(Calendar.SECOND));
		
		Calendar endYearMonthDay = Calendar.getInstance();
		endYearMonthDay.setTime((Date)this.endDateSpinner.getValue());
		Calendar endTime = Calendar.getInstance();
		endTime.setTime((Date)this.endTimeSpinner.getValue());
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.YEAR, endYearMonthDay.get(Calendar.YEAR));
		endDate.set(Calendar.MONTH, endYearMonthDay.get(Calendar.MONTH));
		endDate.set(Calendar.DAY_OF_MONTH, endYearMonthDay.get(Calendar.DAY_OF_MONTH));
		endDate.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
		endDate.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));
		endDate.set(Calendar.SECOND, endTime.get(Calendar.SECOND));
		
		dateCondition.setStartDate(startDate.getTime());
		dateCondition.setEndDate(endDate.getTime());
		System.out.println();
	}

	public void refreshCreatedConditions(Object[] conditionNames1) {
		this.conditionNames = conditionNames1;
		String[] names = new String[conditionNames1.length];
		for (int i = 0; i < conditionNames1.length; i++) {
			names[i] = "Condition " + (i+1) + " : " + conditionNames1[i];
		}
		this.conditions.setListData(names);
	}

//	public void refreshFilteredEntities(String[] filteredNames) {
//		this.filteredList.setListData(filteredNames);		
//	}

	public void refreshResultConditionString(String stringCondition) {
		this.resultConditionTextField.setText(stringCondition);
	}

	public void enableAdd(boolean b) {
		this.addButton.setEnabled(!b);
		this.changeButton.setEnabled(b);
	}
	
	public void enableRemoveButton(boolean b) {
		this.removeButton.setEnabled(b);		
	}
	
	public void createStartCalendar() {
		createCalendar(this.startDayButton, this.startDateSpinner);
	}

	public void createEndCalendar() {
		createCalendar(this.endDayButton, this.endDateSpinner);
	}
	
	private void createCalendar(JButton button, DateSpinner dateSpiner) {
		Calendar cal = Calendar.getInstance();
		cal.setTime((Date)dateSpiner.getValue());
		JDialog calendarDialog = CalendarUI.createDialogInstance((JFrame)getParentFrame(this), cal, true, true);
		calendarDialog.pack();
		Point buttonUpperLeft = button.getLocationOnScreen();
		Dimension buttonSize = button.getSize();
		Dimension calendarSize = calendarDialog.getSize();
		calendarDialog.setLocation(buttonUpperLeft.x - (calendarSize.width - buttonSize.width)/2,
				buttonUpperLeft.y - (calendarSize.height - buttonSize.height)/2);
		calendarDialog.setVisible(true);
		
		dateSpiner.setValue(cal.getTime());
	}

	private Component getParentFrame(Component component) {
		if(component instanceof JFrame) {
			return component;
		}
		return getParentFrame(component.getParent());
	}
	
	public Filter getFilter() {
		return this.filter;
	}
	
	public void setFilter(Filter filter) {
		this.filter = filter;
		this.controller.setFilter(filter);
		clearPanel();
		
		this.keysCombo.removeActionListener(this.controller);
		this.keysCombo.removeAllItems();
		
		String[] keyNames = this.filter.getKeyNames();
		for (int i = 0; i < keyNames.length; i++) {
			this.keysCombo.addItem(keyNames[i]);
		}
		this.keysCombo.addActionListener(this.controller);
		this.keysCombo.setSelectedIndex(0);
		this.revalidate();
	}
	
	private void clearPanel() {
		/*Object[] nullStub = new Object[0];
		this.conditions.setListData(nullStub);
		this.linkedConditionList.setListData(nullStub);*/
		
		this.conditionTextField.setText("");
		this.equalsField.setText("");
		this.fromField.setText("");
		this.toField.setText("");
		this.resultConditionTextField.setText("");
		
		Date currentDate = new Date();
		this.startTimeSpinner.setValue(currentDate);
		this.startDateSpinner.setValue(currentDate);
		this.endTimeSpinner.setValue(currentDate);
		this.endDateSpinner.setValue(currentDate);
		
		
	}
}

