package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.ClientModeling_Transferable;
import com.syrus.AMFICOM.CORBA.Survey.ClientParameter_Transferable;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Test.ModelingType;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.AMFICOM.Client.Survey.Result.UI.ModelingDisplayModel;

import java.io.*;

import java.util.*;

public class Modeling extends ObjectResource implements Serializable {

	private static final long			serialVersionUID	= 01L;
	public static final String			typ					= "modeling";
	private List						argumentList;

	/**
	 * @deprecated use setter/getter pair for argumentList to access this field
	 */
	public Vector						arguments			= new Vector();
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long							deleted				= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						domain_id			= ConstStorage.SYS_DOMAIN;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						id					= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long							modified			= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						name				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						scheme_path_id		= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public ClientModeling_Transferable	transferable;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						type_id				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String						user_id				= "";

	public Modeling(ClientModeling_Transferable transferable) {
		this.transferable = transferable;
		this.argumentList = new ArrayList();
		setLocalFromTransferable();
	}

	public Modeling(String id) {
		this.argumentList = new ArrayList();
		this.id = id;
		transferable = new ClientModeling_Transferable();
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel() {
		return new ModelingDisplayModel();
	}

	public static PropertiesPanel getPropertyPane() {
		return new GeneralPanel();
	}

	public void addArgument(Parameter argument) {
		arguments.add(argument);
		argumentList.add(argument);
	}

	/**
	 * @return Returns the argumentList.
	 */
	public List getArgumentList() {
		return argumentList;
	}

	/**
	 * @deprecated use getter for argumentList
	 */
	public Enumeration getChildren(String key) {
		if (key.equals("arguments")) { return arguments.elements(); }
		return new Vector().elements();
	}

	/**
	 * @deprecated use getter for argumentList
	 */
	public Enumeration getChildTypes() {
		Vector vec = new Vector();
		vec.add("arguments");
		return vec.elements();
	}

	/**
	 * @return Returns the deleted.
	 */
	public long getDeleted() {
		return deleted;
	}

	/**
	 * @return Returns the domain_id.
	 */
	public String getDomainId() {
		return domain_id;
	}

	public String getId() {
		return id;
	}

	public ObjectResourceModel getModel() {
		return new ModelingModel(this);
	}

	public long getModified() {
		return modified;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return Returns the schemePathId.
	 */
	public String getSchemePathId() {
		return scheme_path_id;
	}

	public Object getTransferable() {
		return transferable;
	}

	public String getTyp() {
		return typ;
	}

	/**
	 * @return Returns the typeId.
	 */
	public String getTypeId() {
		return type_id;
	}

	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return user_id;
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
		this.domain_id = domainId;
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
		modified = transferable.modified;
		deleted = transferable.deleted;
		id = transferable.id;
		name = transferable.name;
		type_id = transferable.type_id;
		user_id = transferable.user_id;
		scheme_path_id = transferable.scheme_path_id;
		domain_id = transferable.domain_id;

		//arguments = new Vector();
		this.arguments.clear();
		this.argumentList.clear();

		ModelingType mt = (ModelingType) Pool.get(ModelingType.typ, type_id);

		for (int i = 0; i < transferable.arguments.length; i++) {
			Parameter param = new Parameter(transferable.arguments[i]);
			param.updateLocalFromTransferable();
			param.setApt((ActionParameterType) mt.getSortedArguments().get(
					param.getCodename()));
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
		this.scheme_path_id = schemePathId;
	}

	//	/**
	//	 * @param transferable
	//	 * The transferable to set.
	//	 */
	//	public void setTransferable(ClientModeling_Transferable transferable) {
	//		this.transferable = transferable;
	//	}

	public void setTransferableFromLocal() {
		this.transferable.modified = modified;
		this.transferable.deleted = deleted;
		this.transferable.id = id;
		this.transferable.name = name;
		this.transferable.type_id = type_id;
		this.transferable.user_id = user_id;
		this.transferable.scheme_path_id = scheme_path_id;
		this.transferable.domain_id = domain_id;

		//		transferable.arguments = new ClientParameter_Transferable[arguments
		//				.size()];
		//
		//		for (int i = 0; i < transferable.arguments.length; i++) {
		//			Parameter argument = (Parameter) arguments.get(i);
		//			argument.setTransferableFromLocal();
		//			transferable.arguments[i] = (ClientParameter_Transferable) argument
		//					.getTransferable();
		//		}

		if (this.argumentList.isEmpty()) {
			this.transferable.arguments = new ClientParameter_Transferable[this.arguments
					.size()];
			for (int i = 0; i < this.transferable.arguments.length; i++) {
				Parameter argument = (Parameter) this.arguments.get(i);
				argument.setTransferableFromLocal();
				this.transferable.arguments[i] = (ClientParameter_Transferable) argument
						.getTransferable();
			}
		} else {
			HashMap map = new HashMap();
			for (int i = 0; i < this.arguments.size(); i++) {
				Object obj = this.arguments.get(i);
				map.put(obj, obj);
			}
			for (Iterator it = this.argumentList.iterator(); it.hasNext();) {
				Object obj = it.next();
				map.put(obj, obj);
			}

			Set keySet = map.keySet();
			this.transferable.arguments = new ClientParameter_Transferable[keySet
					.size()];
			int i = 0;
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				Parameter argument = (Parameter) it.next();
				argument.setTransferableFromLocal();
				this.transferable.arguments[i++] = (ClientParameter_Transferable) argument
						.getTransferable();
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
		this.type_id = typeId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(String userId) {
		this.changed = true;
		this.user_id = userId;
	}

	public void updateLocalFromTransferable() {
		this.changed = false;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.modified = in.readLong();
		this.user_id = (String) in.readObject();
		this.deleted = in.readLong();
		this.type_id = (String) in.readObject();
		this.scheme_path_id = (String) in.readObject();
		this.domain_id = (String) in.readObject();
		this.arguments = (Vector) in.readObject();
		this.argumentList = (List) in.readObject();
		this.transferable = new ClientModeling_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeLong(this.modified);
		out.writeObject(this.user_id);
		out.writeLong(this.deleted);
		out.writeObject(this.type_id);
		out.writeObject(this.scheme_path_id);
		out.writeObject(this.domain_id);
		out.writeObject(this.arguments);
		out.writeObject(this.argumentList);
		this.changed = false;
	}
}