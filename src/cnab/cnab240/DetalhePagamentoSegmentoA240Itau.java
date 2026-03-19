package cnab.cnab240;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import cnab.FieldCNAB;
import cnab.GrupoRegistrosCNAB;
import cnab.RegistroCNAB;

public class DetalhePagamentoSegmentoA240Itau extends RegistroCNAB
{
	private static SimpleDateFormat sdf=new SimpleDateFormat("ddMMyyyy"); 
	
	private FieldCNAB banco=new FieldCNAB(1,1,3,3,"Codigo do Banco na compensacao","","N");
	private FieldCNAB lote=new FieldCNAB(2,4,7,4,"Lote de Servico","0000","N");
	private FieldCNAB registro=new FieldCNAB(3,8,8,1,"Tipo de Registro","3","N");
	
	private FieldCNAB numeroRegistro=new FieldCNAB(4,9,13,5,"Numero Sequencial do Registro no Lote","","N");
	private FieldCNAB segmento=new FieldCNAB(5,14,14,1,"Codigo de Segmento do Registro de Detalhe","A","A");
	private FieldCNAB tipoMovimento=new FieldCNAB(6,15,17,3,"Tipo de Movimento","","N");
//	private FieldCNAB codigoMovimento=new FieldCNAB(7,16,17,2,"Codigo da Instrucao para Movimento","","N");
	
	private FieldCNAB camara=new FieldCNAB(7,18,20,3,"Codigo da Camara Centralizadora","","N");
	private FieldCNAB bancoFavorecido=new FieldCNAB(8,21,23,3,"Codigo do Banco do Favorecido","","N");
	private FieldCNAB agenciaFavorecido=new FieldCNAB(9,24,28,5,"Agencia do Favorecido","","N");
	private FieldCNAB digitoVerificadorAgenciaFavorecido=new FieldCNAB(10,29,29,1,"Digito Verificador da Agencia do Favorecido","","A");
	private FieldCNAB contaFavorecido=new FieldCNAB(11,30,41,12,"Numero da Conta Corrente do Favorecido","","N");
	private FieldCNAB digitoVerificadorContaFavorecido=new FieldCNAB(12,42,42,1,"Digito Verificador da Conta Corrente Favorecido","","A");
	private FieldCNAB digitoVerificadorAgenciaContaFavorecido=new FieldCNAB(13,43,43,1,"Digito Verificador Agencia/Conta Favorecido","","A");
	
	private FieldCNAB nomeFavorecido=new FieldCNAB(14,44,73,30,"Nome do Favorecido","","A");
	
	private FieldCNAB seuNumero=new FieldCNAB(15,74,93,20,"Numero Documento Atribuido pela Empresa","","A");
	private FieldCNAB dataPagamento=new FieldCNAB(16,94,101,8,"Data do Pagamento","","N");
	private FieldCNAB tipoMoeda=new FieldCNAB(17,102,104,3,"Tipo da Moeda","009","A");
	private FieldCNAB quantidadeMoeda=new FieldCNAB(19,105,119,15,"Quantidade da Moeda","","N",5);
	private FieldCNAB valorPagamento=new FieldCNAB(20,120,134,15,"Valor do Pagamento","","N",2);
	private FieldCNAB nossoNumero=new FieldCNAB(21,135,154,20,"Numero Documento Atribuido pelo Banco","","A");
	private FieldCNAB dataReal=new FieldCNAB(22,155,162,8,"Data Real da Efetivacao do Pagamento","","N");
	private FieldCNAB valorReal=new FieldCNAB(23,163,177,15,"Valor Real da Efetivacao do Pagamento","","N",2);
	
	private FieldCNAB informacao2=new FieldCNAB(24,178,217,40,"Outras informacoes para identificacao de Deposito Judicial e Pagamento Salarios de servidores pelo SIAPE","","A");
	private FieldCNAB codigoFinalidadeDOC=new FieldCNAB(25,218,219,2,"Complemento Tipo Servico","","A");
	private FieldCNAB codigoFinalidadeTED=new FieldCNAB(26,220,224,5,"Codigo Finalidade da TED","","A");
	private FieldCNAB codigoFinalidadeComplementar=new FieldCNAB(27,225,226,2,"Complemento Finalidade de Pagamento","","A");
	private FieldCNAB cnab=new FieldCNAB(28,227,229,3,"Uso exclusivo FEBRABAN/CNAB","","A");
	private FieldCNAB aviso=new FieldCNAB(29,230,230,1,"Aviso ao Favorecido","","N");
	private FieldCNAB ocorrencias=new FieldCNAB(30,231,240,10,"Codigos de ocorrencias para retorno","","A");
	
