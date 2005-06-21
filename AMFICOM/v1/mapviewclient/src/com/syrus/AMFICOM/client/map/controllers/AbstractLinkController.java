/**
 * $Id: AbstractLinkController.java,v 1.25 2005/06/21 12:52:14 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Color;
import java.awt.Stroke;
import java.util.Collection;
import java.util.Iterator;

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.ui.LineComboBox;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.MapElement;

/**
 * Контроллер линейного элемента карты.
 * @author $Author: bass $
 * @version $Revision: 1.25 $, $Date: 2005/06/21 12:52:14 $
 * @module mapviewclient_v1
 */
public abstract class AbstractLinkController extends AbstractMapElementController
{
	/** Кодовое имя атрибута "Толщина линии". */
	public static final String ATTRIBUTE_THICKNESS = "thickness";
	/** Кодовое имя атрибута "Цвет". */
	public static final String ATTRIBUTE_COLOR = "color";
	/** Кодовое имя атрибута "Стиль линии". */
	public static final String ATTRIBUTE_STYLE = "style";
	/** Кодовое имя атрибута "Толщина линии при сигнале тревоги". */
	public static final String ATTRIBUTE_ALARMED_THICKNESS = "alarmed_thickness";
	/** Кодовое имя атрибута "Цвет при сигнале тревоги". */
	public static final String ATTRIBUTE_ALARMED_COLOR = "alarmed_color";

