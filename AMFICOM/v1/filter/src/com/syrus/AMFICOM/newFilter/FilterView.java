/*
 * $Id: FilterView.java,v 1.10 2005/08/25 10:50:46 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.10 $, $Date: 2005/08/25 10:50:46 $
 * @author $Author: max $
 * @module filter
 */
public interface FilterView {

	String	WRONG_NUMBER_MESSAGE	= "you have intered wrong number type in field ";
	String	WRONG_STRING_MESSAGE	= "please, fill the field ";
	String	WRONG_LIST_MESSAGE		= "Select, from list ";
	String	EQUALS_AND_FROM_SIMULTENIOUSLY	= "Fill \"Equals\" or \"From\"";
	String	EQUALS_AND_TO_SIMULTENIOUSLY	= "Fill \"Equals\" or \"To\"";
	String	NO_CONDITIONS_CREATED	= "You have to create condition(s) first";
	String	WRONG_DATE_MESSAGE	= "Please, set the date";

	void showErrorMessage(String message);
	void createLogicalSchemeView(LogicalScheme logicalScheme);
	
	void drawLinkedCondition(ListCondition listCondition);
	void drawStringCondition(StringCondition stringCondition);
	void drawNumberCondition(NumberCondition numberCondition);
	void drawDateCondition(DateCondition dateCondition);
	
	int getSelectedKeyIndex();
	int getSelectedConditionIndex();
	int[] getSelectedConditionIndecies();
	
	String[] getSelectedConditionNames();
	
	Object changeKeyRef();
	Object changeConditionRef();
	Object removeConditionRef();
	Object addConditionRef();
	Object createLogicalSchemeRef();
	Object createdConditionListRef();
	Object startDayButtonRef();
	Object endDayButtonRef();
	
	void setNumberCondition(NumberCondition intCondition);
	void setStringCondition(StringCondition stringCondition);
	void setListCondition(ListCondition listCondition);
	void setDateCondition(DateCondition dateCondition);
	void setKeyNames(String keyNames[]);
	void setSelectedKey(int selectedConditionIndex);
	void setSelectedCondition(int ceratedConditionIndex);
	
	
	void refresh();
	void refreshCreatedConditions(Object[] conditionNames);
	void refreshResultConditionString(String stringCondition);
	void enableAdd(boolean b);
	void enableRemoveButton(boolean b);
	void createStartCalendar();
	void createEndCalendar();
}
