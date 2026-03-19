package portalfidc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import mysql.ConnectorMariaDB;

public class OperacaoSimplificada {

	private static SimpleDateFormat sdfd=new SimpleDateFormat("yyyy-MM-dd");
	
	private boolean aprovadoVadu=false;
	private boolean finalizado=false;
	
	private String nomeFundo="";
	private String nomeArquivoCustodia="";
	private String nomeArquivoentrada="";
	private String nomeCedente="";
	private String tipoRecebivel="";
	private String situacao="";
	
	public OperacaoSimplificada(WebElement row)
	{
			List<WebElement> cols = row.findElements(By.tagName("td"));
//			System.out.println("Identificadas "+cols.size()+" colunas");
			int iCol=0;
			for(WebElement col:cols)
			{
				switch (iCol) {
				case 4:
					this.nomeFundo=col.getText().trim();
					break;
				case 5:
					String nomesArquivoString=col.getText().trim();
					String[] fields=nomesArquivoString.split("/");
					this.nomeArquivoCustodia=fields[0].trim();
					this.nomeArquivoentrada=fields[1].trim();
					break;
				case 6:
					this.nomeCedente=col.getText().trim();
					break;
				case 15:
					this.tipoRecebivel=col.getText().trim();
					break;
				case 14:
					this.situacao=col.getText().trim();
					break;
				default:
					break;
				}
//				System.out.println(iCol+" "+col.getText()+" ");
				
				iCol++;
			}
	}
	
	public void verificar()
	{
		String query="select * from motor_risco_vadu mrv"
				+ " where data_operacao="+"'"+sdfd.format(Calendar.getInstance().getTime())+"'"
				+ " and nome_arquivo="+"'"+this.nomeArquivoCustodia+"'";
		Statement st=null; 
		try {
			st=ConnectorMariaDB.conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ResultSet rs=null;
		try {
			rs=st.executeQuery(query);
			while(rs.next())
			{
				int finalizadoInt=rs.getInt("vadu_risco_resultado_finalizado");
				int sucessoInt=rs.getInt("vadu_risco_analise_sucesso");
				if(finalizadoInt==1)
				{
					this.finalizado=true;
				}
				else {
					this.finalizado=false;
				}
				if(sucessoInt==1)
				{
					this.aprovadoVadu=true;
				}
				else {
					this.aprovadoVadu=false;
				}
		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void show()
	{
		System.out.println("-----------------------------------------------------------------------------------------");
		System.out.println("Fundo: "+this.nomeFundo);
		System.out.println("Cedente: "+this.nomeCedente);
		System.out.println("NomeArquivo: "+this.nomeArquivoCustodia);
		System.out.println("TipoRecebivel: "+this.tipoRecebivel);
		System.out.println("Situacao: "+this.situacao);
		System.out.println("AnaliseFinalizado: "+this.isFinalizado());
		System.out.println("AnaliseSucesso: "+this.isAprovadoVadu());
		System.out.println("-----------------------------------------------------------------------------------------");
	}

	public boolean isAprovadoVadu() {
		return this.aprovadoVadu;
	}

	public void setAprovadoVadu(boolean aprovadoVadu) {
		this.aprovadoVadu = aprovadoVadu;
	}

	public String getNomeFundo() {
		return this.nomeFundo;
	}

	public void setNomeFundo(String nomeFundo) {
		this.nomeFundo = nomeFundo;
	}

	public String getNomeArquivoCustodia() {
		return this.nomeArquivoCustodia;
	}

	public void setNomeArquivoCustodia(String nomeArquivoCustodia) {
		this.nomeArquivoCustodia = nomeArquivoCustodia;
	}

	public String getNomeArquivoentrada() {
		return this.nomeArquivoentrada;
	}

	public void setNomeArquivoentrada(String nomeArquivoentrada) {
		this.nomeArquivoentrada = nomeArquivoentrada;
	}

	public String getNomeCedente() {
		return this.nomeCedente;
	}

	public void setNomeCedente(String nomeCedente) {
		this.nomeCedente = nomeCedente;
	}

	public String getTipoRecebivel() {
		return this.tipoRecebivel;
	}

	public void setTipoRecebivel(String tipoRecebivel) {
		this.tipoRecebivel = tipoRecebivel;
	}

	public String getSituacao() {
		return this.situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public static SimpleDateFormat getSdfd() {
		return sdfd;
	}

	public static void setSdfd(SimpleDateFormat sdfd) {
		OperacaoSimplificada.sdfd = sdfd;
	}

	public boolean isFinalizado() {
		return this.finalizado;
	}

	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}
}


/*
0 
1 12:45
2 
3 
4 MAKENA FIDC NP
5 45938235000167_017.TXT / CB06110007.TXT
6 FIMAL FIOS MAGNETICOS LTDA
7 R$ 0,00
8 R$ 20.309,27
9 R$ 0,00
10 R$ 0,00
11 R$ 21.436,28
12 9
13 R$ 0,00
14 Aguardando aprovação interna do custodiante
15 Duplicata
16 06/11/2024
17 
18 
19 
20 
21 
22 
23 
24 
25 
*/