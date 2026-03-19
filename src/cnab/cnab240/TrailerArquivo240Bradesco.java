package cnab.cnab240;

import java.util.ArrayList;
import java.util.HashMap;

import cnab.FieldCNAB;
import cnab.GrupoRegistrosCNAB;
import cnab.RegistroTrailerDeArquivo;

public class TrailerArquivo240Bradesco extends RegistroTrailerDeArquivo 
{
	private FieldCNAB banco=new FieldCNAB(1,1,3,3,"Codigo do Banco na compensacao","","N");
	private FieldCNAB lote=new FieldCNAB(2,4,7,4,"Lote de Servico","9999","N");
	private FieldCNAB registro=new FieldCNAB(3,8,8,1,"Tipo de Registro","9","N");
	private FieldCNAB cnab0=new FieldCNAB(4,9,17,9,"Uso exclusivo FEBRABAN/CNAB","","A");
	private FieldCNAB quantidadeLotes=new FieldCNAB(5,18,23,6,"Quantidade de Lotes do Arquivo","0","N");
	private FieldCNAB quantidadeRegistros=new FieldCNAB(6,24,29,6,"Quantidade de Registros do Arquivo","0","N");
	private FieldCNAB quantidadeContas=new FieldCNAB(7,30,35,6,"Quantidade de Contas para Conc/Lotes","0","N");
	private FieldCNAB cnab1=new FieldCNAB(8,36,240,205,"Tipo de Registro","","A");
	
	public TrailerArquivo240Bradesco()
	{
		super();
		this.setSize(240);
		this.setTipo("TrailerArquivo240");
	}
	
	public TrailerArquivo240Bradesco(Pagador pagador, ArrayList<Lote240> lotes)
	{
		super();
		this.setSize(240);
		this.setTipo("TrailerArquivo240");
		this.getBanco().setContent(pagador.getDadosBancarios().getDadosBanco().getCodigoCompe());
		this.getQuantidadeLotes().setContent(Integer.toString(lotes.size()));
		int quantidadeRegistros=2;
		for(Lote240 lote:lotes)
		{
			quantidadeRegistros+=2;
			quantidadeRegistros+=lote.getDetalhes().size()*2;
			
		}
		
		this.getQuantidadeRegistros().setContent(Integer.toString(quantidadeRegistros));
		
		this.buildTrailer();
		
	}
	
	public void buildTrailer()
	{
		HashMap<String,GrupoRegistrosCNAB> segmentosTrailer = new HashMap<String,GrupoRegistrosCNAB>();
		GrupoRegistrosCNAB controle = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB cnab0 = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB totais = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB cnab1 = new GrupoRegistrosCNAB();
		
		controle.getFieldsCNAB().put("banco",this.banco);
		controle.getFieldsCNAB().put("lote",this.lote);
		controle.getFieldsCNAB().put("registro",this.registro);
		
		cnab0.getFieldsCNAB().put("cnab0",this.cnab0);
		
		totais.getFieldsCNAB().put("quantidadeLotes", this.quantidadeLotes);
		totais.getFieldsCNAB().put("quantidadeRegistros", this.quantidadeRegistros);
		totais.getFieldsCNAB().put("quantidadeContas", this.quantidadeContas);
		
		cnab1.getFieldsCNAB().put("cnab1",this.cnab1);
		
		segmentosTrailer.put("controle", controle);
		segmentosTrailer.put("cnab0", cnab0);
		segmentosTrailer.put("totais", totais);
		segmentosTrailer.put("cnab1", cnab1);
		
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
	public FieldCNAB getQuantidadeLotes() {
		return quantidadeLotes;
	}
	public void setQuantidadeLotes(FieldCNAB quantidadeLotes) {
		this.quantidadeLotes = quantidadeLotes;
	}
	public FieldCNAB getQuantidadeRegistros() {
		return quantidadeRegistros;
	}
	public void setQuantidadeRegistros(FieldCNAB quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}
	public FieldCNAB getQuantidadeContas() {
		return quantidadeContas;
	}
	public void setQuantidadeContas(FieldCNAB quantidadeContas) {
		this.quantidadeContas = quantidadeContas;
	}
	public FieldCNAB getCnab1() {
		return cnab1;
	}
	public void setCnab1(FieldCNAB cnab1) {
		this.cnab1 = cnab1;
	}
	
}
