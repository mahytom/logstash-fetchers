/**
 * ConfluenceSoapServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package eu.wajja.input.fetcher.soap.confluence;

public class ConfluenceSoapServiceServiceLocator extends org.apache.axis.client.Service implements ConfluenceSoapServiceService {

	private static final long serialVersionUID = 4853654254565968549L;
	private static String baseUrl;

	public ConfluenceSoapServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName, String baseUrl) throws javax.xml.rpc.ServiceException {
//		super(wsdlLoc, sName);
		ConfluenceSoapServiceServiceLocator.baseUrl = baseUrl;
	}

	// Use to get a proxy class for ConfluenceserviceV2
	private java.lang.String confluenceserviceAddress;

	public java.lang.String getConfluenceserviceV2Address() {
		return baseUrl + "/plugins/servlet/soap-axis1/confluenceservice-v2";
	}

	// The WSDD service name defaults to the port name.
	private java.lang.String confluenceserviceV2WSDDServiceName = "confluenceservice-v2";

	public java.lang.String getConfluenceserviceV2WSDDServiceName() {
		return confluenceserviceV2WSDDServiceName;
	}

	public void setConfluenceserviceV2WSDDServiceName(java.lang.String name) {
		confluenceserviceV2WSDDServiceName = name;
	}

	public ConfluenceSoapService getConfluenceserviceV2() throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(confluenceserviceAddress);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getConfluenceserviceV2(endpoint);
	}

	public ConfluenceSoapService getConfluenceserviceV2(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {

		ConfluenceserviceV2SoapBindingStub _stub = new ConfluenceserviceV2SoapBindingStub(portAddress, this, baseUrl);
		_stub.setPortName(getConfluenceserviceV2WSDDServiceName());
		return _stub;
	}

	public void setConfluenceserviceV2EndpointAddress(java.lang.String address) {
		confluenceserviceAddress = address;
	}

	/**
	 * For the given interface, get the stub implementation. If this service has no
	 * port for the given interface, then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		java.lang.String inputPortName = portName.getLocalPart();
		if ("confluenceservice-v2".equals(inputPortName)) {
			return getConfluenceserviceV2();
		} else {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName(confluenceserviceAddress, "ConfluenceSoapServiceService");
	}

	private java.util.HashSet ports = null;

	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName(confluenceserviceAddress, "confluenceservice-v2"));
		}
		return ports.iterator();
	}

}
