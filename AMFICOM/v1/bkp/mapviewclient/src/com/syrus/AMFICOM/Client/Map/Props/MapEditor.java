package com.syrus.AMFICOM.Client.Map.Props;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.client_.general.ui_.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.VoidElement;

public class MapEditor extends DefaultStorableObjectEditor
{
	Map map;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JPanel jPanel = new JPanel();
	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel domainLabel = new JLabel();
	private ObjComboBox domainComboBox = null;
	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	public MapEditor()
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

		this.domainComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModel.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.domainLabel.setText(LangModelMap.getString("Domain"));
		this.descLabel.setText(LangModelMap.getString("Description"));

		this.jPanel.add(this.nameLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.nameTextField, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.domainLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.domainComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.descLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(new JScrollPane(this.descTextArea), ReusedGridBagConstraints.get(0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));

		this.domainComboBox.setEnabled(false);
		
		super.addToUndoableListener(this.nameTextField);
		super.addToUndoableListener(this.descTextArea);
	}

	public Object getObject()
	{
		return this.map;
	}

	public void setObject(Object object)
	{
		if(object instanceof Map)
			this.map = (Map)object;
		else
			if(object instanceof VoidElement)
				this.map = ((VoidElement )object).getMapView().getMap();
		
		this.domainComboBox.removeAllItems();

		if(this.map == null)
		{
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText("");
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText("");
		}
		else
		{
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.map.getName());

			Domain domain = null;
			Collection domains = null;
			
			StorableObjectCondition condition = 
				new EquivalentCondition(ObjectEntities.DOMAIN_ENTITY_CODE);
			try
			{
				domains = AdministrationStorableObjectPool.getStorableObjectsByCondition(
						condition,
						true);
			}
			catch (ApplicationException e)
			{
				e.printStackTrace();
			}

			try
			{
				domain = (Domain )AdministrationStorableObjectPool.getStorableObject(
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
		try 
		{
			this.map.setName(name);
			this.map.setDescription(this.descTextArea.getText());
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
	}
}
