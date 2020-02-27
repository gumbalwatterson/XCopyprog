package com.gumbal;



import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

public class Copying {

	 ArrayList<String> list;
	 
	 public Copying(ArrayList<String> list){
		this.list = list;
	}
	
	public Copying(){
		
	}
	
	
	
	public String copyFoldersAndContent(File source , File dest) {
		
		// if we copy directory and files under that directory
		if(source.isDirectory()) {
		File dest2 = new File (dest.getPath()+"/"+source.getName());
		if(!dest2.exists()) {
		dest2.mkdir();
		subfolder(source, dest2);
		
		}else {
	//System.out.println("folder in that directory already exist");
	return "the folder "+ dest2.getName() +" in that directory already exist";	
	//throw new RuntimeException("folder in that directory already exist"); 
		
			}
		}
		else if(source.isFile()){
		
			try {
				// in copying the destination path must contain at the end of its path
				// the same file name which we want to copied from source
				
				String nameoffile = source.getName();
				
				//String filesource =	source.getPath();
				//File fsource = new File(filesource);
				
				String filedestination  = dest.getPath();
				filedestination = filedestination+"/"+nameoffile;
				File fdestination =new File(filedestination);
				
				Files.copy(source.toPath(), fdestination.toPath());
				
				
		
			}catch(FileAlreadyExistsException e) {
	//System.out.println("the file: " + source.getAbsolutePath() + " was already copied");
			return 	"the file: " + source.getAbsolutePath() + " was already copied";
			}
				catch(IOException e) {
					e.printStackTrace();
				}						
		}
	return "copied sucessfully"; 
	}
	
	
	private  Path subfolder(File source , File dest )  {
		
		
		
		// if file not copied
		if(source.list()!= null) {
				
		List<String> listoffolders = Arrays.asList(source.list());
		
		for(String s1 : listoffolders ) {

		if(source.isDirectory()) {
		
			try {
		
				
				Files.copy(new File(source.getPath()+"/"+s1).toPath()
					  ,new File(dest.getPath()+"/"+s1).toPath());
				
			
			} catch (IOException e) {
			
				
				e.printStackTrace();
			}
						
		subfolder(new File(source.getAbsolutePath()+"/"+s1) ,
				  new File(dest.getAbsolutePath()+"/"+ s1));
		}
		else {	
		
		try {
		String filesource =	source.getPath()+"/"+s1;
		File fsource =new File(filesource);
		
		String filedestination  = dest.getPath() + "/" + s1;
		File fdestination =new File(filedestination);
		
		Files.copy(fsource.toPath(), fdestination.toPath());
		}catch(FileAlreadyExistsException e) {
			System.out.println("the file: " + s1 + " was already copied");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
			}
		}// end of for each loop

	} // end of checking if copied file
		
	return source.toPath();
	
	}
		
}
