package org.incha.ui.stats;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import org.incha.core.JavaProject;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JScrollPane;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.incha.compiler.dom.JavaDomUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractListModel;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.incha.ui.util.NullMonitor;

public class MainClassSearchDialog extends JDialog {
    private JList list = new JList();
    private StartAnalysisDialog startAnalysisDialogCallback;
    private JButton ok = new JButton();
    
    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }
    
    private Map<String, String> findMainClasses(JavaProject project) throws IOException{
        String patternRegex = "void\\s*main\\s*\\(";
        Pattern pattern = Pattern.compile(patternRegex);
        Map<String, String> mainClassToFileName = new HashMap<>();
        try {
            final ICompilationUnit[] units = JavaDomUtils.getCompilationUnits(project, new NullMonitor());
            for (ICompilationUnit unit : units) {
                IPackageDeclaration[] packageDeclarations = unit.getPackageDeclarations();
                IType[] allTypes = unit.getAllTypes();
                for (int j = 0; j < packageDeclarations.length; j++){
                    Matcher matcher = pattern.matcher(allTypes[j].toString());
                    if (matcher.find()) {
                        String fileName =
                                unit.getPath().toString().replaceAll("/", Matcher.quoteReplacement(File.separator));
                        mainClassToFileName.put(
                                packageDeclarations[j].getElementName()+"."+allTypes[j].getElementName(),fileName);
                    }
                }
            }
        } catch (JavaModelException ex) {
            Logger.getLogger(StartAnalysisDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mainClassToFileName;
    }
    
    
    private void configureList(final Map<String, String> mainClassToFileName) {
        final Object[] objs = new Object[mainClassToFileName.size()];
        int i = 0;
        for (String key : mainClassToFileName.keySet()) {
            objs[i] = (Object)key;
            i++;
        }
        list.setModel(new AbstractListModel() {
            Object[] strings = objs;
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    ok.setEnabled(true);
                } 
                if (evt.getClickCount() == 2) {
                    try {
                        okActionPerformed(mainClassToFileName);
                    } catch (IOException ex) {
                        Logger.getLogger(MainClassSearchDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    public MainClassSearchDialog(final StartAnalysisDialog callback, JavaProject project) throws IOException{
        ok.setEnabled(false);
        startAnalysisDialogCallback = callback;
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
       final Map<String, String> mainClassToFileName = findMainClasses(project);
        
        JScrollPane jScrollPane1 = new JScrollPane();
        
        JButton cancel = new JButton();
        
        
        configureList(mainClassToFileName);
        jScrollPane1.setViewportView(list);
        
        ok.setText("Ok");
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    okActionPerformed(mainClassToFileName);
                } catch (IOException ex) {
                    Logger.getLogger(MainClassSearchDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        cancel.setText("Cancel");
        cancel.setToolTipText("");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed();
            }
        });
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(ok, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(99, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ok)
                    .addComponent(cancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        
        
        
    }
    
    private void okActionPerformed(Map<String, String> mainClassToFileName) throws IOException {
        dispose();
        int index = list.getSelectedIndex();
        if (index!= -1){
            String selectedItem = list.getSelectedValue().toString();
            startAnalysisDialogCallback.setClassName(selectedItem,mainClassToFileName.get(selectedItem));
            startAnalysisDialogCallback.enableButtonOk();
        }
    }                                  

    private void cancelActionPerformed() {                                       
        dispose();
    }        


}
