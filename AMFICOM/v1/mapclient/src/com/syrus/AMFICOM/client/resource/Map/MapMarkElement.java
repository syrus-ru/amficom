package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.General.ElementAttribute_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapMarkElement_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.ImageIcon;

public final class MapMarkElement extends MapNodeElement implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "mapmarkelement";

	protected MapMarkElement_Transferable transferable;

	//–азмер пиктограммы маркера
	public static final Rectangle DEFAULT_BOUNDS = new Rectangle(14, 14);
	
	public static final String IMAGE_NAME = "mark";
	public static final String IMAGE_PATH = "images/mark.gif";

	protected String linkId = "";
	protected double distance = 0.0;

	protected Point2D.Double bufferAnchor = new Point2D.Double( 0, 0);

	protected MapNodeLinkElement nodeLink;

	protected MapPhysicalLinkElement link;

	public MapMarkElement()
	{
		setImageId(IMAGE_NAME);
		transferable = new MapMarkElement_Transferable();

		attributes = new HashMap();
	}
	
	public MapMarkElement(
			String id,
			Map map,
			MapPhysicalLinkElement link,
			double len)
	{
		setId(id);
		setName(id);
		this.map = map;
		this.link = link;
		this.linkId = link.getId();
		setImage(IMAGE_PATH);
		
		if(map != null)
			mapId = map.getId();
		attributes = new HashMap();

		moveToFromStartLt(len);

		transferable = new MapMarkElement_Transferable();
	}

	public MapMarkElement(MapMarkElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
		setImageId(IMAGE_NAME);

		attributes = new HashMap();
	}

	public Image getImage()
	{
		return icon;
	}
	
	/**
	 * установить идентификатор изображени€, по которому определ€етс€ 
	 * пиктограмма
	 */
	public void setImage(String iconPath)
	{
		imageId = iconPath;

		int width = (int )Math.round(getBounds().getWidth());
		int height = (int )Math.round(getBounds().getHeight());
		ImageIcon imageIcon = new ImageIcon(iconPath);
		icon = imageIcon.getImage().getScaledInstance(
			width,
			height,
			Image.SCALE_SMOOTH);
		loadImage(icon);
	}

	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		String cloned_id = (String)Pool.get("mapclonedids", id);
		if (cloned_id != null)
			return Pool.get(MapMarkElement.typ, cloned_id);

		MapMarkElement mme = new MapMarkElement(
				dataSource.GetUId(MapMarkElement.typ),
				(Map)map.clone(dataSource), 
				(MapPhysicalLinkElement )link.clone(dataSource),
				this.distance);
				
		mme.anchor = new Point2D.Double(anchor.x, anchor.y);
		mme.bounds = new Rectangle(bounds);
		mme.alarmState = alarmState;
		mme.bufferAnchor = new Point2D.Double(bufferAnchor.x, bufferAnchor.y);
		mme.changed = changed;
		mme.description = description;
		mme.name = name;
		mme.nodeLink = (MapNodeLinkElement )nodeLink.clone(dataSource);
		mme.scaleCoefficient = scaleCoefficient;
		mme.selected = selected;

		Pool.put(MapMarkElement.typ, mme.getId(), mme);
		Pool.put("mapclonedids", id, mme.getId());

		mme.attributes = new HashMap();
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mme.attributes.put(ea2.type_id, ea2);
		}

		return mme;
	}
	
	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.description = transferable.description;
		this.anchor.x = Double.parseDouble(transferable.longitude);
		this.anchor.y = Double.parseDouble(transferable.latitude);
		this.mapId = transferable.mapId;
		this.linkId = transferable.physicalLinkId;
		this.distance = transferable.distance;

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