	public AbstractLinkController(NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	/**
	 * Определить, видна ли рамка выделения элемента карты.
	 * @param mapElement элемент карты
	 * @return флаг видимости рамки выделения
	 */
	public abstract boolean isSelectionVisible(MapElement mapElement);

	/**
	 * Найти тип атрибута по кодовому имени. Если такого типа
	 * не найдено, создать новый.
	 * @param userId пользователь
	 * @param codename кодовое имя
	 * @return тип атрибута
	 */
	public static CharacteristicType getCharacteristicType(
			Identifier userId,
			String codename) {
		CharacteristicTypeSort sort = CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL;
		DataType dataType = DataType.DATA_TYPE_STRING;

		StorableObjectCondition pTypeCondition = new TypicalCondition(
				codename,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.CHARACTERISTIC_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		try {
			Collection pTypes = StorableObjectPool
					.getStorableObjectsByCondition(pTypeCondition, true);
			if(!pTypes.isEmpty()) {
				CharacteristicType type = (CharacteristicType )pTypes.iterator().next();
				return type;
			}
		} catch(ApplicationException ex) {
			//TODO empty
			System.err.println("Exception searching CharacteristicType. Creating new one.");
			ex.printStackTrace();
		}

		try {
			CharacteristicType type = CharacteristicType.createInstance(
					userId,
					codename,
					"no description",
					LangModelMap.getString(codename),
					dataType,
					sort);
			StorableObjectPool.putStorableObject(type);
			StorableObjectPool.flush(type.getId(), true);
			return type;
		} catch(ApplicationException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Найти атрибут элемента карты по типу.
	 * @param mapElement элемент карты
	 * @param cType тип атрибута
	 * @return атрибут
	 */
	public static Characteristic getCharacteristic(
			MapElement mapElement,
			CharacteristicType cType) {
		for(Iterator it = mapElement.getCharacteristics().iterator(); it.hasNext();) {
			Characteristic ch = (Characteristic )it.next();
			if(ch.getType().equals(cType)) {
				return ch;
			}
		}
		return null;
	}

	/**
	 * Установить толщину линии. Толщина определяется
	 * атрибутом {@link #ATTRIBUTE_THICKNESS}. В случае, если
	 * такого атрибута у элемента нет, создается новый.
	 * @param mapElement элемент карты
	 * @param size толщина линии
	 */
	public void setLineSize(MapElement mapElement, int size) {
		CharacteristicType cType = getCharacteristicType(
				LoginManager.getUserId(), 
				AbstractLinkController.ATTRIBUTE_THICKNESS);
		Characteristic attribute = getCharacteristic(mapElement, cType);
		if(attribute == null) {
			try {
				attribute = Characteristic.createInstance(
						LoginManager.getUserId(),
						cType,
						"name",
						"1",
						String.valueOf(size),
						mapElement,
						true,
						true);
				mapElement.addCharacteristic(attribute);
				StorableObjectPool.putStorableObject(attribute);
				StorableObjectPool.flush(attribute.getId(), true);
			} catch(CreateObjectException e) {
				e.printStackTrace();
				return;
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch(ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		attribute.setValue(String.valueOf(size));
	}

	/**
	 * Получить толщину линии. Толщина определяется
	 * атрибутом {@link #ATTRIBUTE_THICKNESS}. В случае, если
	 * такого атрибута у элемента нет, берется значение по умолчанию
	 * ({@link MapPropertiesManager#getThickness()}).
	 * @param mapElement элемент карты
	 * @return толщина линии
	 */
	public int getLineSize(MapElement mapElement) {
		CharacteristicType cType = getCharacteristicType(
				LoginManager.getUserId(), 
				AbstractLinkController.ATTRIBUTE_THICKNESS);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null) {
			return MapPropertiesManager.getThickness();
		}
		return Integer.parseInt(ea.getValue());
	}

	/**
	 * Установить вид линии. Стиль определяется
	 * атрибутом {@link #ATTRIBUTE_STYLE}. В случае, если
	 * такого атрибута у элемента нет, создается новый.
	 * @param mapElement элемент карты
	 * @param style стиль
	 */
	public void setStyle(MapElement mapElement, String style) {
		CharacteristicType cType = getCharacteristicType(
				LoginManager.getUserId(), 
				AbstractLinkController.ATTRIBUTE_STYLE);
		Characteristic attribute = getCharacteristic(mapElement, cType);
		if(attribute == null) {
			try {
				attribute = Characteristic.createInstance(
						LoginManager.getUserId(),
						cType,
						"name",
						"1",
						style,
						mapElement,
						true,
						true);
				mapElement.addCharacteristic(attribute);
				StorableObjectPool.putStorableObject(attribute);
				StorableObjectPool.flush(attribute.getId(), true);
			} catch(CreateObjectException e) {
				e.printStackTrace();
				return;
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch(ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		attribute.setValue(style);
	}

	/**
	 * Получить вид линии. Стиль определяется
	 * атрибутом {@link #ATTRIBUTE_STYLE}. В случае, если
	 * такого атрибута у элемента нет, берется значение по умолчанию
	 * ({@link MapPropertiesManager#getStyle()}).
	 * @param mapElement элемент карты
	 * @return стиль
	 */
	public String getStyle(MapElement mapElement) {
		CharacteristicType cType = getCharacteristicType(
				LoginManager.getUserId(), 
				AbstractLinkController.ATTRIBUTE_STYLE);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null) {
			return MapPropertiesManager.getStyle();
		}
		return ea.getValue();
	}

	/**
	 * Получить стиль линии. Стиль определяется
	 * атрибутом {@link #ATTRIBUTE_STYLE}. В случае, если
	 * такого атрибута у элемента нет, берется значение по умолчанию
	 * ({@link MapPropertiesManager#getStroke()}).
	 * @param mapElement элемент карты
	 * @return стиль
	 */
	public Stroke getStroke(MapElement mapElement) {
		CharacteristicType cType = getCharacteristicType(
				LoginManager.getUserId(), 
				AbstractLinkController.ATTRIBUTE_STYLE);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null) {
			return MapPropertiesManager.getStroke();
		}
		return LineComboBox.getStrokeByType(ea.getValue());
	}

	/**
	 * Установить цвет. Цает определяется
	 * атрибутом {@link #ATTRIBUTE_COLOR}. В случае, если
	 * такого атрибута у элемента нет, создается новый.
	 * @param mapElement элемент карты
	 * @param color цвет
	 */
	public void setColor(MapElement mapElement, Color color) {
		CharacteristicType cType = getCharacteristicType(
				LoginManager.getUserId(), 
				AbstractLinkController.ATTRIBUTE_COLOR);
		Characteristic attribute = getCharacteristic(mapElement, cType);
		if(attribute == null) {
			try {
				attribute = Characteristic.createInstance(
						LoginManager.getUserId(),
						cType,
						"name",
						"1",
						String.valueOf(color.getRGB()),
						mapElement,
						true,
						true);
				mapElement.addCharacteristic(attribute);
				StorableObjectPool.putStorableObject(attribute);
				StorableObjectPool.flush(attribute.getId(), true);
			} catch(CreateObjectException e) {
				e.printStackTrace();
				return;
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch(ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		attribute.setValue(String.valueOf(color.getRGB()));
	}

	/**
	 * Получить цвет. Цает определяется
	 * атрибутом {@link #ATTRIBUTE_COLOR}. В случае, если
	 * такого атрибута у элемента нет, берется значение по умолчанию
	 * ({@link MapPropertiesManager#getColor()}).
	 * @param mapElement элемент карты
	 * @return цвет
	 */
	public Color getColor(MapElement mapElement) {
		CharacteristicType cType = getCharacteristicType(
				LoginManager.getUserId(), 
				AbstractLinkController.ATTRIBUTE_COLOR);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null) {
			return MapPropertiesManager.getColor();
		}
		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * Установить цвет при наличии сигнала тревоги. Цает определяется
	 * атрибутом {@link #ATTRIBUTE_ALARMED_COLOR}. В случае, если
	 * такого атрибута у элемента нет, создается новый.
	 * @param mapElement элемент карты
	 * @param color цвет
	 */
	public void setAlarmedColor(MapElement mapElement, Color color) {
		CharacteristicType cType = getCharacteristicType(
				LoginManager.getUserId(), 
				AbstractLinkController.ATTRIBUTE_ALARMED_COLOR);
		Characteristic attribute = getCharacteristic(mapElement, cType);
		if(attribute == null) {
			try {
				attribute = Characteristic.createInstance(
						LoginManager.getUserId(),
						cType,
						"name",
						"1",
						String.valueOf(color.getRGB()),
						mapElement,
						true,
						true);
				mapElement.addCharacteristic(attribute);
				StorableObjectPool.putStorableObject(attribute);
				StorableObjectPool.flush(attribute.getId(), true);
			} catch(CreateObjectException e) {
				e.printStackTrace();
				return;
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch(ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		attribute.setValue(String.valueOf(color.getRGB()));
	}

	/**
	 * Получить цвет при наличии сигнала тревоги. Цает определяется
	 * атрибутом {@link #ATTRIBUTE_ALARMED_COLOR}. В случае, если
	 * такого атрибута у элемента нет, берется значение по умолчанию
	 * ({@link MapPropertiesManager#getAlarmedColor()}).
	 * @param mapElement элемент карты
	 * @return цвет
	 */
	public Color getAlarmedColor(MapElement mapElement) {
		CharacteristicType cType = getCharacteristicType(
				LoginManager.getUserId(), 
				AbstractLinkController.ATTRIBUTE_ALARMED_COLOR);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null) {
			return MapPropertiesManager.getAlarmedColor();
		}
		return new Color(Integer.parseInt(ea.getValue()));
	}

	/**
	 * Установить толщину линии при наличи сигнала тревоги. Толщина определяется
	 * атрибутом {@link #ATTRIBUTE_ALARMED_THICKNESS}. В случае, если
	 * такого атрибута у элемента нет, создается новый.
	 * @param mapElement элемент карты
	 * @param size толщина линии
	 */
	public void setAlarmedLineSize(MapElement mapElement, int size) {
		CharacteristicType cType = getCharacteristicType(
				LoginManager.getUserId(),
				AbstractLinkController.ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic attribute = getCharacteristic(mapElement, cType);
		if(attribute == null) {
			try {
				attribute = Characteristic.createInstance(
						LoginManager.getUserId(),
						cType,
						"name",
						"1",
						String.valueOf(size),
						mapElement,
						true,
						true);
				mapElement.addCharacteristic(attribute);
				StorableObjectPool.putStorableObject(attribute);
				StorableObjectPool.flush(attribute.getId(), true);
			} catch(CreateObjectException e) {
				e.printStackTrace();
				return;
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch(ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		attribute.setValue(String.valueOf(size));
	}

	/**
	 * Получить толщину линии при наличи сигнала тревоги. Толщина определяется
	 * атрибутом {@link #ATTRIBUTE_ALARMED_THICKNESS}. В случае, если
	 * такого атрибута у элемента нет, берется значение по умолчанию
	 * ({@link MapPropertiesManager#getAlarmedThickness()}).
	 * @param mapElement элемент карты
	 * @return толщина линии
	 */
	public int getAlarmedLineSize(MapElement mapElement) {
		CharacteristicType cType = getCharacteristicType(
				LoginManager.getUserId(),
				AbstractLinkController.ATTRIBUTE_ALARMED_THICKNESS);
		Characteristic ea = getCharacteristic(mapElement, cType);
		if(ea == null) {
			return MapPropertiesManager.getAlarmedThickness();
		}
		return Integer.parseInt(ea.getValue());
	}
}
