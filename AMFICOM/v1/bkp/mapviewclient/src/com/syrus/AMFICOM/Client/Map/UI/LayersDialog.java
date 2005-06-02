/**
 * $Id: LayersDialog.java,v 1.1 2005/05/25 16:15:58 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.UI;

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

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeTypeController;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNodeType;

public class LayersDialog extends JDialog {

	private static final String LABEL = "label";

	private static final String LABEL_VISIBLE_COMBO = "labelVisibleCombo";

	private static final String NAME = "name";

	private static final String VISIBLE_COMBO = "visibleCombo";

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

	public LayersDialog()
	{
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		init();
	}

	private void jbInit()
	{
		this.setSize(new Dimension(550, 400));
		this.getContentPane().setLayout(new BorderLayout());

		this.okButton.setText("Применить");
		this.okButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					ok();
				}
			});
		this.cancelButton.setText("Отменить");
		this.cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					cancel();
				}
			});

		DefaultTableModel siteLabelTableModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		siteLabelTableModel.addColumn("");
		siteLabelTableModel.addRow(new String[] {LangModelMap.getString(MapViewController.ELEMENT_SITENODE)});
		this.siteLabelTable.setModel(siteLabelTableModel);
		this.siteLabelTable.setTableHeader(null);

		DefaultTableModel linkLabelTableModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		linkLabelTableModel.addColumn("");
		linkLabelTableModel.addRow(new String[] {LangModelMap.getString(MapViewController.ELEMENT_PHYSICALLINK)});
		this.linkLabelTable.setModel(linkLabelTableModel);
		this.linkLabelTable.setTableHeader(null);

		DefaultTableModel cableLabelTableModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		cableLabelTableModel.addColumn("");
		cableLabelTableModel.addRow(new String[] {LangModelMap.getString(MapViewController.ELEMENT_CABLEPATH)});
		this.cableLabelTable.setModel(cableLabelTableModel);
		this.cableLabelTable.setTableHeader(null);

		this.siteTableModel = new DefaultTableModel() {
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
					LangModelMap.getString("all"),
					this.allSitesVisibility,
					LangModelMap.getString("all") } );
		this.siteTable.setModel(this.siteTableModel);
		this.siteTable.setTableHeader(null);
		this.siteTable.setDefaultRenderer(Object.class, LayersTableCellRenderer.getInstance());
		this.siteTable.setDefaultEditor(Object.class, LayersTableCellEditor.getInstance());

		this.linkTableModel = new DefaultTableModel() {
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
					LangModelMap.getString("all"),
					this.allLinksVisibility,
					LangModelMap.getString("all") } );
		this.linkTable.setModel(this.linkTableModel);
		this.linkTable.setTableHeader(null);
		this.linkTable.setDefaultRenderer(Object.class, LayersTableCellRenderer.getInstance());
		this.linkTable.setDefaultEditor(Object.class, LayersTableCellEditor.getInstance());

		this.cableTableModel = new DefaultTableModel() {
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
					LangModelMap.getString("all"),
					this.allCablesVisibility.getLabelVisible(),
					LangModelMap.getString("all") } );
		this.cableTable.setModel(this.cableTableModel);
		this.cableTable.setTableHeader(null);
		this.cableTable.setDefaultRenderer(Object.class, LayersTableCellRenderer.getInstance());
		this.cableTable.setDefaultEditor(Object.class, LayersTableCellEditor.getInstance());

		LayersTableCellEditor.getInstance().addCellEditorListener( new CellEditorListener() {
		
			public void editingStopped(ChangeEvent e) {
//				System.out.println("editing stopped. repaint!");
				LayersDialog.this.repaint();
			}

			public void editingCanceled(ChangeEvent e) {
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
		Collection nodeTypes = NodeTypeController.getTopologicalNodeTypes();
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
						LangModelMap.getString("label") } );
		}
		updateAllSitesVisibility();

		this.linkTable.removeAll();
		this.linkTypeVisibility.clear();
		Collection linkTypes = LinkTypeController.getTopologicalLinkTypes();
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
						LangModelMap.getString("label") } );
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
	class LayerVisibility {
		Object type;
		Boolean visible;
		Boolean labelVisible;
		private boolean partial;
		private boolean labelPartial;

		public LayerVisibility(
				Object type,
				boolean visible,
				boolean partial,
				boolean labelVisible,
				boolean labelPartial) {
			this.type = type;
			this.partial = partial;
			this.labelPartial = labelPartial;
			this.visible = new Boolean(visible);
			this.labelVisible = new Boolean(labelVisible);
		}

		public LayerVisibility(
				Object type,
				boolean visible,
				boolean labelVisible) {
			this(type, visible, false, labelVisible, false);
		}

		public Boolean getLabelVisible() {
			return this.labelVisible;
		}

		public void setLabelVisible(boolean labelVisible) {
			this.labelVisible = new Boolean(labelVisible);
		}

		public Object getType() {
			return this.type;
		}

		public void setType(Object type) {
			this.type = type;
		}

		public Boolean getVisible() {
			return this.visible;
		}

		public void setVisible(boolean visible) {
			this.visible = new Boolean(visible);
		}

		public boolean isLabelPartial() {
			return this.labelPartial;
		}

		public void setLabelPartial(boolean labelPartial) {
			this.labelPartial = labelPartial;
		}

		public boolean isPartial() {
			return this.partial;
		}

		public void setPartial(boolean partial) {
			this.partial = partial;
		}
	}
	

