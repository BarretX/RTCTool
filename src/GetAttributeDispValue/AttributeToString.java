package GetAttributeDispValue;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.icu.math.BigDecimal;
import com.ibm.team.process.common.IIteration;
import com.ibm.team.process.common.IIterationHandle;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.process.common.IProjectAreaHandle;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.IContributorHandle;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.IWorkItemCommon;
import com.ibm.team.workitem.common.internal.model.DeliverableHandle;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IAttributeHandle;
import com.ibm.team.workitem.common.model.ICategoryHandle;
import com.ibm.team.workitem.common.model.IDeliverable;
import com.ibm.team.workitem.common.model.IEnumeration;
import com.ibm.team.workitem.common.model.ILiteral;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.Identifier;
import com.ibm.team.workitem.common.workflow.ICombinedWorkflowInfos;

public class AttributeToString {

	private ITeamRepository repository;
	private IProgressMonitor monitor;
	private IProjectArea projectArea; 
	
	private IProjectAreaHandle prjAreaHandle;
	private IWorkItemClient client;
	private IWorkItemCommon itemCommon;
	AttributeToString(ITeamRepository repository,IProgressMonitor monitor,IProjectArea projectArea)
	{
		this.repository = repository;
		this.monitor = monitor;
		this.projectArea = projectArea;
		client = (IWorkItemClient)repository.getClientLibrary(IWorkItemClient.class);
		prjAreaHandle = (IProjectAreaHandle)projectArea.getItemHandle();
		itemCommon = (IWorkItemCommon)repository.getClientLibrary(IWorkItemCommon.class);
	}

	/**
	 * convert to string representation form a base type
	 * 
	 * @param value
	 * @param format
	 * @return
	 */
	public String calculateBaseTypeToString(Object value) 
	{
		String valueString = "";
		if(value instanceof Boolean)
		{
			valueString = value.toString();
		}
		if (value == null) 
		{
			valueString = "";
		}
		if (value instanceof Integer) 
		{
			valueString = ((Integer) value).toString();
		}
		if (value instanceof Long)
		{
			valueString = ((Long) value).toString();
		}
		if (value instanceof Float) 
		{
			valueString = ((Float) value).toString();
		}
		if (value instanceof BigDecimal)
		{
			valueString = ((BigDecimal) value).toString();
		}
		return valueString;
	}
	
