/*
 * $Id: ServerProcessMessageFrame.java,v 1.3 2004/12/23 11:59:51 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process.ui;

import com.syrus.AMFICOM.server.*;
import com.syrus.AMFICOM.server.process.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * @version $Revision: 1.3 $, $Date: 2004/12/23 11:59:51 $
 * @module serverprocess
 */
public final class ServerProcessMessageFrame extends JFrame implements LogWriter
{
	/**
	 * @serial include
	 */
	Alerter alerter;

	/**
	 * @serial include
	 */
	Reporter reporter;

	/**
	 * @serial include
	 */
	Scheduler scheduler;

	/**
	 * @serial include
	 */
	KISTracer kisTracer;

	/**
	 * @serial include
	 */
	UserTracer userTracer;

	/**
	 * @serial include
	 */
	ResultSetChecker rsetChecker;


	/**
	 * @serial include
	 */
	AlertMessagePanel alertPanel = new AlertMessagePanel();

	/**
	 * @serial include
	 */
	ReportMessagePanel reportPanel = new ReportMessagePanel();

	/**
	 * @serial include
	 */
	ScheduleMessagePanel schedulePanel = new ScheduleMessagePanel();

	/**
	 * @serial include
	 */
	KISTraceMessagePanel kisTracePanel = new KISTraceMessagePanel();

	/**
	 * @serial include
	 */
	EventTraceMessagePanel eventTracePanel = new EventTraceMessagePanel();

	/**
	 * @serial include
	 */
	UserTraceMessagePanel userTracePanel = new UserTraceMessagePanel();

	/**
	 * @serial include
	 */
	CheckResultSetPanel rsetPanel = new CheckResultSetPanel();


	/**
	 * @serial include
	 */
	JTabbedPane tabbedPane = new JTabbedPane();

	/**
	 * @serial include
	 */
	boolean connected = false;

	/**
	 * @serial include
	 */
	ConnectionManager connectionManager = null;

	private ArrayList selectedProcessNames;

	private static final long serialVersionUID = -3063740167431364736L;

	public ServerProcessMessageFrame(ArrayList selectedProcessNames)
	{
		this.selectedProcessNames = selectedProcessNames;
		jbInit();
		initModules();
	}

	private void jbInit()
	{
		tabbedPane.setPreferredSize(new Dimension(700, 600));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		setTitle("Мониторинг серверных процессов");
		setResizable(false);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				ServerProcessMessageFrame.this.windowClosing(e);
			}
		});
		pack();
	}

	void initModules()
	{
		connectionManager = new ConnectionManager(this);

		connect();
		if (connected)
		{
			if (selectedProcessNames.size() == 0)
				selectedProcessNames.addAll(ServerProcessName.PROCESS_NAMES);

			if (selectedProcessNames.contains(ServerProcessName.ID_ALERTER))
			{
				tabbedPane.addTab(alertPanel.getName(), alertPanel);
				alerter = new Alerter(alertPanel);
				alerter.start();
			}

			if (selectedProcessNames.contains(ServerProcessName.ID_USERTRACER))
			{
				tabbedPane.addTab(userTracePanel.getName(), userTracePanel);
				userTracer = new UserTracer(userTracePanel);
				userTracer.start();
			}

			if (selectedProcessNames.contains(ServerProcessName.ID_RESULTSETCHECKER))
			{
				tabbedPane.addTab(rsetPanel.getName(), rsetPanel);
				rsetChecker = new ResultSetChecker(rsetPanel);
				rsetChecker.start();
			}
		}
	}

	public void log(String s)
	{
		System.out.println(s);
	}

	void windowClosing(WindowEvent e)
	{
		System.out.println("Server Process is shutting down...");

		if (selectedProcessNames.contains(ServerProcessName.ID_ALERTER))
		{
			alerter.stopRunning();
			alertPanel.closeLog();
		}

		if (selectedProcessNames.contains(ServerProcessName.ID_USERTRACER))
		{
			userTracer.stopRunning();
			userTracePanel.closeLog();
		}

		if (selectedProcessNames.contains(ServerProcessName.ID_RESULTSETCHECKER))
		{
			rsetChecker.stopRunning();
			rsetPanel.closeLog();
		}

		if (connected)
			connectionManager.disconnect();

		dispose();
		System.out.println("Server Process shut down...");
	}

	void connect()
	{
		if ((!connected) && (connectionManager.connect()))
				connected = true;
	}

	void disconnect()
	{
		if (connected)
		{
			connectionManager.disconnect();
			connected = false;
		}
	}
}
