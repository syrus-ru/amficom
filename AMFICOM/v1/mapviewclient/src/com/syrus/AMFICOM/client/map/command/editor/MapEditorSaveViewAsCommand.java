/*
 * $Id: MapEditorSaveViewAsCommand.java,v 1.7 2005/02/01 11:34:56 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewSaveAsCommand;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import java.util.Iterator;
import javax.swing.JDesktopPane;

/**
 * Класс $RCSfile: MapEditorSaveViewAsCommand.java,v $ используется для сохранения топологической схемы в модуле
 * "Редактор топологических схем" с новым именем. Использует команду
 * MapSaveAsCommand
 * 
 * @version $Revision: 1.7 $, $Date: 2005/02/01 11:34:56 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapSaveAsCommand
 */
public class MapEditorSaveViewAsCommand extends VoidCommand
{
	JDesktopPane desktop;
	ApplicationContext aContext;

	/**
	 * 
	 * @param mapFrame окно карты, из которого сохранять схему
	 * @param aContext контекст модуля "Редактор топологических схем"
	 */
	public MapEditorSaveViewAsCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}
	
	public void execute()
	{
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(desktop);

		if(mapFrame == null)
		{
			System.out.println("MapView SaveAs: map frame is null! Cannot complete operation.");
			setResult(Command.RESULT_NO);
			return;
		}
		MapViewSaveAsCommand mvsac = new MapViewSaveAsCommand(mapFrame.getMapView(), aContext);

		mvsac.execute();
		
		if(mvsac.getResult() == RESULT_OK)
		{
			MapView newMapView = mvsac.getNewMapView();
			MapView oldMapView = mapFrame.getMapView();
			
			MapViewController controller = mapFrame.getMapViewer()
				.getLogicalNetLayer().getMapViewController();

			if (mapFrame != null)
			{
				controller.setMapView(newMapView);
			
				controller.setMap(oldMapView.getMap());
				
				for(Iterator it = oldMapView.getSchemes().iterator(); it.hasNext();)
				{
					controller.addScheme((Scheme )it.next());
				}
		
				mapFrame.setTitle( 
					LangModelMap.getString("MapView") + " - " + newMapView.getName());
			}
			setResult(Command.RESULT_OK);
		}

	}

}
