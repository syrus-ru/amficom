package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.util.*;
import java.io.*;
public class TestParametersPanel extends JPanel implements OperationListener {

	public static final String COMMAND_STOP_ANALYSIS = "StopAnalysis";
	public static final String COMMAND_ME_TYPE = "METype";
	public static final String COMMAND_KIS_TYPE = "KISType";
	public static final String COMMAND_PORT_TYPE = "PortType";
	public static final String COMMAND_VISUAL_TEST_SETUP = "VisualTestSetup";
	public static final String COMMAND_TEST_TYPE = "TestType";
	public static final String COMMAND_TEST_SETUP = "TestSetup";
	public static final String COMMAND_VISUAL_TEST_PARAMS = "VisualTestParams";
	public static final String COMMAND_EXT_AFTER_USUAL_ROOT_FRAME =
		"ExtendedAfterUsual_RootFrame";
	public static final String COMMAND_REMOVE_PARAM_FRAME = "RemoveParamFrame";
	public static final String COMMAND_REMOVE_3A_FRAME = "Remove3aFrame";

	public static final String TEST_TYPE_TRACE_AND_ANALYSE =
		"trace_and_analyse";
	public static final String TEST_TYPE_VOICE_ANALYSE = "voice_analyse";

	public static final String PARAMETER_REFLECTION = "ref_ior";
	public static final String PARAMETER_WAVELENGHT = "ref_wvlen";
	public static final String PARAMETER_AVERAGEOUT_COUNT = "ref_scans";
	public static final String PARAMETER_PULSE_WIDTH = "ref_trclen";
	public static final String PARAMETER_RESOLUTION = "ref_res";
	public static final String PARAMETER_MAX_DISTANCE = "ref_pulswd";
	public static final String PARAMETER_PARAMETER = "Parameter";

	private Dispatcher dispatcher;
	private ApplicationContext aContext;
	//	private JPanel parametersPanel;
	//	private JPanel testSetupPanel;
	private JRadioButton patternRadioButton;
	private JRadioButton paramsRadioButton;
	private ArrayList objList = new ArrayList();
	private String testTypeId;
	private String meId;
	private ObjectResourceListBox testSetups;

	private static final String PATTERN_PANEL_NAME = "PATTERN_PANEL";
	private static final String PARAMETERS_PANEL_NAME = "PARAMETERS_PANEL";

	private String[] countOfAverageOut =
		{ "4000", "8000", "16000", "32000", "64000", "128000", "256000" };
	private String[] waveLength = { "1310", "1550", "1625" };

	String[][] resolution = { { "0.25", "0.5", "1", "2", "4", "8", "16" }, {
			"0.5", "1", "2", "4", "8", "16" }, {
			"1", "2", "4", "8", "16" }, {
			"2", "4", "8", "16" }, {
			"4", "8", "16" }, {
			"8", "16" }, {
			"16" }
	};
	String[][] pulseWidth = { { "100", "200", "500" }, {
			"100", "200", "500", "1000" }, {
			"100", "200", "500", "1000" }, {
			"100", "200", "500", "1000", "5000" }, {
			"100", "200", "500", "1000", "5000", "10000" }, {
			"100", "200", "500", "1000", "5000", "10000" }, {
			"100", "200", "500", "1000", "5000", "10000", "20000" }
	};
	private String[] maxDistance =
		{
			"4.096",
			"8.192",
			"16.384",
			"32.768",
			"65.536",
			"131.072",
			"262.144" };

	private JTextField reflectTextField = new JTextField();
	private AComboBox waveLengthComboBox = new AComboBox(waveLength);
	// was jComboBox1
	private AComboBox countOfAverageOutComboBox =
		new AComboBox(countOfAverageOut);
	// was jComboBox2
	private AComboBox resolutionComboBox = new AComboBox(resolution[0]);
	// was jComboBox4
	private AComboBox maxDistanceComboBox = new AComboBox(maxDistance);
	// was jComboBox3
	private AComboBox pulseWidthComboBox = new AComboBox(pulseWidth[0]);
	// was jComboBox5

	/**
	 * @todo only for testing mode 
	 */
	public static void main(String[] args) {
		Environment.initialize();
		LangModelSchedule.initialize();
		TestParametersPanel demo = new TestParametersPanel(null);
		JFrame mainFrame = new JFrame("TimeParametersPanel");
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		//mainFrame.getContentPane().add(demo);
		mainFrame.getContentPane().add(demo);
		mainFrame.pack();
		mainFrame.setSize(new Dimension(600, 465));
		mainFrame.setVisible(true);
	}

