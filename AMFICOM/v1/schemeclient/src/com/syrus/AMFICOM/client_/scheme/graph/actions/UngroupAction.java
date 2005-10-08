/*
 * $Id: UngroupAction.java,v 1.6 2005/10/08 13:49:03 stas Exp $
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
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
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
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/10/08 13:49:03 $
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
			ArrayList<Object> children = new ArrayList<Object>();
			for (int i = 0; i < cells.length; i++) {
				if (graph.isGroup(cells[i])) {
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
				}
			}

			try {
				Set<Identifiable> toDelete = new HashSet<Identifiable>();
				for (Object cell : groups) {
					DeviceGroup group = (DeviceGroup)cell;
					
					if (group.getType() == DeviceGroup.SCHEME_ELEMENT) {
						SchemeElement seToDelete = group.getSchemeElement();
						toDelete.add(seToDelete);
						
						if (seToDelete.getParentSchemeElement() != null) {
							SchemeElement parentSE = seToDelete.getParentSchemeElement();
							for (SchemeElement child : seToDelete.getSchemeElements(false)) {
								child.setParentSchemeElement(parentSE, false);
							}
							for (SchemeLink child : seToDelete.getSchemeLinks(false)) {
								child.setParentSchemeElement(parentSE, false);
							}
						} else if (seToDelete.getParentScheme() != null) {
							Scheme parentS = seToDelete.getParentScheme();
							for (SchemeElement child : seToDelete.getSchemeElements(false)) {
								child.setParentScheme(parentS, false);
							}
							for (SchemeLink child : seToDelete.getSchemeLinks(false)) {
								child.setParentScheme(parentS, false);
							}
						}
					} else if (group.getType() == DeviceGroup.PROTO_ELEMENT) {
						SchemeProtoElement speToDelete = group.getProtoElement();
						toDelete.add(speToDelete);
						if (speToDelete.getParentSchemeProtoElement() != null) {
							SchemeProtoElement parentSPE = speToDelete.getParentSchemeProtoElement();
							for (SchemeProtoElement child : speToDelete.getSchemeProtoElements(true)) {
								child.setParentSchemeProtoElement(parentSPE, false);
							}
							for (SchemeLink child : speToDelete.getSchemeLinks(false)) {
								child.setParentSchemeProtoElement(parentSPE, false);
							}
						}
					}
				}
				StorableObjectPool.delete(toDelete);
			} catch (ApplicationException e1) {
				Log.errorException(e1);
			}

			graph.getModel().remove(groups.toArray());
			graph.setSelectionCells(children.toArray());
		}
	}
}
