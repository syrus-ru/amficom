/*-
 * $Id: MeasurementPath.java,v 1.57 2005/10/30 14:49:09 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import static java.util.logging.Level.SEVERE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.AbstractSchemeElement;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.util.Log;

/**
 * Элемент пути.
 *
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @version $Revision: 1.57 $, $Date: 2005/10/30 14:49:09 $
 * @module mapview
 */
public final class MeasurementPath implements MapElement {
	/**
	 * Флаг выделения.
	 */
	private transient boolean selected = false;

	/**
	 * Флаг наличия сигнала тревоги.
	 */
	private transient boolean alarmState = false;

	/**
	 * Флаг удаления.
	 */
	private transient boolean removed = false;

	/**
	 * Узел карты, к которому привязан начальный узел кабеля.
	 */
	private transient AbstractNode startNode = null;

	/**
	 * Узел карты, к которому привязан конечный узел кабеля.
	 */
	private transient AbstractNode endNode = null;

	/**
	 * Схемный путь.
	 */
	private SchemePath schemePath;

	/**
	 * Вид карты.
	 */
	private MapView mapView;

	/**
	 * Сортированный список кабельных путей, из которых строится
	 * измерительный путь.
	 * to avoid instantiation of multiple objects.
	 */
	private List<CablePath> sortedCablePaths = new LinkedList<CablePath>();
	/**
	 * Сортированный список фрагментов линий, из которых строится
	 * измерительный путь.
	 * to avoid instantiation of multiple objects.
	 */
	private List<NodeLink> sortedNodeLinks = new LinkedList<NodeLink>();
	/**
	 * Сортированный список узлов, по которым проходит
	 * измерительный путь.
	 * to avoid instantiation of multiple objects.
	 */
	private List<AbstractNode> sortedNodes = new LinkedList<AbstractNode>();

	/**
	 * Конструктор.
	 * @param schemePath схемный путь
	 * @param mapView вид
	 */
	private MeasurementPath(final SchemePath schemePath,
			final AbstractNode stNode,
			final AbstractNode eNode,
			final MapView mapView) {
		this.startNode = stNode;
		this.endNode = eNode;
		this.mapView = mapView;

		this.schemePath = schemePath;
	}

	/**
	 * Сохдать новый элемент пути.
	 * 
	 * @param schemePath
	 *        схемный путь
	 * @param mapView
	 *        вид карты
	 * @return новый элемент пути
	 */
	public static MeasurementPath createInstance(final SchemePath schemePath,
			final AbstractNode stNode,
			final AbstractNode eNode,
			final MapView mapView) {
		if (mapView == null || stNode == null || eNode == null || schemePath == null)
			throw new IllegalArgumentException("Argument is 'null'");

		return new MeasurementPath(schemePath, stNode, eNode, mapView);
	}

