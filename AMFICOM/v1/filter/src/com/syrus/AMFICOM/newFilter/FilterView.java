/*
 * $Id: FilterView.java,v 1.3 2005/04/01 08:37:09 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.3 $, $Date: 2005/04/01 08:37:09 $
 * @author $Author: max $
 * @module filter_v1
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
	void createLogicalSchemeView(LogicalScheme logicalScheme, Filter filter);
	
	void drawLinkedCondition(ListCondition listCondition);
	void drawStringCondition(StringCondition stringCondition);
	void drawNumberCondition(NumberCondition numberCondition);
	void drawDateCondition(DateCondition dateCondition);
	
	int getSelectedKeyIndex();
	
	Object changeKeyRef();
	Object removeConditionRef();
	Object addConditionRef();
	Object createLogicalSchemeRef();
	
	Object[] getSelectedConditionNames();
	
	void setNumberCondition(NumberCondition intCondition);
	void setStringCondition(StringCondition stringCondition);
	void setListCondition(ListCondition listCondition);
	void setDateCondition(DateCondition dateCondition);
	
	void refreshCreatedConditions(Object[] conditionNames);
	void refreshFilteredEntities(String[] filteredNames);
}
