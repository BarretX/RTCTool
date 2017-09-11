package SearchWorkItem;

import java.net.URI;
import java.util.List;

import javax.swing.JOptionPane;
import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.team.process.client.IProcessClientService;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.IQueryClient;
import com.ibm.team.workitem.common.expression.AttributeExpression;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.expression.IQueryableAttributeFactory;
import com.ibm.team.workitem.common.expression.QueryableAttributes;
import com.ibm.team.workitem.common.expression.Term;
import com.ibm.team.workitem.common.expression.Term.Operator;
import com.ibm.team.workitem.common.internal.model.WorkItem;
import com.ibm.team.workitem.common.model.AttributeOperation;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;

import com.ibm.team.workitem.common.workflow.ICombinedWorkflowInfos;
import com.ibm.team.process.common.IProjectAreaHandle;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;

public class MulConditionQuery 
{
	
	private IQueryResult<IResolvedResult<IWorkItem>> result;
	private IProjectArea projectArea;
		
	public IProjectArea getProjectArea() {
		return projectArea;
	}
	
	public IQueryResult<IResolvedResult<IWorkItem>> queryByCondition
	(
			ITeamRepository  			repository,
			IProgressMonitor 			monitor, 
			String 						projectAreaName,
			Operator 					operator,
			List<SearchCondition> 		searchConditionList
	)
	{
		//through RTC repository link gain Query client:queryClient, Audit client:auditableClient and Process service client:processClient.
		IProcessClientService processClient= (IProcessClientService) repository.getClientLibrary(IProcessClientService.class);
		IQueryClient queryClient= (IQueryClient) repository.getClientLibrary(IQueryClient.class);
		IAuditableClient auditableClient= (IAuditableClient) repository.getClientLibrary(IAuditableClient.class);	
		
		//Replace the space between the URI by ASCII
		URI uri= URI.create(projectAreaName.replaceAll(" ", "%20"));
				
		//combine of multiple condition
		Term term=null;
		if(operator == null)
		{
			term= new Term(Operator.AND);
		}
		else 
		{
			term= new Term(operator);
		}
		
		try
		{
			//storage attribute name
			IQueryableAttribute iQueryableAttribute;
			
			//generate one condition
			AttributeExpression attributeExpression = null;
			
			//through the URL of Process Area as parameter get a reference of process area
			projectArea = (IProjectArea)processClient.findProcessArea(uri, null, null);
			
			//Get the query object of project area
			IQueryableAttributeFactory factory = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE);
			
			//maybe throw TeamRepositoryException  or  AssertionFailedException
			IQueryableAttribute projectAreaAttribute = factory.findAttribute(projectArea, IWorkItem.PROJECT_AREA_PROPERTY, auditableClient, monitor);
			
			//Build the query conditional expression  project area
			AttributeExpression projectAreaExpression = new AttributeExpression(projectAreaAttribute,AttributeOperation.EQUALS,projectArea);
			term.add(projectAreaExpression);
			
			String itemType;
			String itemTypeValue;
			AttributeOperation attributeOperation;
			
			//combine every condition
			for(int i = 0;i<searchConditionList.size();i++)
			{
				if(searchConditionList.get(i).getItemType()==null ||  searchConditionList.get(i).getItemTypeValue()==null)
				{
					System.out.println("faild hhh:");
					throw new IllegalArgumentException("Condition argument have some rong meesage");
				}
				
				itemType     		= searchConditionList.get(i).getItemType();
				itemTypeValue		= searchConditionList.get(i).getItemTypeValue();
				attributeOperation	= searchConditionList.get(i).getAttributeOperation();
				if(attributeOperation==null)
				{
					attributeOperation = AttributeOperation.EQUALS;
				}
				
				iQueryableAttribute = factory.findAttribute(projectArea, itemType.toString(), auditableClient, monitor);		
				attributeExpression= new AttributeExpression(iQueryableAttribute, attributeOperation, itemTypeValue);
				
				term.add(attributeExpression);
			}		
			result=queryClient.getResolvedExpressionResults(projectArea,term,IWorkItem.FULL_PROFILE);
		}
		catch ( Exception e) 
		{
			System.out.println("faild search: " + e.getMessage());
			JOptionPane.showMessageDialog(null,"Fail search","",JOptionPane.INFORMATION_MESSAGE); 
		}
		
		return result;
	}

	
	public void TraverseResultGetProperties(ITeamRepository repository,IProgressMonitor monitor,IQueryResult<IResolvedResult<IWorkItem>> resultAll)
	{
		if(resultAll ==null)
			return;
		try {
			int tmpcount = 0;
			while(resultAll.hasNext(monitor))
			{
			    IWorkItem wi = (IWorkItem)(resultAll.next(monitor).getItem());
				Long estimate = wi.getDuration()/3600000;
				IWorkItemClient wiCommon = (IWorkItemClient)repository.getClientLibrary(IWorkItemClient.class);
				IProjectAreaHandle prjAreaHandle = (IProjectAreaHandle)projectArea.getItemHandle();
				ICombinedWorkflowInfos wfInfo = wiCommon.findCombinedWorkflowInfos(prjAreaHandle, null);
				String state = wfInfo.getStateName(wi.getState2());
				//String type = wi.getWorkItemType();
				int ID = wi.getId();
				//if(ID == 45159  || ID==45162)
				IWorkItemClient itemClient= (IWorkItemClient)repository.getClientLibrary(IWorkItemClient.class);
				IAttribute attribute = itemClient.findAttribute(projectArea, WorkItem.DUE_DATE_PROPERTY, null);
				
				System.out.println("number:"+ tmpcount +"  estimate: " + estimate+"  state: " + state+"  spendtime: " + wi.getValue(attribute) +"  ID:"+ID);
				tmpcount++;
			}			
		} catch (TeamRepositoryException e) {
			e.printStackTrace();
		}
		
	}
}

