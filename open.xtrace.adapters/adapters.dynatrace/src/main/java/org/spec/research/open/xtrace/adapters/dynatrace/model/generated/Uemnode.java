//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.13 at 11:58:26 AM CET 
//


package org.spec.research.open.xtrace.adapters.dynatrace.model.generated;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}attachment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}uemnode" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="node" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="detail" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="exectotaltime" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="elapsedtime" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="api" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="agent" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="level" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="entryhopcount" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="exithopcount" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="error_state" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "attachment",
    "uemnode"
})
@XmlRootElement(name = "uemnode")
public class Uemnode {

    protected List<Attachment> attachment;
    protected List<Uemnode> uemnode;
    @XmlAttribute(name = "node")
    protected String node;
    @XmlAttribute(name = "detail")
    protected String detail;
    @XmlAttribute(name = "exectotaltime")
    protected Double exectotaltime;
    @XmlAttribute(name = "elapsedtime")
    protected Double elapsedtime;
    @XmlAttribute(name = "api")
    protected String api;
    @XmlAttribute(name = "agent")
    protected String agent;
    @XmlAttribute(name = "level")
    protected BigInteger level;
    @XmlAttribute(name = "entryhopcount")
    protected BigInteger entryhopcount;
    @XmlAttribute(name = "exithopcount")
    protected BigInteger exithopcount;
    @XmlAttribute(name = "error_state")
    protected String errorState;

    /**
     * Gets the value of the attachment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attachment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttachment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Attachment }
     * 
     * 
     */
    public List<Attachment> getAttachment() {
        if (attachment == null) {
            attachment = new ArrayList<Attachment>();
        }
        return this.attachment;
    }

    /**
     * Gets the value of the uemnode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uemnode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUemnode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Uemnode }
     * 
     * 
     */
    public List<Uemnode> getUemnode() {
        if (uemnode == null) {
            uemnode = new ArrayList<Uemnode>();
        }
        return this.uemnode;
    }

    /**
     * Gets the value of the node property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNode() {
        return node;
    }

    /**
     * Sets the value of the node property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNode(String value) {
        this.node = value;
    }

    /**
     * Gets the value of the detail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetail() {
        return detail;
    }

    /**
     * Sets the value of the detail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetail(String value) {
        this.detail = value;
    }

    /**
     * Gets the value of the exectotaltime property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getExectotaltime() {
        return exectotaltime;
    }

    /**
     * Sets the value of the exectotaltime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setExectotaltime(Double value) {
        this.exectotaltime = value;
    }

    /**
     * Gets the value of the elapsedtime property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getElapsedtime() {
        return elapsedtime;
    }

    /**
     * Sets the value of the elapsedtime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setElapsedtime(Double value) {
        this.elapsedtime = value;
    }

    /**
     * Gets the value of the api property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApi() {
        return api;
    }

    /**
     * Sets the value of the api property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApi(String value) {
        this.api = value;
    }

    /**
     * Gets the value of the agent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgent() {
        return agent;
    }

    /**
     * Sets the value of the agent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgent(String value) {
        this.agent = value;
    }

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLevel(BigInteger value) {
        this.level = value;
    }

    /**
     * Gets the value of the entryhopcount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEntryhopcount() {
        return entryhopcount;
    }

    /**
     * Sets the value of the entryhopcount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEntryhopcount(BigInteger value) {
        this.entryhopcount = value;
    }

    /**
     * Gets the value of the exithopcount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getExithopcount() {
        return exithopcount;
    }

    /**
     * Sets the value of the exithopcount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setExithopcount(BigInteger value) {
        this.exithopcount = value;
    }

    /**
     * Gets the value of the errorState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorState() {
        return errorState;
    }

    /**
     * Sets the value of the errorState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorState(String value) {
        this.errorState = value;
    }

}