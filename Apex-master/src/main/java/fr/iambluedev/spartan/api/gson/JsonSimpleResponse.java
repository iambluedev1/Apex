package fr.iambluedev.spartan.api.gson;

public class JsonSimpleResponse {

	private boolean status = true;
	private String result = "no result found";
	
	public JsonSimpleResponse(boolean status, String result){
		this.setStatus(status);
		this.setResult(result);
	}
	
	public JsonSimpleResponse(boolean status){
		this.setStatus(status);
	}
	
	public JsonSimpleResponse(String result){
		this.setResult(result);
	}

	public boolean getStatus() {
		return this.status;
	}

	public String getResult() {
		return this.result;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@SuppressWarnings("unchecked")
	public String toJson(){
		JSONObject obj = new JSONObject();
		obj.put("status", (this.getStatus()) ? "success" : "error");
		obj.put("message", this.getResult());
		return obj.toJSONString();
	}
}