	/**
	 * {@inheritDoc}
	 */
	public Identifier getId() {
		return this.schemePath.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return this.schemePath.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setName(final String name) {
		this.schemePath.setName(name);
	}

	/**
	 * Получить описание.
	 * 
	 * @return описание
	 */
	public String getDescription() {
		return this.schemePath.getDescription();
	}

	/**
	 * Установить описание.
	 * @param description описание
	 */
	public void setDescription(final String description) {
		this.schemePath.setDescription(description);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected() {
		return this.selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(final boolean selected) {
		this.selected = selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAlarmState(final boolean alarmState) {
		this.alarmState = alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getAlarmState() {
		return this.alarmState;
	}

	private DoublePoint location = new DoublePoint(0.0, 0.0);

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation() {
		double x = 0.0D; 
		double y = 0.0D;

		try {
			List<CablePath> cablePaths = this.getCablePaths();
			int count = cablePaths.size();
			if(count > 0) {
				for (final CablePath cablePath : cablePaths) {
					final DoublePoint an = cablePath.getLocation();
					x += an.getX();
					y += an.getY();
				}
				this.location.setLocation(x /= count, y /= count);
			}
			// else leave intact
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			// leave intact
		}

		return this.location;
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean isRemoved() {
		return this.removed;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRemoved(final boolean removed) {
		this.removed = removed;
	}

	/**
	 * Get this.endNode.
	 * @return this.endNode
	 */
	public AbstractNode getEndNode() {
		return this.endNode;
		// return getMapView().getEndNode(this.getSchemePath());
	}

	/**
	 * Set {@link #endNode}.
	 * 
	 * @param endNode
	 *        new endNode
	 */
	public void setEndNode(final AbstractNode endNode) {
		this.endNode = endNode;
	}

	/**
	 * Get {@link #startNode}.
	 * 
	 * @return this.startNode
	 */
	public AbstractNode getStartNode() {
		return this.startNode;
		// return getMapView().getStartNode(this.getSchemePath());
	}
	
	/**
	 * Set {@link #startNode}.
	 * @param startNode new startNode
	 */
	public void setStartNode(final AbstractNode startNode) {
		this.startNode = startNode;
	}

	/**
	 * Установить схемный путь.
	 * 
	 * @param schemePath
	 *        схемный путь.
	 */
	public void setSchemePath(final SchemePath schemePath) {
		this.schemePath = schemePath;
	}

	/**
	 * получить схемный путь.
	 * 
	 * @return схемный путь
	 */
	public SchemePath getSchemePath() {
		return this.schemePath;
	}

	/**
	 * Возвращает топологическую длинну в метрах
	 * 
	 * @return топологическая длина
	 * @throws ApplicationException
	 */
	public double getLengthLt() throws ApplicationException {
		double length = 0;
		for (final CablePath cablePath : this.getSortedCablePaths()) {
			length = length + cablePath.getLengthLt();
		}
		return length;
	}

	/**
	 * Возвращает физическую длину в метрах.
	 * 
	 * @return физическая длина
	 * @throws ApplicationException	
	 */
	public double getLengthLf() throws ApplicationException {
		return this.schemePath.getPhysicalLength();
	}

	/**
	 * Возвращает оптическую длину в метрах.
	 * 
	 * @return оптическая длина
	 * @throws ApplicationException
	 */
	public double getLengthLo() throws ApplicationException {
		return this.schemePath.getOpticalLength();
	}

	/**
	 * Несортированный список кабельных путей, из которых строится
	 * измерительный путь.
	 * to avoid instantiation of multiple objects.
	 */
	private List<CablePath> unsortedCablePaths = new LinkedList<CablePath>();

	/**
	 * Получить список топологических кабелей, которые входят в состав пути.
	 * Список строится динамически.
	 * @return список кабельных путей
	 */
	private List<CablePath> getCablePaths() throws ApplicationException {
		synchronized (this.unsortedCablePaths) {
			final Scheme scheme = this.schemePath.getParentSchemeMonitoringSolution().getParentScheme();

			this.unsortedCablePaths.clear();
			for (final PathElement pathElement : this.schemePath.getPathMembers()) {
				final AbstractSchemeElement abstractSchemeElement = pathElement.getAbstractSchemeElement();
				switch (pathElement.getKind().value()) {
					case IdlKind._SCHEME_ELEMENT:
						final SchemeElement schemeElement = (SchemeElement) abstractSchemeElement;
						final SiteNode site = this.mapView.findElement(schemeElement);
						if (site != null) {
							// TODO think if link to 'site' is needed for mPath
							// mPath.addCablePath(site);
						}
						break;
					case IdlKind._SCHEME_LINK:
						final SchemeLink schemeLink = (SchemeLink) abstractSchemeElement;

						SchemeElement innerSourceElement = schemeLink.getSourceAbstractSchemePort().getParentSchemeDevice().getParentSchemeElement();
						SchemeElement topSourceElement = MapView.getTopLevelSchemeElement(innerSourceElement);
						final SchemeElement startSchemeElement = MapView.getTopologicalSchemeElement(scheme, topSourceElement);

						SchemeElement innerTargetElement = schemeLink.getTargetAbstractSchemePort().getParentSchemeDevice().getParentSchemeElement();
						SchemeElement topTargetElement = MapView.getTopLevelSchemeElement(innerTargetElement);
						final SchemeElement endSchemeElement = MapView.getTopologicalSchemeElement(scheme, topTargetElement);

						final SiteNode startSiteNode = this.mapView.findElement(startSchemeElement);
						final SiteNode endSiteNode = this.mapView.findElement(endSchemeElement);
						if (startSiteNode == endSiteNode) {
							// TODO think if link to 'link' is needed for mPath
							// mPath.addCablePath(startSiteNode);
						}
						break;
					case IdlKind._SCHEME_CABLE_LINK:
						final SchemeCableLink schemeCableLink = (SchemeCableLink) abstractSchemeElement;
						final CablePath cablePath = this.mapView.findCablePath(schemeCableLink);
						if (cablePath != null) {
							this.unsortedCablePaths.add(cablePath);
						}
						break;
					default:
						throw new UnsupportedOperationException("MeasurementPath.getCablePaths: Unknown path element kind: "
								+ pathElement.getKind());
				}
			}
		}
		return Collections.unmodifiableList(this.unsortedCablePaths);
	}

	/**
	 * Get {@link #sortedNodeLinks}.
	 * @return this.sortedNodeLinks
	 */
	public List<NodeLink> getSortedNodeLinks() {
		return Collections.unmodifiableList(this.sortedNodeLinks);
	}

	/**
	 * Get {@link #sortedNodes}.
	 * 
	 * @return this.sortedNodes
	 */
	public List<AbstractNode> getSortedNodes() {
		return Collections.unmodifiableList(this.sortedNodes);
	}

	/**
	 * Get {@link #sortedCablePaths}.
	 * 
	 * @return this.sortedCablePaths
	 */
	public List<CablePath> getSortedCablePaths() {
		return Collections.unmodifiableList(this.sortedCablePaths);
	}
	
	/**
	 * Сортировать элементы пути. Включает сортировку кабельных путей, фрагментов
	 * линий и узлов.
	 */
	public void sortPathElements() throws ApplicationException {
		this.sortedCablePaths.clear();
		this.sortedNodeLinks.clear();
		this.sortedNodes.clear();

		AbstractNode node = getStartNode();

		this.sortedCablePaths.addAll(getCablePaths());

		for (final CablePath cablePath : this.sortedCablePaths) {
			cablePath.sortNodeLinks();
			if (cablePath.getStartNode().equals(node)) {
				this.sortedNodeLinks.addAll(cablePath.getSortedNodeLinks());
				this.sortedNodes.addAll(cablePath.getSortedNodes());
			} else {
				final List<NodeLink> reversedSortedNodeLinks = new ArrayList<NodeLink>(cablePath.getSortedNodeLinks());
				Collections.reverse(reversedSortedNodeLinks);
				for (int i = 0; i < reversedSortedNodeLinks.size(); i++)
					this.sortedNodeLinks.add(reversedSortedNodeLinks.get(i));
				final List<AbstractNode> reversedSortedNodes = new ArrayList<AbstractNode>(cablePath.getSortedNodes());
				Collections.reverse(reversedSortedNodes);
				for (int i = 0; i < reversedSortedNodes.size(); i++)
					this.sortedNodes.add(reversedSortedNodes.get(i));
			}
			node = cablePath.getOtherNode(node);

			// to avoid duplicate entry
			this.sortedNodes.remove(node);
		}
		this.sortedNodes.add(node);
	}

	/**
	 * Получить следующий фрагмент по цепочке сортированных фрагментов.
	 * 
	 * @param nodeLink
	 *        фрагмент
	 * @return следующий фрагмент, или <code>null</code>, если nl - последний в
	 *         списке
	 */
//	public NodeLink nextNodeLink(final NodeLink nodeLink) {
//		List nodeLinks = getSortedNodeLinks();
//		final int index = nodeLinks.indexOf(nodeLink);
//		if (index == nodeLinks.size() - 1)
//			return null;
//
//		return (NodeLink) nodeLinks.get(index + 1);
//	}

	/**
	 * Получить предыдущий фрагмент по цепочке сортированных фрагментов.
	 * @param nodeLink фрагмент
	 * @return предыдущий фрагмент, или <code>null</code>, если nl - первый
	 * в списке
	 */
//	public NodeLink previousNodeLink(final NodeLink nodeLink) {
//		List nodeLinks = getSortedNodeLinks();
//		int index = nodeLinks.indexOf(nodeLink);
//		if (index == 0)
//			return null;
//
//		return (NodeLink) nodeLinks.get(index - 1);
//	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public MapElementState getState() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient
	 */
	public void revert(final MapElementState state) {
		throw new UnsupportedOperationException();
	}

	public Characterizable getCharacterizable() {
		return this.schemePath;
	}

}
