package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.io.FileOutputStream;

import javax.swing.JFileChooser;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;
import com.syrus.io.IniFile;

public class FileSaveCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private BellcoreStructure bs;
	private ApplicationContext aContext;
	private Checker checker;

	public FileSaveCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher( (Dispatcher)value );
	}

	public Object clone()
	{
		return new FileSaveCommand(dispatcher, aContext);
	}

	public void execute()
	{
		try
		{
			this.checker = new Checker(this.aContext.getSessionInterface());
			if(!checker.checkCommand(checker.saveReflectogrammFile))
			{
				return;
			}
		}
		catch (NullPointerException ex)
		{
			System.out.println("Application context and/or user are not defined");
			return;
		}


		String dir;
		IniFile ini = null;
		try
		{
			ini = new IniFile("analyse.ini");
			dir = ini.getValue("lastdir");
		}
		catch (java.io.IOException ex)
		{
			System.out.println("Error reading ini file: analyse.ini");
			dir = "c:\\";
		}

		JFileChooser chooser = new JFileChooser(dir);
		chooser.addChoosableFileFilter(new ChoosableFileFilter("sor", "Bellcore"));
		int returnVal = chooser.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			bs = (BellcoreStructure)(Pool.get("bellcorestructure", "primarytrace"));
			try
			{
				FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile());
				BellcoreWriter bw = new BellcoreWriter();
				fos.write(bw.write(bs));

				if (chooser.getSelectedFile().getAbsolutePath().equals(bs.title))
					dispatcher.notify(new RefChangeEvent("primarytrace", RefChangeEvent.SAVE_EVENT));
				else
				{
					bs.title = chooser.getSelectedFile().getAbsolutePath().toLowerCase();
					dispatcher.notify(new RefChangeEvent("primarytrace",
																RefChangeEvent.SAVE_EVENT + RefChangeEvent.SELECT_EVENT));
				}
			}
			catch (Exception x){x.printStackTrace();}
		}
	}
}