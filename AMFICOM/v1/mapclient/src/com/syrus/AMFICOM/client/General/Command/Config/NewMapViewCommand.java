package com.syrus.AMFICOM.Client.General.Command.Config;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.UI.MessageBox;
import com.syrus.AMFICOM.Client.Map.MapMainFrame;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.Dimension;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JDesktopPane;

public class NewMapViewCommand extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;
	ApplicationModelFactory factory;

	public MapMainFrame frame;

	public NewMapViewCommand()
	{
	}

	public NewMapViewCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext, ApplicationModelFactory factory)
	{
		this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
		this.factory = factory;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		else
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new NewMapViewCommand(dispatcher, desktop, aContext, factory);
	}

	public void execute()
	{
		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(factory.create());
		aC.setConnectionInterface(aContext.getConnectionInterface());
		aC.setSessionInterface(aContext.getSessionInterface());
		aC.setDataSourceInterface(aC.getApplicationModel().getDataSource(aContext.getSessionInterface()));
		aC.setDispatcher(dispatcher);

		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if(dataSource == null)
			return;

		Vector panelElements = new Vector();
		setMapProtoElements(panelElements);
		
		frame = (MapMainFrame )Pool.get("environment", "mapmainframe");
		if(frame == null)
		{
			System.out.println("Starting MAP frame");
			frame = new MapMainFrame(panelElements, aC);
			Pool.put("environment", "mapmainframe", frame);
		}

		if(frame.isVisible())
		{
			if(frame.getParent() != null)
			{
				if(frame.getParent().equals(desktop))
				{
					frame.setMapContext(null);
					frame.show();
					dispatcher.notify(new OperationEvent(frame, 0, "mapframeshownevent"));
					return;
				}
				else
				{
					frame = null;
					MessageBox mb = new MessageBox("Окно топологической схемы уже открыто в другом модуле.");
					return;
				}
			}
		}//if(frame.isVisible())
		else
		{
			JDesktopPane dt = (JDesktopPane )frame.getParent();
			if(dt != null)
			{
				dt.remove(frame);
			}
			desktop.add(frame);
			frame.setContext(aC);
			Dimension dim = desktop.getSize();
			frame.setLocation(0, 0);
			frame.setSize(dim.width * 4 / 5, dim.height);
			frame.setMapContext(null);
			frame.show();
			dispatcher.notify(new OperationEvent(frame, 0, "mapframeshownevent"));
			return;
		}

	}

	private void setMapProtoElements(Vector panelElements)
	{
	Hashtable hash;
	hash = Pool.getHash("mapprotoelement");
	if(hash != null)
		for(Enumeration enum = hash.elements(); enum.hasMoreElements();)
		{
			MapProtoElement mpe = (MapProtoElement )enum.nextElement();
			if(mpe.is_topological)
				panelElements.add(mpe);
		}
	}
}