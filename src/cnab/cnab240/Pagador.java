package cnab.cnab240;

public class Pagador 
{
	private String tipoDeInscricao="";
	private String numeroInscricao="";	
	private String nome="";
	private EnderecoCompleto endereco=new EnderecoCompleto();
	private DadosBancarios dadosBancarios = new DadosBancarios();
	public Pagador()
	{
		
	}
	
	public Pagador(String tipoDeInscricao, String numeroInscricao, String nome, EnderecoCompleto endereco, DadosBancarios dadosBancarios)
	{
		this.tipoDeInscricao=tipoDeInscricao;
		this.numeroInscricao=numeroInscricao;
		this.nome=nome;		
		this.endereco=endereco;
		this.dadosBancarios=dadosBancarios;
	}

	public String getTipoDeInscricao() {
		return tipoDeInscricao;
	}

	public void setTipoDeInscricao(String tipoDeInscricao) {
		this.tipoDeInscricao = tipoDeInscricao;
	}

	public String getNumeroInscricao() {
		return numeroInscricao;
	}

	public void setNumeroInscricao(String numeroInscricao) {
		this.numeroInscricao = numeroInscricao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public EnderecoCompleto getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoCompleto endereco) {
		this.endereco = endereco;
	}

	public DadosBancarios getDadosBancarios() {
		return dadosBancarios;
	}

	public void setDadosBancarios(DadosBancarios dadosBancarios) {
		this.dadosBancarios = dadosBancarios;
	}

	
	
	
}
