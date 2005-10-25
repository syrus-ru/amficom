/*-
 * $Id: Marker.java,v 1.41 2005/10/25 19:53:14 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.resource.DoublePoint;

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
 * @version $Revision: 1.41 $, $Date: 2005/10/25 19:53:14 $
 * @module mapview
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 */

public class Marker extends AbstractNode<Marker> {
	private static final long serialVersionUID = 02L;
	
	/**
	 * Идентификатор исследуемого объекта.
	 */
	protected Identifier monitoredElementId;

	/**
	 * Дистанция от начала измерительного пути до маркера.
	 */
	protected double physicalDistance = 0.0;

	/**
	 * Описатель. Интерпретируется в соответствии с типом маркера.
	 */
	protected Object descriptor;

	/**
	 * Вид карты.
	 */
	protected MapView mapView;
	/**
	 * Измерительный путь, на котором находится маркер.
	 */
	protected MeasurementPath measurementPath;
	/**
	 * Текущий кабель, на котором находится маркер.
	 */
	protected CablePath cablePath;
	/**
	 * Текущий фрагмент линии, на котором находится маркер.
	 */
	protected NodeLink nodeLink;
	/**
	 * Начальный узел на фрагменте линии, на котором находится маркер.
	 */
	protected AbstractNode startNode;
	/**
	 * Конечный узел на фрагменте линии, на котором находится маркер.
	 */
	protected AbstractNode endNode;

	/**
	 * Создание маркера пользователем на карте.
	 *
	 * @param id ижентификатор
	 * @param creatorId пользователь
	 * @param mapView вид карты
	 * @param startNode начальный узел фрагмента
	 * @param endNode конечный узел фрагмента
	 * @param nodeLink фрагмент
	 * @param path измерительный путь
	 * @param monitoredElementId исследуемый объект
	 * @param dpoint географические координаты меркера
	 */
	protected Marker(final Identifier id,
			final Identifier creatorId,
			final MapView mapView,
			final AbstractNode startNode,
			final AbstractNode endNode,
			final NodeLink nodeLink,
			final MeasurementPath path,
			final Identifier monitoredElementId,
			final DoublePoint dpoint) {
		this(id, creatorId, mapView, path, monitoredElementId, String.valueOf(id.getMinor()));

		this.startNode = startNode;
		this.endNode = endNode;
		this.nodeLink = nodeLink;
		setLocation(dpoint);
	}

	/**
	 * Создание маркера пользователем на карте.
	 * 
	 * @param creatorId
	 *        пользователь
	 * @param mapView
	 *        вид карты
	 * @param startNode
	 *        начальный узел фрагмента
	 * @param endNode
	 *        конечный узел фрагмента
	 * @param nodeLink
	 *        фрагмент
	 * @param path
	 *        измерительный путь
	 * @param monitoredElementId
	 *        исследуемый объект
	 * @param dpoint
	 *        географические координаты меркера
	 * @return новый маркер
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 *         нельзя создать
	 */
	public static Marker createInstance(final Identifier creatorId,
			final MapView mapView,
			final AbstractNode startNode,
			final AbstractNode endNode,
			final NodeLink nodeLink,
			final MeasurementPath path,
			final Identifier monitoredElementId,
			final DoublePoint dpoint) throws CreateObjectException {
		if (startNode == null || mapView == null || endNode == null || path == null || dpoint == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			// TODO: use separate entity code for markers!
			final Identifier ide = IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITENODE_CODE);
			return new Marker(ide, creatorId, mapView, startNode, endNode, nodeLink, path, monitoredElementId, dpoint);
		} catch (IdentifierGenerationException e) {
			throw new CreateObjectException("MapMarker.createInstance | cannot generate identifier ", e);
		}
	}

	/**
	 * Создание маркера на основе полученного сообщения с указанием оптической
	 * дистанции.
	 * 
	 * @param id
	 *        идентификатор
	 * @param creatorId
	 *        пользователь
	 * @param mapView
	 *        вид карты
	 * @param path
	 *        измерительный путь
	 * @param monitoredElementId
	 *        идентификатор исследуемого объекта
	 * @param name
	 *        название маркера
	 */
	public Marker(final Identifier id,
			final Identifier creatorId,
			final MapView mapView,
			final MeasurementPath path,
			final Identifier monitoredElementId,
			final String name) {
		super(
				id, 
				new Date(System.currentTimeMillis()), 
				new Date(System.currentTimeMillis()), 
				creatorId, 
				creatorId, 
				StorableObjectVersion.createInitial(),
				name,
				"",
				new DoublePoint(0.0, 0.0));

		this.mapView = mapView;
		this.monitoredElementId = monitoredElementId;
		this.measurementPath = path;
		this.startNode = this.measurementPath.getStartNode();
	}

	/**
	 * Создание маркера на основе полученного сообщения с указанием оптической
	 * дистанции.
	 * 
	 * @param creatorId
	 *        пользователь
	 * @param mapView
	 *        вид карты
	 * @param path
	 *        измерительный путь
	 * @param monitoredElementId
	 *        идентификатор исследуемого объекта
	 * @param name
	 *        название маркера
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 * @return новый маркер
	 */
	public static Marker createInstance(final Identifier creatorId,
			final MapView mapView,
			final MeasurementPath path,
			final Identifier monitoredElementId,
			final String name) throws CreateObjectException {
		if (monitoredElementId == null || mapView == null || path == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final Identifier ide = IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITENODE_CODE);
			return new Marker(ide, creatorId, mapView, path, monitoredElementId, name);
		} catch (IdentifierGenerationException e) {
			throw new CreateObjectException("MapMarker.createInstance | cannot generate identifier ", e);
		}
	}

	/**
	 * Установить идентификатор.
	 * 
	 * @param id
	 *        идентификатор
	 */
	public void setId(final Identifier id) {
		super.id = id;
	}

	/**
	 * Установить вид карты.
	 * 
	 * @param mapView
	 *        вид карты
	 */
	public void setMapView(final MapView mapView) {
		this.mapView = mapView;
	}

	/**
	 * Получить вид карты.
	 * 
	 * @return вид карты
	 */
	public MapView getMapView() {
		return this.mapView;
	}

	public NodeLink previousNodeLink() {
		List<NodeLink> nodeLinks = this.measurementPath.getSortedNodeLinks();
		NodeLink prevNodeLink = null;
		for(final Iterator<NodeLink> iter = nodeLinks.iterator(); iter.hasNext();) {
			final NodeLink bufNodeLink = iter.next();
			if(bufNodeLink.equals(this.nodeLink))
				return prevNodeLink;
			prevNodeLink = bufNodeLink;
		}
		return null;
//		NodeLink nLink;
//		final int index = this.measurementPath.getSortedNodeLinks().indexOf(this.nodeLink);
//		if (index == 0) {
//			nLink = null;
//		}
//		else {
//			nLink = (NodeLink) (this.measurementPath.getSortedNodeLinks().get(index - 1));
//		}
//		return nLink;
	}

	public NodeLink nextNodeLink() {
		List<NodeLink> nodeLinks = this.measurementPath.getSortedNodeLinks();
		for(final Iterator<NodeLink> iter = nodeLinks.iterator(); iter.hasNext();) {
			final NodeLink bufNodeLink = iter.next();
			if(bufNodeLink.equals(this.nodeLink)) {
				if(iter.hasNext())
					return iter.next();
				return null;
			}
		}
		return null;
//		NodeLink nLink;
//		final int index = this.measurementPath.getSortedNodeLinks().indexOf(this.nodeLink);
//		if (index == this.measurementPath.getSortedNodeLinks().size() - 1) {
//			nLink = null;
//		}
//		else {
//			nLink = (NodeLink) (this.measurementPath.getSortedNodeLinks().get(index + 1));
//		}
//		return nLink;
	}

	public CablePath previousCablePath() {
		List<CablePath> cablePaths = this.measurementPath.getSortedCablePaths();
		CablePath prevCablePath = null;
		for(final Iterator<CablePath> iter = cablePaths.iterator(); iter.hasNext();) {
			final CablePath bufCablePath = iter.next();
			if(bufCablePath.equals(this.cablePath))
				return prevCablePath;
			prevCablePath = bufCablePath;
		}
		return null;
	}

	public CablePath nextCablePath() {
		List<CablePath> cablePaths = this.measurementPath.getSortedCablePaths();
		for(final Iterator<CablePath> iter = cablePaths.iterator(); iter.hasNext();) {
			final CablePath bufCablePath = iter.next();
			if(bufCablePath.equals(this.cablePath)) {
				if(iter.hasNext())
					return iter.next();
				return null;
			}
		}
		return null;
	}

