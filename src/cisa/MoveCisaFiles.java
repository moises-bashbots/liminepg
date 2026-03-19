package cisa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mysql.ConnectorMariaDB;
import utils.Utils;

public class MoveCisaFiles {
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat sdfs=new SimpleDateFormat("yyMMdd");
	private static SimpleDateFormat sdfl=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdfls=new SimpleDateFormat("yyyy-MM-dd");
	private static String pathCISA="";
	private static String pathAft="";
	private static String mailBox="";
	private static HashMap<String, String> listSentFiles = new HashMap<String, String>();
	private static HashMap<String, String> listInboxFiles = new HashMap<String, String>();
	private static HashMap<String, String> listToSendFiles = new HashMap<String, String>();
	private static SimpleDateFormat sdfm = new SimpleDateFormat("yyyy-MM-dd");

	private static Date diaEnvio=Calendar.getInstance().getTime();
	private static boolean updateEvents=true;
	private static boolean dateFolder=true;
	private static File fileMovedToday = new File("/home/robot/App/Log/MovedToday.txt");
	private static FileWriter writer=null;

	
	public static void main(String[] args)
	{		
		HashMap<String, String> filesMovedToday = new HashMap<>();

		if(!fileMovedToday.exists())
		{
			try {
				if (fileMovedToday.createNewFile()){
				    System.out.println("File is created!");
				   }else{
				    System.out.println("File already exists.");
				   }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
		    System.out.println("File "+fileMovedToday.getAbsolutePath()+ " already exists.");

			ArrayList<String> lines= Utils.readLinesInFile(fileMovedToday.getAbsolutePath());
			if(lines.size()>0)
			{
				System.out.println("**************************************");
				System.out.println("Files moved today");
				for(String line:lines)
				{
					filesMovedToday.put(line.trim(), line.trim());
					System.out.println(line);
				}
				System.out.println("**************************************");
			}
		}
		
		if(fileMovedToday.exists())
		{
			String stringToday=sdfm.format(Calendar.getInstance().getTime());
			String stringDateFile=sdfm.format(fileMovedToday.lastModified());
			if(stringToday.contains(stringDateFile))
			{
				System.out.println("File with todays keys found!");
				System.out.println(stringToday + "   "+stringDateFile);
				try {
					writer=new FileWriter(fileMovedToday);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("File with todays keys not found anywhere!");
				try {
					writer=new FileWriter(fileMovedToday,false);
					writer.flush();
					writer.close();
					writer=new FileWriter(fileMovedToday);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		Date diaEnvio=Calendar.getInstance().getTime();
		String pathTodayFilesCisa = "";
//		System.out.println("***********************************************************"
//									+ "USAGE                                                                                   "
//									+ "This program accepts four arguments:                                 "
//									+ "1. Path for CISA folder origin files                                          "
//									+ "2. Path for aft folder                                                               "
//									+ "3. Mailbox                                                                               "
//									+ "4. Not to update events nu                                                     "
//									+ "5. Not use dateFolder nd                                                        "
//									+ "6. YYYY-MM-dd specific date to test                                        "
//									+ "***********************************************************");
		if(args.length>0)
		{
			pathCISA=args[0];
			pathAft=args[1];
			mailBox=args[2];
			
			
			for(int i=0;i<args.length;i++)
			{
				System.out.println(i + " : " + args[i]);
			}
			
			if(args.length>3)
			{
				if(args[3].toLowerCase().contains("nu"))
				{
					updateEvents=false;
				}
				if(args[4].toLowerCase().contains("nd"))
				{
					dateFolder=false;
				}
			}
			if(args.length>5)
			{
				try {
					diaEnvio = sdfls.parse(args[5]);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			
		}
		else
		{
			pathCISA="/home/moises/Files/Clients/Limine/TempCISA";
			pathAft="/home/moises/Files/Clients/Limine/TempCISA";
			mailBox="GE102136";
			pathTodayFilesCisa=pathCISA+File.separator+"20211203";
			try {
				diaEnvio=sdfls.parse("2021-12-03");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		
		if(dateFolder)
		{
			pathTodayFilesCisa = pathCISA+File.separator+sdf.format(Calendar.getInstance().getTime());
		}
		else {
			pathTodayFilesCisa = pathCISA;
		}
		
		if(updateEvents)
		{
			ConnectorMariaDB.connect();
		}
		String stringDiaEnvio =  sdfls.format(diaEnvio);
		updateUnavanti(ConnectorMariaDB.conn);
		System.out.println("PathCISA: "+pathCISA);
		System.out.println("PathAft: "+pathAft);
		System.out.println("MailBox: "+mailBox);
		
		String pathAftEnviados=pathAft+File.separator+"data"+File.separator+"LOGBOX"+File.separator+"AFTClient_ArquivosEnviados.xml";
		String pathAftOutbox=pathAft+File.separator+"data"+File.separator+"OUTBOX"+File.separator+mailBox;
		String pathAftInbox=pathAft+File.separator+"data"+File.separator+"INBOX"+File.separator+mailBox;
		String sentTodayPatternString=sdfls.format(diaEnvio);
		
		ArrayList<String> lines = Utils.readLinesInFileUTF8(pathAftEnviados);
		ArrayList<String> operationsSentAlready = readOperationsSent(ConnectorMariaDB.conn);
		System.out.println("Just checked "+operationsSentAlready.size()+ " were sent and registered at the database!");		
		System.out.println("Path AFT Enviados: " + pathAftEnviados);
		String dataShortString=sdfls.format(diaEnvio);
		for(String nomeCnab:operationsSentAlready)
		{
			if(listSentFiles.get(nomeCnab)==null)
			{
				listSentFiles.put(nomeCnab,nomeCnab);
				System.out.println("  - "+nomeCnab);
			}
		}

		for(String line:lines)
		{
//			System.out.println("Sent : "+line);
			String[] fieldsStrings = line.split("\"");
			
			if(line.toLowerCase().contains("signature") && line.contains(dataShortString))			
			{
//				System.out.println("Sent : "+line);
				String remessaFile=fieldsStrings[3];
				String stringHoraEnvio=fieldsStrings[7];
				Date horaEnvio = Calendar.getInstance().getTime();
//				for(int i=0;i<fieldsStrings.length;i++)
//				{
//					System.out.println(i+" - " + fieldsStrings[i]);
//				}
				if(updateEvents)
				{
//					System.out.println("Name: "+remessaFile+ " Time sent: "+horaEnvio);
					updateFileSent(ConnectorMariaDB.conn,remessaFile, horaEnvio);
				}
				else {
					System.out.println("Name: "+remessaFile+ " Time sent: "+stringHoraEnvio);
				}
				if(listSentFiles.get(remessaFile)==null)
				{
					listSentFiles.put(remessaFile, remessaFile);
				}
//				break;
			}
		}
			
		System.out.println("Checking CISA files for today at " + pathTodayFilesCisa);
		
		List<File> listCurrentFiles = Utils.listf(pathTodayFilesCisa,false);
		for(File file: listCurrentFiles)
		{
			
			Path path = Paths.get(file.getAbsolutePath());
			FileTime creationTime=null;
			Date creationDate=null;
			try {
				creationTime = (FileTime) Files.getAttribute(path, "creationTime");
				creationDate = new Date(creationTime.toMillis());
			} catch (IOException e) {
				e.printStackTrace();
			}
			String stringCreationDate = sdfls.format(creationDate);
//			System.out.println("LCF: "+file.getAbsolutePath()+ " -> " +file.getName() +" Created at " + stringCreationDate);
			if(!stringCreationDate.contains(stringDiaEnvio))
			{
				continue;
			}
			
			if(file.getName().toLowerCase().endsWith(".rem") || file.getName().toLowerCase().endsWith(".txt"))
			{			
//				System.out.println(file.getAbsolutePath()+ " -> " +file.getName());
				if(listSentFiles.get(file.getName())==null)
				{
					listToSendFiles.put(file.getName(),file.getAbsolutePath());
					System.out.println(file.getName()+ " append to send!");
				}
				else {
//					System.out.println(file.getName()+ " sent already!");
					try {
						writer.write(file.getName()+"\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						writer.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}			
		}

		System.out.println("Checking the OUTBOX! " +pathAftOutbox);
		List<File> listOutboxFiles = Utils.listf(pathAftOutbox,false);
		for(File file: listOutboxFiles)
		{
			
			if(file.getName().toLowerCase().endsWith(".rem") || file.getName().toLowerCase().endsWith(".txt"))
			{			
				System.out.println(file.getAbsolutePath()+ " -> " +file.getName());
				if(listToSendFiles.get(file.getName())!=null)
				{
					listToSendFiles.remove(file.getName());
					System.out.println(file.getName()+ " moved already!");
					try {
						writer.write(file.getName()+"\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						writer.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}				
			}			
		}			
		
		System.out.println("Checking the history of sent files! " +pathAftOutbox);

		for(String fileSentAlready:filesMovedToday.keySet())
		{
//				System.out.println(fileSentAlready);
				if(listToSendFiles.get(fileSentAlready)!=null)
				{
					listToSendFiles.remove(fileSentAlready);
					System.out.println(" - " +fileSentAlready+ " moved already!");
				}	
		}			
		
		
		if(!listToSendFiles.isEmpty())
		{
			System.out.println("Moving files to OUTBOX");

			for(String keySend:listToSendFiles.keySet())
			{
				String pathDestinyFile=pathAftOutbox+File.separator+keySend;
				File originFile=new File(listToSendFiles.get(keySend)) ;
				File destinyFile = new File(pathDestinyFile);		
				Utils.copyFile(originFile, destinyFile);		
				try {
					writer.write(originFile.getName()+"\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(args.length==0)
				{
					listSentFiles.put(keySend,keySend);
				}
			}
		}
		else {
			System.out.println("No files to send!");
		}
		
		System.out.println("Checking Inbox CISA files at " + pathAftInbox);
		List<File> listFilesAtInbox=Utils.listf(pathAftInbox,false);
		for(File file: listFilesAtInbox)
		{
			if(file.getName().toLowerCase().startsWith("retorno") && file.getName().toLowerCase().contains(sdfs.format(diaEnvio)))
			{
//				System.out.println("IF: "+file.getAbsolutePath() + " : "+file.getName());
				String remessaName=file.getName().replaceAll("Retorno_", "");
				//remessaName=remessaName.substring(0,4)+remessaName.substring(12);
				System.out.println("FileName: "+file.getName()+" RemessaName: "+remessaName);
				if(listSentFiles.get(remessaName)!=null)
				{
					System.out.println("Confirmed!");
					if(updateEvents)
					{
						updateFileReceived(ConnectorMariaDB.conn, remessaName, Calendar.getInstance().getTime());
					}
				}
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void updateFileReceived(Connection conn, String name, Date horaEnvio)	
	{
		String query="select * from operacoes_multipag"
				+ " where"
				+ " data_entrada='"+sdfls.format(diaEnvio)+"'"
				+ " and nome_cnab='"+name+"'";
//		System.out.println(query);
		Statement st=null;
		try {
			st=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ResultSet rs=null;
		int id =0;
		Date horaEnvioBancoDate=null;
		try {
			rs=st.executeQuery(query);
			while(rs.next())
			{
				id = rs.getInt("id_operacoes_multipag");				
				horaEnvioBancoDate=rs.getDate("hora_envio_confirmacao");
				System.out.println("  --" +id + " : "+horaEnvioBancoDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("");
		if(id!=0 && horaEnvioBancoDate==null)
		{
			String update="update operacoes_multipag"
									+ " set hora_envio_confirmacao='"+sdfl.format(horaEnvio)+"'"
									+ " where id_operacoes_multipag="+id;
			System.out.println(update);

			try {
				st.executeUpdate(update);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static ArrayList<String> readOperationsSent(Connection conn)	
	{
		ArrayList<String>  nomesCnab = new ArrayList<>();
		String query="select * from operacoes_multipag"
				+ " where"
				+ " data_entrada='"+sdfls.format(Calendar.getInstance().getTime())+"'"
				+ " and hora_envio is not null";
		System.out.println(query);
		Statement st=null;
		try {
			st=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ResultSet rs=null;
		try {
			rs=st.executeQuery(query);
			while(rs.next())
			{			
				nomesCnab.add(rs.getString("nome_cnab"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nomesCnab;
	}
	
	public static void updateUnavanti(Connection conn)
	{
		String update = "update operacoes_multipag set  hora_envio=CURRENT_TIMESTAMP where codigo_banco_fundo = '460'"
									+ " and data_entrada = "+"'"+sdfls.format(Calendar.getInstance().getTime())+"'"
									+ " and hora_envio is null";
		System.out.println(update);
		Statement statement=null;
		try {
			statement=conn.createStatement();
			statement.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void updateFileSent(Connection conn, String name, Date horaEnvio)	
	{
		String query="select * from operacoes_multipag"
				+ " where"
				+ " data_entrada='"+sdfls.format(diaEnvio)+"'"
				+ " and nome_cnab='"+name+"'";
//		System.out.println(query);
		Statement st=null;
		try {
			st=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ResultSet rs=null;
		int id =0;
		Date horaEnvioBancoDate=null;
		try {
			rs=st.executeQuery(query);
			while(rs.next())
			{
				id = rs.getInt("id_operacoes_multipag");				
				horaEnvioBancoDate=rs.getDate("hora_envio");
//				System.out.println("  --" +id + " : "+horaEnvioBancoDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		System.out.println("");
		if(id!=0 && horaEnvioBancoDate==null)
		{
			String update="update operacoes_multipag"
									+ " set hora_envio='"+sdfl.format(horaEnvio)+"'"
									+ " where id_operacoes_multipag="+id;
			System.out.println(update);

			try {
				st.executeUpdate(update);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
