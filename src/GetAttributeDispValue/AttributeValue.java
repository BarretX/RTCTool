package GetAttributeDispValue;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.icu.math.BigDecimal;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.Identifier;

public class AttributeValue {
	private  AttributeToString m_AttributeAsString;
	
	AttributeValue(ITeamRepository repository,IProgressMonitor monitor,IProjectArea projectArea)
	{
		m_AttributeAsString = new AttributeToString(repository,monitor,projectArea);
	}

	public  String getValueByDisplyName(ITeamRepository repository,IWorkItem workItem,IAttribute iAttribute)
	{
		String value = "";
		try {
			if(workItem.hasAttribute(iAttribute))
			{
				Object valueObject = workItem.getValue(iAttribute);
				if(valueObject != null)
				{
					if(valueObject instanceof Boolean 
							|| valueObject instanceof Integer 
							|| valueObject instanceof Long
							|| valueObject instanceof Float
							|| valueObject instanceof BigDecimal
							)
					{
						//System.out.print("1 &&&&     "+displyName+"  ");
						return m_AttributeAsString.calculateBaseTypeToString(valueObject);
					}
					if(iAttribute.getAttributeType().equalsIgnoreCase("PhaseDetected.Enumeration"))
					{
						//System.out.print("2 &&&&     "+displyName+"  ");
						return m_AttributeAsString.calculateEnumerationLiteralAsString(valueObject, iAttribute);
					}
					if(valueObject instanceof Identifier<?>)
					{
						//System.out.print("3 &&&&     "+displyName+"  ");
						return m_AttributeAsString.calculateEnumerationLiteralAsString(valueObject, iAttribute);
					}
					if(iAttribute.getAttributeType().equalsIgnoreCase("Category"))
					{
						//System.out.print("4 &&&&     "+displyName+"  ");
						return m_AttributeAsString.calculateCategoryAsString(valueObject);
					}
					
					if(iAttribute.getAttributeType().equalsIgnoreCase("timestamp"))
					{
						//System.out.print("5 &&&&     "+displyName+"  ");
						return m_AttributeAsString.calculateTimestampAsString(valueObject);
					}
					if(iAttribute.getAttributeType().equalsIgnoreCase("contributor"))	
					{
						//System.out.print("6 &&&&     "+displyName+"  ");
						return m_AttributeAsString.calculateContributorAsString(valueObject);
					}
					if(iAttribute.getAttributeType().equalsIgnoreCase("deliverable"))	
					{
						//System.out.print("7 &&&&     "+displyName+"  ");
						return m_AttributeAsString.calculateDeliverableAsString(valueObject);
					}
					if(iAttribute.getDisplayName().equalsIgnoreCase("Type"))
					{
						return m_AttributeAsString.TypeAttribueValue(valueObject);
					}
					if(iAttribute.getDisplayName().equalsIgnoreCase("Summary"))
					{
						return m_AttributeAsString.SummaryAttribueValue(valueObject);
					}
					if(iAttribute.getDisplayName().equalsIgnoreCase("Status"))
					{
						return m_AttributeAsString.StstusAttribueValue(workItem);
					}
					if(iAttribute.getDisplayName().equalsIgnoreCase("Priority"))
					{
						return m_AttributeAsString.PriorityAttribueValue(workItem);
					}
					if(iAttribute.getDisplayName().equalsIgnoreCase("Customer Severity"))
					{
						return m_AttributeAsString.customerSeverityAttribueValue(workItem);
					}
					if(iAttribute.getDisplayName().equalsIgnoreCase("Resolution"))
					{
						return m_AttributeAsString.ResolutionAttribueValue(workItem);
					}
					if(iAttribute.getDisplayName().equalsIgnoreCase("Planned for"))
					{
						return m_AttributeAsString.getWorkItemPlannedFor(repository,workItem);
					}
					value = valueObject.toString();
			}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}		
		return value;
	}
}
