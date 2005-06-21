package com.syrus.AMFICOM.client.map.props;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedList;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.mapview.Selection;

public class SelectionEditor extends DefaultStorableObjectEditor {
	Selection selection;

	private JPanel jPanel = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel countLabel = new JLabel();
	private JTextField countTextField = new JTextField();
	private JLabel elementsLabel = new JLabel();
	private WrapperedList elementsList = null;

	public SelectionEditor() {
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	private void jbInit() {
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.elementsList = new WrapperedList(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModelGeneral.getString("Properties"));

		this.countLabel.setText(LangModelMap.getString("SelectionCount"));
		this.elementsLabel.setText(LangModelMap.getString("Elements"));
		this.elementsList.setPreferredSize(new Dimension(MapVisualManager.DEF_WIDTH, MapVisualManager.DEF_HEIGHT * 4));

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
		this.jPanel.add(this.countLabel, constraints);

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
		this.jPanel.add(this.countTextField, constraints);

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
		this.jPanel.add(this.elementsLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.elementsList, constraints);
//		this.jPanel.add(Box.createGlue(), ReusedGridBagConstraints.get(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));

		this.elementsList.setEnabled(false);
		this.countTextField.setEnabled(false);
	}

	public Object getObject() {
		return this.selection;
	}

	public void setObject(Object object) {
		this.selection = (Selection )object;

		this.elementsList.removeAll();

		if(this.selection == null) {
			this.countTextField.setText("");
		}
		else {
			Set elements = this.selection.getElements();
			this.countTextField.setText(String.valueOf(elements.size()));

			this.elementsList.addElements(elements);
		}
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	public void commitChanges() {
		// empty nothing to commit
	}
}
