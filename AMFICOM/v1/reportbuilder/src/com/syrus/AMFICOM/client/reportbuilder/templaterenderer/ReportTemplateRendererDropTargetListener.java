/*
 * $Id: ReportTemplateRendererDropTargetListener.java,v 1.2 2005/09/07 08:43:25 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.Dimension;
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

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.AttachedTextComponent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.TextAttachingType;

public class ReportTemplateRendererDropTargetListener implements DropTargetListener {
	private final ReportTemplateRenderer renderer;
	private final ApplicationContext applicationContext;
	
	private static final int HEADER_TOCOMPONENT_DISTANCE = 10;

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
			
			ReportTreeItem itemTransferred = null;
			Iterator objectsIt = transferObjects.iterator();
			if (objectsIt.hasNext()) {
				try {
					itemTransferred = (ReportTreeItem)objectsIt.next();
				} catch (ClassCastException e) {
				}
			}
			
			if (itemTransferred == null)
				dtde.rejectDrop();
			
			dtde.acceptDrop(DnDConstants.ACTION_MOVE);
			dtde.getDropTargetContext().dropComplete(true);

			String reportModelName = itemTransferred.getReportModel();
			String reportName = itemTransferred.getReportName();
			ReportTemplateDataRenderingComponent dataComponent = 
				this.renderer.createReportTemplateDataRenderingComponent(
					reportName,
					reportModelName,
					point);
			DataStorableElement dataElement =
				(DataStorableElement)dataComponent.getElement();
			//Заголовок для элемента шаблона
			AttachedTextComponent headerTextComponent =
				this.renderer.createTextRenderingComponent(point);
			AttachedTextStorableElement headerTextElement =
				(AttachedTextStorableElement)headerTextComponent.getElement();
			
			headerTextComponent.setText(dataComponent.getReportFullName());
			headerTextElement.setText(headerTextComponent.getText());
			
			Dimension textSize = headerTextComponent.getTextSize();
			headerTextComponent.setSize(textSize);
			headerTextElement.setSize(textSize.width,textSize.height);
			
			Point textLocation = new Point(
					dataComponent.getX() + dataComponent.getWidth() / 2
						- headerTextComponent.getWidth() / 2,
					dataComponent.getY() - headerTextComponent.getHeight()
						- HEADER_TOCOMPONENT_DISTANCE);
			headerTextComponent.setLocation(textLocation);
			headerTextElement.setLocation(textLocation.x,textLocation.y);

			headerTextElement.setAttachment(dataElement,TextAttachingType.TO_TOP);
			headerTextElement.setAttachment(dataElement,TextAttachingType.TO_WIDTH_CENTER);			
			
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
