package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.io.*;
import java.util.Properties;

import javax.swing.JFileChooser;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.io.*;

public class FileSaveCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private BellcoreStructure bs;
	private ApplicationContext aContext;
	private Checker checker;
	private String propertiesFileName = "analysis.properties";

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
			if(!checker.checkCommand(Checker.saveReflectogrammFile))
			{
				return;
			}
		}
		catch (NullPointerException ex)
		{
			System.out.println("Application context and/or user are not defined");
			return;
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

				try
				{
					properties.setProperty("lastdir", chooser.getSelectedFile().getParent().toLowerCase());
					properties.store(new FileOutputStream(propertiesFileName), null);
				}
				catch (IOException ex)
				{
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
}