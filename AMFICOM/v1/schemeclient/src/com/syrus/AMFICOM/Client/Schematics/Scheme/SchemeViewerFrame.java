package com.syrus.AMFICOM.Client.Schematics.Scheme;

import com.syrus.AMFICOM.Client.Survey.SurveyMDIMain;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeGraph;
import com.syrus.AMFICOM.Client.General.Scheme.UgoPanel;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;

public class SchemeViewerFrame extends JInternalFrame implements OperationListener
{
	public ApplicationContext aContext;
	Dispatcher dispatcher;
	public UgoPanel panel;

	public SchemeViewerFrame(ApplicationContext aContext, UgoPanel panel)
	{
		this.panel = panel;
		this.aContext = aContext;
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		init_module();
    
    aContext.getDispatcher().notify (
      new OperationEvent (this,0,SurveyMDIMain.schemeFrameDisplayed));
    
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
					if (panel.removeScheme(sch))
					{
						closeFrame();
					}
				}
			}
		}
	}

	protected void closeFrame ()
	{
		panel.scheme = new Scheme();
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
		getContentPane().add(panel, BorderLayout.CENTER);
	}

	public SchemeGraph getGraph()
	{
		return panel.getGraph();
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

