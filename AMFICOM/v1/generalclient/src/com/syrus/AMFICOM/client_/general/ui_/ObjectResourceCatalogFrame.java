/*
 * $Id: ObjectResourceCatalogFrame.java,v 1.1 2005/01/31 09:22:28 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.util.List;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceCatalogActionModel;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/01/31 09:22:28 $
 * @module generalclient_v1
 */
public class ObjectResourceCatalogFrame
		extends JInternalFrame
		implements OperationListener
{
	public ObjectResourceCatalogPanel panel;
	public ApplicationContext aContext;

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
		this.aContext = aContext;
		panel.setContext(aContext);
		if(aContext != null)
			aContext.getDispatcher().register(this, TreeDataSelectionEvent.type);
	}

	public void setContents(List dataSet)
	{
		panel.setContents(dataSet);
	}

	public void setObjectResourceController(Class cl, ObjectResourceCatalogController orcc)
	{
		panel.setObjectResourceController(cl, orcc);
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

	public void operationPerformed(OperationEvent oe )
	{
		if(oe.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			TreeDataSelectionEvent tdse = (TreeDataSelectionEvent)oe;

			Class cl = tdse.getDataClass();

			String title;
			try
			{
				java.lang.reflect.Field typField = cl.getField("typ");
				title = (String )typField.get(cl);
			}
			catch(IllegalAccessException iae)
			{
				System.out.println("Ошибка определения значения поля typ - " + iae.getMessage());
				title = "";
			}
			catch(Exception e)
			{
				System.out.println("Не найден объект ObjectResource - " + e.getMessage());
				title = "";
			}

			setTitle(LangModel.getString("node" + title));
		}
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
