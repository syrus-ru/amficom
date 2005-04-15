/*
 * ReflectometryTestPanel.java Created on 29.04.2004 13:10:12
 *  
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.SetSort;
import com.syrus.util.ByteArray;

/**
 * @version $Revision: 1.21 $, $Date: 2005/04/15 10:25:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public class ReflectometryTestPanel extends ParametersTestPanel implements ParametersTest, OperationListener {

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

	public static final String		DEFAULT_AVERAGE_QUANTITY	= "4000";
	public static final String		DEFAULT_DISTANCE			= "131072";
	public static final String		DEFAULT_PULSEWIDTH			= "5000";
	public static final String		DEFAULT_RESOLUTION			= "8";
	public static final String		DEFAULT_WAVELENGTH			= "1625";

	public static final String		TEST_TYPE					= "reflectometry";

	public static final String		CHARACTER_MAX_REFRACTION	= "Max_Coef_Preloml";
	public static final String		CHARACTER_MIN_REFRACTION	= "Min_Coef_Preloml";

	public static final String		CHARACTER_WAVE_LENGTH		= "Work_Wave_Length";
	public static final String		MAX_DISTANCE				= "trclen";					//$NON-NLS-1$

	public static final String		PANEL_NAME					= TestParametersPanel.PARAMETERS_PANEL_PREFIX
																		+ "Reflectometry";		//$NON-NLS-1$

	private static final boolean	DEBUG						= false;

	ListNumberComparator			comparator					= new ListNumberComparator();
	double							maxIndexOfRefraction		= 1.46820;
	double							minIndexOfRefraction		= 1.46820;
	Map								pulseWidthMap;
	List							resolutionList;
	Map								traceLength;
	Map								indexOfRefraction;
	Map								averageCount;

	// public static final String PARAMETER_CHAR_IDENTITY =
	// "ref_characterizationidentity";

	private ApplicationContext		aContext;

	JComboBox						maxDistanceComboBox			= new JComboBox();
	JComboBox						pulseWidthComboBox			= new JComboBox();
	JComboBox						resolutionComboBox			= new JComboBox();
	private JComboBox				averageQuantityComboBox		= new JComboBox();
	private Dispatcher				dispatcher;
	private SchedulerModel			schedulerModel;

	private JTextField				refractTextField			= new JTextField(8);

	private JComboBox				waveLengthComboBox			= new JComboBox();

	private Identifier				meId;

	private Identifier				measurementPortId;

	private ParameterType			wvlenParameterType;
	private ParameterType			trclenParameterType;
	private ParameterType			resParameterType;
	private ParameterType			pulswdParameterType;
	private ParameterType			iorParameterType;
	private ParameterType			scansParameterType;

	private JLabel					refractLabel;
	private JLabel					waveLengthLabel;
	private JLabel					countOfAverageOutLabel;
	private JLabel					pulseWidthLabel;
	private JLabel					resolutionLabel;
	private JLabel					maxDistanceLabel;

	long							maxPoints;

	public ReflectometryTestPanel() {
		init();
	}

	public ReflectometryTestPanel(ApplicationContext aContext, Identifier meId) {
		this(aContext, meId, null);
	}

	public ReflectometryTestPanel(ApplicationContext aContext, Identifier meId, Test test) {
		this.aContext = aContext;
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		initModule(aContext.getDispatcher());
		init();
		setMonitoredElementId(meId);
		setTest(test);
	}

	public String getPanelName() {
		return PANEL_NAME;
	}

	public Set getSet() {
		Set set = null;
		try {

			this.refreshTitles();
			
			SetParameter[] params = new SetParameter[6];

			ByteArray byteArray;

			Object wave = this.waveLengthComboBox.getSelectedItem();
			if (wave == null)
				throw new IllegalArgumentException(LangModelSchedule.getString("wave_length_is_not_set"));

			String waveStr = wave.toString();
			if ((waveStr == null) || (waveStr.length() == 0))
				throw new IllegalArgumentException(LangModelSchedule.getString("wave_length_is_not_set"));
			byteArray = this.getByteArray(waveStr, this.wvlenParameterType);

			params[0] = SetParameter.createInstance(this.wvlenParameterType, byteArray.getBytes());

			Object distance = this.maxDistanceComboBox.getSelectedItem();
			if (distance == null)
				throw new IllegalArgumentException(LangModelSchedule.getString("distance_is_not_set"));

			String distanceStr = distance.toString();
			if ((distanceStr == null) || (distanceStr.length() == 0))
				throw new IllegalArgumentException(LangModelSchedule.getString("distance_is_not_set"));
			byteArray = this.getByteArray(distanceStr, this.trclenParameterType);

			params[1] = SetParameter.createInstance(this.trclenParameterType, byteArray.getBytes());

			Object resolution = this.resolutionComboBox.getSelectedItem();
			if (resolution == null)
				throw new IllegalArgumentException(LangModelSchedule.getString("resolution_is_not_set"));
			String resolutionStr = resolution.toString();
			if ((resolutionStr == null) || (resolutionStr.length() == 0))
				throw new IllegalArgumentException(LangModelSchedule.getString("resolution_is_not_set"));
			byteArray = this.getByteArray(resolutionStr, this.resParameterType);

			params[2] = SetParameter.createInstance(this.resParameterType, byteArray.getBytes());

			Object pulse = this.pulseWidthComboBox.getSelectedItem();
			if (pulse == null)
				throw new IllegalArgumentException(LangModelSchedule.getString("pulse_width_is_not_set"));
			String pulseStr = pulse.toString();
			if ((pulseStr == null) || (pulseStr.length() == 0))
				throw new IllegalArgumentException(LangModelSchedule.getString("pulse_width_is_not_set"));

			byteArray = this.getByteArray(pulseStr, this.pulswdParameterType);

			params[3] = SetParameter.createInstance(this.pulswdParameterType, byteArray.getBytes());

			String refract = this.refractTextField.getText();
			if ((refract == null) || (refract.length() == 0))
				throw new IllegalArgumentException(LangModelSchedule.getString("index_of_refraction_is_not_set")); //$NON-NLS-1$
			byteArray = this.getByteArray(refract, this.iorParameterType);

			params[4] = SetParameter.createInstance(this.iorParameterType, byteArray.getBytes());

			Object average = this.averageQuantityComboBox.getSelectedItem();
			if (average == null)
				throw new IllegalArgumentException(LangModelSchedule.getString("average_quantity_is_not_set"));
			String averageStr = average.toString();
			if ((averageStr == null) || (averageStr.length() == 0))
				throw new IllegalArgumentException(LangModelSchedule.getString("average_quantity_is_not_set"));

			byteArray = this.getByteArray(averageStr, this.scansParameterType);

			params[5] = SetParameter.createInstance(this.scansParameterType, byteArray.getBytes());

			RISDSessionInfo sessionInterface = (RISDSessionInfo) this.aContext.getSessionInterface();
			if (this.meId == null)
				throw new IllegalArgumentException(LangModelSchedule.getString("Have not choosen Measurement element"));

			set = Set.createInstance(sessionInterface.getUserIdentifier(), SetSort.SET_SORT_MEASUREMENT_PARAMETERS,
				"Set created by Scheduler", params, Collections.singleton(this.meId));
			MeasurementStorableObjectPool.putStorableObject(set);

		} catch (IllegalArgumentException e) {
			String message = e.getMessage();
			// System.out.println(message);
			Environment.log(Environment.LOG_LEVEL_WARNING, message);
			SchedulerModel.showErrorMessage(this, e);
			set = null;
		} catch (ApplicationException ae) {
			SchedulerModel.showErrorMessage(this, ae);
			this.schedulerModel.setBreakData();
		}
		return set;
	}

	private void refreshTitles() {

		if (this.wvlenParameterType != null && this.trclenParameterType != null && this.resParameterType != null
				&& this.pulswdParameterType != null && this.iorParameterType != null && this.scansParameterType != null)
			return;

		try {
		TypicalCondition waveLengthCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_WAVELENGTH,
																	OperationSort.OPERATION_EQUALS,
																	ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																	StorableObjectWrapper.COLUMN_CODENAME);

		TypicalCondition traceLengthCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_LENGTH,
																		OperationSort.OPERATION_EQUALS,
																		ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																		StorableObjectWrapper.COLUMN_CODENAME);

		TypicalCondition resolutionCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_RESOLUTION,
																	OperationSort.OPERATION_EQUALS,
																	ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																	StorableObjectWrapper.COLUMN_CODENAME);

		TypicalCondition pulseWidthCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_PULSE_WIDTH,
																	OperationSort.OPERATION_EQUALS,
																	ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																	StorableObjectWrapper.COLUMN_CODENAME);

		TypicalCondition indexOfRefractionCondition = new TypicalCondition(
																			ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION,
																			OperationSort.OPERATION_EQUALS,
																			ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																			StorableObjectWrapper.COLUMN_CODENAME);

		TypicalCondition averageCountCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_AVERAGE_COUNT,
																		OperationSort.OPERATION_EQUALS,
																		ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																		StorableObjectWrapper.COLUMN_CODENAME);

		java.util.Set conditions = new HashSet(6);
		conditions.add(waveLengthCondition);
		conditions.add(traceLengthCondition);
		conditions.add(resolutionCondition);
		conditions.add(pulseWidthCondition);
		conditions.add(indexOfRefractionCondition);
		conditions.add(averageCountCondition);

		CompoundCondition compoundCondition = new CompoundCondition(conditions, CompoundConditionSort.OR);

		Collection parameterTypes = GeneralStorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);

		for (Iterator iter = parameterTypes.iterator(); iter.hasNext();) {
			ParameterType parameterType = (ParameterType) iter.next();
			String codeName = parameterType.getCodename();
			if (codeName.equals(waveLengthCondition.getValue()))
				this.wvlenParameterType = parameterType;
			else if (codeName.equals(traceLengthCondition.getValue()))
				this.trclenParameterType = parameterType;
			else if (codeName.equals(resolutionCondition.getValue()))
				this.resParameterType = parameterType;
			else if (codeName.equals(pulseWidthCondition.getValue()))
				this.pulswdParameterType = parameterType;
			else if (codeName.equals(indexOfRefractionCondition.getValue()))
				this.iorParameterType = parameterType;
			else if (codeName.equals(averageCountCondition.getValue()))
				this.scansParameterType = parameterType;
		}
		
		if (this.wvlenParameterType == null) { throw new IllegalArgumentException("Cannot find parameter type "
				+ ParameterTypeCodenames.TRACE_WAVELENGTH); }

		if (this.trclenParameterType == null)
			throw new IllegalArgumentException("Cannot find parameter type " + ParameterTypeCodenames.TRACE_LENGTH);
		if (this.trclenParameterType == null)
			throw new IllegalArgumentException("Cannot find parameter type " + ParameterTypeCodenames.TRACE_RESOLUTION);

		if (this.pulswdParameterType == null)
			throw new IllegalArgumentException("Cannot find parameter type " + ParameterTypeCodenames.TRACE_PULSE_WIDTH);

		if (this.iorParameterType == null)
			throw new IllegalArgumentException("Cannot find parameter type "
					+ ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION);

		if (this.scansParameterType == null)
			throw new IllegalArgumentException("Cannot find parameter type "
					+ ParameterTypeCodenames.TRACE_AVERAGE_COUNT);

		this.refractLabel.setText(this.iorParameterType.getDescription() + this.getUnit(this.iorParameterType));
		this.waveLengthLabel.setText(this.wvlenParameterType.getDescription() + this.getUnit(this.wvlenParameterType));
		System.out.println(this.wvlenParameterType.getDescription() + this.getUnit(this.wvlenParameterType));
		this.countOfAverageOutLabel.setText(this.scansParameterType.getDescription() + this.getUnit(this.scansParameterType));
		this.pulseWidthLabel.setText(this.pulswdParameterType.getDescription() + this.getUnit(this.pulswdParameterType));
		this.resolutionLabel.setText(this.resParameterType.getDescription() + this.getUnit(this.resParameterType));
		this.maxDistanceLabel.setText(this.trclenParameterType.getDescription() + this.getUnit(this.trclenParameterType));
		}catch(ApplicationException exception) {
			SchedulerModel.showErrorMessage(this, exception);
		}
	}

	private String getUnit(ParameterType parameterType) {
		java.util.Set characteristics = parameterType.getCharacteristics();
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			Characteristic characteristic = (Characteristic) it.next();
			if (characteristic.getType().getCodename().startsWith(CharacteristicTypeCodenames.UNITS_PREFIX)) {
				String value = characteristic.getValue();
				if (value != null && value.trim().length() != 0) { 
					return ',' + characteristic.getValue(); 
					}

				return "";
			}
		}
		return "";
	}

	private ByteArray getByteArray(	String value,
									ParameterType parameterType) {
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
		}
		return byteArray;
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equals(SchedulerModel.COMMAND_CHANGE_ME_TYPE)) {
			this.setMonitoredElementId((Identifier) ae.getSource());
		}
	}

	public void setMonitoredElementId(Identifier meId) {
		if (meId.getMajor() != ObjectEntities.ME_ENTITY_CODE)
			return;
		this.meId = meId;
		try {
			MonitoredElement me = (MonitoredElement) ConfigurationStorableObjectPool.getStorableObject(meId, true);
			Identifier measurementPortId = me.getMeasurementPortId();
			if (this.measurementPortId != null && this.measurementPortId.equals(measurementPortId))
				return;
			this.measurementPortId = measurementPortId;
			
			this.wvlenParameterType = null;
			this.trclenParameterType = null;
			this.resParameterType = null;
			this.pulswdParameterType = null;
			this.iorParameterType = null;
			this.scansParameterType = null;
			
			this.refreshTitles();
			
			MeasurementPort port = (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(
				measurementPortId, true);
			LinkedIdsCondition linkedIdsCondition = new LinkedIdsCondition(port.getType().getId(),
																			ObjectEntities.CHARACTERISTIC_ENTITY_CODE);
			Collection characteristics = GeneralStorableObjectPool.getStorableObjectsByCondition(linkedIdsCondition,
				true);

			if (this.traceLength == null)
				this.traceLength = new HashMap();
			else
				this.traceLength.clear();

			if (this.indexOfRefraction == null)
				this.indexOfRefraction = new HashMap();
			else
				this.indexOfRefraction.clear();

			if (this.averageCount == null)
				this.averageCount = new HashMap();
			else
				this.averageCount.clear();

			if (this.resolutionList == null)
				this.resolutionList = new LinkedList();
			else
				this.resolutionList.clear();

			if (this.pulseWidthMap == null)
				this.pulseWidthMap = new HashMap();
			else
				this.pulseWidthMap.clear();

			Pattern pattern = Pattern.compile(CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + "(\\d+)("
					+ CharacteristicTypeCodenames.TRACE_LENGTH_SUFFIX + "|"
					+ CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_SUFFIX + "|"
					+ CharacteristicTypeCodenames.TRACE_INDEX_OF_REFRACTION_SUFFIX + "|"
					+ CharacteristicTypeCodenames.TRACE_AVERAGE_COUNT_SUFFIX + ")");
			for (Iterator it = characteristics.iterator(); it.hasNext();) {
				System.out.println();
				Characteristic characteristic = (Characteristic) it.next();
				StorableObjectType type = characteristic.getType();
				System.out.println("characteristicType is " + type.getId());
				String codename = type.getCodename();
				String value = characteristic.getValue();
				System.out.println("codename is '" + codename + "', value is " + value);

				if (codename.equals(CharacteristicTypeCodenames.TRACE_WAVELENGTH)) {
					String[] values = value.split("\\s+");
					Arrays.sort(values, this.comparator);
					this.waveLengthComboBox.removeAllItems();
					for (int i = 0; i < values.length; i++)
						this.waveLengthComboBox.addItem(values[i]);

				} else if (codename.equals(CharacteristicTypeCodenames.TRACE_RESOLUTION)) {
					String[] values = value.split("\\s+");
					Arrays.sort(values, this.comparator);
					this.resolutionComboBox.removeAllItems();
					for (int i = 0; i < values.length; i++) {
						this.resolutionList.add(values[i]);
						this.resolutionComboBox.addItem(values[i]);
					}

				} else if (codename.equals(CharacteristicTypeCodenames.TRACE_MAXPOINTS)) {
					this.maxPoints = Long.parseLong(value);
				} else if (codename.equals(CHARACTER_MAX_REFRACTION)) {
					try {
						this.maxIndexOfRefraction = Double.parseDouble(value);
					} catch (NumberFormatException nfe) {
						this.maxIndexOfRefraction = 0.0;
					}

				} else if (codename.equals(CHARACTER_MIN_REFRACTION)) {
					try {
						this.minIndexOfRefraction = Double.parseDouble(value);
					} catch (NumberFormatException nfe) {
						this.minIndexOfRefraction = 0.0;
					}
				} else {
					Matcher matcher = pattern.matcher(codename);
					if (matcher.find()) {
						String waveLength = null;
						String suffix = null;
						for (int j = 0; j <= matcher.groupCount(); j++) {
							String substring = codename.substring(matcher.start(j), matcher.end(j));
							System.out.println("j:" + j + "\t" + substring);
							switch (j) {
								case 1:
									waveLength = substring;
									break;
								case 2:
									suffix = substring;
									break;
							}
						}
						if ((waveLength != null) && (suffix != null)) {
							Map map = null;
							if (suffix.equals(CharacteristicTypeCodenames.TRACE_LENGTH_SUFFIX))
								map = this.traceLength;
							else if (suffix.equals(CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_SUFFIX)) {
								map = this.pulseWidthMap;
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

			String wavelength = this.waveLengthComboBox.getSelectedItem().toString();
			if (wavelength != null) {
				System.out.println("wavelength is " + wavelength);
				{
					String value = (String) this.traceLength.get(wavelength);
					System.out.println(value);
					String[] values = value.split("\\s+");
					Arrays.sort(values, this.comparator);
					this.maxDistanceComboBox.removeAllItems();
					for (int i = 0; i < values.length; i++)
						this.maxDistanceComboBox.addItem(values[i]);
				}

				{
					String value = (String) this.pulseWidthMap.get(wavelength);
					String[] values = value.split("\\s+");
					Arrays.sort(values, this.comparator);
					this.pulseWidthComboBox.removeAllItems();
					for (int i = 0; i < values.length; i++)
						this.pulseWidthComboBox.addItem(values[i]);
				}

				{
					String value = (String) this.indexOfRefraction.get(wavelength);
					String[] values = value.split("\\s+");
					Arrays.sort(values, this.comparator);
					this.refractTextField.setText(values[0]);
				}

				{
					String value = (String) this.averageCount.get(wavelength);
					String[] values = value.split("\\s+");
					Arrays.sort(values, this.comparator);
					this.averageQuantityComboBox.removeAllItems();
					for (int i = 0; i < values.length; i++)
						this.averageQuantityComboBox.addItem(values[i]);
				}
			}

		} catch (ApplicationException ae) {
			SchedulerModel.showErrorMessage(this, ae);
		}

	}

	public void setTest(Test test) {
		if (test != null) {
			/**
			 * FIXME how set for many measurementSetup ???
			 */
			for (Iterator it = test.getMeasurementSetupIds().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				try {
					MeasurementSetup measurementSetup = (MeasurementSetup) MeasurementStorableObjectPool
							.getStorableObject(id, true);
					setSet(measurementSetup.getParameterSet());
				} catch (ApplicationException ae) {
					SchedulerModel.showErrorMessage(this, ae);
				}
			}
		}

	}

	public void setSet(Set set) {
		if (set == null) {
			Environment.log(Environment.LOG_LEVEL_WARNING, "Set is null");
			return;
		}

		SetParameter[] setParameters = set.getParameters();
		for (int i = 0; i < setParameters.length; i++) {
			StorableObjectType type = setParameters[i].getType();
			if (type instanceof ParameterType) {
				ParameterType parameterType = (ParameterType) type;
				String codename = parameterType.getCodename();
				if (codename.equals(ParameterTypeCodenames.TRACE_LENGTH)) {
					selectCBValue(this.maxDistanceComboBox, setParameters[i].getStringValue());
				}

			}
		}

		for (int i = 0; i < setParameters.length; i++) {
			StorableObjectType type = setParameters[i].getType();
			if (type instanceof ParameterType) {
				ParameterType parameterType = (ParameterType) type;
				String codename = parameterType.getCodename();
				if (codename.equals(ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION)) {
					this.refractTextField.setText(setParameters[i].getStringValue());
				} else if (codename.equals(ParameterTypeCodenames.TRACE_WAVELENGTH)) {
					selectCBValue(this.waveLengthComboBox, setParameters[i].getStringValue());
				} else if (codename.equals(ParameterTypeCodenames.TRACE_AVERAGE_COUNT)) {
					selectCBValue(this.averageQuantityComboBox, setParameters[i].getStringValue());
				} else if (codename.equals(ParameterTypeCodenames.TRACE_RESOLUTION)) {
					selectCBValue(this.resolutionComboBox, setParameters[i].getStringValue());
				} else if (codename.equals(ParameterTypeCodenames.TRACE_PULSE_WIDTH)) {
					selectCBValue(this.pulseWidthComboBox, setParameters[i].getStringValue());
				}
			}
		}
	}

	private void init() {
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new GridBagLayout());
		// refractTextField.setText("1.467"); //$NON-NLS-1$

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

		this.maxDistanceComboBox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
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
		});

		this.refractLabel = new JLabel(LangModelSchedule.getString("Index_Of_Refraction")); //$NON-NLS-1$
		this.waveLengthLabel = new JLabel(LangModelSchedule.getString("WaveLength")); //$NON-NLS-1$
		this.countOfAverageOutLabel = new JLabel(LangModelSchedule.getString("AverageQuantity")); //$NON-NLS-1$
		this.pulseWidthLabel = new JLabel(LangModelSchedule.getString("PulseWidth")); //$NON-NLS-1$
		this.resolutionLabel = new JLabel(LangModelSchedule.getString("Resolution")); //$NON-NLS-1$
		this.maxDistanceLabel = new JLabel(LangModelSchedule.getString("Distance")); //$NON-NLS-1$

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
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		this.add(this.pulseWidthLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		this.add(this.pulseWidthComboBox, gbc);
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
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.SOUTH;
		this.add(new JLabel(), gbc);

	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, SchedulerModel.COMMAND_CHANGE_ME_TYPE);
	}

	public void unregisterDispatcher() {
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CHANGE_ME_TYPE);
	}

	private void selectCBValue(	JComboBox cb,
								String value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			Object obj = cb.getItemAt(i);
			String item = obj.toString();
			if (DEBUG)
				System.out.println("item:" + item); //$NON-NLS-1$
			if (item.equals(value)) {
				cb.setSelectedItem(obj);
				if (DEBUG)
					System.out.println("selected"); //$NON-NLS-1$
				break;
			}
		}
	}

}
