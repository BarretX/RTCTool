package ConstVar;

import org.dom4j.Document;
import org.dom4j.Element;

import Helper.XmlParseHelper;

public class ConstString {
	//Get const string from API.config
	static
	{
		System.out.println("读取配置文件开始");
		Document document=XmlParseHelper.load("APP.Config");

		Element CHART_URL_element=(Element)document.selectSingleNode("/configuration/urls/url[@name ='CHART_URL']");
		Element CHART_SET_URL_element=(Element)document.selectSingleNode("/configuration/urls/url[@name ='CHART_SET_URL']");
		Element CHART_API_URL_element=(Element)document.selectSingleNode("/configuration/urls/url[@name ='CHART_API_URL']");
		Element CHART_SET_API_URL_element=(Element)document.selectSingleNode("/configuration/urls/url[@name ='CHART_SET_API_URL']");
		Element REPOSITORY_URI_element=(Element)document.selectSingleNode("/configuration/urls/url[@name ='REPOSITORY_URI']");
		Element USER_ID_element=(Element)document.selectSingleNode("/configuration/loginInfo/Info[@name ='USER_ID']");
		Element PASSWORD_element=(Element)document.selectSingleNode("/configuration/loginInfo/Info[@name ='PASSWORD']");
		
		if(CHART_URL_element == null
				||CHART_SET_URL_element == null
				||CHART_API_URL_element == null
				||CHART_SET_API_URL_element == null
				||REPOSITORY_URI_element == null
				||USER_ID_element == null
				||PASSWORD_element == null)
		{
			System.out.println("读取配置文件错误");
			System.exit(0);
		}
			
		CHART_URL=CHART_URL_element.attributeValue("value");
		CHART_SET_URL=CHART_SET_URL_element.attributeValue("value");
		CHART_API_URL=CHART_API_URL_element.attributeValue("value");
		CHART_SET_API_URL=CHART_SET_API_URL_element.attributeValue("value");
		REPOSITORY_URI=REPOSITORY_URI_element.attributeValue("value");
		USER_ID=USER_ID_element.attributeValue("value");
		PASSWORD=PASSWORD_element.attributeValue("value");		
	}
	
	// URL for EagleEye
	public static String CHART_URL;
	public static String CHART_SET_URL;
	public static String CHART_API_URL;
	public static String CHART_SET_API_URL;
	
	//Info for Login
	public static String REPOSITORY_URI;
	public static String USER_ID;
	public static String PASSWORD;
	
	//Product and Chart Name
	public final static String S5KA_NAME="S5KA";
	public final static String S5KA_PM_CHARTSET_NAME="S5KA_For_PM";
	
	public final static String S5KA_QA_CHARTSET_NAME="S5KA Quality Metrics";
	public final static String S5KA_QA_CHART_FY17_Anomlay_Backlog_Weekly= "FY17 Anomaly Backlog_3.0 Weekly" ;
	public final static String S5KA_QA_CHART_FY17_Anomlay_Backlog_Monthly= "FY17 Anomaly Backlog_3.0 Monthly" ;
	public final static String S5KA_QA_CHART_FY17_Anomlay_Backlog_Sprint= "FY17 Anomaly Backlog_3.0 Sprint" ;
	public final static String S5KA_QA_CHART_Shippable_RC_anomaly_Weekly= "Shippable State RC Anomaly Weekly" ;
	public final static String S5KA_QA_CHART_Shippable_RC_anomaly_monthly= "Shippable State RC anomaly monthly";
	public final static String S5KA_QA_CHART_Shippable_RC_anomaly_Sprint= "Shippable State RC anomaly Sprint" ;
	public final static String S5KA_QA_CHART_Runrate_all_anomaly = "Runrate (all anomaly)";
	public final static String S5KA_QA_CHART_Runrate_RC = "Runrate (RC)" ;
	public final static String S5KA_QA_CHART_Stability_all_anomaly = "Stability (all anomaly)" ;
	public final static String S5KA_QA_CHART_Stability_RC = "Stability (RC)";
	public final static String S5KA_PM_CHART_Weekly_Trend="Weekly Trend";
	public final static String S5KA_PM_CHART_Trend_Epic="Trend by Epic";
	public final static String S5KA_PM_CHART_Trend_Team="Trend by Team";
	public final static String S5KA_PM_CHART_ThroughputVelocity_sprint="Throughput-Velocity by sprint";
	public final static String S5KA_PM_CHART_Plan_Actual_Sprint="Plan vs Actual by Sprint";
	public final static String S5KA_PM_CHART_Feature_Progress="Feature Progress";
	public final static String S5KA_PM_CHART_Resource_Sprint="Resource by Sprint";


