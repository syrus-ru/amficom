package com.syrus.AMFICOM.Client.General.Command.Map;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;

import com.syrus.AMFICOM.Client.Configure.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Configure.Map.UI.*;

//A0A
public class MapNewCommand extends VoidCommand
{
	ApplicationContext aContext;
	MapMainFrame mapFrame;
	public MapContext mc;
	public int retCode = 0;

	public MapNewCommand()
	{
	}

	public MapNewCommand(MapMainFrame myMapFrame, ApplicationContext aContext)
	{
		this.mapFrame = myMapFrame;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MapNewCommand(mapFrame, aContext);
	}

	public void execute()
	{
		System.out.println("Creating new map context");
		aContext.getDispatcher().notify(new StatusMessageEvent("Новая топологическая схема"));
		mc = new MapContext( mapFrame.lnl());
		mc.setLongLat(mapFrame.lnl().viewer.getCenter()[0],
									mapFrame.lnl().viewer.getCenter()[1] );

		mc.id = aContext.getDataSourceInterface().GetUId("mapcontext");
		mc.domain_id = aContext.getSessionInterface().getDomainId();
		mc.user_id = aContext.getSessionInterface().getUserId();


		NewMapContextDialog dialog = new NewMapContextDialog( Environment.getActiveWindow(), "Свойства контекста", true, mc);
//		NewMapContextDialog dialog = new NewMapContextDialog( mapFrame.mapJFrame, "Свойства контекста", true, mc);

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
			retCode = 1;
			Pool.put( mc.getTyp(), mc.getId(), mc);
			if (mapFrame != null)
			{
				mapFrame.setMapContext(mc);
				mapFrame.setTitle( LangModelMap.String("AppTitle") + " - "
												 + mapFrame.lnl().getMapContext().name);
			}
			aContext.getDispatcher().notify(new StatusMessageEvent("Операция завершена"));
		}
		else
		{
			aContext.getDispatcher().notify(new StatusMessageEvent("Операция отменена"));
		}
	}

}