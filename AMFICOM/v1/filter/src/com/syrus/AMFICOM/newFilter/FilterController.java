/*
 * $Id: FilterController.java,v 1.3 2005/04/01 08:37:09 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.syrus.AMFICOM.filterclient.ConditionWrapper;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/04/01 08:37:09 $
 * @author $Author: max $
 * @module filter_v1
 */
public class FilterController implements ActionListener, PopupMenuListener {
	
	private Filter model;
	private FilterView view;
		
	private Map	keyTempCondition = new HashMap();
	
	public FilterController(Filter filter, FilterView filterView) {
		
		this.model = filter;
		this.view = filterView;
		
		this.model.addView(this.view);
		
		for (int i = 0; i < this.model.getKeys().length; i++) {
			String key = this.model.getKey(i);
			byte type = this.model.getKeyType(i);
			switch (type) {
			case ConditionWrapper.INT:
			case ConditionWrapper.FLOAT:
			case ConditionWrapper.DOUBLE:
				this.keyTempCondition.put(key, new NumberCondition());
				break;
			case ConditionWrapper.STRING:
				this.keyTempCondition.put(key, new StringCondition());
				break;
			case ConditionWrapper.CONSTRAINT:
			case ConditionWrapper.LIST:
				this.keyTempCondition.put(key, new ListCondition(this.model.getLinkedNames(key)));
				break;
			case ConditionWrapper.DATE:
				this.keyTempCondition.put(key, new DateCondition());
				break;
			default:
				Log.errorMessage("FilterCondition.<init> | Unsupported condition type");
			}
		}		
	}

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == this.view.addConditionRef())
			addConditionToModel();		
		else if (source == this.view.changeKeyRef())
			changeKey();
		else if (source == this.view.removeConditionRef()) {
			removeConditionInModelAndInScheme();			
		}
		else if (source == this.view.createLogicalSchemeRef())
			createLogicalScheme();
	}
	
	private void changeKey() {
		int keyIndex = this.view.getSelectedKeyIndex();
		String key = this.model.getKey(keyIndex);
		byte type = this.model.getKeyType(keyIndex);
		Object tempCondition = getTempCondition(key);
		switch (type) {
		case ConditionWrapper.INT:
		case ConditionWrapper.FLOAT:
		case ConditionWrapper.DOUBLE:
			NumberCondition numberCondition = (NumberCondition) tempCondition;
			this.view.drawNumberCondition(numberCondition);
			break;
		case ConditionWrapper.STRING:
			StringCondition stringCondition = (StringCondition) tempCondition;
			this.view.drawStringCondition(stringCondition);
			break;
		case ConditionWrapper.CONSTRAINT:
		case ConditionWrapper.LIST:
			ListCondition listCondition = (ListCondition) tempCondition;
			this.view.drawLinkedCondition(listCondition);
			break;
		case ConditionWrapper.DATE:
			DateCondition dateCondition = (DateCondition) tempCondition;
			this.view.drawDateCondition(dateCondition);
			break;
		default:
			Log.errorMessage("FilterCondition.changeKey | Unsupported condition type");			
		}
	}

	private void addConditionToModel() {
		saveTempConditions();
		int index = this.view.getSelectedKeyIndex();
		String key = this.model.getKey(index);
		String keyName = this.model.getKeyName(index);
		byte type = this.model.getKeyType(index);
		Object tempCondition = getTempCondition(key);
		switch (type) {
		case ConditionWrapper.INT:
			NumberCondition numberCondition = (NumberCondition) tempCondition;
			String equals = numberCondition.getEquals();
			String from = numberCondition.getFrom();
			String to = numberCondition.getTo();
			boolean includeBounds = numberCondition.isIncludeBounds();
			OperationSort sort = null;
			if (!equals.equals("") && from.equals("") && to.equals("")) {
				int equalsInt;
				try {
					equalsInt = Integer.parseInt(equals);
				} catch (NumberFormatException e) {
					Log.debugMessage("Flter.addCondition | Warning, wrong data format",Log.DEBUGLEVEL07);
					this.view.showErrorMessage(Filter.WRONG_NUMBER_MESSAGE);
					return;
				}
				this.model.addCondition(keyName, new TypicalCondition(equalsInt, equalsInt, OperationSort.OPERATION_EQUALS, this.model.getEntityCode(), key));
				return;
			}
			if (equals.equals("") && !from.equals("") && !to.equals("")) {
				int fromInt;
				int toInt;
				try {
					fromInt = Integer.parseInt(numberCondition.getFrom());
				} catch (NumberFormatException e) {
					Log.debugMessage("Flter.addCondition | Warning, wrong data format",Log.DEBUGLEVEL07);
					this.view.showErrorMessage(Filter.WRONG_NUMBER_MESSAGE);
					return;
				}
				try {
					toInt = Integer.parseInt(numberCondition.getTo());
				} catch (NumberFormatException e) {
					Log.debugMessage("Flter.addCondition | Warning, wrong data format",Log.DEBUGLEVEL07);
					this.view.showErrorMessage(Filter.WRONG_NUMBER_MESSAGE);
					return;
				}
				this.model.addCondition(keyName, new TypicalCondition(fromInt, toInt, OperationSort.OPERATION_IN_RANGE, this.model.getEntityCode(), key));
				return;
			}
			if (equals.equals("") && !from.equals("") && to.equals("")) {
				int fromInt;
				try {
					fromInt = Integer.parseInt(numberCondition.getFrom());
				} catch (NumberFormatException e) {
					Log.debugMessage("Flter.addCondition | Warning, wrong data format",Log.DEBUGLEVEL07);
					this.view.showErrorMessage(Filter.WRONG_NUMBER_MESSAGE);
					return;
				}
				if (includeBounds)
					sort = OperationSort.OPERATION_GREAT_EQUALS;
				else
					sort = OperationSort.OPERATION_GREAT;
				this.model.addCondition(keyName, new TypicalCondition(fromInt, fromInt, sort, this.model.getEntityCode(), key));
				return;
			}
			if (equals.equals("") && from.equals("") && !to.equals("")) {
				int toInt;
				try {
					toInt = Integer.parseInt(numberCondition.getTo());
				} catch (NumberFormatException e) {
					Log.debugMessage("Flter.addCondition | Warning, wrong data format",Log.DEBUGLEVEL07);
					this.view.showErrorMessage(Filter.WRONG_NUMBER_MESSAGE);
					return;
				}
				if (includeBounds)
					sort = OperationSort.OPERATION_LESS_EQUALS;
				else
					sort = OperationSort.OPERATION_LESS;
				this.model.addCondition(keyName, new TypicalCondition(toInt, toInt, sort, this.model.getEntityCode(), key));
				return;
			}			
			if (!equals.equals("") && !to.equals("")) {
				this.view.showErrorMessage(Filter.EQUALS_AND_TO_SIMULTENIOUSLY);
				return;
			}
			if (!equals.equals("") && !from.equals("")) {
				this.view.showErrorMessage(Filter.EQUALS_AND_FROM_SIMULTENIOUSLY);
				return;
			}			
		case ConditionWrapper.FLOAT:
			break;
		case ConditionWrapper.DOUBLE:
			break;
		case ConditionWrapper.STRING:
			StringCondition stringCondition = (StringCondition) tempCondition;
			String conditionString = stringCondition.getString();
			if(conditionString == null || conditionString.equals("")) {
				this.view.showErrorMessage(Filter.WRONG_STRING_MESSAGE);
				return;
			}
			this.model.addCondition(keyName, new TypicalCondition(conditionString, OperationSort.OPERATION_EQUALS, this.model.getEntityCode(), key));
			break;
		case ConditionWrapper.CONSTRAINT:
			ListCondition listCondition2 = (ListCondition) tempCondition;
			Collection conditions = new LinkedList();
			int[] linkedIndex2 = listCondition2.getLinkedIndex();
			if(linkedIndex2.length == 0) {
				this.view.showErrorMessage(Filter.WRONG_LIST_MESSAGE);
				return;
			}
			for (int i = 0; i < linkedIndex2.length; i++) {
				TypicalCondition condition = new TypicalCondition(linkedIndex2[i], linkedIndex2[i], OperationSort.OPERATION_EQUALS, this.model.getEntityCode(), key);
				conditions.add(condition);					
			}
			if (conditions.size() == 1) 
				this.model.addCondition(keyName, (StorableObjectCondition)conditions.iterator().next());
			else {
				try {
					this.model.addCondition(keyName, new CompoundCondition(conditions, CompoundConditionSort.OR));
				} catch (CreateObjectException e) {
					//never will happen;
					Log.errorMessage(e.getMessage());
				}
			}
			break;
		case ConditionWrapper.LIST:
			ListCondition listCondition = (ListCondition) tempCondition;
			Collection linkedObjects = new LinkedList(); 
			int[] linkedIndex = listCondition.getLinkedIndex();
			if(linkedIndex.length == 0) {
				this.view.showErrorMessage(Filter.WRONG_LIST_MESSAGE);
				return;
			}
			for (int i = 0; i < linkedIndex.length; i++) {
				linkedObjects.add(this.model.getLinkedObject(key, linkedIndex[i]));					
			}
			this.model.addCondition(keyName, new LinkedIdsCondition(linkedObjects, this.model.getEntityCode()));
			break;
		case ConditionWrapper.DATE:
			DateCondition dateCondition = (DateCondition) tempCondition;
			Date start = dateCondition.getStartDate();
			Date end = dateCondition.getEndDate();
			OperationSort sort2 = null;
			if (start != null && end != null)
				sort2 = OperationSort.OPERATION_IN_RANGE;				
			else if (start != null && end == null)
				sort2 = OperationSort.OPERATION_GREAT_EQUALS;				
			else if (start == null && end != null)
				sort2 = OperationSort.OPERATION_LESS_EQUALS;
			if (sort2 == null)
				return;
			this.model.addCondition(keyName, new TypicalCondition(start, end, sort2, this.model.getEntityCode(), key));
		default:
			Log.errorMessage("FilterController.addConditionToModel | Unsupported condition type");
		}		
	}
	
	private void removeConditionInModelAndInScheme() {
		Object[] keyNames = this.view.getSelectedConditionNames();
		for (int i = 0; i < keyNames.length; i++) {
			String keyName = (String) keyNames[i];
			this.model.removeCondition(keyName);			
		}
	}
	
	public void createLogicalScheme() {
		if (noConditionsCreated()) {
			this.view.showErrorMessage(Filter.NO_CONDITIONS_CREATED);
			return;
		}
		this.view.createLogicalSchemeView(this.model.getLogicalScheme(), this.model);	
	}
	
	private boolean noConditionsCreated() {
		if (this.model.getKeyNameCondition().keySet() == null
				|| this.model.getKeyNameCondition().keySet().size() == 0) {
			return true;
		}
		return false;
	}
		
	public void popupMenuCanceled(PopupMenuEvent e) {
		// Nothing to do		
	}

	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		// Nothing to do
		
	}

	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		saveTempConditions();
	}
	
	private Object getTempCondition(String key) {
		return this.keyTempCondition.get(key);
	}
	
	private void saveTempConditions() {
		int index = this.view.getSelectedKeyIndex();
		String key = this.model.getKey(index);
		byte type = this.model.getKeyType(index);
		Object tempCondition = getTempCondition(key);
		switch (type) {
		case ConditionWrapper.INT:
		case ConditionWrapper.FLOAT:
		case ConditionWrapper.DOUBLE:
			NumberCondition intCondition = (NumberCondition) tempCondition;
			this.view.setNumberCondition(intCondition);
			break;
		case ConditionWrapper.STRING:
			StringCondition stringCondition = (StringCondition) tempCondition;
			this.view.setStringCondition(stringCondition);
			break;
		case ConditionWrapper.CONSTRAINT:
		case ConditionWrapper.LIST:
			ListCondition listCondition = (ListCondition) tempCondition;
			this.view.setListCondition(listCondition);
			break;
		case ConditionWrapper.DATE:
			DateCondition dateCondition = (DateCondition) tempCondition;
			this.view.setDateCondition(dateCondition);
			break;
		default:
			Log.errorMessage("FilterController.saveTempConditions | Unsupported condition type");			
		}		
	}
	
}
