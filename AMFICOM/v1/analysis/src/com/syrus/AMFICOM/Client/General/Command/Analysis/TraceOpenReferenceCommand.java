package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JFileChooser;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.io.BellcoreStructure;
import com.syrus.io.IniFile;
import com.syrus.io.TraceReader;

public class TraceOpenReferenceCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private BellcoreStructure bs;
	private ApplicationContext aContext;
	private Checker checker;

	public TraceOpenReferenceCommand(Dispatcher dispatcher, ApplicationContext aContext)
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
		return new TraceOpenReferenceCommand(dispatcher, aContext);
	}

	public void execute()
	{
		try
		{
			this.checker = new Checker(this.aContext.getSessionInterface());
			if(!checker.checkCommand(checker.loadReferenceTrace))
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
		chooser.addChoosableFileFilter(new ChoosableFileFilter("sor", "Bellcore"));
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			String id = chooser.getSelectedFile().getAbsolutePath().toLowerCase();
			TraceReader tr = new TraceReader();
			bs = tr.getData(chooser.getSelectedFile());
			if (bs == null)
			{
				 System.out.println("Error reading file: " + id);
				 return;
			}
			if (Pool.getHash("bellcorestructure") != null )
			{
				if (Pool.get("bellcorestructure", "referencetrace") != null)
					 new FileRemoveCommand (dispatcher, "referencetrace", aContext).execute();
			}

			String activeRefId = chooser.getSelectedFile().getAbsolutePath().toLowerCase();
			bs.title = activeRefId;
			Pool.put("bellcorestructure", "referencetrace", bs);
			dispatcher.notify(new RefChangeEvent("referencetrace",
											RefChangeEvent.OPEN_EVENT));
			ini.setValue("lastdir", chooser.getSelectedFile().getParent().toLowerCase());
		}
	}
}