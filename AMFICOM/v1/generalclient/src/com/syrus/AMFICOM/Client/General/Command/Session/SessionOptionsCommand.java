package com.syrus.AMFICOM.Client.General.Command.Session;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.Session.SessionInfoDialog;

import java.awt.Dimension;
import java.awt.Toolkit;

public class SessionOptionsCommand extends VoidCommand implements Command
{
	private ApplicationContext aContext;

	public SessionOptionsCommand()
	{
	}

	public SessionOptionsCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new SessionOptionsCommand(aContext);
	}

	public void execute()
	{
		SessionInfoDialog sDialog = new SessionInfoDialog(aContext.getSessionInterface());

		sDialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = sDialog.getSize();
		sDialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		sDialog.show();
	}

}
