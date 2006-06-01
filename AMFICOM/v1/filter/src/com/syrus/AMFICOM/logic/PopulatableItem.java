/*-
 * $Id: PopulatableItem.java,v 1.11 2006/06/01 14:26:59 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Populatable item with default properties
 *
 * @version $Revision: 1.11 $, $Date: 2006/06/01 14:26:59 $
 * @author $Author: stas $
 * @author Vladimir Dolzhenko
 * @module filter
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
		if (this.childrenFactory != null) {
			synchronized(this) {
				this.childrenFactory.populate(this);
				this.populated = true;
			}
		}

	}

	public boolean isPopulated() {
		return this.populated;
	}	

	public void removeAllChildren() {
		if (this.children != null && !this.children.isEmpty()) {
			final List<Item> list = new ArrayList<Item>(this.children);
			for (final Item item : list) {
				item.setParent(null);
			}
		}
		this.populated = false;
	}

	public void setCanHaveChildren(final boolean canHaveChildren) {
		this.canHaveChildren = canHaveChildren;
	}

	public void setCanHaveParent(final boolean canHaveParent) {
		this.canHaveParent = canHaveParent;
	}

	public void setChildrenFactory(final ChildrenFactory childrenFactory) {
		this.childrenFactory = childrenFactory;
	}

	public void setMaxChildrenCount(final int maxChildrenCount) {
		this.maxChildrenCount = maxChildrenCount;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setObject(final Object object) {
		this.object = object;
	}

	public void setService(final boolean service) {
		this.service = service;
	}

}
