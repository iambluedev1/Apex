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

import de.jackwhite20.apex.Apex;
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
		
		Apex.getLogger().info("Initialising " + this.getName() + " file");
		
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
				Apex.getLogger().error("Error when creating the " + this.getName() + " file");
				e.printStackTrace();
			}
		}else{
			try {
				this.jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(new FileInputStream(this.file)));
				Apex.getLogger().info(this.getName() + " file succesfully loaded");
			} catch (IOException | ParseException e) {
				Apex.getLogger().error("Error when loading the " + this.getName() + " file");
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
			Apex.getLogger().info("File " + this.getName() + " successfully updated");
		} catch (UnsupportedEncodingException e) {
			Apex.getLogger().error("Error when saving the " + this.getName() + " file");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			Apex.getLogger().error("Error when saving the " + this.getName() + " file");
			e.printStackTrace();
		} catch (IOException e) {
			Apex.getLogger().error("Error when saving the " + this.getName() + " file");
			e.printStackTrace();
		}
	}
}
