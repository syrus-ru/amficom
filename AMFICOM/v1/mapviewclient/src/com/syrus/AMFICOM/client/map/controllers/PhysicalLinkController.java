/**
 * $Id: PhysicalLinkController.java,v 1.5 2005/02/02 08:58:10 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.Client.Map.Controllers.AbstractLinkController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeLinkController;

/**
 * линейный элемента карты 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2005/02/02 08:58:10 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class PhysicalLinkController extends AbstractLinkController
{
	private static PhysicalLinkController instance = null;
	
	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapLinkPane";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	protected PhysicalLinkController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new PhysicalLinkController();
		return instance;
	}

	public String getToolTipText(MapElement me)
	{
		if(! (me instanceof PhysicalLink))
			return null;

		PhysicalLink link = (PhysicalLink )me;
		
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

	public boolean isSelectionVisible(MapElement me)
	{
		if(! (me instanceof PhysicalLink))
			return false;

		PhysicalLink link = (PhysicalLink )me;

		return link.isSelected() 
			|| link.isSelectionVisible();
	}

	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		if(! (me instanceof PhysicalLink))
			return false;

		PhysicalLink link = (PhysicalLink )me;
		
		boolean vis = false;
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nodelink = (NodeLink )it.next();
			NodeLinkController nlc = (NodeLinkController)getLogicalNetLayer().getMapViewController().getController(nodelink);
			if(nlc.isElementVisible(nodelink, visibleBounds))
			{
				vis = true;
				break;
			}
		}
		return vis;
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(! (me instanceof PhysicalLink))
			return;

		PhysicalLink link = (PhysicalLink )me;
		
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
			NodeLinkController nlc = (NodeLinkController)getLogicalNetLayer().getMapViewController().getController(nodelink);
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

	public void updateLengthLt(PhysicalLink link)
	{
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nodeLink = (NodeLink )it.next();
			NodeLinkController nlc = (NodeLinkController)getLogicalNetLayer().getMapViewController().getController(nodeLink);
			nlc.updateLengthLt(nodeLink);
		}
	}

	/**
	 * точка находится на фрагменте, если она находится в рамках линий выделения
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		if(! (me instanceof PhysicalLink))
			return false;

		PhysicalLink link = (PhysicalLink )me;
		
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nl = (NodeLink )it.next();
			NodeLinkController nlc = (NodeLinkController)getLogicalNetLayer().getMapViewController().getController(nl);
			if(nlc.isMouseOnElement(nl, currentMousePoint))
				return true;
		}
		return false;
	}

	/**
	 * Получить толщину линии
	 */
	public int getLineSize (MapElement link)
	{
		if(! (link instanceof PhysicalLink))
			return MapPropertiesManager.getThickness();

		PhysicalLink plink = (PhysicalLink )link;

		CharacteristicType cType = getCharacteristicType(getLogicalNetLayer().getUserId(), ATTRIBUTE_THICKNESS);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea != null)
			return Integer.parseInt(ea.getValue());
		else
		{
			LinkTypeController ltc = (LinkTypeController)LinkTypeController.getInstance();
			return ltc.getLineSize((PhysicalLinkType )plink.getType());
		}
	}

	/**
	 * Получить вид линии
	 */
	public String getStyle (MapElement link)
	{
		if(! (link instanceof PhysicalLink))
			return MapPropertiesManager.getStyle();

		PhysicalLink plink = (PhysicalLink )link;

		CharacteristicType cType = getCharacteristicType(getLogicalNetLayer().getUserId(), ATTRIBUTE_STYLE);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea != null)
			return ea.getValue();
		else
		{
			LinkTypeController ltc = (LinkTypeController)com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController.getInstance();
			return ltc.getStyle((PhysicalLinkType )plink.getType());
		}
	}

	/**
	 * Получить стиль линии
	 */
	public Stroke getStroke (MapElement link)
	{
		if(! (link instanceof PhysicalLink))
			return MapPropertiesManager.getStroke();

		PhysicalLink plink = (PhysicalLink )link;

		CharacteristicType cType = getCharacteristicType(getLogicalNetLayer().getUserId(), ATTRIBUTE_STYLE);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea != null)
			return LineComboBox.getStrokeByType(ea.getValue());
		else
		{
			LinkTypeController ltc = (LinkTypeController)LinkTypeController.getInstance();
			return ltc.getStroke((PhysicalLinkType )plink.getType());
		}
	}

	/**
	 * Получить цвет
	 */
	public Color getColor(MapElement link)
	{
		if(! (link instanceof PhysicalLink))
			return MapPropertiesManager.getColor();

		PhysicalLink plink = (PhysicalLink )link;

		CharacteristicType cType = getCharacteristicType(getLogicalNetLayer().getUserId(), ATTRIBUTE_COLOR);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea != null)
			return new Color(Integer.parseInt(ea.getValue()));
		else
		{
			LinkTypeController ltc = (LinkTypeController)com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController.getInstance();
			return ltc.getColor((PhysicalLinkType )plink.getType());
		}
	}

	/**
	 * получить цвет при наличии сигнала тревоги
	 */
	public Color getAlarmedColor(MapElement link)
	{
		if(! (link instanceof PhysicalLink))
			return MapPropertiesManager.getAlarmedColor();

		PhysicalLink plink = (PhysicalLink )link;

		CharacteristicType cType = getCharacteristicType(getLogicalNetLayer().getUserId(), ATTRIBUTE_COLOR);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea != null)
			return new Color(Integer.parseInt(ea.getValue()));
		else
		{
			LinkTypeController ltc = (LinkTypeController)com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController.getInstance();
			return ltc.getAlarmedColor((PhysicalLinkType )plink.getType());
		}
	}

	/**
	 * получить толщину линии при наличи сигнала тревоги
	 */
	public int getAlarmedLineSize (MapElement link)
	{
		if(! (link instanceof PhysicalLink))
			return MapPropertiesManager.getAlarmedThickness();

		PhysicalLink plink = (PhysicalLink )link;

		CharacteristicType cType = getCharacteristicType(getLogicalNetLayer().getUserId(), ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea != null)
			return Integer.parseInt(ea.getValue());
		else
		{
			LinkTypeController ltc = (LinkTypeController)com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController.getInstance();
			return ltc.getAlarmedLineSize((PhysicalLinkType )plink.getType());
		}
	}
}
