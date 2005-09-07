
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIDefaults;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.UI.WrapperedList;
import com.syrus.AMFICOM.client.UI.WrapperedListModel;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Plugger;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupWrapper;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.util.Log;

public class TestParametersPanel implements PropertyChangeListener {

	ApplicationContext		aContext;
	SchedulerModel			schedulerModel;

	

	JPanel					switchPanel;
	List<MeasurementSetup>	msList;
	List<MeasurementSetup>	msListAnalysisOnly;

	// UI components begin
	private JCheckBox useSetupsCheckBox;
	private JLabel analysisLabel;
	private JCheckBox useAnalysisSetupsCheckBox;
	JComboBox analysisComboBox;
	private JLabel patternsLabel;
	WrapperedList<MeasurementSetup> testSetups;
	// UI components end
	
	private Dispatcher		dispatcher;

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
			Log.debugMessage("TestParametersPanel.preparePanels | codename " 
					+ codename 
					+ ", className " 
					+ className, 
				Log.DEBUGLEVEL10);
			this.panels.put(codename, new UIDefaults.LazyValue() {

				public Object createValue(UIDefaults table) {
					return (ParametersTestPanel) Plugger.reflectClass(className,
						new Class[] { ApplicationContext.class}, new Object[] { TestParametersPanel.this.aContext});
				}
			});

		}
	}

	private void createGUI() {
		final AnalysisType[] analysisTypes = AnalysisType.values();

		Comparator<AnalysisType> comparator = new Comparator<AnalysisType>() {
			public int compare(AnalysisType at1, AnalysisType at2) {
				return at1.getDescription().compareTo(at2.getDescription());
			}
		};

		Arrays.sort(analysisTypes, comparator);

		this.analysisComboBox = new JComboBox(analysisTypes);

		this.analysisComboBox.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(final JList list,
					final Object value,
					final int index,
					final boolean isSelected,
					final boolean cellHasFocus) {
				final AnalysisType analysisType = ((AnalysisType) value);
				return super.getListCellRendererComponent(list,
						(analysisType != null) ? analysisType.getDescription() : null,
						index,
						isSelected,
						cellHasFocus);
			}
		});

		this.switchPanel = new JPanel(new CardLayout());

		this.patternPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;

		this.patternPanel.setBorder(BorderFactory.createEtchedBorder());
		this.switchPanel.setBorder(BorderFactory.createEtchedBorder());

		this.useSetupsCheckBox = new JCheckBox(LangModelSchedule.getString("Text.MeasurementParameter.UseSetup"));
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

		this.useAnalysisSetupsCheckBox = new JCheckBox(LangModelSchedule.getString("Text.MeasurementParameter.WithAnalysisParameters"));
		this.useAnalysisSetupsCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JCheckBox checkBox = (JCheckBox) e.getSource();
				WrapperedListModel<MeasurementSetup> wrapperedListModel = TestParametersPanel.this.testSetups.getModel();
				Object selectedValue = TestParametersPanel.this.testSetups.getSelectedValue();
				int selectedIndex = TestParametersPanel.this.testSetups.getSelectedIndex();
				TestParametersPanel.this.testSetups.removeSelectionInterval(selectedIndex, selectedIndex);

				List<MeasurementSetup> list;
				final boolean selected = checkBox.isSelected();
				if (selected) {
					list = TestParametersPanel.this.msListAnalysisOnly;
				} else {
					list = TestParametersPanel.this.msList;
				}

				wrapperedListModel.setElements(list);
				if (selectedValue != null) {
					TestParametersPanel.this.testSetups.setSelectedValue(selectedValue, true);
				}

				TestParametersPanel.this.analysisComboBox.setEnabled(selected);
				if (!selected) {
					TestParametersPanel.this.selectAnalysisType(TestParametersPanel.this.analysisComboBox, null, false);
				}
			}
		});

