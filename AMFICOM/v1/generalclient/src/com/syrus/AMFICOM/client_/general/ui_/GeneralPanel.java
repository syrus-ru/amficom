/*
 * $Id: GeneralPanel.java,v 1.1 2004/10/26 08:18:27 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client_.general.ui_;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import javax.swing.JPanel;
import oracle.jdeveloper.layout.XYLayout;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2004/10/26 08:18:27 $
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

	public Object getObject()
	{
		return null;
	}

	public void setObject(Object or)
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
