package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.Map.MapPipePathElement_Transferable;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Point2D;

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

	protected List physicalLinkIds = new ArrayList();

	protected List links = new ArrayList();

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

		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			link.paint(g, str, color, false);
		}
	}

	public List getLinks()
	{	
		return links;
	}

	/**
	 * ¬нимание! концевые точки линии не обновл€ютс€
	 */
	public void removeLink(MapPhysicalLinkElement link)
	{
		links.remove(link);
	}

	/**
	 * ¬нимание! концевые точки линии не обновл€ютс€
	 */
	public void addLink(MapPhysicalLinkElement link)
	{
		links.add(link);
	}

	/**
	 * получить центр (√ћ“) линии
	 */
	public Point2D.Double getAnchor()
	{
		LinkedList vec = new LinkedList();
		Point2D.Double pts[];

		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mle = (MapPhysicalLinkElement )it.next();
			vec.add(mle.getAnchor());
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
	
	public boolean isMouseOnThisObject(Point currentMousePoint)
	{
		return false;
	}


}