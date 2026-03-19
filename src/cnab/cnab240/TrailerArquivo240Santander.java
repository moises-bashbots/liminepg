package cnab.cnab240;

import java.util.ArrayList;
import java.util.HashMap;

import cnab.FieldCNAB;
import cnab.GrupoRegistrosCNAB;
import cnab.RegistroTrailerDeArquivo;

public class TrailerArquivo240Santander extends RegistroTrailerDeArquivo 
{
	private FieldCNAB banco=new FieldCNAB(1,1,3,3,"Codigo do Banco na compensacao","","N");
	private FieldCNAB lote=new FieldCNAB(2,4,7,4,"Lote de Servico","9999","N");
	private FieldCNAB registro=new FieldCNAB(3,8,8,1,"Tipo de Registro","9","N");
	private FieldCNAB filler=new FieldCNAB(4,9,17,9,"Uso exclusivo FEBRABAN/CNAB","","A");
	private FieldCNAB quantidadeLotes=new FieldCNAB(5,18,23,6,"Quantidade de Lotes do Arquivo","0","N");
	private FieldCNAB quantidadeRegistros=new FieldCNAB(6,24,29,6,"Quantidade de Registros do Arquivo","0","N");
	private FieldCNAB filler2=new FieldCNAB(7,30,240,211,"Tipo de Registro","","A");
	
	public TrailerArquivo240Santander()
	{
		super();
		this.setSize(240);
		this.setTipo("TrailerArquivo240");
	}
	
	public TrailerArquivo240Santander(Pagador pagador, ArrayList<Lote240> lotes)
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
			for(DetalhePagamentoAB240 detalhe:lote.getDetalhes())
			{
				quantidadeRegistros+=detalhe.getNumberRegistrosDetalhes();
			}
			
		}
		
		this.getQuantidadeRegistros().setContent(Integer.toString(quantidadeRegistros));
		
		this.buildTrailer();
		
	}
	
	public void buildTrailer()
	{
		HashMap<String,GrupoRegistrosCNAB> segmentosTrailer = new HashMap<String,GrupoRegistrosCNAB>();
		GrupoRegistrosCNAB controle = new GrupoRegistrosCNAB();
		GrupoRegistrosCNAB totais = new GrupoRegistrosCNAB();
		
		controle.getFieldsCNAB().put("banco",this.banco);
		controle.getFieldsCNAB().put("lote",this.lote);
		controle.getFieldsCNAB().put("registro",this.registro);
		controle.getFieldsCNAB().put("filler",this.filler);
		
		totais.getFieldsCNAB().put("quantidadeLotes", this.quantidadeLotes);
		totais.getFieldsCNAB().put("quantidadeRegistros", this.quantidadeRegistros);
		totais.getFieldsCNAB().put("filler2", this.filler2);
		
		segmentosTrailer.put("controle", controle);
		segmentosTrailer.put("totais", totais);
		
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

	public FieldCNAB getFiller() {
		return filler;
	}

	public void setFiller(FieldCNAB filler) {
		this.filler = filler;
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

	public FieldCNAB getFiller2() {
		return filler2;
	}

	public void setFiller2(FieldCNAB filler2) {
		this.filler2 = filler2;
	}
}
