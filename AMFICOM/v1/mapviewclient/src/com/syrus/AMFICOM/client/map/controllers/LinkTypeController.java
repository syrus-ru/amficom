/**
 * $Id: LinkTypeController.java,v 1.32 2005/06/23 08:28:21 krupenn Exp $
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.ui.LineComboBox;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.IntDimension;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.PhysicalLinkTypeSort;

/**
 * ���������� ���� ��������� �������� �����.
 * @author $Author: krupenn $
 * @version $Revision: 1.32 $, $Date: 2005/06/23 08:28:21 $
 * @module mapviewclient_v1
 */
public final class LinkTypeController extends AbstractLinkController {
	/**
	 * ���-������� ������ ����� �����. ��� ����, ����� ������ {@link Color} 
	 * �� ���������� ������ ��� �� �������� ��� ������ 
	 * {@link #getColor(Identifier, PhysicalLinkType)}, ��������� ������ ���������� �
	 * ���-������� � ��� ����������� ���������� ������������ ��������.
	 */
	private static java.util.Map colorsHolder = new HashMap();
	/**
	 * ���-������� ������ ������� ������� ��� ����� �����. ��� ����, ����� 
	 * ������ {@link Color} �� ���������� ������ ��� �� �������� ��� ������ 
	 * {@link #getAlarmedColor(Identifier, PhysicalLinkType)}, ��������� ������ ���������� �
	 * ���-������� � ��� ����������� ���������� ������������ ��������.
	 */
	private static java.util.Map alarmedColorsHolder = new HashMap();

	/** ���-������� ������ ��� ����������������� ����� �����. */
	private static java.util.Map lineColors = new HashMap();
	/** ���-������� ������� ����� ��� ����������������� ����� �����. */
	private static java.util.Map lineThickness = new HashMap();
	/** ���-������� ����������� �������� ��� ����������������� ����� �����. */
	private static java.util.Map bindDimensions = new HashMap();

	/**
	 * Instance
	 */
	private static LinkTypeController instance = null;
	
	static {
		lineColors.put(PhysicalLinkType.DEFAULT_COLLECTOR, Color.DARK_GRAY);
		lineColors.put(PhysicalLinkType.DEFAULT_TUNNEL, Color.BLACK);
		lineColors.put(PhysicalLinkType.DEFAULT_INDOOR, Color.GREEN);
		lineColors.put(PhysicalLinkType.DEFAULT_OVERHEAD, Color.BLUE);
		lineColors.put(PhysicalLinkType.DEFAULT_SUBMARINE, Color.MAGENTA);
		lineColors.put(PhysicalLinkType.DEFAULT_UNBOUND, Color.RED);

		lineThickness.put(PhysicalLinkType.DEFAULT_COLLECTOR, new Integer(4));
		lineThickness.put(PhysicalLinkType.DEFAULT_TUNNEL, new Integer(2));
		lineThickness.put(PhysicalLinkType.DEFAULT_INDOOR, new Integer(1));
		lineThickness.put(PhysicalLinkType.DEFAULT_OVERHEAD, new Integer(2));
		lineThickness.put(PhysicalLinkType.DEFAULT_SUBMARINE, new Integer(3));
		lineThickness.put(PhysicalLinkType.DEFAULT_UNBOUND, new Integer(1));

		bindDimensions.put(PhysicalLinkType.DEFAULT_COLLECTOR, new IntDimension(2, 6));
		bindDimensions.put(PhysicalLinkType.DEFAULT_TUNNEL, new IntDimension(3, 4));
		bindDimensions.put(PhysicalLinkType.DEFAULT_INDOOR, new IntDimension(1, 1));
		bindDimensions.put(PhysicalLinkType.DEFAULT_OVERHEAD, new IntDimension(10, 1));
		bindDimensions.put(PhysicalLinkType.DEFAULT_SUBMARINE, new IntDimension(3, 4));
		bindDimensions.put(PhysicalLinkType.DEFAULT_UNBOUND, new IntDimension(0, 0));
	}

	/**
	 * Private constructor.
	 */
	private LinkTypeController() {
		super(null);
	}

	/**
	 * Get instance.
	 * 
	 * @return instance
	 */
	public static MapElementController getInstance() {
		if(instance == null)
			instance = new LinkTypeController();
		return instance;
	}

