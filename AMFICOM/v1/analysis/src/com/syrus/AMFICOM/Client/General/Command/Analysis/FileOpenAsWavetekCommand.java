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

public class FileOpenAsWavetekCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;
	private String propertiesFileName = "analysis.properties";

	public FileOpenAsWavetekCommand(Dispatcher dispatcher, ApplicationContext aContext)
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
		return new FileOpenAsWavetekCommand(dispatcher, aContext);
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
		chooser.addChoosableFileFilter(new ChoosableFileFilter("tfw", "Wavetek"));
		String[] traces = {"tfw"};
		chooser.addChoosableFileFilter(new ChoosableFileFilter(traces, "Wavetek Traces"));
		int returnVal = chooser.showOpenDialog(Environment.getActiveWindow());
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			WavetekReader wr = new WavetekReader();
			BellcoreStructure bs = wr.getData(chooser.getSelectedFile());
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
					new FileCloseCommand(dispatcher, aContext).execute();
			}

			bs.title = chooser.getSelectedFile().getName();
			Heap.setBSPrimaryTrace(bs);
			Heap.setActiveContextActivePathIDToEmptyString();
			new InitialAnalysisCommand().execute();
			Heap.primaryTraceOpened(bs);

			dispatcher.notify(new RefChangeEvent(RefUpdateEvent.PRIMARY_TRACE, RefChangeEvent.SELECT_EVENT));
			dispatcher.notify(new RefUpdateEvent(RefUpdateEvent.PRIMARY_TRACE, RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));

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
