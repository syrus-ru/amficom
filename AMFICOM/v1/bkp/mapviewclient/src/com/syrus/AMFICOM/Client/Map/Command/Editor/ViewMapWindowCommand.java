/**
 * $Id: ViewMapWindowCommand.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.UI.MessageBox;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapNewCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewNewCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

/**
 * Команда отображает окно карты 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
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

	public ViewMapWindowCommand()
	{
	}

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

	public Object clone()
	{
		return new ViewMapWindowCommand(dispatcher, desktop, aContext, factory);
	}

	public void execute()
	{
		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(factory.create());
		aC.setConnectionInterface(aContext.getConnectionInterface());
		aC.setSessionInterface(aContext.getSessionInterface());
		aC.setDataSourceInterface(aC.getApplicationModel().getDataSource(aContext.getSessionInterface()));
		aC.setDispatcher(dispatcher);

		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if(dataSource == null)
			return;

		frame = com.syrus.AMFICOM.Client.Map.UI.MapFrame.getMapMainFrame();
		
		MapView mv = null;
		
		MapViewNewCommand command = new MapViewNewCommand(null, aContext);
		command.execute();
		if(command.getResult() == Command.RESULT_OK)
		{
			mv = command.mv;
		}
		MapNewCommand command1 = new MapNewCommand(null, aContext);
		command1.execute();
		if(command1.getResult() == Command.RESULT_OK)
		{
			mv.setMap(command1.mc);
		}

		if(frame.isVisible())
		{
			if(frame.getParent() != null)
			{
				if(frame.getParent().equals(desktop))
				{
					frame.setMapView(mv);
					frame.show();
					dispatcher.notify(new MapEvent(frame, MapEvent.MAP_FRAME_SHOWN));
					aContext.getDispatcher().notify(new StatusMessageEvent(
							StatusMessageEvent.STATUS_MESSAGE,
							LangModel.getString("Finished")));
					setResult(Command.RESULT_OK);
					return;
				}
				else
				{
					frame = null;
					MessageBox mb = new MessageBox(LangModelMap.getString("MapAlreadyOpened"));
					setResult(Command.RESULT_NO);
					return;
				}
			}
		}//if(frame.isVisible())

		JDesktopPane dt = (JDesktopPane )frame.getParent();
		if(dt != null)
		{
			dt.remove(frame);
		}
		desktop.add(frame);
		frame.setContext(aC);
		Dimension dim = desktop.getSize();
		frame.setLocation(0, 0);
		frame.setSize(dim.width * 4 / 5, dim.height * 7 / 8);
		frame.setMapView(mv);
		frame.show();
		dispatcher.notify(new MapEvent(frame, MapEvent.MAP_FRAME_SHOWN));
		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
		setResult(Command.RESULT_OK);
	}

}