/*
 * $Id: FilterView.java,v 1.1 2005/03/25 10:29:31 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/25 10:29:31 $
 * @author $Author: max $
 * @module filter_v1
 */
public interface FilterView {

	static final String	WRONG_NUMBER_MESSAGE	= "you have intered wrong number type in field ";
	static final String	WRONG_STRING_MESSAGE	= "please, fill the field ";
	static final String	WRONG_LIST_MESSAGE		= "Select, from list ";
	static final String	EQUALS_AND_FROM_SIMULTENIOUSLY	= "Fill \"Equals\" or \"From\"";
	static final String	EQUALS_AND_TO_SIMULTENIOUSLY	= "Fill \"Equals\" or \"To\"";
	static final String	NO_CONDITIONS_CREATED	= "You have to create condition(s) first";

	void showErrorMessage(String message);
	void createLogicalSchemeView(LogicalScheme logicalScheme, Filter filter);
	
	void drawLinkedCondition(ListCondition listCondition);
	void drawStringCondition(StringCondition stringCondition);
	void drawNumberCondition(NumberCondition numberCondition);
	
	int getSelectedKeyIndex();
	
	Object changeKeyRef();
	Object removeConditionRef();
	Object addConditionRef();
	Object createLogicalSchemeRef();
	
	Object[] getSelectedConditionNames();
	
	void setNumberCondition(NumberCondition intCondition);
	void setStringCondition(StringCondition stringCondition);
	void setListCondition(ListCondition listCondition);
	
	void refreshCreatedConditions(Object[] conditionNames);
	void refreshFilteredEntities(String[] filteredNames);
}
