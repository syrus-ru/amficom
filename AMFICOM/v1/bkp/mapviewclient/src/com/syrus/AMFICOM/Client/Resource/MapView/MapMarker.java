/**
 * $Id: MapMarker.java,v 1.17 2004/11/11 18:09:30 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathDecompositor;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

import com.syrus.AMFICOM.general.Identifier;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.ImageIcon;

/**
 * Название: Маркер связывания оптической дистанции Lo, полученной      * 
 *         экспериментальным путем, со строительной дистанцией Lf,      * 
 *         вводимой при конфигурировании системы, и топологической      * 
 *         дистанцией Lt, полученной в результате расчетов по           * 
 *         координатам и используемо для отображения в окне карты       * 
 *         Связывание дистанций производится по коэффициентам:          * 
 *             Ku = Lo / Lf                                             * 
 *             Kd = Lf / Lt                                             * 
 *         Связывание отображение маркера на карте и на рефлектограмме  * 
 *         строится на основе дистанции Lo, в связи с чем принимаемая   * 
 *         в сообщении из окна рефлектограммы дистанция преобразуется   * 
 *         из Lo в Lf и Lt и наоборот, в отсылаемом сообщении Lt        * 
 *         преобразуется в Lf и Lo. Исключение составляет случай        * 
 *         создания маркера с карты, в этом случае отправляется Lf,     * 
 *         так как топологическая схема не содержит информации о Ku,    * 
 *         и окно рефлектограммы инициализирует маркер такой            * 
 *         информацией, после чего опять используется Lo.               * 
 * 
 * 
 * 
 * @version $Revision: 1.17 $, $Date: 2004/11/11 18:09:30 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */

public class MapMarker extends MapNodeElement
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "mapmarker";

	/** Размер пиктограммы маркера */
	public static final Rectangle DEFAULT_BOUNDS = new Rectangle(20, 20);
	
	public static final String IMAGE_NAME = "marker";
	public static final String IMAGE_PATH = "images/marker.gif";

	protected Identifier meId;
	protected double distance = 0.0;

	protected MapView mapView;
	

	protected MapMeasurementPathElement measurementPath;

	protected PathDecompositor spd = null;

	protected Object descriptor;

	protected MapCablePathElement cpath;
	protected MapNodeLinkElement nodeLink;
	protected MapNodeElement startNode;
	protected MapNodeElement endNode;

	static
	{
		MapPropertiesManager.setOriginalImage(IMAGE_NAME, new ImageIcon(IMAGE_PATH).getImage());
	}

	
	/**
	 * Создание маркера пользователем на карте
	 * 
	 * @param id
	 * @param mapView
	 * @param startNode
	 * @param mnle
	 * @param topologicalDistance
	 * @param path
	 */
	public MapMarker(
			String id, 
			MapView mapView,
			MapNodeElement startNode,
			MapNodeElement endNode,
			MapNodeLinkElement mnle,
			MapMeasurementPathElement path,
			Point2D.Double dpoint)
	{
		this(id, mapView, 0.0, path, path.getMonitoredElementId());
		
		this.startNode = startNode;
		this.endNode = endNode;
		this.nodeLink = mnle;
		setAnchor(dpoint);
	}

	/**
	 * Создание маркера на основе полученного сообщения с указанием оптической 
	 * дистанции
	 * 
	 * @param id
	 * @param mapView
	 * @param opticalDistance
	 * @param path
	 * @param meId
	 */
	public MapMarker(
			String id, 
			MapView mapView,
			double opticalDistance, 
			MapMeasurementPathElement path,
			Identifier meId)
	{
		this.setId(id);
		this.setName(id);
		this.mapView = mapView;
		this.meId = meId;
		if(mapView != null)
		{
			this.map = mapView.getMap();
			super.setMap(map);
		}
		this.setImageId(IMAGE_NAME);

		this.measurementPath = path;
		this.startNode = measurementPath.getStartNode();
		
		attributes = new HashMap();

		spd = new PathDecompositor(measurementPath.getSchemePath());

		anchor = new Point2D.Double(0.0, 0.0);

		moveToFromStartLo(opticalDistance);
	}

	public Rectangle getDefaultBounds()
	{
		return DEFAULT_BOUNDS;
	}
	
	public Object clone(DataSourceInterface dataSource)
	{
		throw new UnsupportedOperationException();
	}
	
	public String getTyp()
	{
		return typ;
	}

	public void setAnchor(Point2D.Double aAnchor)
	{
		super.setAnchor(aAnchor);
		distance = this.getFromStartLengthLo();
	}

	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
	}
	
	public MapView getMapView()
	{
		return this.mapView;
	}

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	//Послать сообщения что маркер создан
	public void notifyMarkerCreated()
	{
		spd = new PathDecompositor(measurementPath.getSchemePath());

		getMapView().getLogicalNetLayer().sendMapEvent(
			new MapNavigateEvent(
				this,
				MapNavigateEvent.MAP_MARKER_CREATED_EVENT,
				getId(),
				getFromStartLengthLf(),
				measurementPath.getSchemePath().getId(),
				meId));
	}

	//Послать сообщения что маркер удален
	public void notifyMarkerDeleted()
	{
		getMapView().getLogicalNetLayer().sendMapEvent(
			new MapNavigateEvent(
				this,
				MapNavigateEvent.MAP_MARKER_DELETED_EVENT,
				getId(),
				0.0D,
				measurementPath.getSchemePath().getId(),
				meId) );
	}

	//Послать сообщения что маркер перемещается
	public void notifyMarkerMoved()
	{
		getMapView().getLogicalNetLayer().sendMapEvent(
			new MapNavigateEvent(
				this,
				MapNavigateEvent.MAP_MARKER_MOVED_EVENT,
				getId(),
				getFromStartLengthLo(),
				measurementPath.getSchemePath().getId(),
				meId) );
	}

