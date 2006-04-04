/*
 * ReflectometryTestPanel.java Created on 29.04.2004 13:10:12
 *  
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSetPackage.ParameterSetSort;
import com.syrus.AMFICOM.reflectometry.ReflectometryMeasurementParameters;
import com.syrus.AMFICOM.reflectometry.ReflectometryMeasurementSetup;
import com.syrus.AMFICOM.reflectometry.ReflectometryUtil;
import com.syrus.io.DataFormatException;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.108 $, $Date: 2006/04/04 10:53:03 $
 * @author $Author: saa $
 * @author Vladimir Dolzhenko
 * @module scheduler
 */
public final class ReflectometryTestPanel extends ParametersTestPanel implements ParametersTest {

	private static final long	serialVersionUID	= 3257004354304553266L;

	static final BigDecimal ONE_THOUSAND = new BigDecimal("1000");
	
	Map<BigDecimal, String> pulseWidthHiResMap;
	Map<BigDecimal, String> pulseWidthLowResMap;
	List<BigDecimal> resolutionList;
	Map<BigDecimal, String> traceLength;
	Map<BigDecimal, String> indexOfRefraction;
	Map<BigDecimal, String> averageCount;

	
	// UI items begin
	private JLabel			descriptionLabel;
	private JLabel			refractLabel;
	private JLabel			waveLengthLabel;
	private JLabel			countOfAverageOutLabel;
	private JLabel			resolutionLabel;
	private JLabel			maxDistanceLabel;
	private JLabel 			pulseWidthLabel;
	
	private JTextField		descriptionField;

	private JComboBox		waveLengthComboBox;
	JComboBox				refractComboBox;
	JComboBox				maxDistanceComboBox;
	JCheckBox				highResolutionCheckBox;
	JComboBox				pulseWidthHiResComboBox;
	JComboBox				pulseWidthLowResComboBox;
	JComboBox				resolutionComboBox;
	private JComboBox		averageQuantityComboBox;

	private JCheckBox		gsOptionBox;
	private JCheckBox		bcOptionBox;
	private JCheckBox		lfdOptionBox;
	//	UI items end
	
	boolean					skip;

	private Identifier		measurementPortId;
	private Identifier		meId;


	
	Identifier				setId;

	BigDecimal				maxPoints;
	
	private String oldDescription;

	private Map<ParameterType, BigDecimal>				unchangedObjects;
	private MeasurementSetup	measurementSetup;

	private Parameter[]	lastParameters;
	

	public ReflectometryTestPanel() {
		this.createGUI();
	}

