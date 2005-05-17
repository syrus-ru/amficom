/*
 * SaveParametersFrame.java Created on 17.05.2004 18:23:26
 *  
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.Client.Schedule.Commandable;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.util.Log;

/**
 * 
 * @version $Revision: 1.10 $, $Date: 2005/05/17 14:13:59 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module schedulerClone
 */
public class SaveParametersFrame extends JInternalFrame implements 
// ReturnTypeEditor, 
Commandable {

	private static final long	serialVersionUID	= 3257007652956746292L;

	private JPanel			panel;

	private JRadioButton	allResultsButton;
	private JRadioButton	recognizedEventsButton;
	private JRadioButton	measurementIdButton;
	private Command			command;

	SchedulerModel			schedulerModel;
	
	public SaveParametersFrame(ApplicationContext aContext) {
		// this.aContext = aContext;
		this.init();
		if (aContext != null) {
//			SchedulerModel schedulerModel = (SchedulerModel) aContext.getApplicationModel();
//			schedulerModel.setReturnTypeEditor(this);
			
			final Dispatcher dispatcher = aContext.getDispatcher();
			
			this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
			
			OperationListener listener = new OperationListener() {
				private boolean skip = false;
				
				public void operationPerformed(OperationEvent e) {
					String actionCommand = e.getActionCommand();
					Object obj = e.getSource();
					if (actionCommand.equals(SchedulerModel.COMMAND_SET_RETURN_TYPE)) {
						if (!this.skip) {
						setReturnType( (TestReturnType) obj);
						}
					} else if (actionCommand.equals(SchedulerModel.COMMAND_GET_RETURN_TYPE)) {
						TestReturnType testReturnType = getReturnType();
						if (testReturnType != null) {
							this.skip = true;
							dispatcher.notify(new OperationEvent(testReturnType, 0, SchedulerModel.COMMAND_SET_RETURN_TYPE));
							this.skip = false;
						}					
					} 
				}
			};

			dispatcher.register(listener, SchedulerModel.COMMAND_REFRESH_TEST);			
			dispatcher.register(listener, SchedulerModel.COMMAND_SET_RETURN_TYPE);
			dispatcher.register(listener, SchedulerModel.COMMAND_GET_RETURN_TYPE);
		}
		this.command = new WindowCommand(this);
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
					TestReturnType returnType = SaveParametersFrame.this.getReturnType();
					Log.debugMessage("SaveParametersFrame.refreshTestsReturnType | " + returnType.value(), Log.FINEST);

					java.util.Set selectedTestIds = SaveParametersFrame.this.schedulerModel.getSelectedTestIds();
					if (selectedTestIds != null && !selectedTestIds.isEmpty()) {
						try {
							java.util.Set storableObjects = MeasurementStorableObjectPool.getStorableObjects(
								selectedTestIds, true);
							for (Iterator iterator = storableObjects.iterator(); iterator.hasNext();) {
								Test test = (Test) iterator.next();
								if (test.isChanged()) {
									Log.debugMessage("SaveParametersFrame.refreshTestsReturnType | set " + test.getId()
											+ ", " + returnType.value(), Log.FINEST);
									test.setReturnType(returnType);
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

	public TestReturnType getReturnType() {
		TestReturnType returnType = TestReturnType.TEST_RETURN_TYPE_WHOLE;
		if (this.recognizedEventsButton.isSelected())
			returnType = TestReturnType.TEST_RETURN_TYPE_EVENTS;
		else if (this.measurementIdButton.isSelected())
			returnType = TestReturnType.TEST_RETURN_TYPE_REFERENCE;
		return returnType;
	}

	public void setReturnType(TestReturnType returnType) {		
		if (returnType.equals(TestReturnType.TEST_RETURN_TYPE_WHOLE)) {
			this.allResultsButton.doClick();
		} else if (returnType.equals(TestReturnType.TEST_RETURN_TYPE_EVENTS)) {
			this.recognizedEventsButton.doClick();
		} else if (returnType.equals(TestReturnType.TEST_RETURN_TYPE_REFERENCE)) {
			this.measurementIdButton.doClick();
		}
	}

	/**
	 * @return Returns the command.
	 */
	public Command getCommand() {
		return this.command;
	}
}
