package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Schedule.ScheduleMainFrame;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.util.*;
import java.io.*;

public class TestParametersPanel extends JPanel implements OperationListener {

	//	public static final String COMMAND_STOP_ANALYSIS = "StopAnalysis";
	//	public static final String COMMAND_ME_TYPE = "METype";
	//	public static final String COMMAND_KIS_TYPE = "KISType";
	//	public static final String COMMAND_PORT_TYPE = "PortType";
	//	public static final String COMMAND_VISUAL_TEST_SETUP = "VisualTestSetup";
	//	public static final String COMMAND_TEST_TYPE = "TestType";
	//	public static final String COMMAND_TEST_SETUP = "TestSetup";
	//	public static final String COMMAND_VISUAL_TEST_PARAMS =
	// "VisualTestParams";
	//	public static final String COMMAND_EXT_AFTER_USUAL_ROOT_FRAME =
	// "ExtendedAfterUsual_RootFrame";
	//	public static final String COMMAND_REMOVE_PARAM_FRAME =
	// "RemoveParamFrame";
	//	public static final String COMMAND_REMOVE_3A_FRAME = "Remove3aFrame";

	public static final String		COMMAND_CHANGE_PARAM_PANEL	= "ChangeParamPanel";
	public static final String		COMMAND_ADD_PARAM_PANEL		= "AddParamPanel";
	public static final String		COMMAND_CHANGE_PORT_TYPE	= "ChangePortType";
	//public static final String COMMAND_ADD_PARAM_PANEL = "ParamPanel";
	public static final String		TEST_TYPE_TRACE_AND_ANALYSE	= "trace_and_analyse";
	public static final String		TEST_TYPE_VOICE_ANALYSE		= "voice_analyse";
	public static final String		PARAMETER_PARAMETER			= "Parameter";
	public static final String		PARAMETERS_PANEL_PREFIX		= "PARAMETERS_PANEL";

	private Dispatcher				dispatcher;
	private ApplicationContext		aContext;

	private JRadioButton			patternRadioButton;
	private JRadioButton			paramsRadioButton;

	private final JPanel			switchPanel					= new JPanel(
																		new CardLayout());

	//	private HashMap objMap = new HashMap();
	//	private String testTypeId;
	//	private String meId;
	private ObjectResourceListBox	testSetups;

	private static final String		PATTERN_PANEL_NAME			= "PATTERN_PANEL";

	private HashMap					testPanels					= new HashMap();

	private Test					test;

	private SurveyDataSourceImage	surveyDsi;

	private String					currentParametersPanelName;

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
		initModule(aContext.getDispatcher());
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add test parameter panel for various TestTypes
	 * 
	 * @param command
	 *            when dispatcher get this command name test parameter panel
	 *            switch to panel
	 * @param panel
	 *            ParametersTestPanel
	 */
	public void addParameterPanel(String command, ParametersTestPanel panel) {
		testPanels.put(command, panel);
		switchPanel.add(panel, command);
		paramsRadioButton.setEnabled(true);
	}

	public boolean isParameterPanelExists(String command) {
		return testPanels.get(command) != null;
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		//		this.dispatcher.register(this, COMMAND_KIS_TYPE);
		//		this.dispatcher.register(this, COMMAND_PORT_TYPE);
		//		this.dispatcher.register(this, COMMAND_STOP_ANALYSIS);
		//		this.dispatcher.register(this, COMMAND_REMOVE_PARAM_FRAME);
		//		this.dispatcher.register(this, COMMAND_REMOVE_3A_FRAME);
		//		this.dispatcher.register(this, COMMAND_TEST_TYPE);
		//		this.dispatcher.register(this, COMMAND_ME_TYPE);
		//		this.dispatcher.register(this, COMMAND_VISUAL_TEST_PARAMS);
		//		this.dispatcher.register(this, COMMAND_EXT_AFTER_USUAL_ROOT_FRAME);
		////
		this.dispatcher.register(this, TestRequestFrame.COMMAND_DATA_REQUEST);
		this.dispatcher.register(this, COMMAND_CHANGE_PARAM_PANEL);
		this.dispatcher.register(this, COMMAND_ADD_PARAM_PANEL);
	}

