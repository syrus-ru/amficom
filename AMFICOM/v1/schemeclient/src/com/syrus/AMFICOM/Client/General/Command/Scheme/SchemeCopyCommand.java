package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.List;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;

public class SchemeCopyCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemePanel schemePanel;
	UgoPanel ugoPanel;

	public SchemeCopyCommand(ApplicationContext aContext, SchemePanel schemePanel, UgoPanel ugoPanel)
	{
		this.aContext = aContext;
		this.schemePanel = schemePanel;
		this.ugoPanel = ugoPanel;
	}

	public Object clone()
	{
		return new SchemeCopyCommand(aContext, schemePanel, ugoPanel);
	}

	public void execute()
	{
		SchemeGraph graph = schemePanel.getGraph();
		SchemeGraph ugo_graph = ugoPanel.getGraph();

		if (graph.getRoots().length == 0)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Невозможно сохранить пустую схему", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		SaveDialog sd;
		Scheme scheme = schemePanel.getGraph().getScheme();
		while (true)
		{

			sd = new SaveDialog(aContext, aContext.getDispatcher(), "Сохранение схемы");
			int ret = sd.init(scheme, scheme.getName(), false);

			if (ret == 0)
				return;

			if (!MiscUtil.validName(sd.name))
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Некорректное название схемы.", "Ошибка", JOptionPane.OK_OPTION);
			else
				break;
		}

		scheme = (Scheme) scheme.clone();
		scheme.getUgoCell().setData((List)ugo_graph.getArchiveableState(ugo_graph.getRoots()));
		scheme.getSchemeCell().setData((List)graph.getArchiveableState(graph.getRoots()));

		scheme.setName(sd.name);
		scheme.setDescription(sd.description);
//		scheme.created = System.currentTimeMillis();

		final Identifier domainId = new Identifier(
				((RISDSessionInfo) aContext
						.getSessionInterface())
						.getAccessIdentifier().domain_id);
		scheme.setDomainId(domainId);

//		if (!res)
//		{
//			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка сохранения схемы " +
//																		scheme.getName(), "Ошибка", JOptionPane.OK_OPTION);
//			return;
//		}

		try {
			SchemeStorableObjectPool.putStorableObject(scheme);

			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Схема " + scheme.getName() + " успешно сохранена",
					"Сообщение",
					JOptionPane.INFORMATION_MESSAGE);

			aContext.getDispatcher().notify(new TreeListSelectionEvent("",
					TreeListSelectionEvent.SELECT_EVENT + TreeListSelectionEvent.REFRESH_EVENT));
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка сохранения схемы " +
																		scheme.getName(), "Ошибка", JOptionPane.OK_OPTION);
		}

		Pool.removeMap("clonedids");
	}
}

