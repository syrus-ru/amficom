package com.syrus.AMFICOM.client.observer.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.UI.WrapperedTable;
import com.syrus.AMFICOM.client.UI.WrapperedTableModel;
import com.syrus.AMFICOM.client.event.MarkerEvent;
import com.syrus.AMFICOM.client.event.EventReceiver;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.observer.ObserverMainFrame;
import com.syrus.AMFICOM.client.observer.alarm.Alarm;
import com.syrus.AMFICOM.client.observer.alarm.AlarmConditionWrapper;
import com.syrus.AMFICOM.client.observer.alarm.AlarmWrapper;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Action;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.AMFICOM.newFilter.Filter;
import com.syrus.AMFICOM.resource.LangModelObserver;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;
import com.syrus.util.Wrapper;

public class AlarmFrame extends JInternalFrame {
	private static final long serialVersionUID = 516953186269837809L;

	ApplicationContext aContext;

	boolean initial_init = true;

	private Wrapper<Alarm> wrapper;
	WrapperedTableModel<Alarm> model;
	WrapperedTable table;

	JPanel actionPanel = new JPanel();
	JButton buttonAcknowledge = new JButton();
	JButton buttonFix = new JButton();
	JButton buttonDelete = new JButton();
	JButton filterButton = new JButton();
	JButton buttonDescribe = new JButton();
	JButton buttonClose = new JButton();
	JButton buttonRefresh = new JButton();

	boolean perform_processing = true;

	Filter filter = new Filter(new AlarmConditionWrapper());
	Map<Identifier, Identifier> alarmMarkerMapping = new HashMap<Identifier, Identifier>();

	private final class AlarmUpdater implements EventReceiver {
		AlarmUpdater() {
			final ClientSessionEnvironment clientSessionEnvironment = ClientSessionEnvironment.getInstance();
			clientSessionEnvironment.addEventReceiver(this);
		}
	
		/**
		 * @param event
		 * @see EventReceiver#receiveEvent(Event)
		 */
		public void receiveEvent(final Event<?> event) {
			if (!(event instanceof PopupNotificationEvent)) {
				return;
			}

			@SuppressWarnings("unchecked")
			final PopupNotificationEvent popupNotificationEvent = (PopupNotificationEvent) event;

			Alarm alarm = new Alarm(popupNotificationEvent);
			AlarmFrame.this.model.addObject(alarm);
			
			Identifier resultId = popupNotificationEvent.getResultId();
			Identifier peId = popupNotificationEvent.getAffectedPathElementId();
			double optDistance = popupNotificationEvent.getMismatchOpticalDistance();
			
			try {
				Result result = StorableObjectPool.getStorableObject(resultId, true);
				if (result.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
					final Action action = result.getAction();
					Measurement m = (Measurement) action;
				
					Identifier meId = m.getMonitoredElementId();
					PathElement pe = StorableObjectPool.getStorableObject(peId, true);
					SchemePath path = null;
					
					if (pe == null) {
						MonitoredElement me = StorableObjectPool.getStorableObject(meId, true);
						Set<Identifier> tpathIds = me.getMonitoredDomainMemberIds();
						
						if (!tpathIds.isEmpty()) {
							Set<SchemePath> schemePaths = StorableObjectPool.getStorableObjectsByCondition(
									new LinkedIdsCondition(tpathIds.iterator().next(), ObjectEntities.SCHEMEPATH_CODE), true);
							
							if (!schemePaths.isEmpty()) {
								path = schemePaths.iterator().next();
								pe = path.getPathElementByOpticalDistance(optDistance);
							}
						}
					} else {
						path = pe.getParentPathOwner();
					}
					
					Identifier markerId = LocalIdentifierGenerator.generateIdentifier(ObjectEntities.MARK_CODE);
					AlarmFrame.this.alarmMarkerMapping.put(alarm.getId(), markerId);
					MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.ALARMMARKER_CREATED_EVENT,
							markerId, optDistance, path.getId(), meId, pe.getId());

					AlarmFrame.this.aContext.getDispatcher().firePropertyChange(mEvent);
					
					AlarmFrame.this.table.setSelectedValue(alarm);
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		}
	}
	
