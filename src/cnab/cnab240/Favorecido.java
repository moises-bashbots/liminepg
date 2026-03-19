package cnab.cnab240;

import java.util.Date;

public class Favorecido 
{
	private String tipoDeInscricao="";
	private String numeroInscricao="";	
	private String nome="";
	private DadosBancarios dadosBancarios = new DadosBancarios();
	private EnderecoCompleto endereco = new EnderecoCompleto();
	private String chavePIX="";
	private String tipoChavePIX="";
	private String numeroBoletoDARF="";
	private Date dataVencimento=null;
	private boolean cadastrar=false;
	
	public Favorecido()
	{
		
	}
	
	public Favorecido(String tipoDeInscricao, String numeroInscricao, String nome, DadosBancarios dadosBancarios, EnderecoCompleto endereco)
	{
		this.tipoDeInscricao=tipoDeInscricao;
		this.numeroInscricao=numeroInscricao;
		this.nome=nome;
		this.dadosBancarios=dadosBancarios;
		this.endereco=endereco;
	}
	
	public Favorecido(String tipoDeInscricao, String numeroInscricao, String nome, DadosBancarios dadosBancarios, EnderecoCompleto endereco, String chavePIX)
	{
		this.tipoDeInscricao=tipoDeInscricao;
		this.numeroInscricao=numeroInscricao;
		this.nome=nome;
		this.dadosBancarios=dadosBancarios;
		this.endereco=endereco;
		this.chavePIX=chavePIX;
		classificarChavePIX();
	}
	
	public Favorecido(String tipoDeInscricao, String numeroInscricao, String nome, DadosBancarios dadosBancarios, EnderecoCompleto endereco, String chavePIX, String numeroBoleto, Date dataVencimento)
	{
		this.tipoDeInscricao=tipoDeInscricao;
		this.numeroInscricao=numeroInscricao;
		this.nome=nome;
		this.dadosBancarios=dadosBancarios;
		this.endereco=endereco;
		this.chavePIX=chavePIX;
		this.numeroBoletoDARF=numeroBoleto;
		this.dataVencimento=dataVencimento;
		classificarChavePIX();
	}
	
	public Favorecido(String tipoDeInscricao, String numeroInscricao, String nome, DadosBancarios dadosBancarios, EnderecoCompleto endereco, boolean cadastrar)
	{
		this.tipoDeInscricao=tipoDeInscricao;
		this.numeroInscricao=numeroInscricao;
		this.nome=nome;
		this.dadosBancarios=dadosBancarios;
		this.endereco=endereco;
		this.cadastrar=cadastrar;
	}
	
	public Favorecido(String tipoDeInscricao, String numeroInscricao, String nome, DadosBancarios dadosBancarios, EnderecoCompleto endereco, boolean cadastrar, String chavePIX, String numeroBoleto, Date dataVencimento)
	{
		this.tipoDeInscricao=tipoDeInscricao;
		this.numeroInscricao=numeroInscricao;
		this.nome=nome;
		this.dadosBancarios=dadosBancarios;
		this.endereco=endereco;
		this.cadastrar=cadastrar;
		this.chavePIX=chavePIX;	
		this.numeroBoletoDARF=numeroBoleto;
		this.dataVencimento=dataVencimento;
		classificarChavePIX();
	}
	
	public Favorecido(String tipoDeInscricao, String numeroInscricao, String nome, DadosBancarios dadosBancarios, EnderecoCompleto endereco, boolean cadastrar, String chavePIX)
	{
		this.tipoDeInscricao=tipoDeInscricao;
		this.numeroInscricao=numeroInscricao;
		this.nome=nome;
		this.dadosBancarios=dadosBancarios;
		this.endereco=endereco;
		this.cadastrar=cadastrar;
		this.chavePIX=chavePIX;	
		classificarChavePIX();
	}
	
	public void classificarChavePIX()
	{
		if(this.chavePIX!=null)
		{
			if(this.chavePIX.length()>0)
			{
				if(this.chavePIX.startsWith("+"))
				{
					this.tipoChavePIX="telefone";
				}
				else if(this.chavePIX.contains("@"))
				{
					this.tipoChavePIX="email";
				}
				else if(this.chavePIX.matches("[0-9]{11}") || this.chavePIX.matches("[0-9]{14}"))
				{
					this.tipoChavePIX="cpfcnpj";
				}
				else if(this.chavePIX.length()==36)
				{
					this.tipoChavePIX="aleatoria";
				}
			}
		}
		else
		{
			this.chavePIX="";
		}
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
	public DadosBancarios getDadosBancarios() {
		return dadosBancarios;
	}
	public void setDadosBancarios(DadosBancarios dadosBancarios) {
		this.dadosBancarios = dadosBancarios;
	}
	public EnderecoCompleto getEndereco() {
		return endereco;
	}
	public void setEndereco(EnderecoCompleto endereco) {
		this.endereco = endereco;
	}

	public boolean isCadastrar() {
		return cadastrar;
	}

	public void setCadastrar(boolean cadastrar) {
		this.cadastrar = cadastrar;
	}

	public String getChavePIX() {
		return this.chavePIX;
	}

	public void setChavePIX(String chavePIX) {
		this.chavePIX = chavePIX;
	}

	public String getTipoChavePIX() {
		return this.tipoChavePIX;
	}

	public void setTipoChavePIX(String tipoChavePIX) {
		this.tipoChavePIX = tipoChavePIX;
	}

	public String getNumeroBoletoDARF() {
		return this.numeroBoletoDARF;
	}

	public void setNumeroBoletoDARF(String numeroBoleto) {
		this.numeroBoletoDARF = numeroBoleto;
	}

	public Date getDataVencimento() {
		return this.dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
}