//ѕередаЄм переменные в transferable котора€ используетс€ дл€ передачи их в базу данных
	public void setTransferableFromLocal()
	{
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.description = this.description;
		transferable.longitude = String.valueOf(this.anchor.x);
		transferable.latitude = String.valueOf(this.anchor.y);
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

	public String getTyp()
	{
		return typ;
	}

	//»спользуетс€ дл€ дл€ загрузки класса из базы данных
	public void updateLocalFromTransferable()
	{
		link = (MapPhysicalLinkElement )Pool.get(MapPhysicalLinkElement.typ, linkId);
		if(link != null)
		{
			Map mc = (Map)Pool.get(com.syrus.AMFICOM.Client.Resource.Map.Map.typ, this.mapId);
		}
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public ObjectResourceModel getModel()
	{
		return null;//new MapMarkElementModel(this);
	}
	
	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return null;//new MapMarkElementDisplayModel();
	}

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
/*
	public Point2D.Double getAnchor1()
	{
		MapCoordinatesConverter converter = getMap().getConverter();

		if ( anchor != bufferAnchor )
		{
			//–исование о пределение координат маркера происходит путм проецировани€ координат
			//курсора	на линию на которой маркер находитс€
			double startNodeX = converter.convertMapToScreen(startNode.getAnchor()).x;
			double startNodeY = converter.convertMapToScreen(startNode.getAnchor()).y;

			double endNodeX = converter.convertMapToScreen(endNode.getAnchor()).x;
			double endNodeY = converter.convertMapToScreen(endNode.getAnchor()).y;

			double nodeLinkLength =  Math.sqrt( 
					(endNodeX - startNodeX) * (endNodeX - startNodeX) +
					(endNodeY - startNodeY) * (endNodeY - startNodeY) );

			double thisX = converter.convertMapToScreen(anchor).x;
			double thisY = converter.convertMapToScreen(anchor).y;

			double lengthFromStartNode = Math.sqrt( 
					(thisX - startNodeX) * (thisX - startNodeX) +
					(thisY - startNodeY) * (thisY - startNodeY) );

			double cos_b =  
					(endNodeY - startNodeY) /
					(Math.sqrt( 
						(endNodeX - startNodeX) * (endNodeX - startNodeX) +
						(endNodeY - startNodeY) * (endNodeY - startNodeY) ) );

			double sin_b =  
					(endNodeX - startNodeX) /
					(Math.sqrt( 
						(endNodeX - startNodeX) * (endNodeX - startNodeX) +
						(endNodeY - startNodeY) * (endNodeY - startNodeY) ) );

			if ( lengthFromStartNode > nodeLinkLength )
			{
				Iterator it = link.getNodeLinksAt(endNode).iterator();
				while(it.hasNext())
				{
					MapNodeLinkElement nl = (MapNodeLinkElement) it.next();
					if(nl != nodeLink)
					{
						startNode = endNode;
						nodeLink = nl;
						endNode = nl.getOtherNode(startNode);
						lengthFromStartNode -= nodeLinkLength;
					}
					lengthFromStartNode = nodeLinkLength;
				}
			}

			if ( lengthFromStartNode < 0 )
			{
				for(Iterator it = link.getNodeLinksAt(startNode).iterator(); it.hasNext();)
				{
					MapNodeLinkElement nl = (MapNodeLinkElement )it.next();
					if(nl != nodeLink)
					{
						endNode = startNode;
						nodeLink = nl;
						startNode = nl.getOtherNode(endNode);
						lengthFromStartNode += nl.getScreenLength();
					}
					lengthFromStartNode = 0;
				}

			}

			anchor = converter.convertScreenToMap(
				new Point(
					(int)Math.round(startNodeX + sin_b * ( lengthFromStartNode )),
					(int)Math.round(startNodeY + cos_b * ( lengthFromStartNode )) ) );
			bufferAnchor = anchor;
		}

		return anchor;
	}
*/
	public void setAnchor(Point2D.Double aAnchor)
	{
		anchor = aAnchor;
		distance = getFromStartLengthLt();
	}

	//рисуем маркер
	public void paint (Graphics g)
	{
		super.paint(g);

		MapCoordinatesConverter converter = getMap().getConverter();

		Point p = converter.convertMapToScreen( getAnchor());

		Graphics2D pg = ( Graphics2D)g;
		pg.setStroke(new BasicStroke(MapPropertiesManager.getBorderThickness()));
		
		int width = getBounds().width;
		int height = getBounds().height;

		pg.setColor(MapPropertiesManager.getBorderColor());
		pg.setBackground(MapPropertiesManager.getTextBackground());
		pg.setFont(MapPropertiesManager.getFont());

		String s1 = getName();
		pg.drawRect(
				p.x + width, 
				p.y - g.getFontMetrics().getHeight() + 2,
				pg.getFontMetrics().stringWidth(s1), 
				pg.getFontMetrics().getHeight() );

		pg.setColor(MapPropertiesManager.getTextBackground());
		pg.fillRect(
				p.x + width, 
				p.y - g.getFontMetrics().getHeight() + 2,
				pg.getFontMetrics().stringWidth(s1), 
				pg.getFontMetrics().getHeight() );

		g.setColor(MapPropertiesManager.getTextColor());
		g.drawString(
				s1, 
				p.x + width, 
				p.y - 2 );
	}
/*
	public void move (double deltaX, double deltaY)
	{
		super.move(deltaX, deltaY);
		distance = getFromStartLengthLt();
	}
*/
	public double getFromStartLengthLt()
	{
		link.sortNodeLinks();

		double pathLength = 0;
		
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nl = (MapNodeLinkElement )it.next();
			if(nl == nodeLink)
			{
				pathLength += getSizeInDoubleLt();
				break;
			}
			else
			{
				pathLength += nl.getLengthLt();
			}
		}
		return pathLength;
	}
