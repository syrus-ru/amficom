/**
 * $Id: LinkTypeController.java,v 1.19 2005/05/05 09:37:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.UI.LineComboBox;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.map.IntDimension;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.PhysicalLinkTypeSort;
import com.syrus.AMFICOM.map.SiteNodeType;

/**
 * Контроллер типа линейного элемента карты.
 * @author $Author: krupenn $
 * @version $Revision: 1.19 $, $Date: 2005/05/05 09:37:42 $
 * @module mapviewclient_v1
 */
public final class LinkTypeController extends AbstractLinkController
{
	/**
	 * Хэш-таблица цветов типов линий. Для того, чтобы объект {@link Color} 
	 * не создавался каждый раз из атрибута при вызове 
	 * {@link #getColor(Identifier, PhysicalLinkType)}, созданный объект помещается в
	 * хэш-таблицу и при последующих обращениях используется повторно.
	 */
	private static java.util.Map colorsHolder = new HashMap();
	/**
	 * Хэш-таблица цветов сигнала тревоги для типов линий. Для того, чтобы 
	 * объект {@link Color} не создавался каждый раз из атрибута при вызове 
	 * {@link #getAlarmedColor(Identifier, PhysicalLinkType)}, созданный объект помещается в
	 * хэш-таблицу и при последующих обращениях используется повторно.
	 */
	private static java.util.Map alarmedColorsHolder = new HashMap();

	/** Хэш-таблица цветов для предустановленных типов линии. */
	private static java.util.Map lineColors = new HashMap();
	/** Хэш-таблица толщины линии для предустановленных типов линии. */
	private static java.util.Map lineThickness = new HashMap();
	/** Хэш-таблица размерности привязки для предустановленных типов линии. */
	private static java.util.Map bindDimensions = new HashMap();

	/**
	 * Instance
	 */
	private static LinkTypeController instance = null;
	
	static
	{
		lineColors.put(PhysicalLinkType.DEFAULT_COLLECTOR, Color.DARK_GRAY);
		lineColors.put(PhysicalLinkType.DEFAULT_TUNNEL, Color.BLACK);
		lineColors.put(PhysicalLinkType.DEFAULT_UNBOUND, Color.RED);

		lineThickness.put(PhysicalLinkType.DEFAULT_COLLECTOR, new Integer(3));
		lineThickness.put(PhysicalLinkType.DEFAULT_TUNNEL, new Integer(2));
		lineThickness.put(PhysicalLinkType.DEFAULT_UNBOUND, new Integer(1));

		bindDimensions.put(PhysicalLinkType.DEFAULT_COLLECTOR, new IntDimension(2, 6));
		bindDimensions.put(PhysicalLinkType.DEFAULT_TUNNEL, new IntDimension(3, 4));
		bindDimensions.put(PhysicalLinkType.DEFAULT_UNBOUND, new IntDimension(0, 0));
	}

