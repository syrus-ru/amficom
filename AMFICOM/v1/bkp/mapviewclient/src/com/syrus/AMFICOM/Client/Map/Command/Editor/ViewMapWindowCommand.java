/**
 * $Id: ViewMapWindowCommand.java,v 1.11 2005/01/24 16:51:32 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapNewCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewNewCommand;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.map.Map;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

/**
 * Команда отображает окно карты 
 * 
 * 
 * 
 * @version $Revision: 1.11 $, $Date: 2005/01/24 16:51:32 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class ViewMapWindowCommand extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;
	ApplicationModelFactory factory;

	public MapFrame frame;

	public ViewMapWindowCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext, ApplicationModelFactory factory)
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

	public void execute()
	{
		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(factory.create());
		aC.setSessionInterface(aContext.getSessionInterface());
		aC.setDispatcher(dispatcher);

		frame = MapDesktopCommand.findMapFrame(desktop);
		
		if(frame == null)
		{
			frame = new MapFrame();
			desktop.add(frame);
			Dimension dim = desktop.getSize();
			frame.setLocation(0, 0);
			frame.setSize(dim.width * 4 / 5, dim.height * 7 / 8);
			
			setMapFrame(frame);
		}

		frame.setContext(aC);
		showMapFrame(frame);
		dispatcher.notify(new MapEvent(frame, MapEvent.MAP_FRAME_SHOWN));
		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
		setResult(Command.RESULT_OK);
	}

	protected void setMapFrame(MapFrame mapFrame)
	{
		MapView mapView = null;
		Map map = null;
		
		MapNewCommand mnc = new MapNewCommand(aContext);
		mnc.execute();
		if(mnc.getResult() == Command.RESULT_OK)
		{
			map = mnc.getMap();
		}
		else
			return;

		MapViewNewCommand mvnc = new MapViewNewCommand(map, aContext);
		mvnc.execute();
		if(mvnc.getResult() == Command.RESULT_OK)
		{
			mapView = mvnc.getMapView();
		}
		else
			return;
		
		mapView.setMap(map);

		mapView.setLogicalNetLayer(frame.getMapViewer().getLogicalNetLayer());

		frame.setMapView(mapView);
	}

	protected void showMapFrame(MapFrame mapFrame)
	{
		frame.setVisible(true);
	}

}
