
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.client_.general.ui_.ColumnSorter;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.client_.general.ui_.ObjList;
import com.syrus.AMFICOM.client_.general.ui_.ObjListModel;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeController;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.EvaluationTypeController;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupController;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.Test;

public class TestParametersPanel extends JPanel implements OperationListener {

	public static final String	PARAMETER_PARAMETER		= "Parameter";											//$NON-NLS-1$
	public static final String	PARAMETERS_PANEL_PREFIX	= "PARAMETERS_PANEL";									//$NON-NLS-1$

	private static final String	PATTERN_PANEL_NAME		= "PATTERN_PANEL";										//$NON-NLS-1$

	ApplicationContext			aContext;
	private SchedulerModel		schedulerModel;
	ObjComboBox					analysisComboBox		= new ObjComboBox(AnalysisTypeController.getInstance(),
																			Collections.EMPTY_LIST,
																			AnalysisTypeController.KEY_NAME);

	String						currentParametersPanelName;
	ObjComboBox					evaluationComboBox		= new ObjComboBox(EvaluationTypeController.getInstance(),
																			Collections.EMPTY_LIST,
																			EvaluationTypeController.KEY_NAME);
	JRadioButton				paramsRadioButton;

	final JPanel				switchPanel				= new JPanel(new CardLayout());
	HashMap						testMap;

	HashMap						testPanels				= new HashMap();

	ObjList						testSetups;

	JCheckBox					useAnalysisBox;

	private Dispatcher			dispatcher;
	private java.util.List		meList;

	private HashMap				parameters;

	private JRadioButton		patternRadioButton;

	private Test				test;
	private JCheckBox			usePatternsWithAnalysisBox;

