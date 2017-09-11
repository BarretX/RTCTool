package Login;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.ibm.team.process.client.IProcessClientService;
import com.ibm.team.process.client.IProcessItemService;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.ITeamRepository.ILoginHandler.ILoginInfo;
import com.ibm.team.repository.client.ITeamRepository.ILoginHandler;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.TeamRepositoryException;

public class LoginHandler implements ILoginHandler, ILoginInfo{
	private  String userId;
	private  String password;
	private  String repositoryURI;
	private  IProgressMonitor monitor;
	public LoginHandler(String userId, String password, String repositoryURI) 
	{
		 this.userId = userId;
		 this.password = password;
		 this.repositoryURI=repositoryURI;
		 monitor = new NullProgressMonitor();
	}
	public String getUserId() 
	{
	        return userId;
	}

	public String getPassword() 
	{
	        return password;
	}
	public String getRepositoryURI() 
	{
	    	return repositoryURI;
	}
    public IProgressMonitor getMonitor()
    {
    	return this.monitor;
    }
	public ILoginInfo challenge(ITeamRepository repository) 
	{
	        return this;
	}
	public ITeamRepository login()
	{
		System.out.println("RTC team platform starting...");
		
		//Initiate the RTC platform TeamPlatform
		TeamPlatform.startup();										
		System.out.println("getting team repository service...");
		
		//Get the RTC repository connection through TeamPlatform
		ITeamRepository teamRepository = TeamPlatform.getTeamRepositoryService().getTeamRepository(repositoryURI);  
		
		//Register your users on RTC repository
		try {
			System.out.println("registering your login handler...");
			teamRepository.registerLoginHandler(new LoginHandler(userId, password, repositoryURI));
			System.out.println("login your monitor...");
			
			//maybe throw TeamRepositoryException
			teamRepository.login(monitor);
			
			if(teamRepository != null)
			{
				System.out.println(userId+" login successfully" );
				//Show a dialog
				//JOptionPane.showMessageDialog(null,"Success to Login","",JOptionPane.INFORMATION_MESSAGE); 
			}			
		} catch (TeamRepositoryException e) {
			System.out.println("Unable to login: " + e.getMessage());
			JOptionPane.showMessageDialog(null,"Fail to Login","",JOptionPane.INFORMATION_MESSAGE); 
			TeamPlatform.shutdown();
		}
		return teamRepository;
	}
	
	public List<?> GetAllProcessArea(ITeamRepository repository,IProgressMonitor monitor)
	{
		List<?> iProcessAreas = new ArrayList<>();
		IContributor userContributor = repository.loggedInContributor();
		IProcessItemService iProcessItemService = (IProcessItemService)repository.getClientLibrary(IProcessItemService.class);
		try {
			iProcessAreas =  iProcessItemService.findProcessAreas(userContributor, null, IProcessClientService.ALL_PROPERTIES, monitor);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Unable to Get Process Area: " + e.getMessage());
		}
		return iProcessAreas;
	}
		
	public void logoff() {
		 TeamPlatform.shutdown();
	}
}
