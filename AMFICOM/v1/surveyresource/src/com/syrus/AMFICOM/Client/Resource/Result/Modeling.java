/*
 * $Id: Modeling.java,v 1.1 2004/08/18 13:13:22 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.ClientModeling_Transferable;
import com.syrus.AMFICOM.CORBA.Survey.ClientParameter_Transferable;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Object.ObjectPermissionAttributes;
import com.syrus.AMFICOM.Client.Resource.Test.ModelingType;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.AMFICOM.Client.Survey.Result.UI.ModelingDisplayModel;

import java.io.*;

import java.util.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/18 13:13:22 $
 * @author $Author: bob $
 * @module surveyresource_v1
 */
public class Modeling implements ObjectResource, Serializable {

	private static final long			serialVersionUID	= 01L;
	public static final String			TYPE				= "modeling";
	/**
	 * @deprecated use TYPE
	 */
	public static final String			typ					= TYPE;

	private String						domainId			= ConstStorage.SYS_DOMAIN;
	private List						argumentList;
	private long						deleted				= 0;
	private String						id					= "";
	private long						modified			= 0;
	private String						name				= "";
	private String						schemePathId		= "";
	private ClientModeling_Transferable	transferable;
	private String						typeId				= "";
	private String						userId				= "";

	private boolean						changed				= false;

	public Modeling(ClientModeling_Transferable transferable) {
		this.transferable = transferable;
		this.argumentList = new ArrayList();
		setLocalFromTransferable();
	}

	public Modeling(String id) {
		this.argumentList = new ArrayList();
		this.id = id;
		this.transferable = new ClientModeling_Transferable();
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel() {
		return new ModelingDisplayModel();
	}

	public static PropertiesPanel getPropertyPane() {
		return new GeneralPanel();
	}

	public void addArgument(Parameter argument) {
		this.argumentList.add(argument);
	}

	/**
	 * @return Returns the argumentList.
	 */
	public List getArgumentList() {
		return this.argumentList;
	}

	/**
	 * @return Returns the deleted.
	 */
	public long getDeleted() {
		return this.deleted;
	}

	/**
	 * @return Returns the domainId.
	 */
	public String getDomainId() {
		return this.domainId;
	}

	public String getId() {
		return this.id;
	}

	public ObjectResourceModel getModel() {
		return new ModelingModel(this);
	}

	public long getModified() {
		return this.modified;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @return Returns the schemePathId.
	 */
	public String getSchemePathId() {
		return this.schemePathId;
	}

	public Object getTransferable() {
		return this.transferable;
	}

	public String getTyp() {
		return typ;
	}

	/**
	 * @return Returns the typeId.
	 */
	public String getTypeId() {
		return this.typeId;
	}

	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return this.userId;
	}

	/**
	 * @param argumentList
	 *            The argumentList to set.
	 */
	public void setArgumentList(List argumentList) {
		this.changed = true;
		this.argumentList = argumentList;
	}

	/**
	 * @param deleted
	 *            The deleted to set.
	 */
	public void setDeleted(long deleted) {
		this.changed = true;
		this.deleted = deleted;
	}

	/**
	 * @param domainId
	 *            The domainId to set.
	 */
	public void setDomainId(String domainId) {
		this.changed = true;
		this.domainId = domainId;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.changed = true;
		this.id = id;
	}

	public void setLocalFromTransferable() {
		this.modified = this.transferable.modified;
		this.deleted = this.transferable.deleted;
		this.id = this.transferable.id;
		this.name = this.transferable.name;
		this.typeId = this.transferable.type_id;
		this.userId = this.transferable.user_id;
		this.schemePathId = this.transferable.scheme_path_id;
		this.domainId = this.transferable.domain_id;

		this.argumentList.clear();

		ModelingType mt = (ModelingType) Pool.get(ModelingType.typ, this.typeId);

		for (int i = 0; i < this.transferable.arguments.length; i++) {
			Parameter param = new Parameter(this.transferable.arguments[i]);
			param.updateLocalFromTransferable();
			param.setApt((ActionParameterType) mt.getSortedArguments().get(param.getCodename()));
			this.addArgument(param);
		}
		this.changed = false;
	}

	/**
	 * @param modified
	 *            The modified to set.
	 */
	public void setModified(long modified) {
		this.changed = true;
		this.modified = modified;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.changed = true;
		this.name = name;
	}

	/**
	 * @param schemePathId
	 *            The schemePathId to set.
	 */
	public void setSchemePathId(String schemePathId) {
		this.changed = true;
		this.schemePathId = schemePathId;
	}

	public void setTransferableFromLocal() {
		this.transferable.modified = this.modified;
		this.transferable.deleted = this.deleted;
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.type_id = this.typeId;
		this.transferable.user_id = this.userId;
		this.transferable.scheme_path_id = this.schemePathId;
		this.transferable.domain_id = this.domainId;

		{
			int i = 0;
			this.transferable.arguments = new ClientParameter_Transferable[this.argumentList.size()];
			for (Iterator it = this.argumentList.iterator(); it.hasNext(); i++) {
				Parameter argument = (Parameter) it.next();
				argument.setTransferableFromLocal();
				this.transferable.arguments[i] = (ClientParameter_Transferable) argument.getTransferable();
			}
		}
		this.changed = false;
	}

	/**
	 * @param typeId
	 *            The typeId to set.
	 */
	public void setTypeId(String typeId) {
		this.changed = true;
		this.typeId = typeId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(String userId) {
		this.changed = true;
		this.userId = userId;
	}

	public void updateLocalFromTransferable() {
		this.changed = false;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.modified = in.readLong();
		this.userId = (String) in.readObject();
		this.deleted = in.readLong();
		this.typeId = (String) in.readObject();
		this.schemePathId = (String) in.readObject();
		this.domainId = (String) in.readObject();
		this.argumentList = (List) in.readObject();
		this.transferable = new ClientModeling_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeLong(this.modified);
		out.writeObject(this.userId);
		out.writeLong(this.deleted);
		out.writeObject(this.typeId);
		out.writeObject(this.schemePathId);
		out.writeObject(this.domainId);
		out.writeObject(this.argumentList);
		this.changed = false;
	}

	public ObjectPermissionAttributes getPermissionAttributes() {
		throw new UnsupportedOperationException();
	}

	public boolean isChanged() {
		return this.changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

}