	public AlarmFrame(ApplicationContext aContext) {
		this.wrapper = new AlarmWrapper();
		this.model = new WrapperedTableModel<Alarm>(
				this.wrapper,
				new String[] {
						AlarmWrapper.COLUMN_DATE,
						AlarmWrapper.COLUMN_OPTICAL_DISTANCE,
						AlarmWrapper.COLUMN_PHYSICAL_DISTANCE,
						AlarmWrapper.COLUMN_ELEMENT,
						AlarmWrapper.COLUMN_PATH });
		this.table = new WrapperedTable<Alarm>(this.model);
		
		this.table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				
				int n = AlarmFrame.this.table.getSelectedRow();
				int first = e.getFirstIndex();
				int last = e.getLastIndex();
				boolean firstb = AlarmFrame.this.table.getSelectionModel().isSelectedIndex(first);
				boolean lastb = AlarmFrame.this.table.getSelectionModel().isSelectedIndex(last);
				boolean b = n != -1;
				AlarmFrame.this.buttonDelete.setEnabled(b);
				AlarmFrame.this.buttonAcknowledge.setEnabled(b);
				AlarmFrame.this.buttonFix.setEnabled(b);
				AlarmFrame.this.buttonClose.setEnabled(b);
				AlarmFrame.this.buttonDescribe.setEnabled(b);
				
				try {
					Alarm alarm1 = AlarmFrame.this.model.getObject(first);
					Alarm alarm2 = AlarmFrame.this.model.getObject(last);
					
					final ApplicationContext aContext1 = AlarmFrame.this.aContext;
					if (!firstb) {
						Identifier markerId = AlarmFrame.this.alarmMarkerMapping.get(alarm1.getId());
						MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.MARKER_DELETED_EVENT,
								markerId, alarm1.getEvent().getMismatchOpticalDistance(), alarm1.getPath().getId(), 
								alarm1.getMonitoredElement().getId(), alarm1.getPathElement().getId());

						aContext1.getDispatcher().firePropertyChange(mEvent);
					}
					if (!lastb && first != last) {
						Identifier markerId = AlarmFrame.this.alarmMarkerMapping.get(alarm2.getId());
						MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.MARKER_DELETED_EVENT,
								markerId, alarm2.getEvent().getMismatchOpticalDistance(), alarm2.getPath().getId(), 
								alarm2.getMonitoredElement().getId(), alarm2.getPathElement().getId());

						aContext1.getDispatcher().firePropertyChange(mEvent);
					}
					
					if (firstb) {
						Identifier markerId = AlarmFrame.this.alarmMarkerMapping.get(alarm1.getId());
						MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.ALARMMARKER_CREATED_EVENT,
								markerId, alarm1.getEvent().getMismatchOpticalDistance(), alarm1.getPath().getId(), 
								alarm1.getMonitoredElement().getId(), alarm1.getPathElement().getId());

						// notify about measurement
						aContext1.getDispatcher().firePropertyChange(
								new ObjectSelectedEvent(this, alarm1.getMeasurement(), null, ObjectSelectedEvent.MEASUREMENT));

						aContext1.getDispatcher().firePropertyChange(mEvent);
					} 
					if (lastb && first != last) {
						Identifier markerId = AlarmFrame.this.alarmMarkerMapping.get(alarm2.getId());
						MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.ALARMMARKER_CREATED_EVENT,
								markerId, alarm2.getEvent().getMismatchOpticalDistance(), alarm2.getPath().getId(), 
								alarm2.getMonitoredElement().getId(), alarm2.getPathElement().getId());
						
						// notify about measurement
						aContext1.getDispatcher().firePropertyChange(
								new ObjectSelectedEvent(this, alarm1.getMeasurement(), null, ObjectSelectedEvent.MEASUREMENT));
						
