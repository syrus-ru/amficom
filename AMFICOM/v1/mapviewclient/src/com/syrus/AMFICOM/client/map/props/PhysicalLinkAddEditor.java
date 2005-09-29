package com.syrus.AMFICOM.client.map.props;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedList;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;
import com.syrus.AMFICOM.scheme.CableChannelingItem;

public final class PhysicalLinkAddEditor extends DefaultStorableObjectEditor {
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JPanel jPanel = new JPanel();
	WrapperedList cableList = null;
	JScrollPane cablesScrollPane = new JScrollPane();

	private JLabel dimensionLabel = new JLabel();
	private JPanel dimensionPanel = new JPanel();
	private JLabel xLabel = new JLabel();
	private JTextField mTextField = new JTextField();
	private JTextField nTextField = new JTextField();
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
			e.printStackTrace();
		}

	}

	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}

	private void jbInit() {
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.cableList = new WrapperedList(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);


		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModelMap.getString(MapEditorResourceKeys.LABEL_CABLES_BINDING));
		this.cableList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(PhysicalLinkAddEditor.this.processSelection) {
					PhysicalLinkAddEditor.this.processSelection = false;
					Object or = PhysicalLinkAddEditor.this.cableList
							.getSelectedValue();
					cableSelected(or);
					PhysicalLinkAddEditor.this.bindButton.setEnabled(or != null);
					PhysicalLinkAddEditor.this.unbindButton.setEnabled(or != null);
					PhysicalLinkAddEditor.this.selectButton.setEnabled(or != null);
					PhysicalLinkAddEditor.this.processSelection = true;
				}
			}
		});
		this.bindButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/bindcable.gif"))); //$NON-NLS-1$
		this.bindButton.setToolTipText(LangModelMap.getString(MapEditorResourceKeys.BUTTON_BIND_CABLE_TO_PIPE));
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
		this.unbindButton.setToolTipText(LangModelMap.getString(MapEditorResourceKeys.BUTTON_UNBIND_LINK_BINDING));
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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		this.selectButton.setToolTipText(LangModelMap.getString(MapEditorResourceKeys.BUTTON_SELECT_ELEMENT));
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

		this.dimensionLabel.setText(LangModelMap.getString(MapEditorResourceKeys.LABEL_DIMENSION));

		this.buttonsPanel.setLayout(new GridBagLayout());

		this.directionPanel.setLayout(new FlowLayout());
		this.directionPanel.add(this.topDownLabel);
		this.directionPanel.add(this.horvertLabel);
		this.directionPanel.add(this.leftRightLabel);

		this.horvertLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PhysicalLinkAddEditor.this.physicalLink.getBinding()
						.flipHorizontalVertical();
				PhysicalLinkAddEditor.this.horvertLabel.setIcon(
						PhysicalLinkAddEditor.this.physicalLink.getBinding().isHorizontalVertical() 
							? horverticon : verthoricon);
				PhysicalLinkAddEditor.this.tunnelLayout.updateElements();
			}
		});

		this.topDownLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PhysicalLinkAddEditor.this.physicalLink.getBinding()
						.flipTopToBottom();
				PhysicalLinkAddEditor.this.topDownLabel.setIcon(
						PhysicalLinkAddEditor.this.physicalLink.getBinding().isTopToBottom() 
							? topdownicon : downtopicon);
				PhysicalLinkAddEditor.this.tunnelLayout.updateElements();
			}
		});

		this.leftRightLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PhysicalLinkAddEditor.this.physicalLink.getBinding()
						.flipLeftToRight();
				PhysicalLinkAddEditor.this.leftRightLabel.setIcon(
						PhysicalLinkAddEditor.this.physicalLink.getBinding().isLeftToRight() 
							? leftrighticon : rightlefticon);
				PhysicalLinkAddEditor.this.tunnelLayout.updateElements();
			}
		});

		this.horvertLabel.setIcon(horverticon);
		this.horvertLabel.setToolTipText(LangModelMap.getString(MapEditorResourceKeys.BUTTON_NUMBERING_DIRECTION));
		this.topDownLabel.setIcon(topdownicon);
		this.topDownLabel.setToolTipText(LangModelMap.getString(MapEditorResourceKeys.BUTTON_VERTICAL_ORDER));
		this.leftRightLabel.setIcon(leftrighticon);
		this.leftRightLabel.setToolTipText(LangModelMap.getString(MapEditorResourceKeys.BUTTON_HORIZONTAL_ORDER));
		
		this.tunnelsScrollPane.getViewport().add(this.tunnelLayout.getUgoPanel().getGraph());
		this.cablesScrollPane.getViewport().add(this.cableList);

		this.xLabel.setText("X"); //$NON-NLS-1$
		this.dimensionPanel.setLayout(this.gridBagLayout2);

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
		this.jPanel.add(this.dimensionLabel, constraints);

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
		this.jPanel.add(this.dimensionPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
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
		constraints.gridy = 3;
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

//		constraints.gridx = 0;
//		constraints.gridy = 2;
//		constraints.gridwidth = 2;
//		constraints.gridheight = 1;
//		constraints.weightx = 1.0;
//		constraints.weighty = 0.0;
//		constraints.anchor = GridBagConstraints.WEST;
//		constraints.fill = GridBagConstraints.HORIZONTAL;
//		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
//		constraints.ipadx = 0;
//		constraints.ipady = 0;
//		this.jPanel.add(Box.createVerticalStrut(3), constraints);

		this.bindButton.setEnabled(false);
		this.unbindButton.setEnabled(false);
		this.selectButton.setEnabled(false);

		super.addToUndoableListener(this.mTextField);
		super.addToUndoableListener(this.nTextField);
	}

	public Object getObject() {
		return this.physicalLink;
	}

	public void setObject(Object object) {
		this.cableList.removeAll();
		this.physicalLink = (PhysicalLink )object;
		if(this.physicalLink == null) {
			this.cableList.setEnabled(false);
			this.tunnelLayout.setBinding(null);

			this.mTextField.setText(""); //$NON-NLS-1$
			this.nTextField.setText(""); //$NON-NLS-1$
		}
		else {
			this.cableList.setEnabled(true);
			PhysicalLinkBinding binding = this.physicalLink.getBinding();

			this.mTextField.setText(String.valueOf(this.physicalLink.getBinding().getDimension().getWidth()));
			this.nTextField.setText(String.valueOf(this.physicalLink.getBinding().getDimension().getHeight()));

			this.tunnelLayout.setBinding(binding);

			List list = binding.getBindObjects();
			if(list != null) {
				this.cableList.addElements(list);
			}

		}
	}

	public void cableSelected(Object or) {
		this.tunnelLayout.setActiveElement(or);
	}

	public void cableBindingSelected(int col, int row) {
		if(this.processSelection) {
			this.processSelection = false;
			if(this.bindButton.isSelected()) {
				Object or = this.cableList.getSelectedValue();
				bind(or);
				this.bindButton.setSelected(false);
				setBindMode(false);
			}
			else {
				PhysicalLinkBinding binding = this.physicalLink.getBinding();
				List list = binding.getBindObjects();
				if(list != null) {
					this.cableList.getSelectionModel().clearSelection();
					for(Iterator it = list.iterator(); it.hasNext();) {
						CablePath cablePath = (CablePath )it.next();
						CableChannelingItem cableChannelingItem = cablePath.getFirstCCI(this.physicalLink);
						int x = cableChannelingItem.getRowX();
						int y = cableChannelingItem.getPlaceY();
						if(x == col && y == row) {
							this.cableList.setSelectedValue(cablePath, true);
						}
					}
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

	public void bind(Object or) {
		PhysicalLinkBinding binding = this.physicalLink.getBinding();
		IntPoint pt = this.tunnelLayout.getActiveCoordinates();
		if(pt != null) {
			binding.bind(or, pt.x, pt.y);
			CablePath cablePath = (CablePath )or;
			CableChannelingItem cci = cablePath.getFirstCCI(this.physicalLink);
			cci.setRowX(pt.x);
			cci.setPlaceY(pt.y);
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
			this.cableList.setEnabled(true);
			this.unbindButton.setEnabled(true);
			this.selectButton.setEnabled(true);
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
		for(CableChannelingItem cableChannelingItem : cablePath.getSchemeCableLink().getPathMembers()) {
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
		int m = Integer.parseInt(this.mTextField.getText());
		int n = Integer.parseInt(this.nTextField.getText());
		if(!this.physicalLink.getBinding().getDimension().equals(new IntDimension(m, n))) {
			this.physicalLink.getBinding().setDimension(new IntDimension(m, n));
			this.tunnelLayout.setDimension(m, n);
			this.tunnelLayout.updateElements();
		}
		super.commitChanges();
	}
}
