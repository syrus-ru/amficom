package com.syrus.AMFICOM.client.observer.ui;

import static com.syrus.AMFICOM.resource.ObserverResourceKeys.FRAME_ALARM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.alarm.Alarm;
import com.syrus.AMFICOM.client.UI.ADefaultTableCellRenderer;
import com.syrus.AMFICOM.client.UI.WrapperedTable;
import com.syrus.AMFICOM.client.UI.WrapperedTableModel;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.EventReceiver;
import com.syrus.AMFICOM.client.event.MarkerEvent;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client.sound.SoundManager;
import com.syrus.AMFICOM.eventv2.AbstractLineMismatchEvent;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.LineMismatchEvent;
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
import com.syrus.AMFICOM.measurement.Measurement;
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
	
	private Wrapper<Alarm> wrapper;
	WrapperedTableModel<Alarm> model;
	WrapperedTable<Alarm> table;

	JPanel actionPanel = new JPanel();
	JButton buttonAcknowledge = new JButton();
	JButton buttonFix = new JButton();
	JButton buttonDelete = new JButton();
	JButton filterButton = new JButton();
	JButton buttonDescribe = new JButton();
	JButton buttonClose = new JButton();
	JButton buttonRefresh = new JButton();

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
			final Dispatcher dispatcher2 = AlarmFrame.this.aContext.getDispatcher();
			if (event instanceof PopupNotificationEvent) {
				Log.debugMessage("PopupNotificationEvent received", Level.FINER);
				final PopupNotificationEvent popupNotificationEvent = (PopupNotificationEvent) event;
				try {
					final Identifier lmeId = popupNotificationEvent.getLineMismatchEventId(); 
					final AbstractLineMismatchEvent lineMismatchEvent = StorableObjectPool.getStorableObject(lmeId, true);
					final LineMismatchEvent parentLineMismatchEvent = lineMismatchEvent.getParentLineMismatchEvent();
					
					if (parentLineMismatchEvent == null) { // leader
						Alarm alarm = new Alarm(lineMismatchEvent);
						AlarmFrame.this.model.addObject(alarm);
						AlarmFrame.this.table.setSelectedValue(alarm);
						alarmSignal();
					} else {
						List<Alarm> alarms = AlarmFrame.this.model.getValues();
						for (Alarm alarm : alarms) {
							if (alarm.getLeadEvent().equals(parentLineMismatchEvent)) {
								alarm.addLineMismatchEvent(lineMismatchEvent);
								break;
							}
						}
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
					dispatcher2.firePropertyChange(new PropertyChangeEvent(this, "MeasurementStarted", null, m.getMonitoredElementId()));
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				
			} else if (event instanceof MeasurementCompletedEvent) {
				Log.debugMessage("MeasurementCompletedEvent received", Level.FINER);
				final MeasurementCompletedEvent mcEvent = (MeasurementCompletedEvent)event;
				Identifier measurentId = mcEvent.getMeasurementId();
				try {
					Measurement m = StorableObjectPool.getStorableObject(measurentId, true);
					dispatcher2.firePropertyChange(new PropertyChangeEvent(this, "MeasurementStoped", null, m.getMonitoredElementId()));
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
		}
	}
	
	public AlarmFrame(final ApplicationContext aContext) {
		this.wrapper = AlarmWrapper.getInstance();
		this.model = new WrapperedTableModel<Alarm>(
				this.wrapper,
				new String[] {
						AlarmWrapper.COLOR, 
						AlarmWrapper.COLUMN_PATH_NAME, 
						AlarmWrapper.COLUMN_PATHELEMENT_NAME,
						AlarmWrapper.COLUMN_DATE_STARTED,
						AlarmWrapper.COLUMN_DATE_FINISHED,
						AlarmWrapper.COLUMN_MISMATCH_OPTICAL_DISTANCE,
						AlarmWrapper.COLUMN_MISMATCH_PHYSICAL_DISTANCE,
						AlarmWrapper.COLUMN_MEASURE_COUNT });
		this.table = new WrapperedTable<Alarm>(this.model);
		
		final SimpleDateFormat simpleDateFormat = 
			(SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
		final ADefaultTableCellRenderer.DateRenderer dateRenderer = 
			new ADefaultTableCellRenderer.DateRenderer(simpleDateFormat);
		
		this.table.setRenderer(dateRenderer, AlarmWrapper.COLUMN_DATE_STARTED);
		this.table.setRenderer(dateRenderer, AlarmWrapper.COLUMN_DATE_FINISHED);
		this.table.setRenderer(new ADefaultTableCellRenderer.IconRenderer() {
			private static final long serialVersionUID = -6542942610617832169L;
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected1, boolean hasFocus, int row, int column) {
				Alarm alarm = AlarmFrame.this.model.getObject(row);
				Component component = super.getTableCellRendererComponent(table, value, isSelected1, hasFocus, row, column);
				component.setBackground((Color)AlarmWrapper.getInstance().getValue(alarm, AlarmWrapper.COLOR));
				return component;
			}
		}, AlarmWrapper.COLOR);
		
		
		this.table.setAllowAutoResize(true);
		this.table.setAutoscrolls(true);
		
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
				
				Alarm lmEvent1 = AlarmFrame.this.model.getObject(first);
				Alarm lmEvent2 = AlarmFrame.this.model.getObject(last);
				
				final ApplicationContext aContext1 = AlarmFrame.this.aContext;
				
				final Dispatcher dispatcher2 = aContext1.getDispatcher();
				if (!firstb) {
					Identifier markerId = lmEvent1.getMarkerId();
					MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.MARKER_DELETED_EVENT, markerId);
					dispatcher2.firePropertyChange(mEvent);
				}
				if (!lastb && first != last) {
					Identifier markerId = lmEvent2.getMarkerId();
					MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.MARKER_DELETED_EVENT, markerId);
					dispatcher2.firePropertyChange(mEvent);
				}
				
				if (firstb) {
					final PathElement pe = lmEvent1.getPathElement();
					final SchemePath path = lmEvent1.getSchemePath();
					final Measurement measurement = lmEvent1.getLastMeasurement();
					final Identifier monitoredElementId = lmEvent1.getMonitoredElementId();
					final Identifier markerId = lmEvent1.getMarkerId();
					
					final MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.ALARMMARKER_CREATED_EVENT,
							markerId, lmEvent1.getMismatchOpticalDistance(), 
							pe.getId(), path.getId(), monitoredElementId);

					//	notify about measurement
					dispatcher2.firePropertyChange(
							new ObjectSelectedEvent(this, measurement, null, ObjectSelectedEvent.MEASUREMENT));
					dispatcher2.firePropertyChange(mEvent);
				} 
				if (lastb && first != last) {
					final PathElement pe = lmEvent2.getPathElement();
					final SchemePath path = lmEvent2.getSchemePath();
					final Measurement measurement = lmEvent2.getLastMeasurement();
					final Identifier monitoredElementId = lmEvent2.getMonitoredElementId();
					final Identifier markerId = lmEvent1.getMarkerId();
					
					final MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.ALARMMARKER_CREATED_EVENT,
							markerId, lmEvent2.getMismatchOpticalDistance(), 
							pe.getId(), path.getId(), monitoredElementId);
					
					// notify about measurement
					dispatcher2.firePropertyChange(
							new ObjectSelectedEvent(this, measurement, null, ObjectSelectedEvent.MEASUREMENT));
					dispatcher2.firePropertyChange(mEvent);
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
		this.setName(FRAME_ALARM);

		this.setTitle(I18N.getString(FRAME_ALARM));
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
	
	void alarmSignal() {
		SoundManager.loop("sounds/klaxon1.wav");
	}

	public void updateContents() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		
		EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINEMISMATCHEVENT_CODE);
		try {
			Set<AbstractLineMismatchEvent> leaders = StorableObjectPool.getStorableObjectsByCondition(condition , true);
			
			for (LineMismatchEvent event : leaders) {
				Alarm alarm = new Alarm(event);
				this.model.addObject(alarm);
			}
			this.table.sortColumn(4);
			this.model.sortRows(4, false);
			
			if (!leaders.isEmpty()) {
				alarmSignal();
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		this.buttonAcknowledge.setEnabled(false);
		this.buttonFix.setEnabled(false);
		this.buttonDelete.setEnabled(false);
		this.buttonDescribe.setEnabled(false);
		setCursor(Cursor.getDefaultCursor());
	}

	void buttonRefresh_actionPerformed(ActionEvent e) {
		updateContents();
	}

	void buttonDescribe_actionPerformed(ActionEvent e) {
		SoundManager.stop();
		
		int selected = this.table.getSelectedRow();
		if (selected != -1) {
			Alarm alarm = this.model.getObject(selected);
			
			JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
					new JLabel(alarm.getMessage()), 
					LangModelObserver.getString("Message.information"), 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	void buttonAcknowledge_actionPerformed(ActionEvent e) {
		SoundManager.stop();		
//		int mini = this.table.getSelectionModel().getMinSelectionIndex();
//		int maxi = this.table.getSelectionModel().getMaxSelectionIndex();
	}

	void buttonFix_actionPerformed(ActionEvent e) {
		SoundManager.stop();
		final Alarm alarm = this.model.getObject(this.table.getSelectedRow());
		
		this.table.setSelectedValue(null);
		
		MarkerEvent mEvent2 = new MarkerEvent(this, MarkerEvent.MARKER_DELETED_EVENT, alarm.getMarkerId());
		this.aContext.getDispatcher().firePropertyChange(mEvent2);
	}

	void buttonDelete_actionPerformed(ActionEvent e) {
		final Alarm alarm = this.model.getObject(this.table.getSelectedRow());
		this.table.setSelectedValue(null);
		this.model.removeObject(alarm);

		MarkerEvent mEvent2 = new MarkerEvent(this, MarkerEvent.MARKER_DELETED_EVENT, alarm.getMarkerId());
		this.aContext.getDispatcher().firePropertyChange(mEvent2);
	}

	void filterButton_actionPerformed(ActionEvent e) {
		// TODO filtration
	}

	public String getReportTitle() {
		return FRAME_ALARM;
	}

	public TableModel getTableModel() {
		return this.model;
	}
}
