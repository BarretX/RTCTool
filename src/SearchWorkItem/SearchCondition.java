package SearchWorkItem;

import com.ibm.team.workitem.common.model.AttributeOperation;

public class SearchCondition {
	String itemType;
	String itemTypeValue;
	AttributeOperation attributeOperation;
	
	public SearchCondition(String itemType,	String itemTypeValue,AttributeOperation attributeOperation) 
	{
		this.itemType 			=  itemType;
		this.itemTypeValue 		=  itemTypeValue;
		this.attributeOperation	= attributeOperation;
	} 
	
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemTypeValue() {
		return itemTypeValue;
	}
	public void setItemTypeValue(String itemTypeValue) {
		this.itemTypeValue = itemTypeValue;
	}
	public AttributeOperation getAttributeOperation() {
		return attributeOperation;
	}
	public void setAttributeOperation(AttributeOperation attributeOperation) {
		this.attributeOperation = attributeOperation;
	}
	/*Operator operator;
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}*/

}
