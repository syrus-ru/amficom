package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.List;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Schematics.Elements.SaveComponentDialog;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.*;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

public class ComponentSaveCommand extends AbstractCommand {
	ApplicationContext aContext;
	UgoTabbedPane cellPane;
	UgoTabbedPane ugoPane;

	public ComponentSaveCommand(ApplicationContext aContext,
			UgoTabbedPane cellPane, UgoTabbedPane ugoPane) {
		this.aContext = aContext;
		this.cellPane = cellPane;
		this.ugoPane = ugoPane;
	}

	public Object clone() {
		return new ComponentSaveCommand(aContext, cellPane, ugoPane);
	}

	public void execute() {
		SchemeGraph cellGraph = cellPane.getGraph();
		SchemeGraph ugoGraph = ugoPane.getGraph();
		
		SchemeProtoElement proto = null;
		if (ugoPane.getCurrentPanel().getSchemeResource().getSchemeProtoElement() != null) {
			proto = ugoPane.getCurrentPanel().getSchemeResource().getSchemeProtoElement();
		} else if (cellPane.getCurrentPanel().getSchemeResource().getSchemeProtoElement() != null) {
			proto = cellPane.getCurrentPanel().getSchemeResource().getSchemeProtoElement();
		}
		if (proto == null) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"Компонент не найден",
					"Ошибка", JOptionPane.OK_OPTION);
			return;
		}
		
		if (proto.getEquipmentType() == null) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"Не установлен тип компонента",
					"Ошибка", JOptionPane.OK_OPTION);
			return;
		}
		
		Object[] cells = cellGraph.getAll();
		if (cells != null && cells.length != 0) {
			for (int i = 0; i < cells.length; i++)
				if (cells[i] instanceof DefaultLink || cells[i] instanceof DefaultCableLink) {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								"Все линии связи должны находиться внутри сгруппированного компонента",
								"Ошибка", JOptionPane.OK_OPTION);
					return;
				}
		} else {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"Невозможно сохранить пустой компонент", "Ошибка",
					JOptionPane.OK_OPTION);
		}
		
		DeviceGroup[] groups = GraphActions.findTopLevelGroups(cellGraph, cells);
		if (groups.length > 1) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"Компонент без УГО не может состоять более чем из одной группы элементов",
					"Ошибка", JOptionPane.OK_OPTION);
			return;
		} else if (groups.length == 0){
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"Перед сохранением необходимо сгруппировать элементы", "Ошибка",
					JOptionPane.OK_OPTION);
			return;
		}
		
			
		try {
			SchemeImageResource schemeIr = proto.getSchemeCell();
			if (schemeIr == null)
				schemeIr = SchemeObjectsFactory.createImageResource();
			SchemeImageResource ugoIr = proto.getSchemeCell();
			if (ugoIr == null)
				ugoIr = SchemeObjectsFactory.createImageResource();
			schemeIr.setData((List)cellGraph.getArchiveableState());
			ugoIr.setData((List)ugoGraph.getArchiveableState());
			
			proto.setSchemeCell(schemeIr);
			proto.setUgoCell(ugoIr);
			
			SaveComponentDialog frame = new SaveComponentDialog(aContext);
			frame.init(proto);
		} catch (CreateObjectException e) {
			Log.errorException(e);
		}
	}
}
