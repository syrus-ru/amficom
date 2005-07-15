package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.UgoPanel;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.util.Log;

public class SchemeSaveCommand extends AbstractCommand {
	public static final int CANCEL = 0;
	public static final int OK = 1;
	public int ret_code = CANCEL;
	
	SchemeTabbedPane schemeTab;

	public SchemeSaveCommand(SchemeTabbedPane schemeTab) {
		this.schemeTab = schemeTab;
	}

	public Object clone() {
		return new SchemeSaveCommand(schemeTab);
	}

	public void execute() {
		SchemeGraph graph = schemeTab.getGraph();
		ApplicationContext aContext = schemeTab.getContext();

		long status = SchemeActions.getGraphState(graph);
		if ((status & SchemeActions.SCHEME_EMPTY) != 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"Пустая схема!", Constants.ERROR, JOptionPane.OK_OPTION);
			return;
		}
		if ((status & SchemeActions.SCHEME_HAS_UNGROUPED_DEVICE) != 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"Не сгруппированное устройство!", Constants.ERROR,
					JOptionPane.OK_OPTION);
			return;
		}
		if ((status & SchemeActions.SCHEME_HAS_DEVICE_GROUP) == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					Constants.ERROR_COMPONENT_NOT_FOUND, Constants.ERROR,
					JOptionPane.OK_OPTION);
			return;
		}
		
		SchemeResource res = schemeTab.getCurrentPanel().getSchemeResource();

		if (res.getCellContainerType() == SchemeResource.SCHEME_ELEMENT) // сохраняем компонент
		{
			SchemeElement se = res.getSchemeElement();

			if (se.getSchemeCell() == null) {
				try {
					se.setSchemeCell(SchemeObjectsFactory.createSchemeImageResource());
				} catch (CreateObjectException e) {
					Log.errorException(e);
					return;
				}
			}
			se.getSchemeCell().setData((List) graph.getArchiveableState());

			for (Iterator it = schemeTab.getAllPanels().iterator(); it.hasNext();) {
				ElementsPanel p = (ElementsPanel) it.next();
				SchemeResource res1 = p.getSchemeResource();
				if (res1.getCellContainerType() == SchemeResource.SCHEME) {
					Scheme s = res1.getScheme();
					if (SchemeUtils.isSchemeContainsElement(s, se)) {
						schemeTab.setGraphChanged(p.getGraph(), true);
						JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								"Элемент " + se.getName() + " успешно сохранен в схеме " + s.getName(), 
								"Сообщение",
								JOptionPane.INFORMATION_MESSAGE);
						break;
					}
				}
			}
			schemeTab.setGraphChanged(false);
			return;
		} else if (res.getCellContainerType() == SchemeResource.SCHEME) // сохраняем схему
		{
			Scheme scheme = res.getScheme();
			if (scheme.getUgoCell() == null) {
				int ret = JOptionPane.showConfirmDialog(
								Environment.getActiveWindow(),
								"Схему нельзя будет включить в другую схему,\nт.к. не создано условное графическое обозначение схемы.\nПродолжить сохранение?",
								"Предупреждение", 
								JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION)
					return;
			}
			
			try {
				SchemeImageResource schemeIr = scheme.getSchemeCell();
				if (schemeIr == null)
					schemeIr = SchemeObjectsFactory.createSchemeImageResource();
				
				schemeIr.setData((List) graph.getArchiveableState());
				scheme.setSchemeCell(schemeIr);
				StorableObjectPool.flush(scheme.getId(), false);
				schemeTab.setGraphChanged(false);
				
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Схема "
						+ scheme.getName() + " успешно сохранена", "Сообщение",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
		}
	}
}
