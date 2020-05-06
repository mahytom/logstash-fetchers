/**
 * RemoteSpacePermissionSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.atlassian.confluence.rpc.soap.beans;

public class RemoteSpacePermissionSet  implements java.io.Serializable {
    private com.atlassian.confluence.rpc.soap.beans.RemoteContentPermission[] spacePermissions;

    private java.lang.String type;

    public RemoteSpacePermissionSet() {
    }

    public RemoteSpacePermissionSet(
           com.atlassian.confluence.rpc.soap.beans.RemoteContentPermission[] spacePermissions,
           java.lang.String type) {
           this.spacePermissions = spacePermissions;
           this.type = type;
    }


    /**
     * Gets the spacePermissions value for this RemoteSpacePermissionSet.
     * 
     * @return spacePermissions
     */
    public com.atlassian.confluence.rpc.soap.beans.RemoteContentPermission[] getSpacePermissions() {
        return spacePermissions;
    }


    /**
     * Sets the spacePermissions value for this RemoteSpacePermissionSet.
     * 
     * @param spacePermissions
     */
    public void setSpacePermissions(com.atlassian.confluence.rpc.soap.beans.RemoteContentPermission[] spacePermissions) {
        this.spacePermissions = spacePermissions;
    }


    /**
     * Gets the type value for this RemoteSpacePermissionSet.
     * 
     * @return type
     */
    public java.lang.String getType() {
        return type;
    }


    /**
     * Sets the type value for this RemoteSpacePermissionSet.
     * 
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RemoteSpacePermissionSet)) return false;
        RemoteSpacePermissionSet other = (RemoteSpacePermissionSet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.spacePermissions==null && other.getSpacePermissions()==null) || 
             (this.spacePermissions!=null &&
              java.util.Arrays.equals(this.spacePermissions, other.getSpacePermissions()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getSpacePermissions() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSpacePermissions());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSpacePermissions(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RemoteSpacePermissionSet.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpacePermissionSet"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spacePermissions");
        elemField.setXmlName(new javax.xml.namespace.QName("", "spacePermissions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteContentPermission"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
