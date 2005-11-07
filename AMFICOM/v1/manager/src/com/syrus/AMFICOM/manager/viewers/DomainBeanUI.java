/*-
* $Id: DomainBeanUI.java,v 1.2 2005/11/07 15:24:19 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

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
import com.syrus.AMFICOM.manager.AbstractBean;
import com.syrus.AMFICOM.manager.DomainBean;
import com.syrus.AMFICOM.manager.DomainBeanWrapper;
import com.syrus.AMFICOM.manager.DomainPerpective;
import com.syrus.AMFICOM.manager.MPort;
import com.syrus.AMFICOM.manager.NetBeanFactory;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/07 15:24:19 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainBeanUI extends TableBeanUI<DomainBean> {
	
	public DomainBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame,
			DomainBeanWrapper.getInstance(),
			new String[] {
				DomainBeanWrapper.KEY_NAME,
				DomainBeanWrapper.KEY_DESCRIPTION},
				"com/syrus/AMFICOM/manager/resources/icons/domain.gif", 
				"com/syrus/AMFICOM/manager/resources/domain2.png");
	}

	@Override
	public JPopupMenu getPopupMenu(final DomainBean domainBean,
	                               final Object cell) {
		if (cell != null) {
			final JPopupMenu popupMenu = new JPopupMenu();			
			
			final AbstractAction enterAction = new AbstractAction(I18N.getString("Manager.Dialog.EnterIntoDomain")) {

				@SuppressWarnings("unqualified-field-access")
				public void actionPerformed(ActionEvent e) {
					
					MPort port = (MPort) ((DefaultGraphCell) cell).getChildAt(0);
					
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
					
					managerMainFrame.setPerspective(new DomainPerpective(managerMainFrame, domainBean));				
					
					
					
				}
			};
			
			final Icon enterIcon = UIManager.getIcon(ENTER_ICON);
			if (enterIcon != null) {
				enterAction.putValue(Action.SMALL_ICON, enterIcon);
			}
			
			popupMenu.add(enterAction);
			return popupMenu;
		}

		return null;
	}
}

