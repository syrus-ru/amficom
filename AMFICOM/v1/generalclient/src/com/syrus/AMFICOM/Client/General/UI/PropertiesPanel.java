/*
 * $Id: PropertiesPanel.java,v 1.6 2004/09/25 19:53:39 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import javax.swing.JPanel;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2004/09/25 19:53:39 $
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
