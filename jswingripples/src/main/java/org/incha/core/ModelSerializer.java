package org.incha.core;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.incha.ui.JSwingRipplesApplication;
import org.kohsuke.github.GHIssue;
import org.w3c.dom.DOMImplementation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
/**
 * Stores information and methods for the parser of XML files.
 */
public class ModelSerializer {
    /**
     * XML attribute name.
     */
    protected static final String NAME_ATTR = "name";
    /**
     * XML element 'file'.
     */
    protected static final String FILE = "file";
    /**
     * XML element 'resource'.
     */
    protected static final String SOURCES = "sources";
    /**
     * XML element 'repository'.
     */
    protected static final String REPOSITORY = "repository";
    /**
     * XML element 'project'.
     */
    protected static final String PROJECT = "project";
    /**
     * XML element 'application'.
     */
    protected static final String APPLICATION = "application";

    /**
     * Default constructor.
     */
    public ModelSerializer() {
        super();
    }

    public JavaProjectsModel parse(final Reader r) throws SAXException, IOException {
        final DocumentBuilder db = newDocumentBuilder();
        final Document dom = db.parse(new InputSource(r));

        final JavaProjectsModel model = new JavaProjectsModel();
        for (final Element p: getChildElements(dom.getDocumentElement(), PROJECT)) {
            parseProject(model, p);
        }

        return model;
    }
    /**
     * @param model model.
     * @param projectItem XML item.
     */
    private void parseProject(final JavaProjectsModel model, final Element projectItem) {
        final JavaProject project = new JavaProject(projectItem.getAttribute(NAME_ATTR));
        model.addProject(project);

        //process sources
        final Element sources = getFirstChildElement(projectItem, SOURCES);

        List<Element> files = getChildElements(sources, FILE);
        for (final Element e : files) {
            project.getBuildPath().addSource(new File(e.getTextContent()));
        }
        final Element repo = getFirstChildElement(projectItem, REPOSITORY);
        project.getGHRepo().replaceRepository(repo.getTextContent());
    }

    /**
     * @param element the element.
     * @param tagName the tag name.
     * @return
     */
    private List<Element> getChildElements(final Element element, final String tagName) {
        final NodeList nodes = element.getChildNodes();
        final List<Element> result = new LinkedList<Element>();

        final int len = nodes.getLength();
        for (int i = 0; i < len; i++) {
            final Node n = nodes.item(i);
            if (n instanceof Element) {
                final Element e = (Element) n;
                if (tagName.equals(e.getTagName())) {
                    result.add(e);
                }
            }
        }

        return result;
    }
    /**
     * @param element the element.
     * @param tagName the tag name.
     * @return the first found child element by given child name.
     */
    private Element getFirstChildElement(final Element element, final String tagName) {
        final List<Element> els = getChildElements(element, tagName);
        return els.size() == 0 ? null : els.get(0);
    }

    /**
     * @param model saves model.
     * @param out output writer.
     * @throws TransformerException
     */
    public void save(final JavaProjectsModel model, final Writer out) throws TransformerException {
        final Document dom = newDocumentBuilder().newDocument();
        final Element application = dom.createElement(APPLICATION);
        dom.appendChild(application);

        for (final JavaProject p : model.getProjects()) {
            final Element project = dom.createElement(PROJECT);
            project.setAttribute(NAME_ATTR, p.getName());
            application.appendChild(project);

            //add build path
            //add sources
            final Element sources = dom.createElement(SOURCES);
            project.appendChild(sources);
            for (final File f : p.getBuildPath().getSources()) {
                final Element src = dom.createElement(FILE);
                sources.appendChild(src);
                src.appendChild(dom.createTextNode(f.getPath()));
            }
            final Element repository = dom.createElement(REPOSITORY);
            project.appendChild(repository);
            repository.appendChild(dom.createTextNode(p.getGHRepo().getCurrentRepository()));

        }

        saveDom(dom, out);
    }

    /**
     * @return
     * @throws ParserConfigurationException
     */
    private DocumentBuilder newDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (final ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param node the node to save.
     * @param out the output.
     * @throws TransformerException
     */
    private void saveDom(final Node node, final Writer out) throws TransformerException {
        final TransformerFactory f = TransformerFactory.newInstance();
        final Transformer transformer = f.newTransformer();

        final DOMSource source = new DOMSource(node);
        final StreamResult result = new StreamResult(out);
        transformer.transform(source, result);
    }
    
    public static void generate(List<GHIssue> openIssuesList, String projectName) throws Exception {        
        String xmlFileName = projectName;        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation implementation = builder.getDOMImplementation();
        Document document = implementation.createDocument(null, "project", null);
        document.setXmlVersion("1.0");        
        //Main Node
        Element root = document.getDocumentElement();
        //Name Node
        Element nameNode = document.createElement("name");
        Text nameValue = document.createTextNode(xmlFileName);
        nameNode.appendChild(nameValue);
        //List Issue Node
        Element listIssuesNode = document.createElement("list_issues");        
        for (GHIssue gHIssue : openIssuesList) {            
            //Issue Node
            Element issueNode = document.createElement("issue");
            issueNode.setAttribute("id", Integer.toString(gHIssue.getId()));
            //Number Node
            Element numberIssueNode = document.createElement("number"); 
            Text numberIssueValue = document.createTextNode(Integer.toString(gHIssue.getNumber()));
            numberIssueNode.appendChild(numberIssueValue);
            //Title Node
            Element titleIssueNode = document.createElement("title"); 
            Text titleIssueValue = document.createTextNode(gHIssue.getTitle());
            titleIssueNode.appendChild(titleIssueValue);            
            //append numberIssueNode to issueNode
            issueNode.appendChild(numberIssueNode);
            //append titleIssueNode to issueNode
            issueNode.appendChild(titleIssueNode);
            //append issueNode to listIssuesNode
            listIssuesNode.appendChild(issueNode);
        }
        //append nameNode to raiz
        root.appendChild(nameNode);
        //append itemNode to raiz
        root.appendChild(listIssuesNode);        
        //Generate XML
        Source source = new DOMSource(document);
        //Indicamos donde lo queremos almacenar
        Result result = new StreamResult(new java.io.File(JSwingRipplesApplication.getHome() + File.separator +xmlFileName+".xml")); //nombre del archivo
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);
    }
}
