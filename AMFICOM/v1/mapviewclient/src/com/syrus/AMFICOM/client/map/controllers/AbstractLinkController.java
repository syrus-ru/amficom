/**
 * $Id: AbstractLinkController.java,v 1.8 2005/02/08 15:11:10 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.General.UI.LineComboBox;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeWrapper;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StringFieldCondition;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.Color;
import java.awt.Stroke;

import java.util.Iterator;
import java.util.List;

/**
 * ���������� ��������� �������� �����.
 * @author $Author: krupenn $
 * @version $Revision: 1.8 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
 */
public abstract class AbstractLinkController implements MapElementController
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
	 * ���������� ����.
	 */
	protected LogicalNetLayer logicalNetLayer;

	/**
	 * {@inheritDoc}
	 */
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public LogicalNetLayer getLogicalNetLayer()
	{
		return this.logicalNetLayer;
	}

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

//		StorableObjectCondition pTypeCondition = new TypicalCondition(
//				codename, 
//				OperationSort.OPERATION_EQUALS,
//				ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE,
//				CharacteristicTypeWrapper.COLUMN_CODENAME);
		StorableObjectCondition pTypeCondition = new StringFieldCondition(
			codename,
			ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE,
			StringFieldSort.STRINGSORT_BASE);

		try
		{
			List pTypes =
				GeneralStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
			for (Iterator it = pTypes.iterator(); it.hasNext();)
			{
				CharacteristicType type = (CharacteristicType )it.next();
				if (type.getCodename().equals(codename))
				{
					return type;
				}
			}
		}
		catch(ApplicationException ex)
		{
			System.err.println("Exception searching CharacteristicType. Creating new one.");
			ex.printStackTrace();
		}

		try
		{
			CharacteristicType type = CharacteristicType.createInstance(
					userId,
					codename,
					"",
					dataType.value(),
					sort);
			GeneralStorableObjectPool.putStorableObject(type);
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
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						mapElement.getId(),
						true,
						true);
				mapElement.addCharacteristic(ea);
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
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						mapElement.getId(),
						true,
						true);
				mapElement.addCharacteristic(ea);
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
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						mapElement.getId(),
						true,
						true);
				mapElement.addCharacteristic(ea);
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
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						mapElement.getId(),
						true,
						true);
				mapElement.addCharacteristic(ea);
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
						CharacteristicTypeSort._CHARACTERISTICTYPESORT_VISUAL,
						"",
						mapElement.getId(),
						true,
						true);
				mapElement.addCharacteristic(ea);
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
