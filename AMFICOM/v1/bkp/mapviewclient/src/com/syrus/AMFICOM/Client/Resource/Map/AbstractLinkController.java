/**
 * $Id: AbstractLinkController.java,v 1.1 2004/12/07 17:05:54 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.CharacteristicType;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.Identifier;
import java.awt.Color;
import java.awt.Stroke;

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
public abstract class AbstractLinkController implements MapElementController
{
	protected LogicalNetLayer lnl;

	public void setLogicalNetLayer(LogicalNetLayer lnl)
	{
		this.lnl = lnl;
	}
	
	public LogicalNetLayer getLogicalNetLayer()
	{
		return lnl;
	}

	public abstract boolean isSelectionVisible(MapElement me);

	public String getToolTipText(MapElement me)
	{
		if(! (me instanceof MapLinkElement))
			return null;

		MapLinkElement link = (MapLinkElement )me;
		
		String s1 = link.getName();
		String s2 = "";
		String s3 = "";
		try
		{
			MapNodeElement smne = link.getStartNode();
			s2 =  ":\n" 
				+ "   " 
				+ LangModelMap.getString("From") 
				+ " " 
				+ smne.getName() 
				+ " [" 
				+ LangModel.getString("node" + smne.getTyp()) 
				+ "]";
			MapNodeElement emne = link.getEndNode();
			s3 = "\n" 
				+ "   " 
				+ LangModelMap.getString("To") 
				+ " " 
				+ emne.getName() 
				+ " [" 
				+ LangModel.getString("node" + emne.getTyp()) 
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
	 * Установить толщину линии
	 */
	public void setLineSize (MapLinkElement link, int size)
	{
		Characteristic ea = (Characteristic )link.attributes.get("thickness");
		if(ea == null)
		{
//			CharacteristicType eat = (CharacteristicType )Pool.get(
//					CharacteristicType.typ, 
//					"thickness");
//			if(eat == null)
//				return;
//			ea = new Characteristic(
//					"attr" + System.currentTimeMillis(),
//					eat.getName(),
//					String.valueOf(size),
//					"thickness");
			link.attributes.put("thickness", ea);
		}
		ea.setValue(String.valueOf(size));
	}

	/**
	 * Получить толщину линии
	 */
	public int getLineSize (MapLinkElement link)
	{
		Characteristic ea = (Characteristic )link.attributes.get("thickness");
		if(ea == null)
			return MapPropertiesManager.getThickness();
		return Integer.parseInt(ea.getValue());
	}

	/**
	 * Установить вид линии
	 */
	public void setStyle (MapLinkElement link, String style)
	{
		Characteristic ea = (Characteristic )link.attributes.get("style");
		if(ea == null)
		{
//			CharacteristicType eat = (CharacteristicType )Pool.get(
//					CharacteristicType.typ,
//					"style");
//			if(eat == null)
//				return;
//			ea = new Characteristic(
//					"attr" + System.currentTimeMillis(),
//					eat.getName(),
//					style,
//					"style");
			link.attributes.put("style", ea);
		}
		ea.setValue(style);
	}

	/**
	 * Получить вид линии
	 */
	public String getStyle (MapLinkElement link)
	{
		Characteristic ea = (Characteristic )link.attributes.get("style");
		if(ea == null)
			return MapPropertiesManager.getStyle();
		return ea.getValue();
	}

	/**
	 * Получить стиль линии
	 */
	public Stroke getStroke (MapLinkElement link)
	{
		Characteristic ea = (Characteristic )link.attributes.get("style");
		if(ea == null)
			return MapPropertiesManager.getStroke();

		return LineComboBox.getStrokeByType(ea.getValue());

	}

	/**
	 * Установить цвет
	 */
	public void setColor (MapLinkElement link, Color color)
	{
		Characteristic ea = (Characteristic )link.attributes.get("color");
		if(ea == null)
		{
//			CharacteristicType eat = (CharacteristicType )Pool.get(
//					CharacteristicType.typ,
//					"color");
//			if(eat == null)
//				return;
//			ea = new Characteristic(
//					"attr" + System.currentTimeMillis(),
//					eat.getName(),
//					String.valueOf(color.getRGB()),
//					"color");
			link.attributes.put("color", ea);
		}
		ea.setValue(String.valueOf(color.getRGB()));
	}

	/**
	 * Получить цвет
	 */
	public Color getColor(MapLinkElement link)
	{
		Characteristic ea = (Characteristic )link.attributes.get("color");
		if(ea == null)
			return MapPropertiesManager.getColor();
		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * установить цвет при наличии сигнала тревоги
	 */
	public void setAlarmedColor (MapLinkElement link, Color color)
	{
		Characteristic ea = (Characteristic )link.attributes.get("alarmed_color");
		if(ea == null)
		{
//			CharacteristicType eat = (CharacteristicType )Pool.get(
//					CharacteristicType.typ,
//					"alarmed_color");
//			if(eat == null)
//				return;
//			ea = new Characteristic(
//					"attr" + System.currentTimeMillis(),
//					eat.getName(),
//					String.valueOf(color.getRGB()),
//					"alarmed_color");
			link.attributes.put("alarmed_color", ea);
		}
		ea.setValue(String.valueOf(color.getRGB()));
	}

	/**
	 * получить цвет при наличии сигнала тревоги
	 */
	public Color getAlarmedColor(MapLinkElement link)
	{
		Characteristic ea = (Characteristic )link.attributes.get("alarmed_color");
		if(ea == null)
			return MapPropertiesManager.getAlarmedColor();
		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * установить толщину линии при наличи сигнала тревоги
	 */
	public void setAlarmedLineSize (MapLinkElement link, int size)
	{
		Characteristic ea = (Characteristic )link.attributes.get("alarmed_thickness");
		if(ea == null)
		{
//			CharacteristicType eat = (CharacteristicType )CharacteristicType.get(
//					CharacteristicType.typ, 
//					"alarmed_thickness");
//			if(eat == null)
//				return;
//			ea = new Characteristic(
//					"attr" + System.currentTimeMillis());
//					eat.getName(),
//					String.valueOf(size),
//					"alarmed_thickness");
			link.attributes.put("alarmed_thickness", ea);
		}
		ea.setValue(String.valueOf(size));
	}

	/**
	 * получить толщину линии при наличи сигнала тревоги
	 */
	public int getAlarmedLineSize (MapLinkElement link)
	{
		Characteristic ea = (Characteristic )link.attributes.get("alarmed_thickness");
		if(ea == null)
			return MapPropertiesManager.getAlarmedThickness();
		return Integer.parseInt(ea.getValue());
	}
}
