package sit.portal.directtransport.services.model;

import java.util.ArrayList;

public class jsTreeNode {
	public jsTreeNode(String Title, String Type, String State, String attrID, String role)
	{
		jsData data = new jsData();
		
		data.setTitle(Title);
		
		this.setState(State);
		this.data = data;
		
		jsTreeAttribute attr = new jsTreeAttribute();
		attr.setId(attrID);
		attr.setRole(role);
		attr.setRef(Type);
		
		this.attr = attr;
		
		this.children = new ArrayList<jsTreeNode>();
		
		this.metadata = new jsMetaData();
	}
	
	public void addChild(jsTreeNode node)
	{
		this.children.add(node);
	}
	
	private jsMetaData metadata;
	public jsMetaData getMetadata() {
		return metadata;
	}

	private jsData data;
	private jsTreeAttribute attr;
	private String state;
	private ArrayList<jsTreeNode> children;
		
	public jsData getData() {
		return data;
	}
	public void setData(jsData data) {
		this.data = data;
	}
	public jsTreeAttribute getAttr() {
		return attr;
	}
	public void setAttr(jsTreeAttribute attr) {
		this.attr = attr;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public ArrayList<jsTreeNode> getChildren() {
		return children;
	}
	
	
}
