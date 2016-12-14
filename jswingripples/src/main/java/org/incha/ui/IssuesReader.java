
package org.incha.ui;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

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
            
        } catch (SAXException e) {
            JOptionPane.showMessageDialog(null, e.toString(), "InfoBox: " + "File not found", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.toString(), "InfoBox: " + "File not found", JOptionPane.INFORMATION_MESSAGE);
        } catch (ParserConfigurationException e){
            JOptionPane.showMessageDialog(null, e.toString(), "InfoBox: " + "File not found", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