	private PagamentoFornecedor pagamento=new PagamentoFornecedor();
	
	
	public DetalhePagamentoSegmentoA240Itau()
	{
		super();
		this.setSize(240);	
		this.setTipo("SegmentoA");
	}
	
	public DetalhePagamentoSegmentoA240Itau(PagamentoFornecedor pagamento, int iLote)
	{
		
		super();
		this.pagamento=pagamento;
		this.setSize(240);	
		this.setTipo("SegmentoA");
		
		this.getBanco().setContent(pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe());
		this.getLote().setContent(Integer.toString(iLote));
		this.getTipoMovimento().setContent("000");
		System.out.println();
		String codigoBancoPagador=pagamento.getPagador().getDadosBancarios().getDadosBanco().getCodigoCompe();
		String codigoBancoFavorecido=pagamento.getFavorecido().getDadosBancarios().getDadosBanco().getCodigoCompe();
		if(codigoBancoPagador.contains(codigoBancoFavorecido) || codigoBancoFavorecido.contains(codigoBancoPagador))
		{
			this.getCamara().setContent("000");
		}
		else
		{
			this.getCamara().setContent("018");
		}
		this.getBancoFavorecido().setContent(pagamento.getFavorecido().getDadosBancarios().getDadosBanco().getCodigoCompe());
		this.getAgenciaFavorecido().setContent(pagamento.getFavorecido().getDadosBancarios().getAgencia());
//		this.getDigitoVerificadorAgenciaFavorecido().setContent(pagamento.getFavorecido().getDadosBancarios().getDigitoAgencia());
		this.getContaFavorecido().setContent(pagamento.getFavorecido().getDadosBancarios().getNumeroConta()+pagamento.getFavorecido().getDadosBancarios().getDigitoConta());
//		this.getDigitoVerificadorContaFavorecido().setContent(pagamento.getFavorecido().getDadosBancarios().getDigitoConta());
		this.getDigitoVerificadorAgenciaContaFavorecido().setContent(pagamento.getFavorecido().getDadosBancarios().getDigitoAgenciaConta());
		this.getNomeFavorecido().setContent(pagamento.getFavorecido().getNome());
		if(!pagamento.isProblema())
		{
			this.getSeuNumero().setContent(pagamento.getCredito().getSeuNumero());
		}
		else
		{
			String seuNumeroModificado=pagamento.getCredito().getSeuNumero();
			if(pagamento.getTentativa()<10)
			{
				seuNumeroModificado=Integer.toString(pagamento.getTentativa())+seuNumeroModificado.substring(1,seuNumeroModificado.length());
			}
			this.getSeuNumero().setContent(seuNumeroModificado);
		}
		this.getDataPagamento().setContent(sdf.format(pagamento.getCredito().getDataPagamento()));
//		this.getTipoMoeda().setContent(pagamento.getCredito().getTipoMoeda());
		this.getQuantidadeMoeda().setContent(Integer.toString(pagamento.getCredito().getQuantidadeMoeda()));
		
//		Utils.doubleToCNABValue(this.getValorPagamento(), pagamento.getValorInteiro()/100.0);
		this.getValorPagamento().setContent(Long.toString(pagamento.getValorInteiro()));
		
		this.getNossoNumero().setContent(pagamento.getCredito().getNossoNumero());
		this.getDataReal().setContent(sdf.format(pagamento.getCredito().getDataReal()));
		
//		Utils.doubleToCNABValue(this.valorReal, pagamento.getValorInteiro()/100.0);
		this.getValorReal().setContent(Long.toString(pagamento.getValorInteiro()));
		
		this.getCodigoFinalidadeTED().setContent("00010"); //10 Credito em conta
		this.getCodigoFinalidadeComplementar().setContent("CC"); // CC Conta corrente / PP Conta Poupanca
		this.getAviso().setContent("0");
		
		this.buildSegmentoA();
	}
	
