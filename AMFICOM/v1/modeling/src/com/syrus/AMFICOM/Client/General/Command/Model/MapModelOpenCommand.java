package com.syrus.AMFICOM.Client.General.Command.Model;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Command.Config.NewMapViewCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.MapOpenCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.MapModelingApplicationModelFactory;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;

import com.syrus.AMFICOM.Client.Model.ModelMDIMain;

public class MapModelOpenCommand extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	ModelMDIMain parent;
	Checker checker;

	public MapModelOpenCommand()
	{
	}

	public MapModelOpenCommand(Dispatcher dispatcher, ApplicationContext aContext, ModelMDIMain parent)
	{
		this.dispatcher = dispatcher;
		this.parent = parent;
		this.aContext = aContext;
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
		return new MapModelOpenCommand(dispatcher, aContext, parent);
	}

	public void execute()
	{
		boolean forFirst = true;
		this.checker = new Checker(this.aContext.getSessionInterface());
		if(!checker.checkCommand
			 (checker.openMapForModeling))
		return;

		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
			return;

		MapOpenCommand com = new MapOpenCommand(null, null, aContext);
		com.execute();
		if(com.retCode == 1)
		{
			parent.mapContext = (MapContext )Pool.get("mapcontext", com.retobj_id);

			NewMapViewCommand com2 = new NewMapViewCommand(
					dispatcher,
					parent.desktopPane,
					aContext,
					new MapModelingApplicationModelFactory());
			com2.execute();
			com2.frame.setMapContext(parent.mapContext);
			com2.frame.setLocation(500, 0);
			parent.mapframe = com2.frame;

			if(forFirst)
			{
				new ViewModelMapPropertiesCommand(parent, parent.desktopPane, aContext).execute();
				new ViewModelMapElementsCommand(parent, parent.desktopPane, aContext).execute();
			}

			Scheme scheme = (Scheme)Pool.get(Scheme.typ, parent.mapContext.scheme_id);
			scheme.unpack();

			dispatcher.notify(new OperationEvent(scheme, 0, "mapopenevent"));
//			dispatcher.notify(new SchemeElementsEvent(this, scheme,
//				SchemeElementsEvent.OPEN_PRIMARY_SCHEME_EVENT));
		}
	}

}