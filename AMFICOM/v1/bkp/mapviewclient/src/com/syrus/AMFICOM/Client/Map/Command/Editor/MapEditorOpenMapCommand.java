/*
 * $Id: MapEditorOpenMapCommand.java,v 1.6 2004/12/28 17:35:12 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewCloseCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewNewCommand;
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
 * @version $Revision: 1.6 $, $Date: 2004/12/28 17:35:12 $
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

		MapOpenCommand moc = new MapOpenCommand(desktop, aContext);
		// в модуле редактирования топологических схем у пользователя есть
		// возможность удалять MapContext в окне управления схемами
		moc.setCanDelete(true);
		moc.execute();
		
		if (moc.getResult() == Command.RESULT_OK)
		{
			map = moc.getMap();

			MapFrame mapFrame = MapFrame.getMapMainFrame();
			if(mapFrame == null)
			{
				ViewMapWindowCommand mapCommand = new ViewMapWindowCommand(
					aContext.getDispatcher(), 
					desktop, 
					aContext, 
					new MapMapEditorApplicationModelFactory());

				mapCommand.execute();
				this.mapFrame = mapCommand.frame;
			}

			if(mapFrame == null)
				return;

			MapViewNewCommand cmd = new MapViewNewCommand(map, aContext);
			cmd.execute();

			MapView mapView = cmd.getMapView();

			mapFrame.setMapView(mapView);

			ViewMapPropertiesCommand propCommand = new ViewMapPropertiesCommand(desktop, aContext);
			propCommand.execute();
			this.propFrame = propCommand.frame;

			ViewMapElementsCommand elementsCommand = new ViewMapElementsCommand(desktop, aContext);
			elementsCommand.execute();
			this.elementsFrame = elementsCommand.frame;
		}
	}

	public MapFrame getMapFrame()
	{
		return mapFrame;
	}

	public MapPropertyFrame getPropertiesFrame()
	{
		return propFrame;
	}

	public MapElementsFrame getElementsFrame()
	{
		return elementsFrame;
	}

}
