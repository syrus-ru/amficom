package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
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

	public SchemeSaveAsCommand(ApplicationContext aContext,
			SchemeTabbedPane schemeTab) {
		this.aContext = aContext;
		this.schemeTab = schemeTab;
	}

	@Override
	public Object clone() {
		return new SchemeSaveAsCommand(this.aContext, this.schemeTab);
	}

	@Override
	public void execute() {
		if (this.schemeTab.getCurrentPanel().isTopLevelSchemeMode()) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.save_top_level"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		
		SchemeGraph graph = this.schemeTab.getGraph();

		long status = SchemeActions.getGraphState(graph);
		if ((status & SchemeActions.SCHEME_EMPTY) != 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.empty_scheme"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		if ((status & SchemeActions.SCHEME_HAS_UNGROUPED_DEVICE) != 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.ungrouped_device"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		if ((status & SchemeActions.SCHEME_HAS_DEVICE_GROUP) == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.component_not_found"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		
		SchemeResource res = this.schemeTab.getCurrentPanel().getSchemeResource();
		
		 // сохраняем только схему 
		if (res.getCellContainerType() == SchemeResource.SCHEME) {
			String name = JOptionPane.showInputDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.input.scheme_name"), //$NON-NLS-1$
					LangModelScheme.getString("Message.input"), //$NON-NLS-1$
					JOptionPane.OK_CANCEL_OPTION);
			
			if (name == null) {
				return;
			}
			Scheme scheme = res.getScheme();
			
			try {
				Scheme clone = scheme.clone();
				clone.setName(name);
				Map<Identifier, Identifier> clonedIds = clone.getClonedIdMap();
				
				SchemeImageResource schemeIr = clone.getSchemeCell();
				if (schemeIr == null) {
					schemeIr = SchemeObjectsFactory.createSchemeImageResource();
				}
				schemeIr.setData((List) graph.getArchiveableState());
				
				ApplicationContext internalContext =  new ApplicationContext();
				internalContext.setDispatcher(new Dispatcher());
				UgoTabbedPane pane = new UgoTabbedPane(internalContext);
				SchemeGraph invisibleGraph = pane.getGraph();
				
				SchemeActions.writeClonedIds(invisibleGraph, schemeIr, clonedIds);
				
				SchemeImageResource ugoIr = clone.getUgoCell();
				if (ugoIr != null) {
					SchemeActions.writeClonedIds(invisibleGraph, ugoIr, clonedIds);
				}
				
				// write clonedIds to all schemeImages
				// TODO getSchemeElementsRecursievely
				for (SchemeElement schemeElement : clone.getSchemeElements(false)) {
					SchemeImageResource seSchemeCell = schemeElement.getSchemeCell();
					if (seSchemeCell != null) {
						SchemeActions.writeClonedIds(invisibleGraph, seSchemeCell, clonedIds);
					}
					SchemeImageResource seUgoCell = schemeElement.getSchemeCell();
					if (seUgoCell != null) {
						SchemeActions.writeClonedIds(invisibleGraph, seUgoCell, clonedIds);
					}
				}

				Identifier userId = LoginManager.getUserId();
				StorableObjectPool.flush(scheme.getReverseDependencies(false), userId, false);
				
				this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, clone.getId(), SchemeEvent.CREATE_OBJECT));
				
				if (clone.getUgoCell() == null) {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
							clone.getName() + " " + LangModelScheme.getString("Message.information.no_ugo"),  //$NON-NLS-1$ //$NON-NLS-2$
							LangModelScheme.getString("Message.information"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);					
				} else {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
							clone.getName() + " " + LangModelScheme.getString("Message.information.scheme_saved"),  //$NON-NLS-1$ //$NON-NLS-2$
							LangModelScheme.getString("Message.information"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (CloneNotSupportedException e) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						LangModelScheme.getString("Message.error.create_scheme_clone"), //$NON-NLS-1$
						LangModelScheme.getString("Message.error"), //$NON-NLS-1$
						JOptionPane.OK_OPTION);
				Log.errorException(e);
			} catch (ApplicationException e) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						LangModelScheme.getString("Message.error.save_scheme") + ": " + e.getMessage(), //$NON-NLS-1$ //$NON-NLS-2$
						LangModelScheme.getString("Message.error"), //$NON-NLS-1$
						JOptionPane.OK_OPTION);
				Log.errorException(e);
			}
			
		} else {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.save_copy_nonscheme"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
		}
	}
	
	
}
