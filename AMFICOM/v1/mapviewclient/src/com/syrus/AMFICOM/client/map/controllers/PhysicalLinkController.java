/*-
 * $$Id: PhysicalLinkController.java,v 1.38 2006/02/07 15:27:11 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.util.Log;

/**
 * Контроллер линейного элемента карты.
 * 
 * @version $Revision: 1.38 $, $Date: 2006/02/07 15:27:11 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class PhysicalLinkController extends AbstractLinkController {
	
	private static PhysicalLinkController instance;
	
	/**
	 * Private constructor.
	 */
	protected PhysicalLinkController(final NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static PhysicalLinkController createInstance(final NetMapViewer netMapViewer) {
		if (instance == null) {
			instance = new PhysicalLinkController(netMapViewer);
		}
		return instance;
	}
	
	public static PhysicalLinkController getInstance() {
		assert instance != null : "PhysicalLinkController not initialyzed"; //$NON-NLS-1$
		return instance;
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
					+ I18N.getString(MapEditorResourceKeys.FROM_LOWERCASE)
					+ " " //$NON-NLS-1$
					+ smne.getName()
					+ " [" //$NON-NLS-1$
					+ MapViewController.getMapElementReadableType(smne)
					+ "]"; //$NON-NLS-1$
			final AbstractNode emne = link.getEndNode();
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

		if(!MapPropertiesManager.isLayerVisible(link.getType())) {
			return false;
		}

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

		final PhysicalLink physicalLink = (PhysicalLink) mapElement;

		if (!isElementVisible(physicalLink, visibleBounds)) {
			return;
		}

		final Stroke strokeForLink = getStroke(physicalLink);
		final Color color = getColor(physicalLink);

		this.paint(physicalLink, g, visibleBounds, strokeForLink, color, isSelectionVisible(physicalLink));
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
	public int getLineSize(final Characterizable characterizable) {
		if (!(characterizable instanceof PhysicalLink)) {
			return MapPropertiesManager.getThickness();
		}

		final PhysicalLink plink = (PhysicalLink) characterizable;

		final Characteristic ea = getCharacteristic(characterizable, this.thicknessCharType);
		if (ea != null) {
			return Integer.parseInt(ea.getValue());
		}

		final LinkTypeController ltc = (LinkTypeController) LinkTypeController.getInstance();
		return ltc.getLineSize(plink.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	protected String getStyle(final Characterizable characterizable) {
		if (!(characterizable instanceof PhysicalLink)) {
			return MapPropertiesManager.getStyle();
		}

		final PhysicalLink plink = (PhysicalLink) characterizable;

		final Characteristic ea = getCharacteristic(characterizable, this.styleCharType);
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
	public Color getColor(final Characterizable characterizable) {
//		return super.getColor(characterizable);
		if (!(characterizable instanceof PhysicalLink)) {
			return MapPropertiesManager.getColor();
		}
		
		Color color = this.colors.get(characterizable.getId());
		if (color != null) {
			return color;
		}
		final Characteristic ea = getCharacteristic(characterizable, this.colorCharType);
		if (ea != null) {
			color = new Color(Integer.parseInt(ea.getValue()));
			this.colors.put(characterizable.getId(), color);
			return color;
		}

		final PhysicalLink physicalLink = (PhysicalLink) characterizable;
		final LinkTypeController ltc = (LinkTypeController) LinkTypeController.getInstance();
		color = ltc.getColor(physicalLink.getType());
		this.colors.put(characterizable.getId(), color);
		return color;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getAlarmedColor(final Characterizable characterizable) {
		if (!(characterizable instanceof PhysicalLink)) {
			return MapPropertiesManager.getAlarmedColor();
		}

		final PhysicalLink plink = (PhysicalLink) characterizable;

		final Characteristic ea = getCharacteristic(characterizable, this.alarmedColorCharType);
		if (ea != null) {
			return new Color(Integer.parseInt(ea.getValue()));
		}

		final LinkTypeController ltc = (LinkTypeController) LinkTypeController.getInstance();
		return ltc.getAlarmedColor(plink.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	/*@Override
	public int getAlarmedLineSize(final Characterizable characterizable) {
		if (!(characterizable instanceof PhysicalLink)) {
			return MapPropertiesManager.getAlarmedThickness();
		}

		final PhysicalLink physicalLink = (PhysicalLink) characterizable;

		final Characteristic ea = getCharacteristic(characterizable, this.alarmedThicknessCharType);
		if (ea != null) {
			return Integer.parseInt(ea.getValue());
		}

		final LinkTypeController ltc = (LinkTypeController) LinkTypeController.getInstance();
		return ltc.getAlarmedLineSize(physicalLink.getType());
	}*/

	public Rectangle2D getBoundingRectangle(MapElement mapElement) throws MapConnectionException, MapDataException {
		final PhysicalLink physicalLink = (PhysicalLink) mapElement;
		Rectangle2D rectangle = null;
		for(NodeLink nodeLink : physicalLink.getNodeLinks()) {
			NodeLinkController controller = (NodeLinkController) this.logicalNetLayer.getMapViewController().getController(nodeLink);
			Rectangle2D nodeLinkRectangle = controller.getBoundingRectangle(nodeLink);
			if (rectangle == null)
				rectangle = nodeLinkRectangle;
			else
				rectangle = rectangle.createUnion(nodeLinkRectangle);
		}
		return rectangle;
	}
}
