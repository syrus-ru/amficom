
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;

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

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.UI.WrapperedList;
import com.syrus.AMFICOM.client.UI.WrapperedListModel;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Plugger;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeWrapper;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.EvaluationTypeWrapper;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupWrapper;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.util.Log;

public class TestParametersPanel implements PropertyChangeListener {

	ApplicationContext		aContext;
	SchedulerModel			schedulerModel;

	WrapperedComboBox		analysisComboBox;

	WrapperedComboBox		evaluationComboBox;

	JPanel					switchPanel;
	List<MeasurementSetup>	msList;
	List<MeasurementSetup>	msListAnalysisOnly;

	WrapperedList			testSetups;

	JCheckBox				useAnalysisBox;

	JTabbedPane				tabbedPane;
	private JRadioButton	useAnalysisSetups;
	private JRadioButton	useWOAnalysisSetups;

	private Dispatcher		dispatcher;

	ParametersTestPanel		parametersTestPanel;

//	MeasurementSetup		measurementSetup;
	Identifier		measurementSetupId;

	private final Hashtable	panels	= new UIDefaults();
	PropertyChangeEvent		propertyChangeEvent;

	public TestParametersPanel(final ApplicationContext aContext) {
		this.aContext = aContext;
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();

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
			Log.debugMessage("TestParametersPanel.preparePanels | codename " + codename, Level.FINEST);
			Log.debugMessage("TestParametersPanel.preparePanels | className " + className, Level.FINEST);
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

		this.analysisComboBox = new WrapperedComboBox(AnalysisTypeWrapper.getInstance(),
														StorableObjectWrapper.COLUMN_DESCRIPTION,
														StorableObjectWrapper.COLUMN_ID);
		this.evaluationComboBox = new WrapperedComboBox(EvaluationTypeWrapper.getInstance(),
														StorableObjectWrapper.COLUMN_DESCRIPTION,
														StorableObjectWrapper.COLUMN_ID);
		
//		this.analysisComboBox.setEditable(true);
//		this.evaluationComboBox.setEditable(true);
		
		
		this.switchPanel = new JPanel(new CardLayout());

		JPanel patternPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;

		patternPanel.setBorder(BorderFactory.createEtchedBorder());
		this.switchPanel.setBorder(BorderFactory.createEtchedBorder());

		{
			this.useAnalysisSetups = new JRadioButton(LangModelSchedule.getString("with analysis parameters"));
			this.useAnalysisSetups.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					WrapperedListModel wrapperedListModel = (WrapperedListModel) TestParametersPanel.this.testSetups.getModel();
					Object selectedValue = TestParametersPanel.this.testSetups.getSelectedValue();
					int selectedIndex = TestParametersPanel.this.testSetups.getSelectedIndex();
					TestParametersPanel.this.testSetups.removeSelectionInterval(selectedIndex, selectedIndex);					
					wrapperedListModel.setElements(TestParametersPanel.this.msListAnalysisOnly);
					if (selectedValue != null) {
						TestParametersPanel.this.testSetups.setSelectedValue(selectedValue, true);
					}
					
					TestParametersPanel.this.useAnalysisBox.setEnabled(true);
				}
			});
			this.useWOAnalysisSetups = new JRadioButton(LangModelSchedule.getString("without analysis parameters"));
			this.useWOAnalysisSetups.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					WrapperedListModel wrapperedListModel = (WrapperedListModel) TestParametersPanel.this.testSetups.getModel();
					Object selectedValue = TestParametersPanel.this.testSetups.getSelectedValue();
					int selectedIndex = TestParametersPanel.this.testSetups.getSelectedIndex();
					TestParametersPanel.this.testSetups.removeSelectionInterval(selectedIndex, selectedIndex);					
					wrapperedListModel.setElements(TestParametersPanel.this.msList);
					if (selectedValue != null) {
						TestParametersPanel.this.testSetups.setSelectedValue(selectedValue, true);
					}

					if (TestParametersPanel.this.useAnalysisBox.isSelected()) {
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

		this.analysisComboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				WrapperedComboBox comboBox = (WrapperedComboBox) e.getSource();
				AnalysisType analysisType = (AnalysisType) comboBox.getSelectedItem();
				java.util.Set selectedTestIds = TestParametersPanel.this.schedulerModel.getSelectedTestIds();
				if (selectedTestIds != null && !selectedTestIds.isEmpty()
						&& TestParametersPanel.this.propertyChangeEvent == null) {
					try {
						java.util.Set storableObjects = StorableObjectPool.getStorableObjects(selectedTestIds, true);
						for (Iterator iterator = storableObjects.iterator(); iterator.hasNext();) {
							Test test = (Test) iterator.next();
							if (test.isChanged()) {
								test.setAnalysisTypeId(analysisType != null ? analysisType.getId() : null);
							}
						}
					} catch (ApplicationException e1) {
						AbstractMainFrame.showErrorMessage(TestParametersPanel.this.parametersTestPanel, e1);
					}
				}
			}
		});

		this.evaluationComboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				WrapperedComboBox comboBox = (WrapperedComboBox) e.getSource();
				EvaluationType evaluationType = (EvaluationType) comboBox.getSelectedItem();
				java.util.Set selectedTestIds = TestParametersPanel.this.schedulerModel.getSelectedTestIds();
				if (selectedTestIds != null && !selectedTestIds.isEmpty()
						&& TestParametersPanel.this.propertyChangeEvent == null) {
					try {
						java.util.Set storableObjects = StorableObjectPool.getStorableObjects(selectedTestIds, true);
						for (Iterator iterator = storableObjects.iterator(); iterator.hasNext();) {
							Test test = (Test) iterator.next();
							if (test.isChanged()) {
								test.setEvaluationTypeId(evaluationType != null ? evaluationType.getId() : null);
							}
						}
					} catch (ApplicationException e1) {
						AbstractMainFrame.showErrorMessage(TestParametersPanel.this.parametersTestPanel, e1);
					}
				}
			}
		});

		this.testSetups = new WrapperedList(MeasurementSetupWrapper.getInstance(),
											StorableObjectWrapper.COLUMN_DESCRIPTION, StorableObjectWrapper.COLUMN_ID);
		this.testSetups.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					final WrapperedList objList = (WrapperedList) e.getSource();
					final MeasurementSetup measurementSetup1 = (MeasurementSetup) objList.getSelectedValue();
					if (measurementSetup1 == null) {
						return;
					}
					final JPopupMenu popup = new JPopupMenu();
					
					final JMenuItem msSummaryInfo = new JMenuItem(LangModelSchedule.getString("Measurement setup summary info"));					
					msSummaryInfo.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e1) {
							MeasurementSetupWrapper wrapper = MeasurementSetupWrapper.getInstance();
							String info = (String) wrapper.getValue(measurementSetup1,
								MeasurementSetupWrapper.SUMMARY_INFO);
							JOptionPane.showConfirmDialog(objList, info, LangModelSchedule.getString("Measurement setup summary info"),
								JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

						}
					});
					
					popup.add(msSummaryInfo);

					final JMenuItem msRename = new JMenuItem(LangModelSchedule.getString("Rename Measurement setup"));					
					msRename.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e1) {
							MeasurementSetupWrapper wrapper = MeasurementSetupWrapper.getInstance();
							String info = (String) wrapper.getValue(measurementSetup1,
								MeasurementSetupWrapper.SUMMARY_INFO);
							Object object = JOptionPane.showInputDialog(objList, 
								LangModelSchedule.getString("Rename Measurement setup") + '\n'
								+ measurementSetup1.getDescription() + "\n\n"
								+ LangModelSchedule.getString("Measurement setup summary info") + '\n'
								+ info + "\n\n" + LangModelSchedule.getString("New Measurement setup name") + ':',
								LangModelSchedule.getString("Rename Measurement setup"),
								JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    null,
			                    measurementSetup1.getDescription());
							
							if (object != null) {
								measurementSetup1.setDescription((String) object);
								WrapperedListModel wrapperedListModel = (WrapperedListModel) TestParametersPanel.this.testSetups.getModel();
								wrapperedListModel.sort();
							}

						}
					});
					popup.add(msRename);

					popup.show(objList, e.getX(), e.getY());
					
				}
			}
		});

		this.testSetups.setEnabled(false);
		this.testSetups.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				final MeasurementSetup measurementSetup1 = 
					(MeasurementSetup) TestParametersPanel.this.testSetups.getSelectedValue();
				if (measurementSetup1 != null) {

					Set<Identifier>  selectedTestIds = TestParametersPanel.this.schedulerModel.getSelectedTestIds();
					if (selectedTestIds != null && !selectedTestIds.isEmpty()
							&& TestParametersPanel.this.propertyChangeEvent == null) {
						try {
							Set<Identifier> measurementSetupIdSet = Collections.singleton(measurementSetup1.getId());
							Set<StorableObject> storableObjects = StorableObjectPool
									.getStorableObjects(selectedTestIds, true);
							for (Iterator iterator = storableObjects.iterator(); iterator.hasNext();) {
								Test test = (Test) iterator.next();
								if (test.isChanged()) {
									Log.debugMessage(
										"TestParametersPanel$ListSelectionListener.valueChanged | set to test "
												+ test.getId() + " > " + measurementSetup1.getId(), Level.FINEST);
									test.setMeasurementSetupIds(measurementSetupIdSet);
								}
							}
						} catch (ApplicationException e1) {
							AbstractMainFrame.showErrorMessage(TestParametersPanel.this.parametersTestPanel, e1);
						}
					}

					TestParametersPanel.this.useAnalysisBox.setEnabled(true);
					if (TestParametersPanel.this.parametersTestPanel != null) {
						new Thread() {
								public void run() {
								TestParametersPanel.this.parametersTestPanel
										.setSet(measurementSetup1.getParameterSet());
							}
						}.start();
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
				if (TestParametersPanel.this.propertyChangeEvent == null) {
					if (!enable) {
						TestParametersPanel.this.selectComboBox(TestParametersPanel.this.analysisComboBox, null, false);
						TestParametersPanel.this.selectComboBox(TestParametersPanel.this.evaluationComboBox, null,
							false);
					}
				}
				TestParametersPanel.this.analysisComboBox.setEnabled(enable);
				evaluationLabel.setEnabled(enable);
				TestParametersPanel.this.evaluationComboBox.setEnabled(enable);
			}
		});
		// it's for set enabled status for analysisComboBox &
		// evaluationComboBox

		this.analysisComboBox.setEnabled(false);
		this.evaluationComboBox.setEnabled(false);

		this.tabbedPane.addTab(LangModelSchedule.getString("Pattern"), null, patternPanel, LangModelSchedule
				.getString("Use pattern"));
		this.tabbedPane.addTab(LangModelSchedule.getString("Parameters"), null, this.switchPanel, LangModelSchedule
				.getString("Use parameters"));

		this.tabbedPane.setSelectedIndex(0);
		
		if (this.parametersTestPanel == null) {
			this.tabbedPane.setEnabledAt(1, false);
		}
	}

	public Identifier getAnalysisTypeId() {
		Identifier analysisTypeId = null;
		if (this.tabbedPane.getSelectedIndex() == 0 && this.useAnalysisBox.isSelected()) {
			Identifiable identifiable = (Identifiable) this.analysisComboBox.getSelectedItem();
			analysisTypeId = identifiable != null ? identifiable.getId() : Identifier.VOID_IDENTIFIER;
		}
		return analysisTypeId;
	}

	public Identifier getEvaluationTypeId() {
		Identifier evaluationTypeId = null;
		if (this.tabbedPane.getSelectedIndex() == 0 && this.useAnalysisBox.isSelected()) {
			Identifiable identifiable = (Identifiable) this.evaluationComboBox.getSelectedItem();
			evaluationTypeId = identifiable != null ? identifiable.getId() : Identifier.VOID_IDENTIFIER;
		}
		return evaluationTypeId;
	}

	public MeasurementSetup getMeasurementSetup() {
		MeasurementSetup measurementSetup1 = null;
		if (this.tabbedPane.getSelectedIndex() == 0) {
			measurementSetup1 = (MeasurementSetup) this.testSetups.getSelectedValue();
			if (measurementSetup1 == null) {
				JOptionPane.showMessageDialog(this.tabbedPane, LangModelSchedule
						.getString("Have_not_choosen_measurement_pattern"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.OK_OPTION);
				return null;
			}
		}
		return measurementSetup1;
	}

	public ParameterSet getSet() {
		ParameterSet set = null;
		if (this.tabbedPane.getSelectedIndex() == 1) {
			set = this.parametersTestPanel != null ? this.parametersTestPanel.getSet() : null;
		}
		return set;
	}

	public void setMeasurementSetup(final MeasurementSetup measurementSetup) {
//		Log.debugMessage("TestParametersPanel.setMeasurementSetup | "
//				+ (measurementSetup != null ? measurementSetup.getId() : null), Level.FINEST);		

		this.testSetups.setSelectedValue(null, false);
			
//		System.out.println("TestParametersPanel.setMeasurementSetup() | " + this.testSetups.getSelectedIndex());
		if (measurementSetup != null) {
			this.testSetups.setSelectedValue(measurementSetup, true);
		}
//		System.out.println("TestParametersPanel.setMeasurementSetup() | " + this.testSetups.getSelectedIndex());
		
		if (this.measurementSetupId != null && measurementSetup != null
				&& this.measurementSetupId.equals(measurementSetup.getId()) || this.msList == null) {
//			 Log.debugMessage("TestParametersPanel.setMeasurementSetup | return ", Level.FINEST);
			 if (this.msList == null) {
				 this.measurementSetupId = measurementSetup.getId();
			 }
			return;
		}

		this.measurementSetupId = measurementSetup.getId();
		
//		Log.debugMessage("TestParametersPanel.setMeasurementSetup | measurementSetupId " + measurementSetupId, Log.FINEST);

		{
			boolean exist = false;
			for(MeasurementSetup ms : this.msList) {
				if (ms.getId().equals(this.measurementSetupId)) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				this.msList.add(measurementSetup);
				// if total list doesn't contains ms, and ms with analysis - add to analysis ms list
				if(measurementSetup.getCriteriaSet() != null &&
						measurementSetup.getEtalon() != null &&
						measurementSetup.getThresholdSet() != null) {
					this.msListAnalysisOnly.add(measurementSetup);
				}
			}
		}
		
		if (this.useAnalysisSetups.isSelected() && this.testSetups.getSelectedIndex() < 0
				&& this.parametersTestPanel != null) {
			this.useWOAnalysisSetups.doClick();
			this.tabbedPane.setSelectedIndex(1);
		} else {
			this.tabbedPane.setSelectedIndex(0);
		}
	}

	public void setMeasurementSetups(Collection<MeasurementSetup> measurementSetups) {
		
//		Log.debugMessage("TestParametersPanel.setMeasurementSetups | ", Level.FINEST);
		
		if (this.msList == null) {
			this.msList = new LinkedList<MeasurementSetup>();
		} else {
			this.msList.clear();
		}
		
		if (this.msListAnalysisOnly == null) {
			this.msListAnalysisOnly = new LinkedList<MeasurementSetup>();
		} else {
			this.msListAnalysisOnly.clear();
		}

		this.msList.addAll(measurementSetups);
		// year! really equals links to the same object
		for(MeasurementSetup measurementSetup : measurementSetups) {
			if(measurementSetup.getCriteriaSet() != null ||
					measurementSetup.getEtalon() != null ||
					measurementSetup.getThresholdSet() != null) {
				this.msListAnalysisOnly.add(measurementSetup);
			}
		}

//		Collections.sort(this.msList, new WrapperComparator(MeasurementSetupController.getInstance(),
//															MeasurementSetupController.KEY_NAME, true));
		WrapperedListModel wrapperedListModel = (WrapperedListModel) this.testSetups.getModel();

		int selectedIndex = this.testSetups.getSelectedIndex();
		this.testSetups.removeSelectionInterval(selectedIndex, selectedIndex);
		
		if (this.useAnalysisSetups.isSelected()) {
			wrapperedListModel.setElements(this.msListAnalysisOnly);
		} else {
			wrapperedListModel.setElements(this.msList);
		}
		
//		wrapperedListModel.removeAllElements();
//		for (Iterator it = this.msList.iterator(); it.hasNext();) {
//			MeasurementSetup measurementSetup1 = (MeasurementSetup) it.next();
//			if (!this.useAnalysisSetups.isSelected() || measurementSetup1.getCriteriaSet() != null
//					|| measurementSetup1.getThresholdSet() != null || measurementSetup1.getEtalon() != null) {
//				wrapperedListModel.addElement(measurementSetup1);
//			}
//		}

		this.testSetups.setEnabled(true);
		this.useAnalysisSetups.setEnabled(true);
		this.useWOAnalysisSetups.setEnabled(true);

		
//		Log.debugMessage("TestParametersPanel.setMeasurementSetups | measurementSetupId " + measurementSetupId, Log.FINEST);
		
		if (this.measurementSetupId != null) {
			try {
				this.setMeasurementSetup((MeasurementSetup) StorableObjectPool.getStorableObject(this.measurementSetupId, true));
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Object selectedItem = this.analysisComboBox.getSelectedItem();
		if (selectedItem != null) {
			this.selectComboBox(this.analysisComboBox, ((Identifiable) selectedItem).getId(), true);
		}
		selectedItem = this.evaluationComboBox.getSelectedItem();
		if (selectedItem != null) {
			this.selectComboBox(this.evaluationComboBox, ((Identifiable) selectedItem).getId(), true);
		}
	}

//	public void updateMeasurementSetups() {
//		if (this.msList == null) {
//			this.schedulerModel.refreshMeasurementSetups();
//			return;
//		}
//	}

	public void setSet(ParameterSet set) {
		if (set != null && this.parametersTestPanel != null) {
			// this.parametersTestPanel.setSet(set);
		}
	}

	public void setAnalysisTypes(Collection analysisTypes) {
		boolean enable = this.analysisComboBox.isEditable();
		this.analysisComboBox.setEditable(true);
		WrapperedListModel model = (WrapperedListModel) TestParametersPanel.this.analysisComboBox.getModel();
		model.removeAllElements();
		model.addElement(null);
		for (Iterator it = analysisTypes.iterator(); it.hasNext();) {
			AnalysisType analysisType = (AnalysisType) it.next();
			model.addElement(analysisType);
		}
		if (!enable) {
			this.analysisComboBox.setEditable(enable);
		}
	}

	public void setEvaluationTypes(Collection evaluationTypes) {
		boolean enable = this.evaluationComboBox.isEditable();
		this.evaluationComboBox.setEditable(true);

		WrapperedListModel model = (WrapperedListModel) TestParametersPanel.this.evaluationComboBox.getModel();
		model.removeAllElements();
		model.addElement(null);
		for (Iterator it = evaluationTypes.iterator(); it.hasNext();) {
			EvaluationType evaluationType = (EvaluationType) it.next();
			model.addElement(evaluationType);
		}
		if (!enable) {
			this.evaluationComboBox.setEditable(enable);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		this.propertyChangeEvent = evt;
		// String commandName = ae.getActionCommand();
		String propertyName = evt.getPropertyName();
		Object newValue = evt.getNewValue();
		// Object obj = ae.getSource();
		// Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" +
		// commandName, getClass().getName());
		if (propertyName.equals(SchedulerModel.COMMAND_CHANGE_ME_TYPE)) {
			Identifier meId = (Identifier) evt.getNewValue();
			try {
				MonitoredElement me = (MonitoredElement) StorableObjectPool.getStorableObject(meId, true);
				MeasurementPort port = (MeasurementPort) StorableObjectPool.getStorableObject(
					me.getMeasurementPortId(), true);
				this.switchPanel.removeAll();

				this.parametersTestPanel = (ParametersTestPanel) this.panels.get(port.getType().getCodename());
				if (this.parametersTestPanel != null) {
					this.parametersTestPanel.setMonitoredElement(me);
					/*
					 * / this.switchPanel.add(parametersTestPanel); /
					 */
					this.switchPanel.add(this.parametersTestPanel, "");
					// */
					this.tabbedPane.revalidate();
					this.tabbedPane.setEnabledAt(1, true);
				}
			} catch (ApplicationException e) {
				AbstractMainFrame.showErrorMessage(this.tabbedPane, e);
			}
		} else if (propertyName.equals(SchedulerModel.COMMAND_SET_ANALYSIS_TYPE)) {
			this.selectComboBox(this.analysisComboBox, (Identifier) newValue, true);
		} else if (propertyName.equals(SchedulerModel.COMMAND_SET_EVALUATION_TYPE)) {
			this.selectComboBox(this.evaluationComboBox, (Identifier) newValue, true);
		} else if (propertyName.equals(SchedulerModel.COMMAND_SET_MEASUREMENT_SETUP)) {
			this.setMeasurementSetup((MeasurementSetup) newValue);
		} else if (propertyName.equals(SchedulerModel.COMMAND_SET_MEASUREMENT_SETUPS)) {
			this.setMeasurementSetups((Collection<MeasurementSetup>) newValue);
		} else if (propertyName.equals(SchedulerModel.COMMAND_SET_SET)) {
			this.setSet((ParameterSet) newValue);
		} else if (propertyName.equals(SchedulerModel.COMMAND_GET_ANALYSIS_TYPE)) {
			Identifier analysisTypeId = this.getAnalysisTypeId();
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_SET_ANALYSIS_TYPE,
																		null, analysisTypeId));
		} else if (propertyName.equals(SchedulerModel.COMMAND_GET_EVALUATION_TYPE)) {
			Identifier evaluationTypeId = this.getEvaluationTypeId();
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this,
																		SchedulerModel.COMMAND_SET_EVALUATION_TYPE,
																		null, evaluationTypeId));
		} else if (propertyName.equals(SchedulerModel.COMMAND_GET_MEASUREMENT_SETUP)) {
			MeasurementSetup measurementSetup1 = getMeasurementSetup();
			if (measurementSetup1 != null) {
				this.dispatcher
						.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_SET_MEASUREMENT_SETUP,
																	null, measurementSetup1));
			}
		} else if (propertyName.equals(SchedulerModel.COMMAND_GET_SET)) {
			ParameterSet set = getSet();
			if (set != null) {
				this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_SET_SET, null,
																			set));
			}
		} else if (propertyName.equals(SchedulerModel.COMMAND_ADD_NEW_MEASUREMENT_SETUP)) {
			int selectedIndex = this.testSetups.getSelectedIndex();
			this.testSetups.removeSelectionInterval(selectedIndex, selectedIndex);
			this.tabbedPane.setSelectedIndex(1);
		} else if (propertyName.equals(SchedulerModel.COMMAND_SET_ANALYSIS_TYPES)) {
			this.setAnalysisTypes((Collection) newValue);
		} else if (propertyName.equals(SchedulerModel.COMMAND_SET_EVALUATION_TYPES)) {
			this.setEvaluationTypes((Collection) newValue);
		}

		this.propertyChangeEvent = null;
	}

	public JComponent getComponent() {
		return this.tabbedPane;
	}

	public void unregisterDispatcher() {
		this.dispatcher.removePropertyChangeListener(SchedulerModel.COMMAND_CHANGE_ME_TYPE, this);
	}

	private void initModule(final Dispatcher dispatcher1) {
		this.dispatcher = dispatcher1;
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_CHANGE_ME_TYPE, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_ANALYSIS_TYPE, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_EVALUATION_TYPE, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_MEASUREMENT_SETUP, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_MEASUREMENT_SETUPS, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_ANALYSIS_TYPES, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_EVALUATION_TYPES, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_SET, this);

		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_ANALYSIS_TYPE, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_EVALUATION_TYPE, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_MEASUREMENT_SETUP, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_SET, this);

		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_ADD_NEW_MEASUREMENT_SETUP, this);

	}

	synchronized void selectComboBox(	WrapperedComboBox cb,
										Identifier identifier,
										boolean changeStatus) {
		if (changeStatus && !this.useAnalysisBox.isSelected()) {
			if (!this.useAnalysisSetups.isSelected()) {
				this.useAnalysisSetups.doClick();
			}
			this.useAnalysisBox.doClick();
		}

		StorableObject storableObject = null;
		if (identifier != null) {
			try {
				storableObject = StorableObjectPool.getStorableObject(identifier, true);
			} catch (ApplicationException e) {
				AbstractMainFrame.showErrorMessage(this.parametersTestPanel, e);
			}
		}
		
//		System.out.println("TestParametersPanel.selectComboBox() | " + identifier);
		
		cb.setSelectedItem(storableObject);

		// Log.debugMessage("TestParametersPanel.selectComboBox |
		// this.evaluationComboBox.getSelectedItem(): " +
		// this.evaluationComboBox.getSelectedItem(), Log.FINEST);
		// Log.debugMessage("TestParametersPanel.selectComboBox |
		// this.analysisComboBox.getSelectedItem(): " +
		// this.analysisComboBox.getSelectedItem(), Log.FINEST);
		// Log.debugMessage("TestParametersPanel.selectComboBox |
		// this.useAnalysisBox.isSelected():" +
		// this.useAnalysisBox.isSelected(), Log.FINEST);

//		System.out.println("TestParametersPanel.selectComboBox() | " + (this.evaluationComboBox.getSelectedItem() == null));
//		System.out.println("TestParametersPanel.selectComboBox() | " + (this.analysisComboBox.getSelectedItem() == null));
		
		Object evaluationSelectedItem = this.evaluationComboBox.getSelectedItem();
		Object analysisSelectedItem = this.analysisComboBox.getSelectedItem();
		
		if ((evaluationSelectedItem == null && analysisSelectedItem == null
				&& this.useAnalysisBox.isSelected()) ||
				((evaluationSelectedItem != null || analysisSelectedItem != null) && !this.useAnalysisBox.isSelected())) {
//			System.out.println("TestParametersPanel.selectComboBox() | this.useAnalysisSetups.isSelected():" + this.useAnalysisSetups.isSelected());
//			System.out.println("TestParametersPanel.selectComboBox() | this.useAnalysisBox.isSelected(): " + this.useAnalysisBox.isSelected());
			if (!this.useAnalysisSetups.isSelected() && !this.useAnalysisBox.isSelected()) {
//				System.out.println("TestParametersPanel.selectComboBox()");
				this.useAnalysisSetups.doClick();
			}
			this.useAnalysisBox.doClick();
		}
	}

}
