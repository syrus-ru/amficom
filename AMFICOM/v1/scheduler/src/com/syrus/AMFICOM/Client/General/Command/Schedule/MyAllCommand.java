package com.syrus.AMFICOM.Client.General.Command.Schedule;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import com.syrus.io.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.AMFICOM.Client.Test.*;

public class MyAllCommand extends VoidCommand
{
	ScheduleMDIMain parent;
	ApplicationContext aContext;
	Checker checker;

	public MyAllCommand()
	{
	}

	public MyAllCommand(ScheduleMDIMain parent, ApplicationContext aContext)
	{
	  this.parent = parent;
	  this.aContext = aContext;
	  this.checker = new Checker(this.aContext.getDataSourceInterface());
	}

	public Object clone()
	{
		return new MyAllCommand(parent, aContext);
	}

	public void execute()
	{
		if (checker.checkCommand(Checker.openTestPlannerWindow))
		{
			new MyTableCommand(parent, aContext).execute();
			new MyPlanCommand(parent, aContext).execute();
			new MyParamCommand(parent, aContext).execute();
			new MySaveCommand(parent, aContext).execute();
			new MyTreeCommand(parent, aContext).execute();
			new MyTimeCommand(parent, aContext).execute();
		}
	}
}