package com.syrus.AMFICOM.Client.Survey.Alarm;

import com.syrus.AMFICOM.CORBA.General.AlarmStatus;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.SchemeNavigateEvent;
import com.syrus.AMFICOM.Client.General.Filter.FilterDialog;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
import com.syrus.AMFICOM.Client.Resource.Alarm.SystemEvent;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import com.syrus.AMFICOM.Client.Resource.SurveyDataSourceImage;
import com.syrus.AMFICOM.Client.Survey.Alarm.Filter.AlarmFilter;
import com.syrus.AMFICOM.Client.Survey.Alarm.Filter.DefaultAlarmFilter;
import com.syrus.AMFICOM.Client.Survey.Alarm.UI.AlarmController;
import com.syrus.AMFICOM.Client.Survey.SurveyMDIMain;
import com.syrus.AMFICOM.analysis.dadara.OpticalAlarmDescriptor;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.ApplicationException;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class AlarmFrame extends JInternalFrame
		implements OperationListener
{
	ApplicationContext aContext;

	boolean initial_init = true;
	BorderLayout borderLayout1 = new BorderLayout();
//	AlarmToolBar toolBar = new AlarmToolBar();
	JPanel actionPanel = new JPanel();

	private AlarmController controller;
	private ObjectResourceTableModel model;
	private ObjectResourceTable table;

	JButton buttonAcknowledge = new JButton();
	JButton buttonFix = new JButton();
	JButton buttonDelete = new JButton();
	JButton filterButton = new JButton();
	JButton buttonDescribe = new JButton();
	JButton buttonClose = new JButton();
	JButton buttonRefresh = new JButton();

	boolean perform_processing = true;

	AlarmFilter filter = new DefaultAlarmFilter();

	static SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	public AlarmFrame(ApplicationContext aContext)
	{
		super();
		controller = AlarmController.getInstance();
		model = new ObjectResourceTableModel(controller);
		table = new ObjectResourceTable(model);

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		setContext(aContext);
	}

	private void jbInit() throws Exception
{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		this.setResizable(true);

		this.setTitle(LangModelSurvey.getString("Alarm_signals"));
		this.getContentPane().setLayout(borderLayout1);
		this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter()
		{
			public void internalFrameActivated(InternalFrameEvent e)
			{
				this_internalFrameActivated(e);
			}

			public void internalFrameOpened(InternalFrameEvent e)
			{
				this_internalFrameOpened(e);
			}
		});
		this.addComponentListener(new java.awt.event.ComponentAdapter()
		{
			public void componentShown(ComponentEvent e)
			{
				this_componentShown(e);
			}
		});

		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				alarmTable_valueChanged(e);
			}
		});

		actionPanel.setLayout(new XYLayout());
		buttonAcknowledge.setText("Подтвердить");
		buttonAcknowledge.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonAcknowledge_actionPerformed(e);
			}
		});
		buttonFix.setText("Снять");
		buttonFix.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonFix_actionPerformed(e);
			}
		});
		buttonClose.setText("Закрыть");
		buttonClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonClose_actionPerformed(e);
			}
		});
		buttonRefresh.setText("Обновить");
		buttonRefresh.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonRefresh_actionPerformed(e);
			}
		});
		buttonDelete.setText("Удалить");
		buttonDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonDelete_actionPerformed(e);
			}
		});
		filterButton.setText("Фильтр");
		filterButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				filterButton_actionPerformed(e);
			}
		});
		buttonDescribe.setText("Описание");
		buttonDescribe.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonDescribe_actionPerformed(e);
			}
		});

