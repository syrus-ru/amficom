package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeFactory;
import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.Scheme;

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
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), "Текущая схема не сохранена. Сохранить?", "Новая схема", JOptionPane.YES_NO_CANCEL_OPTION);
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
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), "Создать новую схему?", "Новая схема", JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.NO_OPTION)
				return;
		}


		SaveDialog sd;
		while (true)
		{
			//name = JOptionPane.showInputDialog(Environment.getActiveWindow(), "Название схемы", "Новая схема", JOptionPane.OK_CANCEL_OPTION);
			sd = new SaveDialog(aContext, aContext.getDispatcher(), "Новая схема");
			int ret = sd.init("", "", false);

			if (ret == 0)
				return;

			if (!MiscUtil.validName(sd.name))
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Некорректное название схемы.", "Ошибка", JOptionPane.OK_OPTION);
			else
				break;
		}
		*/

		Scheme scheme = SchemeFactory.createScheme();
		scheme.name("Новая схема");
		try {
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
					getAccessIdentifier().domain_id);
			Domain domain = (Domain)AdministrationStorableObjectPool.getStorableObject(
					domain_id, true);
			scheme.domainImpl(domain);
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}

		aContext.getDispatcher().notify(new SchemeElementsEvent(this, scheme,
				SchemeElementsEvent.OPEN_PRIMARY_SCHEME_EVENT));

		ret_code = OK;
	}
}


