/*-
 * $Id: DomainBean.java,v 1.7 2005/08/23 15:02:14 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import static com.syrus.AMFICOM.manager.DomainBeanWrapper.KEY_DESCRIPTION;
import static com.syrus.AMFICOM.manager.DomainBeanWrapper.KEY_NAME;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.UI.JGraphText;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/23 15:02:14 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainBean extends Bean {
	
	Domain domain;
	
	@Override
	public JPopupMenu getMenu(	final JGraphText graph,
								final Object cell) {

		if (cell != null) {
			final JPopupMenu popupMenu = new JPopupMenu();

			popupMenu.add(new AbstractAction(LangModelManager.getString("Dialog.EnterIntoDomain")) {

				public void actionPerformed(ActionEvent e) {
					
					MPort port = (MPort) ((DefaultGraphCell) cell).getChildAt(0);
					
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
						
						if (bean2 == null || !bean2.getCodeName().startsWith(NetBeanFactory.NET_CODENAME)) {
							JOptionPane.showMessageDialog(popupMenu, 
								LangModelManager.getString("Error.DomainContainsNotOnlyNetwork"), 
								LangModelManager.getString("Error"),
								JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					
					graph.currentPerspectiveLabel.setText(LangModelManager.getString("Label.SelectedDomain") + ':' + ((DefaultGraphCell) cell).getUserObject());
					
					graph.domainsButton.setEnabled(true);
					
					graph.domainButton.setEnabled(false);
					
					graph.netButton.setEnabled(false);
					
					graph.userButton.setEnabled(true);

					graph.armButton.setEnabled(true);

					graph.rtuButton.setEnabled(true);

					graph.serverButton.setEnabled(true);

					graph.mcmButton.setEnabled(true);
					
					graph.setPerspective(new DomainPerpective(DomainBean.this));
					
					graph.showOnlyDescendants((DefaultGraphCell) cell);
					
					graph.showOnly(new String[] {NetBeanFactory.NET_CODENAME, 
							ObjectEntities.SYSTEMUSER, 
							ARMBeanFactory.ARM_CODENAME, 
							ObjectEntities.KIS, 
							ObjectEntities.SERVER, 
							ObjectEntities.MCM});
					
					
				}
			});
			return popupMenu;
		}

		return null;
	}
	
	@Override
	protected void setId(Identifier storableObject) {
		super.setId(storableObject);
		try {
			this.domain = StorableObjectPool.getStorableObject(this.id, true);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public final String getDescription() {
		return this.domain.getDescription();
	}

	@Override
	public final String getName() {
		return this.domain.getName();
	}
	
	public final void setDescription(String description) {
		String description2 = this.domain.getDescription();
		if (description2 != description &&
				(description2 != null && !description2.equals(description) ||
				!description.equals(description2))) {
			this.domain.setDescription(description);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_DESCRIPTION, description2, description));
		}	
	}
	
	@Override
	public final void setName(String name) {
		String name2 = this.domain.getName();
		if (name2 != name &&
				(name2 != null && !name2.equals(name) ||
				!name.equals(name2))) {
			this.domain.setName(name);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_NAME, name2, name));
		}
	}
	
	@Override
	public void applyTargetPort(MPort oldPort, MPort newPort) {
		Identifier parentId = Identifier.VOID_IDENTIFIER;
		if (newPort != null) {
			parentId = ((DomainBean) newPort.getUserObject()).getId();
		}
		System.out.println("DomainBean.applyTargetPort() | " + domain.getId() + ", set parent " + parentId); 
		this.domain.setDomainId(parentId);
	}
	
}
