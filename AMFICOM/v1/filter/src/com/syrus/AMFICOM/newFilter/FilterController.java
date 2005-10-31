/*
 * $Id: FilterController.java,v 1.28 2005/10/31 12:30:03 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.newFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.TypicalSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.28 $, $Date: 2005/10/31 12:30:03 $
 * @author $Author: bass $
 * @module filter_v1
 */
public class FilterController implements ActionListener, PopupMenuListener, ListSelectionListener {
	
	public static final String	WRONG_NUMBER_MESSAGE			= "filter.error.message.wrongnumber";
	public static final String	WRONG_STRING_MESSAGE			= "filter.error.message.wrongstring";
	public static final String	WRONG_LIST_MESSAGE				= "filter.error.message.wronglist";
	public static final String	WRONG_DATE_MESSAGE				= "filter.error.message.wrongdate";
	public static final String	EQUALS_AND_FROM_SIMULTENIOUSLY	= "filter.error.message.equalsandfromsimultaneously";
	public static final String	EQUALS_AND_TO_SIMULTENIOUSLY	= "filter.error.message.equalsandtosimultaneously";
	public static final String	NO_CONDITIONS_CREATED			= "filter.error.message.noconditionscreated";
			
	private Filter model;
	private FilterView view;
		
	private Map<ConditionKey, TemporalCondition> keyTempCondition = new HashMap<ConditionKey, TemporalCondition>();
	
	public FilterController(final Filter filter, final FilterView filterView) {
		
		this.view = filterView;
		setFilter(filter);
	}
	
