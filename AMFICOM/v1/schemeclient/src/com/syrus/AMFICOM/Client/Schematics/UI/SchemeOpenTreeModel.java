package com.syrus.AMFICOM.Client.Schematics.UI;

import java.awt.Color;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.SchemePackage.Type;

public class SchemeOpenTreeModel extends ObjectResourceTreeModel
{
	ApplicationContext aContext;

	private static Type[] schemeTypes = new Type[] {
		Type.NETWORK,
		Type.BUILDING,
		Type.CABLE_SUBNETWORK
	};

	public SchemeOpenTreeModel(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode(
				"root",
				"Ñõåìû",
				true,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif")));
	}

	public ImageIcon getNodeIcon(ObjectResourceTreeNode node)
	{
		return null;
	}

	public Color getNodeTextColor(ObjectResourceTreeNode node)
	{
		return null;
	}

	public void nodeAfterSelected(ObjectResourceTreeNode node)
	{
	}

	public void nodeBeforeExpanded(ObjectResourceTreeNode node)
	{
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals("root"))
				return Type.class;
		}
		else if (node.getObject() instanceof Type)
			return Scheme.class;
		return null;
	}

	public ObjectResourceController getNodeChildController(ObjectResourceTreeNode node)
	{
		if (node.getObject() instanceof String) {
			String s = (String)node.getObject();
			if (s.equals("root"))
				return null;
		}
		else if (node.getObject() instanceof Type)
			return SchemeController.getInstance();
		return null;
	}

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals("root"))
			{
				String type;
				for (int i = 0; i < schemeTypes.length; i++)
				{
					switch (schemeTypes[i].value()) {
						case Type._NETWORK:
							type = LangModelSchematics.getString("NETWORK");
							break;
						case Type._CABLE_SUBNETWORK:
							type = LangModelSchematics.getString("CABLE_SUBNETWORK");
							break;
						default:
							type = LangModelSchematics.getString("BUILDING");
					}

					vec.add(new ObjectResourceTreeNode(schemeTypes[i], type, true,
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				}
			}
		}
		else
		{
			if(node.getObject() instanceof Type)
			{
				Type type = (Type)node.getObject();
				TypicalCondition condition = new TypicalCondition(
					String.valueOf(type.value()),
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.SCHEME_ENTITY_CODE,
					com.syrus.AMFICOM.scheme.SchemeController.COLUMN_TYPE);
				try {
					List schemes = SchemeStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = schemes.iterator(); it.hasNext(); ) {
						Scheme sc = (Scheme)it.next();
						vec.add(new ObjectResourceTreeNode(sc, sc.getName(), true,
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif"))));
					}
				}
				catch (ApplicationException ex1) {
					ex1.printStackTrace();
				}
			}
		}
		return vec;
	}
}
