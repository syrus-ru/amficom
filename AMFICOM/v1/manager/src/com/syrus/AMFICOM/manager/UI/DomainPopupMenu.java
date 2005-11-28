/*-
* $Id: DomainPopupMenu.java,v 1.3 2005/11/28 14:47:04 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.beans.DomainBean;
import com.syrus.AMFICOM.manager.beans.NetBeanFactory;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.manager.perspective.DomainPerpective;
import com.syrus.AMFICOM.manager.perspective.DomainsPerspective;
import com.syrus.AMFICOM.manager.viewers.TableBeanUI;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.3 $, $Date: 2005/11/28 14:47:04 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainPopupMenu extends AbstractItemPopupMenu<DomainsPerspective> {	
	
	@Override
	public JPopupMenu getPopupMenu(final DefaultGraphCell cell,
			final DomainsPerspective perspective) {

		if (cell != null) {
			final JPopupMenu popupMenu = new JPopupMenu();			
			
			final AbstractAction enterAction = new AbstractAction(I18N.getString("Manager.Dialog.EnterIntoDomain")) {

				@SuppressWarnings("unqualified-field-access")
				public void actionPerformed(ActionEvent e) {
					
					MPort port = (MPort) cell.getChildAt(0);
					
					List<Port> ports = port.getSources();
					
					if (ports.isEmpty()) {
						JOptionPane.showMessageDialog(popupMenu, 
							I18N.getString("Manager.Error.DomainDoesnotContainNetwork"), 
							I18N.getString("Manager.Error"),
							JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					for(Port port2 : ports) {
						MPort port3 = (MPort) port2;
						Log.debugMessage("DomainBean.getMenu | " + port3, Log.DEBUGLEVEL10);
						AbstractBean bean2 = port3.getBean();
						
						if (bean2 == null || 
								!bean2.getId().startsWith(NetBeanFactory.NET_CODENAME)) {
							JOptionPane.showMessageDialog(popupMenu, 
								I18N.getString("Manager.Error.DomainContainsNotOnlyNetwork"), 
								I18N.getString("Manager.Error"),
								JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					
					final ManagerMainFrame managerMainFrame = perspective.getManagerMainFrame();
					final DomainPerpective domainPerspective = perspective.getDomainPerspective((DomainBean) port.getBean());
					managerMainFrame.setPerspective(domainPerspective);
					
					
					
					
				}
			};
			
			final Icon enterIcon = UIManager.getIcon(TableBeanUI.ENTER_ICON);
			if (enterIcon != null) {
				enterAction.putValue(Action.SMALL_ICON, enterIcon);
			}
			
			popupMenu.add(enterAction);
			return popupMenu;
		}

		return null;
	
	}
	
}

