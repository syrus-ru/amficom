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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.ParameterSet_TransferablePackage.ParameterSetSort;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.44 $, $Date: 2005/06/17 11:01:13 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public class ReflectometryTestPanel extends ParametersTestPanel implements ParametersTest {

	private static final long	serialVersionUID	= 3257004354304553266L;

	private class ListNumberComparator implements java.util.Comparator {

		private int	direction	= 1;

		public ListNumberComparator() {
			this.direction = 1;
		}

		public ListNumberComparator(int direction) {
			this.direction = direction;
		}

		public int compare(	Object o1,
							Object o2) {
			String s1 = o1.toString();
			String s2 = o2.toString();
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
				if (d1 < d2)
					result = -this.direction;
				else if (d1 == d2)
					result = 0;
				else
					result = this.direction;
			} else {
				result = this.direction * s1.compareTo(s2);
			}

			return result;
		}
	}

	ListNumberComparator	comparator;
	double					maxIndexOfRefraction			= 1.46820;
	double					minIndexOfRefraction			= 1.46820;
	
	Map						pulseWidthHiResMap;
	Map						pulseWidthLowResMap;
	List					resolutionList;
	Map						traceLength;
	Map						indexOfRefraction;
	Map						averageCount;

	private JTextField		refractTextField;

	private JComboBox		waveLengthComboBox;
	JComboBox				maxDistanceComboBox;
	JCheckBox				pulseWidthCheckBox;
	JComboBox				pulseWidthHiResComboBox;
	JComboBox				pulseWidthLowResComboBox;
	JComboBox				resolutionComboBox;
	private JComboBox		averageQuantityComboBox;

	private JCheckBox		gsOptionBox;
	private JCheckBox		bcOptionBox;
	private JCheckBox		lfdOptionBox;

	SchedulerModel			schedulerModel;

	boolean					skip;

	private Identifier		measurementPortId;
	private Identifier		meId;

	private ParameterType	wvlenParameterType;
	private ParameterType	trclenParameterType;
	private ParameterType	resParameterType;
	private ParameterType	pulswdHiResParameterType;
	private ParameterType	pulswdLowResParameterType;
	private ParameterType	iorParameterType;
	private ParameterType	scansParameterType;
	private ParameterType	gsFlagParameterType;
	private ParameterType	lfdFlagParameterType;

	private JLabel			refractLabel;
	private JLabel			waveLengthLabel;
	private JLabel			countOfAverageOutLabel;
	private JLabel			resolutionLabel;
	private JLabel			maxDistanceLabel;

	Identifier				setId;

	long					maxPoints;
	private Map				unchangedMeasurementSetupNewMap	= null;

	private Map				unchangedObjects;
	private Dispatcher		dispatcher;

	public ReflectometryTestPanel(ApplicationContext aContext) {
		super(aContext);
		this.aContext = aContext;
		this.dispatcher = aContext.getDispatcher();
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		this.createGUI();
	}

	public synchronized ParameterSet getSet() {
		ParameterSet set = null;
		if (this.setId != null) {
			try {
				set = (ParameterSet) StorableObjectPool.getStorableObject(this.setId, true);
				if (!set.isChanged()) {
					Parameter[] parameters = set.getParameters();
					for (int i = 0; i < parameters.length; i++) {
						ParameterType type = (ParameterType) parameters[i].getType();
						String codename = type.getCodename();
						Object value = null;

						// = parameters[i].getStringValue();
						if (codename.equals(this.wvlenParameterType.getCodename())) {
							value = this.waveLengthComboBox.getSelectedItem().toString();
						} else if (codename.equals(this.trclenParameterType.getCodename())) {
							value = this.maxDistanceComboBox.getSelectedItem();
						} else if (codename.equals(this.resParameterType.getCodename())) {
							value = this.resolutionComboBox.getSelectedItem();
						} else if (codename.equals(this.pulswdHiResParameterType.getCodename())) {
							if (this.pulseWidthCheckBox.isSelected()) {
								value = this.pulseWidthHiResComboBox.getSelectedItem();
							} else {
								value = this.pulseWidthLowResComboBox.getSelectedItem();
								codename = this.pulswdLowResParameterType.getCodename();
							}
						} else if (codename.equals(this.pulswdLowResParameterType.getCodename())) {
							if (!this.pulseWidthCheckBox.isSelected()) {
								value = this.pulseWidthLowResComboBox.getSelectedItem();
							} else {
								value = this.pulseWidthHiResComboBox.getSelectedItem();
								codename = this.pulswdHiResParameterType.getCodename();
							}
						} else if (codename.equals(this.iorParameterType.getCodename())) {
							value = this.refractTextField.getText();
						} else if (codename.equals(this.scansParameterType.getCodename())) {
							value = this.averageQuantityComboBox.getSelectedItem();
						} else if (codename.equals(this.gsFlagParameterType.getCodename())) {
							value = Boolean.toString(this.gsOptionBox.isSelected());
						} else if (codename.equals(this.lfdFlagParameterType.getCodename())) {
							value = Boolean.toString(this.lfdOptionBox.isSelected());
						}
						if (value != null && !value.equals(this.unchangedObjects.get(codename))) {
							this.setId = null;
							break;
						}
					}
				}
			} catch (ApplicationException e) {
				AbstractMainFrame.showErrorMessage(this, e);
				this.setId = null;
			}
		}

		// Log.debugMessage("ReflectometryTestPanel.getSet | set id is " +
		// this.setId, Log.FINEST);
		if (this.setId == null) {
			try {

				this.refreshTitles();

				if (this.unchangedObjects == null) {
					this.unchangedObjects = new HashMap();
				} else {
					this.unchangedObjects.clear();
				}

				Parameter[] params = new Parameter[6 + (this.gsFlagParameterType != null ? 1 : 0)
						+ (this.lfdFlagParameterType != null ? 1 : 0)];

				ByteArray byteArray;

				Object wave = this.waveLengthComboBox.getSelectedItem();
				if (wave == null)
					throw new IllegalArgumentException(LangModelSchedule.getString("Error.WaveLengthIsNotSet")); //$NON-NLS-1$

				String waveStr = wave.toString();
				if ((waveStr == null) || (waveStr.length() == 0))
					throw new IllegalArgumentException(LangModelSchedule.getString("Error.WaveLengthIsNotSet")); //$NON-NLS-1$

				byteArray = this.getByteArray(waveStr, this.wvlenParameterType);

				params[0] = Parameter.createInstance(this.wvlenParameterType, byteArray.getBytes());

				Object distance = this.maxDistanceComboBox.getSelectedItem();
				if (distance == null)
					throw new IllegalArgumentException(LangModelSchedule.getString("Error.DistanceIsNotSet")); //$NON-NLS-1$

				String distanceStr = distance.toString();
				if ((distanceStr == null) || (distanceStr.length() == 0))
					throw new IllegalArgumentException(LangModelSchedule.getString("Error.DistanceIsNotSet")); //$NON-NLS-1$
				byteArray = this.getByteArray(distanceStr, this.trclenParameterType);

				params[1] = Parameter.createInstance(this.trclenParameterType, byteArray.getBytes());

				Object resolution = this.resolutionComboBox.getSelectedItem();
				if (resolution == null)
					throw new IllegalArgumentException(LangModelSchedule.getString("Error.ResolutionIsNotSet")); //$NON-NLS-1$
				String resolutionStr = resolution.toString();
				if ((resolutionStr == null) || (resolutionStr.length() == 0))
					throw new IllegalArgumentException(LangModelSchedule.getString("Error.ResolutionIsNotSet")); //$NON-NLS-1$
				byteArray = this.getByteArray(resolutionStr, this.resParameterType);

				params[2] = Parameter.createInstance(this.resParameterType, byteArray.getBytes());

				if (this.pulseWidthCheckBox.isSelected()) {
					Object pulse = this.pulseWidthHiResComboBox.getSelectedItem();
					if (pulse == null)
						throw new IllegalArgumentException(LangModelSchedule.getString("Error.PulseWidthIsNotSet")); //$NON-NLS-1$
					String pulseStr = pulse.toString();
					if ((pulseStr == null) || (pulseStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule.getString("Error.PulseWidthIsNotSet")); //$NON-NLS-1$

					byteArray = this.getByteArray(pulseStr, this.pulswdHiResParameterType);

					params[3] = Parameter.createInstance(this.pulswdHiResParameterType, byteArray.getBytes());
				} else {
					Object pulse = this.pulseWidthLowResComboBox.getSelectedItem();
					if (pulse == null)
						throw new IllegalArgumentException(LangModelSchedule.getString("Error.PulseWidthIsNotSet")); //$NON-NLS-1$
					String pulseStr = pulse.toString();
					if ((pulseStr == null) || (pulseStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule.getString("Error.PulseWidthIsNotSet")); //$NON-NLS-1$

					byteArray = this.getByteArray(pulseStr, this.pulswdLowResParameterType);

					params[3] = Parameter.createInstance(this.pulswdLowResParameterType, byteArray.getBytes());
				}

				String refract = this.refractTextField.getText();
				if ((refract == null) || (refract.length() == 0))
					throw new IllegalArgumentException(LangModelSchedule.getString("Error.IndexOfRefractionIsNotSet")); //$NON-NLS-1$
				byteArray = this.getByteArray(refract, this.iorParameterType);

				params[4] = Parameter.createInstance(this.iorParameterType, byteArray.getBytes());

				Object average = this.averageQuantityComboBox.getSelectedItem();
				if (average == null)
					throw new IllegalArgumentException(LangModelSchedule.getString("Error.AverageQuantityIsNotSet")); //$NON-NLS-1$
				String averageStr = average.toString();
				if ((averageStr == null) || (averageStr.length() == 0))
					throw new IllegalArgumentException(LangModelSchedule.getString("Error.AverageQuantityIsNotSet")); //$NON-NLS-1$

				byteArray = this.getByteArray(averageStr, this.scansParameterType);

				params[5] = Parameter.createInstance(this.scansParameterType, byteArray.getBytes());

				if (this.meId == null)
					throw new IllegalArgumentException(LangModelSchedule.getString(LangModelSchedule
							.getString("Error.HaveNotChoosenMeasurementElement"))); //$NON-NLS-1$

				if (this.gsFlagParameterType != null) {
					byteArray = this.getByteArray(Boolean.toString(this.gsOptionBox.isSelected()),
						this.gsFlagParameterType);
					params[6] = Parameter.createInstance(this.gsFlagParameterType, byteArray.getBytes());
				}

				if (this.lfdFlagParameterType != null) {
					byteArray = this.getByteArray(Boolean.toString(this.lfdOptionBox.isSelected()),
						this.lfdFlagParameterType);
					params[7] = Parameter.createInstance(this.lfdFlagParameterType, byteArray.getBytes());
				}

				set = ParameterSet
						.createInstance(
							LoginManager.getUserId(),
							ParameterSetSort.SET_SORT_MEASUREMENT_PARAMETERS,
							LangModelSchedule.getString("Text.SetCreatedByScheduler"), params, Collections.singleton(this.meId)); //$NON-NLS-1$
				StorableObjectPool.putStorableObject(set);
				this.setId = set.getId();

				// Log.debugMessage("ReflectometryTestPanel.getSet | ",
				// Log.FINEST);
			} catch (IllegalArgumentException e) {
				// Log.errorException(e);
				AbstractMainFrame.showErrorMessage(this, e);
			} catch (ApplicationException ae) {
				AbstractMainFrame.showErrorMessage(this, ae);
				this.schedulerModel.setBreakData();
			}
		} else {
			// Log.debugMessage("ReflectometryTestPanel.getSet | set.isChanged()
			// " + set.isChanged(), Log.FINEST);
			if (set.isChanged()) {
				Parameter[] parameters = set.getParameters();
				for (int i = 0; i < parameters.length; i++) {
					ParameterType type = (ParameterType) parameters[i].getType();
					String codename = type.getCodename();
					Object value = null;
					// = parameters[i].getStringValue();
					if (codename.equals(this.wvlenParameterType.getCodename())) {
						value = this.waveLengthComboBox.getSelectedItem().toString();
					} else if (codename.equals(this.trclenParameterType.getCodename())) {
						value = this.maxDistanceComboBox.getSelectedItem();
					} else if (codename.equals(this.resParameterType.getCodename())) {
						value = this.resolutionComboBox.getSelectedItem();
					} else if (codename.equals(this.pulswdHiResParameterType.getCodename())) {
						if (this.pulseWidthCheckBox.isSelected()) {
							value = this.pulseWidthHiResComboBox.getSelectedItem();
						} else {
							this.unchangedObjects.remove(codename);
							value = this.pulseWidthLowResComboBox.getSelectedItem();
							ByteArray byteArray = this.getByteArray(value.toString(), this.pulswdLowResParameterType);
							try {
								Parameter setParameter = Parameter.createInstance(this.pulswdLowResParameterType,
									byteArray.getBytes());
								parameters[i] = setParameter;
							} catch (CreateObjectException e) {
								AbstractMainFrame.showErrorMessage(this, e);
							}
						}
					} else if (codename.equals(this.pulswdLowResParameterType.getCodename())) {
						if (!this.pulseWidthCheckBox.isSelected()) {
							value = this.pulseWidthLowResComboBox.getSelectedItem();
						} else {
							this.unchangedObjects.remove(codename);
							value = this.pulseWidthHiResComboBox.getSelectedItem();
							ByteArray byteArray = this.getByteArray(value.toString(), this.pulswdHiResParameterType);
							try {
								Parameter setParameter = Parameter.createInstance(this.pulswdHiResParameterType,
									byteArray.getBytes());
								parameters[i] = setParameter;
							} catch (CreateObjectException e) {
								AbstractMainFrame.showErrorMessage(this, e);
							}
						}
					}

					else if (codename.equals(this.iorParameterType.getCodename())) {
						value = this.refractTextField.getText();
					} else if (codename.equals(this.scansParameterType.getCodename())) {
						value = this.averageQuantityComboBox.getSelectedItem();
					} else if (codename.equals(this.gsFlagParameterType.getCodename())) {
						value = Boolean.toString(this.gsOptionBox.isSelected());
					} else if (codename.equals(this.lfdFlagParameterType.getCodename())) {
						value = Boolean.toString(this.lfdOptionBox.isSelected());
					}
					if (value != null && !this.unchangedObjects.get(codename).equals(value)) {
						ByteArray byteArray = this.getByteArray(value.toString(), type);
						try {
							parameters[i] = Parameter.createInstance(type, byteArray.getBytes());
						} catch (CreateObjectException e) {
							AbstractMainFrame.showErrorMessage(this, e);
						}
					}
				}
				set.setParameters(parameters);
			}
		}
		return set;
	}

	private void refreshUnchangedMap() {
		if (this.unchangedObjects == null) {
			this.unchangedObjects = new HashMap();
		} else {
			this.unchangedObjects.clear();
		}

		Object wave = this.waveLengthComboBox.getSelectedItem();
		this.unchangedObjects.put(this.wvlenParameterType.getCodename(), wave != null ? wave.toString() : null);

		Object distance = this.maxDistanceComboBox.getSelectedItem();
		this.unchangedObjects
				.put(this.trclenParameterType.getCodename(), distance != null ? distance.toString() : null);

		Object resolution = this.resolutionComboBox.getSelectedItem();
		this.unchangedObjects.put(this.resParameterType.getCodename(), resolution != null ? resolution.toString()
				: null);

		if (this.pulseWidthCheckBox.isSelected()) {
			Object pulse = this.pulseWidthHiResComboBox.getSelectedItem();
			this.unchangedObjects.put(this.pulswdHiResParameterType.getCodename(), pulse != null ? pulse.toString()
					: null);
		} else {
			Object pulse = this.pulseWidthLowResComboBox.getSelectedItem();
			this.unchangedObjects.put(this.pulswdLowResParameterType.getCodename(), pulse != null ? pulse.toString()
					: null);
		}

		String refract = this.refractTextField.getText();
		this.unchangedObjects.put(this.iorParameterType.getCodename(), refract != null ? refract.toString() : null);

		Object average = this.averageQuantityComboBox.getSelectedItem();
		this.unchangedObjects.put(this.scansParameterType.getCodename(), average != null ? average.toString() : null);

		if (this.gsFlagParameterType != null) {
			String string = Boolean.toString(this.gsOptionBox.isSelected());
			this.unchangedObjects.put(this.gsFlagParameterType.getCodename(), string);
		}

		if (this.lfdFlagParameterType != null) {
			String string = Boolean.toString(this.lfdOptionBox.isSelected());
			this.unchangedObjects.put(this.lfdFlagParameterType.getCodename(), string);
		}
	}

	private void refreshTitles() {

		if (this.wvlenParameterType != null && this.trclenParameterType != null && this.resParameterType != null
				&& this.pulswdHiResParameterType != null && this.iorParameterType != null
				&& this.scansParameterType != null)
			return;

		try {
			TypicalCondition waveLengthCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_WAVELENGTH,
																		OperationSort.OPERATION_EQUALS,
																		ObjectEntities.PARAMETER_TYPE_CODE,
																		StorableObjectWrapper.COLUMN_CODENAME);

			TypicalCondition traceLengthCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_LENGTH,
																			OperationSort.OPERATION_EQUALS,
																			ObjectEntities.PARAMETER_TYPE_CODE,
																			StorableObjectWrapper.COLUMN_CODENAME);

			TypicalCondition resolutionCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_RESOLUTION,
																		OperationSort.OPERATION_EQUALS,
																		ObjectEntities.PARAMETER_TYPE_CODE,
																		StorableObjectWrapper.COLUMN_CODENAME);

			TypicalCondition pulseWidthHiResCondition = new TypicalCondition(
																				ParameterTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES,
																				OperationSort.OPERATION_EQUALS,
																				ObjectEntities.PARAMETER_TYPE_CODE,
																				StorableObjectWrapper.COLUMN_CODENAME);

			TypicalCondition pulseWidthLowResCondition = new TypicalCondition(
																				ParameterTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES,
																				OperationSort.OPERATION_EQUALS,
																				ObjectEntities.PARAMETER_TYPE_CODE,
																				StorableObjectWrapper.COLUMN_CODENAME);

			TypicalCondition indexOfRefractionCondition = new TypicalCondition(
																				ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION,
																				OperationSort.OPERATION_EQUALS,
																				ObjectEntities.PARAMETER_TYPE_CODE,
																				StorableObjectWrapper.COLUMN_CODENAME);

			TypicalCondition averageCountCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_AVERAGE_COUNT,
																			OperationSort.OPERATION_EQUALS,
																			ObjectEntities.PARAMETER_TYPE_CODE,
																			StorableObjectWrapper.COLUMN_CODENAME);

			TypicalCondition gainSpliceFlagCondition = new TypicalCondition(
																			ParameterTypeCodenames.TRACE_FLAG_GAIN_SPLICE_ON,
																			OperationSort.OPERATION_EQUALS,
																			ObjectEntities.PARAMETER_TYPE_CODE,
																			StorableObjectWrapper.COLUMN_CODENAME);

			TypicalCondition liveFiberDetectFlagCondition = new TypicalCondition(
																					ParameterTypeCodenames.TRACE_FLAG_LIVE_FIBER_DETECT,
																					OperationSort.OPERATION_EQUALS,
																					ObjectEntities.PARAMETER_TYPE_CODE,
																					StorableObjectWrapper.COLUMN_CODENAME);

			java.util.Set conditions = new HashSet(9);
			conditions.add(waveLengthCondition);
			conditions.add(traceLengthCondition);
			conditions.add(resolutionCondition);
			conditions.add(pulseWidthHiResCondition);
			conditions.add(pulseWidthLowResCondition);
			conditions.add(indexOfRefractionCondition);
			conditions.add(averageCountCondition);
			conditions.add(gainSpliceFlagCondition);
			conditions.add(liveFiberDetectFlagCondition);

			// CompoundCondition compoundCondition = new
			// CompoundCondition(conditions, CompoundConditionSort.OR);

			Collection parameterTypes = new ArrayList(6);
			for (Iterator iterator = conditions.iterator(); iterator.hasNext();) {
				StorableObjectCondition condition = (StorableObjectCondition) iterator.next();

				java.util.Set storableObjectsByCondition = StorableObjectPool.getStorableObjectsByCondition(condition, true, true);
				if (storableObjectsByCondition.isEmpty())
					throw new IllegalArgumentException(
														LangModelSchedule.getString("Error.CannotFindParameterTypes") + " "); //$NON-NLS-1$ //$NON-NLS-2$
				parameterTypes.add(storableObjectsByCondition.iterator().next());

			}
			// GeneralStorableObjectPool.getStorableObjectsByCondition(compoundCondition,
			// true);

			for (Iterator iter = parameterTypes.iterator(); iter.hasNext();) {
				ParameterType parameterType = (ParameterType) iter.next();
				String codeName = parameterType.getCodename();
				if (codeName.equals(waveLengthCondition.getValue())) {
					this.wvlenParameterType = parameterType;
				} else if (codeName.equals(traceLengthCondition.getValue())) {
					this.trclenParameterType = parameterType;
				} else if (codeName.equals(resolutionCondition.getValue())) {
					this.resParameterType = parameterType;
				} else if (codeName.equals(pulseWidthHiResCondition.getValue())) {
					this.pulswdHiResParameterType = parameterType;
				} else if (codeName.equals(pulseWidthLowResCondition.getValue())) {
					this.pulswdLowResParameterType = parameterType;
				} else if (codeName.equals(indexOfRefractionCondition.getValue())) {
					this.iorParameterType = parameterType;
				} else if (codeName.equals(averageCountCondition.getValue())) {
					this.scansParameterType = parameterType;
				} else if (codeName.equals(gainSpliceFlagCondition.getValue())) {
					this.gsFlagParameterType = parameterType;
				} else if (codeName.equals(liveFiberDetectFlagCondition.getValue())) {
					this.lfdFlagParameterType = parameterType;
				}

			}

			if (this.wvlenParameterType == null) { throw new IllegalArgumentException(LangModelSchedule
					.getString("Error.CannotFindParameterType") + " " //$NON-NLS-2$
					+ ParameterTypeCodenames.TRACE_WAVELENGTH); }

			if (this.trclenParameterType == null)
				throw new IllegalArgumentException(
													LangModelSchedule.getString("Error.CannotFindParameterType") + " " + ParameterTypeCodenames.TRACE_LENGTH); //$NON-NLS-2$
			if (this.trclenParameterType == null)
				throw new IllegalArgumentException(LangModelSchedule.getString("Error.CannotFindParameterType") + " " //$NON-NLS-2$
						+ ParameterTypeCodenames.TRACE_RESOLUTION);

			if (this.pulswdHiResParameterType == null)
				throw new IllegalArgumentException(LangModelSchedule.getString("Error.CannotFindParameterType") + " " //$NON-NLS-2$
						+ ParameterTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES);

			if (this.iorParameterType == null)
				throw new IllegalArgumentException(LangModelSchedule.getString("Error.CannotFindParameterType") + " " //$NON-NLS-2$
						+ ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION);

			if (this.scansParameterType == null)
				throw new IllegalArgumentException(LangModelSchedule.getString("Error.CannotFindParameterType") + " " //$NON-NLS-2$
						+ ParameterTypeCodenames.TRACE_AVERAGE_COUNT);

			this.refractLabel.setText(this.iorParameterType.getDescription() + this.getUnit(this.iorParameterType));
			this.waveLengthLabel.setText(this.wvlenParameterType.getDescription()
					+ this.getUnit(this.wvlenParameterType));
			this.countOfAverageOutLabel.setText(this.scansParameterType.getDescription()
					+ this.getUnit(this.scansParameterType));
			this.pulseWidthCheckBox.setText(this.pulswdHiResParameterType.getDescription()
				+ this.getUnit(this.pulswdHiResParameterType));
