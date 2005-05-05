/*
 * $Id: GeneralPanel.java,v 1.7 2005/05/05 11:04:47 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.UI;

import javax.swing.JPanel;

import oracle.jdeveloper.layout.XYLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.corba.portable.reflect.common.ObjectResource;

/**
 * @author $Author: bob $
 * @version $Revision: 1.7 $, $Date: 2005/05/05 11:04:47 $
 * @module generalclient_v1
 */
public class GeneralPanel extends JPanel implements ObjectResourcePropertiesPane
{
	XYLayout xYLayout1 = new XYLayout();

	public ApplicationContext aContext = new ApplicationContext();

	public GeneralPanel()
	{
		jbInit();
	}

	private void jbInit()
	{
		setName(LangModel.getString("labelTabbedProperties"));
		this.setLayout(xYLayout1);
	}

	public ObjectResource getObjectResource()
	{
		return null;
	}

	public void setObjectResource(ObjectResource or)
	{
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public boolean modify()
	{
		return false;
	}

	public boolean create()
	{
		return false;
	}

	public boolean delete()
	{
		return false;
//		or.prepareRemoval();
//		Pool.remove(or.getTyp(), or);
	}

	public boolean open()
	{
		return false;
	}

	public boolean save()
	{
		return false;
	}

	public boolean cancel()
	{
		return false;
	}
}
