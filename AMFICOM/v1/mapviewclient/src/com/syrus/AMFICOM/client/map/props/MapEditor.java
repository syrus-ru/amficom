package com.syrus.AMFICOM.client.map.props;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MiscUtil;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.VoidElement;

/**
 * @version $Revision: 1.8 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapEditor extends DefaultStorableObjectEditor {
	Map map;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JPanel jPanel = new JPanel();
	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel domainLabel = new JLabel();
	private WrapperedComboBox domainComboBox = null;
	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	public MapEditor() {
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	private void jbInit() {
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.domainComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModelGeneral.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.domainLabel.setText(LangModelMap.getString("Domain"));
		this.descLabel.setText(LangModelMap.getString("Description"));

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = null;
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
		constraints.insets = null;
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
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.domainLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.domainComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.descLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(new JScrollPane(this.descTextArea), constraints);

		this.domainComboBox.setEnabled(false);
		
		super.addToUndoableListener(this.nameTextField);
		super.addToUndoableListener(this.descTextArea);
	}

	public Object getObject() {
		return this.map;
	}

	public void setObject(Object object) {
		if(object instanceof Map)
			this.map = (Map)object;
		else
			if(object instanceof VoidElement)
				this.map = ((VoidElement )object).getMapView().getMap();
		
		this.domainComboBox.removeAllItems();

		if(this.map == null) {
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText("");
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText("");
		}
		else {
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.map.getName());

			Domain domain = null;
			Collection domains = null;
			
			StorableObjectCondition condition = 
				new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
			try {
				domains = StorableObjectPool.getStorableObjectsByCondition(
						condition,
						true);
			} catch(ApplicationException e) {
				e.printStackTrace();
			}

			try {
				domain = (Domain )StorableObjectPool.getStorableObject(
						this.map.getDomainId(), 
						false);
			} catch(ApplicationException e) {
				e.printStackTrace();
			}

			this.domainComboBox.addElements(domains);
			this.domainComboBox.setSelectedItem(domain);

			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.map.getDescription());
		}
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	public void commitChanges() {
		String name = this.nameTextField.getText();
		if(MiscUtil.validName(name))
		try {
			this.map.setName(name);
			this.map.setDescription(this.descTextArea.getText());
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
}
