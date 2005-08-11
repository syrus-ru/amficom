/**
 * $Id: AbstractLinkController.java,v 1.35 2005/08/11 13:55:41 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.ui.LineComboBox;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.util.Log;

/**
 * ���������� ��������� �������� �����.
 * @author $Author: arseniy $
 * @version $Revision: 1.35 $, $Date: 2005/08/11 13:55:41 $
 * @module mapviewclient
 */
public abstract class AbstractLinkController extends AbstractMapElementController {
	/**
	 * ����� �������� Color - ��������� ��� (���������������� ��� ������ �������������)
	 */
	Map<String, Color> colors = new HashMap<String, Color>();

	/**
	 * ����� �������� Stroke - ��������� ��� (���������������� ��� ������ �������������)
	 */
	Map<String, BasicStroke> strokes = new HashMap<String, BasicStroke>();	

	/** ������� ��� �������� "������� �����". */
	public static final String ATTRIBUTE_THICKNESS = "thickness";
	/** ������� ��� �������� "����". */
	public static final String ATTRIBUTE_COLOR = "color";
	/** ������� ��� �������� "����� �����". */
	public static final String ATTRIBUTE_STYLE = "style";
	/** ������� ��� �������� "������� ����� ��� ������� �������". */
	public static final String ATTRIBUTE_ALARMED_THICKNESS = "alarmed_thickness";
	/** ������� ��� �������� "���� ��� ������� �������". */
	public static final String ATTRIBUTE_ALARMED_COLOR = "alarmed_color";

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
	 * ����������, ����� �� ����� ��������� �������� �����.
	 * @param mapElement ������� �����
	 * @return ���� ��������� ����� ���������
	 */
	public abstract boolean isSelectionVisible(final MapElement mapElement);

	/**
	 * ����� ��� �������� �� �������� �����. ���� ������ ����
	 * �� �������, ������� �����.
	 * @param userId ������������
	 * @param codename ������� ���
	 * @return ��� ��������
	 */
	public static CharacteristicType getCharacteristicType(final Identifier userId, final String codename) {
		final CharacteristicTypeSort sort = CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL;
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
			// TODO empty
			System.err.println("Exception searching CharacteristicType. Creating new one.");
			ex.printStackTrace();
		}

