/*
 * ReflectometryTestPanel.java Created on 29.04.2004 13:10:12
 *  
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.*;
import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.measurement.LinkedIdsCondition;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.ParameterTypeCodenames;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.StringFieldCondition;
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

		public int compare(Object o1, Object o2) {
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

	public static final String		TEST_TYPE					= "trace_and_analyse";

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
	double							maxIndexOfRefraction		= 0.0;
	double							minIndexOfRefraction		= 0.0;
	AComboBox						pulseWidthComboBox			= new AComboBox();
	HashMap							pulseWidthMap;
	AComboBox						resolutionComboBox			= new AComboBox();
	HashMap							resolutionMap;

	//public static final String PARAMETER_CHAR_IDENTITY =
	// "ref_characterizationidentity";

	private ApplicationContext		aContext;

	private AComboBox				averageQuantityComboBox		= new AComboBox();
	private Dispatcher				dispatcher;

	private JTextField				refractTextField			= new JTextField();

	private Test					test;
	private AComboBox				waveLengthComboBox			= new AComboBox();

	public ReflectometryTestPanel() {
		init();
	}

	public ReflectometryTestPanel(ApplicationContext aContext, MeasurementPort port) {
		this(aContext, port, null);
	}

	public ReflectometryTestPanel(ApplicationContext aContext, MeasurementPort port, Test test) {
		this.aContext = aContext;
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
				StringFieldCondition stringFieldCondition = StringFieldCondition.getInstance();
				stringFieldCondition.setEntityCode(new Short(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE));
				stringFieldCondition.setString(TEST_TYPE);
				measurementType = (MeasurementType) MeasurementStorableObjectPool
						.getStorableObjectsByCondition(stringFieldCondition, true);
			} else {
				measurementType = this.test.getMeasurementType();
			}

			//if (test_setup_id.equals(""))
			{
				try {

					Short parameterTypeShort = new Short(ObjectEntities.PARAMETERTYPE_ENTITY_CODE);

					StringFieldCondition stringFieldCondition = new StringFieldCondition(
																							ParameterTypeCodenames.TRACE_WAVELENGTH,
																							parameterTypeShort);

					ParameterType wvlenParam = (ParameterType) MeasurementStorableObjectPool
							.getStorableObjectsByCondition(stringFieldCondition, true).get(0);

					stringFieldCondition.setString(ParameterTypeCodenames.TRACE_LENGTH);
					ParameterType trclenParam = (ParameterType) MeasurementStorableObjectPool
							.getStorableObjectsByCondition(stringFieldCondition, true).get(0);

					stringFieldCondition.setString(ParameterTypeCodenames.TRACE_RESOLUTION);
					ParameterType resParam = (ParameterType) MeasurementStorableObjectPool
							.getStorableObjectsByCondition(stringFieldCondition, true).get(0);

					stringFieldCondition.setString(ParameterTypeCodenames.TRACE_PULSE_WIDTH);
					ParameterType pulswdParam = (ParameterType) MeasurementStorableObjectPool
							.getStorableObjectsByCondition(stringFieldCondition, true).get(0);

					stringFieldCondition.setString(ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION);
					ParameterType iorParam = (ParameterType) MeasurementStorableObjectPool
							.getStorableObjectsByCondition(stringFieldCondition, true).get(0);

					stringFieldCondition.setString(ParameterTypeCodenames.TRACE_AVERAGE_COUNT);
					ParameterType scansParam = (ParameterType) MeasurementStorableObjectPool
							.getStorableObjectsByCondition(stringFieldCondition, true).get(0);

					SetParameter[] params = new SetParameter[6];

					ByteArray byteArray;

					Object wave = this.waveLengthComboBox.getSelectedItem();
					if (wave == null)
						throw new IllegalArgumentException(LangModelSchedule.getString("wave_length_is_not_set"));

					String waveStr = wave.toString();
					if ((waveStr == null) || (waveStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule.getString("wave_length_is_not_set"));
					byteArray = new ByteArray(Integer.parseInt(waveStr));

					Identifier paramId = IdentifierPool.generateId(ObjectEntities.SETPARAMETER_ENTITY_CODE);
					params[0] = new SetParameter(paramId, wvlenParam, byteArray.getBytes());

					Object distance = this.maxDistanceComboBox.getSelectedItem();
					if (distance == null)
						throw new IllegalArgumentException(LangModelSchedule.getString("distance_is_not_set"));

					String distanceStr = distance.toString();
					if ((distanceStr == null) || (distanceStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule.getString("distance_is_not_set"));
					byteArray = new ByteArray(Double.parseDouble(distanceStr));

					paramId = IdentifierPool.generateId(ObjectEntities.SETPARAMETER_ENTITY_CODE);
					params[1] = new SetParameter(paramId, trclenParam, byteArray.getBytes());

					Object resolution = this.resolutionComboBox.getSelectedItem();
					if (resolution == null)
						throw new IllegalArgumentException(LangModelSchedule.getString("resolution_is_not_set"));
					String resolutionStr = resolution.toString();
					if ((resolutionStr == null) || (resolutionStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule.getString("resolution_is_not_set"));
					byteArray = new ByteArray(Double.parseDouble(resolutionStr));

					paramId = IdentifierPool.generateId(ObjectEntities.SETPARAMETER_ENTITY_CODE);
					params[2] = new SetParameter(paramId, resParam, byteArray.getBytes());

					Object pulse = this.pulseWidthComboBox.getSelectedItem();
					if (pulse == null)
						throw new IllegalArgumentException(LangModelSchedule.getString("pulse_width_is_not_set"));
					String pulseStr = pulse.toString();
					if ((pulseStr == null) || (pulseStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule.getString("pulse_width_is_not_set"));

					byteArray = new ByteArray(Long.parseLong(pulseStr));

					paramId = IdentifierPool.generateId(ObjectEntities.SETPARAMETER_ENTITY_CODE);
					params[3] = new SetParameter(paramId, pulswdParam, byteArray.getBytes());

					String refract = this.refractTextField.getText();
					if ((refract == null) || (refract.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule
								.getString("index_of_refraction_is_not_set")); //$NON-NLS-1$
					byteArray = new ByteArray(Double.parseDouble(refract));

					paramId = IdentifierPool.generateId(ObjectEntities.SETPARAMETER_ENTITY_CODE);
					params[4] = new SetParameter(paramId, iorParam, byteArray.getBytes());

					Object average = this.averageQuantityComboBox.getSelectedItem();
					if (average == null)
						throw new IllegalArgumentException(LangModelSchedule.getString("average_quantity_is_not_set"));
					String averageStr = average.toString();
					if ((averageStr == null) || (averageStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule.getString("average_quantity_is_not_set"));

					byteArray = new ByteArray(Double.parseDouble(averageStr));

					paramId = IdentifierPool.generateId(ObjectEntities.SETPARAMETER_ENTITY_CODE);
					params[5] = new SetParameter(paramId, scansParam, byteArray.getBytes());

					Identifier id = IdentifierPool.generateId(ObjectEntities.SET_ENTITY_CODE);
					set = Set.createInstance(id, null, SetSort.SET_SORT_MEASUREMENT_PARAMETERS,
												"Set created by Scheduler", params, null);

				} catch (IOException ioe) {
					ioe.printStackTrace();
				} catch (IllegalArgumentException e) {
					String message = e.getMessage();
					//System.out.println(message);
					Environment.log(Environment.LOG_LEVEL_WARNING, message);
					SchedulerModel.showErrorMessage(this, e);
					set = null;
				}
			}
		} catch (ApplicationException ae) {
			SchedulerModel.showErrorMessage(this, ae);
		}
		return set;
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		//Object obj = ae.getSource();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equals(TestUpdateEvent.TYPE)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			setTest(tue.test);
		}
	}

	public void setPort(MeasurementPort port) {
		try {
			LinkedIdsCondition linkedIdsCondition = LinkedIdsCondition.getInstance();
			linkedIdsCondition.setEntityCode(ObjectEntities.CHARACTERISTIC_ENTITY_CODE);
			linkedIdsCondition.setIdentifier(port.getId());

			List characteristics = ConfigurationStorableObjectPool.getStorableObjectsByCondition(linkedIdsCondition,
																									true);

			if (this.resolutionMap == null)
				this.resolutionMap = new HashMap();
			else
				this.resolutionMap.clear();
			if (this.pulseWidthMap == null)
				this.pulseWidthMap = new HashMap();
			else
				this.pulseWidthMap.clear();
			Pattern pattern = Pattern.compile(MAX_DISTANCE + "_(\\d+)_(" + PULSE_WIDTH + "|" + RESOLUTION + ")");
			for (Iterator it = characteristics.iterator(); it.hasNext();) {
				Characteristic character = (Characteristic) it.next();
				String name = character.getName();
				String value = character.getValue();
				//			System.out.println(key + "\t" + character.id + "\t"
				//					+ character.value);
				if (name.equals(PARAMETER_AVERAGE_QUANTITY)) {
					String[] values = value.split("\\s+");
					Arrays.sort(values, this.comparator);
					this.averageQuantityComboBox.removeAllItems();
					for (int i = 0; i < values.length; i++)
						this.averageQuantityComboBox.addItem(values[i]);

				} else if (name.equals(CHARACTER_WAVE_LENGTH)) {
					String[] values = value.split("\\s+");
					Arrays.sort(values, this.comparator);
					this.waveLengthComboBox.removeAllItems();
					for (int i = 0; i < values.length; i++)
						this.waveLengthComboBox.addItem(values[i]);

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
						String maxLength = null;
						String suffix = null;
						for (int j = 0; j <= matcher.groupCount(); j++) {
							//System.out.println("j:"+j+"\t"+);
							String substring = name.substring(matcher.start(j), matcher.end(j));
							switch (j) {
								case 1:
									maxLength = substring;
									break;
								case 2:
									suffix = substring;
									break;
							}
						}
						if ((maxLength != null) && (suffix != null)) {
							HashMap map = null;
							if (suffix.equals(RESOLUTION))
								map = this.resolutionMap;
							else if (suffix.equals(PULSE_WIDTH)) {
								map = this.pulseWidthMap;
							}

							if (map != null) {
								map.put(maxLength, value);
							}
						}
					}
				}
			}
			if (this.waveLengthComboBox.getItemCount() == 0)
				this.waveLengthComboBox.addItem(DEFAULT_WAVELENGTH);

			if (this.averageQuantityComboBox.getItemCount() == 0)
				this.averageQuantityComboBox.addItem(DEFAULT_AVERAGE_QUANTITY);
			this.refractTextField.setText(Double.toString(this.minIndexOfRefraction));
			if (this.resolutionMap.keySet().size() == this.pulseWidthMap.keySet().size()) {

				java.util.Set set = this.resolutionMap.keySet();
				Object[] values = set.toArray();
				Arrays.sort(values, this.comparator);
				if (this.resolutionMap.keySet().size() == 0) {
					this.resolutionMap.put(DEFAULT_DISTANCE, DEFAULT_RESOLUTION);
					this.pulseWidthMap.put(DEFAULT_DISTANCE, DEFAULT_PULSEWIDTH);
				}
				if (values.length == 0)
					values = new String[] { DEFAULT_DISTANCE};
				this.maxDistanceComboBox.removeAllItems();
				for (int i = 0; i < values.length; i++)
					this.maxDistanceComboBox.addItem(values[i].toString());
				this.maxDistanceComboBox.setSelectedIndex(0);
			}
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
						setMeasurementSetup(measurementSetup);
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
						//double pulsewidth = new
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

	public void setMeasurementSetup(MeasurementSetup measurementSetup) {
		setSet(measurementSetup.getParameterSet());
	}

	private void init() {
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new GridBagLayout());
		//refractTextField.setText("1.467"); //$NON-NLS-1$
		//maxDistanceComboBox
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

		this.maxDistanceComboBox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					AComboBox comboBox = (AComboBox) e.getSource();
					String maxLength = (String) comboBox.getSelectedItem();
					for (int index = 0; index <= 1; index++) {
						HashMap map;
						AComboBox aComboBox;
						if (index == 0) {
							map = ReflectometryTestPanel.this.pulseWidthMap;
							aComboBox = ReflectometryTestPanel.this.pulseWidthComboBox;
						} else {
							map = ReflectometryTestPanel.this.resolutionMap;
							aComboBox = ReflectometryTestPanel.this.resolutionComboBox;
						}
						String value = (String) map.get(maxLength);
						String[] values = value.split("\\s+");
						Arrays.sort(values, ReflectometryTestPanel.this.comparator);
						aComboBox.removeAllItems();
						for (int i = 0; i < values.length; i++)
							aComboBox.addItem(values[i]);
					}

				}
			}
		});
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
		this.dispatcher.register(this, TestUpdateEvent.TYPE);
	}

	public void unregisterDispatcher() {
		this.dispatcher.unregister(this, TestUpdateEvent.TYPE);
	}

	private void selectCBValue(AComboBox cb, double value) {
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

	private void selectCBValue(AComboBox cb, int value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			Object obj = cb.getItemAt(i);
			String item = obj.toString();
			int v = Integer.parseInt(item);
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

	private void selectCBValue(AComboBox cb, long value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			Object obj = cb.getItemAt(i);
			String item = obj.toString();
			//item = item.replaceAll(".", "");
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