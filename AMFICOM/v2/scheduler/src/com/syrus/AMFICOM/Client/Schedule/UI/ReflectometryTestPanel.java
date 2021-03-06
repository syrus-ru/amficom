/*
 * ReflectometryTestPanel.java Created on 29.04.2004 13:10:12
 *  
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
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
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.TestType;
import com.syrus.AMFICOM.Client.Scheduler.General.*;
import com.syrus.util.ByteArray;

/**
 * @author Vladimir Dolzhenko
 */
public class ReflectometryTestPanel extends ParametersTestPanel implements
		ParametersTest, OperationListener {

	private class ListNumberComparator implements java.util.Comparator {

		private int	direction	= 1;

		public ListNumberComparator() {
			direction = 1;
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
					result = -direction;
				else if (d1 == d2)
					result = 0;
				else
					result = direction;
			} else {
				result = direction * s1.compareTo(s2);
			}

			return result;
		}
	}
	public static final String		CHARACTER_MAX_REFRACTION	= "Max_Coef_Preloml";
	public static final String		CHARACTER_MIN_REFRACTION	= "Min_Coef_Preloml";

	public static final String		CHARACTER_WAVE_LENGTH		= "Work_Wave_Length";

	private static final boolean	DEBUG						= false;
	public static final String		MAX_DISTANCE				= "trclen";					//$NON-NLS-1$

	public static final String		PANEL_NAME					= TestParametersPanel.PARAMETERS_PANEL_PREFIX
																		+ "Reflectometry";		//$NON-NLS-1$
	public static final String		PARAMETER_PREFIX			= "ref_";
	public static final String		PULSE_WIDTH					= "pulswd";					//$NON-NLS-1$
	public static final String		RESOLUTION					= "res";						//$NON-NLS-1$

	public static final String		PARAMETER_AVERAGE_QUANTITY	= "ref_scans";					//$NON-NLS-1$
	public static final String		PARAMETER_MAX_DISTANCE		= PARAMETER_PREFIX
																		+ MAX_DISTANCE;		//$NON-NLS-1$	
	public static final String		PARAMETER_PULSE_WIDTH		= PARAMETER_PREFIX
																		+ PULSE_WIDTH;			//$NON-NLS-1$
	public static final String		PARAMETER_REFRACTION		= "ref_ior";					//$NON-NLS-1$
	public static final String		PARAMETER_RESOLUTION		= PARAMETER_PREFIX
																		+ RESOLUTION;			//$NON-NLS-1$
	public static final String		PARAMETER_WAVELENGHT		= "ref_wvlen";					//$NON-NLS-1$

	//public static final String PARAMETER_CHAR_IDENTITY =
	// "ref_characterizationidentity";

	private ApplicationContext		aContext;

	private AComboBox				averageQuantityComboBox		= new AComboBox();

	ListNumberComparator			comparator					= new ListNumberComparator();
	private Dispatcher				dispatcher;
	AComboBox						maxDistanceComboBox			= new AComboBox();
	double							maxIndexOfRefraction		= 0.0;
	double							minIndexOfRefraction		= 0.0;
	AComboBox						pulseWidthComboBox			= new AComboBox();
	HashMap							pulseWidthMap;

	private JTextField				refractTextField			= new JTextField();
	AComboBox						resolutionComboBox			= new AComboBox();
	HashMap							resolutionMap;

	private Test					test;
	private AComboBox				waveLengthComboBox			= new AComboBox();

	public ReflectometryTestPanel() {
		init();
	}

	public ReflectometryTestPanel(ApplicationContext aContext, AccessPort port) {
		this.aContext = aContext;
		initModule(aContext.getDispatcher());
		init();
		setPort(port);
	}

	public ReflectometryTestPanel(ApplicationContext aContext, AccessPort port,
			Test test) {
		this.aContext = aContext;
		initModule(aContext.getDispatcher());
		init();
		setPort(port);
		setTest(test);
	}

	public String getPanelName() {
		return PANEL_NAME;
	}

	public TestArgumentSet getParameters() {

		DataSourceInterface dsi = aContext.getDataSourceInterface();
		String test_setup_id;
		String test_type_id;
		if (test == null) {
			test_setup_id = ""; //$NON-NLS-1$
			test_type_id = TestParametersPanel.TEST_TYPE_TRACE_AND_ANALYSE;
		} else {
			test_setup_id = test.getTestSetupId();
			test_type_id = test.getTestTypeId();
		}
		TestArgumentSet tas = null;
		//if (test_setup_id.equals(""))
		{
			//			tas.id = dsi.GetUId(TestArgumentSet.TYPE);
			//			Pool.put(TestArgumentSet.TYPE, as.getId(), as);
			//			tas.name = tas.id;
			//			tas.created = 0;
			//			tas.created_by = "";
			//			tas.test_type_id = test_type_id;
			/**
			 * @TODO recast to static final fields 
			 */
			//System.out.println("test_setup_id:" + test_setup_id);
			TestSetup ts = (TestSetup) Pool.get(TestSetup.typ, test_setup_id);
			if (ts == null) {
				ts = new TestSetup();
				//System.out.println("new TestSetup()");
			}
			//System.out.println("ts:" + ts.id);
			tas = (TestArgumentSet) Pool.get(TestArgumentSet.typ, ts
					.getTestArgumentSetId());
			if (tas == null) {
				tas = new TestArgumentSet();
				//System.out.println("new TestArgumentSet()");
			}
			//System.out.println("tas:" + tas.id);
			ActionParameterType apt;

			TestType testType = (TestType) Pool.get(TestType.typ, test_type_id);
			apt = (ActionParameterType) testType.getSortedArguments().get(
					PARAMETER_REFRACTION);
			try {
				ByteArray byteArray;
				String refract = refractTextField.getText();
				if ((refract == null) || (refract.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule
								.getString("index_of_refraction_is_not_set")); //$NON-NLS-1$
				byteArray = new ByteArray(Double.parseDouble(refract));
				// dsi.GetUId("testargument")
				Parameter refractParam = new Parameter(dsi
						.GetUId(PARAMETER_ID_NAME), apt.getId(), byteArray
						.getBytes(), PARAMETER_REFRACTION, "double"); //$NON-NLS-1$
				if (refractParam == null)
						throw new IllegalArgumentException(LangModelSchedule
								.getString("index_of_refraction_is_not_set"));

				tas.addArgument(refractParam);

				apt = (ActionParameterType) testType.getSortedArguments().get(
						PARAMETER_WAVELENGHT);
				Object wave = waveLengthComboBox.getSelectedItem();
				if (wave == null)
						throw new IllegalArgumentException(LangModelSchedule
								.getString("wave_length_is_not_set"));

				String waveStr = wave.toString();
				if ((waveStr == null) || (waveStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule
								.getString("wave_length_is_not_set"));
				byteArray = new ByteArray(Integer.parseInt(waveStr));
				//			 dsi.GetUId("testargument")
				Parameter waveLengthParam = new Parameter(dsi
						.GetUId(PARAMETER_ID_NAME), apt.getId(), byteArray
						.getBytes(), PARAMETER_WAVELENGHT, "int"); //$NON-NLS-1$
				tas.addArgument(waveLengthParam);

				Object average = averageQuantityComboBox.getSelectedItem();
				if (average == null)
						throw new IllegalArgumentException(LangModelSchedule
								.getString("average_quantity_is_not_set"));
				String averageStr = average.toString();
				if ((averageStr == null) || (averageStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule
								.getString("average_quantity_is_not_set"));

				apt = (ActionParameterType) testType.getSortedArguments().get(
						PARAMETER_AVERAGE_QUANTITY);
				byteArray = new ByteArray(Double.parseDouble(averageStr));
				// dsi.GetUId("testargument")
				Parameter averageOutCountParam = new Parameter(dsi
						.GetUId(PARAMETER_ID_NAME), apt.getId(), byteArray
						.getBytes(), PARAMETER_AVERAGE_QUANTITY, "double"); //$NON-NLS-1$
				tas.addArgument(averageOutCountParam);
				Object pulse = pulseWidthComboBox.getSelectedItem();
				if (pulse == null)
						throw new IllegalArgumentException(LangModelSchedule
								.getString("pulse_width_is_not_set"));
				String pulseStr = pulse.toString();
				if ((pulseStr == null) || (pulseStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule
								.getString("pulse_width_is_not_set"));
				apt = (ActionParameterType) testType.getSortedArguments().get(
						PARAMETER_PULSE_WIDTH);
				byteArray = new ByteArray(Double.parseDouble(pulseStr));
				// dsi.GetUId("testargument")
				Parameter pulseWidthParam = new Parameter(dsi
						.GetUId(PARAMETER_ID_NAME), apt.getId(), byteArray
						.getBytes(), PARAMETER_PULSE_WIDTH, "double"); //$NON-NLS-1$
				tas.addArgument(pulseWidthParam);

				Object resolution = resolutionComboBox.getSelectedItem();
				if (resolution == null)
						throw new IllegalArgumentException(LangModelSchedule
								.getString("resolution_is_not_set"));
				String resolutionStr = resolution.toString();
				if ((resolutionStr == null) || (resolutionStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule
								.getString("resolution_is_not_set"));
				apt = (ActionParameterType) testType.getSortedArguments().get(
						PARAMETER_RESOLUTION);
				byteArray = new ByteArray(Double.parseDouble(resolutionStr));
				// dsi.GetUId("testargument")
				Parameter resolutionParam = new Parameter(dsi
						.GetUId(PARAMETER_ID_NAME), apt.getId(), byteArray
						.getBytes(), PARAMETER_RESOLUTION, "double"); //$NON-NLS-1$
				tas.addArgument(resolutionParam);

				Object distance = maxDistanceComboBox.getSelectedItem();
				if (distance == null)
						throw new IllegalArgumentException(LangModelSchedule
								.getString("distance_is_not_set"));

				String distanceStr = distance.toString();
				if ((distanceStr == null) || (distanceStr.length() == 0))
						throw new IllegalArgumentException(LangModelSchedule
								.getString("distance_is_not_set"));
				apt = (ActionParameterType) testType.getSortedArguments().get(
						PARAMETER_MAX_DISTANCE);
				byteArray = new ByteArray(Double.parseDouble(distanceStr));
				Parameter maxDistanceParam = new Parameter(dsi
						.GetUId(PARAMETER_ID_NAME), apt.getId(), byteArray
						.getBytes(), PARAMETER_MAX_DISTANCE, "double"); //$NON-NLS-1$
				tas.addArgument(maxDistanceParam);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (IllegalArgumentException e) {
				String message = e.getMessage();
				System.out.println(message);
				JOptionPane.showMessageDialog(this, message, LangModelSchedule
						.getString("Error"), JOptionPane.OK_OPTION);
				tas = null;
			}
		}
		return tas;
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		//Object obj = ae.getSource();
		Environment.log(Environment.LOG_LEVEL_INFO,"commandName:"+commandName,getClass().getName());
		if (commandName.equals(TestUpdateEvent.TYPE)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			if (tue.TEST_SELECTED) {
				setTest(tue.test);
			}
		}
	}

	public void setPort(AccessPort port) {
		Hashtable table = port.characteristics;
		if (this.resolutionMap == null)
			this.resolutionMap = new HashMap();
		else
			this.resolutionMap.clear();
		if (this.pulseWidthMap == null)
			this.pulseWidthMap = new HashMap();
		else
			this.pulseWidthMap.clear();
		Pattern pattern = Pattern.compile(MAX_DISTANCE + "_(\\d+)_("
				+ PULSE_WIDTH + "|" + RESOLUTION + ")");
		for (Iterator it = table.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			Characteristic character = (Characteristic) table.get(key);
			//			System.out.println(key + "\t" + character.id + "\t"
			//					+ character.value);
			if (key.equals(PARAMETER_AVERAGE_QUANTITY)) {
				String[] values = character.value.split("\\s+");
				Arrays.sort(values, comparator);
				averageQuantityComboBox.removeAllItems();
				for (int i = 0; i < values.length; i++)
					averageQuantityComboBox.addItem(values[i]);

			} else if (key.equals(CHARACTER_WAVE_LENGTH)) {
				String[] values = character.value.split("\\s+");
				Arrays.sort(values, comparator);
				waveLengthComboBox.removeAllItems();
				for (int i = 0; i < values.length; i++)
					waveLengthComboBox.addItem(values[i]);

			} else if (key.equals(CHARACTER_MAX_REFRACTION)) {
				try {
					maxIndexOfRefraction = Double.parseDouble(character.value);
				} catch (NumberFormatException nfe) {
					// nothing to do
				}

			} else if (key.equals(CHARACTER_MIN_REFRACTION)) {
				try {
					minIndexOfRefraction = Double.parseDouble(character.value);
				} catch (NumberFormatException nfe) {
					//					 nothing to do
				}
			} else {
				Matcher matcher = pattern.matcher(key);
				if (matcher.find()) {
					String maxLength = null;
					String suffix = null;
					for (int j = 0; j <= matcher.groupCount(); j++) {
						//System.out.println("j:"+j+"\t"+);
						String substring = key.substring(matcher.start(j),
								matcher.end(j));
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
							map.put(maxLength, character.value);
						}
					}
				}
			}
		}
		if (waveLengthComboBox.getItemCount() == 0)
				waveLengthComboBox.addItem("1625");
		if (averageQuantityComboBox.getItemCount() == 0)
				averageQuantityComboBox.addItem("4000");
		refractTextField.setText(Double.toString(minIndexOfRefraction));
		String defaultMaxDistance = "131072";
		if (this.resolutionMap.keySet().size() == this.pulseWidthMap.keySet()
				.size()) {

			Set set = this.resolutionMap.keySet();
			Object[] values = set.toArray();
			Arrays.sort(values, comparator);
			if (this.resolutionMap.keySet().size() == 0) {
				resolutionMap.put(defaultMaxDistance, "8");
				pulseWidthMap.put(defaultMaxDistance, "5000");
			}
			if (values.length == 0)
					values = new String[] { defaultMaxDistance};
			maxDistanceComboBox.removeAllItems();
			for (int i = 0; i < values.length; i++)
				maxDistanceComboBox.addItem(values[i].toString());
			maxDistanceComboBox.setSelectedIndex(0);
		}

	}

	public void setTest(Test test) {
		this.test = test;
		TestArgumentSet tas = null;
		if (test.getTestArgumentSet() != null)
			tas = test.getTestArgumentSet();
		else {
			TestSetup testSetup = null;
			if ((test.getTestSetupId() != null)
					|| (test.getTestSetupId().length() > 0))
					testSetup = (TestSetup) Pool.get(TestSetup.typ, test
							.getTestSetupId());
			if (testSetup == null) testSetup = test.getTestSetup();
			if (testSetup == null) {
				if (test.getTestArgumentSetId() != null)
						tas = (TestArgumentSet) Pool.get(TestArgumentSet.typ,
								test.getTestArgumentSetId());
				if (tas == null) {
					aContext.getDataSourceInterface().LoadTestArgumentSets(
							new String[] { test.getTestArgumentSetId()});
					tas = (TestArgumentSet) Pool.get(TestArgumentSet.typ, test
							.getTestArgumentSetId());
				}
			} else {
				DataSourceInterface dsi = aContext.getDataSourceInterface();

				tas = (TestArgumentSet) Pool.get(TestArgumentSet.typ, testSetup
						.getTestArgumentSetId());
				if (tas == null) {
					dsi.LoadTestArgumentSets(new String[] { testSetup
							.getTestArgumentSetId()});
					tas = (TestArgumentSet) Pool.get(TestArgumentSet.typ,
							testSetup.getTestArgumentSetId());
				}
				/**
				 * @todo get Analysis , Evaluation from TestSetup
				 */
			}
		}

		if (tas == null) {
			System.err.println("TestArgumentSet not found " //$NON-NLS-1$
					+ test.getTestArgumentSetId());
			return;
		}

		try {
			java.util.List arguments = tas.getArgumentList();
			for (int i = 0; i < arguments.size(); i++) {
				Parameter p = (Parameter) arguments.get(i);
				String codeName = p.getCodename();
				byte[] value = p.getValue();
				if (codeName.equals(PARAMETER_REFRACTION)) {
					double refraction = new ByteArray(value).toDouble();
					if (DEBUG) System.out.println("reflection:" + refraction); //$NON-NLS-1$
					refractTextField.setText(new Double(refraction).toString());
				} else if (codeName.equals(PARAMETER_WAVELENGHT)) {
					int wavelength = new ByteArray(value).toInt();
					if (DEBUG) System.out.println("wavelength:" + wavelength); //$NON-NLS-1$
					selectCBValue(waveLengthComboBox, wavelength);
				} else if (codeName.equals(PARAMETER_AVERAGE_QUANTITY)) {
					double averages = (int) new ByteArray(value).toDouble();
					if (DEBUG) System.out.println("averages:" + averages); //$NON-NLS-1$
					selectCBValue(averageQuantityComboBox, averages);
				} else if (codeName.equals(PARAMETER_MAX_DISTANCE)) {
					double length = new ByteArray(value).toDouble();
					if (DEBUG) System.out.println("length:" + length); //$NON-NLS-1$
					selectCBValue(maxDistanceComboBox, length);
				} else if (codeName.equals(PARAMETER_RESOLUTION)) {
					double resolution = new ByteArray(value).toDouble();
					if (DEBUG) System.out.println("resolution:" + resolution); //$NON-NLS-1$
					selectCBValue(resolutionComboBox, resolution);
				} else if (codeName.equals(PARAMETER_PULSE_WIDTH)) {
					//double pulsewidth = new ByteArray(p.value).toDouble();
					long pulsewidth = new ByteArray(value).toLong();
					if (DEBUG) System.out.println("pulsewidth:" + pulsewidth); //$NON-NLS-1$
					selectCBValue(pulseWidthComboBox, pulsewidth);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		//cb.addItem(String.valueOf(ior));

	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.TYPE);
	}

	private void init() {
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new GridBagLayout());
		//refractTextField.setText("1.467"); //$NON-NLS-1$
		//maxDistanceComboBox
		{
			Dimension d = new Dimension(75, 20);
			UIStorage.setRigidSize(refractTextField, d);
			UIStorage.setRigidSize(waveLengthComboBox, d);
			UIStorage.setRigidSize(averageQuantityComboBox, d);
			UIStorage.setRigidSize(pulseWidthComboBox, d);
			UIStorage.setRigidSize(resolutionComboBox, d);
			UIStorage.setRigidSize(maxDistanceComboBox, d);

		}

		refractTextField.addActionListener(new ActionListener() {

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
					textField.setText(Double.toString(minIndexOfRefraction));
				} else {
					if (refract < minIndexOfRefraction) {
						textField
								.setText(Double.toString(minIndexOfRefraction));
					} else {
						if (refract > maxIndexOfRefraction) {
							textField.setText(Double
									.toString(maxIndexOfRefraction));
						}
					}
				}

			}
		});

		maxDistanceComboBox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					AComboBox comboBox = (AComboBox) e.getSource();
					String maxLength = (String) comboBox.getSelectedItem();
					for (int index = 0; index <= 1; index++) {
						HashMap map;
						AComboBox aComboBox;
						if (index == 0) {
							map = pulseWidthMap;
							aComboBox = pulseWidthComboBox;
						} else {
							map = resolutionMap;
							aComboBox = resolutionComboBox;
						}
						String value = (String) map.get(maxLength);
						String[] values = value.split("\\s+");
						Arrays.sort(values, comparator);
						aComboBox.removeAllItems();
						for (int i = 0; i < values.length; i++)
							aComboBox.addItem(values[i]);
					}

				}
			}
		});
		JLabel refractLabel = new JLabel(LangModelSchedule.getString("Index_Of_Refraction")); //$NON-NLS-1$
		JLabel waveLengthLabel = new JLabel(LangModelSchedule.getString("WaveLength")); //$NON-NLS-1$
		JLabel countOfAverageOutLabel = new JLabel(LangModelSchedule
				.getString("AverageQuantity")); //$NON-NLS-1$
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
		add(refractTextField, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(waveLengthLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(waveLengthComboBox, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(countOfAverageOutLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(averageQuantityComboBox, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(pulseWidthLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(pulseWidthComboBox, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(resolutionLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(resolutionComboBox, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(maxDistanceLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(maxDistanceComboBox, gbc);
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.SOUTH;
		add(new JLabel(), gbc);

	}

	private void selectCBValue(AComboBox cb, double value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			Object obj = cb.getItemAt(i);
			String item = obj.toString();
			double v = Double.parseDouble(item);
			if (DEBUG) System.out.println("item:" + v); //$NON-NLS-1$
			if (v == value) {
				cb.setSelectedItem(obj);
				if (DEBUG) System.out.println("selected"); //$NON-NLS-1$
				break;
			}
		}
	}

	private void selectCBValue(AComboBox cb, int value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			Object obj = cb.getItemAt(i);
			String item = obj.toString();
			int v = Integer.parseInt(item);
			if (DEBUG) System.out.println("item:" + v); //$NON-NLS-1$
			if (v == value) {
				cb.setSelectedItem(obj);
				if (DEBUG) System.out.println("selected"); //$NON-NLS-1$
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
			if (DEBUG) System.out.println("item:" + v); //$NON-NLS-1$
			if (v == value) {
				cb.setSelectedItem(obj);
				if (DEBUG) System.out.println("selected"); //$NON-NLS-1$
				break;
			}
		}
	}

}