//	public Point2D.Double getAnchor()
//	{
/*
		Vector nl = transmissionPath.sortNodeLinks();
		if ( anchor != bufferAnchor )
		{
			LogicalNetLayer lnl = getLogicalNetLayer();

			//Рисование о пределение координат маркера происходит путм проецирования координат
			//курсора	на линию на которой маркер находится
			double startNodeX = lnl.convertLongLatToScreen(startNode.getAnchor()).x;
			double startNodeY = lnl.convertLongLatToScreen(startNode.getAnchor()).y;

			double endNodeX = lnl.convertLongLatToScreen(endNode.getAnchor()).x;
			double endNodeY = lnl.convertLongLatToScreen(endNode.getAnchor()).y;

			double nodeLinkLength =  Math.sqrt( 
					(endNodeX - startNodeX) * (endNodeX - startNodeX) +
					(endNodeY - startNodeY) * (endNodeY - startNodeY) );

			double thisX = lnl.convertLongLatToScreen(anchor).x;
			double thisY = lnl.convertLongLatToScreen(anchor).y;

			double lengthFromStartNode = Math.sqrt( 
					(thisX - startNodeX) * (thisX - startNodeX) +
					(thisY - startNodeY) * (thisY - startNodeY) );

			double cos_b =  (endNodeY - startNodeY) / nodeLinkLength;
			double sin_b =  (endNodeX - startNodeX) / nodeLinkLength;

			if ( lengthFromStartNode > nodeLinkLength )
			{
//				int i1 = nl.indexOf(nodeLink);

				if(nodeLinkIndex < nl.size() - 1)
				{
					nodeLinkIndex++;
					MapNodeLinkElement mnle = (MapNodeLinkElement) nl.get(nodeLinkIndex);
					startNode = endNode;
					nodeLink = mnle;
					endNode = getMap().getOtherNodeOfNodeLink(mnle, startNode);
					lengthFromStartNode -= nodeLinkLength;
				}
				lengthFromStartNode = nodeLinkLength;
			}
			else
			if ( lengthFromStartNode < 0 )
			{
//				int i1 = nl.indexOf(nodeLink);

				if(nodeLinkIndex > 0)
				{
					nodeLinkIndex--;
					MapNodeLinkElement mnle = (MapNodeLinkElement) nl.get(nodeLinkIndex);
					endNode = startNode;
					nodeLink = mnle;
					startNode = getMap().getOtherNodeOfNodeLink(mnle, endNode);
					lengthFromStartNode += getNodeLinkScreenLength(mnle);
				}
				lengthFromStartNode = 0;
			}

			anchor = lnl.convertScreenToLongLat(new Point(
				(int)Math.round(startNodeX + sin_b * lengthFromStartNode ),
				(int)Math.round(startNodeY + cos_b * lengthFromStartNode ) ) );
			bufferAnchor = anchor;
		}// if ( anchor != bufferAnchor )
*/
//		return anchor;
//	}
/*
	public MotionDescriptor getMotionDescriptor(Point point)
	{
		MapCoordinatesConverter converter = getMap().getConverter();

		//Рисование о пределение координат маркера происходит путм проецирования координат
		//курсора	на линию на которой маркер находится

		double startNodeX = converter.convertMapToScreen(startNode.getAnchor()).x;
		double startNodeY = converter.convertMapToScreen(startNode.getAnchor()).y;

		double endNodeX = converter.convertMapToScreen(endNode.getAnchor()).x;
		double endNodeY = converter.convertMapToScreen(endNode.getAnchor()).y;

		double nodeLinkLength = Math.sqrt( 
			(endNodeX - startNodeX) * (endNodeX - startNodeX) +
			(endNodeY - startNodeY) * (endNodeY - startNodeY) );

		double thisX = converter.convertMapToScreen(getAnchor()).x;
		double thisY = converter.convertMapToScreen(getAnchor()).y;

		double lengthFromStartNode = Math.sqrt( 
			(thisX - startNodeX) * (thisX - startNodeX) +
			(thisY - startNodeY) * (thisY - startNodeY) );

		double sinB =  (endNodeY - startNodeY) / nodeLinkLength;

		double cosB =  (endNodeX - startNodeX) / nodeLinkLength;

		double mousePointX = point.x;
		double mousePointY = point.y;

		double lengthThisToMousePoint = Math.sqrt( 
			(mousePointX - thisX) * (mousePointX - thisX) +
			(mousePointY - thisY) * (mousePointY - thisY) );

		double cosA = (lengthThisToMousePoint == 0 ) ? 0.0 :
			(	(endNodeX - startNodeX) * (mousePointX - thisX) + 
				(endNodeY - startNodeY) * (mousePointY - thisY) ) /
			( nodeLinkLength * lengthThisToMousePoint );

		lengthFromStartNode = lengthFromStartNode + cosA * lengthThisToMousePoint;
		
		return new MotionDescriptor(
			converter.convertMapToScreen(startNode.getAnchor()),
			converter.convertMapToScreen(endNode.getAnchor()),
			converter.convertMapToScreen(getAnchor()),
			point);
	}
*/
	public double getFromStartLengthLt()
	{
		double path_length = 0;
/*
		Vector nl = transmissionPath.sortNodeLinks();
		Vector pl = transmissionPath.sortPhysicalLinks();
		Vector n = transmissionPath.sortNodes();
		
		MapNodeLinkElement mnle;
		boolean point_reached = false;
		for(Enumeration plen = pl.elements(); plen.hasMoreElements() && !point_reached;)
		{
			MapPhysicalLinkElement mple = (MapPhysicalLinkElement )plen.nextElement();
			if(nodeLink.PhysicalLinkID.equals(mple.getId()))
			{
				Vector nl2 = mple.sortNodeLinks();
				point_reached = true;
				boolean direct_order = (nl.indexOf(nl2.get(0)) <= nl.indexOf(nodeLink));
				int size = nl2.size();
				for(int i = 0; i < size; i++)
				{
					if(direct_order)
						mnle = (MapNodeLinkElement )nl2.get(i);
					else
						mnle = (MapNodeLinkElement )nl2.get(size - i - 1);
							
					if ( mnle == nodeLink)
					{
						if ( n.indexOf(startNode) < n.indexOf(endNode))
							return path_length + getSizeInDoubleLt();
						else
							return path_length + nodeLink.getSizeInDoubleLt() - getSizeInDoubleLt();
					}
					else
					{
						path_length += mnle.getSizeInDoubleLt();
					}
				}// for(int i
			}// if(nodeLink.PhysicalLinkID
			else
			{
				path_length += mple.getSizeInDoubleLt();
			}
		}// for(Enumeration plen
*/
		return 0;
	}

	public double getFromStartLengthLf()
	{
/*
		if(schemePath == null)
			return 0.0D;

		Vector nl = transmissionPath.sortNodeLinks();
		Vector pl = transmissionPath.sortPhysicalLinks();
		Vector n = transmissionPath.sortNodes();
		
		double path_length = 0;
		MapNodeElement bufferNode = transmissionPath.startNode;

		boolean point_reached = false;
		MapNodeLinkElement mnle;

		PathElement pes[] = new PathElement[schemePath.links.size()];
		for(int i = 0; i < schemePath.links.size(); i++)
		{
			PathElement pe = (PathElement )schemePath.links.get(i);
			pes[pe.n] = pe;
		}
		Vector pvec = new Vector();
		for(int i = 0; i < pes.length; i++)
		{
			pvec.add(pes[i]);
		}

		Enumeration enum = pvec.elements();
		PathElement pe = null;

		for(Enumeration plen = pl.elements(); plen.hasMoreElements() && !point_reached;)
		{
			MapPhysicalLinkElement mple = (MapPhysicalLinkElement )plen.nextElement();

			pe = (PathElement )enum.nextElement();
			bufferNode.countPhysicalLength(schemePath, pe, enum);
			path_length += bufferNode.getPhysicalLength();

			if(bufferNode.equals(mple.startNode))
				bufferNode = mple.endNode;
			else
				bufferNode = mple.startNode;

			if(nodeLink.PhysicalLinkID.equals(mple.getId()))
			{
				Vector nl2 = mple.sortNodeLinks();
				point_reached = true;
				double temp_length = 0.0D; // Count topological length over cable until marker reached
				boolean direct_order = (nl.indexOf(nl2.get(0)) <= nl.indexOf(nodeLink));
				int size = nl2.size();
				for(int i = 0; i < size; i++)
				{
					if(direct_order)
						mnle = (MapNodeLinkElement )nl2.get(i);
					else
						mnle = (MapNodeLinkElement )nl2.get(size - i - 1);
							
					if ( mnle == nodeLink)
					{
						if ( n.indexOf(startNode) < n.indexOf(endNode))
							temp_length += getSizeInDoubleLt();
						else
							temp_length += nodeLink.getSizeInDoubleLt() - getSizeInDoubleLt();
						return path_length + mple.getKd() * temp_length;// Convert to physical length
					}
					else
					{
						temp_length += mnle.getSizeInDoubleLt();
					}
				}// for(int i
			}// if(nodeLink.PhysicalLinkID
			else
			{
				path_length += mple.getSizeInDoubleLf();
			}
		}// for(Enumeration plen
*/
		return 0.0D;
	}

	/**
	 * returns distance from nodelink starting node to marker's anchor
	 * in geographical coordinates
	 * @return 
	 */
	public double startToThis()
	{
		Point2D.Double from = startNode.getAnchor();
		Point2D.Double to = getAnchor();

		MapCoordinatesConverter converter = getMap().getConverter();
		return converter.distance(from, to);
	}

	/**
	 * returns distance from nodelink ending node to marker's anchor
	 * in geographical coordinates
	 * @return 
	 */
	public double endToThis()
	{
		Point2D.Double from = endNode.getAnchor();
		Point2D.Double to = getAnchor();

		MapCoordinatesConverter converter = getMap().getConverter();
		return converter.distance(from, to);
	}


	public double getOpticalDistanceFromStart()
	{
		return 0.0;
	}

	public MapNodeLinkElement previousNodeLink()
	{
		MapNodeLinkElement nl;
		int index = measurementPath.getSortedNodeLinks().indexOf(nodeLink);
		if(index == 0)
			nl = null;
		else
			nl = (MapNodeLinkElement )(measurementPath.getSortedNodeLinks().get(index - 1));
		return nl;
	}

	public MapNodeLinkElement nextNodeLink()
	{
		MapNodeLinkElement nl;
		int index = measurementPath.getSortedNodeLinks().indexOf(nodeLink);
		if(index == measurementPath.getSortedNodeLinks().size() - 1)
			nl = null;
		else
			nl = (MapNodeLinkElement )(measurementPath.getSortedNodeLinks().get(index + 1));
		return nl;
	}

	public MapSiteNodeElement getLeft()
	{
		List nodes = cpath.getSortedNodes();
		MapNodeElement node = null;
		for(ListIterator lit = nodes.listIterator(nodes.indexOf(startNode)); lit.hasPrevious();)
		{
			node = (MapNodeElement )lit.previous();
			if(node instanceof MapSiteNodeElement)
				break;
			node = null;
		}
		return (MapSiteNodeElement )node;
	}

	public MapSiteNodeElement getRight()
	{
		List nodes = cpath.getSortedNodes();
		MapNodeElement node = null;
		for(ListIterator lit = nodes.listIterator(nodes.indexOf(endNode) - 1); lit.hasNext();)
		{
			node = (MapNodeElement )lit.next();
			if(node instanceof MapSiteNodeElement)
				break;
			node = null;
		}
		return (MapSiteNodeElement )node;
	}

	public double getPhysicalDistanceFromLeft()
	{
		MapSiteNodeElement left = getLeft();
		double kd = cpath.getKd();
		double dist = startToThis();

		MapNodeElement node = startNode;
		List nodeLinks = cpath.getSortedNodeLinks();
		for(ListIterator lit = nodeLinks.listIterator(nodeLinks.indexOf(nodeLink)); lit.hasPrevious();)
		{
			MapNodeLinkElement nl = (MapNodeLinkElement )lit.previous();
			if(nl != nodeLink)
				dist += nl.getLengthLt();
			if(node instanceof MapSiteNodeElement)
				break;
		}
		return dist * kd;
	}

	public double getPhysicalDistanceFromFight()
	{
		MapSiteNodeElement left = getRight();
		double kd = cpath.getKd();
		double dist = endToThis();

		MapNodeElement node = endNode;
		List nodeLinks = cpath.getSortedNodeLinks();
		for(ListIterator lit = nodeLinks.listIterator(nodeLinks.indexOf(nodeLink)); lit.hasNext();)
		{
			MapNodeLinkElement nl = (MapNodeLinkElement )lit.next();
			if(nl != nodeLink)
				dist += nl.getLengthLt();
			if(node instanceof MapSiteNodeElement)
				break;
		}
		return dist * kd;
	}

	//Передвинуть в точку на заданной расстоянии от нсчала
