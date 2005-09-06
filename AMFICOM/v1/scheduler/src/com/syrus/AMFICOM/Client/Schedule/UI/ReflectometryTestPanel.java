/*
 * ReflectometryTestPanel.java Created on 29.04.2004 13:10:12
 *  
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
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
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSetPackage.ParameterSetSort;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.74 $, $Date: 2005/09/06 07:48:17 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler
 */
public class ReflectometryTestPanel extends ParametersTestPanel implements ParametersTest {

	private static final long	serialVersionUID	= 3257004354304553266L;

	private class ListNumberComparator implements Comparator<String> {

		private int	direction	= 1;

		public ListNumberComparator() {
			this.direction = 1;
		}

		public ListNumberComparator(final int direction) {
			this.direction = direction;
		}

		public int compare(final String s1, final String s2) {
			double d1 = 0;
			double d2 = 0;
			boolean isDoubleNumber = false;
			int result = 0;

			try {
				d1 = Double.parseDouble(s1);
				d2 = Double.parseDouble(s2);
				isDoubleNumber = true;
			} catch (NumberFormatException nfe) {
				isDoubleNumber = false;
			}

			if (isDoubleNumber) {
				if (d1 < d2) {
					result = -this.direction;
				}
				else if (d1 == d2) {
					result = 0;
				}
				else {
					result = this.direction;
				}
			} else {
				result = this.direction * s1.compareTo(s2);
			}

			return result;
		}
	}

	ListNumberComparator	comparator;
	double					maxIndexOfRefraction			= 1.46820;
	double					minIndexOfRefraction			= 1.46820;

	Map<String, String> pulseWidthHiResMap;
	Map<String, String> pulseWidthLowResMap;
	List<String> resolutionList;
	Map<String, String> traceLength;
	Map<String, String> indexOfRefraction;
	Map<String, String> averageCount;

	
	// UI items begin
	private JLabel			refractLabel;
	private JLabel			waveLengthLabel;
	private JLabel			countOfAverageOutLabel;
	private JLabel			resolutionLabel;
	private JLabel			maxDistanceLabel;
	private JLabel 			pulseWidthLabel;
	
	private JTextField		refractTextField;
	private JComboBox		waveLengthComboBox;
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
	
	SchedulerModel			schedulerModel;

	boolean					skip;

	private Identifier		measurementPortId;
	private Identifier		meId;


	
	Identifier				setId;

	long					maxPoints;
	private Map<Identifier, Identifier>				unchangedMeasurementSetupNewMap	= null;

	private Map<ParameterType, String>				unchangedObjects;
	private Dispatcher		dispatcher;

	public ReflectometryTestPanel(final ApplicationContext aContext) {
		super(aContext);
		this.dispatcher = aContext.getDispatcher();
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		this.createGUI();
	}

