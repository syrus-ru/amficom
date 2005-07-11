/*-
 * $Id: SchemeEventHandler.java,v 1.2 2005/07/11 12:31:39 stas Exp $
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
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/07/11 12:31:39 $
 * @module schemeclient_v1
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
			
			frame.setVisualManager(event.getVisualManager());
			StorableObjectEditor editor = frame.getCurrentEditor();
			if (editor != null)
				editor.setObject(event.getSelectedObject());	
		} else if (e.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent event = (SchemeEvent)e;
			if (event.isType(SchemeEvent.UPDATE_OBJECT)) {
				Object newObject = e.getNewValue();
				StorableObjectEditor editor = frame.getCurrentEditor();
				if (editor != null && newObject.equals(editor.getObject())) {
					editor.setObject(newObject);
				}
			}
		}
	}
}
