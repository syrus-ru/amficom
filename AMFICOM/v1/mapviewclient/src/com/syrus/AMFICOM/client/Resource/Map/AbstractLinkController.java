/**
 * $Id: AbstractLinkController.java,v 1.3 2004/12/23 16:57:59 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.General.UI.LineComboBox;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.CharacteristicType;
import com.syrus.AMFICOM.configuration.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StringFieldCondition;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;

import java.awt.Color;
import java.awt.Stroke;

import java.util.Iterator;
import java.util.List;

/**
 * линейный элемента карты 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/12/23 16:57:59 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public abstract class AbstractLinkController implements MapElementController
{
	public static final String ATTRIBUTE_THICKNESS = "thickness";
	public static final String ATTRIBUTE_COLOR = "color";
	public static final String ATTRIBUTE_STYLE = "style";
	public static final String ATTRIBUTE_ALARMED_THICKNESS = "alarmed_thckness";
	public static final String ATTRIBUTE_ALARMED_COLOR = "alarmed_color";

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

	public static CharacteristicType getCharacteristicType(
			Identifier userId, 
			String codename)
	{
		CharacteristicTypeSort sort = CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL;
		DataType dataType = DataType.DATA_TYPE_STRING;

		StorableObjectCondition pTypeCondition = new StringFieldCondition(
			codename,
			ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE,
			StringFieldSort.STRINGSORT_BASE);

		try
		{
			List pTypes =
				MeasurementStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
			for (Iterator it = pTypes.iterator(); it.hasNext();)
			{
				CharacteristicType type = (CharacteristicType )it.next();
				if (type.getCodename().equals(codename))
					return type;
			}
		}
		catch(ApplicationException ex)
		{
			System.err.println("Exception searching CharacteristicType. Creating new one.");
			ex.printStackTrace();
		}

		try
		{
			return CharacteristicType.createInstance(
					userId,
					codename,
					"",
					dataType.value(),
					sort);
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static Characteristic getCharacteristic(
			MapElement me, 
			CharacteristicType cType)
	{
		for(Iterator it = me.getCharacteristics().iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic )it.next();
			if(ch.getType().equals(cType))
				return ch;
		}
		return null;
	}

	/**
	 * Установить толщину линии
	 */
	public void setLineSize (MapElement link, int size)
	{
		CharacteristicType cType = getCharacteristicType(link.getMap().getCreatorId(), ATTRIBUTE_THICKNESS);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						link.getMap().getCreatorId(),
						cType,
						"",
						"",
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						link.getId(),
						true,
						true);
				link.addCharacteristic(ea);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(size));
	}

	/**
	 * Получить толщину линии
	 */
	public int getLineSize (MapElement link)
	{
		CharacteristicType cType = getCharacteristicType(link.getMap().getCreatorId(), ATTRIBUTE_THICKNESS);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getThickness();
		return Integer.parseInt(ea.getValue());
	}

	/**
	 * Установить вид линии
	 */
	public void setStyle (MapElement link, String style)
	{
		CharacteristicType cType = getCharacteristicType(link.getMap().getCreatorId(), ATTRIBUTE_STYLE);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						link.getMap().getCreatorId(),
						cType,
						"",
						"",
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						link.getId(),
						true,
						true);
				link.addCharacteristic(ea);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(style);
	}

	/**
	 * Получить вид линии
	 */
	public String getStyle (MapElement link)
	{
		CharacteristicType cType = getCharacteristicType(link.getMap().getCreatorId(), ATTRIBUTE_STYLE);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getStyle();
		return ea.getValue();
	}

	/**
	 * Получить стиль линии
	 */
	public Stroke getStroke (MapElement link)
	{
		CharacteristicType cType = getCharacteristicType(link.getMap().getCreatorId(), ATTRIBUTE_STYLE);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getStroke();

		return LineComboBox.getStrokeByType(ea.getValue());
	}

	/**
	 * Установить цвет
	 */
	public void setColor (MapElement link, Color color)
	{
		CharacteristicType cType = getCharacteristicType(link.getMap().getCreatorId(), ATTRIBUTE_COLOR);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						link.getMap().getCreatorId(),
						cType,
						"",
						"",
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						link.getId(),
						true,
						true);
				link.addCharacteristic(ea);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(color.getRGB()));
	}

	/**
	 * Получить цвет
	 */
	public Color getColor(MapElement link)
	{
		CharacteristicType cType = getCharacteristicType(link.getMap().getCreatorId(), ATTRIBUTE_COLOR);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getColor();
		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * установить цвет при наличии сигнала тревоги
	 */
	public void setAlarmedColor (MapElement link, Color color)
	{
		CharacteristicType cType = getCharacteristicType(link.getMap().getCreatorId(), ATTRIBUTE_ALARMED_COLOR);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						link.getMap().getCreatorId(),
						cType,
						"",
						"",
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						link.getId(),
						true,
						true);
				link.addCharacteristic(ea);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(color.getRGB()));
	}

	/**
	 * получить цвет при наличии сигнала тревоги
	 */
	public Color getAlarmedColor(MapElement link)
	{
		CharacteristicType cType = getCharacteristicType(link.getMap().getCreatorId(), ATTRIBUTE_ALARMED_COLOR);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getAlarmedColor();
		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * установить толщину линии при наличи сигнала тревоги
	 */
	public void setAlarmedLineSize (MapElement link, int size)
	{
		CharacteristicType cType = getCharacteristicType(link.getMap().getCreatorId(), ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						link.getMap().getCreatorId(),
						cType,
						"",
						"",
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						link.getId(),
						true,
						true);
				link.addCharacteristic(ea);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(size));
	}

	/**
	 * получить толщину линии при наличи сигнала тревоги
	 */
	public int getAlarmedLineSize (MapElement link)
	{
		CharacteristicType cType = getCharacteristicType(link.getMap().getCreatorId(), ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getAlarmedThickness();
		return Integer.parseInt(ea.getValue());
	}
}
