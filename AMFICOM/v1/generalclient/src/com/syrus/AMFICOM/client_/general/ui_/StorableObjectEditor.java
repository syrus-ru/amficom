/*
 * $Id: StorableObjectEditor.java,v 1.3 2005/05/18 14:01:19 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

/**
 * interface for editing Object's properties
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/05/18 14:01:19 $
 * @module generalclient_v1
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