	/**
	 * @todo описать что делает этот метод...
	 */
	private synchronized ParameterSet getSet() {
		ParameterSet parameterSet = null;
		synchronized (this) { // XXX: synchronize(this) внутри synchronized-метода не имеет смысла
			if (this.setId != null) {
				/*
				 * проверяем, действителен ли setId.
				 * Если нет - обнуляем его (bug 517)
				 */
				try {
					parameterSet = (ParameterSet) StorableObjectPool.getStorableObject(this.setId, true);
				} catch (ApplicationException e) {
					Log.errorMessage(e);
					// ignore the problem, leave null parameterSet
				}
				if (parameterSet == null) {
					Log.debugMessage("RTP.getSet(): failed to get parameterSet by id. Ok.", Level.WARNING);
					this.setId = null;
				}
			}
			if (this.setId != null) {
				{ // тут был 'try'
					if (!parameterSet.isChanged()) {
						/*
						 * "parameterSet not changed":
						 * перебираем все типы параметров, перечисленных в parameterSet
						 * и сравниваем соответствующие им unchangedObjects со значениями из GUI;
						 * если обнаруживается отличие в их значениях, сбрасываем setId в null
						 */
	//					System.out.println("ReflectometryTestPanel.getSet() | " + this.setId + " !set.isChanged()");
						final Parameter[] parameters = parameterSet.getParameters();
						for (int i = 0; i < parameters.length; i++) {
							final ParameterType type = parameters[i].getType();
							ParameterType typeUnchanged = type;
							Object value = null;
	
							if (type.equals(ParameterType.REF_WAVE_LENGTH)) {
								value = this.waveLengthComboBox.getSelectedItem().toString();
							} else if (type.equals(ParameterType.REF_TRACE_LENGTH)) {
								value = this.maxDistanceComboBox.getSelectedItem();
							} else if (type.equals(ParameterType.REF_RESOLUTION)) {
								value = this.resolutionComboBox.getSelectedItem();
							} else if (type.equals(ParameterType.REF_PULSE_WIDTH_HIGH_RES)) {
								if (this.highResolutionCheckBox.isSelected()) {
									value = this.pulseWidthHiResComboBox.getSelectedItem();
								} else {
									value = this.pulseWidthLowResComboBox.getSelectedItem();
									typeUnchanged = ParameterType.REF_PULSE_WIDTH_LOW_RES;
								}
							} else if (type.equals(ParameterType.REF_PULSE_WIDTH_LOW_RES)) {
								if (!this.highResolutionCheckBox.isSelected()) {
									value = this.pulseWidthLowResComboBox.getSelectedItem();
								} else {
									value = this.pulseWidthHiResComboBox.getSelectedItem();
									typeUnchanged = ParameterType.REF_PULSE_WIDTH_HIGH_RES;
								}
							} else if (type.equals(ParameterType.REF_INDEX_OF_REFRACTION)) {
								value = this.refractComboBox.getSelectedItem();
							} else if (type.equals(ParameterType.REF_AVERAGE_COUNT)) {
								value = this.averageQuantityComboBox.getSelectedItem();
							} else if (type.equals(ParameterType.REF_FLAG_GAIN_SPLICE_ON)) {
								value = Boolean.toString(this.gsOptionBox.isSelected());
							} else if (type.equals(ParameterType.REF_FLAG_LIFE_FIBER_DETECT)) {
								value = Boolean.toString(this.lfdOptionBox.isSelected());
							}
							if (value != null && !value.equals(this.unchangedObjects.get(typeUnchanged))) {
								this.setId = null;
								break;
							}
						}
					} else {
						/*
						 * "parameterSet changed":
						 * 1. сохраняем копию параметры parameterSet в lastParameters
						 * 2. если меняется REF_PULSE_WIDTH_HIGH_RES/REF_PULSE_WIDTH_LOW_RES:
						 * 2.1. выбрасываем из unchangedObjects параметр, которого больше нет
						 * 2.2. кладем (неявно, через getByteArray) в unchangedObjects новый параметр 
						 * 3. создаем новые экземпляры Parameter для тех параметров, которые изменились
						 * 4. задаем для parameterSet набор параметров, в котором некоторые параметры, возможно, изменились, а некоторые - старые
						 */
//						System.err.println("getSet(): setId != null, pars changed");
	//					System.out.println("ReflectometryTestPanel.getSet() | " + this.setId + " set.isChanged()");
						final Parameter[] parameters = parameterSet.getParameters();
						this.lastParameters = parameters.clone();
						for (int i = 0; i < parameters.length; i++) {
							final ParameterType type = parameters[i].getType();
							final BigDecimal value;
							if (type.equals(ParameterType.REF_WAVE_LENGTH)) {
								value = (BigDecimal) this.waveLengthComboBox.getSelectedItem();
							} else if (type.equals(ParameterType.REF_TRACE_LENGTH)) {
								value = (BigDecimal) this.maxDistanceComboBox.getSelectedItem();
							} else if (type.equals(ParameterType.REF_RESOLUTION)) {
								value = (BigDecimal) this.resolutionComboBox.getSelectedItem();
							} else if (type.equals(ParameterType.REF_PULSE_WIDTH_HIGH_RES)) {
								if (this.highResolutionCheckBox.isSelected()) {
									value = (BigDecimal) this.pulseWidthHiResComboBox.getSelectedItem();
								} else {
									this.unchangedObjects.remove(type);
									value = (BigDecimal) this.pulseWidthLowResComboBox.getSelectedItem();
									final ByteArray byteArray = this.getByteArray(value, ParameterType.REF_PULSE_WIDTH_LOW_RES);
									try {
										final Parameter setParameter = Parameter.createInstance(ParameterType.REF_PULSE_WIDTH_LOW_RES,
												byteArray.getBytes());
										parameters[i] = setParameter;
										continue;
									} catch (CreateObjectException e) {
										AbstractMainFrame.showErrorMessage(this, e);
										break;
									}
								}
							} else if (type.equals(ParameterType.REF_PULSE_WIDTH_LOW_RES)) {
								if (!this.highResolutionCheckBox.isSelected()) {
									value = (BigDecimal) this.pulseWidthLowResComboBox.getSelectedItem();
								} else {
									this.unchangedObjects.remove(type);
									value = (BigDecimal) this.pulseWidthHiResComboBox.getSelectedItem();
									final ByteArray byteArray = this.getByteArray(value, ParameterType.REF_PULSE_WIDTH_HIGH_RES);
									try {
										final Parameter setParameter = Parameter.createInstance(ParameterType.REF_PULSE_WIDTH_HIGH_RES,
												byteArray.getBytes());
										parameters[i] = setParameter;
										continue;
									} catch (CreateObjectException e) {
										AbstractMainFrame.showErrorMessage(this, e);
										break;
									}
								}
							} else if (type.equals(ParameterType.REF_INDEX_OF_REFRACTION)) {
								value = (BigDecimal) this.refractComboBox.getSelectedItem();
							} else if (type.equals(ParameterType.REF_AVERAGE_COUNT)) {
								value = (BigDecimal) this.averageQuantityComboBox.getSelectedItem();
							} else if (type.equals(ParameterType.REF_FLAG_GAIN_SPLICE_ON)) {
								value = this.gsOptionBox.isSelected() ? BigDecimal.ONE : BigDecimal.ZERO;
							} else if (type.equals(ParameterType.REF_FLAG_LIFE_FIBER_DETECT)) {
								value = this.lfdOptionBox.isSelected() ? BigDecimal.ONE : BigDecimal.ZERO;
							} else {
								value = null;
							}
	
							final BigDecimal bigDecimal = this.unchangedObjects.get(type);
							if (value != null && (bigDecimal == null || !bigDecimal.equals(value))) {
								final ByteArray byteArray = this.getByteArray(value, type);
								try {
									parameters[i] = Parameter.createInstance(type, byteArray.getBytes());
								} catch (CreateObjectException e) {
									AbstractMainFrame.showErrorMessage(this, e);
								}
							}
						}
						parameterSet.setParameters(parameters);
					}
				}
			} else {

//				//				maybe this is better?:
//			}
//			if (this.setId == null) {

				/*
				 * "setId == null":
				 * 1. unchangedObjects очищаем
				 * 2. создаем и заполняем новый набор параметров
				 * 2.1. при этом автоматически наполняется unchangedObjects
				 * 3. setId := id созданного набора
				 */
	//			System.out.println("ReflectometryTestPanel.getSet() | " + this.setId + " == null");
				try {
	
					this.refreshTitles();
	
					if (this.unchangedObjects == null) {
						this.unchangedObjects = new HashMap<ParameterType, BigDecimal>();
					} else {
						this.unchangedObjects.clear();
					}
	
					final Parameter[] params = new Parameter[8];
	
					ByteArray byteArray;
	
					final BigDecimal wave = (BigDecimal) this.waveLengthComboBox.getSelectedItem();
					if (wave == null) {
						throw new IllegalArgumentException(I18N.getString("Scheduler.Error.WaveLengthIsNotSet")); //$NON-NLS-1$
					}
	
					byteArray = this.getByteArray(wave, ParameterType.REF_WAVE_LENGTH);
	
					params[0] = Parameter.createInstance(ParameterType.REF_WAVE_LENGTH, byteArray.getBytes());
	
					final BigDecimal distance = (BigDecimal) this.maxDistanceComboBox.getSelectedItem();
					if (distance == null) {
						throw new IllegalArgumentException(I18N.getString("Scheduler.Error.DistanceIsNotSet")); //$NON-NLS-1$
					}
					byteArray = this.getByteArray(distance, ParameterType.REF_TRACE_LENGTH);
	
					params[1] = Parameter.createInstance(ParameterType.REF_TRACE_LENGTH, byteArray.getBytes());
	
					final BigDecimal resolution = (BigDecimal) this.resolutionComboBox.getSelectedItem();
					if (resolution == null) {
						throw new IllegalArgumentException(I18N.getString("Scheduler.Error.ResolutionIsNotSet")); //$NON-NLS-1$
					}
					byteArray = this.getByteArray(resolution, ParameterType.REF_RESOLUTION);
	
					params[2] = Parameter.createInstance(ParameterType.REF_RESOLUTION, byteArray.getBytes());
	
					if (this.highResolutionCheckBox.isSelected()) {
						final BigDecimal pulse = (BigDecimal) this.pulseWidthHiResComboBox.getSelectedItem();
						if (pulse == null) {
							throw new IllegalArgumentException(I18N.getString("Scheduler.Error.PulseWidthIsNotSet")); //$NON-NLS-1$
						}
	
						byteArray = this.getByteArray(pulse, ParameterType.REF_PULSE_WIDTH_HIGH_RES);
	
						params[3] = Parameter.createInstance(ParameterType.REF_PULSE_WIDTH_HIGH_RES, byteArray.getBytes());
					} else {
						final BigDecimal pulse = (BigDecimal) this.pulseWidthLowResComboBox.getSelectedItem();
						if (pulse == null) {
							throw new IllegalArgumentException(I18N.getString("Scheduler.Error.PulseWidthIsNotSet")); //$NON-NLS-1$
						}
	
						byteArray = this.getByteArray(pulse, ParameterType.REF_PULSE_WIDTH_LOW_RES);
	
						params[3] = Parameter.createInstance(ParameterType.REF_PULSE_WIDTH_LOW_RES, byteArray.getBytes());
					}
	
					final BigDecimal refract = (BigDecimal) this.refractComboBox.getSelectedItem();
					if (refract == null) {
						throw new IllegalArgumentException(I18N.getString("Scheduler.Error.IndexOfRefractionIsNotSet")); //$NON-NLS-1$
					}
					byteArray = this.getByteArray(refract, ParameterType.REF_INDEX_OF_REFRACTION);
	
					params[4] = Parameter.createInstance(ParameterType.REF_INDEX_OF_REFRACTION, byteArray.getBytes());
	
					final BigDecimal average = (BigDecimal) this.averageQuantityComboBox.getSelectedItem();
					if (average == null) {
						throw new IllegalArgumentException(I18N.getString("Scheduler.Error.AverageQuantityIsNotSet")); //$NON-NLS-1$
					}
	
					byteArray = this.getByteArray(average, ParameterType.REF_AVERAGE_COUNT);
	
					params[5] = Parameter.createInstance(ParameterType.REF_AVERAGE_COUNT, byteArray.getBytes());
	
					if (this.meId == null) {
						throw new IllegalArgumentException(I18N.getString(I18N.getString("Scheduler.Error.HaveNotChoosenMeasurementElement"))); //$NON-NLS-1$
					}
	
					byteArray = this.getByteArray(this.gsOptionBox.isSelected() ? BigDecimal.ONE : BigDecimal.ZERO , ParameterType.REF_FLAG_GAIN_SPLICE_ON);
					params[6] = Parameter.createInstance(ParameterType.REF_FLAG_GAIN_SPLICE_ON, byteArray.getBytes());
					
					byteArray = this.getByteArray(this.lfdOptionBox.isSelected() ? BigDecimal.ONE : BigDecimal.ZERO,
							ParameterType.REF_FLAG_LIFE_FIBER_DETECT);
					params[7] = Parameter.createInstance(ParameterType.REF_FLAG_LIFE_FIBER_DETECT, byteArray.getBytes());
	
					parameterSet = ParameterSet.createInstance(LoginManager.getUserId(),
							ParameterSetSort.SET_SORT_MEASUREMENT_PARAMETERS,
							I18N.getString("Scheduler.Text.MeasurementParameter.SetCreatedByScheduler"), params, Collections.singleton(this.meId)); //$NON-NLS-1$
					this.setId = parameterSet.getId();
	
	//				System.out.println("ReflectometryTestPanel.getSet() | newSet: " + this.setId);
//					Log.debugMessage(Log.FINEST);
				} catch (final IllegalArgumentException e) {
					Log.errorMessage(e);
					AbstractMainFrame.showErrorMessage(this, e);
				} catch (ApplicationException ae) {
					AbstractMainFrame.showErrorMessage(this, ae);
					this.schedulerModel.setBreakData();
				}
			} 
		}
		return parameterSet;
	}

