/*-
* $Id: AbstractBean.java,v 1.4 2005/07/15 11:59:00 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;


/**
 * @version $Revision: 1.4 $, $Date: 2005/07/15 11:59:00 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager_v1
 */
public abstract class AbstractBean {

	protected String	storableObject;
	protected Validator	validator;

	protected JPanel	propertyPanel;
	protected JPopupMenu		menu;
	
	protected AbstractBean() {
		// nothing
	}
	
	protected AbstractBean(final String storableObject,
	                       final Validator validator,
	                       final JPanel propertyPanel,
	                       final JPopupMenu menu) {
		this.storableObject = storableObject;
		this.validator = validator;
		this.propertyPanel = propertyPanel;
		this.menu = menu;
	}
	
	public final String getStorableObject() {
		return this.storableObject;
	}
	
	public final boolean isTargetValid(AbstractBean targetBean) {
		return this.validator != null ?
			this.validator.isValid(this, targetBean) :
//			TODO development bypass
			true;
	}
	
	public final JPanel getPropertyPanel() {
		return this.propertyPanel;
	}
	
	public JPopupMenu getMenu(final JGraph graph, final Object cell) {
		return this.menu;
	}
	
	public void updateEdgeAttributes(DefaultEdge edge, DefaultPort port) {
		// nothing yet
	}

	
	protected final void setMenu(JPopupMenu menu) {
		this.menu = menu;
	}

	
	protected final void setPropertyPanel(JPanel propertyPanel) {
		this.propertyPanel = propertyPanel;
	}

	
	protected final void setStorableObject(String storableObject) {
		this.storableObject = storableObject;
	}

	
	protected final void setValidator(Validator validator) {
		this.validator = validator;
	}


}

