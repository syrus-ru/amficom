/*
 * $Id: FilterView.java,v 1.11 2005/09/21 13:07:14 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

/**
 * @version $Revision: 1.11 $, $Date: 2005/09/21 13:07:14 $
 * @author $Author: max $
 * @module filter
 */
public interface FilterView {

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