	/**
	 * Заполняет this.unchangedMap по текущему состоянию GUI элементов
	 */
	private void refreshUnchangedMap() {
		if (this.unchangedObjects == null) {
			this.unchangedObjects = new HashMap<ParameterType, BigDecimal>();
		} else {
			this.unchangedObjects.clear();
		}

		final BigDecimal wave = (BigDecimal) this.waveLengthComboBox.getSelectedItem();
		this.unchangedObjects.put(ParameterType.REF_WAVE_LENGTH, wave);

		final BigDecimal distance = (BigDecimal) this.maxDistanceComboBox.getSelectedItem();
		this.unchangedObjects.put(ParameterType.REF_TRACE_LENGTH, distance);

		final BigDecimal resolution = (BigDecimal) this.resolutionComboBox.getSelectedItem();
		this.unchangedObjects.put(ParameterType.REF_RESOLUTION, resolution);

		if (this.highResolutionCheckBox.isSelected()) {
			final BigDecimal pulse = (BigDecimal) this.pulseWidthHiResComboBox.getSelectedItem();
			this.unchangedObjects.put(ParameterType.REF_PULSE_WIDTH_HIGH_RES, pulse);
		} else {
			final BigDecimal pulse = (BigDecimal) this.pulseWidthLowResComboBox.getSelectedItem();
			this.unchangedObjects.put(ParameterType.REF_PULSE_WIDTH_LOW_RES, pulse);
		}

		final BigDecimal refract = (BigDecimal) this.refractComboBox.getSelectedItem();
		this.unchangedObjects.put(ParameterType.REF_INDEX_OF_REFRACTION, refract);

		final BigDecimal average = (BigDecimal) this.averageQuantityComboBox.getSelectedItem();
		this.unchangedObjects.put(ParameterType.REF_AVERAGE_COUNT, average);
		
		final BigDecimal gs = this.gsOptionBox.isSelected() ? BigDecimal.ONE : BigDecimal.ZERO;
		this.unchangedObjects.put(ParameterType.REF_FLAG_GAIN_SPLICE_ON, gs);
		
		final BigDecimal lfd = this.lfdOptionBox.isSelected() ? BigDecimal.ONE : BigDecimal.ZERO;
		this.unchangedObjects.put(ParameterType.REF_FLAG_LIFE_FIBER_DETECT, lfd);
	}

