/**
 * $Id: MapMarkElement.java,v 1.21 2004/12/08 16:20:01 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.General.ElementAttribute_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapMarkElement_Transferable;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * ����� ����� ���������� � ��������� �� ��������� � ������� (MapLink) 
 * 
 * 
 * 
 * @version $Revision: 1.21 $, $Date: 2004/12/08 16:20:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapMarkElement extends MapNodeElement implements Serializable
{
	private static final long serialVersionUID = 02L;
	/**
	 * @deprecated
	 */
	public static final String typ = "mapmarkelement";

	/**
	 * @deprecated
	 */
	protected MapMarkElement_Transferable transferable;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";
	public static final String COLUMN_DISTANCE = "distance";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";

	/**
	 * @deprecated
	 */
	public static final String IMAGE_NAME = "mark";

	/**
	 * @deprecated
	 */
	protected String linkId = "";
	/**
	 * @deprecated
	 */
	protected double distance = 0.0;

	/**
	 * @deprecated
	 */
	protected MapNodeLinkElement nodeLink;
	/**
	 * @deprecated
	 */
	protected MapNodeElement startNode;

	/**
	 * @deprecated
	 */
	protected MapPhysicalLinkElement link;

	public static String[][] exportColumns = null;
	
	protected double sizeInDoubleLt;

	public MapMarkElement()
	{
		this.setImageId(IMAGE_NAME);

		attributes = new HashMap();

		transferable = new MapMarkElement_Transferable();
	}
	
	public MapMarkElement(
			String id,
			Map map,
			MapPhysicalLinkElement link,
			double len)
	{
		this.setId(id);
		this.setName(id);
		this.map = map;
		this.link = link;
		this.linkId = link.getId();
		this.setImageId(IMAGE_NAME);
		
		if(map != null)
			mapId = map.getId();
		attributes = new HashMap();

//		this.moveToFromStartLt(len);

		transferable = new MapMarkElement_Transferable();
	}

	/**
	 * @deprecated
	 */
	public MapMarkElement(MapMarkElement_Transferable transferable)
	{
		this.transferable = transferable;
		this.setLocalFromTransferable();
		this.setImageId(IMAGE_NAME);

		attributes = new HashMap();
	}

	/**
	 * @deprecated
	 */
/*	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		String clonedId = (String)Pool.get(MapPropertiesManager.MAP_CLONED_IDS, id);
		if (clonedId != null)
			return Pool.get(MapMarkElement.typ, clonedId);

		MapMarkElement mme = new MapMarkElement(
				dataSource.GetUId(MapMarkElement.typ),
				(Map)map.clone(dataSource), 
				(MapPhysicalLinkElement )link.clone(dataSource),
				this.distance);
				
		mme.anchor = new Point2D.Double(anchor.x, anchor.y);
		mme.bounds = new Rectangle(bounds);
		mme.alarmState = alarmState;
		mme.changed = changed;
		mme.description = description;
		mme.name = name;
		mme.nodeLink = (MapNodeLinkElement )nodeLink.clone(dataSource);
		mme.scaleCoefficient = scaleCoefficient;
		mme.selected = selected;

		Pool.put(MapMarkElement.typ, mme.getId(), mme);
		Pool.put(MapPropertiesManager.MAP_CLONED_IDS, id, mme.getId());

		mme.attributes = new HashMap();
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mme.attributes.put(ea2.type_id, ea2);
		}

		return mme;
	}
*/	
	/**
	 * @deprecated
	 */
	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.description = transferable.description;
