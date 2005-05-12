
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client_.general.ui_.ColumnSorter;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.client_.general.ui_.ObjList;
import com.syrus.AMFICOM.client_.general.ui_.ObjListModel;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.Plugger;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeController;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.EvaluationTypeController;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupController;
import com.syrus.AMFICOM.measurement.MeasurementSetupWrapper;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.util.Log;

public class TestParametersPanel
implements OperationListener
//, MeasurementSetupEditor,
//		AnalysisTypeEditor, EvaluationTypeEditor, SetEditor 
		{

	ApplicationContext			aContext;
	private SchedulerModel		schedulerModel;
	ObjComboBox					analysisComboBox		= new ObjComboBox(AnalysisTypeController.getInstance(),
																			Collections.EMPTY_LIST,
																			AnalysisTypeController.KEY_NAME);

	ObjComboBox					evaluationComboBox		= new ObjComboBox(EvaluationTypeController.getInstance(),
																			Collections.EMPTY_LIST,
																			EvaluationTypeController.KEY_NAME);
	final JPanel				switchPanel				= new JPanel(new CardLayout());
	List						msList;

	ObjList						testSetups;

	JCheckBox					useAnalysisBox;

	 JTabbedPane tabbedPane;
	private JRadioButton		useAnalysisSetups;
	private JRadioButton		useWOAnalysisSetups;

	private Dispatcher			dispatcher;
	
	ParametersTestPanel parametersTestPanel;


	
	private MeasurementSetup measurementSetup;
	
	private Hashtable panels = new UIDefaults(); 

	public TestParametersPanel(final ApplicationContext aContext) {
		this.aContext = aContext;
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
//		this.schedulerModel.setMeasurementSetupEditor(this);
//		this.schedulerModel.setAnalysisTypeEditor(this);
//		this.schedulerModel.setEvaluationTypeEditor(this);
//		this.schedulerModel.setSetEditor(this);

		if (aContext != null) {
			initModule(aContext.getDispatcher());
		}
		
		this.createGUI();
		this.preparePanels();
	}

	private void preparePanels() {
		ResourceBundle bundle = ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Schedule.UI.parametersPanel");
		Enumeration keys = bundle.getKeys();
		while (keys.hasMoreElements()) {
			String codename = (String) keys.nextElement();
			final String className = (String) bundle.getObject(codename);
			Log.debugMessage("TestParametersPanel.preparePanels | codename " + codename, Log.FINEST);
			Log.debugMessage("TestParametersPanel.preparePanels | className " + className, Log.FINEST);
			this.panels.put(codename, new UIDefaults.LazyValue() {

				public Object createValue(UIDefaults table) {
					return (ParametersTestPanel) Plugger.reflectClass(className,
						new Class[] { ApplicationContext.class}, new Object[] { TestParametersPanel.this.aContext});
				}
			});

		}
	}

	private void createGUI() {
		this.tabbedPane = new JTabbedPane();		
		JPanel patternPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		
		patternPanel.setBorder(BorderFactory.createEtchedBorder());
		this.switchPanel.setBorder(BorderFactory.createEtchedBorder());

		final ActionListener listener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				updateMeasurementSetups();
				setMeasurementSetups(TestParametersPanel.this.msList);
			}
		};

		{
			this.useAnalysisSetups = new JRadioButton(LangModelSchedule.getString("with analysis parameters"));
			this.useAnalysisSetups.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					listener.actionPerformed(e);
					TestParametersPanel.this.useAnalysisBox.setEnabled(true);
				}
			});
			this.useWOAnalysisSetups = new JRadioButton(LangModelSchedule.getString("without analysis parameters"));
			this.useWOAnalysisSetups.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					listener.actionPerformed(e);
					if(TestParametersPanel.this.useAnalysisBox.isSelected()) {
						TestParametersPanel.this.useAnalysisBox.doClick();
					} else {
						TestParametersPanel.this.useAnalysisBox.doClick();
						TestParametersPanel.this.useAnalysisBox.doClick();
					}
					
					TestParametersPanel.this.useAnalysisBox.setEnabled(false);
				}
			});
			
			patternPanel.add(new JLabel(LangModelSchedule.getString("Use setup") + ':'), gbc);
			patternPanel.add(this.useAnalysisSetups, gbc);
			patternPanel.add(this.useWOAnalysisSetups, gbc);
			
			this.useWOAnalysisSetups.setSelected(true);
			this.useAnalysisSetups.setEnabled(false);
			this.useWOAnalysisSetups.setEnabled(false);
			
			this.useAnalysisBox = new JCheckBox(LangModelSchedule.getString("Perform analisys"), false); //$NON-NLS-1$
			patternPanel.add(this.useAnalysisBox, gbc);
			this.useAnalysisBox.setEnabled(false);

			
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add(this.useAnalysisSetups);
			buttonGroup.add(this.useWOAnalysisSetups);
		}

		final JLabel analysisLabel = new JLabel(LangModelSchedule.getString("Analysis")); //$NON-NLS-1$
		patternPanel.add(analysisLabel, gbc);
		patternPanel.add(this.analysisComboBox, gbc);
		final JLabel evaluationLabel = new JLabel(LangModelSchedule.getString("Evaluation Analysis")); //$NON-NLS-1$
		patternPanel.add(evaluationLabel, gbc);
		patternPanel.add(this.evaluationComboBox, gbc);
		patternPanel.add(new JLabel(LangModelSchedule.getString("Patterns")), gbc);
		this.testSetups = new ObjList(MeasurementSetupController.getInstance(), MeasurementSetupController.KEY_NAME);
		this.testSetups.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					final ObjList objList = (ObjList) e.getSource();
					final MeasurementSetup measurementSetup1 = (MeasurementSetup) objList.getSelectedValue();
					final JMenuItem deleteTestMenuItem = new JMenuItem("Show Measurement setup summary info");
					final JPopupMenu popup = new JPopupMenu();
					popup.add(deleteTestMenuItem);
					popup.show(objList, e.getX(), e.getY());

					deleteTestMenuItem.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							MeasurementSetupWrapper wrapper = MeasurementSetupWrapper.getInstance();
							String info = (String) wrapper.getValue(measurementSetup1,
								MeasurementSetupWrapper.SUMMARY_INFO);
							JOptionPane.showConfirmDialog(objList, info, "Measurement setup summary info",
								JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

						}
					});

				}
			}
		});
		
		this.testSetups.setEnabled(false);
		this.testSetups.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				MeasurementSetup measurementSetup1 = (MeasurementSetup) TestParametersPanel.this.testSetups
						.getSelectedValue();
				if (measurementSetup1 != null) {
					TestParametersPanel.this.useAnalysisBox.setEnabled(true);
					try {
						TestParametersPanel.this.analysisComboBox.removeAll();

						LinkedIdsCondition linkedIdsCondition = null;
						{
							Set criteriaSet = measurementSetup1.getCriteriaSet();
							if (criteriaSet != null) {
								SetParameter[] setParameters = criteriaSet.getParameters();
								java.util.Set set = new HashSet(setParameters.length);
								for (int i = 0; i < setParameters.length; i++)
									set.add(setParameters[i].getType().getId());
								if (!set.isEmpty()) {
									linkedIdsCondition = new LinkedIdsCondition(set,
																				ObjectEntities.ANALYSISTYPE_ENTITY_CODE);

									Collection analysisTypes = MeasurementStorableObjectPool
											.getStorableObjectsByCondition(linkedIdsCondition, true);

									for (Iterator it = analysisTypes.iterator(); it.hasNext();) {
										AnalysisType analysisType = (AnalysisType) it.next();
										((ObjListModel) TestParametersPanel.this.analysisComboBox.getModel())
												.addElement(analysisType);
									}
								}
							}
						}

						TestParametersPanel.this.evaluationComboBox.removeAll();
						{
							Set thresholdSet = measurementSetup1.getThresholdSet();
							if (thresholdSet != null) {
								SetParameter[] setParameters = thresholdSet.getParameters();
								java.util.Set set = new HashSet(setParameters.length);
								for (int i = 0; i < setParameters.length; i++)
									set.add(setParameters[i].getType().getId());
								if (!set.isEmpty()) {
									if (linkedIdsCondition == null) {
										linkedIdsCondition = new LinkedIdsCondition(
																					set,
																					ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);
									}

									else {
										linkedIdsCondition.setEntityCode(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);
										linkedIdsCondition.setLinkedIds(set);
									}
									Collection evaluationTypes = MeasurementStorableObjectPool
											.getStorableObjectsByCondition(linkedIdsCondition, true);
									for (Iterator it = evaluationTypes.iterator(); it.hasNext();) {
										EvaluationType evaluationType = (EvaluationType) it.next();
										((ObjListModel) TestParametersPanel.this.evaluationComboBox.getModel())
												.addElement(evaluationType);
									}
								}
							}
						}

						if (TestParametersPanel.this.parametersTestPanel != null) {
							TestParametersPanel.this.parametersTestPanel.setSet(measurementSetup1.getParameterSet());
						}
					} catch (ApplicationException ae) {
						SchedulerModel.showErrorMessage(TestParametersPanel.this.tabbedPane, ae);
					}

				}
			}
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

			}
		});
		// it's for set enabled status for analysisComboBox &
		// evaluationComboBox

		this.analysisComboBox.setEnabled(false);
		this.evaluationComboBox.setEnabled(false);

		
		this.tabbedPane.addTab(LangModelSchedule.getString("Pattern"), null, patternPanel, LangModelSchedule.getString("Use pattern"));
		this.tabbedPane.addTab(LangModelSchedule.getString("Parameters"), null, this.switchPanel, LangModelSchedule.getString("Use parameters"));

		this.tabbedPane.setSelectedIndex(0);
	}

	public AnalysisType getAnalysisType() {
		AnalysisType analysisType = null;
		if (this.tabbedPane.getSelectedIndex() == 0 && this.useAnalysisBox.isSelected()) {
			analysisType = (AnalysisType) this.analysisComboBox.getSelectedItem();
		}
		return analysisType;
	}

	public EvaluationType getEvaluationType() {
		EvaluationType evaluationType = null;
		if (this.tabbedPane.getSelectedIndex() == 0 && this.useAnalysisBox.isSelected()) {
			evaluationType = (EvaluationType) this.evaluationComboBox.getSelectedItem();
		}
		return evaluationType;
	}

	public MeasurementSetup getMeasurementSetup() {
		MeasurementSetup measurementSetup1 = null;
		if (this.tabbedPane.getSelectedIndex() == 0) {
			measurementSetup1 = (MeasurementSetup) this.testSetups.getSelectedValue();
			if (measurementSetup1 == null) {
				JOptionPane
						.showMessageDialog(
							this.tabbedPane,
							LangModelSchedule.getString("Have_not_choosen_measurement_pattern"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.OK_OPTION);
				return null;
			}
		}
		return measurementSetup1;
	}

	public Set getSet() {
		Set set = null;
		if (this.tabbedPane.getSelectedIndex() == 1) {
			set = this.parametersTestPanel != null ? this.parametersTestPanel.getSet() : null;
		}
		return set;
	}

	public void setAnalysisType(AnalysisType analysisType) {
		if (analysisType != null)
			selectComboBox(this.analysisComboBox, analysisType.getId());
	}

	public void setEvaluationType(EvaluationType evaluationType) {
		if (evaluationType != null)
			selectComboBox(this.evaluationComboBox, evaluationType.getId());
	}

	public void setMeasurementSetup(MeasurementSetup measurementSetup) {
		this.measurementSetup = measurementSetup; 
		if (this.msList == null)
			return;
		
		if (!this.msList.contains(measurementSetup)) {
			this.msList.add(measurementSetup);
		}
		this.testSetups.setSelectedValue(measurementSetup, true);
		if (this.useAnalysisSetups.isSelected() && this.testSetups.getSelectedIndex() < 0 && this.parametersTestPanel != null) {
			this.parametersTestPanel.setSet(measurementSetup.getParameterSet());
			this.tabbedPane.setSelectedIndex(1);
		} else {
			this.tabbedPane.setSelectedIndex(0);
		}
	}

	public void setMeasurementSetups(Collection measurementSetups) {
		
		if (this.msList == null)
			this.msList = new LinkedList();
		else {
			// year! really equals links to the same object
			if (this.msList != measurementSetups)
				this.msList.clear();
		}
		// year! really equals links to the same object
		if (this.msList != measurementSetups)
			this.msList.addAll(measurementSetups);

		Collections.sort(this.msList, new ColumnSorter(MeasurementSetupController.getInstance(),
														MeasurementSetupController.KEY_NAME, true));
		this.testSetups.removeAll();
		for (Iterator it = this.msList.iterator(); it.hasNext();) {
			MeasurementSetup measurementSetup1 = (MeasurementSetup) it.next();
			if (!this.useAnalysisSetups.isSelected() || measurementSetup1.getCriteriaSet() != null
					|| measurementSetup1.getThresholdSet() != null || measurementSetup1.getEtalon() != null)
				((ObjListModel) this.testSetups.getModel()).addElement(measurementSetup1);
		}
		
		this.testSetups.setEnabled(true);
		this.useAnalysisSetups.setEnabled(true);
		this.useWOAnalysisSetups.setEnabled(true);

		if (this.measurementSetup != null) {
			this.setMeasurementSetup(this.measurementSetup);
		}
	}

	public void updateMeasurementSetups() {
		if (this.msList == null) {
			this.schedulerModel.refreshMeasurementSetups();
			return;
		}
	}

	public void setSet(Set set) {
		if (set != null && this.parametersTestPanel != null) {
			this.parametersTestPanel.setSet(set);
		}
	}

	public void setAnalysisTypes(Collection analysisTypes) {
		for (Iterator it = analysisTypes.iterator(); it.hasNext();) {
			AnalysisType analysisType = (AnalysisType) it.next();
			((ObjListModel) TestParametersPanel.this.analysisComboBox.getModel()).addElement(analysisType);
		}
	}

	public void setEvaluationTypes(Collection evaluationTypes) {
		for (Iterator it = evaluationTypes.iterator(); it.hasNext();) {
			EvaluationType evaluationType = (EvaluationType) it.next();
			((ObjListModel) TestParametersPanel.this.evaluationComboBox.getModel()).addElement(evaluationType);
		}
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Object obj = ae.getSource();
//		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equals(SchedulerModel.COMMAND_CHANGE_ME_TYPE)) {			
			Identifier meId = (Identifier) obj;
			try {
				MonitoredElement me = (MonitoredElement) ConfigurationStorableObjectPool.getStorableObject(meId, true);
				MeasurementPort port = (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(me
					.getMeasurementPortId(), true);
				this.switchPanel.removeAll();
				
				this.parametersTestPanel = (ParametersTestPanel) this.panels.get(port.getType().getCodename());
				if (this.parametersTestPanel != null) {
					this.parametersTestPanel.setMonitoredElement(me);
					/*/
					this.switchPanel.add(parametersTestPanel);
					/*/
					this.switchPanel.add(this.parametersTestPanel, "");
					//*/
					this.tabbedPane.revalidate();
				}
			}
			catch (ApplicationException e) {
				SchedulerModel.showErrorMessage(this.tabbedPane, e);
			}
		} 
	}
	
	public JComponent getComponent() {
		return this.tabbedPane;
	}

	public void unregisterDispatcher() {
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CHANGE_ME_TYPE);
	}

	private void initModule(final Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, SchedulerModel.COMMAND_CHANGE_ME_TYPE);
