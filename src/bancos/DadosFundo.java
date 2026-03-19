package bancos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DadosFundo {
	private int idDadosFundo=0;
	private String nome="";
	private String cnpj="";
	
	public DadosFundo()
	{
		
	}
	
	public DadosFundo(Connection conn, String cnpj)
	{
		this.cnpj=cnpj;
			String query="select * from dados_fundo where cnpj = '"+this.cnpj+"'";
		Statement st = null;
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = st.executeQuery(query);
			while(rs.next())
			{
				this.idDadosFundo=rs.getInt("id_dados_fundo");
				this.nome=rs.getString("nome");
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public DadosFundo(Connection conn, int idDadosFundo)
	{
		this.idDadosFundo=idDadosFundo;
			String query="select * from dados_fundo where id_dados_fundo = "+this.idDadosFundo;
		Statement st = null;
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = st.executeQuery(query);
			while(rs.next())
			{
				this.cnpj=rs.getString("cnpj");
				this.nome=rs.getString("nome");
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

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
