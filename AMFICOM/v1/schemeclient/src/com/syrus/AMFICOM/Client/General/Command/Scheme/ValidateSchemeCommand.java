/*-
 * $Id: ValidateSchemeCommand.java,v 1.5.4.1 2006/05/18 17:50:00 bass Exp $
 *
 * Copyright ї 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.AbstractSchemeElement;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.util.Log;

public class ValidateSchemeCommand extends AbstractCommand {
	SchemeTabbedPane cellPane;
	List<String> messages = new LinkedList<String>();
	
	public ValidateSchemeCommand(SchemeTabbedPane cellPane) {
		this.cellPane = cellPane;
	}
	
	@Override
	public void execute() {
		messages.clear();
		ElementsPanel panel = this.cellPane.getCurrentPanel();
		if (panel != null) {
			SchemeResource res = panel.getSchemeResource();
			SchemeGraph graph = panel.getGraph();
			if (res.getCellContainerType() == SchemeResource.SCHEME) {
				try {
					Scheme scheme = res.getScheme();
					
					Set<SchemePath> paths = scheme.getSchemePathsRecursively();
					for (SchemePath path : paths) {
						for (PathElement pe : path.getPathMembers()) {
							try {
								AbstractSchemeElement ase = pe.getAbstractSchemeElement();
								if (ase == null) {
									throw new NullPointerException("Linked AbstractSchemeElement '" + pe.getAbstractSchemeElementId() + "' is null");
								}
							} catch (Throwable t) {
								Log.errorMessage("PathElement " + pe + " is invalid. Caused by:\n" + t.getMessage());
								Log.errorMessage("Deleting " + pe + " Please check consistency of SchemePath " + path);
								pe.setParentPathOwner(null, false);
							}
						}
						
						PathElement last = path.getPathMembers().last();
						if (last.getKind() != IdlKind.SCHEME_ELEMENT) {
							Log.errorMessage("PathElement " + last + " is invalid. Must be SchemeElement kind");
							Log.errorMessage("Deleting " + last + " Please check consistency of SchemePath " + path);
							last.setParentPathOwner(null, false);
						}
					}
					
					Set<SchemeCableLink> scls = scheme.getSchemeCableLinks();
					for (SchemeCableLink scl : scls) {
						DefaultCableLink dcl = SchemeActions.findSchemeCableLinkById(graph, scl.getId());

						if (dcl == null) {
							Log.errorMessage("SchemeCableLink " + scl + " is invalid. It doesn't found on parent schemeGraph. Deleting it.");
							scl.setParentScheme(null);
						}
					}
					
					String[] wells = new String[] {"тк", "пк", "нк", "пик" };
					
					for (SchemeElement se : scheme.getSchemeElements()) {
						final String name = se.getName();
						se.setLabel(name);
						for (int i = 0; i < wells.length; i++) {
							final int n = name.indexOf(wells[i]);
							if (n != -1) {
								se.setLabel(name.substring(n));
								break;
							}	
						}
					}
						
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}

			SchemeActions.isValid(graph);
			this.messages.addAll(SchemeActions.getValidationMessages());
		
			if (this.messages.isEmpty()) {
				JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
						LangModelScheme.getString("Message.information.scheme_valid"), 
						LangModelScheme.getString("Message.information"),
						JOptionPane.INFORMATION_MESSAGE);	
			} else {
				JList messagesList = new JList(this.messages.toArray(new String[this.messages.size()]));
				JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
						new JScrollPane(messagesList), 
						LangModelScheme.getString("Message.information"),
						JOptionPane.INFORMATION_MESSAGE);
			}

		}
	}
}
