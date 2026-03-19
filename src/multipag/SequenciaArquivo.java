package multipag;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import mysql.ConnectorMariaDB;
import utils.Utils;

public class SequenciaArquivo 
{
	private int idSequenciaArquivo=0;
	private Date dataArquivo=null;
	private int sequencia=0;
	private String nomeArquivo="";
	private String convenio="";
	
	private static SimpleDateFormat sdfm = new SimpleDateFormat("yyyy-MM-dd");
	
	public SequenciaArquivo()
	{
		
	}
	
	public SequenciaArquivo(Date dataArquivo, int sequencia, String nomeArquivo)
	{
		this.dataArquivo=dataArquivo;
		this.sequencia=sequencia;
		this.nomeArquivo=nomeArquivo;
	}

	public SequenciaArquivo(Connection conn, Date dataArquivo, String nomeArquivo, String convenio)
	{
		this.convenio=convenio;
		String query="select *"
					+ " from sequencia_arquivo"
				+ " where convenio='"+this.convenio+"'"
				+ " order by sequencia desc limit 1";
		Statement st=null;
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = st.executeQuery(query);
			while(rs.next())
			{
				this.idSequenciaArquivo=rs.getInt("id_sequencia_arquivo");
				this.dataArquivo=rs.getDate("data_arquivo");
				this.sequencia=rs.getInt("sequencia");
				this.nomeArquivo=rs.getString("nome_arquivo");
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(this.idSequenciaArquivo==0)
		{
			query="select *"
						+ " from sequencia_arquivo"
	//		+ " where data_arquivo='"+sdfm.format(dataArquivo)+"'"
					+ " order by sequencia desc limit 1";
			st=null;
			try {
				st = conn.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			

				ResultSet rs;
				try {
					rs = st.executeQuery(query);
					while(rs.next())
					{
						this.idSequenciaArquivo=rs.getInt("id_sequencia_arquivo");
						this.dataArquivo=rs.getDate("data_arquivo");
						this.sequencia=rs.getInt("sequencia");
						this.nomeArquivo=rs.getString("nome_arquivo");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
		
		
		if(this.idSequenciaArquivo==0)
		{
			this.dataArquivo=dataArquivo;
			this.nomeArquivo=nomeArquivo;
			this.sequencia=16;
				String sql="insert into sequencia_arquivo (data_arquivo,sequencia,nome_arquivo, convenio)"
					+ " values "
					+ "('"+sdfm.format(this.dataArquivo)+"'"
					+ ","+this.sequencia
					+ ",'"+this.nomeArquivo+"'"
					+ ",'"+this.convenio+"'"
					+ ")";
			
			try {
				st.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Utils.wait(0.5);
			try {
				ResultSet rs = st.executeQuery(query);
				while(rs.next())
				{
					this.idSequenciaArquivo=rs.getInt("id_sequencia_arquivo");
					this.dataArquivo=rs.getDate("data_arquivo");
					this.sequencia=rs.getInt("sequencia");
					this.nomeArquivo=rs.getString("nome_arquivo");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		else if(this.nomeArquivo!=nomeArquivo)
		{
			this.dataArquivo=dataArquivo;
			this.nomeArquivo=nomeArquivo;
			this.sequencia=this.sequencia+1;
				String sql="insert into sequencia_arquivo (data_arquivo,sequencia,nome_arquivo,convenio)"
					+ " values "
					+ "('"+sdfm.format(this.dataArquivo)+"'"
					+ ","+this.sequencia
					+ ",'"+this.nomeArquivo+"'"
					+ ",'"+this.convenio+"'"
					+ ")";
			
			try {
				st.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			Utils.wait(0.5);
			try {
				ResultSet rs = st.executeQuery(query);
				while(rs.next())
				{
					this.idSequenciaArquivo=rs.getInt("id_sequencia_arquivo");
					this.dataArquivo=rs.getDate("data_arquivo");
					this.sequencia=rs.getInt("sequencia");
					this.nomeArquivo=rs.getString("nome_arquivo");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}	

		}
	}
	
	public SequenciaArquivo(Connection conn, Date dataArquivo, String nomeArquivo)
	{
		String query="select *"
					+ " from sequencia_arquivo"
//		+ " where data_arquivo='"+sdfm.format(dataArquivo)+"'"
				+ " order by sequencia desc limit 1";
		Statement st=null;
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = st.executeQuery(query);
			while(rs.next())
			{
				this.idSequenciaArquivo=rs.getInt("id_sequencia_arquivo");
				this.dataArquivo=rs.getDate("data_arquivo");
				this.sequencia=rs.getInt("sequencia");
				this.nomeArquivo=rs.getString("nome_arquivo");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(this.idSequenciaArquivo==0)
		{
			this.dataArquivo=dataArquivo;
			this.nomeArquivo=nomeArquivo;
			this.sequencia=16;
				String sql="insert into sequencia_arquivo (data_arquivo,sequencia,nome_arquivo)"
					+ " values "
					+ "('"+sdfm.format(this.dataArquivo)+"'"
					+ ","+this.sequencia
					+ ",'"+this.nomeArquivo+"'"
					+ ")";
			
			try {
				st.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Utils.wait(0.5);
			try {
				ResultSet rs = st.executeQuery(query);
				while(rs.next())
				{
					this.idSequenciaArquivo=rs.getInt("id_sequencia_arquivo");
					this.dataArquivo=rs.getDate("data_arquivo");
					this.sequencia=rs.getInt("sequencia");
					this.nomeArquivo=rs.getString("nome_arquivo");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		else if(this.nomeArquivo!=nomeArquivo)
		{
			this.dataArquivo=dataArquivo;
			this.nomeArquivo=nomeArquivo;
			this.sequencia=this.sequencia+1;
				String sql="insert into sequencia_arquivo (data_arquivo,sequencia,nome_arquivo)"
					+ " values "
					+ "('"+sdfm.format(this.dataArquivo)+"'"
					+ ","+this.sequencia
					+ ",'"+this.nomeArquivo+"'"
					+ ")";
			
			try {
				st.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			Utils.wait(0.5);
			try {
				ResultSet rs = st.executeQuery(query);
				while(rs.next())
				{
					this.idSequenciaArquivo=rs.getInt("id_sequencia_arquivo");
					this.dataArquivo=rs.getDate("data_arquivo");
					this.sequencia=rs.getInt("sequencia");
					this.nomeArquivo=rs.getString("nome_arquivo");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}	

		}
	}
	
	
	public void updateNomeArquivo(String nomeArquivo)
	{
		String sql="update sequencia_arquivo set nome_arquivo='"+nomeArquivo+"'"
				+ " where id_sequencia_arquivo="+this.idSequenciaArquivo;
		this.nomeArquivo=nomeArquivo;
		try {
			Statement st = ConnectorMariaDB.getConn().createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public int getIdSequenciaArquivo() {
		return idSequenciaArquivo;
	}
	public void setIdSequenciaArquivo(int idSequenciaArquivo) {
		this.idSequenciaArquivo = idSequenciaArquivo;
	}
	public Date getDataArquivo() {
		return dataArquivo;
	}
	public void setDataArquivo(Date dataArquivo) {
		this.dataArquivo = dataArquivo;
	}
	public int getSequencia() {
		return sequencia;
	}
	public void setSequencia(int sequencia) {
		this.sequencia = sequencia;
	}
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	public static SimpleDateFormat getSdfm() {
		return sdfm;
	}

	public static void setSdfm(SimpleDateFormat sdfm) {
		SequenciaArquivo.sdfm = sdfm;
	}
	
}
