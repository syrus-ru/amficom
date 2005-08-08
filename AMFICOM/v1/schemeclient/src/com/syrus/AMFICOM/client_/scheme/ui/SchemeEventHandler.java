/*-
 * $Id: SchemeEventHandler.java,v 1.4 2005/08/08 11:58:07 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.UI.AbstractEventHandler;
import com.syrus.AMFICOM.client.UI.AbstractPropertiesFrame;
import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class SchemeEventHandler extends AbstractEventHandler {
	public SchemeEventHandler(AbstractPropertiesFrame frame, ApplicationContext aContext) {
		super(frame);
		setContext(aContext);
	}

	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(ObjectSelectedEvent.TYPE, this);
			this.aContext.getDispatcher().removePropertyChangeListener(SchemeEvent.TYPE, this);
		}
		this.aContext = aContext;
		this.aContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, this);
		this.aContext.getDispatcher().addPropertyChangeListener(SchemeEvent.TYPE, this);
	}
	
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent event = (ObjectSelectedEvent) e;
			
			this.frame.setVisualManager(event.getVisualManager());
			StorableObjectEditor editor = this.frame.getCurrentEditor();
			if (editor != null)
				editor.setObject(event.getSelectedObject());	
		} else if (e.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent event = (SchemeEvent)e;
			if (event.isType(SchemeEvent.UPDATE_OBJECT)) {
				Object newObject = e.getNewValue();
				StorableObjectEditor editor = this.frame.getCurrentEditor();
				if (editor != null && newObject.equals(editor.getObject())) {
					editor.setObject(newObject);
				}
			}
		}
	}
}