//	public SiteNode getLeft() {
//		final List<AbstractNode> nodes = this.cpath.getSortedNodes();
//		SiteNode previous = null;
//		for (final AbstractNode node : nodes) {
//			if (node instanceof SiteNode) {
//				previous = (SiteNode) node;
//			}
//			if (node.equals(this.startNode)) {
//				break;
//			}
//		}
//		return previous;
//	}
//
//	public SiteNode getRight() {
//		final List<AbstractNode> nodes = this.cpath.getSortedNodes();
//		SiteNode found = null;
//
//		for (final AbstractNode node : nodes) {
//			if (node instanceof SiteNode) {
//				found = (SiteNode) node;
//			}
//			if (node.equals(this.endNode)) {
//				break;
//			}
//		}
//		return found;
//	}

	public void setMeasurementPath(final MeasurementPath measurementPath) {
		this.measurementPath = measurementPath;
	}

	public MeasurementPath getMeasurementPath() {
		return this.measurementPath;
	}

	public void setDescriptor(final Object descriptor) {
		this.descriptor = descriptor;
	}

	public Object getDescriptor() {
		return this.descriptor;
	}

	public void setNodeLink(final NodeLink nodeLink) {
		this.nodeLink = nodeLink;
	}

	public NodeLink getNodeLink() {
		return this.nodeLink;
	}

	public void setStartNode(final AbstractNode startNode) {
		this.startNode = startNode;
	}

	public AbstractNode getStartNode() {
		return this.startNode;
	}

	public void setEndNode(final AbstractNode endNode) {
		this.endNode = endNode;
	}

	public AbstractNode getEndNode() {
		return this.endNode;
	}

	public CablePath getCablePath() {
		return this.cablePath;
	}

	public void setCablePath(final CablePath cpath) {
		this.cablePath = cpath;
	}

	public void setMonitoringElementId(final Identifier monitoringElementId) {
		this.monitoredElementId = monitoringElementId;
	}

	public Identifier getMonitoringElementId() {
		return this.monitoredElementId;
	}

	public void setPhysicalDistance(final double physicalDistance) {
		this.physicalDistance = physicalDistance;
	}

	public double getPhysicalDistance() {
		return this.physicalDistance;
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public MapElementState getState() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public void revert(final MapElementState state) {
		throw new UnsupportedOperationException();
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * Suppress since this class is not storable
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	public void insert() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is not storable (unlike
	 * {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is not storable
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	@Override
	public final IdlStorableObject getTransferable(final ORB orb) {
		throw new UnsupportedOperationException();
	}

	public Characterizable getCharacterizable() {
		return null;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected final StorableObjectWrapper<Marker> getWrapper() {
		throw new UnsupportedOperationException(
				"Marker#getWrapper() is unsupported");
	}
}
