package cnab.cnab240;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import utils.Utils;

public class DadosFundo {
	private int idDadosFundo=0;
	private String nome="";
	private String cnpj="";
	
	public DadosFundo()
	{
		
	}
	
	public DadosFundo(Connection conn, int idDadosFundo)
	{
		this.idDadosFundo=idDadosFundo;
			String query = "select * from dados_fundo where id_dados_fundo="+this.idDadosFundo;

		Statement st=null;
		
		try {
			st=conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ResultSet rs=st.executeQuery(query);
			while(rs.next())
			{
				this.nome=rs.getString("nome");
				this.cnpj=rs.getString("cnpj");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public DadosFundo(Connection conn, String cnpj)
	{
		this.cnpj=cnpj;
			String query = "select * from dados_fundo where cnpj='"+this.cnpj+"'";

		System.out.println(query);
		Statement st=null;
		
		try {
			st=conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ResultSet rs=st.executeQuery(query);
			while(rs.next())
			{
				this.idDadosFundo=rs.getInt("id_dados_fundo");
				this.nome=Utils.normalizeUri(rs.getString("nome"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public DadosFundo(Connection conn, String cnpj)
//	{
//		this.cnpj=cnpj;
//		String query = "select * from dados_bancarios_fundo where cnpj_fundo='"+this.cnpj+"'";
//
//		System.out.println(query);
//		Statement st=null;
//		
//		try {
//			st=conn.createStatement();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			ResultSet rs=st.executeQuery(query);
//			while(rs.next())
//			{
//				this.idDadosFundo=rs.getInt("id_dados_bancarios_fundo");
//				this.nome=Utils.normalizeUri(rs.getString("nome_fundo"));
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public int getIdDadosFundo() {
		return idDadosFundo;
	}

	public void setIdDadosFundo(int idDadosFundo) {
		this.idDadosFundo = idDadosFundo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	

}
