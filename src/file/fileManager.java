package file;

import java.io.File;

public class fileManager {
	
	public static void main(String[] arg0) {
		fileManager t = new fileManager();
		System.out.println(t.getDiskList());
		System.out.println(t.getDirectoryList("E:\\"));
	}
	public String getDiskList() {
		String result = "";
		File[] roots =  File.listRoots();
		for(int i=0;i<roots.length-1;i++) {
			result += roots[i].toString().substring(0,2)+",";
		}
		result += roots[roots.length-1].toString().substring(0,2);
		//System.out.println(result);
		return result;
	}
	
	public String getDirectoryList(String path) {
		System.out.println("客户端接收到的path: "+path);
		File file=new File(path);
		String result = "";
		 if(file.exists()){
			 if(file.isDirectory()){
				 File f[]=file.listFiles();
				 if(f!=null){
					 result = f[0].getName();
	                    for(int i=1;i<f.length;i++){
	                    	result += ","+f[i].getName();
	                    }
	                    //System.out.println("客户端执行结果: "+result);
	                }
			 }
			 else {
				 //System.out.println("不是目录");
				 //System.out.println(file);
				 return result;
			 }
		 }
		 else {
			 //System.out.println("不存在该目录");
			 return result;
		 }
		return result;
	}

}
