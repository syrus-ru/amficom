/**
 * $Id: MeasurementPathController.java,v 1.36 2005/09/08 06:50:34 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Set;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;

/**
 * Контроллер топологическиго пути.
 * @author $Author: krupenn $
 * @version $Revision: 1.36 $, $Date: 2005/09/08 06:50:34 $
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
		String s2 = "";
		String s3 = "";
		try {
			final AbstractNode smne = mpath.getStartNode();
			s2 = ":\n"
					+ "   "
					+ LangModelMap.getString("From")
					+ " "
					+ smne.getName()
					+ " ["
					+ MapViewController.getMapElementReadableType(smne)
					+ "]";
			final AbstractNode emne = mpath.getEndNode();
			s3 = "\n"
					+ "   "
					+ LangModelMap.getString("To")
					+ " "
					+ emne.getName()
					+ " ["
					+ MapViewController.getMapElementReadableType(emne)
					+ "]";
		} catch (Exception e) {
			Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getToolTipText()", e);
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

		final MeasurementPath mpath = (MeasurementPath) mapElement;

		if (!isElementVisible(mpath, visibleBounds)) {
			return;
		}

		final Stroke stroke = getStroke(mpath);
		final Color color = getColor(mpath);

		this.paint(mpath, g, visibleBounds, stroke, color, isSelectionVisible(mpath));
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
	 */
	public MapElement getMapElement(final MeasurementPath path, final PathElement pe) {
		MapElement me = null;
		final MapView mapView = this.logicalNetLayer.getMapView();
		switch (pe.getKind().value()) {
			case IdlKind._SCHEME_ELEMENT:
				final SchemeElement se = (SchemeElement) pe.getAbstractSchemeElement();
				final SiteNode site = mapView.findElement(se);
				if (site != null) {
					me = site;
				}
				break;
			case IdlKind._SCHEME_LINK:
				try {
					final SchemeLink link = (SchemeLink) pe.getAbstractSchemeElement();
					final SchemeElement sse = SchemeUtils.getSchemeElementByDevice(path.getSchemePath().getParentSchemeMonitoringSolution().getParentScheme(),
							link.getSourceAbstractSchemePort().getParentSchemeDevice());
					final SchemeElement ese = SchemeUtils.getSchemeElementByDevice(path.getSchemePath().getParentSchemeMonitoringSolution().getParentScheme(),
							link.getTargetAbstractSchemePort().getParentSchemeDevice());
					final SiteNode ssite = mapView.findElement(sse);
					final SiteNode esite = mapView.findElement(ese);
					if (ssite != null && ssite.equals(esite)) {
						me = ssite;
					}
				} catch(ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case IdlKind._SCHEME_CABLE_LINK:
				final SchemeCableLink clink = (SchemeCableLink) pe.getAbstractSchemeElement();
				final CablePath cp = mapView.findCablePath(clink);
				if (cp != null) {
					me = cp;
				}
				break;
			default:
				throw new UnsupportedOperationException();
		}
		return me;
	}

	/**
	 * Получить идентификатор исследуемого объекта, которому соответствует
	 * измерительный путь.
	 * @param path путь
	 * @return идентификатор или <code>null</code>, если исследуемый объект 
	 * не найден
	 */
	public Identifier getMonitoredElementId(final MeasurementPath path) {
		final TransmissionPath tp = path.getSchemePath().getTransmissionPath();
		if(tp == null)
			return null;
		Set<Identifier> monitoredElementIds = tp.getMonitoredElementIds();
		if(monitoredElementIds.size() == 0)
			return null;
		return monitoredElementIds.iterator().next();
	}
}
