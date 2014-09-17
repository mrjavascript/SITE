package org.sitenv.portlets.ccdavalidator.models;

import java.util.ArrayList;

public class SampleCCDATreeNode {
	public SampleCCDATreeNode(String Title, String Type, String State, String attrID, String role)
	{
		SampleCCDAData data = new SampleCCDAData();
		
		data.setTitle(Title);
		
		this.setState(State);
		this.data = data;
		
		SampleCCDATreeAttribute attr = new SampleCCDATreeAttribute();
		attr.setId(attrID);
		attr.setRole(role);
		attr.setRef(Type);
		
		this.attr = attr;
		
		this.children = new ArrayList<SampleCCDATreeNode>();
		
		this.metadata = new SampleCCDAMetaData();
	}
	
	public void addChild(SampleCCDATreeNode node)
	{
		this.children.add(node);
	}
	
	private SampleCCDAMetaData metadata;
	public SampleCCDAMetaData getMetadata() {
		return metadata;
	}

	private SampleCCDAData data;
	private SampleCCDATreeAttribute attr;
	private String state;
	private ArrayList<SampleCCDATreeNode> children;
		
	public SampleCCDAData getData() {
		return data;
	}
	public void setData(SampleCCDAData data) {
		this.data = data;
	}
	public SampleCCDATreeAttribute getAttr() {
		return attr;
	}
	public void setAttr(SampleCCDATreeAttribute attr) {
		this.attr = attr;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public ArrayList<SampleCCDATreeNode> getChildren() {
		return children;
	}
	
	
}
