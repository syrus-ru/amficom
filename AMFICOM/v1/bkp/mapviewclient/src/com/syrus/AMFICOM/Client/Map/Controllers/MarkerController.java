/**
 * $Id: MarkerController.java,v 1.1 2004/12/24 15:42:12 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.PathDecompositor;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.PathElement;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.ImageIcon;
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.Client.Map.Controllers.AbstractNodeController;
import com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath;
import com.syrus.AMFICOM.Client.Map.mapview.Marker;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;

/**
 * элемент карты - узел 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/24 15:42:12 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MarkerController extends AbstractNodeController
{
	/** Размер пиктограммы маркера */
	public static final Rectangle DEFAULT_BOUNDS = new Rectangle(20, 20);
	
	public static final String IMAGE_NAME = "marker";
	public static final String IMAGE_PATH = "images/marker.gif";

	private static boolean needInit = true;

	private static MarkerController instance = null;
	
	protected MarkerController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new MarkerController();
		return instance;
	}

	public Rectangle getDefaultBounds()
	{
		return DEFAULT_BOUNDS;
	}

	public String getToolTipText(MapElement me)
	{
		if(! (me instanceof Marker))
			return null;
			
		Marker marker = (Marker)me;
		
		String s1 = marker.getName() 
			+ " (" + LangModelMap.getString("Path_lowercase") 
			+ " " + marker.getMeasurementPath().getName() + ")";

		return s1;
	}

	public void paint(MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(needInit)
		{
			MapPropertiesManager.setOriginalImage(
				getLogicalNetLayer().getImageId(IMAGE_NAME, IMAGE_PATH),
				new ImageIcon(IMAGE_PATH).getImage());
		}
		super.paint(me, g, visibleBounds);
	}

	/**
	 * returns distance from nodelink starting node to marker's anchor
	 * in geographical coordinates
	 * @return 
	 */
	public double startToThis(Marker marker)
	{
		DoublePoint from = marker.getStartNode().getLocation();
		DoublePoint to = marker.getLocation();

		MapCoordinatesConverter converter = getLogicalNetLayer();
		return converter.distance(from, to);
	}

	/**
	 * returns distance from nodelink ending node to marker's anchor
	 * in geographical coordinates
	 * @return 
	 */
	public double endToThis(Marker marker)
	{
		DoublePoint from = marker.getEndNode().getLocation();
		DoublePoint to = marker.getLocation();

		MapCoordinatesConverter converter = getLogicalNetLayer();
		return converter.distance(from, to);
	}

	public double getPhysicalDistanceFromLeft(Marker marker)
	{
		SiteNode left = marker.getLeft();
		double kd = marker.getCablePath().getKd();
		double dist = startToThis(marker);

		AbstractNode node = marker.getStartNode();
		List nodeLinks = marker.getCablePath().getSortedNodeLinks();
		for(ListIterator lit = nodeLinks.listIterator(nodeLinks.indexOf(marker.getNodeLink())); lit.hasPrevious();)
		{
			NodeLink nl = (NodeLink)lit.previous();
			if(nl != marker.getNodeLink())
				dist += nl.getLengthLt();
			if(node instanceof SiteNode)
				break;
		}
		return dist * kd;
	}

	public double getPhysicalDistanceFromRight(Marker marker)
	{
		SiteNode left = marker.getRight();
		double kd = marker.getCablePath().getKd();
		double dist = endToThis(marker);

		AbstractNode node = marker.getEndNode();
		List nodeLinks = marker.getCablePath().getSortedNodeLinks();
		for(ListIterator lit = nodeLinks.listIterator(nodeLinks.indexOf(marker.getNodeLink())); lit.hasNext();)
		{
			NodeLink nl = (NodeLink)lit.next();
			if(nl != marker.getNodeLink())
				dist += nl.getLengthLt();
			if(node instanceof SiteNode)
				break;
		}
		return dist * kd;
	}

	//Передвинуть в точку на заданной расстоянии от нсчала
