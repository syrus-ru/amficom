/**
 * $Id: LinkTypeController.java,v 1.2 2004/12/08 16:20:22 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttributeType;
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
 * @version $Revision: 1.2 $, $Date: 2004/12/08 16:20:22 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class LinkTypeController extends AbstractLinkController
{
	private static LinkTypeController instance = null;
	
	private LinkTypeController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new LinkTypeController();
		return instance;
	}

	public boolean isSelectionVisible(MapElement me)
	{
		throw new UnsupportedOperationException();
	}

	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		throw new UnsupportedOperationException();
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * точка находится на фрагменте, если она находится в рамках линий выделения
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Установить толщину линии
	 */
	public void setLineSize (MapLinkProtoElement link, int size)
	{
		ElementAttribute ea = (ElementAttribute )link.attributes.get("thickness");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ, 
					"thickness");
			if(eat == null)
				return;
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					String.valueOf(size),
					"thickness");
			link.attributes.put("thickness", ea);
		}
		ea.value = String.valueOf(size);
//		Characteristic ea = (Characteristic )link.attributes.get("thickness");
//		if(ea == null)
//		{
////			CharacteristicType eat = (CharacteristicType )Pool.get(
////					CharacteristicType.typ, 
////					"thickness");
////			if(eat == null)
////				return;
////			ea = new Characteristic(
////					"attr" + System.currentTimeMillis(),
////					eat.getName(),
////					String.valueOf(size),
////					"thickness");
//			link.attributes.put("thickness", ea);
//		}
//		ea.setValue(String.valueOf(size));
	}

	/**
	 * Получить толщину линии
	 */
	public int getLineSize (MapLinkProtoElement link)
	{
		ElementAttribute ea = (ElementAttribute )link.attributes.get("thickness");
		if(ea == null)
			return MapPropertiesManager.getThickness();
		return Integer.parseInt(ea.value);
//		Characteristic ea = (Characteristic )link.attributes.get("thickness");
//		if(ea == null)
//			return MapPropertiesManager.getThickness();
//		return Integer.parseInt(ea.getValue());
	}

	/**
	 * Установить вид линии
	 */
	public void setStyle (MapLinkProtoElement link, String style)
	{
		ElementAttribute ea = (ElementAttribute )link.attributes.get("style");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ,
					"style");
			if(eat == null)
				return;
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					style,
					"style");
			link.attributes.put("style", ea);
		}
		ea.value = style;
//		Characteristic ea = (Characteristic )link.attributes.get("style");
//		if(ea == null)
//		{
////			CharacteristicType eat = (CharacteristicType )Pool.get(
////					CharacteristicType.typ,
////					"style");
////			if(eat == null)
////				return;
////			ea = new Characteristic(
////					"attr" + System.currentTimeMillis(),
////					eat.getName(),
////					style,
////					"style");
//			link.attributes.put("style", ea);
//		}
//		ea.setValue(style);
	}

	/**
	 * Получить вид линии
	 */
	public String getStyle (MapLinkProtoElement link)
	{
		ElementAttribute ea = (ElementAttribute )link.attributes.get("style");
		if(ea == null)
			return MapPropertiesManager.getStyle();
		return ea.value;
//		Characteristic ea = (Characteristic )link.attributes.get("style");
//		if(ea == null)
//			return MapPropertiesManager.getStyle();
//		return ea.getValue();
	}

	/**
	 * Получить стиль линии
	 */
	public Stroke getStroke (MapLinkProtoElement link)
	{
		ElementAttribute ea = (ElementAttribute )link.attributes.get("style");
		if(ea == null)
			return MapPropertiesManager.getStroke();

		return LineComboBox.getStrokeByType(ea.value);
//		Characteristic ea = (Characteristic )link.attributes.get("style");
//		if(ea == null)
//			return MapPropertiesManager.getStroke();
//
//		return LineComboBox.getStrokeByType(ea.getValue());

	}

	/**
	 * Установить цвет
	 */
	public void setColor (MapLinkProtoElement link, Color color)
	{
		ElementAttribute ea = (ElementAttribute )link.attributes.get("color");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ,
					"color");
			if(eat == null)
				return;
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					String.valueOf(color.getRGB()),
					"color");
			link.attributes.put("color", ea);
		}
		ea.value = String.valueOf(color.getRGB());
