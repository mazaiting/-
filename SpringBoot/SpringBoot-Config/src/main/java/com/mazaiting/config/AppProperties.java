package com.mazaiting.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
//prefix app, find app.* values
@ConfigurationProperties("app") 
public class AppProperties {
	private String error;
	private List<Menu> menus = new ArrayList<>();
	private Compiler compiler = new Compiler();
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public Compiler getCompiler() {
		return compiler;
	}

	public void setCompiler(Compiler compiler) {
		this.compiler = compiler;
	}

	public static class Menu {
		private String name;
		private String path;
		private String title;
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		@Override
		public String toString() {
			return "Menu [name=" + name + ", path=" + path + ", title=" + title + "]";
		}
	}
	
	public static class Compiler {
		private String timeout;
		private String outputFolder;
		public String getTimeout() {
			return timeout;
		}
		public void setTimeout(String timeout) {
			this.timeout = timeout;
		}
		public String getOutputFolder() {
			return outputFolder;
		}
		public void setOutputFolder(String outputFolder) {
			this.outputFolder = outputFolder;
		}
		@Override
		public String toString() {
			return "Compiler [timeout=" + timeout + ", outputFolder=" + outputFolder + "]";
		}
	}

	@Override
	public String toString() {
		return "AppProperties [error=" + error + ", menus=" + menus + ", compiler=" + compiler + "]";
	}
}
