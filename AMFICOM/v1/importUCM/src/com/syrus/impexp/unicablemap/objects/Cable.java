/*-
 * $Id: Cable.java,v 1.8 2005/10/06 10:25:28 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.xml.XmlCableChannelingItem;
import com.syrus.AMFICOM.scheme.xml.XmlCableChannelingItemSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableLink;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableThread;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableThreadSeq;
import com.syrus.impexp.unicablemap.UniCableMapType;
import com.syrus.impexp.unicablemap.map.Link;

public class Cable {
	public static final String TYPE = UniCableMapType.UCM_CABLE_LINEAR;
	
	private String id;
	private String name;
	private Integer typeId;
	private Integer codenameId;
	private String startPortId; // порт
	private String endPortId; // порт
	private Integer layoutId;
	private List<ChannelingItem> channelingItems = new LinkedList<ChannelingItem>();
	private ChannelingItem first;
	private ChannelingItem last;
	private Collection<CableThread> threads = new LinkedList<CableThread>();
	
	private String startSiteId;
	private String endSiteId;

	private Map<String, ChannelingItem> channelingItemsMap = new HashMap<String, ChannelingItem>();
	
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
		this.endPortId = endId;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public void setStartPortId(String startId) {
		this.startPortId = startId;
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
		this.channelingItemsMap.put(item.getTunnelId(), item);
	}
	
	public void setFirstChannelingItem(ChannelingItem item) {
		this.first = item;
		this.channelingItemsMap.put(item.getTunnelId(), item);
	}

	public void setLastChannelingItem(ChannelingItem item) {
		this.last = item;
		this.channelingItemsMap.put(item.getTunnelId(), item);
	}

	int counter = 0;
	public XmlSchemeCableLink toXMLObject(XmlIdentifier parentId) {
		XmlSchemeCableLink xmlCL = XmlSchemeCableLink.Factory.newInstance();
		XmlIdentifier uid = xmlCL.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		
		if (this.name.length() > 32) {
			System.out.println("cable name (" + this.name + ") length greater then 32 symbols. cuting... ");
			this.name = this.name.substring(0, 31);
		}
		
		xmlCL.setName(this.name);
//		xmlCL.setDescription("");
		
		if (this.typeId == null) {
			System.err.println("Link typeIs is null");
			this.typeId = Integer.valueOf(0);
		}
		
		XmlIdentifier linkTypeId = xmlCL.addNewCableLinkTypeId();
		linkTypeId.setStringValue(String.valueOf(this.typeId));
		xmlCL.setCableLinkTypeId(linkTypeId);
		
		if (this.startPortId != null) {
			XmlIdentifier suid = xmlCL.addNewSourceSchemeCablePortId();
			suid.setStringValue(this.startPortId);
		} else {
			System.out.println("Cable (" + (++this.counter) + "): startId is null for " + this.name);
		}

		if (this.endPortId != null) {
			XmlIdentifier euid = xmlCL.addNewTargetSchemeCablePortId();
			euid.setStringValue(this.endPortId);
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

	public void sortChannelingItems(List<Link> links) {

		List<Link> tempLinks = new LinkedList<Link>(links);
		LinkedList<ChannelingItem> tempChannelingItems = new LinkedList<ChannelingItem>(this.channelingItems);
		LinkedList<ChannelingItem> newChannelingItems = new LinkedList<ChannelingItem>();

		String bufStartSiteId = this.startSiteId;
		String bufEndSiteId = "";

		int i = 0;
		if(this.first != null) {
			newChannelingItems.add(this.first);
			this.first.setNumber(i++);
			bufStartSiteId = this.first.getEndSiteId();
		}

		while(tempLinks.size() != 0) {
			boolean found = false;
			for(Link link : tempLinks) {
				if(link.getStartNodeId().equals(bufStartSiteId)) {
					bufEndSiteId = link.getEndNodeId();
					found = true;
				}
				if(link.getEndNodeId().equals(bufStartSiteId)) {
					bufEndSiteId = link.getStartNodeId();
					found = true;
				}
				if(found) {
					ChannelingItem channelingItem = this.channelingItemsMap.get(link.getId());
					if(channelingItem == null) {
						System.out.println("Ошибка! не задано место кабеля для кабеля '"
								+ this.name + "' (" + this.id
								+ "), тоннель '"
								+ link.getName() + "' (" + link.getId()
								+ ")!");
					}
					else {
						tempChannelingItems.remove(channelingItem);
						channelingItem.setNumber(i++);
						channelingItem.setEndSiteId(bufEndSiteId);
						channelingItem.setStartSiteId(bufStartSiteId);
						newChannelingItems.add(channelingItem);
					}
					tempLinks.remove(link);
					bufStartSiteId = bufEndSiteId;
					break;
				}
			}
			if(!found) {
				System.out.println("Ошибка! отсутствует связность прокладки кабеля '"
						+ this.name + "' (" + this.id
						+ "), начало " + this.startSiteId
						+ "), конец " + this.endSiteId);
				System.out.print("		кабель проходит по тоннелям ");
				for(Link tempLink : links) {
					System.out.print(" " + tempLink.getId());
				}
				System.out.println("");
				System.out.print("		места кабеля заданы в тоннелях ");
				for(ChannelingItem channelingItem : this.channelingItems) {
					System.out.print(" " + channelingItem.getTunnelId());
				}
				System.out.println("");
				this.channelingItems.clear();
				return;
			}
		}
		if(this.last != null) {
			newChannelingItems.add(this.last);
			this.last.setNumber(i++);
		}
		if(tempChannelingItems.size() != 0) {
			System.out.print("Ошибка! задано место кабеля '"
					+ this.name + "' (" + this.id
					+ ") без информации о прохождении кабеля по тоннелю! ");
			for(ChannelingItem channelingItem : tempChannelingItems) {
				System.out.print(" " + channelingItem.getTunnelId());
			}
			System.out.println("");
		}
		this.channelingItems = newChannelingItems;
	}

	public String getEndSiteId() {
		return this.endSiteId;
	}

	public void setEndSiteId(String endSiteId) {
		this.endSiteId = endSiteId;
	}

	public String getStartSiteId() {
		return this.startSiteId;
	}

	public void setStartSiteId(String startSiteId) {
		this.startSiteId = startSiteId;
	}

}