//		this.anchor.x = Double.parseDouble(transferable.longitude);
//		this.anchor.y = Double.parseDouble(transferable.latitude);
		this.mapId = transferable.mapId;
		this.linkId = transferable.physicalLinkId;
		this.distance = transferable.distance;

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	/**
	 * @deprecated
	 */
	public void setTransferableFromLocal()
	{
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.description = this.description;
//		transferable.longitude = String.valueOf(this.anchor.x);
//		transferable.latitude = String.valueOf(this.anchor.y);
		transferable.mapId = map.getId();
		transferable.physicalLinkId = this.linkId;
		transferable.distance = this.distance;

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}
	}

	/**
	 * @deprecated
	 */
	public String getTyp()
	{
		return typ;
	}

	/**
	 * @deprecated
	 */
	public void updateLocalFromTransferable()
	{
		link = (MapPhysicalLinkElement )Pool.get(MapPhysicalLinkElement.typ, linkId);
		if(link != null)
		{
			map = (Map )Pool.get(Map.typ, this.mapId);
		}
//		this.moveToFromStartLt(distance);
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
	private static final String PROPERTY_PANE_CLASS_NAME = "";

	/**
	 * @deprecated
	 */
	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	public void setLocation(DoublePoint location)
	{
		super.setLocation(location);
		distance = this.getFromStartLengthLt();
	}

	public double getFromStartLengthLt()
	{
		link.sortNodeLinks();

		double pathLength = 0;
		
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nl = (MapNodeLinkElement )it.next();
			if(nl == nodeLink)
			{
				pathLength += this.getSizeInDoubleLt();
				break;
			}
			else
			{
				pathLength += nl.getLengthLt();
			}
		}
		return pathLength;
	}

	public double getFromEndLengthLt()
	{
		link.sortNodeLinks();

		double pathLength = 0;
		
		ListIterator it = link.getNodeLinks().listIterator(
				link.getNodeLinks().size());
		while(it.hasPrevious())
		{
			MapNodeLinkElement nl = (MapNodeLinkElement )it.previous();
			if(nl == nodeLink)
			{
				pathLength += nl.getLengthLt() - this.getSizeInDoubleLt();
				break;
			}
			else
				pathLength += nl.getLengthLt();
		}
		return pathLength;
	}

	public double getSizeInDoubleLt()
	{
//		updateSizeInDoubleLt();
		return sizeInDoubleLt;
	}

	public double getDistance()
	{
		return distance;
	}

	public String[][] getExportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new String[7][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_PHYSICAL_LINK_ID;
			exportColumns[4][0] = COLUMN_DISTANCE;
			exportColumns[5][0] = COLUMN_X;
			exportColumns[6][0] = COLUMN_Y;
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = linkId;
		exportColumns[4][1] = String.valueOf(getDistance());
		exportColumns[5][1] = String.valueOf(getLocation().x);
		exportColumns[6][1] = String.valueOf(getLocation().y);
		
		return exportColumns;
	}
	
	public void setColumn(String field, String value)
	{
		if(field.equals(COLUMN_ID))
			this.setId(value);
		else
		if(field.equals(COLUMN_NAME))
			this.setName(value);
		else
		if(field.equals(COLUMN_DESCRIPTION))
			this.setDescription(value);
		else
		if(field.equals(COLUMN_PHYSICAL_LINK_ID))
			linkId = value;
		else
		if(field.equals(COLUMN_DISTANCE))
			distance = Double.parseDouble(value);
		else
		if(field.equals(COLUMN_X))
			location.x = Double.parseDouble(value);
		else
		if(field.equals(COLUMN_Y))
			location.y = Double.parseDouble(value);
	}

	/**
	 * @deprecated
	 */
/*	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(bounds);
		out.writeObject(mapId);
		out.writeObject(linkId);
		out.writeDouble(anchor.x);
		out.writeDouble(anchor.y);
		out.writeDouble(distance);
		out.writeObject(imageId);

		out.writeObject(attributes);
	}
*/

/*	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		bounds = (Rectangle )in.readObject();
		mapId = (String )in.readObject();
		linkId = (String )in.readObject();
		double x = in.readDouble();
		double y = in.readDouble();
		anchor = new Point2D.Double(x, y);
		distance = in.readDouble();
		imageId = (String )in.readObject();

		attributes = (HashMap )in.readObject();

		transferable = new MapMarkElement_Transferable();
		this.updateLocalFromTransferable();

//		this.moveToFromStart(this.distance);
	}
*/
	public void setNodeLink(MapNodeLinkElement nodeLink)
	{
		this.nodeLink = nodeLink;
	}

	public MapNodeLinkElement getNodeLink()
	{
		return nodeLink;
	}

	public MapPhysicalLinkElement getLink()
	{
		return link;
	}


	public void setStartNode(MapNodeElement startNode)
	{
		this.startNode = startNode;
	}


	public MapNodeElement getStartNode()
	{
		return startNode;
	}
}
