package fr.iambluedev.spartan.api.gson.parser;

import java.util.List;
import java.util.Map;

/**
 * Container factory for creating containers for JSON object and JSON array.
 * 
 * @see fr.bluekilleur.revocms.web.api.utils.libs.json.parser.JSONParser#parse(java.io.Reader, ContainerFactory)
 * 
 * @author FangYidong<fangyidong@yahoo.com.cn>
 */
public interface ContainerFactory {
	/**
	 * @return A Map instance to store JSON object, or null if you want to use fr.bluekilleur.revocms.web.api.utils.libs.json.JSONObject.
	 */
	@SuppressWarnings("rawtypes")
	Map createObjectContainer();
	
	/**
	 * @return A List instance to store JSON array, or null if you want to use fr.bluekilleur.revocms.web.api.utils.libs.json.JSONArray. 
	 */
	@SuppressWarnings("rawtypes")
	List creatArrayContainer();
}
