/**
 * $Id: MapMeasurementPathElement.java,v 1.2 2004/09/23 10:07:15 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * элемент пути 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/23 10:07:15 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapMeasurementPathElement extends MapLinkElement implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "mapmeasurementpathelement";

	protected ArrayList sortedNodes = new ArrayList();
	protected ArrayList sortedNodeLinks = new ArrayList();
	protected boolean nodeLinksSorted = false;

	protected ArrayList cablePaths = new ArrayList();
	protected boolean cablePathsSorted = false;
	
	protected SchemePath schemePath;
	
	protected String mapViewId = "";
	
	protected MapView mapView;

	public String[][] getExportColumns()
	{
		return null;
	}

	public void setColumn(String field, String value)
	{
	}

	public MapMeasurementPathElement(
			SchemePath schemePath,
			String id, 
			MapNodeElement stNode, 
			MapNodeElement eNode, 
			MapView mapView)
	{
		this.mapView = mapView;

		this.id = id;
		this.name = schemePath.getName();
		if(mapView != null)
		{
			mapViewId = mapView.getId();
			map = mapView.getMap();
			if(map != null)
				mapId = map.getId();
		}
		startNode = stNode;
		endNode = eNode;
		attributes = new HashMap();
		
		setSchemePath(schemePath);
	}

	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
	}
	
	public MapView getmapView()
	{
		return this.mapView;
	}

	public Object clone(DataSourceInterface dataSource)
	{
/*
		String cloned_id = (String)Pool.get("mapclonedids", id);
		if (cloned_id != null)
			return Pool.get(MapPathElement.typ, cloned_id);

		MapPathElement mtpe = new MapPathElement(
				dataSource.GetUId(MapPathElement.typ),
				(MapNodeElement )startNode.clone(dataSource),
				(MapNodeElement )endNode.clone(dataSource),
				(Map )map.clone(dataSource) );
				
		mtpe.changed = changed;
		mtpe.description = description;
//		mtpe.endNode = (MapNodeElement )endNode.clone(dataSource);
		mtpe.endNode_id = endNode_id;
		mtpe.name = name;
		mtpe.scheme_path_id = (String )Pool.get("schemeclonedids", scheme_path_id);
		mtpe.selected = selected;
		mtpe.show_alarmed = show_alarmed;
//		mtpe.startNode = (MapNodeElement )startNode.clone(dataSource);
		mtpe.startNode_id = startNode_id;
		mtpe.type_id = type_id;

		Pool.put(MapPathElement.typ, mtpe.getId(), mtpe);
		Pool.put("mapclonedids", id, mtpe.getId());

		mtpe.physicalLink_ids = new Vector(physicalLink_ids.size());
		for (int i = 0; i < physicalLink_ids.size(); i++)
			mtpe.physicalLink_ids.add(Pool.get("mapclonedids", (String )physicalLink_ids.get(i)));

		mtpe.attributes = new Hashtable();
		for(Enumeration enum = attributes.elements(); enum.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )enum.nextElement();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mtpe.attributes.put(ea2.type_id, ea2);
		}

		return mtpe;
*/
		return null;
	}

	//этот класс используется для востановления данных из базы
	public void setLocalFromTransferable()
	{
	}

	//этот класс используется для отпрвки данных в базу
	public void setTransferableFromLocal()
	{
	}

	public String getTyp()
	{
		return typ;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	//Используется для загрузкт данных из базы
	public void updateLocalFromTransferable()
	{
		this.startNode = (MapNodeElement )
				Pool.get(MapSiteNodeElement.typ, startNodeId);
		if(this.startNode == null)
			this.startNode = (MapNodeElement )
					Pool.get(MapPhysicalNodeElement.typ, startNodeId);

		this.endNode = (MapNodeElement )
				Pool.get(MapSiteNodeElement.typ, endNodeId);
		if(this.endNode == null)
			this.endNode = (MapNodeElement )
					Pool.get(MapPhysicalNodeElement.typ, endNodeId);
		this.mapView = (MapView)Pool.get(MapView.typ, this.mapViewId);
	}

	public ObjectResourceModel getModel()
	{
		return null;//new MapPathElementModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return null;//new MapPathElementDisplayModel();
	}
	
	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	public static PropertiesPanel getPropertyPane()
	{
		return null;//new MapPathPane();
	}
	
	public void setSchemePath(SchemePath schemePath)
	{
		this.schemePath = schemePath;
		this.name = schemePath.getName();
	}

	public SchemePath getSchemePath()
	{
		return schemePath;
	}

	public MapView getMapView()
	{
		return this.mapView;
	}
	
	public boolean isSelectionVisible()
	{
		return isSelected();
	}

	public void paint(Graphics g, Stroke stroke, Color color, boolean selectionVisible)
	{
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			cpath.paint(g, stroke, color, selectionVisible);
		}
	}

	public void paint(Graphics g)
	{
		BasicStroke stroke = (BasicStroke )this.getStroke();
		Stroke str = new BasicStroke(
				this.getLineSize(), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());
		Color color = this.getColor();

		paint(g, str, color, isSelectionVisible());
	}


	public boolean isMouseOnThisObject(Point currentMousePoint)
	{
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MapElement me = (MapElement )it.next();
			if(me.isMouseOnThisObject(currentMousePoint))
				return true;
		}
		return false;
	}
	
	public Object getTransferable()
	{
		return null;
	}

	//Возвращает топологическую длинну в метрах
	public double getLengthLt()
	{
		double length = 0;
		Iterator e = getCablePaths().iterator();
		while( e.hasNext())
		{
			MapCablePathElement cpath = (MapCablePathElement )e.next();
			length = length + cpath.getLengthLt();
		}
		return length;
	}

	public double getLengthLf()
	{
		return schemePath.getPhysicalLength();
	}

	public List getCablePaths()
	{	
		return cablePaths;
	}

	/**
	 * Внимание! концевые точки линии не обновляются
	 */
	public void removeCablePath(MapCablePathElement cpath)
	{
		cablePaths.remove(cpath);
		cablePathsSorted = false;
	}

	/**
	 * Внимание! концевые точки линии не обновляются
	 */
	public void addCablePath(MapCablePathElement cpath)
	{
		cablePaths.add(cpath);
		cablePathsSorted = false;
	}

	public void sortCablePaths()
	{
		if(!cablePathsSorted)
		{
			MapNodeElement smne = this.getStartNode();
			ArrayList vec = new ArrayList();
			while(!smne.equals(this.getEndNode()))
			{
				for(Iterator it = getCablePaths().iterator(); it.hasNext();)
				{
					MapCablePathElement cpath = (MapCablePathElement )it.next();

					if(cpath.getStartNode().equals(smne))
					{
						vec.add(cpath);
						it.remove();
						smne = cpath.getEndNode();
						break;
					}
					else
					if(cpath.getEndNode().equals(smne))
					{
						vec.add(cpath);
						it.remove();
						smne = cpath.getStartNode();
						break;
					}
				}
			}
			this.cablePaths = vec;
			cablePathsSorted = true;
		}
	}

	public MapCablePathElement nextCablePath(MapCablePathElement cablePath)
	{
		int index = getCablePaths().indexOf(cablePath);
		if(index == getCablePaths().size() - 1)
			return cablePath;
		else
			return (MapCablePathElement )getCablePaths().get(index + 1);
	}

	public MapCablePathElement previousCablePath(MapCablePathElement cpath)
	{
		int index = getCablePaths().indexOf(cpath);
		if(index == 0)
			return cpath;
		else
			return (MapCablePathElement )getCablePaths().get(index - 1);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);

		startNodeId = getStartNode().getId();
		endNodeId = getEndNode().getId();

		out.writeObject(startNodeId);
		out.writeObject(endNodeId);

		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		startNodeId = (String )in.readObject();
		endNodeId = (String )in.readObject();
		attributes = (HashMap )in.readObject();

		updateLocalFromTransferable();
		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}

}
