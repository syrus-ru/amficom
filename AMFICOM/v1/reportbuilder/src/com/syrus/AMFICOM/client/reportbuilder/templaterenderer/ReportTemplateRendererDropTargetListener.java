/*
 * $Id: ReportTemplateRendererDropTargetListener.java,v 1.1 2005/09/05 12:22:51 peskovsky Exp $
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
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.resource.IntPoint;

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
		if (df[0].getHumanPresentableName().equals(LogicalTreeUI.TRANSFERABLE_OBJECTS))	{
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
				
				TableDataStorableElement element = new TableDataStorableElement(
					itemTransferred.getReportName(),
					itemTransferred.getReportModel(),
					new IntPoint(point.x,point.y),
					1);
				 	
				ReportTemplateDataRenderingComponent component =
					new ReportTemplateDataRenderingComponent(element);
				component.addMouseListener(DRIComponentMouseListener.getInstance());
				component.addMouseMotionListener(DRIComponentMouseMotionListener.getInstance());
					
				this.renderer.add(component);
				component.initMinimumSizes();
				component.setLocation(element.getX(),element.getY());
				component.setSize(new Dimension(ReportTemplateDataRenderingComponent.DEFAULT_SIZE));
				
				this.applicationContext.getDispatcher().firePropertyChange(
						new ReportFlagEvent(this,ReportFlagEvent.REPAINT_RENDERER));			
			} catch (UnsupportedFlavorException e) {
				dtde.getDropTargetContext().dropComplete(false);
			} catch (IOException e) {
				dtde.getDropTargetContext().dropComplete(false);
			}
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
