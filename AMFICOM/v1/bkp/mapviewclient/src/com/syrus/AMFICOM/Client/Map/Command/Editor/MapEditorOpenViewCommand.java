/*
 * $Id: MapEditorOpenViewCommand.java,v 1.8 2004/10/26 13:32:01 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewOpenCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapElementsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapPropertyFrame;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import javax.swing.JDesktopPane;

/**
 * Класс $RCSfile: MapEditorOpenViewCommand.java,v $ используется для открытия топологической схемы в модуле
 * "Редактор топологических схем". Вызывается команда MapOpenCommand, и если 
 * пользователь выбрал MapContext, открывается окно карты и сопутствующие окна
 * и MapContext передается в окно карты
 * 
 * @version $Revision: 1.8 $, $Date: 2004/10/26 13:32:01 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapOpenCommand
 */
public class MapEditorOpenViewCommand extends VoidCommand
{
	protected ApplicationContext aContext;
	protected JDesktopPane desktop;
	
	MapFrame mapFrame = null;
	MapPropertyFrame propFrame = null;
	MapElementsFrame elementsFrame = null;
	
	MapView mapView = null;

	public MapEditorOpenViewCommand()
	{
	}

	/**
	 * 
	 * @param desktop куда класть окно карты
	 * @param aContext Контекст модуля "Редактор топологических схем"
	 */
	public MapEditorOpenViewCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{

		if(!MapFrame.getMapMainFrame().checkCanCloseMap())
			return;
		if(!MapFrame.getMapMainFrame().checkCanCloseMapView())
			return;

		ApplicationModelFactory factory = new MapMapEditorApplicationModelFactory();

		MapViewOpenCommand moc = new MapViewOpenCommand(desktop, MapFrame.getMapMainFrame(), aContext);
		// в модуле редактирования топологических схем у пользователя есть
		// возможность удалять MapContext в окне управления схемами
		moc.setCanDelete(true);
		moc.execute();
		if (moc.getResult() == Command.RESULT_OK)
		{
			mapView = (MapView )moc.getReturnObject();
		
			ViewMapWindowCommand mapCommand = new ViewMapWindowCommand(aContext.getDispatcher(), desktop, aContext, factory);
			mapCommand.execute();
			this.mapFrame = mapCommand.frame;

			if(mapFrame == null)
				return;

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
