/*-
 * $$Id: MeasurementPathController.java,v 1.49 2006/02/22 15:39:47 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.scheme.AbstractSchemeElement;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.util.Log;

/**
 * Контроллер топологическиго пути.
 * 
 * @version $Revision: 1.49 $, $Date: 2006/02/22 15:39:47 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MeasurementPathController extends AbstractLinkController {

	/**
	 * Private constructor.
	 */
	private MeasurementPathController(final NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(final NetMapViewer netMapViewer) {
		return new MeasurementPathController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelectionVisible(final MapElement mapElement) {
		if (!(mapElement instanceof MeasurementPath)) {
			return false;
		}
		final MeasurementPath mpath = (MeasurementPath) mapElement;
		return mpath.isSelected();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isElementVisible(final MapElement mapElement, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof MeasurementPath)) {
			return false;
		}

		final MeasurementPath mpath = (MeasurementPath) mapElement;

		boolean vis = false;
		for (final CablePath cpath : mpath.getSortedCablePaths()) {
			final CableController cc = (CableController) this.logicalNetLayer.getMapViewController().getController(cpath);
			if (cc.isElementVisible(cpath, visibleBounds)) {
				vis = true;
				break;
			}
		}
		return vis;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(final MapElement mapElement) {
		if (!(mapElement instanceof MeasurementPath)) {
			return null;
		}

		final MeasurementPath mpath = (MeasurementPath) mapElement;

		String s1 = mpath.getName();
		String s2 = ""; //$NON-NLS-1$
		String s3 = ""; //$NON-NLS-1$
		try {
			final AbstractNode smne = mpath.getStartNode();
			s2 = ":\n" //$NON-NLS-1$
					+ "   " //$NON-NLS-1$
					+ I18N.getString(MapEditorResourceKeys.FROM_LOWERCASE)
					+ " " //$NON-NLS-1$
					+ smne.getName()
					+ " [" //$NON-NLS-1$
					+ MapViewController.getMapElementReadableType(smne)
					+ "]"; //$NON-NLS-1$
			final AbstractNode emne = mpath.getEndNode();
			s3 = "\n" //$NON-NLS-1$
					+ "   " //$NON-NLS-1$
					+ I18N.getString(MapEditorResourceKeys.TO_LOWERCASE)
					+ " " //$NON-NLS-1$
					+ emne.getName()
					+ " [" //$NON-NLS-1$
					+ MapViewController.getMapElementReadableType(emne)
					+ "]"; //$NON-NLS-1$
		} catch (Exception e) {
			Log.debugMessage(e, Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return s1 + s2 + s3;
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(final MapElement mapElement, final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof MeasurementPath)) {
			return;
		}

		final MeasurementPath measurementPath = (MeasurementPath) mapElement;

		if (!isElementVisible(measurementPath, visibleBounds)) {
			return;
		}

		final Stroke stroke = getStroke(measurementPath.getCharacterizable());
		final Color color = getColor(measurementPath.getCharacterizable());

		this.paint(measurementPath, g, visibleBounds, stroke, color, isSelectionVisible(measurementPath));
	}

	/**
	 * Отрисовать путь с заданным стилем и цветом.
	 * 
	 * @param mpath
	 *        путь
	 * @param g
	 *        графический контекст
	 * @param visibleBounds
	 *        видимая область
	 * @param stroke
	 *        стиль линии
	 * @param color
	 *        цвет линии
	 * @param selectionVisible
	 *        рисовать рамку выделения
	 */
	public void paint(final MeasurementPath mpath,
			final Graphics g,
			final Rectangle2D.Double visibleBounds,
			final Stroke stroke,
			final Color color,
			final boolean selectionVisible) throws MapConnectionException, MapDataException {
		if (!isElementVisible(mpath, visibleBounds)) {
			return;
		}

		for (final CablePath cpath : mpath.getSortedCablePaths()) {
			final CableController cc = (CableController) this.logicalNetLayer.getMapViewController().getController(cpath);
			cc.paint(cpath, g, visibleBounds, stroke, color, selectionVisible);
		}
	}

	/**
	 * {@inheritDoc} <br>
	 * Точка находится на пути, если она находится на любом кабеле, котораый
	 * входит в путь.
	 */
	public boolean isMouseOnElement(final MapElement mapElement, final Point currentMousePoint)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof MeasurementPath)) {
			return false;
		}

		final MeasurementPath mpath = (MeasurementPath) mapElement;

		for (final CablePath cpath : mpath.getSortedCablePaths()) {
			final CableController cc = (CableController) this.logicalNetLayer.getMapViewController().getController(cpath);
			if (cc.isMouseOnElement(cpath, currentMousePoint)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Получить топологический элемент, соответствующий участку схемного пути.
	 * @param path путь
	 * @param pe элемент пути
	 * @return элемент карты
	 * @throws ApplicationException 
	 */
	public MapElement getMapElement(final MeasurementPath path, final PathElement pe) throws ApplicationException {
		MapElement mapElement = null;
		final MapView mapView = this.logicalNetLayer.getMapView();
		final AbstractSchemeElement abstractSchemeElement = pe.getAbstractSchemeElement();
		switch (pe.getKind().value()) {
			case IdlKind._SCHEME_ELEMENT:
				try {
					final SchemeElement se = (SchemeElement) abstractSchemeElement;
					final SchemeElement top = MapView.getTopLevelSchemeElement(se);
					
					final Scheme scheme1 = top.getNearestParentScheme();
					if (scheme1.getParentSchemeElement() != null) {
						final SchemeElement topological = scheme1.getParentSchemeElement();
						mapElement = mapView.findElement(topological);
					} else {
						final SchemeElement topological = MapView.getTopologicalSchemeElement(scheme1, top);
						mapElement = mapView.findElement(topological);
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				break;
			case IdlKind._SCHEME_LINK:
				final SchemeLink schemeLink = (SchemeLink) abstractSchemeElement;
				Scheme scheme = path.getSchemePath().getParentSchemeMonitoringSolution().getParentScheme();
				SchemeElement innerSourceElement = schemeLink.getSourceAbstractSchemePort().getParentSchemeDevice().getParentSchemeElement();
				SchemeElement topSourceElement = MapView.getTopLevelSchemeElement(innerSourceElement);
				final SchemeElement startSchemeElement = MapView.getTopologicalSchemeElement(scheme, topSourceElement);

				SchemeElement innerTargetElement = schemeLink.getTargetAbstractSchemePort().getParentSchemeDevice().getParentSchemeElement();
				SchemeElement topTargetElement = MapView.getTopLevelSchemeElement(innerTargetElement);
				final SchemeElement endSchemeElement = MapView.getTopologicalSchemeElement(scheme, topTargetElement);
				final SiteNode ssite = mapView.findElement(startSchemeElement);
				final SiteNode esite = mapView.findElement(endSchemeElement);
				if (ssite != null && ssite.equals(esite)) {
					mapElement = ssite;
				}
				break;
			case IdlKind._SCHEME_CABLE_LINK:
				final SchemeCableLink clink = (SchemeCableLink) abstractSchemeElement;
				mapElement = mapView.findCablePath(clink);
				break;
			default:
				throw new UnsupportedOperationException();
		}
		return mapElement;
	}

	/**
	 * Получить идентификатор исследуемого объекта, которому соответствует
	 * измерительный путь.
	 * @param path путь
	 * @return идентификатор или <code>null</code>, если исследуемый объект 
	 * не найден
	 * @throws ApplicationException 
	 */
	public Identifier getMonitoredElementId(final MeasurementPath path) throws ApplicationException {
		SortedSet<PathElement> pathMemebers = path.getSchemePath().getPathMembers();
		if (!pathMemebers.isEmpty()) {
			AbstractSchemePort startPort = pathMemebers.first().getEndAbstractSchemePort();
			if (startPort != null) {
				Identifier measurementPortId = startPort.getMeasurementPortId();
				if (!measurementPortId.isVoid()) {
					LinkedIdsCondition condition = new LinkedIdsCondition(measurementPortId, ObjectEntities.MONITOREDELEMENT_CODE);
					Set<MonitoredElement> mes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					if (!mes.isEmpty()) {
						MonitoredElement me = mes.iterator().next();
						return me.getId();
					}
				}
			}
		}
		return null;
	}

	public Rectangle2D getBoundingRectangle(MapElement mapElement) throws MapConnectionException, MapDataException {
		final MeasurementPath mpath = (MeasurementPath) mapElement;
		Rectangle2D rectangle = new Rectangle();
		for (final CablePath cablePath : mpath.getSortedCablePaths()) {
			final CableController controller = (CableController) this.logicalNetLayer.getMapViewController().getController(cablePath);
			rectangle = rectangle.createUnion(controller.getBoundingRectangle(cablePath));
		}
		return rectangle;
	}
}
