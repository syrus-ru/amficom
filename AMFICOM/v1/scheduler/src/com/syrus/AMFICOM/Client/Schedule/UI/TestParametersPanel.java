
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.UI.ProcessingDialog;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.UI.WrapperedList;
import com.syrus.AMFICOM.client.UI.WrapperedListModel;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.DescribableWrapper;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Plugger;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupWrapper;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;

final class TestParametersPanel implements PropertyChangeListener {

	ApplicationContext		aContext;
	SchedulerModel			schedulerModel;
	
	JPanel					switchPanel;
	List<MeasurementSetup>	msList;
	List<MeasurementSetup>	msListAnalysisOnly;

	// UI components begin
	private JCheckBox useSetupsCheckBox;
	private JLabel analysisLabel;
	private JCheckBox useAnalysisSetupsCheckBox;
	JCheckBox allAvailableCheckBox;
	
	WrapperedComboBox<Describable> analysisComboBox;
	private JLabel patternsLabel;
	WrapperedList<MeasurementSetup> testSetups;
	// UI components end
	
	Dispatcher		dispatcher;

	ParametersTestPanel		parametersTestPanel;

	Identifier		measurementSetupId;

	private final UIDefaults panels = new UIDefaults();
	
	PropertyChangeEvent propertyChangeEvent;
	
