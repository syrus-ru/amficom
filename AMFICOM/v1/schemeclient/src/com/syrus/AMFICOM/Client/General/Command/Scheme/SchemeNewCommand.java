package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.Scheme;

public class SchemeNewCommand extends VoidCommand
{
	public static final int CANCEL = 0;
	public static final int OK = 1;

	ApplicationContext aContext;

	public int ret_code = CANCEL;

	public SchemeNewCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new SchemeNewCommand(aContext);
	}

	public void execute()
	{
	/*	if (spanel.getGraph().isGraphChanged())
		{
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), "������� ����� �� ���������. ���������?", "����� �����", JOptionPane.YES_NO_CANCEL_OPTION);
			if (ret == JOptionPane.CANCEL_OPTION)
				return;
			if (ret == JOptionPane.YES_OPTION)
			{
				SchemeSaveCommand ssc = new SchemeSaveCommand(aContext, spanel, upanel);
				ssc.execute();
				if (ssc.ret_code == SchemeSaveCommand.CANCEL)
					return;
			}
		}
		else
		{
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), "������� ����� �����?", "����� �����", JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.NO_OPTION)
				return;
		}


		SaveDialog sd;
		while (true)
		{
			//name = JOptionPane.showInputDialog(Environment.getActiveWindow(), "�������� �����", "����� �����", JOptionPane.OK_CANCEL_OPTION);
			sd = new SaveDialog(aContext, aContext.getDispatcher(), "����� �����");
			int ret = sd.init("", "", false);

			if (ret == 0)
				return;

			if (!MiscUtil.validName(sd.name))
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "������������ �������� �����.", "������", JOptionPane.OK_OPTION);
			else
				break;
		}
		*/

		Scheme scheme = Scheme.createInstance();
		scheme.setName("����� �����");

		final Identifier domainId = new Identifier(
				((RISDSessionInfo) aContext
						.getSessionInterface())
						.getAccessIdentifier().domain_id);
		scheme.setDomainId(domainId);

		aContext.getDispatcher().notify(new SchemeElementsEvent(this, scheme,
				SchemeElementsEvent.OPEN_PRIMARY_SCHEME_EVENT));

		ret_code = OK;
	}
}


