package cnab.cnab240;

import java.util.ArrayList;
import java.util.HashMap;

import cnab.FieldCNAB;
import cnab.GrupoRegistrosCNAB;
import cnab.RegistroCNAB;
import utils.Utils;

public class TrailerLote240Unavanti extends RegistroCNAB 
{
	private FieldCNAB banco=new FieldCNAB(1,1,3,3,"Codigo do Banco na compensacao","","N");
	private FieldCNAB lote=new FieldCNAB(2,4,7,4,"Lote de Servico","","N");
	private FieldCNAB registro=new FieldCNAB(3,8,8,1,"Tipo de Registro","5","N");
	private FieldCNAB brancos=new FieldCNAB(4,9,17,9,"Complemento de Registro","","A");	
	private FieldCNAB quantidadeRegistros=new FieldCNAB(5,18,23,6,"Quantidade de Registros do Lote","0","N");
	private FieldCNAB valor=new FieldCNAB(6,24,41,18,"Somatoria de Valores","0","N",2);
	private FieldCNAB zeros=new FieldCNAB(7,42,59,18,"Somatoria de Quantidade de Moedas","0","N");
	private FieldCNAB brancos2=new FieldCNAB(8,60,230,171,"Complemento de Registro","","A");	
	private FieldCNAB ocorrencias=new FieldCNAB(10,231,240,10,"Codigos das ocorrencias para retorno","","A");
	
	public TrailerLote240Unavanti()
	{
		super();
		this.setSize(240);
		this.setTipo("TrailerLote240");
	}
	
	public TrailerLote240Unavanti(Pagador pagador, ArrayList<DetalhePagamentoAB240> detalhes, int iLote)
	{
		super();
		this.setSize(240);
		this.setTipo("TrailerLote240");
		this.getBanco().setContent(pagador.getDadosBancarios().getDadosBanco().getCodigoCompe());
		this.getLote().setContent(Integer.toString(iLote));
		this.getQuantidadeRegistros().setContent(Integer.toString(detalhes.size()*2+2));
		long valorTotalInteiro=0;
		double valorTotal=0;
		double quantidadeTotal=0;
		for(DetalhePagamentoAB240 detalhe:detalhes)
		{
			valorTotalInteiro+=detalhe.getDetalheAUnavanti().getPagamento().getValorInteiro();
			quantidadeTotal+=Utils.doubleFromCNABValue(detalhe.getDetalheAUnavanti().getQuantidadeMoeda());
		}
		this.getValor().setContent(Long.toString(valorTotalInteiro));
		
		this.buildTrailer();
	}
	
	public void buildTrailer()
	{
		HashMap<String,GrupoRegistrosCNAB> segmentosTrailer = new HashMap<String,GrupoRegistrosCNAB>();
		GrupoRegistrosCNAB controle = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB totais = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB ocorrencias = new GrupoRegistrosCNAB();
		
		controle.getFieldsCNAB().put("banco",this.banco);
		controle.getFieldsCNAB().put("lote",this.lote);
		controle.getFieldsCNAB().put("registro",this.registro);
		controle.getFieldsCNAB().put("brancos",this.brancos);

		
		totais.getFieldsCNAB().put("quantidadeRegistros", this.quantidadeRegistros);
		totais.getFieldsCNAB().put("valor", this.valor);

		ocorrencias.getFieldsCNAB().put("zeros",this.zeros);
		ocorrencias.getFieldsCNAB().put("brancos2",this.brancos2);
		ocorrencias.getFieldsCNAB().put("ocorrencias",this.ocorrencias);
		
		segmentosTrailer.put("controle", controle);		
		segmentosTrailer.put("totais", totais);
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

	public FieldCNAB getBrancos() {
		return brancos;
	}

	public void setBrancos(FieldCNAB brancos) {
		this.brancos = brancos;
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

	public FieldCNAB getZeros() {
		return zeros;
	}

	public void setZeros(FieldCNAB zeros) {
		this.zeros = zeros;
	}

	public FieldCNAB getBrancos2() {
		return brancos2;
	}

	public void setBrancos2(FieldCNAB brancos2) {
		this.brancos2 = brancos2;
	}

	public FieldCNAB getOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(FieldCNAB ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

	}
