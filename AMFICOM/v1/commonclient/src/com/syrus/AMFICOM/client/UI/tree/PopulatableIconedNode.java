/*
 * $Id: PopulatableIconedNode.java,v 1.1 2005/05/25 07:55:08 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.tree;

import javax.swing.Icon;

import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.logic.*;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 07:55:08 $
 * @module generalclient_v1
 */

public class PopulatableIconedNode extends IconedNode implements Populatable, Visualizable {
	private ChildrenFactory factory;
	private boolean populated = false;
	
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
		this.factory = factory;
	}

	public void populate() {
		this.factory.populate(this);
		this.populated = true;
	}
	
	public VisualManager getVisualManager() {
		if (this.factory instanceof VisualManagerFactory)
			return ((VisualManagerFactory)this.factory).getVisualManager(this);
		return null;
	}

	public boolean isPopulated() {
		return this.populated;
	}
}
