/*-
 * $Id: MSChooserUI.java,v 1.4 2006/03/28 13:40:59 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.prediction.ui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.UI.WrapperedList;
import com.syrus.AMFICOM.client.UI.WrapperedListModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupWrapper;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.MonitoredElementWrapper;
import com.syrus.AMFICOM.newFilter.DateSpinner;
import com.syrus.AMFICOM.newFilter.TimeSpinner;
import com.syrus.util.Log;

public final class MSChooserUI {
	private static final long serialVersionUID = -5870932369028340654L;
	private static JPanel mainPanel;
	static JDialog	dialog;
	static WrapperedComboBox<MonitoredElement> cmbMeCombo = new WrapperedComboBox<MonitoredElement>(
			MonitoredElementWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME, 
			StorableObjectWrapper.COLUMN_ID);
	static WrapperedList<MeasurementSetup> lsMsList = new WrapperedList<MeasurementSetup>(
			MeasurementSetupWrapper.getInstance(), StorableObjectWrapper.COLUMN_DESCRIPTION, 
			StorableObjectWrapper.COLUMN_ID);
	static DateSpinner spnStartDateSpinner = new DateSpinner();
	static TimeSpinner spnStartTimeSpinner = new TimeSpinner();
	static DateSpinner spnEndDateSpinner = new DateSpinner();
	static TimeSpinner spnEndTimeSpinner = new TimeSpinner();
	static JButton okButton = new JButton(I18N.getString("Common.Button.OK"));
	static JButton cancelButton = new JButton(I18N.getString("Common.Button.Cancel"));
	
	static int retCode;
	
	public MSChooserUI(Frame frame) {
		initUI();
				
		if (dialog == null) {
			JOptionPane optionPane = 
				new JOptionPane(mainPanel, 
						JOptionPane.PLAIN_MESSAGE, 
						JOptionPane.OK_CANCEL_OPTION,
						null, 
						new Object[] { 
						okButton, 
						cancelButton}, 
						null);
			dialog = optionPane.createDialog(frame, I18N.getString("Title.measurementLoad"));
			dialog.setModal(true);
			dialog.setResizable(true);
		}
		retCode = Command.RESULT_CANCEL;
		dialog.setVisible(true);
	}

	@SuppressWarnings("unused")
	private void initUI() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			
			JLabel lbStartDateLabel = new JLabel(I18N.getString("Label.startDate"));
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			spnStartTimeSpinner.setValue(calendar.getTime());
			spnEndTimeSpinner.setValue(calendar.getTime());
			spnStartDateSpinner.setValue(calendar.getTime());
			calendar.roll(Calendar.DAY_OF_MONTH, 1);
			spnEndDateSpinner.setValue(calendar.getTime());

			JLabel lbEndDateLabel = new JLabel(I18N.getString("Label.endDate"));
			JLabel lbDateLabel = new JLabel(I18N.getString("Label.measurementDate"));
			JLabel lbMeLabel = new JLabel(I18N.getString("Label.monitoredElement"));
			JLabel lbMsLabel = new JLabel(I18N.getString("Label.measurementSetup"));
			JButton btRefreshMsBut = new JButton();
			
			GridBagLayout gbPanel0 = new GridBagLayout();
			GridBagConstraints gbcPanel0 = new GridBagConstraints();
			mainPanel.setLayout( gbPanel0 );
			
			gbcPanel0.gridx = 0;
			gbcPanel0.gridy = 3;
			gbcPanel0.gridwidth = 2;
			gbcPanel0.gridheight = 1;
			gbcPanel0.fill = GridBagConstraints.BOTH;
			gbcPanel0.weightx = 0;
			gbcPanel0.weighty = 0;
			gbcPanel0.anchor = GridBagConstraints.NORTH;
			gbPanel0.setConstraints( lbStartDateLabel, gbcPanel0 );
			mainPanel.add( lbStartDateLabel );
			
			gbcPanel0.gridx = 2;
			gbcPanel0.gridy = 3;
			gbcPanel0.gridwidth = 3;
			gbcPanel0.gridheight = 1;
			gbcPanel0.fill = GridBagConstraints.BOTH;
			gbcPanel0.weightx = 1;
			gbcPanel0.weighty = 0;
			gbcPanel0.anchor = GridBagConstraints.NORTH;
			gbPanel0.setConstraints( spnStartDateSpinner, gbcPanel0 );
			mainPanel.add( spnStartDateSpinner );
			
			gbcPanel0.gridx = 0;
			gbcPanel0.gridy = 4;
			gbcPanel0.gridwidth = 2;
			gbcPanel0.gridheight = 1;
			gbcPanel0.fill = GridBagConstraints.BOTH;
			gbcPanel0.weightx = 0;
			gbcPanel0.weighty = 0;
			gbcPanel0.anchor = GridBagConstraints.NORTH;
			gbPanel0.setConstraints( lbEndDateLabel, gbcPanel0 );
			mainPanel.add( lbEndDateLabel );
			
			gbcPanel0.gridx = 2;
			gbcPanel0.gridy = 4;
			gbcPanel0.gridwidth = 3;
			gbcPanel0.gridheight = 1;
			gbcPanel0.fill = GridBagConstraints.BOTH;
			gbcPanel0.weightx = 1;
			gbcPanel0.weighty = 0;
			gbcPanel0.anchor = GridBagConstraints.NORTH;
			gbPanel0.setConstraints( spnEndDateSpinner, gbcPanel0 );
			mainPanel.add( spnEndDateSpinner );
			
			gbcPanel0.gridx = 0;
			gbcPanel0.gridy = 2;
			gbcPanel0.gridwidth = 6;
			gbcPanel0.gridheight = 1;
			gbcPanel0.fill = GridBagConstraints.BOTH;
			gbcPanel0.weightx = 0;
			gbcPanel0.weighty = 0;
			gbcPanel0.anchor = GridBagConstraints.NORTH;
			gbPanel0.setConstraints( lbDateLabel, gbcPanel0 );
			mainPanel.add( lbDateLabel );
			
			gbcPanel0.gridx = 0;
			gbcPanel0.gridy = 0;
			gbcPanel0.gridwidth = 6;
			gbcPanel0.gridheight = 1;
			gbcPanel0.fill = GridBagConstraints.BOTH;
			gbcPanel0.weightx = 0;
			gbcPanel0.weighty = 0;
			gbcPanel0.anchor = GridBagConstraints.NORTH;
			gbPanel0.setConstraints( lbMeLabel, gbcPanel0 );
			mainPanel.add( lbMeLabel );
			
			gbcPanel0.gridx = 0;
			gbcPanel0.gridy = 1;
			gbcPanel0.gridwidth = 7;
			gbcPanel0.gridheight = 1;
			gbcPanel0.fill = GridBagConstraints.BOTH;
			gbcPanel0.weightx = 1;
			gbcPanel0.weighty = 0;
			gbcPanel0.anchor = GridBagConstraints.NORTH;
			gbPanel0.setConstraints( cmbMeCombo, gbcPanel0 );
			mainPanel.add( cmbMeCombo );
			
			gbcPanel0.gridx = 0;
			gbcPanel0.gridy = 5;
			gbcPanel0.gridwidth = 6;
			gbcPanel0.gridheight = 1;
			gbcPanel0.fill = GridBagConstraints.BOTH;
			gbcPanel0.weightx = 1;
			gbcPanel0.weighty = 0;
			gbcPanel0.anchor = GridBagConstraints.NORTH;
			gbPanel0.setConstraints( lbMsLabel, gbcPanel0 );
			mainPanel.add( lbMsLabel );
			
			gbcPanel0.gridx = 6;
			gbcPanel0.gridy = 5;
			gbcPanel0.gridwidth = 1;
			gbcPanel0.gridheight = 1;
			gbcPanel0.fill = GridBagConstraints.BOTH;
			gbcPanel0.weightx = 0;
			gbcPanel0.weighty = 0;
			gbcPanel0.anchor = GridBagConstraints.NORTH;
			gbPanel0.setConstraints( btRefreshMsBut, gbcPanel0 );
			mainPanel.add( btRefreshMsBut );
			
			JScrollPane jspMsList = new JScrollPane(lsMsList);
			gbcPanel0.gridx = 0;
			gbcPanel0.gridy = 6;
			gbcPanel0.gridwidth = 7;
			gbcPanel0.gridheight = 1;
			gbcPanel0.fill = GridBagConstraints.BOTH;
			gbcPanel0.weightx = 1;
			gbcPanel0.weighty = 1;
			gbcPanel0.anchor = GridBagConstraints.NORTH;
			gbPanel0.setConstraints( jspMsList, gbcPanel0 );
			mainPanel.add( jspMsList );
			
			gbcPanel0.gridx = 5;
			gbcPanel0.gridy = 3;
			gbcPanel0.gridwidth = 2;
			gbcPanel0.gridheight = 1;
			gbcPanel0.fill = GridBagConstraints.BOTH;
			gbcPanel0.weightx = 1;
			gbcPanel0.weighty = 0;
			gbcPanel0.anchor = GridBagConstraints.NORTH;
			gbPanel0.setConstraints( spnStartTimeSpinner, gbcPanel0 );
			mainPanel.add( spnStartTimeSpinner );
			
			gbcPanel0.gridx = 5;
			gbcPanel0.gridy = 4;
			gbcPanel0.gridwidth = 2;
			gbcPanel0.gridheight = 1;
			gbcPanel0.fill = GridBagConstraints.BOTH;
			gbcPanel0.weightx = 1;
			gbcPanel0.weighty = 0;
			gbcPanel0.anchor = GridBagConstraints.NORTH;
			gbPanel0.setConstraints( spnEndTimeSpinner, gbcPanel0 );
			mainPanel.add( spnEndTimeSpinner );
			
			btRefreshMsBut.setToolTipText(I18N.getString("Button.refresh"));
			btRefreshMsBut.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
			btRefreshMsBut.setFocusPainted(false);
			btRefreshMsBut.setIcon(UIManager.getIcon(ResourceKeys.ICON_REFRESH));
			btRefreshMsBut.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					try {
						final MonitoredElement me1 = (MonitoredElement)cmbMeCombo.getSelectedItem();
						if (me1 != null) {
							final LinkedIdsCondition condition1 = new LinkedIdsCondition(me1.getId(), ObjectEntities.MEASUREMENTSETUP_CODE);
							final Set<MeasurementSetup> mSetups = StorableObjectPool.getStorableObjectsByCondition(
									condition1, true);
							final WrapperedListModel<MeasurementSetup> model = lsMsList.getModel();
							model.setElements(mSetups);
						}
					} catch (ApplicationException e1) {
						Log.errorMessage(e1);
					}
				}
			});
			okButton.setEnabled(false);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					retCode = Command.RESULT_OK;
					dialog.dispose();
				}
			});
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					retCode = Command.RESULT_CANCEL;
					MSChooserUI.dialog.dispose();
				}
			});
			lsMsList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (e.getValueIsAdjusting()) {
						return;
					}
					okButton.setEnabled(lsMsList.getSelectedIndex() != -1);
				}
			});
			
			try {
				LinkedIdsCondition condition1 = new LinkedIdsCondition(LoginManager.getDomainId(), ObjectEntities.MONITOREDELEMENT_CODE);
				Set<MonitoredElement> mes = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
				((WrapperedListModel<MonitoredElement>)cmbMeCombo.getModel()).setElements(mes);
			} catch (ApplicationException e1) {
				Log.errorMessage(e1);
			}
		}
	}
	
	static Date combineDates(Date date, Date time) {
		final Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);
		final Calendar timeCalendar = Calendar.getInstance();
		timeCalendar.setTime(time);
		dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
		dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
		return dateCalendar.getTime();
	}

	public Date getFromDate() {
		return combineDates((Date)spnStartDateSpinner.getValue(), 
				(Date)spnStartTimeSpinner.getValue());
	}

	public Date getToDate() {
		return combineDates((Date)spnEndDateSpinner.getValue(), 
				(Date)spnEndTimeSpinner.getValue());
	}
	
	public MonitoredElement getMonitoredElement() {
		return (MonitoredElement)cmbMeCombo.getSelectedItem();
	}

	public MeasurementSetup getMeasurementSetup() {
		return (MeasurementSetup)lsMsList.getSelectedValue();
	}

	public int getRetCode() {
		return retCode;
	}
}
