/**
 * $Id: MapNodeLinkElement.java,v 1.17 2004/10/06 14:10:05 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.General.ElementAttribute_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapNodeLinkElement_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;

/**
 * элемнт карты - фрагмент линии
 * 
 * 
 * 
 * @version $Revision: 1.17 $, $Date: 2004/10/06 14:10:05 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapNodeLinkElement extends MapLinkElement implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "mapnodelinkelement";

	protected MapNodeLinkElement_Transferable transferable;

	public static final String COLUMN_ID = "id";	
	public static final String COLUMN_NAME = "name";	
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";	
	public static final String COLUMN_START_NODE_ID = "start_node_id";	
	public static final String COLUMN_END_NODE_ID = "end_node_id";	

	/** идентификатор линии, в составе которой находится фрагмент */
	protected String physicalLinkId;

	/** границы объекта, отображающего длину фрагмента */
	protected Rectangle labelBox = new Rectangle();
	
	protected double lengthLt = 0.0D;

	public static String[][] exportColumns = null;

	public String[][] getExportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new String[6][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_PHYSICAL_LINK_ID;
			exportColumns[4][0] = COLUMN_START_NODE_ID;
			exportColumns[5][0] = COLUMN_END_NODE_ID;
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = getPhysicalLinkId();
		exportColumns[4][1] = getStartNode().getId();
		exportColumns[5][1] = getEndNode().getId();
		
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
		if(field.equals(COLUMN_PHYSICAL_LINK_ID))
			setPhysicalLinkId(value);
		else
		if(field.equals(COLUMN_START_NODE_ID))
			startNodeId = value;
		else
		if(field.equals(COLUMN_END_NODE_ID))
			endNodeId = value;
	}

	public MapNodeLinkElement()
	{
		attributes = new HashMap();

		selected = false;

		transferable = new MapNodeLinkElement_Transferable();
	}

	public MapNodeLinkElement(MapNodeLinkElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public MapNodeLinkElement (
			String id, 
			MapNodeElement stNode, 
			MapNodeElement eNode, 
			Map map)
	{
		this.map = map;

		this.id = id;
		this.name = id;
		if(map != null)
			mapId = map.getId();
		startNode = stNode;
		endNode = eNode;
		attributes = new HashMap();

		selected = false;

		transferable = new MapNodeLinkElement_Transferable();
	}

	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		String clonedId = (String)Pool.get("mapclonedids", id);
		if (clonedId != null)
			return Pool.get(MapNodeLinkElement.typ, clonedId);

		MapNodeLinkElement mnle = new MapNodeLinkElement(
				dataSource.GetUId(MapNodeLinkElement.typ),
				(MapNodeElement )startNode.clone(dataSource),
				(MapNodeElement )endNode.clone(dataSource),
				(Map)map.clone(dataSource) );
				
		mnle.changed = changed;
		mnle.description = description;
		mnle.name = name;
		mnle.physicalLinkId = physicalLinkId;
		mnle.selected = selected;

		Pool.put(MapNodeLinkElement.typ, mnle.getId(), mnle);
		Pool.put("mapclonedids", id, mnle.getId());

		mnle.attributes = new HashMap();
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mnle.attributes.put(ea2.type_id, ea2);
		}

		return mnle;
	}

	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.mapId = transferable.mapId;
		this.physicalLinkId = transferable.physicalLinkId;
		this.startNodeId = transferable.startNodeId;
		this.endNodeId = transferable.endNodeId;

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.mapId = map.id;
		transferable.startNodeId = this.startNode.getId();
		transferable.endNodeId = this.endNode.getId();
		transferable.physicalLinkId = this.physicalLinkId;

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

	public String getTyp()
	{
		return typ;
	}

	/**
	 * Используется для для загрузки класса из базы данных
	 */
	public void updateLocalFromTransferable()
	{
		this.startNode = (MapNodeElement )
				Pool.get(MapSiteNodeElement.typ, startNodeId);
		if(this.startNode == null)
			this.startNode = (MapNodeElement )
					Pool.get(MapPhysicalNodeElement.typ, startNodeId);

		this.endNode = (MapNodeElement )
				Pool.get(MapSiteNodeElement.typ, endNodeId);
		if(this.endNode == null)
			this.endNode = (MapNodeElement )
					Pool.get(MapPhysicalNodeElement.typ, endNodeId);

		this.map = (Map )Pool.get(Map.typ, this.mapId);
	}

	public Object getTransferable()
	{
		return transferable;
	}

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	public boolean getAlarmState()
	{
		return getMap().getPhysicalLink(getPhysicalLinkId()).getAlarmState();
	}

	public boolean isSelectionVisible()
	{
		return isSelected() || getMap().getPhysicalLink(getPhysicalLinkId()).isSelectionVisible();
	}

	public boolean isVisible(Rectangle2D.Double visibleBounds)
	{
		return visibleBounds.intersectsLine(
			getStartNode().getAnchor().x,
			getStartNode().getAnchor().y,
			getEndNode().getAnchor().x,
			getEndNode().getAnchor().y);
//		return visibleBounds.contains(getStartNode().getAnchor()) 
//			|| visibleBounds.contains(getEndNode().getAnchor());
	}

	/**
	 * Рисуем NodeLink взависимости от того выбрана она или нет,
	 * а так же если она выбрана выводим её рамер
	 */
	public void paint (Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!isVisible(visibleBounds))
			return;

		BasicStroke basistroke;
		Color color;
		int lineSize;

		lineSize = getMap().getPhysicalLink(getPhysicalLinkId()).getLineSize();
		basistroke = (BasicStroke )getMap().getPhysicalLink(getPhysicalLinkId()).getStroke();
		color = getMap().getPhysicalLink(getPhysicalLinkId()).getColor();
	
		Stroke str = new BasicStroke(
				lineSize, 
				basistroke.getEndCap(), 
				basistroke.getLineJoin(), 
				basistroke.getMiterLimit(), 
				basistroke.getDashArray(), 
				basistroke.getDashPhase());

		paint(g, visibleBounds, str, color);

		MapCoordinatesConverter converter = getMap().getConverter();
		Point from = converter.convertMapToScreen(startNode.getAnchor());
		Point to = converter.convertMapToScreen(endNode.getAnchor());

		//Рисовать табличку с длинной NodeLink
		if ( MapPropertiesManager.isShowLength() )
		{
			int fontHeight = g.getFontMetrics().getHeight();
			String text = getSizeAsString() + " " + MapPropertiesManager.getMetric();
			int textWidth = g.getFontMetrics().stringWidth(text);
			int centerX = (from.x + to.x) / 2;
			int centerY = (from.y + to.y) / 2;

			g.setColor(MapPropertiesManager.getBorderColor());
			g.setFont(MapPropertiesManager.getFont());

			labelBox = new Rectangle(
					centerX,
					centerY - fontHeight + 2,
					textWidth,
					fontHeight);

			g.drawRect(
					centerX,
					centerY - fontHeight + 2,
					textWidth,
					fontHeight);

			g.setColor(MapPropertiesManager.getTextBackground());
			g.fillRect(
					centerX,
					centerY - fontHeight + 2,
					textWidth,
					fontHeight);

			g.setColor(MapPropertiesManager.getTextColor());
			g.drawString(
					text,
					centerX,
					centerY);
		}
	}

	public void paint (Graphics g, Rectangle2D.Double visibleBounds, Stroke stroke, Color color)
	{
		if(!isVisible(visibleBounds))
			return;

		updateLengthLt();

		MapCoordinatesConverter converter = getMap().getConverter();

		Point from = converter.convertMapToScreen(startNode.getAnchor());
		Point to = converter.convertMapToScreen(endNode.getAnchor());

		Graphics2D p = (Graphics2D )g;

		p.setStroke( stroke);

		//Если alarm есть то специальный thread будет менять showAlarmState и
		// NodeLink будет мигать
		if ( (this.getAlarmState()) && MapPropertiesManager.isShowAlarmState() )
		{
			p.setColor(this.getAlarmedColor());
		}
		else
		{
			p.setColor(color);
		}

		if (isSelectionVisible())
		{
			p.setColor(MapPropertiesManager.getSelectionColor());
			p.setStroke(new BasicStroke(MapPropertiesManager.getSelectionThickness()));
		}

		p.drawLine(from.x, from.y, to.x, to.y);

		if (isSelectionVisible())
		{
			p.setStroke(MapPropertiesManager.getSelectionStroke());

			double length = Math.sqrt( 
					(from.x - to.x) * (from.x - to.x) 
					+ (from.y - to.y) * (from.y - to.y) );

			// рисуем по линии выделения, которые идут параллельно фрагменту
			// с отступом 4 и 6 точек с каждой стороны
			double l = 4;
			double l1 = 6;
			
			// a - угол наклона
			double sinA = (from.y - to.y) / length;

			double cosA = (from.x - to.x) / length;

			int lxshift = (int )(l * sinA);
			int lyshift = (int )(l * cosA);

			int l1xshift = (int )(l1 * sinA);
			int l1yshift = (int )(l1 * cosA);

			p.setColor(MapPropertiesManager.getFirstSelectionColor());
			p.drawLine(
					from.x + lxshift, 
					from.y - lyshift, 
					to.x + lxshift, 
					to.y - lyshift);
			p.drawLine(
					from.x - lxshift, 
					from.y + lyshift, 
					to.x - lxshift, 
					to.y + lyshift);

			p.setColor(MapPropertiesManager.getSecondSelectionColor());
			p.drawLine(
					from.x + l1xshift, 
					from.y - l1yshift, 
					to.x + l1xshift, 
					to.y - l1yshift);
			p.drawLine(
					from.x - l1xshift, 
					from.y + l1yshift, 
					to.x - l1xshift, 
					to.y + l1yshift);
		}

	}

	private static Polygon searchPolygon = new Polygon(new int[6], new int[6], 6);

	/**
	 * точка находится на фрагменте, если она находится в рамках линий выделения
	 */
	public boolean isMouseOnThisObject(Point currentMousePoint)
	{
		MapCoordinatesConverter converter = getMap().getConverter();

		int[] xx = searchPolygon.xpoints;
		int[] yy = searchPolygon.ypoints;

		Point from = converter.convertMapToScreen(startNode.getAnchor());
		Point to = converter.convertMapToScreen(endNode.getAnchor());

		int minX = (int )from.getX();
		int maxX = (int )to.getX();

		int minY = (int )from.getY();
		int maxY = (int )to.getY();
		
		int mouseTolerancy = MapPropertiesManager.getMouseTolerancy();

		if (Math.abs(maxX - minX) < Math.abs(maxY - minY))
		{
			xx[0] = minX - mouseTolerancy; yy[0] = minY;
			xx[1] = maxX - mouseTolerancy; yy[1] = maxY;
			xx[2] = maxX; yy[2] = maxY + mouseTolerancy;
			xx[3] = maxX + mouseTolerancy; yy[3] = maxY;
			xx[4] = minX + mouseTolerancy; yy[4] = minY;
			xx[5] = minX; yy[5] = minY - mouseTolerancy;
		}
		else
		{
			xx[0] = minX; yy[0] = minY + mouseTolerancy;
			xx[1] = maxX; yy[1] = maxY + mouseTolerancy;
			xx[2] = maxX + mouseTolerancy; yy[2] = maxY;
			xx[3] = maxX; yy[3] = maxY - mouseTolerancy;
			xx[4] = minX; yy[4] = minY - mouseTolerancy;
			xx[5] = minX - mouseTolerancy; yy[5] = minY;
		}

		searchPolygon.invalidate();
		if(searchPolygon.contains(currentMousePoint))
		{
			return true;
		}
		return false;
	}

	public boolean isMouseOnThisObjectsLabel(Point currentMousePoint)
	{
//		if(getMap().linkState == MapContext.SHOW_NODE_LINK 
//				&& this.getShowSizeContext() )
		return labelBox.contains(currentMousePoint);
	}
	
	public String getPhysicalLinkId()
	{
		return this.physicalLinkId;
	}
	
	public void setPhysicalLinkId(String physicalLinkId)
	{
		this.physicalLinkId = physicalLinkId;
		if(getStartNode() instanceof MapPhysicalNodeElement)
			((MapPhysicalNodeElement )getStartNode()).setPhysicalLinkId(physicalLinkId);
		if(getEndNode() instanceof MapPhysicalNodeElement)
			((MapPhysicalNodeElement )getEndNode()).setPhysicalLinkId(physicalLinkId);
	}

	public Rectangle getLabelBox()
	{
		return labelBox;
	}

	/**
	 * Возвращяет длинну линии с одной цифрой после запятой
	 */
	public String getSizeAsString()
	{
		double d = getLengthLt();

		double d100 = d * 10;
		int i = (int )d100;
		double retd = (double )i;

		return String.valueOf(retd / 10.0);
	}

	/**
	 * Получить топологическую длинну NodeLink
	 */
	public double getLengthLt()
	{
		return lengthLt;
	}
	
	public void updateLengthLt()
	{
		MapCoordinatesConverter converter = getMap().getConverter();

		if(converter != null)
			lengthLt = converter.distance(startNode.getAnchor(), endNode.getAnchor());
	}

	/**
	 * Получить строительную длинну NodeLink
	 */