	/**
	 * {@inheritDoc} Suppress since PhysicalLinkType is not really a Map Element
	 */
	public String getToolTipText(MapElement mapElement) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since PhysicalLinkType is not really a Map Element
	 */
	public boolean isSelectionVisible(MapElement mapElement) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since PhysicalLinkType is not really a Map Element
	 */
	public boolean isElementVisible(
			MapElement mapElement,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since PhysicalLinkType is not really a Map Element
	 */
	public void paint(
			MapElement mapElement,
			Graphics g,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since PhysicalLinkType is not really a Map Element
	 */
	public boolean isMouseOnElement(
			MapElement mapElement,
			Point currentMousePoint)
			throws MapConnectionException, MapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * �������� ���� �� �������� ����� ��� ������������������ ���� �����.
	 * 
	 * @param codename ������� ���
	 * @return ����
	 */
	public static Color getLineColor(String codename) {
		return (Color )lineColors.get(codename);
	}

	/**
	 * �������� ������� ����� �� �������� ����� ��� ������������������ ����
	 * �����.
	 * 
	 * @param codename ������� ���
	 * @return �������
	 */
	public static int getLineThickness(String codename) {
		return ((Integer )lineThickness.get(codename)).intValue();
	}

	/**
	 * �������� ����������� �������� �� �������� ����� ��� ������������������
	 * ���� �����.
	 * 
	 * @param codename ������� ���
	 * @return ����������� ��������
	 */
	public static IntDimension getBindDimension(String codename) {
		return (IntDimension )bindDimensions.get(codename);
	}

	/**
	 * ����� ������� ���� ����� �� ����.
	 * 
	 * @param linkType ��� �����
	 * @param cType ��� ��������
	 * @return �������
	 */
	public static Characteristic getCharacteristic(
			PhysicalLinkType linkType,
			CharacteristicType cType) {
		for(Iterator it = linkType.getCharacteristics().iterator(); it.hasNext();) {
			Characteristic ch = (Characteristic )it.next();
			if(ch.getType().equals(cType))
				return ch;
		}
		return null;
	}

	/**
	 * ���������� ������� �����. ������� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_THICKNESS}. 
	 * � ������, ���� ������ �������� � �������� ���, ��������� �����.
	 * @param linkType ��� �����
	 * @param size ������� �����
	 */
	public void setLineSize(Identifier userId, PhysicalLinkType linkType, int size)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_THICKNESS);
		Characteristic attribute = getCharacteristic(linkType, cType);
		if(attribute == null)
		{
			try
			{
				attribute = Characteristic.createInstance(
						userId,
						cType,
						"name",
						"1",
						String.valueOf(size),
						linkType,
						true,
						true);
				linkType.addCharacteristic(attribute);
				StorableObjectPool.putStorableObject(attribute);
				StorableObjectPool.flush(attribute.getId(), true);
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
		attribute.setValue(String.valueOf(size));
	}

	/**
	 * �������� ������� �����. ������� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_THICKNESS}. 
	 * � ������, ���� ������ �������� � �������� ���, ������� �������� 
	 * �� ��������� ({@link MapPropertiesManager#getThickness()}).
	 * @param linkType ��� �����
	 * @return ������� �����
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
	 * ���������� ����� ���� �����. ����� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_STYLE}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param linkType ��� �����
	 * @param style �����
	 */
	public void setStyle(Identifier userId, PhysicalLinkType linkType, String style)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_STYLE);
		Characteristic attribute = getCharacteristic(linkType, cType);
		if(attribute == null)
		{
			try
			{
				attribute = Characteristic.createInstance(
						userId,
						cType,
						"name",
						"1",
						style,
						linkType,
						true,
						true);
				linkType.addCharacteristic(attribute);
				StorableObjectPool.putStorableObject(attribute);
				StorableObjectPool.flush(attribute.getId(), true);
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
		attribute.setValue(style);
	}

	/**
	 * �������� ����� �����. ����� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_STYLE}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getStyle()}).
	 * @param linkType ��� �����
	 * @return �����
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
	 * �������� ����� ���� �����. ����� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_STYLE}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getStroke()}).
	 * @param linkType ��� �����
	 * @return �����
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
	 * ���������� ���� ���� �����. ���� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param linkType ��� �����
	 * @param color ����
	 */
	public void setColor(Identifier userId, PhysicalLinkType linkType, Color color)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_COLOR);
		Characteristic attribute = getCharacteristic(linkType, cType);
		if(attribute == null)
		{
			try
			{
				attribute = Characteristic.createInstance(
						userId,
						cType,
						"name",
						"1",
						String.valueOf(color.getRGB()),
						linkType,
						true,
						true);
				linkType.addCharacteristic(attribute);
				StorableObjectPool.putStorableObject(attribute);
				StorableObjectPool.flush(attribute.getId(), true);
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
		attribute.setValue(String.valueOf(color.getRGB()));
		colorsHolder.put(linkType, color);
	}

	/**
	 * �������� ���� ���� �����. ���� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getColor()}). ��� ������ ������ ������
	 * ��� ������� <code>linkType</code> ���������� ���� ���������� � 
	 * ���-������� {@link #colorsHolder} � ��� ��������� ������� ������� �� ���.
	 * @param linkType ��� �����
	 * @return ����
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
	 * ���������� ���� ���� ����� ��� ������� ������� �������. ���� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_ALARMED_COLOR}. 
	 * � ������, ���� ������ �������� � �������� ���, ��������� �����.
	 * @param linkType ��� �����
	 * @param color ����
	 */
	public void setAlarmedColor(Identifier userId, PhysicalLinkType linkType, Color color)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_ALARMED_COLOR);
		Characteristic attribute = getCharacteristic(linkType, cType);
		if(attribute == null)
		{
			try
			{
				attribute = Characteristic.createInstance(
						userId,
						cType,
						"name",
						"1",
						String.valueOf(color.getRGB()),
						linkType,
						true,
						true);
				linkType.addCharacteristic(attribute);
				StorableObjectPool.putStorableObject(attribute);
				StorableObjectPool.flush(attribute.getId(), true);
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
		attribute.setValue(String.valueOf(color.getRGB()));
		alarmedColorsHolder.put(linkType, color);
	}

	/**
	 * �������� ���� ��� ���� ����� ��� ������� ������� �������. ���� 
	 * ������������ ��������� 
	 * {@link AbstractLinkController#ATTRIBUTE_ALARMED_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getAlarmedColor()}). ��� ������ ������ ������
	 * ��� ������� <code>linkType</code> ���������� ���� ���������� � 
	 * ���-������� {@link #alarmedColorsHolder} � ��� ��������� ������� 
	 * ������� �� ���.
	 * @param linkType ��� �����
	 * @return ����
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
	 * ���������� ������� ����� ��� ���� ����� ��� ������ ������� �������. 
	 * ������� ������������ ��������� 
	 * {@link AbstractLinkController#ATTRIBUTE_ALARMED_THICKNESS}. � ������, 
	 * ���� ������ �������� � �������� ���, ��������� �����.
	 * @param linkType ��� �����
	 * @param size ������� �����
	 */
	public void setAlarmedLineSize(Identifier userId, PhysicalLinkType linkType, int size)
	{
		CharacteristicType cType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic attribute = getCharacteristic(linkType, cType);
		if(attribute == null)
		{
			try
			{
				attribute = Characteristic.createInstance(
						userId,
						cType,
						"name",
						"1",
						String.valueOf(size),
						linkType,
						true,
						true);
				linkType.addCharacteristic(attribute);
				StorableObjectPool.putStorableObject(attribute);
				StorableObjectPool.flush(attribute.getId(), true);
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
		attribute.setValue(String.valueOf(size));
	}

	/**
	 * �������� ������� ����� ��� ���� ����� ��� ������ ������� �������. 
	 * ������� ������������ ��������� 
	 * {@link AbstractLinkController#ATTRIBUTE_ALARMED_THICKNESS}. � ������, 
	 * ���� ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getAlarmedThickness()}).
	 * @param linkType ��� �����
	 * @return ������� �����
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
	 * �������� ��� ����� �� �������� �����.
	 * @param codename ������� ���
	 * @return ��� �����
	 */
	public static PhysicalLinkType getPhysicalLinkType(
			String codename)
	{
		StorableObjectCondition pTypeCondition = new TypicalCondition(
				codename, 
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PHYSICALLINK_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		try
		{
			Collection pTypes =
				StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
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
	 * �������� ��� ����� �� �������� �����. � ������, ���� ������ ���� ���,
	 * ��������� �����.
	 * @param userId ������������
	 * @param codename ������� ���
	 * @return ��� �����
	 * @throws CreateObjectException 
	 */
	private static PhysicalLinkType getPhysicalLinkType(
			Identifier userId,
			PhysicalLinkTypeSort sort,
			String codename) throws ApplicationException
	{
		PhysicalLinkType type = getPhysicalLinkType(codename);
		if(type == null) {
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

			StorableObjectPool.putStorableObject(type);
			StorableObjectPool.flush(type.getId(), true);
		}
		return type;
	}

	public static void createDefaults(Identifier creatorId) throws ApplicationException
	{
		// make sure PhysicalLinkType.TUNNEL is created
		LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkTypeSort.TUNNEL, PhysicalLinkType.DEFAULT_TUNNEL);
		// make sure PhysicalLinkType.COLLECTOR is created
		LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkTypeSort.COLLECTOR, PhysicalLinkType.DEFAULT_COLLECTOR);
		// make sure PhysicalLinkType.UNBOUND is created
		LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkTypeSort.UNBOUND, PhysicalLinkType.DEFAULT_UNBOUND);
	}

	/**
	 * �������� ������ ���� ����� �����.
	 * @return ������ ����� ����� &lt;{@link PhysicalLinkType}&gt;
	 */
	public static Collection getTopologicalLinkTypes() {
		StorableObjectCondition pTypeCondition = new EquivalentCondition(
				ObjectEntities.PHYSICALLINK_TYPE_CODE);

		Collection list = null;
		try {
			list =
				StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);

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
	 * �������� ��� ����� �� ��������� ({@link PhysicalLinkType#DEFAULT_TUNNEL}).
	 * @return ��� �����
	 */
	public static PhysicalLinkType getDefaultPhysicalLinkType()
	{
		return LinkTypeController.getPhysicalLinkType(PhysicalLinkType.DEFAULT_TUNNEL);
	}

	/**
	 * �������� ��� ������������� ����� ({@link PhysicalLinkType#DEFAULT_UNBOUND}).
	 * @return ��� �����
	 */
	public static PhysicalLinkType getUnboundPhysicalLinkType()
	{
		return LinkTypeController.getPhysicalLinkType(PhysicalLinkType.DEFAULT_UNBOUND);
	}
}
