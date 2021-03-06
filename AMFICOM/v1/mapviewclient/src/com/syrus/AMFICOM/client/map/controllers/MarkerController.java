 /*-
 * $$Id: MarkerController.java,v 1.54 2006/06/16 11:26:23 stas Exp $$
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
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.util.Log;

/**
 * ?????????? ???????.
 * 
 * @version $Revision: 1.54 $, $Date: 2006/06/16 11:26:23 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MarkerController extends AbstractNodeController {
	/** ?????? ??????????? ???????. */
	public static final Rectangle MARKER_BOUNDS = new Rectangle(20, 20);

	/** ??? ???????????. */
	public static final String IMAGE_NAME = "marker"; //$NON-NLS-1$
	/** ???????????. */
	public static final String IMAGE_PATH = "images/marker.gif"; //$NON-NLS-1$

	/**
	 * ???? ????????????? ???????????????? ??????????? ????????. ?????????????
	 * ?????????? ???? ??? ??? ?????? ????????? ? ????????? ???????.
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
	 * @param marker ??????
	 * @return ???????????
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
	 *        ??????
	 * @return ??????????
	 */
	public double endToThis(final Marker marker) throws MapConnectionException, MapDataException {
		final DoublePoint from = marker.getEndNode().getLocation();
		final DoublePoint to = marker.getLocation();

		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
		return converter.distance(from, to);
	}

	/**
	 * ?????????? ?????????? ?????????? ?? ??????? ?? ?????????? ???? ?? ???????
	 * ?????? ?????????????? ????.
	 * 
	 * @param marker
	 *        ??????
	 * @return ??????????
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
	 * ?????????? ?????????? ?????????? ?? ??????? ?? ?????????? ???? 
	 * ?? ??????? ????? ?????????????? ????.
	 * @param marker ??????
	 * @return ??????????
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
	 * ?????????? ?????????? ?????????? ?? ??????? ?? ?????? ?????????????? ????.
	 * @param marker ??????
	 * @return ??????????
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
	 * ??????????? ?????? ?? ???????? ?????????? ?????????? ?? ??????
	 * ?????????????? ????.
	 * 
	 * @param marker
	 *        ??????
	 * @param dist
	 *        ??????????
	 * @throws ApplicationException 
	 */
