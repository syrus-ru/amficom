/*
 * $Id: ObjectResourceCatalogFrame.java,v 1.5 2004/09/27 06:16:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2004/09/27 06:16:58 $
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

	public void setDisplayModel(ObjectResourceDisplayModel dmod)
	{
		panel.setDisplayModel(dmod);
	}

	public void setObjectResourceClass(Class orclass)
	{
		panel.setObjectResourceClass(orclass);
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

		panel = new ObjectResourceCatalogPanel(aContext);
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
