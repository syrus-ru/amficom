/*
 * $Id: ServerProcessManager.java,v 1.1 2004/06/22 09:57:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process;

import com.syrus.AMFICOM.server.process.prefs.SMTPConnectionManager;
import com.syrus.AMFICOM.server.process.ui.ServerProcessMessageFrame;
import com.syrus.util.prefs.ui.JPreferencesManagerFrame;
import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 09:57:10 $
 * @module serverprocess
 */
final class ServerProcessManager 
{
	ServerProcessMessageFrame frame;

	private static final String ID_CONFIG_LONG = "--config";
	private static final String ID_CONFIG_SHORT = "-c";
	private static final String ID_HELP_LONG = "--help";
	private static final String ID_HELP_SHORT = "-h";
	private static final String ID_USAGE_LONG = "--usage";
	private static final String ID_USAGE_SHORT = "-u";

	static {
		try {
			Class.forName(SMTPConnectionManager.class.getName());
		} catch (ClassNotFoundException cnfe) {
			;
		}
	}

	ServerProcessManager(ArrayList selectedProcessNames)
	{
		if (selectedProcessNames.size() == 0)
			System.out.println("Loading all processes.");
		else
			System.out.println("Loading only selected processes:");
			for (int i = 0; i < selectedProcessNames.size(); i ++)
				System.out.println('\t' + (String )selectedProcessNames.get(i));
		frame = new ServerProcessMessageFrame(selectedProcessNames);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation((screenSize.width - frameSize.width) / 2,
			(screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		try 
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if ((args.length == 1))
		{
			String arg = args[0];
			if (arg.equals(ID_CONFIG_SHORT)
					|| arg.equals(ID_CONFIG_LONG))
				configure();
			else if (arg.equals(ID_HELP_SHORT)
					|| arg.equals(ID_HELP_LONG)
					|| arg.equals(ID_USAGE_SHORT)
					|| arg.equals(ID_USAGE_LONG))
				usage();
			else
				startModules(args);
		}
		else
			startModules(args);
	}

	private static void usage()
	{
		System.err.println("Usage:");
		System.err.println("\tServerProcessManager");
		System.err.println("\t-- will start ServerProcessManager with all modules enabled. This is the default.");
		System.err.println("\tServerProcessManager <module name> [<module name> [...]]");
		System.err.println("\t-- will start ServerProcessManager with only selected modules enabled. Valid module names are:");
		Iterator iterator = ServerProcessName.PROCESS_NAMES.iterator();
		while (iterator.hasNext())
			System.err.println("\t\t" + iterator.next());
		System.err.println("\tServerProcessManager -c|--config");
		System.err.println("\t-- will start ServerProcessManager in config mode.");
		System.err.println("\tServerProcessManager -h|--help|-u|--usage");
		System.err.println("\t-- displays this help message.");
	}

	private static void configure()
	{
		(new JPreferencesManagerFrame()).show();
	}

	private static void startModules(String selectedProcessNames[])
	{
		/*
		 * Here, no check for module name validity is needed: invalid module
		 * names are just ignored.
		 */
		new ServerProcessManager(new ArrayList(Arrays.asList(selectedProcessNames)));
	}
}
