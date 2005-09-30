/*-
 * $$Id: LinkTypeController.java,v 1.61 2005/09/30 16:08:39 krupenn Exp $$
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
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
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

/**
 * ���������� ���� ��������� �������� �����.
 * 
 * @version $Revision: 1.61 $, $Date: 2005/09/30 16:08:39 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class LinkTypeController extends AbstractLinkController {
	/**
	 * ���-������� ������ ����� �����. ��� ����, ����� ������ {@link Color} 
	 * �� ���������� ������ ��� �� �������� ��� ������ 
	 * {@link #getColor(PhysicalLinkType)}, ��������� ������ ���������� �
	 * ���-������� � ��� ����������� ���������� ������������ ��������.
	 */
	private static java.util.Map<Identifier, Color> colorsHolder = new HashMap<Identifier, Color>();
	/**
	 * ���-������� ������ ������� ������� ��� ����� �����. ��� ����, ����� ������
	 * {@link Color} �� ���������� ������ ��� �� �������� ��� ������
	 * {@link #getAlarmedColor(PhysicalLinkType)}, ��������� ������ ���������� �
	 * ���-������� � ��� ����������� ���������� ������������ ��������.
	 */
	private static java.util.Map<Identifier, Color> alarmedColorsHolder = new HashMap<Identifier, Color>();

	/** ���-������� ������ ��� ����������������� ����� �����. */
	private static java.util.Map<String, Color> defaultLineColors = new HashMap<String, Color>();
	/** ���-������� ������� ����� ��� ����������������� ����� �����. */
	private static java.util.Map<String, Integer> defaultLineThickness = new HashMap<String, Integer>();
	/** ���-������� ����������� �������� ��� ����������������� ����� �����. */
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
	 * �������� ���� �� �������� ����� ��� ������������������ ���� �����.
	 * 
	 * @param codename
	 *        ������� ���
	 * @return ����
	 */
	public static Color getDefaultLineColor(final String codename) {
		return defaultLineColors.get(codename);
	}

	/**
	 * �������� ������� ����� �� �������� ����� ��� ������������������ ���� �����.
	 * 
	 * @param codename
	 *        ������� ���
	 * @return �������
	 */
	public static int getDefaultLineThickness(final String codename) {
		return defaultLineThickness.get(codename).intValue();
	}

	/**
	 * �������� ����������� �������� �� �������� ����� ��� ������������������ ����
	 * �����.
	 * 
	 * @param codename
	 *        ������� ���
	 * @return ����������� ��������
	 */
	public static IntDimension getDefaultBindDimension(final String codename) {
		return defaultBindDimensions.get(codename);
	}

	@Override
	public void setColor(final Characterizable characterizable, final Color color) {
		super.setColor(characterizable, color);
		colorsHolder.put(characterizable.getId(), color);
	}

	/**
	 * �������� ���� ���� �����. ���� ������������ ���������
	 * {@link AbstractLinkController#ATTRIBUTE_COLOR}. � ������, ���� ������
	 * �������� � �������� ���, ������� �������� �� ��������� ({@link MapPropertiesManager#getColor()}).
	 * ��� ������ ������ ������ ��� ������� <code>linkType</code> ����������
	 * ���� ���������� � ���-������� {@link #colorsHolder} � ��� ��������� �������
	 * ������� �� ���.
	 * 
	 * @param characterizable
	 *        ��� �����
	 * @return ����
	 */
	@Override
	public Color getColor(final Characterizable characterizable) {
		Color color = colorsHolder.get(characterizable.getId());
		if (color == null) {
			final Characteristic ea = getCharacteristic(characterizable, this.colorCharType);

			if (ea == null) {
				color = MapPropertiesManager.getColor();
			}
			else {
				color = this.colors.get(ea.getValue());
				if (color == null) {
					color = new Color(Integer.parseInt(ea.getValue()));
					this.colors.put(ea.getValue(), color);
				}
				color = new Color(Integer.parseInt(ea.getValue()));
			}

			colorsHolder.put(characterizable.getId(), color);
		}
		return color;
	}

	/**
	 * ���������� ���� ���� ����� ��� ������� ������� �������. ���� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_ALARMED_COLOR}. �
	 * ������, ���� ������ �������� � �������� ���, ��������� �����.
	 */
	@Override
	public void setAlarmedColor(final Characterizable characterizable, final Color color) {
		super.setAlarmedColor(characterizable, color);
		alarmedColorsHolder.put(characterizable.getId(), color);
	}

	/**
	 * �������� ���� ��� ���� ����� ��� ������� ������� �������. ���� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_ALARMED_COLOR}. �
	 * ������, ���� ������ �������� � �������� ���, ������� �������� �� ��������� ({@link MapPropertiesManager#getAlarmedColor()}).
	 * ��� ������ ������ ������ ��� ������� <code>linkType</code> ����������
	 * ���� ���������� � ���-������� {@link #alarmedColorsHolder} � ��� ���������
	 * ������� ������� �� ���.
	 */
	@Override
	public Color getAlarmedColor(final Characterizable characterizable) {
		Color color = alarmedColorsHolder.get(characterizable.getId());
		if (color == null) {
			Characteristic ea = getCharacteristic(characterizable, this.alarmedColorCharType);
			if (ea == null) {
				color = MapPropertiesManager.getAlarmedColor();
			}
			else {
				color = this.colors.get(ea.getValue());
				if (color == null) {
					color = new Color(Integer.parseInt(ea.getValue()));
					this.colors.put(ea.getValue(), color);
				}
				color = new Color(Integer.parseInt(ea.getValue()));
			}

			alarmedColorsHolder.put(characterizable.getId(), color);
		}
		return color;
	}

	/**
	 * �������� ��� ����� �� �������� �����.
	 * @param codename ������� ���
	 * @return ��� �����
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
	 * �������� ��� ����� �� �������� �����. � ������, ���� ������ ���� ���,
	 * ��������� �����.
	 * 
	 * @param codename
	 *        ������� ���
	 * @return ��� �����
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
					LangModelMap.getString(codename),
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
	 * �������� ������ ���� ����� �����.
	 * @return ������ ����� ����� &lt;{@link PhysicalLinkType}&gt;
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
	 * �������� ��� ����� �� ��������� ({@link PhysicalLinkType#DEFAULT_TUNNEL}).
	 * @return ��� �����
	 * @throws ApplicationException 
	 */
	public static PhysicalLinkType getDefaultPhysicalLinkType() throws ApplicationException {
		return LinkTypeController.getPhysicalLinkType(PhysicalLinkType.DEFAULT_TUNNEL, false);
	}

	/**
	 * �������� ��� ������������� ����� ({@link PhysicalLinkType#DEFAULT_UNBOUND}).
	 * @return ��� �����
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
			e.printStackTrace();
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
