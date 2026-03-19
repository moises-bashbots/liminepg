package cnab.cnab240;

import java.util.Calendar;
import java.util.Date;

public class Credito 
{
	private String seuNumero="";
	private Date dataPagamento=Calendar.getInstance().getTime();
	private String tipoMoeda="";
	private int quantidadeMoeda=0;
	private double valorPagamento=0;
	private String nossoNumero="";
	private Date dataReal=Calendar.getInstance().getTime();
	private double valorReal=0;
	private long valorInteiro=0;
	
	public Credito()
	{
		
	}
	
	public Credito(String seuNumero, Date dataPagamento, String tipoMoeda, int quantidadeMoeda, double valorPagamento, String nossoNumero, Date dataReal, double valorReal)
	{
		this.seuNumero=seuNumero;
		this.dataPagamento=dataPagamento;
		this.tipoMoeda=tipoMoeda;
		this.quantidadeMoeda=quantidadeMoeda;
		this.valorPagamento=valorPagamento;
		this.nossoNumero=nossoNumero;
		this.dataReal=dataReal;
		this.valorReal=valorReal;
		this.valorInteiro=(long)(this.valorPagamento*100.0);
	}
	
	
	public String getSeuNumero() {
		return seuNumero;
	}
	public void setSeuNumero(String seuNumero) {
		this.seuNumero = seuNumero;
	}
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	public String getTipoMoeda() {
		return tipoMoeda;
	}
	public void setTipoMoeda(String tipoMoeda) {
		this.tipoMoeda = tipoMoeda;
	}
	public int getQuantidadeMoeda() {
		return quantidadeMoeda;
	}
	public void setQuantidadeMoeda(int quantidadeMoeda) {
		this.quantidadeMoeda = quantidadeMoeda;
	}
	public double getValorPagamento() {
		return valorPagamento;
	}
	public void setValorPagamento(double valorPagamento) {
		this.valorPagamento = valorPagamento;
	}
	public String getNossoNumero() {
		return nossoNumero;
	}
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}
	public Date getDataReal() {
		return dataReal;
	}
	public void setDataReal(Date dataReal) {
		this.dataReal = dataReal;
	}
	public double getValorReal() {
		return valorReal;
	}
	public void setValorReal(double valorReal) {
		this.valorReal = valorReal;
	}

	public long getValorInteiro() {
		return valorInteiro;
	}

	public void setValorInteiro(long valorInteiro) {
		this.valorInteiro = valorInteiro;
	}
	
	

}
