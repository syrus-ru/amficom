package com.syrus.AMFICOM.Client.Map.Props;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Set;

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
import com.syrus.AMFICOM.client_.general.ui_.ObjList;
import com.syrus.AMFICOM.mapview.Selection;

public class SelectionEditor extends DefaultStorableObjectEditor
{
	Selection selection;

	private JPanel jPanel = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel countLabel = new JLabel();
	private JTextField countTextField = new JTextField();
	private JLabel elementsLabel = new JLabel();
	private ObjList elementsList = null;

	public SelectionEditor()
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

		this.elementsList = new ObjList(controller, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModel.getString("Properties"));

		this.countLabel.setText(LangModelMap.getString("SelectionCount"));
		this.elementsLabel.setText(LangModelMap.getString("Elements"));
		this.elementsList.setPreferredSize(new Dimension(MapVisualManager.DEF_WIDTH, MapVisualManager.DEF_HEIGHT * 4));

		this.jPanel.add(this.countLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.countTextField, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.elementsLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.elementsList, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, null, 0, 0));
//		this.jPanel.add(Box.createGlue(), ReusedGridBagConstraints.get(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));

		this.elementsList.setEnabled(false);
		this.countTextField.setEnabled(false);
	}

	public Object getObject()
	{
		return this.selection;
	}

	public void setObject(Object object)
	{
		this.selection = (Selection)object;

		this.elementsList.removeAll();

		if(this.selection == null)
		{
			this.countTextField.setText("");
		}
		else
		{
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
