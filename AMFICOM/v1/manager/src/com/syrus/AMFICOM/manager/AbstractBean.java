/*-
 * $Id: AbstractBean.java,v 1.5 2005/07/19 09:49:00 bob Exp $
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

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.5 $, $Date: 2005/07/19 09:49:00 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager_v1
 */
public abstract class AbstractBean {

	protected Identifier	storableObject;
	protected Validator		validator;

	protected String		name;
	protected String		codeName;

	protected JPanel		propertyPanel;

	protected AbstractBean() {
		// nothing
	}

	protected AbstractBean(final Identifier storableObject,
			final Validator validator,
			final JPanel propertyPanel) {
		this.storableObject = storableObject;
		this.validator = validator;
		this.propertyPanel = propertyPanel;
	}

	public final Identifier getStorableObject() {
		return this.storableObject;
	}

	public final boolean isTargetValid(AbstractBean targetBean) {
		return this.validator != null ? this.validator.isValid(this, targetBean) :
		// TODO development bypass
				true;
	}

	public JPanel getPropertyPanel() {
		return this.propertyPanel;
	}

	public JPopupMenu getMenu(	final JGraph graph,
								final Object cell) {
		return null;
	}

	public void updateEdgeAttributes(	DefaultEdge edge,
										DefaultPort port) {
		// nothing yet
	}

	protected final void setPropertyPanel(JPanel propertyPanel) {
		this.propertyPanel = propertyPanel;
	}

	protected final void setStorableObject(Identifier storableObject) {
		this.storableObject = storableObject;
	}

	protected final void setValidator(Validator validator) {
		this.validator = validator;
	}
	
	protected final String getCodeName() {
		return this.codeName;
	}
	
	protected final void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	
	protected final String getName() {
		return this.name;
	}
	
	protected final void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " is " + this.codeName + '/' + this.name + '/';
	}
}
