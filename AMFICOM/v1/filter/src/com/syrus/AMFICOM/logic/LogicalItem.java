/*
 * $Id: LogicalItem.java,v 1.1 2005/02/22 09:02:39 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.Collection;
import java.util.LinkedList;

import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/22 09:02:39 $
 * @author $Author: bob $
 * @module filter_v1
 */
public class LogicalItem implements Item {

	public final static String	AND		= "AND";
	public final static String	OR		= "OR";
	public final static String	ROOT	= "Result";

	private Collection			children;

	private int					maxChildrenCount;
	private int					maxParentCount;

	private String				name;
	private Object				type;

	public LogicalItem(StorableObjectCondition condition) {
		this.type = condition;
		this.maxChildrenCount = 0;
		this.maxParentCount = 1;
		this.name = condition.toString();
	}

	public LogicalItem(String sort) {
		if (sort.equals(OR)) {
			this.type = CompoundConditionSort.OR;
			this.name = OR;
			this.maxChildrenCount = Integer.MAX_VALUE;
			this.maxParentCount = 1;
		} else if (sort.equals(AND)) {
			this.type = CompoundConditionSort.AND;
			this.name = AND;
			this.maxChildrenCount = Integer.MAX_VALUE;
			this.maxParentCount = 1;
		} else if (sort.equals(ROOT)) {
			this.type = ROOT;
			this.name = ROOT;
			this.maxChildrenCount = 1;
			this.maxParentCount = 0;
		} else
			throw new UnsupportedOperationException("LogicalItem.<init> | Operation " + sort + " is not supported.");		
	}

	public void addChild(Item childItem) {
		if (this.children == null)
			this.children = new LinkedList();
		this.children.add(childItem);
	}

	public Collection getChildren() {
		return this.children;
	}

	public int getMaxChildrenCount() {
		return this.maxChildrenCount;
	}

	public int getMaxParentCount() {
		return this.maxParentCount;
	}

	public String getName() {
		return this.name;
	}

	public Object getObject() {
		return this.type;
	}

	public void removeChild(Item childItem) {
		this.children.remove(childItem);
	}
}
