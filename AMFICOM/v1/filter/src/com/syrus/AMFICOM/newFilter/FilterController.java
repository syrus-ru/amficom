/*
 * $Id: FilterController.java,v 1.12 2005/06/04 16:56:22 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.newFilter;

import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;
import com.syrus.util.Log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;





/**
 * @version $Revision: 1.12 $, $Date: 2005/06/04 16:56:22 $
 * @author $Author: bass $
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
		for (Iterator it = this.model.getKeys().iterator(); it.hasNext();) {
			ConditionKey conditionKey = (ConditionKey) it.next();
			byte type = conditionKey.getType();
			switch (type) {
			case ConditionWrapper.INT:
			case ConditionWrapper.FLOAT:
			case ConditionWrapper.DOUBLE:
				this.keyTempCondition.put(conditionKey, new NumberCondition());
				break;
			case ConditionWrapper.STRING:
				this.keyTempCondition.put(conditionKey, new StringCondition());
				break;
			case ConditionWrapper.CONSTRAINT:
				this.keyTempCondition.put(conditionKey, new ListCondition(conditionKey.getConstraintNames()));
				break;
			case ConditionWrapper.LIST:
				this.keyTempCondition.put(conditionKey, new ListCondition());
				break;
			case ConditionWrapper.DATE:
				this.keyTempCondition.put(conditionKey, new DateCondition());
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
		ConditionKey key = (ConditionKey) this.model.getKeys().get(keyIndex);
		setActiveButton(key.getName());
		byte type = key.getType();
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
		case ConditionWrapper.CONSTRAINT: {
			ListCondition listCondition = (ListCondition) tempCondition;
			this.view.drawLinkedCondition(listCondition);
			break;
		}
		case ConditionWrapper.LIST:
			ListCondition listCondition = (ListCondition) tempCondition;
			List linkedObjects = key.getLinkedObjects();
			if(linkedObjects == null) {
				try {
					List linkedStorableObjects = new ArrayList(this.model.linkedConditionLoader.loadLinkedCondition(key.getLinkedEntityCode()));
					key.setLinkedObjects(linkedStorableObjects);
				} catch (IllegalDataException e1) {
					e1.printStackTrace();
				}
				linkedObjects = key.getLinkedObjects();
				String[] linkedNames = new String[linkedObjects.size()];
				for (int i = 0; i < linkedObjects.size(); i++) {
					StorableObject linkedObject = (StorableObject) linkedObjects.get(i);
					StorableObjectWrapper wrapper;
					try {
						wrapper = StorableObjectWrapper.getWrapper(key.getLinkedEntityCode());
					} catch (IllegalDataException e) {
						Log.errorMessage(e.getMessage());
						return;
					}
					linkedNames[i] = (String) wrapper.getValue(linkedObject, StorableObjectWrapper.VIEW_NAME);
				}
				listCondition.setLinkedNames(linkedNames);
			}
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

	private void setActiveButton(String name) {
		if(this.model.getConditionNames().contains(name))
			this.view.enableChangeDisableAdd(true);
		else
			this.view.enableChangeDisableAdd(false);
	}

	private void addConditionToModel() {
		saveTempConditions();
		int index = this.view.getSelectedKeyIndex();
		ConditionKey conditionKey = (ConditionKey) this.model.getKeys().get(index);
		String key = conditionKey.getKey();
		byte type = conditionKey.getType();
		Object tempCondition = getTempCondition(conditionKey);
		short entityCode = this.model.getEntityCode();
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
				this.model.addCondition(new TypicalCondition(equalsInt, equalsInt, OperationSort.OPERATION_EQUALS, entityCode, key), conditionKey);
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
				this.model.addCondition(new TypicalCondition(fromInt, toInt, OperationSort.OPERATION_IN_RANGE, entityCode, key), conditionKey);
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
				this.model.addCondition(new TypicalCondition(fromInt, fromInt, sort, entityCode, key), conditionKey);
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
				this.model.addCondition(new TypicalCondition(toInt, toInt, sort, entityCode, key), conditionKey);
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
			break;
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
			this.model.addCondition(new TypicalCondition(conditionString, OperationSort.OPERATION_EQUALS, entityCode, key), conditionKey);
			break;
		case ConditionWrapper.CONSTRAINT:
			ListCondition listCondition2 = (ListCondition) tempCondition;
			Set conditions = new HashSet();
			int[] selectedIndices = listCondition2.getSelectedIndices();
			if(selectedIndices.length == 0) {
				this.view.showErrorMessage(Filter.WRONG_LIST_MESSAGE);
				return;
			}
			for (int i = 0; i < selectedIndices.length; i++) {
				TypicalCondition condition = new TypicalCondition(selectedIndices[i], selectedIndices[i], OperationSort.OPERATION_EQUALS, entityCode, key);
				conditions.add(condition);					
			}
			if (conditions.size() == 1)
				this.model.addCondition((StorableObjectCondition)conditions.iterator().next(), conditionKey);
			else {
				try {
					this.model.addCondition(new CompoundCondition(conditions, CompoundConditionSort.OR), conditionKey);
				} catch (CreateObjectException e) {
					//never will happen;
					Log.errorMessage(e.getMessage());
				}
			}
			break;
		case ConditionWrapper.LIST:
			ListCondition listCondition = (ListCondition) tempCondition;
			List linkedObjects = conditionKey.getLinkedObjects();
			int[] linkedIndex = listCondition.getSelectedIndices();
			if(linkedIndex.length == 0) {
				this.view.showErrorMessage(Filter.WRONG_LIST_MESSAGE);
				return;
			}
			ArrayList selectedObjectIds = new ArrayList(linkedIndex.length);
			for (int i = 0; i < linkedIndex.length; i++) {
				selectedObjectIds.add(((StorableObject)linkedObjects.get(linkedIndex[i])).getId());									
			}
			this.model.addCondition(new LinkedIdsCondition(new HashSet(selectedObjectIds), entityCode), conditionKey);
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
			this.model.addCondition(new TypicalCondition(start, end, sort2, entityCode, key), conditionKey);
			break;
		default:
			Log.errorMessage("FilterController.addConditionToModel | Unsupported condition type");
		}
		setActiveButton(conditionKey.getName());
	}
	
	private void removeConditionInModelAndInScheme() {
		String[] selectedConditionNames = this.view.getSelectedConditionNames();
		for (int i = 0; i < selectedConditionNames.length; i++) {
			this.model.removeCondition(selectedConditionNames[i]);			
		}
		int index = this.view.getSelectedKeyIndex();
		ConditionKey conditionKey = (ConditionKey) this.model.getKeys().get(index);
		setActiveButton(conditionKey.getName());
	}
	
	public void createLogicalScheme() {
		if (!this.model.hasCondition()) {
			this.view.showErrorMessage(Filter.NO_CONDITIONS_CREATED);
			return;
		}
		this.view.createLogicalSchemeView(this.model.logicalScheme);	
	}
	
//	private boolean noConditionsCreated() {
//		if (this.model.getKeyNameCondition().keySet() == null
//				|| this.model.getKeyNameCondition().keySet().size() == 0) {
//			return true;
//		}
//		return false;
//	}
		
	public void popupMenuCanceled(PopupMenuEvent e) {
		// Nothing to do		
	}

	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		// Nothing to do
		
	}

	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		saveTempConditions();
	}
	
	private Object getTempCondition(ConditionKey key) {
		return this.keyTempCondition.get(key);
	}
	
	private void saveTempConditions() {
		int index = this.view.getSelectedKeyIndex();
		ConditionKey conditionKey = (ConditionKey) this.model.getKeys().get(index);
		byte type = conditionKey.getType();
		Object tempCondition = getTempCondition(conditionKey);
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
