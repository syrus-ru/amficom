/*
 * $Id: ReportTemplateElementsTreeMouseListener.java,v 1.2 2005/09/22 14:50:03 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.map.report.MapReportModel;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.reportbuilder.ModuleMode.MODULE_MODE;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportQuickViewEvent;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.ReportDataChecker;
import com.syrus.AMFICOM.client.scheme.report.SchemeReportModel;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.AbstractSchemeLink;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;

public class ReportTemplateElementsTreeMouseListener implements MouseListener{
	private JTree tree = null;
	private ApplicationContext applicationContext = null;
	public ReportTemplateElementsTreeMouseListener(JTree tree) {
		this.tree = tree;
	}
	
	public void setContext(ApplicationContext aContext) {
		this.applicationContext = aContext;
	}
	
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() >= 2) {
			TreePath treePath = 
				this.tree.getPathForLocation(e.getX(),e.getY());
			
			if (treePath == null)
				return;
			
			if (treePath.getPathCount() < 2)
				//TODO ����������� �������� pathCount - ��� �� ������
				//������� ��������� �������
				return;
			//�������, ��� �� ����� - �������� ������ ��� �������
			IconedNode secondLevelNode = (IconedNode)treePath.getPathComponent(1);
			if (!secondLevelNode.getObject().equals(
					ReportTemplateElementsTreeModel.REPORT_DATA_ROOT))
				return;
			
			Object lastNodeObject =
				((IconedNode)(treePath.getLastPathComponent())).getObject();

			if (!(ReportDataChecker.isObjectInstallable(lastNodeObject)))
				return;
			
			if (ModuleMode.getMode().equals(MODULE_MODE.REPORT_PREVIEW))
				this.applicationContext.getDispatcher().firePropertyChange(
						new ReportFlagEvent(this,ReportFlagEvent.CHANGE_VIEW));
			
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
