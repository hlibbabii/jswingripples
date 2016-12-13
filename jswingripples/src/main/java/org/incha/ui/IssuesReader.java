/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.incha.ui;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jrobledo
 */
public class IssuesReader {
    private String filename;
    private Object[][] data;
    private String[] columnNames = {"Number","Title"};
    
    public IssuesReader(String filename) {
        this.filename = filename;
    }
    
    public Object[][] loadData(){
        return this.data;
    }
    
    public String[] loadColumnNames(){
        return this.columnNames;
    }
    
    public void load(){
        try {
            File fXmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("issue");
            Object[][] a = new Object[nList.getLength()][2];
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        Object[] b = new Object[2];
                        b[0] = eElement.getElementsByTagName("number").item(0).getTextContent();
                        b[1] = eElement.getElementsByTagName("title").item(0).getTextContent();
                        a[temp] = b;
                    }
            }
        this.data = a;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
