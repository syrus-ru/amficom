/*
 * $Id: MapEditorOpenMapCommand.java,v 1.5 2004/12/22 16:38:40 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapOpenCommand;
import com.syrus.AMFICOM.Client.Map.Editor.MapEditorMainFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapElementsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapPropertyFrame;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import javax.swing.JDesktopPane;

/**
 * Класс $RCSfile: MapEditorOpenMapCommand.java,v $ используется для открытия топологической схемы в модуле
 * "Редактор топологических схем". Вызывается команда MapOpenCommand, и если 
 * пользователь выбрал MapContext, открывается окно карты и сопутствующие окна
 * и MapContext передается в окно карты
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/22 16:38:40 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapOpenCommand
 */
public class MapEditorOpenMapCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;

	MapFrame mapFrame = null;
	MapPropertyFrame propFrame = null;
	MapElementsFrame elementsFrame = null;
	
	Map map = null;
	MapView mapView = null;

	public MapEditorOpenMapCommand()
	{
	}

	/**
	 * 
	 * @param desktop куда класть окно карты
	 * @param aContext Контекст модуля "Редактор топологических схем"
	 */
	public MapEditorOpenMapCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		if(mapFrame.getMapMainFrame() != null)
		{
			if(!mapFrame.getMapMainFrame().checkCanCloseMap())
				return;
			if(!mapFrame.getMapMainFrame().checkCanCloseMapView())
				return;
		}

		ApplicationModelFactory factory = new MapMapEditorApplicationModelFactory();

		MapOpenCommand moc = new MapOpenCommand(desktop, MapFrame.getMapMainFrame(), aContext);
		// в модуле редактирования топологических схем у пользователя есть
		// возможность удалять MapContext в окне управления схемами
		moc.setCanDelete(true);
		moc.execute();
		
		if (moc.getResult() == Command.RESULT_OK)
		{
			map = (Map)moc.getReturnObject();
			if(MapFrame.getMapMainFrame() != null)
				mapView = MapFrame.getMapMainFrame().getMapView();

			ViewMapWindowCommand mapCommand = new ViewMapWindowCommand(aContext.getDispatcher(), desktop, aContext, factory);
			mapCommand.execute();
			this.mapFrame = mapCommand.frame;

			if(mapFrame == null)
				return;
				
			if(mapView != null)
				mapFrame.setMapView(mapView);
			else
				mapFrame.getMapView().setMap(map);

			ViewMapPropertiesCommand propCommand = new ViewMapPropertiesCommand(desktop, aContext);
			propCommand.execute();
			this.propFrame = propCommand.frame;

			ViewMapElementsCommand elementsCommand = new ViewMapElementsCommand(desktop, aContext);
			elementsCommand.execute();
			this.elementsFrame = elementsCommand.frame;
		}
	}

}
