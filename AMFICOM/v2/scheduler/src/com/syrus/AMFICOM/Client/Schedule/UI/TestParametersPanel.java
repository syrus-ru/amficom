package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;

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
	public static final boolean		DEBUG						= true;
	public static final String		COMMAND_CHANGE_PARAM_PANEL	= "ChangeParamPanel";									//$NON-NLS-1$
	public static final String		COMMAND_ADD_PARAM_PANEL		= "AddParamPanel";										//$NON-NLS-1$
	public static final String		COMMAND_CHANGE_PORT_TYPE	= "ChangePortType";									//$NON-NLS-1$
	public static final String		COMMAND_CHANGE_TEST_TYPE	= "ChangeTestType";									//$NON-NLS-1$
	public static final String		COMMAND_CHANGE_ME_TYPE		= "ChangeMEType";										//$NON-NLS-1$
	public static final String		COMMAND_CHANGE_KIS			= "ChangeKIS";											//$NON-NLS-1$
	//public static final String COMMAND_ADD_PARAM_PANEL = "ParamPanel";
	public static final String		TEST_TYPE_TRACE_AND_ANALYSE	= "trace_and_analyse";									//$NON-NLS-1$
	public static final String		TEST_TYPE_VOICE_ANALYSE		= "voice_analyse";										//$NON-NLS-1$
	public static final String		PARAMETER_PARAMETER			= "Parameter";											//$NON-NLS-1$
	public static final String		PARAMETERS_PANEL_PREFIX		= "PARAMETERS_PANEL";									//$NON-NLS-1$

	private Dispatcher				dispatcher;
	ApplicationContext				aContext;

	private JRadioButton			patternRadioButton;
	JRadioButton					paramsRadioButton;

	JCheckBox						useAnalysisBox;
	ObjectResourceComboBox			analysisComboBox			= new ObjectResourceComboBox(AnalysisType.typ, true);
	ObjectResourceComboBox			evaluationComboBox			= new ObjectResourceComboBox(EvaluationType.typ, true);

	final JPanel					switchPanel					= new JPanel(new CardLayout());

	//	private HashMap objMap = new HashMap();
	//	private String testTypeId;
	//	private String meId;

	ObjectResourceListBox			testSetups;
	private HashMap					testMap;

	private static final String		PATTERN_PANEL_NAME			= "PATTERN_PANEL";										//$NON-NLS-1$

	HashMap							testPanels					= new HashMap();

	private Test					test;

	private SurveyDataSourceImage	surveyDsi;
	private HashMap					parameters;

	String							currentParametersPanelName;

	/**
	 * @todo only for testing mode
	 */
	public static void main(String[] args) {
		Environment.initialize();
		TestParametersPanel demo = new TestParametersPanel(null);
		JFrame mainFrame = new JFrame("TimeParametersPanel"); //$NON-NLS-1$
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

		if (aContext != null) {
			initModule(aContext.getDispatcher());
		}
		setLayout(new BorderLayout());

		patternRadioButton = UIStorage
				.createRadioButton(LangModelSchedule.getString("UsePattern"), new AbstractAction() { //$NON-NLS-1$

										public void actionPerformed(ActionEvent e) {
											CardLayout cl = (CardLayout) (switchPanel.getLayout());
											cl.show(switchPanel, PATTERN_PANEL_NAME);
											revalidate();
										}
									});
		paramsRadioButton = UIStorage
				.createRadioButton(LangModelSchedule.getString("UseParameters"), new AbstractAction() { //$NON-NLS-1$

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

		JPanel patternPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		patternPanel.setBorder(BorderFactory.createEtchedBorder());
		useAnalysisBox = new JCheckBox(LangModelSchedule.getString("PerformAnalys"), true); //$NON-NLS-1$
		patternPanel.add(useAnalysisBox, gbc);
		final JLabel analysisLabel = new JLabel(LangModelSchedule.getString("Analysis")); //$NON-NLS-1$
		patternPanel.add(analysisLabel, gbc);
		patternPanel.add(analysisComboBox, gbc);
		final JLabel evaluationLabel = new JLabel(LangModelSchedule.getString("EvaluationAnalysis")); //$NON-NLS-1$
		patternPanel.add(evaluationLabel, gbc);
		patternPanel.add(evaluationComboBox, gbc);
		testMap = new HashMap();
		testSetups = new ObjectResourceListBox();
		testSetups.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				//if (e.getStateChange() == ItemEvent.SELECTED) {
				TestSetup ts = (TestSetup) TestParametersPanel.this.testSetups.getSelectedObjectResource();
				if (ts != null) {
					TestParametersPanel.this.useAnalysisBox.setEnabled(true);
					DataSet dsAnalysis = new DataSet();
					dsAnalysis.add((ObjectResource) Pool.get(AnalysisType.typ, ts.getAnalysisTypeId()));

					DataSet dsEvaluation = new DataSet();
					dsEvaluation.add((ObjectResource) Pool.get(EvaluationType.typ, ts.getEvaluationTypeId()));
					TestParametersPanel.this.analysisComboBox.setContents(dsAnalysis.elements(), true);
					TestParametersPanel.this.evaluationComboBox.setContents(dsEvaluation.elements(), true);

					for (Iterator it = TestParametersPanel.this.testPanels.keySet().iterator(); it.hasNext();) {
						String key = (String) it.next();
						ParametersTestPanel panel = (ParametersTestPanel) (TestParametersPanel.this.testPanels.get(key));
						panel.setTestSetup(ts);
					}

				} else {
					TestParametersPanel.this.paramsRadioButton.doClick();
				}
			}
			//}
			});
		JScrollPane scroll = new JScrollPane(testSetups);
		gbc.weighty = 1.0;
		patternPanel.add(scroll, gbc);

		useAnalysisBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean enable = checkBox.isSelected();
				analysisLabel.setEnabled(enable);
				analysisComboBox.setEnabled(enable);
				evaluationLabel.setEnabled(enable);
				evaluationComboBox.setEnabled(enable);
				//				if (enable) {
				//					TestSetup ts = (TestSetup) testSetups
				//							.getSelectedObjectResource();
				//					
				//
				//				}

			}
		});
		// it's for set enabled status for analysisComboBox & evaluationComboBox
		useAnalysisBox.doClick();
		useAnalysisBox.setEnabled(testSetups.getModel().getSize() > 0);

		//switchPanel.add(parametersPanel, PARAMETERS_PANEL_NAME);
		switchPanel.add(patternPanel, PATTERN_PANEL_NAME);
		add(switchPanel, BorderLayout.CENTER);

		patternRadioButton.doClick();

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
		this.dispatcher.register(this, SchedulerModel.COMMAND_DATA_REQUEST);
		this.dispatcher.register(this, COMMAND_CHANGE_PARAM_PANEL);
		this.dispatcher.register(this, COMMAND_ADD_PARAM_PANEL);
		this.dispatcher.register(this, COMMAND_CHANGE_TEST_TYPE);
		this.dispatcher.register(this, COMMAND_CHANGE_ME_TYPE);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CLEAN);
	}

	public void setTest(Test test) {
		this.test = test;		
		String meid = test.getMonitoredElementId();
		String testtypeid = test.getTestTypeId();
		if (surveyDsi == null)
			surveyDsi = new SurveyDataSourceImage(aContext.getDataSourceInterface());
		String[] testSetupME = surveyDsi.getTestSetupByME(meid);
		String[] testSetupTestType = surveyDsi.getTestSetupByTestType(testtypeid);
		for (int i = 0; i < testSetupME.length; i++) {
			//System.out.println("ts_me:" + ts_me[i]);
			TestSetup ts = (TestSetup) Pool.get(TestSetup.typ, testSetupME[i]);
			//testSetups.add(ts);
			testMap.put(ts.getId(), ts);
		}
		for (int i = 0; i < testSetupTestType.length; i++) {
			//System.out.println("ts_tt:" + ts_tt[i]);
			TestSetup ts = (TestSetup) Pool.get(TestSetup.typ, testSetupTestType[i]);
			//testSetups.add(ts);
			testMap.put(ts.getId(), ts);
		}
				updateTestSetupList();
		
//				boolean selected = false;
//				TestSetup testsetup = (TestSetup) Pool.get(TestSetup.typ,
//						test.getTestSetupId());
//				if (testsetup != null) {
//					//orList.setSelected(testsetup);
//					System.out.println("selected:" + testsetup.id);
//					testSetups.setSelected(testsetup);
//					selected = true;
//				}
//				if (selected)
//					patternRadioButton.doClick();
//				else
//					paramsRadioButton.doClick();
//				useAnalysisBox.setEnabled(testSetups.getModel().getSize() > 0);
//		System.out.println("end of setTest");
	}

	private void getParameters() {
		if (parameters == null)
			parameters = new HashMap();
		TestSetup ts = (TestSetup) testSetups.getSelectedObjectResource();
		if (ts == null) {
			JOptionPane
					.showMessageDialog(
										this,
										LangModelSchedule.getString("Do_not_choose_measurement_pattern"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
										JOptionPane.OK_OPTION);
			parameters = null;
			return;
		}
		parameters.put(TestSetup.typ, ts);
		EvaluationType evaluationType = null;
		AnalysisType analysisType = null;
		//DataSourceInterface dsi = aContext.getDataSourceInterface();
		if (useAnalysisBox.isSelected()) {
			/**
			 * @todo neeed to put evaluation and analysis
			 */
			evaluationType = (EvaluationType) evaluationComboBox.getSelectedObjectResource();
			analysisType = (AnalysisType) analysisComboBox.getSelectedObjectResource();

		}
		//if (evaluationType != null)
		parameters.put(EvaluationType.typ, evaluationType);
		//if (analysisType != null)
		parameters.put(AnalysisType.typ, analysisType);
	}

	public void operationPerformed(OperationEvent ae) {
		ApplicationModel aModel = aContext.getApplicationModel();
		String commandName = ae.getActionCommand();
		Object obj = ae.getSource();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equalsIgnoreCase(SchedulerModel.COMMAND_DATA_REQUEST)) {
			if (paramsRadioButton.isSelected()) {
				TestArgumentSet tas = ((ParametersTestPanel) (testPanels.get(currentParametersPanelName)))
						.getTestArgumentSet();
				if (tas != null)
					dispatcher.notify(new OperationEvent(tas, SchedulerModel.DATA_ID_PARAMETERS,
															SchedulerModel.COMMAND_SEND_DATA));
			} else if (patternRadioButton.isSelected()) {
				this.getParameters();
				if (parameters != null) {
					TestSetup ts = (TestSetup) parameters.get(TestSetup.typ);
					AnalysisType analysisType = (AnalysisType) parameters.get(AnalysisType.typ);
					EvaluationType evaluationType = (EvaluationType) parameters.get(EvaluationType.typ);
					dispatcher.notify(new OperationEvent((ts == null) ? (Object) "" : (Object) ts, //$NON-NLS-1$
															SchedulerModel.DATA_ID_PARAMETERS_PATTERN,
															SchedulerModel.COMMAND_SEND_DATA));
					//if (analysisType != null)
					dispatcher.notify(new OperationEvent((analysisType == null) ? (Object) "" //$NON-NLS-1$
							: (Object) analysisType, SchedulerModel.DATA_ID_PARAMETERS_ANALYSIS,
															SchedulerModel.COMMAND_SEND_DATA));
					dispatcher.notify(new OperationEvent((evaluationType == null) ? (Object) "" //$NON-NLS-1$
							: (Object) evaluationType, SchedulerModel.DATA_ID_PARAMETERS_EVALUATION,
															SchedulerModel.COMMAND_SEND_DATA));
				}
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
		} else if (commandName.equals(TestUpdateEvent.TYPE)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			setTest(tue.test);
		} else if (commandName.equals(COMMAND_CHANGE_TEST_TYPE)) {
			String testtypeid = obj.toString();
			//testSetups.removeAll();
			if (surveyDsi == null)
				surveyDsi = new SurveyDataSourceImage(aContext.getDataSourceInterface());
			String[] testSetupTestType = surveyDsi.getTestSetupByTestType(testtypeid);
			for (int i = 0; i < testSetupTestType.length; i++) {
				//System.out.println(">" + ts_tt[i]);
				TestSetup ts = (TestSetup) Pool.get(TestSetup.typ, testSetupTestType[i]);
				//System.out.println("ts:" + ts.id);
				//testSetups.add(ts);
				testMap.put(ts.getId(), ts);
			}
			updateTestSetupList();

		} else if (commandName.equals(COMMAND_CHANGE_ME_TYPE)) {
			String meId = (String) obj;
			//			DataSourceInterface dsi = aContext.getDataSourceInterface();
			if (this.surveyDsi == null)
				this.surveyDsi = new SurveyDataSourceImage(aContext.getDataSourceInterface());
			this.testMap.clear();
			String[] testSetupME = this.surveyDsi.getTestSetupByME(meId);
			for (int i = 0; i < testSetupME.length; i++) {
				TestSetup ts = (TestSetup) Pool.get(TestSetup.typ, testSetupME[i]);
				//testSetups.add(ts);
				testMap.put(ts.getId(), ts);
			}
			updateTestSetupList();
		} else if (commandName.equals(SchedulerModel.COMMAND_CLEAN)) {
			this.testMap.clear();
			this.patternRadioButton.doClick();
			this.testPanels.clear();
			updateTestSetupList();
		}

		aModel.fireModelChanged(""); //$NON-NLS-1$
	}

	private void updateTestSetupList() {
		//System.out.println("updateTestSetupList");
		//TestSetup selectedTs = (TestSetup)
		// testSetups.getSelectedObjectResource();
		this.testSetups.removeAll();
		for (Iterator it = this.testMap.values().iterator(); it.hasNext();) {
			TestSetup ts = (TestSetup) it.next();
			this.testSetups.add(ts);
		}
		//testSetups.setSelected(selectedTs);
		if (this.test != null) {
			System.out.println("test.test_setup_id:" + this.test.getTestSetupId());		
			
			TestSetup testsetup = (TestSetup) Pool.get(TestSetup.typ, this.test.getTestSetupId());
			System.out.println((testsetup==null)?"testSetup is null":"testSetupID:"+testsetup.getId());
			if (testsetup != null) {
				//orList.setSelected(testsetup);
				//System.out.println("selected:" + testsetup.id);				
				ListModel lmodel = this.testSetups.getModel();
				for(int i=0;i<lmodel.getSize();i++){
					TestSetup ts2 = (TestSetup)lmodel.getElementAt(i);
					System.out.println("ts2.getId():"+ts2.getId());
				}
				
				this.testSetups.setSelected(testsetup);
				this.patternRadioButton.doClick();

				if ((this.test.getEvalution() != null) || (this.test.getAnalysis() != null)) {
					if (!useAnalysisBox.isSelected())
						useAnalysisBox.doClick();
				}
				if (test.getEvalution() != null) {
					//System.out.println("test.evalution isn't null");
					selectComboBox(evaluationComboBox, test.getEvalution().getTypeId());
				}
				if (test.getAnalysis() != null) {
					//System.out.println("test.analysis isn't null");
					selectComboBox(analysisComboBox, test.getAnalysis().getTypeId());
				}

			} else {
				this.paramsRadioButton.doClick();
			}
		}
	}

	private void selectComboBox(ObjectResourceComboBox cb, String value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			Object obj = cb.getItemAt(i);
			String id = null;
			//System.out.println("obj:" + obj.getClass().getName());
			if (obj instanceof AnalysisType) {
				id = ((AnalysisType) obj).getId();
			} else if (obj instanceof EvaluationType) {
				id = ((EvaluationType) obj).getId();
			}
			if (id != null) {
				//System.out.println("id:" + id);
				if (id.equals(value)) {
					cb.setSelectedIndex(i);
					//System.out.println("selected " + i);
					break;
				}
			}
		}
	}

}