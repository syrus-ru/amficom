/*
 * $Id: MapImportCommand.java,v 1.9 2004/12/07 17:05:54 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Command.ImportCommand;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
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
 * @version $Revision: 1.9 $, $Date: 2004/12/07 17:05:54 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapImportCommand extends ImportCommand
{
	public static final String MAP_TYPE = "map";
	public static final String MARK_TYPE = "mapmarkelement";
	public static final String SITE_TYPE = "mapsiteelement";
	public static final String NODE_TYPE = "mapnodeelement";
	public static final String NODELINK_TYPE = "mapnodelinkelement";
	public static final String COLLECTOR_TYPE = "mappipepathelement";
	public static final String LINK_TYPE = "maplinkelement";
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
		if(!type.equals(MAP_TYPE))
			return;

		String field;
		String value;

		MapElement me;
//		List loadedObjects = new LinkedList();

		map = new Map();
		for (int i = 1; i < exportColumns.length; i++) 
		{
			field = exportColumns[i][0];
			value = exportColumns[i][1];
			if(field.equals(Map.COLUMN_ID))
				value = super.getClonedId(MAP_TYPE, value);
			map.setColumn(field, value);
		}
		Pool.put(MAP_TYPE, map.getId(), map);
//		loadedObjects.add(map);

		map.setDomainId(aContext.getSessionInterface().getDomainId());
		map.setUserId(aContext.getSessionInterface().getUserId());

		while(true)
		{
			exportColumns = super.readObject();
			if(exportColumns == null)
				break;
			type = exportColumns[0][0];

			if(type.equals(MARK_TYPE))
				me = new MapMarkElement();
			else
			if(type.equals(NODELINK_TYPE))
				me = new MapNodeLinkElement();
			else
			if(type.equals(LINK_TYPE))
				me = new MapPhysicalLinkElement();
			else
			if(type.equals(NODE_TYPE))
				me = new MapPhysicalNodeElement();
			else
			if(type.equals(COLLECTOR_TYPE))
				me = new MapPipePathElement();
			else
			if(type.equals(SITE_TYPE))
				me = new MapSiteNodeElement();
			else
				return;

			me.setMap(map);
			for (int i = 1; i < exportColumns.length; i++) 
			{
				field = exportColumns[i][0];
				value = exportColumns[i][1];
				if(type.equals(MARK_TYPE))
				{
					if(field.equals(MapMarkElement.COLUMN_ID))
						value = super.getClonedId(MARK_TYPE, value);
					else
					if(field.equals(MapMarkElement.COLUMN_PHYSICAL_LINK_ID))
						value = super.getClonedId(LINK_TYPE, value);
				}
				else
				if(type.equals(NODELINK_TYPE))
				{
					if(field.equals(MapNodeLinkElement.COLUMN_ID))
						value = super.getClonedId(NODELINK_TYPE, value);
					else
					if(field.equals(MapNodeLinkElement.COLUMN_PHYSICAL_LINK_ID))
						value = super.getClonedId(LINK_TYPE, value);
					else
					if(field.equals(MapNodeLinkElement.COLUMN_START_NODE_ID))
						value = super.getClonedId(SITE_TYPE, value);
					else
					if(field.equals(MapNodeLinkElement.COLUMN_END_NODE_ID))
						value = super.getClonedId(SITE_TYPE, value);
				}
				else
				if(type.equals(LINK_TYPE))
				{
					if(field.equals(MapPhysicalLinkElement.COLUMN_ID))
						value = super.getClonedId(LINK_TYPE, value);
					else
					if(field.equals(MapPhysicalLinkElement.COLUMN_START_NODE_ID))
						value = super.getClonedId(SITE_TYPE, value);
					else
					if(field.equals(MapPhysicalLinkElement.COLUMN_END_NODE_ID))
						value = super.getClonedId(SITE_TYPE, value);
					else
					if(field.equals(MapPhysicalLinkElement.COLUMN_NODE_LINKS))
					{
						String val2 = "";
						for(Iterator it = ResourceUtil.parseStrings(value).iterator(); it.hasNext();)
						{
							val2 += super.getClonedId(NODELINK_TYPE, (String )(it.next())) + " ";
						}
						value = val2;
					}
				}
				else
				if(type.equals(NODE_TYPE))
				{
					if(field.equals(MapPhysicalNodeElement.COLUMN_ID))
						value = super.getClonedId(SITE_TYPE, value);
					else
					if(field.equals(MapNodeLinkElement.COLUMN_PHYSICAL_LINK_ID))
						value = super.getClonedId(LINK_TYPE, value);
				}
				else
				if(type.equals(COLLECTOR_TYPE))
				{
					if(field.equals(MapPipePathElement.COLUMN_ID))
						value = super.getClonedId(COLLECTOR_TYPE, value);
					if(field.equals(MapPipePathElement.COLUMN_LINKS))
					{
						String val2 = "";
						for(Iterator it = ResourceUtil.parseStrings(value).iterator(); it.hasNext();)
						{
							val2 += super.getClonedId(LINK_TYPE, (String )(it.next())) + " ";
						}
						value = val2;
					}
				}
				else
				if(type.equals(SITE_TYPE))
				{
					if(field.equals(MapSiteNodeElement.COLUMN_ID))
						value = super.getClonedId(SITE_TYPE, value);
				}
				me.setColumn(field, value);
			}
			Pool.put(((ObjectResource )me).getTyp(), me.getId(), me);
//			loadedObjects.add(me);

			if(type.equals(MARK_TYPE))
				map.addNode((MapMarkElement )me);
			else
			if(type.equals(NODELINK_TYPE))
				map.addNodeLink((MapNodeLinkElement )me);
			else
			if(type.equals(LINK_TYPE))
				map.addPhysicalLink((MapPhysicalLinkElement )me);
			else
			if(type.equals(NODE_TYPE))
				map.addNode((MapPhysicalNodeElement )me);
			else
			if(type.equals(COLLECTOR_TYPE))
				map.addCollector((MapPipePathElement )me);
			else
			if(type.equals(SITE_TYPE))
				map.addNode((MapSiteNodeElement )me);
			else
				return;
		}

		super.close();

		map.updateFromPool();

//		for(Iterator it = loadedObjects.iterator(); it.hasNext();)
//		{
//			ObjectResource or = (ObjectResource )it.next();
//			or.updateLocalFromTransferable();
//		}

		MapView mv = mapFrame.getMapView();
		mv.removeSchemes();
		mv.setMap(map);
		mapFrame.setMapView(mv);
		
		Dispatcher disp = mapFrame.getContext().getDispatcher();
		if(disp != null)
		{
			disp.notify(new MapEvent(mv, MapEvent.MAP_VIEW_CHANGED));
			disp.notify(new MapEvent(mv, MapEvent.MAP_VIEW_SELECTED));
		}

		setResult(Command.RESULT_OK);
	}

}
