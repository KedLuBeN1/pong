module pong {
	requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens vsb_cs_java.pong to javafx.fxml , jakarta.xml.bind;
    exports vsb_cs_java.pong;
    requires jakarta.xml.bind; 
    requires org.eclipse.persistence.moxy;
    requires jakarta.json;
    requires com.sun.tools.xjc;
}