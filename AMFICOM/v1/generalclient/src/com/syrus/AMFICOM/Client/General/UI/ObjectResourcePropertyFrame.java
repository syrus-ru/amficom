/*
 * $Id: ObjectResourcePropertyFrame.java,v 1.6 2005/05/05 11:04:47 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.Client.General.UI;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.corba.portable.reflect.common.ObjectResource;

/**
 * @author $Author: bob $
 * @version $Revision: 1.6 $, $Date: 2005/05/05 11:04:47 $
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
		panel.initialize(new String[] {"��������", "��������"}, null);
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
