/**
 * $Id: ViewMapWindowCommand.java,v 1.13 2005/02/08 15:11:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapNewCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewNewCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Команда отображает окно карты 
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
 */
public class ViewMapWindowCommand extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;
	ApplicationModelFactory factory;

	protected MapFrame mapFrame;

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
		aC.setApplicationModel(this.factory.create());
		aC.setSessionInterface(this.aContext.getSessionInterface());
		aC.setDispatcher(this.dispatcher);

		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		
		if(this.mapFrame == null)
		{
			this.mapFrame = new MapFrame();
			this.desktop.add(this.mapFrame);
			Dimension dim = this.desktop.getSize();
			this.mapFrame.setLocation(0, 0);
			this.mapFrame.setSize(dim.width * 4 / 5, dim.height * 7 / 8);
			
			setMapFrame();
		}

		this.mapFrame.setContext(aC);
		this.mapFrame.setVisible(true);
		this.dispatcher.notify(new MapEvent(this.mapFrame, MapEvent.MAP_FRAME_SHOWN));
		this.aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
		setResult(Command.RESULT_OK);
	}

	protected void setMapFrame()
	{
		MapView mapView = null;
		Map map = null;
		
		MapNewCommand mnc = new MapNewCommand(this.aContext);
		mnc.execute();
		if(mnc.getResult() == Command.RESULT_OK)
		{
			map = mnc.getMap();
		}
		else
			return;

		MapViewNewCommand mvnc = new MapViewNewCommand(map, this.aContext);
		mvnc.execute();
		if(mvnc.getResult() == Command.RESULT_OK)
		{
			mapView = mvnc.getMapView();
		}
		else
			return;
		
		mapView.setMap(map);

//		mapView.setLogicalNetLayer(frame.getMapViewer().getLogicalNetLayer());

		this.mapFrame.setMapView(mapView);
	}

}