	public final static String CCW_NAME="CCW";
	public final static String CCW_PM_CHARTSET_NAME="CCW Project Metrics";
	public final static String CCW_QA_CHARTSET_NAME="CCW_For_QA";
	public final static String CCW_PM_CHART_Weekly_Trend="CCW Release Burnup";
	public final static String CCW_PM_CHART_Weekly_BurnDown="R12-PI2 Burn Down";
	public final static String CCW_PM_CHART_Trend_Epic="Trend by Epic";
	public final static String CCW_PM_CHART_Trend_Team="Trend by Team";
	public final static String CCW_PM_CHART_ThroughputVelocity_sprint="Throughput-Velocity by sprint";
	public final static String CCW_PM_CHART_Plan_Actual_Sprint="Plan vs Actual by Sprint";
	public final static String CCW_PM_CHART_Feature_Progress="Top 10 Epics";  //all epic
	
	public final static String FTVIEWSE_NAME="FTViewSE";
	public final static String FTVIEWSE_PM_CHARTSET_NAME="FTView Project Metrics";
	public final static String FTVIEWSE_QA_CHARTSET_NAME="FTViewSE_For_QA";
	public final static String FTVIEWSE_PM_CHART_Weekly_Trend="FTView Release Burnup";
	public final static String FTVIEWSE_PM_CHART_Weekly_BurnDown="PI13 Burn Down";
	public final static String FTVIEWSE_PM_CHART_Trend_Epic="Trend by Epic";
	public final static String FTVIEWSE_PM_CHART_Trend_Team="Trend by Team";
	public final static String FTVIEWSE_PM_CHART_ThroughputVelocity_sprint="Throughput-Velocity by sprint";
	public final static String FTVIEWSE_PM_CHART_Plan_Actual_Sprint="Plan vs Actual by Sprint";
	public final static String FTVIEWSE_PM_CHART_Feature_Progress="Top 10 Epics";  //all epic	
//	public final static String FTVIEWSE_PM_CHART_Resource_Sprint="Resource by Sprint";
	
	public final static String FTSP_NAME="FTSP";
	public final static String FTSP_PM_CHARTSET_NAME="FTSP Project Metrics";
	public final static String FTSP_QA_CHARTSET_NAME="FTSP_For_QA";
	public final static String FTSP_PM_CHART_Weekly_Trend="FTSP Release Burnup";
	public final static String FTSP_PM_CHART_Weekly_BurnDown="PI8 Burn Down";
	public final static String FTSP_PM_CHART_Trend_Epic="Trend by Epic";
	public final static String FTSP_PM_CHART_Trend_Team="Trend by Team";
	public final static String FTSP_PM_CHART_ThroughputVelocity_sprint="Throughput-Velocity by sprint";
	public final static String FTSP_PM_CHART_Plan_Actual_Sprint="Plan vs Actual by Sprint";
	public final static String FTSP_PM_CHART_Feature_Progress="Top 10 Epics";  //all epic
	
	public final static String Communications_NAME="Communications";
	public final static String Communications_PM_CHARTSET_NAME="Communications Project Metrics";
	public final static String Communications_QA_CHARTSET_NAME="Communications_For_QA";
	public final static String Communications_PM_CHART_Weekly_Trend="Communications Release Burnup";
	public final static String Communications_PM_CHART_Weekly_BurnDown="PI8 Burn Down";
	public final static String Communications_PM_CHART_Trend_Epic="Trend by Epic";
	public final static String Communications_PM_CHART_Trend_Team="Trend by Team";
	public final static String Communications_PM_CHART_ThroughputVelocity_sprint="Throughput-Velocity by sprint";
	public final static String Communications_PM_CHART_Plan_Actual_Sprint="Plan vs Actual by Sprint";
	public final static String Communications_PM_CHART_Feature_Progress="Top 10 Epics";  //all epic
	
	public final static String FTAC_NAME="FTAC";
	public final static String FTAC_PM_CHARTSET_NAME="FTAC Project Metrics";
	public final static String FTAC_QA_CHARTSET_NAME="FTAC_For_QA";
	public final static String FTAC_PM_CHART_Weekly_Trend="FTAC Release Burnup";
	public final static String FTAC_PM_CHART_Weekly_BurnDown="Sprint 26 Burn Down";
	public final static String FTAC_PM_CHART_Trend_Epic="Trend by Epic";
	public final static String FTAC_PM_CHART_Trend_Team="Trend by Team";
	public final static String FTAC_PM_CHART_ThroughputVelocity_sprint="Throughput-Velocity by sprint";
	public final static String FTAC_PM_CHART_Plan_Actual_Sprint="Plan vs Actual by Sprint";
	public final static String FTAC_PM_CHART_Feature_Progress="Top 10 Epics";  //all epic

}
