package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import oracle.jdeveloper.layout.VerticalFlowLayout;
import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class MapLinkGeneralPanel extends GeneralPanel 
{
	MapPhysicalLinkElement maplink;

	private JPanel mainPanel = new JPanel();
	private JPanel labelsPanel = new JPanel();
	private JPanel controlsPanel = new JPanel();
	JLabel jLabel5 = new JLabel();
	JLabel jLabel4 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel1 = new JLabel();
	private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
	ObjectResourceComboBox linkComboBox = new ObjectResourceComboBox(SchemeCableLink.typ, true);
	ObjectResourceComboBox typeComboBox = new ObjectResourceComboBox("maplinkproto");
	JTextField idField = new JTextField();
	JTextField nameField = new JTextField();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel descriptionPanel = new JPanel();
	private JPanel descLabelPanel = new JPanel();
	private BorderLayout borderLayout3 = new BorderLayout();
	private VerticalFlowLayout verticalFlowLayout3 = new VerticalFlowLayout();
	JLabel jLabel7 = new JLabel();
	JScrollPane descriptionScrollPane = new JScrollPane();
	public JTextPane descriptionTextPane = new JTextPane();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	
	public MapLinkGeneralPanel()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public MapLinkGeneralPanel(MapPhysicalLinkElement maplink)
	{
		this();
		setObjectResource(maplink);
	}

	private void jbInit() throws Exception
	{
		setName("Общие");
	
//		this.setPreferredSize(new Dimension(510, 410));
//		this.setMaximumSize(new Dimension(510, 410));
//		this.setMinimumSize(new Dimension(510, 410));
		this.setLayout(gridBagLayout1);
		this.setSize(new Dimension(479, 497));

		jLabel5.setText("Элемент линии");
		jLabel5.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabel4.setText("Тип");
		jLabel4.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabel2.setText("Идентификатор");
		jLabel2.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabel1.setText("Название");
		jLabel1.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linkComboBox.setEnabled(false);
		idField.setEnabled(false);
		jLabel7.setText("Примечания");
		jLabel7.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		descriptionScrollPane.getViewport().add(descriptionTextPane, null);

		this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
		this.add(jLabel4, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jLabel5, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jLabel7, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			this.add(jLabel2, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(nameField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
		this.add(typeComboBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(linkComboBox, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(descriptionScrollPane, new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			this.add(idField, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

	}

	public ObjectResource getObjectResource()
	{
		return maplink;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.maplink = (MapPhysicalLinkElement )or;

		if(maplink == null)
		{
			linkComboBox.setSelected("");
			nameField.setText("");
			idField.setText("");
			typeComboBox.setSelected("");
			descriptionTextPane.setText("");
		}
		else
		{
			if(maplink.LINK_ID != null && !maplink.LINK_ID.equals(""))
				linkComboBox.setSelected(maplink.LINK_ID);
			else
				linkComboBox.setSelected("");

			nameField.setText(maplink.getName());
			idField.setText(maplink.getId());
			typeComboBox.setSelected(maplink.type_id);
			descriptionTextPane.setText(maplink.description);
		}
		return true;
	}

	public boolean modify()
	{
		maplink.name = nameField.getText();

		MapPhysicalLinkProtoElement mplpe = (MapPhysicalLinkProtoElement )typeComboBox.getSelectedObjectResource();
		maplink.type_id = mplpe.getId();

		maplink.description = descriptionTextPane.getText();
		return true;
	}

}