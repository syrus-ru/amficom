package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.io.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrameSimplified;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.io.*;

public class FileAddCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private BellcoreStructure bs;
	private ApplicationContext aContext;
	private Checker checker;
	private String propertiesFileName = "analysis.properties";

	public FileAddCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher )value);
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public Object clone()
	{
		return new FileAddCommand(dispatcher, aContext);
	}

	public void execute()
	{
		if (!AnalyseMainFrameSimplified.DEBUG) // XXX: saa: security bypass
		{
			try
			{
				this.checker = new Checker(this.aContext.getSessionInterface());
				if(!checker.checkCommand(Checker.openReflectogrammFile))
				{
					return;
				}
			}
			catch (NullPointerException ex)
			{
				System.out.println("Application context and/or user are not defined");
				return;
			}
		}

		Properties properties = new Properties();
		String lastDir = "";
		try
		{
			properties.load(new FileInputStream(propertiesFileName));
			lastDir = properties.getProperty("lastdir");
		}
		catch (IOException ex)
		{
		}

		JFileChooser chooser = new JFileChooser(lastDir);
		chooser.addChoosableFileFilter(new ChoosableFileFilter("sor", "Bellcore"));
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			System.out.println("DEBUG: the user has added file " + chooser.getSelectedFile().getAbsolutePath()); // FIXME: debugging purpose only
			String id = chooser.getSelectedFile().getAbsolutePath().toLowerCase();
			if (Pool.getMap("bellcorestructure") != null )
			{
				Iterator it = Pool.getMap("bellcorestructure").keySet().iterator();
				while (it.hasNext())
				{
					if (((String)it.next()).equals(id))
					{
						JOptionPane cDialog = new JOptionPane();
						int ret = JOptionPane.showConfirmDialog (
								null,
								LangModelAnalyse.getString("messageFileAlreadyLoaded"),
								"",
								JOptionPane.YES_NO_OPTION);
						if (ret == JOptionPane.NO_OPTION)
							 return;
						if (ret == JOptionPane.YES_OPTION)
							 new FileRemoveCommand(dispatcher, id, aContext).execute();
					}
				}
			}
			TraceReader tr = new TraceReader();
			bs = tr.getData(chooser.getSelectedFile());
			if (bs == null)
			{
				bs = tr.getData(chooser.getSelectedFile());
				if (bs == null)
				{
					JOptionPane.showMessageDialog (Environment.getActiveWindow(),
							LangModelAnalyse.getString("messageReadError") + ":\n" + chooser.getSelectedFile().getAbsolutePath(),
							LangModelAnalyse.getString("messageError"),
							JOptionPane.OK_OPTION);
					return;
				}
			}
			bs.title = chooser.getSelectedFile().getName();
			Pool.put("bellcorestructure", id, bs);
			dispatcher.notify(new RefChangeEvent(id,
											RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));

			try
			{
				properties.setProperty("lastdir", chooser.getSelectedFile().getParent().toLowerCase());
				properties.store(new FileOutputStream(propertiesFileName), null);
			}
			catch (IOException ex)
			{
			}
		}
	}
}
