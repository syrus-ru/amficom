/*
 * $Id: FilterController.java,v 1.19 2005/08/09 21:10:10 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.newFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.util.Log;





/**
 * @version $Revision: 1.19 $, $Date: 2005/08/09 21:10:10 $
 * @author $Author: arseniy $
 * @module filter
 */
public class FilterController implements ActionListener, PopupMenuListener, ListSelectionListener {
	
	private Filter model;
	private FilterView view;
		
	private Map<ConditionKey, Object>	keyTempCondition = new HashMap<ConditionKey, Object>();
	
	public FilterController(final Filter filter, final FilterView filterView) {
		this.view = filterView;
		setFilter(filter);
	}
	
	public void setFilter(final Filter filter) {
		this.model = filter;
		this.model.addView(this.view);
		this.keyTempCondition.clear();
		for (final ConditionKey conditionKey : this.model.getKeys()) {
			final byte type = conditionKey.getType();
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
				Log.errorMessage("FilterController.setFilter | Unsupported condition type");
			}
		}
	}

	public void actionPerformed(final ActionEvent evt) {
		final Object source = evt.getSource();
		if (source == this.view.addConditionRef()) {
			this.addConditionToModel();
		} else if (source == this.view.changeKeyRef()) {
			this.changeKey();
		} else if (source == this.view.removeConditionRef()) {
			this.removeConditionInModelAndInScheme();
		} else if (source == this.view.createLogicalSchemeRef()) {
			this.createLogicalScheme();
		} else if (source == this.view.startDayButtonRef()) {
			this.view.createStartCalendar();
		} else if (source == this.view.endDayButtonRef()) {
			this.view.createEndCalendar();
		}
	}

	public void valueChanged(final ListSelectionEvent e) {
		final Object source = e.getSource();
		if (source == this.view.createdConditionListRef()) {
			this.changeRemoveState();
		}
	}

	private void changeRemoveState() {
		final String[] selectedNames = this.view.getSelectedConditionNames();
		if (selectedNames.length > 0) {
			this.view.enableRemoveButton(true);
		} else {
			this.view.enableRemoveButton(false);
		}
	}

	private void changeKey() {
		final int keyIndex = this.view.getSelectedKeyIndex();
		final ConditionKey key = this.model.getKeys().get(keyIndex);
		this.setActiveButton(key.getName());
		byte type = key.getType();
		final Object tempCondition = this.getTempCondition(key);
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
				final ListCondition listCondition = (ListCondition) tempCondition;
				List<StorableObject> linkedObjects = key.getLinkedObjects();
				if (linkedObjects == null) {
					try {
						final List<StorableObject> linkedStorableObjects = new ArrayList<StorableObject>(this.model.getLinkedConditionLoader().loadLinkedCondition(key.getLinkedEntityCode()));
						key.setLinkedObjects(linkedStorableObjects);
					} catch (IllegalDataException e1) {
						e1.printStackTrace();
					}
					linkedObjects = key.getLinkedObjects();
					final String[] linkedNames = new String[linkedObjects.size()];
					int i = 0;
					for (final StorableObject linkedObject : linkedObjects) {
						StorableObjectWrapper<StorableObject> wrapper;
						try {
							wrapper = StorableObjectWrapper.getWrapper(key.getLinkedEntityCode());
						} catch (IllegalDataException e) {
							Log.errorMessage(e.getMessage());
							return;
						}
						linkedNames[i++] = (String) wrapper.getValue(linkedObject, StorableObjectWrapper.VIEW_NAME);
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

	private void setActiveButton(final String name) {
		if (this.model.getConditionNames().contains(name)) {
			this.view.enableAdd(true);
		} else {
			this.view.enableAdd(false);
		}
	}

	private void addConditionToModel() {
		this.saveTempConditions();
		final int index = this.view.getSelectedKeyIndex();
		final ConditionKey conditionKey = this.model.getKeys().get(index);
		final String key = conditionKey.getKey();
		final byte type = conditionKey.getType();
		final Object tempCondition = getTempCondition(conditionKey);
		final short entityCode = this.model.getEntityCode();
		switch (type) {
			case ConditionWrapper.INT:
				final NumberCondition numberCondition = (NumberCondition) tempCondition;
				final String equals = numberCondition.getEquals();
				final String from = numberCondition.getFrom();
				final String to = numberCondition.getTo();
				final boolean includeBounds = numberCondition.isIncludeBounds();
				OperationSort sort = null;
				if (!equals.equals("") && from.equals("") && to.equals("")) {
					int equalsInt;
					try {
						equalsInt = Integer.parseInt(equals);
					} catch (NumberFormatException e) {
						Log.debugMessage("Flter.addCondition | Warning, wrong data format", Log.DEBUGLEVEL07);
						this.view.showErrorMessage(Filter.WRONG_NUMBER_MESSAGE);
						return;
					}
					this.model.addCondition(new TypicalCondition(equalsInt, equalsInt, OperationSort.OPERATION_EQUALS, entityCode, key),
							conditionKey);
					return;
				}
				if (equals.equals("") && !from.equals("") && !to.equals("")) {
					int fromInt;
					int toInt;
					try {
						fromInt = Integer.parseInt(numberCondition.getFrom());
					} catch (NumberFormatException e) {
						Log.debugMessage("Flter.addCondition | Warning, wrong data format", Log.DEBUGLEVEL07);
						this.view.showErrorMessage(Filter.WRONG_NUMBER_MESSAGE);
						return;
					}
					try {
						toInt = Integer.parseInt(numberCondition.getTo());
					} catch (NumberFormatException e) {
						Log.debugMessage("Flter.addCondition | Warning, wrong data format", Log.DEBUGLEVEL07);
						this.view.showErrorMessage(Filter.WRONG_NUMBER_MESSAGE);
						return;
					}
					this.model.addCondition(new TypicalCondition(fromInt, toInt, OperationSort.OPERATION_IN_RANGE, entityCode, key),
							conditionKey);
					return;
				}
				if (equals.equals("") && !from.equals("") && to.equals("")) {
					int fromInt;
					try {
						fromInt = Integer.parseInt(numberCondition.getFrom());
					} catch (NumberFormatException e) {
						Log.debugMessage("Flter.addCondition | Warning, wrong data format", Log.DEBUGLEVEL07);
						this.view.showErrorMessage(Filter.WRONG_NUMBER_MESSAGE);
						return;
					}
					if (includeBounds) {
						sort = OperationSort.OPERATION_GREAT_EQUALS;
					}
					else {
						sort = OperationSort.OPERATION_GREAT;
					}
					this.model.addCondition(new TypicalCondition(fromInt, fromInt, sort, entityCode, key), conditionKey);
					return;
				}
				if (equals.equals("") && from.equals("") && !to.equals("")) {
					int toInt;
					try {
						toInt = Integer.parseInt(numberCondition.getTo());
					} catch (NumberFormatException e) {
						Log.debugMessage("Flter.addCondition | Warning, wrong data format", Log.DEBUGLEVEL07);
						this.view.showErrorMessage(Filter.WRONG_NUMBER_MESSAGE);
						return;
					}
					if (includeBounds) {
						sort = OperationSort.OPERATION_LESS_EQUALS;
					}
					else {
						sort = OperationSort.OPERATION_LESS;
					}
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
				final StringCondition stringCondition = (StringCondition) tempCondition;
				final String conditionString = stringCondition.getString();
				if (conditionString == null || conditionString.equals("")) {
					this.view.showErrorMessage(Filter.WRONG_STRING_MESSAGE);
					return;
				}
				this.model.addCondition(new TypicalCondition(conditionString, OperationSort.OPERATION_EQUALS, entityCode, key),
						conditionKey);
				break;
			case ConditionWrapper.CONSTRAINT:
				final ListCondition listCondition2 = (ListCondition) tempCondition;
				final Set<StorableObjectCondition> conditions = new HashSet<StorableObjectCondition>();
				final int[] selectedIndices = listCondition2.getSelectedIndices();
				if (selectedIndices.length == 0) {
					this.view.showErrorMessage(Filter.WRONG_LIST_MESSAGE);
					return;
				}
				for (int i = 0; i < selectedIndices.length; i++) {
					final TypicalCondition condition = new TypicalCondition(selectedIndices[i],
							selectedIndices[i],
							OperationSort.OPERATION_EQUALS,
							entityCode,
							key);
					conditions.add(condition);
				}
				if (conditions.size() == 1) {
					this.model.addCondition(conditions.iterator().next(), conditionKey);
				}
				else {
					try {
						this.model.addCondition(new CompoundCondition(conditions, CompoundConditionSort.OR), conditionKey);
					} catch (CreateObjectException e) {
						// never will happen;
						Log.errorMessage(e.getMessage());
					}
				}
				break;
			case ConditionWrapper.LIST:
				final ListCondition listCondition = (ListCondition) tempCondition;
				final List<StorableObject> linkedObjects = conditionKey.getLinkedObjects();
				final int[] linkedIndex = listCondition.getSelectedIndices();
				if (linkedIndex.length == 0) {
					this.view.showErrorMessage(Filter.WRONG_LIST_MESSAGE);
					return;
				}
				final ArrayList<Identifier> selectedObjectIds = new ArrayList<Identifier>(linkedIndex.length);
				for (int i = 0; i < linkedIndex.length; i++) {
					selectedObjectIds.add(linkedObjects.get(linkedIndex[i]).getId());
				}
				this.model.addCondition(new LinkedIdsCondition(new HashSet<Identifier>(selectedObjectIds), entityCode), conditionKey);
				break;
			case ConditionWrapper.DATE:
				final DateCondition dateCondition = (DateCondition) tempCondition;
				final Date start = dateCondition.getStartDate();
				final Date end = dateCondition.getEndDate();
				OperationSort sort2 = null;
				if (start != null && end != null) {
					sort2 = OperationSort.OPERATION_IN_RANGE;
				}
				else if (start != null && end == null) {
					sort2 = OperationSort.OPERATION_GREAT_EQUALS;
				}
				else if (start == null && end != null) {
					sort2 = OperationSort.OPERATION_LESS_EQUALS;
				}
				if (sort2 == null) {
					return;
				}
				this.model.addCondition(new TypicalCondition(start, end, sort2, entityCode, key), conditionKey);
				break;
			default:
				Log.errorMessage("FilterController.addConditionToModel | Unsupported condition type");
		}
		this.setActiveButton(conditionKey.getName());
	}

	private void removeConditionInModelAndInScheme() {
		final String[] selectedConditionNames = this.view.getSelectedConditionNames();
		for (int i = 0; i < selectedConditionNames.length; i++) {
			this.model.removeCondition(selectedConditionNames[i]);
		}
		final int index = this.view.getSelectedKeyIndex();
		final ConditionKey conditionKey = this.model.getKeys().get(index);
		this.setActiveButton(conditionKey.getName());
	}

	public void createLogicalScheme() {
		if (!this.model.hasCondition()) {
			this.view.showErrorMessage(Filter.NO_CONDITIONS_CREATED);
			return;
		}
		this.view.createLogicalSchemeView(this.model.getLogicalScheme());
	}

// private boolean noConditionsCreated() {
//		if (this.model.getKeyNameCondition().keySet() == null
//				|| this.model.getKeyNameCondition().keySet().size() == 0) {
//			return true;
//		}
//		return false;
//	}
		
	public void popupMenuCanceled(final PopupMenuEvent e) {
		// Nothing to do
	}

	public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
		// Nothing to do
	}

	public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
		saveTempConditions();
	}

	private Object getTempCondition(final ConditionKey key) {
		return this.keyTempCondition.get(key);
	}

	private void saveTempConditions() {
		final int index = this.view.getSelectedKeyIndex();
		final ConditionKey conditionKey = this.model.getKeys().get(index);
		final byte type = conditionKey.getType();
		final Object tempCondition = getTempCondition(conditionKey);
		switch (type) {
			case ConditionWrapper.INT:
			case ConditionWrapper.FLOAT:
			case ConditionWrapper.DOUBLE:
				final NumberCondition intCondition = (NumberCondition) tempCondition;
				this.view.setNumberCondition(intCondition);
				break;
			case ConditionWrapper.STRING:
				final StringCondition stringCondition = (StringCondition) tempCondition;
				this.view.setStringCondition(stringCondition);
				break;
			case ConditionWrapper.CONSTRAINT:
			case ConditionWrapper.LIST:
				final ListCondition listCondition = (ListCondition) tempCondition;
				this.view.setListCondition(listCondition);
				break;
			case ConditionWrapper.DATE:
				final DateCondition dateCondition = (DateCondition) tempCondition;
				this.view.setDateCondition(dateCondition);
				break;
			default:
				Log.errorMessage("FilterController.saveTempConditions | Unsupported condition type");
		}
	}

}