//		Characteristic ea = (Characteristic )link.attributes.get("color");
//		if(ea == null)
//		{
////			CharacteristicType eat = (CharacteristicType )Pool.get(
////					CharacteristicType.typ,
////					"color");
////			if(eat == null)
////				return;
////			ea = new Characteristic(
////					"attr" + System.currentTimeMillis(),
////					eat.getName(),
////					String.valueOf(color.getRGB()),
////					"color");
//			link.attributes.put("color", ea);
//		}
//		ea.setValue(String.valueOf(color.getRGB()));
	}

	/**
	 * Получить цвет
	 */
	public Color getColor(MapLinkProtoElement link)
	{
		ElementAttribute ea = (ElementAttribute )link.attributes.get("color");
		if(ea == null)
			return MapPropertiesManager.getColor();
		return new Color(Integer.parseInt(ea.value));
//		Characteristic ea = (Characteristic )link.attributes.get("color");
//		if(ea == null)
//			return MapPropertiesManager.getColor();
//		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * установить цвет при наличии сигнала тревоги
	 */
	public void setAlarmedColor (MapLinkProtoElement link, Color color)
	{
		ElementAttribute ea = (ElementAttribute )link.attributes.get("alarmed_color");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ,
					"alarmed_color");
			if(eat == null)
				return;
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					String.valueOf(color.getRGB()),
					"alarmed_color");
			link.attributes.put("alarmed_color", ea);
		}
		ea.value = String.valueOf(color.getRGB());
//		Characteristic ea = (Characteristic )link.attributes.get("alarmed_color");
//		if(ea == null)
//		{
////			CharacteristicType eat = (CharacteristicType )Pool.get(
////					CharacteristicType.typ,
////					"alarmed_color");
////			if(eat == null)
////				return;
////			ea = new Characteristic(
////					"attr" + System.currentTimeMillis(),
////					eat.getName(),
////					String.valueOf(color.getRGB()),
////					"alarmed_color");
//			link.attributes.put("alarmed_color", ea);
//		}
//		ea.setValue(String.valueOf(color.getRGB()));
	}

	/**
	 * получить цвет при наличии сигнала тревоги
	 */
	public Color getAlarmedColor(MapLinkProtoElement link)
	{
		ElementAttribute ea = (ElementAttribute )link.attributes.get("alarmed_color");
		if(ea == null)
			return MapPropertiesManager.getAlarmedColor();
		return new Color(Integer.parseInt(ea.value));
//		Characteristic ea = (Characteristic )link.attributes.get("alarmed_color");
//		if(ea == null)
//			return MapPropertiesManager.getAlarmedColor();
//		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * установить толщину линии при наличи сигнала тревоги
	 */
	public void setAlarmedLineSize (MapLinkProtoElement link, int size)
	{
		ElementAttribute ea = (ElementAttribute )link.attributes.get("alarmed_thickness");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ, 
					"alarmed_thickness");
			if(eat == null)
				return;
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					String.valueOf(size),
					"alarmed_thickness");
			link.attributes.put("alarmed_thickness", ea);
		}
		ea.value = String.valueOf(size);
//		Characteristic ea = (Characteristic )link.attributes.get("alarmed_thickness");
//		if(ea == null)
//		{
////			CharacteristicType eat = (CharacteristicType )CharacteristicType.get(
////					CharacteristicType.typ, 
////					"alarmed_thickness");
////			if(eat == null)
////				return;
////			ea = new Characteristic(
////					"attr" + System.currentTimeMillis());
////					eat.getName(),
////					String.valueOf(size),
////					"alarmed_thickness");
//			link.attributes.put("alarmed_thickness", ea);
//		}
//		ea.setValue(String.valueOf(size));
	}

	/**
	 * получить толщину линии при наличи сигнала тревоги
	 */
	public int getAlarmedLineSize (MapLinkProtoElement link)
	{
		ElementAttribute ea = (ElementAttribute )link.attributes.get("alarmed_thickness");
		if(ea == null)
			return MapPropertiesManager.getAlarmedThickness();
		return Integer.parseInt(ea.value);
//		Characteristic ea = (Characteristic )link.attributes.get("alarmed_thickness");
//		if(ea == null)
//			return MapPropertiesManager.getAlarmedThickness();
//		return Integer.parseInt(ea.getValue());
	}
}
