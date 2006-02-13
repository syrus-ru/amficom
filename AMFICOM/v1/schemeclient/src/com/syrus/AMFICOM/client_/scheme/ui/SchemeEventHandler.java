/*-
 * $Id: SchemeEventHandler.java,v 1.13 2006/02/13 08:37:29 stas Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.beans.PropertyChangeEvent;

import javax.swing.event.ChangeEvent;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.UI.AbstractEventHandler;
import com.syrus.AMFICOM.client.UI.AbstractPropertiesFrame;
import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.13 $, $Date: 2006/02/13 08:37:29 $
 * @module schemeclient
 */

public class SchemeEventHandler extends AbstractEventHandler {
	public SchemeEventHandler(AbstractPropertiesFrame frame, ApplicationContext aContext) {
		super(frame);
		setContext(aContext);
	}

	@Override
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
			
			/**
			 * ���� ������� ������ ��� ��������� � ����� ��������� �������� ������ �����������
			 * ������� ����� ��������
			 * �� ���� ������ ���� ���:
			 * this.frame.setVisualManager(event.getVisualManager());
			 * StorableObjectEditor editor = this.frame.getCurrentEditor();
			 * if (editor != null) {
			 * 		editor.setObject(event.getSelectedObject());
			 * }
			 * 
			 * 
			 */
			
			if (event.getSelectedObject() instanceof Identifiable) {
				try {
					StorableObject object = StorableObjectPool.getStorableObject(((Identifiable)event.getSelectedObject()).getId(), false);
					
					if (!event.isSelected(ObjectSelectedEvent.INRACK)) {
						this.frame.setVisualManager(event.getVisualManager());
						StorableObjectEditor editor = this.frame.getCurrentEditor();
						if (editor != null)
							editor.setObject(object);
					} else if (event.isSelected(ObjectSelectedEvent.SCHEME_ELEMENT)) {
						SchemeElement se = (SchemeElement)object;
						try {
							if (se.getProtoEquipment().getType().equals(EquipmentType.RACK)) {
								this.frame.setVisualManager(event.getVisualManager());
								StorableObjectEditor editor = this.frame.getCurrentEditor();
								if (editor != null)
									editor.setObject(object);
							} else {
								StorableObjectEditor editor = this.frame.getCurrentEditor();
								if (editor instanceof SchemeCellPanel) {
									SchemeCellPanel panel = (SchemeCellPanel)editor;
									UgoTabbedPane pane = (UgoTabbedPane)panel.getGUI();
									SchemeGraph graph = pane.getGraph();
									DefaultGraphCell cell = SchemeActions.findObjectById(graph, object.getId());
									if (cell instanceof DeviceGroup) {
										cell = GraphActions.getMainCell((DeviceGroup)cell);
									}
									graph.setSelectionCell(cell);
								}
							}
						} catch (ApplicationException e1) {
							Log.errorMessage(e1);
						}
					} else {
						StorableObjectEditor editor = this.frame.getCurrentEditor();
						Object selectedObject = event.getSelectedObject();
						if (editor instanceof SchemeCellPanel 
								&& selectedObject instanceof Identifiable) {
							SchemeCellPanel panel = (SchemeCellPanel)editor;
							UgoTabbedPane pane = (UgoTabbedPane)panel.getGUI();
							SchemeGraph graph = pane.getGraph();
							graph.setSelectionCell(SchemeActions.findObjectById(graph, ((Identifiable)selectedObject).getId()));
						}
					}
				} catch (ApplicationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				this.frame.setVisualManager(event.getVisualManager());
				StorableObjectEditor editor = this.frame.getCurrentEditor();
				if (editor != null) {
					editor.setObject(event.getSelectedObject());
				}
			}
		} else if (e.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent event = (SchemeEvent)e;
			if (event.isType(SchemeEvent.UPDATE_OBJECT)) {
				Object newObject = e.getNewValue();
				StorableObjectEditor editor = this.frame.getCurrentEditor();
				if (editor != null && newObject.equals(editor.getObject())) {
					editor.setObject(editor.getObject());
				}
			}
		}
	}

	public void stateChanged(ChangeEvent e) {
		// empty
	}
}
