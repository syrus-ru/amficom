/*
 * $Id: Filter.java,v 1.1 2005/03/15 16:11:44 max Exp $
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
public class Filter {
	
	private FilterGUI filterGUI;
	private ConditionWrapper wrapper;
	private short entityCode;
	private Map keyCondition = new HashMap(); 
	private Map	keyTemporalCondition = new HashMap();
	private static final String	WRONG_NUMBER_MESSAGE	= "you have intered wrong number type in field ";
	private static final String	WRONG_STRING_MESSAGE	= "please, fill the field ";
	private static final String	WRONG_LIST_MESSAGE		= "Select, from list ";
		
	public Filter(ConditionWrapper wrapper, FilterGUI filterGUI) {
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
		this.keyCondition.put(key, new TypicalCondition(firstLong, secondLong, operation , this.entityCode, key));		
	}
	
	public void createTypicalCondition(final Date firstDate,
			final Date secondDate,
			final OperationSort operation,
			final String key) {
		this.keyCondition.put(key, new TypicalCondition(firstDate, secondDate, operation , this.entityCode, key));
	}
	
	public void createTypicalCondition(final double firstDouble,
			final double secondDouble,
			final OperationSort operation,
			final String key) {
		this.keyCondition.put(key, new TypicalCondition(firstDouble, secondDouble, operation , this.entityCode, key));
	}
	
	public void createTypicalCondition(final int firstInt,
			final int secondInt,
			final OperationSort operation,
			final String key) {
		this.keyCondition.put(key, new TypicalCondition(firstInt, secondInt, operation , this.entityCode, key));
	}
	
	public void addTypicalCondition(final String value, 
			final OperationSort operation, 
			final String key) {
		this.keyCondition.put(key, new TypicalCondition(value, operation , this.entityCode, key));
	}
	
	public void createLinkedIdsCondition(String key, final Identifier identifier) {
		this.keyCondition.put(key, new LinkedIdsCondition(identifier, this.entityCode));
	}

	public void createLinkedIdsCondition(String key, final List linkedIds) {
		this.keyCondition.put(key, new LinkedIdsCondition(linkedIds, this.entityCode));
	}
	
	public Collection getCreatedConditions() {
		return this.keyCondition.values();
	}

	public StorableObjectCondition getResultCondition() throws CreateObjectException, IllegalDataException {
		LogicalConditionScheme logicalConditionScheme = new LogicalConditionScheme(this.getCreatedConditions());
		return logicalConditionScheme.getResultCondition();				
	}

	public void changeKey(String keyName) {
		String key = this.wrapper.getKey(keyName);
		byte type = this.wrapper.getType(key);
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
			filterGUI.linkedConditionList.setListData(linkedNames);
			ListCondition listCondition = (ListCondition) this.keyTemporalCondition.get(key);
			if(listCondition == null) {
				listCondition = new ListCondition(new int[0]);
				this.keyTemporalCondition.put(key, listCondition);
			}
			filterGUI.linkedConditionList.setListData(linkedNames);
			if (listCondition.getLinkedIndex().length > 0)
				filterGUI.linkedConditionList.setSelectedIndices(listCondition.getLinkedIndex());
			this.filterGUI.drawListCondition();
			break;
		default:
			Log.errorMessage("Filter.changeKey | Unsupported condition type");			
		}
	}

	public void addCondition() {
		String keyName = this.filterGUI.getKey();
		String key = this.wrapper.getKey(keyName);
		byte type = this.wrapper.getType(key);
		switch (type) {
		case ConditionWrapper.INT:
			try {
				int equalsInt = Integer.parseInt(filterGUI.equalsField.getText());
			} catch (NumberFormatException e) {
				Log.debugMessage("Flter.addCondition | Warning, wrong data format",Log.DEBUGLEVEL07);
				filterGUI.errorMessage(this.WRONG_NUMBER_MESSAGE + FilterGUI.EQUALS_LABEL);
			}
			try {
				int fromInt = Integer.parseInt(filterGUI.fromField.getText());
			} catch (NumberFormatException e) {
				Log.debugMessage("Flter.addCondition | Warning, wrong data format",Log.DEBUGLEVEL07);
				filterGUI.errorMessage(this.WRONG_NUMBER_MESSAGE + FilterGUI.FROM_LABEL);
			}
			try {
				int fromInt = Integer.parseInt(filterGUI.toField.getText());
			} catch (NumberFormatException e) {
				Log.debugMessage("Flter.addCondition | Warning, wrong data format",Log.DEBUGLEVEL07);
				filterGUI.errorMessage(this.WRONG_NUMBER_MESSAGE + FilterGUI.TO_LABEL);
			}
		case ConditionWrapper.FLOAT:
			break;
		case ConditionWrapper.DOUBLE:
			break;
		case ConditionWrapper.STRING:
			String conditionString = filterGUI.conditionTextField.getText();
			if(conditionString == null || conditionString.equals("")) {
				filterGUI.errorMessage(Filter.WRONG_STRING_MESSAGE);
				return;
			}
				
			addTypicalCondition(conditionString, OperationSort.OPERATION_EQUALS, key);
			break;
		case ConditionWrapper.LIST:
			LinkedList linkedObjects = new LinkedList(); 
			int[] linkedIdsIndex = filterGUI.linkedConditionList.getSelectedIndices();
			if(linkedIdsIndex.length == 0) {
				filterGUI.errorMessage(Filter.WRONG_LIST_MESSAGE);
				return;
			}
			for (int i = 0; i < linkedIdsIndex.length; i++) {
				try {
					linkedObjects.add(wrapper.getLinkedObject(key, linkedIdsIndex[i]));					
				} catch (IllegalDataException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			createLinkedIdsCondition(key, linkedObjects);
			break;
		default:
			Log.errorMessage("Filter.changeKey | Unsupported condition type");			
		}
		
		filterGUI.conditionList.setListData(getConditionNames());
		
	}

	private String[] getConditionNames() {
		String[] conditionNames = new String[keyCondition.keySet().size()];
		int i = 0;
		for (Iterator it = wrapper.getKeyNames().iterator(); it.hasNext();) {
			String keyName = (String) it.next();
			String key = wrapper.getKey(keyName);
			if (keyCondition.keySet().contains(key)) {
				conditionNames[i] = keyName;
				i++;
			}
		}
		return conditionNames;
	}


	public Object[] getKeys() {
		return wrapper.getKeyNames().toArray();		
	}

	public String[] getLinkedConditionList() {
		String keyName = this.filterGUI.getKey();
		String key = this.wrapper.getKey(keyName);
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
		String keyName = (String) this.filterGUI.keysCombo.getSelectedItem();
		String key = this.wrapper.getKey(keyName);
		byte type = this.wrapper.getType(key); 
		switch (type) {
		case ConditionWrapper.INT:
		case ConditionWrapper.FLOAT:
		case ConditionWrapper.DOUBLE:
			NumberCondition intCondition = (NumberCondition) this.keyTemporalCondition.get(key);
			intCondition.setEquals(this.filterGUI.equalsField.getText());
			intCondition.setFrom(this.filterGUI.fromField.getText());
			intCondition.setTo(this.filterGUI.toField.getText());
			intCondition.setIncludeBounds(this.filterGUI.boundaryCheckBox.isSelected());
			break;
		case ConditionWrapper.STRING:
			StringCondition stringCondition = (StringCondition) this.keyTemporalCondition.get(key);
			stringCondition.setString(this.filterGUI.conditionTextField.getText());
			break;
		case ConditionWrapper.LIST:
			ListCondition listCondition = (ListCondition) this.keyTemporalCondition.get(key);
			listCondition.setLinkedIndex(this.filterGUI.linkedConditionList.getSelectedIndices());
			break;
		default:
			Log.errorMessage("Filter.changeKey | Unsupported condition type");			
		}
			
		
	}

	public void removeCondition() {
		int[] conditionIndex = filterGUI.conditionList.getSelectedIndices();
		for (int i = 0; i < conditionIndex.length; i++) {
			this.keyCondition.remove()
			
			filterGUI.conditionList.remove(conditionIndex[i]);
		}
	}
}