	public TestParametersPanel(ApplicationContext aContext) {
		this.aContext = aContext;
		this.dispatcher = aContext.getDispatcher();
		dispatcher.register(this, COMMAND_KIS_TYPE);
		dispatcher.register(this, COMMAND_PORT_TYPE);
		dispatcher.register(this, COMMAND_STOP_ANALYSIS);
		dispatcher.register(this, COMMAND_REMOVE_PARAM_FRAME);
		dispatcher.register(this, COMMAND_REMOVE_3A_FRAME);
		dispatcher.register(this, COMMAND_TEST_TYPE);
		dispatcher.register(this, COMMAND_ME_TYPE);
		dispatcher.register(this, COMMAND_VISUAL_TEST_PARAMS);
		dispatcher.register(this, COMMAND_EXT_AFTER_USUAL_ROOT_FRAME);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		setLayout(new BorderLayout());

		final JPanel switchPanel = new JPanel(new CardLayout());

		patternRadioButton =
			UIUtil
				.createRadioButton(
					LangModelSchedule.String("labelUsePattern"),
					new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (switchPanel.getLayout());
				cl.show(switchPanel, PATTERN_PANEL_NAME);
				revalidate();
			}
		});
		paramsRadioButton =
			UIUtil
				.createRadioButton(
					LangModelSchedule.String("labelUseParameters"),
					new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (switchPanel.getLayout());
				cl.show(switchPanel, PARAMETERS_PANEL_NAME);
				revalidate();
			}
		});
		ButtonGroup group = new ButtonGroup();
		group.add(patternRadioButton);
		group.add(paramsRadioButton);

		Box typePanel = new Box(BoxLayout.Y_AXIS);
		typePanel.add(patternRadioButton);
		typePanel.add(paramsRadioButton);

		add(typePanel, BorderLayout.NORTH);

		JPanel parametersPanel = new JPanel(new GridBagLayout());
		parametersPanel.setBorder(BorderFactory.createEtchedBorder());
		JLabel reflectLabel =
			new JLabel(LangModelSchedule.String("labelReflect"));
		JLabel waveLengthLabel =
			new JLabel(LangModelSchedule.String("labelWaveLength"));
		JLabel countOfAverageOutLabel =
			new JLabel(LangModelSchedule.String("labelAverCount"));
		JLabel pulseWidthLabel =
			new JLabel(LangModelSchedule.String("labelImpuls"));
		JLabel resolutionLabel =
			new JLabel(LangModelSchedule.String("labelDetalM"));
		JLabel maxDistanceLabel =
			new JLabel(LangModelSchedule.String("labelMaxDistance"));

		reflectTextField.setText("1.467");
		{
			Dimension d = new Dimension(75, 20);
			UIUtil.setRigidSize(reflectTextField, d);
			UIUtil.setRigidSize(waveLengthComboBox, d);
			UIUtil.setRigidSize(countOfAverageOutComboBox, d);
			UIUtil.setRigidSize(pulseWidthComboBox, d);
			UIUtil.setRigidSize(resolutionComboBox, d);
			UIUtil.setRigidSize(maxDistanceComboBox, d);

		}
		/**
		 * @todo do follow
		 */

		reflectTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				objList.set(0, reflectTextField.getText());
				sendParameters();
			}
		});
		waveLengthComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					objList.set(1, waveLengthComboBox.getSelectedItem());
					sendParameters();
				}
			}
		});
		countOfAverageOutComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					objList.set(2, countOfAverageOutComboBox.getSelectedItem());
					sendParameters();
				}
			}
		});
		resolutionComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					objList.set(4, resolutionComboBox.getSelectedItem());
					sendParameters();
				}
			}
		});
		pulseWidthComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					objList.set(5, pulseWidthComboBox.getSelectedItem());
					sendParameters();
				}
			}
		});

		maxDistanceComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					//System.out.println(Par_Objects.size());
					//Par_Objects.setElementAt(pulseWidthComboBox.getSelectedItem(), 3);
					objList.set(3, maxDistanceComboBox.getSelectedItem());
					int index = maxDistanceComboBox.getSelectedIndex();

					pulseWidthComboBox.removeAllItems();
					resolutionComboBox.removeAllItems();

					for (int j = 0; j < pulseWidth[index].length; j++)
						pulseWidthComboBox.addItem(pulseWidth[index][j]);
					for (int j = 0; j < resolution[index].length; j++)
						resolutionComboBox.addItem(resolution[index][j]);

					sendParameters();
				}
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		parametersPanel.add(reflectLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		parametersPanel.add(reflectTextField, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		parametersPanel.add(waveLengthLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		parametersPanel.add(waveLengthComboBox, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		parametersPanel.add(countOfAverageOutLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		parametersPanel.add(countOfAverageOutComboBox, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		parametersPanel.add(pulseWidthLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		parametersPanel.add(pulseWidthComboBox, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		parametersPanel.add(resolutionLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		parametersPanel.add(resolutionComboBox, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		parametersPanel.add(maxDistanceLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		parametersPanel.add(maxDistanceComboBox, gbc);
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.SOUTH;
		parametersPanel.add(new JLabel(), gbc);

		JPanel patternPanel = new JPanel(new BorderLayout());
		patternPanel.setBorder(BorderFactory.createEtchedBorder());
		testSetups = new ObjectResourceListBox();
		JScrollPane scroll = new JScrollPane(testSetups);
		patternPanel.add(scroll, BorderLayout.CENTER);

		switchPanel.add(parametersPanel, PARAMETERS_PANEL_NAME);
		switchPanel.add(patternPanel, PATTERN_PANEL_NAME);
		add(switchPanel, BorderLayout.CENTER);

		paramsRadioButton.doClick();
	}

	private void sendParameters() {
		dispatcher.notify(new OperationEvent(objList, 0, PARAMETER_PARAMETER));
		dispatcher.notify(new OperationEvent("", 0, COMMAND_STOP_ANALYSIS));

	}
	public void setTest(Test test) {
		if (test.test_type_id.equals(TEST_TYPE_TRACE_AND_ANALYSE)) {
			TestSetup tset =
				(TestSetup) Pool.get(TestSetup.typ, test.test_setup_id);
			//new SurveyDataSourceImage(aCon
			//tset.
			ReflectometryTestPanel p =
				new ReflectometryTestPanel(aContext, test);
			//parametersPanel.removeAll();
			//parametersPanel.add(p, BorderLayout.CENTER);
			//updateUI();
			revalidate();
		}
	}

	public void operationPerformed(OperationEvent ae) {
		ApplicationModel aModel = aContext.getApplicationModel();
		String commandName = ae.getActionCommand();
		System.out.println("commandName:" + commandName);

		if (commandName.equals(COMMAND_STOP_ANALYSIS)) {
			//ListModel lm = orList.getModel();
			//orList.setModel(lm);
		} else if (commandName.equals(COMMAND_ME_TYPE)) {
			testSetups.removeAll();
			//orList.setContents("");			
			//meid = ae.getSource().toString();
		} else if (commandName.equals(COMMAND_EXT_AFTER_USUAL_ROOT_FRAME)) {
			testSetups.removeAll();
			//orList.setContents("");
		} else if (
			commandName.equals(COMMAND_KIS_TYPE)
				|| commandName.equals(COMMAND_PORT_TYPE)) {
			testSetups.removeAll();
			//orList.setContents("");
			dispatcher.notify(new OperationEvent("", 0, COMMAND_STOP_ANALYSIS));
		} else if (commandName.equals(COMMAND_VISUAL_TEST_PARAMS)) {
			testSetups.removeAll();
			//orList.setContents("");
			Test parTest = (Test) ae.getSource();
			DataSourceInterface dsi = aContext.getDataSourceInterface();
			meId = parTest.monitored_element_id;
			testTypeId = parTest.test_type_id;
			String[] ts_me =
				new SurveyDataSourceImage(dsi).getTestSetupByME(meId);
			//			//			String[] ts_me = dsi.getTestSetupsByME(meid);
			String[] ts_tt =
				new SurveyDataSourceImage(dsi).getTestSetupByTestType(
					testTypeId);
			//			//			String[] ts_tt = dsi.getTestSetupsByTestType(testtypeid);
			String[] ts = new String[ts_me.length + ts_tt.length];
			for (int i = 0; i < ts_me.length; i++) {
				ts[i] = ts_me[i];
				//orList.add((TestSetup) Pool.get(TestSetup.typ, ts[i]));
				testSetups.add((TestSetup) Pool.get(TestSetup.typ, ts[i]));
			}
			for (int i = 0; i < ts_tt.length; i++) {
				ts[i + ts_me.length] = ts_tt[i];
				//				orList.add((TestSetup) Pool.get(TestSetup.typ, ts[i + ts_me.length]));
				testSetups.add(
					(TestSetup) Pool.get(TestSetup.typ, ts[i + ts_me.length]));
			}

			dsi.LoadTestArgumentSets(
				new String[] { parTest.test_argument_set_id });
			TestArgumentSet as =
				(TestArgumentSet) Pool.get(
					TestArgumentSet.typ,
					parTest.test_argument_set_id);
			Vector arg = as.arguments;
			if (arg.size() == 1) {
				try {
					Parameter param1 = (Parameter) arg.elementAt(0);
					String par1 = (new ByteArray(param1.value)).toUTFString();
					waveLengthComboBox.setSelectedItem(String.valueOf(par1));
				} catch (java.io.IOException ex) {
				}
			} else if (arg.size() == 6) {
				try {
					for (int i = 0; i < arg.size(); i++) {
						Parameter par = (Parameter) arg.elementAt(i);

						if (par.codename.equals(PARAMETER_REFLECTION)) {
							reflectTextField.setText(
								String.valueOf(
									(new ByteArray(par.value)).toDouble()));

						} else if (par.codename.equals(PARAMETER_WAVELENGHT)) {
							waveLengthComboBox.setSelectedItem(
								String.valueOf(
									(new ByteArray(par.value)).toInt()));
						} else if (
							par.codename.equals(PARAMETER_AVERAGEOUT_COUNT)) {
							countOfAverageOutComboBox.setSelectedItem(
								String.valueOf(
									(new ByteArray(par.value)).toDouble()));
						} else if (
							par.codename.equals(PARAMETER_PULSE_WIDTH)) {
							pulseWidthComboBox.setSelectedItem(
								String.valueOf(
									(new ByteArray(par.value)).toDouble()));
						} else if (par.codename.equals(PARAMETER_RESOLUTION)) {
							pulseWidthComboBox.setSelectedItem(
								String.valueOf(
									(new ByteArray(par.value)).toDouble()));
						} else if (
							par.codename.equals(PARAMETER_MAX_DISTANCE)) {
							maxDistanceComboBox.setSelectedItem(
								String.valueOf(
									(new ByteArray(par.value)).toLong()));
						}
					}
				} catch (java.io.IOException ex) {
				}
			}
			TestSetup testsetup =
				(TestSetup) Pool.get(TestSetup.typ, parTest.test_setup_id);
			if (testsetup != null) {
				/**
				 * @todo testSetups select testsetup
				 */

				testSetups.setSelectedIndex(0);
				//orList.setSelected(testsetup);
				dispatcher.notify(
					new OperationEvent(testsetup, 0, COMMAND_TEST_SETUP));
			}

			dispatcher.notify(
				new OperationEvent(parTest, 0, COMMAND_VISUAL_TEST_SETUP));
		} else if (commandName.equals(COMMAND_REMOVE_PARAM_FRAME)) {
			//this.dispose();
		} else if (commandName.equals(COMMAND_REMOVE_3A_FRAME)) {
			//this.dispose();
		} else if (commandName.equals(COMMAND_TEST_TYPE)) {

			testTypeId = ae.getSource().toString();
			testSetups.removeAll();
			DataSourceInterface dsi = aContext.getDataSourceInterface();
			String[] ts_me =
				new SurveyDataSourceImage(dsi).getTestSetupByME(meId);
			String[] ts_tt =
				new SurveyDataSourceImage(dsi).getTestSetupByTestType(
					testTypeId);
			//			String[] ts_me = dsi.getTestSetupsByME(meid);
			//			String[] ts_tt = dsi.getTestSetupsByTestType(testTypeId);
			String[] ts = new String[ts_me.length + ts_tt.length];
			for (int i = 0; i < ts_me.length; i++) {
				ts[i] = ts_me[i];
				testSetups.add((TestSetup) Pool.get(TestSetup.typ, ts[i]));
			}
			for (int i = 0; i < ts_tt.length; i++) {
				ts[i + ts_me.length] = ts_tt[i];
				TestSetup tst =
					(TestSetup) Pool.get(TestSetup.typ, ts[i + ts_me.length]);
				if (tst.monitored_element_ids.length == 0)
					testSetups.add(tst);
			}

			if (ae
				.getSource()
				.toString()
				.equals(TEST_TYPE_TRACE_AND_ANALYSE)) {
				objList.clear();

				//				jLabel1.setText(LangModelSchedule.String("labelReflect"));
				//				jLabel2.setText(LangModelSchedule.String("labelWaveLength"));
				//				jLabel3.setText(LangModelSchedule.String("labelAverCount"));
				//				jLabel4.setText(LangModelSchedule.String("labelImpuls"));
				//				jLabel5.setText(LangModelSchedule.String("labelDetalM"));
				//				jLabel6.setText(LangModelSchedule.String("labelMaxDistance"));

				reflectTextField.setText("1.467");

				if (ts.length == 0) {
					dispatcher.notify(
						new OperationEvent("", 0, COMMAND_STOP_ANALYSIS));
				}

				objList.add(reflectTextField.getText());
				objList.add(waveLengthComboBox.getSelectedItem());
				objList.add(countOfAverageOutComboBox.getSelectedItem());
				objList.add(pulseWidthComboBox.getSelectedItem());
				objList.add(resolutionComboBox.getSelectedItem());
				objList.add(maxDistanceComboBox.getSelectedItem());

			} else if (
				ae.getSource().toString().equals(TEST_TYPE_VOICE_ANALYSE)) {
				objList.clear();
				if (ts.length == 0) {
					dispatcher.notify(
						new OperationEvent("", 0, COMMAND_STOP_ANALYSIS));
				}
				//jLabel1.setText(LangModelSchedule.String("labelIdIzmer"));
				reflectTextField.setText("");
				objList.add(reflectTextField.getText());

				//
				//				jLabel1.setVisible(true);
				//				jLabel2.setVisible(false);
				//				jLabel3.setVisible(false);
				//				jLabel4.setVisible(false);
				//				jLabel5.setVisible(false);
				//				jLabel6.setVisible(false);
				//
				//				reflectTextField.setVisible(true);
				//				waveLengthComboBox.setVisible(false);
				//				countOfAverageOutComboBox.setVisible(false);
				//				pulseWidthComboBox.setVisible(false);
				//				resolutionComboBox.setVisible(false);
				//				maxDistanceComboBox.setVisible(false);

			}
		}
		aModel.fireModelChanged("");
	}

	private class ReflectometryTestPanel extends JPanel {
		private AComboBox cb = new AComboBox();
		private ApplicationContext aContext;
		private Test test;

		protected ReflectometryTestPanel(
			ApplicationContext aContext,
			Test test) {
			this.aContext = aContext;
			this.test = test;
			try {
				jbInit();
			} catch (Exception e) {
				e.printStackTrace();
			}

			init(test);
		}

		private void jbInit() throws Exception {
			setBorder(BorderFactory.createEtchedBorder());

			add(cb);
		}

		void init(Test test) {
			TestArgumentSet tas =
				(TestArgumentSet) Pool.get(
					TestArgumentSet.typ,
					test.test_argument_set_id);
			if (tas == null) {
				aContext.getDataSourceInterface().LoadTestArgumentSets(
					new String[] { test.test_argument_set_id });
				tas =
					(TestArgumentSet) Pool.get(
						TestArgumentSet.typ,
						test.test_argument_set_id);
			}

			if (tas == null) {
				System.err.println(
					"TestArgumentSet not found " + test.test_argument_set_id);
				return;
			}

			double ior = 0;
			int wavelength = 0;
			int averages = 0;
			double length = 0;
			double resolution = 0;
			double pulsewidth = 0;

			try {
				for (Enumeration en = tas.arguments.elements();
					en.hasMoreElements();
					) {
					Parameter p = (Parameter) en.nextElement();
					if (p.codename.equals(PARAMETER_REFLECTION))
						ior = new ByteArray(p.value).toDouble();
					else if (p.codename.equals(PARAMETER_WAVELENGHT))
						wavelength = new ByteArray(p.value).toInt();
					else if (p.codename.equals(PARAMETER_AVERAGEOUT_COUNT))
						averages = (int) new ByteArray(p.value).toDouble();
					else if (p.codename.equals(PARAMETER_PULSE_WIDTH))
						pulsewidth = new ByteArray(p.value).toDouble();
					else if (p.codename.equals(PARAMETER_RESOLUTION))
						resolution = new ByteArray(p.value).toDouble();
					else if (p.codename.equals(PARAMETER_MAX_DISTANCE))
						length = new ByteArray(p.value).toDouble();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			cb.addItem(String.valueOf(ior));
		}
	}

}
