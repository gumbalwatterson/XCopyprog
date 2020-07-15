package com.gumbal;



import java.awt.Component;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.io.BufferedInputStream;
import java.io.File;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.event.TreeModelListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;




public class XCopy  {

protected static FileSystemView fsv = FileSystemView.getFileSystemView();

private JFrame frame;
private JButton btnCopy;
private String pathfortree;
private String pathfortree2;
private Copying copying;
private JTree tree;
private JTree tree2; 

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					XCopy window = new XCopy();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public XCopy() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 837, 560);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("XCopy");
		frame.setResizable(false);
		
		copying = new Copying();
		
// first tree
 File[] roots = File.listRoots();
 CustomTreeNode customtreenode = new CustomTreeNode(roots);
 tree = new JTree(customtreenode); 
 tree.setCellRenderer(new FileTreeCellRenderer());
 tree.setRootVisible(false);
 tree.setDragEnabled(true);
 tree.getSelectionModel().setSelectionMode(TreeSelectionModel.
         DISCONTIGUOUS_TREE_SELECTION);
 
 DefaultTreeModel modelfortree = new DefaultTreeModel(customtreenode);
 tree.setModel(modelfortree);

 
  JScrollPane scrollpane = new JScrollPane(tree);
  scrollpane.setBounds(10, 50, 400,400);
  frame.getContentPane().add(scrollpane);
 
  
// second tree
  File[] roots2 = File.listRoots();
  CustomTreeNode customtreenode2 = new CustomTreeNode(roots2);
  tree2 = new JTree(customtreenode2);    
  tree2.setCellRenderer(new FileTreeCellRenderer());
  tree2.setModel(modelfortree);
  tree2.setRootVisible(false);
  
  tree2.setTransferHandler(new TransferHandler() {
	  @Override
     public boolean importData(TransferSupport support) {
       if (!canImport(support)) {
         return false;
       }
       JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
       TreePath path = dl.getPath();


       File destination = null;
       CustomTreeNode n = (CustomTreeNode)path.getLastPathComponent();
       
       if(tree2.getLeadSelectionPath()==null
       		&& tree.getLeadSelectionPath() ==  null) return false;
       

       if(((CustomTreeNode)tree2.getSelectionPath().getLastPathComponent())
		.file.getAbsolutePath().equals(n.file.getAbsolutePath())) {
	
    	   destination = n.file;
       						}


		
	TreePath[] treepath =	tree.getSelectionPaths();

String finish[] = new String[treepath.length];

	for(int  i =0 ; i <treepath.length; i++) {
	  
	if(i == 0)System.out.println("copying...");
	
	File source  =((CustomTreeNode)treepath[i].getLastPathComponent()).file;
	
	if(destination!= null) {
finish[i] = new Copying().copyFoldersAndContent(source, destination);
		
		}
	}
	
		
	String warrningsMessages ="";
		
	
			if(finish.length==1 && finish[0].equals("copied sucessfully")) {
			
				
				EventQueue.invokeLater(new Runnable() {
					
					public void run() {
					
						JOptionPane.showMessageDialog(XCopy.this.frame,"copied sucessfully");
					}
					
				});
				
				
				// switch on the sound of successful task				
	try (BufferedInputStream myStream = new BufferedInputStream(this.getClass().getResourceAsStream("xCopyComplete.wav"));
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(myStream)){ 
		
		Clip clip = AudioSystem.getClip();		    
					
					if(!clip.isOpen()) {
						clip.open(inputStream);
					}
					
					clip.start(); 
					     

					} catch (Exception e) {
					
						e.printStackTrace();
					}
			}else {
				
				for(String s : finish) {
					warrningsMessages += s + "\n";
				}
				
				String warrnings = warrningsMessages;	
				
				// display messages informing user that files have already been copied 
				EventQueue.invokeLater(new Runnable() {
				
		public void run() {
		
			JOptionPane.showMessageDialog(XCopy.this.frame, warrnings,
			"Warrning", JOptionPane.WARNING_MESSAGE);
			
						}
		});			
		
			}

	modelfortree.reload((CustomTreeNode)tree2.getSelectionPath().getLastPathComponent());
	 
	return true;
    }
	  
	  public boolean canImport(TransferSupport support) {
	        if (!support.isDrop()) {
	          return false;
	        } 
	        support.setShowDropLocation(true);
	        if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	          System.out.println("only string is supported");
	          return false;
	        }
	        JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
	        TreePath path = dl.getPath();
	        if (path == null) {
	          return false;
	        }
	        return true;
	      }
 });
 
  
  JScrollPane scrollpane2 = new JScrollPane(tree2);
  scrollpane2.setBounds(420, 50, 400,400);
   frame.getContentPane().add(scrollpane2);
   		
	JLabel lblDestination = new JLabel("Destination");
	lblDestination.setBounds(583, 11, 81, 14);
	frame.getContentPane().add(lblDestination);
	
	JLabel lblSource = new JLabel("Source");
	lblSource.setBounds(183, 11, 46, 14);
	frame.getContentPane().add(lblSource);
		
	}

}

