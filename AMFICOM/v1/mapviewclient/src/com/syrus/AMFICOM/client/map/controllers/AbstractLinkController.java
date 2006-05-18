/*-
 * $$Id: AbstractLinkController.java,v 1.55.2.1 2006/05/18 17:50:00 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.BasicStroke;
import java.awt.Color;
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
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * ���������� ��������� �������� �����.
 * 
 * @version $Revision: 1.55.2.1 $, $Date: 2006/05/18 17:50:00 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public abstract class AbstractLinkController extends AbstractMapElementController {
	/**
	 * ����� �������� Color - ��������� ��� (���������������� ��� ������ �������������)
	 */
	Map<Identifiable, Color> colors = new HashMap<Identifiable, Color>();
	Map<Identifiable, Color> alarmedColors = new HashMap<Identifiable, Color>();

	/**
	 * ����� �������� Stroke - ��������� ��� (���������������� ��� ������ �������������)
	 */
	Map<Identifiable, BasicStroke> alarmedStrokes = new HashMap<Identifiable, BasicStroke>();
	Map<Identifiable, BasicStroke> strokes = new HashMap<Identifiable, BasicStroke>();	

	
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

		String codename = null;
		try {
			codename = ATTRIBUTE_STYLE;
			this.styleCharType = CharacteristicType.valueOf(codename);

			codename = ATTRIBUTE_THICKNESS;
			this.thicknessCharType = CharacteristicType.valueOf(codename);

			codename = ATTRIBUTE_COLOR;
			this.colorCharType = CharacteristicType.valueOf(codename);

			codename = ATTRIBUTE_ALARMED_THICKNESS;
			this.alarmedThicknessCharType = CharacteristicType.valueOf(codename);

			codename = ATTRIBUTE_ALARMED_COLOR;
			this.alarmedColorCharType = CharacteristicType.valueOf(codename);
		} catch (ApplicationException ae) {
			throw new Error("Cannot load CharacteristicType '" + codename + "'", ae);
		}
	}

	/**
	 * ����������, ����� �� ����� ��������� �������� �����.
	 * @param mapElement ������� �����
	 * @return ���� ��������� ����� ���������
	 */
	public abstract boolean isSelectionVisible(final MapElement mapElement);

	/**
	 * @deprecated should be rewritten via StorableObjectCondition 
	 */
	@Shitlet
	@Deprecated
	public static Characteristic getCharacteristic(final Characterizable characterizable, final CharacteristicType characteristicType) {
		try {
			final long d = System.nanoTime();
			final Set<Characteristic> characteristics = characterizable.getCharacteristics();
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
				characterizable.addCharacteristic(attribute);
			} catch (CreateObjectException e) {
				Log.errorMessage(e);
				return;
			} catch (IllegalObjectEntityException e) {
				Log.errorMessage(e);
				return;
			} catch (ApplicationException e) {
				Log.errorMessage(e);
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
	protected int getLineSize(final Characterizable characterizable) {
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
	 */
	public void clearCachedElement(final Characterizable characterizable) {
		this.strokes.remove(characterizable.getId());
		this.colors.remove(characterizable.getId());
		this.alarmedColors.remove(characterizable.getId());
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
	protected String getStyle(final Characterizable characterizable) {
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
	 * ���������� ����. ���� ������������ ��������� {@link #ATTRIBUTE_COLOR}. �
	 * ������, ���� ������ �������� � �������� ���, ��������� �����.
	 * 
	 * @param characterizable
	 *        ������� �����
	 * @param color
	 *        ����
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
				characterizable.addCharacteristic(attribute);
			} catch(CreateObjectException e) {
				Log.errorMessage(e);
				return;
			} catch(IllegalObjectEntityException e) {
				Log.errorMessage(e);
				return;
			} catch(ApplicationException e) {
				Log.errorMessage(e);
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
				characterizable.addCharacteristic(attribute);
			} catch (CreateObjectException e) {
				Log.errorMessage(e);
				return;
			} catch (IllegalObjectEntityException e) {
				Log.errorMessage(e);
				return;
			} catch (ApplicationException e) {
				Log.errorMessage(e);
				return;
			}
		}
		attribute.setValue(String.valueOf(color.getRGB()));
		this.alarmedColors.put(characterizable.getId(), color);
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
	 * ���������� ������� ����� ��� ������ ������� �������. ������� ������������
	 * ��������� {@link #ATTRIBUTE_ALARMED_THICKNESS}. � ������, ����
	 * ������ �������� � �������� ���, ��������� �����.
	 * @param characterizable ������� �����
	 * @param size ������� �����
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
				Log.errorMessage(e);
				return;
			} catch (IllegalObjectEntityException e) {
				Log.errorMessage(e);
				return;
			} catch (ApplicationException e) {
				Log.errorMessage(e);
				return;
			}
		}
		attribute.setValue(String.valueOf(size));
	}*/

	/**
	 * �������� ������� ����� ��� ������ ������� �������. ������� ������������
	 * ��������� {@link #ATTRIBUTE_ALARMED_THICKNESS}. � ������, ���� ������
	 * �������� � �������� ���, ������� �������� �� ��������� ({@link MapPropertiesManager#getAlarmedThickness()}).
	 * 
	 * @param characterizable
	 *        ������� �����
	 * @return ������� �����
	 */
	/*public int getAlarmedLineSize(final Characterizable characterizable) {
		final Characteristic ea = getCharacteristic(characterizable, this.alarmedThicknessCharType);
		if (ea == null) {
			return MapPropertiesManager.getAlarmedThickness();
		}
		return Integer.parseInt(ea.getValue());
	}*/
}
