/**
 * $Id: SiteType.java,v 1.1 2005/09/29 15:28:08 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.impexp.unicablemap.map;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeType;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeTypeSort.Enum;
import com.syrus.impexp.unicablemap.UniCableMapDatabase;
import com.syrus.impexp.unicablemap.UniCableMapObject;
import com.syrus.impexp.unicablemap.UniCableMapParameter;

public class SiteType {

	private String name;
	private String codename;
	private String description = "";
	private String uid;
	private String sort;

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String uid) {
		this.uid = uid;
	}

	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.name;
	}

	public String getId() {
		return this.uid;
	}

	public String getSort() {
		return this.sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getCodename() {
		return this.codename;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}

	public XmlSiteNodeType getXmlSiteNodeType() {
		XmlSiteNodeType xmlSiteNodeType = XmlSiteNodeType.Factory.newInstance();

		XmlIdentifier xmlId = xmlSiteNodeType.addNewId();
		xmlId.setStringValue(String.valueOf(this.uid));
		xmlSiteNodeType.setName(this.name);
		xmlSiteNodeType.setDescription(this.description);
		xmlSiteNodeType.setCodename(this.codename);
		xmlSiteNodeType.setTopological(true);
		xmlSiteNodeType.setSort(Enum.forString(this.sort));
		if(this.name == null) {
			xmlSiteNodeType.setName(this.description);
		}
		
		return xmlSiteNodeType;
	}

	public static SiteType parseSiteType(
			UniCableMapDatabase ucmDatabase,
			UniCableMapObject ucmObject,
			String sort) {
		SiteType siteType = new SiteType();

		for(UniCableMapParameter param : ucmObject.buf.params) {
			if(param.realParameter.text.equals(UniCableMapParameter.UCM_NAME_MANDATORY)) {
				siteType.setName(param.value);
			}
		}

		siteType.setDescription(ucmObject.text);
		siteType.setSort(sort);

		String id = String.valueOf(ucmObject.un);
		siteType.setId(id);
		siteType.setCodename(id);

		return siteType;
	}

}
