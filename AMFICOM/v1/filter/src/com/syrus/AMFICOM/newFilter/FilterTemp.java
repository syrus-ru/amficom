/*
 * $Id: FilterTemp.java,v 1.1 2005/03/15 16:11:44 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ConditionWrapperTemp;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/15 16:11:44 $
 * @author $Author: max $
 * @module misc
 */
public class FilterTemp {
	
	private LogicalConditionScheme logicalConditionScheme;
	private FilterGUITemp filterGUI;
	private ConditionWrapperTemp wrapper;
	private short entityCode;
	private Map keyNameCondition = new HashMap(); 
	private Map	keyTemporalCondition = new HashMap();
	private static final String	WRONG_NUMBER_MESSAGE	= "you have intered wrong number type in field ";
	private static final String	WRONG_STRING_MESSAGE	= "please, fill the field ";
	private static final String	WRONG_LIST_MESSAGE		= "Select, from list ";
		
	public FilterTemp(ConditionWrapperTemp wrapper, FilterGUITemp filterGUI) {
		this.wrapper = wrapper;
		entityCode = wrapper.getEntityCode();
		this.filterGUI = filterGUI;
		
//		for (Iterator iter = wrapper.getKeys().iterator(); iter.hasNext();) {
//			String key = (String) iter.next();
//			byte type = wrapper			
//		}
	}
	
	
	public void createTypicalCondition(final long firstLong,
			final long secondLong,
			final OperationSort operation,
			final String key) {
		this.keyNameCondition.put(key, new TypicalCondition(firstLong, secondLong, operation , this.entityCode, key));		
	}
	
	public void createTypicalCondition(final Date firstDate,
			final Date secondDate,
			final OperationSort operation,
			final String key) {
		this.keyNameCondition.put(key, new TypicalCondition(firstDate, secondDate, operation , this.entityCode, key));
	}
	
	public void createTypicalCondition(final double firstDouble,
			final double secondDouble,
			final OperationSort operation,
			final String key) {
		this.keyNameCondition.put(key, new TypicalCondition(firstDouble, secondDouble, operation , this.entityCode, key));
	}
	
	public void createTypicalCondition(final int firstInt,
			final int secondInt,
			final OperationSort operation,
			final String key) {
		this.keyNameCondition.put(key, new TypicalCondition(firstInt, secondInt, operation , this.entityCode, key));
	}
	
	public void addTypicalCondition(final String value, 
			final OperationSort operation, 
			final String keyName) {
		this.keyNameCondition.put(keyName, new TypicalCondition(value, operation , this.entityCode, keyName));
	}
	
	public void createLinkedIdsCondition(String keyName, final Identifier identifier) {
		this.keyNameCondition.put(keyName, new LinkedIdsCondition(identifier, this.entityCode));
	}

	public void createLinkedIdsCondition(String keyName, final List linkedIds) {
		this.keyNameCondition.put(keyName, new LinkedIdsCondition(linkedIds, this.entityCode));
	}
	
	public Collection getCreatedConditions() {
		return this.keyNameCondition.values();
	}

//	public StorableObjectCondition getResultCondition() throws CreateObjectException, IllegalDataException {
//		LogicalConditionScheme logicalConditionScheme = new LogicalConditionScheme(this.getCreatedConditions());
//		return logicalConditionScheme.getResultCondition();				
//	}

	public void changeKey(int keyIndex) {
		String key = this.wrapper.getKey(keyIndex);
		byte type = this.wrapper.getType(keyIndex);
		switch (type) {
		case ConditionWrapper.INT:
		case ConditionWrapper.FLOAT:
		case ConditionWrapper.DOUBLE:
			NumberCondition numberCondition = (NumberCondition) this.keyTemporalCondition.get(key);
			if(numberCondition == null) {
				numberCondition = new NumberCondition("", "", "", true);
				this.keyTemporalCondition.put(key, numberCondition);				
			}
			this.filterGUI.drawNumberCondition(numberCondition);
			break;
		case ConditionWrapper.STRING:
			StringCondition stringCondition = (StringCondition) this.keyTemporalCondition.get(key);
			if(stringCondition == null) {
				stringCondition = new StringCondition("");
				this.keyTemporalCondition.put(key, stringCondition);
			}
			this.filterGUI.drawStringCondition(stringCondition);
			break;
		case ConditionWrapper.LIST:
			String[] linkedNames = null;
			try {
				linkedNames = this.wrapper.getLinkedNames(key);
			} catch (IllegalDataException e) {
				Log.errorMessage(e.getMessage());
			}
			ListCondition listCondition = (ListCondition) this.keyTemporalCondition.get(key);
			if(listCondition == null) {
				listCondition = new ListCondition(new int[0], linkedNames);
				this.keyTemporalCondition.put(key, listCondition);
			}
			this.filterGUI.drawLinkedCondition(listCondition);
			break;
		default:
			Log.errorMessage("Filter.changeKey | Unsupported condition type");			
		}
	}