/*	public void moveToFromStartLo(final Marker marker, final double dist) throws MapConnectionException, MapDataException, ApplicationException {
		marker.setOpticalDistance(dist);
		
		final SchemePath schemePath = marker.getMeasurementPath().getSchemePath();
		if (schemePath == null) {
			this.moveToFromStartLf(marker, dist);
		}
		else {
			this.moveToFromStartLf(marker, schemePath.getPhysicalDistance(dist));
		}
	}*/
		/*
	public void moveToFromStartLo(final Marker marker, final Identifier peId, final double optDist) throws MapConnectionException, MapDataException, ApplicationException {
		PathElement pe = StorableObjectPool.getStorableObject(peId, true);
		SchemePath schemePath = pe.getParentPathOwner();
		this.moveToFromStartLf(marker, pe, schemePath.getPhysicalDistance(optDist));
	}*/

	/**
	 * ??????????? ?????? ?? ???????? ?????????? ?????????? ?? ??????
	 * ?????????????? ????.
	 * 
	 * @param marker
	 *        ??????
	 * @param opticalDistance
	 *        ??????????
	 * @throws ApplicationException 
	 * @throws ApplicationException 
	 * @throws MapDataException 
	 * @throws MapConnectionException 
	 */
	public void moveToFromStartLo(final Marker marker, final double opticalDistance)
	throws MapConnectionException,
	MapDataException, ApplicationException {
		final MeasurementPath measurementPath = marker.getMeasurementPath();
		final SchemePath schemePath = measurementPath.getSchemePath();
		final SortedSet<PathElement> pathElements = schemePath.getPathMembers();
		
		final PathElement currentPE = schemePath.getPathElementByOpticalDistance(opticalDistance);
		double[] optPEDistance = schemePath.getOpticalDistanceFromStart(currentPE);
		
		double opticalDistance1 = opticalDistance;
		// for lastPE check marker not exceeds length of path
		if (currentPE.equals(pathElements.last())) {
			if (opticalDistance > optPEDistance[1]) {
				opticalDistance1 = optPEDistance[1];
			}
		}
		marker.setPhysicalDistance(schemePath.getPhysicalDistance(opticalDistance1));

		final MeasurementPathController pathController = (MeasurementPathController) this.logicalNetLayer.getMapViewController().getController(measurementPath);		
		MapElement me = pathController.getMapElement(measurementPath, currentPE);

		
		if (currentPE.getKind().equals(IdlKind.SCHEME_ELEMENT)) {
			if (me instanceof CablePath) {
				this.setRelativeToCablePath(marker, 0);
				Log.errorMessage("MarkerController : SchemeElement object but sets relative to CablePath");
			} else { 
				this.setRelativeToNode(marker, (AbstractNode) me);
			}
		} else {
			double localDistance;
			SchemeElement source;
			if (currentPE.getKind().equals(IdlKind.SCHEME_CABLE_LINK)) {
				final SchemeCableLink scl = currentPE.getSchemeCableLink();
				final SchemeCablePort sourcePort = scl.getSourceAbstractSchemePort();
				source = sourcePort.getParentSchemeDevice().getParentSchemeElement();
			} else {
				final SchemeLink sl = currentPE.getSchemeLink();
				final SchemePort sourcePort = sl.getSourceAbstractSchemePort();
				source = sourcePort.getParentSchemeDevice().getParentSchemeElement();
			}
			
			final int seq = currentPE.getSequentialNumber();
			final PathElement previous = schemePath.getPathMember(seq - 1);
			
			if (!previous.getAbstractSchemeElement().equals(source)) {
				localDistance = schemePath.getPhysicalDistance(optPEDistance[1]) - marker.getPhysicalDistance();
			} else {
				localDistance = marker.getPhysicalDistance() - schemePath.getPhysicalDistance(optPEDistance[0]);
			}
			
			if (me instanceof CablePath) {
				marker.setCablePath((CablePath) me);
				this.setRelativeToCablePath(marker, localDistance);
			} else { 
				this.setRelativeToNode(marker, (AbstractNode) me);
				Log.errorMessage("MarkerController : not SchemeElement object but sets relative to AbstractNode");
			}
		}
	}
	
	/*
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
*/
	/**
	 * Adjust marker position at given node.
	 * 
	 * @param marker
	 *        ??????
	 * @param node
	 *        ????????? ????
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
	 *        ??????
	 * @param physicalDistance
	 *        ?????????? ??????????
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
	 * @param marker ??????
	 * @param screenDistance ???????? ??????????
	 */
	public void adjustPosition(final Marker marker, final double screenDistance) throws MapConnectionException, MapDataException {
		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		
		if (marker.getStartNode().equals(marker.getEndNode())) {
			marker.setLocation(marker.getStartNode().getLocation());
		} else {
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
	}

	/**
	 * ???????? ?????????? ?????????? ?? ?????? ?????????????? ???? ?? ???????.
	 * @param marker ??????
	 * @return ?????????? ??????????
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
						cumulativeTopologicalDistance += startToThis(marker);
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
	 * ??????? ????????? ??? ?????? ??????.
	 * @param marker ??????
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
									null,
									marker.getMeasurementPath().getSchemePath().getId(),
									marker.getMonitoringElementId()));
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	/**
	 * ??????? ????????? ??? ?????? ??????.
	 * @param marker ??????
	 */
	public void notifyMarkerDeleted(final Marker marker) {
		this.logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MarkerEvent(this,
				MarkerEvent.MARKER_DELETED_EVENT,
				marker.getId()));
	}

	/**
	 * ??????? ????????? ??? ?????? ????????????.
	 * @param marker ??????
	 * @throws MapDataException 
	 * @throws MapConnectionException 
	 */
	public void notifyMarkerMoved(final Marker marker) throws MapConnectionException, MapDataException {
		try {
			final SchemePath schemePath = marker.getMeasurementPath().getSchemePath();
			this.logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MarkerEvent(this,
					MarkerEvent.MARKER_MOVED_EVENT,
					marker.getId(),
					schemePath.getOpticalDistance(marker.getPhysicalDistance()),
					null, 
					schemePath.getId(),
					marker.getMonitoringElementId()));
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}

}
