package com.syrus.AMFICOM.Client.Resource.Map;

import com.ofx.geometry.SxDoublePoint;

import com.syrus.AMFICOM.CORBA.Map.MapMarkElement_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.ElementAttribute_Transferable;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.VoidStrategy;
import com.syrus.AMFICOM.Client.Map.UI.Display.MapMarkElementDisplayModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.DEF;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import java.io.IOException;
import java.io.Serializable;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

//A0A
public class MapMarkElement extends MapNodeElement implements Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "mapmarkelement";
	MapMarkElement_Transferable transferable;

//–азмер пиктограммы маркера
	public final static Rectangle defaultBounds = new Rectangle(14, 14);
	protected Rectangle bounds = new Rectangle(defaultBounds);

	public String id = "";
	public String name = "";
	public String description = "";
	public String owner_id = "";

	public String mapContextID = "";

	public String link_id = "";
	public double distance = 0.0;

	public Color color = Color.green;

	protected SxDoublePoint anchor = new SxDoublePoint( 0, 0);
	protected SxDoublePoint bufferAnchor = new SxDoublePoint( 0, 0);

	public MapNodeLinkElement nodeLink;
	public MapNodeElement startNode;
	public MapNodeElement endNode;
	//public double length;

	MapPhysicalLinkElement link;
	
	public MapMarkElement()
	{
		setImageID("images/mark.gif");
		transferable = new MapMarkElement_Transferable();

		attributes = new Hashtable();
//		moveToFromStart(this.distance);
	}
	
	public MapMarkElement(
			MapContext myMapContext,
			MapPhysicalLinkElement link,
			double mylen)
	{
		mapContext = myMapContext;
		this.link = link;
		this.link_id = link.getId();
		setImageID("images/mark.gif");
		
		if(mapContext != null)
			mapContextID = mapContext.getId();
		attributes = new Hashtable();

		moveToFromStart(mylen);

		transferable = new MapMarkElement_Transferable();
	}

	public MapMarkElement(MapMarkElement_Transferable Tmyransferable)
	{
		this.transferable = Tmyransferable;
		setLocalFromTransferable();
		setImageID("images/mark.gif");

		attributes = new Hashtable();
//		moveToFromStart(this.distance);
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("mapclonedids", id);
		if (cloned_id != null)
			return Pool.get(MapMarkElement.typ, cloned_id);

		MapMarkElement mme = new MapMarkElement(
				(MapContext )mapContext.clone(dataSource), 
				(MapPhysicalLinkElement )link.clone(dataSource),
				this.distance);
				
		mme.id = dataSource.GetUId(MapMarkElement.typ);
		mme.anchor = new SxDoublePoint(anchor.x, anchor.y);
		mme.bounds = new Rectangle(bounds);
		mme.alarmState = alarmState;
		mme.bufferAnchor = new SxDoublePoint(bufferAnchor.x, bufferAnchor.y);
		mme.changed = changed;
		mme.color = new Color(color.getRGB());
		mme.description = description;
		mme.endNode = (MapNodeElement )endNode.clone(dataSource);
		mme.name = name;
		mme.nodeLink = (MapNodeLinkElement )nodeLink.clone(dataSource);
		mme.owner_id = dataSource.getSession().getUserId();
		mme.scaleCoefficient = scaleCoefficient;
		mme.selected = selected;
		mme.selectedNodeLineSize = selectedNodeLineSize;
		mme.startNode = (MapNodeElement )startNode.clone(dataSource);
		mme.showAlarmState = showAlarmState;
		mme.type_id = type_id;

		Pool.put(MapMarkElement.typ, mme.getId(), mme);
		Pool.put("mapclonedids", id, mme.getId());

		mme.attributes = new Hashtable();
		for(Enumeration enum = attributes.elements(); enum.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )enum.nextElement();
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
		this.owner_id = transferable.owner_id;
		this.mapContextID = transferable.map_id;
//		this.setImageID( transferable.symbol_id);
		this.link_id = transferable.link_id;
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
		transferable.owner_id = mapContext.user_id ;
		transferable.map_id = mapContext.getId();
		transferable.symbol_id = "";
		transferable.link_id = this.link_id;
		transferable.distance = this.distance;

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Enumeration e = attributes.elements(); e.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute)e.nextElement();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}
	}

	public String getTyp()
	{
		return typ;
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public Object getTransferable()
	{
		return transferable;
	}
	//»спользуетс€ дл€ дл€ загрузки класса из базы данных
	public void updateLocalFromTransferable()
	{
		link = (MapPhysicalLinkElement )Pool.get(MapPhysicalLinkElement.typ, link_id);
		if(link != null)
		{
			MapContext mc = (MapContext )Pool.get(MapContext.typ, this.mapContextID);
			startNode = mc.getNode(link.startNode_id);
			endNode = mc.getNode(link.endNode_id);
		}
	}

	public ObjectResourceModel getModel()
	{
		return new MapMarkElementModel(this);
	}
	
	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new MapMarkElementDisplayModel();
	}

	public void setImageID(String iconPath)
	{

		imageID = iconPath;
		ImageIcon myImageIcon = new ImageIcon(imageID);
		icon = myImageIcon.getImage().getScaledInstance(
				(int)getBounds().getWidth(),
				(int)getBounds().getHeight(),
				Image.SCALE_SMOOTH);
	}

	public String getImageID()
	{
		return imageID;
	}

	public SxDoublePoint getAnchor()
	{
		if ( anchor !=bufferAnchor )
		{
			//–исование о пределение координат маркера происходит путм проецировани€ координат
			//курсора	на линию на которой маркер находитс€
			double startNodeX = getLogicalNetLayer().convertLongLatToScreen(startNode.getAnchor()).x;
			double startNodeY = getLogicalNetLayer().convertLongLatToScreen(startNode.getAnchor()).y;

			double endNodeX = getLogicalNetLayer().convertLongLatToScreen(endNode.getAnchor()).x;
			double endNodeY = getLogicalNetLayer().convertLongLatToScreen(endNode.getAnchor()).y;

			double nodeLinkLength =  Math.sqrt( 
					(endNodeX - startNodeX) * (endNodeX - startNodeX) +
					(endNodeY - startNodeY) * (endNodeY - startNodeY) );

			double thisX = getLogicalNetLayer().convertLongLatToScreen(anchor).x;
			double thisY = getLogicalNetLayer().convertLongLatToScreen(anchor).y;

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

			//A0A
			if ( lengthFromStartNode > nodeLinkLength )
			{
				Enumeration e = getNodeLinksContainingNode(endNode).elements();
				while(e.hasMoreElements())
				{
					MapNodeLinkElement myNodeLink = (MapNodeLinkElement) e.nextElement();
					if(myNodeLink != nodeLink)
					{
						startNode = endNode;
						nodeLink = myNodeLink;
						endNode = getMapContext().getOtherNodeOfNodeLink(myNodeLink, startNode);
						lengthFromStartNode -= nodeLinkLength;
					}
					lengthFromStartNode = nodeLinkLength;
				}
			}

			//A0A
			if ( lengthFromStartNode < 0 )
			{
				Enumeration e = getNodeLinksContainingNode(startNode).elements();
				while(e.hasMoreElements())
				{
					MapNodeLinkElement myNodeLink = (MapNodeLinkElement) e.nextElement();
					if(myNodeLink != nodeLink)
					{
						endNode = startNode;
						nodeLink = myNodeLink;
						startNode = getMapContext().getOtherNodeOfNodeLink(myNodeLink, endNode);
						lengthFromStartNode += getNodeLinkScreenLength(myNodeLink);
					}
					lengthFromStartNode = 0;
				}

			}

			//A0A
			anchor = getLogicalNetLayer().convertScreenToLongLat(
				new Point(
					(int)Math.round(startNodeX + sin_b * ( lengthFromStartNode )),
					(int)Math.round(startNodeY + cos_b * ( lengthFromStartNode )) ) );
			bufferAnchor = anchor;
		}

		return anchor;
	}

	public void setAnchor(SxDoublePoint aAnchor)
	{
		anchor = aAnchor;
		distance = getFromStartLengthLt();
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void select()
	{
		selected = true;
	}

	public void deselect()
	{
		selected = false;
	}

	//рисуем маркер
	public void paint (Graphics g)
	{
		Point p = getLogicalNetLayer().convertMapToScreen( getAnchor());

		Graphics2D pg = ( Graphics2D)g;
		pg.setStroke(new BasicStroke(getSelectedNodeLineSize()));

		pg.drawImage(
				icon,
				p.x - icon.getWidth(getLogicalNetLayer()) / 2,
				p.y - icon.getHeight(getLogicalNetLayer()) / 2,
				getLogicalNetLayer());

		//≈сли выбрано то расуем пр€моугольник
		if (isSelected())
		{
			pg.setColor( Color.red);
			pg.drawRect( 
					p.x - icon.getWidth( getLogicalNetLayer()) / 2,
					p.y - icon.getHeight( getLogicalNetLayer()) / 2,
					icon.getWidth( getLogicalNetLayer()),
					icon.getHeight( getLogicalNetLayer()));
		}
		if ( true )
		{
			pg.setColor(Color.black);
			pg.setBackground(Color.white);
			pg.setFont(DEF.font);

			String s1 = getName();
			pg.drawRect(
					p.x + icon.getWidth( getLogicalNetLayer()), 
					p.y - g.getFontMetrics().getHeight() * 1 + 2,
					pg.getFontMetrics().stringWidth(s1), 
					pg.getFontMetrics().getHeight() * 1 );

			pg.setColor(Color.white);
			pg.fillRect(
					p.x + icon.getWidth( getLogicalNetLayer()), 
					p.y - g.getFontMetrics().getHeight() * 1 + 2,
					pg.getFontMetrics().stringWidth(s1), 
					pg.getFontMetrics().getHeight() * 1 );

			g.setColor(Color.black);
			g.drawString(
					s1, 
					p.x + icon.getWidth( getLogicalNetLayer()), 
					p.y - 2 );
		}
/*
		if ( true )
		{
			pg.setColor(Color.black);
			pg.setBackground(Color.white);

			java.text.DecimalFormat df2 = new java.text.DecimalFormat("###,#0.0");

			String s1 = getName();
			String s2 = df2.format(this.getFromStartLength()) + " " + link.getMetric();
			String s3 = df2.format(this.getFromEndLength()) + " " + link.getMetric();

			pg.drawRect(
					p.x + icon.getWidth( getLogicalNetLayer()), 
					p.y - g.getFontMetrics().getHeight() * 3 + 2,
					Math.max(Math.max(pg.getFontMetrics().stringWidth(s1), pg.getFontMetrics().stringWidth(s2)), pg.getFontMetrics().stringWidth(s3)), 
					pg.getFontMetrics().getHeight() * 3 );

			pg.setColor(Color.white);
			pg.fillRect(
					p.x + icon.getWidth( getLogicalNetLayer()), 
					p.y - g.getFontMetrics().getHeight() * 3 + 2,
					Math.max(Math.max(pg.getFontMetrics().stringWidth(s1), pg.getFontMetrics().stringWidth(s2)), pg.getFontMetrics().stringWidth(s3)), 
					pg.getFontMetrics().getHeight() * 3 );

			g.setColor(Color.black);
			g.drawString(
					s1, 
					p.x + icon.getWidth( getLogicalNetLayer()), 
					p.y - g.getFontMetrics().getHeight() * 3 + 2 );
			g.drawString(
					s2, 
					p.x + icon.getWidth( getLogicalNetLayer()), 
					p.y - g.getFontMetrics().getHeight() * 2 + 2 );
			g.drawString(
					s3, 
					p.x + icon.getWidth( getLogicalNetLayer()), 
					p.y - g.getFontMetrics().getHeight() * 1 + 2 );
		}
*/
	}

	public void paint (Graphics g, Point myPoint)
	{
	}

	//A0A
	public boolean isMouseOnThisObject (Point currentMousePoint)
	{
		int width = icon.getWidth( getLogicalNetLayer());
		int height = icon.getHeight( getLogicalNetLayer());
		Point p = getLogicalNetLayer().convertMapToScreen(anchor);
		Rectangle imageBounds = new Rectangle( p.x-width/2 , p.y-height/2 , width, height);
		if (imageBounds.contains(currentMousePoint))
		{
			return true;
		}
		return false;
	}

	//A0A
	public void move (double deltaX, double deltaY)
	{
		double x = anchor.getX();
		double y = anchor.getY();
		this.anchor = new SxDoublePoint(x + deltaX, y + deltaY);
		distance = getFromStartLengthLt();
	}

	//ќбработка событии св€зпнный с Node
	public MapStrategy getMapStrategy(
			ApplicationContext aContext,
			LogicalNetLayer logicalNetLayer,
			MouseEvent me,
			Point sourcePoint)
	{
		int mode = getLogicalNetLayer().getMode();
		int actionMode = logicalNetLayer.getActionMode();

		MapStrategy strategy = new VoidStrategy();
		Point myPoint = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if ((actionMode != LogicalNetLayer.SELECT_ACTION_MODE) &&
				(actionMode != LogicalNetLayer.MOVE_ACTION_MODE) )
			{
				logicalNetLayer.getMapContext().deselectAll();
			}
			select();
			switch (mode)
			{
				case LogicalNetLayer.MOUSE_DRAGGED:
					//ѕроверка того что маркер можно перемещать и его перемещение
					if ( link.getLogicalNetLayer().mapMainFrame
							.aContext.getApplicationModel().isEnabled("mapActionMarkMove"))
					{
						link.select();

//						moveToFromStart(distance);

						//–исование о пределение координат маркера происходит путм проецировани€ координат
						//курсора	на линию на которой маркер находитс€

						double startNodeX = getLogicalNetLayer().convertLongLatToScreen(startNode.getAnchor()).x;
						double startNodeY = getLogicalNetLayer().convertLongLatToScreen(startNode.getAnchor()).y;

						double endNodeX = getLogicalNetLayer().convertLongLatToScreen(endNode.getAnchor()).x;
						double endNodeY = getLogicalNetLayer().convertLongLatToScreen(endNode.getAnchor()).y;

						double nodeLinkLength =  Math.sqrt( 
								(endNodeX - startNodeX) * (endNodeX - startNodeX) +
								(endNodeY - startNodeY) * (endNodeY - startNodeY) );

						double thisX = getLogicalNetLayer().convertLongLatToScreen(getAnchor()).x;
						double thisY = getLogicalNetLayer().convertLongLatToScreen(getAnchor()).y;

						double lengthFromStartNode = Math.sqrt( 
								(thisX - startNodeX) * (thisX - startNodeX) +
								(thisY - startNodeY) * (thisY - startNodeY) );

						//A0A
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

						double mousePointX = myPoint.x;
						double mousePointY = myPoint.y;

						double lengthThisToMousePoint = Math.sqrt( 
								(mousePointX - thisX) * (mousePointX - thisX) +
								(mousePointY - thisY) * (mousePointY - thisY) );

						double cos_a = ( 
							(endNodeX - startNodeX) * (mousePointX - thisX) + 
							(endNodeY - startNodeY)*(mousePointY - thisY) ) /
								(Math.sqrt( 
									(endNodeX - startNodeX) * (endNodeX - startNodeX) +
									(endNodeY - startNodeY) * (endNodeY - startNodeY) ) *
								Math.sqrt( 
									(mousePointX - thisX) * (mousePointX - thisX) +
									(mousePointY - thisY) * (mousePointY - thisY) ) );

						lengthFromStartNode = lengthFromStartNode + cos_a * lengthThisToMousePoint;

						//A0A
						if ( lengthFromStartNode > nodeLinkLength )
						{
							Enumeration e = getNodeLinksContainingNode( endNode).elements();
							while ( e.hasMoreElements())
							{
								MapNodeLinkElement myNodeLink = (MapNodeLinkElement) e.nextElement();
								if ( myNodeLink != nodeLink)
								{
									startNode = endNode;
									nodeLink = myNodeLink;
									endNode = getMapContext().getOtherNodeOfNodeLink(myNodeLink, startNode);
									lengthFromStartNode -= nodeLinkLength;
									return new VoidStrategy();
								}
								lengthFromStartNode = nodeLinkLength;
							}
						}

						//A0A
						if ( lengthFromStartNode < 0 )
						{
							Enumeration e = getNodeLinksContainingNode( startNode).elements();
							while ( e.hasMoreElements())
							{
								MapNodeLinkElement myNodeLink = (MapNodeLinkElement) e.nextElement();
								if ( myNodeLink != nodeLink)
								{
									endNode = startNode;
									nodeLink = myNodeLink;
									startNode = getMapContext().getOtherNodeOfNodeLink(myNodeLink, endNode);
									lengthFromStartNode += getNodeLinkScreenLength(myNodeLink);
									return new VoidStrategy();
								}
								lengthFromStartNode = 0;
							}
						}

						//A0A
						bufferAnchor = getLogicalNetLayer().convertScreenToLongLat(
							new Point(
								(int)Math.round(startNodeX + sin_b * ( lengthFromStartNode )),
								(int)Math.round(startNodeY + cos_b * ( lengthFromStartNode )) ) );

						setAnchor(bufferAnchor);
					}
					break;
			}//switch

		}//SwingUtilities.isLeftMouseButton(me)
		return strategy;
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return mapContext.getLogicalNetLayer();
	}

	public MapContext getMapContext()
	{
		return mapContext;
	}

	public void setMapContext( MapContext myMapContext)
	{
		mapContext = myMapContext;
	}

	// онтекстное меню
	public JPopupMenu getContextMenu(JFrame myFrame)
	{
		return new JPopupMenu();
	}

	//A0A
	public static double getNodeLinkScreenLength( MapNodeLinkElement myNodeLink)
	{
		double x1 = myNodeLink.getLogicalNetLayer().convertLongLatToScreen(myNodeLink.startNode.getAnchor()).x;
		double y1 = myNodeLink.getLogicalNetLayer().convertLongLatToScreen(myNodeLink.startNode.getAnchor()).y;

		double x1_ = myNodeLink.getLogicalNetLayer().convertLongLatToScreen(myNodeLink.endNode.getAnchor()).x;
		double y1_ = myNodeLink.getLogicalNetLayer().convertLongLatToScreen(myNodeLink.endNode.getAnchor()).y;

		return Math.sqrt( 
				(x1_ - x1) * (x1_ - x1) +
				(y1_ - y1) * (y1_ - y1) );
	}
	
	//ѕолучить NodeLinks содержащие данный myNode в данном transmissionPath
	public Vector getNodeLinksContainingNode(MapNodeElement myNode)
	{
		Vector returnNodeLink = new Vector();
		Enumeration e = mapContext.getNodeLinksInPhysicalLink( link.getId()).elements();

		while (e.hasMoreElements())
		{
			MapNodeLinkElement myNodeLink = (MapNodeLinkElement)e.nextElement();

			if ( (myNodeLink.endNode == myNode) || (myNodeLink.startNode == myNode))
			{
				returnNodeLink.add(myNodeLink);
			}

		}

		return returnNodeLink;
	}

	//A0A
	public double getFromStartLengthLt()
	{
		double path_length = 0;
		MapNodeElement bufferStartNode;
		MapNodeElement bufferEndNode;
		MapNodeLinkElement startNodeLink = (MapNodeLinkElement)getNodeLinksContainingNode(link.startNode).get(0);
		bufferStartNode = link.startNode;

		//A0A
		do
		{
			//A0A
			if ( startNodeLink.startNode == bufferStartNode)
				bufferEndNode = startNodeLink.endNode;
			else
				bufferEndNode = startNodeLink.startNode;

			//A0A
			if ( startNodeLink == nodeLink)
			{
				if ( bufferStartNode == startNode)
					return path_length + getSizeInDoubleLt();
				else
					return path_length + startNodeLink.getSizeInDoubleLt() - getSizeInDoubleLt();
			}
			//A0A
			else
			{
				path_length = path_length + startNodeLink.getSizeInDoubleLt();

				Enumeration e = getNodeLinksContainingNode(bufferEndNode).elements();
				MapNodeLinkElement bufferNodeLink = (MapNodeLinkElement)e.nextElement();

				while( e.hasMoreElements() && ( bufferNodeLink == startNodeLink ) )
				{
					bufferNodeLink = (MapNodeLinkElement)e.nextElement();
				}
				startNodeLink = bufferNodeLink;
				bufferStartNode = bufferEndNode;
			}

		}
		//A0A
		while ( getMapContext().getNodeLinksInPhysicalLink(link.getId())
					 .contains(nodeLink) );
		return 0;
	}

	//A0A
	public double getFromStartLengthLf()
	{
		double path_length = 0;
		MapNodeElement bufferStartNode;
		MapNodeElement bufferEndNode;
		MapNodeLinkElement startNodeLink = (MapNodeLinkElement)getNodeLinksContainingNode(link.startNode).get(0);
		bufferStartNode = link.startNode;

		//A0A
		do
		{
			//A0A
			if ( startNodeLink.startNode == bufferStartNode)
				bufferEndNode = startNodeLink.endNode;
			else
				bufferEndNode = startNodeLink.startNode;

			//A0A
			if ( startNodeLink == nodeLink)
			{
				if ( bufferStartNode == startNode)
					return path_length + getSizeInDoubleLf();
				else
					return path_length + startNodeLink.getSizeInDoubleLf() - getSizeInDoubleLf();
			}
			//A0A
			else
			{
				path_length = path_length + startNodeLink.getSizeInDoubleLf();

				Enumeration e = getNodeLinksContainingNode(bufferEndNode).elements();
				MapNodeLinkElement bufferNodeLink = (MapNodeLinkElement)e.nextElement();

				while( e.hasMoreElements() && ( bufferNodeLink == startNodeLink ) )
				{
					bufferNodeLink = (MapNodeLinkElement)e.nextElement();
				}
				startNodeLink = bufferNodeLink;
				bufferStartNode = bufferEndNode;
			}

		}
		//A0A
		while ( getMapContext().getNodeLinksInPhysicalLink(link.getId())
					 .contains(nodeLink) );
		return 0;
	}

	//A0A
	public double getFromEndLengthLt()
	{
		double path_length = 0;
		MapNodeElement bufferStartNode;
		MapNodeElement bufferEndNode;
		MapNodeLinkElement endNodeLink = (MapNodeLinkElement)getNodeLinksContainingNode(link.endNode).get(0);
		bufferEndNode = link.endNode;

		//A0A
		do
		{
			//A0A
			if ( endNodeLink.endNode == bufferEndNode)
			{
				bufferStartNode = endNodeLink.startNode;
			}
			else
			{
				bufferStartNode = endNodeLink.endNode;
			}

			//A0A
			if ( endNodeLink == nodeLink)
			{
				if ( bufferStartNode == startNode)
				{
					return path_length + endNodeLink.getSizeInDoubleLt() - getSizeInDoubleLt();
//         return path_length + getSizeInDouble();
				}
				else
				{
					return path_length + getSizeInDoubleLt();
//         return path_length + endNodeLink.getSizeInDouble() - getSizeInDouble();
				}
			}
			//A0A
			else
			{
				path_length = path_length + endNodeLink.getSizeInDoubleLt();

				Enumeration e = getNodeLinksContainingNode(bufferStartNode).elements();
				MapNodeLinkElement bufferNodeLink = (MapNodeLinkElement)e.nextElement();

				while( e.hasMoreElements() && ( bufferNodeLink == endNodeLink ) )
				{
					bufferNodeLink = (MapNodeLinkElement)e.nextElement();
				}
				endNodeLink = bufferNodeLink;
				bufferEndNode = bufferStartNode;
			}

		}
		//A0A
		while ( getMapContext().getNodeLinksInPhysicalLink(link.getId())
					 .contains(nodeLink) );
		return 0;
	}

	//A0A
	public double getFromEndLengthLf()
	{
		double path_length = 0;
		MapNodeElement bufferStartNode;
		MapNodeElement bufferEndNode;
		MapNodeLinkElement endNodeLink = (MapNodeLinkElement)getNodeLinksContainingNode(link.endNode).get(0);
		bufferEndNode = link.endNode;

		//A0A
		do
		{
			//A0A
			if ( endNodeLink.endNode == bufferEndNode)
			{
				bufferStartNode = endNodeLink.startNode;
			}
			else
			{
				bufferStartNode = endNodeLink.endNode;
			}

			//A0A
			if ( endNodeLink == nodeLink)
			{
				if ( bufferStartNode == startNode)
				{
					return path_length + endNodeLink.getSizeInDoubleLf() - getSizeInDoubleLf();
//         return path_length + getSizeInDouble();
				}
				else
				{
					return path_length + getSizeInDoubleLf();
//         return path_length + endNodeLink.getSizeInDouble() - getSizeInDouble();
				}
			}
			//A0A
			else
			{
				path_length = path_length + endNodeLink.getSizeInDoubleLf();

				Enumeration e = getNodeLinksContainingNode(bufferStartNode).elements();
				MapNodeLinkElement bufferNodeLink = (MapNodeLinkElement)e.nextElement();

				while( e.hasMoreElements() && ( bufferNodeLink == endNodeLink ) )
				{
					bufferNodeLink = (MapNodeLinkElement)e.nextElement();
				}
				endNodeLink = bufferNodeLink;
				bufferEndNode = bufferStartNode;
			}

		}
		//A0A
		while ( getMapContext().getNodeLinksInPhysicalLink(link.getId())
					 .contains(nodeLink) );
		return 0;
	}

	//A0A
	public double getSizeInDoubleLt()
	{

		SxDoublePoint from = startNode.getAnchor();
		SxDoublePoint to = getAnchor();

		double a1 = from.x * 3.14 / 180;
		double a2 = from.y * 3.14 / 180;
		double b1 = to.x * 3.14 / 180;
		double b2 = to.y * 3.14 / 180;

		double r = 6400000;

		double d;

		d = r * Math.sqrt
		(
		( Math.cos(a1) * Math.cos(a2) - Math.cos(b1) * Math.cos(b2) ) *
		( Math.cos(a1) * Math.cos(a2) - Math.cos(b1) * Math.cos(b2)) +

		( Math.sin(a1) * Math.cos(a2) - Math.sin(b1) * Math.cos(b2) ) *
		( Math.sin(a1) * Math.cos(a2) - Math.sin(b1) * Math.cos(b2) ) +
		( Math.sin(a2) - Math.sin(b2))*
		( Math.sin(a2) - Math.sin(b2))

		);
		return d;
	}

	//A0A
	public double getSizeInDoubleLf()
	{
		double Kd = getMapContext().getPhysicalLinkbyNodeLink(nodeLink.getId()).getKd();
		return getSizeInDoubleLt() * Kd;
	}


	//ѕередвинуть в точку на заданной рассто€нии от нсчала
	public void moveToFromStart(double distance)
	{
		this.distance = distance;
//		if ( transmissionPath.getLogicalNetLayer().mapMainFrame
//				.aContext.getApplicationModel().isEnabled("mapActionMarkerMove"))
		{
			if ( distance > link.getSizeInDoubleLt())
			{
				moveToFromStart(link.getSizeInDoubleLt());
				return;
			}

			double path = 0;
			MapNodeElement bufferStartNode;
			MapNodeElement bufferEndNode;
			MapNodeLinkElement startNodeLink = (MapNodeLinkElement)getNodeLinksContainingNode(link.startNode).get(0);
			bufferStartNode = link.startNode;

			//A0A
			do
			{
				//A0A
				if ( startNodeLink.startNode == bufferStartNode)
					bufferEndNode = startNodeLink.endNode;
				else
					bufferEndNode = startNodeLink.startNode;

				//ѕроверка того что точка с заданной длинной лежит на данной nodeLink
				if ( path + startNodeLink.getSizeInDoubleLt() > distance)
				{
					startNode = bufferStartNode;
					endNode = getMapContext().getOtherNodeOfNodeLink(startNodeLink, startNode);
					path = distance - path;
					nodeLink = startNodeLink;

					double startNodeX = getLogicalNetLayer().convertLongLatToScreen(startNode.getAnchor()).x;
					double startNodeY = getLogicalNetLayer().convertLongLatToScreen(startNode.getAnchor()).y;

					double endNodeX = getLogicalNetLayer().convertLongLatToScreen(endNode.getAnchor()).x;
					double endNodeY = getLogicalNetLayer().convertLongLatToScreen(endNode.getAnchor()).y;

					double nodeLinkLength =  Math.sqrt( 
							(endNodeX - startNodeX) * (endNodeX - startNodeX) +
							(endNodeY - startNodeY) * (endNodeY - startNodeY) );

					double thisX = getLogicalNetLayer().convertLongLatToScreen(anchor).x;
					double thisY = getLogicalNetLayer().convertLongLatToScreen(anchor).y;

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
					//A0A
					setAnchor(
							getLogicalNetLayer().convertScreenToLongLat(
								new Point(
									(int)Math.round(startNodeX + sin_b * ( path/nodeLink.getSizeInDoubleLt()*nodeLinkLength )),
									(int)Math.round(startNodeY + cos_b * ( path/nodeLink.getSizeInDoubleLt()*nodeLinkLength )) ) ) );
					nodeLink = startNodeLink;

					return;
				}
				//A0A
				else
				{
					path = path + startNodeLink.getSizeInDoubleLt();
					Enumeration e = getNodeLinksContainingNode(bufferEndNode).elements();
					MapNodeLinkElement bufferNodeLink = (MapNodeLinkElement)e.nextElement();
					while( e.hasMoreElements() && ( bufferNodeLink == startNodeLink ) )
					{
						bufferNodeLink = (MapNodeLinkElement)e.nextElement();
					}
					startNodeLink = bufferNodeLink;
					bufferStartNode = bufferEndNode;
				}

			}
			//A0A
			while ( true);
		}
	}

	//ѕроверка того что объект можно перемещать
	public boolean isMovable()
	{
		if ( link.getLogicalNetLayer().mapMainFrame
				.aContext.getApplicationModel().isEnabled("mapActionMarkMove"))
		{
			return true;
		}
		return false;
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(bounds);
		out.writeObject(owner_id);
		out.writeObject(mapContextID);
		out.writeObject(link_id);
		out.writeDouble(anchor.x);
		out.writeDouble(anchor.y);
		out.writeDouble(distance);
		out.writeObject(imageID);

		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		bounds = (Rectangle )in.readObject();
		owner_id = (String )in.readObject();
		mapContextID = (String )in.readObject();
		link_id = (String )in.readObject();
		double x = in.readDouble();
		double y = in.readDouble();
		anchor = new SxDoublePoint(x, y);
		distance = in.readDouble();
		imageID = (String )in.readObject();

		attributes = (Hashtable )in.readObject();

		transferable = new MapMarkElement_Transferable();
		updateLocalFromTransferable();

//		moveToFromStart(this.distance);
	}
}