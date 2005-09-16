/**
 * $Id: LinkTypeController.java,v 1.56 2005/09/16 08:19:17 krupenn Exp $
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
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
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
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.util.Log;

/**
 * ���������� ���� ��������� �������� �����.
 * @author $Author: krupenn $
 * @version $Revision: 1.56 $, $Date: 2005/09/16 08:19:17 $
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
	private static java.util.Map<String, Color> lineColors = new HashMap<String, Color>();
	/** ���-������� ������� ����� ��� ����������������� ����� �����. */
	private static java.util.Map<String, Integer> lineThickness = new HashMap<String, Integer>();
	/** ���-������� ����������� �������� ��� ����������������� ����� �����. */
	private static java.util.Map<String, IntDimension> bindDimensions = new HashMap<String, IntDimension>();

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
	public static Color getLineColor(final String codename) {
		return lineColors.get(codename);
	}

	/**
	 * �������� ������� ����� �� �������� ����� ��� ������������������ ���� �����.
	 * 
	 * @param codename
	 *        ������� ���
	 * @return �������
	 */
	public static int getLineThickness(final String codename) {
		return lineThickness.get(codename).intValue();
	}

	/**
	 * �������� ����������� �������� �� �������� ����� ��� ������������������ ����
	 * �����.
	 * 
	 * @param codename
	 *        ������� ���
	 * @return ����������� ��������
	 */
	public static IntDimension getBindDimension(final String codename) {
		return bindDimensions.get(codename);
	}

	/**
	 * ����� ������� ���� ����� �� ����.
	 * 
	 * @param linkType ��� �����
	 * @param cType ��� ��������
	 * @return �������
	 */
	public static Characteristic getCharacteristic(final PhysicalLinkType linkType, final CharacteristicType cType) {
		try {
			final long d = System.nanoTime();
			final Set<Characteristic> characteristics = linkType.getCharacteristics(false);
			final long f = System.nanoTime();
			MapViewController.addTime6(f - d);
			// Log.debugMessage("linkType.getCharacteristics() at " + (f - d) + " ns",
			// Level.INFO);
			for (final Characteristic ch : characteristics) {
				if (ch.getType().equals(cType)) {
					return ch;
				}
			}
		} catch (ApplicationException e) {
			Log.debugException(e, Level.WARNING);
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
	public void setLineSize(final Identifier userId, final PhysicalLinkType linkType, final int size) {
		Characteristic attribute = getCharacteristic(linkType, this.thicknessCharType);
		if (attribute == null) {
			try {
				attribute = Characteristic.createInstance(userId,
						this.thicknessCharType,
						"name",
						"1",
						String.valueOf(size),
						linkType,
						true,
						true);
				StorableObjectPool.flush(attribute, userId, true);
				linkType.addCharacteristic(attribute);
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
	 * �������� ������� �����. ������� ������������ ���������
	 * {@link AbstractLinkController#ATTRIBUTE_THICKNESS}. � ������, ���� ������
	 * �������� � �������� ���, ������� �������� �� ��������� ({@link MapPropertiesManager#getThickness()}).
	 * 
	 * @param linkType
	 *        ��� �����
	 * @return ������� �����
	 */
	public int getLineSize(final PhysicalLinkType linkType) {
		final Characteristic ea = getCharacteristic(linkType, this.thicknessCharType);
		if (ea == null) {
			return MapPropertiesManager.getThickness();
		}
		return Integer.parseInt(ea.getValue());
	}

	/**
	 * ���������� ����� ���� �����. ����� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_STYLE}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param linkType ��� �����
	 * @param style �����
	 */
	public void setStyle(final Identifier userId, final PhysicalLinkType linkType, final String style) {
		Characteristic attribute = getCharacteristic(linkType, this.styleCharType);
		if (attribute == null) {
			try {
				attribute = Characteristic.createInstance(userId, this.styleCharType, "name", "1", style, linkType, true, true);
				StorableObjectPool.flush(attribute, userId, true);
				linkType.addCharacteristic(attribute);
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
		attribute.setValue(style);
	}

	/**
	 * �������� ����� �����. ����� ������������ ���������
	 * {@link AbstractLinkController#ATTRIBUTE_STYLE}. � ������, ���� ������
	 * �������� � �������� ���, ������� �������� �� ��������� ({@link MapPropertiesManager#getStyle()}).
	 * 
	 * @param linkType
	 *        ��� �����
	 * @return �����
	 */
	public String getStyle(final PhysicalLinkType linkType) {
		final Characteristic ea = getCharacteristic(linkType, this.styleCharType);
		if (ea == null) {
			return MapPropertiesManager.getStyle();
		}
		return ea.getValue();
	}

	/**
	 * ���������� ���� ���� �����. ���� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param linkType ��� �����
	 * @param color ����
	 */
	public void setColor(final Identifier userId, final PhysicalLinkType linkType, final Color color) {
		Characteristic attribute = getCharacteristic(linkType, this.colorCharType);
		if (attribute == null) {
			try {
				attribute = Characteristic.createInstance(userId,
						this.colorCharType,
						"name",
						"1",
						String.valueOf(color.getRGB()),
						linkType,
						true,
						true);
				StorableObjectPool.flush(attribute, userId, true);
				linkType.addCharacteristic(attribute);
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
		colorsHolder.put(linkType.getId(), color);
	}

	/**
	 * �������� ���� ���� �����. ���� ������������ ���������
	 * {@link AbstractLinkController#ATTRIBUTE_COLOR}. � ������, ���� ������
	 * �������� � �������� ���, ������� �������� �� ��������� ({@link MapPropertiesManager#getColor()}).
	 * ��� ������ ������ ������ ��� ������� <code>linkType</code> ����������
	 * ���� ���������� � ���-������� {@link #colorsHolder} � ��� ��������� �������
	 * ������� �� ���.
	 * 
	 * @param linkType
	 *        ��� �����
	 * @return ����
	 */
	public Color getColor(final PhysicalLinkType linkType) {
		Color color = colorsHolder.get(linkType.getId());
		if (color == null) {
			final Characteristic ea = getCharacteristic(linkType, this.colorCharType);

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

			colorsHolder.put(linkType.getId(), color);
		}
		return color;
	}

	/**
	 * ���������� ���� ���� ����� ��� ������� ������� �������. ���� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_ALARMED_COLOR}. �
	 * ������, ���� ������ �������� � �������� ���, ��������� �����.
	 * 
	 * @param linkType
	 *        ��� �����
	 * @param color
	 *        ����
	 */
	public void setAlarmedColor(final Identifier userId, final PhysicalLinkType linkType, final Color color) {
		Characteristic attribute = getCharacteristic(linkType, this.alarmedColorCharType);
		if (attribute == null) {
			try {
				attribute = Characteristic.createInstance(userId,
						this.alarmedColorCharType,
						"name",
						"1",
						String.valueOf(color.getRGB()),
						linkType,
						true,
						true);
				StorableObjectPool.flush(attribute, userId, true);
				linkType.addCharacteristic(attribute);
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
		alarmedColorsHolder.put(linkType.getId(), color);
	}

	/**
	 * �������� ���� ��� ���� ����� ��� ������� ������� �������. ���� ������������
	 * ��������� {@link AbstractLinkController#ATTRIBUTE_ALARMED_COLOR}. �
	 * ������, ���� ������ �������� � �������� ���, ������� �������� �� ��������� ({@link MapPropertiesManager#getAlarmedColor()}).
	 * ��� ������ ������ ������ ��� ������� <code>linkType</code> ����������
	 * ���� ���������� � ���-������� {@link #alarmedColorsHolder} � ��� ���������
	 * ������� ������� �� ���.
	 * 
	 * @param linkType
	 *        ��� �����
	 * @return ����
	 */
	public Color getAlarmedColor(final PhysicalLinkType linkType) {
		Color color = alarmedColorsHolder.get(linkType.getId());
		if (color == null) {
			Characteristic ea = getCharacteristic(linkType, this.alarmedColorCharType);
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

			alarmedColorsHolder.put(linkType.getId(), color);
		}
		return color;
	}

	/**
	 * ���������� ������� ����� ��� ���� ����� ��� ������ ������� �������. �������
	 * ������������ ���������
	 * {@link AbstractLinkController#ATTRIBUTE_ALARMED_THICKNESS}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * 
	 * @param linkType
	 *        ��� �����
	 * @param size
	 *        ������� �����
	 */
	public void setAlarmedLineSize(final Identifier userId, final PhysicalLinkType linkType, final int size) {
		Characteristic attribute = getCharacteristic(linkType, this.alarmedThicknessCharType);
		if (attribute == null) {
			try {
				attribute = Characteristic.createInstance(userId,
						this.alarmedThicknessCharType,
						"name",
						"1",
						String.valueOf(size),
						linkType,
						true,
						true);
				StorableObjectPool.flush(attribute, userId, true);
				linkType.addCharacteristic(attribute);
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
	 * �������� ������� ����� ��� ���� ����� ��� ������ ������� �������. �������
	 * ������������ ���������
	 * {@link AbstractLinkController#ATTRIBUTE_ALARMED_THICKNESS}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ��������� ({@link MapPropertiesManager#getAlarmedThickness()}).
	 * 
	 * @param linkType
	 *        ��� �����
	 * @return ������� �����
	 */
	public int getAlarmedLineSize(final PhysicalLinkType linkType) {
		final Characteristic ea = getCharacteristic(linkType, this.alarmedThicknessCharType);
		if (ea == null) {
			return MapPropertiesManager.getAlarmedThickness();
		}
		return Integer.parseInt(ea.getValue());
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
	 * @param userId
	 *        ������������
	 * @param codename
	 *        ������� ���
	 * @return ��� �����
	 * @throws CreateObjectException
	 */
	static PhysicalLinkType getPhysicalLinkType(final MapLibrary mapLibrary,
			final Identifier userId,
			final PhysicalLinkTypeSort sort,
			final String codename,
			final boolean topological) throws ApplicationException {
		PhysicalLinkType type = getPhysicalLinkType(codename, true);
		if (type == null) {
			LinkTypeController ltc = (LinkTypeController) LinkTypeController.getInstance();

			type = PhysicalLinkType.createInstance(userId,
					sort,
					codename,
					LangModelMap.getString(codename),
					"",
					LinkTypeController.getBindDimension(codename),
					topological,
					mapLibrary.getId());

			ltc.setLineSize(userId, type, LinkTypeController.getLineThickness(codename));
			ltc.setColor(userId, type, LinkTypeController.getLineColor(codename));

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
