/*-
 * $Id: PopulatableItem.java,v 1.1 2005/03/31 09:54:30 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

/**
 * Populatable item with default properties
 * 
 * @version $Revision: 1.1 $, $Date: 2005/03/31 09:54:30 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public class PopulatableItem extends AbstractItem implements Populatable {

	protected boolean			canHaveChildren	= true;
	protected boolean			canHaveParent	= true;

	protected boolean			populated		= false;
	protected ChildrenFactory	childrenFactory;

	protected int				maxChildrenCount;

	protected String			name;
	protected Object			object;

	protected boolean			service			= false;

	public boolean canHaveChildren() {
		return this.canHaveChildren;
	}

	public boolean canHaveParent() {
		return this.canHaveParent;
	}

	public int getMaxChildrenCount() {
		return this.maxChildrenCount;
	}

	public String getName() {
		return this.name;
	}

	public Object getObject() {
		return this.object;
	}

	public boolean isService() {
		return this.service;
	}

	public void populate() {
		if (!this.populated && this.childrenFactory != null) {
			this.childrenFactory.populate(this);
			this.populated = true;
		}

	}

	public void setCanHaveChildren(boolean canHaveChildren) {
		this.canHaveChildren = canHaveChildren;
	}

	public void setCanHaveParent(boolean canHaveParent) {
		this.canHaveParent = canHaveParent;
	}

	public void setChildrenFactory(ChildrenFactory childrenFactory) {
		this.childrenFactory = childrenFactory;
	}

	public void setMaxChildrenCount(int maxChildrenCount) {
		this.maxChildrenCount = maxChildrenCount;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public void setService(boolean service) {
		this.service = service;
	}

}
