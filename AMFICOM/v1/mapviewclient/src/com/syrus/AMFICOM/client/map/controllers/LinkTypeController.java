/**
 * $Id: LinkTypeController.java,v 1.4 2005/01/20 14:37:52 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.LineComboBox;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.StringFieldCondition;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.AMFICOM.map.IntDimension;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.PhysicalLinkType;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import java.util.HashMap;
import java.util.Iterator;
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.Client.Map.Controllers.AbstractLinkController;
import java.util.LinkedList;
import java.util.List;

/**
 * линейный элемента карты 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2005/01/20 14:37:52 $
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

	public String getToolTipText(MapElement me)
	{
		throw new UnsupportedOperationException();
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

	private static java.util.Map lineColors = new HashMap();
	
	static
	{
		lineColors.put(PhysicalLinkType.COLLECTOR, Color.DARK_GRAY);
		lineColors.put(PhysicalLinkType.TUNNEL, Color.BLACK);
		lineColors.put(PhysicalLinkType.UNBOUND, Color.RED);
	}

	public static Color getLineColor(String codename)
	{
		return (Color )lineColors.get(codename);
	}

	private static java.util.Map lineThickness = new HashMap();
	
	static
	{
		lineThickness.put(PhysicalLinkType.COLLECTOR, new Integer(3));
		lineThickness.put(PhysicalLinkType.TUNNEL, new Integer(2));
		lineThickness.put(PhysicalLinkType.UNBOUND, new Integer(1));
	}

	public static int getLineThickness(String codename)
	{
		return ((Integer )lineThickness.get(codename)).intValue();
	}
	

	private static java.util.Map bindDimensions = new HashMap();
	
	static
	{
		bindDimensions.put(PhysicalLinkType.COLLECTOR, new IntDimension(2, 6));
		bindDimensions.put(PhysicalLinkType.TUNNEL, new IntDimension(3, 4));
		bindDimensions.put(PhysicalLinkType.UNBOUND, new IntDimension(0, 0));
	}

	public static IntDimension getBindDimension(String codename)
	{
		return (IntDimension )bindDimensions.get(codename);
	}
	
	public static Characteristic getCharacteristic(
			PhysicalLinkType me, 
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
	public void setLineSize (PhysicalLinkType link, int size)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_THICKNESS);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						link.getCreatorId(),
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
	public int getLineSize (PhysicalLinkType link)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_THICKNESS);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getThickness();
		return Integer.parseInt(ea.getValue());
	}

	/**
	 * Установить вид линии
	 */
	public void setStyle (PhysicalLinkType link, String style)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_STYLE);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						link.getCreatorId(),
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
	public String getStyle (PhysicalLinkType link)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_STYLE);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getStyle();
		return ea.getValue();
	}

	/**
	 * Получить стиль линии
	 */
	public Stroke getStroke (PhysicalLinkType link)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_STYLE);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getStroke();

		return LineComboBox.getStrokeByType(ea.getValue());
	}

	/**
	 * Установить цвет
	 */
	public void setColor (PhysicalLinkType link, Color color)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_COLOR);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						link.getCreatorId(),
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
	public Color getColor(PhysicalLinkType link)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_COLOR);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getColor();
		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * установить цвет при наличии сигнала тревоги
	 */
	public void setAlarmedColor (PhysicalLinkType link, Color color)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_ALARMED_COLOR);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						link.getCreatorId(),
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
	public Color getAlarmedColor(PhysicalLinkType link)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_ALARMED_COLOR);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getAlarmedColor();
		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * установить толщину линии при наличи сигнала тревоги
	 */
	public void setAlarmedLineSize (PhysicalLinkType link, int size)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						link.getCreatorId(),
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
	public int getAlarmedLineSize (PhysicalLinkType link)
	{
		CharacteristicType cType = getCharacteristicType(link.getCreatorId(), ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic ea = (Characteristic )getCharacteristic(link, cType);
		if(ea == null)
			return MapPropertiesManager.getAlarmedThickness();
		return Integer.parseInt(ea.getValue());
	}

	public static PhysicalLinkType getPhysicalLinkType(
			Identifier userId,
			String codename)
	{
		StorableObjectCondition pTypeCondition = new StringFieldCondition(
			codename,
			ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE,
			StringFieldSort.STRINGSORT_BASE);

		try
		{
			List pTypes =
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
			System.err.println("Exception searching ParameterType. Creating new one.");
			ex.printStackTrace();
		}

		try
		{
			LinkTypeController ltc = (LinkTypeController )LinkTypeController.getInstance();

			PhysicalLinkType pType = PhysicalLinkType.createInstance(
				userId,
				codename,
				LangModelMap.getString(codename),
				"",
				LinkTypeController.getBindDimension(codename));

			ltc.setLineSize(pType, LinkTypeController.getLineThickness(codename));
			ltc.setColor(pType, LinkTypeController.getLineColor(codename));

			MapStorableObjectPool.putStorableObject(pType);
			return pType;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static List getPens(ApplicationContext aContext)
	{
		Identifier creatorId = new Identifier(
			aContext.getSessionInterface().getAccessIdentifier().user_id);

		PhysicalLinkType mlpe = null;

		mlpe = LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkType.TUNNEL);
		mlpe = LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkType.COLLECTOR);

		StorableObjectCondition pTypeCondition = new StringFieldCondition(
			"",
			ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE,
			StringFieldSort.STRINGSORT_BASE);

		List list = null;
		try
		{
			list =
				MapStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);

			list.remove(getDefaultUnboundPen(creatorId));
		}
		catch(Exception e)
		{
			list = new LinkedList();
		}
		
		return list;
	}
	
	public static PhysicalLinkType getDefaultPen(Identifier creatorId)
	{
		return LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkType.TUNNEL);
	}

	public static PhysicalLinkType getDefaultUnboundPen(Identifier creatorId)
	{
		return LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkType.UNBOUND);
	}
}
