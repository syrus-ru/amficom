package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.*;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.util.Log;

public class SchemeSaveCommand extends VoidCommand {
	public static final int CANCEL = 0;
	public static final int OK = 1;
	public int ret_code = CANCEL;
	
	ApplicationContext aContext;
	SchemeTabbedPane schemeTab;
	UgoTabbedPane ugoTab;

	public SchemeSaveCommand(ApplicationContext aContext,
			SchemeTabbedPane schemeTab, UgoTabbedPane ugoTab) {
		this.aContext = aContext;
		this.schemeTab = schemeTab;
		this.ugoTab = ugoTab;
	}

	public Object clone() {
		return new SchemeSaveCommand(aContext, schemeTab, ugoTab);
	}

	public void execute() {
		SchemeGraph graph = schemeTab.getGraph();
		SchemeGraph ugograph = ugoTab.getGraph();

		if (graph.getRoots().length == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"Невозможно сохранить пустую схему", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}
		SchemeResource res = schemeTab.getCurrentPanel().getSchemeResource();
		SchemeResource ugores = ugoTab.getCurrentPanel().getSchemeResource();

		if (res.getSchemeElement() != null) // сохраняем компонент
		{
			SchemeElement se = res.getSchemeElement();

			if (se.getSchemeCell() == null) {
				try {
					se.setSchemeCell(SchemeObjectsFactory.createImageResource());
				} catch (CreateObjectException e) {
					Log.errorException(e);
					return;
				}
			}
			se.getSchemeCell().setData((List) graph.getArchiveableState());

			for (Iterator it = schemeTab.getAllPanels().iterator(); it.hasNext();) {
				UgoPanel p = (UgoPanel) it.next();
				Scheme s = p.getSchemeResource().getScheme();
				if (s != null) {
					if (SchemeUtils.isSchemeContainsElement(s, se)) {
						schemeTab.setGraphChanged(p.getGraph(), true);
						JOptionPane
								.showMessageDialog(Environment.getActiveWindow(),
										"Элемент " + se.getName() + " успешно сохранен в схеме "
												+ s.getName(), "Сообщение",
										JOptionPane.INFORMATION_MESSAGE);
						break;
					}
				}
			}
			schemeTab.setGraphChanged(false);
			return;
		}

		Scheme scheme = res.getScheme();
		if (scheme.equals(ugores.getScheme())) {
			if (ugograph.getRoots().length == 0) {
				int ret = JOptionPane
						.showConfirmDialog(
								Environment.getActiveWindow(),
								"Схему нельзя будет включить в другую схему,\nт.к. не создано условное графическое обозначение схемы.\nПродолжить сохранение?",
								"Предупреждение", JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION)
					return;
			}
		} else if (res.getScheme().getUgoCell() == null) {
			int ret = JOptionPane
					.showConfirmDialog(
							Environment.getActiveWindow(),
							"Схему нельзя будет включить в другую схему,\nт.к. не создано условное графическое обозначение схемы.\nПродолжить сохранение?",
							"Предупреждение", JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION)
				return;
		}

		SaveDialog sd;
		while (true) {
			sd = new SaveDialog(aContext, aContext.getDispatcher(),
					"Сохранение схемы");
			int ret = // sd.init(schemePanel.scheme.getName(),
								// schemePanel.scheme.description, false);
			sd.init(scheme, scheme.getName(), false);
			if (ret == 0)
				return;

			if (!MiscUtil.validName(sd.name))
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						"Некорректное название схемы.", "Ошибка", JOptionPane.OK_OPTION);
			else
				break;
		}

		scheme.setName(sd.name);
		scheme.setDescription(sd.description);
		scheme.setSchemeKind(sd.type);

		try {

			if (scheme.getSchemeCell() == null) {
				scheme.setSchemeCell(SchemeObjectsFactory.createImageResource());
			}
			scheme.getSchemeCell().setData((List) graph.getArchiveableState());
			if (scheme.equals(ugores.getScheme())) {
				if (scheme.getUgoCell() == null) {
					scheme.setUgoCell(SchemeObjectsFactory.createImageResource());
				}
				scheme.getUgoCell().setData((List) ugograph.getArchiveableState());
				ugoTab.setGraphChanged(false);
			}
			SchemeStorableObjectPool.putStorableObject(scheme);

			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Схема "
					+ scheme.getName() + " успешно сохранена", "Сообщение",
					JOptionPane.INFORMATION_MESSAGE);

			aContext.getDispatcher().notify(
					new TreeListSelectionEvent("", TreeListSelectionEvent.SELECT_EVENT
							+ TreeListSelectionEvent.REFRESH_EVENT));
			ret_code = OK;
		} catch (ApplicationException ex) {
			Log.errorException(ex);
		}
	}
}