	public synchronized ParameterSet getSet() {
		ParameterSet parameterSet = null;
			synchronized (this) {
			if (this.setId != null) {
				try {
					parameterSet = (ParameterSet) StorableObjectPool.getStorableObject(this.setId, true);
					if (!parameterSet.isChanged()) {
	//					System.out.println("ReflectometryTestPanel.getSet() | " + this.setId + " !set.isChanged()");
						final Parameter[] parameters = parameterSet.getParameters();
						for (int i = 0; i < parameters.length; i++) {
							final ParameterType type = parameters[i].getType();
							ParameterType typeUnchanged = type;
							Object value = null;
	
							// = parameters[i].getStringValue();
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
								value = this.refractTextField.getText();
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
	//					System.out.println("ReflectometryTestPanel.getSet() | " + this.setId + " set.isChanged()");
						final Parameter[] parameters = parameterSet.getParameters();
						for (int i = 0; i < parameters.length; i++) {
							final ParameterType type = parameters[i].getType();
							Object value = null;
							// = parameters[i].getStringValue();
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
									this.unchangedObjects.remove(type);
									value = this.pulseWidthLowResComboBox.getSelectedItem();
									final ByteArray byteArray = this.getByteArray(value.toString(), ParameterType.REF_PULSE_WIDTH_LOW_RES);
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
									value = this.pulseWidthLowResComboBox.getSelectedItem();
								} else {
									this.unchangedObjects.remove(type);
									value = this.pulseWidthHiResComboBox.getSelectedItem();
									final ByteArray byteArray = this.getByteArray(value.toString(), ParameterType.REF_PULSE_WIDTH_HIGH_RES);
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
								value = this.refractTextField.getText();
							} else if (type.equals(ParameterType.REF_AVERAGE_COUNT)) {
								value = this.averageQuantityComboBox.getSelectedItem();
							} else if (type.equals(ParameterType.REF_FLAG_GAIN_SPLICE_ON)) {
								value = Boolean.toString(this.gsOptionBox.isSelected());
							} else if (type.equals(ParameterType.REF_FLAG_LIFE_FIBER_DETECT)) {
								value = Boolean.toString(this.lfdOptionBox.isSelected());
							}
	
							final String string = this.unchangedObjects.get(type);
							if (value != null && (string == null || !string.equals(value))) {
								final ByteArray byteArray = this.getByteArray(value.toString(), type);
								try {
									parameters[i] = Parameter.createInstance(type, byteArray.getBytes());
								} catch (CreateObjectException e) {
									AbstractMainFrame.showErrorMessage(this, e);
								}
							}
						}				
						parameterSet.setParameters(parameters);
					
					}
				} catch (ApplicationException e) {
					AbstractMainFrame.showErrorMessage(this, e);
					this.setId = null;
				}
			} else  {
	//			System.out.println("ReflectometryTestPanel.getSet() | " + this.setId + " == null");
				try {
	
					this.refreshTitles();
	
					if (this.unchangedObjects == null) {
						this.unchangedObjects = new HashMap<ParameterType, String>();
					} else {
						this.unchangedObjects.clear();
					}
	
					final Parameter[] params = new Parameter[8];
	
					ByteArray byteArray;
	
					Object wave = this.waveLengthComboBox.getSelectedItem();
					if (wave == null) {
						throw new IllegalArgumentException(LangModelSchedule.getString("Error.WaveLengthIsNotSet")); //$NON-NLS-1$
					}
	
					String waveStr = wave.toString();
					if ((waveStr == null) || (waveStr.length() == 0)) {
						throw new IllegalArgumentException(LangModelSchedule.getString("Error.WaveLengthIsNotSet")); //$NON-NLS-1$
					}
	
					byteArray = this.getByteArray(waveStr, ParameterType.REF_WAVE_LENGTH);
	
					params[0] = Parameter.createInstance(ParameterType.REF_WAVE_LENGTH, byteArray.getBytes());
	
					Object distance = this.maxDistanceComboBox.getSelectedItem();
					if (distance == null) {
						throw new IllegalArgumentException(LangModelSchedule.getString("Error.DistanceIsNotSet")); //$NON-NLS-1$
					}
	
					String distanceStr = distance.toString();
					if ((distanceStr == null) || (distanceStr.length() == 0)) {
						throw new IllegalArgumentException(LangModelSchedule.getString("Error.DistanceIsNotSet")); //$NON-NLS-1$
					}
					byteArray = this.getByteArray(distanceStr, ParameterType.REF_TRACE_LENGTH);
	
					params[1] = Parameter.createInstance(ParameterType.REF_TRACE_LENGTH, byteArray.getBytes());
	
					Object resolution = this.resolutionComboBox.getSelectedItem();
					if (resolution == null) {
						throw new IllegalArgumentException(LangModelSchedule.getString("Error.ResolutionIsNotSet")); //$NON-NLS-1$
					}
					final String resolutionStr = resolution.toString();
					if ((resolutionStr == null) || (resolutionStr.length() == 0)) {
						throw new IllegalArgumentException(LangModelSchedule.getString("Error.ResolutionIsNotSet")); //$NON-NLS-1$
					}
					byteArray = this.getByteArray(resolutionStr, ParameterType.REF_RESOLUTION);
	
					params[2] = Parameter.createInstance(ParameterType.REF_RESOLUTION, byteArray.getBytes());
	
					if (this.highResolutionCheckBox.isSelected()) {
						final Object pulse = this.pulseWidthHiResComboBox.getSelectedItem();
						if (pulse == null) {
							throw new IllegalArgumentException(LangModelSchedule.getString("Error.PulseWidthIsNotSet")); //$NON-NLS-1$
						}
						final String pulseStr = pulse.toString();
						if ((pulseStr == null) || (pulseStr.length() == 0)) {
							throw new IllegalArgumentException(LangModelSchedule.getString("Error.PulseWidthIsNotSet")); //$NON-NLS-1$
						}
	
						byteArray = this.getByteArray(pulseStr, ParameterType.REF_PULSE_WIDTH_HIGH_RES);
	
						params[3] = Parameter.createInstance(ParameterType.REF_PULSE_WIDTH_HIGH_RES, byteArray.getBytes());
					} else {
						final Object pulse = this.pulseWidthLowResComboBox.getSelectedItem();
						if (pulse == null) {
							throw new IllegalArgumentException(LangModelSchedule.getString("Error.PulseWidthIsNotSet")); //$NON-NLS-1$
						}
						final String pulseStr = pulse.toString();
						if ((pulseStr == null) || (pulseStr.length() == 0)) {
							throw new IllegalArgumentException(LangModelSchedule.getString("Error.PulseWidthIsNotSet")); //$NON-NLS-1$
						}
	
						byteArray = this.getByteArray(pulseStr, ParameterType.REF_PULSE_WIDTH_LOW_RES);
	
						params[3] = Parameter.createInstance(ParameterType.REF_PULSE_WIDTH_LOW_RES, byteArray.getBytes());
					}
	
					final String refract = this.refractTextField.getText();
					if ((refract == null) || (refract.length() == 0)) {
						throw new IllegalArgumentException(LangModelSchedule.getString("Error.IndexOfRefractionIsNotSet")); //$NON-NLS-1$
					}
					byteArray = this.getByteArray(refract, ParameterType.REF_INDEX_OF_REFRACTION);
	
					params[4] = Parameter.createInstance(ParameterType.REF_INDEX_OF_REFRACTION, byteArray.getBytes());
	
					final Object average = this.averageQuantityComboBox.getSelectedItem();
					if (average == null) {
						throw new IllegalArgumentException(LangModelSchedule.getString("Error.AverageQuantityIsNotSet")); //$NON-NLS-1$
					}
					final String averageStr = average.toString();
					if ((averageStr == null) || (averageStr.length() == 0)) {
						throw new IllegalArgumentException(LangModelSchedule.getString("Error.AverageQuantityIsNotSet")); //$NON-NLS-1$
					}
	
					byteArray = this.getByteArray(averageStr, ParameterType.REF_AVERAGE_COUNT);
	
					params[5] = Parameter.createInstance(ParameterType.REF_AVERAGE_COUNT, byteArray.getBytes());
	
					if (this.meId == null) {
						throw new IllegalArgumentException(LangModelSchedule.getString(LangModelSchedule.getString("Error.HaveNotChoosenMeasurementElement"))); //$NON-NLS-1$
					}
	
					byteArray = this.getByteArray(Boolean.toString(this.gsOptionBox.isSelected()), ParameterType.REF_FLAG_GAIN_SPLICE_ON);
					params[6] = Parameter.createInstance(ParameterType.REF_FLAG_GAIN_SPLICE_ON, byteArray.getBytes());
					
					byteArray = this.getByteArray(Boolean.toString(this.lfdOptionBox.isSelected()),
							ParameterType.REF_FLAG_LIFE_FIBER_DETECT);
					params[7] = Parameter.createInstance(ParameterType.REF_FLAG_LIFE_FIBER_DETECT, byteArray.getBytes());
	
					parameterSet = ParameterSet.createInstance(LoginManager.getUserId(),
							ParameterSetSort.SET_SORT_MEASUREMENT_PARAMETERS,
							LangModelSchedule.getString("Text.MeasurementParameter.SetCreatedByScheduler"), params, Collections.singleton(this.meId)); //$NON-NLS-1$
					this.setId = parameterSet.getId();
	
	//				System.out.println("ReflectometryTestPanel.getSet() | newSet: " + this.setId);
					// Log.debugMessage("ReflectometryTestPanel.getSet | ",
					// Log.FINEST);
				} catch (IllegalArgumentException e) {
					// Log.errorException(e);
					AbstractMainFrame.showErrorMessage(this, e);
				} catch (ApplicationException ae) {
					AbstractMainFrame.showErrorMessage(this, ae);
					this.schedulerModel.setBreakData();
				}
			} 
			}
		return parameterSet;
	}

