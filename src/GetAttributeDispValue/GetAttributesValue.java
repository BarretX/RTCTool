package GetAttributeDispValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;

public class GetAttributesValue {
	private  AttributeValue m_AttributeValue;
	private  Map< String,IAttribute> allAttributeMap;
	private  IWorkItemClient client;
	
	public GetAttributesValue(ITeamRepository repository,IProgressMonitor monitor,IProjectArea projectArea)
	{
		m_AttributeValue = new AttributeValue(repository,monitor,projectArea);
		allAttributeMap = new HashMap<String, IAttribute>();
		client = (IWorkItemClient)repository.getClientLibrary(IWorkItemClient.class);
		try {
			List<IAttribute> allAttribute = client.findAttributes(projectArea, monitor);
			for(IAttribute iAttribute : allAttribute)
			{
				allAttributeMap.put(iAttribute.getDisplayName(),iAttribute);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("faild init: " + e.getMessage());
			m_AttributeValue=null;//delete object
			JOptionPane.showMessageDialog(null,"Fail init","",JOptionPane.INFORMATION_MESSAGE); 			
		}
	}
	
	public List<String> GetAllAttributeDispName()
	{
		List<String> allDispNames = new ArrayList<>();
		for(Map.Entry<String,IAttribute> entry : allAttributeMap.entrySet())
		{
			allDispNames.add(entry.getKey());
		}
		return allDispNames;
	}
	
	public List<List<String>> GetAllNeedAttribute
	(
			ITeamRepository repository,
			IProgressMonitor monitor,
			IProjectArea projectArea ,
			IQueryResult<IResolvedResult<IWorkItem>> resultAll,
			List<String> needAttributesName
	)
	{
		if(resultAll==null || needAttributesName == null)
			return null;	
		List<List<String>> valueListReturn = new ArrayList<List<String>>();
		List<IAttribute> needIAttributes = new ArrayList<>();
		try {
			for(String displyName : needAttributesName)
			{
				if(displyName==null)
					throw new IllegalArgumentException("displyName does not have");
				needIAttributes.add(allAttributeMap.get(displyName));
			}
			
			IResolvedResult<IWorkItem> resolved =null;
			IWorkItem workItem = null;
			while(resultAll.hasNext(monitor))
			{
				 resolved = resultAll.next(monitor);
				 workItem = (IWorkItem)resolved.getItem();
				 List<String> tmplist = new ArrayList<>();
				 for(IAttribute iAttribute : needIAttributes)
				 {
					 tmplist.add(m_AttributeValue.getValueByDisplyName(workItem, iAttribute));
				 }
				 valueListReturn.add(tmplist);
			}	
		}catch (TeamRepositoryException e) {
			e.printStackTrace();
		}
		return valueListReturn;
	}
}
