/**
 * ConfluenceserviceV2SoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eu.wajja.input.fetcher.soap.confluence;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

import com.atlassian.confluence.rpc.AlreadyExistsException;
import com.atlassian.confluence.rpc.AuthenticationFailedException;
import com.atlassian.confluence.rpc.InvalidSessionException;
import com.atlassian.confluence.rpc.NotPermittedException;
import com.atlassian.confluence.rpc.RemoteException;
import com.atlassian.confluence.rpc.VersionMismatchException;
import com.atlassian.confluence.rpc.soap.beans.AbstractRemotePageSummary;
import com.atlassian.confluence.rpc.soap.beans.RemoteAttachment;
import com.atlassian.confluence.rpc.soap.beans.RemoteBlogEntry;
import com.atlassian.confluence.rpc.soap.beans.RemoteBlogEntrySummary;
import com.atlassian.confluence.rpc.soap.beans.RemoteClusterInformation;
import com.atlassian.confluence.rpc.soap.beans.RemoteComment;
import com.atlassian.confluence.rpc.soap.beans.RemoteConfluenceUser;
import com.atlassian.confluence.rpc.soap.beans.RemoteContentPermission;
import com.atlassian.confluence.rpc.soap.beans.RemoteContentPermissionSet;
import com.atlassian.confluence.rpc.soap.beans.RemoteContentSummaries;
import com.atlassian.confluence.rpc.soap.beans.RemoteContentSummary;
import com.atlassian.confluence.rpc.soap.beans.RemoteLabel;
import com.atlassian.confluence.rpc.soap.beans.RemoteNodeStatus;
import com.atlassian.confluence.rpc.soap.beans.RemotePage;
import com.atlassian.confluence.rpc.soap.beans.RemotePageHistory;
import com.atlassian.confluence.rpc.soap.beans.RemotePageSummary;
import com.atlassian.confluence.rpc.soap.beans.RemotePageUpdateOptions;
import com.atlassian.confluence.rpc.soap.beans.RemotePermission;
import com.atlassian.confluence.rpc.soap.beans.RemoteSearchResult;
import com.atlassian.confluence.rpc.soap.beans.RemoteServerInfo;
import com.atlassian.confluence.rpc.soap.beans.RemoteSpace;
import com.atlassian.confluence.rpc.soap.beans.RemoteSpaceGroup;
import com.atlassian.confluence.rpc.soap.beans.RemoteSpacePermissionSet;
import com.atlassian.confluence.rpc.soap.beans.RemoteSpaceSummary;
import com.atlassian.confluence.rpc.soap.beans.RemoteUser;
import com.atlassian.confluence.rpc.soap.beans.RemoteUserInformation;

/**
 * ConfluenceserviceV2SoapBindingStub.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.4 Apr 22, 2006
 * (06:55:48 PDT) WSDL2Java emitter.
 */

public class ConfluenceserviceV2SoapBindingStub extends Stub implements ConfluenceSoapService {
	private java.util.Vector cachedSerClasses = new java.util.Vector();
	private java.util.Vector cachedSerQNames = new java.util.Vector();
	private java.util.Vector cachedSerFactories = new java.util.Vector();
	private java.util.Vector cachedDeserFactories = new java.util.Vector();
	public static String baseUrl;

	static org.apache.axis.description.OperationDesc[] _operations;

	static {
		_operations = new org.apache.axis.description.OperationDesc[165];
		_initOperationDesc1();
		_initOperationDesc2();
		_initOperationDesc3();
		_initOperationDesc4();
		_initOperationDesc5();
		_initOperationDesc6();
		_initOperationDesc7();
		_initOperationDesc8();
		_initOperationDesc9();
		_initOperationDesc10();
		_initOperationDesc11();
		_initOperationDesc12();
		_initOperationDesc13();
		_initOperationDesc14();
		_initOperationDesc15();
		_initOperationDesc16();
		_initOperationDesc17();
	}