/*
		MapNodeElement bufferStartNode;
		MapNodeElement bufferEndNode;
		MapNodeLinkElement startNodeLink = (MapNodeLinkElement)link.getNodeLinksAt(link.startNode).get(0);
		bufferStartNode = link.startNode;

		do
		{
			if ( startNodeLink.getStartNode() == bufferStartNode)
				bufferEndNode = startNodeLink.getEndNode();
			else
				bufferEndNode = startNodeLink.getStartNode();

			if ( startNodeLink == nodeLink)
			{
				if ( bufferStartNode == startNode)
					return path_length + getSizeInDoubleLt();
				else
					return path_length + startNodeLink.getSizeInDoubleLt() - getSizeInDoubleLt();
			}
			else
			{
				path_length = path_length + startNodeLink.getSizeInDoubleLt();

				Iterator it = link.getNodeLinksAt(bufferEndNode).iterator();
				MapNodeLinkElement bufferNodeLink = (MapNodeLinkElement )it.next();

				while( it.hasNext() && ( bufferNodeLink == startNodeLink ) )
				{
					bufferNodeLink = (MapNodeLinkElement )it.next();
				}
				startNodeLink = bufferNodeLink;
				bufferStartNode = bufferEndNode;
			}

		}
		while ( link.getNodeLinks().contains(nodeLink) );
		return 0;
	}
*/	
/*
	public double getFromStartLengthLf()
	{
		double path_length = 0;
		MapNodeElement bufferStartNode;
		MapNodeElement bufferEndNode;
		MapNodeLinkElement startNodeLink = (MapNodeLinkElement )getNodeLinksContainingNode(link.startNode).get(0);
		bufferStartNode = link.startNode;

		do
		{
			if ( startNodeLink.startNode == bufferStartNode)
				bufferEndNode = startNodeLink.endNode;
			else
				bufferEndNode = startNodeLink.startNode;

			if ( startNodeLink == nodeLink)
			{
				if ( bufferStartNode == startNode)
					return path_length + getSizeInDoubleLf();
				else
					return path_length + startNodeLink.getSizeInDoubleLf() - getSizeInDoubleLf();
			}
			else
			{
				path_length = path_length + startNodeLink.getSizeInDoubleLf();

				Iterator it = getNodeLinksContainingNode(bufferEndNode).iterator();
				MapNodeLinkElement bufferNodeLink = (MapNodeLinkElement)it.next();

				while( it.hasNext() && ( bufferNodeLink == startNodeLink ) )
				{
					bufferNodeLink = (MapNodeLinkElement)it.next();
				}
				startNodeLink = bufferNodeLink;
				bufferStartNode = bufferEndNode;
			}

		}
		while ( link.getNodeLinks().contains(nodeLink) );
		return 0;
	}
*/
	public double getFromEndLengthLt()
	{
		link.sortNodeLinks();

		double pathLength = 0;
		
		ListIterator it = link.getNodeLinks().listIterator(
				link.getNodeLinks().size());
		for(;it.hasPrevious();)
		{
			MapNodeLinkElement nl = (MapNodeLinkElement )it.previous();
			if(nl == nodeLink)
			{
				pathLength += nl.getLengthLt() - getSizeInDoubleLt();
				break;
			}
			else
				pathLength += nl.getLengthLt();
		}
		return pathLength;
	}
