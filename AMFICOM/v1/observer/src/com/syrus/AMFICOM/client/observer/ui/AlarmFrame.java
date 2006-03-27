package com.syrus.AMFICOM.client.observer.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

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

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.UI.WrapperedTable;
import com.syrus.AMFICOM.client.UI.WrapperedTableModel;
import com.syrus.AMFICOM.client.event.EventReceiver;
import com.syrus.AMFICOM.client.event.MarkerEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.observer.ObserverMainFrame;
import com.syrus.AMFICOM.eventv2.AbstractLineMismatchEvent;
import com.syrus.AMFICOM.eventv2.AbstractReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper;
import com.syrus.AMFICOM.eventv2.MeasurementCompletedEvent;
import com.syrus.AMFICOM.eventv2.MeasurementStartedEvent;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Action;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.AMFICOM.newFilter.ConditionKey;
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

	private Wrapper<AbstractLineMismatchEvent> wrapper;
	WrapperedTableModel<AbstractLineMismatchEvent> model;
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

	Filter filter = new Filter(new ConditionWrapper(){
		public List<ConditionKey> getKeys() {
			// TODO Auto-generated method stub
			return null;
		}

		public short getEntityCode() {
			// TODO Auto-generated method stub
			return 0;
		}
	});
	
	private final class AlarmUpdater implements EventReceiver {
		AlarmUpdater() {
			final ClientSessionEnvironment clientSessionEnvironment = ClientSessionEnvironment.getInstance();
			clientSessionEnvironment.addEventReceiver(this);
		}
	
		/**
		 * @param event
		 * @see EventReceiver#receiveEvent(Event)
		 */
		@SuppressWarnings("unchecked")
		public void receiveEvent(final Event<?> event) {
			if (event instanceof PopupNotificationEvent) {
				Log.debugMessage("PopupNotificationEvent received", Level.FINER);
				final PopupNotificationEvent popupNotificationEvent = (PopupNotificationEvent) event;
				try {
					final Identifier lmeId = popupNotificationEvent.getLineMismatchEventId(); 
					final AbstractLineMismatchEvent lineMismatchEvent = StorableObjectPool.getStorableObject(lmeId, true);
					
					AlarmFrame.this.model.addObject(lineMismatchEvent);

					Identifier rmeId = lineMismatchEvent.getReflectogramMismatchEventId();
					final AbstractReflectogramMismatchEvent reflectogramMismatchEvent = StorableObjectPool.getStorableObject(rmeId, true);
					
					Identifier resultId = reflectogramMismatchEvent.getResultId();
					Result result = StorableObjectPool.getStorableObject(resultId, true);
					
					double optDistance = lineMismatchEvent.getMismatchOpticalDistance();

					if (result.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
						final Action action = result.getAction();
						Measurement m = (Measurement) action;
						
						Identifier meId = m.getMonitoredElementId();
						PathElement pe = StorableObjectPool.getStorableObject(lineMismatchEvent.getAffectedPathElementId(), true);
						SchemePath path = pe.getParentPathOwner();
						
						MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.ALARMMARKER_CREATED_EVENT,
								lmeId, optDistance, pe.getId(), path.getId(), meId);

						AlarmFrame.this.aContext.getDispatcher().firePropertyChange(mEvent);
						AlarmFrame.this.table.setSelectedValue(lineMismatchEvent);
					} else {
						throw new IllegalStateException("Result of ReflectogramMismatchEvent.getResultId() must have ResultSort.RESULT_SORT_MEASUREMENT");
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			} else if (event instanceof MeasurementStartedEvent) {
				Log.debugMessage("MeasurementStartedEvent received", Level.FINER);
				final MeasurementStartedEvent msEvent = (MeasurementStartedEvent)event;
				Identifier measurentId = msEvent.getMeasurementId();
				try {
					Measurement m = StorableObjectPool.getStorableObject(measurentId, true);
					AlarmFrame.this.aContext.getDispatcher().firePropertyChange(new PropertyChangeEvent(this, "MeasurementStarted", null, m.getMonitoredElementId()));
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				
			} else if (event instanceof MeasurementCompletedEvent) {
				Log.debugMessage("MeasurementCompletedEvent received", Level.FINER);
				final MeasurementCompletedEvent mcEvent = (MeasurementCompletedEvent)event;
				Identifier measurentId = mcEvent.getMeasurementId();
				try {
					Measurement m = StorableObjectPool.getStorableObject(measurentId, true);
					AlarmFrame.this.aContext.getDispatcher().firePropertyChange(new PropertyChangeEvent(this, "MeasurementStoped", null, m.getMonitoredElementId()));
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
		}
	}
	
	public AlarmFrame(final ApplicationContext aContext) {
		this.wrapper = LineMismatchEventAlarmWrapper.getInstance();
		this.model = new WrapperedTableModel<AbstractLineMismatchEvent>(
				this.wrapper,
				new String[] {
						LineMismatchEventAlarmWrapper.COLUMN_DATE_CREATED,
						LineMismatchEventWrapper.COLUMN_MISMATCH_OPTICAL_DISTANCE,
						LineMismatchEventWrapper.COLUMN_MISMATCH_PHYSICAL_DISTANCE,
						LineMismatchEventAlarmWrapper.COLUMN_PATHELEMENT_NAME,
						LineMismatchEventAlarmWrapper.COLUMN_PATH_NAME });
		this.table = new WrapperedTable<AbstractLineMismatchEvent>(this.model);
		
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
					AbstractLineMismatchEvent lmEvent1 = AlarmFrame.this.model.getObject(first);
					AbstractLineMismatchEvent lmEvent2 = AlarmFrame.this.model.getObject(last);
					
					Identifier rme1Id = lmEvent1.getReflectogramMismatchEventId();
					Identifier rme2Id = lmEvent2.getReflectogramMismatchEventId();
					
					AbstractReflectogramMismatchEvent rmEvent1 = StorableObjectPool.getStorableObject(rme1Id, true);
					AbstractReflectogramMismatchEvent rmEvent2 = StorableObjectPool.getStorableObject(rme2Id, true);
					
					final ApplicationContext aContext1 = AlarmFrame.this.aContext;
					
					if (!firstb) {
						MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.MARKER_DELETED_EVENT,
								lmEvent1.getId());

						aContext1.getDispatcher().firePropertyChange(mEvent);
					}
					if (!lastb && first != last) {
						MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.MARKER_DELETED_EVENT,
								lmEvent2.getId());

						aContext1.getDispatcher().firePropertyChange(mEvent);
					}
					
					if (firstb) {
						final Identifier affectedPathElementId = lmEvent1.getAffectedPathElementId();
						PathElement pe = StorableObjectPool.getStorableObject(affectedPathElementId, true);
						SchemePath path = pe.getParentPathOwner();
						MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.ALARMMARKER_CREATED_EVENT,
								lmEvent1.getId(), lmEvent1.getMismatchOpticalDistance(), 
								affectedPathElementId, path.getId(), rmEvent1.getMonitoredElementId());

						//	notify about measurement
						Result result = StorableObjectPool.getStorableObject(rmEvent1.getResultId(), true);
						if (AnalysisUtil.hasMeasurementByResult(result)) {
							Measurement m = AnalysisUtil.getMeasurementByResult(result);
							aContext1.getDispatcher().firePropertyChange(
									new ObjectSelectedEvent(this, m, null, ObjectSelectedEvent.MEASUREMENT));
						}
						aContext1.getDispatcher().firePropertyChange(mEvent);
					} 
					if (lastb && first != last) {
						final Identifier affectedPathElementId2 = lmEvent2.getAffectedPathElementId();
						PathElement pe = StorableObjectPool.getStorableObject(affectedPathElementId2, true);
						SchemePath path = pe.getParentPathOwner();
						MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.ALARMMARKER_CREATED_EVENT,
								lmEvent2.getId(), lmEvent2.getMismatchOpticalDistance(), 
								affectedPathElementId2, path.getId(), rmEvent2.getMonitoredElementId());
						
						// notify about measurement
						Result result = StorableObjectPool.getStorableObject(rmEvent2.getResultId(), true);
						if (AnalysisUtil.hasMeasurementByResult(result)) {
							Measurement m = AnalysisUtil.getMeasurementByResult(result);
							aContext1.getDispatcher().firePropertyChange(
									new ObjectSelectedEvent(this, m, null, ObjectSelectedEvent.MEASUREMENT));
						}
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
//		this.actionPanel.add(this.buttonAcknowledge);
		this.actionPanel.add(this.buttonFix);
//		this.actionPanel.add(this.buttonDelete);
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
		EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINEMISMATCHEVENT_CODE);
		try {
			Set<AbstractLineMismatchEvent> limeMismatchEvents = StorableObjectPool.getStorableObjectsByCondition(condition , true);
			this.model.setValues(limeMismatchEvents);
			this.model.sortRows(0, this.model.getSortOrder(0));
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		this.buttonAcknowledge.setEnabled(false);
		this.buttonFix.setEnabled(false);
		this.buttonDelete.setEnabled(false);
		this.buttonDescribe.setEnabled(false);
	}

	void buttonRefresh_actionPerformed(ActionEvent e) {
		updateContents();
	}

	void buttonDescribe_actionPerformed(ActionEvent e) {
		int selected = this.table.getSelectedRow();
		if (selected != -1) {
			AbstractLineMismatchEvent lineMismatchEvent = this.model.getObject(selected);
		
		JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
				lineMismatchEvent.getMessage(), 
				LangModelObserver.getString("Message.information"), 
				JOptionPane.INFORMATION_MESSAGE);
		}
	}

	void buttonAcknowledge_actionPerformed(ActionEvent e) {
		int mini = this.table.getSelectionModel().getMinSelectionIndex();
		int maxi = this.table.getSelectionModel().getMaxSelectionIndex();
	}

	void buttonFix_actionPerformed(ActionEvent e) {
		AbstractLineMismatchEvent lmEvent = this.model.getObject(this.table.getSelectedRow());
		this.table.setSelectedValue(null);
		
		MarkerEvent mEvent2 = new MarkerEvent(this, MarkerEvent.MARKER_DELETED_EVENT,
				lmEvent.getId());
		this.aContext.getDispatcher().firePropertyChange(mEvent2);
	}

	void buttonDelete_actionPerformed(ActionEvent e) {
		AbstractLineMismatchEvent lmEvent = this.model.getObject(this.table.getSelectedRow());
		this.table.setSelectedValue(null);
		this.model.removeObject(lmEvent);

		MarkerEvent mEvent2 = new MarkerEvent(this, MarkerEvent.MARKER_DELETED_EVENT,
				lmEvent.getId());
		this.aContext.getDispatcher().firePropertyChange(mEvent2);
	}

	void filterButton_actionPerformed(ActionEvent e) {
		// TODO filtration
	}
}
