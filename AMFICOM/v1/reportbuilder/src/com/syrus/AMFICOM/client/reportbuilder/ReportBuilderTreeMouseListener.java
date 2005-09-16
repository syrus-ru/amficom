/*
 * $Id: ReportBuilderTreeMouseListener.java,v 1.2 2005/09/16 13:26:30 peskovsky Exp $
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

import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportQuickViewEvent;

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
			IconedNode secondLevelNode = (IconedNode)treePath.getPathComponent(1);
			if (!secondLevelNode.getObject().equals(
					ReportTemplateElementsTreeModel.REPORT_DATA_ROOT))
				return;
			
			Object lastNodeObject =
				((IconedNode)(treePath.getLastPathComponent())).getObject();

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
