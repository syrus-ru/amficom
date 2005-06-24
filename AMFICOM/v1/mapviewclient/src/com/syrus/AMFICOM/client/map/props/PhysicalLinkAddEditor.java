package com.syrus.AMFICOM.client.map.props;

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

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedList;
import com.syrus.AMFICOM.client.UI.WrapperedListModel;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.IntPoint;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;

public final class PhysicalLinkAddEditor extends DefaultStorableObjectEditor {
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JPanel jPanel = new JPanel();
	WrapperedList cableList = null;
	JScrollPane cablesScrollPane = new JScrollPane();

	TunnelLayout tunnelLayout = null;
	JScrollPane tunnelsScrollPane = new JScrollPane();

	private JPanel buttonsPanel = new JPanel();
	JToggleButton bindButton = new JToggleButton();
	JButton unbindButton = new JButton();

	JLabel horvertLabel = new JLabel();
	JLabel topDownLabel = new JLabel();
	JLabel leftRightLabel = new JLabel();
	private JPanel directionPanel = new JPanel();
	
	private List unboundElements = new LinkedList();

	PhysicalLink physicalLink;
	
	private LogicalNetLayer logicalNetLayer;

	boolean processSelection = true;

	static Icon horverticon;
	static Icon verthoricon;

	static Icon topdownicon;
	static Icon downtopicon;
	static Icon leftrighticon;
	static Icon rightlefticon;

	private NetMapViewer netMapViewer;

	static {
		horverticon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/horvert.gif"));
		verthoricon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/verthor.gif"));
		topdownicon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/topdown.gif"));
		downtopicon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/downtop.gif"));
		leftrighticon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/leftright.gif"));
		rightlefticon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/rightleft.gif"));
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
		this.logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
	}

	private void jbInit() {
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.cableList = new WrapperedList(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);


		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModelMap.getString("LinkBinding"));
		this.cableList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(PhysicalLinkAddEditor.this.processSelection) {
					PhysicalLinkAddEditor.this.processSelection = false;
					Object or = PhysicalLinkAddEditor.this.cableList
							.getSelectedValue();
					cableSelected(or);
					PhysicalLinkAddEditor.this.bindButton
							.setEnabled(or != null);
					PhysicalLinkAddEditor.this.unbindButton
							.setEnabled(or != null);
					PhysicalLinkAddEditor.this.processSelection = true;
				}
			}
		});
		this.bindButton.setText("Привязать");
		this.bindButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBindMode(PhysicalLinkAddEditor.this.bindButton.isSelected());
				// Object or = cableList.getSelectedObjectResource();
				// bind(or);
			}
		});
		this.unbindButton.setText("Отвязать");
		this.unbindButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object or = PhysicalLinkAddEditor.this.cableList
						.getSelectedValue();
				unbind(or);
			}
		});
		this.buttonsPanel.setLayout(new FlowLayout());
		this.buttonsPanel.add(this.bindButton);
		this.buttonsPanel.add(this.unbindButton);

		this.directionPanel.setLayout(new FlowLayout());
		this.directionPanel.add(this.topDownLabel);
		this.directionPanel.add(this.horvertLabel);
		this.directionPanel.add(this.leftRightLabel);

		this.horvertLabel.addMouseListener(new MouseAdapter() {
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
		this.topDownLabel.setIcon(topdownicon);
		this.leftRightLabel.setIcon(leftrighticon);
		
		this.tunnelsScrollPane.getViewport().add(this.tunnelLayout.getUgoPanel().getGraph());
		this.cablesScrollPane.getViewport().add(this.cableList);

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 2;
		constraints.weightx = 1.0;
		constraints.weighty = 0.1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.cablesScrollPane, constraints);

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
		this.jPanel.add(this.buttonsPanel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
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

		constraints.gridx = 0;
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
		this.jPanel.add(Box.createVerticalStrut(3), constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.gridheight = 3;
		constraints.weightx = 1.0;
		constraints.weighty = 0.9;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.tunnelsScrollPane, constraints);

		this.bindButton.setEnabled(false);
		this.unbindButton.setEnabled(false);
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
		}
		else {
			this.cableList.setEnabled(true);
			PhysicalLinkBinding binding = this.physicalLink.getBinding();

			this.tunnelLayout.setBinding(binding);

			// TODO demo cheat!
//			this.cableList.addElements(Collections.singletonList(this.physicalLink));
			//
			
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
						CablePath cp = (CablePath )it.next();
						IntPoint position = cp.getBindingPosition(this.physicalLink);
						if(position.x == col && position.y == row) {
							this.cableList.setSelectedValue(cp, true);
						}
					}
				}
			}
			this.processSelection = true;
		}
	}

	public void bind(Object or) {
		PhysicalLinkBinding binding = this.physicalLink.getBinding();
		IntPoint pt = this.tunnelLayout.getActiveCoordinates();
		if(pt != null) {
			binding.bind(or, pt.x, pt.y);
			CablePath cp = (CablePath )or;
			CableChannelingItem cci = (CableChannelingItem )(cp.getBinding().get(this.physicalLink));
			cci.setRowX(pt.x);
			cci.setPlaceY(pt.y);
			this.tunnelLayout.updateElements();
		}
	}

	void setBindMode(boolean bindModeEnabled) {
		if(bindModeEnabled) {
			this.cableList.setEnabled(false);
			this.unbindButton.setEnabled(false);
		}
		else {
			this.cableList.setEnabled(true);
			this.unbindButton.setEnabled(true);
		}
	}

	public void unbind(Object or) {
		CablePath cablePath = (CablePath )or;

		cablePath.removeLink(this.physicalLink);

		CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
				this.physicalLink.getStartNode(),
				this.physicalLink.getEndNode());
		command.setNetMapViewer(this.netMapViewer);
		command.execute();

		UnboundLink unbound = command.getUnbound();
		unbound.setCablePath(cablePath);

		cablePath.addLink(unbound, CableController.generateCCI(
				cablePath,
				unbound,
				LoginManager.getUserId()));
		this.physicalLink.getBinding().remove(cablePath);

		((WrapperedListModel )this.cableList.getModel()).removeElement(cablePath);

		this.tunnelLayout.updateElements();
	}

	public List getUnboundElements() {
		return this.unboundElements;
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	public void commitChanges() {
		// nothing to commit
	}
}