//			this.pulseWidthHiResLabel.setText(this.pulswdHiResParameterType.getDescription()
//					+ this.getUnit(this.pulswdHiResParameterType));
//			this.pulseWidthLowResLabel.setText(this.pulswdLowResParameterType.getDescription()
//					+ this.getUnit(this.pulswdLowResParameterType));
			this.resolutionLabel.setText(this.resParameterType.getDescription() + this.getUnit(this.resParameterType));
			this.maxDistanceLabel.setText(this.trclenParameterType.getDescription()
					+ this.getUnit(this.trclenParameterType));

			this.gsOptionBox.setVisible(this.gsFlagParameterType != null);
			this.lfdOptionBox.setVisible(this.lfdFlagParameterType != null);
		} catch (ApplicationException exception) {
			AbstractMainFrame.showErrorMessage(this, exception);
		}
	}

	private String getUnit(ParameterType parameterType) {
		java.util.Set characteristics = parameterType.getCharacteristics();
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			Characteristic characteristic = (Characteristic) it.next();
			if (characteristic.getType().getCodename().startsWith(CharacteristicTypeCodenames.UNITS_PREFIX)) {
				String value = characteristic.getValue();
				if (value != null && value.trim().length() != 0) { return ',' + characteristic.getValue(); }

				return ""; //$NON-NLS-1$
			}
		}
		return ""; //$NON-NLS-1$
	}

	private ByteArray getByteArray(	String value,
									ParameterType parameterType) {

		this.unchangedObjects.put(parameterType.getCodename(), value);

		ByteArray byteArray = null;
		DataType dataType = parameterType.getDataType();
		switch (dataType.value()) {
			case DataType._DATA_TYPE_INTEGER:
				byteArray = new ByteArray(Integer.parseInt(value));
				break;
			case DataType._DATA_TYPE_DOUBLE:
				byteArray = new ByteArray(Double.parseDouble(value));
				break;
			case DataType._DATA_TYPE_STRING:
				byteArray = new ByteArray(value);
				break;
			case DataType._DATA_TYPE_LONG:
				byteArray = new ByteArray(Long.parseLong(value));
				break;
			case DataType._DATA_TYPE_BOOLEAN:
				byteArray = new ByteArray(Boolean.valueOf(value).booleanValue());
				break;
		}
		return byteArray;
	}

	public void setMonitoredElement(MonitoredElement me) {
		this.skip = true;
		this.meId = me.getId();
		Identifier measurementPortId1 = me.getMeasurementPortId();
		if (this.measurementPortId == null || !this.measurementPortId.equals(measurementPortId1)) {
			try {
				this.measurementPortId = measurementPortId1;

				this.wvlenParameterType = null;
				this.trclenParameterType = null;
				this.resParameterType = null;
				this.pulswdLowResParameterType = null;
				this.pulswdHiResParameterType = null;
				this.iorParameterType = null;
				this.scansParameterType = null;

				this.refreshTitles();

				MeasurementPort port = (MeasurementPort) StorableObjectPool.getStorableObject(measurementPortId1, true);
				LinkedIdsCondition linkedIdsCondition = new LinkedIdsCondition(
																				port.getType().getId(),
																				ObjectEntities.CHARACTERISTIC_CODE);
				Collection characteristics = StorableObjectPool.getStorableObjectsByCondition(linkedIdsCondition, true, true);

				if (this.traceLength == null) {
					this.traceLength = new HashMap();
				} else {
					this.traceLength.clear();
				}

				if (this.indexOfRefraction == null) {
					this.indexOfRefraction = new HashMap();
				} else {
					this.indexOfRefraction.clear();
				}

				if (this.averageCount == null) {
					this.averageCount = new HashMap();
				} else {
					this.averageCount.clear();
				}

				if (this.resolutionList == null) {
					this.resolutionList = new LinkedList();
				} else {
					this.resolutionList.clear();
				}

				if (this.pulseWidthHiResMap == null) {
					this.pulseWidthHiResMap = new HashMap();
				} else {
					this.pulseWidthHiResMap.clear();
				}

				if (this.pulseWidthLowResMap == null) {
					this.pulseWidthLowResMap = new HashMap();
				} else {
					this.pulseWidthLowResMap.clear();
				}

				Pattern pattern = Pattern.compile(CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + "(\\d+)(" //$NON-NLS-1$
						+ CharacteristicTypeCodenames.TRACE_LENGTH_SUFFIX + "|" //$NON-NLS-1$
						+ CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES_SUFFIX + "|" //$NON-NLS-1$
						+ CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES_SUFFIX + "|" //$NON-NLS-1$
						+ CharacteristicTypeCodenames.TRACE_INDEX_OF_REFRACTION_SUFFIX + "|" //$NON-NLS-1$
						+ CharacteristicTypeCodenames.TRACE_AVERAGE_COUNT_SUFFIX + ")"); //$NON-NLS-1$

				// Log.debugMessage("ReflectometryTestPanel.setMonitoredElementId
				// |
				// characteristics.size() "
				// + characteristics.size(), Log.FINEST);

				for (Iterator it = characteristics.iterator(); it.hasNext();) {
					// System.out.println();
					Characteristic characteristic = (Characteristic) it.next();
					StorableObjectType type = characteristic.getType();
					// System.out.println("characteristicType is " +
					// type.getId());
					String codename = type.getCodename();
					String value = characteristic.getValue();
					// System.out.println("codename is '" + codename + "', value
					// isO " + value);

					if (codename.equals(CharacteristicTypeCodenames.TRACE_WAVELENGTH)) {
						String[] values = value.split("\\s+"); //$NON-NLS-1$
						Arrays.sort(values, this.comparator);
						this.waveLengthComboBox.removeAllItems();
						for (int i = 0; i < values.length; i++)
							this.waveLengthComboBox.addItem(values[i]);

					} else if (codename.equals(CharacteristicTypeCodenames.TRACE_RESOLUTION)) {
						String[] values = value.split("\\s+"); //$NON-NLS-1$
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
						Matcher matcher = pattern.matcher(codename);
						if (matcher.find()) {
							String waveLength = null;
							String suffix = null;
							for (int j = 0; j <= matcher.groupCount(); j++) {
								String substring = codename.substring(matcher.start(j), matcher.end(j));
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
								Map map = null;
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

				Object selectedItem = this.waveLengthComboBox.getSelectedItem();
				String wavelength = selectedItem != null ? this.waveLengthComboBox.getSelectedItem().toString() : null;
				if (wavelength != null) {
					// System.out.println("wavelength is " + wavelength);
					if (this.traceLength != null) {
						String value = (String) this.traceLength.get(wavelength);
						if (value == null)
							throw new ObjectNotFoundException(LangModelSchedule
									.getString("Error.TraceLengthValueNotFound")); //$NON-NLS-1$
						String[] values = value.split("\\s+"); //$NON-NLS-1$
						Arrays.sort(values, this.comparator);
						this.maxDistanceComboBox.removeAllItems();
						for (int i = 0; i < values.length; i++) {
							this.maxDistanceComboBox.addItem(values[i]);
						}
					} else {
						// TODO throw exception
					}

					if (this.pulseWidthHiResMap != null) {
						String value = (String) this.pulseWidthHiResMap.get(wavelength);
						if (value != null) {
							String[] values = value.split("\\s+"); //$NON-NLS-1$
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
						String value = (String) this.pulseWidthLowResMap.get(wavelength);
						// Log.debugMessage("ReflectometryTestPanel.setMonitoredElement
						// | pulseWidthLowResMap value: " + value, Log.FINEST);
						if (value != null) {
							String[] values = value.split("\\s+"); //$NON-NLS-1$
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
						String value = (String) this.indexOfRefraction.get(wavelength);
						if (value != null) {
							String[] values = value.split("\\s+"); //$NON-NLS-1$
							Arrays.sort(values, this.comparator);
							this.refractTextField.setText(values[0]);
						} else {
							// TODO throw exception
						}
					} else {
						// TODO throw exception
					}

					if (this.averageCount != null) {
						String value = (String) this.averageCount.get(wavelength);
						if (value != null) {
							String[] values = value.split("\\s+"); //$NON-NLS-1$
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
		java.util.Set selectedTestIds = this.schedulerModel.getSelectedTestIds();
		if (selectedTestIds != null && !selectedTestIds.isEmpty()) {
			ParameterSet parameterSet = this.getSet();
			if (parameterSet.isChanged()) {
				try {
					java.util.Set storableObjects = StorableObjectPool.getStorableObjects(selectedTestIds, true);
					SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
					boolean creareNewSetup = false;

					for (Iterator iterator = storableObjects.iterator(); iterator.hasNext();) {
						Test test = (Test) iterator.next();
						if (test.isChanged()) {
							java.util.Set measurementSetupIds = test.getMeasurementSetupIds();
							if (measurementSetupIds.size() == 1) {
								MeasurementSetup measurementSetup = (MeasurementSetup) StorableObjectPool
										.getStorableObject((Identifier) measurementSetupIds.iterator().next(), true);
								if (measurementSetup.isChanged()) {
									measurementSetup.setParameterSet(parameterSet);
								} else {
									Identifier measurementSetupId = null;
									if (this.unchangedMeasurementSetupNewMap == null) {
										this.unchangedMeasurementSetupNewMap = new HashMap();

									} else {
										measurementSetupId = (Identifier) this.unchangedMeasurementSetupNewMap
												.get(measurementSetup.getId());
									}

									if (measurementSetupId == null) {
										MeasurementSetup measurementSetup1 = MeasurementSetup
												.createInstance(LoginManager.getUserId(), parameterSet,
													measurementSetup.getCriteriaSet(), measurementSetup
															.getThresholdSet(), measurementSetup.getEtalon(),
													LangModelSchedule.getString("created by Scheduler") + " /"
															+ sdf.format(new Date()) + "/", measurementSetup
															.getMeasurementDuration(), measurementSetup
															.getMonitoredElementIds(), measurementSetup
															.getMeasurementTypeIds());
										StorableObjectPool.putStorableObject(measurementSetup1);
										measurementSetupId = measurementSetup1.getId();
										this.unchangedMeasurementSetupNewMap.put(measurementSetup.getId(),
											measurementSetupId);
										creareNewSetup = true;
									}

									test.setMeasurementSetupIds(Collections.singleton(measurementSetupId));
								}
							} else {
								// TODO PROBLEM ?
							}
						}
					}

					if (creareNewSetup) {
						this.skip = true;
						this.dispatcher
								.firePropertyChange(new PropertyChangeEvent(
																			this,
																			SchedulerModel.COMMAND_ADD_NEW_MEASUREMENT_SETUP,
																			null, null));
						this.skip = false;
					}
				} catch (ApplicationException e) {
					AbstractMainFrame.showErrorMessage(this, e);
				}
			}
		}
	}

	public void setSet(ParameterSet set) {
		if (this.skip) {
			return;
		}		
		
		if (this.setId != null && this.setId.equals(set.getId())) {
			return;
		}		
		
		this.skip = true;
		if (this.unchangedObjects == null) {
			this.unchangedObjects = new HashMap();
		} else {
			this.unchangedObjects.clear();
		}		

		this.gsOptionBox.setSelected(false);
		this.lfdOptionBox.setSelected(false);

		Parameter[] setParameters = set.getParameters();
		for (int i = 0; i < setParameters.length; i++) {
			StorableObjectType type = setParameters[i].getType();
			if (type instanceof ParameterType) {
				ParameterType parameterType = (ParameterType) type;
				String codename = parameterType.getCodename();
				// Log.debugMessage("ReflectometryTestPanel.setSet | codename "
				// + codename , Log.FINEST);
				if (codename.equals(ParameterTypeCodenames.TRACE_LENGTH)) {
					selectCBValue(this.maxDistanceComboBox, setParameters[i].getStringValue());
				} else if (codename.equals(ParameterTypeCodenames.TRACE_FLAG_GAIN_SPLICE_ON)) {
					try {
						boolean b = new ByteArray(setParameters[i].getValue()).toBoolean();
						// Log.debugMessage("ReflectometryTestPanel.setSet |
						// TRACE_FLAG_GAIN_SPLICE " + b, Log.FINEST);
						this.gsOptionBox.setSelected(b);

					} catch (IOException e) {
						AbstractMainFrame.showErrorMessage(this, e);
					}
				} else if (codename.equals(ParameterTypeCodenames.TRACE_FLAG_LIVE_FIBER_DETECT)) {
					try {
						boolean b = new ByteArray(setParameters[i].getValue()).toBoolean();
						// Log.debugMessage("ReflectometryTestPanel.setSet |
						// TRACE_FLAG_LIVE_FIBER_DETECT " + b, Log.FINEST);
						this.lfdOptionBox.setSelected(b);

					} catch (IOException e) {
						AbstractMainFrame.showErrorMessage(this, e);
					}
				}

			}
		}

		for (int i = 0; i < setParameters.length; i++) {
			StorableObjectType type = setParameters[i].getType();
			if (type instanceof ParameterType) {
				ParameterType parameterType = (ParameterType) type;
				String codename = parameterType.getCodename();
				String stringValue = setParameters[i].getStringValue();
				Log.debugMessage("ReflectometryTestPanel.setSet | codename: " + codename + ", stringValue:" + stringValue, Log.FINEST);
				if (codename.equals(ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION)) {
					this.refractTextField.setText(stringValue);
				} else if (codename.equals(ParameterTypeCodenames.TRACE_WAVELENGTH)) {
					selectCBValue(this.waveLengthComboBox, stringValue);
				} else if (codename.equals(ParameterTypeCodenames.TRACE_AVERAGE_COUNT)) {
					selectCBValue(this.averageQuantityComboBox, stringValue);
				} else if (codename.equals(ParameterTypeCodenames.TRACE_RESOLUTION)) {
					selectCBValue(this.resolutionComboBox, stringValue);
				} else if (codename.equals(ParameterTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES)) {
					selectCBValue(this.pulseWidthHiResComboBox, stringValue);
					if (!this.pulseWidthCheckBox.isSelected()) {
						this.pulseWidthCheckBox.doClick();
					}
				}  else if (codename.equals(ParameterTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES)) {
					selectCBValue(this.pulseWidthLowResComboBox, stringValue);
					if (this.pulseWidthCheckBox.isSelected()) {
						this.pulseWidthCheckBox.doClick();
					}
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
		this.pulseWidthCheckBox = new JCheckBox("Use HiRes pulse width");
		this.pulseWidthHiResComboBox = new JComboBox();
		this.pulseWidthLowResComboBox = new JComboBox();
		this.resolutionComboBox = new JComboBox();
		this.averageQuantityComboBox = new JComboBox();

		this.refractLabel = new JLabel(LangModelSchedule.getString("Text.IndexOfRefraction")); //$NON-NLS-1$
		this.waveLengthLabel = new JLabel(LangModelSchedule.getString("Text.WaveLength")); //$NON-NLS-1$
		this.countOfAverageOutLabel = new JLabel(LangModelSchedule.getString("Text.AverageQuantity")); //$NON-NLS-1$
		this.resolutionLabel = new JLabel(LangModelSchedule.getString("Text.Resolution")); //$NON-NLS-1$
		this.maxDistanceLabel = new JLabel(LangModelSchedule.getString("Text.Distance")); //$NON-NLS-1$

		this.gsOptionBox = new JCheckBox();
		this.bcOptionBox = new JCheckBox();
		this.lfdOptionBox = new JCheckBox();

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
					/* 1000 m is 1 km */
					Object selectedItem = comboBox.getSelectedItem();
					if (selectedItem == null)
						return;
					double maxDistance = 1000.0 * Double.parseDouble(selectedItem.toString());
					ReflectometryTestPanel.this.resolutionComboBox.removeAllItems();
					for (Iterator it = ReflectometryTestPanel.this.resolutionList.iterator(); it.hasNext();) {
						String res = (String) it.next();
						double resolution = Double.parseDouble(res);
						if (maxDistance / resolution < ReflectometryTestPanel.this.maxPoints) {
							ReflectometryTestPanel.this.resolutionComboBox.addItem(res);
						}
					}
				}

			}
		});

		this.pulseWidthCheckBox.addActionListener(new ActionListener() {

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
		this.pulseWidthCheckBox.addActionListener(changeActionListener);
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
		
		this.bcOptionBox.setSelected(false);
		this.bcOptionBox.setEnabled(true);

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
		this.add(this.pulseWidthCheckBox, gbc);

		this.add(this.pulseWidthHiResComboBox, gbc);

		this.add(this.pulseWidthLowResComboBox, gbc);

		this.pulseWidthCheckBox.doClick();

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(this.resolutionLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		this.add(this.resolutionComboBox, gbc);

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(this.maxDistanceLabel, gbc);
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(this.maxDistanceComboBox, gbc);

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(new JLabel(LangModelSchedule.getString("Text.GainSplice")), gbc); //$NON-NLS-1$
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(this.gsOptionBox, gbc);

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(new JLabel(LangModelSchedule.getString("Text.LiveFiberDetect")), gbc); //$NON-NLS-1$
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(this.lfdOptionBox, gbc);

		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.SOUTH;
		this.add(new JLabel(), gbc);
		this.skip = false;
	}

	private void selectCBValue(	JComboBox cb,
								String value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			Object obj = cb.getItemAt(i);
			String item = obj.toString();
			double d = Double.parseDouble(item);
			double e = Double.parseDouble(value);
			if (d == e) {
				cb.setSelectedItem(obj);
				break;
			}
		}
	}

}
