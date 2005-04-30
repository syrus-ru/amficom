package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.io.*;
import java.util.Properties;

import java.awt.Cursor;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.io.*;

public class FileOpenAsBellcoreCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;
	private String propertiesFileName = "analysis.properties";

	public FileOpenAsBellcoreCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new FileOpenAsBellcoreCommand(dispatcher, aContext);
	}

	public void execute()
	{
		try
		{
			Checker checker = new Checker(this.aContext.getSessionInterface());
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
		String[] traces = {"sor"};
		chooser.addChoosableFileFilter(new ChoosableFileFilter(traces, "Bellcore Traces"));
		int returnVal = chooser.showOpenDialog(Environment.getActiveWindow());
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			BellcoreReader br = new BellcoreReader();
			BellcoreStructure bs = br.getData(chooser.getSelectedFile());
			if (bs == null)
			{
				JOptionPane.showMessageDialog (Environment.getActiveWindow(),
																			 LangModelAnalyse.getString("messageReadError") + ": " + chooser.getSelectedFile().getAbsolutePath(),
																			 LangModelAnalyse.getString("messageError"),
																			 JOptionPane.OK_OPTION);
				return;
			}
			if (!Heap.hasEmptyAllBSMap())
			{
				if (Heap.getBSPrimaryTrace() != null)
					new FileCloseCommand(aContext).execute();
			}

			bs.title = chooser.getSelectedFile().getName();
			Heap.setBSPrimaryTrace(bs);
			Heap.setActiveContextActivePathIDToEmptyString();
			new AnalysisCommand().execute();
			Heap.primaryTraceOpened(bs);
			Heap.setCurrentTracePrimary();
			dispatcher.notify(new RefUpdateEvent(Heap.PRIMARY_TRACE_KEY, RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));

			try
			{
				properties.setProperty("lastdir", chooser.getSelectedFile().getParent().toLowerCase());
				properties.store(new FileOutputStream(propertiesFileName), null);
			}
			catch (IOException ex)
			{
			}
			Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
}
