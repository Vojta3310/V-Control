package Moduls.Skladnik.DataStructure;

import Moduls.Skladnik.io.xml.XML.Tag;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author Ondřej Bleha
 */
public class Kategorie {
    private final DefaultTreeModel model;
    private final DefaultMutableTreeNode root;
    private int maxID;
    
    public Kategorie(DefaultTreeModel model){        
        this.model = model;
        this.root = (DefaultMutableTreeNode) model.getRoot();
        this.maxID = 0;
    }
    
    public DefaultMutableTreeNode getRoot(){
        return root;
    }
    
    public void addKategorie(JTree tree, String name, String syn){
        
        if (!name.equals("")){
            Tag k = new Tag(name, syn, zvysMaxID());
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(k);
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();        
            if (selectedNode == null || parent==null) {
                model.insertNodeInto(newNode, root, root.getChildCount());
            }else if(selectedNode.getUserObject().getClass()==Tag.class){            
                model.insertNodeInto(newNode, selectedNode, selectedNode.getChildCount());
            }else{
                JOptionPane.showMessageDialog(new javax.swing.JFrame(),
                    "Při vkládání kategorie se někde vyskytla chyba!",
                    "Chyba",
                    JOptionPane.ERROR_MESSAGE);
            }
            tree.expandPath(tree.getSelectionPath());
        }
    }
    
    public void removeKategorie(JTree tree){
        
        //smaze uzel
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();        
        if (selectedNode != null && parent!=null){
            model.removeNodeFromParent(selectedNode);
        }
        tree.setSelectionRow(0);
    }
    
    public void editKategorie(JTree tree, String name, String syn){
        if(tree.getSelectionPath()!=null){
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if(node.getUserObject().getClass()==Tag.class){
                Tag k = (Tag) node.getUserObject();
                k.setNazev(name);
                k.setSynonyma(syn);
            }
        }
    }
    
    public DefaultTreeModel getModel(){
        return model;
    }
    
    public String getStringPath(int kategorie){
        TreeNode[] nodes;
        if(kategorie == 0){
            return "/Vše";
        }else{
            nodes = najdiPolozku(kategorie, root).getPath();
        }
        StringBuilder string = new StringBuilder();
            for (TreeNode node1 : nodes) {
                string.append("/");
                string.append(node1);
            }
        return string.toString();
    }
    
    public TreePath getPath(int kategorie){
        if(kategorie == 0){
            return new TreePath(root.getPath());
        }else{
            TreePath path = new TreePath(najdiPolozku(kategorie, root).getPath());
            if(path == null){
                path = new TreePath(root.getPath());
            }
            return path;
        }
        
    }
    
    public boolean isPodkategorie(int box, DefaultMutableTreeNode selected){ //box vysle signal nahoru a pokud trefi selected tak to vrati true
        //System.out.println(box+": je podkategorii");
        if (selected != null){
            boolean stav = false;
            DefaultMutableTreeNode currentNode = najdiPolozku(box, root);
            if (currentNode==null){
              currentNode=root;
            }
            if(selected.isRoot()){
                return true;
            }
            Tag sel_tag = (Tag) selected.getUserObject();
            do{
                Tag t = (Tag) currentNode.getUserObject();
                if (t.getID() == sel_tag.getID()){
                    stav = true;
                }
                if (currentNode.getParent() == null){
                    currentNode = null;
                }else{
                    currentNode = (DefaultMutableTreeNode) currentNode.getParent();
                }
            }while(currentNode != null && stav == false);
            return stav;
        }else{        
            return true;
        }
    }
    
    public DefaultMutableTreeNode najdiPolozku(int kategorie, DefaultMutableTreeNode root){
        if(root.getChildCount() > 0){
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) root.getFirstChild();
            do{
                Tag tag = (Tag) currentNode.getUserObject();  
                //System.out.println("hledam"+tag.getID());
                if(tag.getID() == kategorie){  
                  return currentNode;
                }
                
                DefaultMutableTreeNode node = najdiPolozku(kategorie, currentNode);
                if(node != null){
                    return node;
                }
                currentNode = (DefaultMutableTreeNode) root.getChildAfter(currentNode);
            }while(currentNode != null);
        }
        return null;
    }
    
    public int getMaxID(){
        return maxID;
    }
        
    public void setMaxID(int hodnota){
        maxID = hodnota;
    }
    
    public int zvysMaxID(){
        maxID++;
        return maxID;
    }
    
}
