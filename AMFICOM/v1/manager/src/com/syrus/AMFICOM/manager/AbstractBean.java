/*-
* $Id: AbstractBean.java,v 1.2 2005/07/14 13:16:36 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import javax.swing.JPanel;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;


/**
 * @version $Revision: 1.2 $, $Date: 2005/07/14 13:16:36 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager_v1
 */
public abstract class AbstractBean {

	protected String storableObject;
	protected Validator validator;
	protected JPanel propertyPanel;
	
	protected AbstractBean(final String storableObject,
	                       final Validator validator,
	                       final JPanel propertyPanel) {
		this.storableObject = storableObject;
		this.validator = validator;
		this.propertyPanel = propertyPanel;
	}
	
	public final String getStorableObject() {
		return this.storableObject;
	}
	
	public final Validator getValidator() {
		return this.validator;
	}
	
	public final JPanel getPropertyPanel() {
		return this.propertyPanel;
	}
	
	public abstract void updateEdgeAttributes(DefaultEdge edge, DefaultPort port);
	
}

