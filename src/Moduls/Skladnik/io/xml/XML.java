package Moduls.Skladnik.io.xml;

import Moduls.Skladnik.DataStructure.Box;
import Moduls.Skladnik.DataStructure.ISklad;
import Moduls.Skladnik.DataStructure.ModelService;
import Moduls.Skladnik.DataStructure.Sklad;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Ondřej Bleha
 */
public class XML {
    private final String file;
    
    public XML(String file) throws IOException{
        if(file != null && new File(file).exists()){
            this.file = file;
            System.out.println("XML");
        }else{
            this.file = System.getProperty("user.dir")+"/Sklad.xml";
            
            //this.file = new File("/Sklad.xml").getAbsoluteFile().toString();
        }
        System.out.println(this.file);
    }
    
    public ISklad read() throws ParserConfigurationException, SAXException, IOException{
        if(file != null && new File(file).exists()){
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            Handler handler = new Handler();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new FileReader(file)));
            return handler.getSklad();
        }else{
            return new Sklad(1,1,1, new DefaultTreeModel(new DefaultMutableTreeNode(new Tag("Vše", "", 0))), new ModelService());
        }
    }
    
    public void stow(ISklad sklad) throws IOException, XMLStreamException {
        FileWriter outputFile = new FileWriter(file);
        XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(outputFile);

        writer.writeStartDocument("UTF-8", "1.0");
        int a = 0;
        Box box;

        writer.writeStartElement("sklad");
        writer.writeAttribute("sirka", String.valueOf(sklad.getSIRKA()));
        writer.writeAttribute("vyska", String.valueOf(sklad.getVYSKA()));
        writer.writeAttribute("hloubka", String.valueOf(sklad.getHLOUBKA()));
        
        for (int y = 0; y < sklad.getSIRKA(); y++) {
            writer.writeStartElement("sloupec");
            writer.writeAttribute("y", String.valueOf(y+1));
            for (int z = 0; z < sklad.getVYSKA(); z++) {
                writer.writeStartElement("radek");
                writer.writeAttribute("z", String.valueOf(z+1));
                for (int x = 0; x < sklad.getHLOUBKA(); x++) {
                    box = sklad.getBox(y+1, z+1, x+1);
                    if (box != null){
                        writer.writeStartElement("box");
                        writer.writeAttribute("x", String.valueOf(x+1));                    
                        writer.writeAttribute("obsah", String.valueOf(box.getObsah()));
                        writer.writeAttribute("kategorie", String.valueOf(box.getKategorie()));
                        writer.writeAttribute("pojmy", String.valueOf(box.getPojmy()));
                        writer.writeAttribute("id", String.valueOf(box.getID()));
                        writer.writeAttribute("vytazeno", String.valueOf(box.getVytazeno()));
                        writer.writeEndElement();
                    }
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        //vyndane
        writer.writeStartElement("vyndane");
        DefaultListModel<Box> model = sklad.getVyndane().getListModel();
        for (int i = 0; i < model.getSize(); i++) {
            writer.writeStartElement("box");
            Box b = model.getElementAt(i);
                writer.writeAttribute("obsah", String.valueOf(b.getObsah()));
                writer.writeAttribute("kategorie", String.valueOf(b.getKategorie()));
                writer.writeAttribute("pojmy", String.valueOf(b.getPojmy()));
                writer.writeAttribute("id", String.valueOf(b.getID()));
                writer.writeAttribute("vytazeno", String.valueOf(b.getVytazeno()));
            writer.writeEndElement();
        }
        writer.writeEndElement();
        //kategorie
        writer.writeStartElement("kategorie");
        writer.writeAttribute("maxID", String.valueOf(sklad.getKategorie().getMaxID()));
        
        ulozStrom((DefaultMutableTreeNode) sklad.getTreeModel().getRoot(), writer);
        
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();
        if (a != 0) {
            throw new IndexOutOfBoundsException();
        }
    }
    
    private void ulozStrom(DefaultMutableTreeNode root, XMLStreamWriter writer) throws XMLStreamException{
        if(root.getChildCount() > 0){
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) root.getFirstChild();
            do{
                Tag tag = (Tag) currentNode.getUserObject();
                writer.writeStartElement("pojem");
                writer.writeAttribute("nazev", tag.getNazev());
                writer.writeAttribute("synonyma", tag.getSynonyma());
                writer.writeAttribute("id", String.valueOf(tag.getID()));            
                    ulozStrom(currentNode, writer);            
                writer.writeEndElement();
                currentNode = (DefaultMutableTreeNode) root.getChildAfter(currentNode);
            }while(currentNode != null);
        }
    }
    
    static class Handler extends DefaultHandler {

        ISklad Sklad;              
        boolean boo_sklad = false;
        boolean boo_sloupec = false;
        boolean boo_radek = false;
        boolean boo_kategorie = false;
        boolean boo_vyndane = false;
        int s;
        int r;
        int h;
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Tag("Vše", "", 0));
        DefaultMutableTreeNode currentNode;
        DefaultTreeModel model = new DefaultTreeModel(root);
        ModelService vyndane = new ModelService();
        
        public DefaultTreeModel getTreeModel(){
            return model;
        }
        public ModelService getVyndane(){
            return vyndane;
        }
        public DefaultMutableTreeNode getRoot() {
            return root;
        }
        public ISklad getSklad() {
            return Sklad;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {         
            
            if (qName.equals("sklad")) {
                boo_sklad = true;
                int sirka = 0;
                int vyska = 0;
                int hloubka = 0;
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getLocalName(i).equals("sirka")) {
                        sirka = Integer.valueOf(attributes.getValue(i));
                    }
                    if (attributes.getLocalName(i).equals("vyska")) {
                        vyska = Integer.valueOf(attributes.getValue(i));
                    }
                    if (attributes.getLocalName(i).equals("hloubka")) {
                        hloubka = Integer.valueOf(attributes.getValue(i));
                    }
                }
                if (sirka*vyska*hloubka != 0){
                    Sklad = new Sklad(sirka, vyska, hloubka, model, vyndane);
                }else{
                    throw new IndexOutOfBoundsException("Neplatna velikost skladu! (obsahuje nulu)");
                }
            }
            
            if (qName.equals("sloupec") && boo_sklad) {
                boo_sloupec = true;
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getLocalName(i).equals("y")) {
                        s = Integer.valueOf(attributes.getValue(i));
                    }
                }
            }
            
            if (qName.equals("radek") && boo_sklad && boo_sloupec) {
                boo_radek = true;
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getLocalName(i).equals("z")) {
                        r = Integer.valueOf(attributes.getValue(i));
                    }
                }
            }
            
            if (qName.equals("box") && boo_sklad && boo_sloupec && boo_radek && !boo_vyndane) {
                String obsah = "";
                int kategorie = 0;
                String pojmy = "";
                int id = 0;
                int vytazeno = 0;
                
                for (int i = 0; i < attributes.getLength(); i++) {
                    switch (attributes.getLocalName(i)) {
                        case "x":
                            h = Integer.valueOf(attributes.getValue(i));
                            break;
                        case "obsah":
                            obsah = attributes.getValue(i);
                            break;
                        case "kategorie":
                            kategorie = Integer.valueOf(attributes.getValue(i));
                            break;
                        case "pojmy":
                            pojmy = attributes.getValue(i);
                            break;
                        case "id":
                            id = Integer.valueOf(attributes.getValue(i));
                            break;
                        case "vytazeno":
                            vytazeno = Integer.valueOf(attributes.getValue(i));
                            break;
                    }
                }
                Sklad.setBox(s, r, h, new Box(obsah, kategorie, pojmy, id, vytazeno));
            }
            
            if (qName.equals("vyndane")) {
                boo_vyndane = true;
            }
            
            if (qName.equals("box") && boo_sklad && !boo_sloupec && !boo_radek && boo_vyndane) {
                String obsah = "";
                int kategorie = 0;
                String pojmy = "";
                int id = 0;
                int vytazeno = 0;
                
                for (int i = 0; i < attributes.getLength(); i++) {
                    switch (attributes.getLocalName(i)) {
                        case "x":
                            h = Integer.valueOf(attributes.getValue(i));
                            break;
                        case "obsah":
                            obsah = attributes.getValue(i);
                            break;
                        case "kategorie":
                            kategorie = Integer.valueOf(attributes.getValue(i));
                            break;
                        case "pojmy":
                            pojmy = attributes.getValue(i);
                            break;
                        case "id":
                            id = Integer.valueOf(attributes.getValue(i));
                            break;
                        case "vytazeno":
                            vytazeno = Integer.valueOf(attributes.getValue(i));
                            break;
                    }
                }
                vyndane.vlozBox(new Box(obsah, kategorie, pojmy, id, vytazeno));
            }
            
            if (qName.equals("kategorie")) {
                boo_kategorie = true;
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getLocalName(i).equals("maxID")) {
                        Sklad.getKategorie().setMaxID(Integer.valueOf(attributes.getValue(i)));
                    }
                }
            }
            
            if (qName.equals("pojem") && boo_kategorie) {
                String nazev = null;
                String synonyma = null;
                int id = 0;
                for (int i = 0; i < attributes.getLength(); i++) {
                    switch (attributes.getLocalName(i)) {
                        case "nazev":
                            nazev = attributes.getValue(i);
                            break;
                        case "synonyma":
                            synonyma = attributes.getValue(i);
                            break;
                        case "id":
                            id = Integer.valueOf(attributes.getValue(i));
                            break;
                    }
                }
                //vytvori uzel
                Tag t = new Tag(nazev, synonyma, id);
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(t);
                if (currentNode == null) {
                    root.add(newNode);
                } else {
                    // Must not be the root node...
                    currentNode.add(newNode);
                }
                currentNode = newNode;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {

            switch (qName) {
                case "sklad":
                    boo_sklad = false;
                    break;
                case "sloupec":
                    boo_sloupec = false;
                    break;
                case "radek":
                    boo_radek = false;
                    break;
                case "kategorie":
                    boo_kategorie = false;
                    break;
                case "vyndane":
                    boo_vyndane = false;
                    break;
                case "pojem":
                    currentNode = (DefaultMutableTreeNode) currentNode.getParent();
                    break;
            }
        }

        @Override
        public void setDocumentLocator(Locator locator) {
        }

        @Override
        public void startDocument() throws SAXException {
            if (boo_sklad || boo_sloupec || boo_radek || boo_kategorie || boo_vyndane) {
                throw new VerifyError();
            }
        }

        @Override
        public void endDocument() throws SAXException {
            if (boo_sklad || boo_sloupec || boo_radek || boo_kategorie || boo_vyndane) {
                throw new VerifyError();
            }
        }
    }
    
    public static class Tag {
    private String nazev;
    private String synonyma;
    private final int id;

    public Tag(String nazev, String synonyma, int id) {
      this.nazev = nazev;
      this.synonyma = synonyma;
      this.id = id;
    }

    public String getNazev() {
      return nazev;
    }
    
    public String getSynonyma() {
      return synonyma;
    }
    
    public void setNazev(String nazev){
        this.nazev = nazev;
    }
    
    public void setSynonyma(String synonyma){
        this.synonyma = synonyma;
    }

    public int getID() {
      return id;
    }

    @Override
    public String toString() {
      return nazev;
    }
  }
}
