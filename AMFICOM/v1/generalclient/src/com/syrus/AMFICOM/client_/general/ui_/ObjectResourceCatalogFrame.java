/*
 * $Id: ObjectResourceCatalogFrame.java,v 1.3 2005/03/01 08:55:22 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.util.List;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceCatalogActionModel;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/03/01 08:55:22 $
 * @module generalclient_v1
 */
public class ObjectResourceCatalogFrame
		extends JInternalFrame
{
	public ObjectResourceCatalogPanel panel;

	public ObjectResourceCatalogFrame(String title)
	{
		this(title, new ApplicationContext());
	}

	public ObjectResourceCatalogFrame(String title, ApplicationContext aContext)
	{
		super(title);

		try
		{
			jbInit();
			pack();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		setContext(aContext);
	}

	public void setContext(ApplicationContext aContext)
	{
		panel.setContext(aContext);
	}

	public void setContents(List dataSet)
	{
		panel.setContents(dataSet);
	}

	public void setObjectResourceController(ObjectResourceController controller)
	{
		panel.setObjectResourceController(controller);
	}

	public void setActionModel(ObjectResourceCatalogActionModel orcam)
	{
		panel.setActionModel(orcam);
	}

	private void jbInit() throws Exception
	{
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		this.setResizable(true);

		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));

		getContentPane().setLayout(new BorderLayout());

		panel = new ObjectResourceCatalogPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
	}

	public void doDefaultCloseAction()
	{
		if (isMaximum())
		try
		{
			setMaximum(false);
		}
		catch (java.beans.PropertyVetoException ex)
		{
			ex.printStackTrace();
		}
		super.doDefaultCloseAction();
	 }
}
