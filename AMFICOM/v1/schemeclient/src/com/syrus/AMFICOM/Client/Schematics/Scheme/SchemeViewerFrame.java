package com.syrus.AMFICOM.Client.Schematics.Scheme;

import java.beans.PropertyVetoException;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.scheme.corba.Scheme;

public class SchemeViewerFrame extends JInternalFrame implements OperationListener
{
	public static final String schemeFrameDisplayed = "schemeFrameDisplayed";

	public ApplicationContext aContext;
	Dispatcher dispatcher;
	UgoTabbedPane pane;

	public SchemeViewerFrame(ApplicationContext aContext, UgoTabbedPane pane)
	{
		this.aContext = aContext;
		this.pane = pane;
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		init_module();
	}

	public void init_module()
	{
		dispatcher = aContext.getDispatcher();
		dispatcher.register(this, SchemeElementsEvent.type);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(SchemeElementsEvent.type))
		{
			SchemeElementsEvent see = (SchemeElementsEvent)ae;
			if (see.CLOSE_SCHEME)
			{
				if (see.obj instanceof Scheme)
				{
					Scheme sch = (Scheme)see.obj;
//					if (panel.removeScheme(sch))
					{
						closeFrame();
					}
				}
			}
		}
	}

	protected void closeFrame ()
	{
//		panel.scheme = new Scheme();
		dispatcher.unregister(this, SchemeElementsEvent.type);
		doDefaultCloseAction();
	}

	private void jbInit() throws Exception
	{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		setResizable(true);
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pane, BorderLayout.CENTER);
	}

	public SchemeGraph getGraph()
	{
		return pane.getPanel().getGraph();
	}

	public void doDefaultCloseAction()
	{
		if (isMaximum())
			try
			{
				setMaximum(false);
			}
			catch (PropertyVetoException ex)
			{
				ex.printStackTrace();
			}
		super.doDefaultCloseAction();
	}
}

