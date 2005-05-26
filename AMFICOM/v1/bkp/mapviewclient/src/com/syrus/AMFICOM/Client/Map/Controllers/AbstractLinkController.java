/**
 * $Id: AbstractLinkController.java,v 1.16 2005/05/26 14:04:50 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import java.awt.Color;
import java.awt.Stroke;
import java.util.Collection;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.UI.LineComboBox;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.map.MapElement;

/**
 * ���������� ��������� �������� �����.
 * @author $Author: bass $
 * @version $Revision: 1.16 $, $Date: 2005/05/26 14:04:50 $
 * @module mapviewclient_v1
 */
public abstract class AbstractLinkController extends AbstractMapElementController
{
	/** ������� ��� �������� "������� �����". */
	public static final String ATTRIBUTE_THICKNESS = "thickness";
	/** ������� ��� �������� "����". */
	public static final String ATTRIBUTE_COLOR = "color";
	/** ������� ��� �������� "����� �����". */
	public static final String ATTRIBUTE_STYLE = "style";
	/** ������� ��� �������� "������� ����� ��� ������� �������". */
	public static final String ATTRIBUTE_ALARMED_THICKNESS = "alarmed_thckness";
	/** ������� ��� �������� "���� ��� ������� �������". */
	public static final String ATTRIBUTE_ALARMED_COLOR = "alarmed_color";

	/**
	 * ����������, ����� �� ����� ��������� �������� �����.
	 * @param mapElement ������� �����
	 * @return ���� ��������� ����� ���������
	 */
	public abstract boolean isSelectionVisible(MapElement mapElement);

	/**
	 * ����� ��� �������� �� �������� �����. ���� ������ ����
	 * �� �������, ������� �����.
	 * @param userId ������������
	 * @param codename ������� ���
	 * @return ��� ��������
	 */
	public static CharacteristicType getCharacteristicType(
			Identifier userId, 
			String codename)
	{
		CharacteristicTypeSort sort = CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL;
		DataType dataType = DataType.DATA_TYPE_STRING;

		StorableObjectCondition pTypeCondition = new TypicalCondition(
				codename, 
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		try
		{
			Collection pTypes =
				StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
			if(!pTypes.isEmpty())
			{
				CharacteristicType type = (CharacteristicType )pTypes.iterator().next();
				return type;
			}
		}
		catch(ApplicationException ex)
		{
			//TODO empty
			System.err.println("Exception searching CharacteristicType. Creating new one.");
			ex.printStackTrace();
		}

		try
		{
			CharacteristicType type = CharacteristicType.createInstance(
					userId,
					codename,
					"",
					dataType,
					sort);
			StorableObjectPool.putStorableObject(type);
			StorableObjectPool.flushGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, true);
			return type;
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ����� ������� �������� ����� �� ����.
	 * @param mapElement ������� �����
	 * @param cType ��� ��������
	 * @return �������
	 */
	public static Characteristic getCharacteristic(
			MapElement mapElement, 
			CharacteristicType cType)
	{
		for(Iterator it = mapElement.getCharacteristics().iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic )it.next();
			if(ch.getType().equals(cType))
			{
				return ch;
			}
		}
		return null;
	}

	/**
	 * ���������� ������� �����. ������� ������������
	 * ��������� {@link #ATTRIBUTE_THICKNESS}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param mapElement ������� �����
	 * @param size ������� �����
	 */
	public void setLineSize (MapElement mapElement, int size)
	{
		CharacteristicType cType = getCharacteristicType(
				getLogicalNetLayer().getUserId(), 
				AbstractLinkController.ATTRIBUTE_THICKNESS);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						getLogicalNetLayer().getUserId(),
						cType,
						"",
						"",
						"",
						mapElement,
						true,
						true);
				mapElement.addCharacteristic(ea);
				StorableObjectPool.putStorableObject(ea);
				StorableObjectPool.flushGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, true);
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
	 * �������� ������� �����. ������� ������������
	 * ��������� {@link #ATTRIBUTE_THICKNESS}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getThickness()}).
	 * @param mapElement ������� �����
	 * @return ������� �����
	 */
	public int getLineSize (MapElement mapElement)
	{
		CharacteristicType cType = getCharacteristicType(
				getLogicalNetLayer().getUserId(), 
				AbstractLinkController.ATTRIBUTE_THICKNESS);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null)
		{
			return MapPropertiesManager.getThickness();
		}
		return Integer.parseInt(ea.getValue());
	}

