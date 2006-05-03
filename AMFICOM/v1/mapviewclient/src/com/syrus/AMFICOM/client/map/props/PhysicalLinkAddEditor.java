/*-
 * $$Id: PhysicalLinkAddEditor.java,v 1.45 2006/05/03 04:46:32 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.UI.WrapperedList;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.map.editor.MapPermissionManager;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;
import com.syrus.AMFICOM.map.PipeBlock;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;
import com.syrus.util.PropertyChangeException;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.45 $, $Date: 2006/05/03 04:46:32 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class PhysicalLinkAddEditor extends DefaultStorableObjectEditor {
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JPanel jPanel = new JPanel();
	WrapperedList cableList = null;
	JScrollPane cablesScrollPane = new JScrollPane();

	private JLabel pipeBlockLabel = new JLabel();
	private JPanel pipeBlockPanel = new JPanel();
	WrapperedComboBox pipeBlockComboBox = null;
	JButton addBlockButton = new JButton();
	JButton removeBlockButton = new JButton();

	private JLabel dimensionLabel = new JLabel();
	private JPanel dimensionPanel = new JPanel();
	private JLabel xLabel = new JLabel();
	JTextField mTextField = new JTextField();
	JTextField nTextField = new JTextField();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	TunnelLayout tunnelLayout = null;
	JScrollPane tunnelsScrollPane = new JScrollPane();

	private JPanel buttonsPanel = new JPanel();
	JToggleButton bindButton = new JToggleButton();
	JButton unbindButton = new JButton();
	JButton selectButton = new JButton();

	JLabel horvertLabel = new JLabel();
	JLabel topDownLabel = new JLabel();
	JLabel leftRightLabel = new JLabel();
	private JPanel directionPanel = new JPanel();
	
	private JButton commitButton = new JButton();

	private List unboundElements = new LinkedList();

	PhysicalLink physicalLink;
	PipeBlock pipeBlock;
	
	boolean processSelection = true;

	static Icon horverticon;
	static Icon verthoricon;

	static Icon topdownicon;
	static Icon downtopicon;
	static Icon leftrighticon;
	static Icon rightlefticon;

	private NetMapViewer netMapViewer;

	private static Dimension buttonSize = new Dimension(24, 24);

	static {
		horverticon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/horvert.gif")); //$NON-NLS-1$
		verthoricon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/verthor.gif")); //$NON-NLS-1$
		topdownicon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/topdown.gif")); //$NON-NLS-1$
		downtopicon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/downtop.gif")); //$NON-NLS-1$
		leftrighticon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/leftright.gif")); //$NON-NLS-1$
		rightlefticon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/rightleft.gif")); //$NON-NLS-1$
	}

	public PhysicalLinkAddEditor() {
		this.tunnelLayout = new TunnelLayout(this);
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}

	}

	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}

	private void jbInit() {
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.cableList = new WrapperedList(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);
		PipeBlockWrapper pipeBlockWrapper = PipeBlockWrapper.getInstance();

		this.pipeBlockComboBox = new WrapperedComboBox(pipeBlockWrapper, PipeBlockWrapper.KEY_NUMBER, PipeBlockWrapper.KEY_NUMBER);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(I18N.getString(MapEditorResourceKeys.LABEL_CABLES_BINDING));
		this.cableList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(PhysicalLinkAddEditor.this.processSelection) {
					PhysicalLinkAddEditor.this.processSelection = false;
					Object or = PhysicalLinkAddEditor.this.cableList
							.getSelectedValue();
					cableSelected(or);
					PhysicalLinkAddEditor.this.bindButton.setEnabled(isEditable() && or != null);
					PhysicalLinkAddEditor.this.unbindButton.setEnabled(isEditable() && or != null);
					PhysicalLinkAddEditor.this.selectButton.setEnabled(isEditable() && or != null);
					PhysicalLinkAddEditor.this.processSelection = true;
				}
			}
		});
		this.bindButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/bindcable.gif"))); //$NON-NLS-1$
		this.bindButton.setToolTipText(I18N.getString(MapEditorResourceKeys.BUTTON_BIND_CABLE_TO_PIPE));
		this.bindButton.setPreferredSize(buttonSize);
		this.bindButton.setMaximumSize(buttonSize);
		this.bindButton.setMinimumSize(buttonSize);
		this.bindButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBindMode(PhysicalLinkAddEditor.this.bindButton.isSelected());
				// Object or = cableList.getSelectedObjectResource();
				// bind(or);
			}
		});
		this.unbindButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/delete.gif"))); //$NON-NLS-1$
		this.unbindButton.setToolTipText(I18N.getString(MapEditorResourceKeys.BUTTON_UNBIND_LINK_BINDING));
		this.unbindButton.setPreferredSize(buttonSize);
		this.unbindButton.setMaximumSize(buttonSize);
		this.unbindButton.setMinimumSize(buttonSize);
		this.unbindButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object or = PhysicalLinkAddEditor.this.cableList
						.getSelectedValue();
				try {
					unbind(or);
				} catch(ApplicationException e1) {
					Log.errorMessage(e1);
				}
			}
		});

		this.selectButton.setToolTipText(I18N.getString(MapEditorResourceKeys.BUTTON_SELECT_ELEMENT));
		this.selectButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/selectlink.gif"))); //$NON-NLS-1$
		this.selectButton.setPreferredSize(buttonSize);
		this.selectButton.setMaximumSize(buttonSize);
		this.selectButton.setMinimumSize(buttonSize);
		this.selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object or = PhysicalLinkAddEditor.this.cableList
						.getSelectedValue();
				selectCable(or);
			}
		});

		this.dimensionLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_DIMENSION));
		this.pipeBlockLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_PIPEBLOCK));

		this.buttonsPanel.setLayout(new GridBagLayout());
		this.pipeBlockPanel.setLayout(new GridBagLayout());
		
		this.pipeBlockComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					Object or = PhysicalLinkAddEditor.this.pipeBlockComboBox.getSelectedItem();
					selectBlock(or);
				}
				else {
					selectBlock(null);
				}
			}
		});

		this.addBlockButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/newprop.gif"))); //$NON-NLS-1$
		this.addBlockButton.setToolTipText(I18N.getString(MapEditorResourceKeys.BUTTON_ADD));
		this.addBlockButton.setPreferredSize(buttonSize);
		this.addBlockButton.setMaximumSize(buttonSize);
		this.addBlockButton.setMinimumSize(buttonSize);
		this.addBlockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PipeBlock newPipeBlock;
				try {
					newPipeBlock = PipeBlock.createInstance(LoginManager.getUserId(), -1, 0, 0, true, true, true);
					PhysicalLinkAddEditor.this.physicalLink.getBinding().addPipeBlock(newPipeBlock);
					PhysicalLinkAddEditor.this.pipeBlockComboBox.addItem(newPipeBlock);
					PhysicalLinkAddEditor.this.pipeBlockComboBox.setSelectedItem(newPipeBlock);
				} catch(CreateObjectException e1) {
					Log.errorMessage(e1);
				}
			}
		});

		this.removeBlockButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/delete.gif"))); //$NON-NLS-1$
		this.removeBlockButton.setToolTipText(I18N.getString(MapEditorResourceKeys.BUTTON_REMOVE));
		this.removeBlockButton.setPreferredSize(buttonSize);
		this.removeBlockButton.setMaximumSize(buttonSize);
		this.removeBlockButton.setMinimumSize(buttonSize);
		this.removeBlockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PhysicalLinkAddEditor.this.physicalLink.getBinding().removePipeBlock(PhysicalLinkAddEditor.this.pipeBlock);
				PhysicalLinkAddEditor.this.pipeBlockComboBox.removeItem(PhysicalLinkAddEditor.this.pipeBlock);
				selectBlock(PhysicalLinkAddEditor.this.pipeBlockComboBox.getSelectedItem());
			}
		});

		this.directionPanel.setLayout(new FlowLayout());
		this.directionPanel.add(this.topDownLabel);
		this.directionPanel.add(this.horvertLabel);
		this.directionPanel.add(this.leftRightLabel);

		this.horvertLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (PhysicalLinkAddEditor.this.horvertLabel.isEnabled()) {
					PhysicalLinkAddEditor.this.pipeBlock.flipHorizontalVertical();
					PhysicalLinkAddEditor.this.horvertLabel.setIcon(
							PhysicalLinkAddEditor.this.pipeBlock.isHorizontalVertical() 
							? horverticon : verthoricon);
					PhysicalLinkAddEditor.this.tunnelLayout.updateElements();
				}
			}
		});

		this.topDownLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (PhysicalLinkAddEditor.this.topDownLabel.isEnabled()) {
					PhysicalLinkAddEditor.this.pipeBlock.flipTopToBottom();
					PhysicalLinkAddEditor.this.topDownLabel.setIcon(
							PhysicalLinkAddEditor.this.pipeBlock.isTopToBottom() 
							? topdownicon : downtopicon);
					PhysicalLinkAddEditor.this.tunnelLayout.updateElements();
				}
			}
		});

		this.leftRightLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (PhysicalLinkAddEditor.this.leftRightLabel.isEnabled()) {
					PhysicalLinkAddEditor.this.pipeBlock.flipLeftToRight();
					PhysicalLinkAddEditor.this.leftRightLabel.setIcon(
							PhysicalLinkAddEditor.this.pipeBlock.isLeftToRight() 
							? leftrighticon : rightlefticon);
					PhysicalLinkAddEditor.this.tunnelLayout.updateElements();
				}
			}
		});

		this.horvertLabel.setIcon(horverticon);
		this.horvertLabel.setToolTipText(I18N.getString(MapEditorResourceKeys.BUTTON_NUMBERING_DIRECTION));
		this.topDownLabel.setIcon(topdownicon);
		this.topDownLabel.setToolTipText(I18N.getString(MapEditorResourceKeys.BUTTON_VERTICAL_ORDER));
		this.leftRightLabel.setIcon(leftrighticon);
		this.leftRightLabel.setToolTipText(I18N.getString(MapEditorResourceKeys.BUTTON_HORIZONTAL_ORDER));
		
		this.tunnelsScrollPane.getViewport().add(this.tunnelLayout.getUgoPanel().getGraph());
		this.cablesScrollPane.getViewport().add(this.cableList);

		this.xLabel.setText("X"); //$NON-NLS-1$
		this.dimensionPanel.setLayout(this.gridBagLayout2);

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

		constraints.gridx =  0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.pipeBlockPanel.add(this.pipeBlockComboBox, constraints);

		constraints.gridx =  1;
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
		this.pipeBlockPanel.add(this.addBlockButton, constraints);

		constraints.gridx =  2;
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
		this.pipeBlockPanel.add(this.removeBlockButton, constraints);

		constraints.gridx =  0;
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
		this.buttonsPanel.add(this.commitButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.buttonsPanel.add(this.bindButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.buttonsPanel.add(this.unbindButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.buttonsPanel.add(this.selectButton, constraints);

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
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.3;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.cablesScrollPane, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.buttonsPanel, constraints);

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
		this.jPanel.add(this.pipeBlockLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.pipeBlockPanel, constraints);

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
		this.jPanel.add(this.dimensionLabel, constraints);

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
		this.jPanel.add(this.dimensionPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.7;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.tunnelsScrollPane, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.SOUTHEAST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.directionPanel, constraints);

		this.bindButton.setEnabled(false);
		this.unbindButton.setEnabled(false);
		this.selectButton.setEnabled(false);
	}

	protected void selectBlock(Object or) {
		this.removeBlockButton.setEnabled(isEditable() && this.pipeBlockComboBox.getModel().getSize() > 1);
		if(!(or instanceof PipeBlock)) {
			this.pipeBlock = null;
			this.tunnelLayout.setPipeBlock(null);
			return;
		}
		this.pipeBlock = (PipeBlock)or;
		this.mTextField.setText(String.valueOf(this.pipeBlock.getDimension().getWidth()));
		this.nTextField.setText(String.valueOf(this.pipeBlock.getDimension().getHeight()));

		this.horvertLabel.setIcon(
				this.pipeBlock.isHorizontalVertical() 
					? horverticon : verthoricon);

		this.leftRightLabel.setIcon(
				this.pipeBlock.isLeftToRight() 
					? leftrighticon : rightlefticon);

		this.topDownLabel.setIcon(
				this.pipeBlock.isTopToBottom() 
					? topdownicon : downtopicon);

		this.tunnelLayout.setPipeBlock(this.pipeBlock);
	}

	public Object getObject() {
		return this.physicalLink;
	}

	@Override
	protected boolean isEditable() {
		return MapPermissionManager.isEditionAllowed();
	}

	public void setObject(Object object) {
		final boolean editable = isEditable();
		this.commitButton.setEnabled(editable);
		this.addBlockButton.setEnabled(editable);
		this.removeBlockButton.setEnabled(false);
		
		this.horvertLabel.setEnabled(editable);
		this.topDownLabel.setEnabled(editable);
		this.leftRightLabel.setEnabled(editable);
		
		this.cableList.removeAll();
		this.pipeBlockComboBox.removeAllItems();
		this.physicalLink = (PhysicalLink )object;
		if(this.physicalLink == null) {
			this.cableList.setEnabled(false);
			this.pipeBlockComboBox.setEnabled(false);
			this.tunnelLayout.setPipeBlock(null);

			this.mTextField.setText(""); //$NON-NLS-1$
			this.nTextField.setText(""); //$NON-NLS-1$
		}
		else {
			this.cableList.setEnabled(true);
			this.pipeBlockComboBox.setEnabled(true);
			PhysicalLinkBinding binding = this.physicalLink.getBinding();
			final Set<PipeBlock> pipeBlocks = binding.getPipeBlocks();
			if(pipeBlocks != null) {
				this.pipeBlockComboBox.addElements(pipeBlocks);
				if(pipeBlocks.size() > 0) {
					this.pipeBlockComboBox.setSelectedItem(pipeBlocks.iterator().next());
				}
			}

			List list = binding.getBindObjects();
			if(list != null) {
				this.cableList.addElements(list);
			}

		}
	}

	public void cableSelected(Object or) {
		if(this.pipeBlock != null && this.pipeBlock.equals(this.physicalLink.getBinding().getPipeBlock(or))) {
			this.tunnelLayout.setActiveElement(or);
		}
	}

	public void cableBindingSelected(int col, int row) {
		if(this.processSelection) {
			this.processSelection = false;
			if(this.bindButton.isSelected()) {
				Object or = this.cableList.getSelectedValue();
				try {
					bind(or);
				} catch(ApplicationException e) {
					Log.errorMessage(e);
				}
				this.bindButton.setSelected(false);
				setBindMode(false);
			}
			else {
				try {
					PhysicalLinkBinding binding = this.physicalLink
							.getBinding();
					List list = binding.getBindObjects();
					if(list != null) {
						this.cableList.getSelectionModel().clearSelection();
						for(Iterator it = list.iterator(); it.hasNext();) {
							CablePath cablePath = (CablePath) it.next();
							CableChannelingItem cableChannelingItem = cablePath
									.getFirstCCI(this.physicalLink);
							PipeBlock block = cableChannelingItem.getPipeBlock();
							if(block.equals(this.pipeBlock)) {
								int x = cableChannelingItem.getRowX();
								int y = cableChannelingItem.getPlaceY();
								if(x == col && y == row) {
									this.cableList.setSelectedValue(
											cablePath,
											true);
								}
							}
						}
					}
				} catch(Exception e) {
					Log.errorMessage(e);
				}
			}
			this.processSelection = true;
		}
	}

	void selectCable(Object or) {
		CablePath cablePath = (CablePath )or;
		this.netMapViewer.getLogicalNetLayer().getMapView().getMap().clearSelection();
		this.netMapViewer.getLogicalNetLayer().deselectAll();
		this.netMapViewer.getLogicalNetLayer().setCurrentMapElement(cablePath);
		this.netMapViewer.getLogicalNetLayer().getMapView().getMap().setSelected(cablePath, true);
		this.netMapViewer.getLogicalNetLayer().sendSelectionChangeEvent();
	}

	public void bind(Object or) throws ApplicationException {
		IntPoint pt = this.tunnelLayout.getActiveCoordinates();
		if(pt != null) {
			this.pipeBlock.bind(or, pt.x, pt.y);
			CablePath cablePath = (CablePath )or;
			CableChannelingItem cci = cablePath.getFirstCCI(this.physicalLink);
			cci.setRowX(pt.x);
			cci.setPlaceY(pt.y);
			cci.setPipeBlock(this.pipeBlock);
			this.tunnelLayout.updateElements();
		}
	}

	void setBindMode(boolean bindModeEnabled) {
		if(bindModeEnabled) {
			this.cableList.setEnabled(false);
			this.unbindButton.setEnabled(false);
			this.selectButton.setEnabled(false);
		}
		else {
			this.cableList.setEnabled(isEditable());
			this.unbindButton.setEnabled(isEditable());
			this.selectButton.setEnabled(isEditable());
		}
	}

	public void unbind(Object or) throws ApplicationException {
		CablePath cablePath = (CablePath )or;

		CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
				this.physicalLink.getStartNode(),
				this.physicalLink.getEndNode());
		command.setNetMapViewer(this.netMapViewer);
		command.execute();

		UnboundLink unbound = command.getUnbound();
		unbound.setCablePath(cablePath);

//		CableChannelingItem cci = cablePath.getFirstCCI(this.physicalLink);
		for(CableChannelingItem cableChannelingItem : cablePath.getCachedCCIs()) {
			if(cablePath.getBinding().get(cableChannelingItem) == this.physicalLink) {
				CableChannelingItem newCableChannelingItem = 
					CableController.generateCCI(
							cablePath, 
							unbound,
							cableChannelingItem.getStartSiteNode(),
							cableChannelingItem.getEndSiteNode());
				newCableChannelingItem.insertSelfBefore(cableChannelingItem);
				cablePath.removeLink(cableChannelingItem);
				cablePath.addLink(unbound, newCableChannelingItem);
				cableChannelingItem.setParentPathOwner(null, false);
			}
		}

		this.physicalLink.getBinding().remove(cablePath);

		this.cableList.getModel().removeElement(cablePath);

		this.tunnelLayout.updateElements();

		this.netMapViewer.getLogicalNetLayer().getCommandList().flush();
	}

	public List getUnboundElements() {
		return this.unboundElements;
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
		
		int m = Integer.parseInt(PhysicalLinkAddEditor.this.mTextField.getText());
		int n = Integer.parseInt(PhysicalLinkAddEditor.this.nTextField.getText());
		if(!PhysicalLinkAddEditor.this.pipeBlock.getDimension().equals(new IntDimension(m, n))) {
			PhysicalLinkAddEditor.this.pipeBlock.setDimension(new IntDimension(m, n));
			PhysicalLinkAddEditor.this.tunnelLayout.setDimension(m, n);
			PhysicalLinkAddEditor.this.tunnelLayout.updateElements();
		}

		super.commitChanges();
	}
}

class PipeBlockWrapper implements Wrapper {
	public static final String KEY_NUMBER = MapEditorResourceKeys.LABEL_NUMBER;
	private List keys = Collections.unmodifiableList(Arrays
			.asList(new String[] { KEY_NUMBER }));

	static PipeBlockWrapper instance;
	
	private PipeBlockWrapper() {
		// nothing
	}
	
	public void setValue(Object object, String key, Object value)
			throws PropertyChangeException {
		//nothing
	}

	public static PipeBlockWrapper getInstance() {
		if(instance == null) {
			instance = new PipeBlockWrapper();
		}
		return instance;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public Object getValue(Object object, String key) {
		if(! (object instanceof PipeBlock)) {
			return " "; //$NON-NLS-1$
		}
		PipeBlock pipeBlock = (PipeBlock) object;
		return String.valueOf(pipeBlock.getNumber());
	}

	public void setPropertyValue(
			String key,
			Object objectKey,
			Object objectValue) {
		// nothing
	}

	public Object getPropertyValue(String key) {
		// nothing
		return null;
	}

	public Class<?> getPropertyClass(String key) {
		// nothing
		return null;
	}

	public String getName(String key) {
		return "Номер";
	}

	public List getKeys() {
		return this.keys;
	}

}
