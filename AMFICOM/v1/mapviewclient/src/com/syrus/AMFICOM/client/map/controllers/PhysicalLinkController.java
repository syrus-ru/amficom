/**
 * $Id: PhysicalLinkController.java,v 1.30 2005/09/16 14:53:34 krupenn Exp $
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
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;

/**
 * Контроллер линейного элемента карты.
 * @author $Author: krupenn $
 * @version $Revision: 1.30 $, $Date: 2005/09/16 14:53:34 $
 * @module mapviewclient
 */
public class PhysicalLinkController extends AbstractLinkController {
	
	/**
	 * Private constructor.
	 */
	protected PhysicalLinkController(final NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(final NetMapViewer netMapViewer) {
		return new PhysicalLinkController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(final MapElement mapElement) {
		if (!(mapElement instanceof PhysicalLink)) {
			return null;
		}

		final PhysicalLink link = (PhysicalLink) mapElement;
		final PhysicalLinkType linkType = link.getType();

		final String s1 = linkType.getName() + ": " + link.getName(); //$NON-NLS-1$
		String s2 = ""; //$NON-NLS-1$
		String s3 = ""; //$NON-NLS-1$
		try {
			final AbstractNode smne = link.getStartNode();
			s2 = "\n" //$NON-NLS-1$
					+ "   " //$NON-NLS-1$
					+ LangModelMap.getString("From") //$NON-NLS-1$
					+ " " //$NON-NLS-1$
					+ smne.getName()
					+ " [" //$NON-NLS-1$
					+ MapViewController.getMapElementReadableType(smne)
					+ "]"; //$NON-NLS-1$
			final AbstractNode emne = link.getEndNode();
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
	@Override
	public boolean isSelectionVisible(final MapElement mapElement) {
		if (!(mapElement instanceof PhysicalLink)) {
			return false;
		}

		final PhysicalLink link = (PhysicalLink) mapElement;

		return link.isSelected();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isElementVisible(final MapElement mapElement, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof PhysicalLink)) {
			return false;
		}

		final PhysicalLink link = (PhysicalLink) mapElement;

		boolean vis = false;
		for (final NodeLink nodelink : link.getNodeLinks()) {
			final NodeLinkController nlc = (NodeLinkController) this.logicalNetLayer.getMapViewController().getController(nodelink);
			if (nlc.isElementVisible(nodelink, visibleBounds)) {
				vis = true;
				break;
			}
		}
		return vis;
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(final MapElement mapElement, final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof PhysicalLink)) {
			return;
		}

		final PhysicalLink link = (PhysicalLink) mapElement;

		if (!isElementVisible(link, visibleBounds)) {
			return;
		}

		final Stroke strokeForLink = getStroke(link);
		final Color color = getColor(link);

		this.paint(link, g, visibleBounds, strokeForLink, color, isSelectionVisible(link));
	}

	/**
	 * Отрисовать линию с заданным стилем и цветом.
	 * 
	 * @param link
	 *        линия
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
	public void paint(final PhysicalLink link,
			final Graphics g,
			final Rectangle2D.Double visibleBounds,
			final Stroke stroke,
			final Color color,
			final boolean selectionVisible) throws MapConnectionException, MapDataException {
		if (!isElementVisible(link, visibleBounds)) {
			return;
		}

		this.updateLengthLt(link);

		boolean showName = false;
		if (MapPropertiesManager.isLayerLabelVisible(link.getType())) {
			showName = true;
		}

		for (final NodeLink nodelink : link.getNodeLinks()) {
			final NodeLinkController nlc = (NodeLinkController) this.logicalNetLayer.getMapViewController().getController(nodelink);
			nlc.paint(nodelink, g, visibleBounds, stroke, color, selectionVisible);

			if (showName) {
				final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
				final Point from = converter.convertMapToScreen(nodelink.getStartNode().getLocation());
				final Point to = converter.convertMapToScreen(nodelink.getEndNode().getLocation());

				g.setColor(MapPropertiesManager.getBorderColor());
				g.setFont(MapPropertiesManager.getFont());

				final int fontHeight = g.getFontMetrics().getHeight();
				final String text = link.getName();
				final int textWidth = g.getFontMetrics().stringWidth(text);
				final int centerX = (from.x + to.x) / 2;
				final int centerY = (from.y + to.y) / 2;

				g.drawRect(centerX, centerY - fontHeight + 2, textWidth, fontHeight);

				g.setColor(MapPropertiesManager.getTextBackground());
				g.fillRect(centerX, centerY - fontHeight + 2, textWidth, fontHeight);

				g.setColor(MapPropertiesManager.getTextColor());
				g.drawString(text, centerX, centerY);

				showName = false;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMouseOnElement(final MapElement mapElement, final Point currentMousePoint)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof PhysicalLink)) {
			return false;
		}

		final PhysicalLink link = (PhysicalLink) mapElement;

		for (final NodeLink nl : link.getNodeLinks()) {
			final NodeLinkController nlc = (NodeLinkController) this.logicalNetLayer.getMapViewController().getController(nl);
			if (nlc.isMouseOnElement(nl, currentMousePoint)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Пересчитать топологическую длину линии, складывающуюся из фрагментов, из
	 * которых состоит линия.
	 * 
	 * @param link
	 *        линия
	 */
	public void updateLengthLt(final PhysicalLink link) throws MapConnectionException, MapDataException {
		for (final NodeLink nodeLink : link.getNodeLinks()) {
			final NodeLinkController nlc = (NodeLinkController) this.logicalNetLayer.getMapViewController().getController(nodeLink);
			nlc.updateLengthLt(nodeLink);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getLineSize(final MapElement mapElement) {
		if (!(mapElement instanceof PhysicalLink)) {
			return MapPropertiesManager.getThickness();
		}

		final PhysicalLink plink = (PhysicalLink) mapElement;

		final Characteristic ea = getCharacteristic(mapElement, this.thicknessCharType);
		if (ea != null) {
			return Integer.parseInt(ea.getValue());
		}

		final LinkTypeController ltc = (LinkTypeController) LinkTypeController.getInstance();
		return ltc.getLineSize(plink.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStyle(final MapElement mapElement) {
		if (!(mapElement instanceof PhysicalLink)) {
			return MapPropertiesManager.getStyle();
		}

		final PhysicalLink plink = (PhysicalLink) mapElement;

		final Characteristic ea = getCharacteristic(mapElement, this.styleCharType);
		if (ea != null) {
			return ea.getValue();
		}

		final LinkTypeController ltc = (LinkTypeController) LinkTypeController.getInstance();
		return ltc.getStyle(plink.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getColor(final MapElement mapElement) {
		if (!(mapElement instanceof PhysicalLink)) {
			return MapPropertiesManager.getColor();
		}

		final PhysicalLink plink = (PhysicalLink) mapElement;

		final Characteristic ea = getCharacteristic(mapElement, this.colorCharType);
		if (ea != null) {
			Color color = this.colors.get(ea.getValue());
			if (color == null) {
				color = new Color(Integer.parseInt(ea.getValue()));
				this.colors.put(ea.getValue(), color);
			}
			return color;
		}
		final LinkTypeController ltc = (LinkTypeController) LinkTypeController.getInstance();
		return ltc.getColor(plink.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getAlarmedColor(final MapElement mapElement) {
		if (!(mapElement instanceof PhysicalLink)) {
			return MapPropertiesManager.getAlarmedColor();
		}

		final PhysicalLink plink = (PhysicalLink) mapElement;

		final Characteristic ea = getCharacteristic(mapElement, this.alarmedColorCharType);
		if (ea != null) {
			return new Color(Integer.parseInt(ea.getValue()));
		}

		final LinkTypeController ltc = (LinkTypeController) LinkTypeController.getInstance();
		return ltc.getAlarmedColor(plink.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAlarmedLineSize(final MapElement mapElement) {
		if (!(mapElement instanceof PhysicalLink)) {
			return MapPropertiesManager.getAlarmedThickness();
		}

		final PhysicalLink physicalLink = (PhysicalLink) mapElement;

		final Characteristic ea = getCharacteristic(mapElement, this.alarmedThicknessCharType);
		if (ea != null) {
			return Integer.parseInt(ea.getValue());
		}

		final LinkTypeController ltc = (LinkTypeController) LinkTypeController.getInstance();
		return ltc.getAlarmedLineSize(physicalLink.getType());
	}

	public Rectangle2D getBoundingRectangle(MapElement mapElement) throws MapConnectionException, MapDataException {
		final PhysicalLink physicalLink = (PhysicalLink) mapElement;
		Rectangle2D rectangle = new Rectangle();
		for(NodeLink nodeLink : physicalLink.getNodeLinks()) {
			NodeLinkController controller = (NodeLinkController) this.logicalNetLayer.getMapViewController().getController(nodeLink);
			rectangle = rectangle.createUnion(controller.getBoundingRectangle(nodeLink));
		}
		return rectangle;
	}
}
