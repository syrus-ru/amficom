/*
 * $Id: MapSaveAsCommand.java,v 1.3 2004/06/28 11:47:51 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.MapMainFrame;
import com.syrus.AMFICOM.Client.Map.UI.NewMapContextDialog;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDesktopPane;

/**
 * Класс $RCSfile: MapSaveAsCommand.java,v $ используется для сохранения топологической схемы с новым
 * именем
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/06/28 11:47:51 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapSaveAsCommand extends VoidCommand
{
	MapMainFrame mapFrame;
    ApplicationContext aContext;

	public MapSaveAsCommand()
	{
	}

	/**
	 * 
	 * @param paramName comments
	 * @exception Exception comments
	 */
	public MapSaveAsCommand(MapMainFrame myMapFrame, ApplicationContext aContext)
	{
		this.mapFrame = myMapFrame;
		this.aContext = aContext;
	}

	/**
	 * @deprecated
	 */
	public MapSaveAsCommand(JDesktopPane desktop, MapMainFrame myMapFrame, ApplicationContext aContext)
	{
		this(myMapFrame, aContext);
	}

	public Object clone()
	{
		return new MapSaveAsCommand(mapFrame, aContext);
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

		aContext.getDispatcher().notify(new StatusMessageEvent("Сохранение топологической схемы..."));

		NewMapContextDialog dialog = new NewMapContextDialog(mapFrame.mapJFrame, "Свойства контекста", true, mc2);

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
					aContext.getDispatcher().notify(new StatusMessageEvent("Схемы несовместимы"));
					return;
				}
				String new_scheme_id = (String )scheme.clones.get(mc.scheme_id);
				if(new_scheme_id == null)
				{
					aContext.getDispatcher().notify(new StatusMessageEvent("Схемы несовместимы"));
					return;
				}
				if(!new_scheme_id.equals(scheme.id))
				{
					aContext.getDispatcher().notify(new StatusMessageEvent("Схема сохранена с ошибкой"));
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
			aContext.getDispatcher().notify(new StatusMessageEvent("Схема успешно сохранена"));
		}
		else
		{
			aContext.getDispatcher().notify(new StatusMessageEvent("Операция отменена"));
		}
	}

}