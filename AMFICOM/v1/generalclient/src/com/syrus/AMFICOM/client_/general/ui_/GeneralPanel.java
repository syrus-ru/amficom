/*
 * $Id: GeneralPanel.java,v 1.2 2005/03/11 16:10:18 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client_.general.ui_;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import javax.swing.*;
import javax.swing.JPanel;
import oracle.jdeveloper.layout.XYLayout;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/11 16:10:18 $
 * @module generalclient_v1
 */
public class GeneralPanel extends JPanel implements ObjectResourcePropertiesPane
{
	JComponent panel;
	
	public ApplicationContext aContext = new ApplicationContext();

	public GeneralPanel()
	{
		jbInit();
	}
	
	public JComponent getGUI() {
		if (panel == null)
			panel = new JPanel();
		return panel; 
	}

	private void jbInit()
	{
		XYLayout xYLayout1 = new XYLayout();
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
