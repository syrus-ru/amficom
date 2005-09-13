/*
 * $Id: ReportTemplateRendererDropTargetListener.java,v 1.3 2005/09/13 12:23:11 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.client.map.report.MapReportModel;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.scheme.report.SchemeReportModel;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.AbstractSchemeLink;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;

public class ReportTemplateRendererDropTargetListener implements DropTargetListener {
	private final ReportTemplateRenderer renderer;
	private final ApplicationContext applicationContext;
	
	public ReportTemplateRendererDropTargetListener(
			ReportTemplateRenderer renderer,
			ApplicationContext aContext) {
		this.renderer = renderer;
		this.applicationContext = aContext;

		DropTarget dt = new DropTarget(this.renderer, this);
		dt.setActive(true);
	}
	
	public void drop(DropTargetDropEvent dtde) {
		Point point = dtde.getLocation();

		DataFlavor[] df = dtde.getCurrentDataFlavors();
		Transferable transferable = dtde.getTransferable();
		if (!df[0].getHumanPresentableName().equals(LogicalTreeUI.TRANSFERABLE_OBJECTS))
			return;

		try {
			List transferObjects = (List)transferable.getTransferData(df[(0)]);
			
			String reportModelName = null;
			String reportName = null;
			Object additionalData = null;
			
			Iterator objectsIt = transferObjects.iterator();
			if (objectsIt.hasNext()) {
				Object objectTransferred = objectsIt.next();
				if (objectTransferred instanceof ReportTreeItem) {
					ReportTreeItem item = (ReportTreeItem)objectTransferred;
					reportModelName = item.getReportModel();
					reportName = item.getReportName();
				}
				//Для сиюминутных отчётов по схеме
				else if (	(objectTransferred instanceof Scheme)
						||	(objectTransferred instanceof SchemeElement)
						||	(objectTransferred instanceof AbstractSchemePort)
						||	(objectTransferred instanceof AbstractSchemeLink)
						||	(objectTransferred instanceof SchemePath)) {
					reportModelName = SchemeReportModel.class.getName();
					reportName = SchemeReportModel.SELECTED_OBJECT_CHARS;
					additionalData = ((StorableObject)objectTransferred).getId();
				}
				//Для сиюминутных отчётов по карте				
				else if (	(objectTransferred instanceof com.syrus.AMFICOM.map.Map)
						||	(objectTransferred instanceof PhysicalLink)
						||	(objectTransferred instanceof SiteNode)) {
					reportModelName = MapReportModel.class.getName();
					reportName = SchemeReportModel.SELECTED_OBJECT_CHARS;
					additionalData = ((StorableObject)objectTransferred).getId();
				}
				
				else {
					dtde.rejectDrop();
					return;
				}
				dtde.acceptDrop(DnDConstants.ACTION_MOVE);
				dtde.getDropTargetContext().dropComplete(true);
			}
			this.renderer.createDataComponentWithText(
					reportName,
					reportModelName,
					additionalData,
					point,
					null);
			
			this.applicationContext.getDispatcher().firePropertyChange(
					new ReportFlagEvent(this,ReportFlagEvent.REPAINT_RENDERER));
		} catch (UnsupportedFlavorException e) {
			dtde.getDropTargetContext().dropComplete(false);
		} catch (IOException e) {
			dtde.getDropTargetContext().dropComplete(false);
		}
	}
	
	public void dragEnter(DropTargetDragEvent dtde) {
		//Empty
	}

	public void dragOver(DropTargetDragEvent dtde) {
		//Empty
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		//Empty
	}

	public void dragExit(DropTargetEvent dte) {
		//Empty
	}
}