	private void refreshTitles() {
		this.refractLabel.setText(ParameterType.REF_INDEX_OF_REFRACTION.getDescription()
				+ this.getUnit(ParameterType.REF_INDEX_OF_REFRACTION));
		this.waveLengthLabel.setText(ParameterType.REF_WAVE_LENGTH.getDescription()
				+ this.getUnit(ParameterType.REF_WAVE_LENGTH));
		this.countOfAverageOutLabel.setText(ParameterType.REF_AVERAGE_COUNT.getDescription()
				+ this.getUnit(ParameterType.REF_AVERAGE_COUNT));
//		this.pulseWidthCheckBox.setText(ParameterType.REF_PULSE_WIDTH_HIGH_RES.getDescription()
//				+ this.getUnit(ParameterType.REF_PULSE_WIDTH_HIGH_RES));
		this.pulseWidthLabel.setText(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.PulseWidth") 
			+ this.getUnit(ParameterType.REF_PULSE_WIDTH_HIGH_RES));
//		this.pulseWidthHiResLabel.setText(ParameterType.REF_PULSE_WIDTH_HIGH_RES.getDescription()
//				+ this.getUnit(ParameterType.REF_PULSE_WIDTH_HIGH_RES));
//		this.pulseWidthLowResLabel.setText(ParameterType.REF_PULSE_WIDTH_LOW_RES.getDescription()
//				+ this.getUnit(ParameterType.REF_PULSE_WIDTH_LOW_RES));
		this.resolutionLabel.setText(ParameterType.REF_RESOLUTION.getDescription()
				+ this.getUnit(ParameterType.REF_RESOLUTION));
		this.maxDistanceLabel.setText(ParameterType.REF_TRACE_LENGTH.getDescription()
				+ this.getUnit(ParameterType.REF_TRACE_LENGTH));

		this.gsOptionBox.setVisible(true);
		this.lfdOptionBox.setVisible(true);
	}

	private String getUnit(final ParameterType parameterType) {
		String name = parameterType.getMeasurementUnit().getName();
		return name.trim().length() > 0 ? ',' + name : "";
	}

	/**
	 * Кроме всего прочего, изменяет unchangedObjects
	 */
	private ByteArray getByteArray(final BigDecimal value, final ParameterType parameterType) {

		this.unchangedObjects.put(parameterType, value);

		ByteArray byteArray = null;
		final DataType dataType = parameterType.getDataType();
		switch (dataType) {
			case INTEGER:
				byteArray = new ByteArray(value.intValue());
				break;
			case DOUBLE:
				byteArray = new ByteArray(value.doubleValue());
				break;
			case LONG:
				byteArray = new ByteArray(value.longValue());
				break;
			case BOOLEAN:
				byteArray = new ByteArray(value.equals(BigDecimal.ONE));
				break;
			default:
				Log.errorMessage("Illegal data type '" + dataType.getCodename() + "'/" + dataType.ordinal()
						+ " for parameter type '" + parameterType.getCodename() + "'");
		}
		return byteArray;
	}

	@Override
	public void setEnableEditing(final boolean enable) {
		this.descriptionLabel.setEnabled(enable);
		this.refractLabel.setEnabled(enable);
		this.waveLengthLabel.setEnabled(enable);
		this.countOfAverageOutLabel.setEnabled(enable);
		this.resolutionLabel.setEnabled(enable);
		this.maxDistanceLabel.setEnabled(enable);
		this.pulseWidthLabel.setEnabled(enable);

		this.descriptionField.setEnabled(enable);
		this.refractComboBox.setEnabled(enable);
		this.waveLengthComboBox.setEnabled(enable);
		this.maxDistanceComboBox.setEnabled(enable);
		this.highResolutionCheckBox.setEnabled(enable);
		this.pulseWidthHiResComboBox.setEnabled(enable);
		this.pulseWidthLowResComboBox.setEnabled(enable);
		this.resolutionComboBox.setEnabled(enable);
		this.averageQuantityComboBox.setEnabled(enable);

		this.gsOptionBox.setEnabled(enable);
		this.bcOptionBox.setEnabled(enable);
		this.lfdOptionBox.setEnabled(enable);
	}

