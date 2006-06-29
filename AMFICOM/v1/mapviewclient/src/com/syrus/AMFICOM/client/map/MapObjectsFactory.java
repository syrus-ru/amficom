/*-
 * $Id: MapObjectsFactory.java,v 1.1 2006/06/29 08:17:32 stas Exp $
 *
 * Copyright ї 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.map.controllers.UnboundNodeController;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PipeBlock;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;


public final class MapObjectsFactory {
	private static final Set<NodeLink> nodeLinksCreated = new HashSet<NodeLink>();
	private static final Set<NodeLink> unboundNodeLinksCreated = new HashSet<NodeLink>();
	private static final Set<UnboundLink> unboundLinksCreated = new HashSet<UnboundLink>();
	private static final Set<CablePath> cablePathsCreated = new HashSet<CablePath>();
	private static final Set<UnboundNode> unboundNodesCreated = new HashSet<UnboundNode>();
	private static final Set<MeasurementPath> measurementPathsCreated = new HashSet<MeasurementPath>();
	
	public static void clear() {
		nodeLinksCreated.clear();
		unboundNodeLinksCreated.clear();
		unboundLinksCreated.clear();
		cablePathsCreated.clear();
		unboundNodesCreated.clear();
		measurementPathsCreated.clear();
	}
	
	/**
	 * @note this must call from AWT EQ
	 * @param logicalNetLayer
	 */
	public static void placeObjectsCreated(final LogicalNetLayer logicalNetLayer) {
		final MapView mapView = logicalNetLayer.getMapView();
		final Map map = mapView.getMap();
		for (CablePath cablePath : cablePathsCreated) {
			mapView.addCablePath(cablePath);
		}
		for (NodeLink nodeLink : nodeLinksCreated) {
			map.addNodeLink(nodeLink);
		}
		for (NodeLink nodeLink : unboundNodeLinksCreated) {
			mapView.addUnboundNodeLink(nodeLink);
		}
		for (UnboundLink unboundLink : unboundLinksCreated) {
			mapView.addUnboundLink(unboundLink);
		}
		for (UnboundNode unboundNode : unboundNodesCreated) {
			UnboundNodeController unc = (UnboundNodeController)logicalNetLayer.getMapViewController().getController(unboundNode);
			unc.updateScaleCoefficient(unboundNode);
			mapView.addUnboundNode(unboundNode);
		}
		for (MeasurementPath measurementPath : measurementPathsCreated) {
			mapView.addMeasurementPath(measurementPath);
		}
	}
	
	private MapObjectsFactory() {
		assert false: ErrorMessages.NOT_IMPLEMENTED;
	}
	
	public static CablePath createCablePath(final SchemeCableLink schemeCableLink, 
			final SiteNode startNode, final SiteNode endNode) throws CreateObjectException {
		CablePath cablePath = CablePath.createInstance(schemeCableLink, startNode, endNode);
		cablePathsCreated.add(cablePath);

		// идем по всем узлам кабельного пути от начального
		Identifier bufferStartSiteId = startNode.getId();
		// цикл по элементам привязки кабеля.
		try {
			//TODO эта операция и прохождение по всем элементам равны по времени, остальные = 0
			final SortedSet<CableChannelingItem> pathMembers = schemeCableLink.getPathMembers();
			
			for(CableChannelingItem cci : pathMembers) {
				PhysicalLink link = cci.getPhysicalLink();
				
				Identifier currentStartNodeId = cci.getStartSiteNodeId();
				Identifier currentEndNodeId = cci.getEndSiteNodeId();
				
				// check if link and cci's ends equals
				if (link != null) {
					final boolean bs1 = currentStartNodeId.equals(link.getStartNodeId());
					final boolean bs2 = currentStartNodeId.equals(link.getEndNodeId());
					final boolean bs = bs1 || bs2;
					
					final boolean be1 = currentEndNodeId.equals(link.getStartNodeId());
					final boolean be2 = currentEndNodeId.equals(link.getEndNodeId());
					final boolean be = be1 || be2;
					
					if (!bs) {
						Log.errorMessage("Link's startNode and CCI's startNode not equals for '" + link.getName() + "' (cable '" + schemeCableLink.getName() + "')");
						if (be1) {
							cci.setEndSiteNode((SiteNode)link.getStartNode());
							currentEndNodeId = link.getStartNodeId();
						} else {
							cci.setStartSiteNode((SiteNode)link.getStartNode());
							currentStartNodeId = link.getStartNodeId();
						}
					}
					if (!be) {
						Log.errorMessage("Link's endNode and CCI's endNode not equals for '" + link.getName() + "' (cable '" + schemeCableLink.getName() + "')");
						if (bs2) {
							cci.setStartSiteNode((SiteNode)link.getEndNode());
							currentStartNodeId = link.getEndNodeId();
						} else {
							cci.setEndSiteNode((SiteNode)link.getEndNode());
							currentEndNodeId = link.getEndNodeId();
						}
					}
				}
				
				// a link between bufferStartSite and current cci exists
				boolean exists = false;
				
				// переходим к следующему узлу кабельного пути
				if(bufferStartSiteId.equals(currentStartNodeId)) {
					bufferStartSiteId = currentEndNodeId;
					exists = true;
				}	else if(bufferStartSiteId.equals(currentEndNodeId)) {
					bufferStartSiteId = currentStartNodeId;
					exists = true;
				}
				
				// если ни одно из двух предыдущих условий не выполнено, то есть
				// существует разрыв последовательности привязки (линии 
				// bufferStartSite - cci.startSiteId не существует), то
				// создать на месте разрыва непроложенную линию из одного фрагмента
				if(!exists) {
					AbstractNode bufferStartSite = StorableObjectPool.getStorableObject(bufferStartSiteId, true);
					AbstractNode currentStartNode = StorableObjectPool.getStorableObject(currentStartNodeId, true);
					
					UnboundLink unbound = createUnboundLinkWithNodeLink(startNode, endNode);
					
					CableChannelingItem newCableChannelingItem = CableController.generateCCI(
							cablePath, unbound, bufferStartSite, currentStartNode);
					
					try {
						newCableChannelingItem.insertSelfBefore(cci);
					} catch(AssertionError e) {
						Log.errorMessage(e);
						schemeCableLink.getPathMembers().first().setParentPathOwner(null, true);
						bufferStartSite = startNode;
						break;
					}
					cablePath.addLink(unbound, newCableChannelingItem);
					unbound.setCablePath(cablePath);
					bufferStartSiteId = currentEndNodeId;
				}
				// привязать кабель к существующей линии
				
				// если линия не существует, опустить данный элемент привязки
				if(link == null) {
					AbstractNode currentStartNode = StorableObjectPool.getStorableObject(currentStartNodeId, true);
					AbstractNode currentEndNode = StorableObjectPool.getStorableObject(currentEndNodeId, true);
					
					UnboundLink unbound = createUnboundLinkWithNodeLink(currentStartNode, currentEndNode);
					cablePath.addLink(unbound, cci);
					unbound.setCablePath(cablePath);
				} else {
					link.getBinding().add(cablePath);
					if(cci.getRowX() != -1
							&& cci.getPlaceY() != -1) {
						PipeBlock block = null;
						try {
							block = cci.getPipeBlock();
							if(block == null) {
								Log.errorMessage("link '" + link.getName() + "' (id " + link.getId() 
										+ ") cci '" + cci.getId() + "' has null block!");
							} else {
								block.bind(cablePath, cci.getRowX(), cci.getPlaceY());
							}
						} catch(ArrayIndexOutOfBoundsException e) {
							final IntDimension dimension = block.getDimension();
							Log.errorMessage("link '" + link.getName() + "' (id " + link.getId() 
									+ ") block '" + block.getNumber() + "' has dimensions (" + dimension.getWidth()
									+ ", " + dimension.getHeight() + "), which is inconsistent with cci (id " + cci.getId()
									+ ") with position (" + cci.getRowX() + ", " + cci.getPlaceY() + ")");
						}
					}
					cablePath.addLink(link, cci);
				}
			}
			// если элементы привязки не доходят до конца, создать непривязанную
			// линию от текущего до конечного узла
			if(!endNode.getId().equals(bufferStartSiteId)) {
				AbstractNode bufferStartSite = StorableObjectPool.getStorableObject(bufferStartSiteId, true);
				UnboundLink unbound = createUnboundLinkWithNodeLink(bufferStartSite, endNode);
//			Log.debugMessage("Unbound fragment for Cable " + xmlId1.getStringValue() + " between nodes  '" + bufferStartSite.getName() + "' and '" + this.endNode.getName() + "'", Log.DEBUGLEVEL09);
				CableChannelingItem newCableChannelingItem = 
					CableController.generateCCI(
							cablePath, 
							unbound,
							bufferStartSite,
							endNode);
				cablePath.addLink(unbound, newCableChannelingItem);
				unbound.setCablePath(cablePath);
			}
		} catch (ApplicationException e) {
			throw new CreateObjectException(e);
		}
		return cablePath;
	}
	
	protected static UnboundLink createUnboundLinkWithNodeLink(final AbstractNode startNode,
			final AbstractNode endNode) throws CreateObjectException {
		final UnboundLink unbound = createUnboundLink(startNode, endNode);
		createNodeLink(unbound, startNode, endNode);
		return unbound;
	}
	
	protected static UnboundLink createUnboundLink(final AbstractNode startNode, 
			final AbstractNode endNode) throws CreateObjectException {
		try {
			final UnboundLink unboundLink = (UnboundLink) UnboundLink.createInstance(LoginManager.getUserId(),
					startNode, endNode, LinkTypeController.getUnboundPhysicalLinkType());
			unboundLinksCreated.add(unboundLink);
			return unboundLink;
		} catch (ApplicationException e) {
			throw new CreateObjectException(e);
		}
	}
	
	protected static NodeLink createNodeLink(final PhysicalLink physicalLink, final AbstractNode startNode,
			final AbstractNode endNode) throws CreateObjectException {
		final NodeLink nodeLink = NodeLink.createInstance(LoginManager.getUserId(), physicalLink, startNode, endNode);
		if(physicalLink instanceof UnboundLink) {
			unboundNodeLinksCreated.add(nodeLink);
		} else {
			nodeLinksCreated.add(nodeLink);
		}
		return nodeLink;
	}
	
	public static UnboundNode createUnboundNode(SchemeElement schemeElement, DoublePoint point) 
			throws CreateObjectException {
		try {
			UnboundNode unbound = UnboundNode.createInstance(LoginManager.getUserId(),
					schemeElement, point, NodeTypeController.getUnboundNodeType());
			unboundNodesCreated.add(unbound);
			return unbound;
		} catch (ApplicationException e) {
			throw new CreateObjectException(e);
		}
	}
	
	public static MeasurementPath createMeasurementPath(final SchemePath schemePath,
			final AbstractNode startNode, final AbstractNode endNode, final MapView mapView) 
			throws CreateObjectException {
		try {
			final MeasurementPath measurementPath = MeasurementPath.createInstance(schemePath, startNode, endNode, mapView);
			measurementPath.sortPathElements();
			measurementPathsCreated.add(measurementPath);
			return measurementPath;
		} catch (ApplicationException e) {
			throw new CreateObjectException(e);
		}
	}

	public static Set<UnboundNode> getUnboundNodesCreated() {
		return Collections.unmodifiableSet(unboundNodesCreated);
	}

	public static Set<CablePath> getCablePathsCreated() {
		return Collections.unmodifiableSet(cablePathsCreated);
	}

	public static Set<MeasurementPath> getMeasurementPathsCreated() {
		return Collections.unmodifiableSet(measurementPathsCreated);
	}
}
