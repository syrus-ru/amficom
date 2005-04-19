package com.syrus.AMFICOM.Client.Map.Props;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.ObjList;
import com.syrus.AMFICOM.map.Collector;

public class CollectorEditor extends DefaultStorableObjectEditor
{
	Collector collector;

	private JPanel jPanel = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel topologicalLengthLabel = new JLabel();
	private JTextField topologicalLengthTextField = new JTextField();
	private JLabel tunnelsLabel = new JLabel();
	private ObjList tunnelsList = null;
	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	public CollectorEditor()
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

		this.tunnelsList = new ObjList(controller, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModel.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.topologicalLengthLabel.setText(LangModelMap.getString("TopologicalLength"));
		this.tunnelsLabel.setText(LangModelMap.getString("StartNode"));
		this.descLabel.setText(LangModelMap.getString("Description"));
		this.tunnelsList.setPreferredSize(new Dimension(MapVisualManager.DEF_WIDTH, MapVisualManager.DEF_HEIGHT * 4));

		this.jPanel.add(this.nameLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.nameTextField, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.topologicalLengthLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.topologicalLengthTextField, ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.tunnelsLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.tunnelsList, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(new JSeparator(SwingConstants.HORIZONTAL), ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.descLabel, ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.descTextArea, ReusedGridBagConstraints.get(1, 4, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));

		super.addToUndoableListener(this.nameTextField);
		super.addToUndoableListener(this.descTextArea);
		
		this.tunnelsList.setEnabled(false);
		this.topologicalLengthTextField.setEnabled(false);
	}

	public Object getObject()
	{
		return this.collector;
	}

	public void setObject(Object object)
	{
		this.collector = (Collector)object;

		this.tunnelsList.removeAll();

		if(this.collector == null)
		{
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText("");
			this.topologicalLengthTextField.setText("");
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText("");
		}
		else
		{
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.collector.getName());
			
			this.topologicalLengthTextField.setText(String.valueOf(this.collector.getLengthLt()));

			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.collector.getDescription());

			this.tunnelsList.addElements(this.collector.getPhysicalLinks());
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
			if(!name.equals(this.collector.getName()))
				this.collector.setName(name);
			this.collector.setDescription(this.descTextArea.getText());
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
	}
}
