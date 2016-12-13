/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.incha.ui;

/**
 *
 * @author jrobledo
 */
public class IssuesReader {
    private String filename;
    
    public Object[][] loadData(){
                    Object[][] data = {
                    {new Integer(79), "Update RS032"},
                    {new Integer(77), "Save issues changes in the issues file (local)"},
                    {new Integer(76), "Upload changes to GitHub"},
                    {new Integer(75), "Change the color of a text"},
                    {new Integer(74), "Right clic in a word and show an option menu (with the color options)"}};
        return data;
    }
    
    public String[] loadColumnNames(){
        String[] columnNames = {"Number",
                        "Title"};
        return columnNames;
    }
}
