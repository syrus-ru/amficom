/**
 * $Id: MapPhysicalLinkElement.java,v 1.8 2004/09/14 14:48:26 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
 * @version $Revision: 1.8 $, $Date: 2004/09/14 14:48:26 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapPhysicalLinkElement extends MapLinkElement implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "maplinkelement";

	protected MapPhysicalLinkElement_Transferable transferable;

	//Вектор NodeLink из которых состоит path
	protected ArrayList nodeLinkIds = new ArrayList();
	protected ArrayList nodeLinks = new ArrayList();

	protected String mapProtoId = "";

	protected ArrayList sortedNodes = new ArrayList();
	protected boolean nodeLinksSorted = false;

	protected MapPhysicalLinkBinding binding;

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
		
		binding = new MapPhysicalLinkBinding(this, proto.getBindingDimension());

		transferable = new MapPhysicalLinkElement_Transferable();
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("mapclonedids", id);
		if (cloned_id != null)
			return Pool.get(MapPhysicalLinkElement.typ, cloned_id);

		MapPhysicalLinkElement mple = new MapPhysicalLinkElement(
				dataSource.GetUId(MapPhysicalLinkElement.typ),
				(MapNodeElement )startNode.clone(dataSource),
				(MapNodeElement )endNode.clone(dataSource),
				(Map )map.clone(dataSource),
				(MapLinkProtoElement )Pool.get(MapLinkProtoElement.typ, mapProtoId));
				
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
		transferable.nodeLinkIds = (String[] )nodeLinkIds.toArray(new String[0]);
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
		this.map = (Map)Pool.get(com.syrus.AMFICOM.Client.Resource.Map.Map.typ, this.mapId);

		this.nodeLinks = new ArrayList();
		for (int i = 0; i < nodeLinkIds.size(); i++)
		{
			MapNodeLinkElement mnle = getMap().getNodeLink((String )nodeLinkIds.get(i));
			this.nodeLinks.add( mnle);
		}
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

	boolean isSelectionVisible()
	{
		return isSelected();
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

		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodelink = (MapNodeLinkElement )it.next();
			nodelink.paint(g, str, color);
		}
	}

	/**
	 * Рисуем NodeLink взависимости от того выбрана она или нет
	 * а так же если она выбрана выводим её рамер
	 */
	public void paint1 (Graphics g)
	{
		MapCoordinatesConverter converter = getMap().getConverter();

		Graphics2D p = (Graphics2D )g;
		
		BasicStroke stroke = (BasicStroke )this.getStroke();
		Stroke str = new BasicStroke(
				this.getLineSize(), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());

		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodelink = (MapNodeLinkElement )it.next();

			Point from = converter.convertMapToScreen( nodelink.startNode.getAnchor());
			Point to = converter.convertMapToScreen( nodelink.endNode.getAnchor());

			p.setStroke(str);
			p.setColor(this.getColor());

			if (this.getAlarmState())
			{
				if (MapPropertiesManager.isShowAlarmState() )
					p.setColor(this.getAlarmedColor());
				else
					p.setColor(this.getColor());
			}
			else
				p.setColor(this.getColor());

			p.drawLine( from.x, from.y, to.x, to.y);
			if ( isSelected())
			{
				p.setStroke(MapPropertiesManager.getSelectionStroke());

				double l = 4;
				double l1 = 6;
				double cos_a = (from.y - to.y) 
					/ Math.sqrt( 
							(from.x - to.x) * (from.x - to.x) 
							+ (from.y - to.y) * (from.y - to.y) );

				double sin_a = (from.x - to.x) 
					/ Math.sqrt( 
							(from.x - to.x) * (from.x - to.x) 
							+ (from.y - to.y) * (from.y - to.y) );

				p.setColor(MapPropertiesManager.getFirstSelectionColor());
				p.drawLine(
						from.x + (int)(l * cos_a), 
						from.y  - (int)(l * sin_a), 
						to.x + (int)(l * cos_a), 
						to.y - (int)(l * sin_a));
				p.drawLine(
						from.x - (int)(l * cos_a), 
						from.y  + (int)(l * sin_a), 
						to.x - (int)(l * cos_a), 
						to.y + (int)(l * sin_a));

				p.setColor(MapPropertiesManager.getSecondSelectionColor());
				p.drawLine(
						from.x + (int)(l1 * cos_a), 
						from.y  - (int)(l1 * sin_a), 
						to.x + (int)(l1 * cos_a), 
						to.y - (int)(l1 * sin_a));
				p.drawLine(
						from.x - (int)(l1 * cos_a), 
						from.y  + (int)(l1 * sin_a), 
						to.x - (int)(l1 * cos_a), 
						to.y + (int)(l1 * sin_a));
			}
		}
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
	 * Возвращяет длинну линии пересчитанную на коэффициент топологической 
	 * привязки
	 */
	public double getLengthLf()
	{
		double Kd = getKd();
		return getLengthLt() * Kd;
	}

	/**
	 * возвращает строительную длину линии
	 */
	public double getPhysicalLength()
	{
//		if(LINK_ID == null)
			return 0.0D;
//		SchemeCableLink scl = (SchemeCableLink )Pool.get(SchemeCableLink.typ, LINK_ID);
//		if(scl == null)
//			return 0.0D;
//		return scl.getPhysicalLength();
	}

	/**
	 * возвращает коэффициент топологической привязки
	 */
	public double getKd()
	{
//		double ph_len = getPhysicalLength();
//		if(ph_len == 0.0D)
//			return 1.0;
//		double top_len = getSizeInDoubleLt();
//		if(top_len == 0.0D)
			return 1.0;
//
//		double Kd = ph_len / top_len;
//		return Kd;
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

	protected java.util.List getNodeLinkIds()
	{
		return this.nodeLinkIds;
	}
	
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
		this.mapProtoId = mples.mapProtoId;
		
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

//		updateLocalFromTransferable();
		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}


	public void setMapProtoId(String mapProtoId)
	{
		this.mapProtoId = mapProtoId;
	}


	public String getMapProtoId()
	{
		return mapProtoId;
	}


	public MapPhysicalLinkBinding getBinding()
	{
		return binding;
	}

}