/*
		double path_length = 0;
		MapNodeElement bufferStartNode;
		MapNodeElement bufferEndNode;
		MapNodeLinkElement endNodeLink = (MapNodeLinkElement )getNodeLinksContainingNode(link.endNode).get(0);
		bufferEndNode = link.endNode;

		do
		{
			if ( endNodeLink.endNode == bufferEndNode)
			{
				bufferStartNode = endNodeLink.startNode;
			}
			else
			{
				bufferStartNode = endNodeLink.endNode;
			}

			if ( endNodeLink == nodeLink)
			{
				if ( bufferStartNode == startNode)
				{
					return path_length + endNodeLink.getSizeInDoubleLt() - getSizeInDoubleLt();
				}
				else
				{
					return path_length + getSizeInDoubleLt();
				}
			}
			else
			{
				path_length = path_length + endNodeLink.getSizeInDoubleLt();

				Iterator it = link.getNodeLinksAt(bufferStartNode).iterator();
				MapNodeLinkElement bufferNodeLink = (MapNodeLinkElement)it.next();

				while( it.hasNext() && ( bufferNodeLink == endNodeLink ) )
				{
					bufferNodeLink = (MapNodeLinkElement)it.next();
				}
				endNodeLink = bufferNodeLink;
				bufferEndNode = bufferStartNode;
			}

		}
		while ( link.getNodeLinks().contains(nodeLink) );
		return 0;
	}
*/
/*
	public double getFromEndLengthLf()
	{
		double path_length = 0;
		MapNodeElement bufferStartNode;
		MapNodeElement bufferEndNode;
		MapNodeLinkElement endNodeLink = (MapNodeLinkElement)getNodeLinksContainingNode(link.endNode).get(0);
		bufferEndNode = link.endNode;

		do
		{
			if ( endNodeLink.endNode == bufferEndNode)
			{
				bufferStartNode = endNodeLink.startNode;
			}
			else
			{
				bufferStartNode = endNodeLink.endNode;
			}

			if ( endNodeLink == nodeLink)
			{
				if ( bufferStartNode == startNode)
				{
					return path_length + endNodeLink.getSizeInDoubleLf() - getSizeInDoubleLf();
				}
				else
				{
					return path_length + getSizeInDoubleLf();
				}
			}
			else
			{
				path_length = path_length + endNodeLink.getSizeInDoubleLf();

				Iterator it = getNodeLinksContainingNode(bufferStartNode).iterator();
				MapNodeLinkElement bufferNodeLink = (MapNodeLinkElement)it.next();

				while( it.hasNext() && ( bufferNodeLink == endNodeLink ) )
				{
					bufferNodeLink = (MapNodeLinkElement)it.next();
				}
				endNodeLink = bufferNodeLink;
				bufferEndNode = bufferStartNode;
			}

		}
		while ( link.getNodeLinks().contains(nodeLink) );
		return 0;
	}
*/
	public double getSizeInDoubleLt()
	{
		MapCoordinatesConverter converter = getMap().getConverter();
		
		link.sortNodes();
		
		List nodes = link.getSortedNodes();
		
		Point2D.Double from;
		Point2D.Double to = getAnchor();

		if(nodes.indexOf(nodeLink.getStartNode()) < nodes.indexOf(nodeLink.getEndNode()))
			from = nodeLink.getStartNode().getAnchor();
		else
			from = nodeLink.getEndNode().getAnchor();

		return converter.distance(from, to);
	}

/*
	public double getSizeInDoubleLf()
	{
		double Kd = getMap().getPhysicalLink(nodeLink.getPhysicalLinkId()).getKd();
		return getSizeInDoubleLt() * Kd;
	}
*/
	public double getDistance()
	{
		return distance;
	}

	//ѕередвинуть в точку на заданной рассто€нии от начала
	public void moveToFromStartLt(double distance)
	{
		MapCoordinatesConverter converter = getMap().getConverter();

		this.distance = distance;
		
		link.sortNodeLinks();

		if ( distance > link.getLengthLt())
		{
			moveToFromStartLt(link.getLengthLt());
			return;
		}

		double path = 0;
		
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			nodeLink = (MapNodeLinkElement )it.next();
			nodeLink.updateLengthLt();
			if(path + nodeLink.getLengthLt() > distance)
				break;
			else
				path += nodeLink.getLengthLt();
		}
		
		path = distance - path;

		double nodeLinkLength =  nodeLink.getScreenLength();
		nodeLink.calcScreenSlope();
		double cos_b = nodeLink.getScreenCos();
		double sin_b = nodeLink.getScreenSin();
		
		Point2D.Double dsp = nodeLink.getStartNode().getAnchor();
		Point sp = converter.convertMapToScreen(dsp);

		setAnchor(
			converter.convertScreenToMap(
				new Point(
					(int)Math.round(sp.x + sin_b * path),
					(int)Math.round(sp.y + cos_b * path) ) ) );
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
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

	private void readObject(java.io.ObjectInputStream in)
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
		updateLocalFromTransferable();

//		moveToFromStart(this.distance);
	}


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
}
