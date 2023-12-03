package vsb_cs_java.pong;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class Score implements Serializable
{
	private static final long serialVersionUID = 1L;
	//@XmlElement(name = "id")
	
	@XmlTransient
	private Integer id = null;
	@XmlElement(name = "pl1Score")
	private int pl1Score;
	@XmlElement(name = "pl1Name")
	private String pl1Name;
	@XmlElement(name = "pl2Score")
	private int pl2Score;
	@XmlElement(name = "pl2Score")
	private String pl2Name;
	
	
	@SuppressWarnings("unused")
  	private Score() {
 	}
	 
    public Score(String pl1Name, String pl2Name) {
        pl1Score = 0;
    	pl2Score = 0;
    	this.pl1Name = pl1Name;
    	this.pl2Name = pl2Name;
    }
    
    public Score(int id, String pl1Name,int pl1Score, String pl2Name, int pl2Score) {
    	this.pl1Score = pl1Score;
    	this.pl2Score = pl2Score;
    	this.pl1Name = pl1Name;
    	this.pl2Name = pl2Name;
    	this.id = id;
    }
    
    public Score(String pl1Name,int pl1Score, String pl2Name, int pl2Score) {
    	this.pl1Score = pl1Score;
    	this.pl2Score = pl2Score;
    	this.pl1Name = pl1Name;
    	this.pl2Name = pl2Name;
    }
    
    
    public void pl1Scored() {
    	pl1Score += 1;
    }
    
    public void pl2Scored() {
    	pl2Score += 1;
    }
    
    public String getPl1Name() {
    	return pl1Name;
    }
    
    public String getPl2Name() {
    	return pl2Name;
    }
    
    
    public int getPl1Score() {
    	return pl1Score;
    }
    
    public int getPl2Score() {
    	return pl2Score;
    }
    
    public void setScore(int p1, int p2) {
    	pl1Score += p1;
    	pl2Score += p2;
    }
    
    public int getLowerScore() {
    	if(pl1Score < pl2Score) {
    		return pl1Score;
    	}
    	return pl2Score;
    }

    public Integer getId() {
    	return id;
    }
    
    public void setId(Integer id) {
    	this.id = id;
    }
    
    @Override
	public String toString() {
		return pl1Name + ":" + pl1Score + " | " + pl2Name + ":" + pl2Score;
	}
}