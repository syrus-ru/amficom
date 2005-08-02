/**
 * $Id: AbstractLinkController.java,v 1.30 2005/08/02 07:33:14 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

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
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.util.Log;

/**
 * Контроллер линейного элемента карты.
 * @author $Author: krupenn $
 * @version $Revision: 1.30 $, $Date: 2005/08/02 07:33:14 $
 * @module mapviewclient_v1
 */
public abstract class AbstractLinkController extends AbstractMapElementController
{
	/**
	 * Карта объектов Color - локальный кэш (инициализируется при первом использовании)
	 */
	Map colors = new HashMap();
	/**
	 * Карта объектов Stroke - локальный кэш (инициализируется при первом использовании)
	 */
	Map strokes = new HashMap();	

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

	protected CharacteristicType thicknessCharType = null;
	protected CharacteristicType styleCharType = null;
	protected CharacteristicType colorCharType = null;
	protected CharacteristicType alarmedThicknessCharType = null;
	protected CharacteristicType alarmedColorCharType = null;	
	
	public AbstractLinkController(NetMapViewer netMapViewer) {
		super(netMapViewer);

		Identifier userId = LoginManager.getUserId();

		this.styleCharType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_STYLE);
		this.thicknessCharType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_THICKNESS);
		this.colorCharType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_COLOR);

		this.alarmedThicknessCharType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_ALARMED_THICKNESS);

		this.alarmedColorCharType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_ALARMED_COLOR);
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
		DataType dataType = DataType.STRING;

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
			StorableObjectPool.flush(type, userId, true);
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
		try {
			for(Iterator it = mapElement.getCharacteristics().iterator(); it.hasNext();) {
				Characteristic ch = (Characteristic )it.next();
				if(ch.getType().equals(cType)) {
					return ch;
				}
			}
		} catch(ApplicationException e) {
			Log.debugException(e, Level.WARNING);
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
		Characteristic attribute = getCharacteristic(mapElement, this.thicknessCharType);
		if(attribute == null) {
			try {
				attribute = Characteristic.createInstance(
						LoginManager.getUserId(),
						this.thicknessCharType,
						"name",
						"1",
						String.valueOf(size),
						mapElement,
						true,
						true);
				StorableObjectPool.putStorableObject(attribute);
				StorableObjectPool.flush(attribute, LoginManager.getUserId(), true);
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
		Characteristic ea = getCharacteristic(mapElement, this.thicknessCharType);
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
		Characteristic attribute = getCharacteristic(mapElement, this.styleCharType);
		if(attribute == null) {
			try {
				attribute = Characteristic.createInstance(
						LoginManager.getUserId(),
						this.styleCharType,
						"name",
						"1",
						style,
						mapElement,
						true,
						true);
				StorableObjectPool.putStorableObject(attribute);
				StorableObjectPool.flush(attribute, LoginManager.getUserId(), true);
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
		Characteristic ea = getCharacteristic(mapElement, this.styleCharType);
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
		int thickness = getLineSize(mapElement);
		String style = getStyle(mapElement);
		String key = style + " " + thickness;
		BasicStroke strokeForLink = (BasicStroke)this.strokes.get(key);
		if(strokeForLink == null) {
			strokeForLink = LineComboBox.getStrokeByType(style); 
			int defaultThickness = (int)strokeForLink.getLineWidth();	
	
			if (thickness != defaultThickness)
				strokeForLink = new BasicStroke(
						thickness,
						strokeForLink.getEndCap(),
						strokeForLink.getLineJoin(),
						strokeForLink.getMiterLimit(),
						strokeForLink.getDashArray(),
						strokeForLink.getDashPhase());

			this.strokes.put(key, strokeForLink);
		}

		return strokeForLink;
	}

	/**
	 * Установить цвет. Цает определяется
	 * атрибутом {@link #ATTRIBUTE_COLOR}. В случае, если
	 * такого атрибута у элемента нет, создается новый.
	 * @param mapElement элемент карты
	 * @param color цвет
	 */
	public void setColor(MapElement mapElement, Color color) {
		Characteristic attribute = getCharacteristic(mapElement, this.colorCharType);
		if(attribute == null) {
			try {
				attribute = Characteristic.createInstance(
						LoginManager.getUserId(),
						this.colorCharType,
						"name",
						"1",
						String.valueOf(color.getRGB()),
						mapElement,
						true,
						true);
				StorableObjectPool.putStorableObject(attribute);
				StorableObjectPool.flush(attribute, LoginManager.getUserId(), true);
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
		Characteristic ea = getCharacteristic(mapElement, this.colorCharType);
		if(ea == null) {
			return MapPropertiesManager.getColor();
		}
			
		Color color = (Color)this.colors.get(ea.getValue());
		if (color == null)
		{
			color = new Color(Integer.parseInt(ea.getValue()));
			this.colors.put(ea.getValue(),color);
		}
		return color;
	}

	/**
	 * Установить цвет при наличии сигнала тревоги. Цает определяется
	 * атрибутом {@link #ATTRIBUTE_ALARMED_COLOR}. В случае, если
	 * такого атрибута у элемента нет, создается новый.
	 * @param mapElement элемент карты
	 * @param color цвет
	 */
	public void setAlarmedColor(MapElement mapElement, Color color) {
		Characteristic attribute = getCharacteristic(mapElement, this.alarmedColorCharType);
		if(attribute == null) {
			try {
				attribute = Characteristic.createInstance(
						LoginManager.getUserId(),
						this.alarmedColorCharType,
						"name",
						"1",
						String.valueOf(color.getRGB()),
						mapElement,
						true,
						true);
				StorableObjectPool.putStorableObject(attribute);
				StorableObjectPool.flush(attribute, LoginManager.getUserId(), true);
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
		Characteristic ea = getCharacteristic(mapElement, this.alarmedColorCharType);
		if(ea == null) {
			return MapPropertiesManager.getAlarmedColor();
		}
		Color color = (Color)this.colors.get(ea.getValue());
		if (color == null)
		{
			color = new Color(Integer.parseInt(ea.getValue()));
			this.colors.put(ea.getValue(),color);
		}
		return color;
	}

	/**
	 * Установить толщину линии при наличи сигнала тревоги. Толщина определяется
	 * атрибутом {@link #ATTRIBUTE_ALARMED_THICKNESS}. В случае, если
	 * такого атрибута у элемента нет, создается новый.
	 * @param mapElement элемент карты
	 * @param size толщина линии
	 */
	public void setAlarmedLineSize(MapElement mapElement, int size) {
		Characteristic attribute = getCharacteristic(mapElement, this.alarmedThicknessCharType);
		if(attribute == null) {
			try {
				attribute = Characteristic.createInstance(
						LoginManager.getUserId(),
						this.alarmedThicknessCharType,
						"name",
						"1",
						String.valueOf(size),
						mapElement,
						true,
						true);
				StorableObjectPool.putStorableObject(attribute);
				StorableObjectPool.flush(attribute, LoginManager.getUserId(), true);
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
		Characteristic ea = getCharacteristic(mapElement, this.alarmedThicknessCharType);
		if(ea == null) {
			return MapPropertiesManager.getAlarmedThickness();
		}
		return Integer.parseInt(ea.getValue());
	}
}
