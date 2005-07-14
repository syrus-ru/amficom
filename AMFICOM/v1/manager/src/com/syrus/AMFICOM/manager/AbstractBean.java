/*-
* $Id: AbstractBean.java,v 1.1 2005/07/14 10:14:11 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import javax.swing.JPanel;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;

import com.syrus.AMFICOM.general.StorableObject;


/**
 * @version $Revision: 1.1 $, $Date: 2005/07/14 10:14:11 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager_v1
 */
public abstract class AbstractBean {

	protected StorableObject storableObject;
	protected Validator validator;
	protected JPanel propertyPanel;
	
	protected AbstractBean(final StorableObject storableObject,
	                       final Validator validator,
	                       final JPanel propertyPanel) {
		this.storableObject = storableObject;
		this.validator = validator;
		this.propertyPanel = propertyPanel;
	}
	
	public final StorableObject getStorableObject() {
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

