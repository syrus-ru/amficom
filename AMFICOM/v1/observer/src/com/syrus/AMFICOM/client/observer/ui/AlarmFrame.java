package com.syrus.AMFICOM.client.observer.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.Marker;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.UI.WrapperedTable;
import com.syrus.AMFICOM.client.UI.WrapperedTableModel;
import com.syrus.AMFICOM.client.event.MarkerEvent;
import com.syrus.AMFICOM.client.event.PopupMessageReceiver;
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
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
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

public class AlarmFrame extends JInternalFrame implements
		PropertyChangeListener {
	ApplicationContext aContext;

	boolean initial_init = true;

	private Wrapper<Alarm> wrapper;
	WrapperedTableModel<Alarm> model;
	private WrapperedTable table;

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
	AlarmUpdater updater;
	Map<Alarm, Marker> alarmMarkerMapping = new HashMap<Alarm, Marker>();

	class AlarmUpdater implements PopupMessageReceiver {
		AlarmUpdater() {
			final ClientSessionEnvironment clientSessionEnvironment = ClientSessionEnvironment.getInstance();
			clientSessionEnvironment.addPopupMessageReceiver(this);
		}
	
		public void receiveMessage(Event event) {
			PopupNotificationEvent popupNotificationEvent = (PopupNotificationEvent)event;
			Alarm alarm = new Alarm(popupNotificationEvent);
			AlarmFrame.this.model.addObject(alarm);
			
			Identifier resultId = popupNotificationEvent.getResultId();
			double optDistance = popupNotificationEvent.getMismatchOpticalDistance();
			
			try {
				Result result = StorableObjectPool.getStorableObject(resultId, true);
				if (result.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
					final Action action = result.getAction();
					Measurement m = (Measurement) action;
					
					// notify about measurement
					AlarmFrame.this.aContext.getDispatcher().firePropertyChange(
							new ObjectSelectedEvent(this, m, null, ObjectSelectedEvent.MEASUREMENT));
					
					Identifier meId = m.getMonitoredElementId();
					MonitoredElement me = StorableObjectPool.getStorableObject(meId, true);
					Set<Identifier> tpathIds = me.getMonitoredDomainMemberIds();

					if (!tpathIds.isEmpty()) {
						Set<SchemePath> schemePaths = StorableObjectPool.getStorableObjectsByCondition(
								new LinkedIdsCondition(tpathIds.iterator().next(), ObjectEntities.SCHEMEPATH_CODE), true);
						
						if (!schemePaths.isEmpty()) {
							SchemePath path = schemePaths.iterator().next();
							PathElement pe = path.getPathElementByOpticalDistance(optDistance);

							Marker marker = new Marker("", 0);
							MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.ALARMMARKER_CREATED_EVENT,
									marker.getId(), optDistance, path.getId(), meId, pe.getId());

							AlarmFrame.this.alarmMarkerMapping.put(alarm, marker);
							AlarmFrame.this.aContext.getDispatcher().firePropertyChange(mEvent);
						}
					}
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
						AlarmWrapper.COLUMN_ELEMENT,
						AlarmWrapper.COLUMN_PATH });
		this.table = new WrapperedTable<Alarm>(this.model);
		
		this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				
				boolean b = e.getFirstIndex() != -1;
				AlarmFrame.this.buttonDelete.setEnabled(b);
				AlarmFrame.this.buttonAcknowledge.setEnabled(b);
				AlarmFrame.this.buttonFix.setEnabled(b);
				AlarmFrame.this.buttonClose.setEnabled(b);
				AlarmFrame.this.buttonDescribe.setEnabled(b);

			}
		});

		this.initUI();

		this.updater = new AlarmUpdater();
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
		this.table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						alarmTable_valueChanged(e);
					}
				});

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
		this.getContentPane().add(this.table, BorderLayout.CENTER);
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
		aContext.getDispatcher().addPropertyChangeListener("alarmreceived", this);
	}

	public ApplicationContext getContext() {
		return this.aContext;
	}

	public ApplicationModel getModel() {
		return this.aContext.getApplicationModel();
	}

	public void propertyChange(PropertyChangeEvent pce) {
		if(pce.getPropertyName().equals("alarmreceived")) {
			updateContents();
		}
	}

	public void updateContents() {
		
		try {
			// TODO use ObjectEntities.ALARM_CODE
			StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.UPDIKE_CODE);
			Set<Alarm> alarms = StorableObjectPool.<Alarm>getStorableObjectsByCondition(condition , true);
			this.model.setValues(alarms);

			this.buttonAcknowledge.setEnabled(false);
			this.buttonFix.setEnabled(false);
			this.buttonDelete.setEnabled(false);
			this.buttonDescribe.setEnabled(false);
		} catch(ApplicationException e) {
			e.printStackTrace();
		}
	}

	public void alarmTable_valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting())
			return;

		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if(lsm.isSelectionEmpty()) {
			this.buttonAcknowledge.setEnabled(false);
			this.buttonFix.setEnabled(false);
			this.buttonDelete.setEnabled(false);
			this.buttonDescribe.setEnabled(false);
			// no rows are selected
		}
		else {
			Alarm alarm = (Alarm) this.model.getObject(this.table.getSelectedRow());
/*
			Pool.put("activecontext", "useractionselected", "alarm_selected");
			Pool.put("activecontext", "selected_id", alarm.getId());

			if(aContext.getDispatcher() != null)
				aContext.getDispatcher().notify(
						new OperationEvent(this, 0, "activecontextevent"));

			switch(alarm.status.value()) {
				case AlarmStatus._ALARM_STATUS_GENERATED:
					buttonAcknowledge.setEnabled(true);
					buttonFix.setEnabled(true);
					buttonDelete.setEnabled(true);
					buttonDescribe.setEnabled(true);
					break;
				case AlarmStatus._ALARM_STATUS_ASSIGNED:
					buttonAcknowledge.setEnabled(false);
					buttonFix.setEnabled(true);
					buttonDelete.setEnabled(true);
					buttonDescribe.setEnabled(true);
					break;
				case AlarmStatus._ALARM_STATUS_FIXED:
					buttonAcknowledge.setEnabled(false);
					buttonFix.setEnabled(false);
					buttonDelete.setEnabled(true);
					buttonDescribe.setEnabled(true);
			}

			try {
				SchemeNavigateEvent ev = null;
				MonitoredElement me = (MonitoredElement) ConfigurationStorableObjectPool
						.getStorableObject(alarm.getMonitoredElementId(), true);
				if(me
						.getSort()
						.equals(
								MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH)) {
					List tpathIds = me.getMonitoredDomainMemberIds();

					Identifier domain_id = new Identifier(
							((RISDSessionInfo) aContext.getSessionInterface())
									.getAccessIdentifier().domain_id);
					Domain domain = (Domain) ConfigurationStorableObjectPool
							.getStorableObject(domain_id, true);
					DomainCondition condition = new DomainCondition(
							domain,
							ObjectEntities.SCHEME_PATH_ENTITY_CODE);
					List paths = SchemeStorableObjectPool
							.getStorableObjectsByCondition(condition, true);

					for(Iterator it = paths.iterator(); it.hasNext();) {
						SchemePath sp = (SchemePath) it.next();
						if(tpathIds.contains(sp.pathImpl().getId())) {
							ev = new SchemeNavigateEvent(new SchemePath[] { sp
							}, SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT);
							break;
						}
					}
				}
				else if(me.getSort().equals(
						MonitoredElementSort.MONITOREDELEMENT_SORT_EQUIPMENT)) {
					List eqIds = me.getMonitoredDomainMemberIds();

					Identifier domain_id = new Identifier(
							((RISDSessionInfo) aContext.getSessionInterface())
									.getAccessIdentifier().domain_id);
					Domain domain = (Domain) ConfigurationStorableObjectPool
							.getStorableObject(domain_id, true);
					DomainCondition condition = new DomainCondition(
							domain,
							ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE);
					List elements = SchemeStorableObjectPool
							.getStorableObjectsByCondition(condition, true);

					for(Iterator it = elements.iterator(); it.hasNext();) {
						SchemeElement se = (SchemeElement) it.next();
						if(eqIds.contains(se.equipmentImpl().getId())) {
							ev = new SchemeNavigateEvent(
									new SchemeElement[] { se
									},
									SchemeNavigateEvent.SCHEME_ELEMENT_SELECTED_EVENT);
							break;
						}
					}
				}
				perform_processing = false;
				aContext.getDispatcher().notify(ev);
				perform_processing = true;
			} catch(ApplicationException ex) {
				ex.printStackTrace();
			}
*/
		}
	}

	void buttonRefresh_actionPerformed(ActionEvent e) {
		updateContents();
	}

	void buttonDescribe_actionPerformed(ActionEvent e) {
		Alarm alarm = (Alarm) this.model.getObject(this.table.getSelectedRow());
		
		JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
				alarm.getEvent().getMessage(), 
				LangModelObserver.getString("Message.information"), 
				JOptionPane.INFORMATION_MESSAGE);
		
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
		int mini = this.table.getSelectionModel().getMinSelectionIndex();
		int maxi = this.table.getSelectionModel().getMaxSelectionIndex();
		Alarm[] alarms = new Alarm[maxi - mini + 1];
		for(int i = 0; i + mini < maxi + 1; i++)
			alarms[i] = (Alarm) this.model.getObject(i + mini);