//		this.schedulerModel.setMeasurementSetupEditor(this);
//		this.schedulerModel.setAnalysisTypeEditor(this);
//		this.schedulerModel.setEvaluationTypeEditor(this);
//		this.schedulerModel.setSetEditor(this);
		
		OperationListener operationListener = new OperationListener() {
			private boolean skip = false;
			
			public void operationPerformed(OperationEvent e) {
				String actionCommand = e.getActionCommand();
				Object object = e.getSource();
				if (actionCommand.equals(SchedulerModel.COMMAND_SET_ANALYSIS_TYPE)) {
					if (!this.skip) {
						setAnalysisType((AnalysisType) object);
					}					
				} else if (actionCommand.equals(SchedulerModel.COMMAND_SET_EVALUATION_TYPE)) {
					if (!this.skip) {
						setEvaluationType((EvaluationType) object);
					}
				} else if (actionCommand.equals(SchedulerModel.COMMAND_SET_MEASUREMENT_SETUP)) {
					if (!this.skip) {
						setMeasurementSetup((MeasurementSetup) object);
					}
				}  else if (actionCommand.equals(SchedulerModel.COMMAND_SET_MEASUREMENT_SETUPS)) {
					if (!this.skip) {
						setMeasurementSetups((Collection) object);
					}
				}   else if (actionCommand.equals(SchedulerModel.COMMAND_SET_SET)) {
					if (!this.skip) {
						setSet((Set) object);
					}
				} if (actionCommand.equals(SchedulerModel.COMMAND_GET_ANALYSIS_TYPE)) {
					AnalysisType analysisType = getAnalysisType();
					if (analysisType != null) {
						this.skip = true;
						dispatcher.notify(new OperationEvent(analysisType, 0, SchedulerModel.COMMAND_SET_ANALYSIS_TYPE));
						this.skip = false;
					}
				} else if (actionCommand.equals(SchedulerModel.COMMAND_GET_EVALUATION_TYPE)) {
					EvaluationType evaluationType = getEvaluationType();
					if (evaluationType != null) {
						this.skip = true;
						dispatcher.notify(new OperationEvent(evaluationType, 0, SchedulerModel.COMMAND_SET_EVALUATION_TYPE));
						this.skip = false;
					}
				} else if (actionCommand.equals(SchedulerModel.COMMAND_GET_MEASUREMENT_SETUP)) {
					MeasurementSetup measurementSetup1 = getMeasurementSetup();
					if (measurementSetup1 != null) {
						this.skip = true;
						dispatcher.notify(new OperationEvent(measurementSetup1, 0, SchedulerModel.COMMAND_SET_MEASUREMENT_SETUP));
						this.skip = false;
					}
				}  else if (actionCommand.equals(SchedulerModel.COMMAND_GET_SET)) {
					Set set = getSet();
					if (set != null) {
						this.skip = true;
						dispatcher.notify(new OperationEvent(set, 0, SchedulerModel.COMMAND_SET_SET));
						this.skip = false;
					}
				} 
				
			}
		};
		this.dispatcher.register(operationListener, SchedulerModel.COMMAND_SET_ANALYSIS_TYPE);
		this.dispatcher.register(operationListener, SchedulerModel.COMMAND_SET_EVALUATION_TYPE);
		this.dispatcher.register(operationListener, SchedulerModel.COMMAND_SET_MEASUREMENT_SETUP);
		this.dispatcher.register(operationListener, SchedulerModel.COMMAND_SET_MEASUREMENT_SETUPS);
		this.dispatcher.register(operationListener, SchedulerModel.COMMAND_SET_SET);

		this.dispatcher.register(operationListener, SchedulerModel.COMMAND_GET_ANALYSIS_TYPE);
		this.dispatcher.register(operationListener, SchedulerModel.COMMAND_GET_EVALUATION_TYPE);
		this.dispatcher.register(operationListener, SchedulerModel.COMMAND_GET_MEASUREMENT_SETUP);
		this.dispatcher.register(operationListener, SchedulerModel.COMMAND_GET_SET);

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