	/**
	 * Compute the string representation for a category
	 * 
	 * @param value
	 * @return
	 * @throws TeamRepositoryException
	 */
	public String calculateCategoryAsString(Object value)
	{
		String valueString = "";
		try {
			if (value != null) {
				if (value instanceof ICategoryHandle) {
					valueString = itemCommon.resolveHierarchicalName((ICategoryHandle) value, monitor);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return valueString;
	}
	
	/**
	 * convert to string form one enumeration literal
	 * 
	 * @param value
	 * @param attribute
	 * @return
	 */
	public String calculateEnumerationLiteralAsString(Object value,IAttribute attribute)
	{
		String valueString = "";
		try {
			
			if (value == null) {
				return "";
			}
			if (!(value instanceof Identifier<?>)) {
				return "Value not an enumeration literal";
			}
			IEnumeration<? extends ILiteral> enumeration = itemCommon.resolveEnumeration(attribute, monitor);
			Identifier<? extends ILiteral> currentIdentifier = (Identifier<? extends ILiteral>) value;
			ILiteral literal = enumeration.findEnumerationLiteral(currentIdentifier);
			valueString = literal.getName();
		} catch (Exception e) {
			
		}
		
		return valueString;
	}
	
	/**
	 * convert to string form one deliveraable literal
	 * 
	 * @param value
	 * @param attribute
	 * @return
	 */
	public String calculateDeliverableAsString(Object value)
	{
		String valueString = "";
		try {
					IDeliverable deliverable = (IDeliverable)repository.itemManager().fetchCompleteItem
							(
									(DeliverableHandle)value, 
									IItemManager.DEFAULT,
									monitor
									);
					valueString=deliverable.getName();
		} catch (Exception e) {
		}
		
		return valueString;
	}
	
	/**
	 * Compute the string representation for a timestamp
	 * 
	 * 
	 * @param value
	 * @return
	 */
	public String calculateTimestampAsString(Object value) 
	{
		String valueString = "";
		if (value != null) 
		{
			valueString = value.toString();
		}
		return valueString;
	}

	/**
	 * convert to string  form  contributor/user
	 * 
	 * @param value
	 * @return
	 * @throws TeamRepositoryException
	 */
	public String calculateContributorAsString(Object value)
	{
		String valueString = "";
		if (value == null) {
			return "";
		}
		try {
			if ((value instanceof IContributorHandle)) 
			{
				IContributor contributor = (IContributor) repository.itemManager().fetchCompleteItem
						(
						(IContributorHandle) value, 
						IItemManager.DEFAULT,
						monitor
						);
				valueString=contributor.getName();
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return valueString;		
	}
	
	/**
	 * Get the IIteration object from a handle
	 * 
	 * @param handle
	 * @return
	 * @throws TeamRepositoryException
	 */
	public IIteration resolveIteration(IIterationHandle handle)
	{
		IIteration iteration = null;
		if (handle instanceof IIteration) 
		{
			return (IIteration) handle;
		}
		try 
		{
			iteration = (IIteration) repository.itemManager().fetchCompleteItem
					(
							(IIterationHandle) handle,
							IItemManager.DEFAULT, 
							monitor
					);
		} catch (Exception e)
		{
			// TODO: handle exception
		}		
		return iteration;
	}

	private  ILiteral getILiteralByID (Identifier findLiteralID, IAttributeHandle iAttribute)throws TeamRepositoryException 
	{
		IWorkItemClient workItemClient = (IWorkItemClient)repository.getClientLibrary(IWorkItemClient.class);
		IEnumeration enumeration = workItemClient.resolveEnumeration(iAttribute, null);
		
		List literalsList = enumeration.getEnumerationLiterals();
		for(Iterator iterator = literalsList.iterator(); iterator.hasNext();)
		{
			ILiteral iLiteral =(ILiteral)iterator.next();
			if(iLiteral.getIdentifier2().equals(findLiteralID))
			{
				return iLiteral;
			}
		}return null;
	}

	public String TypeAttribueValue(Object value)
	{
		return value.toString();
	}
	
	public String SummaryAttribueValue(Object value)
	{
		return value.toString();
	}

	public String StstusAttribueValue(IWorkItem workItem)
	{
		String state = "";
		try {
			ICombinedWorkflowInfos wfInfo = client.findCombinedWorkflowInfos(prjAreaHandle, null);
			state = wfInfo.getStateName(workItem.getState2());
		}catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}
	
	public String PriorityAttribueValue(IWorkItem workItem)
	{
		String priority = "";
		try {
			IAttribute prityAttribute = client.findAttribute(projectArea, IWorkItem.PRIORITY_PROPERTY, null);
			Object valueObject = workItem.getPriority();
			if(valueObject instanceof Identifier)
			{
				Identifier literalIdentifier = (Identifier)valueObject;
				ILiteral literal = getILiteralByID(literalIdentifier,prityAttribute);
				priority = literal.getName();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return priority;
	}
	
	public String customerSeverityAttribueValue(IWorkItem workItem)
	{
		String customerSeverity = "";
		try {
			IAttribute prityAttribute = client.findAttribute(projectArea, IWorkItem.SEVERITY_PROPERTY, null);
			Object valueObject = workItem.getSeverity();
			if(valueObject instanceof Identifier)
			{
				Identifier literalIdentifier = (Identifier)valueObject;
				ILiteral literal = getILiteralByID(literalIdentifier,prityAttribute);
				customerSeverity = literal.getName();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return customerSeverity;
	}
	
	public String ResolutionAttribueValue(IWorkItem workItem)
	{
		String Resolution = "";
		try {
			ICombinedWorkflowInfos wfInfo = client.findCombinedWorkflowInfos(prjAreaHandle, null);
			Resolution = wfInfo.getResolutionName(workItem.getResolution2());
		}catch (Exception e) {
			// TODO: handle exception
		}
		return Resolution;
	}
}