	/**
	 * ���������� ��� �����. ����� ������������
	 * ��������� {@link #ATTRIBUTE_STYLE}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param mapElement ������� �����
	 * @param style �����
	 */
	public void setStyle (MapElement mapElement, String style)
	{
		CharacteristicType cType = getCharacteristicType(
				getLogicalNetLayer().getUserId(), 
				AbstractLinkController.ATTRIBUTE_STYLE);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						getLogicalNetLayer().getUserId(),
						cType,
						"",
						"",
						"",
						mapElement,
						true,
						true);
				mapElement.addCharacteristic(ea);
				StorableObjectPool.putStorableObject(ea);
				StorableObjectPool.flushGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, true);
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
	 * �������� ��� �����. ����� ������������
	 * ��������� {@link #ATTRIBUTE_STYLE}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getStyle()}).
	 * @param mapElement ������� �����
	 * @return �����
	 */
	public String getStyle (MapElement mapElement)
	{
		CharacteristicType cType = getCharacteristicType(
				getLogicalNetLayer().getUserId(), 
				AbstractLinkController.ATTRIBUTE_STYLE);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null)
		{
			return MapPropertiesManager.getStyle();
		}
		return ea.getValue();
	}

	/**
	 * �������� ����� �����. ����� ������������
	 * ��������� {@link #ATTRIBUTE_STYLE}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getStroke()}).
	 * @param mapElement ������� �����
	 * @return �����
	 */
	public Stroke getStroke (MapElement mapElement)
	{
		CharacteristicType cType = getCharacteristicType(
				getLogicalNetLayer().getUserId(), 
				AbstractLinkController.ATTRIBUTE_STYLE);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null)
		{
			return MapPropertiesManager.getStroke();
		}
		return LineComboBox.getStrokeByType(ea.getValue());
	}

	/**
	 * ���������� ����. ���� ������������
	 * ��������� {@link #ATTRIBUTE_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param mapElement ������� �����
	 * @param color ����
	 */
	public void setColor (MapElement mapElement, Color color)
	{
		CharacteristicType cType = getCharacteristicType(
				getLogicalNetLayer().getUserId(), 
				AbstractLinkController.ATTRIBUTE_COLOR);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						getLogicalNetLayer().getUserId(),
						cType,
						"",
						"",
						"",
						mapElement,
						true,
						true);
				mapElement.addCharacteristic(ea);
				StorableObjectPool.putStorableObject(ea);
				StorableObjectPool.flushGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, true);
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
	}

	/**
	 * �������� ����. ���� ������������
	 * ��������� {@link #ATTRIBUTE_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getColor()}).
	 * @param mapElement ������� �����
	 * @return ����
	 */
	public Color getColor(MapElement mapElement)
	{
		CharacteristicType cType = getCharacteristicType(
				getLogicalNetLayer().getUserId(), 
				AbstractLinkController.ATTRIBUTE_COLOR);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null)
		{
			return MapPropertiesManager.getColor();
		}
		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * ���������� ���� ��� ������� ������� �������. ���� ������������
	 * ��������� {@link #ATTRIBUTE_ALARMED_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param mapElement ������� �����
	 * @param color ����
	 */
	public void setAlarmedColor (MapElement mapElement, Color color)
	{
		CharacteristicType cType = getCharacteristicType(
				getLogicalNetLayer().getUserId(), 
				AbstractLinkController.ATTRIBUTE_ALARMED_COLOR);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						getLogicalNetLayer().getUserId(),
						cType,
						"",
						"",
						"",
						mapElement,
						true,
						true);
				mapElement.addCharacteristic(ea);
				StorableObjectPool.putStorableObject(ea);
				StorableObjectPool.flushGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, true);
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
	}

	/**
	 * �������� ���� ��� ������� ������� �������. ���� ������������
	 * ��������� {@link #ATTRIBUTE_ALARMED_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getAlarmedColor()}).
	 * @param mapElement ������� �����
	 * @return ����
	 */
	public Color getAlarmedColor(MapElement mapElement)
	{
		CharacteristicType cType = getCharacteristicType(
				getLogicalNetLayer().getUserId(), 
				AbstractLinkController.ATTRIBUTE_ALARMED_COLOR);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null)
		{
			return MapPropertiesManager.getAlarmedColor();
		}
		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * ���������� ������� ����� ��� ������ ������� �������. ������� ������������
	 * ��������� {@link #ATTRIBUTE_ALARMED_THICKNESS}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param mapElement ������� �����
	 * @param size ������� �����
	 */
	public void setAlarmedLineSize (MapElement mapElement, int size)
	{
		CharacteristicType cType = getCharacteristicType(
				getLogicalNetLayer().getUserId(), 
				AbstractLinkController.ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null)
		{
			try
			{
				ea = Characteristic.createInstance(
						getLogicalNetLayer().getUserId(),
						cType,
						"",
						"",
						"",
						mapElement,
						true,
						true);
				mapElement.addCharacteristic(ea);
				StorableObjectPool.putStorableObject(ea);
				StorableObjectPool.flushGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, true);
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
	 * �������� ������� ����� ��� ������ ������� �������. ������� ������������
	 * ��������� {@link #ATTRIBUTE_ALARMED_THICKNESS}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getAlarmedThickness()}).
	 * @param mapElement ������� �����
	 * @return ������� �����
	 */
	public int getAlarmedLineSize(MapElement mapElement)
	{
		CharacteristicType cType = getCharacteristicType(
				getLogicalNetLayer().getUserId(), 
				AbstractLinkController.ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null)
		{
			return MapPropertiesManager.getAlarmedThickness();
		}
		return Integer.parseInt(ea.getValue());
	}
}
