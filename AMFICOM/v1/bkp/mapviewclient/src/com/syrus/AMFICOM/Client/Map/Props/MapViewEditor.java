package com.syrus.AMFICOM.Client.Map.Props;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
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
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.VoidElement;

public class MapViewEditor extends DefaultStorableObjectEditor
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JPanel jPanel = new JPanel();
	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel mapLabel = new JLabel();
	private ObjComboBox mapComboBox = null;
	private JLabel domainLabel = new JLabel();
	private ObjComboBox domainComboBox = null;

	private JLabel longLabel = new JLabel();
	private JTextField longTextField = new JTextField();
	private JLabel latLabel = new JLabel();
	private JTextField latTextField = new JTextField();
	private JLabel scaleLabel = new JLabel();
	private JTextField scaleTextField = new JTextField();

	private JLabel schemesLabel = new JLabel();
	private JScrollPane schemesScrollPane = new JScrollPane();
	private ObjectResourceListBox schemesList = new ObjectResourceListBox();

	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	MapView mapView;

	public MapViewEditor()
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
		this.mapComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModel.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.mapLabel.setText(LangModelMap.getString("Map"));
		this.domainLabel.setText(LangModelMap.getString("Domain"));
		this.longLabel.setText(LangModelMap.getString("Longitude"));
		this.latLabel.setText(LangModelMap.getString("Latitude"));
		this.scaleLabel.setText(LangModelMap.getString("Scale"));
		this.schemesLabel.setText(LangModelMap.getString("Schemes"));
		this.schemesScrollPane.setPreferredSize(new Dimension(MapVisualManager.DEF_WIDTH, MapVisualManager.DEF_HEIGHT * 4));
		this.schemesScrollPane.setMinimumSize(new Dimension(MapVisualManager.DEF_WIDTH, MapVisualManager.DEF_HEIGHT * 4));
		this.schemesScrollPane.setMaximumSize(new Dimension(MapVisualManager.DEF_WIDTH, MapVisualManager.DEF_HEIGHT * 4));
		this.schemesScrollPane.getViewport().add(this.schemesList);

		this.descLabel.setText(LangModelMap.getString("Description"));

		this.jPanel.add(this.nameLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.nameTextField, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.mapLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.mapComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.domainLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.domainComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.longLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.longTextField, ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.latLabel, ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.latTextField, ReusedGridBagConstraints.get(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.scaleLabel, ReusedGridBagConstraints.get(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.scaleTextField, ReusedGridBagConstraints.get(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.schemesLabel, ReusedGridBagConstraints.get(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.schemesScrollPane, ReusedGridBagConstraints.get(0, 7, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(Box.createHorizontalStrut(5), ReusedGridBagConstraints.get(1, 8, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.descLabel, ReusedGridBagConstraints.get(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(new JScrollPane(this.descTextArea), ReusedGridBagConstraints.get(0, 10, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));

		this.mapComboBox.setEnabled(false);
		this.domainComboBox.setEnabled(false);

		super.addToUndoableListener(this.nameTextField);
		super.addToUndoableListener(this.longTextField);
		super.addToUndoableListener(this.latTextField);
		super.addToUndoableListener(this.scaleTextField);
		super.addToUndoableListener(this.descTextArea);
	}

	public Object getObject()
	{
		return this.mapView;
	}

	public void setObject(Object objectResource)
	{
		if(objectResource instanceof VoidElement)
		{
			this.mapView = ((VoidElement)objectResource).getMapView();
		}
		else
			this.mapView = (MapView )objectResource;

		this.domainComboBox.removeAllItems();
		this.mapComboBox.removeAllItems();

		if(this.mapView == null)
		{
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText("");
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText("");

			this.longTextField.setEnabled(false);
			this.longTextField.setText("");
			this.latTextField.setEnabled(false);
			this.latTextField.setText("");

			this.scaleTextField.setEnabled(false);
			this.scaleTextField.setText("");
			
			this.schemesList.removeAll();
		}
		else
		{
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.mapView.getName());

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
						this.mapView.getDomainId(),
						false);
			} catch(ApplicationException e) {
				e.printStackTrace();
			}

			this.domainComboBox.addElements(domains);
			this.domainComboBox.setSelectedItem(domain);

//			Collection maps = null;
//
//			StorableObjectCondition domainCondition = 
//				new LinkedIdsCondition(domain.getId(), ObjectEntities.MAP_ENTITY_CODE);
//			try
//			{
//				maps = MapStorableObjectPool.getStorableObjectsByCondition(
//						domainCondition,
//						true);
//			}
//			catch (ApplicationException e)
//			{
//				e.printStackTrace();
//			}
//			this.mapComboBox.addElements(maps);
			this.mapComboBox.addItem(this.mapView.getMap());
			this.mapComboBox.setSelectedItem(this.mapView.getMap());

			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.mapView.getDescription());

			this.longTextField.setEnabled(true);
			this.longTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(this.mapView.getCenter().getX()));
			this.latTextField.setEnabled(true);
			this.latTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(this.mapView.getCenter().getY()));

			this.scaleTextField.setEnabled(true);
			this.scaleTextField.setText(String.valueOf(this.mapView.getScale()));
			
			this.schemesList.setContents(this.mapView.getSchemes());
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
			this.mapView.setName(name);
			this.mapView.setDescription(this.descTextArea.getText());

			try 
			{
				double x = Double.parseDouble(this.longTextField.getText());
				double y = Double.parseDouble(this.longTextField.getText());
				
				this.mapView.setCenter(new DoublePoint(x, y));

				double s = Double.parseDouble(this.scaleTextField.getText());
				
				this.mapView.setScale(s);
			} 
			catch (NumberFormatException ex) 
			{
				System.out.println(ex.getMessage());
			} 
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
	}
}
