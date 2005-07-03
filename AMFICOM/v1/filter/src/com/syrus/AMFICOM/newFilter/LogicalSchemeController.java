/*
 * $Id: LogicalSchemeController.java,v 1.4 2005/05/18 12:42:50 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

import java.util.Collection;
import java.util.Iterator;

import com.syrus.AMFICOM.logic.LogicalItem;
import com.syrus.AMFICOM.logic.LogicalSchemeUI;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/18 12:42:50 $
 * @author $Author: bass $
 * @module filter_v1
 */

public class LogicalSchemeController {
	
	private LogicalScheme model;
	private LogicalSchemeView view;
	private LogicalSchemeUI logicalSchemeUI;
	
	private static final String	CONDITION_DELETE_ERROR	= "Can't delete condition";
	private static final String	ROOT_DELETE_ERROR		= "Can't delete root element";
	private static final String	NOT_LINKED_CONDITION	= "One or more conditions are not linked";
	private static final String	TO_BIG_NUMBER_OF_CHILDREN	= "To big number of children in the element ";
	private static final String	TO_SMALL_NUMBER_OF_CHILDREN	= "To small number of children in the element ";
	
	LogicalSchemeController(LogicalScheme scheme, LogicalSchemeView schemeView) {
		this.model = scheme;
		this.view = schemeView;
		this.logicalSchemeUI = new LogicalSchemeUI(this.model.getRootItem());
	}

	public void addAndItem() {
		this.logicalSchemeUI.addItem(new LogicalItem(LogicalItem.AND));
	}
	public void addOrItem() {
		this.logicalSchemeUI.addItem(new LogicalItem(LogicalItem.OR));
		
	}
	public void deleteItem() {
		Collection selectedItems = this.logicalSchemeUI.getSelectedItems();
		for (Iterator it = selectedItems.iterator(); it.hasNext();) {
			LogicalItem selectedItem = (LogicalItem) it.next();
			if(selectedItem.getType().equals(LogicalItem.CONDITION)) {
				this.view.showErrorMessage(CONDITION_DELETE_ERROR);
				return;
			} else if(selectedItem.getType().equals(LogicalItem.ROOT)) {
				this.view.showErrorMessage(ROOT_DELETE_ERROR);
				return;
			}
		}
		this.logicalSchemeUI.deleteSelectedItems();
	}
	public void confirm() {
		if(getValidation()) {
			this.view.dispose();			
		}		
	}
	public void cancel() {
		this.model.restore();
		this.view.dispose();
	}
	public LogicalSchemeUI getLogicalSchemeUI() {
		return this.logicalSchemeUI;
	}
	public void arrange(int width, int height) {
		this.logicalSchemeUI.setSize(width, height);
		this.logicalSchemeUI.arrange();		
	}
	private boolean getValidation() {
		LogicalItem rootItem = this.model.getRootItem();
		if (getConditionCount(rootItem) < getConditionCount(this.model.getSavedRootItem())) {
			this.view.showErrorMessage(NOT_LINKED_CONDITION);
			return false;
		}
		if (!isValid(rootItem))
			return false;
		return iterate(rootItem);
	}
	private boolean isValid(LogicalItem item) {
		int have = item.getChildrenCount();
		int max = item.getMaxChildrenCount();
		int min = item.getMinChildrenCount();
		if(have > max) {
			this.view.showErrorMessage(TO_BIG_NUMBER_OF_CHILDREN + item.getName() + " need " + max + " have " + have);
			return false;
		}
		if(have < min) {
			this.view.showErrorMessage(TO_SMALL_NUMBER_OF_CHILDREN + item.getName() + " need " + min + " have " + have);
			return false;				
		}
		return true;
	}
	
	private boolean iterate(LogicalItem parentItem) {
		if(parentItem.getChildren() == null)
			return true;
		for (Iterator it = parentItem.getChildren().iterator(); it.hasNext();) {
			LogicalItem item = (LogicalItem) it.next();
			if(!isValid(item))
				return false;
			if(item.getMaxChildrenCount() != 0)
				return iterate(item);
		}
		return true;
	}
	
	private int getConditionCount(LogicalItem parentItem) {
		int number = 0;
		return getConditionCount0(parentItem, number);
	}
	
	private int getConditionCount0(LogicalItem parentItem, int number) {
		if(parentItem == null || parentItem.getChildren() == null)
			return number;
		for (Iterator it = parentItem.getChildren().iterator(); it.hasNext();) {
			LogicalItem item = (LogicalItem) it.next();
			if (item.getMaxChildrenCount() == 0)
				number++;
			else
				number = getConditionCount0(item, number);		
		}
		
		return number;
	}
}
