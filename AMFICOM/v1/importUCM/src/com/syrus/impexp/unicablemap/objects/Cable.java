/*-
 * $Id: Cable.java,v 1.2 2005/09/07 12:47:45 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.xml.XmlCableChannelingItem;
import com.syrus.AMFICOM.scheme.xml.XmlCableChannelingItemSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableLink;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableThread;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableThreadSeq;
import com.syrus.impexp.unicablemap.TextWriter;
import com.syrus.impexp.unicablemap.UniCableMapType;

public class Cable {
	public static final String TYPE = UniCableMapType.UCM_CABLE_LINEAR;
	
	private String id;
	private String name;
	private Integer typeId;
	private Integer codenameId;
	private String startId; // порт
	private String endId; // порт
	private Integer layoutId;
	private Collection<ChannelingItem> channelingItems = new LinkedList<ChannelingItem>();
	private ChannelingItem last;
	private Collection<CableThread> threads = new LinkedList<CableThread>();
	

	public Cable(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setCodenameId(Integer codenameId) {
		this.codenameId = codenameId;
	}

	public void setEndPortId(String endId) {
		this.endId = endId;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public void setStartPortId(String startId) {
		this.startId = startId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	
	public void setLayoutId(Integer layoutId) {
		this.layoutId = layoutId;
	}
	
	public Collection<CableThread> getThreads() {
		return this.threads;
	}

	public void setThreads(Collection<CableThread> threads) {
		this.threads = threads;
	}
	
	public void addCableThread(CableThread thread) {
		this.threads.add(thread);
	}
	
	public void addChannelingItem(ChannelingItem item) {
		item.setNumber(this.channelingItems.size());
		this.channelingItems.add(item);
	}
	
	public void setChannelingItem(ChannelingItem item) {
		this.last = item;
	}

	public void write(TextWriter writer) {
		writer.startObject(TYPE);
		writer.put("id", String.valueOf(this.id));
		writer.put("name", this.name);
		writer.put("typeid", String.valueOf(this.typeId));
		writer.put("codenameid", String.valueOf(this.codenameId));
		writer.put("startid", String.valueOf(this.startId)); // муфта или кабельный ввод
		writer.put("endid", String.valueOf(this.endId)); // муфта или кабельный ввод
		writer.put("layoutid", String.valueOf(this.layoutId));
		writer.endObject();
	}

	int counter = 0;
	public XmlSchemeCableLink toXMLObject(XmlIdentifier parentId) {
		XmlSchemeCableLink xmlCL = XmlSchemeCableLink.Factory.newInstance();
		XmlIdentifier uid = xmlCL.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		
		if (this.name.length() > 32) {
			System.err.println("cable");
		}
		
		xmlCL.setName(this.name);
//		xmlCL.setDescription("");
		
		if (this.typeId == null) {
			System.err.println("Link typeIs is null");
			this.typeId = 0;
		}
		
		XmlIdentifier linkTypeId = xmlCL.addNewCableLinkTypeId();
		linkTypeId.setStringValue(String.valueOf(this.typeId));
		xmlCL.setCableLinkTypeId(linkTypeId);
		
		if (this.startId != null) {
			XmlIdentifier suid = xmlCL.addNewSourceSchemeCablePortId();
			suid.setStringValue(this.startId);
		} else {
			System.out.println("Cable (" + (++this.counter) + "): startId is null for " + this.name);
		}

		if (this.endId != null) {
			XmlIdentifier euid = xmlCL.addNewTargetSchemeCablePortId();
			euid.setStringValue(this.endId);
		} else {
			System.out.println("Cable (" + (++this.counter) + "): endId is null for " + this.name);
		}
		
		double length = 0;
		for (ChannelingItem item : this.channelingItems) {
			length += item.getLength();
		}
		
		xmlCL.setParentSchemeId(parentId);
		
		xmlCL.setPhysicalLength(length);
		xmlCL.setOpticalLength(length);
		
		if (!this.threads.isEmpty()) {
			XmlSchemeCableThreadSeq xmlSchemeThreads = xmlCL.addNewSchemeCableThreads();

			Collection<XmlSchemeCableThread> scts = new ArrayList<XmlSchemeCableThread>(this.threads.size());
			for (Object thread : this.threads) {
				scts.add(((CableThread)thread).toXMLObject());
			}
			xmlSchemeThreads.setSchemeCableThreadArray(scts.toArray(new XmlSchemeCableThread[scts.size()]));	
		}
		
		if (!this.channelingItems.isEmpty()) {
			XmlCableChannelingItemSeq xmlChannelingItems = xmlCL.addNewCableChannelingItems();

			Collection<XmlCableChannelingItem> cis = new ArrayList<XmlCableChannelingItem>(this.channelingItems.size() + (this.last == null ? 0 : 1));
			for (Object channelingItem : this.channelingItems) {
				cis.add(((ChannelingItem)channelingItem).toXMLObject());
			}
			if (this.last != null) {
				this.last.setNumber(this.channelingItems.size());
				cis.add(this.last.toXMLObject());
			}
			xmlChannelingItems.setCableChannelingItemArray(cis.toArray(new XmlCableChannelingItem[cis.size()]));	
		}
		return xmlCL;
	}
}