						aContext1.getDispatcher().firePropertyChange(mEvent);
					} 
				} catch (ApplicationException e1) {
					Log.errorMessage(e1);
				}
			}
		});

		this.initUI();

		new AlarmUpdater();
		setContext(aContext);
	}

	private void initUI() {
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		this.setResizable(true);
		this.setName(ObserverMainFrame.ALARM_FRAME);

		this.setTitle(LangModelObserver.getString("title.alarm_frame"));
		this.getContentPane().setLayout(new BorderLayout());
		this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				grabFocus();
			}

			@Override
			public void internalFrameOpened(InternalFrameEvent e) {
				grabFocus();
			}
		});
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				if(AlarmFrame.this.initial_init)
					init_module();
			}
		});

		this.table.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		this.actionPanel.setLayout(new FlowLayout());
		this.buttonAcknowledge.setText("Подтвердить");
		this.buttonAcknowledge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonAcknowledge_actionPerformed(e);
			}
		});
		this.buttonFix.setText("Снять");
		this.buttonFix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonFix_actionPerformed(e);
			}
		});
		this.buttonClose.setText("Закрыть");
		this.buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		this.buttonRefresh.setText("Обновить");
		this.buttonRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonRefresh_actionPerformed(e);
			}
		});
		this.buttonDelete.setText("Удалить");
		this.buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonDelete_actionPerformed(e);
			}
		});
		this.filterButton.setText("Фильтр");
		this.filterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filterButton_actionPerformed(e);
			}
		});
		this.buttonDescribe.setText("Описание");
		this.buttonDescribe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonDescribe_actionPerformed(e);
			}
		});

		// this.getContentPane().add(toolBar, BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(this.table);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		this.actionPanel.add(this.buttonAcknowledge);
		this.actionPanel.add(this.buttonFix);
		this.actionPanel.add(this.buttonDelete);
//		this.actionPanel.add(this.filterButton);
		this.actionPanel.add(this.buttonDescribe);
//		this.actionPanel.add(this.buttonClose);
//		this.actionPanel.add(this.buttonRefresh);
		this.getContentPane().add(this.actionPanel, BorderLayout.SOUTH);
	}

	public void init_module() {
		this.initial_init = false;
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.fireModelChanged();

		updateContents();

		this.buttonAcknowledge.setEnabled(false);
		this.buttonFix.setEnabled(false);
		this.buttonDelete.setEnabled(false);
		this.buttonDescribe.setEnabled(false);
	}

	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public ApplicationContext getContext() {
		return this.aContext;
	}

	public ApplicationModel getModel() {
		return this.aContext.getApplicationModel();
	}

	public void updateContents() {
		
//		try {
			// TODO use ObjectEntities.ALARM_CODE
//			StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.UPDIKE_CODE);
//			Set<Alarm> alarms = StorableObjectPool.<Alarm>getStorableObjectsByCondition(condition , true);
//			this.model.setValues(alarms);

			this.buttonAcknowledge.setEnabled(false);
			this.buttonFix.setEnabled(false);
			this.buttonDelete.setEnabled(false);
			this.buttonDescribe.setEnabled(false);
//		} catch(ApplicationException e) {
//			e.printStackTrace();
//		}
	}

	void buttonRefresh_actionPerformed(ActionEvent e) {
		updateContents();
	}

	void buttonDescribe_actionPerformed(ActionEvent e) {
		int selected = this.table.getSelectedRow();
		if (selected != -1) {
			Alarm alarm = this.model.getObject(selected);
		
		JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
				alarm.getEvent().getMessage(), 
				LangModelObserver.getString("Message.information"), 
				JOptionPane.INFORMATION_MESSAGE);
		}