//	public void moveToFromStartLt(double distance)
//	{
/*
		LogicalNetLayer lnl = getLogicalNetLayer();
		if ( lnl.mapMainFrame
				.aContext.getApplicationModel().isEnabled(
					MapApplicationModel.ACTION_USE_MARKER))
		{
			double pathl = transmissionPath.getSizeInDoubleLt();
			if ( distance > pathl)
				distance = pathl;

			Vector nl = transmissionPath.sortNodeLinks();
			Vector pl = transmissionPath.sortPhysicalLinks();
			Vector n = transmissionPath.sortNodes();
		
			double path_length = 0;

			boolean point_reached = false;
			MapNodeLinkElement mnle;
			
			for(Enumeration plen = pl.elements(); plen.hasMoreElements() && !point_reached;)
			{
				MapPhysicalLinkElement mple = (MapPhysicalLinkElement )plen.nextElement();

				if ( path_length + mple.getSizeInDoubleLt() > distance)
				{
					Vector nl2 = mple.sortNodeLinks();
					point_reached = true;
					boolean direct_order = (nl.indexOf(nl2.get(0)) <= nl.indexOf(nodeLink));
					int size = nl2.size();
					for(int i = 0; i < size; i++)
					{
						if(direct_order)
							mnle = (MapNodeLinkElement )nl2.get(i);
						else
							mnle = (MapNodeLinkElement )nl2.get(size - i - 1);
							
						if ( path_length + mnle.getSizeInDoubleLt() > distance)
						{
							nodeLink = mnle;
							nodeLinkIndex = nl.indexOf(mnle);
							if ( n.indexOf(mnle.startNode) < n.indexOf(mnle.endNode))
							{
								startNode = mnle.startNode;
								endNode = mnle.endNode;
							}
							else
							{
								startNode = mnle.endNode;
								endNode = mnle.startNode;
							}

							double nl_distance = distance - path_length;

							adjustPosition(nl_distance, false);
							return;
						}// if ( ... > distance
						else
						{
							path_length += mnle.getSizeInDoubleLt();
						}
					}// for(int i
				}// if ( ... > distance
				else
				{
					path_length += mple.getSizeInDoubleLt();
				}
			}// for(Enumeration plen
		}// if ( lnl.mapMainFrame
*/
//	}

	public double getFromStartLengthLo()
	{
		if(spd == null)
			return getFromStartLengthLf();
		else
			return spd.getOpticalDistanceByPhysical(getFromStartLengthLf());
	}

	public void moveToFromStartLo(double dist)
	{
		if(spd == null)
			moveToFromStartLf(dist);
		else
			moveToFromStartLf(spd.getPhysicalDistanceByOptical(dist));
	}

	//Передвинуть в точку на заданном расстоянии от начала (физ)
	public void moveToFromStartLf(double physicalDistance)
	{
		distance = physicalDistance;

		double pathl = measurementPath.getLengthLf();
		if ( distance > pathl)
			distance = pathl;

		MapElement me = null;
		double pathLength = 0;
		double localDistance = 0.0;

		for(Iterator it = measurementPath.getSchemePath().links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement )it.next();
			double d = pe.getPhysicalLength();
			if(pathLength + d > distance)
			{
				me = measurementPath.getMapElement(pe);
				localDistance = distance - pathLength;
				break;
			}
			else
			{
				pathLength += d;
			}
		}
		
		if(me != null)
		{
			if(me instanceof MapCablePathElement)
			{
				this.cpath = (MapCablePathElement )me;
				setRelativeToCablePath(localDistance);
			}
			else
			{
				this.startNode = (MapNodeElement )me;
				setRelativeToNode(startNode);
			}
		}
	}

	/**
	 * adjust marker position accurding to physical distance relative
	 * to current cable path
	 * 
	 */
	public void setRelativeToNode(MapNodeElement node)
	{
		this.startNode = node;
		
		MapNodeLinkElement nl = null;
		
		for(Iterator it = node.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nlink = (MapNodeLinkElement )it.next();
			if(nl == null 
				|| measurementPath.getSortedNodeLinks().indexOf(nl)
					> measurementPath.getSortedNodeLinks().indexOf(nlink))
				nl = nlink;
		}
		if(measurementPath.getSortedNodes().indexOf(node) 
			> measurementPath.getSortedNodes().indexOf(nl.getOtherNode(node)))
				node = nl.getOtherNode(node);
		
		endNode = nl.getOtherNode(node);
		nodeLink = nl;
		adjustPosition(0.0);
	}

	/**
	 * adjust marker position accurding to physical distance relative
	 * to current cable path
	 * 
	 */
	public void setRelativeToCablePath(double physicalDistance)
	{
		MapCoordinatesConverter converter = this.getMap().getConverter();

		double kd = cpath.getKd();
		double topologicalDistance = physicalDistance / kd;
		double cumulativeDistance = 0.0;
		
		MapNodeElement sn = cpath.getStartNode();
		MapNodeElement on = cpath.getEndNode();
		if(cpath.getSortedNodes().indexOf(sn) > cpath.getSortedNodes().indexOf(on))
			sn = on;
		
		// serch for a node link
		for(Iterator it = cpath.getSortedNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nl = (MapNodeLinkElement )it.next();
			if(cumulativeDistance + nl.getLengthLt() > topologicalDistance)
			{
				nodeLink = nl;
				startNode = sn;
				endNode = nl.getOtherNode(sn);

				double distanceFromStart = topologicalDistance - cumulativeDistance;
				Point2D.Double newPoint = converter.pointAtDistance(
						startNode.getAnchor(), 
						endNode.getAnchor(),
						distanceFromStart);
				setAnchor(newPoint);

//				adjustPosition(converter.convertMapToScreen(distanceFromStart));
				break;
			}
			else
			{
				cumulativeDistance += nl.getLengthLt();
				sn = nl.getOtherNode(sn);
			}
		}
	}

	/**
	 * adjust marker position accurding to topological distance relative
	 * to current node link (which comprises startNode and endNode)
	 * 
	 */
	public void adjustPosition(double screenDistance)
	{
		MapCoordinatesConverter converter = getMap().getConverter();

		Point sp = converter.convertMapToScreen(startNode.getAnchor());
	
		double startNodeX = sp.x;
		double startNodeY = sp.y;

		Point ep = converter.convertMapToScreen(endNode.getAnchor());

		double endNodeX = ep.x;
		double endNodeY = ep.y;

		double nodeLinkLength =  Math.sqrt( 
				(endNodeX - startNodeX) * (endNodeX - startNodeX) +
				(endNodeY - startNodeY) * (endNodeY - startNodeY) );

		double sinB = (endNodeY - startNodeY) / nodeLinkLength;

		double cosB = (endNodeX - startNodeX) / nodeLinkLength;

		setAnchor(converter.convertScreenToMap(new Point(
			(int )Math.round(startNodeX + cosB * screenDistance),
			(int )Math.round(startNodeY + sinB * screenDistance) ) ) );
	}


	public String getToolTipText()
	{
		String s1 = getName() 
			+ " (" + LangModelMap.getString("Path_lowercase") 
			+ " " + measurementPath.getName() + ")";

		return s1;
	}

	public MapElementState getState()
	{
		throw new UnsupportedOperationException();
	}

	public void revert(MapElementState state)
	{
		throw new UnsupportedOperationException();
	}

	public void setMeasurementPath(MapMeasurementPathElement measurementPath)
	{
		this.measurementPath = measurementPath;
	}


	public MapMeasurementPathElement getMeasurementPath()
	{
		return measurementPath;
	}

	public String[][] getExportColumns()
	{
		throw new UnsupportedOperationException();
	}

	public void setColumn(String field, String value)
	{
		throw new UnsupportedOperationException();
	}

	public void setDescriptor(Object descriptor)
	{
		this.descriptor = descriptor;
	}


	public Object getDescriptor()
	{
		return descriptor;
	}

	public PathDecompositor getPathDecompositor()
	{
		return spd;
	}
	
	public void setPathDecompositor(PathDecompositor spd)
	{
		this.spd = spd;
	}

	public void setNodeLink(MapNodeLinkElement nodeLink)
	{
		this.nodeLink = nodeLink;
	}


	public MapNodeLinkElement getNodeLink()
	{
		return nodeLink;
	}


	public void setStartNode(MapNodeElement startNode)
	{
		this.startNode = startNode;
	}


	public MapNodeElement getStartNode()
	{
		return startNode;
	}


	public void setEndNode(MapNodeElement endNode)
	{
		this.endNode = endNode;
	}


	public MapNodeElement getEndNode()
	{
		return endNode;
	}

}
