//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.13 at 11:58:26 AM CET 
//


package org.spec.research.open.xtrace.adapters.dynatrace.model.generated;

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
 *         &lt;element name="source" type="{}sourceType" minOccurs="0"/>
 *         &lt;element name="comparesource" type="{}sourceType" minOccurs="0"/>
 *         &lt;element name="recordset" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="record" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="structure" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="healthhistory" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="details" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="labels" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="os" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "source",
    "comparesource",
    "recordset"
})
@XmlRootElement(name = "infrastructureoverview")
public class Infrastructureoverview {

    protected SourceType source;
    protected SourceType comparesource;
    protected Infrastructureoverview.Recordset recordset;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "description")
    protected String description;

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link SourceType }
     *     
     */
    public SourceType getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceType }
     *     
     */
    public void setSource(SourceType value) {
        this.source = value;
    }

    /**
     * Gets the value of the comparesource property.
     * 
     * @return
     *     possible object is
     *     {@link SourceType }
     *     
     */
    public SourceType getComparesource() {
        return comparesource;
    }

    /**
     * Sets the value of the comparesource property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceType }
     *     
     */
    public void setComparesource(SourceType value) {
        this.comparesource = value;
    }

    /**
     * Gets the value of the recordset property.
     * 
     * @return
     *     possible object is
     *     {@link Infrastructureoverview.Recordset }
     *     
     */
    public Infrastructureoverview.Recordset getRecordset() {
        return recordset;
    }

    /**
     * Sets the value of the recordset property.
     * 
     * @param value
     *     allowed object is
     *     {@link Infrastructureoverview.Recordset }
     *     
     */
    public void setRecordset(Infrastructureoverview.Recordset value) {
        this.recordset = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }


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
     *         &lt;element name="record" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="structure" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="healthhistory" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="details" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="labels" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="os" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "record"
    })
    public static class Recordset {

        protected List<Infrastructureoverview.Recordset.Record> record;

        /**
         * Gets the value of the record property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the record property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRecord().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Infrastructureoverview.Recordset.Record }
         * 
         * 
         */
        public List<Infrastructureoverview.Recordset.Record> getRecord() {
            if (record == null) {
                record = new ArrayList<Infrastructureoverview.Recordset.Record>();
            }
            return this.record;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="structure" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="healthhistory" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="details" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="labels" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="os" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Record {

            @XmlAttribute(name = "structure")
            protected String structure;
            @XmlAttribute(name = "healthhistory")
            protected String healthhistory;
            @XmlAttribute(name = "details")
            protected String details;
            @XmlAttribute(name = "labels")
            protected String labels;
            @XmlAttribute(name = "os")
            protected String os;

            /**
             * Gets the value of the structure property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getStructure() {
                return structure;
            }

            /**
             * Sets the value of the structure property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setStructure(String value) {
                this.structure = value;
            }

            /**
             * Gets the value of the healthhistory property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getHealthhistory() {
                return healthhistory;
            }

            /**
             * Sets the value of the healthhistory property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setHealthhistory(String value) {
                this.healthhistory = value;
            }

            /**
             * Gets the value of the details property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDetails() {
                return details;
            }

            /**
             * Sets the value of the details property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDetails(String value) {
                this.details = value;
            }

            /**
             * Gets the value of the labels property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLabels() {
                return labels;
            }

            /**
             * Sets the value of the labels property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLabels(String value) {
                this.labels = value;
            }

            /**
             * Gets the value of the os property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getOs() {
                return os;
            }

            /**
             * Sets the value of the os property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setOs(String value) {
                this.os = value;
            }

        }

    }

}