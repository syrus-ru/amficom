package com.syrus.AMFICOM.Client.General.Command.Map;

import java.awt.*;
import javax.swing.*;
import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;

import com.syrus.AMFICOM.Client.Map.UI.*;

//A0A
public class MapSaveAsCommand extends VoidCommand
{
	MapMainFrame mapFrame;
    ApplicationContext aContext;
    JDesktopPane desktop;

	public MapSaveAsCommand()
	{
	}

	public MapSaveAsCommand(JDesktopPane desktop, MapMainFrame myMapFrame, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.mapFrame = myMapFrame;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MapSaveAsCommand(desktop, mapFrame, aContext);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		if(dataSource == null)
			return;
			
		MapContext mc = mapFrame.getMapContext();

		MapContext mc2 = new MapContext();
		mc2.setLongLat(mc.longitude, mc.latitude);
		mc2.domain_id = mc.domain_id;
		mc2.user_id = aContext.getSessionInterface().getUserId();
		mc2.description = mc.description;
		mc2.longitude = mc.longitude;
		mc2.latitude = mc.latitude;
		mc2.created_by = mc2.user_id;
		mc2.modified = mc2.created;
		mc2.modified_by = mc2.user_id;
		mc2.scheme_id = mc.scheme_id;

		aContext.getDispatcher().notify(new StatusMessageEvent("—охранение топологической схемы..."));

		NewMapContextDialog dialog = new NewMapContextDialog(mapFrame.mapJFrame, "—войства контекста", true, mc2);

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =  dialog.getSize();

		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dialog.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		dialog.setVisible(true);

		if ( dialog.ifAccept())
		{
			if(!mc2.scheme_id.equals(mc.scheme_id))
			{
				Scheme scheme = (Scheme )Pool.get( Scheme.typ, mc2.scheme_id);
				
				if(scheme.clones == null)
				{
					aContext.getDispatcher().notify(new StatusMessageEvent("—хемы несовместимы"));
					return;
				}
				String new_scheme_id = (String )scheme.clones.get(mc.scheme_id);
				if(new_scheme_id == null)
				{
					aContext.getDispatcher().notify(new StatusMessageEvent("—хемы несовместимы"));
					return;
				}
				if(!new_scheme_id.equals(scheme.id))
				{
					aContext.getDispatcher().notify(new StatusMessageEvent("—хема сохранена с ошибкой"));
					return;
				}

				Pool.putHash("schemeclonedids", scheme.clones);
			}
			
			mc2 = (MapContext )mc.clone(dataSource);

			if(!mc2.scheme_id.equals(mc.scheme_id))
			{
				Pool.removeHash("schemeclonedids");
				Pool.removeHash("mapclonedids");
			}

			Pool.put( mc2.getTyp(), mc2.getId(), mc2);
			dataSource.SaveMap(mc2.getId());

			if (mapFrame != null)
			{
				mapFrame.setMapContext(mc2);
				mapFrame.setTitle( LangModelMap.String("AppTitle") + " - "
												 + mapFrame.lnl().getMapContext().name);
			}
			aContext.getDispatcher().notify(new StatusMessageEvent("—хема успешно сохранена"));
		}
		else
		{
			aContext.getDispatcher().notify(new StatusMessageEvent("ќпераци€ отменена"));
		}
	}

}