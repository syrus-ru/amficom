/*-
 * $$Id: MarkerController.java,v 1.47 2006/02/22 15:48:18 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.event.MarkerEvent;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.util.Log;

/**
 *  онтроллер маркера.
 * 
 * @version $Revision: 1.47 $, $Date: 2006/02/22 15:48:18 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MarkerController extends AbstractNodeController {
	/** –азмер пиктограммы маркера. */
	public static final Rectangle MARKER_BOUNDS = new Rectangle(20, 20);

	/** »м€ пиктограммы. */
	public static final String IMAGE_NAME = "marker"; //$NON-NLS-1$
	/** ѕиктограмма. */
	public static final String IMAGE_PATH = "images/marker.gif"; //$NON-NLS-1$

	/**
	 * ‘лаг необходимости инициализировать изображени€ маркеров. »нициализаци€
	 * проводитс€ один раз при первом обращении к отрисовке маркера.
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

	public static void init() throws ApplicationException {
		if (needInit) {
			imageId = NodeTypeController.getImageId(MarkerController.IMAGE_NAME, MarkerController.IMAGE_PATH);
			MapPropertiesManager.setOriginalImage(imageId, new ImageIcon(MarkerController.IMAGE_PATH).getImage());
			needInit = false;
		}
	}

	@Override
	public Identifier getImageId(final AbstractNode node) {
		return imageId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getImage(final AbstractNode node) {
		return MapPropertiesManager.getScaledImage(imageId);
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

		final String s1 = marker.getName() + " (" + I18N.getString(MapEditorResourceKeys.PATH_LOWERCASE) + " " //$NON-NLS-1$ //$NON-NLS-2$
				+ marker.getMeasurementPath().getName() + ")"; //$NON-NLS-1$

		return s1;
	}

	/**
	 * Returns distance from nodelink starting node to marker's anchor
	 * in geographical coordinates.
	 * @param marker маркер
	 * @return рпассто€ние
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
	 * @return рассто€ние
	 */
	public double endToThis(final Marker marker) throws MapConnectionException, MapDataException {
		final DoublePoint from = marker.getEndNode().getLocation();
		final DoublePoint to = marker.getLocation();

		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
		return converter.distance(from, to);
	}

	/**
	 * ¬озвращает физическое рассто€ние от маркера до ближайшего узла со стороны
	 * начала измерительного пути.
	 * 
	 * @param marker
	 *        маркер
	 * @return рассто€ние
	 */
	public double getPhysicalDistanceFromLeft(final Marker marker) throws MapConnectionException, MapDataException {
		final NodeLink nodeLink = marker.getNodeLink();
		final CablePath cablePath = marker.getCablePath();

//		final double kd = cablePath.getKd();
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

		return dist;
	}

	/**
	 * ¬озвращает физическое рассто€ние от маркера до ближайшего узла 
	 * со стороны конца измерительного пути.
	 * @param marker маркер
	 * @return рассто€ние
	 */
	public double getPhysicalDistanceFromRight(final Marker marker) throws MapConnectionException, MapDataException {
		try {
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
		} catch(ApplicationException e) {
			return 0.0D;
		}
	}

	/**
	 * ¬озвращает физическое рассто€ние от маркера до начала измерительного пути.
	 * @param marker маркер
	 * @return рассто€ние
	 * @throws MapDataException 
	 * @throws MapConnectionException 
	 */
	public double getFromStartLengthLo(final Marker marker) throws MapConnectionException, MapDataException {
		try {
			final SchemePath schemePath = marker.getMeasurementPath().getSchemePath();
			if (schemePath == null) {
				return this.getFromStartLengthLf(marker);
			}

			return schemePath.getOpticalDistance(this.getFromStartLengthLf(marker));
		} catch(ApplicationException e) {
			return 0.0D;
		}
	}

	/**
	 * ѕередвинуть маркер на заданное оптическое рассто€ние от начала
	 * измерительного пути.
	 * 
	 * @param marker
	 *        маркер
	 * @param dist
	 *        рассто€ние
	 * @throws ApplicationException 
	 */
	public void moveToFromStartLo(final Marker marker, final double dist) throws MapConnectionException, MapDataException, ApplicationException {
		final SchemePath schemePath = marker.getMeasurementPath().getSchemePath();
		if (schemePath == null) {
			this.moveToFromStartLf(marker, dist);
		}
		else {
			this.moveToFromStartLf(marker, schemePath.getPhysicalDistance(dist));
		}
	}
		
	public void moveToFromStartLo(final Marker marker, final Identifier peId, final double optDist) throws MapConnectionException, MapDataException, ApplicationException {
		PathElement pe = StorableObjectPool.getStorableObject(peId, true);
		SchemePath schemePath = pe.getParentPathOwner();
		this.moveToFromStartLf(marker, pe, schemePath.getPhysicalDistance(optDist));
	}

	/**
	 * ѕередвинуть маркер на заданное физическое рассто€ние от начала
	 * измерительного пути.
	 * 
	 * @param marker
	 *        маркер
	 * @param physicalDistance
	 *        рассто€ние
	 * @throws ApplicationException 
	 * @throws ApplicationException 
	 * @throws MapDataException 
	 * @throws MapConnectionException 
	 */
	public void moveToFromStartLf(final Marker marker, final double physicalDistance)
	throws MapConnectionException,
	MapDataException, ApplicationException {
		marker.setPhysicalDistance(physicalDistance);
		
		final MeasurementPath measurementPath = marker.getMeasurementPath();
		
		final double pathl = measurementPath.getLengthLf();
		if (marker.getPhysicalDistance() > pathl) {
			marker.setPhysicalDistance(pathl);
		}
		
		MapElement me = null;
		double pathLength = 0;
		double localDistance = 0.0;
		
		final MeasurementPathController pathController = (MeasurementPathController) this.logicalNetLayer.getMapViewController().getController(measurementPath);
		
		final SortedSet<PathElement> pathElements = measurementPath.getSchemePath().getPathMembers();
		for (final PathElement pathElement : pathElements) {
			final double d = SchemeUtils.getPhysicalLength(pathElement);
			if (pathLength + d > marker.getPhysicalDistance()) {
				me = pathController.getMapElement(measurementPath, pathElement);
				localDistance = marker.getPhysicalDistance() - pathLength;
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
	
	public void moveToFromStartLf(final Marker marker, final PathElement pathElement, final double physicalDistance)
			throws MapConnectionException,
				MapDataException, ApplicationException {
		marker.setPhysicalDistance(physicalDistance);

		final MeasurementPath measurementPath = marker.getMeasurementPath();

		final double pathl = measurementPath.getLengthLf();
		if (marker.getPhysicalDistance() > pathl) {
			marker.setPhysicalDistance(pathl);
		}

		MapElement me = null;
		double pathLength = 0;
		double localDistance = 0.0;

		final MeasurementPathController pathController = (MeasurementPathController) this.logicalNetLayer.getMapViewController().getController(measurementPath);

		final SchemePath parentPath = pathElement.getParentPathOwner();
		pathLength =  parentPath.getPhysicalDistance(parentPath.getOpticalDistanceFromStart(pathElement)[0]);
		localDistance = marker.getPhysicalDistance() - pathLength;
		me = pathController.getMapElement(measurementPath, pathElement);
		
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
//		final MapView mapView = marker.getMapView();
//		final Map map = mapView.getMap();
		marker.setStartNode(node);
		marker.setEndNode(node);

//		final MeasurementPath measurementPath = marker.getMeasurementPath();

//		NodeLink nodeLink = null;
//
//		for (final NodeLink nlink : mapView.getNodeLinks(node)) {
//			if (nodeLink == null
//					|| measurementPath.getSortedNodeLinks().indexOf(nodeLink) > measurementPath.getSortedNodeLinks().indexOf(nlink))
//				nodeLink = nlink;
//		}
//		if (measurementPath.getSortedNodes().indexOf(node) > measurementPath.getSortedNodes().indexOf(nodeLink.getOtherNode(node))
//				&& measurementPath.getSortedNodes().indexOf(nodeLink.getOtherNode(node)) != -1) {
//			node = nodeLink.getOtherNode(node);
//		}
//
//		marker.setEndNode(nodeLink.getOtherNode(node));
//		marker.setNodeLink(nodeLink);
		this.adjustPosition(marker, 0.0);
	}

	/**
	 * Adjust marker position accurding to physical distance relative to current
	 * cable path.
	 * 
	 * @param marker
	 *        маркер
	 * @param physicalDistance
	 *        физическое рассто€ние
	 * @throws ApplicationException 
	 */
	public void setRelativeToCablePath(final Marker marker, final double physicalDistance)
			throws MapConnectionException,
				MapDataException, ApplicationException {
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
	 * @param screenDistance экранное рассто€ние
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
	 * ѕолучить физическое рассто€ние от начала измерительного пути до маркера.
	 * @param marker маркер
	 * @return физическое рассто€ние
	 * @throws MapDataException 
	 * @throws MapConnectionException 
	 * @throws ApplicationException 
	 */
	public double getFromStartLengthLf(final Marker marker) throws MapConnectionException, MapDataException, ApplicationException {
		final MeasurementPath measurementPath = marker.getMeasurementPath();

		double physicalDistance = 0.0D;
		
		for(CablePath cablePath : measurementPath.getSortedCablePaths()) {
			if(marker.getCablePath().equals(cablePath)) {
				double cumulativeTopologicalDistance = 0.0D;
				for(NodeLink nodeLink : cablePath.getSortedNodeLinks()) {
					if(marker.getNodeLink().equals(nodeLink)) {
						cumulativeTopologicalDistance += getPhysicalDistanceFromLeft(marker);
						break;
					}
					cumulativeTopologicalDistance += nodeLink.getLengthLt();
				}
				double kd = cablePath.getKd();
				physicalDistance += cumulativeTopologicalDistance * kd;
				break;
			}
			physicalDistance += cablePath.getLengthLf();
		}
		return physicalDistance;
	}

	/**
	 * ѕослать сообщени€ что маркер создан.
	 * @param marker маркер
	 * @throws MapDataException 
	 * @throws MapConnectionException 
	 */
	public void notifyMarkerCreated(final Marker marker) throws MapConnectionException, MapDataException {
		try {
			this.logicalNetLayer.getContext().getDispatcher()
					.firePropertyChange(
							new MarkerEvent(
									this,
									MarkerEvent.MARKER_CREATED_EVENT,
									marker.getId(),
									getFromStartLengthLo(marker),
									marker.getMeasurementPath().getSchemePath()
											.getId(),
									marker.getMonitoringElementId()));
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	/**
	 * ѕослать сообщени€ что маркер удален.
	 * @param marker маркер
	 */
	public void notifyMarkerDeleted(final Marker marker) {
		this.logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MarkerEvent(this,
				MarkerEvent.MARKER_DELETED_EVENT,
				marker.getId(),
				0.0D,
				marker.getMeasurementPath().getSchemePath().getId(),
				marker.getMonitoringElementId()));
	}

	/**
	 * ѕослать сообщени€ что маркер перемещаетс€.
	 * @param marker маркер
	 * @throws MapDataException 
	 * @throws MapConnectionException 
	 */
	public void notifyMarkerMoved(final Marker marker) throws MapConnectionException, MapDataException {
		this.logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MarkerEvent(this,
				MarkerEvent.MARKER_MOVED_EVENT,
				marker.getId(),
				getFromStartLengthLo(marker),
				marker.getMeasurementPath().getSchemePath().getId(),
				marker.getMonitoringElementId()));
	}

}