/*
		for(int i = 0; i < alarms.length; i++) {
			alarms[i].status = AlarmStatus.ALARM_STATUS_FIXED;
			alarms[i].fixed_when = System.currentTimeMillis();
			alarms[i].fixed_by = aContext.getSessionInterface().getUserId();
			aContext.getDataSourceInterface().SetAlarm(alarms[i].getId());
		}
		updateContents();
*/
	}

	void buttonDelete_actionPerformed(ActionEvent e) {
		int mini = this.table.getSelectionModel().getMinSelectionIndex();
		int maxi = this.table.getSelectionModel().getMaxSelectionIndex();
		
		Alarm[] alarms = new Alarm[maxi - mini + 1];
		
		for(int i = 0; i + mini < maxi + 1; i++) {
			alarms[i] = (Alarm) this.model.getObject(i + mini);
		}
		
		for(Alarm alarm : alarms) {
			MarkerEvent mEvent2 = new MarkerEvent(this, MarkerEvent.MARKER_DELETED_EVENT,
				this.alarmMarkerMapping.get(alarm).getId(), alarm.getEvent().getMismatchOpticalDistance(),
				alarm.getPath().getId(), alarm.getMonitoredElement().getId(),
				alarm.getPathElement().getId());
			this.aContext.getDispatcher().firePropertyChange(mEvent2);
			
			this.model.removeObject(alarm);
		}
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
