package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.io.BellcoreStructure;
import com.syrus.io.IniFile;
import com.syrus.io.WavetekReader;

public class FileOpenAsWavetekCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private BellcoreStructure bs;
	private ApplicationContext aContext;
	private Checker checker;

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
			this.checker = new Checker(this.aContext.getSessionInterface());
			if(!checker.checkCommand(checker.openReflectogrammFile))
			{
				return;
			}
		}
		catch (NullPointerException ex)
		{
			System.out.println("Application context and/or user are not defined");
			return;
		}

		IniFile ini = (IniFile)Pool.get("inifile", "analyse");

		JFileChooser chooser = new JFileChooser(ini.getValue("lastdir"));
		chooser.addChoosableFileFilter(new ChoosableFileFilter("tfw", "Wavetek"));
		String[] traces = {"tfw"};
		chooser.addChoosableFileFilter(new ChoosableFileFilter(traces, "Wavetek Traces"));
		int returnVal = chooser.showOpenDialog(Environment.getActiveWindow());
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			WavetekReader wr = new WavetekReader();
			bs = wr.getData(chooser.getSelectedFile());
			if (bs == null)
			{
				JOptionPane.showMessageDialog (Environment.getActiveWindow(),
																			 LangModelAnalyse.getString("messageReadError") + ": " + chooser.getSelectedFile().getAbsolutePath(),
																			 LangModelAnalyse.getString("messageError"),
																				JOptionPane.OK_OPTION);
				return;
			}
			if (Pool.getHash("bellcorestructure") != null )
			{
				if ((BellcoreStructure)Pool.get("bellcorestructure", "primarytrace") != null)
					new FileCloseCommand(dispatcher, aContext).execute();
			}

			String activeRefId = chooser.getSelectedFile().getAbsolutePath().toLowerCase();
			bs.title = chooser.getSelectedFile().getName();
			Pool.put("bellcorestructure", "primarytrace", bs);
			Pool.put("activecontext", "activepathid", bs.supParams.OT);

			new InitialAnalysisCommand().execute();

			dispatcher.notify(new RefChangeEvent("primarytrace",
											RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
			dispatcher.notify(new RefUpdateEvent("primarytrace", RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));
			ini.setValue("lastdir", chooser.getSelectedFile().getParent().toLowerCase());
		}
	}
}
