package org.incha.ui.texteditor;

import junit.framework.TestCase;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.incha.core.texteditor.FileOpen;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by hlib on 02.11.17.
 */
public class TextEditorTest extends TestCase {
    public void testOpenFile() throws Exception {
        /* given */
        final String stubFileName = "stub1.java";
        FileOpen fileOpenStub1 = createFileOpenStub(stubFileName);

        FileOpen.FileOpenFactory fileOpenFactoryMock = mock(FileOpen.FileOpenFactory.class);
        when(fileOpenFactoryMock.create(stubFileName)).thenReturn(fileOpenStub1);

        JTabbedPane jTabbedPaneMock = spy(JTabbedPane.class);

        TextEditor.TextEditorElementsFactory textEditorElementsFactoryMock
                = mock(TextEditor.TextEditorElementsFactory.class);
        when(textEditorElementsFactoryMock.createJTabbedPane())
                .thenReturn(jTabbedPaneMock);

        TextEditor textEditor = new TextEditor(null,
                fileOpenFactoryMock, textEditorElementsFactoryMock);

        /* when */
        textEditor.openFile(stubFileName);

        /* then */
        List<FileOpen> openFiles = getOpenFiles(textEditor);
        assertEquals(stubFileName + " apparently was not added to open file list" , 1, openFiles.size());

        FileOpen fileOpen = openFiles.get(0);
        assertEquals(fileOpenStub1, fileOpen);

        verify(jTabbedPaneMock).addTab(eq(stubFileName), any(Component.class));
    }

    @SuppressWarnings("unchecked")
    private List<FileOpen> getOpenFiles(TextEditor textEditor) throws NoSuchFieldException, IllegalAccessException {
        Field f = textEditor.getClass().getDeclaredField("openFiles");
        f.setAccessible(true);
        return (List<FileOpen>) f.get(textEditor);
    }

    private FileOpen createFileOpenStub(String path) {
        FileOpen fileOpenStub = mock(FileOpen.class);
        when(fileOpenStub.getPath()).thenReturn(path);
        RSyntaxTextArea area = new RSyntaxTextArea();
        area.setText("package" + path + ";");
        when(fileOpenStub.getText()).thenReturn(area);
        when(fileOpenStub.getFileName()).thenReturn(path);
        when(fileOpenStub.open()).thenReturn(true);

        return fileOpenStub;
    }

}