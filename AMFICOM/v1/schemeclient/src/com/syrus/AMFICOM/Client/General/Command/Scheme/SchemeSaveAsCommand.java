package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

public class SchemeSaveAsCommand extends AbstractCommand {
	public static final int CANCEL = 0;
	public static final int OK = 1;
	public int ret_code = CANCEL;
	
	ApplicationContext aContext;
	SchemeTabbedPane schemeTab;
	private Set<Scheme> childSchemes = new HashSet<Scheme>();

	public SchemeSaveAsCommand(ApplicationContext aContext,
			SchemeTabbedPane schemeTab) {
		this.aContext = aContext;
		this.schemeTab = schemeTab;
	}

	@Override
	public void execute() {
		if (this.schemeTab.getCurrentPanel().isTopLevelSchemeMode()) {
			JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
					LangModelScheme.getString("Message.error.save_top_level"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		
		SchemeGraph graph = this.schemeTab.getGraph();

		long status = SchemeActions.getGraphState(graph);
		if ((status & SchemeActions.SCHEME_EMPTY) != 0) {
			JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
					LangModelScheme.getString("Message.error.empty_scheme"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		if ((status & SchemeActions.SCHEME_HAS_UNGROUPED_DEVICE) != 0) {
			JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
					LangModelScheme.getString("Message.error.ungrouped_device"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		if ((status & SchemeActions.SCHEME_HAS_DEVICE_GROUP) == 0) {
			JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
					LangModelScheme.getString("Message.error.component_not_found"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		
		SchemeResource res = this.schemeTab.getCurrentPanel().getSchemeResource();
		
		 // сохраняем только схему 
		if (res.getCellContainerType() == SchemeResource.SCHEME) {
			String name = JOptionPane.showInputDialog(AbstractMainFrame.getActiveMainFrame(),
					LangModelScheme.getString("Message.input.scheme_name"), //$NON-NLS-1$
					LangModelScheme.getString("Message.input"), //$NON-NLS-1$
					JOptionPane.OK_CANCEL_OPTION);
			
			if (name == null) {
				return;
			}
						
			try {
				Scheme scheme1 = res.getScheme();
				Scheme scheme = scheme1.clone();
				scheme.setName(name);
				Map<Identifier, Identifier> clonedIds = scheme.getClonedIdMap();
				
				SchemeImageResource schemeIr = scheme.getSchemeCell();
				if (schemeIr == null) {
					schemeIr = SchemeObjectsFactory.createSchemeImageResource();
				}
				schemeIr.setData((List) graph.getArchiveableState());
				
				ApplicationContext internalContext =  new ApplicationContext();
				internalContext.setDispatcher(new Dispatcher());
				UgoTabbedPane pane = new UgoTabbedPane(internalContext);
				SchemeGraph invisibleGraph = pane.getGraph();
				
				SchemeActions.writeClonedIds(invisibleGraph, schemeIr, clonedIds);
				
				SchemeImageResource ugoIr = scheme.getUgoCell();
				if (ugoIr != null) {
					SchemeActions.writeClonedIds(invisibleGraph, ugoIr, clonedIds);
				}
				
				// write clonedIds to all schemeImages
				this.childSchemes.clear();
				for (SchemeElement schemeElement : scheme.getSchemeElements(false)) {
					writeIdsToCloneAndChildren(schemeElement, invisibleGraph, clonedIds);
				}

				Identifier userId = LoginManager.getUserId();
				scheme.setParentSchemeElement(null, false);
				scheme.saveChanges(userId);
				for (final Scheme child : this.childSchemes) {
					child.saveChanges(userId);
				}
				
				this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, scheme.getId(), SchemeEvent.CREATE_OBJECT));
				
				if (scheme.getUgoCell() == null) {
					JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
							scheme.getName() + " " + LangModelScheme.getString("Message.information.no_ugo"),  //$NON-NLS-1$ //$NON-NLS-2$
							LangModelScheme.getString("Message.information"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);					
				} else {
					JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
							scheme.getName() + " " + LangModelScheme.getString("Message.information.scheme_saved"),  //$NON-NLS-1$ //$NON-NLS-2$
							LangModelScheme.getString("Message.information"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (CloneNotSupportedException e) {
				JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
						LangModelScheme.getString("Message.error.create_scheme_clone"), //$NON-NLS-1$
						LangModelScheme.getString("Message.error"), //$NON-NLS-1$
						JOptionPane.OK_OPTION);
				Log.errorMessage(e);
			} catch (ApplicationException e) {
				JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
						LangModelScheme.getString("Message.error.save_scheme") + ": " + e.getMessage(), //$NON-NLS-1$ //$NON-NLS-2$
						LangModelScheme.getString("Message.error"), //$NON-NLS-1$
						JOptionPane.OK_OPTION);
				Log.errorMessage(e);
			}
			
		} else {
			JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
					LangModelScheme.getString("Message.error.save_copy_nonscheme"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
		}
	}
	
	/*void writeIdsToCloneAndChildren(Scheme scheme, SchemeGraph graph, Map<Identifier, Identifier> clonedIds) throws ApplicationException {
		SchemeImageResource seSchemeCell = scheme.getSchemeCell();
		if (seSchemeCell != null) {
			SchemeActions.writeClonedIds(graph, seSchemeCell, clonedIds);
		}
		SchemeImageResource seUgoCell = scheme.getSchemeCell();
		if (seUgoCell != null) {
			SchemeActions.writeClonedIds(graph, seUgoCell, clonedIds);
		}
		for (SchemeElement child : scheme.getSchemeElements(false)) {
			writeIdsToCloneAndChildren(child, graph, clonedIds);
		}
	}*/
	
	void writeIdsToCloneAndChildren(SchemeElement schemeElement, SchemeGraph graph, Map<Identifier, Identifier> clonedIds) throws ApplicationException {
		SchemeImageResource seSchemeCell = schemeElement.getSchemeCell();
		if (seSchemeCell != null) {
			SchemeActions.writeClonedIds(graph, seSchemeCell, clonedIds);
		}
		SchemeImageResource seUgoCell = schemeElement.getSchemeCell();
		if (seUgoCell != null) {
			SchemeActions.writeClonedIds(graph, seUgoCell, clonedIds);
		}
		
		/*
		Scheme scheme = schemeElement.getScheme(false);
		if (scheme != null) {
			this.childSchemes.add(scheme);
			writeIdsToCloneAndChildren(scheme, graph, clonedIds);
		}
		*/
		
		for (SchemeElement child : schemeElement.getSchemeElements(false)) {
			writeIdsToCloneAndChildren(child, graph, clonedIds);
		}
	}
	
}
