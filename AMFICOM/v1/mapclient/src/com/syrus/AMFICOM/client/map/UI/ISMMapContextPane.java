package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.Map.UI.MapContextPane;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Resource.Map.ISMMapContext;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import javax.swing.JLabel;

public class ISMMapContextPane extends MapContextPane
{
	JLabel mapLabel = new JLabel();
	public ObjectResourceComboBox mapTextField = new ObjectResourceComboBox("mapcontext");

	public ISMMapContextPane()
	{
		super();
		try
		{
				jbInit();
		}
		catch(Exception e)
		{
		}
	}

	public ISMMapContextPane(MapContext mapContext)
	{
		super();
		try
		{
				jbInit();
		}
		catch(Exception e)
		{
		}
		setObjectResource(mapContext);
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.mapContext = (MapContext)or;

		idTextField.setText(((ISMMapContext)mapContext).ISM_id);
		nameTextField.setText(((ISMMapContext)mapContext).ISM_name);
		descriptionTextPane.setText(((ISMMapContext)mapContext).ISM_description);
//                ownerTextField.setText(((ISMMapContext )mapContext).ISM_user_id);
		ownerTextField.setSelected(((ISMMapContext)mapContext).ISM_user_id);
//                domainTextField.setText(((ISMMapContext )mapContext).ISM_domain_id);
		domainTextField.setSelected(((ISMMapContext)mapContext).ISM_domain_id);
//		mapTextField.setText(((ISMMapContext )mapContext).ISM_map_id);
		mapTextField.setSelected(((ISMMapContext)mapContext).ISM_map_id);
		return true;
	}

	public boolean modify()
	{
		((ISMMapContext )mapContext).ISM_id = idTextField.getText();
		((ISMMapContext )mapContext).ISM_name = nameTextField.getText();
		((ISMMapContext )mapContext).ISM_description = descriptionTextPane.getText();
//                ((ISMMapContext )mapContext).ISM_user_id = ownerTextField.getText();
		((ISMMapContext )mapContext).ISM_user_id = (String)(ownerTextField.getSelected());
//                ((ISMMapContext )mapContext).ISM_domain_id = domainTextField.getText();
		((ISMMapContext )mapContext).ISM_domain_id = (String)(domainTextField.getSelected());
//		((ISMMapContext )mapContext).ISM_map_id = mapTextField.getText();
		((ISMMapContext )mapContext).ISM_map_id = (String)(mapTextField.getSelected());
		return true;
	}

	private void jbInit() throws Exception
	{
		mapLabel.setText("Структура сети");
/*
		labelsPanel.remove(schemeLabel);
		controlsPanel.remove(schemeTextField);
		
		mainPropertiesPanel.add(mapLabel, new XYConstraints(15, 95, 100, 20));
		mainPropertiesPanel.add(mapTextField, new XYConstraints(115, 95, 100, 20));
*/
	}

}