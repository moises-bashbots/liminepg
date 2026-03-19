package pixsantander;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PagamentoPIXDadosBancariosSantander {
	private static SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
/*
 * 
		  {
		  "id": "fd119dc4-d48f-460a-b751-7f99c074831e",
		  "tags": [
		    "RH",
		    "123456"
		  ],
		  "remittanceInformation": "string",
		  "beneficiary": {
		    "branch": 1000,
		    "number": 10301293232123458000,
		    "type": "CONTA_CORRENTE",
		    "documentType": "CPF",
		    "documentNumber": 12345678909,
		    "name": "John Lennon",
		    "bankCode": 1234,
		    "ispb": 123456
		  },
		  "paymentValue": 100.99
		}
  */
	private double valorPagamento=0;
	private Date dataPagamento=null;
	private String ispb="";
	private String compe="";
	private String tipoIdentificacaoConta="";
	private String agenciaRecebedor="";
	private String contaRecebedor="";
	private String tipoIdentificacaoRecebedor="";
	private String identificacaoRecebedor="";
	private String informacoesEntreUsuarios="";
	private String referenciaEmpresa="";
	private String identificacaoComprovante="";
	private String txid="";
	private String pagador="";
	private String tipoConta="";
	private String agencia="";
	private String conta="";
	private String tipoPessoa="";
	private String documento="";
	private String moduloSispag="";
	
	public PagamentoPIXDadosBancariosSantander()
	{
		
	}
	
	public PagamentoPIXDadosBancariosSantander(
			double valorPagamento,
			Date dataPagamento,
			String compe,
			String ispb,
			String tipoIdentificacaoConta,
			String agenciaRecebedor,
			String contaRecebedor,
			String tipoIdentificacaoRecebedor,
			String identificacaoRecebedor,
			String informacoesEntreUsuarios,
			String referenciaEmpresa,
			String identificacaoComprovante,
			String tipoConta,
			String agencia,
			String conta,
			String tipoPessoa,
			String documento,
			String moduloSispag)
	{
//		valorPagamento, 
//		dataPagamento, 
//		compe, 
//		ispb, 
//		tipoIdentificacaoConta, 
//		agenciaRecebedor, 
//		contaRecebedor, 
//		tipoIdentificacaoRecebedor, 
//		identificacaoRecebedor, 
//		informacoesEntreUsuarios, 
//		referenciaEmpresa, 
//		identificacaoComprovante, 
//		tipoConta, 
//		agencia, 
//		conta, 
//		tipoPessoa, 
//		documento, 
//		moduloSispag);

		this.valorPagamento=valorPagamento;
		this.dataPagamento=dataPagamento;
		this.compe=compe;
		this.ispb=ispb;
		this.tipoIdentificacaoConta=tipoIdentificacaoConta;
		this.agenciaRecebedor=agenciaRecebedor;
		this.contaRecebedor=contaRecebedor;
		this.tipoIdentificacaoRecebedor=tipoIdentificacaoRecebedor;
		this.identificacaoRecebedor=identificacaoRecebedor;
		this.informacoesEntreUsuarios=informacoesEntreUsuarios;
		this.referenciaEmpresa=referenciaEmpresa;
		this.identificacaoComprovante=identificacaoComprovante;
		this.tipoConta=tipoConta;
		this.agencia=agencia;
		this.conta=conta;
		this.tipoPessoa=tipoPessoa;
		this.documento=documento;
		this.moduloSispag=moduloSispag;
	}
	
	public static void main(String[] args) 
	{
		double valorPagamento=123.40;
		Date dataPagamento=null;
		try {
			dataPagamento = sdf.parse("2020-07-13");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String compe="341";
		String ispb="60701190";
		String tipoIdentificacaoConta="CC";
		String agenciaRecebedor="1500";
		String contaRecebedor="00052012";
		String tipoIdentificacaoRecebedor="F";
		String identificacaoRecebedor="24361690000172";
		String informacoesEntreUsuarios="transferencia para o João - referente a manutencao";
		String referenciaEmpresa="154785966666666";
		String identificacaoComprovante="transferencia para o João - referente a manutencao";
		String txid="bqJJjaVAThmtnUeVLNnSXQ";
		String tipoConta="CC";
		String agencia="1500";
		String conta="00800550";
		String tipoPessoa="J";
		String documento="24361690000172";
		String moduloSispag="Fornecedores";
		
		PagamentoPIXDadosBancariosSantander pagamentoPIXDadosBancarios = new PagamentoPIXDadosBancariosSantander(valorPagamento, dataPagamento, compe, ispb, tipoIdentificacaoConta, agenciaRecebedor, contaRecebedor, tipoIdentificacaoRecebedor, identificacaoRecebedor, informacoesEntreUsuarios, referenciaEmpresa, identificacaoComprovante, tipoConta, agencia, conta, tipoPessoa, documento, moduloSispag);
			
		
		System.out.println(pagamentoPIXDadosBancarios.toJSON());
		
	}

	public String toJSON()
	{
		String documentType="";
		if(this.documento.length()==11)
		{
			documentType="CPF";
		}
		else
		{
			documentType="CNPJ";
		}
		String jsonString="{\n"
		+ "  \"tags\": [\n"
		+ "    \""+ this.referenciaEmpresa+ "\",\n"
		+ "    \""+ this.identificacaoComprovante+ "\"\n"
		+ "  ],\n"
		+ "  \"remittanceInformation\": \"string\",\n"
		+ "  \"beneficiary\": {\n"
		+ "    \"branch\": "+ ""
				+ this.agenciaRecebedor
				+ ""+ ",\n"
		+ "    \"number\": "+ ""
				+ this.contaRecebedor
				+ ""+ ",\n"
		+ "    \"type\": \"CONTA_CORRENTE\",\n"
		+ "    \"documentType\": \""+ documentType+ "\",\n"
		+ "    \"documentNumber\": "+ this.identificacaoRecebedor	+ ",\n"
		+ "    \"name\": \""+ this.identificacaoRecebedor+ "\",\n"
		+ "    \"bankCode\": "	+ this.compe	+ "\n"
		+ "  },\n"
		+ "  \"paymentValue\": "+ this.valorPagamento+ "\n"
		+ "}";
		
		jsonString="{\\n    \\\"tags\\\": [\\n        \\\"24361690000172\\\",\\n        \\\"Transferenciad\\\"\\n    ],\\n    \\\"remittanceInformation\\\": \\\"string\\\",\\n    \\\"beneficiary\\\": {\\n        \\\"branch\\\": 2000,\\n        \\\"number\\\": 771143,\\n        \\\"type\\\": \\\"CONTA_CORRENTE\\\",\\n        \\\"documentType\\\": \\\"CNPJ\\\",\\n        \\\"documentNumber\\\": 24361690000172,\\n        \\\"name\\\": \\\"Limine\\\",\\n        \\\"bankCode\\\": 341\\n    },\\n    \\\"paymentValue\\\": 1.01\\n}\\n";
		
//		String jsonOldString= "{" + 
//				"\"valor_pagamento\": \"" + this.valorPagamento+"\"," + 
//				"\"data_pagamento\": \""+sdf.format(dataPagamento)+"\"," + 
//				"\"ispb\": \""+this.ispb+"\"," + 
//				"\"tipo_identificacao_conta\": \""+this.tipoIdentificacaoConta+"\"," + 
//				"\"agencia_recebedor\": \""+this.agenciaRecebedor+"\"," + 
//				"\"conta_recebedor\": \""+this.contaRecebedor+"\"," + 
//				"\"tipo_de_identificacao_do_recebedor\": \""+this.tipoIdentificacaoRecebedor+"\"," + 
//				"\"identificacao_recebedor\": \""+this.identificacaoRecebedor+"\"," + 
//				"\"informacoes_entre_usuarios\": \""+this.informacoesEntreUsuarios+"\"," + 
//				"\"referencia_empresa\": \""+this.referenciaEmpresa+"\"," + 
//				"\"identificacao_comprovante\": \""+this.identificacaoComprovante+"\"," + 
//				"\"txid\": \""+this.txid+"\"," + 
//				"\"pagador\": {" + 
//				"\"tipo_conta\": \""+this.tipoConta+"\"," + 
//				"\"agencia\": \""+this.agencia+"\"," + 
//				"\"conta\": \""+this.conta+"\"," + 
//				"\"tipo_pessoa\": \""+this.tipoPessoa+"\"," + 
//				"\"documento\": \""+this.documento+"\"," + 
//				"\"modulo_sispag\": \""+this.moduloSispag+"\"" + 
//				"}" + 
//				"}";
//	            
	    return jsonString;
	}

	
	public String toBeautyJSON()
	{
		String jsonString= "{\n" + 
				"\"valor_pagamento\": \"" + this.valorPagamento+"\",\n" + 
				"\"data_pagamento\": \""+sdf.format(dataPagamento)+"\",\n" + 
				"\"ispb\": \""+this.ispb+"\",\n" + 
				"\"tipo_identificacao_conta\": \""+this.tipoIdentificacaoConta+"\",\n" + 
				"\"agencia_recebedor\": \""+this.agenciaRecebedor+"\",\n" + 
				"\"conta_recebedor\": \""+this.contaRecebedor+"\",\n" + 
				"\"tipo_de_identificacao_do_recebedor\": \""+this.tipoIdentificacaoRecebedor+"\",\n" + 
				"\"identificacao_recebedor\": \""+this.identificacaoRecebedor+"\",\n" + 
				"\"informacoes_entre_usuarios\": \""+this.informacoesEntreUsuarios+"\",\n" + 
				"\"referencia_empresa\": \""+this.referenciaEmpresa+"\",\n" + 
				"\"identificacao_comprovante\": \""+this.identificacaoComprovante+"\",\n" + 
				"\"txid\": \""+this.txid+"\",\n" + 
				"\"pagador\": {\n" + 
				"\"tipo_conta\": \""+this.tipoConta+"\",\n" + 
				"\"agencia\": \""+this.agencia+"\",\n" + 
				"\"conta\": \""+this.conta+"\",\n" + 
				"\"tipo_pessoa\": \""+this.tipoPessoa+"\",\n" + 
				"\"documento\": \""+this.documento+"\",\n" + 
				"\"modulo_sispag\": \""+this.moduloSispag+"\"\n" + 
				"}\n" + 
				"}";
	            
	    return jsonString;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		PagamentoPIXDadosBancariosSantander.sdf = sdf;
	}

	public double getValorPagamento() {
		return this.valorPagamento;
	}

	public void setValorPagamento(double valorPagamento) {
		this.valorPagamento = valorPagamento;
	}

	public Date getDataPagamento() {
		return this.dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getIspb() {
		return this.ispb;
	}

	public void setIspb(String ispb) {
		this.ispb = ispb;
	}

	public String getCompe() {
		return this.compe;
	}

	public void setCompe(String compe) {
		this.compe = compe;
	}

	public String getTipoIdentificacaoConta() {
		return this.tipoIdentificacaoConta;
	}

	public void setTipoIdentificacaoConta(String tipoIdentificacaoConta) {
		this.tipoIdentificacaoConta = tipoIdentificacaoConta;
	}

	public String getAgenciaRecebedor() {
		return this.agenciaRecebedor;
	}

	public void setAgenciaRecebedor(String agenciaRecebedor) {
		this.agenciaRecebedor = agenciaRecebedor;
	}

	public String getContaRecebedor() {
		return this.contaRecebedor;
	}

	public void setContaRecebedor(String contaRecebedor) {
		this.contaRecebedor = contaRecebedor;
	}

	public String getTipoIdentificacaoRecebedor() {
		return this.tipoIdentificacaoRecebedor;
	}

	public void setTipoIdentificacaoRecebedor(String tipoIdentificacaoRecebedor) {
		this.tipoIdentificacaoRecebedor = tipoIdentificacaoRecebedor;
	}

	public String getIdentificacaoRecebedor() {
		return this.identificacaoRecebedor;
	}

	public void setIdentificacaoRecebedor(String identificacaoRecebedor) {
		this.identificacaoRecebedor = identificacaoRecebedor;
	}

	public String getInformacoesEntreUsuarios() {
		return this.informacoesEntreUsuarios;
	}

	public void setInformacoesEntreUsuarios(String informacoesEntreUsuarios) {
		this.informacoesEntreUsuarios = informacoesEntreUsuarios;
	}

	public String getReferenciaEmpresa() {
		return this.referenciaEmpresa;
	}

	public void setReferenciaEmpresa(String referenciaEmpresa) {
		this.referenciaEmpresa = referenciaEmpresa;
	}

	public String getIdentificacaoComprovante() {
		return this.identificacaoComprovante;
	}

	public void setIdentificacaoComprovante(String identificacaoComprovante) {
		this.identificacaoComprovante = identificacaoComprovante;
	}

	public String getTxid() {
		return this.txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public String getPagador() {
		return this.pagador;
	}

	public void setPagador(String pagador) {
		this.pagador = pagador;
	}

	public String getTipoConta() {
		return this.tipoConta;
	}

	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getAgencia() {
		return this.agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getConta() {
		return this.conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public String getTipoPessoa() {
		return this.tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getDocumento() {
		return this.documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getModuloSispag() {
		return this.moduloSispag;
	}

	public void setModuloSispag(String moduloSispag) {
		this.moduloSispag = moduloSispag;
	}
	
}
