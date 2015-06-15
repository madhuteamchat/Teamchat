package yammer.groups;

import java.util.List;

public class Group {
        public String name;
        public int id;
        public List<String> members; 
        public String description;
        public String url;
        
        public Group(String name,int id,List<String> members,String description,String url) { {
        	this.name=name;
        	this.id=id;
        	this.members=members;
        	this.description=description;
        	this.url=url;
        }
        	
        }
}
