/*
 * $Id: Filter.java,v 1.8 2005/04/12 13:00:23 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.LinkedConditionLoader;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.8 $, $Date: 2005/04/12 13:00:23 $
 * @author $Author: max $
 * @module misc
 */
public class Filter {
	
	public static final String	WRONG_NUMBER_MESSAGE	= "you have intered wrong number type in field ";
	public static final String	WRONG_STRING_MESSAGE	= "please, fill the field ";
	public static final String	WRONG_LIST_MESSAGE		= "Select, from list ";
	public static final String	EQUALS_AND_FROM_SIMULTENIOUSLY	= "Fill \"Equals\" or \"From\"";
	public static final String	EQUALS_AND_TO_SIMULTENIOUSLY	= "Fill \"Equals\" or \"To\"";
	public static final String	NO_CONDITIONS_CREATED	= "You have to create condition(s) first";
	public static final String	WRONG_DATE_MESSAGE	= "Please, set the date";

	private Map nameCondition = new HashMap();
	private List keys;
	private String[] keyNames;
	
	private Collection filterViews;
	
	private short	entityCode;
	
	LogicalScheme logicalScheme;
	LinkedConditionLoader linkedConditionLoader;
		
	public Filter(ConditionWrapper wrapper, LinkedConditionLoader linkedConditionLoader) {
		this.keys = wrapper.getKeys();
		this.entityCode = wrapper.getEntityCode(); 
		this.filterViews = new LinkedList();
		this.linkedConditionLoader = linkedConditionLoader;
		this.keyNames = new String[this.keys.size()];
		int i = 0;
		for (Iterator it = this.keys.iterator(); it.hasNext();i++) {
			ConditionKey conditionKey = (ConditionKey) it.next();
			this.keyNames[i] = conditionKey.getName();
		}
		this.logicalScheme = new LogicalScheme();
	}
	
	public void addView (FilterView view) {
		this.filterViews.add(view);
	}
	
	public void removeView (FilterView view) {
		this.filterViews.remove(view);
	}
	
	public void addCondition(StorableObjectCondition condition, ConditionKey key) {
		String conditionName = "Condition " + this.keys.indexOf(key) + " : " + key.getName();
		this.nameCondition.put(conditionName, condition);
		this.logicalScheme.addCondition(conditionName, condition);
		refreshCreatedConditions();
	}
	
	public void removeCondition(String name) {
		this.nameCondition.remove(name);
		this.logicalScheme.removeCondition(name);
		refreshCreatedConditions();
	}

	public void refreshCreatedConditions() {
		for (Iterator it = this.filterViews.iterator(); it.hasNext();) {
			FilterView view = (FilterView) it.next();
			view.refreshCreatedConditions(this.nameCondition.keySet().toArray());
		}
	}
	
	public String[] getKeyNames() {
		return this.keyNames;
	}

	public short getEntityCode() {
		return this.entityCode;
	}
	public List getKeys() {
		return this.keys;
	}

	public boolean hasCondition() {
		if (this.nameCondition.size() == 0)
			return false;
		return true;
	}	
}
