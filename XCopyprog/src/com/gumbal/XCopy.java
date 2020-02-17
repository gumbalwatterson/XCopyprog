package com.gumbal;



import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;





public class XCopy implements TreeSelectionListener {
	protected static FileSystemView fsv = FileSystemView.getFileSystemView();
	private JFrame frame;
private JButton btnCopy;
private   String pathfortree;
private String pathfortree2;
private JButton btnaddextension;
private JTextArea text;
private Copying copying;
private JTree tree;
private JTree tree2; 
private boolean extensionadded = false; 
/**
	 * Launch the application.
	 */
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

	/**
	 * Create the application.
	 */
	public XCopy() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		copying = new Copying();
		
		// first tree
 File[] roots = File.listRoots();
 CustomTreeNode customtreenode = new CustomTreeNode(roots);
 tree = new JTree(customtreenode); 
 tree.setCellRenderer(new FileTreeCellRenderer());
 tree.addTreeSelectionListener(this);
 tree.setRootVisible(false);
 
  JScrollPane scrollpane = new JScrollPane(tree);
  scrollpane.setBounds(10, 10, 400,400);
  frame.getContentPane().add(scrollpane);
  	
// second tree
  File[] roots2 = File.listRoots();
  CustomTreeNode customtreenode2 = new CustomTreeNode(roots2);
  tree2 = new JTree(customtreenode2); 
  tree2.setCellRenderer(new FileTreeCellRenderer());
  
  tree2.addTreeSelectionListener(this); /**/
  
  tree2.setRootVisible(false);
  
  JScrollPane scrollpane2 = new JScrollPane(tree2);
  scrollpane2.setBounds(420, 10, 400,400);
   frame.getContentPane().add(scrollpane2);
   	
   
   //button for copying
   
	btnCopy = new JButton("copy");
	btnCopy.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		
		// code for copy
	if(pathfortree != null && !tree2.isSelectionEmpty() ) {	
		
			String s =pathfortree; 
			String s2  = pathfortree2;
			
			File source = new File(s);
			File dest = new File(s2);
		System.out.println("source " + source);
		System.out.println("destination " + dest);
			copying.copyFoldersAndContent(source, dest);
	
	}

		}
	});
	
	btnCopy.setBounds(250, 449, 89, 23);
	btnCopy.setEnabled(false);
	frame.getContentPane().add(btnCopy);
	
	
	//extension button
	btnaddextension = new JButton("add extensions");
	btnaddextension.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		String ext;
		if(XCopy.this.text.getText() != null && !XCopy.this.text.getText().isEmpty() ) {

			ext = XCopy.this.text.getText();
			String []str =ext.split(",");
			
			ArrayList<String> extensionlist = new ArrayList<>();
			for(int i = 0 ; i<str.length ; i++) {
			extensionlist.add(str[i]);
			}
			extensionadded = copying.setExtensions(extensionlist);
		}
	

		}
	});
	
	btnaddextension.setBounds(370, 449, 140, 23);
	frame.getContentPane().add(btnaddextension);

	
	// textArea
  text = new JTextArea();
  text.setBounds(370 , 480 , 200 , 150);
  text.setLineWrap(true);
  text.setWrapStyleWord(true);
  text.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) { 
          if(text.getText().length() == 0)
              btnCopy.setEnabled(false);
          else
          {
              btnCopy.setEnabled(true);
          }
      }
});
  JScrollPane scrollpane3 = new JScrollPane(text);
  scrollpane3.setBounds(370, 480, 200,150);
  scrollpane3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
  frame.getContentPane().add(scrollpane3);
  
	
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
System.out.println(extensionadded);

		if(e.getNewLeadSelectionPath() == null)return;
		
		if(	tree ==e.getSource()) {
			CustomTreeNode node =(CustomTreeNode)e.getPath().getLastPathComponent();
			setpathForTree(node.file.getAbsolutePath());
			
		
		}else {

		
		CustomTreeNode node =(CustomTreeNode)e.getPath().getLastPathComponent();
		setpathForTree2(node.file.getAbsolutePath());
		}		
		
	}
		
		public void setpathForTree(String path) {
			pathfortree = path;
		System.out.println(pathfortree);
		}
		
		public void setpathForTree2(String path) {
			pathfortree2 = path;
			System.out.println(pathfortree2);
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
		
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
	
}
class FileTreeCellRenderer extends DefaultTreeCellRenderer {
	
	private Map<String, Icon> iconCache = new HashMap<String, Icon>();
	
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
		Icon icon = this.iconCache.get(filename);
			if (icon == null) {
		
				icon = XCopy.fsv.getSystemIcon(file);
				this.iconCache.put(filename,icon);
			}
			result.setIcon(icon);
		}
		return result;
	}
}

