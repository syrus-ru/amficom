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
	public static final String	PARAMETER_PARAMETER		= "Parameter";											//$NON-NLS-1$
	public static final String	PARAMETERS_PANEL_PREFIX	= "PARAMETERS_PANEL";									//$NON-NLS-1$

	private Dispatcher			dispatcher;
	ApplicationContext			aContext;

	private JRadioButton		patternRadioButton;
	JRadioButton				paramsRadioButton;

	JCheckBox					useAnalysisBox;
	ObjectResourceComboBox		analysisComboBox		= new ObjectResourceComboBox(AnalysisType.typ, true);
	ObjectResourceComboBox		evaluationComboBox		= new ObjectResourceComboBox(EvaluationType.TYPE, true);

	final JPanel				switchPanel				= new JPanel(new CardLayout());

	ObjectResourceListBox		testSetups;
	private HashMap				testMap;

	private static final String	PATTERN_PANEL_NAME		= "PATTERN_PANEL";										//$NON-NLS-1$

	HashMap						testPanels				= new HashMap();

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

		this.patternRadioButton = UIStorage
				.createRadioButton(LangModelSchedule.getString("UsePattern"), new AbstractAction() { //$NON-NLS-1$

										public void actionPerformed(ActionEvent e) {
											CardLayout cl = (CardLayout) (TestParametersPanel.this.switchPanel
													.getLayout());
											cl.show(TestParametersPanel.this.switchPanel, PATTERN_PANEL_NAME);
											revalidate();
										}
									});
		this.paramsRadioButton = UIStorage
				.createRadioButton(LangModelSchedule.getString("UseParameters"), new AbstractAction() { //$NON-NLS-1$

										public void actionPerformed(ActionEvent e) {
											CardLayout cl = (CardLayout) (TestParametersPanel.this.switchPanel
													.getLayout());
											cl.show(TestParametersPanel.this.switchPanel,
													TestParametersPanel.this.currentParametersPanelName);
											revalidate();
										}
									});
		this.paramsRadioButton.setEnabled(false);
		ButtonGroup group = new ButtonGroup();
		group.add(this.patternRadioButton);
		group.add(this.paramsRadioButton);

		Box typePanel = new Box(BoxLayout.Y_AXIS);
		typePanel.add(this.patternRadioButton);
		typePanel.add(this.paramsRadioButton);

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
		this.useAnalysisBox = new JCheckBox(LangModelSchedule.getString("PerformAnalys"), true); //$NON-NLS-1$
		patternPanel.add(this.useAnalysisBox, gbc);
		final JLabel analysisLabel = new JLabel(LangModelSchedule.getString("Analysis")); //$NON-NLS-1$
		patternPanel.add(analysisLabel, gbc);
		patternPanel.add(this.analysisComboBox, gbc);
		final JLabel evaluationLabel = new JLabel(LangModelSchedule.getString("EvaluationAnalysis")); //$NON-NLS-1$
		patternPanel.add(evaluationLabel, gbc);
		patternPanel.add(this.evaluationComboBox, gbc);
		this.testMap = new HashMap();
		patternPanel.add(new JLabel(LangModelSchedule.getString("Patterns")), gbc);
		this.testSetups = new ObjectResourceListBox();
		this.testSetups.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				//if (e.getStateChange() == ItemEvent.SELECTED) {
				TestSetup ts = (TestSetup) TestParametersPanel.this.testSetups.getSelectedObjectResource();
				if (ts != null) {
					TestParametersPanel.this.useAnalysisBox.setEnabled(true);
					//DataSet dsAnalysis = new DataSet();
					//dsAnalysis.add((ObjectResource)
					// Pool.get(AnalysisType.typ, ts.getAnalysisTypeId()));

					//DataSet dsEvaluation = new DataSet();
					//dsEvaluation.add((ObjectResource)
					// Pool.get(EvaluationType.typ, ts.getEvaluationTypeId()));
					//TestParametersPanel.this.analysisComboBox.setContents(dsAnalysis.elements(),
					// true);
					TestParametersPanel.this.analysisComboBox.removeAll();
					TestParametersPanel.this.analysisComboBox.add((ObjectResource) Pool.get(AnalysisType.typ, ts
							.getAnalysisTypeId()));

					//TestParametersPanel.this.evaluationComboBox.setContents(dsEvaluation.elements(),
					// true);
					TestParametersPanel.this.evaluationComboBox.removeAll();
					TestParametersPanel.this.evaluationComboBox.add((ObjectResource) Pool.get(EvaluationType.TYPE, ts
							.getEvaluationTypeId()));

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

		this.useAnalysisBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean enable = checkBox.isSelected();
				analysisLabel.setEnabled(enable);
				TestParametersPanel.this.analysisComboBox.setEnabled(enable);
				evaluationLabel.setEnabled(enable);
				TestParametersPanel.this.evaluationComboBox.setEnabled(enable);
				//				if (enable) {
				//					TestSetup ts = (TestSetup) testSetups
				//							.getSelectedObjectResource();
				//					
				//
				//				}

			}
		});
		// it's for set enabled status for analysisComboBox & evaluationComboBox
		this.useAnalysisBox.doClick();
		this.useAnalysisBox.setEnabled(this.testSetups.getModel().getSize() > 0);

		//switchPanel.add(parametersPanel, PARAMETERS_PANEL_NAME);
		this.switchPanel.add(patternPanel, PATTERN_PANEL_NAME);
		add(this.switchPanel, BorderLayout.CENTER);

		this.patternRadioButton.doClick();

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
		this.testPanels.put(command, panel);
		this.switchPanel.add(panel, command);
		this.paramsRadioButton.setEnabled(true);
	}

	public boolean isParameterPanelExists(String command) {
		return this.testPanels.get(command) != null;
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, SchedulerModel.COMMAND_DATA_REQUEST);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CHANGE_PARAM_PANEL);
		this.dispatcher.register(this, SchedulerModel.COMMAND_ADD_PARAM_PANEL);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CHANGE_ME_TYPE);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CLEAN);
		this.dispatcher.register(this, SchedulerModel.COMMAND_AVAILABLE_ME);
	}
	
	public void unregisterDispatcher() {
		for(Iterator it=this.testPanels.keySet().iterator();it.hasNext();){
			Object key = it.next();
			ParametersTestPanel panel = (ParametersTestPanel)this.testPanels.get(key);
			panel.unregisterDispatcher();
		}
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_DATA_REQUEST);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CHANGE_PARAM_PANEL);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_ADD_PARAM_PANEL);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CHANGE_ME_TYPE);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CLEAN);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_AVAILABLE_ME);
	}

	public void setTest(Test test) {
		if ((this.test == null) || (!this.test.getId().equals(test.getId()))) {
			this.test = test;

			if (this.meList == null)
				this.meList = new ArrayList();
			else
				this.meList.clear();
			this.meList.add(Pool.get(MonitoredElement.typ, test.getMonitoredElementId()));
			updateTestSetupList();
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
		this.parameters.put(TestSetup.TYPE, ts);
		EvaluationType evaluationType = null;
		AnalysisType analysisType = null;
		//DataSourceInterface dsi = aContext.getDataSourceInterface();
		if (this.useAnalysisBox.isSelected()) {
			evaluationType = (EvaluationType) this.evaluationComboBox.getSelectedObjectResource();
			analysisType = (AnalysisType) this.analysisComboBox.getSelectedObjectResource();

		}
		//if (evaluationType != null)
		this.parameters.put(EvaluationType.TYPE, evaluationType);
		//if (analysisType != null)
		this.parameters.put(AnalysisType.typ, analysisType);
	}

	public void operationPerformed(OperationEvent ae) {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		String commandName = ae.getActionCommand();
		Object obj = ae.getSource();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equalsIgnoreCase(SchedulerModel.COMMAND_DATA_REQUEST)) {
			if (this.paramsRadioButton.isSelected()) {
				TestArgumentSet tas = ((ParametersTestPanel) (this.testPanels.get(this.currentParametersPanelName)))
						.getTestArgumentSet();
				if (tas != null)
					this.dispatcher.notify(new OperationEvent(tas, SchedulerModel.DATA_ID_PARAMETERS,
																SchedulerModel.COMMAND_SEND_DATA));
			} else if (this.patternRadioButton.isSelected()) {
				this.getParameters();
				if (this.parameters != null) {
					TestSetup ts = (TestSetup) this.parameters.get(TestSetup.TYPE);
					AnalysisType analysisType = (AnalysisType) this.parameters.get(AnalysisType.typ);
					EvaluationType evaluationType = (EvaluationType) this.parameters.get(EvaluationType.TYPE);
					this.dispatcher.notify(new OperationEvent((ts == null) ? (Object) "" : (Object) ts, //$NON-NLS-1$
																SchedulerModel.DATA_ID_PARAMETERS_PATTERN,
																SchedulerModel.COMMAND_SEND_DATA));
					//if (analysisType != null)
					this.dispatcher.notify(new OperationEvent((analysisType == null) ? (Object) "" //$NON-NLS-1$
							: (Object) analysisType, SchedulerModel.DATA_ID_PARAMETERS_ANALYSIS,
																SchedulerModel.COMMAND_SEND_DATA));
					this.dispatcher.notify(new OperationEvent((evaluationType == null) ? (Object) "" //$NON-NLS-1$
							: (Object) evaluationType, SchedulerModel.DATA_ID_PARAMETERS_EVALUATION,
																SchedulerModel.COMMAND_SEND_DATA));
				}
			}

		} else if (commandName.equals(SchedulerModel.COMMAND_CHANGE_PARAM_PANEL)) {
			this.currentParametersPanelName = obj.toString();
			this.paramsRadioButton.doClick();
		} else if (commandName.equals(SchedulerModel.COMMAND_ADD_PARAM_PANEL)) {
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
		} else if (commandName.equals(SchedulerModel.COMMAND_CHANGE_ME_TYPE)) {
			String meId = (String) obj;
			if (this.meList == null)
				this.meList = new ArrayList();
			this.meList.clear();
			this.meList.add(Pool.get(MonitoredElement.typ, meId));
			updateTestSetupList();
		}

		aModel.fireModelChanged(""); //$NON-NLS-1$
	}

	private void updateTestSetupList() {
		this.testSetups.removeAll();
		this.testMap.clear();
		if (this.meList != null) {
			Hashtable tsTable = Pool.getHash(TestSetup.TYPE);
			if (tsTable != null) {
				for (Iterator it = tsTable.keySet().iterator(); it.hasNext();) {
					TestSetup ts = (TestSetup) tsTable.get(it.next());
					String[] meIds = ts.getMonitoredElementIds();
					if (meIds.length == 0) {
						//						System.out.println("meIds.length == 0\t" +
						// ts.getId());
						this.testMap.put(ts.getId(), ts);
					} else
						for (Iterator iter = this.meList.iterator(); iter.hasNext();) {
							MonitoredElement me = (MonitoredElement) iter.next();
							String meId = me.getId();
							for (int i = 0; i < meIds.length; i++) {
								if (meIds[i].equals(meId)) {
									//									System.out.println("meId:" + meId + "\t"
									// + ts.getId());
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

		if (this.test != null) {

			TestSetup testsetup = (TestSetup) Pool.get(TestSetup.TYPE, this.test.getTestSetupId());

			if ((this.test.getEvalution() != null) || (this.test.getAnalysis() != null)
					|| (this.test.getAnalysisId().length() > 0)) {
				if (!this.useAnalysisBox.isSelected())
					this.useAnalysisBox.doClick();
			} else {
				if (this.useAnalysisBox.isSelected())
					this.useAnalysisBox.doClick();
			}

			if (testsetup != null) {
				//System.out.println("testsetup:" + testsetup.getId());
				this.testSetups.setSelected(testsetup);
				this.patternRadioButton.doClick();

				//System.out.println("getAnalysisId:" +
				// this.test.getAnalysisId());

				if (this.test.getEvalution() != null) {
					//System.out.println("test.evalution isn't null");
					selectComboBox(this.evaluationComboBox, this.test.getEvalution().getTypeId());
				}
				if (this.test.getAnalysisId().length() > 0) {
					Analysis analysis = this.test.getAnalysis();
					if (analysis == null) {
						analysis = (Analysis) Pool.get(Analysis.TYPE, this.test.getAnalysisId());
						//this.test.setAnalysis(analysis);
						selectComboBox(this.analysisComboBox, analysis.getTypeId());
					}
				}
				if (this.test.getAnalysis() != null) {
					//System.out.println("test.analysis isn't null");
					selectComboBox(this.analysisComboBox, this.test.getAnalysis().getTypeId());
				}

			} else {
				//System.out.println("testsetup is null");
				this.paramsRadioButton.doClick();
			}
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