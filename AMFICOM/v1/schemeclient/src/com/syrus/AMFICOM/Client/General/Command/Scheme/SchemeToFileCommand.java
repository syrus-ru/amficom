package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.io.*;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
//import com.syrus.io.DirectoryToFile;

public class SchemeToFileCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;

	FileOutputStream fos;
	PrintWriter pw;

	public SchemeToFileCommand()
	{
	}

	public SchemeToFileCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new SchemeToFileCommand(dispatcher, aContext);
	}

	public void execute()
	{

//dataSource.RemoveFromScheme(id, id[], id[], id[]);
//dataSource.RemoveScheme(id);

//    dataSource.SaveSchemeProtos();
//    dataSource.RemoveSchemeProtos();


//DirectoryToFile.writeAll();


//		DirectoryToFile.readAll();
	}
}

