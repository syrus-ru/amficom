/*
 * $Id: PopulatableIconedNode.java,v 1.8 2006/06/01 14:28:04 stas Exp $
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
import com.syrus.util.Shitlet;

/**
 * @author $Author: stas $
 * @version $Revision: 1.8 $, $Date: 2006/06/01 14:28:04 $
 * @module commonclient
 */
@Shitlet
public class PopulatableIconedNode extends IconedNode implements Populatable, Visualizable {
	private ChildrenFactory childrenFactory;
	private boolean populated = false;

	public PopulatableIconedNode() {
		super();
	}
	
	public PopulatableIconedNode(final ChildrenFactory factory, final Namable object) {
		this(factory, object, true);
	}
	
	public PopulatableIconedNode(final ChildrenFactory factory,
	                             final Namable object, 
	                             final boolean allowsChildren) {
		this(factory, object, (Icon)null, allowsChildren);
	}
	
	public PopulatableIconedNode(final ChildrenFactory factory, 
	                             final Namable object, 
	                             final Icon icon) {
		this(factory, object, icon, true);
	}
	
	public PopulatableIconedNode(final ChildrenFactory factory, 
	                             final Namable object, 
	                             final Icon icon, 
	                             final boolean allowsChildren) {
		this(factory, object, object.getName(), icon, allowsChildren);
		super.named = false;
	}

	public PopulatableIconedNode(final ChildrenFactory factory, 
	                             final Object object, 
	                             final String name) {
		this(factory, object, name, null, true);
	}
	
	public PopulatableIconedNode(final ChildrenFactory factory,
	                             final Object object, 
	                             final String name, 
	                             final boolean allowsChildren) {
		this(factory, object, name, null, allowsChildren);
	}
	
	public PopulatableIconedNode(final ChildrenFactory factory, 
	                             final Object object, 
	                             final String name, 
	                             final Icon icon) {
		this(factory, object, name, icon, true);
	}
	
	public PopulatableIconedNode(final ChildrenFactory factory, 
	                             final Object object, 
	                             final String name, 
	                             final Icon icon, 
	                             final boolean allowsChildren) {
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
		if (this.childrenFactory instanceof VisualManagerFactory) {
			return ((VisualManagerFactory)this.childrenFactory).getVisualManager(this);
		}
		return null;
	}

	public void setChildrenFactory(final ChildrenFactory childrenFactory) {
		this.childrenFactory = childrenFactory;
	}
	
	public boolean isPopulated() {
		return this.populated;
	}

	public ChildrenFactory getChildrenFactory() {
		return this.childrenFactory;
	}
	
	@Override
	public void clearChildren() {
		super.clearChildren();
		this.populated = false;
	}
	
}