	public void buildSegmentoA()
	{
		HashMap<String,GrupoRegistrosCNAB> segmentosDetalhe = new HashMap<String,GrupoRegistrosCNAB>();
		GrupoRegistrosCNAB controle = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB servico = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB favorecido = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB credito = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB informacao2 = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB codigoFinalidadeDOC = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB codigoFinalidadeTED = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB codigoFinalidadeComplementar = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB cnab = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB aviso = new GrupoRegistrosCNAB();		
		GrupoRegistrosCNAB ocorrencias = new GrupoRegistrosCNAB();
		
		controle.getFieldsCNAB().put("banco",this.banco);
		controle.getFieldsCNAB().put("lote",this.lote);
		controle.getFieldsCNAB().put("registro",this.registro);
		servico.getFieldsCNAB().put("numeroRegistro", this.numeroRegistro);
		servico.getFieldsCNAB().put("segmento", this.segmento);
		servico.getFieldsCNAB().put("tipoMovimento", this.tipoMovimento);
		favorecido.getFieldsCNAB().put("camara", this.camara);
		favorecido.getFieldsCNAB().put("bancoFavorecido", this.bancoFavorecido);
		favorecido.getFieldsCNAB().put("agenciaFavorecido", this.agenciaFavorecido);
		favorecido.getFieldsCNAB().put("digitoVerificadorAgenciaFavorecido", this.digitoVerificadorAgenciaFavorecido);
		favorecido.getFieldsCNAB().put("contaFavorecido", this.contaFavorecido);
		favorecido.getFieldsCNAB().put("digitoVerificadorContaFavorecido", this.digitoVerificadorContaFavorecido);
		favorecido.getFieldsCNAB().put("digitoVerificadorAgenciaContaFavorecido", this.digitoVerificadorAgenciaContaFavorecido);
		favorecido.getFieldsCNAB().put("nomeFavorecido", this.nomeFavorecido);
		credito.getFieldsCNAB().put("seuNumero", this.seuNumero);
		credito.getFieldsCNAB().put("dataPagamento", this.dataPagamento);
		credito.getFieldsCNAB().put("tipoMoeda", this.tipoMoeda);
		credito.getFieldsCNAB().put("quantidadeMoeda", this.quantidadeMoeda);
		credito.getFieldsCNAB().put("valorPagamento", this.valorPagamento);
		credito.getFieldsCNAB().put("nossoNumero", this.nossoNumero);
		credito.getFieldsCNAB().put("dataReal", this.dataReal);
		credito.getFieldsCNAB().put("valorReal", this.valorReal);
		informacao2.getFieldsCNAB().put("informacao2", this.informacao2);
		codigoFinalidadeDOC.getFieldsCNAB().put("codigoFinalidadeDOC", this.codigoFinalidadeDOC);
		codigoFinalidadeTED.getFieldsCNAB().put("codigoFinalidadeTED", this.codigoFinalidadeTED);
		codigoFinalidadeComplementar.getFieldsCNAB().put("codigoFinalidadeComplementar", this.codigoFinalidadeComplementar);
		cnab.getFieldsCNAB().put("cnab", this.cnab);
		aviso.getFieldsCNAB().put("aviso", this.aviso);
		ocorrencias.getFieldsCNAB().put("ocorrencias", this.ocorrencias);
	
		segmentosDetalhe.put("controle", controle);
		segmentosDetalhe.put("servico", servico);
		segmentosDetalhe.put("favorecido", favorecido);
		segmentosDetalhe.put("credito", credito);
		segmentosDetalhe.put("informacao2", informacao2);
		segmentosDetalhe.put("codigoFinalidadeDOC", codigoFinalidadeDOC);
		segmentosDetalhe.put("codigoFinalidadeTED", codigoFinalidadeTED);
		segmentosDetalhe.put("codigoFinalidadeComplementar", codigoFinalidadeComplementar);
		segmentosDetalhe.put("cnab", cnab);
		segmentosDetalhe.put("aviso", aviso);
		segmentosDetalhe.put("ocorrencias", ocorrencias);
		
		this.segmentosCNAB=segmentosDetalhe;
	}

	public FieldCNAB getBanco() {
		return banco;
	}

	public void setBanco(FieldCNAB banco) {
		this.banco = banco;
	}

	public FieldCNAB getLote() {
		return lote;
	}

	public void setLote(FieldCNAB lote) {
		this.lote = lote;
	}

	public FieldCNAB getRegistro() {
		return registro;
	}

	public void setRegistro(FieldCNAB registro) {
		this.registro = registro;
	}

	public FieldCNAB getSegmento() {
		return segmento;
	}

	public void setSegmento(FieldCNAB segmento) {
		this.segmento = segmento;
	}

