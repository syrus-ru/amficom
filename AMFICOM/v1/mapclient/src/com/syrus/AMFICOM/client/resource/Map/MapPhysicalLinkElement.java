/**
 * $Id: MapPhysicalLinkElement.java,v 1.13 2004/09/23 10:05:29 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.General.ElementAttribute_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapPhysicalLinkElement_Transferable;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Resource.ResourceUtil;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Point2D;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * элемент линии 
 * 
 * 
 * 
 * @version $Revision: 1.13 $, $Date: 2004/09/23 10:05:29 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapPhysicalLinkElement extends MapLinkElement implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "maplinkelement";

	protected MapPhysicalLinkElement_Transferable transferable;

	public static final String COLUMN_ID = "id";	
	public static final String COLUMN_NAME = "name";	
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_PROTO_ID = "proto_id";	
	public static final String COLUMN_START_NODE_ID = "start_node_id";	
	public static final String COLUMN_END_NODE_ID = "end_node_id";	
	public static final String COLUMN_NODE_LINKS = "node_links";	
	public static final String COLUMN_CITY = "city";	
	public static final String COLUMN_STREET = "street";	
	public static final String COLUMN_BUILDING = "building";	

	//Вектор NodeLink из которых состоит path
	protected ArrayList nodeLinkIds = new ArrayList();
	protected ArrayList nodeLinks = new ArrayList();

	protected String mapProtoId = "";
	protected MapLinkProtoElement proto;

	protected String city = "";
	protected String street = "";
	protected String building = "";

	protected ArrayList sortedNodes = new ArrayList();
	protected boolean nodeLinksSorted = false;

	protected MapPhysicalLinkBinding binding;

	public static String[][] exportColumns = null;

	public String[][] getExportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new String[10][2];
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
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = getMapProtoId();
		exportColumns[4][1] = getStartNode().getId();
		exportColumns[5][1] = getEndNode().getId();
		exportColumns[6][1] = "";
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			exportColumns[6][1] += mnle.getId() + " ";
		}
		exportColumns[7][1] = getCity();
		exportColumns[8][1] = getStreet();
		exportColumns[9][1] = getBuilding();
		
		return exportColumns;
	}
	
	public void setColumn(String field, String value)
	{
		if(field.equals(COLUMN_ID))
			setId(value);
		else
		if(field.equals(COLUMN_NAME))
			setName(value);
		else
		if(field.equals(COLUMN_DESCRIPTION))
			setDescription(value);
		else
		if(field.equals(COLUMN_PROTO_ID))
			setMapProtoId(value);
		else
		if(field.equals(COLUMN_START_NODE_ID))
			startNodeId = value;
		else
		if(field.equals(COLUMN_END_NODE_ID))
			endNodeId = value;
		else
		if(field.equals(COLUMN_NODE_LINKS))
		{
			nodeLinkIds.clear();
			for(Iterator it = ResourceUtil.parseStrings(value).iterator(); it.hasNext();)
				nodeLinkIds.add(it.next());
		}
		else
		if(field.equals(COLUMN_CITY))
			setCity(value);
		else
		if(field.equals(COLUMN_STREET))
			setStreet(value);
		else
		if(field.equals(COLUMN_BUILDING))
			setBuilding(value);
	}
	
	public MapPhysicalLinkElement()
	{
		selected = false;
		
		transferable = new MapPhysicalLinkElement_Transferable();
	}

	public MapPhysicalLinkElement( MapPhysicalLinkElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public MapPhysicalLinkElement (
			String id, 
			MapNodeElement stNode, 
			MapNodeElement eNode, 
			Map map,
			MapLinkProtoElement proto)
	{
		this.map = map;

		this.id = id;
		this.name = id;
		if(map != null)
			mapId = map.getId();
		startNode = stNode;
		endNode = eNode;
		selected = false;
		mapProtoId = proto.getId();
		this.proto = proto;
		
		binding = new MapPhysicalLinkBinding(this, proto.getBindingDimension());

		transferable = new MapPhysicalLinkElement_Transferable();
	}

	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		String clonedId = (String)Pool.get("mapclonedids", id);
		if (clonedId != null)
			return Pool.get(MapPhysicalLinkElement.typ, clonedId);

		MapPhysicalLinkElement mple = new MapPhysicalLinkElement(
				dataSource.GetUId(MapPhysicalLinkElement.typ),
				(MapNodeElement )startNode.clone(dataSource),
				(MapNodeElement )endNode.clone(dataSource),
				(Map )map.clone(dataSource),
				getProto());
				
		mple.changed = changed;
		mple.description = description;
		mple.name = name;
		mple.selected = selected;
		mple.mapProtoId = mapProtoId;

		Pool.put(MapPhysicalLinkElement.typ, mple.getId(), mple);
		Pool.put("mapclonedids", id, mple.getId());

		mple.nodeLinkIds = new ArrayList(nodeLinks.size());
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			mple.nodeLinkIds.add(Pool.get("mapclonedids", mnle.getId()));
		}

		mple.attributes = new HashMap();
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mple.attributes.put(ea2.type_id, ea2);
		}

		return mple;
	}

	public void updateAttributes()
	{
		attributes.clear();
	    for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, Pool.get(ElementAttribute.typ, transferable.attributes[i].id));
	}

	public void setLocalFromTransferable()
	{
		int i;

		this.id = transferable.id;
		this.name = transferable.name;
		this.mapProtoId = transferable.mapProtoId;
		this.description = transferable.description;
		this.mapId = transferable.mapId;
		this.startNodeId = transferable.startNodeId;
		this.endNodeId = transferable.endNodeId;
		for(i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));

		this.nodeLinkIds = new ArrayList();
		for (i = 0; i < transferable.nodeLinkIds.length; i++)
		{
			this.nodeLinkIds.add( transferable.nodeLinkIds[i]);
		}
	}

	public void setTransferableFromLocal()
	{
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.description = this.description;
		transferable.mapId = map.id;
		transferable.startNodeId = this.startNode.getId();
		transferable.endNodeId = this.endNode.getId();

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}
		transferable.mapProtoId = mapProtoId;


		this.nodeLinkIds = new ArrayList();
		for (i = 0; i < nodeLinks.size(); i++)
		{
			this.nodeLinkIds.add( ((MapNodeLinkElement )nodeLinks.get(i)).getId());
		}
		transferable.nodeLinkIds = (String[] )nodeLinkIds.toArray(new String[nodeLinkIds.size()]);
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public String getTyp()
	{
		return typ;
	}

	//Используется для для загрузки класса из базы данных
	public void updateLocalFromTransferable()
	{
		this.startNode = (MapNodeElement )Pool.get(MapSiteNodeElement.typ, startNodeId);
		if(this.startNode == null)
			this.startNode = (MapNodeElement )Pool.get(MapPhysicalNodeElement.typ, startNodeId);

		this.endNode = (MapNodeElement )Pool.get(MapSiteNodeElement.typ, endNodeId);
		if(this.endNode == null)
			this.endNode = (MapNodeElement )Pool.get(MapPhysicalNodeElement.typ, endNodeId);
		this.map = (Map)Pool.get(Map.typ, this.mapId);

		this.nodeLinks = new ArrayList();
		for (int i = 0; i < nodeLinkIds.size(); i++)
		{
			MapNodeLinkElement mnle = getMap().getNodeLink((String )nodeLinkIds.get(i));
			this.nodeLinks.add( mnle);
		}
		
		proto = (MapLinkProtoElement )Pool.get(MapLinkProtoElement.typ, mapProtoId);

		binding = new MapPhysicalLinkBinding(this, proto.getBindingDimension());
	}

	public ObjectResourceModel getModel()
	{
		return null;//new MapPhysicalLinkElementModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return null;//new MapPhysicalLinkElementDisplayModel();
	}

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapLinkPane";

	public String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	public static PropertiesPanel getPropertyPane()
	{
		return null;
	}

	public String getToolTipText()
	{
		String s1 = name;
		String s2 = "";
		String s3 = "";
		try
		{
			MapNodeElement smne = getStartNode();
			s2 =  ":\n" + "   " + LangModelMap.getString("From") + " " + smne.getName() + " [" + LangModel.getString("node" + smne.getTyp()) + "]";
			MapNodeElement emne = getEndNode();
			s3 = "\n" + "   " + LangModelMap.getString("To") + " " + emne.getName() + " [" + LangModel.getString("node" + emne.getTyp()) + "]";
		}
		catch(Exception e)
		{
//			e.printStackTrace();
		}
		return s1 + s2 + s3;
	}

	public boolean isSelectionVisible()
	{
		return isSelected() || selectionVisible;
	}
	
	protected boolean selectionVisible = false;

	public void paint(Graphics g, Stroke stroke, Color color, boolean selectionVisible)
	{
		this.selectionVisible = selectionVisible;
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodelink = (MapNodeLinkElement )it.next();
			nodelink.paint(g, stroke, color);
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
		Color color = getColor();

		paint(g, str, color, false);
	}

	/**
	 * Возвращяет топологическую длинну линии
	 */
	public double getLengthLt()
	{
		double returnValue = 0;
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )it.next();
			returnValue += nodeLink.getLengthLt();
		}
		return returnValue;
	}

	/**
	 * возвращает false, поскольку проверка проводится как правило по всем
	 * элементам, и в частности по фрагментам линии, что вернет true
	 * для фрагмента. Поэтому в режиме работы с линиями ищется фрагмент
	 * и из него уже берется ссылка на эту линию
	 */
	public boolean isMouseOnThisObject(Point currentMousePoint)
	{
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapElement me = (MapElement )it.next();
			if(me.isMouseOnThisObject(currentMousePoint))
				return true;
		}
		return false;
	}

