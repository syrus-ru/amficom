/*-
 * $Id: StorableObjectEditor.java,v 1.3 2005/08/19 12:45:55 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

/**
 * @author $Author: bob $
 * @version $Revision: 1.3 $, $Date: 2005/08/19 12:45:55 $
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
	Collection<ChangeListener> getChangeListeners();
}
