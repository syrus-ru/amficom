package com.syrus.AMFICOM.Client.Map.Props;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.SiteNodeTypeSort;

public class CollectorEditor extends DefaultStorableObjectEditor
{
	Collector collector;

	private JPanel jPanel = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel topologicalLengthLabel = new JLabel();
	private JTextField topologicalLengthTextField = new JTextField();
	private JLabel piquetsLabel = new JLabel();
	private ObjList piquetsList = null;
	private JScrollPane piquetsScrollPane = new JScrollPane();
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

		this.piquetsList = new ObjList(controller, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModel.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.topologicalLengthLabel.setText(LangModelMap.getString("TopologicalLength"));
		this.piquetsLabel.setText(LangModelMap.getString("piquet"));
		this.descLabel.setText(LangModelMap.getString("Description"));

		this.piquetsScrollPane.setPreferredSize(new Dimension(MapVisualManager.DEF_WIDTH, MapVisualManager.DEF_HEIGHT * 4));
		this.piquetsScrollPane.setMinimumSize(new Dimension(MapVisualManager.DEF_WIDTH, MapVisualManager.DEF_HEIGHT * 4));
		this.piquetsScrollPane.setMaximumSize(new Dimension(MapVisualManager.DEF_WIDTH, MapVisualManager.DEF_HEIGHT * 4));
		this.piquetsScrollPane.getViewport().add(this.piquetsList);

		this.jPanel.add(this.nameLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.nameTextField, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.topologicalLengthLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.topologicalLengthTextField, ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.piquetsLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.piquetsScrollPane, ReusedGridBagConstraints.get(0, 3, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(new JSeparator(SwingConstants.HORIZONTAL), ReusedGridBagConstraints.get(0, 4, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));

		this.jPanel.add(this.descLabel, ReusedGridBagConstraints.get(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(new JScrollPane(this.descTextArea), ReusedGridBagConstraints.get(0, 6, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));

		super.addToUndoableListener(this.nameTextField);
		super.addToUndoableListener(this.descTextArea);
		
		this.piquetsList.setEnabled(false);
		this.topologicalLengthTextField.setEnabled(false);
	}

	public Object getObject()
	{
		return this.collector;
	}

	public void setObject(Object object)
	{
		this.collector = (Collector)object;

		this.piquetsList.removeAll();

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

			Set piquets = new HashSet();
			
			for(Iterator iter = this.collector.getPhysicalLinks().iterator(); iter.hasNext();) {
				PhysicalLink link = (PhysicalLink )iter.next();
				
				if(link.getStartNode() instanceof SiteNode
						&& ((SiteNodeType )((SiteNode )link.getStartNode()).getType()).getSort().equals(SiteNodeTypeSort.PIQUET))
					piquets.add(link.getStartNode());
				if(link.getEndNode() instanceof SiteNode
						&& ((SiteNodeType )((SiteNode )link.getEndNode()).getType()).getSort().equals(SiteNodeTypeSort.PIQUET))
					piquets.add(link.getEndNode());
			}

			this.piquetsList.addElements(piquets);
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