//	protected java.util.List getNodeLinkIds()
//	{
//		return this.nodeLinkIds;
//	}
	
	public List getNodeLinks()
	{	
		return nodeLinks;
	}

	/**
	 * Внимание! концевые точки линии не обновляются
	 */
	public void removeNodeLink(MapNodeLinkElement nodeLink)
	{
		nodeLinks.remove(nodeLink);
		nodeLinksSorted = false;
	}

	/**
	 * Внимание! концевые точки линии не обновляются
	 */
	public void addNodeLink(MapNodeLinkElement addNodeLink)
	{
		nodeLinks.add(addNodeLink);
		nodeLinksSorted = false;
//		addNodeLink.setPhysicalLinkId(getId());
	}

	//Получить NodeLinks содержащие данный node в данном transmissionPath
	public java.util.List getNodeLinksAt(MapNodeElement node)
	{
		LinkedList returnNodeLink = new LinkedList();
		Iterator e = getNodeLinks().iterator();

		while (e.hasNext())
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )e.next();
			if ( (nodeLink.endNode == node) || (nodeLink.startNode == node))
				returnNodeLink.add(nodeLink);
		}
		return returnNodeLink;
	}

	/**
	 * получить центр (ГМТ) линии
	 */
	public Point2D.Double getAnchor()
	{
		LinkedList vec = new LinkedList();
		Point2D.Double pts[];

		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			vec.add(mnle.getAnchor());
		}

		pts = (Point2D.Double[] )vec.toArray(new Point2D.Double[vec.size()]);
		Point2D.Double point = new Point2D.Double(0.0, 0.0);
		for(int i = 0; i < pts.length; i++)
		{
			point.x += pts[i].x;
			point.y += pts[i].y;
		}
		point.x /= pts.length;
		point.y /= pts.length;
		
		return point;
	}
	
	public void sortNodes()
	{
		sortNodeLinks();
	}
	
	public java.util.List getSortedNodes()
	{
		if(!nodeLinksSorted)
			return null;
		return sortedNodes;
	}

	public void sortNodeLinks()
	{
		if(!nodeLinksSorted)
		{
			MapNodeElement smne = this.getStartNode();
			ArrayList vec = new ArrayList();
			ArrayList nodevec = new ArrayList();
			while(!smne.equals(this.getEndNode()))
			{
				nodevec.add(smne);

				for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
				{
					MapNodeLinkElement nodeLink = (MapNodeLinkElement )it.next();

					if(nodeLink.getStartNode().equals(smne))
					{
						vec.add(nodeLink);
						it.remove();
						smne = nodeLink.getEndNode();
						break;
					}
					else
					if(nodeLink.getEndNode().equals(smne))
					{
						vec.add(nodeLink);
						it.remove();
						smne = nodeLink.getStartNode();
						break;
					}
				}
			}
			nodevec.add(this.endNode);
			this.nodeLinks = vec;
			nodeLinksSorted = true;
			this.sortedNodes = nodevec;
		}
	}

	public MapNodeLinkElement nextNodeLink(MapNodeLinkElement nl)
	{
		int index = getNodeLinks().indexOf(nl);
		if(index == getNodeLinks().size() - 1)
			return nl;
		else
			return (MapNodeLinkElement )getNodeLinks().get(index + 1);
	}

	public MapNodeLinkElement previousNodeLink(MapNodeLinkElement nl)
	{
		int index = getNodeLinks().indexOf(nl);
		if(index == 0)
			return nl;
		else
			return (MapNodeLinkElement )getNodeLinks().get(index - 1);
	}

	/**
	 * получить текущее состояние
	 */
	public MapElementState getState()
	{
		return new MapPhysicalLinkElementState(this);
	}

	/**
	 * восстановить состояние
	 */
	public void revert(MapElementState state)
	{
		super.revert(state);
		
		MapPhysicalLinkElementState mples = (MapPhysicalLinkElementState )state;

		this.nodeLinks = new ArrayList();
		for(Iterator it = mples.nodeLinks.iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			mnle.setPhysicalLinkId(getId());
			this.nodeLinks.add(mnle);
		}
		this.setMapProtoId(mples.mapProtoId);
		
		nodeLinksSorted = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(mapId);

		startNodeId = getStartNode().getId();
		endNodeId = getEndNode().getId();

		out.writeObject(startNodeId);
		out.writeObject(endNodeId);

		out.writeObject(attributes);

		out.writeObject(mapProtoId);

		this.nodeLinkIds = new ArrayList();
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeElement mne = (MapNodeElement )it.next();
			nodeLinkIds.add(mne.getId());
		}
		out.writeObject(nodeLinkIds);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		mapId = (String )in.readObject();
		startNodeId = (String )in.readObject();
		endNodeId = (String )in.readObject();
		attributes = (HashMap )in.readObject();

		mapProtoId = (String )in.readObject();
		nodeLinkIds = (ArrayList )in.readObject();

		transferable = new MapPhysicalLinkElement_Transferable();

		updateLocalFromTransferable();
		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}


	public void setMapProtoId(String mapProtoId)
	{
		this.mapProtoId = mapProtoId;
		proto = (MapLinkProtoElement )Pool.get(MapLinkProtoElement.typ, mapProtoId);
	}


	public String getMapProtoId()
	{
		return mapProtoId;
	}


	public MapPhysicalLinkBinding getBinding()
	{
		return binding;
	}


	public void setCity(String city)
	{
		this.city = city;
	}


	public String getCity()
	{
		return city;
	}


	public void setStreet(String street)
	{
		this.street = street;
	}


	public String getStreet()
	{
		return street;
	}


	public void setBuilding(String building)
	{
		this.building = building;
	}


	public String getBuilding()
	{
		return building;
	}


	public void setProto(MapLinkProtoElement proto)
	{
		this.proto = proto;
		if(proto != null)
			this.mapProtoId = proto.getId();
	}


	public MapLinkProtoElement getProto()
	{
		return proto;
	}

	/**
	 * Получить толщину линии
	 */
	public int getLineSize ()
	{
		return proto.getLineSize();
	}

	/**
	 * Получить вид линии
	 */
	public String getStyle ()
	{
		return proto.getStyle();
	}

	/**
	 * Получить стиль линии
	 */
	public Stroke getStroke ()
	{
		return proto.getStroke();
	}

	/**
	 * Получить цвет
	 */
	public Color getColor()
	{
		return proto.getColor();
	}

	/**
	 * получить цвет при наличии сигнала тревоги
	 */
	public Color getAlarmedColor()
	{
		return proto.getAlarmedColor();
	}

	/**
	 * получить толщину линии при наличи сигнала тревоги
	 */
	public int getAlarmedLineSize ()
	{
		return proto.getAlarmedLineSize();
	}
	
}
