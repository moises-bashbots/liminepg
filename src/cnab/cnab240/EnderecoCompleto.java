package cnab.cnab240;

public class EnderecoCompleto 
{
	private String logradouro="";
	private String numero="";
	private String complemento="";
	private String bairro="";	
	private String cidade="";
	private String cep="";
	private String complementoCep="";
	private String estado="";
	
	public EnderecoCompleto()
	{
		
	}
	
	public EnderecoCompleto(String logradouro, String numero, String complemento, String bairro, String cep, String complementoCep, String cidade, String estado)
	{
		this.logradouro=logradouro;
		this.numero=numero;
		this.complemento=complemento;
		this.bairro=bairro;
		this.cep=cep;
		this.complementoCep=complementoCep;
		this.cidade=cidade;
		this.estado=estado;
	}
	
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getComplementoCep() {
		return complementoCep;
	}
	public void setComplementoCep(String complementoCep) {
		this.complementoCep = complementoCep;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	

}
