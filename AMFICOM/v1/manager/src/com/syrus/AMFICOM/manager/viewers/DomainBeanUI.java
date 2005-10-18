/*-
* $Id: DomainBeanUI.java,v 1.1 2005/10/18 15:10:39 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

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
 * @version $Revision: 1.1 $, $Date: 2005/10/18 15:10:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
class DomainBeanUI extends TableBeanUI<DomainBean> {
	
	public DomainBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame,
			DomainBeanWrapper.getInstance(),
			new String[] {
				DomainBeanWrapper.KEY_NAME,
				DomainBeanWrapper.KEY_DESCRIPTION},
				"com/syrus/AMFICOM/manager/resources/icons/domain.gif", 
				"com/syrus/AMFICOM/manager/resources/domain2.png");
	}

	public JPopupMenu getPopupMenu(final DomainBean domainBean,
	                               final Object cell) {
		if (cell != null) {
			final JPopupMenu popupMenu = new JPopupMenu();			
			
			final AbstractAction action = new AbstractAction(I18N.getString("Manager.Dialog.EnterIntoDomain")) {

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
								!bean2.getCodeName().startsWith(NetBeanFactory.NET_CODENAME)) {
							JOptionPane.showMessageDialog(popupMenu, 
								I18N.getString("Manager.Error.DomainContainsNotOnlyNetwork"), 
								I18N.getString("Manager.Error"),
								JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					
					managerMainFrame.setPerspective(new DomainPerpective(managerMainFrame, domainBean, cell));				
					
					
					
				}
			};
			
			Icon icon;
			URL resource = DomainBean.class.getClassLoader().getResource("com/syrus/AMFICOM/manager/resources/icons/enter.gif");
			if (resource != null) {
				icon = new ImageIcon(resource);
			}
			
			// TODO
			
			
			popupMenu.add(action);
			return popupMenu;
		}

		return null;
	}
}

