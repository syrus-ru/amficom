/*
 * $Id: MapEditorSaveViewAsCommand.java,v 1.5 2005/01/21 13:49:27 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import java.util.Iterator;
import javax.swing.JDesktopPane;

/**
 * Класс $RCSfile: MapEditorSaveViewAsCommand.java,v $ используется для сохранения топологической схемы в модуле
 * "Редактор топологических схем" с новым именем. Использует команду
 * MapSaveAsCommand
 * 
 * @version $Revision: 1.5 $, $Date: 2005/01/21 13:49:27 $
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
			
			newMapView.setMap(mapFrame.getMapView().getMap());
			
			for(Iterator it = mapFrame.getMapView().getSchemes().iterator(); it.hasNext();)
			{
				newMapView.addScheme((Scheme )it.next());
			}
		
			if (mapFrame != null)
			{
				mapFrame.setMapView(newMapView);
				mapFrame.setTitle( 
					LangModelMap.getString("MapView") + " - " + newMapView.getName());
			}
			setResult(Command.RESULT_OK);
		}

	}

}
