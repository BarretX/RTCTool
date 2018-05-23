package Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.ibm.team.repository.client.ITeamRepository;
import Login.LoginHandler;
import ConstVar.*;
import DataForChart.CCWDataFactory;
import DataForChart.CommunicationsDataFactory;
import DataForChart.FTSPDataFactory;
import DataForChart.FTViewSEDataFactory;
import DataForChart.ProductData;
import DataForChart.S5KADataFactory;
import Helper.XmlParseHelper;
import ChartManage.ChartManager;
import Charts.*;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Program {

	public static void main(String[] args) {
		
		System.out.println("RTC Tool Starting!");

		 Date DateBegin=new Date();
		loginRTC();
		createCharts();
		logoffRTC();
		
		//Output
		printCharts();
		Date DateEnd=new Date();
		
		System.out.println(DateBegin.toString());
		System.out.println(DateEnd.toString());
		
	}
	
	//Login/off RTC
	public static LoginHandler handler=null;
	public static ITeamRepository repository=null;
	public static void loginRTC()
	{
	    handler=new LoginHandler(ConstString.USER_ID, ConstString.PASSWORD, ConstString.REPOSITORY_URI);
	    repository=handler.login();
	}
	public static void createCharts()
	{			
		createChartsForFTViewSE_PM();	saveFTViewArt_UrlToXml();	
		createChartsForFTSP_PM();		saveFTSP_UrlToXml();
		createChartsForCommunications_PM();	saveCommunications_UrlToXml();	
	}
	
	public static Map<String,String> ChartSets=new HashMap<String,String>();

	public static void logoffRTC()
	{
	    handler.logoff();
	}	
	
	
	//Output Charts:
	public static void printCharts()
	{
		System.out.println();
		System.out.println();
		System.out.println("**********************Chart Output Begin**********************");
		System.out.println("**************************************************************");
		
		System.out.println(ConstString.FTVIEWSE_NAME);
		if(ChartSetForFTViewSE_PM!=null)
			System.out.println("*ChartSet*"+ConstString.FTVIEWSE_PM_CHARTSET_NAME+": "+ ConstString.CHART_SET_URL + ChartSetForFTViewSE_PM);
		if(ChartsForFTViewSE_PM!=null)
			printMap(ChartsForFTViewSE_PM);
		if(ChartSetForFTViewSE_QA!=null)
			System.out.println("*ChartSet*"+ConstString.FTVIEWSE_QA_CHARTSET_NAME+": "+ ConstString.CHART_SET_URL + ChartSetForFTViewSE_QA);
		if(ChartsForFTViewSE_QA!=null)
			printMap(ChartsForFTViewSE_QA);
		
		System.out.println(ConstString.FTSP_NAME);
		if(ChartSetForFTSP_PM!=null)
			System.out.println("*ChartSet*"+ConstString.FTSP_PM_CHARTSET_NAME+": "+ ConstString.CHART_SET_URL + ChartSetForFTSP_PM);
		if(ChartsForFTSP_PM!=null)
			printMap(ChartsForFTSP_PM);
		
		System.out.println(ConstString.Communications_NAME);
		if(ChartSetForCommunications_PM!=null)
			System.out.println("*ChartSet*"+ConstString.Communications_PM_CHARTSET_NAME+": "+ ConstString.CHART_SET_URL + ChartSetForCommunications_PM);
		if(ChartsForCommunications_PM!=null)
			printMap(ChartsForCommunications_PM);
		
		System.out.println("**********************Chart Output End************************");
		System.out.println("**************************************************************");
		System.out.println();
		System.out.println();
	}
	
	
	//Create charts for S5KA
	public static Map<String,String> ChartsForS5KA_PM= null;
	public static String ChartSetForS5KA_PM=null;
	public static void createChartsForS5KA_PM()
	{
		List<String> chartSetIDList=new ArrayList<String>();
		ChartsForS5KA_PM = new HashMap<String,String>();
		
		ProductData d1=S5KADataFactory.Get_S5KA_PM_Data_Weekly_Trend();
		if(null != d1)
		{
			ChartManager Mgr_S5KA_PM_CHART_Weekly_Trend=new ChartManager(d1, new LineChart(ConstString.S5KA_PM_CHART_Weekly_Trend));
			String id1=Mgr_S5KA_PM_CHART_Weekly_Trend.createChartInEagleEye();
			chartSetIDList.add(id1);
			ChartsForS5KA_PM.put(ConstString.S5KA_PM_CHART_Weekly_Trend, id1);
		}
		
		ProductData d2=S5KADataFactory.Get_S5KA_PM_Data_Trend_Epic();
		if(null != d2)
		{
			ChartManager Mgr_S5KA_PM_CHART_Trend_Epic=new ChartManager(d2, new ColumnChart(ConstString.S5KA_PM_CHART_Trend_Epic));
			String id2=Mgr_S5KA_PM_CHART_Trend_Epic.createChartInEagleEye();
			chartSetIDList.add(id2);
			ChartsForS5KA_PM.put(ConstString.S5KA_PM_CHART_Trend_Epic, id2);
		}
		
		ProductData d3=S5KADataFactory.Get_S5KA_PM_Data_Trend_Team();
		if(null != d3)
		{
			ChartManager Mgr_S5KA_PM_CHART_Trend_Team=new ChartManager(d3, new ColumnChart(ConstString.S5KA_PM_CHART_Trend_Team));
			String id3=Mgr_S5KA_PM_CHART_Trend_Team.createChartInEagleEye();
			chartSetIDList.add(id3);
			ChartsForS5KA_PM.put(ConstString.S5KA_PM_CHART_Trend_Team, id3);
		}
		
		ProductData d4=S5KADataFactory.Get_S5KA_PM_Data_ThroughputVelocity_sprint();
		if(null != d4)
		{
			ChartManager Mgr_S5KA_PM_CHART_ThroughputVelocity_sprint=new ChartManager(d4, new ComboChart(ConstString.S5KA_PM_CHART_ThroughputVelocity_sprint));
			String id4=Mgr_S5KA_PM_CHART_ThroughputVelocity_sprint.createChartInEagleEye();
			chartSetIDList.add(id4);
			ChartsForS5KA_PM.put(ConstString.S5KA_PM_CHART_ThroughputVelocity_sprint, id4);
		}
		
		ProductData d5=S5KADataFactory.Get_S5KA_PM_Data_Plan_Actual_Sprint();
		if(null != d5)
		{
			ChartManager Mgr_S5KA_PM_CHART_Plan_Actual_Sprint=new ChartManager(d5, new ColumnChart(ConstString.S5KA_PM_CHART_Plan_Actual_Sprint));
			String id5=Mgr_S5KA_PM_CHART_Plan_Actual_Sprint.createChartInEagleEye();
			chartSetIDList.add(id5);
			ChartsForS5KA_PM.put(ConstString.S5KA_PM_CHART_Plan_Actual_Sprint, id5);
		}
		
		ProductData d6=S5KADataFactory.Get_S5KA_PM_Data_Feature_Progress();
		if(null != d6)
		{
			ChartManager Mgr_S5KA_PM_CHART_Feature_Progress=new ChartManager(d6, new BarChart(ConstString.S5KA_PM_CHART_Feature_Progress));
			String id6=Mgr_S5KA_PM_CHART_Feature_Progress.createChartInEagleEye();
			chartSetIDList.add(id6);
			ChartsForS5KA_PM.put(ConstString.S5KA_PM_CHART_Feature_Progress, id6);
		}

		if(null != chartSetIDList && chartSetIDList.size()>0)
		{
			ChartSetForS5KA_PM=ChartManager.CreateChartSet(ConstString.S5KA_PM_CHARTSET_NAME,null,chartSetIDList);
			ChartSets.put(ConstString.S5KA_PM_CHARTSET_NAME, ChartSetForS5KA_PM);
		}
	}
	
	public static Map<String,String> ChartsForS5KA_QA= null;
	public static String ChartSetForS5KA_QA=null;
	public static void createChartsForS5KA_QA()
	{
		List<String> chartSetIDList=new ArrayList<String>();
		ChartsForS5KA_QA = new HashMap<String,String>();
		
		ProductData d1=S5KADataFactory.Get_S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly();
		if(null != d1)
		{
			ChartManager Mgr_S5KA_QA_CHART_FY17_Anomlay_Backlog_Weekly=new ChartManager(d1 ,new ColumnChart(ConstString.S5KA_QA_CHART_FY17_Anomlay_Backlog_Weekly));
			String id1=Mgr_S5KA_QA_CHART_FY17_Anomlay_Backlog_Weekly.createChartInEagleEye();
			chartSetIDList.add(id1);
			ChartsForS5KA_QA.put(ConstString.S5KA_QA_CHART_FY17_Anomlay_Backlog_Weekly, id1);
		}
		
		ProductData d2=S5KADataFactory.Get_S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly();
		if(null != d2)
		{
			ChartManager Mgr_S5KA_QA_CHART_FY17_Anomlay_Backlog_Monthly=new ChartManager(d2 ,new ColumnChart(ConstString.S5KA_QA_CHART_FY17_Anomlay_Backlog_Monthly));
			String id2=Mgr_S5KA_QA_CHART_FY17_Anomlay_Backlog_Monthly.createChartInEagleEye();
			chartSetIDList.add(id2);
			ChartsForS5KA_QA.put(ConstString.S5KA_QA_CHART_FY17_Anomlay_Backlog_Monthly, id2);
		}
		
		ProductData d3=S5KADataFactory.Get_S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint();
		if(null != d3)
		{
			ChartManager Mgr_S5KA_QA_CHART_FY17_Anomlay_Backlog_Sprint=new ChartManager(d3,new ColumnChart(ConstString.S5KA_QA_CHART_FY17_Anomlay_Backlog_Sprint));
			String id3=Mgr_S5KA_QA_CHART_FY17_Anomlay_Backlog_Sprint.createChartInEagleEye();
			chartSetIDList.add(id3);
			ChartsForS5KA_QA.put(ConstString.S5KA_QA_CHART_FY17_Anomlay_Backlog_Sprint, id3);
		}
		
		ProductData d4=S5KADataFactory.Get_S5KA_QA_Data_Shippable_RC_anomaly_Weekly();
		if(null != d4)
		{
			ChartManager Mgr_S5KA_QA_CHART_Shippable_RC_anomaly_Weekly=new ChartManager(d4 ,new ColumnChart(ConstString.S5KA_QA_CHART_Shippable_RC_anomaly_Weekly));
			String id4=Mgr_S5KA_QA_CHART_Shippable_RC_anomaly_Weekly.createChartInEagleEye();
			chartSetIDList.add(id4);
			ChartsForS5KA_QA.put(ConstString.S5KA_QA_CHART_Shippable_RC_anomaly_Weekly, id4);
		}
		
		ProductData d5 = S5KADataFactory.Get_S5KA_QA_Data_Shippable_RC_anomaly_monthly();
		if(null != d5)
		{
			ChartManager Mgr_S5KA_QA_CHART_Shippable_RC_anomaly_monthly=new ChartManager(d5,new ColumnChart(ConstString.S5KA_QA_CHART_Shippable_RC_anomaly_monthly));
			String id5=Mgr_S5KA_QA_CHART_Shippable_RC_anomaly_monthly.createChartInEagleEye();
			chartSetIDList.add(id5);
			ChartsForS5KA_QA.put(ConstString.S5KA_QA_CHART_Shippable_RC_anomaly_monthly, id5);
		}
		
		ProductData d6= S5KADataFactory.Get_S5KA_QA_Data_Shippable_RC_anomaly_Sprint();
		if(null != d6)
		{
			ChartManager Mgr_S5KA_QA_CHART_Shippable_RC_anomaly_Sprint=new ChartManager(d6 ,new ColumnChart(ConstString.S5KA_QA_CHART_Shippable_RC_anomaly_Sprint));
			String id6=Mgr_S5KA_QA_CHART_Shippable_RC_anomaly_Sprint.createChartInEagleEye();
			chartSetIDList.add(id6);
			ChartsForS5KA_QA.put(ConstString.S5KA_QA_CHART_Shippable_RC_anomaly_Sprint, id6);
		}
		
		ProductData d7=S5KADataFactory.Get_S5KA_QA_Data_Runrate_all_anomaly();
		if(null != d7)
		{
			ChartManager Mgr_S5KA_QA_CHART_Runrate_all_anomaly=new ChartManager(d7 ,new LineChart(ConstString.S5KA_QA_CHART_Runrate_all_anomaly));
			String id7=Mgr_S5KA_QA_CHART_Runrate_all_anomaly.createChartInEagleEye();
			chartSetIDList.add(id7);
			ChartsForS5KA_QA.put(ConstString.S5KA_QA_CHART_Runrate_all_anomaly, id7);
		}
		
		ProductData d8=S5KADataFactory.Get_S5KA_QA_Data_Runrate_RC();
		if(null != d8)
		{
			ChartManager Mgr_S5KA_QA_CHART_Runrate_RC=new ChartManager( d8,new LineChart(ConstString.S5KA_QA_CHART_Runrate_RC));
			String id8=Mgr_S5KA_QA_CHART_Runrate_RC.createChartInEagleEye();
			chartSetIDList.add(id8);
			ChartsForS5KA_QA.put(ConstString.S5KA_QA_CHART_Runrate_RC, id8);
		}
		
		ProductData d9 =S5KADataFactory.Get_S5KA_QA_Data_Stability_all_anomaly();
		if(null != d9)
		{
			ChartManager Mgr_S5KA_QA_CHART_Stability_all_anomaly=new ChartManager(d9 ,new LineChart(ConstString.S5KA_QA_CHART_Stability_all_anomaly));
			String id9=Mgr_S5KA_QA_CHART_Stability_all_anomaly.createChartInEagleEye();
			chartSetIDList.add(id9);
			ChartsForS5KA_QA.put(ConstString.S5KA_QA_CHART_Stability_all_anomaly, id9);
		}
		
		ProductData d10 =S5KADataFactory.Get_S5KA_QA_Data_Stability_RC();
		if(null != d10)
		{
			ChartManager Mgr_S5KA_QA_CHART_Stability_RC=new ChartManager(d10 ,new LineChart(ConstString.S5KA_QA_CHART_Stability_RC));
			String id10=Mgr_S5KA_QA_CHART_Stability_RC.createChartInEagleEye();
			chartSetIDList.add(id10);
			ChartsForS5KA_QA.put(ConstString.S5KA_QA_CHART_Stability_RC, id10);
		}
		
		if(null != chartSetIDList && chartSetIDList.size()>0)
		{
			ChartSetForS5KA_QA=ChartManager.CreateChartSet(ConstString.S5KA_QA_CHARTSET_NAME,null,chartSetIDList);
			ChartSets.put(ConstString.S5KA_QA_CHARTSET_NAME, ChartSetForS5KA_QA);
		}
	}
	
	//Create charts for CCW
	public static Map<String,String> ChartsForCCW_PM= null;
	public static String ChartSetForCCW_PM=null;
	public static void createChartsForCCW_PM()
	{
		List<String> chartSetIDList=new ArrayList<String>();
		ChartsForCCW_PM = new HashMap<String,String>();
		
		ProductData d1=CCWDataFactory.Get_CCW_PM_Data_Weekly_Trend();
		if(null != d1)
		{
			ChartManager Mgr_CCW_PM_CHART_Weekly_Trend=new ChartManager(d1, new LineChart(ConstString.CCW_PM_CHART_Weekly_Trend));
			String id1=Mgr_CCW_PM_CHART_Weekly_Trend.createChartInEagleEye();
			chartSetIDList.add(id1);
			ChartsForCCW_PM.put(ConstString.CCW_PM_CHART_Weekly_Trend, id1);
		}
		
		ProductData d2=CCWDataFactory.Get_CCW_PM_Data_Trend_Epic();
		if(null != d2)
		{
			ChartManager Mgr_CCW_PM_CHART_Trend_Epic=new ChartManager(d2, new ColumnChart(ConstString.CCW_PM_CHART_Trend_Epic));
			String id2=Mgr_CCW_PM_CHART_Trend_Epic.createChartInEagleEye();
			chartSetIDList.add(id2);
			ChartsForCCW_PM.put(ConstString.CCW_PM_CHART_Trend_Epic, id2);
		}
		
		ProductData d3=CCWDataFactory.Get_CCW_PM_Data_Trend_Team();
		if(null != d3)
		{
			ChartManager Mgr_CCW_PM_CHART_Trend_Team=new ChartManager(d3, new ColumnChart(ConstString.CCW_PM_CHART_Trend_Team));
			String id3=Mgr_CCW_PM_CHART_Trend_Team.createChartInEagleEye();
			chartSetIDList.add(id3);
			ChartsForCCW_PM.put(ConstString.CCW_PM_CHART_Trend_Team, id3);
		}
		
		ProductData d4=CCWDataFactory.Get_CCW_PM_Data_ThroughputVelocity_sprint();
		if(null != d4)
		{
			ChartManager Mgr_CCW_PM_CHART_ThroughputVelocity_sprint=new ChartManager(d4, new ComboChart(ConstString.CCW_PM_CHART_ThroughputVelocity_sprint));
			String id4=Mgr_CCW_PM_CHART_ThroughputVelocity_sprint.createChartInEagleEye();
			chartSetIDList.add(id4);
			ChartsForCCW_PM.put(ConstString.CCW_PM_CHART_ThroughputVelocity_sprint, id4);
		}
		
		ProductData d5=CCWDataFactory.Get_CCW_PM_Data_Plan_Actual_Sprint();
		if(null != d5)
		{
			ChartManager Mgr_CCW_PM_CHART_Plan_Actual_Sprint=new ChartManager(d5, new ColumnChart(ConstString.CCW_PM_CHART_Plan_Actual_Sprint));
			String id5=Mgr_CCW_PM_CHART_Plan_Actual_Sprint.createChartInEagleEye();
			chartSetIDList.add(id5);
			ChartsForCCW_PM.put(ConstString.CCW_PM_CHART_Plan_Actual_Sprint, id5);
		}
		
		ProductData d6=CCWDataFactory.Get_CCW_PM_Data_Feature_Progress();
		if(null != d6)
		{
			ChartManager Mgr_CCW_PM_CHART_Feature_Progress=new ChartManager(d6, new BarChart(ConstString.CCW_PM_CHART_Feature_Progress));
			String id6=Mgr_CCW_PM_CHART_Feature_Progress.createChartInEagleEye();
			chartSetIDList.add(id6);
			ChartsForCCW_PM.put(ConstString.CCW_PM_CHART_Feature_Progress, id6);
		}

		if(null != chartSetIDList && chartSetIDList.size()>0)
		{
			ChartSetForCCW_PM=ChartManager.CreateChartSet(ConstString.CCW_PM_CHARTSET_NAME,null,chartSetIDList);
			ChartSets.put(ConstString.CCW_PM_CHARTSET_NAME, ChartSetForCCW_PM);
		}
	}
	public static Map<String,String> ChartsForCCW_QA= null;
	public static String ChartSetForCCW_QA=null;
	public static void createChartsForCCW_QA()
	{
		
	}
	
	//Create charts for FTAC
	public static Map<String,String> ChartsForFTAC_PM= null;
	public static String ChartSetForFTAC_PM=null;
	public static void createChartsForFTAC_PM()
	{	
	}
	public static Map<String,String> ChartsForFTAC_QA= null;
	public static String ChartSetForFTAC_QA=null;
	public static void createChartsForFTAC_QA()
	{
	}

	//Create charts for FTViewSE
	public static Map<String,String> ChartsForFTViewSE_PM= null;
	public static String ChartSetForFTViewSE_PM=null;
	public static void createChartsForFTViewSE_PM()
	{
		List<String> chartSetIDList=new ArrayList<String>();
		ChartsForFTViewSE_PM = new HashMap<String,String>();
		
		ProductData d1=FTViewSEDataFactory.Get_FTVIEWSE_PM_Data_Weekly_Trend();
		if(null != d1)
		{
			ChartManager Mgr_FTVIEWSE_PM_CHART_Weekly_Trend=new ChartManager(d1, new LineChart(ConstString.FTVIEWSE_PM_CHART_Weekly_Trend));
			String id1=Mgr_FTVIEWSE_PM_CHART_Weekly_Trend.createChartInEagleEye();
			chartSetIDList.add(id1);
			ChartsForFTViewSE_PM.put(ConstString.FTVIEWSE_PM_CHART_Weekly_Trend, id1);
		}
		
		ProductData d2=FTViewSEDataFactory.Get_FTVIEWSE_PM_Data_BurnDown();
		if(null != d2)
		{
			ChartManager Mgr_FTVIEWSE_PM_CHART_BurnDown=new ChartManager(d2, new LineChart(ConstString.FTVIEWSE_PM_CHART_Weekly_BurnDown));
			String id2=Mgr_FTVIEWSE_PM_CHART_BurnDown.createChartInEagleEye();
			chartSetIDList.add(id2);
			ChartsForFTViewSE_PM.put(ConstString.FTVIEWSE_PM_CHART_Weekly_BurnDown, id2);
		}
		
		ProductData d3=FTViewSEDataFactory.Get_FTVIEWSE_PM_Data_Plan_Actual_Sprint();
		if(null != d3)
		{
			ChartManager Mgr_FTVIEWSE_PM_CHART_Plan_Actual_Sprint=new ChartManager(d3, new ComboChart(ConstString.FTVIEWSE_PM_CHART_Plan_Actual_Sprint));
			String id3=Mgr_FTVIEWSE_PM_CHART_Plan_Actual_Sprint.createChartInEagleEye();
			chartSetIDList.add(id3);
			ChartsForFTViewSE_PM.put(ConstString.FTVIEWSE_PM_CHART_Plan_Actual_Sprint, id3);
		}
	
		/*ProductData d4=FTViewSEDataFactory.Get_FTVIEWSE_PM_Data_ThroughputVelocity_sprint();
		if(null != d4)
		{
			ChartManager Mgr_FTVIEWSE_PM_CHART_ThroughputVelocity_sprint=new ChartManager(d4, new ComboChart(ConstString.FTVIEWSE_PM_CHART_ThroughputVelocity_sprint));
			String id4=Mgr_FTVIEWSE_PM_CHART_ThroughputVelocity_sprint.createChartInEagleEye();
			chartSetIDList.add(id4);
			ChartsForFTViewSE_PM.put(ConstString.FTVIEWSE_PM_CHART_ThroughputVelocity_sprint, id4);
		}*/
		
		/*ProductData d5=FTViewSEDataFactory.Get_FTVIEWSE_PM_Data_Trend_Team();
		if(null != d5)
		{
			ChartManager Mgr_FTVIEWSE_PM_CHART_Trend_Team=new ChartManager(d5, new ColumnChart(ConstString.FTVIEWSE_PM_CHART_Trend_Team));
			String id5=Mgr_FTVIEWSE_PM_CHART_Trend_Team.createChartInEagleEye();
			chartSetIDList.add(id5);
			ChartsForFTViewSE_PM.put(ConstString.FTVIEWSE_PM_CHART_Trend_Team, id5);
		}		*/
		
		ProductData d6=FTViewSEDataFactory.Get_FTVIEWSE_PM_Data_All_Epic();
		if(null != d6)
		{
			ChartManager Mgr_FTVIEWSE_PM_CHART_Feature_Progress=new ChartManager(d6, new BarChart(ConstString.FTVIEWSE_PM_CHART_Feature_Progress));
			String id6=Mgr_FTVIEWSE_PM_CHART_Feature_Progress.createChartInEagleEye();
			chartSetIDList.add(id6);
			ChartsForFTViewSE_PM.put(ConstString.FTVIEWSE_PM_CHART_Feature_Progress, id6);
		}	
		
	/*	ProductData d7=FTViewSEDataFactory.Get_FTVIEWSE_PM_Data_Trend_Epic();
		if(null != d7)
		{
			ChartManager Mgr_FTVIEWSE_PM_CHART_Trend_Epic=new ChartManager(d7, new ColumnChart(ConstString.FTVIEWSE_PM_CHART_Trend_Epic));
			String id7=Mgr_FTVIEWSE_PM_CHART_Trend_Epic.createChartInEagleEye();
			chartSetIDList.add(id7);
			ChartsForFTViewSE_PM.put(ConstString.FTVIEWSE_PM_CHART_Trend_Epic, id7);
		}*/
	 
		if(null != chartSetIDList && chartSetIDList.size()>0)
		{
			ChartSetForFTViewSE_PM=ChartManager.CreateChartSet(ConstString.FTVIEWSE_PM_CHARTSET_NAME,null,chartSetIDList);
			ChartSets.put(ConstString.FTVIEWSE_PM_CHARTSET_NAME, ChartSetForFTViewSE_PM);
		}
	}
	
		public static Map<String,String> ChartsForFTSP_PM= null;
		public static String ChartSetForFTSP_PM=null;
		public static void createChartsForFTSP_PM()
		{
			List<String> chartSetIDList=new ArrayList<String>();
			ChartsForFTSP_PM = new HashMap<String,String>();
			
			ProductData d1=FTSPDataFactory.Get_FTSP_PM_Data_Weekly_Trend();
			if(null != d1)
			{
				ChartManager Mgr_FTSP_PM_CHART_Weekly_Trend=new ChartManager(d1, new LineChart(ConstString.FTSP_PM_CHART_Weekly_Trend));
				String id1=Mgr_FTSP_PM_CHART_Weekly_Trend.createChartInEagleEye();
				chartSetIDList.add(id1);
				ChartsForFTSP_PM.put(ConstString.FTSP_PM_CHART_Weekly_Trend, id1);
			}
			
			ProductData d2=FTSPDataFactory.Get_FTSP_PM_Data_BurnDown();
			if(null != d2)
			{
				ChartManager Mgr_FTSP_PM_CHART_BurnDown=new ChartManager(d2, new LineChart(ConstString.FTSP_PM_CHART_Weekly_BurnDown));
				String id2=Mgr_FTSP_PM_CHART_BurnDown.createChartInEagleEye();
				chartSetIDList.add(id2);
				ChartsForFTSP_PM.put(ConstString.FTSP_PM_CHART_Weekly_BurnDown, id2);
			}
			
			ProductData d3=FTSPDataFactory.Get_FTSP_PM_Data_Plan_Actual_Sprint();
			if(null != d3)
			{
				ChartManager Mgr_FTSP_PM_CHART_Plan_Actual_Sprint=new ChartManager(d3, new ComboChart(ConstString.FTSP_PM_CHART_Plan_Actual_Sprint));
				String id3=Mgr_FTSP_PM_CHART_Plan_Actual_Sprint.createChartInEagleEye();
				chartSetIDList.add(id3);
				ChartsForFTSP_PM.put(ConstString.FTSP_PM_CHART_Plan_Actual_Sprint, id3);
			}
			
			ProductData d6=FTSPDataFactory.Get_FTSP_PM_Data_All_Epic();
			if(null != d6)
			{
				ChartManager Mgr_FTSP_PM_CHART_Feature_Progress=new ChartManager(d6, new BarChart(ConstString.FTSP_PM_CHART_Feature_Progress));
				String id6=Mgr_FTSP_PM_CHART_Feature_Progress.createChartInEagleEye();
				chartSetIDList.add(id6);
				ChartsForFTSP_PM.put(ConstString.FTSP_PM_CHART_Feature_Progress, id6);
			}	
					 
			if(null != chartSetIDList && chartSetIDList.size()>0)
			{
				ChartSetForFTSP_PM=ChartManager.CreateChartSet(ConstString.FTSP_PM_CHARTSET_NAME,null,chartSetIDList);
				ChartSets.put(ConstString.FTSP_PM_CHARTSET_NAME, ChartSetForFTSP_PM);
			}
		}
		
		public static Map<String,String> ChartsForCommunications_PM= null;
		public static String ChartSetForCommunications_PM=null;
		public static void createChartsForCommunications_PM()
		{
			List<String> chartSetIDList=new ArrayList<String>();
			ChartsForCommunications_PM = new HashMap<String,String>();
			
			ProductData d1=CommunicationsDataFactory.Get_Communications_PM_Data_Weekly_Trend();
			if(null != d1)
			{
				ChartManager Mgr_Communications_PM_CHART_Weekly_Trend=new ChartManager(d1, new LineChart(ConstString.Communications_PM_CHART_Weekly_Trend));
				String id1=Mgr_Communications_PM_CHART_Weekly_Trend.createChartInEagleEye();
				chartSetIDList.add(id1);
				ChartsForCommunications_PM.put(ConstString.Communications_PM_CHART_Weekly_Trend, id1);
			}
			
			ProductData d2=CommunicationsDataFactory.Get_Communications_PM_Data_BurnDown();
			if(null != d2)
			{
				ChartManager Mgr_Communications_PM_CHART_BurnDown=new ChartManager(d2, new LineChart(ConstString.Communications_PM_CHART_Weekly_BurnDown));
				String id2=Mgr_Communications_PM_CHART_BurnDown.createChartInEagleEye();
				chartSetIDList.add(id2);
				ChartsForCommunications_PM.put(ConstString.Communications_PM_CHART_Weekly_BurnDown, id2);
			}
			
			ProductData d3=CommunicationsDataFactory.Get_Communications_PM_Data_Plan_Actual_Sprint();
			if(null != d3)
			{
				ChartManager Mgr_Communications_PM_CHART_Plan_Actual_Sprint=new ChartManager(d3, new ComboChart(ConstString.Communications_PM_CHART_Plan_Actual_Sprint));
				String id3=Mgr_Communications_PM_CHART_Plan_Actual_Sprint.createChartInEagleEye();
				chartSetIDList.add(id3);
				ChartsForCommunications_PM.put(ConstString.Communications_PM_CHART_Plan_Actual_Sprint, id3);
			}
			
			ProductData d6=CommunicationsDataFactory.Get_Communications_PM_Data_All_Epic();
			if(null != d6)
			{
				ChartManager Mgr_Communications_PM_CHART_Feature_Progress=new ChartManager(d6, new BarChart(ConstString.Communications_PM_CHART_Feature_Progress));
				String id6=Mgr_Communications_PM_CHART_Feature_Progress.createChartInEagleEye();
				chartSetIDList.add(id6);
				ChartsForCommunications_PM.put(ConstString.Communications_PM_CHART_Feature_Progress, id6);
			}	
					 
			if(null != chartSetIDList && chartSetIDList.size()>0)
			{
				ChartSetForCommunications_PM=ChartManager.CreateChartSet(ConstString.Communications_PM_CHARTSET_NAME,null,chartSetIDList);
				ChartSets.put(ConstString.Communications_PM_CHARTSET_NAME, ChartSetForCommunications_PM);
			}
		}
		
		
	public static Map<String,String> ChartsForFTViewSE_QA= null;
	public static String ChartSetForFTViewSE_QA=null;
	public static void createChartsForFTViewSE_QA()
	{
		
	}

	public static void printMap(Map<String,String> map)
	{
		for (String name : map.keySet())
			System.out.println("<Chart>"+name +":    "+ ConstString.CHART_URL +map.get(name));
	}
	
	public static void saveS5KA_UrlToXml(Document document)
	{
		if(document==null)
			return;
		Node s5kaNode=document.selectSingleNode("/Products/Bucket[@name='Logix']/Product[@name ='S5KA']");
		if(s5kaNode==null)
			return;

		if(ChartsForS5KA_PM!=null)
		{
			for(String key:ChartsForS5KA_PM.keySet())
			{
				Element e=(Element)s5kaNode.selectSingleNode(String.format(".//Chart[@name=\'%s\']", key));
				if(e != null)
					e.addAttribute("url", ConstString.CHART_URL + ChartsForS5KA_PM.get(key));
			}
		}
		if(ChartsForS5KA_QA!=null)
		{
			for(String key:ChartsForS5KA_QA.keySet())
			{
				Element e=(Element)s5kaNode.selectSingleNode(String.format(".//Chart[@name=\'%s\']", key));
				if(e != null)
					e.addAttribute("url", ConstString.CHART_URL + ChartsForS5KA_QA.get(key));
			}
		}
		updateXmlTime(s5kaNode);
	}
	public static void saveFTViewSE_UrlToXml(Document document)
	{
		if(document==null)
			return;
		Node FTViewSENode=document.selectSingleNode("/Products/Bucket[@name='FTView']/Product[@name ='FTView SE']");
		if(FTViewSENode==null)
			return;
		if(ChartsForFTViewSE_PM!=null)
		{
			for(String key:ChartsForFTViewSE_PM.keySet())
			{
				Element e=(Element)FTViewSENode.selectSingleNode(String.format(".//Chart[@name=\'%s\']", key));
				if(e != null)
					e.addAttribute("url", ConstString.CHART_URL + ChartsForFTViewSE_PM.get(key));
			}
		}

		if(ChartsForFTViewSE_QA!=null)
		{
			for(String key:ChartsForFTViewSE_QA.keySet())
			{
				Element e=(Element)FTViewSENode.selectSingleNode(String.format(".//Chart[@name=\'%s\']", key));
				if(e != null)
					e.addAttribute("url", ConstString.CHART_URL + ChartsForFTViewSE_QA.get(key));
			}
		}
		updateXmlTime(FTViewSENode);
	}
	
	public static void saveFTViewArt_UrlToXml()
	{
		Document document=XmlParseHelper.load("config.xml");
		
		if(document==null)
			return;
		Node FTViewArtNode=document.selectSingleNode("/Products/Bucket[@name='FTView']/Product[@name ='FTView ART']");
		if(FTViewArtNode==null)
			return;
		if(ChartsForFTViewSE_PM!=null)
		{
			for(String key:ChartsForFTViewSE_PM.keySet())
			{
				Element e=(Element)FTViewArtNode.selectSingleNode(String.format(".//Chart[@name=\'%s\']", key));
				if(e != null)
					e.addAttribute("url", ConstString.CHART_URL + ChartsForFTViewSE_PM.get(key));
			}
		}

		if(ChartsForFTViewSE_QA!=null)
		{
			for(String key:ChartsForFTViewSE_QA.keySet())
			{
				Element e=(Element)FTViewArtNode.selectSingleNode(String.format(".//Chart[@name=\'%s\']", key));
				if(e != null)
					e.addAttribute("url", ConstString.CHART_URL + ChartsForFTViewSE_QA.get(key));
			}
		}
		
		if(ChartSetForFTViewSE_PM!=null)
		{
				Element e=(Element)FTViewArtNode.selectSingleNode(String.format(".//Chart[@name=\'%s\']", ConstString.FTVIEWSE_PM_CHARTSET_NAME));
				if(e != null)
					e.addAttribute("url", ConstString.CHART_SET_URL + ChartSetForFTViewSE_PM);
		}
		updateXmlTime(FTViewArtNode);
		
		XmlParseHelper.persist(document,"config.xml");
	}
	
	public static void saveCommunications_UrlToXml()
	{
		Document document=XmlParseHelper.load("config.xml");
		
		if(document==null)
			return;
		Node CommunicationsNode=document.selectSingleNode("/Products/Bucket[@name='Services']/Product[@name ='Communications']");
		if(CommunicationsNode==null)
			return;
		if(ChartsForCommunications_PM!=null)
		{
			for(String key:ChartsForCommunications_PM.keySet())
			{
				Element e=(Element)CommunicationsNode.selectSingleNode(String.format(".//Chart[@name=\'%s\']", key));
				if(e != null)
					e.addAttribute("url", ConstString.CHART_URL + ChartsForCommunications_PM.get(key));
			}
		}
		
		if(ChartSetForCommunications_PM!=null)
		{
			Element e=(Element)CommunicationsNode.selectSingleNode(String.format(".//Chart[@name=\'%s\']", ConstString.Communications_PM_CHARTSET_NAME));
			if(e != null)
				e.addAttribute("url", ConstString.CHART_SET_URL + ChartSetForCommunications_PM);
		}
		updateXmlTime(CommunicationsNode);
		
		XmlParseHelper.persist(document,"config.xml");
	}
	
	public static void saveFTSP_UrlToXml()
	{
		Document document=XmlParseHelper.load("config.xml");
		
		if(document==null)
			return;
		Node FTSPNode=document.selectSingleNode("/Products/Bucket[@name='Services']/Product[@name ='FTSP']");
		if(FTSPNode==null)
			return;
		if(ChartsForFTSP_PM!=null)
		{
			for(String key:ChartsForFTSP_PM.keySet())
			{
				Element e=(Element)FTSPNode.selectSingleNode(String.format(".//Chart[@name=\'%s\']", key));
				if(e != null)
					e.addAttribute("url", ConstString.CHART_URL + ChartsForFTSP_PM.get(key));
			}
		}
		
		if(ChartSetForFTSP_PM!=null)
		{
			Element e=(Element)FTSPNode.selectSingleNode(String.format(".//Chart[@name=\'%s\']", ConstString.FTSP_PM_CHARTSET_NAME));
			if(e != null)
				e.addAttribute("url", ConstString.CHART_SET_URL + ChartSetForFTSP_PM);
		}
		updateXmlTime(FTSPNode);
		
		XmlParseHelper.persist(document,"config.xml");
	}
	
	private static void updateXmlTime(Node document)
	{
		List<Node> timeNodes =document.selectNodes(".//@time");
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for(Node n : timeNodes)
		{
			Attribute timeAttri=(Attribute)n;
			timeAttri.setValue(df.format(new Date()));
		}	
	}
}
