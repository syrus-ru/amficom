/*
 * $Id: PropertiesPanel.java,v 1.7 2005/05/05 11:04:47 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI;

import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.corba.portable.reflect.common.ObjectResource;

/**
 * @author $Author: bob $
 * @version $Revision: 1.7 $, $Date: 2005/05/05 11:04:47 $
 * @module generalclient_v1
 * @deprecated Extend {@link JPanel} and implement
 *             {@link ObjectResourcePropertiesPane} instead.
 */
public abstract class PropertiesPanel extends JPanel implements ObjectResourcePropertiesPane {
	public PropertiesPanel() {
		this.setName(LangModel.getString("labelTabbedProperties"));
	}

	public abstract ObjectResource getObjectResource();

	/**
	 * @param objectResource the new value of <code>objectResource</code>
	 *        property.
	 * @return <code>true</code> if operation succeeded, <code>false</code>
	 *         otherwise.
	 */
	public abstract void setObjectResource(ObjectResource objectResource);

	public abstract void setContext(ApplicationContext aContext);

	public abstract boolean modify();

	public abstract boolean create();

	public abstract boolean delete();

	public abstract boolean open();

	public abstract boolean save();

	public boolean cancel() {
		return false;
	}
}
