/*
 * $Id: MapExportCommand.java,v 1.1.2.1 2004/09/24 10:55:35 krupenn Exp $
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

import com.syrus.AMFICOM.Client.Map.Command.*;
import com.syrus.AMFICOM.Client.Map.MapMainFrame;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import java.io.File;
import java.util.Iterator;

/**
 * Класс $RCSfile: MapExportCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @version $Revision: 1.1.2.1 $, $Date: 2004/09/24 10:55:35 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapExportCommand extends ExportCommand
{
	/**
	 * окно карты
	 */
	public MapExportCommand()
	{
		super(null);
	}

	public void execute()
	{
		MapMainFrame mapFrame = (MapMainFrame )Pool.get("environment", "mapmainframe");

        System.out.println("Export map");

		MapContext map = mapFrame.getMapContext();
		String[][] exportColumns;
		
		String fileName = super.openFileForWriting("");
		if(fileName == null)
			return;
		super.open(fileName);
		
		super.startObject(getTyp(map));
		exportColumns = getExportColumns(map);
		for (int i = 0; i < exportColumns.length; i++) 
		{
			super.put(exportColumns[i][0], exportColumns[i][1]);
		}
		super.endObject();

		for(Iterator it = map.getAllElements().iterator(); it.hasNext();)
		{
			MapElement me = (MapElement )it.next();
			exportColumns = getExportColumns(me);
			if(exportColumns != null)
			{
				super.startObject(getTyp(me));
				for (int i = 0; i < exportColumns.length; i++) 
				{
					super.put(exportColumns[i][0], exportColumns[i][1]);
				}
				super.endObject();
			}
		}
		
		super.close();

		setResult(Command.RESULT_OK);
	}

	private String getTyp(Object obj)
	{
		String res = "";
		
		if(obj instanceof MapContext)
			res = "map";
		else
		if(obj instanceof MapPhysicalNodeElement)
			res = "mapnodeelement";
		else
		if(obj instanceof MapNodeLinkElement)
			res = "mapnodelinkelement";
		else
		if(obj instanceof MapEquipmentNodeElement)
			res = "mapsiteelement";
		else
		if(obj instanceof MapPhysicalLinkElement)
			res = "maplinkelement";
		return res;
	}

	private String[][] getExportColumns(Object obj)
	{
		String[][] res = null;
		
		if(obj instanceof MapContext)
			res = getMapContextExportColumns((MapContext )obj);
		else
		if(obj instanceof MapPhysicalNodeElement)
			res = getMapNodeExportColumns((MapPhysicalNodeElement )obj);
		else
		if(obj instanceof MapNodeLinkElement)
			res = getMapNodeLinkExportColumns((MapNodeLinkElement )obj);
		else
		if(obj instanceof MapEquipmentNodeElement)
			res = getMapEquipmentExportColumns((MapEquipmentNodeElement )obj);
		else
		if(obj instanceof MapPhysicalLinkElement)
			res = getMapLinkExportColumns((MapPhysicalLinkElement )obj);
		return res;
	}

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_CREATED = "created";
	public static final String COLUMN_CREATED_BY = "created_by";
	public static final String COLUMN_MODIFIED = "modified";
	public static final String COLUMN_MODIFIED_BY = "modified_by";
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";	
	public static final String COLUMN_START_NODE_ID = "start_node_id";	
	public static final String COLUMN_END_NODE_ID = "end_node_id";	
	public static final String COLUMN_PROTO_ID = "proto_id";	
	public static final String COLUMN_NODE_LINKS = "node_links";	
	public static final String COLUMN_CITY = "city";	
	public static final String COLUMN_STREET = "street";	
	public static final String COLUMN_BUILDING = "building";	
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	public static final String COLUMN_ACTIVE = "active";
	public static final String COLUMN_COEF = "coef";
	public static final String COLUMN_IMAGE_ID = "image_id";

	private String[][] getMapContextExportColumns(MapContext map)
	{
		String[][] exportColumns = new String[3][2];
		exportColumns[0][0] = COLUMN_ID;
		exportColumns[1][0] = COLUMN_NAME;
		exportColumns[2][0] = COLUMN_DESCRIPTION;

		exportColumns[0][1] = map.getId();
		exportColumns[1][1] = map.getName();
		exportColumns[2][1] = map.description;
		return exportColumns;
	}

	private String[][] getMapNodeExportColumns(MapPhysicalNodeElement node)
	{
		String[][] exportColumns = new String[7][2];
		exportColumns[0][0] = COLUMN_ID;
		exportColumns[1][0] = COLUMN_NAME;
		exportColumns[2][0] = COLUMN_DESCRIPTION;
		exportColumns[3][0] = COLUMN_PHYSICAL_LINK_ID;
		exportColumns[4][0] = COLUMN_X;
		exportColumns[5][0] = COLUMN_Y;
		exportColumns[6][0] = COLUMN_ACTIVE;

		exportColumns[0][1] = node.getId();
		exportColumns[1][1] = node.getName();
		exportColumns[2][1] = node.description;
		exportColumns[3][1] = node.getPhysicalLinkID();
		exportColumns[4][1] = String.valueOf(node.getAnchor().x);
		exportColumns[5][1] = String.valueOf(node.getAnchor().y);
		exportColumns[6][1] = String.valueOf(node.isActive());

		return exportColumns;
	}

	private String[][] getMapNodeLinkExportColumns(MapNodeLinkElement nodelink)
	{
		String[][] exportColumns = new String[6][2];
		exportColumns[0][0] = COLUMN_ID;
		exportColumns[1][0] = COLUMN_NAME;
		exportColumns[2][0] = COLUMN_DESCRIPTION;
		exportColumns[3][0] = COLUMN_PHYSICAL_LINK_ID;
		exportColumns[4][0] = COLUMN_START_NODE_ID;
		exportColumns[5][0] = COLUMN_END_NODE_ID;

		exportColumns[0][1] = nodelink.getId();
		exportColumns[1][1] = nodelink.getName();
		exportColumns[2][1] = nodelink.description;
		exportColumns[3][1] = nodelink.getMapContext().getPhysicalLinkbyNodeLink(nodelink.getId()).getId();
		exportColumns[4][1] = nodelink.startNode.getId();
		exportColumns[5][1] = nodelink.endNode.getId();

		return exportColumns;
	}

	private String[][] getMapEquipmentExportColumns(MapEquipmentNodeElement site)
	{
		String[][] exportColumns = new String[11][2];
		exportColumns[0][0] = COLUMN_ID;
		exportColumns[1][0] = COLUMN_NAME;
		exportColumns[2][0] = COLUMN_DESCRIPTION;
		exportColumns[3][0] = COLUMN_PROTO_ID;
		exportColumns[4][0] = COLUMN_X;
		exportColumns[5][0] = COLUMN_Y;
		exportColumns[6][0] = COLUMN_CITY;
		exportColumns[7][0] = COLUMN_STREET;
		exportColumns[8][0] = COLUMN_BUILDING;
		exportColumns[9][0] = COLUMN_COEF;
		exportColumns[10][0] = COLUMN_IMAGE_ID;

		exportColumns[0][1] = site.getId();
		exportColumns[1][1] = site.getName();
		exportColumns[2][1] = site.description;
		exportColumns[3][1] = "building";
		exportColumns[4][1] = String.valueOf(site.getAnchor().x);
		exportColumns[5][1] = String.valueOf(site.getAnchor().y);
		exportColumns[6][1] = "";
		exportColumns[7][1] = "";
		exportColumns[8][1] = "";
		exportColumns[9][1] = String.valueOf(site.scaleCoefficient);
		exportColumns[10][1] = site.getImageID();

		return exportColumns;
	}

	private String[][] getMapLinkExportColumns(MapPhysicalLinkElement link)
	{
		String[][] exportColumns = new String[10][2];
		exportColumns[0][0] = COLUMN_ID;
		exportColumns[1][0] = COLUMN_NAME;
		exportColumns[2][0] = COLUMN_DESCRIPTION;
		exportColumns[3][0] = COLUMN_PROTO_ID;
		exportColumns[4][0] = COLUMN_START_NODE_ID;
		exportColumns[5][0] = COLUMN_END_NODE_ID;
		exportColumns[6][0] = COLUMN_NODE_LINKS;
		exportColumns[7][0] = COLUMN_CITY;
		exportColumns[8][0] = COLUMN_STREET;
		exportColumns[9][0] = COLUMN_BUILDING;

		exportColumns[0][1] = link.getId();
		exportColumns[1][1] = link.getName();
		exportColumns[2][1] = link.description;
		exportColumns[3][1] = "tunnel";
		exportColumns[4][1] = link.startNode.getId();
		exportColumns[5][1] = link.endNode.getId();
		exportColumns[6][1] = "";
		for(Iterator it = link.nodeLink_ids.iterator(); it.hasNext();)
		{
			exportColumns[6][1] += (String )it.next() + " ";
		}
		exportColumns[7][1] = "";
		exportColumns[8][1] = "";
		exportColumns[9][1] = "";

		return exportColumns;
	}
}
