/*
 * $Id: Filter.java,v 1.7 2005/04/01 10:42:34 max Exp $
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
import java.util.Map;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.7 $, $Date: 2005/04/01 10:42:34 $
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

	
	private Map	keyNameCondition = new HashMap();
	private Collection filterViews;
	private String[] keys;
	private String[] keyNames;
	private byte[] keyTypes;
	private Collection initialEntities;
	private Collection filteredEntities;
	private ConditionWrapper wrapper;
	private short entityCode;
	private LogicalScheme logicalScheme;
					
	public Filter(ConditionWrapper wrapper) {
		
		this.wrapper 			= wrapper;
		this.keys 				= wrapper.getKeys();
		this.keyNames 			= wrapper.getKeyNames();
		this.keyTypes 			= wrapper.getTypes();
		this.entityCode 		= wrapper.getEntityCode();
		this.initialEntities	= wrapper.getInitialEntities();
		this.filteredEntities	= new LinkedList(this.initialEntities);
		
		this.filterViews = new LinkedList();
		
	}
	
	public void addView (FilterView view) {
		this.filterViews.add(view);
	}
	
	public void removeView (FilterView view) {
		this.filterViews.remove(view);
	}
	
	public String[] getKeys() {
		return this.keys;
	}
	
	public String getKeyName(int index) {
		return this.keyNames[index];
	}
	
	public String[] getLinkedNames(String key) {
		try {
			return this.wrapper.getLinkedNames(key);
		} catch (IllegalDataException e) {
			Log.errorMessage(e.getMessage());
		}
		return new String[0];
	}
	
	/*public String getKeyName(String key) {
		return this.keyNames[getIndex(key)];
	}*/
	
	/*public String getKey(String keyName) {
		return this.keys[getIndex(keyName)];
	}*/
	
	public byte getKeyType(int index) {
		return this.keyTypes[index];
	}
	
	/*private int getIndex(String key) {
		for (int i = 0; i < this.keys.length; i++) {
			String tempKey = this.keys[i];
			if(tempKey.equals(key))
				return i;
		}
		return -1;
	}*/
	
	public String getKey(int index) {
		return this.keys[index];
	}

	public void addCondition(String keyName, StorableObjectCondition condition) {
		this.keyNameCondition.put(keyName, condition);
		if (this.logicalScheme == null)
			this.logicalScheme = new LogicalScheme(keyName, condition);
		else
			this.logicalScheme.addCondition(keyName, condition);
		refreshCreatedConditions();
		refreshFilteredEntities();		
	}
	
	public void refreshFilteredEntities() {
		if (this.logicalScheme == null)
			this.filteredEntities = new LinkedList(this.initialEntities);
		else {
			StorableObjectCondition resultCondition = this.logicalScheme.getResultCondition();
			this.filteredEntities = new LinkedList();
			for (Iterator it = this.initialEntities.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				try {
					if (resultCondition.isConditionTrue(storableObject)) {
						this.filteredEntities.add(storableObject);
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e.getMessage());
				}
			}
		}		
		String[] filteredNames = getFilteredNames();
		for (Iterator it = this.filterViews.iterator(); it.hasNext();) {
			FilterView view = (FilterView) it.next();
			view.refreshFilteredEntities(filteredNames);
		}
	}
	
	/*public void removeCondition(StorableObjectCondition condition) {
		this.conditions.remove(condition);
	}*/

	public short getEntityCode() {
		return this.entityCode;
	}
	
	public Object getLinkedObject(String key, int i) {
		try {
			return this.wrapper.getLinkedObject(key, i);
		} catch (IllegalDataException e) {
			Log.errorMessage(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void removeCondition(String keyName) {
		this.keyNameCondition.remove(keyName);
		if (this.keyNameCondition == null || this.keyNameCondition.size() == 0)
			this.logicalScheme = null;
		else
			this.logicalScheme.removeCondition(keyName);
		refreshCreatedConditions();
		refreshFilteredEntities();
	}

	public Map getKeyNameCondition() {
		return this.keyNameCondition;
	}
	
	public void refreshCreatedConditions() {
		for (Iterator it = this.filterViews.iterator(); it.hasNext();) {
			FilterView view = (FilterView) it.next();
			view.refreshCreatedConditions(this.keyNameCondition.keySet().toArray());
		}
	}
	
//	public void refreshFilteredList() {
//		for (Iterator it = this.filterViews.iterator(); it.hasNext();) {
//			FilterView view = (FilterView) it.next();
//			view.refreshFilteredList(getFilteredNames());
//		}
//	}
	
	public String[] getFilteredNames() {
		int i=0;
		String[] filteredNames = new String[this.filteredEntities.size()];
		for (Iterator iter = this.filteredEntities.iterator(); iter.hasNext();i++) {
			StorableObject storableObject = (StorableObject) iter.next();
			filteredNames[i] = this.wrapper.getInitialName(storableObject);			
		}
		return filteredNames;
	}

	public Object[] getKeyNames() {
		return this.keyNames;
	}

	public StorableObjectCondition getCondition(String keyName) {
		return (StorableObjectCondition) this.keyNameCondition.get(keyName);
	}

	public LogicalScheme getLogicalScheme() {
		return this.logicalScheme;
	}
}
