/*
 * $Id: PopulatableNode.java,v 1.1 2005/03/28 11:40:28 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree_;

import com.syrus.AMFICOM.logic.*;
import com.syrus.AMFICOM.logic.AbstractItem;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/28 11:40:28 $
 * @module generalclient_v1
 */

public class PopulatableNode extends AbstractItem implements Populatable {
	private boolean populated = false;
	private ChildrenFactory factory;
	private Object object;
	private String name;
	private boolean allowsChildren = true;
	
	public PopulatableNode(ChildrenFactory factory, Object object, String name, boolean allowsChildren) {
		this.factory = factory;
		this.object = object;
		this.name = name;
		this.allowsChildren = allowsChildren;
	}
	
	public void setPoputateChildrenFactory(ChildrenFactory factory) {
		this.factory = factory;
	}

	public void populate() {
		if (!populated) {
			this.factory.populate(this);
			this.populated = true;
		}
	}

	public boolean isPopulated() {
		return populated;
	}

	public int getMaxChildrenCount() {
		return Integer.MAX_VALUE;
	}

	public boolean canHaveParent() {
		return true;
	}
	
	public boolean canHaveChildren() {
		return allowsChildren;
	}

	public String getName() {
		return name;
	}

	public Object getObject() {
		return object;
	}

	public boolean isService() {
		return false;
	}
}