	private static void _initOperationDesc1() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("updatePage");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePage"),
				RemotePage.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePageUpdateOptions"),
				RemotePageUpdateOptions.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePage"));
		oper.setReturnClass(RemotePage.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "updatePageReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"VersionMismatchException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "VersionMismatchException"),
				true));
		_operations[0] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("isPluginInstalled");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "isPluginInstalledReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[1] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getActiveUsers");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_xsd_string"));
		oper.setReturnClass(java.lang.String[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getActiveUsersReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[2] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getComment");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteComment"));
		oper.setReturnClass(RemoteComment.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getCommentReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[3] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getPermissions");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_xsd_string"));
		oper.setReturnClass(java.lang.String[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getPermissionsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[4] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpace"));
		oper.setReturnClass(RemoteSpace.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[5] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("search");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSearchResult"));
		oper.setReturnClass(RemoteSearchResult[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "searchReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[6] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("search");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"), java.util.HashMap.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSearchResult"));
		oper.setReturnClass(RemoteSearchResult[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "searchReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[7] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removePage");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removePageReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[8] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getServerInfo");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteServerInfo"));
		oper.setReturnClass(RemoteServerInfo.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getServerInfoReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[9] = oper;

	}

	private static void _initOperationDesc2() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getGroups");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_xsd_string"));
		oper.setReturnClass(java.lang.String[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getGroupsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[10] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getAttachment");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteAttachment"));
		oper.setReturnClass(RemoteAttachment.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getAttachmentReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[11] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("login");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "loginReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"AuthenticationFailedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "AuthenticationFailedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[12] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("logout");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "logoutReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[13] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getChildren");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemotePageSummary"));
		oper.setReturnClass(RemotePageSummary[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getChildrenReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[14] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeGroup");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeGroupReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[15] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeUser");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeUserReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[16] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addGroup");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addGroupReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[17] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getPage");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePage"));
		oper.setReturnClass(RemotePage.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getPageReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[18] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getPage");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePage"));
		oper.setReturnClass(RemotePage.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getPageReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[19] = oper;

	}

	private static void _initOperationDesc3() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getUser");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteUser"));
		oper.setReturnClass(RemoteUser.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getUserReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[20] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getUserByName");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteConfluenceUser"));
		oper.setReturnClass(RemoteConfluenceUser.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getUserByNameReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[21] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("isPluginEnabled");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "isPluginEnabledReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[22] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getClusterInformation");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteClusterInformation"));
		oper.setReturnClass(RemoteClusterInformation.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getClusterInformationReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[23] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addComment");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteComment"),
				RemoteComment.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteComment"));
		oper.setReturnClass(RemoteComment.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addCommentReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[24] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeSpaceGroup");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeSpaceGroupReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[25] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getSpaceGroups");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSpaceGroup"));
		oper.setReturnClass(RemoteSpaceGroup[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getSpaceGroupsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[26] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getRelatedLabelsInSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteLabel"));
		oper.setReturnClass(RemoteLabel[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getRelatedLabelsInSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[27] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getSpacesContainingContentWithLabel");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSpace"));
		oper.setReturnClass(RemoteSpace[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getSpacesContainingContentWithLabelReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[28] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getSpacesWithLabel");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSpace"));
		oper.setReturnClass(RemoteSpace[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getSpacesWithLabelReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[29] = oper;

	}

	private static void _initOperationDesc4() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getLabelsByDetail");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteLabel"));
		oper.setReturnClass(RemoteLabel[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getLabelsByDetailReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[30] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getMostPopularLabels");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteLabel"));
		oper.setReturnClass(RemoteLabel[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getMostPopularLabelsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[31] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getMostPopularLabelsInSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteLabel"));
		oper.setReturnClass(RemoteLabel[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getMostPopularLabelsInSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[32] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getRecentlyUsedLabels");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteLabel"));
		oper.setReturnClass(RemoteLabel[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getRecentlyUsedLabelsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[33] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getRecentlyUsedLabelsInSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteLabel"));
		oper.setReturnClass(RemoteLabel[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getRecentlyUsedLabelsInSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[34] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("renderContent");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"), java.util.HashMap.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "renderContentReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[35] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("renderContent");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "renderContentReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[36] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("movePageToTopLevel");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "movePageToTopLevelReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[37] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("installPlugin");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "installPluginReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[38] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getAttachments");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteAttachment"));
		oper.setReturnClass(RemoteAttachment[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getAttachmentsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[39] = oper;

	}

	private static void _initOperationDesc5() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addAttachment");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteAttachment"),
				RemoteAttachment.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteAttachment"));
		oper.setReturnClass(RemoteAttachment.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addAttachmentReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[40] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addAttachment");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteAttachment"),
				RemoteAttachment.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteAttachment"));
		oper.setReturnClass(RemoteAttachment.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addAttachmentReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[41] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeAttachment");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeAttachmentReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[42] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getContentPermissionSet");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteContentPermissionSet"));
		oper.setReturnClass(RemoteContentPermissionSet.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getContentPermissionSetReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[43] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getContentPermissionSets");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteContentPermissionSet"));
		oper.setReturnClass(RemoteContentPermissionSet[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getContentPermissionSetsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[44] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getComments");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteComment"));
		oper.setReturnClass(RemoteComment[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getCommentsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[45] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeComment");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeCommentReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[46] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addUser");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteUser"),
				RemoteUser.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[47] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addUser");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteUser"),
				RemoteUser.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[48] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getSpaces");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSpaceSummary"));
		oper.setReturnClass(RemoteSpaceSummary[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getSpacesReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[49] = oper;

	}

	private static void _initOperationDesc6() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getDescendents");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemotePageSummary"));
		oper.setReturnClass(RemotePageSummary[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getDescendentsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[50] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getAncestors");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemotePageSummary"));
		oper.setReturnClass(RemotePageSummary[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getAncestorsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[51] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getSpaceStatus");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getSpaceStatusReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[52] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getPages");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemotePageSummary"));
		oper.setReturnClass(RemotePageSummary[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getPagesReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[53] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getPagePermissions");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemotePermission"));
		oper.setReturnClass(RemotePermission[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getPagePermissionsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[54] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getUserByKey");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteConfluenceUser"));
		oper.setReturnClass(RemoteConfluenceUser.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getUserByKeyReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[55] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("setContentPermissions");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteContentPermission"), RemoteContentPermission[].class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "setContentPermissionsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[56] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("editComment");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteComment"),
				RemoteComment.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteComment"));
		oper.setReturnClass(RemoteComment.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "editCommentReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[57] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("stopActivity");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "stopActivityReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[58] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("moveAttachment");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "moveAttachmentReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[59] = oper;

	}

	private static void _initOperationDesc7() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getTopLevelPages");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemotePageSummary"));
		oper.setReturnClass(RemotePageSummary[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getTopLevelPagesReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[60] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("startActivity");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "startActivityReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[61] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getAttachmentData");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
		oper.setReturnClass(byte[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getAttachmentDataReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[62] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("isWatchingSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "isWatchingSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[63] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("isWatchingPage");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "isWatchingPageReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[64] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getTrashContents");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteContentSummaries"));
		oper.setReturnClass(RemoteContentSummaries.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getTrashContentsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[65] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("emptyTrash");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "emptyTrashReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[66] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("renameUser");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "renameUserReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[67] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("deactivateUser");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "deactivateUserReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[68] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("reactivateUser");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "reactivateUserReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[69] = oper;

	}

	private static void _initOperationDesc8() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[70] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addUserToGroup");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addUserToGroupReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[71] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getRelatedLabels");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteLabel"));
		oper.setReturnClass(RemoteLabel[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getRelatedLabelsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[72] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getSpaceGroup");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpaceGroup"));
		oper.setReturnClass(RemoteSpaceGroup.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getSpaceGroupReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"AlreadyExistsException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "AlreadyExistsException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[73] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("setSpaceStatus");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "setSpaceStatusReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[74] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeAllPermissionsForGroup");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeAllPermissionsForGroupReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[75] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("hasGroup");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "hasGroupReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[76] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("exportSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "exportSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[77] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("exportSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "exportSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[78] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeUserFromGroup");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeUserFromGroupReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[79] = oper;

	}

	private static void _initOperationDesc9() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("watchSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "watchSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[80] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("setEnableWysiwyg");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "setEnableWysiwygReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[81] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getUserGroups");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_xsd_string"));
		oper.setReturnClass(java.lang.String[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getUserGroupsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[82] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpace"),
				RemoteSpace.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpace"));
		oper.setReturnClass(RemoteSpace.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"AlreadyExistsException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "AlreadyExistsException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[83] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("hasUser");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "hasUserReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[84] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("isDarkFeatureEnabled");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "isDarkFeatureEnabledReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[85] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("watchPage");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "watchPageReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[86] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getPageHistory");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemotePageHistory"));
		oper.setReturnClass(RemotePageHistory[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getPageHistoryReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[87] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("movePage");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "movePageReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[88] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("storePage");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePage"),
				RemotePage.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePage"));
		oper.setReturnClass(RemotePage.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "storePageReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"VersionMismatchException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "VersionMismatchException"),
				true));
		_operations[89] = oper;

	}

	private static void _initOperationDesc10() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("purgeFromTrash");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "purgeFromTrashReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[90] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getPageSummary");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePageSummary"));
		oper.setReturnClass(RemotePageSummary.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getPageSummaryReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[91] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getPageSummary");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePageSummary"));
		oper.setReturnClass(RemotePageSummary.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getPageSummaryReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[92] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getSpacesInGroup");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSpaceSummary"));
		oper.setReturnClass(RemoteSpaceSummary[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getSpacesInGroupReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[93] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getSpacePermissionSets");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSpacePermissionSet"));
		oper.setReturnClass(RemoteSpacePermissionSet[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getSpacePermissionSetsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[94] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getSpacePermissionSet");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpacePermissionSet"));
		oper.setReturnClass(RemoteSpacePermissionSet.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getSpacePermissionSetReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[95] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addSpaceWithDefaultPermissions");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpace"),
				RemoteSpace.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpace"));
		oper.setReturnClass(RemoteSpace.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addSpaceWithDefaultPermissionsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"AlreadyExistsException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "AlreadyExistsException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[96] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("storeSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpace"),
				RemoteSpace.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpace"));
		oper.setReturnClass(RemoteSpace.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "storeSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[97] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addSpaceGroup");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpaceGroup"),
				RemoteSpaceGroup.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpaceGroup"));
		oper.setReturnClass(RemoteSpaceGroup.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addSpaceGroupReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"AlreadyExistsException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "AlreadyExistsException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[98] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addPersonalSpaceWithDefaultPermissions");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpace"),
				RemoteSpace.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpace"));
		oper.setReturnClass(RemoteSpace.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addPersonalSpaceWithDefaultPermissionsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"AlreadyExistsException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "AlreadyExistsException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[99] = oper;

	}

	private static void _initOperationDesc11() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addPersonalSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpace"),
				RemoteSpace.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpace"));
		oper.setReturnClass(RemoteSpace.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addPersonalSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"AlreadyExistsException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "AlreadyExistsException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[100] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getSpaceLevelPermissions");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_xsd_string"));
		oper.setReturnClass(java.lang.String[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getSpaceLevelPermissionsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[101] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addPermissionToSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addPermissionToSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[102] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addGlobalPermissions");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_xsd_string"),
				java.lang.String[].class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addGlobalPermissionsReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[103] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addGlobalPermission");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addGlobalPermissionReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[104] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addAnonymousUsePermission");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addAnonymousUsePermissionReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[105] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addAnonymousViewUserProfilePermission");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addAnonymousViewUserProfilePermissionReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[106] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeAnonymousViewUserProfilePermission");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeAnonymousViewUserProfilePermissionReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[107] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeGlobalPermission");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeGlobalPermissionReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[108] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addPermissionsToSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_xsd_string"),
				java.lang.String[].class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addPermissionsToSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[109] = oper;

	}

	private static void _initOperationDesc12() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removePermissionFromSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removePermissionFromSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[110] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("editUser");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteUser"),
				RemoteUser.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "editUserReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[111] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("isActiveUser");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "isActiveUserReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[112] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("changeMyPassword");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "changeMyPasswordReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[113] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("watchPageForUser");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "watchPageForUserReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[114] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removePageWatch");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removePageWatchReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[115] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeSpaceWatch");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeSpaceWatchReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[116] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removePageWatchForUser");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removePageWatchForUserReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[117] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getWatchersForPage");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteUser"));
		oper.setReturnClass(RemoteUser[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getWatchersForPageReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[118] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("isWatchingSpaceForType");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "isWatchingSpaceForTypeReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[119] = oper;

	}

	private static void _initOperationDesc13() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getWatchersForSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteUser"));
		oper.setReturnClass(RemoteUser[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getWatchersForSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[120] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("changeUserPassword");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "changeUserPasswordReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[121] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("setUserInformation");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteUserInformation"),
				RemoteUserInformation.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "setUserInformationReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[122] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getUserInformation");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteUserInformation"));
		oper.setReturnClass(RemoteUserInformation.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getUserInformationReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[123] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("setUserPreferenceBoolean");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "setUserPreferenceBooleanReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[124] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getUserPreferenceBoolean");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getUserPreferenceBooleanReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[125] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("setUserPreferenceLong");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "setUserPreferenceLongReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[126] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getUserPreferenceLong");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		oper.setReturnClass(long.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getUserPreferenceLongReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[127] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("setUserPreferenceString");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "setUserPreferenceStringReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[128] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getUserPreferenceString");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getUserPreferenceStringReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[129] = oper;

	}

	private static void _initOperationDesc14() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addProfilePicture");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addProfilePictureReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[130] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("renameUsers");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"), java.util.HashMap.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_xsd_string"));
		oper.setReturnClass(java.lang.String[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "renameUsersReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[131] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getBlogEntryByDayAndTitle");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteBlogEntry"));
		oper.setReturnClass(RemoteBlogEntry.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getBlogEntryByDayAndTitleReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[132] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getBlogEntryByDateAndTitle");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteBlogEntry"));
		oper.setReturnClass(RemoteBlogEntry.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getBlogEntryByDateAndTitleReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[133] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getBlogEntry");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteBlogEntry"));
		oper.setReturnClass(RemoteBlogEntry.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getBlogEntryReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[134] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getBlogEntries");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteBlogEntrySummary"));
		oper.setReturnClass(RemoteBlogEntrySummary[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getBlogEntriesReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[135] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("exportSite");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "exportSiteReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[136] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("performBackup");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "performBackupReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[137] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("flushIndexQueue");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "flushIndexQueueReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[138] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("clearIndexQueue");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "clearIndexQueueReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[139] = oper;

	}

	private static void _initOperationDesc15() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("importSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "importSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[140] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("setEnableAnonymousAccess");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "setEnableAnonymousAccessReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[141] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getLabelsById");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteLabel"));
		oper.setReturnClass(RemoteLabel[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getLabelsByIdReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[142] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getLabelContentById");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSearchResult"));
		oper.setReturnClass(RemoteSearchResult[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getLabelContentByIdReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[143] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getLabelContentByName");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSearchResult"));
		oper.setReturnClass(RemoteSearchResult[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getLabelContentByNameReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[144] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getLabelContentByObject");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteLabel"),
				RemoteLabel.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSearchResult"));
		oper.setReturnClass(RemoteSearchResult[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getLabelContentByObjectReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[145] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addLabelByName");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addLabelByNameReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[146] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("storeBlogEntry");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteBlogEntry"),
				RemoteBlogEntry.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteBlogEntry"));
		oper.setReturnClass(RemoteBlogEntry.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "storeBlogEntryReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"VersionMismatchException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "VersionMismatchException"),
				true));
		_operations[147] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getClusterNodeStatuses");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteNodeStatus"));
		oper.setReturnClass(RemoteNodeStatus[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getClusterNodeStatusesReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[148] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getPermissionsForUser");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_xsd_string"));
		oper.setReturnClass(java.lang.String[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "getPermissionsForUserReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[149] = oper;

	}

	private static void _initOperationDesc16() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeAnonymousUsePermission");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeAnonymousUsePermissionReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[150] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addAnonymousPermissionToSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addAnonymousPermissionToSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[151] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addAnonymousPermissionsToSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_xsd_string"),
				java.lang.String[].class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addAnonymousPermissionsToSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[152] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeAnonymousPermissionFromSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeAnonymousPermissionFromSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[153] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("convertWikiToStorageFormat");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(java.lang.String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "convertWikiToStorageFormatReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[154] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removePageVersionById");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removePageVersionByIdReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[155] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removePageVersionByVersion");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removePageVersionByVersionReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[156] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("recoverMainIndex");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "recoverMainIndexReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[157] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addLabelById");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addLabelByIdReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[158] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addLabelByObject");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteLabel"),
				RemoteLabel.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addLabelByObjectReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[159] = oper;

	}

	private static void _initOperationDesc17() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("addLabelByNameToSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "addLabelByNameToSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[160] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeLabelByName");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeLabelByNameReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[161] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeLabelById");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeLabelByIdReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[162] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeLabelByObject");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteLabel"),
				RemoteLabel.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeLabelByObjectReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[163] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("removeLabelByNameFromSpace");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "removeLabelByNameFromSpaceReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"InvalidSessionException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"NotPermittedException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException"),
				true));
		oper.addFault(new org.apache.axis.description.FaultDesc(
				new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "fault"),
				"RemoteException",
				new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException"),
				true));
		_operations[164] = oper;

	}

	public ConfluenceserviceV2SoapBindingStub() throws org.apache.axis.AxisFault {
		this(null);
	}

	public ConfluenceserviceV2SoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service, String baseUrl) {

		this(service);
		this.baseUrl = baseUrl;
		super.cachedEndpoint = endpointURL;

	}

	public ConfluenceserviceV2SoapBindingStub(javax.xml.rpc.Service service) {

		if (service == null) {
			super.service = new Service();
		} else {
			super.service = service;
		}

		((Service) super.service).setTypeMappingVersion("1.2");
		java.lang.Class cls;
		javax.xml.namespace.QName qName;
		javax.xml.namespace.QName qName2;
		java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
		java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
		java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
		java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
		java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
		java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
		java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
		java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
		java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
		java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "AbstractRemotePageSummary");
		cachedSerQNames.add(qName);
		cls = AbstractRemotePageSummary.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteAttachment");
		cachedSerQNames.add(qName);
		cls = RemoteAttachment.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteBlogEntry");
		cachedSerQNames.add(qName);
		cls = RemoteBlogEntry.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteBlogEntrySummary");
		cachedSerQNames.add(qName);
		cls = RemoteBlogEntrySummary.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteClusterInformation");
		cachedSerQNames.add(qName);
		cls = RemoteClusterInformation.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteComment");
		cachedSerQNames.add(qName);
		cls = RemoteComment.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteConfluenceUser");
		cachedSerQNames.add(qName);
		cls = RemoteConfluenceUser.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteContentPermission");
		cachedSerQNames.add(qName);
		cls = RemoteContentPermission.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteContentPermissionSet");
		cachedSerQNames.add(qName);
		cls = RemoteContentPermissionSet.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteContentSummaries");
		cachedSerQNames.add(qName);
		cls = RemoteContentSummaries.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteContentSummary");
		cachedSerQNames.add(qName);
		cls = RemoteContentSummary.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteLabel");
		cachedSerQNames.add(qName);
		cls = RemoteLabel.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteNodeStatus");
		cachedSerQNames.add(qName);
		cls = RemoteNodeStatus.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePage");
		cachedSerQNames.add(qName);
		cls = RemotePage.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePageHistory");
		cachedSerQNames.add(qName);
		cls = RemotePageHistory.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePageSummary");
		cachedSerQNames.add(qName);
		cls = RemotePageSummary.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePageUpdateOptions");
		cachedSerQNames.add(qName);
		cls = RemotePageUpdateOptions.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePermission");
		cachedSerQNames.add(qName);
		cls = RemotePermission.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSearchResult");
		cachedSerQNames.add(qName);
		cls = RemoteSearchResult.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteServerInfo");
		cachedSerQNames.add(qName);
		cls = RemoteServerInfo.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpace");
		cachedSerQNames.add(qName);
		cls = RemoteSpace.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpaceGroup");
		cachedSerQNames.add(qName);
		cls = RemoteSpaceGroup.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpacePermissionSet");
		cachedSerQNames.add(qName);
		cls = RemoteSpacePermissionSet.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpaceSummary");
		cachedSerQNames.add(qName);
		cls = RemoteSpaceSummary.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteUser");
		cachedSerQNames.add(qName);
		cls = RemoteUser.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteUserInformation");
		cachedSerQNames.add(qName);
		cls = RemoteUserInformation.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://cluster.confluence.atlassian.com", "ClusterJoinConfig");
		cachedSerQNames.add(qName);
		cls = com.atlassian.confluence.cluster.ClusterJoinConfig.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "AlreadyExistsException");
		cachedSerQNames.add(qName);
		cls = AlreadyExistsException.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "AuthenticationFailedException");
		cachedSerQNames.add(qName);
		cls = AuthenticationFailedException.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "InvalidSessionException");
		cachedSerQNames.add(qName);
		cls = InvalidSessionException.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "NotPermittedException");
		cachedSerQNames.add(qName);
		cls = NotPermittedException.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "RemoteException");
		cachedSerQNames.add(qName);
		cls = RemoteException.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://rpc.confluence.atlassian.com", "VersionMismatchException");
		cachedSerQNames.add(qName);
		cls = VersionMismatchException.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "mapItem");
		cachedSerQNames.add(qName);
		cls = eu.wajja.input.fetcher.model.MapItem.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteAttachment");
		cachedSerQNames.add(qName);
		cls = RemoteAttachment[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteAttachment");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteBlogEntrySummary");
		cachedSerQNames.add(qName);
		cls = RemoteBlogEntrySummary[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteBlogEntrySummary");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteComment");
		cachedSerQNames.add(qName);
		cls = RemoteComment[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteComment");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteContentPermission");
		cachedSerQNames.add(qName);
		cls = RemoteContentPermission[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteContentPermission");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteContentPermissionSet");
		cachedSerQNames.add(qName);
		cls = RemoteContentPermissionSet[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteContentPermissionSet");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteContentSummary");
		cachedSerQNames.add(qName);
		cls = RemoteContentSummary[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteContentSummary");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteLabel");
		cachedSerQNames.add(qName);
		cls = RemoteLabel[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteLabel");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteNodeStatus");
		cachedSerQNames.add(qName);
		cls = RemoteNodeStatus[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteNodeStatus");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemotePageHistory");
		cachedSerQNames.add(qName);
		cls = RemotePageHistory[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePageHistory");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemotePageSummary");
		cachedSerQNames.add(qName);
		cls = RemotePageSummary[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePageSummary");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemotePermission");
		cachedSerQNames.add(qName);
		cls = RemotePermission[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePermission");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSearchResult");
		cachedSerQNames.add(qName);
		cls = RemoteSearchResult[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSearchResult");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSpace");
		cachedSerQNames.add(qName);
		cls = RemoteSpace[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpace");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSpaceGroup");
		cachedSerQNames.add(qName);
		cls = RemoteSpaceGroup[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpaceGroup");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSpacePermissionSet");
		cachedSerQNames.add(qName);
		cls = RemoteSpacePermissionSet[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpacePermissionSet");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteSpaceSummary");
		cachedSerQNames.add(qName);
		cls = RemoteSpaceSummary[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpaceSummary");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_tns1_RemoteUser");
		cachedSerQNames.add(qName);
		cls = RemoteUser[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteUser");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_xsd_anyType");
		cachedSerQNames.add(qName);
		cls = java.lang.Object[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName(baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2", "ArrayOf_xsd_string");
		cachedSerQNames.add(qName);
		cls = java.lang.String[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
		qName2 = null;
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

	}

	protected Call createCall() throws java.rmi.RemoteException {
		try {
			Call _call = super._createCall();
			if (super.maintainSessionSet) {
				_call.setMaintainSession(super.maintainSession);
			}
			if (super.cachedUsername != null) {
				_call.setUsername(super.cachedUsername);
			}
			if (super.cachedPassword != null) {
				_call.setPassword(super.cachedPassword);
			}
			if (super.cachedEndpoint != null) {
				_call.setTargetEndpointAddress(super.cachedEndpoint);
			}
			if (super.cachedTimeout != null) {
				_call.setTimeout(super.cachedTimeout);
			}
			if (super.cachedPortName != null) {
				_call.setPortName(super.cachedPortName);
			}
			java.util.Enumeration keys = super.cachedProperties.keys();
			while (keys.hasMoreElements()) {
				java.lang.String key = (java.lang.String) keys.nextElement();
				_call.setProperty(key, super.cachedProperties.get(key));
			}
			// All the type mapping information is registered
			// when the first call is made.
			// The type mapping information is actually registered in
			// the TypeMappingRegistry of the service, which
			// is the reason why registration is only needed for the first call.
			synchronized (this) {
				if (firstCall()) {
					// must set encoding style before registering serializers
					_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
					_call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
					for (int i = 0; i < cachedSerFactories.size(); ++i) {
						java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
						javax.xml.namespace.QName qName = (javax.xml.namespace.QName) cachedSerQNames.get(i);
						java.lang.Object x = cachedSerFactories.get(i);
						if (x instanceof Class) {
							java.lang.Class sf = (java.lang.Class) cachedSerFactories.get(i);
							java.lang.Class df = (java.lang.Class) cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						} else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
							org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory) cachedSerFactories.get(i);
							org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory) cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						}
					}
				}
			}
			return _call;
		} catch (java.lang.Throwable _t) {
			throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
		}
	}

	public RemoteSpacePermissionSet getSpacePermissionSet(java.lang.String in0, java.lang.String in1, java.lang.String in2)
			throws java.rmi.RemoteException, InvalidSessionException, NotPermittedException, RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[95]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://soap.rpc.confluence.atlassian.com", "getSpacePermissionSet"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0, in1, in2 });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (RemoteSpacePermissionSet) _resp;
				} catch (java.lang.Exception _exception) {
					return (RemoteSpacePermissionSet) org.apache.axis.utils.JavaUtils.convert(_resp, RemoteSpacePermissionSet.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof InvalidSessionException) {
					throw (InvalidSessionException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof NotPermittedException) {
					throw (NotPermittedException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof RemoteException) {
					throw (RemoteException) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

	public java.lang.String login(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, AuthenticationFailedException, RemoteException {

		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}

		Call _call = createCall();
		_call.setOperation(_operations[12]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://soap.rpc.confluence.atlassian.com", "login"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0, in1 });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (java.lang.String) _resp;
				} catch (java.lang.Exception _exception) {
					return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			if (axisFaultException.detail != null) {
				if (axisFaultException.detail instanceof java.rmi.RemoteException) {
					throw (java.rmi.RemoteException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof AuthenticationFailedException) {
					throw (AuthenticationFailedException) axisFaultException.detail;
				}
				if (axisFaultException.detail instanceof RemoteException) {
					throw (RemoteException) axisFaultException.detail;
				}
			}
			throw axisFaultException;
		}
	}

}
