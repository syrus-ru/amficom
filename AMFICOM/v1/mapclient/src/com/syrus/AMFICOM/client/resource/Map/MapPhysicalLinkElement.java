/**
 * $Id: MapPhysicalLinkElement.java,v 1.31 2004/12/08 16:20:01 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ResourceUtil;

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
 * @version $Revision: 1.31 $, $Date: 2004/12/08 16:20:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapPhysicalLinkElement extends MapLinkElement implements Serializable
{
	private static final long serialVersionUID = 02L;

	/**
	 * @deprecated
	 */
	public static final String typ = "maplinkelement";

	/**
	 * @deprecated
	 */
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

	/**
	 * @deprecated
	 */
	protected List nodeLinkIds = new ArrayList();

	/**
	 * @deprecated
	 */
	protected String mapProtoId = "";

	//Вектор NodeLink из которых состоит path
	/**
	 * @deprecated
	 */
	protected List nodeLinks = new LinkedList();
	/**
	 * @deprecated
	 */
	protected MapLinkProtoElement proto;

	/**
	 * @deprecated
	 */
	protected String city = "";
	/**
	 * @deprecated
	 */
	protected String street = "";
	/**
	 * @deprecated
	 */
	protected String building = "";

	protected List sortedNodes = new LinkedList();
	protected boolean nodeLinksSorted = false;

	protected MapPhysicalLinkBinding binding;

	public static String[][] exportColumns = null;

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapLinkPane";

	public MapPhysicalLinkElement()
	{
		selected = false;
		
		transferable = new MapPhysicalLinkElement_Transferable();
	}

	/**
	 * @deprecated
	 */
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
		
		binding = new MapPhysicalLinkBinding(proto.getBindingDimension());

		transferable = new MapPhysicalLinkElement_Transferable();
	}

	/**
	 * @deprecated
	 */
/*	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		String clonedId = (String)Pool.get(MapPropertiesManager.MAP_CLONED_IDS, id);
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
		Pool.put(MapPropertiesManager.MAP_CLONED_IDS, id, mple.getId());

		mple.nodeLinkIds = new ArrayList(nodeLinks.size());
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			mple.nodeLinkIds.add(Pool.get(MapPropertiesManager.MAP_CLONED_IDS, mnle.getId()));
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
*/
	/**
	 * @deprecated
	 */
	public void updateAttributes()
	{
		attributes.clear();
	    for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, Pool.get(ElementAttribute.typ, transferable.attributes[i].id));
	}

	/**
	 * @deprecated
	 */
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
		
		binding = new MapPhysicalLinkBinding(new IntDimension(
				transferable.dimensionX,
				transferable.dimensionY));
	}

	/**
	 * @deprecated
	 */
	public void setTransferableFromLocal()
	{
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.description = this.description;
		transferable.mapId = map.id;
		transferable.startNodeId = this.startNode.getId();
		transferable.endNodeId = this.endNode.getId();

		transferable.dimensionX = binding.getDimension().width;
		transferable.dimensionY = binding.getDimension().height;

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

	/**
	 * @deprecated
	 */
	public Object getTransferable()
	{
		return transferable;
	}

	/**
	 * @deprecated
	 */
	public String getTyp()
	{
		return typ;
	}

	//Используется для для загрузки класса из базы данных
	/**
	 * @deprecated
	 */
	public void updateLocalFromTransferable()
	{
		this.startNode = (MapNodeElement )Pool.get(MapSiteNodeElement.typ, startNodeId);
		if(this.startNode == null)
			this.startNode = (MapNodeElement )Pool.get(MapPhysicalNodeElement.typ, startNodeId);

		this.endNode = (MapNodeElement )Pool.get(MapSiteNodeElement.typ, endNodeId);
		if(this.endNode == null)
			this.endNode = (MapNodeElement )Pool.get(MapPhysicalNodeElement.typ, endNodeId);
		this.map = (Map )Pool.get(Map.typ, this.mapId);

		this.nodeLinks = new LinkedList();
		for (int i = 0; i < nodeLinkIds.size(); i++)
		{
			MapNodeLinkElement mnle = getMap().getNodeLink((String )nodeLinkIds.get(i));
			this.nodeLinks.add( mnle);
		}
		
		proto = (MapLinkProtoElement )Pool.get(MapLinkProtoElement.typ, mapProtoId);

		binding = new MapPhysicalLinkBinding(proto.getBindingDimension());
	}

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	public void setStartNode(MapNodeElement startNode)
	{
		super.setStartNode(startNode);
		nodeLinksSorted = false;
	}
	
	public void setEndNode(MapNodeElement endNode)
	{
		super.setEndNode(endNode);
		nodeLinksSorted = false;
	}

	/**
	 * @deprecated
	 */
	protected boolean selectionVisible = false;

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


	public List getNodeLinks()
	{	
		return nodeLinks;
	}

	public void clearNodeLinks()
	{	
		nodeLinks.clear();
		nodeLinksSorted = false;
	}

	public MapNodeLinkElement getStartNodeLink()
	{
		MapNodeLinkElement startNodeLink = null;

		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			startNodeLink = (MapNodeLinkElement )it.next();
			if(startNodeLink.getStartNode().equals(getStartNode())
				|| startNodeLink.getEndNode().equals(getStartNode()))
			{
				break;
			}
		}
		return startNodeLink;
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
	}

	/**
	 * Получить NodeLinks содержащие данный node в данном transmissionPath
	 */
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

	public DoublePoint getLocation()
	{
		int count = 0;
		DoublePoint point = new DoublePoint(0.0, 0.0);

		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			DoublePoint an = mnle.getLocation();
			point.x += an.x;
			point.y += an.y;
			count ++;
		}
		point.x /= count;
		point.y /= count;
		
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
			MapNodeLinkElement nl = null;
			LinkedList vec = new LinkedList();
			List nodevec = new LinkedList();
			int count = getNodeLinks().size();
			for (int i = 0; i < count; i++) 
			{
				nodevec.add(smne);

				for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
				{
					MapNodeLinkElement nodeLink = (MapNodeLinkElement )it.next();

					if(! nodeLink.equals(nl))
					{
						if(nodeLink.getStartNode().equals(smne))
						{
							vec.add(nodeLink);
							it.remove();
							smne = nodeLink.getEndNode();
							nl = nodeLink;
							break;
						}
						else
						if(nodeLink.getEndNode().equals(smne))
						{
							vec.add(nodeLink);
							it.remove();
							smne = nodeLink.getStartNode();
							nl = nodeLink;
							break;
						}
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
		sortNodeLinks();
		int index = getNodeLinks().indexOf(nl);
		if(index == getNodeLinks().size() - 1)
			return null;
		else
			return (MapNodeLinkElement )getNodeLinks().get(index + 1);
	}

	public MapNodeLinkElement previousNodeLink(MapNodeLinkElement nl)
	{
		sortNodeLinks();
		int index = getNodeLinks().indexOf(nl);
		if(index == 0)
			return null;
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

		this.nodeLinks = new LinkedList();
		for(Iterator it = mples.nodeLinks.iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			mnle.setPhysicalLinkId(getId());
			this.nodeLinks.add(mnle);
		}
		this.setMapProtoId(mples.mapProtoId);
		
		nodeLinksSorted = false;

//		updateLengthLt();
	}

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

	/**
	 * @deprecated
	 */	
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
			MapNodeLinkElement mne = (MapNodeLinkElement )it.next();
			nodeLinkIds.add(mne.getId());
		}
		out.writeObject(nodeLinkIds);
	}

	/**
	 * @deprecated
	 */	
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

//		updateLocalFromTransferable();
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

}
