/**
 * $Id: PhysicalLinkController.java,v 1.26 2005/08/11 12:43:30 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

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
 * ���������� ��������� �������� �����.
 * @author $Author: arseniy $
 * @version $Revision: 1.26 $, $Date: 2005/08/11 12:43:30 $
 * @module mapviewclient
 */
public class PhysicalLinkController extends AbstractLinkController {
	
	/**
	 * Private constructor.
	 */
	protected PhysicalLinkController(NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(NetMapViewer netMapViewer) {
		return new PhysicalLinkController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(MapElement mapElement) {
		if(!(mapElement instanceof PhysicalLink))
			return null;

		PhysicalLink link = (PhysicalLink )mapElement;
		PhysicalLinkType linkType = (PhysicalLinkType )link.getType();

		String s1 = linkType.getName() + ": " + link.getName();
		String s2 = "";
		String s3 = "";
		try {
			AbstractNode smne = link.getStartNode();
			s2 =  "\n" 
				+ "   " 
				+ LangModelMap.getString("From") 
				+ " " 
				+ smne.getName() 
				+ " [" 
				+ MapViewController.getMapElementReadableType(smne)
				+ "]";
			AbstractNode emne = link.getEndNode();
			s3 = "\n" 
				+ "   " 
				+ LangModelMap.getString("To") 
				+ " " 
				+ emne.getName() 
				+ " [" 
				+ MapViewController.getMapElementReadableType(emne)
				+ "]";
		} catch(Exception e) {
			Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getToolTipText()", 
				e);
		}
		return s1 + s2 + s3;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelectionVisible(MapElement mapElement) {
		if(!(mapElement instanceof PhysicalLink))
			return false;

		PhysicalLink link = (PhysicalLink )mapElement;

		return link.isSelected();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isElementVisible(
			MapElement mapElement,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(!(mapElement instanceof PhysicalLink))
			return false;

		PhysicalLink link = (PhysicalLink )mapElement;

		boolean vis = false;
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();) {
			NodeLink nodelink = (NodeLink )it.next();
			NodeLinkController nlc = (NodeLinkController )this.logicalNetLayer
					.getMapViewController().getController(nodelink);
			if(nlc.isElementVisible(nodelink, visibleBounds)) {
				vis = true;
				break;
			}
		}
		return vis;
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(
			MapElement mapElement,
			Graphics g,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(!(mapElement instanceof PhysicalLink))
			return;

		PhysicalLink link = (PhysicalLink )mapElement;
		
		if(!isElementVisible(link, visibleBounds))
			return;

		Stroke strokeForLink = getStroke(link);
		Color color = getColor(link);

		paint(link, g, visibleBounds, strokeForLink, color, isSelectionVisible(link));
	}

	/**
	 * ���������� ����� � �������� ������ � ������.
	 * @param link �����
	 * @param g ����������� ��������
	 * @param visibleBounds ������� �������
	 * @param stroke ����� �����
	 * @param color ���� �����
	 * @param selectionVisible �������� ����� ���������
	 */
	public void paint(
			PhysicalLink link,
			Graphics g,
			Rectangle2D.Double visibleBounds,
			Stroke stroke,
			Color color,
			boolean selectionVisible)
			throws MapConnectionException, MapDataException {
		if(!isElementVisible(link, visibleBounds))
			return;

		updateLengthLt(link);

		boolean showName = false;
		if(MapPropertiesManager.isLayerLabelVisible(link.getType())) {
			showName = true;
		}

		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();) {
			NodeLink nodelink = (NodeLink )it.next();
			NodeLinkController nlc = (NodeLinkController)this.logicalNetLayer
				.getMapViewController().getController(nodelink);
			nlc.paint(nodelink, g, visibleBounds, stroke, color, selectionVisible);
			
			if(showName) {
				MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
				Point from = converter.convertMapToScreen(nodelink.getStartNode().getLocation());
				Point to = converter.convertMapToScreen(nodelink.getEndNode().getLocation());

				g.setColor(MapPropertiesManager.getBorderColor());
				g.setFont(MapPropertiesManager.getFont());
	
				int fontHeight = g.getFontMetrics().getHeight();
				String text = link.getName();
				int textWidth = g.getFontMetrics().stringWidth(text);
				int centerX = (from.x + to.x) / 2;
				int centerY = (from.y + to.y) / 2;

				g.drawRect(
						centerX,
						centerY - fontHeight + 2,
						textWidth,
						fontHeight);
	
				g.setColor(MapPropertiesManager.getTextBackground());
				g.fillRect(
						centerX,
						centerY - fontHeight + 2,
						textWidth,
						fontHeight);
	
				g.setColor(MapPropertiesManager.getTextColor());
				g.drawString(
						text,
						centerX,
						centerY);

				showName = false;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMouseOnElement(
			MapElement mapElement,
			Point currentMousePoint)
			throws MapConnectionException, MapDataException {
		if(!(mapElement instanceof PhysicalLink))
			return false;

		PhysicalLink link = (PhysicalLink )mapElement;

		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();) {
			NodeLink nl = (NodeLink )it.next();
			NodeLinkController nlc = (NodeLinkController)this.logicalNetLayer
				.getMapViewController().getController(nl);
			if(nlc.isMouseOnElement(nl, currentMousePoint))
				return true;
		}
		return false;
	}

	/**
	 * ����������� �������������� ����� �����, �������������� �� ����������,
	 * �� ������� ������� �����.
	 * @param link �����
	 */
	public void updateLengthLt(PhysicalLink link)
			throws MapConnectionException, MapDataException {
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();) {
			NodeLink nodeLink = (NodeLink )it.next();
			NodeLinkController nlc = (NodeLinkController )this.logicalNetLayer
					.getMapViewController().getController(nodeLink);
			nlc.updateLengthLt(nodeLink);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getLineSize(MapElement mapElement) {
		if(! (mapElement instanceof PhysicalLink))
			return MapPropertiesManager.getThickness();

		PhysicalLink plink = (PhysicalLink )mapElement;

		Characteristic ea = getCharacteristic(mapElement, this.thicknessCharType);
		if(ea != null)
			return Integer.parseInt(ea.getValue());

		LinkTypeController ltc = (LinkTypeController)LinkTypeController.getInstance();
		return ltc.getLineSize((PhysicalLinkType )plink.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	public String getStyle(MapElement mapElement) {
		if(!(mapElement instanceof PhysicalLink))
			return MapPropertiesManager.getStyle();

		PhysicalLink plink = (PhysicalLink )mapElement;

		Characteristic ea = getCharacteristic(mapElement, this.styleCharType);
		if(ea != null)
			return ea.getValue();

		LinkTypeController ltc = (LinkTypeController)LinkTypeController.getInstance();
		return ltc.getStyle((PhysicalLinkType )plink.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	public Color getColor(MapElement mapElement) {
		if(!(mapElement instanceof PhysicalLink))
			return MapPropertiesManager.getColor();

		PhysicalLink plink = (PhysicalLink )mapElement;

		Characteristic ea = getCharacteristic(mapElement, this.colorCharType);
		if(ea != null)
		{
			Color color = (Color)this.colors.get(ea.getValue());
			if (color == null)
			{
				color = new Color(Integer.parseInt(ea.getValue()));
				this.colors.put(ea.getValue(),color);
			}
			return color;
		}
		LinkTypeController ltc = (LinkTypeController)LinkTypeController.getInstance();
		return ltc.getColor((PhysicalLinkType )plink.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	public Color getAlarmedColor(MapElement mapElement) {
		if(! (mapElement instanceof PhysicalLink))
			return MapPropertiesManager.getAlarmedColor();

		PhysicalLink plink = (PhysicalLink )mapElement;

		Characteristic ea = getCharacteristic(mapElement, this.alarmedColorCharType);
		if(ea != null)
			return new Color(Integer.parseInt(ea.getValue()));

		LinkTypeController ltc = (LinkTypeController)LinkTypeController.getInstance();
		return ltc.getAlarmedColor((PhysicalLinkType )plink.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	public int getAlarmedLineSize(MapElement mapElement) {
		if(! (mapElement instanceof PhysicalLink))
			return MapPropertiesManager.getAlarmedThickness();

		PhysicalLink plink = (PhysicalLink )mapElement;

		Characteristic ea = getCharacteristic(mapElement, this.alarmedThicknessCharType);
		if(ea != null)
			return Integer.parseInt(ea.getValue());

		LinkTypeController ltc = (LinkTypeController)LinkTypeController.getInstance();
		return ltc.getAlarmedLineSize((PhysicalLinkType )plink.getType());
	}
}
