package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

public class MapNewCommand extends VoidCommand
{
	ApplicationContext aContext;
	MapFrame mapFrame;

	public MapView mv;
	public Map mc;

	public MapNewCommand()
	{
	}

	public MapNewCommand(MapFrame mapFrame, ApplicationContext aContext)
	{
		this.mapFrame = mapFrame;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MapNewCommand(mapFrame, aContext);
	}

	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_CONFIG, "Creating new map", getClass().getName(), "execute()");
		
//		new MapViewNewCommand(mapFrame, aContext).execute();

		System.out.println("Creating new map context");
		aContext.getDispatcher().notify(
				new StatusMessageEvent(
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString("MapNew")));
		mc = new Map();

		mc.setId(aContext.getDataSourceInterface().GetUId(Map.typ));
		mc.setDomainId(aContext.getSessionInterface().getDomainId());
		mc.setUserId(aContext.getSessionInterface().getUserId());
/*
		ObjectResourcePropertiesDialog dialog = new ObjectResourcePropertiesDialog( 
				Environment.getActiveWindow(), 
				LangModel.getString("MapContextProperties"), 
				true, 
				mc,
				com.syrus.AMFICOM.Client.Resource.Map.Map.getPropertyPane());

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =  dialog.getSize();

		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dialog.setLocation(
				(screenSize.width - frameSize.width) / 2, 
				(screenSize.height - frameSize.height) / 2);
		dialog.setVisible(true);

		if ( dialog.ifAccept())
		{
*/
		Pool.put( mc.getTyp(), mc.getId(), mc);

		mv = new MapView();

		mv.setId(aContext.getDataSourceInterface().GetUId(MapView.typ));
		mv.setDomainId(aContext.getSessionInterface().getDomainId());
		mv.setName(LangModelMap.getString("New"));

		mv.setMap(mc);

		Pool.put( MapView.typ, mv.getId(), mv);

		if (mapFrame != null)
		{
			mv.setLogicalNetLayer(mapFrame.getMapViewer().getLogicalNetLayer());
			mapFrame.setMapView(mv);
			mapFrame.setTitle( LangModelMap.getString("Map") + " - " + mv.getName());
		}
		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
		setResult(Command.RESULT_OK);
/*
		}
		else
		{
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
		}
*/
	}

}
