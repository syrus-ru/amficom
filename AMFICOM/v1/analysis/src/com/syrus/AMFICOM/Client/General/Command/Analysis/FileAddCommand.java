package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.util.Enumeration;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.io.BellcoreStructure;
import com.syrus.io.IniFile;
import com.syrus.io.TraceReader;

public class FileAddCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private BellcoreStructure bs;
	private ApplicationContext aContext;
	private Checker checker;

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
		try
		{
			this.checker = new Checker(this.aContext.getSessionInterface());
		/*
			The code for administrating should be placed here
		*/
		}
		catch (NullPointerException ex)
		{
			System.out.println("Application context and/or user are not defined");
			return;
		}


		IniFile ini = (IniFile)Pool.get("inifile", "analyse");

		JFileChooser chooser = new JFileChooser(ini.getValue("lastdir"));
		chooser.addChoosableFileFilter(new ChoosableFileFilter("sor", "Bellcore"));
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			String id = chooser.getSelectedFile().getAbsolutePath().toLowerCase();
			if (Pool.getHash("bellcorestructure") != null )
			{
				Enumeration enum = Pool.getHash("bellcorestructure").keys();
				while (enum.hasMoreElements())
				{
					if (((String)enum.nextElement()).equals(id))
					{
						JOptionPane cDialog = new JOptionPane();
						int ret = cDialog.showConfirmDialog (
								null,
								LangModelAnalyse.getString("messageFileAlreadyLoaded"),
								"",
								JOptionPane.YES_NO_OPTION);
						if (ret == cDialog.NO_OPTION)
							 return;
						if (ret == cDialog.YES_OPTION)
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
			ini.setValue("lastdir", chooser.getSelectedFile().getParent().toLowerCase());
		}
	}
}
