package com.syrus.AMFICOM.Client.General.Command.Config;

import com.syrus.AMFICOM.Client.General.Command.Map.MapOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.Pool;

import javax.swing.JDesktopPane;

public class MapViewOpenCommand extends VoidCommand
{
	public Dispatcher dispatcher;
	public ApplicationContext aContext;
	public JDesktopPane desktop;
	public ApplicationModelFactory factory;

	public boolean opened = false;
	public String mc_id = "";

	public MapViewOpenCommand()
	{
	}

	public MapViewOpenCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext, ApplicationModelFactory factory)
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
		return new MapViewOpenCommand(dispatcher, desktop, aContext, factory);
	}

	public void execute()
	{
		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(factory.create());
		aC.setConnectionInterface(aContext.getConnectionInterface());
		aC.setSessionInterface(aContext.getSessionInterface());
		aC.setDataSourceInterface(aC.getApplicationModel().getDataSource(aContext.getSessionInterface()));
		aC.setDispatcher(dispatcher);

//		MapOpenCommand com = new MapOpenCommand(desktop, null, aContext);
		MapOpenCommand com = new MapOpenCommand(desktop, null, aC);
		com.execute();
		if(com.retCode == 1)
		{
			MapContext mc = (MapContext )Pool.get("mapcontext", com.retobj_id);
/*			
			if(mc.isOpened())
			{
				MessageBox mb = new MessageBox("Топологическая схема уже открыта в другом окне!");
				return;
			}
*/		
			NewMapViewCommand com2 = new NewMapViewCommand(dispatcher, desktop, aContext, factory);
			com2.execute();
			if(com2.frame == null)
				return;
				
			while(!com2.frame.isVisible())
				;
			com2.frame.setMapContext(mc);
			opened = true;
			mc_id = com.retobj_id;
		}
	}

}