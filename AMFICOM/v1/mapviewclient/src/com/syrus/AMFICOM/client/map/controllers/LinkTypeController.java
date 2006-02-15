/*-
 * $$Id: LinkTypeController.java,v 1.65 2006/02/15 11:12:33 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.util.Log;

/**
 * Контроллер типа линейного элемента карты.
 * 
 * @version $Revision: 1.65 $, $Date: 2006/02/15 11:12:33 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class LinkTypeController extends AbstractLinkController {
	/**
	 * Хэш-таблица цветов типов линий. Для того, чтобы объект {@link Color} 
	 * не создавался каждый раз из атрибута при вызове 
	 * {@link #getColor(PhysicalLinkType)}, созданный объект помещается в
	 * хэш-таблицу и при последующих обращениях используется повторно.
	 */
//	private static java.util.Map<Identifier, Color> colorsHolder = new HashMap<Identifier, Color>();
	/**
	 * Хэш-таблица цветов сигнала тревоги для типов линий. Для того, чтобы объект
	 * {@link Color} не создавался каждый раз из атрибута при вызове
	 * {@link #getAlarmedColor(PhysicalLinkType)}, созданный объект помещается в
	 * хэш-таблицу и при последующих обращениях используется повторно.
	 */
//	private static java.util.Map<Identifier, Color> alarmedColorsHolder = new HashMap<Identifier, Color>();

	/** Хэш-таблица цветов для предустановленных типов линии. */
	private static java.util.Map<String, Color> defaultLineColors = new HashMap<String, Color>();
	/** Хэш-таблица толщины линии для предустановленных типов линии. */
	private static java.util.Map<String, Integer> defaultLineThickness = new HashMap<String, Integer>();
	/** Хэш-таблица размерности привязки для предустановленных типов линии. */
	private static java.util.Map<String, IntDimension> defaultBindDimensions = new HashMap<String, IntDimension>();

	/**
	 * Instance
	 */
	private static LinkTypeController instance = null;

	static {
		defaultLineColors.put(PhysicalLinkType.DEFAULT_COLLECTOR, Color.DARK_GRAY);
		defaultLineColors.put(PhysicalLinkType.DEFAULT_TUNNEL, Color.BLACK);
		defaultLineColors.put(PhysicalLinkType.DEFAULT_INDOOR, Color.GREEN);
		defaultLineColors.put(PhysicalLinkType.DEFAULT_OVERHEAD, Color.BLUE);
		defaultLineColors.put(PhysicalLinkType.DEFAULT_SUBMARINE, Color.MAGENTA);
		defaultLineColors.put(PhysicalLinkType.DEFAULT_UNBOUND, Color.RED);

		defaultLineThickness.put(PhysicalLinkType.DEFAULT_COLLECTOR, new Integer(4));
		defaultLineThickness.put(PhysicalLinkType.DEFAULT_TUNNEL, new Integer(2));
		defaultLineThickness.put(PhysicalLinkType.DEFAULT_INDOOR, new Integer(1));
		defaultLineThickness.put(PhysicalLinkType.DEFAULT_OVERHEAD, new Integer(2));
		defaultLineThickness.put(PhysicalLinkType.DEFAULT_SUBMARINE, new Integer(3));
		defaultLineThickness.put(PhysicalLinkType.DEFAULT_UNBOUND, new Integer(1));

		defaultBindDimensions.put(PhysicalLinkType.DEFAULT_COLLECTOR, new IntDimension(2, 6));
		defaultBindDimensions.put(PhysicalLinkType.DEFAULT_TUNNEL, new IntDimension(3, 4));
		defaultBindDimensions.put(PhysicalLinkType.DEFAULT_INDOOR, new IntDimension(1, 1));
		defaultBindDimensions.put(PhysicalLinkType.DEFAULT_OVERHEAD, new IntDimension(10, 1));
		defaultBindDimensions.put(PhysicalLinkType.DEFAULT_SUBMARINE, new IntDimension(3, 4));
		defaultBindDimensions.put(PhysicalLinkType.DEFAULT_UNBOUND, new IntDimension(0, 0));
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
		if (instance == null) {
			instance = new LinkTypeController();
		}
		return instance;
	}

	/**
	 * {@inheritDoc} Suppress since PhysicalLinkType is not really a Map Element
	 */
	public String getToolTipText(final MapElement mapElement) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since PhysicalLinkType is not really a Map Element
	 */
	@Override
	public boolean isSelectionVisible(final MapElement mapElement) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since PhysicalLinkType is not really a Map Element
	 */
	public boolean isElementVisible(final MapElement mapElement, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since PhysicalLinkType is not really a Map Element
	 */
	public void paint(final MapElement mapElement, final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since PhysicalLinkType is not really a Map Element
	 */
	public boolean isMouseOnElement(final MapElement mapElement, final Point currentMousePoint)
			throws MapConnectionException,
				MapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Получить цвет по кодовому имени для предустановленного типа линии.
	 * 
	 * @param codename
	 *        кодовое имя
	 * @return цвет
	 */
	public static Color getDefaultLineColor(final String codename) {
		return defaultLineColors.get(codename);
	}

	/**
	 * Получить толщину линии по кодовому имени для предустановленного типа линии.
	 * 
	 * @param codename
	 *        кодовое имя
	 * @return толщина
	 */
	public static int getDefaultLineThickness(final String codename) {
		return defaultLineThickness.get(codename).intValue();
	}

	/**
	 * Получить размерность привязки по кодовому имени для предустановленного типа
	 * линии.
	 * 
	 * @param codename
	 *        кодовое имя
	 * @return размерность привязки
	 */
	public static IntDimension getDefaultBindDimension(final String codename) {
		return defaultBindDimensions.get(codename);
	}

	@Override
	@Deprecated
	public void setColor(final Characterizable characterizable, final Color color) {
		super.setColor(characterizable, color);
//		colorsHolder.put(characterizable.getId(), color);
	}

	/**
	 * Получить цвет типа линии. Цает определяется атрибутом
	 * {@link AbstractLinkController#ATTRIBUTE_COLOR}. В случае, если такого
	 * атрибута у элемента нет, берется значение по умолчанию ({@link MapPropertiesManager#getColor()}).
	 * При первом вызове метода для объекта <code>linkType</code> полученный
	 * цвет помещается в хэш-таблицу {@link #colorsHolder} и при повторных вызовах
	 * берется из нее.
	 * 
	 * @param characterizable
	 *        тип линии
	 * @return цвет
	 */
//	@Override
//	@Deprecated
//	public Color getColor(final Characterizable characterizable) {
//		return super.getColor(characterizable);
//		Color color = colorsHolder.get(characterizable.getId());
//		if (color == null) {
//			final Characteristic ea = getCharacteristic(characterizable, this.colorCharType);
//
//			if (ea == null) {
//				color = MapPropertiesManager.getColor();
//			}
//			else {
//				color = this.colors.get(ea.getValue());
//				if (color == null) {
//					color = new Color(Integer.parseInt(ea.getValue()));
//					this.colors.put(ea.getValue(), color);
//				}
//				color = new Color(Integer.parseInt(ea.getValue()));
//			}
//
//			colorsHolder.put(characterizable.getId(), color);
//		}
//		return color;
//	}

	/**
	 * Установить цвет типа линии при наличии сигнала тревоги. Цает определяется
	 * атрибутом {@link AbstractLinkController#ATTRIBUTE_ALARMED_COLOR}. В
	 * случае, если такого атрибута у элемента нет, создается новый.
	 */
	@Override
	@Deprecated
	public void setAlarmedColor(final Characterizable characterizable, final Color color) {
		super.setAlarmedColor(characterizable, color);
//		alarmedColorsHolder.put(characterizable.getId(), color);
	}

	/**
	 * Получить цвет для типа линии при наличии сигнала тревоги. Цает определяется
	 * атрибутом {@link AbstractLinkController#ATTRIBUTE_ALARMED_COLOR}. В
	 * случае, если такого атрибута у элемента нет, берется значение по умолчанию ({@link MapPropertiesManager#getAlarmedColor()}).
	 * При первом вызове метода для объекта <code>linkType</code> полученный
	 * цвет помещается в хэш-таблицу {@link #alarmedColorsHolder} и при повторных
	 * вызовах берется из нее.
	 */
	@Override
	@Deprecated
	public Color getAlarmedColor(final Characterizable characterizable) {
		return super.getAlarmedColor(characterizable);
		
//		Color color = alarmedColorsHolder.get(characterizable.getId());
//		if (color == null) {
//			Characteristic ea = getCharacteristic(characterizable, this.alarmedColorCharType);
//			if (ea == null) {
//				color = MapPropertiesManager.getAlarmedColor();
//			}
//			else {
//				color = this.colors.get(ea.getValue());
//				if (color == null) {
//					color = new Color(Integer.parseInt(ea.getValue()));
//					this.colors.put(ea.getValue(), color);
//				}
//				color = new Color(Integer.parseInt(ea.getValue()));
//			}
//
//			alarmedColorsHolder.put(characterizable.getId(), color);
//		}
//		return color;
	}

	/**
	 * Получить тип линии по кодовому имени.
	 * @param codename кодовое имя
	 * @return тип линии
	 * @throws ApplicationException 
	 */
	public static PhysicalLinkType getPhysicalLinkType(final String codename) throws ApplicationException {
		return getPhysicalLinkType(codename, false);
	}

	static PhysicalLinkType getPhysicalLinkType(final String codename, final boolean useLoader) throws ApplicationException {
		StorableObjectCondition pTypeCondition = new TypicalCondition(codename,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PHYSICALLINK_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		final Collection<PhysicalLinkType> pTypes = StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, useLoader);
		if (pTypes.size() > 0) {
			return pTypes.iterator().next();
		}

		return null;
	}

	/**
	 * Получить тип линии по кодовому имени. В случае, если такого типа нет,
	 * создается новый.
	 * 
	 * @param codename
	 *        кодовое имя
	 * @return тип линии
	 * @throws CreateObjectException
	 */
	static PhysicalLinkType getPhysicalLinkType(final MapLibrary mapLibrary,
			final PhysicalLinkTypeSort sort,
			final String codename,
			final boolean topological) throws ApplicationException {
		PhysicalLinkType type = getPhysicalLinkType(codename, true);
		if (type == null) {
			LinkTypeController ltc = (LinkTypeController) LinkTypeController.getInstance();
			final Identifier userId = LoginManager.getUserId();

			type = PhysicalLinkType.createInstance(
					userId,
					sort,
					codename,
					I18N.getString(codename),
					"", //$NON-NLS-1$
					LinkTypeController.getDefaultBindDimension(codename),
					topological,
					mapLibrary.getId());

			ltc.setLineSize(type, LinkTypeController.getDefaultLineThickness(codename));
			ltc.setColor(type, LinkTypeController.getDefaultLineColor(codename));

			StorableObjectPool.flush(type, userId, true);
		}
		else {
			type.setSort(sort);
			type.setTopological(topological);
		}
		return type;
	}

	/**
	 * Получить список всех типов линий.
	 * @return список типов линий &lt;{@link PhysicalLinkType}&gt;
	 */
	public static Collection<PhysicalLinkType> getTopologicalLinkTypes(Map map) {
		Set<PhysicalLinkType> objects = new HashSet<PhysicalLinkType>();
		for(MapLibrary library : map.getMapLibraries()) {
			for(PhysicalLinkType physicalLinkType : library.getPhysicalLinkTypes()) {
				if(physicalLinkType.isTopological()) {
					objects.add(physicalLinkType);
				}
			}
		}

		return objects;
	}

	/**
	 * Получить тип линии по умолчанию ({@link PhysicalLinkType#DEFAULT_TUNNEL}).
	 * @return тип линии
	 * @throws ApplicationException 
	 */
	public static PhysicalLinkType getDefaultPhysicalLinkType() throws ApplicationException {
		return LinkTypeController.getPhysicalLinkType(PhysicalLinkType.DEFAULT_TUNNEL, false);
	}

	/**
	 * Получить тип непривязанной линии ({@link PhysicalLinkType#DEFAULT_UNBOUND}).
	 * @return тип линии
	 * @throws ApplicationException 
	 */
	public static PhysicalLinkType getUnboundPhysicalLinkType() throws ApplicationException {
		return LinkTypeController.getPhysicalLinkType(PhysicalLinkType.DEFAULT_UNBOUND, false);
	}

	public static PhysicalLinkType getIndoorLinkType() {
		try {
			final StorableObjectCondition pTypeCondition = new EquivalentCondition(ObjectEntities.PHYSICALLINK_TYPE_CODE);
			final Set<PhysicalLinkType> list = StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, false);

			for (Iterator it = list.iterator(); it.hasNext();) {
				final PhysicalLinkType type = (PhysicalLinkType) it.next();
				if (type.getSort().value() == PhysicalLinkTypeSort._INDOOR) {
					return type;
				}
			}
		} catch (Exception e) {
			Log.errorMessage(e);
		}
		return null;
	}

	/**
	 * {@inheritDoc} Suppress since PhysicalLinkType is not really a Map Element
	 */
	public Rectangle getBoundingRectangle(MapElement mapElement) throws MapConnectionException, MapDataException {
		throw new UnsupportedOperationException();
	}
}