	public TestParametersPanel(final ApplicationContext aContext) {
		this.aContext = aContext;
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();

		if (aContext != null) {
			initModule(aContext.getDispatcher());
		}
		setLayout(new BorderLayout());

		this.patternRadioButton = UIStorage.createRadioButton(
			LangModelSchedule.getString("UsePattern"), new AbstractAction() { //$NON-NLS-1$

				public void actionPerformed(ActionEvent e) {
					CardLayout cl = (CardLayout) (TestParametersPanel.this.switchPanel.getLayout());
					cl.show(TestParametersPanel.this.switchPanel, PATTERN_PANEL_NAME);
					revalidate();
				}
			});
		this.paramsRadioButton = UIStorage.createRadioButton(
			LangModelSchedule.getString("UseParameters"), new AbstractAction() { //$NON-NLS-1$

				public void actionPerformed(ActionEvent e) {
					CardLayout cl = (CardLayout) (TestParametersPanel.this.switchPanel.getLayout());
					cl.show(TestParametersPanel.this.switchPanel, TestParametersPanel.this.currentParametersPanelName);
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

		this.usePatternsWithAnalysisBox = new JCheckBox(LangModelSchedule.getString("UsePatternsWithAnalisys"), true); //$NON-NLS-1$
		this.usePatternsWithAnalysisBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				updateTestSetupList();
			}
		});
		patternPanel.add(this.usePatternsWithAnalysisBox, gbc);

		this.useAnalysisBox = new JCheckBox(LangModelSchedule.getString("PerformAnalisys"), true); //$NON-NLS-1$
		patternPanel.add(this.useAnalysisBox, gbc);
		final JLabel analysisLabel = new JLabel(LangModelSchedule.getString("Analysis")); //$NON-NLS-1$
		patternPanel.add(analysisLabel, gbc);
		/**
		 * FIXME return back !!! fix gui
		 */
		patternPanel.add(this.analysisComboBox, gbc);
		final JLabel evaluationLabel = new JLabel(LangModelSchedule.getString("EvaluationAnalysis")); //$NON-NLS-1$
		patternPanel.add(evaluationLabel, gbc);
		/**
		 * FIXME return back !!! fix gui
		 */
		patternPanel.add(this.evaluationComboBox, gbc);
		this.testMap = new HashMap();
		patternPanel.add(new JLabel(LangModelSchedule.getString("Patterns")), gbc);
		this.testSetups = new ObjList(MeasurementSetupController.getInstance(), MeasurementSetupController.KEY_NAME);
		this.testSetups.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				// if (e.getStateChange() == ItemEvent.SELECTED)
				// {
				MeasurementSetup measurementSetup = (MeasurementSetup) TestParametersPanel.this.testSetups
						.getSelectedValue();
				if (measurementSetup != null) {
					TestParametersPanel.this.useAnalysisBox.setEnabled(true);
					// DataSet dsAnalysis = new DataSet();
					// dsAnalysis.add((ObjectResource)
					// Pool.get(AnalysisType.typ,
					// ts.getAnalysisTypeId()));

					// DataSet dsEvaluation = new DataSet();
					// dsEvaluation.add((ObjectResource)
					// Pool.get(EvaluationType.typ,
					// ts.getEvaluationTypeId()));
					// TestParametersPanel.this.analysisComboBox.setContents(dsAnalysis.elements(),
					// true);
					try {
						TestParametersPanel.this.analysisComboBox.removeAll();

						LinkedIdsCondition linkedIdsCondition = new LinkedIdsCondition(
																						(List) null,
																						ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
						{
							Set criteriaSet = measurementSetup.getCriteriaSet();
							if (criteriaSet != null) {
								SetParameter[] setParameters = criteriaSet.getParameters();
								List list = new ArrayList(setParameters.length);
								for (int i = 0; i < setParameters.length; i++)
									list.add(setParameters[i].getId());
								linkedIdsCondition.setLinkedIds(list);

								Collection analysisTypes = MeasurementStorableObjectPool.getStorableObjectsByCondition(
									linkedIdsCondition, true);

								for (Iterator it = analysisTypes.iterator(); it.hasNext();) {
									AnalysisType analysisType = (AnalysisType) it.next();
									((ObjListModel) TestParametersPanel.this.analysisComboBox.getModel())
											.addElement(analysisType);
								}
							}
						}

						TestParametersPanel.this.evaluationComboBox.removeAll();
						linkedIdsCondition.setEntityCode(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);
						{
							Set thresholdSet = measurementSetup.getThresholdSet();
							if (thresholdSet != null) {
								SetParameter[] setParameters = thresholdSet.getParameters();
								List list = new ArrayList(setParameters.length);
								for (int i = 0; i < setParameters.length; i++)
									list.add(setParameters[i].getId());
								linkedIdsCondition.setLinkedIds(list);
								Collection evaluationTypes = MeasurementStorableObjectPool
										.getStorableObjectsByCondition(linkedIdsCondition, true);
								for (Iterator it = evaluationTypes.iterator(); it.hasNext();) {
									EvaluationType evaluationType = (EvaluationType) it.next();
									((ObjListModel) TestParametersPanel.this.evaluationComboBox.getModel())
											.addElement(evaluationType);
								}
							}
						}

						for (Iterator it = TestParametersPanel.this.testPanels.keySet().iterator(); it.hasNext();) {
							String key = (String) it.next();
							ParametersTestPanel panel = (ParametersTestPanel) (TestParametersPanel.this.testPanels
									.get(key));
							panel.setSet(measurementSetup.getParameterSet());
						}
					} catch (ApplicationException ae) {
						SchedulerModel.showErrorMessage(TestParametersPanel.this, ae);
					}

				} else {
					TestParametersPanel.this.paramsRadioButton.doClick();
				}
			}
			// }
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
				// if (enable) {
				// TestSetup ts = (TestSetup) testSetups
				// .getSelectedObjectResource();
				//					
				//
				// }

			}
		});
		// it's for set enabled status for analysisComboBox &
		// evaluationComboBox
		this.useAnalysisBox.doClick();
		this.useAnalysisBox.setEnabled(this.testSetups.getModel().getSize() > 0);

		// switchPanel.add(parametersPanel, PARAMETERS_PANEL_NAME);
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
	public void addParameterPanel(	String command,
									ParametersTestPanel panel) {
		this.testPanels.put(command, panel);
		this.switchPanel.add(panel, command);
		this.paramsRadioButton.setEnabled(true);
	}

	public boolean isParameterPanelExists(String command) {
		return this.testPanels.get(command) != null;
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Object obj = ae.getSource();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equalsIgnoreCase(SchedulerModel.COMMAND_DATA_REQUEST)) {
			if (this.paramsRadioButton.isSelected()) {
				Set set = ((ParametersTestPanel) (this.testPanels.get(this.currentParametersPanelName))).getSet();
				this.schedulerModel.setMeasurementSetupIds(null);
				this.schedulerModel.setAnalysisType(null);
				this.schedulerModel.setEvaluationType(null);
				this.schedulerModel.setSet(set);
			} else if (this.patternRadioButton.isSelected()) {
				this.getParameters();
				if (this.parameters != null) {
					MeasurementSetup measurementSetup = (MeasurementSetup) this.parameters
							.get(ObjectEntities.MS_ENTITY);
					AnalysisType analysisType = (AnalysisType) this.parameters.get(ObjectEntities.ANALYSISTYPE_ENTITY);
					EvaluationType evaluationType = (EvaluationType) this.parameters
							.get(ObjectEntities.EVALUATIONTYPE_ENTITY);
					this.schedulerModel.setMeasurementSetupIds(Collections.singletonList(measurementSetup.getId()));
					this.schedulerModel.setAnalysisType(analysisType);
					this.schedulerModel.setEvaluationType(evaluationType);
					this.schedulerModel.setSet(null);
				}
			}

		} else if (commandName.equals(SchedulerModel.COMMAND_CHANGE_PARAM_PANEL)) {
			this.currentParametersPanelName = obj.toString();
			this.paramsRadioButton.doClick();
		} else if (commandName.equals(SchedulerModel.COMMAND_ADD_PARAM_PANEL)) {
			ParametersTestPanel panel = (ParametersTestPanel) obj;
			String name = panel.getPanelName();
			// System.out.println("name : "+name);
			if (!isParameterPanelExists(name)) {
				// System.out.println("name isn't at map");
				addParameterPanel(name, panel);
			}
			this.currentParametersPanelName = name;
			this.paramsRadioButton.doClick();
		} else if (commandName.equals(SchedulerModel.COMMAND_REFRESH_TEST)
				|| commandName.equals(SchedulerModel.COMMAND_REFRESH_TESTS)) {
			this.setTest(this.schedulerModel.getSelectedTest());
		} else if (commandName.equals(SchedulerModel.COMMAND_CLEAN)) {
			this.testMap.clear();
			this.patternRadioButton.doClick();
			this.testPanels.clear();
			if (this.meList != null)
				this.meList.clear();
			updateTestSetupList();
		} else if (commandName.equals(SchedulerModel.COMMAND_AVAILABLE_ME)) {
			this.meList = (java.util.List) obj;
			updateTestSetupList();
		} else if (commandName.equals(SchedulerModel.COMMAND_CHANGE_ME_TYPE)) {
			try {
				Identifier meId = (Identifier) obj;
				if (this.meList == null)
					this.meList = new ArrayList();
				this.meList.clear();
				this.meList.add(ConfigurationStorableObjectPool.getStorableObject(meId, true));
				updateTestSetupList();
			} catch (ApplicationException e) {
				SchedulerModel.showErrorMessage(this, e);
			}
		}
	}

	public void setTest(Test test) {
		if ((this.test == null) || (!this.test.getId().equals(test.getId()))) {
			this.test = test;

			if (this.meList == null)
				this.meList = new LinkedList();
			else
				this.meList.clear();
			MonitoredElement monitoredElement = test.getMonitoredElement();
			if (!this.meList.contains(monitoredElement))
				this.meList.add(monitoredElement);
			updateTestSetupList();
		}

	}

	public void unregisterDispatcher() {
		for (Iterator it = this.testPanels.keySet().iterator(); it.hasNext();) {
			Object key = it.next();
			ParametersTestPanel panel = (ParametersTestPanel) this.testPanels.get(key);
			panel.unregisterDispatcher();
		}
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_DATA_REQUEST);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CHANGE_PARAM_PANEL);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_ADD_PARAM_PANEL);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CHANGE_ME_TYPE);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CLEAN);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_AVAILABLE_ME);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_REFRESH_TEST);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_REFRESH_TESTS);
	}

	void updateTestSetupList() {
		try {

			this.testSetups.removeAll();
			this.testMap.clear();
			if (this.meList != null) {
				LinkedIdsCondition linkedIdsCondition;
				{
					List meIdList = new ArrayList(this.meList.size());
					for (Iterator it = this.meList.iterator(); it.hasNext();) {
						MonitoredElement me = (MonitoredElement) it.next();
						meIdList.add(me.getId());

					}
					linkedIdsCondition = new LinkedIdsCondition(meIdList, ObjectEntities.MS_ENTITY_CODE);
				}
				Collection msList = MeasurementStorableObjectPool.getStorableObjectsByCondition(linkedIdsCondition,
					true);
				for (Iterator it = msList.iterator(); it.hasNext();) {
					MeasurementSetup ts = (MeasurementSetup) it.next();
					Collection meIds = ts.getMonitoredElementIds();
					if (meIds.isEmpty()) {
						// System.out.println("meIds.length
						// == 0\t" +
						// ts.getId());
						this.testMap.put(ts.getId(), ts);
					} else
						for (Iterator iter = this.meList.iterator(); iter.hasNext();) {
							MonitoredElement me = (MonitoredElement) iter.next();
							Identifier meId = me.getId();
							for (Iterator meIt = meIds.iterator(); meIt.hasNext();) {
								Identifier meId2 = (Identifier) meIt.next();
								if (meId2.equals(meId)) {
									// System.out.println("meId:"
									// +
									// meId
									// +
									// "\t"
									// +
									// ts.getId());
									this.testMap.put(ts.getId(), ts);
								}
							}
						}

				}

			}

			List msList = new ArrayList(this.testMap.values());
			Collections.sort(msList, new ColumnSorter(MeasurementSetupController.getInstance(),
														MeasurementSetupController.KEY_NAME, true));
			for (Iterator it = msList.iterator(); it.hasNext();) {
				MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
				if (!this.usePatternsWithAnalysisBox.isSelected() || measurementSetup.getCriteriaSet() != null
						|| measurementSetup.getThresholdSet() != null || measurementSetup.getEtalon() != null)
					((ObjListModel) this.testSetups.getModel()).addElement(measurementSetup);
			}

			if (this.test != null) {
				/**
				 * FIXME WARNING!!! test when more than one element
				 */
				Identifier msId = (Identifier) this.test.getMeasurementSetupIds().iterator().next();
				MeasurementSetup measurementSetup = (MeasurementSetup) MeasurementStorableObjectPool.getStorableObject(
					msId, true);
				if ((this.test.getEvaluationType() != null) || (this.test.getAnalysisType() != null)) {
					if (!this.useAnalysisBox.isSelected())
						this.useAnalysisBox.doClick();
				} else {
					if (this.useAnalysisBox.isSelected())
						this.useAnalysisBox.doClick();
				}

				if (measurementSetup != null) {
					// System.out.println("testsetup:" +
					// testsetup.getId());
					this.testSetups.setSelectedValue(measurementSetup, true);
					this.patternRadioButton.doClick();

					// System.out.println("getAnalysisId:" +
					// this.test.getAnalysisId());

					if (this.test.getEvaluationType() != null) {
						// System.out.println("test.evalution
						// isn't null");
						selectComboBox(this.evaluationComboBox, this.test.getEvaluationType().getId());
					}
					if (this.test.getAnalysisType() != null) {
						selectComboBox(this.analysisComboBox, this.test.getAnalysisType().getId());
					}

				} else {
					// System.out.println("testsetup is
					// null");
					this.paramsRadioButton.doClick();
				}
			}
		} catch (ApplicationException ae) {
			SchedulerModel.showErrorMessage(this, ae);
		}
		// testSetups.setSelected(selectedTs);
	}

	private void getParameters() {
		if (this.parameters == null)
			this.parameters = new HashMap();
		MeasurementSetup ts = (MeasurementSetup) this.testSetups.getSelectedValue();
		if (ts == null) {
			JOptionPane.showMessageDialog(this,
				LangModelSchedule.getString("Do_not_choose_measurement_pattern"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
				JOptionPane.OK_OPTION);
			this.parameters = null;
			return;
		}
		this.parameters.put(ObjectEntities.MS_ENTITY, ts);
		EvaluationType evaluationType = null;
		AnalysisType analysisType = null;
		// DataSourceInterface dsi = aContext.getDataSourceInterface();
		if (this.useAnalysisBox.isSelected()) {
			evaluationType = (EvaluationType) this.evaluationComboBox.getSelectedItem();
			analysisType = (AnalysisType) this.analysisComboBox.getSelectedItem();

		}
		// if (evaluationType != null)
		this.parameters.put(ObjectEntities.EVALUATIONTYPE_ENTITY, evaluationType);
		// if (analysisType != null)
		this.parameters.put(ObjectEntities.ANALYSISTYPE_ENTITY, analysisType);
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, SchedulerModel.COMMAND_DATA_REQUEST);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CHANGE_PARAM_PANEL);
		this.dispatcher.register(this, SchedulerModel.COMMAND_ADD_PARAM_PANEL);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CHANGE_ME_TYPE);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CLEAN);
		this.dispatcher.register(this, SchedulerModel.COMMAND_AVAILABLE_ME);
		this.dispatcher.register(this, SchedulerModel.COMMAND_REFRESH_TEST);
		this.dispatcher.register(this, SchedulerModel.COMMAND_REFRESH_TESTS);
	}

	private void selectComboBox(ObjComboBox cb,
								Identifier value) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			Object obj = cb.getItemAt(i);
			Identifier id = null;
			// System.out.println("obj:" +
			// obj.getClass().getName());
			if (obj instanceof AnalysisType) {
				id = ((AnalysisType) obj).getId();
			} else if (obj instanceof EvaluationType) {
				id = ((EvaluationType) obj).getId();
			}
			if (id != null) {
				// System.out.println("id:" + id);
				if (id.equals(value)) {
					cb.setSelectedIndex(i);
					// System.out.println("selected " + i);
					break;
				}
			}
		}
	}

}