/*
 * $Id: Filter.java,v 1.14 2005/08/09 21:10:10 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.LinkedConditionLoader;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.14 $, $Date: 2005/08/09 21:10:10 $
 * @author $Author: arseniy $
 * @module misc
 */
public class Filter {
	public static final String WRONG_NUMBER_MESSAGE = "you have intered wrong number type in field ";
	public static final String WRONG_STRING_MESSAGE = "please, fill the field ";
	public static final String WRONG_LIST_MESSAGE = "Select, from list ";
	public static final String EQUALS_AND_FROM_SIMULTENIOUSLY = "Fill \"Equals\" or \"From\"";
	public static final String EQUALS_AND_TO_SIMULTENIOUSLY = "Fill \"Equals\" or \"To\"";
	public static final String NO_CONDITIONS_CREATED = "You have to create condition(s) first";
	public static final String WRONG_DATE_MESSAGE = "Please, set the date";

	private List<String> conditionNames = new LinkedList<String>();
	private List<ConditionKey> keys;
	private String[] keyNames;

	private Collection<FilterView> filterViews;

	private short entityCode;

	private LogicalScheme logicalScheme;
	private LinkedConditionLoader linkedConditionLoader;

	public Filter(final ConditionWrapper wrapper, final LinkedConditionLoader linkedConditionLoader) {
		this.keys = new ArrayList<ConditionKey>(wrapper.getKeys());
		this.entityCode = wrapper.getEntityCode();
		this.filterViews = new LinkedList<FilterView>();
		this.linkedConditionLoader = linkedConditionLoader;
		this.keyNames = new String[this.keys.size()];
		int i = 0;
		for (final Iterator<ConditionKey> it = this.keys.iterator(); it.hasNext(); i++) {
			final ConditionKey conditionKey = it.next();
			this.keyNames[i] = conditionKey.getName();
		}
		this.logicalScheme = new LogicalScheme();
	}

	public void addView(final FilterView view) {
		this.filterViews.add(view);
	}

	public void removeView(final FilterView view) {
		this.filterViews.remove(view);
	}

	public void addCondition(final StorableObjectCondition condition, final ConditionKey key) {
		final String conditionName = key.getName();
		this.conditionNames.add(conditionName);
		this.logicalScheme.addCondition(conditionName, condition);
		refreshCreatedConditions();
	}

	public void removeCondition(final String name) {
		this.conditionNames.remove(name);
		this.logicalScheme.removeCondition(name);
		refreshCreatedConditions();
	}

	public void refreshCreatedConditions() {
		for (final FilterView view : this.filterViews) {
			view.refreshCreatedConditions(this.conditionNames.toArray());
			view.refreshResultConditionString(this.logicalScheme.getStringCondition());
		}
	}

	public String[] getKeyNames() {
		return this.keyNames;
	}

	public short getEntityCode() {
		return this.entityCode;
	}

	public List<ConditionKey> getKeys() {
		return this.keys;
	}

	public boolean hasCondition() {
		if (this.conditionNames.size() == 0) {
			return false;
		}
		return true;
	}

	public List<String> getConditionNames() {
		return this.conditionNames;
	}

	public LinkedConditionLoader getLinkedConditionLoader() {
		return this.linkedConditionLoader;
	}

	public LogicalScheme getLogicalScheme() {
		return this.logicalScheme;
	}

	public StorableObjectCondition getCondition() {
		return this.logicalScheme.getResultCondition();
	}
}
