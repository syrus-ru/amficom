/*
 * $Id: MapImportCommand.java,v 1.10 2004/12/22 16:38:40 krupenn Exp $
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
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.User;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.SiteNode;
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
 * @version $Revision: 1.10 $, $Date: 2004/12/22 16:38:40 $
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
/*			
		ApplicationContext aContext = mapFrame.getContext();

		try
		{
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
	
			Identifier userId = new Identifier(
				aContext.getSessionInterface().getAccessIdentifier().user_id);

			Identifier domainId = new Identifier(
				aContext.getSessionInterface().getAccessIdentifier().domain_id);
	
			map = Map.createInstance(userId, "", "");
			map.setDomainId(domainId);
	
			for (int i = 1; i < exportColumns.length; i++) 
			{
				field = exportColumns[i][0];
				value = exportColumns[i][1];
				if(field.equals(com.syrus.AMFICOM.map.Map.COLUMN_ID))
					value = super.getClonedId(MAP_TYPE, value);
				map.setColumn(field, value);
			}

			MapStorableObjectPool.putStorableObject(map);
	
			while(true)
			{
				exportColumns = super.readObject();
				if(exportColumns == null)
					break;
				type = exportColumns[0][0];
	
				if(type.equals(MARK_TYPE))
					me = new Mark();
				else
				if(type.equals(NODELINK_TYPE))
					me = new NodeLink();
				else
				if(type.equals(LINK_TYPE))
					me = new PhysicalLink();
				else
				if(type.equals(NODE_TYPE))
					me = new TopologicalNode();
				else
				if(type.equals(COLLECTOR_TYPE))
					me = new Collector();
				else
				if(type.equals(SITE_TYPE))
					me = new SiteNode();
				else
					return;
	
				me.setMap(map);
				for (int i = 1; i < exportColumns.length; i++) 
				{
					field = exportColumns[i][0];
					value = exportColumns[i][1];
					if(type.equals(MARK_TYPE))
					{
						if(field.equals(com.syrus.AMFICOM.map.Mark.COLUMN_ID))
							value = super.getClonedId(MARK_TYPE, value);
						else
						if(field.equals(com.syrus.AMFICOM.map.Mark.COLUMN_PHYSICAL_LINK_ID))
							value = super.getClonedId(LINK_TYPE, value);
					}
					else
					if(type.equals(NODELINK_TYPE))
					{
						if(field.equals(com.syrus.AMFICOM.map.NodeLink.COLUMN_ID))
							value = super.getClonedId(NODELINK_TYPE, value);
						else
						if(field.equals(com.syrus.AMFICOM.map.NodeLink.COLUMN_PHYSICAL_LINK_ID))
							value = super.getClonedId(LINK_TYPE, value);
						else
						if(field.equals(com.syrus.AMFICOM.map.NodeLink.COLUMN_START_NODE_ID))
							value = super.getClonedId(SITE_TYPE, value);
						else
						if(field.equals(com.syrus.AMFICOM.map.NodeLink.COLUMN_END_NODE_ID))
							value = super.getClonedId(SITE_TYPE, value);
					}
					else
					if(type.equals(LINK_TYPE))
					{
						if(field.equals(com.syrus.AMFICOM.map.PhysicalLink.COLUMN_ID))
							value = super.getClonedId(LINK_TYPE, value);
						else
						if(field.equals(com.syrus.AMFICOM.map.PhysicalLink.COLUMN_START_NODE_ID))
							value = super.getClonedId(SITE_TYPE, value);
						else
						if(field.equals(com.syrus.AMFICOM.map.PhysicalLink.COLUMN_END_NODE_ID))
							value = super.getClonedId(SITE_TYPE, value);
						else
						if(field.equals(com.syrus.AMFICOM.map.PhysicalLink.COLUMN_NODE_LINKS))
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
						if(field.equals(com.syrus.AMFICOM.map.TopologicalNode.COLUMN_ID))
							value = super.getClonedId(SITE_TYPE, value);
						else
						if(field.equals(com.syrus.AMFICOM.map.NodeLink.COLUMN_PHYSICAL_LINK_ID))
							value = super.getClonedId(LINK_TYPE, value);
					}
					else
					if(type.equals(COLLECTOR_TYPE))
					{
						if(field.equals(com.syrus.AMFICOM.map.Collector.COLUMN_ID))
							value = super.getClonedId(COLLECTOR_TYPE, value);
						if(field.equals(com.syrus.AMFICOM.map.Collector.COLUMN_LINKS))
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
						if(field.equals(com.syrus.AMFICOM.map.SiteNode.COLUMN_ID))
							value = super.getClonedId(SITE_TYPE, value);
					}
					me.setColumn(field, value);
				}
	
				if(type.equals(MARK_TYPE))
					map.addNode((Mark )me);
				else
				if(type.equals(NODELINK_TYPE))
					map.addNodeLink((NodeLink )me);
				else
				if(type.equals(LINK_TYPE))
					map.addPhysicalLink((PhysicalLink )me);
				else
				if(type.equals(NODE_TYPE))
					map.addNode((TopologicalNode )me);
				else
				if(type.equals(COLLECTOR_TYPE))
					map.addCollector((Collector )me);
				else
				if(type.equals(SITE_TYPE))
					map.addNode((SiteNode )me);
				else
					return;
			}
	
			super.close();
	
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
		catch (CommunicationException e)
		{
			e.printStackTrace();
			setResult(Command.RESULT_NO);
		}
		catch (DatabaseException e)
		{
			e.printStackTrace();
			setResult(Command.RESULT_NO);
		}
		catch (IllegalObjectEntityException e)
		{
			e.printStackTrace();
			setResult(Command.RESULT_NO);
		}
*/
	}

}
