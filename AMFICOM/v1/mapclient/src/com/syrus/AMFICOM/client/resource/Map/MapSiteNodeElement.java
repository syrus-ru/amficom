/**
 * $Id: MapSiteNodeElement.java,v 1.16 2004/12/23 16:35:17 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;

/**
 * уэел 
 * 
 * 
 * 
 * @version $Revision: 1.16 $, $Date: 2004/12/23 16:35:17 $
 * @module
 * @author $Author: krupenn $
 * @see
 * @deprecated
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


	protected static Object[][] exportColumns = null;

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

	
}