	public void setFilter(final Filter filter) {
		this.model = filter;
		this.model.addView(this.view);
		this.keyTempCondition.clear();
		Collection<ConditionKey> conditionKeys = this.model.getKeys();
		String[] keyNames = new String[conditionKeys.size()];
		int i = 0;
		for (ConditionKey conditionKey : conditionKeys) {
			keyNames[i++] = conditionKey.getName();
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
				Log.errorMessage("Unsupported condition type");
			}
		}
		this.view.setKeyNames(keyNames);
		refresh();
	}

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == this.view.addConditionRef()) {
			addCondition();
		} else if (source == this.view.changeConditionRef()) {
			addCondition();
		} else if (source == this.view.removeConditionRef()) {
			removeCondition();
		} else if (source == this.view.changeKeyRef()) {
			changeKey();
		} else if (source == this.view.createLogicalSchemeRef()) {
			createLogicalScheme();
		} else if (source == this.view.startDayButtonRef()) {
			this.view.createStartCalendar();
		} else if (source == this.view.endDayButtonRef()) {
			this.view.createEndCalendar();
		}
	}
	
	public void valueChanged(ListSelectionEvent e) {
		Object source = e.getSource();
		if (source == this.view.createdConditionListRef()) {
			int index = this.view.getSelectedConditionIndex();
			if(index == -1) {
				return;
			}
			ConditionKey key = this.model.getCreatedConditionKey(index);
			this.view.setSelectedKey(this.model.getKeyIndex(key));
			changeKey(this.model.getKeyIndex(key));
			setActiveButton();
		}
	}
	
	private void changeKey() {
		int keyIndex = this.view.getSelectedKeyIndex();
		changeKey(keyIndex);
	}
	
	private void changeKey(int keyIndex) {
		checkForExternalCondition();
		ConditionKey key = this.model.getKeys().get(keyIndex);
		int ceratedConditionIndex = this.model.getCreatedConditionKeyIndex(key);
		this.view.setSelectedCondition(ceratedConditionIndex);
		setActiveButton();
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
			List<StorableObject> linkedObjects = key.getLinkedObjects();
			if(linkedObjects == null) {
				//try {
					List<StorableObject> linkedStorableObjects = new ArrayList<StorableObject>();//(this.model.getLinkedConditionLoader().loadLinkedCondition(key.getLinkedEntityCode()));
					key.setLinkedObjects(linkedStorableObjects);
				//} catch (IllegalDataException e1) {
				//	e1.printStackTrace();
				//}
				linkedObjects = key.getLinkedObjects();
				String[] linkedNames = new String[linkedObjects.size()];
				for (int i = 0; i < linkedObjects.size(); i++) {
					StorableObject linkedObject = linkedObjects.get(i);
					StorableObjectWrapper<StorableObject> wrapper;
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
			Log.errorMessage("Unsupported condition type");			
		}
	}

	private void checkForExternalCondition() {
		List<ConditionKey> createdKeys = this.model.getCreatedConditionKeys();
		for (ConditionKey createdKey : createdKeys) {
			TemporalCondition temporalCondition = this.keyTempCondition.get(createdKey);
			//TODO:to think about it
			if(temporalCondition.isEmpty()) {
				TypicalCondition typicalCondition = (TypicalCondition)this.model.getCreatedCondition(createdKey);
				TypicalSort conditionType = typicalCondition.getType();
				OperationSort operationSort = typicalCondition.getOperation();
				switch (conditionType.value()) {
				case TypicalSort._TYPE_NUMBER_INT:
				case TypicalSort._TYPE_NUMBER_DOUBLE:
					NumberCondition numberCondition = (NumberCondition) temporalCondition;
					switch (operationSort.value()) {
					case OperationSort._OPERATION_EQUALS:
						if(conditionType.value() == TypicalSort._TYPE_NUMBER_INT) {
							numberCondition.setEquals(String.valueOf(typicalCondition.getFirstInt()));
						} else {
							numberCondition.setEquals(String.valueOf(typicalCondition.getFirstDouble()));
						}
						break;
					case OperationSort._OPERATION_GREAT_EQUALS:
						numberCondition.setIncludeBounds(true);
					case OperationSort._OPERATION_GREAT:
						if(conditionType.value() == TypicalSort._TYPE_NUMBER_INT) {
							numberCondition.setFrom(String.valueOf(typicalCondition.getFirstInt()));
						} else {
							numberCondition.setFrom(String.valueOf(typicalCondition.getFirstDouble()));
						}
						break;
					case OperationSort._OPERATION_LESS_EQUALS:
						numberCondition.setIncludeBounds(true);
					case OperationSort._OPERATION_LESS:
						if(conditionType.value() == TypicalSort._TYPE_NUMBER_INT) {
							numberCondition.setTo(String.valueOf(typicalCondition.getSecondInt()));
						} else {
							numberCondition.setTo(String.valueOf(typicalCondition.getSecondDouble()));
						}
						break;
					case OperationSort._OPERATION_IN_RANGE:
						if(conditionType.value() == TypicalSort._TYPE_NUMBER_INT) {
						numberCondition.setFrom(String.valueOf(typicalCondition.getFirstInt()));
						numberCondition.setTo(String.valueOf(typicalCondition.getSecondInt()));
						} else {
							numberCondition.setFrom(String.valueOf(typicalCondition.getFirstDouble()));
							numberCondition.setTo(String.valueOf(typicalCondition.getSecondDouble()));
						}
						break;
					default:
						throw new UnsupportedOperationException();
					}
					break;
				case TypicalSort._TYPE_DATE:
					DateCondition dateCondition = (DateCondition) temporalCondition;
					switch (operationSort.value()) {
					case OperationSort._OPERATION_GREAT_EQUALS:
					case OperationSort._OPERATION_GREAT:
						dateCondition.setStartDate(new Date(typicalCondition.getFirstLong()));
						break;
					case OperationSort._OPERATION_LESS_EQUALS:
					case OperationSort._OPERATION_LESS:
						dateCondition.setEndDate(new Date(typicalCondition.getSecondLong()));
						break;
					case OperationSort._OPERATION_IN_RANGE:
						dateCondition.setStartDate(new Date(typicalCondition.getFirstLong()));
						dateCondition.setEndDate(new Date(typicalCondition.getSecondLong()));
						break;
					default:
						throw new UnsupportedOperationException();
					}
					break;
				case TypicalSort._TYPE_STRING:
					StringCondition stringCondition = (StringCondition) temporalCondition;
					stringCondition.setString((String)typicalCondition.getValue());
					switch (operationSort.value()) {
					case OperationSort._OPERATION_EQUALS:
						stringCondition.setSubstring(false);
						break;
					case OperationSort._OPERATION_SUBSTRING:
						stringCondition.setSubstring(true);
						break;
					default:
						throw new UnsupportedOperationException();
					}
					break;
				default:
					throw new UnsupportedOperationException();
				}
			}
		}
	}

	private void setActiveButton() {
		if(this.view.getSelectedConditionIndex() == -1) {
			this.view.enableAdd(false);
			this.view.enableRemoveButton(false);
		} else {
			this.view.enableAdd(true);
			this.view.enableRemoveButton(true);
		}
	}

	private void addCondition() {
		saveTempConditions();
		int index = this.view.getSelectedKeyIndex();
		ConditionKey conditionKey = this.model.getKeys().get(index);
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
					Log.debugMessage("Warning, wrong data format",Log.DEBUGLEVEL07);
					this.view.showErrorMessage(LangModelFilter.getString(WRONG_NUMBER_MESSAGE));
					return;
				}
				this.model.addCondition0(new TypicalCondition(equalsInt, equalsInt, OperationSort.OPERATION_EQUALS, entityCode, key), conditionKey);
				return;
			}
			if (equals.equals("") && !from.equals("") && !to.equals("")) {
				int fromInt;
				int toInt;
				try {
					fromInt = Integer.parseInt(numberCondition.getFrom());
				} catch (NumberFormatException e) {
					Log.debugMessage("Warning, wrong data format",Log.DEBUGLEVEL07);
					this.view.showErrorMessage(LangModelFilter.getString(WRONG_NUMBER_MESSAGE));
					return;
				}
				try {
					toInt = Integer.parseInt(numberCondition.getTo());
				} catch (NumberFormatException e) {
					Log.debugMessage("Warning, wrong data format",Log.DEBUGLEVEL07);
					this.view.showErrorMessage(LangModelFilter.getString(WRONG_NUMBER_MESSAGE));
					return;
				}
				this.model.addCondition0(new TypicalCondition(fromInt, toInt, OperationSort.OPERATION_IN_RANGE, entityCode, key), conditionKey);
				return;
			}
			if (equals.equals("") && !from.equals("") && to.equals("")) {
				int fromInt;
				try {
					fromInt = Integer.parseInt(numberCondition.getFrom());
				} catch (NumberFormatException e) {
					Log.debugMessage("Warning, wrong data format",Log.DEBUGLEVEL07);
					this.view.showErrorMessage(LangModelFilter.getString(WRONG_NUMBER_MESSAGE));
					return;
				}
				if (includeBounds)
					sort = OperationSort.OPERATION_GREAT_EQUALS;
				else
					sort = OperationSort.OPERATION_GREAT;
				this.model.addCondition0(new TypicalCondition(fromInt, fromInt, sort, entityCode, key), conditionKey);
				return;
			}
			if (equals.equals("") && from.equals("") && !to.equals("")) {
				int toInt;
				try {
					toInt = Integer.parseInt(numberCondition.getTo());
				} catch (NumberFormatException e) {
					Log.debugMessage("Warning, wrong data format",Log.DEBUGLEVEL07);
					this.view.showErrorMessage(LangModelFilter.getString(WRONG_NUMBER_MESSAGE));
					return;
				}
				if (includeBounds)
					sort = OperationSort.OPERATION_LESS_EQUALS;
				else
					sort = OperationSort.OPERATION_LESS;
				this.model.addCondition0(new TypicalCondition(toInt, toInt, sort, entityCode, key), conditionKey);
				return;
			}			
			if (!equals.equals("") && !to.equals("")) {
				this.view.showErrorMessage(LangModelFilter.getString(EQUALS_AND_TO_SIMULTENIOUSLY));
				return;
			}
			if (!equals.equals("") && !from.equals("")) {
				this.view.showErrorMessage(LangModelFilter.getString(EQUALS_AND_FROM_SIMULTENIOUSLY));
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
				this.view.showErrorMessage(LangModelFilter.getString(WRONG_STRING_MESSAGE));
				return;
			}
			if (stringCondition.isSubstring()) {
				this.model.addCondition0(new TypicalCondition(conditionString, OperationSort.OPERATION_SUBSTRING, entityCode, key), conditionKey);
			} else {
				this.model.addCondition0(new TypicalCondition(conditionString, OperationSort.OPERATION_EQUALS, entityCode, key), conditionKey);
			}
			break;
		case ConditionWrapper.CONSTRAINT:
			ListCondition listCondition2 = (ListCondition) tempCondition;
			Set<StorableObjectCondition> conditions = new HashSet<StorableObjectCondition>();
			int[] selectedIndices = listCondition2.getSelectedIndices();
			if(selectedIndices.length == 0) {
				this.view.showErrorMessage(LangModelFilter.getString(WRONG_LIST_MESSAGE));
				return;
			}
			for (int i = 0; i < selectedIndices.length; i++) {
				TypicalCondition condition = new TypicalCondition(selectedIndices[i], selectedIndices[i], OperationSort.OPERATION_EQUALS, entityCode, key);
				conditions.add(condition);					
			}
			if (conditions.size() == 1)
				this.model.addCondition0(conditions.iterator().next(), conditionKey);
			else {
				this.model.addCondition0(new CompoundCondition(conditions, CompoundConditionSort.OR), conditionKey);
			}
			break;
		case ConditionWrapper.LIST:
			ListCondition listCondition = (ListCondition) tempCondition;
			List<StorableObject> linkedObjects = conditionKey.getLinkedObjects();
			int[] linkedIndex = listCondition.getSelectedIndices();
			if(linkedIndex.length == 0) {
				this.view.showErrorMessage(LangModelFilter.getString(WRONG_LIST_MESSAGE));
				return;
			}
			ArrayList<Identifier> selectedObjectIds = new ArrayList<Identifier>(linkedIndex.length);
			for (int i = 0; i < linkedIndex.length; i++) {
				selectedObjectIds.add((linkedObjects.get(linkedIndex[i])).getId());									
			}
			this.model.addCondition0(new LinkedIdsCondition(new HashSet<Identifier>(selectedObjectIds), entityCode), conditionKey);
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
			this.model.addCondition0(new TypicalCondition(start, end, sort2, entityCode, key), conditionKey);
			break;
		default:
			Log.errorMessage("Unsupported condition type");
		}
		setActiveButton();
		this.view.setSelectedCondition(this.model.getCreatedConditionKeyIndex(conditionKey));
	}
	
	private void removeCondition() {
		int[] selectedConditionIndecies = this.view.getSelectedConditionIndecies();
		for (int i = 0; i < selectedConditionIndecies.length; i++) {
			ConditionKey key = this.model.getCreatedConditionKey(selectedConditionIndecies[i]);
			this.model.removeCondition(key);			
		}
		setActiveButton();
	}
	
	public void createLogicalScheme() {
		if (!this.model.hasCondition()) {
			this.view.showErrorMessage(LangModelFilter.getString(NO_CONDITIONS_CREATED));
			return;
		}
		this.view.createLogicalSchemeView(this.model.getLogicalScheme());	
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
	
	private Object getTempCondition(ConditionKey key) {
		return this.keyTempCondition.get(key);
	}
	
	private void saveTempConditions() {
		int index = this.view.getSelectedKeyIndex();
		ConditionKey conditionKey = this.model.getKeys().get(index);
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
			Log.errorMessage("Unsupported condition type");			
		}		
	}

	public void refresh() {
		List<String> conditionNames = this.model.getConditionNames();
		String[] keyNames = conditionNames.toArray(new String[conditionNames.size()]);
		this.view.refreshCreatedConditions(keyNames);
		this.view.refreshResultConditionString(this.model.getLogicalScheme().getStringCondition());
	}
}
