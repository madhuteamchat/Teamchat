package yammer;

public class UploadedFile {
  public String name;
  public String type;
  public String downloadurl;
  
  public UploadedFile(String name,String downloadurl,String type) {
	  this.name=name;
	  this.downloadurl=downloadurl;
	  this.type=type;
  }
  
}