	private void jbInit() throws Exception {
		setLayout(new BorderLayout());

		patternRadioButton = UIUtil.createRadioButton(LangModelSchedule
				.getString("labelUsePattern"), new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (switchPanel.getLayout());
				cl.show(switchPanel, PATTERN_PANEL_NAME);
				revalidate();
			}
		});
		paramsRadioButton = UIUtil.createRadioButton(LangModelSchedule
				.getString("labelUseParameters"), new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (switchPanel.getLayout());
				cl.show(switchPanel, currentParametersPanelName);
				revalidate();
			}
		});
		paramsRadioButton.setEnabled(false);
		ButtonGroup group = new ButtonGroup();
		group.add(patternRadioButton);
		group.add(paramsRadioButton);

		Box typePanel = new Box(BoxLayout.Y_AXIS);
		typePanel.add(patternRadioButton);
		typePanel.add(paramsRadioButton);

		add(typePanel, BorderLayout.NORTH);

		JPanel parametersPanel = new JPanel(new GridBagLayout());
		parametersPanel.setBorder(BorderFactory.createEtchedBorder());

		JPanel patternPanel = new JPanel(new BorderLayout());
		patternPanel.setBorder(BorderFactory.createEtchedBorder());
		testSetups = new ObjectResourceListBox();
		JScrollPane scroll = new JScrollPane(testSetups);
		patternPanel.add(scroll, BorderLayout.CENTER);

		//switchPanel.add(parametersPanel, PARAMETERS_PANEL_NAME);
		switchPanel.add(patternPanel, PATTERN_PANEL_NAME);
		add(switchPanel, BorderLayout.CENTER);

		patternRadioButton.doClick();

	}

	public void setTest(Test test) {
		this.test = test;
		testSetups.removeAll();
		DataSourceInterface dsi = aContext.getDataSourceInterface();
		String meid = test.monitored_element_id;
		String testtypeid = test.test_type_id;
		if (surveyDsi == null) surveyDsi = new SurveyDataSourceImage(dsi);
		String[] ts_me = surveyDsi.getTestSetupByME(meid);
		String[] ts_tt = surveyDsi.getTestSetupByTestType(testtypeid);
		for (int i = 0; i < ts_me.length; i++) {
			TestSetup ts = (TestSetup) Pool.get(TestSetup.typ, ts_me[i]);
			testSetups.add(ts);
		}
		for (int i = 0; i < ts_tt.length; i++) {
			TestSetup ts = (TestSetup) Pool.get(TestSetup.typ, ts_tt[i]);
			testSetups.add(ts);
		}

		boolean selected = false;
		TestSetup testsetup = (TestSetup) Pool.get(TestSetup.typ,
				test.test_setup_id);
		if (testsetup != null) {
			//orList.setSelected(testsetup);
			testSetups.setSelected(testsetup);
			selected = true;
		}
		if (selected)
			patternRadioButton.doClick();
		else
			paramsRadioButton.doClick();
	}

	public TestSetup getParameters() {
		TestSetup ts = (TestSetup) testSetups.getSelectedObjectResource();
		return ts;
	}

	public void operationPerformed(OperationEvent ae) {
		ApplicationModel aModel = aContext.getApplicationModel();
		String commandName = ae.getActionCommand();
		Object obj = ae.getSource();
		if (ScheduleMainFrame.DEBUG)
				System.out.println(getClass().getName() + " commandName:"
						+ commandName);
		if (commandName.equalsIgnoreCase(TestRequestFrame.COMMAND_DATA_REQUEST)) {
			if (paramsRadioButton.isSelected()) {
				TestArgumentSet tas = ((ParametersTestPanel) (testPanels
						.get(currentParametersPanelName))).getParameters();
				dispatcher.notify(new OperationEvent(tas,
						TestRequestFrame.DATA_ID_PARAMETERS,
						TestRequestFrame.COMMAND_SEND_DATA));
			} else if (patternRadioButton.isSelected()) {
				TestSetup ts = this.getParameters();
				if (ts != null)
						dispatcher.notify(new OperationEvent(ts,
								TestRequestFrame.DATA_ID_PARAMETERS_PATTERN,
								TestRequestFrame.COMMAND_SEND_DATA));
			}

		} else if (commandName.equals(COMMAND_CHANGE_PARAM_PANEL)) {
			currentParametersPanelName = obj.toString();
			paramsRadioButton.doClick();
		} else if (commandName.equals(COMMAND_ADD_PARAM_PANEL)) {
			ParametersTestPanel panel = (ParametersTestPanel) obj;
			String name = panel.getPanelName();
			//System.out.println("name : "+name);
			if (!isParameterPanelExists(name)) {
				//System.out.println("name isn't at map");
				addParameterPanel(name, panel);
			}
			currentParametersPanelName = name;
			paramsRadioButton.doClick();
		} else if (commandName.equals(TestUpdateEvent.typ)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			setTest(tue.test);
		}

		aModel.fireModelChanged("");
	}

}