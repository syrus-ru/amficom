/*
 * $Id: MapEditorOpenMapCommand.java,v 1.10 2005/02/08 15:11:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapOpenCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewNewCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapElementsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapPropertyFrame;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Класс $RCSfile: MapEditorOpenMapCommand.java,v $ используется для открытия топологической схемы в модуле
 * "Редактор топологических схем". Вызывается команда MapOpenCommand, и если 
 * пользователь выбрал MapContext, открывается окно карты и сопутствующие окна
 * и MapContext передается в окно карты
 * 
 * @version $Revision: 1.10 $, $Date: 2005/02/08 15:11:10 $
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
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		if(this.mapFrame != null)
		{
			if(!this.mapFrame.checkCanCloseMap())
				return;
			if(!this.mapFrame.checkCanCloseMapView())
				return;
		}

		MapOpenCommand mapOpenCommand = new MapOpenCommand(this.desktop, this.aContext);
		// в модуле редактирования топологических схем у пользователя есть
		// возможность удалять MapContext в окне управления схемами
		mapOpenCommand.setCanDelete(true);
		mapOpenCommand.execute();
		
		if (mapOpenCommand.getResult() == Command.RESULT_OK)
		{
			this.map = mapOpenCommand.getMap();

			if(this.mapFrame == null)
			{
				ViewMapWindowCommand mapCommand = new ViewMapWindowCommand(
					this.aContext.getDispatcher(), 
					this.desktop, 
					this.aContext, 
					new MapMapEditorApplicationModelFactory());

				mapCommand.execute();
				this.mapFrame = mapCommand.mapFrame;
			}

			if(this.mapFrame == null)
				return;

			MapViewNewCommand cmd = new MapViewNewCommand(this.map, this.aContext);
			cmd.execute();

			this.mapView = cmd.getMapView();

			this.mapFrame.setMapView(this.mapView);

			ViewMapPropertiesCommand propCommand = new ViewMapPropertiesCommand(this.desktop, this.aContext);
			propCommand.execute();
			this.propFrame = propCommand.frame;

			ViewMapElementsCommand elementsCommand = new ViewMapElementsCommand(this.desktop, this.aContext);
			elementsCommand.execute();
			this.elementsFrame = elementsCommand.frame;
		}
	}

	public MapFrame getMapFrame()
	{
		return this.mapFrame;
	}

	public MapPropertyFrame getPropertiesFrame()
	{
		return this.propFrame;
	}

	public MapElementsFrame getElementsFrame()
	{
		return this.elementsFrame;
	}

}
