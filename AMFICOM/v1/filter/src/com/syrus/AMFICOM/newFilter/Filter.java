/*-
 * $Id: Filter.java,v 1.20 2005/10/30 15:20:27 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.newFilter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.20 $, $Date: 2005/10/30 15:20:27 $
 * @author $Author: bass $
 * @module filter
 */
public class Filter {
	
	private List<ConditionKey> createdConditionKeys = new LinkedList<ConditionKey>();
	private LogicalScheme logicalScheme = new LogicalScheme();
	private Collection<FilterView> filterViews = new LinkedList<FilterView>();
	private List<ConditionKey> keys;
	private short entityCode;
	
	public Filter(final ConditionWrapper wrapper) {
		this.keys = wrapper.getKeys();
		this.entityCode = wrapper.getEntityCode();
	}
	
	public void addView(final FilterView view) {
		this.filterViews.add(view);
	}
	
	public void removeView(final FilterView view) {
		this.filterViews.remove(view);
	}
	
	public void addCondition(final StorableObjectCondition condition, final ConditionKey key) {
		if (!this.keys.contains(key)) {
			assert Log.errorMessage("Illegal data: Filter.addCondition | Wrong key (you must select it from appropriate condition wrapper)");
		}
		addCondition0(condition, key);
	}
	
	void addCondition0(final StorableObjectCondition condition, final ConditionKey key) {
		final String conditionName = key.getName();
		if(!this.createdConditionKeys.contains(key)) {
			this.createdConditionKeys.add(key);
		}
		this.logicalScheme.addCondition(conditionName, condition);
		refresh();
	}
	
	public void removeCondition(final ConditionKey key) {
		this.createdConditionKeys.remove(key);
		this.logicalScheme.removeCondition(key.getName());
		refresh();
	}

	private void refresh() {
		for (FilterView view : this.filterViews) {
			view.refresh();
		}
	}
	
	short getEntityCode() {
		return this.entityCode;
	}
	
	List<ConditionKey> getKeys() {
		return this.keys;
	}

	boolean hasCondition() {
		if (this.createdConditionKeys.size() == 0)
			return false;
		return true;
	}
	
	List<String> getConditionNames() {
		final List<String> names = new LinkedList<String>();
		for(ConditionKey key : this.createdConditionKeys) {
			names.add(key.getName());
		}
		return names;
	}
	
	LogicalScheme getLogicalScheme() {
		return this.logicalScheme;
	}
	
	public StorableObjectCondition getCondition() {
		return this.logicalScheme.getResultCondition();
	}

	ConditionKey getCreatedConditionKey(int index) {
		return this.createdConditionKeys.get(index);
	}
	
	int getKeyIndex(final ConditionKey key) {
		return this.keys.indexOf(key);
	}
	
	int getCreatedConditionKeyIndex(final ConditionKey key) {
		return this.createdConditionKeys.indexOf(key);
	}

	List<ConditionKey> getCreatedConditionKeys() {
		return this.createdConditionKeys;	
	}
	
	StorableObjectCondition getCreatedCondition(final ConditionKey key) {
		return this.logicalScheme.getCondition(key.getName());
	}
}
