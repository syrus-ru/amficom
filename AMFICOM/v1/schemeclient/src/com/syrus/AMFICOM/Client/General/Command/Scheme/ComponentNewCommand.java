package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;

public class ComponentNewCommand extends AbstractCommand {
	ApplicationContext aContext;
	UgoTabbedPane cellPane;
	private static int counter = 1; 

	public ComponentNewCommand(ApplicationContext aContext,
			UgoTabbedPane cellPane) {
		this.aContext = aContext;
		this.cellPane = cellPane;
	}

	public Object clone() {
		return new ComponentNewCommand(aContext, cellPane);
	}

	public void execute() {
		SchemeGraph cellGraph = cellPane.getGraph();
		
//		SchemeResource sr = cellPane.getCurrentPanel().getSchemeResource();
//		SchemeProtoElement proto = sr.getSchemeProtoElement();
		
		if (cellGraph.getAll().length != 0) {
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
					"Создать новый компонент?", "Новый компонент",
					JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.NO_OPTION)
				return;
		} 
//		else if (proto != null) {
//			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
//					"Создать новый компонент?", "Новый компонент",
//					JOptionPane.YES_NO_OPTION);
//			if (ret == JOptionPane.NO_OPTION)
//				return;			
//		}
		
//		try {
//			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.EQUIPMENT_TYPE_CODE);
//			EquipmentType eqt = null;
//			Set<EquipmentType> eqTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
//			if (!eqTypes.isEmpty())
//				eqt = eqTypes.iterator().next();
//			
//			proto = SchemeProtoElement.createInstance(LoginManager.getUserId(), "Новый компонент (" + Integer.toString(counter) + ")");
//			proto.setEquipmentType(eqt);
//			counter++;
//			aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, proto, SchemeEvent.OPEN_PROTOELEMENT));
//		} catch (ApplicationException e) {
//			Log.errorException(e);
//		}
		((ElementsPanel)cellPane.getCurrentPanel()).getSchemeResource().setSchemeProtoElement(null);
		GraphActions.clearGraph(cellGraph);
		cellGraph.selectionNotify();
	}
	
	/*
	public void execute() {
		SchemeResource sr = cellPane.getCurrentPanel().getSchemeResource();
		SchemeProtoElement proto = sr.getSchemeProtoElement();
		if (proto != null) {
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
					"Создать новый компонент?", "Новый компонент",
					JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.NO_OPTION)
				return;			
		}
		try {
			proto = SchemeProtoElement.createInstance(LoginManager.getUserId(), "Новый компонент (" + Integer.toString(counter) + ")");
			counter++;
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, proto, SchemeEvent.OPEN_PROTOELEMENT));
		} catch (CreateObjectException e) {
			Log.errorException(e);
		}
	}
	 */
}
