package serpro;

public class ErrorResponse {
	private String chave="";
	private int responseCode=0;
	private String response="";
	
	public ErrorResponse()
	{
		
	}
	
	public ErrorResponse(String chave, int responseCode, String response)
	{
		this.chave=chave;
		this.responseCode=responseCode;
		this.response=response;
	}
	
	
	public String getChave() {
		return chave;
	}
	public void setChave(String chave) {
		this.chave = chave;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	
	
	

}
