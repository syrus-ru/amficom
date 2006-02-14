/*-
 * $$Id: AbstractLinkController.java,v 1.53 2006/02/14 10:20:06 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.ui.LineComboBox;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * Контроллер линейного элемента карты.
 * 
 * @version $Revision: 1.53 $, $Date: 2006/02/14 10:20:06 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public abstract class AbstractLinkController extends AbstractMapElementController {
	/**
	 * Карта объектов Color - локальный кэш (инициализируется при первом использовании)
	 */
	Map<Identifiable, Color> colors = new HashMap<Identifiable, Color>();
	Map<Identifiable, Color> alarmedColors = new HashMap<Identifiable, Color>();

	/**
	 * Карта объектов Stroke - локальный кэш (инициализируется при первом использовании)
	 */
	Map<Identifiable, BasicStroke> alarmedStrokes = new HashMap<Identifiable, BasicStroke>();
	Map<Identifiable, BasicStroke> strokes = new HashMap<Identifiable, BasicStroke>();	

	
	/** Кодовое имя атрибута "Толщина линии". */
	public static final String ATTRIBUTE_THICKNESS = "thickness"; //$NON-NLS-1$
	/** Кодовое имя атрибута "Цвет". */
	public static final String ATTRIBUTE_COLOR = "color"; //$NON-NLS-1$
	/** Кодовое имя атрибута "Стиль линии". */
	public static final String ATTRIBUTE_STYLE = "style"; //$NON-NLS-1$
	/** Кодовое имя атрибута "Толщина линии при сигнале тревоги". */
	public static final String ATTRIBUTE_ALARMED_THICKNESS = "alarmed_thickness"; //$NON-NLS-1$
	/** Кодовое имя атрибута "Цвет при сигнале тревоги". */
	public static final String ATTRIBUTE_ALARMED_COLOR = "alarmed_color"; //$NON-NLS-1$

	protected CharacteristicType thicknessCharType = null;
	protected CharacteristicType styleCharType = null;
	protected CharacteristicType colorCharType = null;
	protected CharacteristicType alarmedThicknessCharType = null;
	protected CharacteristicType alarmedColorCharType = null;	

	public AbstractLinkController(final NetMapViewer netMapViewer) {
		super(netMapViewer);

		final Identifier userId = LoginManager.getUserId();

		this.styleCharType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_STYLE,
				I18N.getString(MapEditorResourceKeys.VALUE_STYLE));
		this.thicknessCharType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_THICKNESS,
				I18N.getString(MapEditorResourceKeys.VALUE_THICKNESS));
		this.colorCharType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_COLOR,
				I18N.getString(MapEditorResourceKeys.VALUE_COLOR));

		this.alarmedThicknessCharType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_ALARMED_THICKNESS,
				I18N.getString(MapEditorResourceKeys.VALUE_ALARMED_THICKNESS));

		this.alarmedColorCharType = getCharacteristicType(
				userId, 
				AbstractLinkController.ATTRIBUTE_ALARMED_COLOR,
				I18N.getString(MapEditorResourceKeys.VALUE_ALARMED_COLOR));
	}

	/**
	 * Определить, видна ли рамка выделения элемента карты.
	 * @param mapElement элемент карты
	 * @return флаг видимости рамки выделения
	 */
	public abstract boolean isSelectionVisible(final MapElement mapElement);

	/**
	 * Найти тип атрибута по кодовому имени. Если такого типа
	 * не найдено, создать новый.
	 * @param userId пользователь
	 * @param codename кодовое имя
	 * @return тип атрибута
	 */
	public static CharacteristicType getCharacteristicType(
			final Identifier userId, 
			final String codename,
			final String name) {
		final CharacteristicTypeSort sort = CharacteristicTypeSort.VISUAL;
		final DataType dataType = DataType.STRING;

		final StorableObjectCondition pTypeCondition = new TypicalCondition(codename,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.CHARACTERISTIC_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		try {
			final Collection pTypes = StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
			if (!pTypes.isEmpty()) {
				final CharacteristicType type = (CharacteristicType) pTypes.iterator().next();
				return type;
			}
		} catch (ApplicationException ex) {
			Log.debugMessage("Exception searching CharacteristicType. Creating new one.", Level.CONFIG); //$NON-NLS-1$
		}

		try {
			final CharacteristicType type = CharacteristicType.createInstance(userId,
					codename,
					I18N.getString(MapEditorResourceKeys.VALUE_NO_DESCRIPTION),
					name,
					dataType,
					sort);
			StorableObjectPool.flush(type, userId, true);
			return type;
		} catch (ApplicationException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @deprecated should be rewritten via StorableObjectCondition 
	 */
	@Shitlet
	@Deprecated
	public static Characteristic getCharacteristic(final Characterizable characterizable, final CharacteristicType characteristicType) {
		try {
			final long d = System.nanoTime();
			final Set<Characteristic> characteristics = characterizable.getCharacteristics(false);
			final long f = System.nanoTime();
			MapViewController.addTime6(f - d);
//			Log.debugMessage("at " + (f - d) + "ns", Level.INFO);
			for (final Characteristic ch : characteristics) {
				if (ch.getType().equals(characteristicType)) {
					return ch;
				}
			}
		} catch (ApplicationException e) {
			Log.debugMessage(e, Level.WARNING);
		}
		return null;
	}

	/**
	 * Установить толщину линии. Толщина определяется
	 * атрибутом {@link #ATTRIBUTE_THICKNESS}. В случае, если
	 * такого атрибута у элемента нет, создается новый.
	 * @param characterizable элемент карты
	 * @param size толщина линии
	 */
	public void setLineSize(final Characterizable characterizable, final int size) {
		this.strokes.remove(characterizable.getId());
		
		Characteristic attribute = getCharacteristic(characterizable, this.thicknessCharType);
		if (attribute == null) {
			try {
				attribute = Characteristic.createInstance(LoginManager.getUserId(),
						this.thicknessCharType,
						I18N.getString(MapEditorResourceKeys.NONAME),
						"1", //$NON-NLS-1$
						String.valueOf(size),
						characterizable,
						true,
						true);
				StorableObjectPool.flush(attribute, LoginManager.getUserId(), true);
				characterizable.addCharacteristic(attribute, false);
			} catch (CreateObjectException e) {
				e.printStackTrace();
				return;
			} catch (IllegalObjectEntityException e) {
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
	 * Получить толщину линии. Толщина определяется атрибутом
	 * {@link #ATTRIBUTE_THICKNESS}. В случае, если такого атрибута у элемента
	 * нет, берется значение по умолчанию ({@link MapPropertiesManager#getThickness()}).
	 */
	protected int getLineSize(final Characterizable characterizable) {
		final Characteristic ea = getCharacteristic(characterizable, this.thicknessCharType);
		if (ea == null) {
			return MapPropertiesManager.getThickness();
		}
		return Integer.parseInt(ea.getValue());
	}

	/**
	 * Установить вид линии. Стиль определяется
	 * атрибутом {@link #ATTRIBUTE_STYLE}. В случае, если
	 * такого атрибута у элемента нет, создается новый.
	 * @param characterizable элемент карты
	 */
	public void clearCachedElement(final Characterizable characterizable) {
		this.strokes.remove(characterizable.getId());
		this.colors.remove(characterizable.getId());
		this.alarmedColors.remove(characterizable.getId());
	}

	/**
	 * Получить вид линии. Стиль определяется атрибутом {@link #ATTRIBUTE_STYLE}.
	 * В случае, если такого атрибута у элемента нет, берется значение по
	 * умолчанию ({@link MapPropertiesManager#getStyle()}).
	 * 
	 * @param characterizable
	 *        элемент карты
	 * @return стиль
	 */
	protected String getStyle(final Characterizable characterizable) {
		final Characteristic ea = getCharacteristic(characterizable, this.styleCharType);
		if (ea == null) {
			return MapPropertiesManager.getStyle();
		}
		return ea.getValue();
	}

	/**
	 * Получить стиль линии. Стиль определяется
	 * атрибутом {@link #ATTRIBUTE_STYLE}. В случае, если
	 * такого атрибута у элемента нет, берется значение по умолчанию
	 * ({@link MapPropertiesManager#getStroke()}).
	 * @param characterizable элемент карты
	 * @return стиль
	 */
	public BasicStroke getStroke(final Characterizable characterizable) {
		BasicStroke strokeForLink = this.strokes.get(characterizable.getId());
		
		if (strokeForLink == null) {
			final int thickness = getLineSize(characterizable);
			final String style = getStyle(characterizable);
			strokeForLink = LineComboBox.getStrokeByType(style);
			final int defaultThickness = (int) strokeForLink.getLineWidth();

			if (thickness != defaultThickness)
				strokeForLink = new BasicStroke(thickness,
						strokeForLink.getEndCap(),
						strokeForLink.getLineJoin(),
						strokeForLink.getMiterLimit(),
						strokeForLink.getDashArray(),
						strokeForLink.getDashPhase());

			this.strokes.put(characterizable.getId(), strokeForLink);
		}

		return strokeForLink;
	}

	/**
	 * Установить цвет. Цает определяется атрибутом {@link #ATTRIBUTE_COLOR}. В
	 * случае, если такого атрибута у элемента нет, создается новый.
	 * 
	 * @param characterizable
	 *        элемент карты
	 * @param color
	 *        цвет
	 */
	public void setColor(final Characterizable characterizable, final Color color) {
		this.colors.put(characterizable.getId(), color);
		
		Characteristic attribute = getCharacteristic(characterizable, this.colorCharType);
		if(attribute == null) {
			try {
				attribute = Characteristic.createInstance(
						LoginManager.getUserId(),
						this.colorCharType,
						I18N.getString(MapEditorResourceKeys.NONAME),
						"1", //$NON-NLS-1$
						String.valueOf(color.getRGB()),
						characterizable,
						true,
						true);
				StorableObjectPool.flush(attribute, LoginManager.getUserId(), true);
				characterizable.addCharacteristic(attribute, false);
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
	 * @param characterizable элемент карты
	 * @return цвет
	 */
	public Color getColor(final Characterizable characterizable) {
		Color color = this.colors.get(characterizable);
		if (color == null) {
			final Characteristic ea = getCharacteristic(characterizable, this.colorCharType);
			if (ea == null) {
				return MapPropertiesManager.getColor();
			}
			color = new Color(Integer.parseInt(ea.getValue()));
			this.colors.put(characterizable, color);
		}
		return color;
	}

	/**
	 * Установить цвет при наличии сигнала тревоги. Цает определяется
	 * атрибутом {@link #ATTRIBUTE_ALARMED_COLOR}. В случае, если
	 * такого атрибута у элемента нет, создается новый.
	 * @param characterizable элемент карты
	 * @param color цвет
	 */
	public void setAlarmedColor(final Characterizable characterizable, final Color color) {
		Characteristic attribute = getCharacteristic(characterizable, this.alarmedColorCharType);
		if (attribute == null) {
			try {
				attribute = Characteristic.createInstance(LoginManager.getUserId(),
						this.alarmedColorCharType,
						I18N.getString(MapEditorResourceKeys.NONAME),
						"1", //$NON-NLS-1$
						String.valueOf(color.getRGB()),
						characterizable,
						true,
						true);
				StorableObjectPool.flush(attribute, LoginManager.getUserId(), true);
				characterizable.addCharacteristic(attribute, false);
			} catch (CreateObjectException e) {
				e.printStackTrace();
				return;
			} catch (IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		attribute.setValue(String.valueOf(color.getRGB()));
		this.alarmedColors.put(characterizable.getId(), color);
	}

	/**
	 * Получить цвет при наличии сигнала тревоги. Цает определяется
	 * атрибутом {@link #ATTRIBUTE_ALARMED_COLOR}. В случае, если
	 * такого атрибута у элемента нет, берется значение по умолчанию
	 * ({@link MapPropertiesManager#getAlarmedColor()}).
	 * @param characterizable элемент карты
	 * @return цвет
	 */
	public Color getAlarmedColor(final Characterizable characterizable) {
		Color color = this.alarmedColors.get(characterizable.getId());
		if (color == null) {
			
			final Characteristic ea = getCharacteristic(characterizable, this.alarmedColorCharType);
			if (ea == null) {
				return MapPropertiesManager.getAlarmedColor();
			}
			color = new Color(Integer.parseInt(ea.getValue()));
			this.colors.put(characterizable.getId(), color);
		}
		return color;
	}

	/**
	 * Установить толщину линии при наличи сигнала тревоги. Толщина определяется
	 * атрибутом {@link #ATTRIBUTE_ALARMED_THICKNESS}. В случае, если
	 * такого атрибута у элемента нет, создается новый.
	 * @param characterizable элемент карты
	 * @param size толщина линии
	 */
	/*public void setAlarmedLineSize(final Characterizable characterizable, final int size) {
		Characteristic attribute = getCharacteristic(characterizable, this.alarmedThicknessCharType);
		if (attribute == null) {
			try {
				attribute = Characteristic.createInstance(LoginManager.getUserId(),
						this.alarmedThicknessCharType,
						I18N.getString(MapEditorResourceKeys.NONAME),
						"1", //$NON-NLS-1$
						String.valueOf(size),
						characterizable,
						true,
						true);
				StorableObjectPool.flush(attribute, LoginManager.getUserId(), true);
				characterizable.addCharacteristic(attribute, false);
			} catch (CreateObjectException e) {
				e.printStackTrace();
				return;
			} catch (IllegalObjectEntityException e) {
				e.printStackTrace();
				return;
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		attribute.setValue(String.valueOf(size));
	}*/

	/**
	 * Получить толщину линии при наличи сигнала тревоги. Толщина определяется
	 * атрибутом {@link #ATTRIBUTE_ALARMED_THICKNESS}. В случае, если такого
	 * атрибута у элемента нет, берется значение по умолчанию ({@link MapPropertiesManager#getAlarmedThickness()}).
	 * 
	 * @param characterizable
	 *        элемент карты
	 * @return толщина линии
	 */
	/*public int getAlarmedLineSize(final Characterizable characterizable) {
		final Characteristic ea = getCharacteristic(characterizable, this.alarmedThicknessCharType);
		if (ea == null) {
			return MapPropertiesManager.getAlarmedThickness();
		}
		return Integer.parseInt(ea.getValue());
	}*/
}
