package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.List;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

public class ComponentSaveCommand extends AbstractCommand {
	// ApplicationContext aContext;
	ElementsTabbedPane cellPane;

	public ComponentSaveCommand(ElementsTabbedPane cellPane) {
		// this.aContext = aContext;
		this.cellPane = cellPane;
	}

	public Object clone() {
		return new ComponentSaveCommand(cellPane);
	}

	public void execute() {
		SchemeGraph graph = cellPane.getGraph();
		ApplicationContext aContext = cellPane.getContext();

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

		SchemeProtoElement proto = null;
		if (cellPane.getCurrentPanel().getSchemeResource().getSchemeProtoElement() != null) {
			proto = cellPane.getCurrentPanel().getSchemeResource()
					.getSchemeProtoElement();
		} else {
			// check if the olny DeviceGroup exists
			DeviceGroup[] groups = GraphActions.findAllGroups(graph, graph.getRoots());
			if (groups.length > 1) {
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						"blah-blah-blah! Надо создать УГО или компонент должен быть простым!",
						Constants.ERROR, JOptionPane.OK_OPTION);
				return;
			}
			if ((status & SchemeActions.SCHEME_HAS_LINK) != 0) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						"Простой компонент не может иметь линий связи", Constants.ERROR,
						JOptionPane.OK_OPTION);
				return;
			}
			proto = groups[0].getProtoElement();
		}

		if (proto.getEquipmentType() == null) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"Не установлен тип компонента", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		try {
			SchemeImageResource schemeIr = proto.getSchemeCell();
			if (schemeIr == null)
				schemeIr = SchemeObjectsFactory.createSchemeImageResource();
			schemeIr.setData((List) graph.getArchiveableState());

			proto.setSchemeCell(schemeIr);
			StorableObjectPool.flush(proto.getId(), false);
			cellPane.setGraphChanged(false);
			
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Компонент "
					+ proto.getName() + " успешно сохранен", "Сообщение",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ApplicationException e) {
			Log.errorException(e);
		}
	}
}
