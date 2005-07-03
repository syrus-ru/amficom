/*-
 * $Id: StorableObjectEditor.java,v 1.1 2005/05/25 07:55:08 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 07:55:08 $
 * @module commonclient_v1
 */

public interface StorableObjectEditor {
	/**
	 * @return component witch can render properties 
	 */
	JComponent getGUI();
	/**
	 * @param object - editing object
	 */
	void setObject(Object object);
	/**
	 * @return editing object
	 */
	Object getObject();
	/**
	 * commit changes made by editor 
	 */
	void commitChanges();

	void addChangeListener(ChangeListener listener);
	void removeChangeListener(ChangeListener listener);
}
