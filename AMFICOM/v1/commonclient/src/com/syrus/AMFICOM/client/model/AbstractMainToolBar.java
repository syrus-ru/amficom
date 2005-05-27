/*-
* $Id: AbstractMainToolBar.java,v 1.1 2005/05/27 16:16:11 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.model;

import javax.swing.JToolBar;



/**
 * @version $Revision: 1.1 $, $Date: 2005/05/27 16:16:11 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public abstract class AbstractMainToolBar extends JToolBar implements ApplicationModelListener {

	protected ApplicationModel			aModel;

	public void setModel(ApplicationModel aModel) {
		this.aModel = aModel;
	}

	public ApplicationModel getModel() {
		return this.aModel;
	}

}

