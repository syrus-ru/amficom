
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.UI.ProcessingDialogDummy;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.UI.WrapperedList;
import com.syrus.AMFICOM.client.UI.WrapperedListModel;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.DescribableWrapper;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.ActionTemplate;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupWrapper;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.Test.TestStatus;
import com.syrus.util.Log;

/*
 * TODO
 * 3. implement setDescription и нормальную работу descriptionField
 */
final class TestParametersPanel implements PropertyChangeListener {

	ApplicationContext		aContext;
	SchedulerModel			schedulerModel;

	JPanel					switchPanel;
	List<MeasurementSetup>	msList;
	List<MeasurementSetup>	msListAnalysisOnly;

	// UI components begin
	private JCheckBox		useSetupsCheckBox;
	private JLabel			analysisLabel;
	private JCheckBox		useAnalysisSetupsCheckBox;
	JCheckBox				allAvailableCheckBox;

	// moved from RTP
	private JTextField		descriptionField;

	WrapperedComboBox<Describable> analysisComboBox;
	private JLabel			patternsLabel;
	WrapperedList<MeasurementSetup> testSetups;
	// UI components end

	Dispatcher				dispatcher;

	MeasurementParametersPanel parametersTestPanel;

	Identifier				measurementSetupId; // FIXME: не понятно, то ли мы им пользуемся, то ли нет

	EventObject		currentEvent;

	private JPanel			patternPanel;

	public TestParametersPanel(final ApplicationContext aContext) {
		this.aContext = aContext;
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();

		if (aContext != null) {
			initModule(aContext.getDispatcher());
		}

		this.createGUI();
	}

	private void loggedIn() {
		final Set<AnalysisType> atValues;
		try {
			atValues = AnalysisType.getValues();
		} catch (ApplicationException e) {
			/* XXX: ApplicationException handling */
			Log.errorMessage(e);
			throw new InternalError(e.getMessage());
		}
		final AnalysisType[] analysisTypes =
			atValues.toArray(new AnalysisType[atValues.size()]);

		final List<Describable> analysisTypeList =
			new ArrayList<Describable>(Arrays.asList(analysisTypes));

		this.analysisComboBox.removeAllItems(); // XXX: это лучше делать при loggedOut
		this.analysisComboBox.addElements(analysisTypeList);
		//FIXME:ContextChangeEvent.LOGGED_IN_EVENT;
	}

