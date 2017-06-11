package fr.iambluedev.vulkan.util;

import java.util.List;

import de.jackwhite20.apex.util.BackendInfo;

public class MapUtil {

	public static List<BackendInfo> addAndReturn(List<BackendInfo> list, BackendInfo value){
		list.add(value);
		return list;
	}
	
}