/*
		AlarmDescriptor ad = null;
		if(alarm.type_id.equals("rtutestalarm")
				|| alarm.type_id.equals("rtutestwarning")) {
			ad = new OpticalAlarmDescriptor(alarm);
		}
		if(ad != null) {
			String name;

			try {
				MonitoredElement me = (MonitoredElement) ConfigurationStorableObjectPool
						.getStorableObject(alarm.getMonitoredElementId(), true);
				name = me.getName();
			} catch(ApplicationException ex) {
				name = "' '";
			}

			AlarmPopupFrame f = new AlarmPopupFrame(
					"Отклонение в " + name + " возникло "
							+ sdf.format(new Date(ad.getAlarmTime())),
					alarm,
					aContext);

			aContext.getDispatcher().notify(
					new OperationEvent(
							f,
							0,
							SurveyEvent.ALARM_POPUP_FRAME_DISPLAYED));

			Container desktop = this.getParent();
			f.setLocation(this.getLocation());
			f.setSize(this.getSize());
			f.set(ad);
			desktop.add(f);
			f.show();
			f.toFront();
		}
*/
	}

	void buttonAcknowledge_actionPerformed(ActionEvent e) {
		int mini = this.table.getSelectionModel().getMinSelectionIndex();
		int maxi = this.table.getSelectionModel().getMaxSelectionIndex();
		Alarm[] alarms = new Alarm[maxi - mini + 1];
		for(int i = 0; i + mini < maxi + 1; i++)
			alarms[i] = (Alarm) this.model.getObject(i + mini);
/*
		for(int i = 0; i < alarms.length; i++) {
			alarms[i].status = AlarmStatus.ALARM_STATUS_ASSIGNED;
			alarms[i].assigned = System.currentTimeMillis();
			alarms[i].assigned_to = aContext.getSessionInterface().getUserId();
			aContext.getDataSourceInterface().SetAlarm(alarms[i].getId());
		}
		updateContents();
*/
	}

	void buttonFix_actionPerformed(ActionEvent e) {
		try {
			Alarm alarm = this.model.getObject(this.table.getSelectedRow());
			MarkerEvent mEvent2 = new MarkerEvent(this, MarkerEvent.MARKER_DELETED_EVENT,
					this.alarmMarkerMapping.get(alarm.getId()), 
					alarm.getEvent().getMismatchOpticalDistance(),
					alarm.getPath().getId(), alarm.getMonitoredElement().getId(),
					alarm.getPathElement().getId());
			this.table.setSelectedValue(null);
			this.aContext.getDispatcher().firePropertyChange(mEvent2);
		} catch (ApplicationException e1) {
			Log.errorMessage(e1);
		}
	}

	void buttonDelete_actionPerformed(ActionEvent e) {
		try {
			Alarm alarm = this.model.getObject(this.table.getSelectedRow());
			MarkerEvent mEvent2 = new MarkerEvent(this, MarkerEvent.MARKER_DELETED_EVENT,
					this.alarmMarkerMapping.get(alarm.getId()), 
					alarm.getEvent().getMismatchOpticalDistance(),
					alarm.getPath().getId(), alarm.getMonitoredElement().getId(),
					alarm.getPathElement().getId());
			this.aContext.getDispatcher().firePropertyChange(mEvent2);
			AlarmFrame.this.alarmMarkerMapping.remove(alarm.getId());
			
			this.table.setSelectedValue(null);
			this.model.removeObject(alarm);
		} catch (ApplicationException e1) {
			Log.errorMessage(e1);
		}

		updateContents();
/*
		for(int i = 0; i < alarms.length; i++) {
			alarms[i].status = AlarmStatus.ALARM_STATUS_DELETED;
			alarms[i].fixed_when = System.currentTimeMillis();
			alarms[i].fixed_by = aContext.getSessionInterface().getUserId();
			aContext.getDataSourceInterface().SetAlarm(alarms[i].getId());
		}
		updateContents();
*/
	}

	void filterButton_actionPerformed(ActionEvent e) {
/*
		AlarmFilter orf = (AlarmFilter) filter.clone();
		FilterDialog dlg = new FilterDialog(orf, aContext);
		dlg.pack();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dlg.getSize();
		frameSize.width = 450;
		frameSize.height = frameSize.height + 20;
		dlg.setSize(frameSize);

		if(frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if(frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dlg.setLocation(
				(screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);

		dlg.setModal(true);
		dlg.setVisible(true);

		if(dlg.retcode == 1) {
			filter = orf;
			updateContents();
		}
*/
	}
}