// this.patternPanel.add(new JLabel(LangModelSchedule.getString("Use setup") +
// ':'), gbc);
		this.patternPanel.add(this.useAnalysisSetupsCheckBox, gbc);

		this.analysisLabel = new JLabel(LangModelSchedule.getString("Text.MeasurementParameter.Analysis")); //$NON-NLS-1$
		this.patternPanel.add(this.analysisLabel, gbc);
		this.patternPanel.add(this.analysisComboBox, gbc);
		this.patternsLabel = new JLabel(LangModelSchedule.getString("Text.MeasurementParameter.Patterns"));
		this.patternPanel.add(this.patternsLabel, gbc);

		this.analysisComboBox.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent e) {
				final JComboBox comboBox = (JComboBox) e.getSource();
				final AnalysisType analysisType = (AnalysisType) comboBox.getSelectedItem();
				final Set<Identifier> selectedTestIds = TestParametersPanel.this.schedulerModel.getSelectedTestIds();
				if (selectedTestIds != null && !selectedTestIds.isEmpty() && TestParametersPanel.this.propertyChangeEvent == null) {
					try {
						final Set<Test> tests = StorableObjectPool.getStorableObjects(selectedTestIds, true);
						for (final Test test : tests) {
							if (test.isChanged()) {
								test.setAnalysisType(analysisType != null ? analysisType : null);
							}
						}
					} catch (final ApplicationException e1) {
						AbstractMainFrame.showErrorMessage(TestParametersPanel.this.parametersTestPanel, e1);
					}
				}
			}
		});

		this.testSetups = new WrapperedList<MeasurementSetup>(MeasurementSetupWrapper.getInstance(),
				StorableObjectWrapper.COLUMN_DESCRIPTION,
				StorableObjectWrapper.COLUMN_ID);
		this.testSetups.setEnabled(false);
		this.testSetups.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(final ListSelectionEvent e) {

				final MeasurementSetup measurementSetup = (MeasurementSetup) TestParametersPanel.this.testSetups.getSelectedValue();

				if (TestParametersPanel.this.parametersTestPanel != null) {
					new Thread() {
						@Override
						public void run() {
							TestParametersPanel.this.parametersTestPanel.setSet(measurementSetup != null
									? measurementSetup.getParameterSet()
										: null);
						}
					}.start();
				}

				if (TestParametersPanel.this.propertyChangeEvent != null) {
					return;
				}

				TestParametersPanel.this.measurementSetupId = measurementSetup != null ? measurementSetup.getId() : null;

				if (measurementSetup != null) {
					final Set<Identifier> selectedTestIds = TestParametersPanel.this.schedulerModel.getSelectedTestIds();
					if (selectedTestIds != null && !selectedTestIds.isEmpty() && TestParametersPanel.this.propertyChangeEvent == null) {
						try {
							final Set<Identifier> measurementSetupIdSet = Collections.singleton(measurementSetup.getId());
							final Set<StorableObject> storableObjects = StorableObjectPool.getStorableObjects(selectedTestIds, true);
							for (Iterator iterator = storableObjects.iterator(); iterator.hasNext();) {
								final Test test = (Test) iterator.next();
								if (test.isChanged()) {
									test.setMeasurementSetupIds(measurementSetupIdSet);
								}
							}
						} catch (ApplicationException e1) {
							AbstractMainFrame.showErrorMessage(TestParametersPanel.this.parametersTestPanel, e1);
						}
					}
				}
			}
		});
		final JScrollPane scroll = new JScrollPane(this.testSetups);
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

	public AnalysisType getAnalysisType() {
		return this.useAnalysisSetupsCheckBox.isSelected()
				? (AnalysisType) this.analysisComboBox.getSelectedItem()
					: AnalysisType.UNKNOWN;
	}

	public MeasurementSetup getMeasurementSetup() {
		MeasurementSetup measurementSetup1 = null;
		if (this.useSetupsCheckBox.isSelected()) {
			measurementSetup1 = (MeasurementSetup) this.testSetups.getSelectedValue();
			if (measurementSetup1 == null) {
				JOptionPane.showMessageDialog(this.patternPanel,
						LangModelSchedule.getString("Error.HaveNotChoosenMeasurementPattern"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
						JOptionPane.OK_OPTION);
				return null;
			}
		}
		return measurementSetup1;
	}

	public ParameterSet getSet() {
		ParameterSet set = null;
		if (!this.useSetupsCheckBox.isSelected()) {
			set = this.parametersTestPanel != null ? this.parametersTestPanel.getSet() : null;
		}
		return set;
	}

	public void setMeasurementSetup(final MeasurementSetup measurementSetup) {
//		Log.debugMessage("TestParametersPanel.setMeasurementSetup | "
//				+ (measurementSetup != null ? measurementSetup.getId() : null), Level.FINEST);		

		this.testSetups.setSelectedValue(null, false);
		
		this.measurementSetupId = measurementSetup != null ? measurementSetup.getId() : null;
//		System.out.println("TestParametersPanel.setMeasurementSetup() | " + this.testSetups.getSelectedIndex());
		if (measurementSetup != null) {
			this.testSetups.setSelectedValue(measurementSetup, true);
//			this.setSet(measurementSetup.getParameterSet());
		} else {
			int selectedIndex = this.testSetups.getSelectedIndex();
			this.testSetups.removeSelectionInterval(selectedIndex, selectedIndex);
//			this.setSet(null);
			return;
		}
//		System.out.println("TestParametersPanel.setMeasurementSetup() | " + this.testSetups.getSelectedIndex());
		
//		if (this.measurementSetupId != null && measurementSetup != null
//				&& this.measurementSetupId.equals(measurementSetup.getId()) || this.msList == null) 
		{
//			 Log.debugMessage("TestParametersPanel.setMeasurementSetup | return ", Level.FINEST);
			 if (this.msList == null) {
				 return;
			 }
			
		}
		
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
				
				this.testSetups.setSelectedValue(measurementSetup, true);
			}
		}
		
		boolean params = this.useAnalysisSetupsCheckBox.isSelected() && 
			this.testSetups.getSelectedIndex() < 0;
		
		if (params) {
			if (this.useAnalysisSetupsCheckBox.isSelected()) {
				this.useAnalysisSetupsCheckBox.doClick();
			}
			this.testSetups.setSelectedValue(measurementSetup, true);			
		}
		
		if (!this.useSetupsCheckBox.isSelected()) {
			this.useSetupsCheckBox.doClick();
		}
	}

	public void setMeasurementSetups(Collection<MeasurementSetup> measurementSetups) {

		// Log.debugMessage("TestParametersPanel.setMeasurementSetups | ",
		// Level.FINEST);

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
		for (MeasurementSetup measurementSetup : measurementSetups) {
			if (measurementSetup.getCriteriaSet() != null
					|| measurementSetup.getEtalon() != null
					|| measurementSetup.getThresholdSet() != null) {
				this.msListAnalysisOnly.add(measurementSetup);
			}
		}

		final WrapperedListModel<MeasurementSetup> wrapperedListModel = this.testSetups.getModel();

		int selectedIndex = this.testSetups.getSelectedIndex();
		this.testSetups.removeSelectionInterval(selectedIndex, selectedIndex);

		if (this.useAnalysisSetupsCheckBox.isSelected()) {
			wrapperedListModel.setElements(this.msListAnalysisOnly);
		} else {
			wrapperedListModel.setElements(this.msList);
		}

		this.testSetups.setEnabled(true);
		this.useAnalysisSetupsCheckBox.setEnabled(true);

		if (this.measurementSetupId != null) {
			try {
				this.setMeasurementSetup((MeasurementSetup) StorableObjectPool.getStorableObject(this.measurementSetupId, true));
			} catch (ApplicationException e) {
				JOptionPane.showMessageDialog(this.patternPanel,
						LangModelSchedule.getString("Error.Text.CannotGetObject"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
						JOptionPane.OK_OPTION);
			}
		}

		this.selectAnalysisType(this.analysisComboBox, (AnalysisType) this.analysisComboBox.getSelectedItem(), true);
	}

	public void setSet(final ParameterSet set) {
		if (this.parametersTestPanel != null) {
			 this.parametersTestPanel.setSet(set);
		}
	}

	public void setAnalysisTypes(final Set<AnalysisType> analysisTypes) {
		final boolean enable = this.analysisComboBox.isEditable();
		this.analysisComboBox.setEditable(true);
		WrapperedListModel<?> model = (WrapperedListModel) TestParametersPanel.this.analysisComboBox.getModel();
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
					this.switchPanel.add(this.parametersTestPanel, "");
					this.patternPanel.revalidate();
					this.parametersTestPanel.setEnableEditing(!this.useSetupsCheckBox.isSelected());
				}
			} catch (ApplicationException e) {
				AbstractMainFrame.showErrorMessage(this.patternPanel, e);
			}
		} else if (propertyName.equals(SchedulerModel.COMMAND_SET_ANALYSIS_TYPE)) {
			this.selectAnalysisType(this.analysisComboBox, (AnalysisType) newValue, true);
		} else if (propertyName.equals(SchedulerModel.COMMAND_SET_MEASUREMENT_SETUP)) {
			this.setMeasurementSetup((MeasurementSetup) newValue);
		} else if (propertyName.equals(SchedulerModel.COMMAND_SET_MEASUREMENT_SETUPS)) {
			this.setMeasurementSetups((Collection) newValue);
		} else if (propertyName.equals(SchedulerModel.COMMAND_SET_SET)) {
			this.setSet((ParameterSet) newValue);
		} else if (propertyName.equals(SchedulerModel.COMMAND_GET_ANALYSIS_TYPE)) {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_SET_ANALYSIS_TYPE,
																		null, this.getAnalysisType()));
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
			this.setMeasurementSetup((MeasurementSetup) evt.getNewValue());
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
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_SET, this);

		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_ANALYSIS_TYPE, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_MEASUREMENT_SETUP, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_SET, this);

		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_ADD_NEW_MEASUREMENT_SETUP, this);

	}

	synchronized void selectAnalysisType(	final JComboBox cb,
	                                 	final AnalysisType analysisType,
	                                 	final boolean changeStatus) {
		cb.setSelectedItem(analysisType);
		AnalysisType selectedItem = (AnalysisType) cb.getSelectedItem();		
		if (changeStatus && selectedItem != AnalysisType.UNKNOWN
				&& !this.useAnalysisSetupsCheckBox.isSelected()) {
			this.useAnalysisSetupsCheckBox.doClick();
		}

	}

}
