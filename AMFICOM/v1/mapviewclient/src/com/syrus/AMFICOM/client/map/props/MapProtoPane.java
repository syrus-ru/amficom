package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.ImagesDialog;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.resource.AbstractImageResource;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public final class MapProtoPane
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	private JLabel nameLabel = new JLabel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JTextField nameTextField = new JTextField();
	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	SiteNodeType proto;

	private LogicalNetLayer lnl;

	Identifier imageId;

	private JLabel imageLabel = new JLabel();
	private JPanel imagePanel = new JPanel();
	private JButton imageButton = new JButton();

	private ApplicationContext aContext;

	private static MapProtoPane instance = new MapProtoPane();

	private MapProtoPane()
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

	public static ObjectResourcePropertiesPane getInstance()
	{
		return instance;
	}

	public void setLogicalNetLayer(LogicalNetLayer lnl)
	{
		this.lnl = lnl;
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return this.lnl;
	}

	private void jbInit()
	{
		this.setLayout(this.gridBagLayout1);
		this.setName(LangModel.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.descLabel.setText(LangModelMap.getString("Description"));
		this.descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.imageLabel.setText(LangModelMap.getString("Image"));
		this.imageLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.imagePanel.setLayout(new BorderLayout());

		this.imageButton.setText(LangModelMap.getString("Change"));
		this.imageButton.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.imageButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					changeImage();
				}
			});

		this.add(this.nameLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.nameTextField, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.imageLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.imageButton, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.imagePanel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 1, 1, 2, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.descLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.descTextArea, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));
	}

	public Object getObject()
	{
		return null;
	}

	public void setObject(Object objectResource)
	{
		this.proto = (SiteNodeType)objectResource;
		if(this.proto == null)
		{
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText("");
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText("");

			this.imageId = null;
			this.imagePanel.removeAll();
			this.imageButton.setEnabled(false);
		}
		else
		{
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.proto.getName());
			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.proto.getDescription());

			this.imageId = this.proto.getImageId();
			this.imagePanel.removeAll();

			Image im;
			try
			{
				AbstractImageResource imageResource = (AbstractImageResource )
					ResourceStorableObjectPool.getStorableObject(this.imageId, true);
				
				ImageIcon icon = null;

				if(imageResource instanceof FileImageResource)
				{
					FileImageResource fir = (FileImageResource )imageResource;
					icon = new ImageIcon(fir.getFileName());
				}
				else
				{
					icon = new ImageIcon(imageResource.getImage());
				}

				im = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			}
			catch (CommunicationException e)
			{
				e.printStackTrace();
				return;
			}
			catch (DatabaseException e)
			{
				e.printStackTrace();
				return;
			}

			this.imagePanel.add(new JLabel(new ImageIcon(im)));
			this.imagePanel.revalidate();
			this.imageButton.setEnabled(true);
		}
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	void changeImage()
	{
		ImagesDialog frame = new ImagesDialog(this.aContext);
		
		try
		{
			AbstractImageResource imageResource = (AbstractImageResource )
				ResourceStorableObjectPool.getStorableObject(this.imageId, true);
			frame.setImageResource((BitmapImageResource )imageResource);
		}
		catch (CommunicationException e)
		{
			e.printStackTrace();
			return;
		}
		catch (DatabaseException e)
		{
			e.printStackTrace();
			return;
		}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) 
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width) 
			frameSize.width = screenSize.width;
		frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		frame.setModal(true);
		frame.setVisible(true);
		if(frame.ret_code == 1)
		{
			this.imagePanel.removeAll();
			AbstractImageResource ir = frame.getImageResource();
			this.imageId = ir.getId();
			ImageIcon icon;

			if(ir instanceof FileImageResource)
			{
				FileImageResource fir = (FileImageResource )ir;
				icon = new ImageIcon(fir.getFileName());
			}
			else
			{
				icon = new ImageIcon(ir.getImage());
			}

			
			Image im = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			this.imagePanel.add(new JLabel(new ImageIcon(im)));
			this.imagePanel.revalidate();
		}
	}

	public boolean modify()
	{
		try 
		{
			this.proto.setName(this.nameTextField.getText());
			this.proto.setDescription(this.descTextArea.getText());
			this.proto.setImageId(this.imageId);
			return true;
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
		
		return false;
	}

	public boolean create()
	{
		return false;
	}

	public boolean delete()
	{
		return false;
	}

	public boolean open()
	{
		return false;
	}

	public boolean save()
	{
		return false;
	}

	public boolean cancel()
	{
		return false;
	}
}
