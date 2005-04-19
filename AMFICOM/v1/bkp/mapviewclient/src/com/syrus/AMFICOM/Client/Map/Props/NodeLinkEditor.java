package com.syrus.AMFICOM.Client.Map.Props;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.client_.general.ui_.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.map.NodeLink;

public class NodeLinkEditor extends DefaultStorableObjectEditor
{
	NodeLink nodeLink;

	private JPanel jPanel = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel topologicalLengthLabel = new JLabel();
	private JTextField topologicalLengthTextField = new JTextField();
	private JLabel linkLabel = new JLabel();
	private ObjComboBox linkComboBox = null;

	private JLabel startLabel = new JLabel();
	private ObjComboBox startComboBox = null;
	private JLabel endLabel = new JLabel();
	private ObjComboBox endComboBox = null;

	public NodeLinkEditor()
	{
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	private void jbInit()
	{
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.linkComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);
		this.startComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);
		this.endComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModel.getString("Properties"));

		this.linkLabel.setText(LangModelMap.getString("Physical_link_id"));
		this.startLabel.setText(LangModelMap.getString("StartNode"));
		this.endLabel.setText(LangModelMap.getString("EndNode"));
		this.topologicalLengthLabel.setText(LangModelMap.getString("TopologicalLength"));

		this.jPanel.add(this.linkLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.linkComboBox, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.startLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.startComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.endLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.endComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.topologicalLengthLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.topologicalLengthTextField, ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(Box.createGlue(), ReusedGridBagConstraints.get(0, 4, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));

		this.startComboBox.setEnabled(false);
		this.endComboBox.setEnabled(false);
		this.topologicalLengthTextField.setEnabled(false);
		this.linkComboBox.setEnabled(false);
	}

	public Object getObject()
	{
		return this.nodeLink;
	}

	public void setObject(Object object)
	{
		this.nodeLink = (NodeLink)object;

		this.linkComboBox.removeAllItems();
		this.startComboBox.removeAllItems();
		this.endComboBox.removeAllItems();

		if(this.nodeLink == null)
		{
			this.topologicalLengthTextField.setText("");
		}
		else
		{
			this.topologicalLengthTextField.setText(String.valueOf(this.nodeLink.getLengthLt()));

			this.linkComboBox.addItem(this.nodeLink.getPhysicalLink());
			this.linkComboBox.setSelectedItem(this.nodeLink.getPhysicalLink());

			this.startComboBox.addItem(this.nodeLink.getStartNode());
			this.startComboBox.setSelectedItem(this.nodeLink.getStartNode());
			this.endComboBox.addItem(this.nodeLink.getEndNode());
			this.endComboBox.setSelectedItem(this.nodeLink.getEndNode());
		}
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	public void commitChanges() {
		// empty - nothing to commit
	}
}
