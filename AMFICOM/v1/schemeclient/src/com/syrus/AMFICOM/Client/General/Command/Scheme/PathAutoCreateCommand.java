package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;


public class PathAutoCreateCommand extends AbstractCommand
{
/*	ApplicationContext aContext;
	UgoPanel panel;

	public PathAutoCreateCommand(ApplicationContext aContext, UgoPanel panel)
	{
		this.aContext = aContext;
		this.panel = panel;
	}

	public Object clone()
	{
		return new PathAutoCreateCommand(aContext, panel);
	}
	public void setParameter(String field, Object value)
	{
		if (field.equals("panel"))
			panel = (UgoPanel)value;
	}

	public void execute()
	{
		Scheme scheme = panel.getSchemeResource().getScheme();
		SchemePath path = panel.getSchemeResource().getSchemePath();
		if (path.getStartSchemeElement() == null ||
				path.getEndSchemeElement() == null)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
																		"Не введено начальное и/или конечное устройство",
																		"Ошибка",
																		JOptionPane.OK_OPTION);
			return;
		}
		Identifier user_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);
		PathBuilder.getInstance(user_id).explore(scheme, path);
		aContext.getDispatcher().notify(new SchemeNavigateEvent(new SchemePath[] {path}, SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT, true));
	}*/
}

