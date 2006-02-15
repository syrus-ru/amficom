/*-
 * $$Id: SiteNodeTypeEditor.java,v 1.22 2006/02/15 11:15:42 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.UI.AComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.ImagesDialog;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.client.resource.MiscUtil;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.AMFICOM.resource.AbstractImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.22 $, $Date: 2006/02/15 11:15:42 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class SiteNodeTypeEditor
		extends DefaultStorableObjectEditor {
	SiteNodeType type;

	Identifier imageId;

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

	private JLabel imageLabel = new JLabel();
	private JPanel imagePanel = new JPanel();
	private JButton imageButton = new JButton();

	private JButton commitButton = new JButton();

	LogicalNetLayer logicalNetLayer;

	private NetMapViewer netMapViewer;

	static String[] sortNames;
	static {
		sortNames = new String[7];
		sortNames[SiteNodeTypeSort._WELL] = 
			I18N.getString("defaultwell");
		sortNames[SiteNodeTypeSort._PIQUET] = 
			I18N.getString("defaultpiquet");
		sortNames[SiteNodeTypeSort._ATS] = 
			I18N.getString("defaultats");
		sortNames[SiteNodeTypeSort._BUILDING] = 
			I18N.getString("defaultbuilding");
		sortNames[SiteNodeTypeSort._CABLE_INLET] = 
			I18N.getString("defaultcableinlet");
		sortNames[SiteNodeTypeSort._TOWER] = 
			I18N.getString("defaulttower");
		sortNames[SiteNodeTypeSort._UNBOUND] = 
			I18N.getString("unbound");
	}
	
	public SiteNodeTypeEditor() {
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
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
		this.sortComboBox.addItem(SiteNodeTypeSort.WELL);
		this.sortComboBox.addItem(SiteNodeTypeSort.PIQUET);
		this.sortComboBox.addItem(SiteNodeTypeSort.ATS);
		this.sortComboBox.addItem(SiteNodeTypeSort.BUILDING);
		this.sortComboBox.addItem(SiteNodeTypeSort.TOWER);
		this.sortComboBox.setRenderer(new DefaultListCellRenderer() {
		
			private static final long serialVersionUID = 2344925204020343951L;

			@Override
			public Component getListCellRendererComponent(
					JList list,
					Object value,
					int index,
					boolean isSelected,
					boolean cellHasFocus) {
				if(!(value instanceof SiteNodeTypeSort)) {
					return null;
				}
				SiteNodeTypeSort sort = (SiteNodeTypeSort) value;
				String text = SiteNodeTypeEditor.sortNames[sort.value()];
				return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
			}
		
		});

		this.jPanel.setLayout(this.gridBagLayout1);
//		this.jPanel.setName(I18N.getString(MapEditorResourceKeys.TITLE_PROPERTIES));

		this.nameLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_NAME));
//		this.nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.libraryLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_IN_LIBRARY));
		this.sortLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_SORT));

		this.descLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_DESCRIPTION));
//		this.descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.imageLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_IMAGE));
//		this.imageLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.imagePanel.setLayout(new BorderLayout());

		this.imageButton.setText(I18N.getString(MapEditorResourceKeys.BUTTON_CHANGE));
//		this.imageButton.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.imageButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					try {
						changeImage();
					} catch(ApplicationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});

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
		this.jPanel.add(this.imageLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.gridheight = 2;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.imagePanel, constraints);

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
		// TODO fix ImagePanel before enabling it
		this.jPanel.add(this.imageButton, constraints);

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

		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.descTextArea, constraints);

		super.addToUndoableListener(this.nameTextField);
		super.addToUndoableListener(this.libraryComboBox);
		super.addToUndoableListener(this.sortComboBox);
		super.addToUndoableListener(this.descTextArea);

		this.imageButton.setEnabled(false);
	}

	public Object getObject() {
		return this.type;
	}

	public void setObject(Object object) {
		this.type = (SiteNodeType )object;
		this.libraryComboBox.removeAllItems();
		if(this.type == null) {
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText(""); //$NON-NLS-1$
			this.libraryComboBox.setEnabled(false);
			this.sortComboBox.setEnabled(false);
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText(""); //$NON-NLS-1$

			this.imageId = null;
			this.imagePanel.removeAll();
			this.imageButton.setEnabled(false);
		}
		else {
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.type.getName());
			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.type.getDescription());

			this.libraryComboBox.setEnabled(true);
			this.libraryComboBox.addElements(this.logicalNetLayer.getMapView().getMap().getMapLibraries());
			this.libraryComboBox.setSelectedItem(this.type.getMapLibrary());

			this.sortComboBox.setEnabled(true);
			this.sortComboBox.setSelectedItem(this.type.getSort());

			this.imageId = this.type.getImageId();
			this.imagePanel.removeAll();

			Image im;
			try {
				AbstractImageResource imageResource = StorableObjectPool
						.getStorableObject(this.imageId, true);

				ImageIcon icon = null;

				if(imageResource instanceof FileImageResource) {
					FileImageResource fir = (FileImageResource )imageResource;
					icon = new ImageIcon(fir.getFileName());
				}
				else {
					icon = new ImageIcon(imageResource.getImage());
				}

				im = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			} catch(ApplicationException e) {
				Log.errorMessage(e);
				return;
			}

			this.imagePanel.add(new JLabel(new ImageIcon(im)));
			this.imagePanel.revalidate();
			// TODO fix ImagePanel before enabling it
			this.imageButton.setEnabled(true);
		}
	}

	void changeImage() throws ApplicationException {
		
		AbstractImageResource imageResource = StorableObjectPool.getStorableObject(this.imageId, true);
		AbstractImageResource ir = ImagesDialog.showImageDialog(imageResource);

		if(ir != null) {
			this.imagePanel.removeAll();
			this.imageId = ir.getId();
			ImageIcon icon;

			if(ir instanceof FileImageResource) {
				FileImageResource fir = (FileImageResource )ir;
				icon = new ImageIcon(fir.getFileName());
			}
			else {
				icon = new ImageIcon(ir.getImage());
			}

			
			Image im = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			this.imagePanel.add(new JLabel(new ImageIcon(im)));
			this.imagePanel.revalidate();
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
			try {
				if(!name.equals(this.type.getName()))
					this.type.setName(name);
				this.type.setMapLibrary((MapLibrary )this.libraryComboBox.getSelectedItem());
				this.type.setSort((SiteNodeTypeSort )this.sortComboBox.getSelectedItem());
				this.type.setDescription(this.descTextArea.getText());
				this.type.setImageId(this.imageId);
				StorableObjectPool.flush(this.type, LoginManager.getUserId(), true);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		super.commitChanges();
	}
}
