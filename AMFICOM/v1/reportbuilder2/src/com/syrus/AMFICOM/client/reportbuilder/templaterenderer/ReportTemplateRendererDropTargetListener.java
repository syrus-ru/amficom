/*
 * $Id: ReportTemplateRendererDropTargetListener.java,v 1.1 2005/12/02 11:37:17 bass Exp $
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
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.util.Log;

public class ReportTemplateRendererDropTargetListener implements DropTargetListener {
	private final ReportTemplateRenderer renderer;
	private final ApplicationContext applicationContext;
	
	public ReportTemplateRendererDropTargetListener(
			ReportTemplateRenderer renderer,
			ApplicationContext aContext) {
		this.renderer = renderer;
		this.applicationContext = aContext;
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
			Identifier additionalData = null;
			
			Iterator objectsIt = transferObjects.iterator();
			if (objectsIt.hasNext()) {
				Object objectTransferred = objectsIt.next();
				
				Map<String,String> reportObjectAttributes = 
					ReportDataChecker.getObjectReportAttributes(objectTransferred);
				if (reportObjectAttributes != null) {
					reportModelName = reportObjectAttributes.get(
							ReportDataChecker.MODEL_CLASS_NAME);
					reportName = reportObjectAttributes.get(
							ReportDataChecker.REPORT_NAME);
					additionalData = ((StorableObject)objectTransferred).getId();
				}
				else if (objectTransferred instanceof ReportTreeItem) {
					ReportTreeItem item = (ReportTreeItem)objectTransferred;
					reportModelName = item.getReportModel();
					reportName = item.getReportName();
				}
				else {
					dtde.rejectDrop();
					return;
				}
				
				dtde.acceptDrop(DnDConstants.ACTION_MOVE);
				dtde.getDropTargetContext().dropComplete(true);
			}
			try {
				this.renderer.createDataComponentWithText(
						reportName,
						reportModelName,
						additionalData,
						point,
						null);
			} catch (Exception e) {
				Log.errorMessage("ReportTemplateRendererDropTargetListener.drop | " + e.getMessage());
				Log.errorException(e);			
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						e.getMessage(),
						I18N.getString("report.Exception.error"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
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