//		this.getContentPane().add(toolBar, BorderLayout.NORTH);
		this.getContentPane().add(table, BorderLayout.CENTER);
		actionPanel.add(buttonAcknowledge, new XYConstraints(5, 5, 110, 27));
		actionPanel.add(buttonFix, new XYConstraints(115, 5, 90, 27));
		actionPanel.add(buttonDelete, new XYConstraints(205, 5, 90, 27));
		actionPanel.add(filterButton, new XYConstraints(295, 5, 90, 27));
		actionPanel.add(buttonDescribe, new XYConstraints(385, 5, 100, 27));
		actionPanel.add(buttonClose, new XYConstraints(485, 5, 90, 27));
		actionPanel.add(buttonRefresh, new XYConstraints(575, 5, 90, 27));
		this.getContentPane().add(actionPanel, BorderLayout.SOUTH);
	}

	public void init_module()
	{
		initial_init = false;
		System.out.println("this file ");
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.fireModelChanged("");

		updateContents();

		buttonAcknowledge.setEnabled(false);
		buttonFix.setEnabled(false);
		buttonDelete.setEnabled(false);
		buttonDescribe.setEnabled(false);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		aContext.getDispatcher().register(this, "alarmreceived");
	}

	public ApplicationContext getContext()
	{
		return aContext;
	}

	public ApplicationModel getModel()
	{
		return aContext.getApplicationModel();
	}
	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals("alarmreceived"))
		{
			updateContents();
		}
	}

	public void updateContents()
	{
		new SurveyDataSourceImage(aContext.getDataSourceInterface()).GetAlarms();

		List alarms = Pool.getList("alarm");
		if(alarms == null)
			alarms = new LinkedList();
		for (Iterator it = alarms.iterator(); it.hasNext();)
		{
			Alarm alarm = (Alarm)it.next();
			if (alarm.status == AlarmStatus.ALARM_STATUS_DELETED)
				it.remove();
		}
		model.setContents(alarms);

		buttonAcknowledge.setEnabled(false);
		buttonFix.setEnabled(false);
		buttonDelete.setEnabled(false);
		buttonDescribe.setEnabled(false);
	}

	public void alarmTable_valueChanged(ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;

		ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		if (lsm.isSelectionEmpty())
		{
			buttonAcknowledge.setEnabled(false);
			buttonFix.setEnabled(false);
			buttonDelete.setEnabled(false);
			buttonDescribe.setEnabled(false);
					//no rows are selected
		}
		else
		{
			Alarm alarm = (Alarm)model.getObject(table.getSelectedRow());
			SystemEvent event = (SystemEvent )Pool.get("event", alarm.event_id);

			Pool.put("activecontext", "useractionselected", "alarm_selected");
			Pool.put("activecontext", "selected_id", alarm.getId());

			if (aContext.getDispatcher() != null)
				aContext.getDispatcher().notify(new OperationEvent(this, 0, "activecontextevent"));

			switch (alarm.status.value()) {
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
				MonitoredElement me = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(
						alarm.getMonitoredElementId(), true);
				if (me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH)) {
					List tpathIds = me.getMonitoredDomainMemberIds();
					for (Iterator it = Pool.getMap(SchemePath.typ).values().iterator(); it.hasNext(); ) {
						SchemePath sp = (SchemePath)it.next();
						if (sp.path != null && tpathIds.contains(sp.path.getId())) {
							ev = new SchemeNavigateEvent(
									new SchemePath[] {sp}
									,
									SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT);
							break;
						}
					}
				}
				else if (me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_EQUIPMENT)) {
					List eqIds = me.getMonitoredDomainMemberIds();
					for (Iterator it = Pool.getMap(SchemeElement.typ).values().iterator(); it.hasNext(); ) {
						SchemeElement se = (SchemeElement)it.next();
						if (se.equipment != null && eqIds.contains(se.equipment.getId())) {
							ev = new SchemeNavigateEvent(
									new SchemeElement[] {se}
									,
									SchemeNavigateEvent.SCHEME_ELEMENT_SELECTED_EVENT);
							break;
						}
					}
				}
				perform_processing = false;
				aContext.getDispatcher().notify(ev);
				perform_processing = true;
			}
			catch (ApplicationException ex) {
				ex.printStackTrace();
			}
		}
	}

	void this_componentShown(ComponentEvent e)
	{
		if(initial_init)
			init_module();
	}

	void this_internalFrameActivated(InternalFrameEvent e)
	{
		this.grabFocus();
	}

	void this_internalFrameOpened(InternalFrameEvent e)
	{
		this.grabFocus();
	}

	void buttonClose_actionPerformed(ActionEvent e)
	{
		this.dispose();
	}

	void buttonRefresh_actionPerformed(ActionEvent e)
	{
		updateContents();
	}

	void buttonDescribe_actionPerformed(ActionEvent e)
	{
		Alarm alarm = (Alarm)model.getObject(table.getSelectedRow());
		AlarmDescriptor ad = null;
		if(alarm.type_id.equals("rtutestalarm") ||
			 alarm.type_id.equals("rtutestwarning"))
		{
			ad = new OpticalAlarmDescriptor(alarm);
		}
		if(ad != null)
		{
			String name;

			try {
				MonitoredElement me = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(
						alarm.getMonitoredElementId(), true);
				name = me.getName();
			}
			catch (ApplicationException ex) {
				name = "' '";
			}

			AlarmPopupFrame f = new AlarmPopupFrame(
					"Отклонение в " +
						name +
						" возникло " +
						sdf.format(new Date(ad.getAlarmTime())),
					alarm,
					aContext);

			aContext.getDispatcher().notify (
				new OperationEvent (f,0,SurveyMDIMain.alarmPopupFrameDisplayed));

			Container desktop = this.getParent();
			f.setLocation(this.getLocation());
			f.setSize(this.getSize());
			f.set(ad);
			desktop.add(f);
			f.show();
			f.toFront();
		}
	}

	void buttonAcknowledge_actionPerformed(ActionEvent e)
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.alarmSignalAssurement))
		{
			return;
		}

		int mini = table.getSelectionModel().getMinSelectionIndex();
		int maxi = table.getSelectionModel().getMaxSelectionIndex();
		Alarm[] alarms = new Alarm[maxi - mini + 1];
		for(int i = 0; i + mini < maxi + 1; i++)
			alarms[i] = (Alarm)model.getObject(i + mini);

		for(int i = 0; i < alarms.length; i++)
		{
			alarms[i].status = AlarmStatus.ALARM_STATUS_ASSIGNED;
			alarms[i].assigned = System.currentTimeMillis();
			alarms[i].assigned_to = aContext.getSessionInterface().getUserId();
			aContext.getDataSourceInterface().SetAlarm(alarms[i].getId());
		}
