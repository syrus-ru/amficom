/**
 * $Id: MarkerController.java,v 1.31 2005/08/12 14:49:41 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.event.MapNavigateEvent;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemeUtils;

/**
 * Контроллер маркера.
 * @author $Author: arseniy $
 * @version $Revision: 1.31 $, $Date: 2005/08/12 14:49:41 $
 * @module mapviewclient
 */
public class MarkerController extends AbstractNodeController {
	/** Размер пиктограммы маркера. */
	public static final Rectangle MARKER_BOUNDS = new Rectangle(20, 20);

	/** Имя пиктограммы. */
	public static final String IMAGE_NAME = "marker";
	/** Пиктограмма. */
	public static final String IMAGE_PATH = "images/marker.gif";

	/**
	 * Флаг необходимости инициализировать изображения маркеров. Инициализация
	 * проводится один раз при первом обращении к отрисовке маркера.
	 */
	private static boolean needInit = true;
	private static Identifier imageId;

	/**
	 * Private constructor.
	 */
	protected MarkerController(final NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(final NetMapViewer netMapViewer) {
		return new MarkerController(netMapViewer);
	}

	public static void init(final Identifier creatorId) throws ApplicationException {
		if (needInit) {
			imageId = NodeTypeController.getImageId(creatorId, MarkerController.IMAGE_NAME, MarkerController.IMAGE_PATH);
			MapPropertiesManager.setOriginalImage(imageId, new ImageIcon(MarkerController.IMAGE_PATH).getImage());
			needInit = false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle getDefaultBounds() {
		return MARKER_BOUNDS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getToolTipText(final MapElement mapElement) {
		if (!(mapElement instanceof Marker)) {
			return null;
		}

		final Marker marker = (Marker) mapElement;

		final String s1 = marker.getName() + " (" + LangModelMap.getString("Path_lowercase") + " "
				+ marker.getMeasurementPath().getName() + ")";

		return s1;
	}

	/**
	 * Returns distance from nodelink starting node to marker's anchor
	 * in geographical coordinates.
	 * @param marker маркер
	 * @return рпасстояние
	 */
	public double startToThis(final Marker marker) throws MapConnectionException, MapDataException {
		final DoublePoint from = marker.getStartNode().getLocation();
		final DoublePoint to = marker.getLocation();

		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
		return converter.distance(from, to);
	}

	/**
	 * Returns distance from nodelink ending node to marker's anchor in
	 * geographical coordinates.
	 * 
	 * @param marker
	 *        маркер
	 * @return расстояние
	 */
	public double endToThis(final Marker marker) throws MapConnectionException, MapDataException {
		final DoublePoint from = marker.getEndNode().getLocation();
		final DoublePoint to = marker.getLocation();

		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
		return converter.distance(from, to);
	}

	/**
	 * Возвращает физическое расстояние от маркера до ближайшего узла со стороны
	 * начала измерительного пути.
	 * 
	 * @param marker
	 *        маркер
	 * @return расстояние
	 */
	public double getPhysicalDistanceFromLeft(final Marker marker) throws MapConnectionException, MapDataException {
		final NodeLink nodeLink = marker.getNodeLink();
		final CablePath cablePath = marker.getCablePath();

		final double kd = cablePath.getKd();
		double dist = startToThis(marker);

		final List<NodeLink> pNodeLinks = nodeLink.getPhysicalLink().getNodeLinks();

		boolean doCount = false;

		for (final  NodeLink curNodeLink : cablePath.getSortedNodeLinks()) {
			if (pNodeLinks.contains(curNodeLink)) {
				doCount = true;
			}
			if (doCount) {
				if (curNodeLink.equals(nodeLink)) {
					break;
				}
				dist += curNodeLink.getLengthLt();
			}
		}

		return dist * kd;
	}

	/**
	 * Возвращает физическое расстояние от маркера до ближайшего узла 
	 * со стороны конца измерительного пути.
	 * @param marker маркер
	 * @return расстояние
	 */
	public double getPhysicalDistanceFromRight(final Marker marker) throws MapConnectionException, MapDataException {
		final NodeLink nodeLink = marker.getNodeLink();
		final CablePath cablePath = marker.getCablePath();

		final double kd = cablePath.getKd();
		double dist = endToThis(marker);

		final List<NodeLink> pNodeLinks = nodeLink.getPhysicalLink().getNodeLinks();
		final List<NodeLink> cNodeLinks = cablePath.getSortedNodeLinks();

		boolean doCount = false;

		for (final ListIterator<NodeLink> liter = cNodeLinks.listIterator(cNodeLinks.size()); liter.hasPrevious();) {
			final NodeLink curNodeLink = liter.previous();
			if (pNodeLinks.contains(curNodeLink)) {
				doCount = true;
			}
			if (doCount) {
				if (curNodeLink.equals(nodeLink)) {
					break;
				}
				dist += curNodeLink.getLengthLt();
			}
		}

		return dist * kd;
	}

	//Передвинуть в точку на заданной расстоянии от нсчала
//	public void moveToFromStartLt(MapMarker marker, double distance)
//	{
/*
		LogicalNetLayer lnl = this.logicalNetLayer;
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

	/**
	 * Возвращает физическое расстояние от маркера до начала измерительного пути.
	 * @param marker маркер
	 * @return расстояние
	 */
	public double getFromStartLengthLo(final Marker marker) {
		final SchemePath schemePath = marker.getMeasurementPath().getSchemePath();
		if (schemePath == null) {
			return this.getFromStartLengthLf(marker);
		}

		return schemePath.getOpticalDistance(this.getFromStartLengthLf(marker));
	}

	/**
	 * Передвинуть маркер на заданное оптическое расстояние от начала
	 * измерительного пути.
	 * 
	 * @param marker
	 *        маркер
	 * @param dist
	 *        расстояние
	 */
	public void moveToFromStartLo(final Marker marker, final double dist) throws MapConnectionException, MapDataException {
		final SchemePath schemePath = marker.getMeasurementPath().getSchemePath();
		if (schemePath == null) {
			this.moveToFromStartLf(marker, dist);
		}
		else {
			this.moveToFromStartLf(marker, schemePath.getPhysicalDistance(dist));
		}
	}

	/**
	 * Передвинуть маркер на заданное физическое расстояние от начала
	 * измерительного пути.
	 * 
	 * @param marker
	 *        маркер
	 * @param physicalDistance
	 *        расстояние
	 */
	public void moveToFromStartLf(final Marker marker, final double physicalDistance)
			throws MapConnectionException,
				MapDataException {
		marker.setDistance(physicalDistance);

		final MeasurementPath measurementPath = marker.getMeasurementPath();

		final double pathl = measurementPath.getLengthLf();
		if (marker.getDistance() > pathl) {
			marker.setDistance(pathl);
		}

		MapElement me = null;
		double pathLength = 0;
		double localDistance = 0.0;

		final MeasurementPathController pathController = (MeasurementPathController) this.logicalNetLayer.getMapViewController().getController(measurementPath);

		final SortedSet<PathElement> pathElements = measurementPath.getSchemePath().getPathMembers();
		for (final PathElement pathElement : pathElements) {
			final double d = SchemeUtils.getPhysicalLength(pathElement);
			if (pathLength + d > marker.getDistance()) {
				me = pathController.getMapElement(measurementPath, pathElement);
				localDistance = marker.getDistance() - pathLength;
				break;
			}

			pathLength += d;
		}

		if (me != null) {
			if (me instanceof CablePath) {
				marker.setCablePath((CablePath) me);
				this.setRelativeToCablePath(marker, localDistance);
			} else {
				this.setRelativeToNode(marker, (AbstractNode) me);
			}
		}
	}

	/**
	 * Adjust marker position at given node.
	 * 
	 * @param marker
	 *        маркер
	 * @param node
	 *        начальный узел
	 */
	public void setRelativeToNode(final Marker marker, AbstractNode node) throws MapConnectionException, MapDataException {
		final Map map = marker.getMapView().getMap();
		marker.setStartNode(node);

		final MeasurementPath measurementPath = marker.getMeasurementPath();

		NodeLink nodeLink = null;

		for (final NodeLink nlink : map.getNodeLinks(node)) {
			if (nodeLink == null
					|| measurementPath.getSortedNodeLinks().indexOf(nodeLink) > measurementPath.getSortedNodeLinks().indexOf(nlink))
				nodeLink = nlink;
		}
		if (measurementPath.getSortedNodes().indexOf(node) > measurementPath.getSortedNodes().indexOf(nodeLink.getOtherNode(node))) {
			node = nodeLink.getOtherNode(node);
		}

		marker.setEndNode(nodeLink.getOtherNode(node));
		marker.setNodeLink(nodeLink);
		this.adjustPosition(marker, 0.0);
	}

	/**
	 * Adjust marker position accurding to physical distance relative to current
	 * cable path.
	 * 
	 * @param marker
	 *        маркер
	 * @param physicalDistance
	 *        физическое расстояние
	 */
	public void setRelativeToCablePath(final Marker marker, final double physicalDistance)
			throws MapConnectionException,
				MapDataException {
		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		final CablePath cablePath = marker.getCablePath();

		final double kd = cablePath.getKd();
		final double topologicalDistance = physicalDistance / kd;
		double cumulativeDistance = 0.0;

		AbstractNode sn = cablePath.getStartNode();
		final AbstractNode on = cablePath.getEndNode();
		if (!cablePath.getSortedNodes().iterator().next().equals(sn)) {
			sn = on;
		}

		// serch for a node link
		for (final NodeLink nl : cablePath.getSortedNodeLinks()) {
			if (cumulativeDistance + nl.getLengthLt() > topologicalDistance) {
				marker.setNodeLink(nl);
				marker.setStartNode(sn);
				marker.setEndNode(nl.getOtherNode(sn));

				final double distanceFromStart = topologicalDistance - cumulativeDistance;
				final DoublePoint newPoint = converter.pointAtDistance(marker.getStartNode().getLocation(),
						marker.getEndNode().getLocation(),
						distanceFromStart);
				marker.setLocation(newPoint);

				// adjustPosition(converter.convertMapToScreen(distanceFromStart));
				break;
			}

			cumulativeDistance += nl.getLengthLt();
			sn = nl.getOtherNode(sn);
		}
	}

	/**
	 * Adjust marker position accurding to screen distance relative
	 * to current node link (which comprises startNode and endNode).
	 * 
	 * @param marker маркер
	 * @param screenDistance экранное расстояние
	 */
	public void adjustPosition(final Marker marker, final double screenDistance) throws MapConnectionException, MapDataException {
		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		final Point sp = converter.convertMapToScreen(marker.getStartNode().getLocation());

		final double startNodeX = sp.x;
		final double startNodeY = sp.y;

		final Point ep = converter.convertMapToScreen(marker.getEndNode().getLocation());

		final double endNodeX = ep.x;
		final double endNodeY = ep.y;

		final double nodeLinkLength = Math.sqrt((endNodeX - startNodeX)
				* (endNodeX - startNodeX)
				+ (endNodeY - startNodeY)
				* (endNodeY - startNodeY));

		final double sinB = (endNodeY - startNodeY) / nodeLinkLength;

		final double cosB = (endNodeX - startNodeX) / nodeLinkLength;

		marker.setLocation(converter.convertScreenToMap(new Point((int) Math.round(startNodeX + cosB * screenDistance),
				(int) Math.round(startNodeY + sinB * screenDistance))));
	}

	/**
	 * Получить топологическое расстояние от начала измерительного пути до
	 * маркера.
	 * 
	 * @param marker
	 *        маркер
	 * @return топологическое расстояние
	 */
	public double getFromStartLengthLt(final Marker marker) {
/*
		double pathLength = 0;

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

	/**
	 * Получить физическое расстояние от начала измерительного пути до маркера.
	 * @param marker маркер
	 * @return физическое расстояние
	 */
	public double getFromStartLengthLf(final Marker marker) {
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
	 * Послать сообщения что маркер создан.
	 * @param marker маркер
	 */
	public void notifyMarkerCreated(final Marker marker) {
		this.logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MapNavigateEvent(this,
				MapNavigateEvent.MAP_MARKER_CREATED_EVENT,
				marker.getId(),
				getFromStartLengthLf(marker),
				marker.getMeasurementPath().getSchemePath().getId(),
				marker.getMeId()));
	}

	/**
	 * Послать сообщения что маркер удален.
	 * @param marker маркер
	 */
	public void notifyMarkerDeleted(final Marker marker) {
		this.logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MapNavigateEvent(this,
				MapNavigateEvent.MAP_MARKER_DELETED_EVENT,
				marker.getId(),
				0.0D,
				marker.getMeasurementPath().getSchemePath().getId(),
				marker.getMeId()));
	}

	/**
	 * Послать сообщения что маркер перемещается.
	 * @param marker маркер
	 */
	public void notifyMarkerMoved(final Marker marker) {
		this.logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MapNavigateEvent(this,
				MapNavigateEvent.MAP_MARKER_MOVED_EVENT,
				marker.getId(),
				getFromStartLengthLo(marker),
				marker.getMeasurementPath().getSchemePath().getId(),
				marker.getMeId()));
	}

}
