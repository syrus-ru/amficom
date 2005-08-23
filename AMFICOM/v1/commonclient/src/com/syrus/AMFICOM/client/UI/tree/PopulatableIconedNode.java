/*
 * $Id: PopulatableIconedNode.java,v 1.4 2005/08/23 09:07:05 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.tree;

import javax.swing.Icon;

import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Populatable;

/**
 * @author $Author: max $
 * @version $Revision: 1.4 $, $Date: 2005/08/23 09:07:05 $
 * @module commonclient
 */

public class PopulatableIconedNode extends IconedNode implements Populatable, Visualizable {
	private ChildrenFactory childrenFactory;
	private boolean populated = false;

	public PopulatableIconedNode() {
		super();
	}
	
	public PopulatableIconedNode(ChildrenFactory factory, Namable object) {
		this(factory, object, true);
	}
	
	public PopulatableIconedNode(ChildrenFactory factory, Namable object, boolean allowsChildren) {
		this(factory, object, (Icon)null, allowsChildren);
	}
	
	public PopulatableIconedNode(ChildrenFactory factory, Namable object, Icon icon) {
		this(factory, object, icon, true);
	}
	
	public PopulatableIconedNode(ChildrenFactory factory, Namable object, Icon icon, boolean allowsChildren) {
		this(factory, object, object.getName(), icon, allowsChildren);
	}

	public PopulatableIconedNode(ChildrenFactory factory, Object object, String name) {
		this(factory, object, name, null, true);
	}
	
	public PopulatableIconedNode(ChildrenFactory factory, Object object, String name, boolean allowsChildren) {
		this(factory, object, name, null, allowsChildren);
	}
	
	public PopulatableIconedNode(ChildrenFactory factory, Object object, String name, Icon icon) {
		this(factory, object, name, icon, true);
	}
	
	public PopulatableIconedNode(ChildrenFactory factory, Object object, String name, Icon icon, boolean allowsChildren) {
		super(object, name, icon, allowsChildren);
		this.childrenFactory = factory;
	}

	public void populate() {
		if (this.childrenFactory != null) {
			this.childrenFactory.populate(this);
			this.populated = true;
		}
	}
	
	public VisualManager getVisualManager() {
		if (this.childrenFactory instanceof VisualManagerFactory)
			return ((VisualManagerFactory)this.childrenFactory).getVisualManager(this);
		return null;
	}

	public void setChildrenFactory(ChildrenFactory childrenFactory) {
		this.childrenFactory = childrenFactory;
	}
	
	public boolean isPopulated() {
		return this.populated;
	}

	public ChildrenFactory getChildrenFactory() {
		return this.childrenFactory;
	}
	
	
}