	@Override
	public void setMonitoredElement(final MonitoredElement me) {
		synchronized (this) {
			this.skip = true;
			this.meId = me.getId();
			final Identifier measurementPortId1 = me.getMeasurementPortId();
			if (this.measurementPortId == null || !this.measurementPortId.equals(measurementPortId1)) {
				try {
					this.measurementPortId = measurementPortId1;
	
					this.refreshTitles();
	
					final MeasurementPort port = 
						StorableObjectPool.getStorableObject(measurementPortId1, true);
					final Identifier portTypeId = port.getType().getId();
					final LinkedIdsCondition linkedIdsCondition = 
						new LinkedIdsCondition(portTypeId,
							ObjectEntities.CHARACTERISTIC_CODE);
					final Set<Characteristic> characteristics = 
						StorableObjectPool.getStorableObjectsByCondition(linkedIdsCondition, true);
					if (this.traceLength == null) {
						this.traceLength = new HashMap<BigDecimal, String>();
					} else {
						this.traceLength.clear();
					}
	
					if (this.indexOfRefraction == null) {
						this.indexOfRefraction = new HashMap<BigDecimal, String>();
					} else {
						this.indexOfRefraction.clear();
					}
	
					if (this.averageCount == null) {
						this.averageCount = new HashMap<BigDecimal, String>();
					} else {
						this.averageCount.clear();
					}
	
					if (this.resolutionList == null) {
						this.resolutionList = new LinkedList<BigDecimal>();
					} else {
						this.resolutionList.clear();
					}
	
					if (this.pulseWidthHiResMap == null) {
						this.pulseWidthHiResMap = new HashMap<BigDecimal, String>();
					} else {
						this.pulseWidthHiResMap.clear();
					}
	
					if (this.pulseWidthLowResMap == null) {
						this.pulseWidthLowResMap = new HashMap<BigDecimal, String>();
					} else {
						this.pulseWidthLowResMap.clear();
					}
	
					Pattern pattern = Pattern.compile(CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + "(\\d+)(" //$NON-NLS-1$
							+ CharacteristicTypeCodenames.TRACE_LENGTH_SUFFIX + "|" //$NON-NLS-1$
							+ CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES_SUFFIX + "|" //$NON-NLS-1$
							+ CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES_SUFFIX + "|" //$NON-NLS-1$
							+ CharacteristicTypeCodenames.TRACE_INDEX_OF_REFRACTION_SUFFIX + "|" //$NON-NLS-1$
							+ CharacteristicTypeCodenames.TRACE_AVERAGE_COUNT_SUFFIX + ")"); //$NON-NLS-1$
	
//					Log.debugMessage("characteristics.size() " + characteristics.size(), Level.FINEST);
	
					for (final Characteristic characteristic : characteristics) {
						final StorableObjectType type = characteristic.getType();
//						 System.out.println("characteristicType is " +					 type.getId());
						final String codename = type.getCodename();
						final String value = characteristic.getValue();
//						 System.out.println("codename is '" + codename + "', valueis: " + value);
	
						if (codename.equals(CharacteristicTypeCodenames.TRACE_WAVELENGTH)) {
							final String[] values = value.split("\\s+"); //$NON-NLS-1$
							final BigDecimal[] bgValues = new BigDecimal[values.length];
							for(int i = 0; i < values.length; i++) {
								bgValues[i] = new BigDecimal(values[i]);
							}
							Arrays.sort(bgValues);
							this.waveLengthComboBox.removeAllItems();
							for (int i = 0; i < bgValues.length; i++) {
								this.waveLengthComboBox.addItem(bgValues[i]);
							}
	
						} else if (codename.equals(CharacteristicTypeCodenames.TRACE_RESOLUTION)) {
							final String[] values = value.split("\\s+"); //$NON-NLS-1$							
							final BigDecimal[] bgValues = new BigDecimal[values.length];
							for(int i = 0; i < values.length; i++) {
								bgValues[i] = new BigDecimal(values[i]);
							}
							Arrays.sort(bgValues);
							this.resolutionComboBox.removeAllItems();
							for (int i = 0; i < bgValues.length; i++) {
								this.resolutionList.add(bgValues[i]);
								this.resolutionComboBox.addItem(bgValues[i]);
							}
	
						} else if (codename.equals(CharacteristicTypeCodenames.TRACE_MAXPOINTS)) {
							this.maxPoints = new BigDecimal(value);
						}
						// else if (codename.equals(CHARACTER_MAX_REFRACTION)) {
						// try {
						// this.maxIndexOfRefraction = Double.parseDouble(value);
						// } catch (NumberFormatException nfe) {
						// this.maxIndexOfRefraction = 0.0;
						// }
						//
						// } else if (codename.equals(CHARACTER_MIN_REFRACTION)) {
						// try {
						// this.minIndexOfRefraction = Double.parseDouble(value);
						// } catch (NumberFormatException nfe) {
						// this.minIndexOfRefraction = 0.0;
						// }
						// }
						else {
							final Matcher matcher = pattern.matcher(codename);
							if (matcher.find()) {
								BigDecimal waveLength = null;
								String suffix = null;
								for (int j = 0; j <= matcher.groupCount(); j++) {
									final String substring = codename.substring(matcher.start(j), matcher.end(j));
									// System.out.println("j:" + j + "\t" +
									// substring);
									switch (j) {
										case 1:
											waveLength = new BigDecimal(substring);
											break;
										case 2:
											suffix = substring;
											break;
									}
								}
	
//								Log.debugMessage("waveLength: " + waveLength, Log.FINEST);
//								Log.debugMessage("suffix: " + suffix, Log.FINEST);
								if ((waveLength != null) && (suffix != null)) {
									Map<BigDecimal, String> map = null;
									if (suffix.equals(CharacteristicTypeCodenames.TRACE_LENGTH_SUFFIX)) {
										map = this.traceLength;
									} else if (suffix.equals(CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES_SUFFIX)) {
										map = this.pulseWidthHiResMap;
									} else if (suffix.equals(CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES_SUFFIX)) {
										map = this.pulseWidthLowResMap;
									} else if (suffix.equals(CharacteristicTypeCodenames.TRACE_INDEX_OF_REFRACTION_SUFFIX)) {
										map = this.indexOfRefraction;
									} else if (suffix.equals(CharacteristicTypeCodenames.TRACE_AVERAGE_COUNT_SUFFIX)) {
										map = this.averageCount;
									}
									if (map != null) {
										map.put(waveLength, value);
									}
								}
							}
						}
					}
	
					final Object selectedItem = this.waveLengthComboBox.getSelectedItem();
					final BigDecimal wavelength = (BigDecimal) (selectedItem != null ? this.waveLengthComboBox.getSelectedItem() : null);
					if (wavelength != null) {
						// System.out.println("wavelength is " + wavelength);
						if (this.traceLength != null) {
							final String value = this.traceLength.get(wavelength);
							if (value == null) {
								throw new ObjectNotFoundException(I18N.getString("Scheduler.Error.TraceLengthValueNotFound")); //$NON-NLS-1$
							}
							final String[] values = value.split("\\s+"); //$NON-NLS-1$
							final BigDecimal[] bgValues = new BigDecimal[values.length];
							for(int i = 0; i < values.length; i++) {
								bgValues[i] = new BigDecimal(values[i]);
							}
							Arrays.sort(bgValues);
							this.maxDistanceComboBox.removeAllItems();
							for (int i = 0; i < bgValues.length; i++) {
								this.maxDistanceComboBox.addItem(bgValues[i]);
							}
						} else {
							// TODO throw exception
						}
	
						if (this.pulseWidthHiResMap != null) {
							final String value = this.pulseWidthHiResMap.get(wavelength);
							if (value != null) {
								final String[] values = value.split("\\s+"); //$NON-NLS-1$
								final BigDecimal[] bgValues = new BigDecimal[values.length];
								for(int i = 0; i < values.length; i++) {
									bgValues[i] = new BigDecimal(values[i]);
								}
								Arrays.sort(bgValues);
								this.pulseWidthHiResComboBox.removeAllItems();
								for (int i = 0; i < bgValues.length; i++) {
									this.pulseWidthHiResComboBox.addItem(bgValues[i]);
								}
							} else {
								// TODO throw exception
							}
						} else {
							// TODO throw exception
						}
	
						if (this.pulseWidthLowResMap != null) {
//							Log.debugMessage("this.pulseWidthLowResMap != null ", Log.FINEST);
							final String value = this.pulseWidthLowResMap.get(wavelength);
//							Log.debugMessage("pulseWidthLowResMap value: " + value, Log.FINEST);
							if (value != null) {
								final String[] values = value.split("\\s+"); //$NON-NLS-1$
								final BigDecimal[] bgValues = new BigDecimal[values.length];
								for(int i = 0; i < values.length; i++) {
									bgValues[i] = new BigDecimal(values[i]);
								}
								Arrays.sort(bgValues);
								this.pulseWidthLowResComboBox.removeAllItems();
								for (int i = 0; i < bgValues.length; i++) {
									this.pulseWidthLowResComboBox.addItem(bgValues[i]);
								}
							} else {
								// TODO throw exception
							}
						} else {
							// TODO throw exception
						}
	
						if (this.indexOfRefraction != null) {
							final String value = this.indexOfRefraction.get(wavelength);
							if (value != null) {
								final String[] values = value.split("\\s+"); //$NON-NLS-1$
								final BigDecimal[] bgValues = new BigDecimal[values.length];
								for(int i = 0; i < values.length; i++) {
									bgValues[i] = new BigDecimal(values[i]);
								}
								Arrays.sort(bgValues);
//								this.refractTextField.setText(bgValues[0].toString());
								this.refractComboBox.removeAllItems();
								for (int i = 0; i < bgValues.length; i++) {
									this.refractComboBox.addItem(bgValues[i]);
								}
							} else {
								// TODO throw exception
							}
						} else {
							// TODO throw exception
						}
	
						if (this.averageCount != null) {
							final String value = this.averageCount.get(wavelength);
							if (value != null) {
								final String[] values = value.split("\\s+"); //$NON-NLS-1$
								final BigDecimal[] bgValues = new BigDecimal[values.length];
								for(int i = 0; i < values.length; i++) {
									bgValues[i] = new BigDecimal(values[i]);
								}
								Arrays.sort(bgValues);
								this.averageQuantityComboBox.removeAllItems();
								for (int i = 0; i < bgValues.length; i++) {
									this.averageQuantityComboBox.addItem(bgValues[i]);
								}
							} else {
								// TODO throw exception
							}
						} else {
							// TODO throw exception
						}
					} else {
						System.err.println(I18N.getString("Scheduler.Error.SelectedWaveLengthIsNull")); //$NON-NLS-1$
					}
	
				} catch (final ApplicationException ae) {
					AbstractMainFrame.showErrorMessage(this, ae);
				}
			}
			this.skip = false;
		}
	}

