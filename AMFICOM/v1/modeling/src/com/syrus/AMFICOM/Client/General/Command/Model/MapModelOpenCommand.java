package com.syrus.AMFICOM.Client.General.Command.Model;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapWindowCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewOpenCommand;
import com.syrus.AMFICOM.Client.Model.ModelMDIMain;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

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

		MapViewOpenCommand viewOpen = new MapViewOpenCommand(parent.desktopPane, null, aContext);
		viewOpen.execute();
		if (viewOpen.getReturnObject() == null)
			return;

		MapView view = (MapView) viewOpen.getReturnObject();
		ViewMapWindowCommand com2 = new ViewMapWindowCommand(
				aContext.getDispatcher(), parent.desktopPane, aContext,
				new MapModelingApplicationModelFactory());
		com2.execute();
		com2.frame.setMapView(view);
		com2.frame.setLocation(500, 0);
		parent.mapframe = com2.frame;

		if (forFirst) {
			new ViewModelMapPropertiesCommand(parent, parent.desktopPane, aContext).
					execute();
			new ViewModelMapElementsCommand(parent, parent.desktopPane, aContext).
					execute();
		}
		dispatcher.notify(new OperationEvent(view.getSchemes(), 0, "mapopenevent"));
	}
}
