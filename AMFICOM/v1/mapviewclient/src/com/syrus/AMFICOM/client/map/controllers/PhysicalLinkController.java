/**
 * $Id: PhysicalLinkController.java,v 1.7 2005/02/07 16:09:26 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.LineComboBox;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import java.util.Iterator;

/**
 * Контроллер линейного элемента карты.
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/02/07 16:09:26 $
 * @module mapviewclient_v1
 */
public class PhysicalLinkController extends AbstractLinkController
{
	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapLinkPane";

	/**
	 * Instance
	 */
	private static PhysicalLinkController instance = null;

	/**
	 * Получить имя класса панели, описывающей свойства кабельного пути.
	 * @return имя класса
	 */
	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	/**
	 * Private constructor.
	 */
	protected PhysicalLinkController()
	{// empty
	}
	
	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new PhysicalLinkController();
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(MapElement mapElement)
	{
		if(! (mapElement instanceof PhysicalLink))
			return null;

		PhysicalLink link = (PhysicalLink )mapElement;
		
		String s1 = link.getName();
		String s2 = "";
		String s3 = "";
		try
		{
			AbstractNode smne = link.getStartNode();
			s2 =  ":\n" 
				+ "   " 
				+ LangModelMap.getString("From") 
				+ " " 
				+ smne.getName() 
				+ " [" 
				+ LangModel.getString("node" + smne.getClass().getName()) 
				+ "]";
			AbstractNode emne = link.getEndNode();
			s3 = "\n" 
				+ "   " 
				+ LangModelMap.getString("To") 
				+ " " 
				+ emne.getName() 
				+ " [" 
				+ LangModel.getString("node" + emne.getClass().getName()) 
				+ "]";
		}
		catch(Exception e)
		{
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
	public boolean isSelectionVisible(MapElement mapElement)
	{
		if(! (mapElement instanceof PhysicalLink))
			return false;

		PhysicalLink link = (PhysicalLink )mapElement;

		return link.isSelected() 
			|| link.isSelectionVisible();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isElementVisible(MapElement mapElement, Rectangle2D.Double visibleBounds)
	{
		if(! (mapElement instanceof PhysicalLink))
			return false;

		PhysicalLink link = (PhysicalLink )mapElement;
		
		boolean vis = false;
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nodelink = (NodeLink )it.next();
			NodeLinkController nlc = (NodeLinkController)getLogicalNetLayer()
				.getMapViewController().getController(nodelink);
			if(nlc.isElementVisible(nodelink, visibleBounds))
			{
				vis = true;
				break;
			}
		}
		return vis;
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint (MapElement mapElement, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(! (mapElement instanceof PhysicalLink))
			return;

		PhysicalLink link = (PhysicalLink )mapElement;
		
		if(!isElementVisible(link, visibleBounds))
			return;

		BasicStroke stroke = (BasicStroke )getStroke(link);
		Stroke str = new BasicStroke(
				getLineSize(link),
				stroke.getEndCap(),
				stroke.getLineJoin(),
				stroke.getMiterLimit(),
				stroke.getDashArray(),
				stroke.getDashPhase());
		Color color = getColor(link);

		paint(link, g, visibleBounds, str, color, false);
	}

	/**
	 * Отрисовать линию с заданным стилем и цветом.
	 * @param link линия
	 * @param g графический контекст
	 * @param visibleBounds видимая область
	 * @param stroke стиль линии
	 * @param color цвет линии
	 * @param selectionVisible рисовать рамку выделения
	 */
	public void paint(
			PhysicalLink link,
			Graphics g, 
			Rectangle2D.Double visibleBounds, 
			Stroke stroke, 
			Color color, 
			boolean selectionVisible)
	{
		if(!isElementVisible(link, visibleBounds))
			return;

		updateLengthLt(link);

		boolean showName = false;
		if(MapPropertiesManager.isShowLinkNames())
		{
			showName = true;
		}

		link.setSelectionVisible(selectionVisible);
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nodelink = (NodeLink )it.next();
			NodeLinkController nlc = (NodeLinkController)getLogicalNetLayer()
				.getMapViewController().getController(nodelink);
			nlc.paint(nodelink, g, visibleBounds, stroke, color);
			
			if(showName)
			{
				MapCoordinatesConverter converter = getLogicalNetLayer();
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
	public boolean isMouseOnElement(MapElement mapElement, Point currentMousePoint)
	{
		if(! (mapElement instanceof PhysicalLink))
			return false;

		PhysicalLink link = (PhysicalLink )mapElement;
		
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nl = (NodeLink )it.next();
			NodeLinkController nlc = (NodeLinkController)getLogicalNetLayer()
				.getMapViewController().getController(nl);
			if(nlc.isMouseOnElement(nl, currentMousePoint))
				return true;
		}
		return false;
	}

	/**
	 * Пересчитать топологическую длину линии, складывающуюся из фрагментов,
	 * из которых состоит линия.
	 * @param link линия
	 */
	public void updateLengthLt(PhysicalLink link)
	{
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nodeLink = (NodeLink )it.next();
			NodeLinkController nlc = (NodeLinkController)getLogicalNetLayer()
				.getMapViewController().getController(nodeLink);
			nlc.updateLengthLt(nodeLink);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getLineSize (MapElement mapElement)
	{
		if(! (mapElement instanceof PhysicalLink))
			return MapPropertiesManager.getThickness();

		PhysicalLink plink = (PhysicalLink )mapElement;

		CharacteristicType cType = getCharacteristicType(
			getLogicalNetLayer().getUserId(), 
			AbstractLinkController.ATTRIBUTE_THICKNESS);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea != null)
			return Integer.parseInt(ea.getValue());

		LinkTypeController ltc = (LinkTypeController)LinkTypeController.getInstance();
		return ltc.getLineSize(getLogicalNetLayer().getUserId(), (PhysicalLinkType )plink.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	public String getStyle (MapElement mapElement)
	{
		if(! (mapElement instanceof PhysicalLink))
			return MapPropertiesManager.getStyle();

		PhysicalLink plink = (PhysicalLink )mapElement;

		CharacteristicType cType = getCharacteristicType(
			getLogicalNetLayer().getUserId(), 
			AbstractLinkController.ATTRIBUTE_STYLE);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea != null)
			return ea.getValue();

		LinkTypeController ltc = (LinkTypeController)LinkTypeController.getInstance();
		return ltc.getStyle(getLogicalNetLayer().getUserId(), (PhysicalLinkType )plink.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	public Stroke getStroke (MapElement mapElement)
	{
		if(! (mapElement instanceof PhysicalLink))
			return MapPropertiesManager.getStroke();

		PhysicalLink plink = (PhysicalLink )mapElement;

		CharacteristicType cType = getCharacteristicType(
			getLogicalNetLayer().getUserId(), 
			AbstractLinkController.ATTRIBUTE_STYLE);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea != null)
			return LineComboBox.getStrokeByType(ea.getValue());

		LinkTypeController ltc = (LinkTypeController)LinkTypeController.getInstance();
		return ltc.getStroke(getLogicalNetLayer().getUserId(), (PhysicalLinkType )plink.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	public Color getColor(MapElement mapElement)
	{
		if(! (mapElement instanceof PhysicalLink))
			return MapPropertiesManager.getColor();

		PhysicalLink plink = (PhysicalLink )mapElement;

		CharacteristicType cType = getCharacteristicType(
			getLogicalNetLayer().getUserId(), 
			AbstractLinkController.ATTRIBUTE_COLOR);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea != null)
			return new Color(Integer.parseInt(ea.getValue()));

		LinkTypeController ltc = (LinkTypeController)LinkTypeController.getInstance();
		return ltc.getColor(getLogicalNetLayer().getUserId(), (PhysicalLinkType )plink.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	public Color getAlarmedColor(MapElement mapElement)
	{
		if(! (mapElement instanceof PhysicalLink))
			return MapPropertiesManager.getAlarmedColor();

		PhysicalLink plink = (PhysicalLink )mapElement;

		CharacteristicType cType = getCharacteristicType(
			getLogicalNetLayer().getUserId(), 
			AbstractLinkController.ATTRIBUTE_COLOR);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea != null)
			return new Color(Integer.parseInt(ea.getValue()));

		LinkTypeController ltc = (LinkTypeController)LinkTypeController.getInstance();
		return ltc.getAlarmedColor(getLogicalNetLayer().getUserId(), (PhysicalLinkType )plink.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	public int getAlarmedLineSize (MapElement mapElement)
	{
		if(! (mapElement instanceof PhysicalLink))
			return MapPropertiesManager.getAlarmedThickness();

		PhysicalLink plink = (PhysicalLink )mapElement;

		CharacteristicType cType = getCharacteristicType(
			getLogicalNetLayer().getUserId(), 
			AbstractLinkController.ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea != null)
			return Integer.parseInt(ea.getValue());

		LinkTypeController ltc = (LinkTypeController)LinkTypeController.getInstance();
		return ltc.getAlarmedLineSize(getLogicalNetLayer().getUserId(), (PhysicalLinkType )plink.getType());
	}
}
