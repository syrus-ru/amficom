/**
 * $Id: PhysicalLinkController.java,v 1.1 2004/12/07 17:05:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.LineComboBox;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.CharacteristicType;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.Identifier;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;

/**
 * линейный элемента карты 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/07 17:05:54 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class PhysicalLinkController extends AbstractLinkController
{
	private static PhysicalLinkController instance = null;
	
	protected PhysicalLinkController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new PhysicalLinkController();
		return instance;
	}

	public boolean isSelectionVisible(MapElement me)
	{
		if(! (me instanceof MapPhysicalLinkElement))
			return false;

		MapPhysicalLinkElement link = (MapPhysicalLinkElement )me;

		return link.isSelected() 
			|| link.selectionVisible;
	}

	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		if(! (me instanceof MapPhysicalLinkElement))
			return false;

		MapPhysicalLinkElement link = (MapPhysicalLinkElement )me;
		
		boolean vis = false;
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodelink = (MapNodeLinkElement )it.next();
			NodeLinkController nlc = (NodeLinkController )getLogicalNetLayer().getMapViewController().getController(nodelink);
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
		if(! (me instanceof MapPhysicalLinkElement))
			return;

		MapPhysicalLinkElement link = (MapPhysicalLinkElement )me;
		
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
			MapPhysicalLinkElement link,
			Graphics g, 
			Rectangle2D.Double visibleBounds, 
			Stroke stroke, 
			Color color, 
			boolean selectionVisible)
	{
		if(!isElementVisible(link, visibleBounds))
			return;

		link.updateLengthLt();

		boolean showName = false;
		if(MapPropertiesManager.isShowLinkNames())
		{
			showName = true;
		}

		link.selectionVisible = selectionVisible;
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodelink = (MapNodeLinkElement )it.next();
			NodeLinkController nlc = (NodeLinkController )getLogicalNetLayer().getMapViewController().getController(nodelink);
			nlc.paint(nodelink, g, visibleBounds, stroke, color);
			
			if(showName)
			{
				MapCoordinatesConverter converter = link.getMap().getConverter();
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
	 * точка находится на фрагменте, если она находится в рамках линий выделения
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		if(! (me instanceof MapPhysicalLinkElement))
			return false;

		MapPhysicalLinkElement link = (MapPhysicalLinkElement )me;
		
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nl = (MapNodeLinkElement )it.next();
			NodeLinkController nlc = (NodeLinkController )getLogicalNetLayer().getMapViewController().getController(nl);
			if(nlc.isMouseOnElement(nl, currentMousePoint))
				return true;
		}
		return false;
	}

	/**
	 * Получить толщину линии
	 */
	public int getLineSize (MapLinkElement link)
	{
		if(! (link instanceof MapPhysicalLinkElement))
			return MapPropertiesManager.getThickness();

		MapPhysicalLinkElement plink = (MapPhysicalLinkElement )link;

		LinkTypeController ltc = LinkTypeController.getInstance();
		return ltc.getLineSize(plink.getProto());
//		Characteristic ea = (Characteristic )link.attributes.get("thickness");
//		if(ea == null)
//			return MapPropertiesManager.getThickness();
//		return Integer.parseInt(ea.getValue());
	}

	/**
	 * Получить вид линии
	 */
	public String getStyle (MapLinkElement link)
	{
		if(! (link instanceof MapPhysicalLinkElement))
			return MapPropertiesManager.getStyle();

		MapPhysicalLinkElement plink = (MapPhysicalLinkElement )link;

		LinkTypeController ltc = LinkTypeController.getInstance();
		return ltc.getStyle(plink.getProto());
//		Characteristic ea = (Characteristic )link.attributes.get("style");
//		if(ea == null)
//			return MapPropertiesManager.getStyle();
//		return ea.getValue();
	}

	/**
	 * Получить стиль линии
	 */
	public Stroke getStroke (MapLinkElement link)
	{
		if(! (link instanceof MapPhysicalLinkElement))
			return MapPropertiesManager.getStroke();

		MapPhysicalLinkElement plink = (MapPhysicalLinkElement )link;

		LinkTypeController ltc = LinkTypeController.getInstance();
		return ltc.getStroke(plink.getProto());
//		Characteristic ea = (Characteristic )link.attributes.get("style");
//		if(ea == null)
//			return MapPropertiesManager.getStroke();
//
//		return LineComboBox.getStrokeByType(ea.getValue());

	}

	/**
	 * Получить цвет
	 */
	public Color getColor(MapLinkElement link)
	{
		if(! (link instanceof MapPhysicalLinkElement))
			return MapPropertiesManager.getColor();

		MapPhysicalLinkElement plink = (MapPhysicalLinkElement )link;

		LinkTypeController ltc = LinkTypeController.getInstance();
		return ltc.getColor(plink.getProto());
//		Characteristic ea = (Characteristic )link.attributes.get("color");
//		if(ea == null)
//			return MapPropertiesManager.getColor();
//		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * получить цвет при наличии сигнала тревоги
	 */
	public Color getAlarmedColor(MapLinkElement link)
	{
		if(! (link instanceof MapPhysicalLinkElement))
			return MapPropertiesManager.getAlarmedColor();

		MapPhysicalLinkElement plink = (MapPhysicalLinkElement )link;

		LinkTypeController ltc = LinkTypeController.getInstance();
		return ltc.getAlarmedColor(plink.getProto());
//		Characteristic ea = (Characteristic )link.attributes.get("alarmed_color");
//		if(ea == null)
//			return MapPropertiesManager.getAlarmedColor();
//		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * получить толщину линии при наличи сигнала тревоги
	 */
	public int getAlarmedLineSize (MapLinkElement link)
	{
		if(! (link instanceof MapPhysicalLinkElement))
			return MapPropertiesManager.getAlarmedThickness();

		MapPhysicalLinkElement plink = (MapPhysicalLinkElement )link;

		LinkTypeController ltc = LinkTypeController.getInstance();
		return ltc.getAlarmedLineSize(plink.getProto());
//		Characteristic ea = (Characteristic )link.attributes.get("alarmed_thickness");
//		if(ea == null)
//			return MapPropertiesManager.getAlarmedThickness();
//		return Integer.parseInt(ea.getValue());
	}
}
