/*-
 * $Id: DomainBean.java,v 1.11 2005/09/04 15:13:26 bob Exp $
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
import com.syrus.util.Log;

/**
 * @version $Revision: 1.11 $, $Date: 2005/09/04 15:13:26 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainBean extends Bean {
	
	Domain domain;
	
	@Override
	public JPopupMenu getMenu(final Object cell) {

		if (cell != null) {
			final JPopupMenu popupMenu = new JPopupMenu();

			popupMenu.add(new AbstractAction(LangModelManager.getString("Dialog.EnterIntoDomain")) {

				public void actionPerformed(ActionEvent e) {
					
					MPort port = (MPort) ((DefaultGraphCell) cell).getChildAt(0);
					
					List<Port> ports = port.getSources();
					
					if (ports.isEmpty()) {
						JOptionPane.showMessageDialog(popupMenu, 
							LangModelManager.getString("Error.DomainDoesnotContainNetwork"), 
							LangModelManager.getString("Error"),
							JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					for(Port port2 : ports) {
						MPort port3 = (MPort) port2;
						Log.debugMessage("DomainBean.getMenu | " + port3, Log.DEBUGLEVEL09);
						AbstractBean bean2 = port3.getBean();
						
						if (bean2 == null || !bean2.getCodeName().startsWith(NetBeanFactory.NET_CODENAME)) {
							JOptionPane.showMessageDialog(popupMenu, 
								LangModelManager.getString("Error.DomainContainsNotOnlyNetwork"), 
								LangModelManager.getString("Error"),
								JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					
					DomainBean.this.graphText.currentPerspectiveLabel.setText(LangModelManager.getString("Label.SelectedDomain") + ':' + ((DefaultGraphCell) cell).getUserObject());
					
					DomainBean.this.graphText.domainsButton.setEnabled(true);
					
					DomainBean.this.graphText.domainButton.setEnabled(false);
					
					DomainBean.this.graphText.netButton.setEnabled(false);
					
					DomainBean.this.graphText.userButton.setEnabled(true);

					DomainBean.this.graphText.armButton.setEnabled(true);

					DomainBean.this.graphText.rtuButton.setEnabled(true);

					DomainBean.this.graphText.serverButton.setEnabled(true);

					DomainBean.this.graphText.mcmButton.setEnabled(true);
					
					DomainBean.this.graphText.setPerspective(new DomainPerpective(DomainBean.this));
					
					DomainBean.this.graphText.showOnlyDescendants((DefaultGraphCell) cell);
					
					DomainBean.this.graphText.showOnly(new String[] {NetBeanFactory.NET_CODENAME, 
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
		System.out.println("DomainBean.applyTargetPort() | " + this.domain.getId() + ", set parent " + parentId); 
		this.domain.setDomainId(parentId);
	}	

	@Override
	public void dispose() throws ApplicationException {
		Log.debugMessage("DomainBean.dispose | " + this.id, Log.DEBUGLEVEL09);
		StorableObjectPool.delete(this.id);		
	}
}
