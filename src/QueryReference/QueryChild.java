package QueryReference;
import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.team.build.internal.common.model.BuildResultHandle;
import com.ibm.team.links.common.IReference;
import com.ibm.team.links.common.registry.IEndPointDescriptor;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.IAuditable;
import com.ibm.team.repository.common.Location;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.common.IWorkItemCommon;
import com.ibm.team.workitem.common.internal.model.WorkItem;
import com.ibm.team.workitem.common.internal.util.PublicAttributeHelper;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import com.ibm.team.workitem.common.model.IWorkItemReferences;
import com.ibm.team.workitem.common.model.ItemProfile;
import com.ibm.team.workitem.common.model.WorkItemEndPoints;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
public class QueryChild {
	/**
	 * Further analyze an item referenced by an URI
	 * @param iReference
	 */
	public void analyzeReferenceTarget(IReference iReference) {
		URI uri = iReference.createURI();
		try {
			System.out.println("   Resolving URI: " + uri.toString());
			ITeamRepository teamRepo = (ITeamRepository) iReference.getLink().getOrigin();
			IAuditableClient auditableClient = (IAuditableClient) teamRepo.getClientLibrary(IAuditableClient.class);

			// get the location from the URI
			Location location = Location.location(uri);
			// resolve the item by location
			IAuditable referenced = auditableClient.resolveAuditableByLocation(location,
					ItemProfile.createFullProfile(location.getItemType()), null);
			// look for a referenced work item
			if (referenced instanceof IWorkItem) {
				IWorkItem referencedWI = (IWorkItem) referenced;
				System.out.println("   Resolved URI (resolve): "
					+ uri.toString() + " to: " + referencedWI.getId()  
					+ " " + referencedWI.getState2().toString());
			}
		} catch (TeamRepositoryException e) {
			e.printStackTrace();
		}
		System.out.println("   Resolved URI: " + uri.toString());
	}
	/**
	 * Analyze an Item
	 */
	private IWorkItem analyzeItem(ITeamRepository teamRepository, Object resolvedRef) {
		//System.out.println(" Resolved item: "	+ resolvedRef.toString());
		IWorkItem item = null;
		if(resolvedRef instanceof IWorkItemHandle){
			IWorkItemHandle handle = (IWorkItemHandle)resolvedRef;
			try {
				IAuditableClient auditableClient= (IAuditableClient) teamRepository.getClientLibrary(IAuditableClient.class);
				item = auditableClient.resolveAuditable(handle, com.ibm.team.workitem.common.model.IWorkItem.FULL_PROFILE, null);
				//System.out.println(item.getId());
			} catch (Exception e) {
				// TODO: handle exception
			}
			}
		/*if(resolvedRef instanceof BuildResultHandle){
			BuildResultHandle handle = (BuildResultHandle)resolvedRef;
		}*/
		return item;
	}
	/**
	 * Analyze a reference
	 */
	public IWorkItem analyzeReference(ITeamRepository teamRepository,IReference iReference) {
		IWorkItem item = null;
		if (iReference.isItemReference()) {
			Object resolvedRef = iReference.resolve();
			item = analyzeItem(teamRepository,resolvedRef);
		}
		return item;
		/*if (iReference.isURIReference()){
			analyzeReferenceTarget(iReference);
		}*/
	}
	/**
	 * Analyze the references of a workitem
	 */
	public List<IWorkItem> analyzeReferences(ITeamRepository teamRepository,IWorkItemReferences references) {
		List<IEndPointDescriptor> endpoints = references.getTypes();
		List<IWorkItem> ChildList = new ArrayList<>();
		for (IEndPointDescriptor iEndPointDescriptor : endpoints) {
			//System.out.println("Endpoint	"	+ iEndPointDescriptor.getDisplayName());
			//List<IReference> typedReferences = references.getReferences(iEndPointDescriptor);
			
			List<IReference> typedReferences = references.getReferences(WorkItemEndPoints.CHILD_WORK_ITEMS);
			for (IReference iReference : typedReferences) {
				IWorkItem item = null;
				item = analyzeReference(teamRepository,iReference);
				if(item != null)
				{
					ChildList.add(item);
				}
			}
		}
		removeDuplicate(ChildList);
		return ChildList;
	}
	
	public void removeDuplicate(List<IWorkItem> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
    }
	
	
	
	public List<IWorkItem> FindEpicChild(IWorkItem epic,ITeamRepository teamRepository,IProgressMonitor monitor)
	{
		List<IWorkItem> ChildList = new ArrayList<>();  
		if(epic!=null)
    	{
    		try {
    				IWorkItemCommon common= (IWorkItemCommon) ((ITeamRepository)epic.getOrigin()).getClientLibrary(IWorkItemCommon.class);
    				IWorkItemReferences references = common.resolveWorkItemReferences(epic, null);
    				ChildList = analyzeReferences(teamRepository,references);
    			
			} catch (Exception e) {
				// TODO: handle exception
			}
			
    	}
		return ChildList;
	}
}
