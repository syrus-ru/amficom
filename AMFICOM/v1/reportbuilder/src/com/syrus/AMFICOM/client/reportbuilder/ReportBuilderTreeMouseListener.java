/*
 * $Id: ReportBuilderTreeMouseListener.java,v 1.1 2005/09/13 12:23:11 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.report.ReportModelPool;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportQuickViewEvent;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.ReportTreeItem;
import com.syrus.AMFICOM.client.scheme.report.SchemeReportModel;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.AbstractSchemeLink;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;

public class ReportBuilderTreeMouseListener implements MouseListener{
	private JTree tree = null;
	private ApplicationContext applicationContext = null;
	public ReportBuilderTreeMouseListener(JTree tree) {
		this.tree = tree;
	}
	
	public void setContext(ApplicationContext aContext) {
		this.applicationContext = aContext;
	}
	
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() >= 2) {
			TreePath treePath = 
				this.tree.getPathForLocation(e.getX(),e.getY());
			
			if (treePath.getPathCount() < 4)
				//TODO ќтслеживать значение pathCount - оно от дерева
				//—хемных элементов зависит
				return;
			//—мотрим, что за ветка - элементы отчЄта или шаблона
			PopulatableIconedNode secondLevelNode = 
				(PopulatableIconedNode)treePath.getPathComponent(1);
			if (!secondLevelNode.getObject().equals(
					ReportTemplateElementsTreeModel.REPORT_ELEMENTS_ROOT))
				return;
			
			Object lastNodeObject =
				((PopulatableIconedNode)(treePath.getLastPathComponent())).getObject();

			this.applicationContext.getDispatcher().firePropertyChange(
				new ReportQuickViewEvent(this,lastNodeObject));
		}
	}

	public void mousePressed(MouseEvent e) {
		// Empty
	}
	public void mouseReleased(MouseEvent e) {
		// Empty
	}
	public void mouseEntered(MouseEvent e) {
		// Empty
	}
	public void mouseExited(MouseEvent e) {
		// Empty
	}
}