//	public double getLengthLf()
//	{
//		double Kd = getMap().getPhysicalLink(getPhysicalLinkId()).getKd();
//		return getLengthLt() * Kd;
//	}

	/**
	 * получить дистанцию от узла до точки
	 */
//	public double getLength(MapNodeElement mne, Point point)
//	{
//		MapCoordinatesConverter converter = getMap().getConverter();
//
//		Point2D.Double from = converter.convertScreenToMap(point);
//		Point2D.Double to = new Point2D.Double(mne.getAnchor().x, mne.getAnchor().y);
//
//		return converter.distance(from, to);
//	}

	/**
	 * получить строительную дистанцию от узла до точки
	 */
//	public double getLengthLf(MapNodeElement mne, Point point)
//	{
//		double Kd = getMap().getPhysicalLink(getPhysicalLinkId()).getKd();
//		return getLength(mne, point) * Kd;
//	}

	/**
	 * установить дистанцию от противоположного узла
	 */
	public void setSizeFrom(MapNodeElement node, double dist)
	{
		MapNodeElement oppositeNode = (startNode.equals(node)) ? endNode : startNode;

		double prevDist = getLengthLt();
		
		double coef = dist / prevDist;

		double absc = coef * (node.getAnchor().x - oppositeNode.getAnchor().x) + oppositeNode.getAnchor().x;
		double ordi = coef * (node.getAnchor().y - oppositeNode.getAnchor().y) + oppositeNode.getAnchor().y;

		node.setAnchor(new Point2D.Double(absc, ordi));
		
		updateLengthLt();
	}

	public double getScreenLength()
	{
		MapCoordinatesConverter converter = getMap().getConverter();
		
		Point start = converter.convertMapToScreen(startNode.getAnchor());
		Point end = converter.convertMapToScreen(endNode.getAnchor());

		return Math.sqrt( 
				(end.x - start.x) * (end.x - start.x) +
				(end.y - start.y) * (end.y - start.y) );
	}


	protected double cosB;
	protected double sinB;
	
	public void calcScreenSlope()
	{
		MapCoordinatesConverter converter = getMap().getConverter();
		
		Point start = converter.convertMapToScreen(startNode.getAnchor());
		Point end = converter.convertMapToScreen(endNode.getAnchor());

		double nodeLinkLength =  Math.sqrt( 
				(end.x - start.x) * (end.x - start.x) +
				(end.y - start.y) * (end.y - start.y) );

		cosB = (end.y - start.y) / nodeLinkLength;

		sinB = (end.x - start.x) / nodeLinkLength;
	}
	
	public double getScreenSin()
	{
		return sinB;
	}
	
	public double getScreenCos()
	{
		return cosB;
	}

	/**
	 * получить центр (середину) фрагмента линии
	 */
	public Point2D.Double getAnchor()
	{
		return new Point2D.Double(
				(startNode.getAnchor().getX() + endNode.getAnchor().getX()) / 2,
				(startNode.getAnchor().getY() + endNode.getAnchor().getY()) / 2);
	}
	
	/**
	 * получить текущее состояние
	 */
	public MapElementState getState()
	{
		return new MapNodeLinkElementState(this);
	}

	/**
	 * восстановить состояние
	 */
	public void revert(MapElementState state)
	{
		super.revert(state);
		
		MapNodeLinkElementState mnles = (MapNodeLinkElementState )state;

		setPhysicalLinkId(mnles.physicalLinkId);

		updateLengthLt();
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(mapId);
		out.writeObject(physicalLinkId);

		startNodeId = getStartNode().getId();
		endNodeId = getEndNode().getId();

		out.writeObject(startNodeId);
		out.writeObject(endNodeId);
		out.writeObject(attributes);
		
		out.writeDouble(lengthLt);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		mapId = (String )in.readObject();
		physicalLinkId = (String )in.readObject();
		startNodeId = (String )in.readObject();
		endNodeId = (String )in.readObject();
		attributes = (HashMap )in.readObject();
		
		lengthLt = in.readDouble();

		transferable = new MapNodeLinkElement_Transferable();

		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}
}
