package com.syrus.AMFICOM.Client.General.Command.Config;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceCatalogFrame;
import com.syrus.AMFICOM.Client.Map.UI.Display.MapContextDisplayModel;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.Pool;

import javax.swing.JDesktopPane;

public class MapCatalogueCommand extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;
	ApplicationModelFactory factory;

	public MapCatalogueCommand()
	{
	}

	public MapCatalogueCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext, ApplicationModelFactory factory)
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
		return new MapCatalogueCommand(dispatcher, desktop, aContext, factory);
	}

	public void execute()
	{
/*
		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(factory.create());
		aC.setConnectionInterface(aContext.getConnectionInterface());
		aC.setSessionInterface(aContext.getSessionInterface());
		aC.setDataSourceInterface(aC.getApplicationModel().getDataSource(aContext.getSessionInterface()));
		aC.setDispatcher(dispatcher);
		DataSourceInterface dataSource = aC.getDataSourceInterface();
*/
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if(dataSource == null)
			return;
			
		ObjectResourceCatalogFrame frame = new ObjectResourceCatalogFrame(LangModelConfig.String("titleMapsCatalog"), aContext);
		frame.setContents(new DataSet(Pool.getHash("mapcontext")));
		frame.setDisplayModel(new MapContextDisplayModel());
		frame.setObjectResourceClass(MapContext.class);

		frame.panel.setButtonPanelVisible(false);

		desktop.add(frame);

		frame.show();
	}

}