package com.syrus.AMFICOM.Client.General.Command.Config;

import java.awt.*;
import javax.swing.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
//import com.syrus.AMFICOM.Client.Test.*;
import com.syrus.AMFICOM.Client.Configure.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;

public class NewJMapViewCommand extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;
	ApplicationModelFactory factory;

	public MapMainFrame frame;

	public NewJMapViewCommand()
	{
	}

	public NewJMapViewCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext, ApplicationModelFactory factory)
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
		return new NewJMapViewCommand(dispatcher, desktop, aContext, factory);
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
        System.out.println("Starting MAP frame");
        Vector panelElements = new Vector();
        LangModelMap.initialize();

//        setMapEquipmentElements(panelElements);
        setMapKISElements(panelElements);
        /*MapMainFrame */

		frame = new MapJMainFrame(panelElements, aC);
		frame.setBounds(0, 0, 600, 520);
		desktop.add(frame);

		Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
		frame.setLocation(0, 0);
		frame.setSize(dim.width * 3 / 5, dim.height);

		frame.show();

		System.out.println("notify map j frame with dispatcher " + dispatcher);
		dispatcher.notify(new OperationEvent(frame, 0, "mapjframeshownevent"));
	}

  private void setMapEquipmentElements(Vector panelElements)
  {
	Hashtable hash;
	hash = Pool.getHash("mapequipmentproto");
	if(hash != null)
		for(Enumeration enum = hash.elements(); enum.hasMoreElements();)
			panelElements.add(enum.nextElement());
  }

  private void setMapKISElements(Vector panelElements)
  {
	Hashtable hash;
	hash = Pool.getHash("mapkisproto");
	if(hash != null)
		for(Enumeration enum = hash.elements(); enum.hasMoreElements();)
			panelElements.add(enum.nextElement());
  }

}