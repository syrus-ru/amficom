/**
 * $Id: MapPipePathElement.java,v 1.12 2004/12/08 16:20:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.Map.MapPipePathElement_Transferable;
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
 * коллектор. 
 * 
 * 
 * 
 * @version $Revision: 1.12 $, $Date: 2004/12/08 16:20:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapPipePathElement extends MapLinkElement implements Serializable
{
	private static final long serialVersionUID = 02L;

	/**
	 * @deprecated
	 */
	public static final String typ = "mappipepathelement";

	/**
	 * @deprecated
	 */
	protected MapPipePathElement_Transferable transferable;

	public static final String COLUMN_ID = "id";	
	public static final String COLUMN_NAME = "name";	
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_LINKS = "links";	

	/**
	 * @deprecated
	 */
	protected List physicalLinkIds = new ArrayList();

	/**
	 * @deprecated
	 */
	protected List links = new LinkedList();

	public static String[][] exportColumns = null;

	public MapPipePathElement()
	{
		transferable = new MapPipePathElement_Transferable();
	}

	/**
	 * @deprecated
	 */
	public MapPipePathElement(MapPipePathElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public MapPipePathElement(
			String id,
			String name,
			Map map)
	{
		super();
		this.id = id;
		this.name = name;
		this.map = map;
		
		if(map != null)
		{
			this.mapId = map.getId();
		}

		transferable = new MapPipePathElement_Transferable();
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
/*	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		String clonedId = (String)Pool.get(MapPropertiesManager.MAP_CLONED_IDS, id);
		if (clonedId != null)
			return Pool.get(MapPipePathElement.typ, clonedId);

		MapPipePathElement mppe = (MapPipePathElement )super.clone();
		mppe.setId(dataSource.GetUId(MapPipePathElement.typ));
		mppe.setStartNode((MapNodeElement )getStartNode().clone(dataSource));
		mppe.setEndNode((MapNodeElement )getEndNode().clone(dataSource));
		mppe.setMap((Map )getMap().clone(dataSource));
				
		mppe.description = description;
		mppe.name = name;
		mppe.selected = selected;

		Pool.put(MapPipePathElement.typ, mppe.getId(), mppe);
		Pool.put(MapPropertiesManager.MAP_CLONED_IDS, id, mppe.getId());

		mppe.physicalLinkIds = new ArrayList(physicalLinkIds.size());
		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mple = (MapPhysicalLinkElement )it.next();
			mppe.physicalLinkIds.add(Pool.get(MapPropertiesManager.MAP_CLONED_IDS, mple.getId()));
		}

		mppe.attributes = new HashMap();
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mppe.attributes.put(ea2.type_id, ea2);
		}

		return mppe;
	}
*/
	/**
	 * Используется для для загрузки класса из базы данных
	 * @deprecated
	 */
	public void updateLocalFromTransferable()
	{
		this.map = (Map )Pool.get(Map.typ, this.mapId);

		links = new LinkedList();

		for(Iterator it = physicalLinkIds.iterator(); it.hasNext();)
		{
			String pli = (String )it.next();
			links.add(getMap().getPhysicalLink(pli));
		}
	}

	public String getName()
	{
		return name;
	}

	public List getLinks()
	{	
		return links;
	}

	/**
	 * Внимание! концевые точки линии не обновляются
	 */
	public void removeLink(MapPhysicalLinkElement link)
	{
		links.remove(link);
	}

	/**
	 * Внимание! концевые точки линии не обновляются
	 */
	public void addLink(MapPhysicalLinkElement link)
	{
		links.add(link);
	}

	public DoublePoint getLocation()
	{
		int count = 0;
		DoublePoint point = new DoublePoint(0.0, 0.0);

		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mle = (MapPhysicalLinkElement )it.next();
			DoublePoint an = mle.getLocation();
			point.x += an.x;
			point.y += an.y;
			count ++;
		}
		point.x /= count;
		point.y /= count;
		
		return point;
	}

	//Возвращает топологическую длинну в метрах
	public double getLengthLt()
	{
		double length = 0;
		Iterator e = getLinks().iterator();
		while( e.hasNext())
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )e.next();
			length = length + link.getLengthLt();
		}
		return length;
	}

	/**
	 * получить текущее состояние
	 */
	public MapElementState getState()
	{
		throw new UnsupportedOperationException();
	}

	public String[][] getExportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new String[4][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_LINKS;
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = "";
		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mple = (MapPhysicalLinkElement )it.next();
			exportColumns[3][1] += mple.getId() + " ";
		}

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
		if(field.equals(COLUMN_LINKS))
		{
			physicalLinkIds.clear();
			for(Iterator it = ResourceUtil.parseStrings(value).iterator(); it.hasNext();)
				physicalLinkIds.add(it.next());
		}
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

		out.writeObject(attributes);

		this.physicalLinkIds = new ArrayList();
		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mle = (MapPhysicalLinkElement )it.next();
			physicalLinkIds.add(mle.getId());
		}
		out.writeObject(physicalLinkIds);
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
		attributes = (HashMap )in.readObject();

		physicalLinkIds = (ArrayList )in.readObject();

		transferable = new MapPipePathElement_Transferable();

//		updateLocalFromTransferable();
		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}


}