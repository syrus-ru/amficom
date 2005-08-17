/*-
 * $Id: AbstractBean.java,v 1.13 2005/08/17 15:59:40 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.jgraph.graph.DefaultEdge;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.manager.UI.JGraphText;

/**
 * @version $Revision: 1.13 $, $Date: 2005/08/17 15:59:40 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class AbstractBean {

	protected Identifier	id;
	
	protected Validator		validator;

	protected String		codeName;

	protected JPanel		propertyPanel;

	protected AbstractBean() {
		// nothing
	}

	protected AbstractBean(final Identifier id,
			final Validator validator,
			final JPanel propertyPanel) {
		this.id = id;
		this.validator = validator;
		this.propertyPanel = propertyPanel;
	}

	public final Identifier getId() {
		return this.id;
	}

	public boolean isTargetValid(AbstractBean targetBean) {
		return this.validator.isValid(this, targetBean);
	}
	
	public abstract void applyTargetPort(MPort oldPort, MPort newPort); 

	public JPanel getPropertyPanel() {
		return this.propertyPanel;
	}

	public JPopupMenu getMenu(	final JGraphText graph,
								final Object cell) {
		return null;
	}

	public void updateEdgeAttributes(	DefaultEdge edge,
										MPort port) {
		// nothing yet
	}

	protected final void setPropertyPanel(JPanel propertyPanel) {
		this.propertyPanel = propertyPanel;
	}

	protected void setId(Identifier id) {
		this.id = id;
	}

	protected final void setValidator(Validator validator) {
		this.validator = validator;
	}
	
	public final String getCodeName() {
		return this.codeName;
	}
	
	public final void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	
	public abstract String getName();
	
	public abstract void setName(String name);

	@Override
	public String toString() {
		return this.getClass().getName() + " is " + this.codeName + '/' + this.getName() + '/';
	}

}