class FileTreeModel  implements TreeModel{

	 File root; 
	 File file;	
	
	public FileTreeModel(File root) { 
		
		this.root = root;
		}

	@Override
	public Object getRoot() {
		
		return this.root;		
	}

	@Override
	public Object getChild(Object parent, int index) {
		String[] children = ((File)parent).list();
		if ((children == null) || (index >= children.length)) return null; 
		return new File((File) parent, children[index]);
		
	}

	@Override
	public int getChildCount(Object parent) {
		String[] children = ((File)parent).list();
		if (children == null) return 0;
		return children.length;
	
		
	}

	@Override
	public boolean isLeaf(Object node) {
	 return ((File)node).isFile(); 
		 
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		
		
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		String[] children = ((File)parent).list();
		if (children == null) return -1;
		String childname = ((File)child).getName();
		for(int i = 0; i < children.length; i++) 
		{
			if (childname.equals(children[i])) 
				return i; 
			} 
		return -1;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		
		
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		
		
	}
	
}

class CustomTreeNode implements TreeNode{
	
	 // Node file.
	  File file;
	
	 // Children of the node file.
	  File[] children;
	
	 // Parent node.
	  TreeNode parent;

	 // Indication whether this node corresponds to a file system root.
	  boolean isFileSystemRoot;

	// Creates a new file tree node.
	public CustomTreeNode(File file, boolean isFileSystemRoot, TreeNode parent) {
		this.file = file;
		this.isFileSystemRoot = isFileSystemRoot;
		this.parent = parent;
		this.children = this.file.listFiles();
		if (this.children == null)
			this.children = new File[0];
	}

	// Creates a new file tree node.
public CustomTreeNode(File[] children) {
		this.file = null;
		this.parent = null;
		this.children = children;
	}

	public Enumeration<?> children() {
		final int elementCount = this.children.length;
		return new Enumeration<File>() {
			int count = 0;

			public boolean hasMoreElements() {
				return this.count < elementCount;
			}

			public File nextElement() {
				if (this.count < elementCount) {
					return CustomTreeNode.this.children[this.count++];
				}
				throw new NoSuchElementException("Vector Enumeration");
			}
		};

	}

	public boolean getAllowsChildren() {
		return true;
	}

	public TreeNode getChildAt(int childIndex) {
		return new CustomTreeNode(this.children[childIndex],
				this.parent == null, this);
	}


	public int getChildCount() {
		return this.children.length;
	}

	public int getIndex(TreeNode node) {
		CustomTreeNode ftn = (CustomTreeNode) node;
		for (int i = 0; i < this.children.length; i++) {
			if (ftn.file.equals(this.children[i]))
				return i;
		}
		return -1;
	}

	public TreeNode getParent() {
		return this.parent;
	}

	
	public boolean isLeaf() {
		return (this.getChildCount() == 0);
	}
	
	public void setchildren(File [] children) {
		
		this.file = null;
		this.parent = null;
		this.children = children;
	}
	
	
}
class FileTreeCellRenderer extends DefaultTreeCellRenderer {
	
	
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
	
		CustomTreeNode cfile = (CustomTreeNode) value;
		File file = cfile.file; 
		String filename = "";
		if (file != null) {
			filename = XCopy.fsv.getSystemDisplayName(file);
				
			
		}
		
		JLabel result = (JLabel) super.getTreeCellRendererComponent(tree,
				filename, sel, expanded, leaf, row, hasFocus);
	
		if(file !=null) {
			
			 Icon icon = XCopy.fsv.getSystemIcon(file);
			
		result.setIcon(icon);
		}
		return result;
	}
}
