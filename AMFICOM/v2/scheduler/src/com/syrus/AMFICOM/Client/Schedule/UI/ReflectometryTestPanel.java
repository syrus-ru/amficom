/*
 * ReflectometryTestPanel.java Created on 29.04.2004 13:10:12
 *  
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.TestType;
import com.syrus.util.ByteArray;

/**
 * @author Vladimir Dolzhenko
 */
public class ReflectometryTestPanel extends ParametersTestPanel implements
		ParametersTest, OperationListener {

	public static final String		PANEL_NAME					= TestParametersPanel.PARAMETERS_PANEL_PREFIX
																		+ "Reflectometry";

	public static final String		PARAMETER_REFLECTION		= "ref_ior";

	public static final String		PARAMETER_WAVELENGHT		= "ref_wvlen";

	public static final String		PARAMETER_AVERAGEOUT_COUNT	= "ref_scans";

	public static final String		PARAMETER_PULSE_WIDTH		= "ref_pulswd";

	public static final String		PARAMETER_RESOLUTION		= "ref_res";

	public static final String		PARAMETER_MAX_DISTANCE		= "ref_trclen";

	private static final boolean	DEBUG						= false;

	//public static final String PARAMETER_CHAR_IDENTITY =
	// "ref_characterizationidentity";

	private ApplicationContext		aContext;

	private Test					test;

	private String[]				averageOutCount				= { "4000",
			"8000", "16000", "32000", "64000", "128000", "256000"};

	private String[]				waveLength					= { "1310",
			"1550", "1625"										};

	private String[][]				resolution					= {
			{ "0.25", "0.5", "1", "2", "4", "8", "16"},
			{ "0.5", "1", "2", "4", "8", "16"}, { "1", "2", "4", "8", "16"},
			{ "2", "4", "8", "16"}, { "4", "8", "16"}, { "8", "16"}, { "16"}};

	private String[][]				pulseWidth					= {
			{ "100", "200", "500"}, { "100", "200", "500", "1000"},
			{ "100", "200", "500", "1000"},
			{ "100", "200", "500", "1000", "5000"},
			{ "100", "200", "500", "1000", "5000", "10000"},
			{ "100", "200", "500", "1000", "5000", "10000"},
			{ "100", "200", "500", "1000", "5000", "10000", "20000"}};

	private String[]				maxDistance					= { "4.096",
			"8.192", "16.384", "32.768", "65.536", "131.072", "262.144"};

	private JTextField				reflectTextField			= new JTextField();

	private AComboBox				waveLengthComboBox			= new AComboBox(
																		waveLength);

	// was jComboBox1
	private AComboBox				averageOutCountComboBox		= new AComboBox(
																		averageOutCount);

	// was jComboBox2
	private AComboBox				resolutionComboBox			= new AComboBox(
																		resolution[0]);

	// was jComboBox4
	private AComboBox				maxDistanceComboBox			= new AComboBox(
																		maxDistance);

	// was jComboBox3
	private AComboBox				pulseWidthComboBox			= new AComboBox(
																		pulseWidth[0]);

	// was jComboBox5

	private Dispatcher				dispatcher;

	public ReflectometryTestPanel() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ReflectometryTestPanel(ApplicationContext aContext) {
		this.aContext = aContext;
		initModule(aContext.getDispatcher());
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ReflectometryTestPanel(ApplicationContext aContext, Test test) {
		this.aContext = aContext;
		initModule(aContext.getDispatcher());
		this.test = test;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//init(test);
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.typ);
	}

	private void jbInit() throws Exception {
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new GridBagLayout());
		reflectTextField.setText("1.467");
		{
			Dimension d = new Dimension(75, 20);
			UIUtil.setRigidSize(reflectTextField, d);
			UIUtil.setRigidSize(waveLengthComboBox, d);
			UIUtil.setRigidSize(averageOutCountComboBox, d);
			UIUtil.setRigidSize(pulseWidthComboBox, d);
			UIUtil.setRigidSize(resolutionComboBox, d);
			UIUtil.setRigidSize(maxDistanceComboBox, d);

		}

		maxDistanceComboBox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					int index = maxDistanceComboBox.getSelectedIndex();

					pulseWidthComboBox.removeAllItems();
					resolutionComboBox.removeAllItems();

					for (int j = 0; j < pulseWidth[index].length; j++)
						pulseWidthComboBox.addItem(pulseWidth[index][j]);
					for (int j = 0; j < resolution[index].length; j++)
						resolutionComboBox.addItem(resolution[index][j]);
				}
			}
		});
		JLabel reflectLabel = new JLabel(LangModelSchedule
				.String("labelReflect"));
		JLabel waveLengthLabel = new JLabel(LangModelSchedule
				.String("labelWaveLength"));
		JLabel countOfAverageOutLabel = new JLabel(LangModelSchedule
				.String("labelAverCount"));
		JLabel pulseWidthLabel = new JLabel(LangModelSchedule
				.String("labelImpuls"));
		JLabel resolutionLabel = new JLabel(LangModelSchedule
				.String("labelDetalM"));
		JLabel maxDistanceLabel = new JLabel(LangModelSchedule
				.String("labelMaxDistance"));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(reflectLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(reflectTextField, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(waveLengthLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(waveLengthComboBox, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(countOfAverageOutLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(averageOutCountComboBox, gbc);
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

	public void setTest(Test test) {
		this.test = test;
		TestArgumentSet tas = null;
		if (test.test_argument_set_id != null)
				tas = (TestArgumentSet) Pool.get(TestArgumentSet.typ,
						test.test_argument_set_id);
		if (tas == null) {
			aContext.getDataSourceInterface().LoadTestArgumentSets(
					new String[] { test.test_argument_set_id});
			tas = (TestArgumentSet) Pool.get(TestArgumentSet.typ,
					test.test_argument_set_id);
		}

		if (tas == null) {
			System.err.println("TestArgumentSet not found "
					+ test.test_argument_set_id);
			return;
		}

		try {
			for (Enumeration en = tas.arguments.elements(); en
					.hasMoreElements();) {
				Parameter p = (Parameter) en.nextElement();
				if (p.codename.equals(PARAMETER_REFLECTION)) {
					double reflection = new ByteArray(p.value).toDouble();
					if (DEBUG) System.out.println("reflection:" + reflection);
					reflectTextField.setText(new Double(reflection).toString());
				} else if (p.codename.equals(PARAMETER_WAVELENGHT)) {
					int wavelength = new ByteArray(p.value).toInt();
					if (DEBUG) System.out.println("wavelength:" + wavelength);
					selectCBValue(waveLengthComboBox, wavelength);
				} else if (p.codename.equals(PARAMETER_AVERAGEOUT_COUNT)) {
					double averages = (int) new ByteArray(p.value).toDouble();
					if (DEBUG) System.out.println("averages:" + averages);
					selectCBValue(averageOutCountComboBox, averages);
				} else if (p.codename.equals(PARAMETER_MAX_DISTANCE)) {
					double length = new ByteArray(p.value).toDouble();
					if (DEBUG) System.out.println("length:" + length);
					selectCBValue(maxDistanceComboBox, length);
				} else if (p.codename.equals(PARAMETER_RESOLUTION)) {
					double resolution = new ByteArray(p.value).toDouble();
					if (DEBUG) System.out.println("resolution:" + resolution);
					selectCBValue(resolutionComboBox, resolution);
				} else if (p.codename.equals(PARAMETER_PULSE_WIDTH)) {
					//double pulsewidth = new ByteArray(p.value).toDouble();
					long pulsewidth = new ByteArray(p.value).toLong();
					if (DEBUG) System.out.println("pulsewidth:" + pulsewidth);
					selectCBValue(pulseWidthComboBox, pulsewidth);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		//cb.addItem(String.valueOf(ior));

	}

	private void selectCBValue(AComboBox cb, int value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			String item = cb.getItemAt(i).toString();
			int v = Integer.parseInt(item);
			if (DEBUG) System.out.println("item:" + v);
			if (v == value) {
				cb.setSelectedIndex(i);
				if (DEBUG) System.out.println("selected");
				break;
			}
		}
	}

	private void selectCBValue(AComboBox cb, double value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			String item = cb.getItemAt(i).toString();
			double v = Double.parseDouble(item);
			if (DEBUG) System.out.println("item:" + v);
			if (v == value) {
				cb.setSelectedIndex(i);
				if (DEBUG) System.out.println("selected");
				break;
			}
		}
	}

	private void selectCBValue(AComboBox cb, long value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			String item = cb.getItemAt(i).toString();
			//item = item.replaceAll(".", "");
			long v = Long.parseLong(item);
			if (DEBUG) System.out.println("item:" + v);
			if (v == value) {
				cb.setSelectedIndex(i);
				if (DEBUG) System.out.println("selected");
				break;
			}
		}
	}

	public TestArgumentSet getParameters() {

		DataSourceInterface dsi = aContext.getDataSourceInterface();
		String test_setup_id;
		String test_type_id;
		if (test == null) {
			test_setup_id = "";
			test_type_id = TestParametersPanel.TEST_TYPE_TRACE_AND_ANALYSE;
		} else {
			test_setup_id = test.test_setup_id;
			test_type_id = test.test_type_id;
		}
		TestArgumentSet tas = null;
		//if (test_setup_id.equals(""))
		{
			//			tas.id = dsi.GetUId(TestArgumentSet.typ);
			//			Pool.put(TestArgumentSet.typ, as.getId(), as);
			//			tas.name = tas.id;
			//			tas.created = 0;
			//			tas.created_by = "";
			//			tas.test_type_id = test_type_id;

			System.out.println("test_setup_id:" + test_setup_id);
			TestSetup ts = (TestSetup) Pool.get(TestSetup.typ, test_setup_id);
			if (ts == null) {
				ts = new TestSetup();
				System.out.println("new TestSetup()");
			}
			System.out.println("ts:" + ts.id);
			tas = (TestArgumentSet) Pool.get(TestArgumentSet.typ,
					ts.test_argument_set_id);
			if (tas == null) {
				tas = new TestArgumentSet();
				System.out.println("new TestArgumentSet()");
			}
			System.out.println("tas:" + tas.id);
			ActionParameterType apt;
			TestType testType = (TestType) Pool.get("testtype", test_type_id);
			apt = (ActionParameterType) testType.sorted_arguments
					.get(PARAMETER_REFLECTION);
			Parameter reflectParam = new Parameter(dsi.GetUId("testargument"),
					apt.getId(), reflectTextField.getText().getBytes(),
					PARAMETER_REFLECTION, "double");
			if (reflectParam == null)
					System.out.println("reflectParam is null");
			tas.addArgument(reflectParam);

			apt = (ActionParameterType) testType.sorted_arguments
					.get(PARAMETER_WAVELENGHT);
			Parameter waveLengthParam = new Parameter(dsi
					.GetUId("testargument"), apt.getId(), waveLengthComboBox
					.getSelectedItem().toString().getBytes(),
					PARAMETER_WAVELENGHT, "int");
			tas.addArgument(waveLengthParam);

			apt = (ActionParameterType) testType.sorted_arguments
					.get(PARAMETER_AVERAGEOUT_COUNT);
			Parameter averageOutCountParam = new Parameter(dsi
					.GetUId("testargument"), apt.getId(),
					averageOutCountComboBox.getSelectedItem().toString()
							.getBytes(), PARAMETER_AVERAGEOUT_COUNT, "double");
			tas.addArgument(averageOutCountParam);

			apt = (ActionParameterType) testType.sorted_arguments
					.get(PARAMETER_PULSE_WIDTH);
			Parameter pulseWidthParam = new Parameter(dsi
					.GetUId("testargument"), apt.getId(), pulseWidthComboBox
					.getSelectedItem().toString().getBytes(),
					PARAMETER_PULSE_WIDTH, "double");
			tas.addArgument(pulseWidthParam);

			apt = (ActionParameterType) testType.sorted_arguments
					.get(PARAMETER_RESOLUTION);
			Parameter resolutionParam = new Parameter(dsi
					.GetUId("testargument"), apt.getId(), resolutionComboBox
					.getSelectedItem().toString().getBytes(),
					PARAMETER_RESOLUTION, "double");
			tas.addArgument(resolutionParam);

			apt = (ActionParameterType) testType.sorted_arguments
					.get(PARAMETER_MAX_DISTANCE);
			Parameter maxDistanceParam = new Parameter(dsi
					.GetUId("testargument"), apt.getId(), maxDistanceComboBox
					.getSelectedItem().toString().getBytes(),
					PARAMETER_MAX_DISTANCE, "double");
			tas.addArgument(maxDistanceParam);
		}
		return tas;
	}

	public String getPanelName() {
		return PANEL_NAME;
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Object obj = ae.getSource();
		System.out
				.println(getClass().getName() + " commandName:" + commandName);
		if (commandName.equals(TestUpdateEvent.typ)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			setTest(tue.test);
		}
	}

}