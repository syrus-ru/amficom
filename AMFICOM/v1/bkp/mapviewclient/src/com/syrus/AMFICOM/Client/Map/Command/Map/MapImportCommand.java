/*
 * $Id: MapImportCommand.java,v 1.5 2004/10/11 16:48:33 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.ImportCommand;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Resource.ResourceUtil;
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
 * @version $Revision: 1.5 $, $Date: 2004/10/11 16:48:33 $
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
		super.setDsi(aContext.getDataSource());
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

		String field;
		String value;

		MapElement me;
		List loadedObjects = new LinkedList();

		map = new Map();
		for (int i = 1; i < exportColumns.length; i++) 
		{
			field = exportColumns[i][0];
			value = exportColumns[i][1];
			if(field.equals(Map.COLUMN_ID))
				value = super.getClonedId(Map.typ, value);
			map.setColumn(field, value);
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
				field = exportColumns[i][0];
				value = exportColumns[i][1];
				if(type.equals(MapMarkElement.typ))
				{
					if(field.equals(MapMarkElement.COLUMN_ID))
						value = super.getClonedId(MapMarkElement.typ, value);
					else
					if(field.equals(MapMarkElement.COLUMN_PHYSICAL_LINK_ID))
						value = super.getClonedId(MapPhysicalLinkElement.typ, value);
				}
				else
				if(type.equals(MapNodeLinkElement.typ))
				{
					if(field.equals(MapNodeLinkElement.COLUMN_ID))
						value = super.getClonedId(MapNodeLinkElement.typ, value);
					else
					if(field.equals(MapNodeLinkElement.COLUMN_PHYSICAL_LINK_ID))
						value = super.getClonedId(MapPhysicalLinkElement.typ, value);
					else
					if(field.equals(MapNodeLinkElement.COLUMN_START_NODE_ID))
						value = super.getClonedId(MapSiteNodeElement.typ, value);
					else
					if(field.equals(MapNodeLinkElement.COLUMN_END_NODE_ID))
						value = super.getClonedId(MapSiteNodeElement.typ, value);
				}
				else
				if(type.equals(MapPhysicalLinkElement.typ))
				{
					if(field.equals(MapPhysicalLinkElement.COLUMN_ID))
						value = super.getClonedId(MapPhysicalLinkElement.typ, value);
					else
					if(field.equals(MapPhysicalLinkElement.COLUMN_START_NODE_ID))
						value = super.getClonedId(MapSiteNodeElement.typ, value);
					else
					if(field.equals(MapPhysicalLinkElement.COLUMN_END_NODE_ID))
						value = super.getClonedId(MapSiteNodeElement.typ, value);
					else
					if(field.equals(MapPhysicalLinkElement.COLUMN_NODE_LINKS))
					{
						String val2 = "";
						for(Iterator it = ResourceUtil.parseStrings(value).iterator(); it.hasNext();)
						{
							val2 += super.getClonedId(MapNodeLinkElement.typ, (String )(it.next())) + " ";
						}
						value = val2;
					}
				}
				else
				if(type.equals(MapPhysicalNodeElement.typ))
				{
					if(field.equals(MapPhysicalNodeElement.COLUMN_ID))
						value = super.getClonedId(MapSiteNodeElement.typ, value);
					else
					if(field.equals(MapNodeLinkElement.COLUMN_PHYSICAL_LINK_ID))
						value = super.getClonedId(MapPhysicalLinkElement.typ, value);
				}
				else
				if(type.equals(MapPipePathElement.typ))
				{
					if(field.equals(MapPipePathElement.COLUMN_ID))
						value = super.getClonedId(MapPipePathElement.typ, value);
					if(field.equals(MapPipePathElement.COLUMN_LINKS))
					{
						String val2 = "";
						for(Iterator it = ResourceUtil.parseStrings(value).iterator(); it.hasNext();)
						{
							val2 += super.getClonedId(MapPhysicalLinkElement.typ, (String )(it.next())) + " ";
						}
						value = val2;
					}
				}
				else
				if(type.equals(MapSiteNodeElement.typ))
				{
					if(field.equals(MapSiteNodeElement.COLUMN_ID))
						value = super.getClonedId(MapSiteNodeElement.typ, value);
				}
				me.setColumn(field, value);
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
		
		Dispatcher disp = mapFrame.getContext().getDispatcher();
		if(disp != null)
			disp.notify(new MapEvent(mv, MapEvent.MAP_VIEW_SELECTED));

		setResult(Command.RESULT_OK);
	}

}
