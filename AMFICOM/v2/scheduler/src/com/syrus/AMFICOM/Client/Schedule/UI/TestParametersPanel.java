package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ISM.MonitoredElement;
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
	public static final boolean	DEBUG						= true;
	public static final String	COMMAND_CHANGE_PARAM_PANEL	= "ChangeParamPanel";									//$NON-NLS-1$
	public static final String	COMMAND_ADD_PARAM_PANEL		= "AddParamPanel";										//$NON-NLS-1$
	public static final String	COMMAND_CHANGE_PORT_TYPE	= "ChangePortType";									//$NON-NLS-1$
	public static final String	COMMAND_CHANGE_TEST_TYPE	= "ChangeTestType";									//$NON-NLS-1$
	public static final String	COMMAND_CHANGE_ME_TYPE		= "ChangeMEType";										//$NON-NLS-1$
	public static final String	COMMAND_CHANGE_KIS			= "ChangeKIS";											//$NON-NLS-1$
	//public static final String COMMAND_ADD_PARAM_PANEL = "ParamPanel";
	public static final String	TEST_TYPE_TRACE_AND_ANALYSE	= "trace_and_analyse";									//$NON-NLS-1$
	public static final String	TEST_TYPE_VOICE_ANALYSE		= "voice_analyse";										//$NON-NLS-1$
	public static final String	PARAMETER_PARAMETER			= "Parameter";											//$NON-NLS-1$
	public static final String	PARAMETERS_PANEL_PREFIX		= "PARAMETERS_PANEL";									//$NON-NLS-1$

	private Dispatcher			dispatcher;
	ApplicationContext			aContext;

	private JRadioButton		patternRadioButton;
	JRadioButton				paramsRadioButton;

	JCheckBox					useAnalysisBox;
	ObjectResourceComboBox		analysisComboBox			= new ObjectResourceComboBox(AnalysisType.typ, true);
	ObjectResourceComboBox		evaluationComboBox			= new ObjectResourceComboBox(EvaluationType.typ, true);

	final JPanel				switchPanel					= new JPanel(new CardLayout());

	//	private HashMap objMap = new HashMap();
	//	private String testTypeId;
	//	private String meId;

	ObjectResourceListBox		testSetups;
	private HashMap				testMap;

	private static final String	PATTERN_PANEL_NAME			= "PATTERN_PANEL";										//$NON-NLS-1$

	HashMap						testPanels					= new HashMap();

	private Test				test;

	private HashMap				parameters;
	private java.util.List		meList;

	String						currentParametersPanelName;

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
		JScrollPane scroll = new JScrollPane(this.testSetups);
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
		//		this.dispatcher.register(this, COMMAND_CHANGE_TEST_TYPE);
		this.dispatcher.register(this, COMMAND_CHANGE_ME_TYPE);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CLEAN);
		this.dispatcher.register(this, SchedulerModel.COMMAND_AVAILABLE_ME);
	}

	public void setTest(Test test) {
		this.test = test;

		this.meList.clear();
		this.meList.add(Pool.get(MonitoredElement.typ, test.getMonitoredElementId()));
		updateTestSetupList();

		if (this.test != null) {

			TestSetup testsetup = (TestSetup) Pool.get(TestSetup.typ, this.test.getTestSetupId());
			if (testsetup != null) {
				this.testSetups.setSelected(testsetup);
				this.patternRadioButton.doClick();

				if ((this.test.getEvalution() != null) || (this.test.getAnalysis() != null)) {
					if (!this.useAnalysisBox.isSelected())
						this.useAnalysisBox.doClick();
				}
				if (test.getEvalution() != null) {
					//System.out.println("test.evalution isn't null");
					selectComboBox(this.evaluationComboBox, test.getEvalution().getTypeId());
				}
				if (test.getAnalysis() != null) {
					//System.out.println("test.analysis isn't null");
					selectComboBox(this.analysisComboBox, test.getAnalysis().getTypeId());
				}

			} else {
				this.paramsRadioButton.doClick();
			}
		}
	}

	private void getParameters() {
		if (this.parameters == null)
			this.parameters = new HashMap();
		TestSetup ts = (TestSetup) this.testSetups.getSelectedObjectResource();
		if (ts == null) {
			JOptionPane
					.showMessageDialog(
										this,
										LangModelSchedule.getString("Do_not_choose_measurement_pattern"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
										JOptionPane.OK_OPTION);
			this.parameters = null;
			return;
		}
		this.parameters.put(TestSetup.typ, ts);
		EvaluationType evaluationType = null;
		AnalysisType analysisType = null;
		//DataSourceInterface dsi = aContext.getDataSourceInterface();
		if (this.useAnalysisBox.isSelected()) {
			evaluationType = (EvaluationType) this.evaluationComboBox.getSelectedObjectResource();
			analysisType = (AnalysisType) this.analysisComboBox.getSelectedObjectResource();

		}
		//if (evaluationType != null)
		this.parameters.put(EvaluationType.typ, evaluationType);
		//if (analysisType != null)
		this.parameters.put(AnalysisType.typ, analysisType);
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
					this.dispatcher.notify(new OperationEvent((evaluationType == null) ? (Object) "" //$NON-NLS-1$
							: (Object) evaluationType, SchedulerModel.DATA_ID_PARAMETERS_EVALUATION,
																SchedulerModel.COMMAND_SEND_DATA));
				}
			}

		} else if (commandName.equals(COMMAND_CHANGE_PARAM_PANEL)) {
			this.currentParametersPanelName = obj.toString();
			this.paramsRadioButton.doClick();
		} else if (commandName.equals(COMMAND_ADD_PARAM_PANEL)) {
			ParametersTestPanel panel = (ParametersTestPanel) obj;
			String name = panel.getPanelName();
			//System.out.println("name : "+name);
			if (!isParameterPanelExists(name)) {
				//System.out.println("name isn't at map");
				addParameterPanel(name, panel);
			}
			this.currentParametersPanelName = name;
			this.paramsRadioButton.doClick();
		} else if (commandName.equals(TestUpdateEvent.TYPE)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			setTest(tue.test);
		} else if (commandName.equals(SchedulerModel.COMMAND_CLEAN)) {
			this.testMap.clear();
			this.patternRadioButton.doClick();
			this.testPanels.clear();
			if (this.meList != null)
				this.meList.clear();
			updateTestSetupList();
		} else if (commandName.equals(SchedulerModel.COMMAND_AVAILABLE_ME)) {
			//this.meList = (java.util.List) obj;
			//updateTestSetupList();
		} else if (commandName.equals(COMMAND_CHANGE_ME_TYPE)) {
			String meId = (String) obj;
			if (this.meList == null)
				this.meList = new ArrayList();
			this.meList.clear();
			this.meList.add(Pool.get(MonitoredElement.typ, meId));
			System.out.println("get me:" + meId);
			updateTestSetupList();
		}

		aModel.fireModelChanged(""); //$NON-NLS-1$
	}

	private void updateTestSetupList() {
		this.testSetups.removeAll();
		this.testMap.clear();
		if (this.meList != null) {
			Hashtable tsTable = Pool.getHash(TestSetup.typ);
			if (tsTable != null) {
				for (Iterator it = tsTable.keySet().iterator(); it.hasNext();) {
					TestSetup ts = (TestSetup) tsTable.get(it.next());
					String[] meIds = ts.getMonitoredElementIds();
					if (meIds.length == 0) {
						System.out.println("meIds.length == 0\t" + ts.getId());
						this.testMap.put(ts.getId(), ts);
					} else
						for (Iterator iter = this.meList.iterator(); iter.hasNext();) {
							MonitoredElement me = (MonitoredElement) iter.next();
							String meId = me.getId();
							for (int i = 0; i < meIds.length; i++) {
								if (meIds[i].equals(meId)) {
									System.out.println("meId:" + meId + "\t" + ts.getId());
									this.testMap.put(ts.getId(), ts);
								}
							}
						}

				}
			}
		}
		for (Iterator it = this.testMap.values().iterator(); it.hasNext();) {
			TestSetup ts = (TestSetup) it.next();
			this.testSetups.add(ts);
		}
		//testSetups.setSelected(selectedTs);
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