module TheMapApp {
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires javafx.media;
	requires javafx.swing;
	requires javafx.web;
	requires junit;
	
	exports app;
	exports geometry;
	exports map;
	exports math;
	exports view2d;
}