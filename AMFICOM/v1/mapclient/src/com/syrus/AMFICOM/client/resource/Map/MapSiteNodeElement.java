/**
 * $Id: MapSiteNodeElement.java,v 1.15 2004/12/22 16:17:38 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;

/**
 * уэел 
 * 
 * 
 * 
 * @version $Revision: 1.15 $, $Date: 2004/12/22 16:17:38 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapSiteNodeElement extends SiteNode
{
	private static final long serialVersionUID = 02L;

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


	protected static String[][] exportColumns = null;

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapSitePane";

	public MapSiteNodeElement()
		throws ObjectNotFoundException, RetrieveObjectException
	{
		super(new Identifier("sitenode"));
		selected = false;
//		setIconName("pc");
	}

	public MapSiteNodeElement(
		DoublePoint location,
		Map map,
		SiteNodeType pe)
		throws ObjectNotFoundException, RetrieveObjectException
	{
		super(new Identifier("sitenode"));

		setName(pe.getName());
		this.setMap(map);

		this.setName(id.toString());
		this.setType(pe);
		this.setDescription("");
		setLocation(location);

		setImageId(pe.getImageId());
		selected = false;
	}
/*
	public void setMapProtoId(Identifier mapProtoId)
	{
		try
		{
			MapNodeProtoElement proto = (MapNodeProtoElement )(MapStorableObjectPool.getStorableObject(mapProtoId, true));
			setProto(proto);
		}
		catch (CommunicationException e)
		{
			
		}
		catch (DatabaseException e)
		{
			
		}
	}
*/
	public void setProto(MapNodeProtoElement proto)
	{
		super.setType(proto);
	}

	public MapNodeProtoElement getProto()
	{
		return (MapNodeProtoElement )super.getType();
	}

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
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
/*
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
		exportColumns[0][1] = getId().toString();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = getProto().getId().toString();
		exportColumns[4][1] = String.valueOf(getLocation().x);
		exportColumns[5][1] = String.valueOf(getLocation().y);
		exportColumns[6][1] = getCity();
		exportColumns[7][1] = getStreet();
		exportColumns[8][1] = getBuilding();
		exportColumns[9][1] = String.valueOf(scaleCoefficient);
		exportColumns[10][1] = getImageId().toString();
		
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
			setMapProtoId(new Identifier(value));
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
		else
		if(field.equals(COLUMN_COEF))
			setScaleCoefficient(Double.parseDouble(value));
		else
		if(field.equals(COLUMN_IMAGE_ID))
			setIconName(value);
	}
*/	
}
