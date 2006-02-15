/*-
 * $$Id: LayersDialog.java,v 1.15 2006/02/15 11:13:06 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.15 $, $Date: 2006/02/15 11:13:06 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class LayersDialog extends JDialog {

	private static final String LABEL = MapEditorResourceKeys.LABEL_LABEL;

	private static final String LABEL_VISIBLE_COMBO = "labelVisibleCombo"; //$NON-NLS-1$

	private static final String NAME = MapEditorResourceKeys.LABEL_NAME;

	private static final String VISIBLE_COMBO = "visibleCombo"; //$NON-NLS-1$

	private JScrollPane layersPanel = new JScrollPane();

	private JPanel tablePanel = new JPanel();

	private JTable siteLabelTable = new JTable();
	private JTable siteTable = new JTable();
	private JTable linkLabelTable = new JTable();
	private JTable linkTable = new JTable();
	private JTable cableLabelTable = new JTable();
	private JTable cableTable = new JTable();

	private JPanel buttonsPanel = new JPanel();
	private JButton okButton = new JButton();
	private JButton cancelButton = new JButton();

	public static final int RET_OK = 1;
	
	protected int retCode = 0;

	private DefaultTableModel siteTableModel;
	private DefaultTableModel linkTableModel;
	private DefaultTableModel cableTableModel;

	LayerVisibility allLinksVisibility;
	Collection nodeTypeVisibility = new LinkedList();

	LayerVisibility allSitesVisibility;
	Collection linkTypeVisibility = new LinkedList();

	private LayerVisibility allCablesVisibility;

	private final NetMapViewer netMapViewer;

	public LayersDialog(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}

		init();
	}

	private void jbInit() {
		this.setSize(new Dimension(550, 400));
		this.getContentPane().setLayout(new BorderLayout());

		this.okButton.setText(I18N.getString(MapEditorResourceKeys.BUTTON_APPLY));
		this.okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ok();
				}
			});
		this.cancelButton.setText(I18N.getString(MapEditorResourceKeys.BUTTON_CANCEL));
		this.cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cancel();
				}
			});

		DefaultTableModel siteLabelTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		siteLabelTableModel.addColumn(""); //$NON-NLS-1$
		siteLabelTableModel.addRow(new String[] {I18N.getString(MapViewController.ELEMENT_SITENODE)});
		this.siteLabelTable.setModel(siteLabelTableModel);
		this.siteLabelTable.setTableHeader(null);

		DefaultTableModel linkLabelTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		linkLabelTableModel.addColumn(""); //$NON-NLS-1$
		linkLabelTableModel.addRow(new String[] {I18N.getString(MapViewController.ELEMENT_PHYSICALLINK)});
		this.linkLabelTable.setModel(linkLabelTableModel);
		this.linkLabelTable.setTableHeader(null);

		DefaultTableModel cableLabelTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		cableLabelTableModel.addColumn(""); //$NON-NLS-1$
		cableLabelTableModel.addRow(new String[] {I18N.getString(MapViewController.ELEMENT_CABLEPATH)});
		this.cableLabelTable.setModel(cableLabelTableModel);
		this.cableLabelTable.setTableHeader(null);

		this.siteTableModel = new DefaultTableModel() {
		    @Override
				public Class<?> getColumnClass(int columnIndex) {
				if(columnIndex == 0 || columnIndex == 2)
					return LayerVisibility.class;
				return Object.class;
			    }

			@Override
			public void setValueAt(Object aValue, int row, int column) {
				Boolean bool = (Boolean )aValue;
				boolean visible = bool.booleanValue();
				if(row == 0) {
					if(column == 0) {
						LayersDialog.this.allSitesVisibility.setVisible(visible);
						LayersDialog.this.allSitesVisibility.setPartial(false);
						for(Iterator iter = LayersDialog.this.nodeTypeVisibility.iterator(); iter
								.hasNext();) {
							LayerVisibility visibility = (LayerVisibility )iter.next();
							visibility.setVisible(visible);
						}
					} else if(column == 2) {
						LayersDialog.this.allSitesVisibility.setLabelVisible(visible);
						LayersDialog.this.allSitesVisibility.setLabelPartial(false);
						for(Iterator iter = LayersDialog.this.nodeTypeVisibility.iterator(); iter
								.hasNext();) {
							LayerVisibility visibility = (LayerVisibility )iter.next();
							visibility.setLabelVisible(visible);
						}
					}
				} else {
					LayerVisibility lv = (LayerVisibility )getValueAt(row, column);
					if(column == 0)
						lv.setVisible(visible);
					else if(column == 2)
						lv.setLabelVisible(visible);
					
					LayersDialog.this.updateAllSitesVisibility();
				}
				fireTableDataChanged();
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 0 || column == 2;
			}
		};
		this.siteTableModel.addColumn(VISIBLE_COMBO);
		this.siteTableModel.addColumn(NAME);
		this.siteTableModel.addColumn(LABEL_VISIBLE_COMBO);
		this.siteTableModel.addColumn(LABEL);
		this.allSitesVisibility = new LayerVisibility(MapViewController.ELEMENT_SITENODE, true, true);
		this.siteTableModel.addRow(
				new Object[] {
					this.allSitesVisibility, 
					I18N.getString(MapEditorResourceKeys.LABEL_ALL),
					this.allSitesVisibility,
					I18N.getString(MapEditorResourceKeys.LABEL_ALL) } );
		this.siteTable.setModel(this.siteTableModel);
		this.siteTable.setTableHeader(null);
		this.siteTable.setDefaultRenderer(LayerVisibility.class, LayersTableCellRenderer.getInstance());
		this.siteTable.setDefaultEditor(LayerVisibility.class, LayersTableCellEditor.getInstance());

		this.linkTableModel = new DefaultTableModel() {
		    @Override
				public Class<?> getColumnClass(int columnIndex) {
				if(columnIndex == 0 || columnIndex == 2)
					return LayerVisibility.class;
				return Object.class;
			    }

			@Override
			public void setValueAt(Object aValue, int row, int column) {
				Boolean bool = (Boolean )aValue;
				boolean visible = bool.booleanValue();
				if(row == 0) {
					if(column == 0) {
						LayersDialog.this.allLinksVisibility.setVisible(visible);
						LayersDialog.this.allLinksVisibility.setPartial(false);
						for(Iterator iter = LayersDialog.this.linkTypeVisibility.iterator(); iter
								.hasNext();) {
							LayerVisibility visibility = (LayerVisibility )iter.next();
							visibility.setVisible(visible);
						}
					} else if(column == 2) {
						LayersDialog.this.allLinksVisibility.setLabelVisible(visible);
						LayersDialog.this.allLinksVisibility.setLabelPartial(false);
						for(Iterator iter = LayersDialog.this.linkTypeVisibility.iterator(); iter
								.hasNext();) {
							LayerVisibility visibility = (LayerVisibility )iter.next();
							visibility.setLabelVisible(visible);
						}
					}
				} else {
					LayerVisibility lv = (LayerVisibility )getValueAt(row, column);
					if(column == 0)
						lv.setVisible(visible);
					else if(column == 2)
						lv.setLabelVisible(visible);
					
					LayersDialog.this.updateAllLinksVisibility();
				}
				fireTableDataChanged();
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 0 || column == 2;
			}
		};
		this.linkTableModel.addColumn(VISIBLE_COMBO);
		this.linkTableModel.addColumn(NAME);
		this.linkTableModel.addColumn(LABEL_VISIBLE_COMBO);
		this.linkTableModel.addColumn(LABEL);
		this.allLinksVisibility = new LayerVisibility(MapViewController.ELEMENT_PHYSICALLINK, true, true);
		this.linkTableModel.addRow(
				new Object[] {
					this.allLinksVisibility, 
					I18N.getString(MapEditorResourceKeys.LABEL_ALL),
					this.allLinksVisibility,
					I18N.getString(MapEditorResourceKeys.LABEL_ALL) } );
		this.linkTable.setModel(this.linkTableModel);
		this.linkTable.setTableHeader(null);
		this.linkTable.setDefaultRenderer(LayerVisibility.class, LayersTableCellRenderer.getInstance());
		this.linkTable.setDefaultEditor(LayerVisibility.class, LayersTableCellEditor.getInstance());

		this.cableTableModel = new DefaultTableModel() {
		    @Override
				public Class<?> getColumnClass(int columnIndex) {
			if(columnIndex == 0 || columnIndex == 2)
				return Boolean.class;
			return Object.class;
		    }

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 0 || column == 2;
			}
		};
		this.cableTableModel.addColumn(VISIBLE_COMBO);
		this.cableTableModel.addColumn(NAME);
		this.cableTableModel.addColumn(LABEL_VISIBLE_COMBO);
		this.cableTableModel.addColumn(LABEL);
		this.allCablesVisibility = new LayerVisibility(MapViewController.ELEMENT_SITENODE, true, true);
		this.cableTableModel.addRow(
				new Object[] {
					this.allCablesVisibility.getVisible(), 
					I18N.getString(MapEditorResourceKeys.LABEL_ALL),
					this.allCablesVisibility.getLabelVisible(),
					I18N.getString(MapEditorResourceKeys.LABEL_ALL) } );
		this.cableTable.setModel(this.cableTableModel);
		this.cableTable.setTableHeader(null);
		this.cableTable.setDefaultRenderer(Boolean.class, LayersTableCellRenderer.getInstance());
		this.cableTable.setDefaultEditor(Boolean.class, LayersTableCellEditor.getInstance());

		LayersTableCellEditor.getInstance().addCellEditorListener( new CellEditorListener() {
		
			public void editingStopped(ChangeEvent e) {
//				System.out.println("editing stopped. repaint!");
				LayersDialog.this.repaint();
			}

			public void editingCanceled(ChangeEvent e) {
				// nothing to do
			}
		});
		
		this.tablePanel.add(this.siteLabelTable);
		this.tablePanel.add(this.siteTable);
		this.tablePanel.add(this.linkLabelTable);
		this.tablePanel.add(this.linkTable);
		this.tablePanel.add(this.cableLabelTable);
		this.tablePanel.add(this.cableTable);
		this.tablePanel.setLayout(new BoxLayout(this.tablePanel, BoxLayout.Y_AXIS));
		this.layersPanel.getViewport().add(this.tablePanel);
		
		this.buttonsPanel.add(this.okButton, null);
		this.buttonsPanel.add(this.cancelButton, null);

		this.getContentPane().add(this.layersPanel, BorderLayout.NORTH);
		this.getContentPane().add(this.buttonsPanel, BorderLayout.SOUTH);
	}

	private void init() {
		this.siteTable.removeAll();
		this.nodeTypeVisibility.clear();
		Collection nodeTypes = NodeTypeController.getTopologicalNodeTypes(this.netMapViewer.getLogicalNetLayer().getMapView().getMap());
		for(Iterator iter = nodeTypes.iterator(); iter.hasNext();) {
			SiteNodeType type = (SiteNodeType )iter.next();
			LayerVisibility visibility = new LayerVisibility(
					type, 
					MapPropertiesManager.isLayerVisible(type), 
					MapPropertiesManager.isLayerLabelVisible(type));
			this.nodeTypeVisibility.add(visibility);
			this.siteTableModel.addRow(
					new Object[] {
						visibility, 
						type.getName(),
						visibility,
						I18N.getString(MapEditorResourceKeys.LABEL_LABEL) } );
		}
		updateAllSitesVisibility();

		this.linkTable.removeAll();
		this.linkTypeVisibility.clear();
		Collection linkTypes = LinkTypeController.getTopologicalLinkTypes(this.netMapViewer.getLogicalNetLayer().getMapView().getMap());
		for(Iterator iter = linkTypes.iterator(); iter.hasNext();) {
			PhysicalLinkType type = (PhysicalLinkType )iter.next();
			LayerVisibility visibility = new LayerVisibility(
					type, 
					MapPropertiesManager.isLayerVisible(type), 
					MapPropertiesManager.isLayerLabelVisible(type)); 
			this.linkTypeVisibility.add(visibility);
			this.linkTableModel.addRow(
					new Object[] {
						visibility, 
						type.getName(),
						visibility,
						I18N.getString(MapEditorResourceKeys.LABEL_LABEL) } );
		}
		updateAllLinksVisibility();
	}

	private void commit() {
		for(Iterator iter = this.nodeTypeVisibility.iterator(); iter.hasNext();) {
			LayerVisibility visibility = (LayerVisibility )iter.next();
			MapPropertiesManager.setLayerVisible(visibility.getType(), visibility.getVisible().booleanValue());
			MapPropertiesManager.setLayerLabelVisible(visibility.getType(), visibility.getLabelVisible().booleanValue());
		}

		for(Iterator iter = this.linkTypeVisibility.iterator(); iter.hasNext();) {
			LayerVisibility visibility = (LayerVisibility )iter.next();
			MapPropertiesManager.setLayerVisible(visibility.getType(), visibility.getVisible().booleanValue());
			MapPropertiesManager.setLayerLabelVisible(visibility.getType(), visibility.getLabelVisible().booleanValue());
		}
	}

	void ok() {
		commit();
		this.retCode = RET_OK;
		dispose();
	}

	void cancel() {
		dispose();
	}
	
	public int getReturnCode() {
		return this.retCode;
	}

	void updateAllSitesVisibility() {
		boolean isvisible = false;
		boolean ispartial = false;
		boolean islabelvisible = false;
		boolean islabelpartial = false;
		for(Iterator iter = this.nodeTypeVisibility.iterator(); iter
		.hasNext();) {
			LayerVisibility visibility = (LayerVisibility )iter.next();
			if(visibility.getVisible().booleanValue())
				isvisible = true;
			if(!visibility.getVisible().booleanValue())
				ispartial = true;

			if(visibility.getLabelVisible().booleanValue())
				islabelvisible = true;
			if(!visibility.getLabelVisible().booleanValue())
				islabelpartial = true;
		}
		this.allSitesVisibility.setVisible(isvisible);
		this.allSitesVisibility.setPartial(ispartial);
		this.allSitesVisibility.setLabelVisible(islabelvisible);
		this.allSitesVisibility.setLabelPartial(islabelpartial);
	}

	void updateAllLinksVisibility() {
		boolean isvisible = false;
		boolean ispartial = false;
		boolean islabelvisible = false;
		boolean islabelpartial = false;
		for(Iterator iter = this.linkTypeVisibility.iterator(); iter
		.hasNext();) {
			LayerVisibility visibility = (LayerVisibility )iter.next();
			if(visibility.getVisible().booleanValue())
				isvisible = true;
			if(!visibility.getVisible().booleanValue())
				ispartial = true;

			if(visibility.getLabelVisible().booleanValue())
				islabelvisible = true;
			if(!visibility.getLabelVisible().booleanValue())
				islabelpartial = true;
		}
		this.allLinksVisibility.setVisible(isvisible);
		this.allLinksVisibility.setPartial(ispartial);
		this.allLinksVisibility.setLabelVisible(islabelvisible);
		this.allLinksVisibility.setLabelPartial(islabelpartial);
	}
}
