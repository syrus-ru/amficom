/*
 * $Id: MapImportCommand.java,v 1.20 2005/02/07 16:09:25 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.ImportCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeTypeController;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.TopologicalNodeWrapper;

import java.io.File;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDesktopPane;

/**
 * Класс $RCSfile: MapImportCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @version $Revision: 1.20 $, $Date: 2005/02/07 16:09:25 $
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

	JDesktopPane desktop;
	ApplicationContext aContext;

	/**
	 * окно карты
	 */
	MapFrame mapFrame;

	public MapImportCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		super();
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		mapFrame = MapDesktopCommand.findMapFrame(desktop);

		if(mapFrame == null)
			return;
			
		try
		{
			Map map = null;
			System.out.println("Import map");
	
			String fileName = ImportCommand.openFileForReading(MapPropertiesManager.getLastDirectory());
			if(fileName == null)
				return;

			File file = new File(fileName);
			MapPropertiesManager.setLastDirectory(file.getParent());

			String ext = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
	
			if(ext == null)
			{
				ext = ".xml";
			}
			
			if(ext.equals(".xml"))
			{
				map = loadXML(file);
			}
			else
			if(ext.equals(".esf"))
			{
				map = loadESF(fileName);
			}	

			if(map == null)
				return;

			MapStorableObjectPool.putStorableObject(map);

			MapView mv = mapFrame.getMapView();
			mapFrame.getMapViewer().getLogicalNetLayer().getMapViewController().removeSchemes();
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
	}

	protected Map loadXML(File file)
	{
		return null;
	}

	protected Map loadESF(String fileName)
		throws DatabaseException,IllegalObjectEntityException
	{
		Map map;
		MapElement me;
		ImportCommand.ImportObject importObject;
		String type;
		java.util.Map exportColumns;

		// make sure default types loaded
		LinkTypeController.getPens(aContext);
		NodeTypeController.getTopologicalProtos(aContext);

		super.open(fileName);

		importObject = super.readObject();
		if(importObject == null)
			return null;
		type = importObject.type;
		exportColumns = importObject.exportColumns;

		if(!type.equals(MAP_TYPE))
			return null;

		correctCrossLinks(type, exportColumns);

		Identifier userId = new Identifier(
			aContext.getSessionInterface().getAccessIdentifier().user_id);

		Identifier domainId = new Identifier(
			aContext.getSessionInterface().getAccessIdentifier().domain_id);

		map = Map.createInstance(userId, domainId, exportColumns);

		while(true)
		{
			importObject = super.readObject();
			if(importObject == null)
				break;
			type = importObject.type;
			exportColumns = importObject.exportColumns;

			correctCrossLinks(type, exportColumns);

			if(type.equals(MARK_TYPE))
			{
				me = Mark.createInstance(userId, exportColumns);
				map.addNode((Mark )me);
			}
			else
			if(type.equals(NODELINK_TYPE))
			{
				me = NodeLink.createInstance(userId, exportColumns);
				map.addNodeLink((NodeLink )me);
			}
			else
			if(type.equals(LINK_TYPE))
			{
				me = PhysicalLink.createInstance(userId, exportColumns);
				map.addPhysicalLink((PhysicalLink )me);
			}
			else
			if(type.equals(NODE_TYPE))
			{
				me = TopologicalNode.createInstance(userId, exportColumns);
				map.addNode((TopologicalNode )me);
			}
			else
			if(type.equals(COLLECTOR_TYPE))
			{
				me = Collector.createInstance(userId, exportColumns);
				map.addCollector((Collector )me);
			}
			else
			if(type.equals(SITE_TYPE))
			{
				me = SiteNode.createInstance(userId, exportColumns);
				map.addNode((SiteNode )me);
			}
			else
				return null;

			me.setMap(map);

			MapStorableObjectPool.putStorableObject((StorableObject )me);
		}

		super.close();

		return map;
	}

	private void correctCrossLinks(String type, java.util.Map exportColumns)
		throws IllegalObjectEntityException
	{
		Object field;
		Object value;

		for(Iterator it = exportColumns.keySet().iterator(); it.hasNext();)
		{
			field = it.next();
			value = exportColumns.get(field);

			if(type.equals(MAP_TYPE))
			{
				if(field.equals(Map.COLUMN_ID))
					value = super.getClonedId(ObjectEntities.MAP_ENTITY_CODE, (String )value);
			}
			else
			if(type.equals(MARK_TYPE))
			{
				if(field.equals(Mark.COLUMN_ID))
					value = super.getClonedId(ObjectEntities.MARK_ENTITY_CODE, (String )value);
				else
				if(field.equals(Mark.COLUMN_PHYSICAL_LINK_ID))
					value = super.getClonedId(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE, (String )value);
			}
			else
			if(type.equals(NODELINK_TYPE))
			{
				if(field.equals(NodeLink.COLUMN_ID))
					value = super.getClonedId(ObjectEntities.NODE_LINK_ENTITY_CODE, (String )value);
				else
				if(field.equals(NodeLink.COLUMN_PHYSICAL_LINK_ID))
					value = super.getClonedId(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE, (String )value);
				else
				if(field.equals(NodeLink.COLUMN_START_NODE_ID))
					value = super.getClonedId(ObjectEntities.SITE_NODE_ENTITY_CODE, (String )value);
				else
				if(field.equals(NodeLink.COLUMN_END_NODE_ID))
					value = super.getClonedId(ObjectEntities.SITE_NODE_ENTITY_CODE, (String )value);
			}
			else
			if(type.equals(LINK_TYPE))
			{
				if(field.equals(PhysicalLink.COLUMN_ID))
					value = super.getClonedId(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE, (String )value);
				else
				if(field.equals(PhysicalLink.COLUMN_START_NODE_ID))
					value = super.getClonedId(ObjectEntities.SITE_NODE_ENTITY_CODE, (String )value);
				else
				if(field.equals(PhysicalLink.COLUMN_END_NODE_ID))
					value = super.getClonedId(ObjectEntities.SITE_NODE_ENTITY_CODE, (String )value);
				else
				if(field.equals(PhysicalLink.COLUMN_NODE_LINKS))
				{
					List list = (List )value;
					List newList = new ArrayList(list.size());
					for(Iterator it2 = list.iterator(); it2.hasNext();)
					{
						String id = (String )it2.next();
						newList.add(super.getClonedId(ObjectEntities.NODE_LINK_ENTITY_CODE, id));
					}
					value = newList;
				}
			}
			else
			if(type.equals(NODE_TYPE))
			{
				if(field.equals(StorableObjectWrapper.COLUMN_ID))
					value = super.getClonedId(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE, (String )value);
				else
				if(field.equals(TopologicalNodeWrapper.COLUMN_PHYSICAL_LINK_ID))
					value = super.getClonedId(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE, (String )value);
			}
			else
			if(type.equals(COLLECTOR_TYPE))
			{
				if(field.equals(Collector.COLUMN_ID))
					value = super.getClonedId(ObjectEntities.COLLECTOR_ENTITY_CODE, (String )value);
				if(field.equals(Collector.COLUMN_LINKS))
				{
					List list = (List )value;
					List newList = new ArrayList(list.size());
					for(Iterator it2 = list.iterator(); it2.hasNext();)
					{
						String id = (String )it2.next();
						newList.add(super.getClonedId(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE, id));
					}
					value = newList;
				}
			}
			else
			if(type.equals(SITE_TYPE))
			{
				if(field.equals(SiteNode.COLUMN_ID))
					value = super.getClonedId(ObjectEntities.SITE_NODE_ENTITY_CODE, (String )value);
			}

			exportColumns.put(field, value);
		}	
	}

}
