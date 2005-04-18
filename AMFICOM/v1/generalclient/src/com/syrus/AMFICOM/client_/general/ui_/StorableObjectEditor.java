/*
 * $Id: StorableObjectEditor.java,v 1.2 2005/04/18 15:30:58 stas Exp $
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
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/18 15:30:58 $
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
