package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.Map.MapPipePathElement_Transferable;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Resource.ResourceUtil;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Point2D;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MapPipePathElement extends MapLinkElement 
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "mappipepathelement";

	protected MapPipePathElement_Transferable transferable;

	public static final String COLUMN_ID = "";	
	public static final String COLUMN_NAME = "";	
	public static final String COLUMN_DESCRIPTION = "";	
	public static final String COLUMN_LINKS = "";	

	protected List physicalLinkIds = new ArrayList();

	protected List links = new ArrayList();

	public static String[][] exportColumns = null;

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
		exportColumns[4][1] = "";
		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mple = (MapPhysicalLinkElement )it.next();
			exportColumns[4][1] += mple.getId() + " ";
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
	
	public MapPipePathElement()
	{
		transferable = new MapPipePathElement_Transferable();
	}

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
	
	public String getTyp()
	{
		return typ;
	}

	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		String clonedId = (String)Pool.get("mapclonedids", id);
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
		Pool.put("mapclonedids", id, mppe.getId());

		mppe.physicalLinkIds = new ArrayList(physicalLinkIds.size());
		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mple = (MapPhysicalLinkElement )it.next();
			mppe.physicalLinkIds.add(Pool.get("mapclonedids", mple.getId()));
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

	public boolean isVisible(Rectangle2D.Double visibleBounds)
	{
		boolean vis = false;
		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			if(link.isVisible(visibleBounds))
			{
				vis = true;
				break;
			}
		}
		return vis;
	}

	public void paint(Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!isVisible(visibleBounds))
			return;

		BasicStroke stroke = (BasicStroke )this.getStroke();
		Stroke str = new BasicStroke(
				this.getLineSize(), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());
		Color color = getColor();

		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			link.paint(g, visibleBounds, str, color, false);
		}
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

	/**
	 * получить центр (ГМТ) линии
	 */
	public Point2D.Double getAnchor()
	{
		int count = 0;
		Point2D.Double point = new Point2D.Double(0.0, 0.0);

		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mle = (MapPhysicalLinkElement )it.next();
			Point2D.Double an = mle.getAnchor();
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

	public boolean isMouseOnThisObject(Point currentMousePoint)
	{
		return false;
	}


}