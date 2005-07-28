/*
 * SaveParametersFrame.java Created on 17.05.2004 18:23:26
 *  
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Test;

/**
 * 
 * @version $Revision: 1.17 $, $Date: 2005/07/28 20:55:07 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module schedulerClone
 */
public class SaveParametersFrame extends JInternalFrame implements  PropertyChangeListener {

	private static final long	serialVersionUID	= 3257007652956746292L;

	private JPanel			panel;

	private JRadioButton	allResultsButton;
	private JRadioButton	recognizedEventsButton;
	private JRadioButton	measurementIdButton;

	SchedulerModel			schedulerModel;

	private Dispatcher	dispatcher;

	public SaveParametersFrame(ApplicationContext aContext) {
		this.init();
		if (aContext != null) {
			this.dispatcher = aContext.getDispatcher();
			
			this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();		

			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_TEST, this);			
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_RETURN_TYPE, this);
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_RETURN_TYPE, this);
		}
	}

	private void init() {
		setTitle(LangModelSchedule.getString("Saving_options")); //$NON-NLS-1$
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		this.panel = getPanel();
		setContentPane(this.panel);

	}

	private JPanel getPanel() {
		if (this.panel == null) {
			this.panel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			this.allResultsButton = new JRadioButton(LangModelSchedule.getString("AllTestResults")); //$NON-NLS-1$
			// this.allResultsButton.addActionListener(new
			// java.awt.event.ActionListener() {
			//
			// public void actionPerformed(ActionEvent e) {
			// // jButton1_actionPerformed(e);
			// }
			// });
			this.recognizedEventsButton = new JRadioButton(LangModelSchedule.getString("Only_recognized_events")); //$NON-NLS-1$
			// this.recognizedEventsButton.addActionListener(new
			// java.awt.event.ActionListener() {
			//
			// public void actionPerformed(ActionEvent e) {
			// // jButton2_actionPerformed(e);
			// }
			// });
			this.measurementIdButton = new JRadioButton(LangModelSchedule.getString("Only_Measurement_Id")); //$NON-NLS-1$
			// this.measurementIdButton.addActionListener(new
			// java.awt.event.ActionListener() {
			//
			// public void actionPerformed(ActionEvent e) {
			// // jButton3_actionPerformed(e);
			// }
			// });
			ButtonGroup group = new ButtonGroup();
			group.add(this.allResultsButton);
			group.add(this.recognizedEventsButton);
			group.add(this.measurementIdButton);

			ActionListener actionListener = new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					final Set<Identifier> selectedTestIds = SaveParametersFrame.this.schedulerModel.getSelectedTestIds();
					if (selectedTestIds != null && !selectedTestIds.isEmpty()) {
						try {
							final Set<Test> storableObjects = StorableObjectPool.getStorableObjects(selectedTestIds, true);
							for (final Test test : storableObjects) {
								if (test.isChanged()) {
									//Return type gone. Don't know, what to write here.
								}
							}
						} catch (ApplicationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				}
			};

			this.allResultsButton.addActionListener(actionListener);
			this.recognizedEventsButton.addActionListener(actionListener);
			this.measurementIdButton.addActionListener(actionListener);

			this.allResultsButton.setSelected(true);
			this.panel.add(this.allResultsButton, gbc);
			this.panel.add(this.recognizedEventsButton, gbc);
			this.panel.add(this.measurementIdButton, gbc);
			gbc.weighty = 1.0;
			this.panel.add(new JLabel(), gbc);
		}
		return this.panel;

	}

	public void propertyChange(PropertyChangeEvent evt) {
			final String propertyName = evt.getPropertyName();
			//Return type gone. Don't know, what to write here.
	}

}
