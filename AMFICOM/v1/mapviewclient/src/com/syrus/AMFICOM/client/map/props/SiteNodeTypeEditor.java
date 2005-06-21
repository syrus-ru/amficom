package com.syrus.AMFICOM.client.map.props;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.ImagesDialog;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.resource.AbstractImageResource;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;

public final class SiteNodeTypeEditor
		extends DefaultStorableObjectEditor {
	SiteNodeType type;

	Identifier imageId;

	private JPanel jPanel = new JPanel();
	private JLabel nameLabel = new JLabel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JTextField nameTextField = new JTextField();
	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	private JLabel imageLabel = new JLabel();
	private JPanel imagePanel = new JPanel();
	private JButton imageButton = new JButton();

	private ApplicationContext aContext;

	public SiteNodeTypeEditor() {
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	private void jbInit() {
		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModelGeneral.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
//		this.nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.descLabel.setText(LangModelMap.getString("Description"));
//		this.descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.imageLabel.setText(LangModelMap.getString("Image"));
//		this.imageLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.imagePanel.setLayout(new BorderLayout());

		this.imageButton.setText(LangModelMap.getString("Change"));
//		this.imageButton.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.imageButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					changeImage();
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
		this.jPanel.add(this.imageLabel, constraints);

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
		this.jPanel.add(this.imageButton, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
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
		constraints.gridy = 3;
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
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.descTextArea, constraints);
	}

	public Object getObject() {
		return this.type;
	}

	public void setObject(Object object) {
		this.type = (SiteNodeType )object;
		if(this.type == null) {
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText("");
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText("");

			this.imageId = null;
			this.imagePanel.removeAll();
			this.imageButton.setEnabled(false);
		}
		else {
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.type.getName());
			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.type.getDescription());

			this.imageId = this.type.getImageId();
			this.imagePanel.removeAll();

			Image im;
			try {
				AbstractImageResource imageResource = (AbstractImageResource )StorableObjectPool
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
				e.printStackTrace();
				return;
			}

			this.imagePanel.add(new JLabel(new ImageIcon(im)));
			this.imagePanel.revalidate();
			this.imageButton.setEnabled(true);
		}
	}

	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	void changeImage() {
		ImagesDialog frame = new ImagesDialog(this.aContext);

		try {
			AbstractImageResource imageResource = (AbstractImageResource )StorableObjectPool.getStorableObject(this.imageId, true);
			frame.setImageResource((BitmapImageResource )imageResource);
		} catch(ApplicationException e) {
			e.printStackTrace();
			return;
		}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) 
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width) 
			frameSize.width = screenSize.width;
		frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		frame.setModal(true);
		frame.setVisible(true);
		if(frame.retCode == 1) {
			this.imagePanel.removeAll();
			AbstractImageResource ir = frame.getImageResource();
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

	public void commitChanges() {
		try {
			this.type.setName(this.nameTextField.getText());
			this.type.setDescription(this.descTextArea.getText());
			this.type.setImageId(this.imageId);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
