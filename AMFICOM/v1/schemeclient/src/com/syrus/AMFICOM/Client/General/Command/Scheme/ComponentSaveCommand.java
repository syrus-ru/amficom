package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

public class ComponentSaveCommand extends AbstractCommand {
	// ApplicationContext aContext;
	ElementsTabbedPane cellPane;

	public ComponentSaveCommand(ElementsTabbedPane cellPane) {
		// this.aContext = aContext;
		this.cellPane = cellPane;
	}

	@Override
	public void execute() {
		SchemeGraph graph = this.cellPane.getGraph();

		long status = SchemeActions.getGraphState(graph);
		if ((status & SchemeActions.SCHEME_EMPTY) != 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.empty_scheme"),  //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		if ((status & SchemeActions.SCHEME_HAS_UNGROUPED_DEVICE) != 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.ungrouped_device"),  //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		if ((status & SchemeActions.SCHEME_HAS_DEVICE_GROUP) == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.component_not_found"),  //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}

		SchemeProtoElement proto = null;
		try {
			if (this.cellPane.getCurrentPanel().getSchemeResource().getSchemeProtoElement() != null) {
				proto = this.cellPane.getCurrentPanel().getSchemeResource().getSchemeProtoElement();
			} else {
				// check if the olny DeviceGroup exists
				DeviceGroup[] groups = GraphActions.findTopLevelGroups(graph, graph.getRoots());
				if (groups.length > 1) {
					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							LangModelScheme.getString("Message.error.compound_component"), //$NON-NLS-1$
							LangModelScheme.getString("Message.error"), //$NON-NLS-1$ 
							JOptionPane.OK_OPTION);
					return;
				}
				if ((status & SchemeActions.SCHEME_HAS_UNGROUPED_LINK) != 0) {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							LangModelScheme.getString("Message.error.simple_component_link"),  //$NON-NLS-1$
							LangModelScheme.getString("Message.error"), //$NON-NLS-1$
							JOptionPane.OK_OPTION);
					return;
				}
				proto = groups[0].getProtoElement();
			}
		} catch (ApplicationException e1) {
			Log.errorMessage(e1);
			return;
		}

//		if (proto.getProtoEquipment() == null) {
//			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
//					LangModelScheme.getString("Message.error.component_type_not_set"),  //$NON-NLS-1$
//					LangModelScheme.getString("Message.error"), //$NON-NLS-1$ 
//					JOptionPane.OK_OPTION);
//			return;
//		}
		
		try {
			proto.getParentSchemeProtoGroup();
		} catch (IllegalStateException e2) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.component_parent_not_set"),  //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		
		if (proto.getParentSchemeProtoElement() != null) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.component_parent_not_set"),  //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		try {
			// add internal objects - SL, SPE
			Set<SchemeLink> schemeLinks = new HashSet<SchemeLink>();
			Set<SchemeProtoElement> schemeProtoElements = new HashSet<SchemeProtoElement>();
			Object[] objects = graph.getRoots();
			for (Object object : objects) {
				if (object instanceof DefaultLink)
					schemeLinks.add(((DefaultLink)object).getSchemeLink());
				else if (object instanceof DeviceGroup) {
					SchemeProtoElement schemeProto = ((DeviceGroup)object).getProtoElement();
					assert schemeProto != null;
					if (!schemeProto.equals(proto)) {
						schemeProtoElements.add(schemeProto);
					} else {
						schemeLinks.addAll(schemeProto.getSchemeLinks(false));
						schemeProtoElements.addAll(schemeProto.getSchemeProtoElements(false));
					}
				}
			}

			proto.setSchemeLinks(schemeLinks, false);
			proto.setSchemeProtoElements(schemeProtoElements, false);

			// create SchemeImageResource
			SchemeImageResource schemeIr = proto.getSchemeCell();
			if (schemeIr == null) {
				schemeIr = SchemeObjectsFactory.createSchemeImageResource();
				proto.setSchemeCell(schemeIr);
			}
			schemeIr.setData((List) graph.getArchiveableState());

			Identifier userId = LoginManager.getUserId();
			StorableObjectPool.flush(proto.getId(), userId, false);
			for (Identifiable identifiable : proto.getReverseDependencies(false)) {
				StorableObjectPool.flush(identifiable, userId, false);
			}
			this.cellPane.setGraphChanged(false);
			
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					proto.getName() + " " + LangModelScheme.getString("Message.information.scheme_saved"),  //$NON-NLS-1$ //$NON-NLS-2$
					LangModelScheme.getString("Message.information"), //$NON-NLS-1$,
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
}
