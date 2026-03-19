package elegibilidade;

public class AcumuladorValorNominal {
	private double valorNominalTotal=0;
	private String nome="";
	private boolean coobrigacao=true;
	
	public AcumuladorValorNominal(double valorNominal, String nome, boolean coobrigacao)
	{
		this.valorNominalTotal=this.valorNominalTotal+valorNominal;
		this.nome = nome;
		this.coobrigacao=coobrigacao;
	}
	
	public AcumuladorValorNominal(double valorNominal, String nome)
	{
		this.valorNominalTotal=this.valorNominalTotal+valorNominal;
		this.nome = nome;
	}

	public double getValorNominalTotal() {
		return valorNominalTotal;
	}

	public void setValorNominalTotal(double valorNominalTotal) {
		this.valorNominalTotal = this.valorNominalTotal+valorNominalTotal;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isCoobrigacao() {
		return coobrigacao;
	}

	public void setCoobrigacao(boolean coobrigacao) {
		this.coobrigacao = coobrigacao;
	}

}
