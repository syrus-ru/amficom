/*
 * $Id: MapEditorSaveViewAsCommand.java,v 1.4 2004/12/28 17:35:12 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import java.util.Iterator;

/**
 * Класс $RCSfile: MapEditorSaveViewAsCommand.java,v $ используется для сохранения топологической схемы в модуле
 * "Редактор топологических схем" с новым именем. Использует команду
 * MapSaveAsCommand
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/28 17:35:12 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapSaveAsCommand
 */
public class MapEditorSaveViewAsCommand extends VoidCommand
{
	MapFrame mapFrame;
	ApplicationContext aContext;

	/**
	 * 
	 * @param mapFrame окно карты, из которого сохранять схему
	 * @param aContext контекст модуля "Редактор топологических схем"
	 */
	public MapEditorSaveViewAsCommand(MapFrame mapFrame, ApplicationContext aContext)
	{
		this.mapFrame = mapFrame;
		this.aContext = aContext;
	}
	
	public void setParameter(String param, Object val)
	{
		if(param.equals("mapFrame"))
			this.mapFrame = (MapFrame )val;
	}

	public void execute()
	{
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
