/*-
 * $Id: StorableObjectEditor.java,v 1.2 2005/08/02 13:03:21 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/08/02 13:03:21 $
 * @module commonclient
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
