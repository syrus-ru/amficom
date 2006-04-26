/*-
 * $Id: ReportLayoutManager.java,v 1.1 2006/04/26 13:04:04 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.report;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.report.TableDataStorableElement;

/**
 * Manages layout of report with possibility to split and move elements to fit page
 * @author Kholshin Stanislav
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2006/04/26 13:04:04 $
 * @module reportclient
 */

public class ReportLayoutManager {
	/**
	 * Performs default layout of report elements taking into account that their 
	 * sizes may differ from ones defined in <code>ReportTepmtate</code>   
	 * @param renderer component containing report elements
	 */
	public static void performLayout (ReportRenderer renderer) {
		performLayout(renderer, null);
	}
	
	/**
	 * Performs default layout of report elements taking into account that their 
	 * sizes may differ from ones defined in <code>ReportTepmtate</code>
	 * @param renderer component containing report elements 
	 * @param margins page bounds. Used if need transfering to next page large
	 * images and tables. Should be <code>null</code> if no paging required 
	 */
	public static void performLayout (ReportRenderer renderer, Dimension margins) {
		// at first sort components by ordinate
		List<RenderingComponent> components = renderer.getRenderingComponents();
		Collections.sort(components, new Comparator<RenderingComponent>(){
			public int compare(RenderingComponent o1, RenderingComponent o2) {
				return o1.getY() - o2.getY();
			}
		});

	  int pages = 1;
	  // actually present components (may changed when splitting tables)
	  List<RenderingComponent> components1 = new ArrayList<RenderingComponent>(components);
		for (RenderingComponent component : components) {
			RenderingComponent upper = getNearestUpperComponent(components1, component);
			if (upper != null) {
				// move component down to distance between template bounds
				component.setY(upper.getY() + upper.getHeight() + distance(component, upper));
			}
			
			if (margins != null) {
				int bottom = component.getY() + component.getHeight();
				// if component bottom exceed margin bounds 
				if (bottom > margins.height * pages) {
					// table split to multiple tables
					if (component instanceof TableDataRenderingComponent) {
						TableDataRenderingComponent initialComponent = (TableDataRenderingComponent)component;
						TableDataStorableElement element = (TableDataStorableElement)initialComponent.getElement(); 

						JTable table = initialComponent.getTable();
						AbstractTableModel model = (AbstractTableModel)table.getModel();
						
						final int initialX = initialComponent.getX();
						final int initialWidth = initialComponent.getWidth();
						final int headerHeight = table.getTableHeader().getPreferredSize().height; 
						final int oneRowHeight = table.getRowHeight(); 
						final int numRowsOnFirstPage = ((margins.height * pages - initialComponent.getY() - headerHeight) / oneRowHeight); // -2
						final int numRowsOnAPage = ((margins.height - headerHeight) / oneRowHeight); // -2						
						
						TableDataRenderingComponent currentComponent = new TableDataRenderingComponent(
								element, 
								new SplittedTableModel(model, 0, numRowsOnFirstPage));
						currentComponent.setX(initialX);
						currentComponent.setY(initialComponent.getY());
						currentComponent.setWidth(initialWidth);
						currentComponent.setHeight(headerHeight + oneRowHeight * (numRowsOnFirstPage));
						
						components1.remove(initialComponent);
						components1.add(currentComponent);
						renderer.remove(initialComponent);
						renderer.add(currentComponent);
						
						int firstRowOnAPage = numRowsOnFirstPage;
						while (true) {
							int lastRowOnAPage = Math.min(model.getRowCount(), firstRowOnAPage + numRowsOnAPage);
							currentComponent = new TableDataRenderingComponent(
									element, 
									new SplittedTableModel(model, firstRowOnAPage, lastRowOnAPage));
							currentComponent.setX(initialX);
							currentComponent.setY(margins.height * pages);
							currentComponent.setWidth(initialWidth);
							currentComponent.setHeight(headerHeight + oneRowHeight * (lastRowOnAPage - firstRowOnAPage));

							components1.add(currentComponent);
							renderer.add(currentComponent);

							pages++;
							if (lastRowOnAPage == model.getRowCount()) {
								break;
							}
							firstRowOnAPage = lastRowOnAPage;
						}
						
					} else { // images and text 
						// if component height less then page height simply move to next page
						if (component.getHeight() <= margins.height) {
							// move to next page with attached text
							if (upper instanceof AttachedTextComponent) {
								upper.setY(margins.height * pages);
								component.setY(upper.getY() + upper.getHeight() + distance(component, upper));
							} else {
								component.setY(margins.height * pages);
							}
							pages++;
						} else { // nothing move just compute pages count
							pages = bottom / margins.height + 1;
						}
					}
				}
			}
		}
	}
	
	private static RenderingComponent getNearestUpperComponent(List<RenderingComponent> components, RenderingComponent component) {
		RenderingComponent nearestUpper = null;
		int minimumDistance = Integer.MAX_VALUE;
		for (RenderingComponent c : components) {
			int distance = distance(component, c);
			if (distance > 0) {
				if (distance < minimumDistance) {
					minimumDistance = distance;
					nearestUpper = c;
				} else if (distance == minimumDistance) {
					if (c.getY() + c.getHeight() > nearestUpper.getY() + nearestUpper.getHeight()) {
						nearestUpper = c;
					}
				}
			}
		}
		return nearestUpper;
	}
	
	private static int distance(RenderingComponent c1, RenderingComponent c2) {
		return c1.getElement().getY() - (c2.getElement().getY() + c2.getElement().getHeight());
	}
}

class SplittedTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 368845024441959888L;
	
	private final AbstractTableModel tableModel;
	private final int startRow;
	private final int endRow;
	
	SplittedTableModel(AbstractTableModel tableModel, int startRow, int endRow) {
		this.tableModel = tableModel;
		this.startRow = startRow;
		this.endRow = endRow;
	}
	
	public int getRowCount() {
		return this.endRow - this.startRow;
	}

	public int getColumnCount() {
		return this.tableModel.getColumnCount();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return this.tableModel.getColumnName(columnIndex);
   }

	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.tableModel.getValueAt(rowIndex + this.startRow, columnIndex);
	}
}

