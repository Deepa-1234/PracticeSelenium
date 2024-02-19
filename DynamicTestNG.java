package com.RunConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlGroups;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.ParallelMode;
import org.testng.xml.XmlTest;

import com.daimler.cockpitframework.constant.GlobalVariables;
import com.daimler.cockpitframework.core.TestExecutor;
import com.daimler.cockpitframework.utilities.RetryFailedTest;
import com.daimler.cockpitframework.utilities.TestAnnotationTransformerListener;

/**
 * This for create testNG.xml at run time, developed to make the "GroupTest" as
 * run configuration Run configuration parameters passed from this class
 * 
 * @author PRIYANV
 * 
 */
public class DynamicTestNG {
	static Logger LOGGER = LogManager.getLogger(DynamicTestNG.class);


	@SuppressWarnings("deprecation")
	public void runTestNGTest(Map<String, String> testngParams) throws IOException {

		// Create an instance on TestNG
		TestNG myTestNG = new TestNG();

		// Create an instance of XML Suite and assign a name for it.
		XmlSuite mySuite = new XmlSuite();
		mySuite.setName("Suite");
		//mySuite.setParallel(XmlSuite.ParallelMode.METHODS);
      	//mySuite.setThreadCount(3);
		
		
        // Create an instance of XmlTest and assign a name for it.
		XmlTest myTest = new XmlTest(mySuite);
		myTest.setName("Pramod");
		//mySuite.setParallel(ParallelMode.METHODS);
		//mySuite.setThreadCount(2);
		
		XmlGroups myGroups = new XmlGroups();

		// Add any parameters that you want to set to the Test.
		myTest.setParameters(testngParams);

		// To Run the Test scripts package

//		 String packages = "com.daimler.cockpitframework.testscripts"; 
//		 XmlPackage xmlPackage = new XmlPackage(packages);
//		 myTest.setXmlPackages(Collections.singletonList(xmlPackage));

		// Create a list which can contain the classes that you want to run.
		List<XmlClass> myClasses = new ArrayList<XmlClass>();
		myClasses.add(new XmlClass("com.daimler.cockpitframework.testscripts.DashboardTestScripts"));
		myClasses.add(new XmlClass("com.daimler.cockpitframework.testscripts.AdminTestScripts"));
		myClasses.add(new XmlClass("com.daimler.cockpitframework.testscripts.Carline3DViewerTestScripts"));
		myClasses.add(new XmlClass("com.daimler.cockpitframework.testscripts.DashboardMainMenuTestScripts"));
		myClasses.add(new XmlClass("com.daimler.cockpitframework.testscripts.DashboardPerspectivesTestScripts"));
        myClasses.add(new XmlClass("com.daimler.cockpitframework.testscripts.DashboardPerspectivesTestScripts2"));
        myClasses.add(new XmlClass("com.daimler.cockpitframework.testscripts.DashboardTestScripts2"));
        
//
//		// Assign that to the XmlTest Object created earlier.
		myTest.setXmlClasses(myClasses);

		// Create a list of XmlTests and add the Xmltest you created earlier to it.
		List<XmlTest> myTests = new ArrayList<XmlTest>();
		myTests.add(myTest);

		// add the list of tests to your Suite.
		mySuite.setTests(myTests);

		// Add the suite to the list of suites.
		List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
		mySuites.add(mySuite);
		if (testngParams.get("suiteName").equalsIgnoreCase("SmokeTest")) {
			mySuite.addIncludedGroup("SmokeTest");
		} else if (testngParams.get("suiteName").equalsIgnoreCase("RegressionTest")) {
			mySuite.addIncludedGroup("RegressionTest");

		} else {
			mySuite.addIncludedGroup("SmokeTest");
			mySuite.addIncludedGroup("RegressionTest");
		}
		// Set the list of Suites to the testNG object you created earlier.
		myTestNG.setXmlSuites(mySuites);
		//mySuite.setThreadCount(3);
		TestListenerAdapter tla = new TestListenerAdapter();

		myTestNG.addListener(tla);
		
		//Following method is not supported in latest TestNG 7.7.0 version
		//myTestNG.setAnnotationTransformer(new TestAnnotationTransformerListener());
		myTestNG.addListener(new TestAnnotationTransformerListener());

		myTestNG.run();
	}