//	public void moveToFromStartLt(MapMarker marker, double distance)
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

	public double getFromStartLengthLo(Marker marker)
	{
		if(marker.getPathDecompositor() == null)
			return getFromStartLengthLf(marker);
		else
			return marker.getPathDecompositor().getOpticalDistance(getFromStartLengthLf(marker));
	}

	public void moveToFromStartLo(Marker marker, double dist)
	{
		if(marker.getPathDecompositor() == null)
			moveToFromStartLf(marker, dist);
		else
			moveToFromStartLf(marker, marker.getPathDecompositor().getPhysicalDistance(dist));
	}

	//Передвинуть в точку на заданном расстоянии от начала (физ)
	public void moveToFromStartLf(Marker marker, double physicalDistance)
	{
		marker.setDistance(physicalDistance);

		double pathl = marker.getMeasurementPath().getLengthLf();
		if ( marker.getDistance() > pathl)
			marker.setDistance(pathl);

		MapElement me = null;
		double pathLength = 0;
		double localDistance = 0.0;

		PathElement []pes = marker.getMeasurementPath().getSchemePath().links();
		for(int i = 0; i < pes.length; i++)
		{
			PathElement pe = (PathElement )pes[i];
			double d = SchemeUtils.getPhysicalLength(pe);
			if(pathLength + d > marker.getDistance())
			{
				me = marker.getMeasurementPath().getMapElement(pe);
				localDistance = marker.getDistance() - pathLength;
				break;
			}
			else
			{
				pathLength += d;
			}
		}
		
		if(me != null)
		{
			if(me instanceof CablePath)
			{
				marker.setCablePath((CablePath)me);
				setRelativeToCablePath(marker, localDistance);
			}
			else
			{
				setRelativeToNode(marker, (AbstractNode )me);
			}
		}
	}

	/**
	 * adjust marker position accurding to physical distance relative
	 * to current cable path
	 * 
	 */
	public void setRelativeToNode(Marker marker, AbstractNode node)
	{
		marker.setStartNode(node);
		
		NodeLink nl = null;
		
		for(Iterator it = node.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nlink = (NodeLink)it.next();
			if(nl == null 
				|| marker.getMeasurementPath().getSortedNodeLinks().indexOf(nl)
					> marker.getMeasurementPath().getSortedNodeLinks().indexOf(nlink))
				nl = nlink;
		}
		if(marker.getMeasurementPath().getSortedNodes().indexOf(node) 
			> marker.getMeasurementPath().getSortedNodes().indexOf(nl.getOtherNode(node)))
				node = nl.getOtherNode(node);
		
		marker.setEndNode(nl.getOtherNode(node));
		marker.setNodeLink(nl);
		adjustPosition(marker, 0.0);
	}

	/**
	 * adjust marker position accurding to physical distance relative
	 * to current cable path
	 * 
	 */
	public void setRelativeToCablePath(Marker marker, double physicalDistance)
	{
		MapCoordinatesConverter converter = getLogicalNetLayer();

		double kd = marker.getCablePath().getKd();
		double topologicalDistance = physicalDistance / kd;
		double cumulativeDistance = 0.0;
		
		AbstractNode sn = marker.getCablePath().getStartNode();
		AbstractNode on = marker.getCablePath().getEndNode();
		if(marker.getCablePath().getSortedNodes().indexOf(sn) > marker.getCablePath().getSortedNodes().indexOf(on))
			sn = on;
		
		// serch for a node link
		for(Iterator it = marker.getCablePath().getSortedNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nl = (NodeLink)it.next();
			if(cumulativeDistance + nl.getLengthLt() > topologicalDistance)
			{
				marker.setNodeLink(nl);
				marker.setStartNode(sn);
				marker.setEndNode(nl.getOtherNode(sn));

				double distanceFromStart = topologicalDistance - cumulativeDistance;
				DoublePoint newPoint = converter.pointAtDistance(
						marker.getStartNode().getLocation(), 
						marker.getEndNode().getLocation(),
						distanceFromStart);
				marker.setLocation(newPoint);

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
	public void adjustPosition(Marker marker, double screenDistance)
	{
		MapCoordinatesConverter converter = getLogicalNetLayer();

		Point sp = converter.convertMapToScreen(marker.getStartNode().getLocation());
	
		double startNodeX = sp.x;
		double startNodeY = sp.y;

		Point ep = converter.convertMapToScreen(marker.getEndNode().getLocation());

		double endNodeX = ep.x;
		double endNodeY = ep.y;

		double nodeLinkLength =  Math.sqrt( 
				(endNodeX - startNodeX) * (endNodeX - startNodeX) +
				(endNodeY - startNodeY) * (endNodeY - startNodeY) );

		double sinB = (endNodeY - startNodeY) / nodeLinkLength;

		double cosB = (endNodeX - startNodeX) / nodeLinkLength;

		marker.setLocation(converter.convertScreenToMap(new Point(
			(int )Math.round(startNodeX + cosB * screenDistance),
			(int )Math.round(startNodeY + sinB * screenDistance) ) ) );
	}

	public double getFromStartLengthLt(Marker marker)
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

	public double getFromStartLengthLf(Marker marker)
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

	//Послать сообщения что маркер создан
	public void notifyMarkerCreated(Marker marker)
	{
		marker.setPathDecompositor(new PathDecompositor(marker.getMeasurementPath().getSchemePath()));

		getLogicalNetLayer().sendMapEvent(
			new MapNavigateEvent(
				this,
				MapNavigateEvent.MAP_MARKER_CREATED_EVENT,
				marker.getId(),
				getFromStartLengthLf(marker),
				marker.getMeasurementPath().getSchemePath().id(),
				marker.getMeId()));
	}

	//Послать сообщения что маркер удален
	public void notifyMarkerDeleted(Marker marker)
	{
		getLogicalNetLayer().sendMapEvent(
			new MapNavigateEvent(
				this,
				MapNavigateEvent.MAP_MARKER_DELETED_EVENT,
				marker.getId(),
				0.0D,
				marker.getMeasurementPath().getSchemePath().id(),
				marker.getMeId()) );
	}

	//Послать сообщения что маркер перемещается
	public void notifyMarkerMoved(Marker marker)
	{
		getLogicalNetLayer().sendMapEvent(
			new MapNavigateEvent(
				this,
				MapNavigateEvent.MAP_MARKER_MOVED_EVENT,
				marker.getId(),
				getFromStartLengthLo(marker),
				marker.getMeasurementPath().getSchemePath().id(),
				marker.getMeId()) );
	}

}
