/*
 * $Id: MapImportCommand.java,v 1.1 2004/09/23 10:07:14 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.Map.ExportCommand;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс $RCSfile: MapImportCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/23 10:07:14 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapImportCommand extends ImportCommand
{
	/**
	 * окно карты
	 */
	MapFrame mapFrame;

	public MapImportCommand(MapFrame mapFrame)
	{
		super(null);
		this.mapFrame = mapFrame;
	}

	public void execute()
	{
		if(mapFrame == null)
			mapFrame = MapFrame.getMapMainFrame();
		if(mapFrame == null)
			return;
			
		ApplicationContext aContext = mapFrame.getContext();
		super.setDsi(aContext.getDataSourceInterface());
        System.out.println("Import map");

		Map map;
		String[][] exportColumns;

		String fileName = super.openFileForReading(MapPropertiesManager.getLastDirectory());
		if(fileName == null)
			return;
		MapPropertiesManager.setLastDirectory(new File(fileName).getParent());
		super.open(fileName);

		exportColumns = super.readObject();
		String type = exportColumns[0][0];
		if(!type.equals(Map.typ))
			return;

		MapElement me;
		List loadedObjects = new LinkedList();

		map = new Map();
		for (int i = 1; i < exportColumns.length; i++) 
		{
			map.setColumn(exportColumns[i][0], exportColumns[i][1]);
		}
		Pool.put(Map.typ, map.getId(), map);
		loadedObjects.add(map);

		map.setDomainId(aContext.getSessionInterface().getDomainId());
		map.setUserId(aContext.getSessionInterface().getUserId());

		while(true)
		{
			exportColumns = super.readObject();
			if(exportColumns == null)
				break;
			type = exportColumns[0][0];

			if(type.equals(MapMarkElement.typ))
				me = new MapMarkElement();
			else
			if(type.equals(MapNodeLinkElement.typ))
				me = new MapNodeLinkElement();
			else
			if(type.equals(MapPhysicalLinkElement.typ))
				me = new MapPhysicalLinkElement();
			else
			if(type.equals(MapPhysicalNodeElement.typ))
				me = new MapPhysicalNodeElement();
			else
			if(type.equals(MapPipePathElement.typ))
				me = new MapPipePathElement();
			else
			if(type.equals(MapSiteNodeElement.typ))
				me = new MapSiteNodeElement();
			else
				return;

			me.setMap(map);
			for (int i = 1; i < exportColumns.length; i++) 
			{
				me.setColumn(exportColumns[i][0], exportColumns[i][1]);
			}
			Pool.put(((ObjectResource )me).getTyp(), me.getId(), me);
			loadedObjects.add(me);

			if(type.equals(MapMarkElement.typ))
				map.addNode((MapMarkElement )me);
			else
			if(type.equals(MapNodeLinkElement.typ))
				map.addNodeLink((MapNodeLinkElement )me);
			else
			if(type.equals(MapPhysicalLinkElement.typ))
				map.addPhysicalLink((MapPhysicalLinkElement )me);
			else
			if(type.equals(MapPhysicalNodeElement.typ))
				map.addNode((MapPhysicalNodeElement )me);
			else
			if(type.equals(MapPipePathElement.typ))
				map.addCollector((MapPipePathElement )me);
			else
			if(type.equals(MapSiteNodeElement.typ))
				map.addNode((MapSiteNodeElement )me);
			else
				return;
		}

		super.close();

		for(Iterator it = loadedObjects.iterator(); it.hasNext();)
		{
			ObjectResource or = (ObjectResource )it.next();
			or.updateLocalFromTransferable();
		}

		MapView mv = mapFrame.getMapView();
		mv.removeSchemes();
		mv.setMap(map);
		mapFrame.setMapView(mv);

		setResult(Command.RESULT_OK);
	}

}
