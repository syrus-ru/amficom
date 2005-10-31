/*-
 * $$Id: PhysicalLinkTypeEditor.java,v 1.12 2005/10/31 15:29:31 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.UI.AComboBox;
import com.syrus.AMFICOM.client.UI.ColorChooserComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.LineThicknessComboBox;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.client.resource.MiscUtil;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.AMFICOM.resource.IntDimension;

/**
 * @version $Revision: 1.12 $, $Date: 2005/10/31 15:29:31 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class PhysicalLinkTypeEditor extends DefaultStorableObjectEditor {
	PhysicalLinkType type;

	private JPanel jPanel = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel libraryLabel = new JLabel();
	private WrapperedComboBox libraryComboBox = null;
	private JLabel sortLabel = new JLabel();
	private AComboBox sortComboBox = null;
	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	private JLabel colorLabel = new JLabel();
	private ColorChooserComboBox colorComboBox = new ColorChooserComboBox(); 
	
	private JLabel thicknessLabel = new JLabel();
	private LineThicknessComboBox thicknessComboBox = new LineThicknessComboBox(); 
	
	private JLabel dimensionLabel = new JLabel();
	private JPanel dimensionPanel = new JPanel();
	private JLabel xLabel = new JLabel();
	private JTextField mTextField = new JTextField();
	private JTextField nTextField = new JTextField();

	private JButton commitButton = new JButton();

	private NetMapViewer netMapViewer;

	LogicalNetLayer logicalNetLayer;

	static String[] sortNames;
	static {
		sortNames = new String[6];
		sortNames[PhysicalLinkTypeSort._TUNNEL] = 
			I18N.getString("defaulttunnel");
		sortNames[PhysicalLinkTypeSort._COLLECTOR] = 
			I18N.getString("defaultcollector");
		sortNames[PhysicalLinkTypeSort._INDOOR] = 
			I18N.getString("defaultindoor");
		sortNames[PhysicalLinkTypeSort._SUBMARINE] = 
			I18N.getString("defaultsubmarine");
		sortNames[PhysicalLinkTypeSort._OVERHEAD] = 
			I18N.getString("defaultoverhead");
		sortNames[PhysicalLinkTypeSort._UNBOUND] = 
			I18N.getString("cable");
	}
	
	public PhysicalLinkTypeEditor() {
		jbInit();
	}

	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
		this.logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
	}

	private void jbInit() {
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.libraryComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);

		this.sortComboBox = new AComboBox();
		this.sortComboBox.addItem(PhysicalLinkTypeSort.TUNNEL);
		this.sortComboBox.addItem(PhysicalLinkTypeSort.COLLECTOR);
		this.sortComboBox.addItem(PhysicalLinkTypeSort.OVERHEAD);
		this.sortComboBox.addItem(PhysicalLinkTypeSort.SUBMARINE);
		this.sortComboBox.setRenderer(new DefaultListCellRenderer() {
		
			private static final long serialVersionUID = -1525332344329271848L;

			@Override
			public Component getListCellRendererComponent(
					JList list,
					Object value,
					int index,
					boolean isSelected,
					boolean cellHasFocus) {
				if(!(value instanceof PhysicalLinkTypeSort)) {
					return null;
				}
				PhysicalLinkTypeSort sort = (PhysicalLinkTypeSort) value;
				String text = PhysicalLinkTypeEditor.sortNames[sort.value()];
				return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
			}
		
		});

		this.jPanel.setLayout(this.gridBagLayout1);
//		this.jPanel.setName(I18N.getString(MapEditorResourceKeys.TITLE_PROPERTIES));

		this.nameLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_NAME));
		this.libraryLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_IN_LIBRARY));
		this.sortLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_SORT));
		this.descLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_DESCRIPTION));
		this.colorLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_COLOR));
		this.thicknessLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_THICKNESS));

		this.dimensionLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_DIMENSION));
		this.xLabel.setText("X"); //$NON-NLS-1$
		this.dimensionPanel.setLayout(new GridBagLayout());

		this.commitButton.setToolTipText(I18N.getString(ResourceKeys.I18N_COMMIT));
		this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.commitButton.setFocusPainted(false);
		this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commitChanges();
			}
		});

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.5;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.dimensionPanel.add(this.mTextField, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.dimensionPanel.add(this.xLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.5;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.dimensionPanel.add(this.nTextField, constraints);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.nameLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.nameTextField, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.commitButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.libraryLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.libraryComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.sortLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.sortComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.colorLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.colorComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.thicknessLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.thicknessComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.dimensionLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.dimensionPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.descLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(new JScrollPane(this.descTextArea), constraints);

		super.addToUndoableListener(this.nameTextField);
		super.addToUndoableListener(this.libraryComboBox);
		super.addToUndoableListener(this.sortComboBox);
		super.addToUndoableListener(this.descTextArea);
		super.addToUndoableListener(this.colorComboBox);
		super.addToUndoableListener(this.thicknessComboBox);
		super.addToUndoableListener(this.mTextField);
		super.addToUndoableListener(this.nTextField);
	}

	public Object getObject() {
		return this.type;
	}

	public void setObject(Object object) {
		this.type = (PhysicalLinkType )object;

		this.libraryComboBox.removeAllItems();

		if(this.type == null) {
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText(""); //$NON-NLS-1$
			this.libraryComboBox.setEnabled(false);
			this.sortComboBox.setEnabled(false);
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText(""); //$NON-NLS-1$

			this.colorComboBox.setEnabled(false);
			this.thicknessComboBox.setEnabled(false);
			this.mTextField.setText(""); //$NON-NLS-1$
			this.nTextField.setText(""); //$NON-NLS-1$
		}
		else {
			this.mTextField.setText(String.valueOf(this.type.getBindingDimension().getWidth()));
			this.nTextField.setText(String.valueOf(this.type.getBindingDimension().getHeight()));

			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.type.getName());
			
			this.libraryComboBox.setEnabled(true);
			this.libraryComboBox.addElements(this.logicalNetLayer.getMapView().getMap().getMapLibraries());
			this.libraryComboBox.setSelectedItem(this.type.getMapLibrary());

			this.sortComboBox.setEnabled(true);
			this.sortComboBox.setSelectedItem(this.type.getSort());

			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.type.getDescription());

			LinkTypeController linkTypeController = (LinkTypeController)
				LinkTypeController.getInstance();
			
			Color color = linkTypeController.getColor(this.type);
			this.colorComboBox.addItem(color);
			this.thicknessComboBox.setSelectedValue(linkTypeController.getLineSize(this.type));
//			this.styleComboBox.setSelectedItem(physicalLinkController.getStyle(this.link);
		}
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	@Override
	public void commitChanges() {
		final ApplicationContext aContext = this.netMapViewer.getLogicalNetLayer().getContext();
		if(!aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_OPERATION_PROHIBITED_IN_MODULE)));
			return;
		}
		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION)));
			return;
		}
//		if(this.type.getMapLibrary().equals(MapLibraryController.getDefaultMapLibrary())) {
//			// cannot commit default types
//			return;
//		}
		String name = this.nameTextField.getText();
		if(MiscUtil.validName(name)) {
			try 
			{
				if(!name.equals(this.type.getName()))
					this.type.setName(name);
				this.type.setMapLibrary((MapLibrary )this.libraryComboBox.getSelectedItem());
				this.type.setDescription(this.descTextArea.getText());
				this.type.setSort((PhysicalLinkTypeSort )this.sortComboBox.getSelectedItem());
	
				LinkTypeController linkTypeController = (LinkTypeController)
						LinkTypeController.getInstance();
				Color color = (Color)this.colorComboBox.getSelectedItem();
				if(! color.equals(linkTypeController.getColor(this.type)))
					linkTypeController.setColor(this.type, color);
				int size = this.thicknessComboBox.getSelectedValue();
				if(size != linkTypeController.getLineSize(this.type))
					linkTypeController.setLineSize(this.type, size);

				int m = Integer.parseInt(this.mTextField.getText());
				int n = Integer.parseInt(this.nTextField.getText());
				if(!this.type.getBindingDimension().equals(new IntDimension(m, n))) {
					this.type.setBindingDimension(new IntDimension(m, n));
				}
				StorableObjectPool.flush(this.type, LoginManager.getUserId(), true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		super.commitChanges();
	}
}
