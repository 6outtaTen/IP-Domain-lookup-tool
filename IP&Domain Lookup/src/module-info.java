module HelloFX {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	
	requires json.java;
	requires javafx.web;
	requires java.base;
	requires jdk.jshell;
	requires jdk.compiler;
	requires jdk.jconsole;
	requires java.desktop;
	
	opens application to javafx.graphics, javafx.fxml;
}
