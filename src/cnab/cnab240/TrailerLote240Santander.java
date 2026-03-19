package cnab.cnab240;

import java.util.ArrayList;
import java.util.HashMap;

import cnab.FieldCNAB;
import cnab.GrupoRegistrosCNAB;
import cnab.RegistroCNAB;
import utils.Utils;

public class TrailerLote240Santander extends RegistroCNAB 
{
	private FieldCNAB banco=new FieldCNAB(1,1,3,3,"Codigo do Banco na compensacao","","N");
	private FieldCNAB lote=new FieldCNAB(2,4,7,4,"Lote de Servico","","N");
	private FieldCNAB registro=new FieldCNAB(3,8,8,1,"Tipo de Registro","5","N");
	private FieldCNAB cnab0=new FieldCNAB(4,9,17,9,"Uso exclusivo FEBRABAN/CNAB","","A");	
	private FieldCNAB quantidadeRegistros=new FieldCNAB(5,18,23,6,"Quantidade de Registros do Lote","0","N");
	private FieldCNAB valor=new FieldCNAB(6,24,41,18,"Somatoria de Valores","0","N",2);
	private FieldCNAB quantidadeMoedas=new FieldCNAB(7,42,59,18,"Somatoria de Quantidade de Moedas","0","N",5);
	private FieldCNAB numeroAvisoDebito=new FieldCNAB(8,60,65,6,"Numero Aviso de Debito","0","N");
	private FieldCNAB cnab1=new FieldCNAB(9,66,230,165,"Uso Exclusivo FEBRABAN/CNAB","","A");
	private FieldCNAB ocorrencias=new FieldCNAB(10,231,240,10,"Codigos das ocorrencias para retorno","","A");
	
	public TrailerLote240Santander()
	{
		super();
		this.setSize(240);
		this.setTipo("TrailerLote240");
	}
	
	public TrailerLote240Santander(Pagador pagador, ArrayList<DetalhePagamentoAB240> detalhes, int iLote)
	{
		super();
		this.setSize(240);
		this.setTipo("TrailerLote240");
		this.getBanco().setContent(pagador.getDadosBancarios().getDadosBanco().getCodigoCompe());
		this.getLote().setContent(Integer.toString(iLote));
		this.getQuantidadeRegistros().setContent(Integer.toString(detalhes.size()*2+2));
		long valorTotalInteiro=0;
		double valorTotal=0;
		double quantidadeTotalMoedas=0;
		double quantidadeTotalRegistros=2;
		
		for(DetalhePagamentoAB240 detalhe:detalhes)
		{
			valorTotalInteiro+=detalhe.getDetalheASantander().getPagamento().getValorInteiro();
			valorTotalInteiro+=Integer.parseInt(detalhe.getDetalheJSantander().getValorPagamento().getContent());
			valorTotalInteiro+=Integer.parseInt(detalhe.getDetalheOSantander().getValorTotalDoPagamento().getContent());
			quantidadeTotalMoedas+=Utils.doubleFromCNABValue(detalhe.getDetalheASantander().getQuantidadeMoeda());
			quantidadeTotalRegistros+=detalhe.getNumberRegistrosDetalhes();
		}
		
		this.getValor().setContent(Long.toString(valorTotalInteiro));
//		Utils.doubleToCNABValue(this.getValor(), valorTotal);
		Utils.doubleToCNABValue(this.getQuantidadeMoedas(), quantidadeTotalMoedas);
		Utils.doubleToCNABValue(this.getQuantidadeRegistros(), quantidadeTotalRegistros);
		this.buildTrailer();
	}
	
	public void buildTrailer()
	{
		HashMap<String,GrupoRegistrosCNAB> segmentosTrailer = new HashMap<String,GrupoRegistrosCNAB>();
		GrupoRegistrosCNAB controle = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB cnab0 = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB totais = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB numeroAvisoDebito = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB cnab1 = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB ocorrencias = new GrupoRegistrosCNAB();
		
		controle.getFieldsCNAB().put("banco",this.banco);
		controle.getFieldsCNAB().put("lote",this.lote);
		controle.getFieldsCNAB().put("registro",this.registro);
		
		cnab0.getFieldsCNAB().put("cnab0",this.cnab0);
		
		totais.getFieldsCNAB().put("quantidadeRegistros", this.quantidadeRegistros);
		totais.getFieldsCNAB().put("valor", this.valor);
		totais.getFieldsCNAB().put("quantidadeMoedas", this.quantidadeMoedas);
		
		numeroAvisoDebito.getFieldsCNAB().put("numeroAvisoDebito", this.numeroAvisoDebito);
		
		cnab1.getFieldsCNAB().put("cnab1",this.cnab1);
		
		ocorrencias.getFieldsCNAB().put("ocorrencias",this.ocorrencias);
		
		segmentosTrailer.put("controle", controle);
		segmentosTrailer.put("cnab0", cnab0);
		segmentosTrailer.put("totais", totais);
		segmentosTrailer.put("numeroAvisoDebito", numeroAvisoDebito);
		segmentosTrailer.put("cnab1", cnab1);
		segmentosTrailer.put("ocorrencias", ocorrencias);
		
		this.segmentosCNAB=segmentosTrailer;
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

	public FieldCNAB getCnab0() {
		return cnab0;
	}

	public void setCnab0(FieldCNAB cnab0) {
		this.cnab0 = cnab0;
	}

	public FieldCNAB getQuantidadeRegistros() {
		return quantidadeRegistros;
	}

	public void setQuantidadeRegistros(FieldCNAB quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}

	public FieldCNAB getValor() {
		return valor;
	}

	public void setValor(FieldCNAB valor) {
		this.valor = valor;
	}

	public FieldCNAB getQuantidadeMoedas() {
		return quantidadeMoedas;
	}

	public void setQuantidadeMoedas(FieldCNAB quantidadeMoedas) {
		this.quantidadeMoedas = quantidadeMoedas;
	}

	public FieldCNAB getNumeroAvisoDebito() {
		return numeroAvisoDebito;
	}

	public void setNumeroAvisoDebito(FieldCNAB numeroAvisoDebito) {
		this.numeroAvisoDebito = numeroAvisoDebito;
	}

	public FieldCNAB getCnab1() {
		return cnab1;
	}

	public void setCnab1(FieldCNAB cnab1) {
		this.cnab1 = cnab1;
	}

	public FieldCNAB getOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(FieldCNAB ocorrencias) {
		this.ocorrencias = ocorrencias;
	}
	
	
}
