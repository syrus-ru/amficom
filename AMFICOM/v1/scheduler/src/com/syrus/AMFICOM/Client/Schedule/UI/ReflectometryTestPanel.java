/*
 * ReflectometryTestPanel.java Created on 29.04.2004 13:10:12
 *  
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
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
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.SetSort;
import com.syrus.util.ByteArray;

/**
 * @author Vladimir Dolzhenko
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
	public static final String		MAX_DISTANCE				= "trclen";						//$NON-NLS-1$

	public static final String		PANEL_NAME					= TestParametersPanel.PARAMETERS_PANEL_PREFIX
																		+ "Reflectometry";			//$NON-NLS-1$

	public static final String		PULSE_WIDTH					= "pulswd";						//$NON-NLS-1$
	public static final String		RESOLUTION					= "res";							//$NON-NLS-1$
	public static final String		PARAMETER_PREFIX			= "ref_";
	public static final String		PARAMETER_AVERAGE_QUANTITY	= "ref_scans";						//$NON-NLS-1$
	public static final String		PARAMETER_MAX_DISTANCE		= PARAMETER_PREFIX + MAX_DISTANCE;	//$NON-NLS-1$	

	public static final String		PARAMETER_PULSE_WIDTH		= PARAMETER_PREFIX + PULSE_WIDTH;	//$NON-NLS-1$
	public static final String		PARAMETER_REFRACTION		= "ref_ior";						//$NON-NLS-1$
	public static final String		PARAMETER_RESOLUTION		= PARAMETER_PREFIX + RESOLUTION;	//$NON-NLS-1$
	public static final String		PARAMETER_WAVELENGHT		= "ref_wvlen";						//$NON-NLS-1$

	private static final boolean	DEBUG						= false;

	ListNumberComparator			comparator					= new ListNumberComparator();
	AComboBox						maxDistanceComboBox			= new AComboBox();
	double							maxIndexOfRefraction		= 1.46820;
	double							minIndexOfRefraction		= 1.46820;
	AComboBox						pulseWidthComboBox			= new AComboBox();
	AComboBox						resolutionComboBox			= new AComboBox();
	Map								pulseWidthMap;
	Map								resolutionMap;
	Map								traceLength;
	Map								indexOfRefraction;
	Map								averageCount;

	// public static final String PARAMETER_CHAR_IDENTITY =
	// "ref_characterizationidentity";

	private ApplicationContext		aContext;

	private AComboBox				averageQuantityComboBox		= new AComboBox();
	private Dispatcher				dispatcher;
	private SchedulerModel			schedulerModel;

	private JTextField				refractTextField			= new JTextField();

	private Test					test;
	private AComboBox				waveLengthComboBox			= new AComboBox();

	private Identifier				meId;

	public ReflectometryTestPanel() {
		init();
	}

	public ReflectometryTestPanel(ApplicationContext aContext, MeasurementPort port) {
		this(aContext, port, null);
	}

	public ReflectometryTestPanel(ApplicationContext aContext, MeasurementPort port, Test test) {
		this.aContext = aContext;
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		initModule(aContext.getDispatcher());
		init();
		setPort(port);
		setTest(test);
	}

	public String getPanelName() {
		return PANEL_NAME;
	}

	public Set getSet() {
		Set set = null;
		try {
			MeasurementType measurementType;
			if (this.test == null) {
				TypicalCondition typicalCondition = new TypicalCondition(TEST_TYPE, OperationSort.OPERATION_EQUALS,
																			ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE,
																			StorableObjectWrapper.COLUMN_CODENAME);
				measurementType = (MeasurementType) (MeasurementStorableObjectPool.getStorableObjectsByCondition(
					typicalCondition, true).iterator().next());
			} else {
				measurementType = this.test.getMeasurementType();
			}

			// if (test_setup_id.equals(""))
			{
				try {

						TypicalCondition waveLengthCondition = new TypicalCondition(
																					ParameterTypeCodenames.TRACE_WAVELENGTH,
																					OperationSort.OPERATION_EQUALS,
																					ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																					StorableObjectWrapper.COLUMN_CODENAME);

						TypicalCondition traceLengthCondition = new TypicalCondition(
																						ParameterTypeCodenames.TRACE_LENGTH,
																						OperationSort.OPERATION_EQUALS,
																						ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																						StorableObjectWrapper.COLUMN_CODENAME);

						TypicalCondition resolutionCondition = new TypicalCondition(
																					ParameterTypeCodenames.TRACE_RESOLUTION,
																					OperationSort.OPERATION_EQUALS,
																					ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																					StorableObjectWrapper.COLUMN_CODENAME);

						TypicalCondition pulseWidthCondition = new TypicalCondition(
																					ParameterTypeCodenames.TRACE_PULSE_WIDTH,
																					OperationSort.OPERATION_EQUALS,
																					ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																					StorableObjectWrapper.COLUMN_CODENAME);

						TypicalCondition indexOfRefractionCondition = new TypicalCondition(
																							ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION,
																							OperationSort.OPERATION_EQUALS,
																							ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																							StorableObjectWrapper.COLUMN_CODENAME);

						TypicalCondition averageCountCondition = new TypicalCondition(
																						ParameterTypeCodenames.TRACE_AVERAGE_COUNT,
																						OperationSort.OPERATION_EQUALS,
																						ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																						StorableObjectWrapper.COLUMN_CODENAME);

						Collection conditions = new ArrayList(6);
						conditions.add(waveLengthCondition);
						conditions.add(traceLengthCondition);
						conditions.add(resolutionCondition);
						conditions.add(pulseWidthCondition);
						conditions.add(indexOfRefractionCondition);
						conditions.add(averageCountCondition);

						CompoundCondition compoundCondition = new CompoundCondition(conditions,
																					CompoundConditionSort.OR);
						
						
						Collection parameterTypes = GeneralStorableObjectPool.getStorableObjectsByCondition(
							compoundCondition, true);
						
						ParameterType wvlenParameterType = null;
						ParameterType trclenParameterType = null;
						ParameterType resParameterType = null;
						ParameterType pulswdParameterType = null;
						ParameterType iorParameterType = null;
						ParameterType scansParameterType = null;
						
						for (Iterator iter = parameterTypes.iterator(); iter.hasNext();) {
							ParameterType parameterType = (ParameterType) iter.next();
							String codeName = parameterType.getCodename();
							if (codeName.equals(waveLengthCondition.getValue()))
								wvlenParameterType = parameterType;
							else if (codeName.equals(traceLengthCondition.getValue()))
								trclenParameterType = parameterType; 
							else if (codeName.equals(resolutionCondition.getValue()))
								resParameterType = parameterType;
							else if (codeName.equals(pulseWidthCondition.getValue()))
								pulswdParameterType = parameterType;
							else if (codeName.equals(indexOfRefractionCondition.getValue()))
								iorParameterType = parameterType;
							else if (codeName.equals(averageCountCondition.getValue()))
								scansParameterType = parameterType;		
						}	
						

					if (wvlenParameterType == null)
						throw new IllegalArgumentException("Cannot find parameter type "
								+ ParameterTypeCodenames.TRACE_WAVELENGTH);

					if (trclenParameterType == null)
						throw new IllegalArgumentException("Cannot find parameter type "
								+ ParameterTypeCodenames.TRACE_LENGTH);
					if (trclenParameterType == null)
						throw new IllegalArgumentException("Cannot find parameter type "
								+ ParameterTypeCodenames.TRACE_RESOLUTION);

					if (pulswdParameterType == null)
						throw new IllegalArgumentException("Cannot find parameter type "
								+ ParameterTypeCodenames.TRACE_PULSE_WIDTH);

					if (iorParameterType == null)
						throw new IllegalArgumentException("Cannot find parameter type "
								+ ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION);

					if (scansParameterType == null)
						throw new IllegalArgumentException("Cannot find parameter type "
								+ ParameterTypeCodenames.TRACE_AVERAGE_COUNT);

					SetParameter[] params = new SetParameter[6];

					ByteArray byteArray;

					Object wave = this.waveLengthComboBox.getSelectedItem();
					if (wave == null)
						throw new IllegalArgumentException(LangModelSchedule.getString("wave_length_is_not_set"));

					String waveStr = wave.toString();
					if ((waveStr == null) || (waveStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule.getString("wave_length_is_not_set"));
					byteArray = new ByteArray(((int) Double.parseDouble(waveStr)));

					params[0] = SetParameter.createInstance(wvlenParameterType, byteArray.getBytes());

					Object distance = this.maxDistanceComboBox.getSelectedItem();
					if (distance == null)
						throw new IllegalArgumentException(LangModelSchedule.getString("distance_is_not_set"));

					String distanceStr = distance.toString();
					if ((distanceStr == null) || (distanceStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule.getString("distance_is_not_set"));
					byteArray = new ByteArray(Double.parseDouble(distanceStr));

					params[1] = SetParameter.createInstance(trclenParameterType, byteArray.getBytes());

					Object resolution = this.resolutionComboBox.getSelectedItem();
					if (resolution == null)
						throw new IllegalArgumentException(LangModelSchedule.getString("resolution_is_not_set"));
					String resolutionStr = resolution.toString();
					if ((resolutionStr == null) || (resolutionStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule.getString("resolution_is_not_set"));
					byteArray = new ByteArray(Double.parseDouble(resolutionStr));

					params[2] = SetParameter.createInstance(resParameterType, byteArray.getBytes());

					Object pulse = this.pulseWidthComboBox.getSelectedItem();
					if (pulse == null)
						throw new IllegalArgumentException(LangModelSchedule.getString("pulse_width_is_not_set"));
					String pulseStr = pulse.toString();
					if ((pulseStr == null) || (pulseStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule.getString("pulse_width_is_not_set"));

					byteArray = new ByteArray(Long.parseLong(pulseStr));

					params[3] = SetParameter.createInstance(pulswdParameterType, byteArray.getBytes());

					String refract = this.refractTextField.getText();
					if ((refract == null) || (refract.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule
								.getString("index_of_refraction_is_not_set")); //$NON-NLS-1$
					byteArray = new ByteArray(Double.parseDouble(refract));

					params[4] = SetParameter.createInstance(iorParameterType, byteArray.getBytes());

					Object average = this.averageQuantityComboBox.getSelectedItem();
					if (average == null)
						throw new IllegalArgumentException(LangModelSchedule.getString("average_quantity_is_not_set"));
					String averageStr = average.toString();
					if ((averageStr == null) || (averageStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule.getString("average_quantity_is_not_set"));

					byteArray = new ByteArray(Double.parseDouble(averageStr));

					params[5] = SetParameter.createInstance(scansParameterType, byteArray.getBytes());

					RISDSessionInfo sessionInterface = (RISDSessionInfo) this.aContext.getSessionInterface();
					set = Set.createInstance(sessionInterface.getUserIdentifier(),
						SetSort.SET_SORT_MEASUREMENT_PARAMETERS, "Set created by Scheduler", params, Collections
								.singletonList(this.meId));
					MeasurementStorableObjectPool.putStorableObject(set);

				} catch (IOException ioe) {
					ioe.printStackTrace();
				} catch (IllegalArgumentException e) {
					String message = e.getMessage();
					// System.out.println(message);
					Environment.log(Environment.LOG_LEVEL_WARNING, message);
					SchedulerModel.showErrorMessage(this, e);
					set = null;
				}
			}
		} catch (ApplicationException ae) {
			SchedulerModel.showErrorMessage(this, ae);
			this.schedulerModel.setBreakData();
		}
		return set;
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		// Object obj = ae.getSource();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equals(SchedulerModel.COMMAND_CHANGE_ME_TYPE)) {
			this.meId = (Identifier) ae.getSource();
		}
	}

	public void setPort(MeasurementPort port) {
		try {
			LinkedIdsCondition linkedIdsCondition = new LinkedIdsCondition(port.getId(),
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

			if (this.resolutionMap == null)
				this.resolutionMap = new HashMap();
			else
				this.resolutionMap.clear();

			if (this.pulseWidthMap == null)
				this.pulseWidthMap = new HashMap();
			else
				this.pulseWidthMap.clear();

			Pattern pattern = Pattern.compile(ParameterTypeCodenames.TRACE_WAVELENGTH + "_(\\d+)_("
					+ ParameterTypeCodenames.TRACE_LENGTH + "|" + ParameterTypeCodenames.TRACE_PULSE_WIDTH + "|"
					+ ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION + "|"
					+ ParameterTypeCodenames.TRACE_AVERAGE_COUNT + ")");
			for (Iterator it = characteristics.iterator(); it.hasNext();) {
				Characteristic characteristic = (Characteristic) it.next();
				String name = characteristic.getName();
				String value = characteristic.getValue();
				// System.out.println(characteristic.getId() + "\t" +
				// characteristic.getName() + "\t"
				// + characteristic.getValue());
				if (name.equals(PARAMETER_AVERAGE_QUANTITY)) {
					String[] values = value.split("\\s+");
					Arrays.sort(values, this.comparator);
					this.averageQuantityComboBox.removeAllItems();
					for (int i = 0; i < values.length; i++)
						this.averageQuantityComboBox.addItem(values[i]);

				} else if (name.equals(ParameterTypeCodenames.TRACE_WAVELENGTH)) {
					String[] values = value.split("\\s+");
					Arrays.sort(values, this.comparator);
					this.waveLengthComboBox.removeAllItems();
					for (int i = 0; i < values.length; i++)
						this.waveLengthComboBox.addItem(values[i]);

				} else if (name.equals(ParameterTypeCodenames.TRACE_RESOLUTION)) {
					String[] values = value.split("\\s+");
					Arrays.sort(values, this.comparator);
					this.resolutionComboBox.removeAllItems();
					for (int i = 0; i < values.length; i++)
						this.resolutionComboBox.addItem(values[i]);

				} else if (name.equals(CHARACTER_MAX_REFRACTION)) {
					try {
						this.maxIndexOfRefraction = Double.parseDouble(value);
					} catch (NumberFormatException nfe) {
						this.maxIndexOfRefraction = 0.0;
					}

				} else if (name.equals(CHARACTER_MIN_REFRACTION)) {
					try {
						this.minIndexOfRefraction = Double.parseDouble(value);
					} catch (NumberFormatException nfe) {
						this.minIndexOfRefraction = 0.0;
					}
				} else {
					Matcher matcher = pattern.matcher(name);
					if (matcher.find()) {
						String waveLength = null;
						String suffix = null;
						for (int j = 0; j <= matcher.groupCount(); j++) {
							String substring = name.substring(matcher.start(j), matcher.end(j));
							// System.out.println("j:"+j+"\t"+substring);
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
							if (suffix.equals(ParameterTypeCodenames.TRACE_LENGTH))
								map = this.traceLength;
							else if (suffix.equals(ParameterTypeCodenames.TRACE_PULSE_WIDTH)) {
								map = this.pulseWidthMap;
							} else if (suffix.equals(ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION)) {
								map = this.indexOfRefraction;
							} else if (suffix.equals(ParameterTypeCodenames.TRACE_AVERAGE_COUNT)) {
								map = this.averageCount;
							}
							if (map != null) {
								map.put(waveLength, value);
							}
						}
					}
				}
			}

			int wavelength = (int) Double.parseDouble(this.waveLengthComboBox.getSelectedItem().toString());
			{
				String value = (String) this.traceLength.get(Integer.toString(wavelength));
				String[] values = value.split("\\s+");
				Arrays.sort(values, this.comparator);
				this.maxDistanceComboBox.removeAllItems();
				for (int i = 0; i < values.length; i++)
					this.maxDistanceComboBox.addItem(values[i]);
			}

			{
				String value = (String) this.pulseWidthMap.get(Integer.toString(wavelength));
				String[] values = value.split("\\s+");
				Arrays.sort(values, this.comparator);
				this.pulseWidthComboBox.removeAllItems();
				for (int i = 0; i < values.length; i++)
					this.pulseWidthComboBox.addItem(values[i]);
			}

			{
				String value = (String) this.indexOfRefraction.get(Integer.toString(wavelength));
				String[] values = value.split("\\s+");
				Arrays.sort(values, this.comparator);
				this.refractTextField.setText(values[0]);
			}

			{
				String value = (String) this.averageCount.get(Integer.toString(wavelength));
				String[] values = value.split("\\s+");
				Arrays.sort(values, this.comparator);
				this.averageQuantityComboBox.removeAllItems();
				for (int i = 0; i < values.length; i++)
					this.averageQuantityComboBox.addItem(values[i]);
			}

			/*
			 * if (this.waveLengthComboBox.getItemCount() == 0)
			 * this.waveLengthComboBox.addItem(DEFAULT_WAVELENGTH);
			 * 
			 * if (this.averageQuantityComboBox.getItemCount() == 0)
			 * this.averageQuantityComboBox.addItem(DEFAULT_AVERAGE_QUANTITY);
			 * this.refractTextField.setText(Double.toString(this.minIndexOfRefraction));
			 * if (this.resolutionMap.keySet().size() ==
			 * this.pulseWidthMap.keySet().size()) {
			 * 
			 * java.util.Set set = this.resolutionMap.keySet(); Object[] values =
			 * set.toArray(); Arrays.sort(values, this.comparator); if
			 * (this.resolutionMap.keySet().size() == 0) {
			 * this.resolutionMap.put(DEFAULT_DISTANCE, DEFAULT_RESOLUTION);
			 * this.pulseWidthMap.put(DEFAULT_DISTANCE, DEFAULT_PULSEWIDTH); }
			 * if (values.length == 0) values = new String[] {
			 * DEFAULT_DISTANCE}; this.maxDistanceComboBox.removeAllItems(); for
			 * (int i = 0; i < values.length; i++)
			 * this.maxDistanceComboBox.addItem(values[i].toString());
			 * this.maxDistanceComboBox.setSelectedIndex(0); }
			 */
		} catch (ApplicationException ae) {
			SchedulerModel.showErrorMessage(this, ae);
		}

	}

	public void setTest(Test test) {
		if (test != null) {
			/**
			 * FIXME fix set the same test
			 */
			{
				this.test = test;
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

	}

	public void setSet(Set set) {
		if (set == null) {
			Environment.log(Environment.LOG_LEVEL_WARNING, "Set is null");
			return;
		}

		try {

			SetParameter[] setParameters = set.getParameters();
			for (int i = 0; i < setParameters.length; i++) {
				StorableObjectType type = setParameters[i].getType();
				if (type instanceof ParameterType) {
					ParameterType parameterType = (ParameterType) type;
					String codename = parameterType.getCodename();
					byte[] value = setParameters[i].getValue();
					if (codename.equals(ParameterTypeCodenames.TRACE_LENGTH)) {
						double length = new ByteArray(value).toDouble();
						if (DEBUG)
							System.out.println("length:" + length); //$NON-NLS-1$
						selectCBValue(this.maxDistanceComboBox, length);
					}

				}
			}

			for (int i = 0; i < setParameters.length; i++) {
				StorableObjectType type = setParameters[i].getType();
				if (type instanceof ParameterType) {
					ParameterType parameterType = (ParameterType) type;
					String codename = parameterType.getCodename();
					byte[] value = setParameters[i].getValue();

					if (codename.equals(ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION)) {
						double refraction = new ByteArray(value).toDouble();
						if (DEBUG)
							System.out.println("refraction:" + refraction); //$NON-NLS-1$
						this.refractTextField.setText(new Double(refraction).toString());
					} else if (codename.equals(ParameterTypeCodenames.TRACE_WAVELENGTH)) {
						int wavelength = new ByteArray(value).toInt();
						if (DEBUG)
							System.out.println("wavelength:" + wavelength); //$NON-NLS-1$
						selectCBValue(this.waveLengthComboBox, wavelength);
					} else if (codename.equals(ParameterTypeCodenames.TRACE_AVERAGE_COUNT)) {
						double averages = (int) new ByteArray(value).toDouble();
						if (DEBUG)
							System.out.println("averages:" + averages); //$NON-NLS-1$
						selectCBValue(this.averageQuantityComboBox, averages);
					} else if (codename.equals(ParameterTypeCodenames.TRACE_RESOLUTION)) {
						double resolution = new ByteArray(value).toDouble();
						if (DEBUG)
							System.out.println("resolution:" + resolution); //$NON-NLS-1$
						selectCBValue(this.resolutionComboBox, resolution);
					} else if (codename.equals(ParameterTypeCodenames.TRACE_PULSE_WIDTH)) {
						// double pulsewidth = new
						// ByteArray(p.value).toDouble();
						long pulsewidth = new ByteArray(value).toLong();
						if (DEBUG)
							System.out.println("pulsewidth:" + pulsewidth); //$NON-NLS-1$
						selectCBValue(this.pulseWidthComboBox, pulsewidth);
					}
				}
			}

		} catch (IOException ioe) {
			Environment.log(Environment.LOG_LEVEL_WARNING, ioe.getMessage());
		}
	}

	private void init() {
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new GridBagLayout());
		// refractTextField.setText("1.467"); //$NON-NLS-1$
		// maxDistanceComboBox
		{
			Dimension d = new Dimension(75, 20);
			UIStorage.setRigidSize(this.refractTextField, d);
			UIStorage.setRigidSize(this.waveLengthComboBox, d);
			UIStorage.setRigidSize(this.averageQuantityComboBox, d);
			UIStorage.setRigidSize(this.pulseWidthComboBox, d);
			UIStorage.setRigidSize(this.resolutionComboBox, d);
			UIStorage.setRigidSize(this.maxDistanceComboBox, d);

		}

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

		// this.maxDistanceComboBox.addItemListener(new ItemListener() {
		//
		// public void itemStateChanged(ItemEvent e) {
		// if (e.getStateChange() == ItemEvent.SELECTED) {
		// AComboBox comboBox = (AComboBox) e.getSource();
		// String maxLength = (String) comboBox.getSelectedItem();
		// for (int index = 0; index <= 1; index++) {
		// Map map;
		// AComboBox aComboBox;
		// if (index == 0) {
		// map = ReflectometryTestPanel.this.pulseWidthMap;
		// aComboBox = ReflectometryTestPanel.this.pulseWidthComboBox;
		// } else {
		// map = ReflectometryTestPanel.this.resolutionMap;
		// aComboBox = ReflectometryTestPanel.this.resolutionComboBox;
		// }
		// String value = (String) map.get(maxLength);
		// String[] values = value.split("\\s+");
		// Arrays.sort(values, ReflectometryTestPanel.this.comparator);
		// aComboBox.removeAllItems();
		// for (int i = 0; i < values.length; i++)
		// aComboBox.addItem(values[i]);
		// }
		//
		// }
		// }
		// });
		JLabel refractLabel = new JLabel(LangModelSchedule.getString("Index_Of_Refraction")); //$NON-NLS-1$
		JLabel waveLengthLabel = new JLabel(LangModelSchedule.getString("WaveLength")); //$NON-NLS-1$
		JLabel countOfAverageOutLabel = new JLabel(LangModelSchedule.getString("AverageQuantity")); //$NON-NLS-1$
		JLabel pulseWidthLabel = new JLabel(LangModelSchedule.getString("PulseWidth")); //$NON-NLS-1$
		JLabel resolutionLabel = new JLabel(LangModelSchedule.getString("Resolution")); //$NON-NLS-1$
		JLabel maxDistanceLabel = new JLabel(LangModelSchedule.getString("Distance")); //$NON-NLS-1$

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(refractLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(this.refractTextField, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(waveLengthLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(this.waveLengthComboBox, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(countOfAverageOutLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(this.averageQuantityComboBox, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(pulseWidthLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(this.pulseWidthComboBox, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(resolutionLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(this.resolutionComboBox, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(maxDistanceLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(this.maxDistanceComboBox, gbc);
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.SOUTH;
		add(new JLabel(), gbc);

	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, SchedulerModel.COMMAND_CHANGE_ME_TYPE);
	}

	public void unregisterDispatcher() {
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CHANGE_ME_TYPE);
	}

	private void selectCBValue(	AComboBox cb,
								double value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			Object obj = cb.getItemAt(i);
			String item = obj.toString();
			double v = Double.parseDouble(item);
			if (DEBUG)
				System.out.println("item:" + v); //$NON-NLS-1$
			if (v == value) {
				cb.setSelectedItem(obj);
				if (DEBUG)
					System.out.println("selected"); //$NON-NLS-1$
				break;
			}
		}
	}

	private void selectCBValue(	AComboBox cb,
								int value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			Object obj = cb.getItemAt(i);
			String item = obj.toString();
			int v = (int) Double.parseDouble(item);
			if (DEBUG)
				System.out.println("item:" + v); //$NON-NLS-1$
			if (v == value) {
				cb.setSelectedItem(obj);
				if (DEBUG)
					System.out.println("selected"); //$NON-NLS-1$
				break;
			}
		}
	}

	private void selectCBValue(	AComboBox cb,
								long value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			Object obj = cb.getItemAt(i);
			String item = obj.toString();
			// item = item.replaceAll(".", "");
			long v = Long.parseLong(item);
			if (DEBUG)
				System.out.println("item:" + v); //$NON-NLS-1$
			if (v == value) {
				cb.setSelectedItem(obj);
				if (DEBUG)
					System.out.println("selected"); //$NON-NLS-1$
				break;
			}
		}
	}

}