	public void setMeasurementSetup(final MeasurementSetup measurementSetup) {
		this.measurementSetup = measurementSetup;
		this.setSet(measurementSetup != null ? measurementSetup.getParameterSet() : null);
		this.setDescription(measurementSetup != null ? measurementSetup.getDescription() : "");
	}
	
	public MeasurementSetup getMeasurementSetup() throws CreateObjectException {
		final MeasurementSetup measurementSetup1 = this.schedulerModel.createMeasurementSetup(this.getSet(), 
				getDescription());
		updateMeasurementSetupDuration(measurementSetup1);
		return measurementSetup1;
	}

	/**
	 * Корректирует в шаблоне продолжительность одного измерения согласно
	 * параметрам измерения этого шаблона.
	 * FIXME: бросает CreateObjectException - по всей видимости,
	 * это не самый подходходящий тип исключения. Однако он оставлен здесь по
	 * историческим соображениями, а также из-за полного отсутствия документации
	 * по исключениями группы ApplicationException.
	 * @param measurementSetup1 шаблон
	 * @throws CreateObjectException ошибка при обработке шаблона
	 */
	private static void updateMeasurementSetupDuration(MeasurementSetup measurementSetup1)
	throws CreateObjectException {
		try {
			final ReflectometryMeasurementParameters measurementParameters =
				new ReflectometryMeasurementSetup(measurementSetup1).getMeasurementParameters();
			measurementSetup1.setMeasurementDuration((long)(1000 * ReflectometryUtil.getUpperEstimatedAgentTestTime(measurementParameters)));
			Log.debugMessage(measurementSetup1.getMeasurementDuration()/1000 + " sec", Log.DEBUGLEVEL09);
		} catch (DataFormatException e) {
			throw new CreateObjectException(e);
		}
	}

	synchronized void refreshTestsSet() {
		if (this.skip) {
			return;
		}

		final ParameterSet parameterSet = this.getSet();
		final String description = this.getDescription();
		final boolean descriptionChanged = !description.equals(this.oldDescription);
		if (parameterSet != null && (parameterSet.isChanged()
					|| descriptionChanged)) {
				Log.debugMessage(Log.DEBUGLEVEL10);
				this.oldDescription = description;
				final Set<Identifier> selectedTestIds = this.schedulerModel.getSelectedTestIds();
				if (!selectedTestIds.isEmpty()) { 
					try {
						final Set<Test> tests = this.schedulerModel.getSelectedTests();
						final Map<Identifier, Identifier> unchangedMeasurementSetupNewMap = new HashMap<Identifier, Identifier>();
						for (final Test test : tests) {
							if (test.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
								final Set<Identifier> measurementSetupIds = test.getMeasurementSetupIds();
								if (measurementSetupIds.size() == 1) {
									final MeasurementSetup baseMeasurementSetup = 
										StorableObjectPool.getStorableObject(
											measurementSetupIds.iterator().next(),
											true);
									
									if (baseMeasurementSetup.isChanged()) {
										baseMeasurementSetup.setParameterSet(parameterSet);
										updateMeasurementSetupDuration(baseMeasurementSetup);

										this.skip = true;

										try { 
											this.schedulerModel.changeMeasurementSetup(baseMeasurementSetup);
											baseMeasurementSetup.setDescription(description);
											this.testParametersPanel.refreshMeasurementSetup(baseMeasurementSetup);
										} catch (final ApplicationException e) {							
											if (this.lastParameters != null) {
												baseMeasurementSetup.getParameterSet().setParameters(this.lastParameters);
												this.refrestParameterSet(this.lastParameters);
												updateMeasurementSetupDuration(baseMeasurementSetup);
												this.schedulerModel.changeMeasurementSetup(baseMeasurementSetup);
											}
											AbstractMainFrame.showErrorMessage(this, e);
										}
										this.skip = false;

									} else {
										Identifier measurementSetupId = 
											unchangedMeasurementSetupNewMap.get(baseMeasurementSetup.getId());
		
										if (measurementSetupId == null) {			
											final MeasurementSetup measurementSetup1 = this.getMeasurementSetup();
											measurementSetup1.setCriteriaSet(baseMeasurementSetup.getCriteriaSet());
											measurementSetup1.setThresholdSet(baseMeasurementSetup.getThresholdSet());
											measurementSetup1.setEtalon(baseMeasurementSetup.getEtalon());
											measurementSetup1.setMeasurementTypes(baseMeasurementSetup.getMeasurementTypes());											
											this.skip = true;
											this.testParametersPanel.setMeasurementSetup(measurementSetup1);
											this.skip = false;
											measurementSetupId = measurementSetup1.getId();
											unchangedMeasurementSetupNewMap.put(baseMeasurementSetup.getId(), measurementSetupId);
											this.schedulerModel.changeMeasurementSetup(measurementSetup1);
										}
									}
								} else {
									// TODO PROBLEM ?
								}
							}
						}
		
					} catch (final ApplicationException e) {
						AbstractMainFrame.showErrorMessage(this, e);
					}
			} else {
				if (this.measurementSetup != null) {
					if (this.measurementSetup.isChanged()) {
						this.measurementSetup.setParameterSet(parameterSet);
						this.measurementSetup.setDescription(description);
						this.testParametersPanel.refreshMeasurementSetup(this.measurementSetup);
					} else {
						{
							final MeasurementSetup measurementSetup1;
							try {
								measurementSetup1 = MeasurementSetup.createInstance(LoginManager.getUserId(),
									parameterSet,
									this.measurementSetup.getCriteriaSet(),
									this.measurementSetup.getThresholdSet(),
									this.measurementSetup.getEtalon(),
									description,
									this.measurementSetup.getMeasurementDuration(),
									this.measurementSetup.getMonitoredElementIds(),
									this.measurementSetup.getMeasurementTypes());
									this.skip = true;
									this.testParametersPanel.setMeasurementSetup(measurementSetup1);
									this.skip = false;
								this.measurementSetup = measurementSetup1;
							} catch (final CreateObjectException e) {
								AbstractMainFrame.showErrorMessage(I18N.getString("Scheduler.Error.CannotCreateMeasurementSetup"));
							}
		
						}
//						this.measurementSetup = null;

					}
				}
			}
		}
	}
	