	public void addCondition() {
		saveParameters();
		int index = filterGUI.getKeyIndex();
		String key = this.wrapper.getKey(index);
		String keyName = this.wrapper.getKeyName(index);
		byte type = this.wrapper.getType(index);
		switch (type) {
		case ConditionWrapper.INT:
			NumberCondition numberCondition = (NumberCondition) keyTemporalCondition.get(key);
			try {
				int equalsInt = Integer.parseInt(numberCondition.getEquals());
			} catch (NumberFormatException e) {
				Log.debugMessage("Flter.addCondition | Warning, wrong data format",Log.DEBUGLEVEL07);
				this.filterGUI.errorMessage(FilterTemp.WRONG_NUMBER_MESSAGE + FilterGUI.EQUALS_LABEL);
			}
			try {
				int fromInt = Integer.parseInt(numberCondition.getFrom());
			} catch (NumberFormatException e) {
				Log.debugMessage("Flter.addCondition | Warning, wrong data format",Log.DEBUGLEVEL07);
				this.filterGUI.errorMessage(FilterTemp.WRONG_NUMBER_MESSAGE + FilterGUI.FROM_LABEL);
			}
			try {
				int fromInt = Integer.parseInt(numberCondition.getTo());
			} catch (NumberFormatException e) {
				Log.debugMessage("Flter.addCondition | Warning, wrong data format",Log.DEBUGLEVEL07);
				this.filterGUI.errorMessage(FilterTemp.WRONG_NUMBER_MESSAGE + FilterGUI.TO_LABEL);
			}
		case ConditionWrapper.FLOAT:
			break;
		case ConditionWrapper.DOUBLE:
			break;
		case ConditionWrapper.STRING:
			StringCondition stringCondition = (StringCondition) this.keyTemporalCondition.get(key);
			String conditionString = stringCondition.getString();
			if(conditionString == null || conditionString.equals("")) {
				this.filterGUI.errorMessage(FilterTemp.WRONG_STRING_MESSAGE);
				return;
			}
			addTypicalCondition(conditionString, OperationSort.OPERATION_EQUALS, keyName);
			break;
		case ConditionWrapper.LIST:
			ListCondition listCondition = (ListCondition) this.keyTemporalCondition.get(key);
			LinkedList linkedObjects = new LinkedList(); 
			int[] linkedIndex = listCondition.getLinkedIndex();
			if(linkedIndex.length == 0) {
				this.filterGUI.errorMessage(FilterTemp.WRONG_LIST_MESSAGE);
				return;
			}
			for (int i = 0; i < linkedIndex.length; i++) {
				try {
					linkedObjects.add(this.wrapper.getLinkedObject(key, linkedIndex[i]));					
				} catch (IllegalDataException e) {
					Log.errorException(e);
				}
			}
			createLinkedIdsCondition(keyName, linkedObjects);
			break;
		default:
			Log.errorMessage("Filter.changeKey | Unsupported condition type");			
		}
		filterGUI.refreshCondition(keyNameCondition.keySet().toArray());
	}

	private String[] getConditionNames() {
		String[] conditionNames = new String[this.keyNameCondition.size()]; 
		int i = 0;
		for (Iterator iter = this.keyNameCondition.keySet().iterator(); iter.hasNext(); i++) {
			String key = (String) iter.next();
			conditionNames[i] = wrapper.getKeyName(key);			
		}
	
		return conditionNames;
	}


	public Object[] getKeys() {
		return this.wrapper.getKeyNames();		
	}

	public String[] getLinkedConditionList() {
		int keyIndex = this.filterGUI.getKeyIndex();
		String key = this.wrapper.getKey(keyIndex);
		try {
			return this.wrapper.getLinkedNames(key);
		} catch (IllegalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String[] getFilteredList() {
		return this.wrapper.getInitialNames();
	}

	public void saveParameters() {
		int keyIndex = this.filterGUI.getKeyIndex();
		String key = this.wrapper.getKey(keyIndex);
		byte type = this.wrapper.getType(keyIndex); 
		switch (type) {
		case ConditionWrapper.INT:
		case ConditionWrapper.FLOAT:
		case ConditionWrapper.DOUBLE:
			NumberCondition intCondition = (NumberCondition) this.keyTemporalCondition.get(key);
			this.filterGUI.setNumberCondition(intCondition);
			break;
		case ConditionWrapper.STRING:
			StringCondition stringCondition = (StringCondition) this.keyTemporalCondition.get(key);
			this.filterGUI.setStringCondition(stringCondition);
			break;
		case ConditionWrapper.LIST:
			ListCondition listCondition = (ListCondition) this.keyTemporalCondition.get(key);
			this.filterGUI.setListCondition(listCondition);
			break;
		default:
			Log.errorMessage("Filter.changeKey | Unsupported condition type");			
		}		
	}

	public void removeCondition(Object[] keyNames) {
		for (int i = 0; i < keyNames.length; i++) {
			String keyName =  (String) keyNames[i];
			this.keyNameCondition.remove(keyName);			
		}
		filterGUI.refreshCondition(keyNameCondition.keySet().toArray());
	}
	
	public void showLogicalScheme() {
		if(this.logicalConditionScheme == null) {
			this.logicalConditionScheme = new LogicalConditionScheme(keyNameCondition, filterGUI);
		} else {
			this.logicalConditionScheme.show();
		}
		
	}
	
	
}