	@SuppressWarnings("serial")
	private void createGUI() {

		this.analysisComboBox = new WrapperedComboBox<Describable>(DescribableWrapper.getInstance(),
			DescribableWrapper.COLUMN_DESCRIPTION,
			null);

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
				TestParametersPanel.this.setUseSetup(useSetup);
			}
		});
		this.patternPanel.add(this.useSetupsCheckBox, gbc);

		this.useAnalysisSetupsCheckBox = new JCheckBox(I18N.getString("Scheduler.Text.MeasurementParameter.WithAnalysisParameters"));
		this.useAnalysisSetupsCheckBox.addActionListener(new ActionListener() {
			@SuppressWarnings("unqualified-field-access")
			public void actionPerformed(final ActionEvent e) {
				boolean withAnalysis = ((JCheckBox)e.getSource()).isSelected();

				analysisComboBox.setEnabled(withAnalysis);
				if (!withAnalysis) {
					selectAnalysisType(null, false);
				}

				// XXX: если показываются не все доступные шаблоны, то список отображаемых шаблонов не меняем
				// (это вольный перевод квазианглоязычного комментария от bob'а)
				if (!allAvailableCheckBox.isSelected()) {
					return;
				}

				final WrapperedListModel<MeasurementSetup> wrapperedListModel = testSetups.getModel();
				MeasurementSetup selectedMeasurementSetup = (MeasurementSetup) testSetups.getSelectedValue();

				testSetups.clearSelection();

				wrapperedListModel.setElements(
						withAnalysis ? msListAnalysisOnly : msList);

				if (selectedMeasurementSetup != null) {
					testSetups.setSelectedValue(selectedMeasurementSetup, true);
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
				if (TestParametersPanel.this.currentEvent == null) {
					final Set<Test> tests = TestParametersPanel.this.schedulerModel.getSelectedTests();
					for (final Test test : tests) {
						if (test.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
							test.setAnalysisTypeId(analysisType == null ? Identifier.VOID_IDENTIFIER : analysisType.getId());
						}
					}
				}
			}
		});

		this.allAvailableCheckBox = new JCheckBox(
			I18N.getString("Scheduler.Text.MeasurementParameter.AllAvailableSetups"),
			false);

		this.allAvailableCheckBox.addActionListener(new ActionListener() {
			@SuppressWarnings("unqualified-field-access")
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
					if (measurementSetup != null) { 
					// XXX: зачем invokeLater? Вроде, все работает и при выполнении "прямо сейчас"
//					SwingUtilities.invokeLater(new Runnable() {
//						public void run() {
							try {
								TestParametersPanel.this.parametersTestPanel.setMeasurementTemplate(measurementSetup.getMeasurementTemplate());
							} catch (ApplicationException e1) {
								/* XXX: ApplicationException handling */
								Log.errorMessage(e1);
								throw new InternalError(e1.getMessage());
							}
//						}
//					});
					} else {
						/*
						 * если не выбран ни один шаблон, оставляем старые
						 * параметры, т.к. MonitoredElement пока еще не изменился
						 */
					}
				}

				if (TestParametersPanel.this.currentEvent != null) {
					return;
				}

				TestParametersPanel.this.currentEvent = e;

				if (measurementSetup != null) {
					final boolean analysisEnable = isAnalysisEnable(measurementSetup);
					if (!analysisEnable) {
						TestParametersPanel.this.analysisComboBox.setSelectedItem(null);
					}
					TestParametersPanel.this.analysisComboBox.setEnabled(analysisEnable);
					try {
						TestParametersPanel.this.schedulerModel.changeMeasurementSetup(measurementSetup);
					} catch (final ApplicationException e1) {
						AbstractMainFrame.showErrorMessage(TestParametersPanel.this.parametersTestPanel, e1);
					}
				}

				TestParametersPanel.this.currentEvent = null;
			}
		});

		/*
		 * Контекстное меню удаление шаблона (по RMB)
		 */
		this.testSetups.addMouseListener(new MouseAdapter() {
			private JPopupMenu createDeleteSetupPopupMenu(
					final MeasurementSetup ms) {
				final JPopupMenu popupMenu = new JPopupMenu();
				final JMenuItem deleteSetup = new JMenuItem(
						I18N.getString("Scheduler.Text.MeasurementParameter.deleteSetup")
						+ " " + ms.getDescription());
				popupMenu.add(deleteSetup);
				deleteSetup.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						deleteMeasurementSetup(ms);
					}
				});
				return popupMenu;
			}

			@Override
			public void mouseClicked(final MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					final WrapperedList source = (WrapperedList) e.getSource();
					final Object selectedValue = source.getSelectedValue();
					if (!(selectedValue instanceof MeasurementSetup)) {
						// not selected or not measurementSetup
						return;
					}
					if (selectedValue != null) {
						JPopupMenu deleteSetupPopupMenu =
							createDeleteSetupPopupMenu(
									(MeasurementSetup)selectedValue);
						deleteSetupPopupMenu.show(source, e.getX(), e.getY());
					}
				}
			}
		});

		final JScrollPane scroll = new JScrollPane(this.testSetups);
		scroll.setAutoscrolls(true);
		gbc.weighty = 1.0;
		this.patternPanel.add(scroll, gbc);
		gbc.weighty = 0.0;

		// @todo: доделать работу с descriptionField: название; отслеживать
		// изменение шаблона и пр.
		this.descriptionField = new JTextField(128);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.patternPanel.add(this.descriptionField, gbc);

		this.patternPanel.add(this.switchPanel, gbc);
		this.analysisComboBox.setEnabled(false);
		this.useSetupsCheckBox.doClick();
	}

	void deleteMeasurementSetup(MeasurementSetup setup) {
		StorableObjectCondition condition =
			new CompoundCondition(
					new LinkedIdsCondition(setup,
							ObjectEntities.TEST_CODE),
					CompoundConditionSort.AND,
					new TypicalCondition(
							TestStatus.TEST_STATUS_COMPLETED,
							OperationSort.OPERATION_NOT_EQUALS,
							ObjectEntities.TEST_CODE,
							TestWrapper.COLUMN_STATUS));

		final int count; 
		try {
			// XXX: На самом деле нам нужно только количество объектов, а не они сами.
			count = StorableObjectPool.getStorableObjectsByCondition(
					condition, true).size();
		} catch (ApplicationException e) {
			e.printStackTrace();
			AbstractMainFrame.showErrorMessage(I18N.getString("Error.CannotAcquireObject"));
			return;
		}

		if (count > 0) {
//		if (count > 0 && false) { // FIXME: && false
			JOptionPane.showMessageDialog(TestParametersPanel.this.testSetups,
					I18N.getString("Scheduler.Text.MeasurementParameter.deleteSetupImpossible.Text"),
			I18N.getString("Scheduler.Text.MeasurementParameter.deleteSetupImpossible.Title"),
			JOptionPane.ERROR_MESSAGE);
			return;
		} else {
			int result = JOptionPane.showConfirmDialog(TestParametersPanel.this.testSetups,
					I18N.getString("Scheduler.Text.MeasurementParameter.deleteSetupAck.Text")
						+ " '" + setup.getDescription() + "'?",
				I18N.getString("Scheduler.Text.MeasurementParameter.deleteSetupAck.Title"),
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			switch(result) {
			case JOptionPane.YES_OPTION:
				try {
					setup.dispose(LoginManager.getUserId());
					// бьем в бубен "вызов духа обновления модели"
					this.schedulerModel.planToolBar.refresh();
					this.schedulerModel.refreshMeasurementSetups();
					msListsToTestSetups(null);
				} catch (ApplicationException e) {
					Log.errorMessage(e);
					JOptionPane.showMessageDialog(TestParametersPanel.this.testSetups,
							I18N.getString("Scheduler.Text.MeasurementParameter.deleteSetupError.Text"),
							I18N.getString("Scheduler.Text.MeasurementParameter.deleteSetupError.Title"),
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				// шаблон удален
				break;
			default:
				// удаление отменено
				// do nothing
			}
		}
	}

	void setUseSetup(final boolean useSetup) {
		this.testSetups.setEnabled(useSetup);
		if (!useSetup) {
			if (this.useAnalysisSetupsCheckBox.isSelected()) {
				this.useAnalysisSetupsCheckBox.doClick();
			}
		}
		this.useAnalysisSetupsCheckBox.setEnabled(useSetup);
		this.patternsLabel.setEnabled(useSetup);
		this.analysisLabel.setEnabled(useSetup);
		this.allAvailableCheckBox.setEnabled(useSetup);

		this.descriptionField.setEnabled(!useSetup);
		if (this.parametersTestPanel != null) {
			this.parametersTestPanel.setEnableEditing(!useSetup);
		}
	}

	/**
	 * Возвращает текущее состояние analysisComboBox.
	 * Если analysisComboBox в неопределенном состоянии, возвращает null
	 * @return текущее состояние analysisComboBox либо null.
	 */
	private final AnalysisType getAnalysisType_orNull() {
		return (AnalysisType) this.analysisComboBox.getSelectedItem();
	}

	/**
	 * Возвращает существующий либо специально сгенерированный шаблон
	 * либо null.
	 *
	 * @todo если выбрано создание нового шаблона, то неизвестно,
	 * должен ли создать один шаблон и возвращать его каждый раз,
	 * или же каждый раз создавать новый. Пока что каждый раз создает новый.
	 * Учитывая, что галочка "использовать шаблон" автоматически выставляется
	 * при создании нового теста, то, быть может, текущее поведение приемлемо.
	 *
	 * @return шаблон либо null
	 */
	private MeasurementSetup getMeasurementSetup() {

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

		// создаем новый шаблон (по параметрам измерения, без анализа)
		try {
			if (this.parametersTestPanel == null) {
				return null;
			}
			final ActionTemplate<Measurement> measurementTemplate =
				this.parametersTestPanel.getMeasurementTemplate();
			return MeasurementSetup.createInstance(LoginManager.getUserId(),
					measurementTemplate.getMeasurementPortTypeId(),
					measurementTemplate.getId(),
					Identifier.VOID_IDENTIFIER,
					getDescription(),
					Collections.singleton(
							this.schedulerModel.getMonitoredElement().getId()));
		} catch (final CreateObjectException e) {
			this.schedulerModel.setBreakData();
			AbstractMainFrame.showErrorMessage(I18N.getString("Scheduler.Error.CannotCreateMeasurementSetup"));
			return null;
		}

	}

	// moved here from bob's ReflectometryTestPanel
	private final String getDescription() {
		final SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);

		final String description = this.descriptionField.getText();
		return description.trim().length() == 0 ?
//				commented according to bug 283
//				I18N.getString("Scheduler.Text.Scheduler.CreatedByScheduler") +
				sdf.format(new Date())
				: description;
	}

	// @todo -- saa
//	private void measurementParametersPanelUpdated() {
//		//getMeasurementTemplate;
//		// FIXME
//		try {
//			assert this.parametersTestPanel != null;
//
//			final ActionTemplate<Measurement> measurementTemplate =
//				this.parametersTestPanel.getMeasurementTemplate();
//
//			MeasurementSetup ms = this.schedulerModel.getMeasurementSetups();
//			if (ms != null && ms.getVersion() != StorableObjectVersion.INITIAL_VERSION) {
//				return; // refuse change; XXX: возможно, стоит активно вернуть GUI в исходное состояние?
//			}
//			ms.setMeasurementTemplateId(measurementTemplate.getId());
//			ms.setAnalysisTemplateId(Identifier.VOID_IDENTIFIER);
//			this.dispatcher.firePropertyChange(
//					new PropertyChangeEvent(this,
//						SchedulerModel.COMMAND_SET_MEASUREMENT_SETUP,
//						null,
//						ms));
//		} catch (final CreateObjectException e) {
//			this.schedulerModel.setBreakData();
//			AbstractMainFrame.showErrorMessage(I18N.getString("Scheduler.Error.CannotCreateMeasurementSetup"));
//		}
//	}

//	// unused?
//	public void refreshMeasurementSetup(final MeasurementSetup measurementSetup) {
//		final WrapperedListModel<MeasurementSetup> wrapperedListModel =
//			this.testSetups.getModel();
//
//		wrapperedListModel.sort();
//
//		this.setMeasurementSetup(measurementSetup, false);
//
//	}

	private void setMeasurementSetup(final MeasurementSetup measurementSetup,
	                                final boolean switchToSetups) {
		final boolean single = !this.allAvailableCheckBox.isSelected();

		this.testSetups.clearSelection();
		this.measurementSetupId = measurementSetup != null ? measurementSetup.getId() : Identifier.VOID_IDENTIFIER;
		if (measurementSetup == null || this.msList == null && !single) {
			return;
		}

		// AS-UNDERSTOOD: в режиме отображения одного шаблона делаем msList := только один шаблон.
		// AS-UNDERSTOOD: при этом msListAnalysisOnly и testSetups.getModel().elements не меняем - оставляем старыми. Доколе и зачем - не знаю
		if (single) {
			resetMsList();
			this.msList.add(measurementSetup);
		}

		try {
			// XXX: надо ли обязательно менять?
			this.changeMonitoredElement(measurementSetup.getMonitoredElementIds().iterator().next());
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}

		if (switchToSetups && !this.useSetupsCheckBox.isSelected()) {
			this.useSetupsCheckBox.doClick();
		}

		final boolean analysisSetupsSelected = this.useAnalysisSetupsCheckBox.isSelected();
		final boolean measurementSetupWithAnalysis = isAnalysisEnable(measurementSetup);

		try {
			if (analysisSetupsSelected &&
					this.schedulerModel.getSelectedTest().getAnalysisType() == null) {
				this.useAnalysisSetupsCheckBox.doClick();
			}
		} catch (final ApplicationException e) {
			Log.errorMessage(e);
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

		this.descriptionField.setText(null);
	}

	/**
	 * Переносит данные из msList/msListAnalysisOnly в this.testSetups.
	 * @param setup шаблон. Если не null, то в режиме "!allAvailable"
	 *   будет показан он, иначе в этом режиме будет пусто.
	 * В режиме "allAvailable" этот параметр игнорируется.
	 */
	private void msListsToTestSetups(MeasurementSetup setup) {
		final WrapperedListModel<MeasurementSetup> wrapperedListModel =
			this.testSetups.getModel();
		final boolean analysisSetupsSelected =
			this.useAnalysisSetupsCheckBox.isSelected();

		if (this.allAvailableCheckBox.isSelected()) {
			wrapperedListModel.setElements(analysisSetupsSelected
					? this.msListAnalysisOnly : this.msList);
		} else {
			final List<MeasurementSetup> set = new ArrayList<MeasurementSetup>(1);
			if (setup != null) {
				set.add(setup);
			}
			wrapperedListModel.setElements(set);
		}
	}

	boolean isAnalysisEnable(final MeasurementSetup measurementSetup) {
		return !measurementSetup.getAnalysisTemplateId().isVoid();
	}

	/**
	 * Устанавливает this.msList, this.msListAnalysisOnly и this.testSetups
	 * по данному набору шаблонов
	 * @param measurementSetups данный набор шаблонов
	 */
	private void setMeasurementSetups0(final Set<MeasurementSetup> measurementSetups) {
		Log.debugMessage(measurementSetups,
			Log.DEBUGLEVEL10);
		resetMsList();
		resetMsListAnalysisOnly();

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

		this.selectAnalysisType(this.getAnalysisType_orNull(), true);

		// FIXME: saa: зачем эта штука, не понимаю.
		// пытаемся сохранить текущий шаблон
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
			if (this.schedulerModel.getSelectedMeasurementType_orNull() != null
					|| this.schedulerModel.getSelectedMonitoredElement() != null) {
				new ProcessingDialogDummy(new Runnable() {

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
		this.parametersTestPanel = this.schedulerModel.getSchedulerHandler().getParametersTestPanel(codename);
		if (this.parametersTestPanel != null) {
			this.parametersTestPanel.setApplicationContext(this.aContext);
//			this.parametersTestPanel.setTestParametersPanel(this); // XXX: по-моему, эта ссылка не нужна. Потому ее и нету.
			this.parametersTestPanel.setMonitoredElement(me);
			this.switchPanel.add(this.parametersTestPanel, "");
			this.patternPanel.revalidate();
			this.setUseSetup(this.useSetupsCheckBox.isSelected());
		} else {
			Log.errorMessage("Port type codename '" + codename + "' is not supported");
			AbstractMainFrame.showErrorMessage(I18N.getString("Scheduler.Error.UnsupportedDeviceType"));
		}
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		this.currentEvent = evt;
		final String propertyName = evt.getPropertyName();
		final Object newValue = evt.getNewValue();
		if (propertyName == SchedulerModel.COMMAND_CHANGE_ME_TYPE) {
			final WrapperedListModel<MeasurementSetup> wrapperedListModel = this.testSetups.getModel();
			wrapperedListModel.removeAllElements();
			try {
				this.changeMonitoredElement((Identifier) evt.getNewValue());
			} catch (final ApplicationException e) {
				AbstractMainFrame.showErrorMessage(I18N.getString("Error.CannotAcquireObject"));
			}
		} else if (propertyName == SchedulerModel.COMMAND_SET_ANALYSIS_TYPE_OR_NULL) {
			this.selectAnalysisType((AnalysisType) newValue, true);
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
		} else if (propertyName == SchedulerModel.COMMAND_GET_ANALYSIS_TYPE_OR_NULL) {
			this.dispatcher.firePropertyChange(
				new PropertyChangeEvent(this,
					SchedulerModel.COMMAND_SET_ANALYSIS_TYPE_OR_NULL,
					null,
					this.getAnalysisType_orNull()));
		} else if (propertyName == SchedulerModel.COMMAND_GET_MEASUREMENT_SETUP) {
			MeasurementSetup measurementSetup1 = getMeasurementSetup();
			if (measurementSetup1 != null) {
				this.dispatcher.firePropertyChange(
					new PropertyChangeEvent(this,
						SchedulerModel.COMMAND_SET_MEASUREMENT_SETUP,
						null,
						measurementSetup1));
			}
		} else if (propertyName == ContextChangeEvent.TYPE) {
			final ContextChangeEvent cce = (ContextChangeEvent)evt;
			if (cce.isLoggedIn()) {
				loggedIn();
			}
		}

		this.currentEvent = null;
	}

	public JComponent getComponent() {
		return this.patternPanel;
	}

	/* что это за метод и кто его должен вызывать - не понятно */
	private void unregisterDispatcher() {
		this.dispatcher.removePropertyChangeListener(SchedulerModel.COMMAND_CHANGE_ME_TYPE, this);
	}

	private void initModule(final Dispatcher dispatcher1) {
		this.dispatcher = dispatcher1;
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_CHANGE_ME_TYPE, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_ANALYSIS_TYPE_OR_NULL, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_MEASUREMENT_SETUP, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_MEASUREMENT_SETUPS, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_MEASUREMENT_SETUP, this);

		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_ANALYSIS_TYPE_OR_NULL, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_MEASUREMENT_SETUP, this);

		// оказывается, тот диспетчер, что нам дают, не сообщает о логине-логауте
//		this.dispatcher.addPropertyChangeListener(ContextChangeEvent.TYPE, this);
		AbstractMainFrame.getGlobalDispatcher().addPropertyChangeListener(ContextChangeEvent.TYPE, this);
	}

	/**
	 * Выбирает в analysisComboBox требуемый тип анализа
	 * @param analysisType требуемый тип анализа or null
	 * @param changeStatus true, чтобы при необходимости включить также и
	 *   useAnalysisSetupsCheckBox
	 */
	synchronized void selectAnalysisType(final AnalysisType analysisType,
			final boolean changeStatus) {
		final JComboBox comboBox = this.analysisComboBox;
		comboBox.setSelectedItem(analysisType);
		AnalysisType selectedItem = (AnalysisType) comboBox.getSelectedItem();
		if (changeStatus && selectedItem != null
				&& !this.useAnalysisSetupsCheckBox.isSelected()) {
			this.useAnalysisSetupsCheckBox.doClick();
		}
	}

	private void resetMsList() {
		if (this.msList == null) {
			this.msList = new LinkedList<MeasurementSetup>();
		} else {
			this.msList.clear();
		}
	}

	private void resetMsListAnalysisOnly() {
		if (this.msListAnalysisOnly == null) {
			this.msListAnalysisOnly = new LinkedList<MeasurementSetup>();
		} else {
			this.msListAnalysisOnly.clear();
		}
	}

//	private void add
}
