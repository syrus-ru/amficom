/*-
 * $Id: DomainBean.java,v 1.1 2005/08/01 11:32:03 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.manager.UI.JGraphText;

/**
 * @version $Revision: 1.1 $, $Date: 2005/08/01 11:32:03 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainBean extends Bean {

	private Identifier		id;

	private Identifier		parentId;

	@Override
	public JPopupMenu getMenu(	final JGraphText graph,
								final Object cell) {

		if (cell != null) {
			final JPopupMenu popupMenu = new JPopupMenu();

			popupMenu.add(new AbstractAction(LangModelManager.getString("Dialog.EnterIntoDomain")) {

				public void actionPerformed(ActionEvent e) {
					
					DefaultGraphCell cell2 =  (DefaultGraphCell) cell;
					
					MPort port = (MPort) cell2.getChildAt(0);
					
					List<Port> ports = graph.isDirect() ? port.getTargets() : port.getSources();
					
					if (ports.isEmpty()) {
						JOptionPane.showMessageDialog(popupMenu, 
							LangModelManager.getString("Error.DomainDoesnotContainNetwork"), 
							LangModelManager.getString("Error"),
							JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					for(Port port2 : ports) {
						MPort port3 = (MPort) port2;
						AbstractBean bean2 = port3.getBean();
						
						if (bean2 == null || !bean2.getCodeName().equals("Net")) {
							JOptionPane.showMessageDialog(popupMenu, 
								LangModelManager.getString("Error.DomainContainsNotOnlyNetwork"), 
								LangModelManager.getString("Error"),
								JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					
					graph.currentPerspectiveLabel.setText(LangModelManager.getString("Label.SelectedDomain") + ':' + cell2.getUserObject());
					
					graph.domainsButton.setEnabled(true);
					
					graph.domainButton.setEnabled(false);
					
					graph.netButton.setEnabled(false);
					
					graph.userButton.setEnabled(true);

					graph.armButton.setEnabled(true);

					graph.rtuButton.setEnabled(true);

					graph.serverButton.setEnabled(true);

					graph.mcmButton.setEnabled(true);
					
					graph.showOnlyDescendants(cell2);
					
					graph.showOnly(new String[] {"Net", "User", "ARM", "RTU", "Server", "MCM"});
					
					System.out.println("DomainBeanFactory | entered");
				}
			});
			return popupMenu;
		}

		return null;
	}

	@Override
	public boolean isTargetValid(AbstractBean targetBean) {
		boolean result = super.isTargetValid(targetBean);
		if (result) {
			DomainBean domainBean = (DomainBean) targetBean;
			this.setParentId(domainBean.getId());
			
//			System.out.println("DomainBean.isTargetValid() | " + this.id + ", targetBean:" + domainBean.id);
		}
		return result;
	}
	
	public final Identifier getId() {
		return this.id;
	}
	
	public final void setId(Identifier id) {
		this.id = id;
	}
	
	public final Identifier getParentId() {
		return this.parentId;
	}
	
	public final void setParentId(Identifier parentId) {
		this.parentId = parentId;
	}		
}
