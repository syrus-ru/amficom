/*
 * $Id: ObjectResourcePropertyFrame.java,v 1.5 2004/09/27 05:56:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import java.awt.*;
import java.util.List;
import javax.swing.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2004/09/27 05:56:57 $
 * @module generalclient_v1
 */
public class ObjectResourcePropertyFrame extends JInternalFrame
		implements OperationListener
{
	protected ObjectResourcePropertyTablePane panel = new ObjectResourcePropertyTablePane();

//	public ObjectResourceCatalogPanel panel;
	public ApplicationContext aContext;

	public ObjectResourcePropertyFrame(String title)
	{
		this(title, new ApplicationContext());
	}

	public ObjectResourcePropertyFrame(String title, ApplicationContext aContext)
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
//		panel.setContext(aContext);
		if(aContext == null)
			return;
		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
//		disp.register(this, MapNavigateEvent.type);
		disp.register(this, TreeDataSelectionEvent.type);
		disp.register(this, TreeListSelectionEvent.typ);
	}

	public void setContents(ObjectResource obj)
	{
		panel.setSelected(obj);
	}

	public void setEnableDisable(boolean b)
	{
		panel.setEnabled(b);
	}

	public void initialize()
	{
		panel.initialize(new String[] {"Свойство", "Значение"}, null);
	}

	private void jbInit() throws Exception
	{

		this.setClosable(true);
		this.setIconifiable(false);
		this.setMaximizable(false);
		this.setResizable(true);

		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		getContentPane().setLayout(new BorderLayout());

		panel = new ObjectResourcePropertyTablePane();
		getContentPane().add(panel, BorderLayout.CENTER);
	}

	public void operationPerformed(OperationEvent oe )
	{
		if(oe.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			TreeDataSelectionEvent tdse = (TreeDataSelectionEvent)oe;

			List data = tdse.getList();
			int n = tdse.getSelectionNumber();

			if (n != -1)
			{
				ObjectResource res = (ObjectResource )data.get(n);
				panel.setSelected(res);
			}
		}
		else
		if(oe.getActionCommand().equals(TreeListSelectionEvent.typ))
		{
			Object o = oe.getSource();
			if(o instanceof ObjectResource)
				panel.setSelected((ObjectResource )o);
		}
/*
		if(oe.getActionCommand().equals(MapNavigateEvent.type))
		{
			ObjectResource me = (ObjectResource )oe.getSource();
			panel.setSelected(me);
		}
*/
	}

}
