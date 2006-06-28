/*-
 * $Id: StorableObjectEditor.java,v 1.4 2006/06/06 12:39:51 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.util.Collection;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2006/06/06 12:39:51 $
 * @module commonclient
 */

/**
 * @todo parametrize this interaface by {@link StorableObject} after map objects 
 * such as CablePath will extends {@link StorableObject} 
 */
public interface StorableObjectEditor<T extends Identifiable> {
	/**
	 * @return component witch can render properties 
	 */
	JComponent getGUI();
	/**
	 * @param object - editing object
	 */
	void setObject(T object);
	/**
	 * @param objects - editing objects
	 */
	void setObjects(Set<T> objects);
	/**
	 * @return editing object
	 */
	T getObject();
	/**
	 * @return editing objects
	 */
	Set<T> getObjects();
	/**
	 * commit changes made by editor 
	 */
	void commitChanges();

	void addChangeListener(ChangeListener listener);
	void removeChangeListener(ChangeListener listener);
	Collection<ChangeListener> getChangeListeners();
}