	private void refreshUnchangedMap() {
		if (this.unchangedObjects == null) {
			this.unchangedObjects = new HashMap<ParameterType, String>();
		} else {
			this.unchangedObjects.clear();
		}

		final Object wave = this.waveLengthComboBox.getSelectedItem();
		this.unchangedObjects.put(ParameterType.REF_WAVE_LENGTH, wave != null ? wave.toString() : null);

		final Object distance = this.maxDistanceComboBox.getSelectedItem();
		this.unchangedObjects.put(ParameterType.REF_TRACE_LENGTH, distance != null ? distance.toString() : null);

		final Object resolution = this.resolutionComboBox.getSelectedItem();
		this.unchangedObjects.put(ParameterType.REF_RESOLUTION, resolution != null ? resolution.toString() : null);

		if (this.highResolutionCheckBox.isSelected()) {
			final Object pulse = this.pulseWidthHiResComboBox.getSelectedItem();
			this.unchangedObjects.put(ParameterType.REF_PULSE_WIDTH_HIGH_RES, pulse != null ? pulse.toString() : null);
		} else {
			final Object pulse = this.pulseWidthLowResComboBox.getSelectedItem();
			this.unchangedObjects.put(ParameterType.REF_PULSE_WIDTH_LOW_RES, pulse != null ? pulse.toString() : null);
		}

		final String refract = this.refractTextField.getText();
		this.unchangedObjects.put(ParameterType.REF_INDEX_OF_REFRACTION, refract != null ? refract.toString() : null);

		final Object average = this.averageQuantityComboBox.getSelectedItem();
		this.unchangedObjects.put(ParameterType.REF_AVERAGE_COUNT, average != null ? average.toString() : null);
		
		final String gs = Boolean.toString(this.gsOptionBox.isSelected());
		this.unchangedObjects.put(ParameterType.REF_FLAG_GAIN_SPLICE_ON, gs);
		
		final String lfd = Boolean.toString(this.lfdOptionBox.isSelected());
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
		this.pulseWidthLabel.setText(LangModelSchedule.getString("Text.MeasurementParameter.Reflectomety.PulseWidth") 
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

	private ByteArray getByteArray(final String value, final ParameterType parameterType) {

		this.unchangedObjects.put(parameterType, value);

		ByteArray byteArray = null;
		final DataType dataType = parameterType.getDataType();
		switch (dataType) {
			case INTEGER:
				byteArray = new ByteArray(Integer.parseInt(value));
				break;
			case DOUBLE:
				byteArray = new ByteArray(Double.parseDouble(value));
				break;
			case STRING:
				byteArray = new ByteArray(value);
				break;
			case LONG:
				byteArray = new ByteArray(Long.parseLong(value));
				break;
			case BOOLEAN:
				byteArray = new ByteArray(Boolean.valueOf(value).booleanValue());
				break;
			default:
				Log.errorMessage("Illegal data type '" + dataType.getCodename() + "'/" + dataType.getCode()
						+ " for parameter type '" + parameterType.getCodename() + "'");
		}
		return byteArray;
		
	}
	
	@Override
	public void setEnableEditing(final boolean enable) {
		this.refractLabel.setEnabled(enable);
		this.waveLengthLabel.setEnabled(enable);
		this.countOfAverageOutLabel.setEnabled(enable);
		this.resolutionLabel.setEnabled(enable);
		this.maxDistanceLabel.setEnabled(enable);
		this.pulseWidthLabel.setEnabled(enable);

		this.refractTextField.setEnabled(enable);
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
//		System.out.println("ReflectometryTestPanel.setMonitoredElement()");
		this.skip = true;
		this.meId = me.getId();
		final Identifier measurementPortId1 = me.getMeasurementPortId();
		if (this.measurementPortId == null || !this.measurementPortId.equals(measurementPortId1)) {
			try {
				this.measurementPortId = measurementPortId1;

				this.refreshTitles();

				final MeasurementPort port = (MeasurementPort) StorableObjectPool.getStorableObject(measurementPortId1, true);
				final LinkedIdsCondition linkedIdsCondition = new LinkedIdsCondition(port.getType().getId(),
						ObjectEntities.CHARACTERISTIC_CODE);
				final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(linkedIdsCondition, true);

				if (this.traceLength == null) {
					this.traceLength = new HashMap<String, String>();
				} else {
					this.traceLength.clear();
				}

				if (this.indexOfRefraction == null) {
					this.indexOfRefraction = new HashMap<String, String>();
				} else {
					this.indexOfRefraction.clear();
				}

				if (this.averageCount == null) {
					this.averageCount = new HashMap<String, String>();
				} else {
					this.averageCount.clear();
				}

				if (this.resolutionList == null) {
					this.resolutionList = new LinkedList<String>();
				} else {
					this.resolutionList.clear();
				}

				if (this.pulseWidthHiResMap == null) {
					this.pulseWidthHiResMap = new HashMap<String, String>();
				} else {
					this.pulseWidthHiResMap.clear();
				}

				if (this.pulseWidthLowResMap == null) {
					this.pulseWidthLowResMap = new HashMap<String, String>();
				} else {
					this.pulseWidthLowResMap.clear();
				}

				Pattern pattern = Pattern.compile(CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + "(\\d+)(" //$NON-NLS-1$
						+ CharacteristicTypeCodenames.TRACE_LENGTH_SUFFIX + "|" //$NON-NLS-1$
						+ CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES_SUFFIX + "|" //$NON-NLS-1$
						+ CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES_SUFFIX + "|" //$NON-NLS-1$
						+ CharacteristicTypeCodenames.TRACE_INDEX_OF_REFRACTION_SUFFIX + "|" //$NON-NLS-1$
						+ CharacteristicTypeCodenames.TRACE_AVERAGE_COUNT_SUFFIX + ")"); //$NON-NLS-1$

//				 Log.debugMessage("ReflectometryTestPanel.setMonitoredElementId | characteristics.size() "
//				 + characteristics.size(), Level.FINEST);

				for (final Characteristic characteristic : characteristics) {
					final StorableObjectType type = characteristic.getType();
//					 System.out.println("characteristicType is " +					 type.getId());
					final String codename = type.getCodename();
					final String value = characteristic.getValue();
//					 System.out.println("codename is '" + codename + "', valueis: " + value);

					if (codename.equals(CharacteristicTypeCodenames.TRACE_WAVELENGTH)) {
						final String[] values = value.split("\\s+"); //$NON-NLS-1$
						Arrays.sort(values, this.comparator);
						this.waveLengthComboBox.removeAllItems();
						for (int i = 0; i < values.length; i++) {
							this.waveLengthComboBox.addItem(values[i]);
						}

					} else if (codename.equals(CharacteristicTypeCodenames.TRACE_RESOLUTION)) {
						final String[] values = value.split("\\s+"); //$NON-NLS-1$
						Arrays.sort(values, this.comparator);
						this.resolutionComboBox.removeAllItems();
						for (int i = 0; i < values.length; i++) {
							this.resolutionList.add(values[i]);
							this.resolutionComboBox.addItem(values[i]);
						}

					} else if (codename.equals(CharacteristicTypeCodenames.TRACE_MAXPOINTS)) {
						this.maxPoints = Long.parseLong(value);
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
							String waveLength = null;
							String suffix = null;
							for (int j = 0; j <= matcher.groupCount(); j++) {
								final String substring = codename.substring(matcher.start(j), matcher.end(j));
								// System.out.println("j:" + j + "\t" +
								// substring);
								switch (j) {
									case 1:
										waveLength = substring;
										break;
									case 2:
										suffix = substring;
										break;
								}
							}

							// Log.debugMessage("ReflectometryTestPanel.setMonitoredElement
							// | waveLength: " + waveLength, Log.FINEST);
							// Log.debugMessage("ReflectometryTestPanel.setMonitoredElement
							// | suffix: " + suffix, Log.FINEST);
							if ((waveLength != null) && (suffix != null)) {
								Map<String, String> map = null;
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
				final String wavelength = selectedItem != null ? this.waveLengthComboBox.getSelectedItem().toString() : null;
				if (wavelength != null) {
					// System.out.println("wavelength is " + wavelength);
					if (this.traceLength != null) {
						final String value = this.traceLength.get(wavelength);
						if (value == null) {
							throw new ObjectNotFoundException(LangModelSchedule.getString("Error.TraceLengthValueNotFound")); //$NON-NLS-1$
						}
						final String[] values = value.split("\\s+"); //$NON-NLS-1$
						Arrays.sort(values, this.comparator);
						this.maxDistanceComboBox.removeAllItems();
						for (int i = 0; i < values.length; i++) {
							this.maxDistanceComboBox.addItem(values[i]);
						}
					} else {
						// TODO throw exception
					}

					if (this.pulseWidthHiResMap != null) {
						final String value = this.pulseWidthHiResMap.get(wavelength);
						if (value != null) {
							final String[] values = value.split("\\s+"); //$NON-NLS-1$
							Arrays.sort(values, this.comparator);
							this.pulseWidthHiResComboBox.removeAllItems();
							for (int i = 0; i < values.length; i++) {
								this.pulseWidthHiResComboBox.addItem(values[i]);
							}
						} else {
							// TODO throw exception
						}
					} else {
						// TODO throw exception
					}

					if (this.pulseWidthLowResMap != null) {
						// Log.debugMessage("ReflectometryTestPanel.setMonitoredElement
						// | this.pulseWidthLowResMap != null ", Log.FINEST);
						final String value = this.pulseWidthLowResMap.get(wavelength);
						// Log.debugMessage("ReflectometryTestPanel.setMonitoredElement
						// | pulseWidthLowResMap value: " + value, Log.FINEST);
						if (value != null) {
							final String[] values = value.split("\\s+"); //$NON-NLS-1$
							Arrays.sort(values, this.comparator);
							this.pulseWidthLowResComboBox.removeAllItems();
							for (int i = 0; i < values.length; i++) {
								this.pulseWidthLowResComboBox.addItem(values[i]);
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
							Arrays.sort(values, this.comparator);
							this.refractTextField.setText(values[0]);
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
							Arrays.sort(values, this.comparator);
							this.averageQuantityComboBox.removeAllItems();
							for (int i = 0; i < values.length; i++) {
								this.averageQuantityComboBox.addItem(values[i]);
							}
						} else {
							// TODO throw exception
						}
					} else {
						// TODO throw exception
					}
				} else {
					System.err.println(LangModelSchedule.getString("Error.SelectedWaveLengthIsNull")); //$NON-NLS-1$
				}

			} catch (ApplicationException ae) {
				AbstractMainFrame.showErrorMessage(this, ae);
			}
		}
		this.skip = false;
	}

	synchronized void refreshTestsSet() {
		if (this.skip) {
			return;
		}
		
		final Set<Identifier> selectedTestIds = this.schedulerModel.getSelectedTestIds();
		if (selectedTestIds != null && !selectedTestIds.isEmpty()) {
			final ParameterSet parameterSet = this.getSet();
			if (parameterSet != null && parameterSet.isChanged()) {
				try {
					final Set<Test> storableObjects = StorableObjectPool.getStorableObjects(selectedTestIds, true);
					final SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);

					MeasurementSetup measurementSetup1 = null;

					for (final Test test : storableObjects) {
						if (test.isChanged()) {
							final Set<Identifier> measurementSetupIds = test.getMeasurementSetupIds();
							if (measurementSetupIds.size() == 1) {
								final MeasurementSetup measurementSetup = (MeasurementSetup) StorableObjectPool.getStorableObject(measurementSetupIds.iterator().next(),
										true);
								if (measurementSetup.isChanged()) {
									measurementSetup.setParameterSet(parameterSet);
								} else {
									Identifier measurementSetupId = null;
									if (this.unchangedMeasurementSetupNewMap == null) {
										this.unchangedMeasurementSetupNewMap = new HashMap<Identifier, Identifier>();
									} else {
										measurementSetupId = this.unchangedMeasurementSetupNewMap.get(measurementSetup.getId());
									}

									if (measurementSetupId == null) {
										measurementSetup1 = MeasurementSetup.createInstance(LoginManager.getUserId(),
												parameterSet,
												measurementSetup.getCriteriaSet(),
												measurementSetup.getThresholdSet(),
												measurementSetup.getEtalon(),
												LangModelSchedule.getString("Text.MeasurementParameter.MeasurementSetupCreatedByScheduler") + " /" + sdf.format(new Date()) + "/",
												measurementSetup.getMeasurementDuration(),
												measurementSetup.getMonitoredElementIds(),
												measurementSetup.getMeasurementTypes());
										measurementSetupId = measurementSetup1.getId();
										this.unchangedMeasurementSetupNewMap.put(measurementSetup.getId(), measurementSetupId);
									}

									test.setMeasurementSetupIds(Collections.singleton(measurementSetupId));
								}
							} else {
								// TODO PROBLEM ?
							}
						}
					}

					if (measurementSetup1 != null) {
						this.skip = true;
						this.dispatcher.firePropertyChange(new PropertyChangeEvent(this,
								SchedulerModel.COMMAND_ADD_NEW_MEASUREMENT_SETUP,
								null,
								measurementSetup1));
						this.skip = false;
					}
				} catch (ApplicationException e) {
					AbstractMainFrame.showErrorMessage(this, e);
				}
			}
		}
	}

	public void setSet(ParameterSet set) {
//		System.out.println("ReflectometryTestPanel.setSet() | 1 " + (set != null ? set.getId() : null));
		if (this.skip) {
			return;
		}		
		
		
		if (this.setId != null && set != null && this.setId.equals(set.getId())) {
			this.skip = false;
			return;
		}

//		System.out.println("ReflectometryTestPanel.setSet() | " + (set != null ? set.getId() : null));
		
		this.skip = true;
		if (this.unchangedObjects == null) {
			this.unchangedObjects = new HashMap<ParameterType, String>();
		} else {
			this.unchangedObjects.clear();
		}
		
		if (set == null) {
			this.setId = null;
			this.skip = false;
			return;
		}

		this.gsOptionBox.setSelected(false);
		this.lfdOptionBox.setSelected(false);	
		
		final Parameter[] setParameters = set.getParameters();
		for (int i = 0; i < setParameters.length; i++) {
			final ParameterType parameterType = setParameters[i].getType();
			//				 Log.debugMessage("ReflectometryTestPanel.setSet | codename "
//				 + codename , Level.FINE);
			if (parameterType.equals(ParameterType.REF_TRACE_LENGTH)) {
				this.selectCBValue(this.maxDistanceComboBox, setParameters[i].getStringValue());
			} else if (parameterType.equals(ParameterType.REF_FLAG_GAIN_SPLICE_ON)) {
				try {
					final boolean b = new ByteArray(setParameters[i].getValue()).toBoolean();
					// Log.debugMessage("ReflectometryTestPanel.setSet |
					// TRACE_FLAG_GAIN_SPLICE " + b, Log.FINEST);
					this.gsOptionBox.setSelected(b);

				} catch (IOException e) {
					AbstractMainFrame.showErrorMessage(this, e);
				}
			} else if (parameterType.equals(ParameterType.REF_FLAG_LIFE_FIBER_DETECT)) {
				try {
					final boolean b = new ByteArray(setParameters[i].getValue()).toBoolean();
					// Log.debugMessage("ReflectometryTestPanel.setSet |
					// TRACE_FLAG_LIVE_FIBER_DETECT " + b, Log.FINEST);
					this.lfdOptionBox.setSelected(b);

				} catch (IOException e) {
					AbstractMainFrame.showErrorMessage(this, e);
				}
			}
		}

		for (int i = 0; i < setParameters.length; i++) {
			final ParameterType parameterType = setParameters[i].getType();
			final String stringValue = setParameters[i].getStringValue();
			// Log.debugMessage("ReflectometryTestPanel.setSet | codename: " +
			// codename + ", stringValue:" + stringValue, Level.FINEST);
			if (parameterType.equals(ParameterType.REF_INDEX_OF_REFRACTION)) {
				this.refractTextField.setText(stringValue);
			} else if (parameterType.equals(ParameterType.REF_WAVE_LENGTH)) {
				this.selectCBValue(this.waveLengthComboBox, stringValue);
			} else if (parameterType.equals(ParameterType.REF_AVERAGE_COUNT)) {
				this.selectCBValue(this.averageQuantityComboBox, stringValue);
			} else if (parameterType.equals(ParameterType.REF_RESOLUTION)) {
				this.selectCBValue(this.resolutionComboBox, stringValue);
			} else if (parameterType.equals(ParameterType.REF_PULSE_WIDTH_HIGH_RES)) {
				this.selectCBValue(this.pulseWidthHiResComboBox, stringValue);
				if (!this.highResolutionCheckBox.isSelected()) {
					this.highResolutionCheckBox.doClick();
				}
			} else if (parameterType.equals(ParameterType.REF_PULSE_WIDTH_LOW_RES)) {
				this.selectCBValue(this.pulseWidthLowResComboBox, stringValue);
				if (this.highResolutionCheckBox.isSelected()) {
					this.highResolutionCheckBox.doClick();
				}
			}
		}
		this.refreshUnchangedMap();
		this.setId = set.getId();
		this.skip = false;
	}

	private void createUIItems() {
		this.comparator = new ListNumberComparator();
		this.refractTextField = new JTextField(8);

		this.waveLengthComboBox = new JComboBox();
		this.maxDistanceComboBox = new JComboBox();
		this.highResolutionCheckBox = new JCheckBox(LangModelSchedule.getString("Text.MeasurementParameter.Reflectomety.HighResolution"));
		this.pulseWidthHiResComboBox = new JComboBox();
		this.pulseWidthHiResComboBox.setMaximumRowCount(15);
		this.pulseWidthLowResComboBox = new JComboBox();
		this.pulseWidthLowResComboBox.setMaximumRowCount(15);
		this.resolutionComboBox = new JComboBox();
		this.averageQuantityComboBox = new JComboBox();

		this.refractLabel = new JLabel(LangModelSchedule.getString("Text.MeasurementParameter.Reflectomety.IndexOfRefraction")); //$NON-NLS-1$
		this.waveLengthLabel = new JLabel(LangModelSchedule.getString("Text.MeasurementParameter.Reflectomety.WaveLength")); //$NON-NLS-1$
		this.countOfAverageOutLabel = new JLabel(LangModelSchedule.getString("Text.MeasurementParameter.Reflectomety.AverageQuantity")); //$NON-NLS-1$
		this.resolutionLabel = new JLabel(LangModelSchedule.getString("Text.MeasurementParameter.Reflectomety.Resolution")); //$NON-NLS-1$
		this.maxDistanceLabel = new JLabel(LangModelSchedule.getString("Text.MeasurementParameter.Reflectomety.Distance")); //$NON-NLS-1$
		this.pulseWidthLabel = new JLabel(LangModelSchedule.getString("Text.MeasurementParameter.Reflectomety.PulseWidth")); //$NON-NLS-1$

		this.gsOptionBox = new JCheckBox(LangModelSchedule.getString("Text.MeasurementParameter.Reflectomety.GainSplice"));
		this.bcOptionBox = new JCheckBox(LangModelSchedule.getString("Text.MeasurementParameter.Reflectomety.BoxCar"));
		this.lfdOptionBox = new JCheckBox(LangModelSchedule.getString("Text.MeasurementParameter.Reflectomety.LiveFiberDetect"));

		this.refractTextField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JTextField textField = (JTextField) e.getSource();
				String value = textField.getText();
				double refract = 0.0;
				boolean isDouble = false;
				try {
					refract = Double.parseDouble(value);
					isDouble = true;
				} catch (NumberFormatException nfe) {
					isDouble = false;
				}

				if (!isDouble) {
					textField.setText(Double.toString(ReflectometryTestPanel.this.minIndexOfRefraction));
				} else {
					if (refract < ReflectometryTestPanel.this.minIndexOfRefraction) {
						textField.setText(Double.toString(ReflectometryTestPanel.this.minIndexOfRefraction));
					} else {
						if (refract > ReflectometryTestPanel.this.maxIndexOfRefraction) {
							textField.setText(Double.toString(ReflectometryTestPanel.this.maxIndexOfRefraction));
						}
					}
				}

			}
		});

		this.maxDistanceComboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				synchronized (ReflectometryTestPanel.this) {
					JComboBox comboBox = (JComboBox) e.getSource();
					Object selectedItem = comboBox.getSelectedItem();
					if (selectedItem == null) {
						return;
					}
					synchronized (ReflectometryTestPanel.this) {
						/* 1000 m is 1 km */
						double maxDistance = 1000.0 * Double.parseDouble(selectedItem.toString());
						String resolutionItem = (String) ReflectometryTestPanel.this.resolutionComboBox.getSelectedItem();
						double resolutionOld = Double.parseDouble(resolutionItem);
						boolean found = false;
						boolean firstItem = true;
						boolean firstItemGreater = false;
						ReflectometryTestPanel.this.resolutionComboBox.removeAllItems();
						for (Iterator it = ReflectometryTestPanel.this.resolutionList.iterator(); it.hasNext();) {
							String res = (String) it.next();
							double resolution = Double.parseDouble(res);
							if (maxDistance / resolution < ReflectometryTestPanel.this.maxPoints) {
								if (res.equals(resolutionItem)) {
									found = true;
								} else {
									if (firstItem) {
										if (resolutionOld < resolution) {
											firstItemGreater = true;
										}
									}
								}
								
								firstItem = false;
								
								ReflectometryTestPanel.this.resolutionComboBox.addItem(res);
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
					}
				}
			}
		});

		this.highResolutionCheckBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean b = checkBox.isSelected();
				ReflectometryTestPanel.this.pulseWidthHiResComboBox.setVisible(b);
				ReflectometryTestPanel.this.pulseWidthLowResComboBox.setVisible(!b);

			}
		});

		ActionListener changeActionListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!ReflectometryTestPanel.this.skip) {
					ReflectometryTestPanel.this.refreshTestsSet();
				}
			}
		};

		this.refractTextField.addActionListener(changeActionListener);
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

		gbc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(this.refractLabel, gbc);
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(this.refractTextField, gbc);

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(this.waveLengthLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		this.add(this.waveLengthComboBox, gbc);

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(this.countOfAverageOutLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		this.add(this.averageQuantityComboBox, gbc);

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
		this.add(this.maxDistanceLabel, gbc);
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(this.maxDistanceComboBox, gbc);

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(this.resolutionLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		this.add(this.resolutionComboBox, gbc);
		
		gbc.weightx = 1.0;
//		gbc.gridwidth = GridBagConstraints.RELATIVE;
//		this.add(new JLabel(LangModelSchedule.getString("Text.GainSplice")), gbc); //$NON-NLS-1$
//		gbc.weightx = 0.0;
//		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(this.gsOptionBox, gbc);

		gbc.weightx = 1.0;
//		this.add(this.bcOptionBox, gbc);
		this.bcOptionBox.setSelected(false);
		this.bcOptionBox.setEnabled(false);

		
		gbc.weightx = 1.0;
//		gbc.gridwidth = GridBagConstraints.RELATIVE;
//		this.add(new JLabel(LangModelSchedule.getString("Text.LiveFiberDetect")), gbc); //$NON-NLS-1$
//		gbc.weightx = 0.0;
//		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(this.lfdOptionBox, gbc);

		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.SOUTH;
		this.add(new JLabel(), gbc);
		this.skip = false;
	}

	private void selectCBValue(final JComboBox cb, final String value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			final Object obj = cb.getItemAt(i);
			final String item = obj.toString();
			final double d = Double.parseDouble(item);
			final double e = Double.parseDouble(value);

			// System.out.println("ReflectometryTestPanel.selectCBValue() | value:" +
			// value + ", item:" + item + ", d:" + d + ", e:" + e);

			if (Math.abs(d - e) < 0.000001) {
				cb.setSelectedItem(obj);
				break;
			}
		}
	}

}
