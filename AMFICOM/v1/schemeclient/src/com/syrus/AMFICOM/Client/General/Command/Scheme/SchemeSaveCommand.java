package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.UgoPanel;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.util.Log;

public class SchemeSaveCommand extends AbstractCommand {
	public static final int CANCEL = 0;
	public static final int OK = 1;
	public int ret_code = CANCEL;
	
	ApplicationContext aContext;
	SchemeTabbedPane schemeTab;

	public SchemeSaveCommand(ApplicationContext aContext,
			SchemeTabbedPane schemeTab) {
		this.aContext = aContext;
		this.schemeTab = schemeTab;
	}

	public Object clone() {
		return new SchemeSaveCommand(aContext, schemeTab);
	}

	public void execute() {
		SchemeGraph graph = schemeTab.getGraph();

		if (graph.getRoots().length == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"���������� ��������� ������ �����", "������", JOptionPane.OK_OPTION);
			return;
		}
		SchemeResource res = schemeTab.getCurrentPanel().getSchemeResource();

		if (res.getSchemeElement() != null) // ��������� ���������
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
				ElementsPanel p = (ElementsPanel) it.next();
				Scheme s = p.getSchemeResource().getScheme();
				if (s != null) {
					if (SchemeUtils.isSchemeContainsElement(s, se)) {
						schemeTab.setGraphChanged(p.getGraph(), true);
						JOptionPane
								.showMessageDialog(Environment.getActiveWindow(),
										"������� " + se.getName() + " ������� �������� � ����� "
												+ s.getName(), "���������",
										JOptionPane.INFORMATION_MESSAGE);
						break;
					}
				}
			}
			schemeTab.setGraphChanged(false);
			return;
		}

		Scheme scheme = res.getScheme();
		if (res.getScheme().getUgoCell() == null) {
			int ret = JOptionPane
					.showConfirmDialog(
							Environment.getActiveWindow(),
							"����� ������ ����� �������� � ������ �����,\n�.�. �� ������� �������� ����������� ����������� �����.\n���������� ����������?",
							"��������������", JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION)
				return;
		}

		/*
		SaveDialog sd;
		while (true) {
			sd = new SaveDialog(aContext, aContext.getDispatcher(),
					"���������� �����");
			int ret = // sd.init(schemePanel.scheme.getName(),
								// schemePanel.scheme.description, false);
			sd.init(scheme, scheme.getName(), false);
			if (ret == 0)
				return;

			if (!MiscUtil.validName(sd.name))
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						"������������ �������� �����.", "������", JOptionPane.OK_OPTION);
			else
				break;
		}

		scheme.setName(sd.name);
		scheme.setDescription(sd.description);
		scheme.setKind(sd.type);

		try {

			if (scheme.getSchemeCell() == null) {
				scheme.setSchemeCell(SchemeObjectsFactory.createImageResource());
			}
			scheme.getSchemeCell().setData((List) graph.getArchiveableState());
		
			StorableObjectPool.putStorableObject(scheme);

			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "����� "
					+ scheme.getName() + " ������� ���������", "���������",
					JOptionPane.INFORMATION_MESSAGE);

			aContext.getDispatcher().notify(
					new TreeListSelectionEvent("", TreeListSelectionEvent.SELECT_EVENT
							+ TreeListSelectionEvent.REFRESH_EVENT));
			ret_code = OK;
		} catch (ApplicationException ex) {
			Log.errorException(ex);
		}*/
	}
}