	private JPanel	patternPanel;	
	
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
			Log.debugMessage("codename " 
					+ codename 
					+ ", className " 
					+ className, 
				Log.DEBUGLEVEL08);
			this.panels.put(codename, new UIDefaults.LazyValue() {

				public Object createValue(UIDefaults table) {
					return Plugger.reflectClass(className,
						new Class[] { ApplicationContext.class, TestParametersPanel.class}, 
						new Object[] { TestParametersPanel.this.aContext, TestParametersPanel.this});
				}
			});

		}
	}

	@SuppressWarnings("serial")
	private void createGUI() {
		final AnalysisType[] analysisTypes = AnalysisType.values();
		
		Arrays.sort(analysisTypes, 
			new WrapperComparator<Describable>(DescribableWrapper.getInstance(), 
					DescribableWrapper.COLUMN_DESCRIPTION));
		
		final List<Describable> analysisTypeList = 
			new ArrayList<Describable>(Arrays.asList(analysisTypes));		

		this.analysisComboBox = new WrapperedComboBox<Describable>(DescribableWrapper.getInstance(),  
			analysisTypeList,
			DescribableWrapper.COLUMN_DESCRIPTION,
			null
			);

		this.switchPanel = new JPanel(new CardLayout());

		this.patternPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;

		this.patternPanel.setBorder(BorderFactory.createEtchedBorder());
		this.switchPanel.setBorder(BorderFactory.createEtchedBorder());

		this.useSetupsCheckBox = new JCheckBox(I18N.getString("Scheduler.Text.MeasurementParameter.UseSetup"));
		this.useSetupsCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean useSetup = checkBox.isSelected();
				if (TestParametersPanel.this.parametersTestPanel != null) {
					TestParametersPanel.this.parametersTestPanel.setEnableEditing(!useSetup);
				}
				TestParametersPanel.this.setEnableEditing(useSetup);
			}
		});
		this.patternPanel.add(this.useSetupsCheckBox, gbc);

		this.useAnalysisSetupsCheckBox = new JCheckBox(I18N.getString("Scheduler.Text.MeasurementParameter.WithAnalysisParameters"));
		this.useAnalysisSetupsCheckBox.addActionListener(new ActionListener() {
			@SuppressWarnings("unqualified-field-access")
			public void actionPerformed(final ActionEvent e) {
				final JCheckBox checkBox = (JCheckBox) e.getSource();
				
				boolean selected = checkBox.isSelected();
				
				analysisComboBox.setEnabled(selected);
				if (!selected) {
					selectAnalysisType(analysisComboBox, 
						AnalysisType.UNKNOWN, 
						false);
				}
				
				// there is no reason to perform this event if disable ability all setups
				if (!allAvailableCheckBox.isSelected()) { 
					return;
				}
		
				final WrapperedListModel<MeasurementSetup> wrapperedListModel = testSetups.getModel();
				MeasurementSetup selectedMeasurementSetup = (MeasurementSetup) testSetups.getSelectedValue();
//				if (!measurementSetupId.isVoid()) {
//					try {
//						Log.debugMessage(".actionPerformed | " + measurementSetupId, Log.DEBUGLEVEL10);
//						selectedValue = StorableObjectPool.getStorableObject(measurementSetupId, true);
//					} catch (final ApplicationException e1) {
//						AbstractMainFrame.showErrorMessage(I18N.getString("Error.CannotAcquireObject"));
//						return;
//					}
//				}
				
				testSetups.clearSelection();

				final List<MeasurementSetup> list;
				
				if (selected) {
					list = msListAnalysisOnly;
				} else {
					list = msList;
				}
				
				wrapperedListModel.setElements(list);
				if (selectedMeasurementSetup != null) {
					testSetups.setSelectedValue(selectedMeasurementSetup, true);
					if (!selected) {
						selected = isAnalysisEnable(selectedMeasurementSetup);
					}
				}

				
			}
		});

		this.patternPanel.add(this.useAnalysisSetupsCheckBox, gbc);

		this.analysisLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Analysis")); //$NON-NLS-1$
		this.patternPanel.add(this.analysisLabel, gbc);
		this.patternPanel.add(this.analysisComboBox, gbc);
		this.patternsLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Patterns"));
		this.patternPanel.add(this.patternsLabel, gbc);

		this.analysisComboBox.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent e) {
				final JComboBox comboBox = (JComboBox) e.getSource();
				final AnalysisType analysisType = (AnalysisType) comboBox.getSelectedItem();
				if (TestParametersPanel.this.propertyChangeEvent == null) {
					final Set<Test> tests = TestParametersPanel.this.schedulerModel.getSelectedTests();
					for (final Test test : tests) {
						if (test.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
							test.setAnalysisType(analysisType);
						}
					}
				}
			}
		});
		
		this.allAvailableCheckBox = new JCheckBox(
			I18N.getString("Scheduler.Text.MeasurementParameter.AllAvailableSetups"),
			false);
		
		this.allAvailableCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				final JCheckBox checkBox = (JCheckBox) e.getSource();
				if (checkBox.isSelected()) {
					refreshMeasurementSetups();
				} else {
					final WrapperedListModel<MeasurementSetup> wrapperedListModel = 
						testSetups.getModel();
					final Set<MeasurementSetup> emptySet = Collections.emptySet();
					wrapperedListModel.setElements(emptySet);
					try {
						refreshMeasurementSetup();
					} catch (ApplicationException e1) {
						AbstractMainFrame.showErrorMessage(I18N.getString("Error.CannotAcquireObject"));
					}
				}
			}
		});
		
		this.patternPanel.add(this.allAvailableCheckBox, gbc);

		this.measurementSetupId = Identifier.VOID_IDENTIFIER;
		this.testSetups = new WrapperedList<MeasurementSetup>(MeasurementSetupWrapper.getInstance(),
				StorableObjectWrapper.COLUMN_DESCRIPTION,
				StorableObjectWrapper.COLUMN_ID);
		this.testSetups.setEnabled(false);
		this.testSetups.setAutoscrolls(true);
		this.testSetups.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(final ListSelectionEvent e) {
				
				final MeasurementSetup measurementSetup = 
					(MeasurementSetup) TestParametersPanel.this.testSetups.getSelectedValue();
				assert Log.debugMessage(measurementSetup, Log.DEBUGLEVEL03);
				if (TestParametersPanel.this.parametersTestPanel != null) {
					SwingUtilities.invokeLater(new Runnable() {
						
						public void run() {
							TestParametersPanel.this.parametersTestPanel.setMeasurementSetup(measurementSetup);
						}
					});
				}

				if (TestParametersPanel.this.propertyChangeEvent != null) {
					return;
				}

				if (measurementSetup != null) {
					final boolean analysisEnable = isAnalysisEnable(measurementSetup);
					if (!analysisEnable) {
						TestParametersPanel.this.analysisComboBox.setSelectedItem(AnalysisType.UNKNOWN);
					}
					TestParametersPanel.this.analysisComboBox.setEnabled(analysisEnable);
					if (TestParametersPanel.this.propertyChangeEvent == null) {
						try {
							TestParametersPanel.this.schedulerModel.changeMeasurementSetup(measurementSetup);							
						} catch (final ApplicationException e1) {
							AbstractMainFrame.showErrorMessage(TestParametersPanel.this.parametersTestPanel, e1);
						}
					}
				}
			}
		});
		final JScrollPane scroll = new JScrollPane(this.testSetups);
		scroll.setAutoscrolls(true);
		gbc.weighty = 1.0;
		this.patternPanel.add(scroll, gbc);

		gbc.weighty = 0.0;
		this.patternPanel.add(this.switchPanel, gbc);
		this.analysisComboBox.setEnabled(false);
		this.useSetupsCheckBox.doClick();
	}

	void setEnableEditing(final boolean enable) {
		this.testSetups.setEnabled(enable);
		if (!enable) {
			if (this.useAnalysisSetupsCheckBox.isSelected()) {
				this.useAnalysisSetupsCheckBox.doClick();
			}
		}
		this.useAnalysisSetupsCheckBox.setEnabled(enable);
		this.patternsLabel.setEnabled(enable);
		this.analysisLabel.setEnabled(enable);
	}

	public final AnalysisType getAnalysisType() {
		final AnalysisType analysisType = (AnalysisType) this.analysisComboBox.getSelectedItem();
		return analysisType != null ? analysisType : AnalysisType.UNKNOWN;
	}

	public MeasurementSetup getMeasurementSetup() {
		if (this.useSetupsCheckBox.isSelected()) {
			final MeasurementSetup measurementSetup = (MeasurementSetup) this.testSetups.getSelectedValue();
			if (measurementSetup == null) {
				JOptionPane.showMessageDialog(this.patternPanel,
						I18N.getString("Scheduler.Error.HaveNotChoosenMeasurementPattern"), I18N.getString("Scheduler.Error"), //$NON-NLS-1$ //$NON-NLS-2$
						JOptionPane.OK_OPTION);
				this.schedulerModel.setBreakData();
				return null;
			}
			return measurementSetup;
		}
		
		// otherwise 
		
		try {
			return this.parametersTestPanel != null ? this.parametersTestPanel.getMeasurementSetup() : null;
		} catch (final CreateObjectException e) {
			this.schedulerModel.setBreakData();
			AbstractMainFrame.showErrorMessage(I18N.getString("Scheduler.Error.CannotCreateMeasurementSetup"));
			return null;
		}
	}

	public void refreshMeasurementSetup(final MeasurementSetup measurementSetup) {
		final WrapperedListModel<MeasurementSetup> wrapperedListModel = 
			this.testSetups.getModel();
		
		wrapperedListModel.sort();
		
		this.setMeasurementSetup(measurementSetup);

	}
	
	public void setMeasurementSetup(final MeasurementSetup measurementSetup) {
		this.setMeasurementSetup(measurementSetup, false);
	}
	
	public void setMeasurementSetup(final MeasurementSetup measurementSetup, 
	                                final boolean switchToSetups) {
		
		final boolean single = !this.allAvailableCheckBox.isSelected();
		
		this.testSetups.clearSelection();
		this.measurementSetupId = measurementSetup != null ? measurementSetup.getId() : Identifier.VOID_IDENTIFIER;
		if (measurementSetup == null || this.msList == null && !single) {
			return;
		}
		
		if (single) {
			if (this.msList == null) {
				this.msList = new LinkedList<MeasurementSetup>();
			} else {
				this.msList.clear();
			}
			this.msList.add(measurementSetup);
		}

		try {
			this.changeMonitoredElement(measurementSetup.getMonitoredElementIds().iterator().next());
		} catch (ApplicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (switchToSetups && !this.useSetupsCheckBox.isSelected()) {
			this.useSetupsCheckBox.doClick();
		}
		
		final boolean analysisSetupsSelected = this.useAnalysisSetupsCheckBox.isSelected();
		final boolean measurementSetupWithAnalysis = 
			measurementSetup.getCriteriaSet() != null &&
			measurementSetup.getEtalon() != null &&
			measurementSetup.getThresholdSet() != null;		 
		
		try {
			if (analysisSetupsSelected && 
					this.schedulerModel.getSelectedTest().getAnalysisType() == AnalysisType.UNKNOWN) {
				this.useAnalysisSetupsCheckBox.doClick();
			}
		} catch (final ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final WrapperedListModel<MeasurementSetup> wrapperedListModel = 
			this.testSetups.getModel();
		
		if (single) {
			wrapperedListModel.setElements(this.msList);
		} else if (!this.msList.contains(measurementSetup)) {
			this.msList.add(measurementSetup);
			// if total list doesn't contains ms, and ms with analysis - add to analysis ms list
			if(measurementSetupWithAnalysis) {
				this.msListAnalysisOnly.add(measurementSetup);
				if (analysisSetupsSelected) {
					wrapperedListModel.addElement(measurementSetup);					
				}
			}

			if (!analysisSetupsSelected) {
				wrapperedListModel.addElement(measurementSetup);
			}			
		}		
		
		if (this.testSetups.getSelectedValue() == null) {
			this.testSetups.setSelectedValue(measurementSetup, true);
		}
	}

	boolean isAnalysisEnable(final MeasurementSetup measurementSetup) {
		return measurementSetup.getCriteriaSet() != null
		|| measurementSetup.getEtalon() != null
		|| measurementSetup.getThresholdSet() != null;
	}
	
	void setMeasurementSetups0(final Set<MeasurementSetup> measurementSetups) {
		Log.debugMessage(measurementSetups,
			Log.DEBUGLEVEL10);
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
		for (final MeasurementSetup measurementSetup : measurementSetups) {
			if (this.isAnalysisEnable(measurementSetup)) {
				this.msListAnalysisOnly.add(measurementSetup);
			}
		}

		final WrapperedListModel<MeasurementSetup> wrapperedListModel = 
			this.testSetups.getModel();

		this.testSetups.clearSelection();

		if (this.useAnalysisSetupsCheckBox.isSelected()) {
			wrapperedListModel.setElements(this.msListAnalysisOnly);
		} else {
			wrapperedListModel.setElements(this.msList);
		}
	}
	
	void setMeasurementSetups(final Set<MeasurementSetup> measurementSetups) {
		this.setMeasurementSetups0(measurementSetups);

//		this.testSetups.setEnabled(true);
//		this.useAnalysisSetupsCheckBox.setEnabled(true);

		this.selectAnalysisType(this.analysisComboBox, this.getAnalysisType(), true);
		
		if (!this.measurementSetupId.isVoid()) {
			try {
				this.setMeasurementSetup((MeasurementSetup) StorableObjectPool.getStorableObject(this.measurementSetupId, true), true);
			} catch (final ApplicationException e) {
				AbstractMainFrame.showErrorMessage(I18N.getString("Error.CannotAcquireObject"));
			}
		}		
	}

	void refreshMeasurementSetups() {
		if (this.allAvailableCheckBox.isSelected()) {
			if (this.schedulerModel.getSelectedMeasurementType() != MeasurementType.UNKNOWN
					|| this.schedulerModel.getSelectedMonitoredElement() != null) {
				new ProcessingDialog(new Runnable() {

					@SuppressWarnings("unqualified-field-access")
					public void run() {
						try {
							setMeasurementSetups(schedulerModel.getMeasurementSetups());
						} catch (final ApplicationException e) {
							AbstractMainFrame.showErrorMessage(e.getMessage());
						}
					}
					
				}, I18N.getString("Common.ProcessingDialog.PlsWait"));
			}
		}
	}
	
	void refreshMeasurementSetup() throws ApplicationException {
		final Test selectedTest = this.schedulerModel.getSelectedTest();
		if (selectedTest != null) {
			final Set<Identifier> measurementSetupIds = selectedTest.getMeasurementSetupIds();
			if (!measurementSetupIds.isEmpty()) {
				final Identifier mainMeasurementSetupId = measurementSetupIds.iterator().next();
				final MeasurementSetup measurementSetup = 
					StorableObjectPool.getStorableObject(mainMeasurementSetupId, true);
				if (measurementSetup != null) {
					this.setMeasurementSetup(measurementSetup, true);
				}
			}
		}
	}
	
	private void changeMonitoredElement(final Identifier monitoredElementId) 
	throws ApplicationException {
		final MonitoredElement me = StorableObjectPool.getStorableObject(monitoredElementId, true);
		final MeasurementPort port = StorableObjectPool.getStorableObject(me.getMeasurementPortId(), true);
		this.switchPanel.removeAll();

		final String codename = port.getType().getCodename();
		this.parametersTestPanel = (ParametersTestPanel) this.panels.get(codename);
		if (this.parametersTestPanel != null) {
			this.parametersTestPanel.setMonitoredElement(me);
			this.switchPanel.add(this.parametersTestPanel, "");
			this.patternPanel.revalidate();
			this.parametersTestPanel.setEnableEditing(!this.useSetupsCheckBox.isSelected());
			this.setEnableEditing(this.useSetupsCheckBox.isSelected());
		} else {
			Log.errorMessage("Port type codename '" + codename + "' does not support");
		}
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		this.propertyChangeEvent = evt;
		final String propertyName = evt.getPropertyName();
		final Object newValue = evt.getNewValue();
		if (propertyName == SchedulerModel.COMMAND_CHANGE_ME_TYPE) {
			try {
				this.changeMonitoredElement((Identifier) evt.getNewValue());
			} catch (final ApplicationException e) {
				AbstractMainFrame.showErrorMessage(I18N.getString("Error.CannotAcquireObject"));
			}
		} else if (propertyName == SchedulerModel.COMMAND_SET_ANALYSIS_TYPE) {
			this.selectAnalysisType(this.analysisComboBox, (AnalysisType) newValue, true);
		} else if (propertyName.equals(SchedulerModel.COMMAND_REFRESH_MEASUREMENT_SETUP)){
			try {
				this.refreshMeasurementSetup();
			} catch (final ApplicationException e) {
				AbstractMainFrame.showErrorMessage(I18N.getString("Error.CannotAcquireObject"));
			}					
		} else if (propertyName == SchedulerModel.COMMAND_SET_MEASUREMENT_SETUP) {
			this.setMeasurementSetup((MeasurementSetup) newValue, true);
		} else if (propertyName == SchedulerModel.COMMAND_SET_MEASUREMENT_SETUPS) {
			this.refreshMeasurementSetups();
		} else if (propertyName == SchedulerModel.COMMAND_GET_ANALYSIS_TYPE) {
			this.dispatcher.firePropertyChange(
				new PropertyChangeEvent(this, 
					SchedulerModel.COMMAND_SET_ANALYSIS_TYPE,
					null, 
					this.getAnalysisType()));
		} else if (propertyName == SchedulerModel.COMMAND_GET_MEASUREMENT_SETUP) {
			MeasurementSetup measurementSetup1 = getMeasurementSetup();
			if (measurementSetup1 != null) {
				this.dispatcher.firePropertyChange(
					new PropertyChangeEvent(this, 
						SchedulerModel.COMMAND_SET_MEASUREMENT_SETUP,
						null, 
						measurementSetup1));
			}
		} 

		this.propertyChangeEvent = null;
	}

	public JComponent getComponent() {
		return this.patternPanel;
	}
	
	public void unregisterDispatcher() {
		this.dispatcher.removePropertyChangeListener(SchedulerModel.COMMAND_CHANGE_ME_TYPE, this);
	}

	private void initModule(final Dispatcher dispatcher1) {
		this.dispatcher = dispatcher1;
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_CHANGE_ME_TYPE, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_ANALYSIS_TYPE, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_MEASUREMENT_SETUP, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_MEASUREMENT_SETUPS, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_MEASUREMENT_SETUP, this);

		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_ANALYSIS_TYPE, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_MEASUREMENT_SETUP, this);

	}

	synchronized void selectAnalysisType(final JComboBox cb,
	                                 	final AnalysisType analysisType,
	                                 	final boolean changeStatus) {
		assert analysisType != null : ErrorMessages.NON_NULL_EXPECTED;
		cb.setSelectedItem(analysisType);
		AnalysisType selectedItem = (AnalysisType) cb.getSelectedItem();		
		if (changeStatus && selectedItem != AnalysisType.UNKNOWN
				&& !this.useAnalysisSetupsCheckBox.isSelected()) {
			this.useAnalysisSetupsCheckBox.doClick();
		}

	}

}
