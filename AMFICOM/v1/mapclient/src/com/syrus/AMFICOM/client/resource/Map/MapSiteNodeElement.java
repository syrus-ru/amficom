/**
 * $Id: MapSiteNodeElement.java,v 1.14 2004/12/08 16:20:01 krupenn Exp $
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
import com.syrus.AMFICOM.CORBA.Map.MapSiteElement_Transferable;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;

/**
 * уэел 
 * 
 * 
 * 
 * @version $Revision: 1.14 $, $Date: 2004/12/08 16:20:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapSiteNodeElement extends MapNodeElement implements Serializable
{
	private static final long serialVersionUID = 02L;

	/**
	 * @deprecated
	 */
	public static final String typ = "mapsiteelement";

	/**
	 * @deprecated
	 */
	protected MapSiteElement_Transferable transferable;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	public static final String COLUMN_PROTO_ID = "proto_id";	
	public static final String COLUMN_CITY = "city";	
	public static final String COLUMN_STREET = "street";	
	public static final String COLUMN_BUILDING = "building";	
	public static final String COLUMN_COEF = "coef";
	public static final String COLUMN_IMAGE_ID = "image_id";

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

	/**
	 * @deprecated
	 */
	protected String mapProtoId = "";

	protected static String[][] exportColumns = null;

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapSitePane";

	public MapSiteNodeElement()
	{
		attributes = new HashMap();
		selected = false;
		setImageId("pc");
//		setScaleCoefficient(1.0D);

		transferable = new MapSiteElement_Transferable();
	}

	/**
	 * @deprecated
	 */
	public MapSiteNodeElement(MapSiteElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public MapSiteNodeElement(
		String id,
		DoublePoint location,
		Map map,
		MapNodeProtoElement pe)
	{
		this(id, location, map, pe.getImageId(), pe.getId());

		name = pe.getName();
	}

	public MapSiteNodeElement(
		String id,
		DoublePoint location,
		Map map,
		String imageId,
		String mapProtoId)
	{
		this.map = map;

		this.setId(id);
		this.setName(id);
		this.mapProtoId = mapProtoId;
		this.setDescription("");
		setLocation(location);

		if(map != null)
			mapId = map.getId();
		attributes = new HashMap();
		setImageId( imageId);
		selected = false;
	}

	/**
	 * @deprecated
	 */
/*	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		String clonedid = (String)Pool.get(MapPropertiesManager.MAP_CLONED_IDS, id);
		if (clonedid != null)
			return Pool.get(MapSiteNodeElement.typ, clonedid);

		MapSiteNodeElement mene = new MapSiteNodeElement(
				dataSource.GetUId(MapSiteNodeElement.typ),
				new Point2D.Double(anchor.x, anchor.y),
				(Map)map.clone(dataSource), 
//				scaleCoefficient,
				imageId,
				mapProtoId);
				
		mene.bounds = new Rectangle(bounds);
		mene.alarmState = alarmState;
		mene.changed = changed;
		mene.description = description;
		mene.name = name;
		mene.optimizerAttribute = optimizerAttribute;
		mene.scaleCoefficient = scaleCoefficient;
		mene.selected = selected;
		mene.mapProtoId = mapProtoId;

		Pool.put(MapSiteNodeElement.typ, mene.getId(), mene);
		Pool.put(MapPropertiesManager.MAP_CLONED_IDS, id, mene.getId());

		mene.attributes = new HashMap();
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mene.attributes.put(ea2.type_id, ea2);
		}

		return mene;
	}
*/
	//Устанавливаем переменные класса из базы данных
	/**
	 * @deprecated
	 */
	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.mapProtoId = transferable.mapProtoId;
		this.description = transferable.description;
//		this.anchor.x = Double.parseDouble(transferable.longitude);
//		this.anchor.y = Double.parseDouble(transferable.latitude);
		this.mapId = transferable.mapId;
		this.setImageId( transferable.symbolId);

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
		transferable.mapProtoId = this.mapProtoId;
		transferable.description = this.description;
//		transferable.longitude = String.valueOf(this.anchor.x);
//		transferable.latitude = String.valueOf(this.anchor.y);
		transferable.mapId = map.getId();
		transferable.symbolId = this.imageId;

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
		this.map = (Map)Pool.get(Map.typ, this.mapId);
	}

	/**
	 * @deprecated
	 */
	public Object getTransferable()
	{
		return transferable;
	}

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	public MapElementState getState()
	{
		return new MapSiteNodeElementState(this);
	}

	public void revert(MapElementState state)
	{
		super.revert(state);
		
		MapSiteNodeElementState msnes = (MapSiteNodeElementState)state;
		
		mapProtoId = msnes.mapProtoId;
	}

	public String[][] getExportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new String[11][2];
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
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = getMapProtoId();
		exportColumns[4][1] = String.valueOf(getLocation().x);
		exportColumns[5][1] = String.valueOf(getLocation().y);
		exportColumns[6][1] = getCity();
		exportColumns[7][1] = getStreet();
		exportColumns[8][1] = getBuilding();
//		exportColumns[9][1] = String.valueOf(scaleCoefficient);
		exportColumns[10][1] = getImageId();
		
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
		if(field.equals(COLUMN_X))
			location.x = Double.parseDouble(value);
		else
		if(field.equals(COLUMN_Y))
			location.y = Double.parseDouble(value);
		else
		if(field.equals(COLUMN_CITY))
			setCity(value);
		else
		if(field.equals(COLUMN_STREET))
			setStreet(value);
		else
		if(field.equals(COLUMN_BUILDING))
			setBuilding(value);
//		else
//		if(field.equals(COLUMN_COEF))
//			setScaleCoefficient(Double.parseDouble(value));
		else
		if(field.equals(COLUMN_IMAGE_ID))
			setImageId( imageId);
	}
	
	/**
	 * @deprecated
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(mapProtoId);
		out.writeObject(description);
		out.writeDouble(location.x);
		out.writeDouble(location.y);
		out.writeObject(mapId);
		out.writeObject(this.getImageId());

		out.writeObject(attributes);
	}

	/**
	 * @deprecated
	 */
	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		mapProtoId = (String )in.readObject();
		description = (String )in.readObject();
		location = new DoublePoint( );
		location.x = in.readDouble();
		location.y = in.readDouble();
		mapId = (String )in.readObject();
		this.setImageId((String )in.readObject());
		attributes = (HashMap )in.readObject();

		transferable = new MapSiteElement_Transferable();
//		updateLocalFromTransferable();
		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}

	public void setMapProtoId(String mapProtoId)
	{
		this.mapProtoId = mapProtoId;
		MapNodeProtoElement proto = (MapNodeProtoElement )Pool.get(MapNodeProtoElement.typ, mapProtoId);
		setImageId(proto.getImageId());
	}

	public String getMapProtoId()
	{
		return mapProtoId;
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
}
