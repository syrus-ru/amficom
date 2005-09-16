/**
 * $Id: CableController.java,v 1.34 2005/09/16 14:53:34 krupenn Exp $
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
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;

/**
 * Контроллер кабеля.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.34 $, $Date: 2005/09/16 14:53:34 $
 * @module mapviewclient
 */
public final class CableController extends AbstractLinkController {

	/**
	 * Private constructor.
	 */
	private CableController(final NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(final NetMapViewer netMapViewer) {
		return new CableController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelectionVisible(final MapElement me) {
		if (!(me instanceof CablePath)) {
			return false;
		}

		final CablePath cpath = (CablePath) me;

		boolean visibility = cpath.isSelected();
		if (!visibility) {
			for (final MeasurementPath measurementPath : this.logicalNetLayer.getMapView().getMeasurementPaths(cpath)) {
				final MeasurementPathController controller = 
					(MeasurementPathController) 
						this.logicalNetLayer.getMapViewController().getController(measurementPath);
				if (controller.isSelectionVisible(measurementPath)) {
					visibility = true;
					break;
				}
			}
		}
		return visibility;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isElementVisible(final MapElement me, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(me instanceof CablePath)) {
			return false;
		}

		final CablePath cablePath = (CablePath) me;

		boolean visibility = false;
		for (final PhysicalLink link : cablePath.getLinks()) {
			final PhysicalLinkController controller = (PhysicalLinkController) this.logicalNetLayer.getMapViewController().getController(link);
			if (controller.isElementVisible(link, visibleBounds)) {
				visibility = true;
				break;
			}
		}
		return visibility;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(final MapElement me) {
		if (!(me instanceof CablePath)) {
			return null;
		}

		final CablePath cpath = (CablePath) me;

		final String s1 = cpath.getName();
		String s2 = ""; //$NON-NLS-1$
		String s3 = ""; //$NON-NLS-1$
		try {
			final AbstractNode smne = cpath.getStartNode();
			s2 = ":\n" //$NON-NLS-1$
					+ "   " //$NON-NLS-1$
					+ LangModelMap.getString("From") //$NON-NLS-1$
					+ " " //$NON-NLS-1$
					+ smne.getName()
					+ " [" //$NON-NLS-1$
					+ MapViewController.getMapElementReadableType(smne)
					+ "]"; //$NON-NLS-1$
			final AbstractNode emne = cpath.getEndNode();
			s3 = "\n" //$NON-NLS-1$
					+ "   " //$NON-NLS-1$
					+ LangModelMap.getString("To") //$NON-NLS-1$
					+ " " //$NON-NLS-1$
					+ emne.getName()
					+ " [" //$NON-NLS-1$
					+ MapViewController.getMapElementReadableType(emne)
					+ "]"; //$NON-NLS-1$
		} catch (Exception e) {
			Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getToolTipText()", e); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return s1 + s2 + s3;
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(final MapElement me, final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(me instanceof CablePath)) {
			return;
		}

		final CablePath cpath = (CablePath) me;

		if (!this.isElementVisible(cpath, visibleBounds)) {
			return;
		}

		final Stroke stroke = getStroke(cpath);
		final Color color = getColor(cpath);

		this.paint(cpath, g, visibleBounds, stroke, color, isSelectionVisible(cpath));
	}

	/**
	 * Отрисовать все линии, из которых состоит кабель, с заданным стилем. и
	 * цветом линии
	 * 
	 * @param cpath
	 *        кабель
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
	public void paint(final CablePath cpath,
			final Graphics g,
			final Rectangle2D.Double visibleBounds,
			final Stroke stroke,
			final Color color,
			final boolean selectionVisible) throws MapConnectionException, MapDataException {
		if (!this.isElementVisible(cpath, visibleBounds)) {
			return;
		}

		for (final PhysicalLink link : cpath.getLinks()) {
			final PhysicalLinkController plc = (PhysicalLinkController) this.logicalNetLayer.getMapViewController().getController(link);
			plc.paint(link, g, visibleBounds, stroke, color, selectionVisible);
		}
	}

	/**
	 * {@inheritDoc} <br>
	 * Точка находится на кабеле, если она находится на любой линии, которая
	 * входит в кабель.
	 */
	public boolean isMouseOnElement(final MapElement me, final Point currentMousePoint)
			throws MapConnectionException,
				MapDataException {
		if (!(me instanceof CablePath)) {
			return false;
		}

		final CablePath cpath = (CablePath) me;

		for (final PhysicalLink link : cpath.getLinks()) {
			final PhysicalLinkController plc = (PhysicalLinkController) this.logicalNetLayer.getMapViewController().getController(link);
			if (plc.isMouseOnElement(link, currentMousePoint)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Создать новый объект привязки к линии.
	 * @param cablePath прокладка кабеля
	 * @param link лниия
	 * @return объект привязки, или <code>null</code> при возникновении ошибки
	 */
	public static CableChannelingItem generateCCI(
			final CablePath cablePath,
			final PhysicalLink link,
			final AbstractNode startNode,
			final AbstractNode endNode) {
		final Identifier creatorId = LoginManager.getUserId();
		CableChannelingItem cci = null;
		try {
			double startSpare = MapPropertiesManager.getSpareLength();
			double endSpare = MapPropertiesManager.getSpareLength();
			final SchemeCableLink schemeCableLink = cablePath.getSchemeCableLink();
			if(!(link instanceof UnboundLink)) {
				cci = CableChannelingItem.createInstance(
						creatorId, 
						startSpare,
						endSpare,
						0,//default
						0,//default
						link,
						(SiteNode)startNode,
						(SiteNode)endNode,
						schemeCableLink);
			}
			else {
				cci = CableChannelingItem.createInstance(
						creatorId, 
						(SiteNode)startNode,
						(SiteNode)endNode,
						schemeCableLink);
			}
		} catch (CreateObjectException e) {
			e.printStackTrace();
			cci = null;
		}

		return cci;
	}

	/**
	 * Получить расстояние от начального узла кабеля до заданной точки.
	 * 
	 * @param cpath
	 *        кабель
	 * @param pt
	 *        точка в экранных координатах
	 * @return дистанция топологическая
	 */
	public double getDistanceFromStartLt(final CablePath cpath, final Point pt) throws MapConnectionException, MapDataException {
		double distance = 0.0;

		AbstractNode node = cpath.getStartNode();
		cpath.sortNodeLinks();
		for (final NodeLink mnle : cpath.getSortedNodeLinks()) {
			final NodeLinkController nlc = (NodeLinkController) this.logicalNetLayer.getMapViewController().getController(mnle);
			if (nlc.isMouseOnElement(mnle, pt)) {
				final DoublePoint dpoint = this.logicalNetLayer.getConverter().convertScreenToMap(pt);
				distance += this.logicalNetLayer.getConverter().distance(dpoint, node.getLocation());
				break;
			}

			distance += mnle.getLengthLt();

			if (mnle.getStartNode().equals(node)) {
				node = mnle.getEndNode();
			}
			else {
				node = mnle.getStartNode();
			}
		}
		return distance;
	}

	/**
	 * Возвращяет расстояние от начального узла кабеля до заданной точки, 
	 * пересчитанное на коэффициент топологической привязки.
	 * @param cpath кабель
	 * @param pt точка в экранных координатах
	 * @return дистанция физическая
	 */
	public double getDistanceFromStartLf(final CablePath cpath, final Point pt) throws MapConnectionException, MapDataException {
		final double kd = cpath.getKd();
		return this.getDistanceFromStartLt(cpath, pt) * kd;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getLineSize(final MapElement link) {
		return MapPropertiesManager.getUnboundThickness();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStyle(final MapElement link) {
		return MapPropertiesManager.getStyle();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stroke getStroke(final MapElement link) {
		return MapPropertiesManager.getStroke();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getColor(final MapElement link) {
		return MapPropertiesManager.getUnboundLinkColor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getAlarmedColor(final MapElement link) {
		return MapPropertiesManager.getAlarmedColor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAlarmedLineSize(final MapElement link) {
		return MapPropertiesManager.getAlarmedThickness();
	}

	public Rectangle2D getBoundingRectangle(MapElement mapElement) throws MapConnectionException, MapDataException {
		final CablePath cablePath = (CablePath) mapElement;
		Rectangle2D rectangle = new Rectangle();
		for(PhysicalLink physicalLink : cablePath.getLinks()) {
			final PhysicalLinkController controller = (PhysicalLinkController) this.logicalNetLayer.getMapViewController().getController(physicalLink);
			rectangle = rectangle.createUnion(controller.getBoundingRectangle(physicalLink));
		}
		return rectangle;
	}
}
