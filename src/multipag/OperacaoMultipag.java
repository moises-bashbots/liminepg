package multipag;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import bancos.DadosBancariosFundo;
import cnab.cnab240.DadosBancarios;
import cnab.cnab240.DarfNumerado;
import utils.Utils;

public class OperacaoMultipag {
	private int idOperacoesMultipag=0;
	private Date dataEntrada=null;
	private Date dataPagamento=null;
	private String nomeArquivo="";
	private String seuNumeroMultipag="";
	private Long seuNumeroMultipagInteiro=(long) 0;
	private String nomeFundo="";
	private String codigoBancoFundo="";
	private String nomeBancoFundo="";
	private String agenciaFundo="";
	private String contaFundo="";
	private String digitoContaFundo="";
	private String cnpjCedente="";
	private String nomeCedente="";
	private String nomeBancoCedente="";
	private String codigoBancoCedente="";
	private String agenciaCedente="";
	private String contaCedente="";
	private String digitoContaCedente="";
	private double valor=0;
	private boolean cnabGerado=false;
	private boolean liquidado=false;
	private boolean problema=false;
	private boolean novo=false;
	private int tentativa=1;
	private String nomeCNAB="";
	private String codigoEnvio="";
	private String codigoRetorno="";
	private String tipoChavePIX="";
	private String numeroBoleto="";
	private String chavePIX="";
	private String idPix="";
	private boolean pixAprovado=false;
	private boolean pixEfetivado=false;
	private DadosBancariosFundo dadosBancariosFundo=new DadosBancariosFundo();
	
	
	private static SimpleDateFormat sdfm = new SimpleDateFormat("yyyy-MM-dd");
	
	public OperacaoMultipag()
	{
		
	}
	