		try {
			final CharacteristicType type = CharacteristicType.createInstance(userId,
					codename,
					"no description",
					LangModelMap.getString(codename),
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
	 * ����� ������� �������� ����� �� ����.
	 * 
	 * @param mapElement
	 *        ������� �����
	 * @param cType
	 *        ��� ��������
	 * @return �������
	 */
	public static Characteristic getCharacteristic(final MapElement mapElement, final CharacteristicType cType) {
		try {
			final long d = System.nanoTime();
			final Set<Characteristic> characteristics = mapElement.getCharacteristics(false);
			final long f = System.nanoTime();
			MapViewController.addTime4(f - d);
			// Log.debugMessage("mapElement.getCharacteristics() at " + (f - d) + "
			// ns", Level.INFO);
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
	 * ��������� {@link #ATTRIBUTE_THICKNESS}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param mapElement ������� �����
	 * @param size ������� �����
	 */
	public void setLineSize(final MapElement mapElement, final int size) {
		Characteristic attribute = getCharacteristic(mapElement, this.thicknessCharType);
		if (attribute == null) {
			try {
				attribute = Characteristic.createInstance(LoginManager.getUserId(),
						this.thicknessCharType,
						"name",
						"1",
						String.valueOf(size),
						mapElement,
						true,
						true);
				StorableObjectPool.flush(attribute, LoginManager.getUserId(), true);
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
	 * {@link #ATTRIBUTE_THICKNESS}. � ������, ���� ������ �������� � ��������
	 * ���, ������� �������� �� ��������� ({@link MapPropertiesManager#getThickness()}).
	 * 
	 * @param mapElement
	 *        ������� �����
	 * @return ������� �����
	 */
	public int getLineSize(final MapElement mapElement) {
		final Characteristic ea = getCharacteristic(mapElement, this.thicknessCharType);
		if (ea == null) {
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
	public void setStyle(final MapElement mapElement, final String style) {
		Characteristic attribute = getCharacteristic(mapElement, this.styleCharType);
		if (attribute == null) {
			try {
				attribute = Characteristic.createInstance(LoginManager.getUserId(),
						this.styleCharType,
						"name",
						"1",
						style,
						mapElement,
						true,
						true);
				StorableObjectPool.flush(attribute, LoginManager.getUserId(), true);
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
	 * �������� ��� �����. ����� ������������ ��������� {@link #ATTRIBUTE_STYLE}.
	 * � ������, ���� ������ �������� � �������� ���, ������� �������� ��
	 * ��������� ({@link MapPropertiesManager#getStyle()}).
	 * 
	 * @param mapElement
	 *        ������� �����
	 * @return �����
	 */
	public String getStyle(final MapElement mapElement) {
		final Characteristic ea = getCharacteristic(mapElement, this.styleCharType);
		if (ea == null) {
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
	public Stroke getStroke(final MapElement mapElement) {
		final int thickness = getLineSize(mapElement);
		final String style = getStyle(mapElement);
		final String key = style + " " + thickness;
		BasicStroke strokeForLink = this.strokes.get(key);
		if (strokeForLink == null) {
			strokeForLink = LineComboBox.getStrokeByType(style);
			final int defaultThickness = (int) strokeForLink.getLineWidth();

			if (thickness != defaultThickness)
				strokeForLink = new BasicStroke(thickness,
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
	 * ���������� ����. ���� ������������ ��������� {@link #ATTRIBUTE_COLOR}. �
	 * ������, ���� ������ �������� � �������� ���, ��������� �����.
	 * 
	 * @param mapElement
	 *        ������� �����
	 * @param color
	 *        ����
	 */
	public void setColor(final MapElement mapElement, final Color color) {
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
	 * �������� ����. ���� ������������
	 * ��������� {@link #ATTRIBUTE_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getColor()}).
	 * @param mapElement ������� �����
	 * @return ����
	 */
	public Color getColor(final MapElement mapElement) {
		final Characteristic ea = getCharacteristic(mapElement, this.colorCharType);
		if (ea == null) {
			return MapPropertiesManager.getColor();
		}

		Color color = this.colors.get(ea.getValue());
		if (color == null) {
			color = new Color(Integer.parseInt(ea.getValue()));
			this.colors.put(ea.getValue(), color);
		}
		return color;
	}

	/**
	 * ���������� ���� ��� ������� ������� �������. ���� ������������
	 * ��������� {@link #ATTRIBUTE_ALARMED_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param mapElement ������� �����
	 * @param color ����
	 */
	public void setAlarmedColor(final MapElement mapElement, final Color color) {
		Characteristic attribute = getCharacteristic(mapElement, this.alarmedColorCharType);
		if (attribute == null) {
			try {
				attribute = Characteristic.createInstance(LoginManager.getUserId(),
						this.alarmedColorCharType,
						"name",
						"1",
						String.valueOf(color.getRGB()),
						mapElement,
						true,
						true);
				StorableObjectPool.flush(attribute, LoginManager.getUserId(), true);
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
	}

	/**
	 * �������� ���� ��� ������� ������� �������. ���� ������������
	 * ��������� {@link #ATTRIBUTE_ALARMED_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getAlarmedColor()}).
	 * @param mapElement ������� �����
	 * @return ����
	 */
	public Color getAlarmedColor(final MapElement mapElement) {
		final Characteristic ea = getCharacteristic(mapElement, this.alarmedColorCharType);
		if (ea == null) {
			return MapPropertiesManager.getAlarmedColor();
		}
		Color color = this.colors.get(ea.getValue());
		if (color == null) {
			color = new Color(Integer.parseInt(ea.getValue()));
			this.colors.put(ea.getValue(), color);
		}
		return color;
	}

	/**
	 * ���������� ������� ����� ��� ������ ������� �������. ������� ������������
	 * ��������� {@link #ATTRIBUTE_ALARMED_THICKNESS}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param mapElement ������� �����
	 * @param size ������� �����
	 */
	public void setAlarmedLineSize(final MapElement mapElement, final int size) {
		Characteristic attribute = getCharacteristic(mapElement, this.alarmedThicknessCharType);
		if (attribute == null) {
			try {
				attribute = Characteristic.createInstance(LoginManager.getUserId(),
						this.alarmedThicknessCharType,
						"name",
						"1",
						String.valueOf(size),
						mapElement,
						true,
						true);
				StorableObjectPool.flush(attribute, LoginManager.getUserId(), true);
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
	 * �������� ������� ����� ��� ������ ������� �������. ������� ������������
	 * ��������� {@link #ATTRIBUTE_ALARMED_THICKNESS}. � ������, ���� ������
	 * �������� � �������� ���, ������� �������� �� ��������� ({@link MapPropertiesManager#getAlarmedThickness()}).
	 * 
	 * @param mapElement
	 *        ������� �����
	 * @return ������� �����
	 */
	public int getAlarmedLineSize(final MapElement mapElement) {
		final Characteristic ea = getCharacteristic(mapElement, this.alarmedThicknessCharType);
		if (ea == null) {
			return MapPropertiesManager.getAlarmedThickness();
		}
		return Integer.parseInt(ea.getValue());
	}
}
