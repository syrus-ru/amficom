package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.io.*;
import java.util.Properties;

import javax.swing.JFileChooser;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrameSimplified;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.io.*;

public class FileSaveAsTextCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;
	private String propertiesFileName = "analysis.properties";

	public FileSaveAsTextCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new FileSaveAsTextCommand(dispatcher, aContext);
	}

	public void execute()
	{
		if (!AnalyseMainFrameSimplified.DEBUG) // XXX: saa: security bypass
		{
			try
			{
				Checker checker = new Checker(this.aContext.getSessionInterface());
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
		chooser.addChoosableFileFilter(new ChoosableFileFilter("txt", "Text Files"));
		int returnVal = chooser.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			BellcoreStructure bs = Heap.getBSPrimaryTrace();
			try
			{
				FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile());
				TextWriter tw = new TextWriter();
				fos.write(tw.write(bs));
				fos.close();
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
