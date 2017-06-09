package fr.iambluedev.spartan.api.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.logging.Level;

import fr.iambluedev.spartan.api.gson.JSONObject;
import fr.iambluedev.spartan.api.gson.parser.JSONParser;
import fr.iambluedev.spartan.api.gson.parser.ParseException;

public abstract class SpartanConfig {

	private String name;
	private File file;
	private File folder;
	private JSONObject jsonObject;
	
	public SpartanConfig(String name){
		this.name = name;
		
		//this.instance.getLogger().log(Level.INFO, "Initialising " + this.getName() + " file");
		
		this.folder = new File("configs");
		this.file = new File(folder, this.name + ".json");
		
		if(!this.folder.exists()){
			this.folder.mkdir();
		}
		
		JSONParser jsonParser = new JSONParser();
		
		if(!this.file.exists()){
			this.jsonObject = new JSONObject();
			try {
				this.file.createNewFile();
				this.setupConfig();
			} catch (IOException e) {
				//this.instance.getLogger().log(Level.SEVERE, "Error when creating the " + this.getName() + " file");
				e.printStackTrace();
			}
		}else{
			try {
				this.jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(new FileInputStream(this.file)));
				//this.instance.getLogger().log(Level.INFO, this.getName() + " file succesfully loaded");
			} catch (IOException | ParseException e) {
				//this.instance.getLogger().log(Level.SEVERE, "Error when loading the " + this.getName() + " file");
				e.printStackTrace();
			}
		}
	}
	
	public abstract void setupConfig();

	public String getName() {
		return this.name;
	}

	public File getFile() {
		return this.file;
	}

	public File getFolder() {
		return this.folder;
	}

	public JSONObject getJsonObject() {
		return this.jsonObject;
	}
	
	public void save(){
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file), "utf-8"))) {
			writer.write(this.jsonObject.toJSONString());
			//this.instance.getLogger().log(Level.INFO, "File " + this.getName() + " successfully created");
		} catch (UnsupportedEncodingException e) {
			//this.instance.getLogger().log(Level.SEVERE, "Error when saving the " + this.getName() + " file");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			//this.instance.getLogger().log(Level.SEVERE, "Error when saving the " + this.getName() + " file");
			e.printStackTrace();
		} catch (IOException e) {
			//this.instance.getLogger().log(Level.SEVERE, "Error when saving the " + this.getName() + " file");
			e.printStackTrace();
		}
	}
}