	public FieldCNAB getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(FieldCNAB tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	public FieldCNAB getCamara() {
		return camara;
	}

	public void setCamara(FieldCNAB camara) {
		this.camara = camara;
	}

	public FieldCNAB getBancoFavorecido() {
		return bancoFavorecido;
	}

	public void setBancoFavorecido(FieldCNAB bancoFavorecido) {
		this.bancoFavorecido = bancoFavorecido;
	}

	public FieldCNAB getAgenciaFavorecido() {
		return agenciaFavorecido;
	}

	public void setAgenciaFavorecido(FieldCNAB agenciaFavorecido) {
		this.agenciaFavorecido = agenciaFavorecido;
	}

	public FieldCNAB getDigitoVerificadorAgenciaFavorecido() {
		return digitoVerificadorAgenciaFavorecido;
	}

	public void setDigitoVerificadorAgenciaFavorecido(FieldCNAB digitoVerificadorAgenciaFavorecido) {
		this.digitoVerificadorAgenciaFavorecido = digitoVerificadorAgenciaFavorecido;
	}

	public FieldCNAB getContaFavorecido() {
		return contaFavorecido;
	}

	public void setContaFavorecido(FieldCNAB contaFavorecido) {
		this.contaFavorecido = contaFavorecido;
	}

	public FieldCNAB getDigitoVerificadorContaFavorecido() {
		return digitoVerificadorContaFavorecido;
	}

	public void setDigitoVerificadorContaFavorecido(FieldCNAB digitoVerificadorContaFavorecido) {
		this.digitoVerificadorContaFavorecido = digitoVerificadorContaFavorecido;
	}

	public FieldCNAB getDigitoVerificadorAgenciaContaFavorecido() {
		return digitoVerificadorAgenciaContaFavorecido;
	}

	public void setDigitoVerificadorAgenciaContaFavorecido(FieldCNAB digitoVerificadorAgenciaContaFavorecido) {
		this.digitoVerificadorAgenciaContaFavorecido = digitoVerificadorAgenciaContaFavorecido;
	}

	public FieldCNAB getNomeFavorecido() {
		return nomeFavorecido;
	}

	public void setNomeFavorecido(FieldCNAB nomeFavorecido) {
		this.nomeFavorecido = nomeFavorecido;
	}

	public FieldCNAB getSeuNumero() {
		return seuNumero;
	}

	public void setSeuNumero(FieldCNAB seuNumero) {
		this.seuNumero = seuNumero;
	}

	public FieldCNAB getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(FieldCNAB dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public FieldCNAB getTipoMoeda() {
		return tipoMoeda;
	}

	public void setTipoMoeda(FieldCNAB tipoMoeda) {
		this.tipoMoeda = tipoMoeda;
	}

	public FieldCNAB getQuantidadeMoeda() {
		return quantidadeMoeda;
	}

	public void setQuantidadeMoeda(FieldCNAB quantidadeMoeda) {
		this.quantidadeMoeda = quantidadeMoeda;
	}

	public FieldCNAB getValorPagamento() {
		return valorPagamento;
	}

	public void setValorPagamento(FieldCNAB valorPagamento) {
		this.valorPagamento = valorPagamento;
	}

	public FieldCNAB getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(FieldCNAB nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public FieldCNAB getDataReal() {
		return dataReal;
	}

	public void setDataReal(FieldCNAB dataReal) {
		this.dataReal = dataReal;
	}

	public FieldCNAB getValorReal() {
		return valorReal;
	}

	public void setValorReal(FieldCNAB valorReal) {
		this.valorReal = valorReal;
	}

	public FieldCNAB getInformacao2() {
		return informacao2;
	}

	public void setInformacao2(FieldCNAB informacao2) {
		this.informacao2 = informacao2;
	}

	public FieldCNAB getCodigoFinalidadeDOC() {
		return codigoFinalidadeDOC;
	}

	public void setCodigoFinalidadeDOC(FieldCNAB codigoFinalidadeDOC) {
		this.codigoFinalidadeDOC = codigoFinalidadeDOC;
	}

	public FieldCNAB getCodigoFinalidadeTED() {
		return codigoFinalidadeTED;
	}

	public void setCodigoFinalidadeTED(FieldCNAB codigoFinalidadeTED) {
		this.codigoFinalidadeTED = codigoFinalidadeTED;
	}

	public FieldCNAB getCodigoFinalidadeComplementar() {
		return codigoFinalidadeComplementar;
	}

	public void setCodigoFinalidadeComplementar(FieldCNAB codigoFinalidadeComplementar) {
		this.codigoFinalidadeComplementar = codigoFinalidadeComplementar;
	}

	public FieldCNAB getCnab() {
		return cnab;
	}

	public void setCnab(FieldCNAB cnab) {
		this.cnab = cnab;
	}

	public FieldCNAB getAviso() {
		return aviso;
	}

	public void setAviso(FieldCNAB aviso) {
		this.aviso = aviso;
	}

	public FieldCNAB getOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(FieldCNAB ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

	public FieldCNAB getNumeroRegistro() {
		return numeroRegistro;
	}

	public void setNumeroRegistro(FieldCNAB numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		DetalhePagamentoSegmentoA240Itau.sdf = sdf;
	}

	public PagamentoFornecedor getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoFornecedor pagamento) {
		this.pagamento = pagamento;
	}

}
