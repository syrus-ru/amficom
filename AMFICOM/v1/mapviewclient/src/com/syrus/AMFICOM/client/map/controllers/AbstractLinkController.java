/*-
 * $$Id: AbstractLinkController.java,v 1.50 2005/12/06 11:34:01 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.Characterizable;
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
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * ���������� ��������� �������� �����.
 * 
 * @version $Revision: 1.50 $, $Date: 2005/12/06 11:34:01 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
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
	public static final String ATTRIBUTE_THICKNESS = "thickness"; //$NON-NLS-1$
	/** ������� ��� �������� "����". */
	public static final String ATTRIBUTE_COLOR = "color"; //$NON-NLS-1$
	/** ������� ��� �������� "����� �����". */
	public static final String ATTRIBUTE_STYLE = "style"; //$NON-NLS-1$
	/** ������� ��� �������� "������� ����� ��� ������� �������". */
	public static final String ATTRIBUTE_ALARMED_THICKNESS = "alarmed_thickness"; //$NON-NLS-1$
	/** ������� ��� �������� "���� ��� ������� �������". */
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
	public static CharacteristicType getCharacteristicType(
			final Identifier userId, 
			final String codename,
			final String name) {
		final IdlCharacteristicTypeSort sort = IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL;
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
	 * ���������� ������� �����. ������� ������������
	 * ��������� {@link #ATTRIBUTE_THICKNESS}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param characterizable ������� �����
	 * @param size ������� �����
	 */
	public void setLineSize(final Characterizable characterizable, final int size) {
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
	 * �������� ������� �����. ������� ������������ ���������
	 * {@link #ATTRIBUTE_THICKNESS}. � ������, ���� ������ �������� � ��������
	 * ���, ������� �������� �� ��������� ({@link MapPropertiesManager#getThickness()}).
	 */
	public int getLineSize(final Characterizable characterizable) {
		final Characteristic ea = getCharacteristic(characterizable, this.thicknessCharType);
		if (ea == null) {
			return MapPropertiesManager.getThickness();
		}
		return Integer.parseInt(ea.getValue());
	}

	/**
	 * ���������� ��� �����. ����� ������������
	 * ��������� {@link #ATTRIBUTE_STYLE}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param characterizable ������� �����
	 * @param style �����
	 */
	public void setStyle(final Characterizable characterizable, final String style) {
		Characteristic attribute = getCharacteristic(characterizable, this.styleCharType);
		if (attribute == null) {
			try {
				attribute = Characteristic.createInstance(LoginManager.getUserId(),
						this.styleCharType,
						I18N.getString(MapEditorResourceKeys.NONAME),
						"1", //$NON-NLS-1$
						style,
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
		attribute.setValue(style);
	}

	/**
	 * �������� ��� �����. ����� ������������ ��������� {@link #ATTRIBUTE_STYLE}.
	 * � ������, ���� ������ �������� � �������� ���, ������� �������� ��
	 * ��������� ({@link MapPropertiesManager#getStyle()}).
	 * 
	 * @param characterizable
	 *        ������� �����
	 * @return �����
	 */
	public String getStyle(final Characterizable characterizable) {
		final Characteristic ea = getCharacteristic(characterizable, this.styleCharType);
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
	 * @param characterizable ������� �����
	 * @return �����
	 */
	public Stroke getStroke(final Characterizable characterizable) {
		final int thickness = getLineSize(characterizable);
		final String style = getStyle(characterizable);
		final String key = style + " " + thickness; //$NON-NLS-1$
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
	 * @param characterizable
	 *        ������� �����
	 * @param color
	 *        ����
	 */
	public void setColor(final Characterizable characterizable, final Color color) {
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
	 * �������� ����. ���� ������������
	 * ��������� {@link #ATTRIBUTE_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getColor()}).
	 * @param characterizable ������� �����
	 * @return ����
	 */
	public Color getColor(final Characterizable characterizable) {
		final Characteristic ea = getCharacteristic(characterizable, this.colorCharType);
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
	 * @param characterizable ������� �����
	 * @param color ����
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
	}

	/**
	 * �������� ���� ��� ������� ������� �������. ���� ������������
	 * ��������� {@link #ATTRIBUTE_ALARMED_COLOR}. � ������, ����
	 * ������ �������� � �������� ���, ������� �������� �� ���������
	 * ({@link MapPropertiesManager#getAlarmedColor()}).
	 * @param characterizable ������� �����
	 * @return ����
	 */
	public Color getAlarmedColor(final Characterizable characterizable) {
		final Characteristic ea = getCharacteristic(characterizable, this.alarmedColorCharType);
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
	 * @param characterizable ������� �����
	 * @param size ������� �����
	 */
	public void setAlarmedLineSize(final Characterizable characterizable, final int size) {
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
	}

	/**
	 * �������� ������� ����� ��� ������ ������� �������. ������� ������������
	 * ��������� {@link #ATTRIBUTE_ALARMED_THICKNESS}. � ������, ���� ������
	 * �������� � �������� ���, ������� �������� �� ��������� ({@link MapPropertiesManager#getAlarmedThickness()}).
	 * 
	 * @param characterizable
	 *        ������� �����
	 * @return ������� �����
	 */
	public int getAlarmedLineSize(final Characterizable characterizable) {
		final Characteristic ea = getCharacteristic(characterizable, this.alarmedThicknessCharType);
		if (ea == null) {
			return MapPropertiesManager.getAlarmedThickness();
		}
		return Integer.parseInt(ea.getValue());
	}
}
