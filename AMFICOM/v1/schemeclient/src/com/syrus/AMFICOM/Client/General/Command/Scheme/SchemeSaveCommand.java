package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.awt.HeadlessException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.util.Log;

public class SchemeSaveCommand extends AbstractCommand {
	public static final int CANCEL = 0;
	public static final int OK = 1;
	public int ret_code = CANCEL;
	
	ElementsTabbedPane schemeTab;

	public SchemeSaveCommand(ElementsTabbedPane schemeTab) {
		this.schemeTab = schemeTab;
	}

	@Override
	public Object clone() {
		return new SchemeSaveCommand(this.schemeTab);
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

		if (res.getCellContainerType() == SchemeResource.SCHEME_ELEMENT) // сохраняем компонент
		{
			try {
				SchemeElement se = res.getSchemeElement();
				
				// add internal objects - SL, SE
				Set<SchemeLink> schemeLinks = new HashSet<SchemeLink>();
				Set<SchemeElement> schemeElements = new HashSet<SchemeElement>();
				Object[] objects = graph.getRoots();
				for (Object object : objects) {
					if (object instanceof DefaultLink)
						schemeLinks.add(((DefaultLink)object).getSchemeLink());
					else if (object instanceof DeviceGroup) {
						SchemeElement schemeElement = ((DeviceGroup)object).getSchemeElement();
						assert schemeElement != null;
						if (!schemeElement.equals(se))
							schemeElements.add(schemeElement);
					}
				}
				se.setSchemeLinks(schemeLinks, false);
				se.setSchemeElements(schemeElements, false);
				
				//	create SchemeImageResource
				if (se.getSchemeCell() == null) {
					try {
						se.setSchemeCell(SchemeObjectsFactory.createSchemeImageResource());
					} catch (CreateObjectException e) {
						assert Log.errorMessage(e);
						return;
					}
				}
				se.getSchemeCell().setData((List) graph.getArchiveableState());
				
				for (Iterator it = this.schemeTab.getAllPanels().iterator(); it.hasNext();) {
					ElementsPanel p = (ElementsPanel) it.next();
					SchemeResource res1 = p.getSchemeResource();
					if (res1.getCellContainerType() == SchemeResource.SCHEME) {
						Scheme s = res1.getScheme();
						if (s.equals(se.getParentScheme())) {
							// TODO refreshing view (ugo)
							
							this.schemeTab.setGraphChanged(true);
							JOptionPane.showMessageDialog(Environment.getActiveWindow(),
									se.getName() + LangModelScheme.getString("Message.information.element_saved_in_scheme") + s.getName(),  //$NON-NLS-1$
									LangModelScheme.getString("Message.information"), //$NON-NLS-1$
									JOptionPane.INFORMATION_MESSAGE);
							break;
						}
					}
				}
			} catch (ApplicationException e) {
				assert Log.errorMessage(e);
			}
			this.schemeTab.setGraphChanged(false);
			return;
		} else if (res.getCellContainerType() == SchemeResource.SCHEME) { // сохраняем схему

			try {
				Scheme scheme = res.getScheme();
//				scheme.setSchemeLinks(schemeLinks);
//				scheme.setSchemeCableLinks(schemeCableLinks);
//				scheme.setSchemeElements(schemeElements);
			
				//	create SchemeImageResource
				SchemeImageResource schemeIr = scheme.getSchemeCell();
				if (schemeIr == null) {
					schemeIr = SchemeObjectsFactory.createSchemeImageResource();
				}

				long start = System.currentTimeMillis();
				schemeIr.setData((List) graph.getArchiveableState());
				scheme.setSchemeCell(schemeIr);
				assert Log.debugMessage("Scheme cell created for : " + (System.currentTimeMillis() - start) + "ms (" + schemeIr.getImage().length + " bytes)", Level.FINER);
				Identifier userId = LoginManager.getUserId();
				
				Set<Scheme> internalSchemes = new HashSet<Scheme>();
				for (SchemeElement se : scheme.getSchemeElements(false)) {
					Scheme internal = se.getScheme(false);
					if (internal != null && internal.isChanged()) {
						internalSchemes.add(internal);
					}
				}
				StorableObjectPool.flush(scheme.getReverseDependencies(false), userId, false);
				for (Scheme changed : internalSchemes) {
					StorableObjectPool.flush(changed.getReverseDependencies(false), userId, false);
				}
				
				LocalXmlIdentifierPool.flush();
				
				this.schemeTab.setGraphChanged(false);
				
				if (scheme.getUgoCell() == null) {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
							scheme.getName() + " " + LangModelScheme.getString("Message.information.no_ugo"),  //$NON-NLS-1$ //$NON-NLS-2$
							LangModelScheme.getString("Message.information"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);					
				} else {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
							scheme.getName() + " " + LangModelScheme.getString("Message.information.scheme_saved"),  //$NON-NLS-1$ //$NON-NLS-2$
							LangModelScheme.getString("Message.information"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (ApplicationException e) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						LangModelScheme.getString("Message.error.save_scheme") + ": " + e.getMessage(), //$NON-NLS-1$ //$NON-NLS-2$
						LangModelScheme.getString("Message.error"), //$NON-NLS-1$
						JOptionPane.OK_OPTION);
				assert Log.errorMessage(e);
			}
		}
	}
}
