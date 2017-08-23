package fr.iambluedev.vulkan.rest;

import java.util.Map;

import de.jackwhite20.apex.rest.response.ApexResponse;
import de.jackwhite20.cobra.shared.Status;

public class ApexMapResponse extends ApexResponse {
	
	@SuppressWarnings("unused")
    private Map<?, ?> list;

    public ApexMapResponse(Status status, String message, Map<?, ?> list) {
        super(status, message);
        this.list = list;
    }
}