	/**
	 * Private constructor.
	 */
	private LinkTypeController()
	{// empty
	}
	
	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new LinkTypeController();
		return instance;
	}

	/**
	 * {@inheritDoc}
	 * Suppress since PhysicalLinkType is not really a Map Element
	 */
	public String getToolTipText(MapElement mapElement)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since PhysicalLinkType is not really a Map Element
	 */
	public boolean isSelectionVisible(MapElement mapElement)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since PhysicalLinkType is not really a Map Element
	 */
	public boolean isElementVisible(MapElement mapElement, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since PhysicalLinkType is not really a Map Element
	 */
	public void paint(MapElement mapElement, Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since PhysicalLinkType is not really a Map Element
	 */
	public boolean isMouseOnElement(MapElement mapElement, Point currentMousePoint)
		throws MapConnectionException, MapDataException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Получить цвет по кодовому имени для предустановленного типа линии.
	 * @param codename кодовое имя
	 * @return цвет
	 */
	public static Color getLineColor(String codename)
	{
		return (Color )lineColors.get(codename);
	}

	/**
	 * Получить толщину линии по кодовому имени для предустановленного 
	 * типа линии.
	 * @param codename кодовое имя
	 * @return толщина
	 */
	public static int getLineThickness(String codename)
	{
		return ((Integer )lineThickness.get(codename)).intValue();
	}

	/**
	 * Получить размерность привязки по кодовому имени для предустановленного 
	 * типа линии.
	 * @param codename кодовое имя
	 * @return размерность привязки
	 */
	public static IntDimension getBindDimension(String codename)
	{
		return (IntDimension )bindDimensions.get(codename);
	}
	
	/**
	 * Найти атрибут типа линии по типу.
	 * @param linkType тип линии
	 * @param cType тип атрибута
	 * @return атрибут
	 */
	public static Characteristic getCharacteristic(
			PhysicalLinkType linkType, 
			CharacteristicType cType)
	{
		for(Iterator it = linkType.getCharacteristics().iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic )it.next();
			if(ch.getType().equals(cType))
				return ch;
		}
		return null;
	}

	/**
	 * Установить толщину линии. Толщина определяется
	 * атрибутом {@link AbstractLinkController#ATTRIBUTE_THICKNESS}. 
	 * В случае, если такого атрибута у элемента нет, создается новый.
	 * @param linkType тип линии
	 * @param size толщина линии
	 */
	public void setLineSize(Identifier userId, PhysicalLinkType linkType, int size)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_THICKNESS);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						userId,
						cType,
						"",
						"",
						CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE,
						"",
						linkType.getId(),
						true,
						true);
				linkType.addCharacteristic(ea);
				GeneralStorableObjectPool.putStorableObject(ea);
				GeneralStorableObjectPool.flush(true);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(size));
	}

	/**
	 * Получить толщину линии. Толщина определяется
	 * атрибутом {@link AbstractLinkController#ATTRIBUTE_THICKNESS}. 
	 * В случае, если такого атрибута у элемента нет, берется значение 
	 * по умолчанию ({@link MapPropertiesManager#getThickness()}).
	 * @param linkType тип линии
	 * @return толщина линии
	 */
	public int getLineSize(Identifier userId, PhysicalLinkType linkType)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_THICKNESS);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
			return MapPropertiesManager.getThickness();
		return Integer.parseInt(ea.getValue());
	}

	/**
	 * Установить стиль типа линии. Стиль определяется
	 * атрибутом {@link AbstractLinkController#ATTRIBUTE_STYLE}. В случае, если
	 * такого атрибута у элемента нет, создается новый.
	 * @param linkType тип линии
	 * @param style стиль
	 */
	public void setStyle(Identifier userId, PhysicalLinkType linkType, String style)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_STYLE);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						userId,
						cType,
						"",
						"",
						CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE,
						"",
						linkType.getId(),
						true,
						true);
				linkType.addCharacteristic(ea);
				GeneralStorableObjectPool.putStorableObject(ea);
				GeneralStorableObjectPool.flush(true);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(style);
	}

	/**
	 * Получить стиль линии. Стиль определяется
	 * атрибутом {@link AbstractLinkController#ATTRIBUTE_STYLE}. В случае, если
	 * такого атрибута у элемента нет, берется значение по умолчанию
	 * ({@link MapPropertiesManager#getStyle()}).
	 * @param linkType тип линии
	 * @return стиль
	 */
	public String getStyle(Identifier userId, PhysicalLinkType linkType)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_STYLE);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
			return MapPropertiesManager.getStyle();
		return ea.getValue();
	}

	/**
	 * Получить стиль типа линии. Стиль определяется
	 * атрибутом {@link AbstractLinkController#ATTRIBUTE_STYLE}. В случае, если
	 * такого атрибута у элемента нет, берется значение по умолчанию
	 * ({@link MapPropertiesManager#getStroke()}).
	 * @param linkType тип линии
	 * @return стиль
	 */
	public Stroke getStroke(Identifier userId, PhysicalLinkType linkType)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_STYLE);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
			return MapPropertiesManager.getStroke();

		return LineComboBox.getStrokeByType(ea.getValue());
	}

	/**
	 * Установить цвет типа линии. Цает определяется
	 * атрибутом {@link AbstractLinkController#ATTRIBUTE_COLOR}. В случае, если
	 * такого атрибута у элемента нет, создается новый.
	 * @param linkType тип линии
	 * @param color цвет
	 */
	public void setColor(Identifier userId, PhysicalLinkType linkType, Color color)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_COLOR);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						userId,
						cType,
						"",
						"",
						CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE,
						"",
						linkType.getId(),
						true,
						true);
				linkType.addCharacteristic(ea);
				GeneralStorableObjectPool.putStorableObject(ea);
				GeneralStorableObjectPool.flush(true);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(color.getRGB()));
		colorsHolder.put(linkType, color);
	}

	/**
	 * Получить цвет типа линии. Цает определяется
	 * атрибутом {@link AbstractLinkController#ATTRIBUTE_COLOR}. В случае, если
	 * такого атрибута у элемента нет, берется значение по умолчанию
	 * ({@link MapPropertiesManager#getColor()}). При первом вызове метода
	 * для объекта <code>linkType</code> полученный цвет помещается в 
	 * хэш-таблицу {@link #colorsHolder} и при повторных вызовах берется из нее.
	 * @param linkType тип линии
	 * @return цвет
	 */
	public Color getColor(Identifier userId, PhysicalLinkType linkType)
	{
		Color color = (Color )colorsHolder.get(linkType);
		if(color == null)
		{
			CharacteristicType cType = getCharacteristicType(
					userId, 
					AbstractLinkController.ATTRIBUTE_COLOR);
			Characteristic ea = getCharacteristic(linkType, cType);

			if(ea == null)
				color = MapPropertiesManager.getColor();
			else
				color = new Color(Integer.parseInt(ea.getValue()));
			
			colorsHolder.put(linkType, color);
		}
		return color;
	}

	/**
	 * Установить цвет типа линии при наличии сигнала тревоги. Цает определяется
	 * атрибутом {@link AbstractLinkController#ATTRIBUTE_ALARMED_COLOR}. 
	 * В случае, если такого атрибута у элемента нет, создается новый.
	 * @param linkType тип линии
	 * @param color цвет
	 */
	public void setAlarmedColor(Identifier userId, PhysicalLinkType linkType, Color color)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_ALARMED_COLOR);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						userId,
						cType,
						"",
						"",
						CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE,
						"",
						linkType.getId(),
						true,
						true);
				linkType.addCharacteristic(ea);
				GeneralStorableObjectPool.putStorableObject(ea);
				GeneralStorableObjectPool.flush(true);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(color.getRGB()));
		alarmedColorsHolder.put(linkType, color);
	}

	/**
	 * Получить цвет для типа линии при наличии сигнала тревоги. Цает 
	 * определяется атрибутом 
	 * {@link AbstractLinkController#ATTRIBUTE_ALARMED_COLOR}. В случае, если
	 * такого атрибута у элемента нет, берется значение по умолчанию
	 * ({@link MapPropertiesManager#getAlarmedColor()}). При первом вызове метода
	 * для объекта <code>linkType</code> полученный цвет помещается в 
	 * хэш-таблицу {@link #alarmedColorsHolder} и при повторных вызовах 
	 * берется из нее.
	 * @param linkType тип линии
	 * @return цвет
	 */
	public Color getAlarmedColor(Identifier userId, PhysicalLinkType linkType)
	{
		Color color = (Color )alarmedColorsHolder.get(linkType);
		if(color == null)
		{
			CharacteristicType cType = getCharacteristicType(
					userId, 
					AbstractLinkController.ATTRIBUTE_ALARMED_COLOR);
			Characteristic ea = getCharacteristic(linkType, cType);
			if(ea == null)
				color = MapPropertiesManager.getAlarmedColor();
			color = new Color(Integer.parseInt(ea.getValue()));

			alarmedColorsHolder.put(linkType, color);
		}
		return color;
	}

	/**
	 * Установить толщину линии для типа линии при наличи сигнала тревоги. 
	 * Толщина определяется атрибутом 
	 * {@link AbstractLinkController#ATTRIBUTE_ALARMED_THICKNESS}. В случае, 
	 * если такого атрибута у элемента нет, создается новый.
	 * @param linkType тип линии
	 * @param size толщина линии
	 */
	public void setAlarmedLineSize(Identifier userId, PhysicalLinkType linkType, int size)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						userId,
						cType,
						"",
						"",
						CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE,
						"",
						linkType.getId(),
						true,
						true);
				linkType.addCharacteristic(ea);
				GeneralStorableObjectPool.putStorableObject(ea);
				GeneralStorableObjectPool.flush(true);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
				return;
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		ea.setValue(String.valueOf(size));
	}

	/**
	 * Получить толщину линии для типа линии при наличи сигнала тревоги. 
	 * Толщина определяется атрибутом 
	 * {@link AbstractLinkController#ATTRIBUTE_ALARMED_THICKNESS}. В случае, 
	 * если такого атрибута у элемента нет, берется значение по умолчанию
	 * ({@link MapPropertiesManager#getAlarmedThickness()}).
	 * @param linkType тип линии
	 * @return толщина линии
	 */
	public int getAlarmedLineSize(Identifier userId, PhysicalLinkType linkType)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic ea = getCharacteristic(linkType, cType);
		if(ea == null)
			return MapPropertiesManager.getAlarmedThickness();
		return Integer.parseInt(ea.getValue());
	}

	/**
	 * Получить тип линии по кодовому имени.
	 * @param codename кодовое имя
	 * @return тип линии
	 */
	public static PhysicalLinkType getPhysicalLinkType(
			String codename)
	{
		StorableObjectCondition pTypeCondition = new TypicalCondition(
				codename, 
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		try
		{
			Collection pTypes =
				MapStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
			for (Iterator it = pTypes.iterator(); it.hasNext();)
			{
				PhysicalLinkType type = (PhysicalLinkType )it.next();
				if (type.getCodename().equals(codename))
					return type;
			}
		}
		catch(ApplicationException ex)
		{
			System.err.println("Exception searching PhysicalLinkType. Creating new one.");
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Получить тип линии по кодовому имени. В случае, если такого типа нет,
	 * создается новый.
	 * @param userId пользователь
	 * @param codename кодовое имя
	 * @return тип линии
	 */
	private static PhysicalLinkType getPhysicalLinkType(
			Identifier userId,
			PhysicalLinkTypeSort sort,
			String codename)
	{
		PhysicalLinkType type = getPhysicalLinkType(codename);
		if(type == null)
		try
		{
			LinkTypeController ltc = (LinkTypeController )LinkTypeController.getInstance();

			type = PhysicalLinkType.createInstance(
				userId,
				sort,
				codename,
				LangModelMap.getString(codename),
				"",
				LinkTypeController.getBindDimension(codename));

			ltc.setLineSize(userId, type, LinkTypeController.getLineThickness(codename));
			ltc.setColor(userId, type, LinkTypeController.getLineColor(codename));

			MapStorableObjectPool.putStorableObject(type);
			MapStorableObjectPool.flush(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			type = null;
		}
		return type;
	}

	public static void createDefaults(Identifier creatorId)
	{
		// make sure PhysicalLinkType.TUNNEL is created
		LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkTypeSort.TUNNEL, PhysicalLinkType.DEFAULT_TUNNEL);
		// make sure PhysicalLinkType.COLLECTOR is created
		LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkTypeSort.COLLECTOR, PhysicalLinkType.DEFAULT_COLLECTOR);
		// make sure PhysicalLinkType.UNBOUND is created
		LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkTypeSort.UNBOUND, PhysicalLinkType.DEFAULT_UNBOUND);
	}

	/**
	 * Получить список всех типов линий.
	 * @return список типов линий &lt;{@link PhysicalLinkType}&gt;
	 */
	public static Collection getTopologicalLinkTypes() {
		StorableObjectCondition pTypeCondition = new EquivalentCondition(
				ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE);

		Collection list = null;
		try {
			list =
				MapStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);

			list.remove(getUnboundPhysicalLinkType());

			for(Iterator it = list.iterator(); it.hasNext();) {
				PhysicalLinkType type = (PhysicalLinkType )it.next();
				if(!type.isTopological())
					it.remove();
			}
		}
		catch(Exception e) {
			list = Collections.EMPTY_LIST;
		}
		
		return list;
	}
	
	/**
	 * Получить тип линии по умолчанию ({@link PhysicalLinkType#DEFAULT_TUNNEL}).
	 * @return тип линии
	 */
	public static PhysicalLinkType getDefaultPhysicalLinkType()
	{
		return LinkTypeController.getPhysicalLinkType(PhysicalLinkType.DEFAULT_TUNNEL);
	}

	/**
	 * Получить тип непривязанной линии ({@link PhysicalLinkType#DEFAULT_UNBOUND}).
	 * @return тип линии
	 */
	public static PhysicalLinkType getUnboundPhysicalLinkType()
	{
		return LinkTypeController.getPhysicalLinkType(PhysicalLinkType.DEFAULT_UNBOUND);
	}
}