	public static void main(String args[]) throws IOException {
		//BasicConfigurator.configure();
		LOGGER.info("Read Run Configuration parameters");
		
//		String one = args[0];  
//		String two = args[1];  
//		System.out.println(one);
//		System.out.println(two);

		// create Options object
		Options options = new Options();

		// add t option
		options.addOption("tp", true, "Test plan to use");
		options.addOption("env", true, "Environment to use");
		options.addOption("br", true, "Carline to use");
		options.addOption("var", true, "Variant to use");
		options.addOption("jid", true, "Job ID to use");
		options.addOption("exby", true, "Executing by user to use");
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);
			
			if(cmd.hasOption("tp")) {
			    // print the date and time
				GlobalVariables.TEST_PLAN_KEY= cmd.getOptionValue("tp");
			}
			if(cmd.hasOption("env")) {
			    // print the date and time
				GlobalVariables.ENV_FROM_UI = cmd.getOptionValue("env");

			}
			if(cmd.hasOption("br")) {
			    // print the date and time
				GlobalVariables.CARLINES = cmd.getOptionValue("br").split("~");

			}
			if(cmd.hasOption("var")) {
			    // print the date and time
				//GlobalVariables.VARIANTS = cmd.getOptionValue("var").split("~");
				GlobalVariables.VARIANTS = cmd.getOptionValue("var");
				

			}
			if(cmd.hasOption("jid")) {
			    // print the date and time
				GlobalVariables.TEST_JOB_ID = Integer.parseInt(cmd.getOptionValue("jid")) ;
			}
			if(cmd.hasOption("exby")) {
			    // print the date and time
				GlobalVariables.EXECUTING_BY = cmd.getOptionValue("exby") ;
			}

		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		
		/*if (args.length != 0) {
			for(int i=0; i<args.length; i++) {
				if(i == 0) {
					GlobalVariables.TEST_PLAN_KEY = args[0];
				}
				if(i == 1) {
					GlobalVariables.CARLINES =  args[1].split("~");
				}
				if(i == 2) {
					//GlobalVariables.VARIANTS = args[2].split("~");
					GlobalVariables.VARIANTS = args[2];
				}
				if(i == 3) {
					GlobalVariables.ENV_FROM_UI = args[3];
				}
				if(i == 4) {
					GlobalVariables.TEST_JOB_ID = Integer.parseInt(args[4]) ;
				}
			}
			System.out.println(GlobalVariables.TEST_PLAN_KEY);
			System.out.println(GlobalVariables.CARLINES);
			System.out.println(GlobalVariables.VARIANTS);
		}*/
		DynamicTestNG dt = new DynamicTestNG();
		// This Map can hold your testNg Parameters.
		Map<String, String> testngParams = new HashMap<String, String>();
		String browser = System.getenv("Browser");
		System.out.println("browser:" + browser);
		String environment = System.getenv("Environment");
		System.out.println("environment:" + environment);
		String executingIn = System.getenv("ExecutingIn");
		System.out.println("executingIn:" + executingIn);
		String language = System.getenv("Language");
		System.out.println("language:" + language);
		String emailTo = System.getenv("emailTo");
		System.out.println("emailTo:" + emailTo);
		String screenshot = System.getenv("screenshot");
		System.out.println("screenshot:" + screenshot);
		String suiteName = System.getenv("SuiteName");
		System.out.println("suiteName:" + suiteName);
		String emailFrom = System.getenv("emailFrom");
		System.out.println("emailFrom:" + emailFrom);
		String confPath = System.getenv("confPath");
		System.out.println("confPath:" + confPath);
		String token = System.getenv("BearerToken");
		System.out.println("token:" + token);
		String logPath = System.getenv("LOG_PATH");
		System.out.println("logPath:" + logPath);
		//String propertyFilePath = System.getenv("filePath");
		testngParams.put("environment", environment);
		testngParams.put("executingIn", executingIn);
		testngParams.put("emailFrom", emailFrom);
		testngParams.put("emailTo", emailTo);
		testngParams.put("browser", browser);
		testngParams.put("language", language);
		testngParams.put("screenshot", screenshot);
		testngParams.put("suiteName", suiteName);
		testngParams.put("confPath", confPath);
		LOGGER.info("Run Configuration parameters added To dyamicTestNG");
		dt.runTestNGTest(testngParams);
		System.out.println(GlobalVariables.TEST_EXECUTION_KEY +"~"+ GlobalVariables.PASS_FAIL);
		
		//TestExecutor executor = new TestExecutor();
		//executor.runTestNGTest(testngParams);

	}

}