	private final String getDescription() {
		final SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);

		final String description = this.descriptionField.getText();
		return description.trim().length() == 0 ?
//				commented according to bug 283
//				I18N.getString("Scheduler.Text.Scheduler.CreatedByScheduler") + 
				sdf.format(new Date()) 
				: description;
	}
	
	private void setDescription(final String description) {
		this.skip = true;
		this.descriptionField.setText(description);
		this.skip = false;
	}

	/**
	 * 1. Если this.skip, то return
	 * 2. Если this.setId имеет тот же id, что и данный set, то return
	 * 3. очищает this.unchangedObjects
	 * 4. выставляет setIdm GUI элементы и this.unchangedMap по данному set
	 * @param set данный set
	 */
	private void setSet(final ParameterSet set) {
//		System.out.println("ReflectometryTestPanel.setSet() | 1 " + (set != null ? set.getId() : null));
		if (this.skip) {
			return;
		}		
		
		if (this.setId != null && set != null && this.setId.equals(set)) {
			this.skip = false;
			return;
		}

//		System.out.println("ReflectometryTestPanel.setSet() | " + (set != null ? set.getId() : null));
		
		this.skip = true;
		if (this.unchangedObjects == null) {
			this.unchangedObjects = new HashMap<ParameterType, BigDecimal>();
		} else {
			this.unchangedObjects.clear();
		}
		
		if (set == null) {
			this.setId = null;
			this.skip = false;
			return;
		}

		this.refrestParameterSet(set.getParameters());
		
		this.setId = set.getId();
		this.skip = false;
	}

	/**
	 * Выставляет GUI элементы и this.unchangedMap
	 * в соответствии с данными параметрами.
	 * @param setParameters данные параметры
	 */
	private void refrestParameterSet(final Parameter[] setParameters ) {
		this.gsOptionBox.setSelected(false);
		this.lfdOptionBox.setSelected(false);

		for (int i = 0; i < setParameters.length; i++) {
			final ParameterType parameterType = setParameters[i].getType();
//			Log.debugMessage("codename " + codename , Level.FINE);
			if (parameterType.equals(ParameterType.REF_TRACE_LENGTH)) {
				this.selectCBValue(this.maxDistanceComboBox, new BigDecimal(setParameters[i].getStringValue()));
			} else if (parameterType.equals(ParameterType.REF_FLAG_GAIN_SPLICE_ON)) {
				try {
					final boolean b = new ByteArray(setParameters[i].getValue()).toBoolean();
//					Log.debugMessage("TRACE_FLAG_GAIN_SPLICE " + b, Log.FINEST);
					this.gsOptionBox.setSelected(b);

				} catch (final IOException e) {
					AbstractMainFrame.showErrorMessage(this, e);
					return;
				}
			} else if (parameterType.equals(ParameterType.REF_FLAG_LIFE_FIBER_DETECT)) {
				try {
					final boolean b = new ByteArray(setParameters[i].getValue()).toBoolean();
//					Log.debugMessage("TRACE_FLAG_LIVE_FIBER_DETECT " + b, Log.FINEST);
					this.lfdOptionBox.setSelected(b);

				} catch (final IOException e) {
					AbstractMainFrame.showErrorMessage(this, e);
					return;
				}
			}
		}

		for (int i = 0; i < setParameters.length; i++) {
			final ParameterType parameterType = setParameters[i].getType();
			final String stringValue = setParameters[i].getStringValue();
			Log.debugMessage(stringValue, Log.DEBUGLEVEL10);
			if (parameterType.equals(ParameterType.REF_INDEX_OF_REFRACTION)) {
				this.selectCBValue(this.refractComboBox, new BigDecimal(stringValue));
			} else if (parameterType.equals(ParameterType.REF_WAVE_LENGTH)) {
				this.selectCBValue(this.waveLengthComboBox, new BigDecimal(stringValue));
			} else if (parameterType.equals(ParameterType.REF_AVERAGE_COUNT)) {
				this.selectCBValue(this.averageQuantityComboBox, new BigDecimal(stringValue));
			} else if (parameterType.equals(ParameterType.REF_RESOLUTION)) {
				this.selectCBValue(this.resolutionComboBox, new BigDecimal(stringValue));
			} else if (parameterType.equals(ParameterType.REF_PULSE_WIDTH_HIGH_RES)) {
				this.selectCBValue(this.pulseWidthHiResComboBox, new BigDecimal(stringValue));
				// NB: highResolutionCheckBox.doClick() is not suitable:
				// it invokes fire update listeners only if it is enabled.
				// so, we process the update by ourselves.
				this.highResolutionCheckBox.setSelected(true);
				handleStateChangeOfHiResComboBox();
			} else if (parameterType.equals(ParameterType.REF_PULSE_WIDTH_LOW_RES)) {
				this.selectCBValue(this.pulseWidthLowResComboBox, new BigDecimal(stringValue));
				this.highResolutionCheckBox.setSelected(false);
				handleStateChangeOfHiResComboBox();
			}
		}
		this.refreshUnchangedMap();
	}

	void handleStateChangeOfHiResComboBox() {
		boolean b = this.highResolutionCheckBox.isSelected();
		ReflectometryTestPanel.this.pulseWidthHiResComboBox.setVisible(b);
		ReflectometryTestPanel.this.pulseWidthLowResComboBox.setVisible(!b);
	}

	private void createUIItems() {
		this.descriptionField = new JTextField(128);

		this.refractComboBox = new JComboBox();
		this.waveLengthComboBox = new JComboBox();
		this.maxDistanceComboBox = new JComboBox();
		this.highResolutionCheckBox = new JCheckBox(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.HighResolution"));
		this.pulseWidthHiResComboBox = new JComboBox();
		this.pulseWidthHiResComboBox.setMaximumRowCount(15);
		this.pulseWidthLowResComboBox = new JComboBox();
		this.pulseWidthLowResComboBox.setMaximumRowCount(15);
		this.resolutionComboBox = new JComboBox();
		this.averageQuantityComboBox = new JComboBox();

		this.descriptionLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.Description"));
		this.refractLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.IndexOfRefraction")); //$NON-NLS-1$
		this.waveLengthLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.WaveLength")); //$NON-NLS-1$
		this.countOfAverageOutLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.AverageQuantity")); //$NON-NLS-1$
		this.resolutionLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.Resolution")); //$NON-NLS-1$
		this.maxDistanceLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.Distance")); //$NON-NLS-1$
		this.pulseWidthLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.PulseWidth")); //$NON-NLS-1$

		this.gsOptionBox = new JCheckBox(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.GainSplice"));
		this.bcOptionBox = new JCheckBox(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.BoxCar"));
		this.lfdOptionBox = new JCheckBox(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.LiveFiberDetect"));

		this.maxDistanceComboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				synchronized (ReflectometryTestPanel.this) {
					final JComboBox comboBox = (JComboBox) e.getSource();
					final Object selectedItem = comboBox.getSelectedItem();
					if (selectedItem == null) {
						return;
					}
					synchronized (ReflectometryTestPanel.this) {
						/* 1000 m is 1 km */
//						double maxDistance = 1000.0 * Double.parseDouble(selectedItem.toString());						
						final BigDecimal maxDistance = ((BigDecimal)selectedItem).multiply(ONE_THOUSAND);
						final BigDecimal resolutionItem = (BigDecimal) ReflectometryTestPanel.this.resolutionComboBox.getSelectedItem();
						boolean found = false;
						boolean firstItem = true;
						boolean firstItemGreater = false;
						
						final boolean previousSkipState = ReflectometryTestPanel.this.skip;
						
						ReflectometryTestPanel.this.skip = true;
						ReflectometryTestPanel.this.resolutionComboBox.removeAllItems();
						for (final BigDecimal resolution : ReflectometryTestPanel.this.resolutionList) {
							final BigDecimal decimal = maxDistance.divide(resolution);
							if (decimal.compareTo(ReflectometryTestPanel.this.maxPoints) < 0) {
								if (resolution.equals(resolutionItem)) {
									found = true;
								} else {
									if (firstItem) {
										if (resolutionItem.compareTo(resolution) < 0) {
											firstItemGreater = true;
										}
									}
								}
								
								firstItem = false;
								
								ReflectometryTestPanel.this.resolutionComboBox.addItem(resolution);
							}
						}
						
						if (found) {
							ReflectometryTestPanel.this.resolutionComboBox.setSelectedItem(resolutionItem);
						} else {
							if (firstItemGreater) {
								ReflectometryTestPanel.this.resolutionComboBox.setSelectedIndex(0);
							} else {
								ReflectometryTestPanel.this.resolutionComboBox.setSelectedIndex(comboBox.getItemCount() - 1);
							}
						}

						ReflectometryTestPanel.this.skip = previousSkipState;
					}
				}
			}
		});

		this.highResolutionCheckBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ReflectometryTestPanel.this.handleStateChangeOfHiResComboBox();
			}
		});

		final ActionListener changeActionListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!ReflectometryTestPanel.this.skip) {
					synchronized(ReflectometryTestPanel.this) {
						Log.debugMessage("ReflectometryTestPanel.ActionListener.actionPerformed | ", Log.DEBUGLEVEL10);
						ReflectometryTestPanel.this.refreshTestsSet();
					}
				}
			}
		};

		final FocusListener textFieldFocusListener = new FocusAdapter() {
			@Override
			public void focusLost(final FocusEvent e) {
				changeActionListener.actionPerformed(null);
			}
		};

		this.descriptionField.addFocusListener(textFieldFocusListener);

		this.descriptionField.addActionListener(changeActionListener);
		this.refractComboBox.addActionListener(changeActionListener);
		this.waveLengthComboBox.addActionListener(changeActionListener);
		this.averageQuantityComboBox.addActionListener(changeActionListener);
		this.highResolutionCheckBox.addActionListener(changeActionListener);
		this.pulseWidthHiResComboBox.addActionListener(changeActionListener);
		this.pulseWidthLowResComboBox.addActionListener(changeActionListener);
		this.resolutionComboBox.addActionListener(changeActionListener);
		this.maxDistanceComboBox.addActionListener(changeActionListener);
		this.gsOptionBox.addActionListener(changeActionListener);
		this.lfdOptionBox.addActionListener(changeActionListener);
	}

	private void createGUI() {
		this.skip = true;
		this.createUIItems();
		this.setLayout(new GridBagLayout());		

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(this.descriptionLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(this.descriptionField, gbc);
		
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(this.refractLabel, gbc);
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(this.refractComboBox, gbc);

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(this.waveLengthLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		this.add(this.waveLengthComboBox, gbc);

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(this.maxDistanceLabel, gbc);
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(this.maxDistanceComboBox, gbc);
		
		gbc.weightx = 1.0;
		this.add(this.highResolutionCheckBox, gbc);

		gbc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(this.pulseWidthLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		this.add(this.pulseWidthHiResComboBox, gbc);

		this.add(this.pulseWidthLowResComboBox, gbc);

		this.highResolutionCheckBox.doClick();

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(this.countOfAverageOutLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		this.add(this.averageQuantityComboBox, gbc);

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(this.resolutionLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		this.add(this.resolutionComboBox, gbc);
		
		gbc.weightx = 1.0;
//		gbc.gridwidth = GridBagConstraints.RELATIVE;
//		this.add(new JLabel(I18N.getString("Scheduler.Text.GainSplice")), gbc); //$NON-NLS-1$
//		gbc.weightx = 0.0;
//		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(this.gsOptionBox, gbc);

		gbc.weightx = 1.0;
//		this.add(this.bcOptionBox, gbc);
		this.bcOptionBox.setSelected(false);
		this.bcOptionBox.setEnabled(false);

		
		gbc.weightx = 1.0;
//		gbc.gridwidth = GridBagConstraints.RELATIVE;
//		this.add(new JLabel(I18N.getString("Scheduler.Text.LiveFiberDetect")), gbc); //$NON-NLS-1$
//		gbc.weightx = 0.0;
//		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(this.lfdOptionBox, gbc);

		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.SOUTH;
		this.add(new JLabel(), gbc);
		this.skip = false;
	}

	private void selectCBValue(final JComboBox cb, final BigDecimal value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			final BigDecimal obj = (BigDecimal) cb.getItemAt(i);
			if (obj.compareTo(value) == 0) {
				cb.setSelectedItem(obj);
				break;
			}
		}
	}

}
