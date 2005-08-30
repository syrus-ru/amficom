
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
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

	JComboBox		analysisComboBox;

	JPanel					switchPanel;
	List<MeasurementSetup>	msList;
	List<MeasurementSetup>	msListAnalysisOnly;

	WrapperedList			testSetups;

	JTabbedPane				tabbedPane;
	
	private JCheckBox 		useAnalysisSetupsCheckBox;

	private Dispatcher		dispatcher;

	ParametersTestPanel		parametersTestPanel;

	Identifier		measurementSetupId;

	private final UIDefaults panels = new UIDefaults();
	PropertyChangeEvent propertyChangeEvent;

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
		this.tabbedPane = new JTabbedPane();
		
		AnalysisType[] analysisTypes = AnalysisType.values();

		Comparator<AnalysisType> comparator = new Comparator<AnalysisType>() {
			public int compare(	AnalysisType at1,
			                   	AnalysisType at2) {
				return at1.getDescription().compareTo(at2.getDescription());
			}
		};

		Arrays.sort(analysisTypes, comparator);
		
		this.analysisComboBox = new JComboBox(analysisTypes);
		
		
		this.analysisComboBox.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(	JList list,
															Object value,
															int index,
															boolean isSelected,
															boolean cellHasFocus) {
				AnalysisType analysisType = ((AnalysisType)value);
				return super.getListCellRendererComponent(list, 
					analysisType != null ? analysisType.getDescription() : null, 
					index, 
					isSelected,
					cellHasFocus);
			}
		});
		
//		this.analysisComboBox.setEditable(true);
		
		
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
			
			this.useAnalysisSetupsCheckBox = new JCheckBox(LangModelSchedule.getString("Text.MeasurementParemeter.WithAnalysisParameters"));
			this.useAnalysisSetupsCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JCheckBox checkBox = (JCheckBox) e.getSource();
					WrapperedListModel wrapperedListModel = (WrapperedListModel) TestParametersPanel.this.testSetups.getModel();
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

			patternPanel.add(new JLabel(LangModelSchedule.getString("Use setup") + ':'), gbc);
			patternPanel.add(this.useAnalysisSetupsCheckBox, gbc);
		}

		final JLabel analysisLabel = new JLabel(LangModelSchedule.getString("Analysis")); //$NON-NLS-1$
		patternPanel.add(analysisLabel, gbc);
		patternPanel.add(this.analysisComboBox, gbc);
		patternPanel.add(new JLabel(LangModelSchedule.getString("Patterns")), gbc);

		this.analysisComboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JComboBox comboBox = (JComboBox) e.getSource();
				AnalysisType analysisType = (AnalysisType) comboBox.getSelectedItem();
				java.util.Set selectedTestIds = TestParametersPanel.this.schedulerModel.getSelectedTestIds();
				if (selectedTestIds != null && !selectedTestIds.isEmpty()
						&& TestParametersPanel.this.propertyChangeEvent == null) {
					try {
						java.util.Set storableObjects = StorableObjectPool.getStorableObjects(selectedTestIds, true);
						for (Iterator iterator = storableObjects.iterator(); iterator.hasNext();) {
							Test test = (Test) iterator.next();
							if (test.isChanged()) {
								test.setAnalysisType(analysisType != null ? analysisType : null);
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

			@Override
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

					if (measurementSetup1.isChanged()) {
						
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
									
									objList.setSelectedValue(measurementSetup1, true);
								}
	
							}
						});
					popup.add(msRename);
					}

					popup.show(objList, e.getX(), e.getY());
					
				}
			}
		});

		this.testSetups.setEnabled(false);
		this.testSetups.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				if (TestParametersPanel.this.propertyChangeEvent != null)
					return;
				
				final MeasurementSetup measurementSetup1 = 
					(MeasurementSetup) TestParametersPanel.this.testSetups.getSelectedValue();
				
				TestParametersPanel.this.measurementSetupId = 
					measurementSetup1 != null ? 
							measurementSetup1.getId() : 
							null;
				
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
//									Log.debugMessage(
//										"TestParametersPanel$ListSelectionListener.valueChanged | set to test "
//												+ test.getId() + " > " + measurementSetup1.getId(), Level.FINEST);
									test.setMeasurementSetupIds(measurementSetupIdSet);
								}
							}
						} catch (ApplicationException e1) {
							AbstractMainFrame.showErrorMessage(TestParametersPanel.this.parametersTestPanel, e1);
						}
					}

//					TestParametersPanel.this.useAnalysisBox.setEnabled(true);
					if (TestParametersPanel.this.parametersTestPanel != null) {
						new Thread() {
								@Override
								public void run() {
								TestParametersPanel.this.parametersTestPanel
										.setSet(measurementSetup1.getParameterSet());
							}
						}.start();
					}

				} else {
					TestParametersPanel.this.parametersTestPanel.setSet(null);
				}
			}
		});
		JScrollPane scroll = new JScrollPane(this.testSetups);
		gbc.weighty = 1.0;
		patternPanel.add(scroll, gbc);

		this.analysisComboBox.setEnabled(false);

		this.tabbedPane.addTab(LangModelSchedule.getString("Pattern"), null, patternPanel, LangModelSchedule
				.getString("Use pattern"));
		this.tabbedPane.addTab(LangModelSchedule.getString("Parameters"), null, this.switchPanel, LangModelSchedule
				.getString("Use parameters"));

		this.tabbedPane.setSelectedIndex(0);
		
		if (this.parametersTestPanel == null) {
			this.tabbedPane.setEnabledAt(1, false);
		}
	}

	public AnalysisType getAnalysisType() {
		if (this.tabbedPane.getSelectedIndex() == 0 && this.useAnalysisSetupsCheckBox.isSelected()) {
			return (AnalysisType) this.analysisComboBox.getSelectedItem();
		}
		return AnalysisType.UNKNOWN;
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
		
		this.tabbedPane.setSelectedIndex(this.useAnalysisSetupsCheckBox.isSelected() || this.parametersTestPanel == null ? 0 : 1);
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
		for(MeasurementSetup measurementSetup : measurementSetups) {
			if(measurementSetup.getCriteriaSet() != null ||
					measurementSetup.getEtalon() != null ||
					measurementSetup.getThresholdSet() != null) {
				this.msListAnalysisOnly.add(measurementSetup);
			}
		}

		WrapperedListModel wrapperedListModel = (WrapperedListModel) this.testSetups.getModel();

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.selectAnalysisType(this.analysisComboBox, (AnalysisType) this.analysisComboBox.getSelectedItem(), true);
	}

	public void setSet(ParameterSet set) {
		if (this.parametersTestPanel != null) {
			 this.parametersTestPanel.setSet(set);
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
			this.tabbedPane.setSelectedIndex(1);
			this.setMeasurementSetup((MeasurementSetup) evt.getNewValue());
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
