package com.syrus.AMFICOM.Client.Survey.Alarm;

import com.syrus.AMFICOM.Client.Survey.SurveyMDIMain;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.io.*;
import javax.swing.table.AbstractTableModel;
import oracle.jdeveloper.layout.*;

import com.syrus.AMFICOM.Client.Survey.Alarm.UI.*;
import com.syrus.AMFICOM.Client.Survey.Alarm.Filter.*;

import oracle.jdeveloper.layout.XYConstraints;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.text.SimpleDateFormat;

import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.AMFICOM.Client.General.Filter.FilterDialog;

public class AlarmFrame extends JInternalFrame
		implements OperationListener,
		ListSelectionListener
{
	ApplicationContext aContext;

	public static IniFile iniFile;
	static String iniFileName = "Alarm.properties";

	boolean initial_init = true;
	BorderLayout borderLayout1 = new BorderLayout();
//	AlarmToolBar toolBar = new AlarmToolBar();
	JPanel actionPanel = new JPanel();

	ObjectResourceTablePane alarmPane = new ObjectResourceTablePane();
	XYLayout xYLayout1 = new XYLayout();

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
//		this.setTitle("Сигналы тревоги");
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

		alarmPane.addListSelectionListener(this);
		alarmPane.getTable().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		actionPanel.setLayout(xYLayout1);
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

		alarmTableInit();

//		this.getContentPane().add(toolBar, BorderLayout.NORTH);
		this.getContentPane().add(alarmPane, BorderLayout.CENTER);
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
		// load values from properties file
		try
		{
			iniFile = new IniFile(iniFileName);
						setFromIniFile();
		}
		catch(java.io.IOException e)
		{
			setDefaults();
		}

		aModel.fireModelChanged("");

		updateContents();

		buttonAcknowledge.setEnabled(false);
		buttonFix.setEnabled(false);
		buttonDelete.setEnabled(false);
		buttonDescribe.setEnabled(false);
	}

	public void setFromIniFile()
	{
	}

	public void setDefaults()
	{
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		if(aContext.getApplicationModel() == null)
			aContext.setApplicationModel(new ApplicationModel());
		setModel(aContext.getApplicationModel());
		aContext.getDispatcher().register(this, "alarmreceived");
	}

	public ApplicationContext getContext()
	{
		return aContext;
	}

	public void setModel(ApplicationModel aModel)
	{
//		aModel.addListener(toolBar);
//		toolBar.setModel(aModel);

//		aModel.fireModelChanged("");
	}

	public ApplicationModel getModel()
	{
		return aContext.getApplicationModel();
	}
	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals("alarmreceived"))
		{
//			AlarmReceivedEvent alarm = (AlarmReceivedEvent)ae;
			updateContents();
			//System.out.println("table updated");
		}
		if(ae.getActionCommand().equals(CatalogNavigateEvent.type))
			if(perform_processing)
		{
			CatalogNavigateEvent cne = (CatalogNavigateEvent )ae;

			if(cne.CATALOG_EQUIPMENT_SELECTED)
			{
				System.out.println("CATALOG_EQUIPMENT_SELECTED");
			}

			if(cne.CATALOG_PATH_SELECTED)
			{
				System.out.println("CATALOG_PATH_SELECTED");
				TransmissionPath[] tps = (TransmissionPath[] )cne.getSource();
				try  {
				System.out.println("path 0 = " + tps[0].getId()); } 
				catch (Exception ex)  {				} 
/*
				for(Enumeration enum = lnl().getMapContext().getTransmissionPath().elements(); enum.hasMoreElements();)
				{
					MapTransmissionPathElement mappath = (MapTransmissionPathElement )enum.nextElement();
					if(mappath.PATH_ID != null && !mappath.PATH_ID.equals(""))
					{
						SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, mappath.PATH_ID);
						if(sp.path_id != null && !sp.path_id.equals(""))
							for(int i = 0; i < tps.length; i++)
								if(sp.path_id.equals(tps[i].getId()))
									mappath.select();
					}
				}
*/
			}
		}
	}

	void alarmTableInit()
	{
		alarmPane.initialize(new AlarmDisplayModel(), new DataSet());
		alarmPane.setSorter(Alarm.getSorter());
	}

	public void updateContents()
	{
//		Pool.put("filter", filter.getId(), filter);
//		new SurveyDataSourceImage(aContext.getDataSourceInterface()).GetAlarms(filter.getId());
		new SurveyDataSourceImage(aContext.getDataSourceInterface()).GetAlarms();

		Hashtable ht = Pool.getHash("alarm");
		Vector deleted_alarms = new Vector();
		if(ht == null)
			ht = new Hashtable();
		Enumeration alarms = ht.elements();
		if(alarms != null)
			while (alarms.hasMoreElements())
			{
				Alarm alarm = (Alarm)alarms.nextElement();
				if (alarm.status == AlarmStatus.ALARM_STATUS_DELETED)
					deleted_alarms.add(alarm.getId());
			}
		for	(int i = 0; i < deleted_alarms.size(); i++)
			ht.remove(deleted_alarms.get(i));

		DataSet ds = new DataSet(ht);
		alarmPane.setContents(filter.filter(ds));
		alarmPane.resortContents();

		buttonAcknowledge.setEnabled(false);
		buttonFix.setEnabled(false);
		buttonDelete.setEnabled(false);
		buttonDescribe.setEnabled(false);
	}

	public void valueChanged(ListSelectionEvent e)
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
			Alarm alarm = (Alarm )alarmPane.getSelectedObject();
			SystemEvent event = (SystemEvent )Pool.get("event", alarm.event_id);

	//		Pool.put("activecontext", "useractionselected", "evaluation_selected");
	//		Pool.put("activecontext", "selected_id", event.descriptor);
			Pool.put("activecontext", "useractionselected", "alarm_selected");
			Pool.put("activecontext", "selected_id", alarm.getId());

			if (aContext.getDispatcher() != null)
				aContext.getDispatcher().notify(new OperationEvent(this, 0, "activecontextevent"));

			CatalogNavigateEvent cne = null;

			MonitoredElement me = (MonitoredElement )Pool.get(MonitoredElement.typ, alarm.getMonitoredElementId());
			if(me.element_type.equals("path"))
			{
				TransmissionPath tp = (TransmissionPath )Pool.get(TransmissionPath.typ, me.element_id);
//				System.out.println("notify CATALOG_PATH_SELECTED_EVENT " + tp.getId());
				cne = new CatalogNavigateEvent(
					new TransmissionPath[] { tp }, 
					CatalogNavigateEvent.CATALOG_PATH_SELECTED_EVENT);
			}
			else
			if(me.element_type.equals("equipment"))
			{
				Equipment eq = (Equipment )Pool.get(Equipment.typ, me.element_id);
//				System.out.println("notify CATALOG_EQUIPMENT_SELECTED_EVENT " + eq.getId());
				cne = new CatalogNavigateEvent(
					new Equipment[] { eq },
					CatalogNavigateEvent.CATALOG_EQUIPMENT_SELECTED_EVENT);
			}

			if(alarm.status.value() == AlarmStatus._ALARM_STATUS_GENERATED)
			{
				buttonAcknowledge.setEnabled(true);
				buttonFix.setEnabled(true);
				buttonDelete.setEnabled(true);
				buttonDescribe.setEnabled(true);
			}
			else
			if(alarm.status.value() == AlarmStatus._ALARM_STATUS_ASSIGNED)
			{
				buttonAcknowledge.setEnabled(false);
				buttonFix.setEnabled(true);
				buttonDelete.setEnabled(true);
				buttonDescribe.setEnabled(true);
			}
			else
			if(alarm.status.value() == AlarmStatus._ALARM_STATUS_FIXED)
			{
				buttonAcknowledge.setEnabled(false);
				buttonFix.setEnabled(false);
				buttonDelete.setEnabled(true);
				buttonDescribe.setEnabled(true);
			}

			perform_processing = false;
			if (aContext.getDispatcher() != null)
				aContext.getDispatcher().notify(cne);
			perform_processing = true;
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
		Alarm alarm = (Alarm )alarmPane.getSelectedObject();
		AlarmDescriptor ad = null;
		if(alarm.type_id.equals("rtutestalarm") ||
			 alarm.type_id.equals("rtutestwarning"))
		{
			ad = new OpticalAlarmDescriptor(alarm, aContext.getDataSourceInterface());
		}
		if(ad != null)
		{
			AlarmPopupFrame f = new AlarmPopupFrame(
					"Отклонение в " +
						Pool.getName(MonitoredElement.typ, alarm.getMonitoredElementId()) + 
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

		int mini = alarmPane.getTable().getSelectionModel().getMinSelectionIndex();
		int maxi = alarmPane.getTable().getSelectionModel().getMaxSelectionIndex();
		Alarm[] alarms = new Alarm[maxi - mini + 1];
		for(int i = 0; i + mini < maxi + 1; i++)
			alarms[i] = (Alarm )alarmPane.getContents().get(i + mini);

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

		int mini = alarmPane.getTable().getSelectionModel().getMinSelectionIndex();
		int maxi = alarmPane.getTable().getSelectionModel().getMaxSelectionIndex();
		Alarm[] alarms = new Alarm[maxi - mini + 1];
		for(int i = 0; i + mini < maxi + 1; i++)
			alarms[i] = (Alarm )alarmPane.getContents().get(i + mini);

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

		int mini = alarmPane.getTable().getSelectionModel().getMinSelectionIndex();
		int maxi = alarmPane.getTable().getSelectionModel().getMaxSelectionIndex();
		Alarm[] alarms = new Alarm[maxi - mini + 1];
		for(int i = 0; i + mini < maxi + 1; i++)
			alarms[i] = (Alarm )alarmPane.getContents().get(i + mini);

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
  
  public AbstractTableModel getTableModel ()
  {
    return this.alarmPane.tableModel;
  }
}

