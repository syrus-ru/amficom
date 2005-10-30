/*
 * $Id: UngroupAction.java,v 1.9 2005/10/30 15:20:54 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;

import com.jgraph.graph.Port;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.Rack;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/10/30 15:20:54 $
 * @module schemeclient
 */

public class UngroupAction extends AbstractAction {
	private static final long serialVersionUID = -8541542275211709897L;
	UgoTabbedPane pane;

	public UngroupAction(UgoTabbedPane pane) {
		super(Constants.UNGROUP);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = this.pane.getGraph();
		Object[] cells = graph.getSelectionCells();
		if (cells != null) {
			ArrayList<Object> groups = new ArrayList<Object>();
			ArrayList<Object> racks = new ArrayList<Object>();
			ArrayList<Object> children = new ArrayList<Object>();
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] instanceof DeviceGroup) {
					groups.add(cells[i]);
					for (int j = 0; j < graph.getModel().getChildCount(cells[i]); j++) {
						Object child = graph.getModel().getChild(cells[i], j);
						if (!(child instanceof Port))
							children.add(child);
					}
//					if (cells[i] instanceof DeviceGroup) {
//						DeviceGroup group = (DeviceGroup) cells[i];
//						pane.getCurrentPanel().getSchemeResource().setSchemeElement(group.getSchemeElement());
//					}
				} else if (cells[i] instanceof Rack) {
					racks.add(cells[i]);
				}
			}

			try {
				Set<Identifiable> toDelete = new HashSet<Identifiable>();
				
				for (Object cell : racks) {
					SchemeElement seToDelete = ((Rack)cell).getSchemeElement();
					toDelete.add(seToDelete);
					if (seToDelete.getParentScheme() != null) {
						Scheme parentS = seToDelete.getParentScheme();
						for (SchemeElement child : new HashSet<SchemeElement>(seToDelete.getSchemeElements(false))) {
							child.setParentScheme(parentS, false);
						}
						for (SchemeLink child : new HashSet<SchemeLink>(seToDelete.getSchemeLinks(false))) {
							child.setParentScheme(parentS, false);
						}
					}
				}
				
				for (Object cell : groups) {
					DeviceGroup group = (DeviceGroup)cell;
					
					if (group.getType() == DeviceGroup.SCHEME_ELEMENT) {
						SchemeElement seToDelete = group.getSchemeElement();
						toDelete.add(seToDelete);
						
						// Devices and links moves up, inner SE delete
						for (SchemeDevice device : new HashSet<SchemeDevice>(seToDelete.getSchemeDevices(false))) {
							device.setParentSchemeProtoElement(SchemeObjectsFactory.stubProtoElement, false);
						}
						for (SchemeLink link : new HashSet<SchemeLink>(seToDelete.getSchemeLinks(false))) {
							link.setParentSchemeProtoElement(SchemeObjectsFactory.stubProtoElement, false);
						}
												
						if (seToDelete.getParentSchemeElement() != null) {
							SchemeElement parentSE = seToDelete.getParentSchemeElement();
							for (SchemeElement child : new HashSet<SchemeElement>(seToDelete.getSchemeElements(false))) {
								child.setParentSchemeElement(parentSE, false);
							}
							for (SchemeLink child : new HashSet<SchemeLink>(seToDelete.getSchemeLinks(false))) {
								child.setParentSchemeElement(parentSE, false);
							}
						} else if (seToDelete.getParentScheme() != null) {
							Scheme parentS = seToDelete.getParentScheme();
							for (SchemeElement child : new HashSet<SchemeElement>(seToDelete.getSchemeElements(false))) {
								child.setParentScheme(parentS, false);
							}
							for (SchemeLink child : new HashSet<SchemeLink>(seToDelete.getSchemeLinks(false))) {
								child.setParentScheme(parentS, false);
							}
						}
					} else if (group.getType() == DeviceGroup.PROTO_ELEMENT) {
						SchemeProtoElement speToDelete = group.getProtoElement();
						toDelete.add(speToDelete);
						
						// Devices and links moves up, inner SE delete
						for (SchemeDevice device : new HashSet<SchemeDevice>(speToDelete.getSchemeDevices(false))) {
							device.setParentSchemeProtoElement(SchemeObjectsFactory.stubProtoElement, false);
						}
						
						if (speToDelete.getParentSchemeProtoElement() != null) {
							SchemeProtoElement parentSPE = speToDelete.getParentSchemeProtoElement();
							for (SchemeProtoElement child : new HashSet<SchemeProtoElement>(speToDelete.getSchemeProtoElements(true))) {
								child.setParentSchemeProtoElement(parentSPE, false);
							}
							for (SchemeLink child : new HashSet<SchemeLink>(speToDelete.getSchemeLinks(false))) {
								child.setParentSchemeProtoElement(parentSPE, false);
							}
						} else {
							for (SchemeProtoElement spe : new HashSet<SchemeProtoElement>(speToDelete.getSchemeProtoElements(false))) {
								toDelete.add(spe);
							} 
							for (SchemeLink link : new HashSet<SchemeLink>(speToDelete.getSchemeLinks(false))) {
								link.setParentSchemeProtoElement(SchemeObjectsFactory.stubProtoElement, false);
							}
						}
					}
				}
				
				for (Identifiable identifiable : toDelete) {
					this.pane.getContext().getDispatcher().firePropertyChange(
							new SchemeEvent(this, identifiable.getId(), SchemeEvent.DELETE_OBJECT));
				}
				StorableObjectPool.delete(toDelete);
			} catch (ApplicationException e1) {
				assert Log.errorMessage(e1);
			}

			graph.getModel().remove(groups.toArray());
			graph.getModel().remove(racks.toArray());
			graph.setSelectionCells(children.toArray());
		}
	}
}
