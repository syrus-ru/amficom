package com.syrus.AMFICOM.Client.General.Command.Config;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;

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
		
//        setMapEquipmentElements(panelElements);
//        setMapKISElements(panelElements);
				/*MapMainFrame */

		frame = (MapMainFrame )Pool.get("environment", "mapmainframe");
		if(frame == null)
		{
			System.out.println("Starting MAP frame");
			LangModelMap.initialize();
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
		}
		else
		{
			JDesktopPane dt = (JDesktopPane )frame.getParent();
			if(dt != null)// && !frame.getParent().equals(dt))
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

/*
		frame = null;
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			try
			{
				MapMainFrame comp = (MapMainFrame )desktop.getComponent(i);
				// уже есть окно карты
				frame = comp;
				break;
			}
			catch(Exception ex)
			{
			}
		}

		if(frame == null)
		{
			MapMainFrame mmf = (MapMainFrame )Pool.get("environment", "mapmainframe");
			if(mmf != null)
			{
				MessageBox mb = new MessageBox("Невозможно открыть второе окно топологической схемы!");
				return;
			}
			frame = new MapMainFrame(panelElements, aC);
			desktop.add(frame);
			Dimension dim = desktop.getSize();
			frame.setLocation(0, 0);
			frame.setSize(dim.width * 4 / 5, dim.height);

			Pool.put("environment", "mapmainframe", frame);
		}
		frame.show();


		dispatcher.notify(new OperationEvent(frame, 0, "mapframeshownevent"));
*/
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

	private void setMapEquipmentElements(Vector panelElements)
	{
	Hashtable hash;
	hash = Pool.getHash("mapprotoelement");
	if(hash != null)
		for(Enumeration enum = hash.elements(); enum.hasMoreElements();)
		{
			MapProtoElement mpe = (MapProtoElement )enum.nextElement();
			if(!mpe.pe_is_kis)
				panelElements.add(mpe);
		}
	}

	private void setMapKISElements(Vector panelElements)
	{
	Hashtable hash;
	hash = Pool.getHash("mapprotoelement");
	if(hash != null)
		for(Enumeration enum = hash.elements(); enum.hasMoreElements();)
		{
			MapProtoElement mpe = (MapProtoElement )enum.nextElement();
			if(mpe.pe_is_kis)
				panelElements.add(mpe);
		}
	}

}