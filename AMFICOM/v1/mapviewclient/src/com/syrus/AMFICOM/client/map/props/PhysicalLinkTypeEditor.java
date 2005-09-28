package com.syrus.AMFICOM.client.map.props;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.ColorChooserComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.LineThicknessComboBox;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.client.resource.MiscUtil;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.PhysicalLinkType;

public class PhysicalLinkTypeEditor extends DefaultStorableObjectEditor {
	PhysicalLinkType type;

	private JPanel jPanel = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel libraryLabel = new JLabel();
	private WrapperedComboBox libraryComboBox = null;
	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	private JLabel colorLabel = new JLabel();
	private ColorChooserComboBox colorComboBox = new ColorChooserComboBox(); 
	
	private JLabel thicknessLabel = new JLabel();
	private LineThicknessComboBox thicknessComboBox = new LineThicknessComboBox(); 
	
	private JLabel styleLabel = new JLabel();
//	private LineStyleComboBox styleComboBox = new LineStyleComboBox(); 

	private JButton commitButton = new JButton();

	private NetMapViewer netMapViewer;

	LogicalNetLayer logicalNetLayer;
	
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

		this.jPanel.setLayout(this.gridBagLayout1);
//		this.jPanel.setName(LangModelGeneral.getString(MapEditorResourceKeys.TITLE_PROPERTIES)); //$NON-NLS-1$

		this.nameLabel.setText(LangModelMap.getString(MapEditorResourceKeys.LABEL_NAME)); //$NON-NLS-1$
		this.libraryLabel.setText(LangModelMap.getString(MapEditorResourceKeys.LABEL_IN_LIBRARY));
		this.descLabel.setText(LangModelMap.getString(MapEditorResourceKeys.LABEL_DESCRIPTION));
		this.colorLabel.setText(LangModelMap.getString(MapEditorResourceKeys.LABEL_COLOR));
		this.thicknessLabel.setText(LangModelMap.getString(MapEditorResourceKeys.LABEL_THICKNESS));
		this.styleLabel.setText(LangModelMap.getString(MapEditorResourceKeys.LABEL_STYLE));

		this.commitButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
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
		this.jPanel.add(this.colorLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
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
		this.jPanel.add(this.thicknessLabel, constraints);

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
		this.jPanel.add(this.thicknessComboBox, constraints);

//		constraints.gridx = 0;
//		constraints.gridy = 4;
//		constraints.gridwidth = 1;
//		constraints.gridheight = 1;
//		constraints.weightx = 0.0;
//		constraints.weighty = 0.0;
//		constraints.anchor = GridBagConstraints.WEST;
//		constraints.fill = GridBagConstraints.NONE;
//		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
//		constraints.ipadx = 0;
//		constraints.ipady = 0;
//		this.jPanel.add(this.styleLabel, constraints);

//		constraints.gridx = 1;
//		constraints.gridy = 4;
//		constraints.gridwidth = 2;
//		constraints.gridheight = 1;
//		constraints.weightx = 1.0;
//		constraints.weighty = 0.0;
//		constraints.anchor = GridBagConstraints.CENTER;
//		constraints.fill = GridBagConstraints.HORIZONTAL;
//		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
//		constraints.ipadx = 0;
//		constraints.ipady = 0;
//		this.jPanel.add(this.styleComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
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
		constraints.gridy = 6;
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
		super.addToUndoableListener(this.descTextArea);
		super.addToUndoableListener(this.colorComboBox);
		super.addToUndoableListener(this.thicknessComboBox);
//		super.addToUndoableListener(this.styleComboBox);
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
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText(""); //$NON-NLS-1$

			this.colorComboBox.setEnabled(false);
			this.thicknessComboBox.setEnabled(false);
//			this.styleComboBox.setEnabled(false);
		}
		else {
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.type.getName());
			
			this.libraryComboBox.setEnabled(true);
			this.libraryComboBox.addElements(this.logicalNetLayer.getMapView().getMap().getMapLibraries());
			this.libraryComboBox.setSelectedItem(this.type.getMapLibrary());

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

	public void commitChanges() {
		String name = this.nameTextField.getText();
		if(MiscUtil.validName(name))
		try 
		{
			if(!name.equals(this.type.getName()))
				this.type.setName(name);
			this.type.setMapLibrary((MapLibrary )this.libraryComboBox.getSelectedItem());
			this.type.setDescription(this.descTextArea.getText());

			LinkTypeController linkTypeController = (LinkTypeController)
					LinkTypeController.getInstance();
			Color color = (Color)this.colorComboBox.getSelectedItem();
			if(! color.equals(linkTypeController.getColor(this.type)))
				linkTypeController.setColor(this.type, color);
			int size = this.thicknessComboBox.getSelectedValue();
			if(size != linkTypeController.getLineSize(this.type))
				linkTypeController.setLineSize(this.type, size);
//			String style = (String)this.styleComboBox.getSelectedItem();
//			if(! style.equals(linkTypeController(this.type)))
//				linkTypeController(type, style);
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
	}
}
