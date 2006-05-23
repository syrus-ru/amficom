/*
 * $Id: RTEComponentDragDropListener.java,v 1.3 2006/05/23 15:41:59 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

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
import com.syrus.AMFICOM.report.AbstractDataStorableElement;
import com.syrus.util.Log;

public class RTEComponentDragDropListener implements DropTargetListener {
	private final RTEDataRenderingComponent renderingComponent;
	private final ApplicationContext applicationContext;
	
	public RTEComponentDragDropListener(
			RTEDataRenderingComponent renderingComponent,
			ApplicationContext aContext) {
		this.renderingComponent = renderingComponent;
		this.applicationContext = aContext;
	}
	
	public void drop(DropTargetDropEvent dtde) {
		DataFlavor[] df = dtde.getCurrentDataFlavors();
		Transferable transferable = dtde.getTransferable();
		if (!df[0].getHumanPresentableName().equals(LogicalTreeUI.TRANSFERABLE_OBJECTS))
			return;

		try {
			List transferObjects = (List)transferable.getTransferData(df[(0)]);
			Iterator objectsIt = transferObjects.iterator();
			if (objectsIt.hasNext()) {
				Object objectTransferred = objectsIt.next();
				Map<String,String> reportObjectProperties = 
					ReportDataChecker.getObjectReportAttributes(objectTransferred);
				if (reportObjectProperties == null) {
					dtde.rejectDrop();
					return;
				}
			
				String reportName = reportObjectProperties.get(
						ReportDataChecker.REPORT_NAME);
				String modelClassName = reportObjectProperties.get(
						ReportDataChecker.MODEL_CLASS_NAME);
				AbstractDataStorableElement dsElement =
					(AbstractDataStorableElement) this.renderingComponent.getElement();
				
				if (	!reportName.equals(dsElement.getReportName())
					||	!modelClassName.equals(dsElement.getModelClassName())) {
					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							I18N.getString("report.Exception.wrongDataToInstall"),
							I18N.getString("report.Exception.error"),
							JOptionPane.ERROR_MESSAGE);
					dtde.rejectDrop();
					return;
				}

			
				Identifier additionalData =
					((StorableObject)objectTransferred).getId();
				
				dtde.acceptDrop(DnDConstants.ACTION_MOVE);
				dtde.getDropTargetContext().dropComplete(true);
				AbstractDataStorableElement storableElement = 
					(AbstractDataStorableElement)this.renderingComponent.getElement();
				storableElement.setReportObjectId(additionalData);
				
				try {
					this.renderingComponent.refreshLabels();
				} catch (Exception e) {
					Log.errorMessage("RTEComponentDragDropListener.drop | " + e.getMessage());
					Log.errorMessage(e);			
					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							e.getMessage(),
							I18N.getString("report.Exception.error"),
							JOptionPane.ERROR_MESSAGE);
					return;
				}
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