	public OperacaoMultipag(
			Connection conn,
			Long seuNumeroMultipagInteiro
			)
	{
		this.seuNumeroMultipagInteiro=seuNumeroMultipagInteiro;
		
			String query="select * from operacoes_multipag"
				+ " where seu_numero_multipag_inteiro="+this.seuNumeroMultipagInteiro+"";
		System.out.println(query);
		Statement st=null;
		try {
			st=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			st.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs=st.getResultSet();
			while(rs.next())
			{
				
				this.idOperacoesMultipag=rs.getInt("id_operacoes_multipag");
				this.dadosBancariosFundo=new DadosBancariosFundo(conn, rs.getInt("dados_bancarios_fundo_id_dados_bancarios_fundo"));
				int cnabGerado=rs.getInt("cnab_gerado");
				int liquidado=rs.getInt("liquidado");
				int problema=rs.getInt("problema");
				this.tentativa=rs.getInt("tentativa");
				this.codigoEnvio=rs.getString("codigo_envio");
				this.codigoRetorno=rs.getString("codigo_retorno");
				
				if(cnabGerado==1)
				{
					this.setCnabGerado(true);
				}
				if(liquidado==1)
				{
					this.setLiquidado(true);
				}
						
				if(problema==1)
				{
					this.setProblema(true);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public OperacaoMultipag(
			Connection conn,
			Date dataEntrada,
			Date dataPagamento,
			String seuNumeroMultipag,
			String nomeArquivo,
			String nomeCNAB)
	{
		this.dataEntrada=dataEntrada;
		this.dataPagamento=dataPagamento;
		this.nomeArquivo=nomeArquivo;
		this.nomeCNAB=nomeCNAB;
			String query="select * from operacoes_multipag"
				+ " where"
				+ " data_entrada='"+sdfm.format(this.dataEntrada)+"'"
				+ " and data_pagamento='"+sdfm.format(this.dataPagamento)+"'"
				+ " and nome_arquivo='"+this.nomeArquivo+"'"
				+ " and nome_cnab='"+this.nomeCNAB+"'";
		System.out.println(query);
		Statement st=null;
		try {
			st=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			st.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		ResultSet rs;
		try {
			rs = st.getResultSet();
			while(rs.next())
			{				
				this.idOperacoesMultipag=rs.getInt("id_operacoes_multipag");
				this.dadosBancariosFundo=new DadosBancariosFundo(conn, rs.getInt("dados_bancarios_fundo_id_dados_bancarios_fundo"));
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		
	}
	
	
	
	public OperacaoMultipag(
			Connection conn,
			Date dataEntrada,
			Date dataPagamento,
			String seuNumeroMultipag,
			String nomeArquivo,
			String contaCedente,
			String digitoContaCedente,
			double valor,
			String tipoChavePIX,
			String chavePIX)
	{
		this.dataEntrada=dataEntrada;
		this.dataEntrada=dataPagamento;
		this.nomeArquivo=nomeArquivo;
		this.seuNumeroMultipag=seuNumeroMultipag;
		System.out.println("SeuNumero: " + this.seuNumeroMultipag);
		this.seuNumeroMultipagInteiro=Long.parseLong(this.seuNumeroMultipag);
		this.contaCedente=contaCedente;
		this.valor=valor;
		this.tipoChavePIX=tipoChavePIX;
		this.chavePIX=chavePIX;
			String query="select * from operacoes_multipag"
				+ " where nome_arquivo='"+this.nomeArquivo+"'";
		System.out.println(query);
		Statement st=null;
		try {
			st=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			st.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs=st.getResultSet();
			while(rs.next())
			{
				
				this.idOperacoesMultipag=rs.getInt("id_operacoes_multipag");
				this.dadosBancariosFundo=new DadosBancariosFundo(conn, rs.getInt("dados_bancarios_fundo_id_dados_bancarios_fundo"));
				int cnabGerado=rs.getInt("cnab_gerado");
				int liquidado=rs.getInt("liquidado");
				int problema=rs.getInt("problema");
				this.tentativa=rs.getInt("tentativa");
				if(cnabGerado==1)
				{
					this.setCnabGerado(true);
				}
				if(liquidado==1)
				{
					this.setLiquidado(true);
				}
						
				if(problema==1)
				{
					this.setProblema(true);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public OperacaoMultipag(
			Connection conn,
			DadosBancarios dadosBancarios,
			Date dataEntrada,
			Date dataPagamento,
			String seuNumeroMultipag,
			String nomeArquivo,
			String nomeFundo,
			String codigoBancoFundo,
			String nomeBancoFundo,
			String agenciaFundo,
			String contaFundo,
			String digitoContaFundo,
			String cnpjCedente,
			String nomeCedente,
			String nomeBancoCedente,
			String codigoBancoCedente,
			String agenciaCedente,
			String contaCedente,
			String digitoContaCedente,
			double valor,
			String tipoChavePIX,
			String chavePIX
			)
	{
		this.dataEntrada=dataEntrada;		
		this.dataPagamento=dataPagamento;
		this.nomeArquivo=nomeArquivo;
		this.seuNumeroMultipag=seuNumeroMultipag;
		this.seuNumeroMultipagInteiro=Long.parseLong(this.seuNumeroMultipag);
		this.nomeFundo=nomeFundo;
		this.codigoBancoFundo=codigoBancoFundo;
		this.nomeBancoFundo=nomeBancoFundo;
		this.agenciaFundo=agenciaFundo;
		this.contaFundo=contaFundo;
		this.digitoContaFundo=digitoContaFundo;
		this.cnpjCedente=cnpjCedente;
		this.nomeCedente=nomeCedente;
		this.nomeBancoCedente=nomeBancoCedente;
		this.codigoBancoCedente=codigoBancoCedente;
		this.agenciaCedente=agenciaCedente;
		this.contaCedente=contaCedente;
		this.digitoContaCedente=digitoContaCedente;
		this.valor=valor;
		this.tipoChavePIX=tipoChavePIX;
		this.chavePIX=chavePIX;
		
			String query="select * from operacoes_multipag"
				+ " where nome_arquivo='"+this.nomeArquivo+"'"
				+ " and seu_numero_multipag_inteiro="+this.seuNumeroMultipagInteiro+"";
		
		System.out.println(query);
		Statement st=null;
		try {
			st=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			st.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs=st.getResultSet();
			while(rs.next())
			{
				this.idOperacoesMultipag=rs.getInt("id_operacoes_multipag");
				this.dadosBancariosFundo=new DadosBancariosFundo(conn, rs.getInt("dados_bancarios_fundo_id_dados_bancarios_fundo"));
				int cnabGerado=rs.getInt("cnab_gerado");
				int liquidado=rs.getInt("liquidado");
				int problema=rs.getInt("problema");
				this.tentativa=rs.getInt("tentativa");
				if(cnabGerado==1)
				{
					this.setCnabGerado(true);
				}
				if(liquidado==1)
				{
					this.setLiquidado(true);
				}						
				if(problema==1)
				{
					this.setProblema(true);
				}
						
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(this.isProblema())
		{
			this.updateProblema(conn, false);
			this.updateTentativa(conn);
		}		
		else if(this.idOperacoesMultipag==0)
		{
			this.setNovo(true);
				String sql="insert into operacoes_multipag "
					+ " (dados_bancarios_fundo_id_dados_bancarios_fundo,"
					+ "data_entrada,"
					+ "data_pagamento,"
					+ "seu_numero_multipag,"
					+ "seu_numero_multipag_inteiro,"
					+ "nome_arquivo,"
					+ "nome_fundo,"
					+ "codigo_banco_fundo,"
					+ "nome_banco_fundo,"
					+ "agencia_fundo,"
					+ "conta_fundo,"
					+ "digito_conta_fundo,"
					+ "cnpj_cedente,"
					+ "nome_cedente,"
					+ "nome_banco_cedente,"
					+ "codigo_banco_cedente,"
					+ "agencia_cedente,"
					+ "conta_cedente,"
					+ "digito_conta_cedente,"
					+ "valor,"
					+ "tipo_chave_pix,"
					+ "chave_pix)"
					+ " values ("
					+ dadosBancarios.getIdDadosBancarios()					
					+ ",'"+sdfm.format(this.dataEntrada)+"'"
					+ ",'"+sdfm.format(this.dataPagamento)+"'"
					+ ",'"+this.seuNumeroMultipag+"'"
					+ ","+this.seuNumeroMultipagInteiro+""
					+ ",'"+this.nomeArquivo+"'"
					+ ",'"+this.nomeFundo+"'"
					+ ",'" + this.codigoBancoFundo + "'"
					+ ",'" + this.nomeBancoFundo+"'"
					+ ",'" + this.agenciaFundo+"'"
					+ ",'" + this.contaFundo+ "'"
					+ ",'" + this.digitoContaFundo+"'"
					+ ",'" + this.cnpjCedente+ "'"
					+ ",'" + this.nomeCedente+ "'"
					+ ",'" + this.nomeBancoCedente+ "'"
					+ ",'" + this.codigoBancoCedente+ "'"
					+ ",'" + this.agenciaCedente+ "'"
					+ ",'" + this.contaCedente+ "'"
					+ ",'" + this.digitoContaCedente+ "'"
					+ "," + this.valor 
					+ ","+"'"+this.tipoChavePIX+"'"
					+ ","+"'"+this.chavePIX+"'"
					+");"
					;
			System.out.println(sql);
			try {
				st.executeUpdate(sql);
				Utils.wait(0.5);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				ResultSet rs=st.getResultSet();
				while(rs.next())
				{
					this.idOperacoesMultipag=rs.getInt("id_operacoes_multipag");
					int cnabGerado=rs.getInt("cnab_gerado");
					int liquidado=rs.getInt("liquidado");
					if(cnabGerado==1)
					{
						this.setCnabGerado(true);
					}
					if(liquidado==1)
					{
						this.setLiquidado(true);
					}
							
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}		
	}
	
	public OperacaoMultipag(
			Connection conn,
			DadosBancarios dadosBancarios,
			Date dataEntrada,
			Date dataPagamento,
			String seuNumeroMultipag,
			String nomeArquivo,
			String nomeFundo,
			String codigoBancoFundo,
			String nomeBancoFundo,
			String agenciaFundo,
			String contaFundo,
			String digitoContaFundo,
			String cnpjCedente,
			String nomeCedente,
			String  numeroBoleto
			)
	{
		this.dataEntrada=dataEntrada;		
		this.dataPagamento=dataPagamento;
		this.nomeArquivo=nomeArquivo;
		this.seuNumeroMultipag=seuNumeroMultipag;
		this.seuNumeroMultipagInteiro=Long.parseLong(this.seuNumeroMultipag);
		this.nomeFundo=nomeFundo;
		this.codigoBancoFundo=codigoBancoFundo;
		this.nomeBancoFundo=nomeBancoFundo;
		this.agenciaFundo=agenciaFundo;
		this.contaFundo=contaFundo;
		this.digitoContaFundo=digitoContaFundo;
		this.cnpjCedente=cnpjCedente;
		this.nomeCedente=nomeCedente;
		this.numeroBoleto=numeroBoleto;
		if(this.numeroBoleto.length()==47)
		{
			this.setValor(Double.parseDouble(this.numeroBoleto.substring(37,47))/100.00);
		}
		if(this.numeroBoleto.length()==48)
		{
			DarfNumerado darf= new DarfNumerado(numeroBoleto);
			this.setValor(darf.getValorReais());
		}
				
			String query="select * from operacoes_multipag"
				+ " where nome_arquivo='"+this.nomeArquivo+"'"
				+ " and seu_numero_multipag_inteiro="+this.seuNumeroMultipagInteiro+"";
		
		System.out.println(query);
		Statement st=null;
		try {
			st=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			st.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs=st.getResultSet();
			while(rs.next())
			{
				this.idOperacoesMultipag=rs.getInt("id_operacoes_multipag");
				this.dadosBancariosFundo=new DadosBancariosFundo(conn, rs.getInt("dados_bancarios_fundo_id_dados_bancarios_fundo"));
				int cnabGerado=rs.getInt("cnab_gerado");
				int liquidado=rs.getInt("liquidado");
				int problema=rs.getInt("problema");
				this.tentativa=rs.getInt("tentativa");
				if(cnabGerado==1)
				{
					this.setCnabGerado(true);
				}
				if(liquidado==1)
				{
					this.setLiquidado(true);
				}						
				if(problema==1)
				{
					this.setProblema(true);
				}
						
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(this.isProblema())
		{
			this.updateProblema(conn, false);
			this.updateTentativa(conn);
		}		
		else if(this.idOperacoesMultipag==0)
		{
			this.setNovo(true);
				String sql="insert into operacoes_multipag "
					+ " (dados_bancarios_fundo_id_dados_bancarios_fundo,"
					+ "data_entrada,"
					+ "data_pagamento,"
					+ "seu_numero_multipag,"
					+ "seu_numero_multipag_inteiro,"
					+ "nome_arquivo,"
					+ "nome_fundo,"
					+ "codigo_banco_fundo,"
					+ "nome_banco_fundo,"
					+ "agencia_fundo,"
					+ "conta_fundo,"
					+ "digito_conta_fundo,"
					+ "cnpj_cedente,"
					+ "nome_cedente,"
					+ "numero_boleto,"
					+ "valor)"
					+ " values ("
					+ dadosBancarios.getIdDadosBancarios()					
					+ ",'"+sdfm.format(this.dataEntrada)+"'"
					+ ",'"+sdfm.format(this.dataPagamento)+"'"
					+ ",'"+this.seuNumeroMultipag+"'"
					+ ","+this.seuNumeroMultipagInteiro+""
					+ ",'"+this.nomeArquivo+"'"
					+ ",'"+this.nomeFundo+"'"
					+ ",'" + this.codigoBancoFundo + "'"
					+ ",'" + this.nomeBancoFundo+"'"
					+ ",'" + this.agenciaFundo+"'"
					+ ",'" + this.contaFundo+ "'"
					+ ",'" + this.digitoContaFundo+"'"
					+ ",'" + this.cnpjCedente+ "'"
					+ ",'" + this.nomeCedente+ "'"
					+ ",'" + this.numeroBoleto+ "'"
					+ "," + this.valor
					+");"
					;
			System.out.println(sql);
			try {
				st.executeUpdate(sql);
				Utils.wait(0.5);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				ResultSet rs=st.getResultSet();
				while(rs.next())
				{
					this.idOperacoesMultipag=rs.getInt("id_operacoes_multipag");
					int cnabGerado=rs.getInt("cnab_gerado");
					int liquidado=rs.getInt("liquidado");
					if(cnabGerado==1)
					{
						this.setCnabGerado(true);
					}
					if(liquidado==1)
					{
						this.setLiquidado(true);
					}
							
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}		
	}

	public OperacaoMultipag(
			Connection conn,
			DadosBancarios dadosBancarios,
			Date dataEntrada,
			Date dataPagamento,
			String seuNumeroMultipag,
			String nomeCNAB,
			String nomeArquivo,
			String nomeFundo,
			String codigoBancoFundo,
			String nomeBancoFundo,
			String agenciaFundo,
			String contaFundo,
			String digitoContaFundo,
			String cnpjCedente,
			String nomeCedente,
			String nomeBancoCedente,
			String codigoBancoCedente,
			String agenciaCedente,
			String contaCedente,
			String digitoContaCedente,
			double valor,
			String tipoChavePIX,
			String chavePIX)
	{
		this.dataEntrada=dataEntrada;
		this.dataPagamento=dataPagamento;
		this.nomeCNAB=nomeCNAB;
		this.nomeArquivo=nomeArquivo;
		this.seuNumeroMultipag=seuNumeroMultipag;
		this.seuNumeroMultipagInteiro=Long.parseLong(this.seuNumeroMultipag);

		this.nomeFundo=nomeFundo;
		this.codigoBancoFundo=codigoBancoFundo;
		this.nomeBancoFundo=nomeBancoFundo;
		this.agenciaFundo=agenciaFundo;
		this.contaFundo=contaFundo;
		this.digitoContaFundo=digitoContaFundo;
		this.cnpjCedente=cnpjCedente;
		this.nomeCedente=nomeCedente;
		this.nomeBancoCedente=nomeBancoCedente;
		this.codigoBancoCedente=codigoBancoCedente;
		this.agenciaCedente=agenciaCedente;
		this.contaCedente=contaCedente;
		this.digitoContaCedente=digitoContaCedente;
		this.valor=valor;
		this.tipoChavePIX=tipoChavePIX;
		this.chavePIX=chavePIX;
		
			String query="select * from operacoes_multipag"
				+ " where nome_arquivo='"+this.nomeArquivo+"'"
				+ " and seu_numero_multipag_inteiro="+this.seuNumeroMultipagInteiro+"";
		System.out.println(query);
		Statement st=null;
		try {
			st=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			st.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs=st.getResultSet();
			while(rs.next())
			{
				this.idOperacoesMultipag=rs.getInt("id_operacoes_multipag");
				this.dadosBancariosFundo=new DadosBancariosFundo(conn, rs.getInt("dados_bancarios_fundo_id_dados_bancarios_fundo"));
				int cnabGerado=rs.getInt("cnab_gerado");
				int liquidado=rs.getInt("liquidado");
				int problema=rs.getInt("problema");
				this.tentativa=rs.getInt("tentativa");
				if(cnabGerado==1)
				{
					this.setCnabGerado(true);
				}
				if(liquidado==1)
				{
					this.setLiquidado(true);
				}
						
				if(problema==1)
				{
					this.setProblema(true);
				}
						
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(this.isProblema())
		{
			this.updateProblema(conn, false);
			this.updateTentativa(conn);
		}		
		else if(this.idOperacoesMultipag==0)
		{
			this.setNovo(true);
				String sql="insert into operacoes_multipag "
					+ " (dados_bancarios_fundo_id_dados_bancarios_fundo,"
					+ "data_entrada,"
					+ "data_pagamento,"
					+ "seu_numero_multipag,"
					+ "seu_numero_multipag_inteiro,"
					+ "nome_cnab,"
					+ "nome_arquivo,"
					+ "nome_fundo,"
					+ "codigo_banco_fundo,"
					+ "nome_banco_fundo,"
					+ "agencia_fundo,"
					+ "conta_fundo,"
					+ "digito_conta_fundo,"
					+ "cnpj_cedente,"
					+ "nome_cedente,"
					+ "nome_banco_cedente,"
					+ "codigo_banco_cedente,"
					+ "agencia_cedente,"
					+ "conta_cedente,"
					+ "digito_conta_cedente,"
					+ "valor,"
					+ "tipo_chave_pix,"
					+ "chave_pix)"
					+ " values ("
					+ dadosBancarios.getIdDadosBancarios()
					+ ",'"+sdfm.format(this.dataEntrada)+"'"
					+ ",'"+sdfm.format(this.dataPagamento)+"'"
					+ ",'"+this.seuNumeroMultipag+"'"
					+ ","+this.seuNumeroMultipagInteiro+""
					+ ",'"+this.nomeCNAB+"'"
					+ ",'"+this.nomeArquivo+"'"
					+ ",'"+this.nomeFundo+"'"
					+ ",'" + this.codigoBancoFundo + "'"
					+ ",'" + this.nomeBancoFundo+"'"
					+ ",'" + this.agenciaFundo+"'"
					+ ",'" + this.contaFundo+ "'"
					+ ",'" + this.digitoContaFundo+"'"
					+ ",'" + this.cnpjCedente+ "'"
					+ ",'" + this.nomeCedente+ "'"
					+ ",'" + this.nomeBancoCedente+ "'"
					+ ",'" + this.codigoBancoCedente+ "'"
					+ ",'" + this.agenciaCedente+ "'"
					+ ",'" + this.contaCedente+ "'"
					+ ",'" + this.digitoContaCedente+ "'"
					+ "," + this.valor 
					+ ",'" + this.tipoChavePIX+ "'"
					+ ",'" + this.chavePIX+ "'"
					+	");"
					;
			System.out.println(sql);
			try {
				st.executeUpdate(sql);
				Utils.wait(0.5);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				st.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				ResultSet rs=st.getResultSet();
				while(rs.next())
				{
					this.idOperacoesMultipag=rs.getInt("id_operacoes_multipag");
					int cnabGerado=rs.getInt("cnab_gerado");
					int liquidado=rs.getInt("liquidado");
					if(cnabGerado==1)
					{
						this.setCnabGerado(true);
					}
					if(liquidado==1)
					{
						this.setLiquidado(true);
					}
							
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}		
	}
	
	public OperacaoMultipag(Connection conn, int idOperacoesMultipag)
	{
		this.idOperacoesMultipag=idOperacoesMultipag;
			String query="select * from operacoes_multipag"
				+ " where id_operacoes_multipag="+this.seuNumeroMultipagInteiro+"";
		
		System.out.println(query);
		Statement st=null;
		try {
			st=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			st.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs=st.getResultSet();
			while(rs.next())
			{
				this.idOperacoesMultipag=rs.getInt("id_operacoes_multipag");
				this.dadosBancariosFundo=new DadosBancariosFundo(conn, rs.getInt("dados_bancarios_fundo_id_dados_bancarios_fundo"));
				int cnabGerado=rs.getInt("cnab_gerado");
				int liquidado=rs.getInt("liquidado");
				int problema=rs.getInt("problema");
				this.tentativa=rs.getInt("tentativa");
				this.idPix=rs.getString("id_pix");
				this.valor=rs.getDouble("valor");
				if(rs.getInt("pix_aprovado")==1)
				{
					this.pixAprovado=true;
				}
				if(rs.getInt("pix_efetivado")==1)
				{
					this.pixEfetivado=true;
				}
				if(cnabGerado==1)
				{
					this.setCnabGerado(true);
				}
				if(liquidado==1)
				{
					this.setLiquidado(true);
				}						
				if(problema==1)
				{
					this.setProblema(true);
				}
						
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateCnabEnviado(Connection conn, boolean enviado, boolean teste)
	{
		String sql="";
		if(enviado)
		{
			sql="update operacoes_multipag set  hora_envio=CURRENT_TIMESTAMP "
					+ " where id_operacoes_multipag=" + this.idOperacoesMultipag;
			if(teste)
			{
				sql="update operacoes_multipag set set  hora_envio=CURRENT_TIMESTAMP, teste=1"
						+ " where id_operacoes_multipag=" + this.idOperacoesMultipag;	
			}
		}
	}
	
	
	public void updateCnabGerado(Connection conn, boolean gerado, boolean teste)
	{
		String sql="";
		if(gerado)
		{
			sql="update operacoes_multipag set cnab_gerado=1, nome_cnab='"+this.nomeCNAB+"', hora_cnab=CURRENT_TIMESTAMP "
					+ " where id_operacoes_multipag=" + this.idOperacoesMultipag;
			if(teste)
			{
				sql="update operacoes_multipag set cnab_gerado=1, teste=1, nome_cnab='"+this.nomeCNAB+"', hora_cnab=CURRENT_TIMESTAMP "
						+ " where id_operacoes_multipag=" + this.idOperacoesMultipag;	
			}
		}
		else
		{
			sql="update operacoes_multipag set cnab_gerado=0, hora_cnab=CURRENT_TIMESTAMP where id_operacoes_multipag=" + this.idOperacoesMultipag;
		}
		
		try {
			Statement st=conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateIdPix(Connection conn, String idPix)
	{
		String sql="update operacoes_multipag set id_pix="+"'"+idPix+"'"+" where id_operacoes_multipag="+this.idOperacoesMultipag;		
		
		try {
			Statement st=conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updatePixEfetivado(Connection conn, boolean efectivated)
	{
		int efectivatedInt=0;
		if(efectivated)
		{
			efectivatedInt=1;
		}
		String sql="update operacoes_multipag set pix_efetivado="+efectivatedInt+" where id_operacoes_multipag="+this.idOperacoesMultipag;		
		
		try {
			Statement st=conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateRetornoBaixado(Connection conn, boolean baixado, String observacao)
	{
		String sql="";
		String sqlObservacao="";
		

		boolean observacaoDiferente=false;
		System.out.println("ObservacaoOrig: '"+observacao+"'");
		observacao=observacao.trim();
		System.out.println("Codigo retorno existente: '" + this.codigoRetorno+"'");
		System.out.println("Observacao: '"+observacao+"'");
		
		if(this.codigoRetorno!=null)
		{
			if(!this.codigoRetorno.contains(observacao))
			{
				System.out.println("Observação nova!");
				sqlObservacao=",codigo_retorno='"+this.codigoRetorno.trim()+observacao.trim()+"',hora_retorno=CURRENT_TIMESTAMP";
				this.codigoRetorno+=observacao.trim();
				observacaoDiferente=true;
			}
			else
			{
				System.out.println("Observação já existente, não será registrada!");
			}
		}
		else
		{
			System.out.println("Primeira Observação!");
			this.codigoRetorno=observacao;
			sqlObservacao=",codigo_retorno='"+this.codigoRetorno+"',hora_retorno=CURRENT_TIMESTAMP";
			observacaoDiferente=true;
		}
		
		if(baixado)
		{
			sql="update operacoes_multipag set retorno_baixado=1" + sqlObservacao
					+ " where id_operacoes_multipag=" + this.idOperacoesMultipag;
		}
		else
		{
			sql="update operacoes_multipag set retorno_baixado=0 where id_operacoes_multipag=" + this.idOperacoesMultipag;
		}
		
		if(observacaoDiferente)
		{
			try {
				Statement st=conn.createStatement();
				st.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public void updateLiquidado(Connection conn, boolean liquidado)
	{
		String sql="";
		if(liquidado)
		{
			sql="update operacoes_multipag set liquidado=1 where id_operacoes_multipag=" + this.idOperacoesMultipag;
		}
		else
		{
			sql="update operacoes_multipag set liquidado=0 where id_operacoes_multipag=" + this.idOperacoesMultipag;
		}
		
		try {
			Statement st=conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateProblema(Connection conn, boolean problema)
	{
		String sql="";
		if(problema)
		{
			sql="update operacoes_multipag set problema=1 where id_operacoes_multipag=" + this.idOperacoesMultipag;
		}
		else
		{
			sql="update operacoes_multipag set problema=0 where id_operacoes_multipag=" + this.idOperacoesMultipag;
		}
		
		try {
			Statement st=conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateTentativa(Connection conn)
	{
		String sql="";
		this.tentativa+=1;
		sql="update operacoes_multipag"
				+ " set tentativa="+this.tentativa
				+ " where id_operacoes_multipag=" + this.idOperacoesMultipag;
		try {
			Statement st=conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getIdOperacoesMultipag() {
		return idOperacoesMultipag;
	}

	public void setIdOperacoesMultipag(int idOperacoesMultipag) {
		this.idOperacoesMultipag = idOperacoesMultipag;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getNomeFundo() {
		return nomeFundo;
	}

	public void setNomeFundo(String nomeFundo) {
		this.nomeFundo = nomeFundo;
	}

	public String getCodigoBancoFundo() {
		return codigoBancoFundo;
	}

	public void setCodigoBancoFundo(String codigoBancoFundo) {
		this.codigoBancoFundo = codigoBancoFundo;
	}

	public String getNomeBancoFundo() {
		return nomeBancoFundo;
	}

	public void setNomeBancoFundo(String nomeBancoFundo) {
		this.nomeBancoFundo = nomeBancoFundo;
	}

	public String getAgenciaFundo() {
		return agenciaFundo;
	}

	public void setAgenciaFundo(String agenciaFundo) {
		this.agenciaFundo = agenciaFundo;
	}

	public String getContaFundo() {
		return contaFundo;
	}

	public void setContaFundo(String contaFundo) {
		this.contaFundo = contaFundo;
	}

	public String getDigitoContaFundo() {
		return digitoContaFundo;
	}

	public void setDigitoContaFundo(String digitoContaFundo) {
		this.digitoContaFundo = digitoContaFundo;
	}

	public String getCnpjCedente() {
		return cnpjCedente;
	}

	public void setCnpjCedente(String cnpjCedente) {
		this.cnpjCedente = cnpjCedente;
	}

	public String getNomeCedente() {
		return nomeCedente;
	}

	public void setNomeCedente(String nomeCedente) {
		this.nomeCedente = nomeCedente;
	}

	public String getNomeBancoCedente() {
		return nomeBancoCedente;
	}

	public void setNomeBancoCedente(String nomeBancoCedente) {
		this.nomeBancoCedente = nomeBancoCedente;
	}

	public String getCodigoBancoCedente() {
		return codigoBancoCedente;
	}

	public void setCodigoBancoCedente(String codigoBancoCedente) {
		this.codigoBancoCedente = codigoBancoCedente;
	}

	public String getAgenciaCedente() {
		return agenciaCedente;
	}

	public void setAgenciaCedente(String agenciaCedente) {
		this.agenciaCedente = agenciaCedente;
	}

	public String getContaCedente() {
		return contaCedente;
	}

	public void setContaCedente(String contaCedente) {
		this.contaCedente = contaCedente;
	}

	public String getDigitoContaCedente() {
		return digitoContaCedente;
	}

	public void setDigitoContaCedente(String digitoContaCedente) {
		this.digitoContaCedente = digitoContaCedente;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public boolean isCnabGerado() {
		return cnabGerado;
	}

	public void setCnabGerado(boolean cnabGerado) {
		this.cnabGerado = cnabGerado;
	}

	public boolean isLiquidado() {
		return liquidado;
	}

	public void setLiquidado(boolean liquidado) {
		this.liquidado = liquidado;
	}

	public static SimpleDateFormat getSdfm() {
		return sdfm;
	}

	public static void setSdfm(SimpleDateFormat sdfm) {
		OperacaoMultipag.sdfm = sdfm;
	}

	public boolean isNovo() {
		return novo;
	}

	public void setNovo(boolean novo) {
		this.novo = novo;
	}

	public String getSeuNumeroMultipag() {
		return seuNumeroMultipag;
	}

	public void setSeuNumeroMultipag(String seuNumeroMultipag) {
		this.seuNumeroMultipag = seuNumeroMultipag;
	}

	public boolean isProblema() {
		return problema;
	}

	public void setProblema(boolean problema) {
		this.problema = problema;
	}

	public int getTentativa() {
		return tentativa;
	}

	public void setTentativa(int tentativa) {
		this.tentativa = tentativa;
	}

	public String getNomeCNAB() {
		return nomeCNAB;
	}

	public void setNomeCNAB(String nomeCNAB) {
		this.nomeCNAB = nomeCNAB;
	}

	public String getCodigoEnvio() {
		return codigoEnvio;
	}

	public void setCodigoEnvio(String codigoEnvio) {
		this.codigoEnvio = codigoEnvio;
	}

	public String getCodigoRetorno() {
		return codigoRetorno;
	}

	public void setCodigoRetorno(String codigoRetorno) {
		this.codigoRetorno = codigoRetorno;
	}

	public Long getSeuNumeroMultipagInteiro() {
		return seuNumeroMultipagInteiro;
	}

	public void setSeuNumeroMultipagInteiro(Long seuNumeroMultipagInteiro) {
		this.seuNumeroMultipagInteiro = seuNumeroMultipagInteiro;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getTipoChavePIX() {
		return this.tipoChavePIX;
	}

	public void setTipoChavePIX(String tipoChavePIX) {
		this.tipoChavePIX = tipoChavePIX;
	}

	public String getChavePIX() {
		return this.chavePIX;
	}

	public void setChavePIX(String chavePIX) {
		this.chavePIX = chavePIX;
	}

	public String getIdPix() {
		return this.idPix;
	}

	public void setIdPix(String idPix) {
		this.idPix = idPix;
	}

	public boolean isPixAprovado() {
		return this.pixAprovado;
	}

	public void setPixAprovado(boolean pixAprovado) {
		this.pixAprovado = pixAprovado;
	}

	public boolean isPixEfetivado() {
		return this.pixEfetivado;
	}

	public void setPixEfetivado(boolean pixEfetivado) {
		this.pixEfetivado = pixEfetivado;
	}

	public DadosBancariosFundo getDadosBancariosFundo() {
		return this.dadosBancariosFundo;
	}

	public void setDadosBancariosFundo(DadosBancariosFundo dadosBancariosFundo) {
		this.dadosBancariosFundo = dadosBancariosFundo;
	}

	public String getNumeroBoleto() {
		return this.numeroBoleto;
	}

	public void setNumeroBoleto(String numeroBoleto) {
		this.numeroBoleto = numeroBoleto;
	}
	
}