/*
		Alarm alarm = (Alarm )alarmPane.getSelectedObject();
//		System.out.println("Index:" + index + "; Object " + alarm.toString());
		alarm.status = AlarmStatus.ASSIGNED;
		alarm.assigned = System.currentTimeMillis();
		alarm.assigned_to = aContext.getSessionInterface().getUserId();
		aContext.getDataSourceInterface().SetAlarm(alarm.getId());
*/
		updateContents();
	}

	void buttonFix_actionPerformed(ActionEvent e)
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.alarmSignalStopping))
		{
			return;
		}

		int mini = table.getSelectionModel().getMinSelectionIndex();
		int maxi = table.getSelectionModel().getMaxSelectionIndex();
		Alarm[] alarms = new Alarm[maxi - mini + 1];
		for(int i = 0; i + mini < maxi + 1; i++)
			alarms[i] = (Alarm)model.getObject(i + mini);

		for(int i = 0; i < alarms.length; i++)
		{
			alarms[i].status = AlarmStatus.ALARM_STATUS_FIXED;
			alarms[i].fixed_when = System.currentTimeMillis();
			alarms[i].fixed_by = aContext.getSessionInterface().getUserId();
			aContext.getDataSourceInterface().SetAlarm(alarms[i].getId());
		}
/*
		Alarm alarm = (Alarm )alarmPane.getSelectedObject();

//		System.out.println("Index:" + index + "; Object " + alarm.toString());

		alarm.status = AlarmStatus.FIXED;
		alarm.fixed_when = System.currentTimeMillis();
		alarm.fixed_by = aContext.getSessionInterface().getUserId();
		aContext.getDataSourceInterface().SetAlarm(alarm.getId());
*/
		updateContents();
	}

	void buttonDelete_actionPerformed(ActionEvent e)
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.alarmSignalDeleting))
		{
			return;
		}

		int mini = table.getSelectionModel().getMinSelectionIndex();
		int maxi = table.getSelectionModel().getMaxSelectionIndex();
		Alarm[] alarms = new Alarm[maxi - mini + 1];
		for(int i = 0; i + mini < maxi + 1; i++)
			alarms[i] = (Alarm)model.getObject(i + mini);

		for(int i = 0; i < alarms.length; i++)
		{
			alarms[i].status = AlarmStatus.ALARM_STATUS_DELETED;
			alarms[i].fixed_when = System.currentTimeMillis();
			alarms[i].fixed_by = aContext.getSessionInterface().getUserId();
			aContext.getDataSourceInterface().SetAlarm(alarms[i].getId());
		}
/*
		Alarm alarm = (Alarm )alarmPane.getSelectedObject();

//		System.out.println("Index:" + index + "; Object " + alarm.toString());

		alarm.status = AlarmStatus.DELETED;
		alarm.fixed_when = System.currentTimeMillis();
		alarm.fixed_by = aContext.getSessionInterface().getUserId();
		aContext.getDataSourceInterface().SetAlarm(alarm.getId());
*/
		updateContents();
	}

	void filterButton_actionPerformed(ActionEvent e)
	{
		AlarmFilter orf = (AlarmFilter )filter.clone();
		FilterDialog dlg = new FilterDialog(orf, aContext);
		dlg.pack();

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
				Dimension frameSize =  dlg.getSize();
		frameSize.width = 450;
		frameSize.height = frameSize.height + 20;
		dlg.setSize(frameSize);

				if (frameSize.height > screenSize.height)
						frameSize.height = screenSize.height;
				if (frameSize.width > screenSize.width)
						frameSize.width = screenSize.width;
				dlg.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);

		dlg.setModal(true);
				dlg.setVisible(true);

		if ( dlg.retcode == 1)
		{
			filter = orf;
			updateContents();
		}
	}
}
