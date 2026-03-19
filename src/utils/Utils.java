package utils;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
//import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cnab.Field;
import cnab.FieldCNAB;
import elegibilidade.AcumuladorValorNominal;
import serpro.ErrorResponse;

public class Utils {
	public static SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
	private static DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
	private static DecimalFormat fnr = new DecimalFormat("#,###.00",otherSymbols);
	private static DecimalFormat fnp = new DecimalFormat("#,###.000",otherSymbols);
	private static DecimalFormat fnc = new DecimalFormat("#,###.00000000",otherSymbols);
	private static DecimalFormat fn = new DecimalFormat("#,###.00",otherSymbols);
	private static DecimalFormat fnn = new DecimalFormat("#.##",otherSymbols);
	public static SimpleDateFormat sdfp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public static String signature = "<br>Atenciosamente, <br><br> \n"
			+ "_________________________<br>";
	public static String signatureOperacao = "<br>Atenciosamente, <br><br> \n"
			+ "_________________________<br>";

	final static String Digits     = "(\\p{Digit}+)";
	final static String HexDigits  = "(\\p{XDigit}+)";
	// an exponent is 'e' or 'E' followed by an optionally 
	// signed decimal integer.
	final static String Exp        = "[eE][+-]?"+Digits;
	final static String fpRegex    =
	    ("[\\x00-\\x20]*"+ // Optional leading "whitespace"
	    "[+-]?(" +         // Optional sign character
	    "NaN|" +           // "NaN" string
	    "Infinity|" +      // "Infinity" string

	    // A decimal floating-point string representing a finite positive
	    // number without a leading sign has at most five basic pieces:
	    // Digits . Digits ExponentPart FloatTypeSuffix
	    // 
	    // Since this method allows integer-only strings as input
	    // in addition to strings of floating-point literals, the
	    // two sub-patterns below are simplifications of the grammar
	    // productions from the Java Language Specification, 2nd 
	    // edition, section 3.10.2.

	    // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
	    "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

	    // . Digits ExponentPart_opt FloatTypeSuffix_opt
	    "(\\.("+Digits+")("+Exp+")?)|"+

	    // Hexadecimal strings
	    "((" +
	    // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
	    "(0[xX]" + HexDigits + "(\\.)?)|" +

	    // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
	    "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

	    ")[pP][+-]?" + Digits + "))" +
	    "[fFdD]?))" +
	    "[\\x00-\\x20]*");// Optional trailing "whitespace"

	
	public static void main(String[] args)
	{
		
//		String text = "GTI1243432567734524524.zip";
//		int position=Utils.positionNumber(text);
//		System.out.println("NumberPosition: " + position  + " Text: " + text);
//		String stringDate="30/12/2017";
//		System.out.println(parseDateDMY(stringDate));
//		
//		String pathFile = "R:\\Carteira\\Processar\\20180103__Carteira_Fundos.html";
//		File input = new File(pathFile);
//		ArrayList<String> lines = rowsFromHtmlTables(input);
//		System.out.println("Begins here!");
//		for(String line:lines)
//		{
//			System.out.println(line);
//		}
//		
		
		String cnpjCrude = "12.400.421/0001-99";
		cnpjCrude = "12 400.421/0001-99";
		System.out.println(cleanString(cnpjCrude));
//		System.out.println(cleanCNPJ(cnpjCrude));
//		String numberCrude = "55.000,05";
//		System.out.println(cleanNumber(numberCrude));
//		
//		if (Pattern.matches(fpRegex, "abracadabra")){
//		    Double.valueOf("abracadabra"); // Will not throw NumberFormatException
//		} 
//		else {
//			System.out.println("Not number");
//		}
//		
//		testSortHashMap();
		
		double testNumber = 1324134134453245.13465;

		
//		System.out.println("Test number: " + testNumber + " Formatted: " + fnr.format(testNumber));
//
//		Date initialDate = null;
//		Date finalDate = null;
////		Connector.connect();
//
//		try {
//			initialDate = sdfd.parse("2020-09-17");
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//		
//		try {
//			finalDate = sdfd.parse("2020-10-14");
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		System.out.println("InitialDate: " + sdfd.format(initialDate));
//		System.out.println("FinalDate: " + sdfd.format(finalDate));
//		
//		System.out.println("Period from initial to final: " + normalDays(initialDate, finalDate));
//		System.out.println("Period from final to initial: " + normalDays(finalDate, initialDate));
//		System.out.println("Period from initial to initial: " + normalDays(initialDate, initialDate));
//
//
////		System.out.println("AimDate: " + sdfd.format(workingDay(initialDate,1,Connector.conn).getData()));
////		System.out.println("AimDate: " + sdfd.format(workingDay(initialDate,-1,Connector.conn).getData()));
//		
//		String testeCadastro="36523456000134";
		String testeCadastro2="adfadfa 36.523.456/0001-34, sdf sdfa adfa 231.015.249-44, asdf sdfsf fd";
		System.out.println("CNPJ: "+extractCNPJ(testeCadastro2));
		System.out.println("CPF: "+extractCPF(testeCadastro2));
//		System.out.println("Cadastro:  " + testeCadastro);
//		System.out.println("Formatado: " + formatCNPJ(testeCadastro));
//		System.out.println("Formatado2: " + cleanCNPJ(testeCadastro));
//		String raizCadastro = testeCadastro.substring(0,8);
//		String teste="loca����o de ve��culos 2.335 MARANH��O";
//		System.out.println(normalizeUri(teste));
//		System.out.println(raizCadastro);
//		System.out.println("Just to test commit!");
		
		String testString="132413413.4453245.13465";
		String testString2="132413413.4453245,13465";
		cleanDecimalNumber(testString);
		cleanDecimalNumber(testString2);
		cleanDecimalNumber("405,21");
		System.out.println(currencyToFlatInteger(11.321, 3));
		System.out.println(currencyToFlatInteger(1021.12, 2));
		
		String testDateShort = "08/2020";
		Date dataCorreta = parseShortDateMMyyyy(testDateShort);
		System.out.println("ShortDate: "+testDateShort + " Correct: "+dataCorreta);
//		
//		System.out.println(sdfd.format(firstDayLastMonth()));
//		System.out.println(sdfd.format(lastDayLastMonth()));
//		System.out.println("Days between: " +normalDays(firstDayLastMonth(), Calendar.getInstance().getTime()));
//		System.out.println("Days between: " +normalDays(Calendar.getInstance().getTime(),firstDayLastMonth()));
//		System.out.println("*****************************");
//		ArrayList<String> lines = extractLinesFromXLSX("/home/moises/Files/Clients/Limine/PagamentosEmLote/PagamentoEmLote20220106.xlsx");
//		
//		for(String line:lines)
//		{
//			System.out.println(line);
//		}
//		System.out.println("*****************************");
//		
//		uniqueStringPIX(35,"3293319000103_099.txt","5432", "1234567", "23132342233", 123.34);
		
		 String valueCell="44951.00";
		 System.out.println("Number: " +fn.format(10.03));
		 System.out.println("Number: " +fnn.format(10.03));
		 System.out.println("Number: " +cleanDecimalNumber(fnn.format(10.03)));
		 System.out.println(dateFromCellExcel(valueCell));
		 String name01="jordane sperafico";
		 String name02="jordane";
		 System.out.println(calculateLevenshtein(name01, name02));
		 
		 String cpfString="23101424844";
		 if(cpfString.matches("[0-9]{11}"))
		 {
			 System.out.println("This is a CPF "+cpfString);
		 }			
		 /*
		  * 		this.getCodigoDeBarras().setContent(
				this.pagamento.getFavorecido().getNumeroBoleto().substring(0,4)
				+this.pagamento.getFavorecido().getNumeroBoleto().substring(32,33)
				+this.pagamento.getFavorecido().getNumeroBoleto().substring(33,47)
				+this.pagamento.getFavorecido().getNumeroBoleto().substring(4,9)
				+this.pagamento.getFavorecido().getNumeroBoleto().substring(10,20)
				+this.pagamento.getFavorecido().getNumeroBoleto().substring(21,31)
					
		  */
		 String linhaDigitavel="03399814588220000000600002101012471860000010000";
		 System.out.println("LinhaDigitavel: "+linhaDigitavel);
		 System.out.println("SizeOfLinhaDigitavel: "+linhaDigitavel.length());
		 String codigoDeBarras=linhaDigitavel.substring(0,4)
				 										+linhaDigitavel.substring(32,33)
				 										+linhaDigitavel.substring(33,47)
				 										+linhaDigitavel.substring(4,9)
				 										+linhaDigitavel.substring(10,20)
				 										+linhaDigitavel.substring(21,31)
				 										;
		 System.out.println("Codigo de barras: "+codigoDeBarras);
		 System.out.println("SizeOfCodigoDeBarras: "+codigoDeBarras.length());
		 int fatorVencimento=Integer.parseInt(linhaDigitavel.substring(33,37));

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1997);
		cal.set(Calendar.MONTH, 10-1);
		cal.set(Calendar.DAY_OF_MONTH, 7);
		 System.out.println(cal.getTime());
		 System.out.println(fatorVencimento);
		 cal.add(Calendar.DAY_OF_MONTH, fatorVencimento);
		 System.out.println(cal.getTime());
		 
		 UUID uuid = UUID.randomUUID();
		 System.out.println(uuid.toString());
		 
	}
	
	public static void deleteDirectory(File file) 
	{
		    File[] contents = file.listFiles();
		    if (contents != null) {
		        for (File f : contents) {
		            if (! Files.isSymbolicLink(f.toPath())) {
		                deleteDirectory(f);
		            }
		        }
		    }
		    file.delete();
	}
	
	public static int min(int... numbers) {
        return Arrays.stream(numbers)
          .min().orElse(Integer.MAX_VALUE);
    }
	
	public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }
	
	public static int calculateLevenshtein(String x, String y) {
	    int[][] dp = new int[x.length() + 1][y.length() + 1];

	    for (int i = 0; i <= x.length(); i++) {
	        for (int j = 0; j <= y.length(); j++) {
	            if (i == 0) {
	                dp[i][j] = j;
	            }
	            else if (j == 0) {
	                dp[i][j] = i;
	            }
	            else {
	                dp[i][j] = min(dp[i - 1][j - 1] 
	                 + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), 
	                  dp[i - 1][j] + 1, 
	                  dp[i][j - 1] + 1);
	            }
	        }
	    }

	    return dp[x.length()][y.length()];
	}
	public static String extractCNPJ(String line)
	{
		String pattern="(^\\d{2}.\\d{3}.\\d{3}/\\d{4}-\\d{2}$)";
		String[] words = line.replaceAll(",", "").split(" ");
		String cnpj=null;
		for(int i=0;i<words.length;i++)
		{
			if(words[i].matches(pattern))
			{
				cnpj=words[i];
				break;
			}
		}
		return cnpj;
	}
	
	public static String extractCPF(String line)
	{
		String pattern="(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)";
		String[] words = line.replaceAll(",", "").split(" ");
		String cpf=null;
		for(int i=0;i<words.length;i++)
		{
			if(words[i].matches(pattern))
			{
				cpf=words[i];
				break;
			}
		}
		return cpf;
	}
	
	public static void unzipFileToFolder(Path sourceFile, Path targetFolder) throws IOException {

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(sourceFile.toFile()))) {

            // list files in zip
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {

                boolean isDirectory = false;
                // example 1.1
                // some zip stored files and folders separately
                // e.g data/
                //     data/folder/
                //     data/folder/file.txt
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }
                else {
//					System.out.println("ZipEntry: "+zipEntry.getName());
				}

                Path newPath = zipSlipProtect(zipEntry, targetFolder);

                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {

                    // example 1.2
                    // some zip stored file path only, need create parent directories
                    // e.g data/folder/file.txt
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }

                    // copy files, nio
//                    System.out.println("ZipEntry: "+zipEntry.getName());
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);

                    // copy files, classic
                    /*try (FileOutputStream fos = new FileOutputStream(newPath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }*/
                }

                zipEntry = zis.getNextEntry();

            }
            zis.closeEntry();

        }

    }
	
	public static void unzipFolder(Path source, Path target) throws IOException {

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {

            // list files in zip
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {

                boolean isDirectory = false;
                // example 1.1
                // some zip stored files and folders separately
                // e.g data/
                //     data/folder/
                //     data/folder/file.txt
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }

                Path newPath = zipSlipProtect(zipEntry, target);

                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {

                    // example 1.2
                    // some zip stored file path only, need create parent directories
                    // e.g data/folder/file.txt
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }

                    // copy files, nio
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);

                    // copy files, classic
                    /*try (FileOutputStream fos = new FileOutputStream(newPath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }*/
                }

                zipEntry = zis.getNextEntry();

            }
            zis.closeEntry();

        }

    }

    // protect zip slip attack
    public static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir)
        throws IOException {

        // test zip slip vulnerability
        // Path targetDirResolved = targetDir.resolve("../../" + zipEntry.getName());

        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        // make sure normalized file still has targetDir as its prefix
        // else throws exception
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }

        return normalizePath;
    }
	
	public static Date parseShortDateMMyyyy(String rawDate)
	{
		int month = Integer.parseInt(rawDate.substring(0,2))-1;
		int year = Integer.parseInt(rawDate.substring(3,7));
		Calendar cal= Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, day);
		
		return cal.getTime();
	}
	
	public static ArrayList<String> extractXMLFields(String xmlString, String nameField)
	{
		ArrayList<String> fields = new ArrayList<String>();
		String startSeparator="<"+nameField;
		String endSeparator="</"+nameField+">";
		
		String[] pieces = xmlString.split(startSeparator);
		int startToSearch=0;
		if(!xmlString.startsWith(startSeparator))
		{
			startToSearch=1;
		}
		
		for(int i=startToSearch; i< pieces.length; i++)
		{
			String[] subPieces = pieces[i].split(endSeparator);
			String field = startSeparator+subPieces[0]+endSeparator;
			fields.add(field);
//			System.out.println("Found: "+field);
		}
		return fields;
	}

	public static String extractXMLFieldWithParameters(String xmlString, String nameField)
	{
		String field="";
		String startSeparatorBegin="<"+nameField;
		String startSeparatorEnd=">";
		
		String startSeparator = startSeparatorBegin+xmlString.split(startSeparatorBegin)[1].split(startSeparatorEnd)[0]+startSeparatorEnd;
		
//		System.out.println("StartSeparator:" + startSeparator);
		
		String endSeparator="</"+nameField+">";
		
		String[] pieces = xmlString.split(startSeparator);
		int startToSearch=0;
		for(int i=1; i< pieces.length; i++)
		{
			String[] subPieces = pieces[i].split(endSeparator);
			field = subPieces[0];
//			System.out.println("Found: "+field);
			break;
		}
		field=startSeparator+field+endSeparator;
		return field;
	}

	
	public static String extractXMLField(String xmlString, String nameField)
	{
		String field="";
		String startSeparator="<"+nameField+">";
		String endSeparator="</"+nameField+">";
		
		String[] pieces = xmlString.split(startSeparator);
		int startToSearch=0;
//		System.out.println(xmlString);
//		for(int i=0; i< pieces.length; i++)
//		{
//			System.out.println(i+": "+pieces[i]);
//			String[] subPieces = pieces[i].split(endSeparator);
//			for(int is=0;is<subPieces.length;is++)
//			{
//				System.out.println("   -"+is+":"+subPieces[is]);
//			}
//		}
		for(int i=1; i< pieces.length; i++)
		{
//			System.out.println(pieces[i]);
			String[] subPieces = pieces[i].split(endSeparator);
//			for(int is=0;is<subPieces.length;is++)
//			{
//				System.out.println("   -"+is+":"+subPieces[is]);
//			}
			field = subPieces[0];

//			System.out.println("Found: "+field);
			break;
		}
		return field;
	}

	public static String extractXMLParameter(String xmlString, String nameParameter)
	{
		String stringParameter="";
		String startSeparator=nameParameter+"=\"";
		String endSeparator="\"";
		String[] pieces = xmlString.split(startSeparator);
		if(pieces.length>1)
		{
			System.out.println("XML:" +xmlString);
			System.out.println("NameParameter:" +nameParameter);
			String[] values = pieces[1].split(endSeparator);
			stringParameter=values[0];
			System.out.println("ValueParameter:" +stringParameter);
		}
		return stringParameter;
	}

	public static String extractXMLParameterString(String xmlString, String nameParameter)
	{
		String completeStringParameter="";
		String stringParameter="";
		String startSeparator=nameParameter+"=\"";
		String endSeparator="\"";
		String[] pieces = xmlString.split(startSeparator);
		if(pieces.length>1)
		{
			System.out.println("XML:" +xmlString);
			System.out.println("NameParameter:" +nameParameter);
			String[] values = pieces[1].split(endSeparator);
			stringParameter=values[0];
			System.out.println("ValueParameter:" +stringParameter);
		}
		completeStringParameter=nameParameter+"=\"" + stringParameter+"\"";
		return completeStringParameter;
	}

	public static String buildXMLParameterString(String nameParameter, String valueParameter)
	{
		String completeStringParameter="";
		completeStringParameter=nameParameter+"=\"" + valueParameter+"\"";
		return completeStringParameter;
	}
	public static String  uniqueStringPIX(int size, String nomeArquivo, String agenciaRecebedor, String contaRecebedor, String cadastroRecebedor, double valor)
	{
		long valorLong = (long) valor*100;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd"); 
		nomeArquivo=nomeArquivo.replaceAll("_", "").replaceAll("\\.", "");
		String prefix = nomeArquivo.substring(nomeArquivo.length()-11, nomeArquivo.length()-3);
		String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
	    String valueString = fillWithChar(15, Long.toString(valorLong), '0');
	    String uniqueString="";
	    
	    char[] upperChars = upperAlphabet.toCharArray();
	    char[] lowerChars = lowerAlphabet.toCharArray();
	    
	    /*
	     * Datetime yyyyMMdd size 8
	     * AgenciaRecebedor size 4
	     * ContaRecebedor size 8
	     * Valor size 5
	     */
	    agenciaRecebedor=fillWithChar(4, agenciaRecebedor, '0');
	    contaRecebedor=fillWithChar(8, contaRecebedor, '0');
	    
	    String numericString = prefix+agenciaRecebedor+contaRecebedor+valueString;
//	    char[] numericChars=numericString.toCharArray();
//	    for(int i=0;i<numericChars.length;i++)
//	    {
//	    	int number=Integer.parseInt(String.valueOf(numericChars[i]));
//	    	if((number+i)%2==0 )
//	    	{
//	    			uniqueString+=upperChars[number];
//	    	}
//	    	else 
//	    	{
//	    			uniqueString+=lowerChars[number];
//			}
//	    }
//	    
//	    
//	    
//	    System.out.println(numericString+" Size "+numericString.length());
//	    System.out.println(uniqueString+" Size "+numericString.length());
//	    return uniqueString;
	    return numericString;
	}
	
	public static String fillWithChar(int size, String original, char fillChar)
	{
		String result="";
		if(original.length()>size)
		{
			result = original.substring(0,size);
		}
		else
		{
			result=original;
			int zerosLeft=size - original.length();
			for(int i=0;i<zerosLeft;i++)
			{
				result=fillChar+result;
			}
			
		}
		return result;
	}
	
	public static Date firstDayLastMonth()
	{
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.MONTH,-1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}
	
	public static Date lastDayLastMonth()
	{
		Calendar cal=Calendar.getInstance();		
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH,-1);
		return cal.getTime();
	}

	public static List<File> listf(String directoryName, boolean verbose) {
        File directory = new File(directoryName);
        List<File> resultList = new ArrayList<File>();
        if(directory.exists())
        {

	
	        // get all the files from a directory
	        File[] fList = directory.listFiles();
	        Arrays.sort(fList,Comparator.comparingLong(File::lastModified));
	        resultList.addAll(Arrays.asList(fList));
	        for (File file : fList) 
	        {
	            if (file.isFile()) 
	            {
	            	if(verbose)
	            	{
	            		System.out.println(file.getAbsolutePath());
	            	}
	            } 
	            else if (file.isDirectory()) 
	            {
	                resultList.addAll(listf(file.getAbsolutePath(),false));
	            }
	        }
        
        //System.out.println(fList);

        }
        else
        {
        	System.out.println("Folder: "+directory.getAbsolutePath()+" doesn't exist");
        }
        return resultList;
    }
	
	public static ArrayList<File> listRemessaFiles(String pathFolder)
	{
		ArrayList<File> remessaFiles = new ArrayList<>();
		List<File> candidateFiles = listf(pathFolder,false);
		for(File fileCandidate:candidateFiles)
		{
			if (fileCandidate.getAbsolutePath().toLowerCase().endsWith("rem")
				||fileCandidate.getAbsolutePath().toLowerCase().endsWith("txt")
					)
			{
				remessaFiles.add(fileCandidate);
			}				
		}
		return remessaFiles;
	}
	
	public static ArrayList<File> listPDFFiles(String pathFolder)
	{
		ArrayList<File> pdfFiles = new ArrayList<>();
		List<File> candidateFiles = listf(pathFolder,false);
		for(File fileCandidate:candidateFiles)
		{
			if (fileCandidate.getAbsolutePath().toLowerCase().endsWith("pdf")
					)
			{
				pdfFiles.add(fileCandidate);
			}				
		}
		return pdfFiles;
	}
	
	public static List<File> listFilesOnly(String directoryName) {
        File directory = new File(directoryName);

        List<File> resultList = new ArrayList<File>();
        FileFilter fileFilter = new FileFilter(){
            public boolean accept(File dir) {          
               if (dir.isFile()) {
                  return true;
               } else {
                  return false;
               }
            }
         };  

        // get all the files from a directory
        File[] fList = directory.listFiles(fileFilter);
        resultList.addAll(Arrays.asList(fList));
        for (File file : fList) 
        {
            if (file.isFile()) 
            {
                //System.out.println(file.getAbsolutePath());
            } 
        }
        //System.out.println(fList);
        return resultList;
    } 
	
	public static List<File> listFilesOnlyFirstLevel(String directoryName) {
        File directory = new File(directoryName);

        List<File> resultList = new ArrayList<File>();

        // get all the files from a directory
        File[] fList = directory.listFiles();
        resultList.addAll(Arrays.asList(fList));
        for (File file : fList) 
        {
            if (file.isFile()) 
            {
                //System.out.println(file.getAbsolutePath());
            } 
        }
        //System.out.println(fList);
        return resultList;
    } 
	
	public static String currencyToFlatInteger(double value, int decimals)
	{
		System.out.println("Original value: "+value + " with "+decimals+" decimals");
		long valueInt = (long) (value*Math.pow(10, decimals));
		return Long.toString(valueInt);
	}
	public static void setTimeZone()
	{
		TimeZone tz = TimeZone.getTimeZone("America/Argentina/Buenos_Aires");
		TimeZone.setDefault(tz);
	}
	public static String normalizeUri(String s) 
	{
		s = Normalizer.normalize(s, Normalizer.Form.NFD);
		s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
//        String r = StringUtils.stripAccents(s);
        return s;
    }
	
	public static String formatCPFCNPJ(String cadastro)
	{
		// Exemplo: CPF: 31894945384 318.949.453-84
		
		if(cadastro.length()==11)
		{
			return cadastro.substring(0,3)
					+"."+cadastro.substring(3,6)
					+"."+cadastro.substring(6,9)
					+"-"+cadastro.substring(9,11)
					;
		}
		// Exemplo: CNPJ: 42.318.949/0001-84
		else if(cadastro.length()==14)
		{
			return cadastro.substring(0,2)
					+"."+cadastro.substring(2,5)
					+"."+cadastro.substring(5,8)
					+"/"+cadastro.substring(8,12)
					+"-"+cadastro.substring(12,14)
					;
		}
		else {
			return null;
		}
	}
	
	public static boolean javascriptClick(WebDriver driver, WebElement webElement)
	{		
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", webElement);
		return true;
	}

	public static String toTitleCaseWord(String word){
	    return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
	}
	
	public static String toTitleCasePhrase(String phrase){	    
	    String[] splitPhrase = phrase.split(" ");
	    String result = "";
	
	    for(String word: splitPhrase){
	        result += toTitleCaseWord(word) + " ";
	    }
	    return result.trim();
	}
	public static String formatCNPJCPF(String cadastro)
	{
		// Exemplo: CNPJ: 42.318.949/0001-84
		if(cadastro.length()==14)
		{
			return cadastro.substring(0,2)
					+"."+cadastro.substring(2,5)
					+"."+cadastro.substring(5,8)
					+"/"+cadastro.substring(8,12)
					+"-"+cadastro.substring(12,14)
					;
		}
		else
		{
			// CNPJ: 231.014.248-44
			return cadastro.substring(0,3)
					+"."+cadastro.substring(3,6)
					+"."+cadastro.substring(6,9)
					+"-"+cadastro.substring(9,11)
					;
		}
	}
	
	public static String replaceFieldContent(String line, FieldCNAB field)
	{
		char[] charLine = line.toCharArray();
		System.out.println("OriginalSize: " + charLine.length);
		char[] charContent = field.getContent().toUpperCase().toCharArray();
		System.out.println("ContentSize: " + charContent.length + " '" + field.getContent()+"'");
		char[] charOutput = new char[charLine.length];
		System.out.println("Begin: "+field.getBegin());
		System.out.println("End: "+field.getEnd());
		
		int indexBegin=field.getBegin()-1;
		int indexEnd=field.getEnd()-1;
		int j=0;
		for(int i=0; i<charLine.length;i++)
		{
			if(i>=indexBegin && i<=indexEnd)
			{
//				System.out.println("i="+i+" j="+j);
				charOutput[i]=charContent[j];
				j++;
			}
			else
			{
				charOutput[i]=charLine[i];
			}
		}
		System.out.println("OrContent:  '" + new String(charLine)+"'");
		System.out.println("NeContent:  '" + new String(charOutput)+"'");
		return new String(charOutput);
	}

//	public static String replaceFieldContent(String line, Field field)
//	{
//		char[] charLine = line.toCharArray();
//		System.out.println("OriginalSize: " + charLine.length);
//		char[] charContent = field.getContent().toUpperCase().toCharArray();
//		System.out.println("ContentSize: " + charContent.length + " '" + field.getContent());
//		char[] charOutput = new char[charLine.length];
//		System.out.println("Begin: "+field.getBegin());
//		System.out.println("End: "+field.getEnd());
//		int indexBegin=field.getBegin()-1;
//		int indexEnd=field.getEnd()-1;
//		int j=0;
//		for(int i=0; i<charLine.length;i++)
//		{
//			if(i>=indexBegin && i<=indexEnd)
//			{
//				System.out.println("i="+i+" j="+j);
//				charOutput[i]=charContent[j];
//				j++;
//			}
//			else
//			{
//				charOutput[i]=charLine[i];
//			}
//		}
//		return new String(charOutput);
//	}

	   public static ArrayList<String> echoAsCSV(Sheet sheet) 
	   {
		   ArrayList<String> lines=new ArrayList<String>();
	        Row row = null;
	        for (int i = 0; i < sheet.getLastRowNum(); i++) 
	        {
	        	String lineString="";
	            row = sheet.getRow(i);
	            for (int j = 0; j < row.getLastCellNum(); j++) 
	            {
	            	lineString+=row.getCell(j).getStringCellValue()+";";
	            	System.out.print("\"" + row.getCell(j) + "\";");
	            }
	            System.out.println();
	            lines.add(lineString);
	        }
	        return lines;
    }
	
	// Convert an XSSFWorkbook to CSV and write to provided OutputStream

	   private static void writeWorkbookAsCSVToOutputStream(HSSFWorkbook workbook, OutputStream out) 
	   {        
	       CSVPrinter csvPrinter = null;       
	       try {       
	           csvPrinter = new CSVPrinter(new OutputStreamWriter(out), CSVFormat.EXCEL.withDelimiter(';'));      
	           
	           FormulaEvaluator formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook)workbook);
	           DataFormatter dataFormatter=new DataFormatter();
	           if (workbook != null) {
	               HSSFSheet sheet = workbook.getSheetAt(0); // Sheet #0 in this example
	               Iterator<Row> rowIterator = sheet.rowIterator();
	               while (rowIterator.hasNext()) {               
	                   Row row = rowIterator.next();
	                   Iterator<Cell> cellIterator = row.cellIterator();
	                   while (cellIterator.hasNext()) 
	                   {
	                       Cell cell = cellIterator.next();
	                       formulaEvaluator.evaluate(cell);
	                       String stringValue=dataFormatter.formatCellValue(cell,formulaEvaluator);
//	                       switch (cell.getCellTypeEnum()) 
//	                       {
//								case STRING:
//			                    	   stringValue=cell.getStringCellValue();
//									break;
//								case NUMERIC:
//			                    	   stringValue=Double.toString(cell.getNumericCellValue());
//									break;		
//								case BOOLEAN:
//										if(cell.getBooleanCellValue())
//										{
//											
//										}
//			                    	   stringValue=Double.toString(cell.getNumericCellValue());
//									break;		
//								default:
//									break;
//							}
	                       
	                       csvPrinter.print(stringValue);
	                   }                   
	                   csvPrinter.println(); // Newline after each row
	               }               
	           }

	       }
	       catch (Exception e) {
	           System.out.println("Failed to write CSV file to output stream: " + e.getMessage());
	       }
	       finally {
	           try {
	               if (csvPrinter != null) {
	                   csvPrinter.flush(); // Flush and close CSVPrinter
	                   csvPrinter.close();
	               }
	           }
	           catch (IOException ioe) {
	               System.out.println("Error when closing CSV Printer: " + ioe.getMessage());
	           }           
	       }
	   }   

	   private static void writeWorkbookAsCSVToOutputStream(XSSFWorkbook workbook, OutputStream out) 
	   {        
	       CSVPrinter csvPrinter = null;       
	       try {       
	           csvPrinter = new CSVPrinter(new OutputStreamWriter(out), CSVFormat.EXCEL.withDelimiter(';'));      
	           
	           FormulaEvaluator formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook)workbook);
	           DataFormatter dataFormatter=new DataFormatter();
	           if (workbook != null) {
	               XSSFSheet sheet = workbook.getSheetAt(0); // Sheet #0 in this example
	               Iterator<Row> rowIterator = sheet.rowIterator();
	               int iRow=0;
	               int numberCols=0;
	               while (rowIterator.hasNext())
	               {               
	            	   System.out.println("Row: "+iRow);
	                   Row row = rowIterator.next();
	                   
	                   Iterator<Cell> cellIterator = row.cellIterator();
	                   int iCol=0;	                   	       
	                   if(iRow==0)
	                   {
	                	   System.out.println("******************************************************************");
	                	   System.out.println("HEADERS ROW");
	                	   System.out.println("******************************************************************");
		                   while (cellIterator.hasNext()) 
		                   {
		                       Cell cell = cellIterator.next();
		                       formulaEvaluator.evaluate(cell);
		                       String stringValue=dataFormatter.formatCellValue(cell,formulaEvaluator);
		                       System.out.println("Col: "+iCol+" "+stringValue);
		                       csvPrinter.print(stringValue);
		                       iCol++;
		                   }     
	                   }
	                   else
	                   {
	                	   for(iCol=0;iCol<numberCols;iCol++)
	                	   {
	                		   Cell cell = row.getCell(iCol);
		                       formulaEvaluator.evaluate(cell);
		                       String stringValue=dataFormatter.formatCellValue(cell,formulaEvaluator);
		                       System.out.println("Col: "+iCol+" "+stringValue);
		                       csvPrinter.print(stringValue);
	                	   }
	                   }
	                   if(iRow==0)
	                   {
	                	   numberCols=iCol;
	                	   System.out.println("NumberOfCols: "+numberCols);
	                   }
	                   iRow++;
	                   csvPrinter.println(); // Newline after each row
	               }               
	           }

	       }
	       catch (Exception e) {
	           System.out.println("Failed to write CSV file to output stream: " + e.getMessage());
	       }
	       finally {
	           try {
	               if (csvPrinter != null) {
	                   csvPrinter.flush(); // Flush and close CSVPrinter
	                   csvPrinter.close();
	               }
	           }
	           catch (IOException ioe) {
	               System.out.println("Error when closing CSV Printer: " + ioe.getMessage());
	           }           
	       }
	   }   

	public static ArrayList<String> extractLinesFromXLSX(String pathFile)
	{
		ArrayList<String> lines = new ArrayList<String>();
		FileInputStream fis=null;
//		OutputStream out=null;
		ByteArrayOutputStream out   = new ByteArrayOutputStream();
		try {
			fis = new FileInputStream(pathFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		XSSFWorkbook wb=null;
//		HSSFWorkbook wb=null;
		try {
//			wb = new HSSFWorkbook(fis);
			wb = new XSSFWorkbook(fis);
			writeWorkbookAsCSVToOutputStream(wb,out);
			String content=out.toString();
			String arraylines[] = content.split("\\r?\\n");
			for(int i=0;i<arraylines.length;i++)
			{
				lines.add(arraylines[i]);
			}
//			System.out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		}


		return lines;
	}
	   
	   
	public static ArrayList<String> extractLinesFromExcel(String pathFile)
	{
		ArrayList<String> lines = new ArrayList<String>();
		try
		{
			FileInputStream fis=new FileInputStream(new File(pathFile));
			XSSFWorkbook wb;
			try 
			{				
				wb = new XSSFWorkbook(fis);
				XSSFSheet sheet=wb.getSheetAt(0);  
				FormulaEvaluator formulaEvaluator=wb.getCreationHelper().createFormulaEvaluator();
				int iLine=0;				
				for(Row row: sheet)     //iteration over row using for each loop  
				{  
					String line="";
//					System.out.println("Line "+iLine + "   --------  Last cell: " + row.getLastCellNum());
					int numberOfCells=row.getLastCellNum();
					for(int iCell=0; iCell<numberOfCells; iCell++)    //iteration over cell using for each loop  
					{  
//						System.out.println(iCell);
						Cell cell=row.getCell(iCell);
//						System.out.print("S:"+cell.getStringCellValue()+ "\t\t");
						if(cell==null)
						{
//							System.out.println("Cell "+iCell + " is null!");
//							System.out.println("Default|");
							line+="; ";
						}
						else {
//							System.out.println("**  "+iCell+ " " + cell.getCellTypeEnum());
							switch(formulaEvaluator.evaluateInCell(cell).getCellType())  
							{  
							
								case NUMERIC:   //field that represents numeric cell type  
								//getting the value of the cell as a number  
//									System.out.println(cell.getNumericCellValue()+ "|");
									if(iCell==0)
									{
										line+=cell.getNumericCellValue();
									}
									else
									{
										line+=";"+cell.getNumericCellValue();
									}
									break;  
								case STRING:    //field that represents string cell type  
								//	getting the value of the cell as a string  
//									System.out.println(cell.getStringCellValue().trim()+ "|");
									if(iCell==0)
									{
										line+=cell.getStringCellValue().trim();
									}
									else
									{
										line+=";"+cell.getStringCellValue().trim();
									}
									break;  
								case BLANK:
//									System.out.println("Blank|");
									line+="; ";
									break;
								case BOOLEAN:
//									System.out.println("Boolean|");
									line+="; ";
									break;
								case ERROR:
//									System.out.println("Error|");
									line+="; ";
									break;
								case FORMULA:
//									System.out.println("Formula|");
									line+="; ";
									break;
								case _NONE:
//									System.out.println("None|");
									line+="; ";
									break;
								default:
//									System.out.println("Default|");
									line+="; ";
									break;
							}  
						}

					}  
//					line+="\r\n";
//					System.out.println("   -------- End of line!\n");
					lines.add(line);
					iLine++;
				}			
			} 
			catch (IOException e) 
			{				
				e.printStackTrace();
				System.out.println("Error opening file "+ pathFile);
				return null;
			}
		}
		catch (Exception e) {

		}
		return lines;
	}
	
	public static Date dateFromCellExcel(String valueCell)
	{
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.YEAR, 1900);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH,1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND, 0);
		int valueInt=(int)Double.parseDouble(valueCell)-2;
//		System.out.println("Number of days since 01/01/1900 "+ valueInt);
		cal.add(Calendar.DAY_OF_MONTH, valueInt);
		return cal.getTime();
	}

	public static ArrayList<String> extractLinesFromExcelAsString(String pathFile)
	{
		ArrayList<String> lines = new ArrayList<String>();
		try {
			FileInputStream fis=new FileInputStream(new File(pathFile));
			XSSFWorkbook wb;
			try {				
				wb = new XSSFWorkbook(fis);
				XSSFSheet sheet=wb.getSheetAt(0);  
				FormulaEvaluator formulaEvaluator=wb.getCreationHelper().createFormulaEvaluator();
				int iLine=0;
				
				DataFormatter formatter = new DataFormatter();
				
				
				for(Row row: sheet)     //iteration over row using for each loop  
				{  
					String line="";
					int iCell=0;
					for(Cell cell: row)    //iteration over cell using for each loop  
					{  
						String stringValue=formatter.formatCellValue(cell);		
						
						switch (cell.getCellType()) {
						case NUMERIC:
							Object o = cell.getNumericCellValue();
							if(iCell==0)
							{
								line+= new BigDecimal(o.toString()).toPlainString();
							}
							else
							{
								line+=";"+new BigDecimal(o.toString()).toPlainString();;
							}  
							
							break;

						case STRING:
							if(iCell==0)
							{
								line+=cell.getStringCellValue();
							}
							else
							{
								line+=";"+cell.getStringCellValue();
							}  
							
							break;
							
						default:
							break;
						}
						
						
						
						iCell++;
					}  
//					line+="\r\n";
//					System.out.println();
					lines.add(line);
				}  
			} catch (IOException e) {				
				e.printStackTrace();
				System.out.println("Error opening file "+ pathFile);
				return null;
			}   
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  
		return lines;
	}	
	
	public static String replaceFieldContent(String line, Field field)
	{
		char[] charLine = line.toCharArray();
		System.out.println("OriginalSize: " + charLine.length);
		char[] charContent = field.getContent().toUpperCase().toCharArray();
		System.out.println("ContentSize: " + charContent.length + " '" + field.getContent()+ "'");
		System.out.println("FieldSize: " + field.getSize());
		char[] charOutput = new char[charLine.length];
		System.out.println("Begin: "+field.getBegin());
		System.out.println("End: "+field.getEnd());
		int indexBegin=field.getBegin()-1;
		int indexEnd=field.getEnd()-1;
		int j=0;
		for(int i=0; i<charLine.length;i++)
		{
			if(i>=indexBegin && i<=indexEnd)
			{
				System.out.println("i="+i+" j="+j + " " + charContent[j] );
				charOutput[i]=charContent[j];
				j++;
			}
			else
			{
				charOutput[i]=charLine[i];
			}
		}
		return new String(charOutput);
	}
	
	public static String replaceFieldContentSilent(String line, FieldCNAB field)
	{
		char[] charLine = line.toCharArray();
		System.out.println("OriginalSize: " + charLine.length);
		char[] charContent = field.getContent().toUpperCase().toCharArray();
		System.out.println("ContentSize: " + charContent.length + " '" + field.getContent()+ "'");
		System.out.println("FieldSize: " + field.getSize());
		char[] charOutput = new char[charLine.length];
		System.out.println("Begin: "+field.getBegin());
		System.out.println("End: "+field.getEnd());
		int indexBegin=field.getBegin()-1;
		int indexEnd=field.getEnd()-1;
		int j=0;
		for(int i=0; i<charLine.length;i++)
		{
			if(i>=indexBegin && i<=indexEnd)
			{
//				System.out.println("i="+i+" j="+j + " " + charContent[j] );
				charOutput[i]=charContent[j];
				j++;
			}
			else
			{
				charOutput[i]=charLine[i];
			}
		}
		return new String(charOutput);
	}
	
	public static String replaceFieldContentSilent(String line, Field field)
	{
		char[] charLine = line.toCharArray();
		System.out.println("OriginalSize: " + charLine.length);
		char[] charContent = field.getContent().toUpperCase().toCharArray();
		System.out.println("ContentSize: " + charContent.length + " '" + field.getContent()+ "'");
		System.out.println("FieldSize: " + field.getSize());
		char[] charOutput = new char[charLine.length];
		System.out.println("Begin: "+field.getBegin());
		System.out.println("End: "+field.getEnd());
		int indexBegin=field.getBegin()-1;
		int indexEnd=field.getEnd()-1;
		int j=0;
		for(int i=0; i<charLine.length;i++)
		{
			if(i>=indexBegin && i<=indexEnd)
			{
//				System.out.println("i="+i+" j="+j + " " + charContent[j] );
				charOutput[i]=charContent[j];
				j++;
			}
			else
			{
				charOutput[i]=charLine[i];
			}
		}
		return new String(charOutput);
	}
	
	public static void doubleToCNABValue(FieldCNAB field, double value)
	{
		String valueString="";
		value=value*Math.pow(10, field.getDecimais());
		Long integerValue = (new Double(value)).longValue();
		valueString = integerValue.toString();
		field.setContent(valueString);
	}
	
	public static String cleanDecimalNumber(String rawDecimalNumber)
	{
		String cleanNumber="";
		char[] charNumber=new char[rawDecimalNumber.length()];
		char[] charReverseNumber=new char[rawDecimalNumber.length()];
		for(int i=0;i<rawDecimalNumber.length();i++)
		{
			charNumber[i]=rawDecimalNumber.charAt(i);
			charReverseNumber[i]=rawDecimalNumber.charAt(rawDecimalNumber.length()-i-1);
		}
//		System.out.println();
//		for(char c:charNumber)
//		{
//			System.out.print(c);
//		}
		boolean point=false;

//		System.out.println();
		int indexDecimal=0;
		for(char c:charReverseNumber)
		{
//			System.out.print(c);
			if(c==',')
			{
//				System.out.println("Found comma");				
				break;
			}
			else if(c=='.')
			{
//				System.out.println("Found point");
				break;
			}
			indexDecimal++;
		}
		
		cleanNumber=rawDecimalNumber.replaceAll(",","").replaceAll("\\.","");
		
		char[] charCleanNumber=new char[cleanNumber.length()+1];
		int decimalPlace=cleanNumber.length()-indexDecimal;
//		
//		System.out.println("CleanNumber: "+cleanNumber);
//		System.out.println("IndexDecimal: "+indexDecimal);
//		System.out.println("DecimalPlace: "+decimalPlace);
		
		for(int i=0;i<=cleanNumber.length();i++)
		{			
			if(i<decimalPlace)
			{
				charCleanNumber[i]=cleanNumber.charAt(i);	
			}
			else
			{
				if(i==decimalPlace)
				{
						charCleanNumber[i]='.';
				}
				else
				{
					charCleanNumber[i]=cleanNumber.charAt(i-1);
				}
			}
			
		}
		cleanNumber=new String(charCleanNumber);
		System.out.println("Final CleanNumber: "+cleanNumber);
		return cleanNumber;
	}
	
	
	public static double doubleFromCNABValue(FieldCNAB field)
	{
		String valueString=field.getContent();
		double value= (double)Long.parseLong(valueString)/Math.pow(10, field.getDecimais());
		return value;
	}
	
	public static ArrayList<String> rowsFromHtmlTables(File inputFile)
	{
		ArrayList<String> lines = new ArrayList<String>();
		Document doc=null;
		try {
			doc = (Document) Jsoup.parse(inputFile,"ISO-8859-1");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Elements rows = doc.select("tr");
		for(Element row:rows)
		{
			Elements columns = row.select("td");
			int iColumn=0;
			String line="";
			
			for(Element column:columns)
			{
				if(iColumn==0)
				{
//					System.out.print(column.text());
					line+=column.text();
				}
				else
				{
//					System.out.print(";"+column.text().trim());
					line+=";"+column.text().trim();
				}
				iColumn++;
			}
			if(line.length()>0)
			{
				lines.add(line);
			}
//			System.out.println();
		}
//		System.out.println("*********************");
//		for(String line:lines)
//		{
//			System.out.println(line);
//		}
//		System.out.println("*********************");
		return lines;
	}

	public static ArrayList<String> rowsFromHtmlTablesNoBlank(File inputFile)
	{
		ArrayList<String> lines = new ArrayList<String>();
		Document doc=null;
		try {
			doc = (Document) Jsoup.parse(inputFile,"ISO-8859-1"); // ISO-8859-1
//			doc = (Document) Jsoup.parse(inputFile,"UTF-8"); // UTF-8
//			doc = (Document) Jsoup.parse(inputFile,"WIN-1252"); // UTF-8
		} catch (IOException e) {
			e.printStackTrace();
		}
		Elements rows = doc.select("tr");
		for(Element row:rows)
		{
			Elements columns = row.select("td");
			int iColumn=0;
			String line="";
			
			for(Element column:columns)
			{
				if(iColumn==0)
				{
//					System.out.print(column.text());
					line+=column.text().replaceAll(";", "");
				}
				else
				{
//					System.out.print(";"+column.text().trim());
					line+=";"+column.text().trim().replaceAll(";", "");
				}
				iColumn++;
			}
			if(line.length()>0 && line.trim().length() >= 3)
			{
//				String lineUtf8="";
//				try {
//					lineUtf8=new String(line.getBytes("ISO-8859-1"), "UTF-8");
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				lines.add(line);
//				System.out.println(line);
//				System.out.println(lineUtf8);
			}
//			System.out.println();
		}
//		System.out.println("*********************");
//		for(String line:lines)
//		{
//			System.out.println(line);
//		}
//		System.out.println("*********************");
		return lines;
	}

	public static ArrayList<String> rowsFromHtmlTablesNoBlankUTF8(File inputFile)
	{
		ArrayList<String> lines = new ArrayList<String>();
		Document doc=null;
		try {
//			doc = (Document) Jsoup.parse(inputFile,"ISO-8859-1"); // ISO-8859-1
			doc = (Document) Jsoup.parse(inputFile,"UTF-8"); // UTF-8
//			doc = (Document) Jsoup.parse(inputFile,"WIN-1252"); // UTF-8
		} catch (IOException e) {
			e.printStackTrace();
		}
		Elements rows = doc.select("tr");
		for(Element row:rows)
		{
			Elements columns = row.select("td");
			int iColumn=0;
			String line="";
			
			for(Element column:columns)
			{
				if(iColumn==0)
				{
//					System.out.print(column.text());
					line+=column.text().replaceAll(";", "");
				}
				else
				{
//					System.out.print(";"+column.text().trim());
					line+=";"+column.text().trim().replaceAll(";", "");
				}
				iColumn++;
			}
			if(line.length()>0 && line.trim().length() >= 3)
			{
//				String lineUtf8="";
//				try {
//					lineUtf8=new String(line.getBytes("ISO-8859-1"), "UTF-8");
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				lines.add(line);
//				System.out.println(line);
//				System.out.println(lineUtf8);
			}
//			System.out.println();
		}
//		System.out.println("*********************");
//		for(String line:lines)
//		{
//			System.out.println(line);
//		}
//		System.out.println("*********************");
		return lines;
	}
	
	
//	public static ArrayList<String> rowsFromHtmlTablesNoBlank(File inputFile)
//	{
//		ArrayList<String> lines = new ArrayList<String>();
//		Document doc=null;
//		try {
//			doc = (Document) Jsoup.parse(inputFile,"UTF-8"); // ISO-8859-1
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Elements rows = doc.select("tr");
//		for(Element row:rows)
//		{
//			Elements columns = row.select("td");
//			int iColumn=0;
//			String line="";
//			
//			for(Element column:columns)
//			{
//				if(iColumn==0)
//				{
////					System.out.print(column.text());
//					line+=column.text();
//				}
//				else
//				{
////					System.out.print(";"+column.text().trim());
//					line+=";"+column.text().trim();
//				}
//				iColumn++;
//			}
//			if(line.length()>0 && line.trim().length() >= 3)
//			{
//				lines.add(line);
//			}
////			System.out.println();
//		}
////		System.out.println("*********************");
////		for(String line:lines)
////		{
////			System.out.println(line);
////		}
////		System.out.println("*********************");
//		return lines;
//	}
	
	
	
	public static String signature(String nameWriter, String emailWriter, String company, String contact)
	{
		String signature="<br>Atenciosamente, <br><br> \n"
			+ "	____________________________________<br>"
			+ " " + nameWriter + "<br> <a href=\"mailto:" + emailWriter +"\">"+ emailWriter +"</a>" +  " <br>\n"
			+ " " + company + "<br>\n"
			+ " " + contact + "<br>\n";
		return signature;
	}

	public static String signature(String nameWriter, String emailWriter)
	{
		String signature="<br>Atenciosamente, <br><br> \n"
			+ "	_________________________<br>"
			+ " " + nameWriter + " <a href=\"mailto:" + emailWriter +"\">"+ emailWriter +"</a>" +  " <br>\n";

		return signature;
	}
	
	public static String signature(String nameWriter, String emailWriter, String phoneWriter)
	{
		String signature="<br>Atenciosamente, <br><br> \n"
			+ "	_______________________________________<br>"
			+ " " + nameWriter + "<br><a href=\"mailto:" + emailWriter +"\">"+ emailWriter +"</a>" +  " <br>\n"
			+ " Tel: " + phoneWriter + " <br>\n";
		return signature;
	}
	
	public static void testSortHashMap()
	{
		 HashMap<String, Integer> hm = new HashMap<String, Integer>();
	        hm.put("Naveen", 2);
	        hm.put("Santosh", 3);
	        hm.put("Ravi", 4);
	        hm.put("Pramod", 1);
	        Set<Entry<String, Integer>> set = hm.entrySet();
	        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(
	                set);
	        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
	            public int compare(Map.Entry<String, Integer> o1,
	                    Map.Entry<String, Integer> o2) {
	                return o2.getValue().compareTo(o1.getValue());
	            }
	        });

	        for (Entry<String, Integer> entry : list) {
	            System.out.println(entry.getValue());

	        }

	}
	
	public static ArrayList<Entry<String, AcumuladorValorNominal>> sortHashMap(HashMap<String, AcumuladorValorNominal> unsortedValorNominal)
	{
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
	        hm.put("Naveen", 2);
	        hm.put("Santosh", 3);
	        hm.put("Ravi", 4);
	        hm.put("Pramod", 1);
	        Set<Entry<String, AcumuladorValorNominal>> set = unsortedValorNominal.entrySet();
	        ArrayList<Entry<String, AcumuladorValorNominal>> list = new ArrayList<Entry<String, AcumuladorValorNominal>>(
	                set);
	        Collections.sort(list, new Comparator<Map.Entry<String, AcumuladorValorNominal>>() {
	            public int compare(Map.Entry<String, AcumuladorValorNominal> o1,
	                    Map.Entry<String, AcumuladorValorNominal> o2) {
	            		if(o1.getValue().getValorNominalTotal() > o2.getValue().getValorNominalTotal())
	            		{
	            			return 1;
	            		}
	            		else if(o1.getValue().getValorNominalTotal() < o2.getValue().getValorNominalTotal()) 
	            		{
	            			return -1;
	            		}
	            		else
	            		{
	            			return 0;
	            		}
	                //return o2.getValue().getValorNominalTotal().compareTo(o1.getValue().getValorNominalTotal());
	            }
	        });

//	        for (Entry<String, AcumuladorValorNominal> entry : list) {
//	            System.out.println(entry.getKey() + "  " + entry.getValue().getValorNominalTotal());
//	        }
	    return list;    
	}

	public static ArrayList<Entry<String, AcumuladorValorNominal>> sortReverseHashMap(HashMap<String, AcumuladorValorNominal> unsortedValorNominal)
	{
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
	        hm.put("Naveen", 2);
	        hm.put("Santosh", 3);
	        hm.put("Ravi", 4);
	        hm.put("Pramod", 1);
	        Set<Entry<String, AcumuladorValorNominal>> set = unsortedValorNominal.entrySet();
	        ArrayList<Entry<String, AcumuladorValorNominal>> list = new ArrayList<Entry<String, AcumuladorValorNominal>>(
	                set);
	        Collections.sort(list, new Comparator<Map.Entry<String, AcumuladorValorNominal>>() {
	            public int compare(Map.Entry<String, AcumuladorValorNominal> o1,
	                    Map.Entry<String, AcumuladorValorNominal> o2) {
	            		if(o1.getValue().getValorNominalTotal() < o2.getValue().getValorNominalTotal())
	            		{
	            			return 1;
	            		}
	            		else if(o1.getValue().getValorNominalTotal() > o2.getValue().getValorNominalTotal()) 
	            		{
	            			return -1;
	            		}
	            		else
	            		{
	            			return 0;
	            		}
	            }
	        });

//	        for (Entry<String, AcumuladorValorNominal> entry : list) {
//	            System.out.println(entry.getKey() + "  " + entry.getValue().getValorNominalTotal());
//	        }
	    return list;    
	}	
	
	public static boolean isDouble(String stringNumber)
	{
		if (Pattern.matches(fpRegex, stringNumber)){
			//Double.valueOf("abracadabra"); // Will not throw NumberFormatException
			return true;
		} 
		else {
			System.out.println(stringNumber);
			return false;
		}
		
	}
	
	public static double cleanNumber(String crudeNumber)
	{		
		double cleanNumber=0.0;
		if(crudeNumber.equals(""))
		{
			return cleanNumber;
		}
		if(crudeNumber.contains("%"))
		{
			if(crudeNumber.contains("("))
			{
				cleanNumber = Double.parseDouble("-"+crudeNumber.replace(".", "").replace(",", ".").replace("(", "").replace(")", "").replace("%", ""))/100.0;
			}
			else
			{
				cleanNumber = Double.parseDouble(crudeNumber.replace(".", "").replace(",", ".").replace("%", ""))/100.0;
			}
		}
		else if(crudeNumber.contains("R$"))
		{
			if(crudeNumber.contains("("))
			{
				cleanNumber = Double.parseDouble("-"+crudeNumber.replace("R$", "").replace(".", "").replace(",", ".").replace("(", "").replace(")", "").trim());
			}
			else
			{
				cleanNumber = Double.parseDouble(crudeNumber.replace("R$", "").replace(".", "").replace(",", ".").replace("(", "").replace(")", "").trim());
			}
		}
		else
		{
			if(crudeNumber.contains("("))
			{
				cleanNumber = Double.parseDouble("-"+crudeNumber.replace(".", "").replace(",", ".").replace("(", "").replace(")", ""));
			}
			else
			{
				cleanNumber = Double.parseDouble(crudeNumber.replace(".", "").replace(",", "."));
			}			
		}
		
//		System.out.println("Number removing points: " + crudeNumber.replace(".", "").replace(",","."));
		return cleanNumber;
	}

	public static double cleanNumberPercentage(String crudeNumber)
	{
		double cleanNumber=0.0;
		if(crudeNumber.equals(""))
		{
			return cleanNumber;
		}

		if(crudeNumber.contains("("))
		{
			cleanNumber = Double.parseDouble("-"+crudeNumber.replace("%", "").replace(".", "").replace(",", ".").replace("(", "").replace(")", ""));
		}
		else
		{
			cleanNumber = Double.parseDouble(crudeNumber.replace("%", "").replace(".", "").replace(",", "."));
		}
//		System.out.println("Number removing points: " + crudeNumber.replace(".", "").replace(",","."));
		return cleanNumber;
	}	
	
	public static String cleanNumberString(String crudeNumber)
	{
		String cleanNumber="";
		cleanNumber = crudeNumber.replace(".", "").replace(",", ".");
//		System.out.println("Number removing points: " + crudeNumber.replace(".", "").replace(",","."));
		return cleanNumber;
	}	
	
	public static String cleanCNPJ(String crudeCNPJ)
	{
		String cleanCNPJ="";
		cleanCNPJ = crudeCNPJ.replaceAll("\\.", "").replaceAll("/", "").replaceAll("-","");
		
		return cleanCNPJ;
	}
	public static String cleanString(String crude)
	{
		String clean="";
		clean=Normalizer.normalize(crude,Normalizer.Form.NFD);
		clean = clean.replaceAll("[^ A-Za-z0-9]","");
		
		return clean;
	}

	
	public static String formatValorBrazil(double valor)
	{
		
		String valorFormatado="";
		if(valor==0)
		{
			return "0,00";
		}
		valorFormatado = fn.format(valor);
		if(valorFormatado.startsWith(","))
		{
			valorFormatado="0"+valorFormatado;
		}
		
		return valorFormatado;
	}
	
	public static String formatValorBrazil3(double valor)
	{
		
		String valorFormatado="";
		if(valor==0)
		{
			return "0,00";
		}
		valorFormatado = fnp.format(valor);
		if(valorFormatado.startsWith(","))
		{
			valorFormatado="0"+valorFormatado;
		}
		
		return valorFormatado;
	}
	
	public static String formatValorBrazilCurrency(double valor)
	{
		
		String valorFormatado="";
		if(valor==0)
		{
			return "0,00";
		}
		valorFormatado = fnr.format(valor);
		
		return valorFormatado;
	}
	
	public static String formatValorBrazilCurrencyWithSymbol(double valor)
	{
		
		String valorFormatado="";
		if(valor==0)
		{
			return "0,00";
		}
		valorFormatado = fnr.format(Math.abs(valor));
		if(valorFormatado.startsWith(","))
		{
			valorFormatado="0"+valorFormatado;
		}
		if(valor<0)
		{
			valorFormatado = "("+valorFormatado+")";
		}
		
		valorFormatado = "R$ "+valorFormatado;
		
		return valorFormatado;
	}	
	
	public static String formatValorBrazilPercentage(double valor)
	{
		
		String valorFormatado="";
		if(valor==0)
		{
			return "0,0";
		}
		valorFormatado = fnr.format(valor);
		
		return valorFormatado;
	}

	public static String formatValorBrazilCota(double valor)
	{
		String valorFormatado="";
		if(valor==0)
		{
			return "0,00000000";
		}
		valorFormatado = fnc.format(valor);
		return valorFormatado;
	}
	
//	public static int workingDays(Connection conn, Date initialDate, Date finalDate)
//	{
//		int workingDays=0;
//		
//		String query = "select count(data) dias from asset.dia_util where data > '" + sdfd.format(initialDate)+"' and data <= '" + sdfd.format(finalDate) + "'";
//		System.out.println(query);
//		Statement st = null;
//		try {
//			st = conn.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			ResultSet rs = st.executeQuery(query);
//			while(rs.next())
//			{
//				workingDays=rs.getInt("dias");
//			} 
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return workingDays;
//	}
//	
//	public static int normalDays(Date d1, Date d2) {
//	    long diff = d2.getTime() - d1.getTime();
//	    int normalDays = (int)(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
////	    if(normalDays>0)
////	    {
////	    	normalDays++;
////	    }
//	    return normalDays;
//	}
	
	public static int normalDays(Date initialDate, Date finalDate)
	{
		int normalDays=0;	
		
//		Date firstDate = sdf.parse("06/24/2017");
//	    Date secondDate = sdf.parse("06/30/2017");

		if(finalDate==null  || initialDate ==null)
		{
			System.out.println("FinalDate: "+finalDate);
			System.out.println("InitialDate: "+initialDate);
			System.exit(1);
		}
		
	    long diffInMillies = finalDate.getTime() - initialDate.getTime();
	    long diff = TimeUnit.DAYS.convert(Math.abs(diffInMillies), TimeUnit.MILLISECONDS);
	    
	    if(diffInMillies<0)
	    {
	    	diff=-diff;
	    }
	    
//		LocalDate inititalLocalDate =  initialDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//		LocalDate finalLocalDate =  finalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//		System.out.println("InitialDate: " + sdfd.format(initialDate));
//		System.out.println("FinalDate: " + sdfd.format(finalDate));
//		normalDays = (int) ChronoUnit.DAYS.between(inititalLocalDate,finalLocalDate);
	    normalDays=(int) diff;
		System.out.println("Period from initial to final: " + normalDays);
		
		return normalDays;
	}
	
	public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}
	
	public static void clearWebElementField(WebElement toClear)
	{
		toClear.sendKeys(Keys.CONTROL+"a");
		toClear.sendKeys(Keys.DELETE);
	}
	
	public static WebElement findElement(WebDriver driver, String searchType, String key, String name)
	{
		System.out.println("Searching " + name + " to click!");
		
		List<WebElement> we = null;
		WebElement result=null;
		if(searchType.toLowerCase().contains("id"))
		{
			we=driver.findElements(By.id(key));
		}
		
		if(searchType.toLowerCase().contains("classname"))
		{
			we=driver.findElements(By.className(key));
		}
		
		if(searchType.toLowerCase().contains("selector"))
		{
			we=driver.findElements(By.cssSelector(key));
		}
		
		if(searchType.toLowerCase().contains("xpath"))
		{
			we=driver.findElements(By.xpath(key));
		}
		
		if(searchType.toLowerCase().contains("text"))
		{
			we=driver.findElements(By.linkText(key));
		}
		
		if(!we.isEmpty())
		{
			if(we.get(0).isDisplayed() && we.get(0).isEnabled())
			{
				result= we.get(0);
			}
		}

		return result;

	}
	
	public static boolean clickElement(WebDriver driver, String searchType, String key, String name)
	{
		System.out.println("Searching " + name + " to click!");
		boolean success=false;
		List<WebElement> we = null;
		if(searchType.toLowerCase().contains("id"))
		{
			we=driver.findElements(By.id(key));
		}
		
		if(searchType.toLowerCase().contains("classname"))
		{
			we=driver.findElements(By.className(key));
		}
		
		if(searchType.toLowerCase().contains("selector"))
		{
			we=driver.findElements(By.cssSelector(key));
		}

		if(searchType.toLowerCase().contains("xpath"))
		{
			we=driver.findElements(By.xpath(key));
		}
		if(searchType.toLowerCase().contains("text"))
		{
			we=driver.findElements(By.linkText(key));
		}
	
		
		if(!we.isEmpty())
		{
			if(we.get(0).isDisplayed() && we.get(0).isEnabled())
			{
				we.get(0).click();
				Utils.waitv(3);
				success=true;
			}
		}
		return success;
	}
	
	public static boolean clickElement(WebDriver driver, String searchType, String key, int delay, String name)
	{
		System.out.println("Searching " + name + " to click!");
		
		boolean success=false;
		List<WebElement> we = null;
		
		if(searchType.toLowerCase().contains("id"))
		{
			we=driver.findElements(By.id(key));
		}
		if(searchType.toLowerCase().contains("classname"))
		{
			we=driver.findElements(By.className(key));
		}
		if(searchType.toLowerCase().contains("selector"))
		{
			we=driver.findElements(By.cssSelector(key));
		}
		if(searchType.toLowerCase().contains("xpath"))
		{
			we=driver.findElements(By.xpath(key));
		}
		
		if(searchType.toLowerCase().contains("text"))
		{
			we=driver.findElements(By.linkText(key));
		}

		
		if(!we.isEmpty())
		{
			if(we.get(0).isDisplayed() && we.get(0).isEnabled())
			{
				we.get(0).click();
				Utils.waitv(delay);
				success=true;
			}
		}
		return success;
	}
	
	public static boolean fillElement(WebDriver driver, String searchType, String key, String content,String name)
	{
		System.out.println("Searching " + name + " to fill with "+content);
		boolean success=false;
		List<WebElement> we = null;
		if(searchType.toLowerCase().contains("id"))
		{
			we=driver.findElements(By.id(key));
		}
		if(searchType.toLowerCase().contains("classname"))
		{
			we=driver.findElements(By.className(key));
		}
		if(searchType.toLowerCase().contains("selector"))
		{
			we=driver.findElements(By.cssSelector(key));
		}
		if(searchType.toLowerCase().contains("xpath"))
		{
			we=driver.findElements(By.xpath(key));
		}
		if(searchType.toLowerCase().contains("text"))
		{
			we=driver.findElements(By.linkText(key));
		}
	
		
		if(!we.isEmpty())
		{
			if(we.get(0).isDisplayed() && we.get(0).isEnabled())
			{
				we.get(0).click();
				Utils.waitv(3);
				clearWebElementField(we.get(0));
				we.get(0).sendKeys(content);
				Utils.waitv(3);
				success=true;
			}
		}
		return success;
	}
	
	public static boolean fillElement(WebDriver driver, String searchType, String key, int delay, String content,String name)
	{
		System.out.println("Searching " + name + " to fill with "+content);
		boolean success=false;
		List<WebElement> we = null;
		if(searchType.toLowerCase().contains("id"))
		{
			we=driver.findElements(By.id(key));
		}
		if(searchType.toLowerCase().contains("classname"))
		{
			we=driver.findElements(By.className(key));
		}
		if(searchType.toLowerCase().contains("selector"))
		{
			we=driver.findElements(By.cssSelector(key));
		}
		if(searchType.toLowerCase().contains("xpath"))
		{
			we=driver.findElements(By.xpath(key));
		}
		if(searchType.toLowerCase().contains("text"))
		{
			we=driver.findElements(By.linkText(key));
		}
	
		
		if(!we.isEmpty())
		{
			if(we.get(0).isDisplayed() && we.get(0).isEnabled())
			{
				we.get(0).click();
				Utils.waitv(3);
				clearWebElementField(we.get(0));
				we.get(0).sendKeys(content);
				Utils.waitv(delay);
				success=true;
			}
		}
		return success;
	}	                             

	
//	public static DiaUtil workingDay(Date initialDate, int days, Connection conn)
//	{
//		String query = "select id_dia_util,data from asset.dia_util where data >= '" + sdfd.format(initialDate) + "' order by id_dia_util limit 1";
////		System.out.println(query);
//		Statement st = null;
//		try {
//			st = conn.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	//	System.out.println(query);
//		
//	//	System.out.println("id_nome_cota nome_cota");
//		int id_dia_util=0;
////		Date initial = null;
//		try {
//			ResultSet rs = st.executeQuery(query);
//			while(rs.next())
//			{
//				id_dia_util = rs.getInt("id_dia_util");
////				initial = rs.getDate("data");
//			} 
//	//		System.out.println("After reading data!");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
////		System.out.println("Initial found: " + sdfd.format(initialUtil.getData()));
//		
//		query = "select id_dia_util, data from asset.dia_util where id_dia_util =" + (id_dia_util+days);
////		System.out.println(query);
//		try {
//			st = conn.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	//	System.out.println(query);
//		
//		int aim_id_dia_util=0;
//		Date aim_date = null;
//		DiaUtil aim = new DiaUtil();
//		try {
//			ResultSet rs = st.executeQuery(query);
//			while(rs.next())
//			{
//				aim_id_dia_util = rs.getInt("id_dia_util");
//				aim_date = rs.getDate("data");
//				aim = new DiaUtil(aim_id_dia_util, aim_date);
//			} 
//	//		System.out.println("After reading data!");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}						
//		return aim;
//	}
	
	public static void wait(double seconds) {
		
		for(int i=1;i<=seconds*1000;i++)
		{
//			System.out.println("Waiting " + i + "/" + seconds);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	public static void waitv(double seconds) {
		
		if(seconds>1.0)
		{
			for(int i=1;i<=seconds*1000;i++)
			{
				System.out.println("Waiting " + i + "/" + seconds);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
		else
		{
			int delta=(int)(seconds*1000);
			try {
				Thread.sleep(delta);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void wait(int seconds) {
		
		for(int i=1;i<=seconds;i++)
		{
//			System.out.println("Waiting " + i + "/" + seconds);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	public static void waitv(String what, int seconds) {
		System.out.print(what + "\n");
		for(int i=1;i<=seconds;i++)
		{
			System.out.print(i + "/" + seconds + " ");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}		
		System.out.println("");
	}

	public static void waitv(int seconds) {
		System.out.print("Waiting ");
		for(int i=1;i<=seconds;i++)
		{
			System.out.print(i + "/" + seconds + " ");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}		
		System.out.println("");
	}
	public static int waitUntilExist(List<WebElement> lista, String descriptor)
	{
		int countSeconds=0;
		while(!existElement(lista) && countSeconds < 60)
		{
			System.out.println("Waiting for " + descriptor + " " + countSeconds + " seconds");
			Utils.wait(1);
			countSeconds++;
		}
		return countSeconds;		
	}
	
	public static boolean existElement(List<WebElement> lista)
	{
		if(lista.size()>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
//	public static String formatCNPJCPF(String cadastro)
//	{
//		String formatted="";
//		if(cadastro.length()==14)
//		{
//			formatted = cadastro.substring(0, 2)+"."+cadastro.substring(2, 5)+"."+cadastro.substring(5,8)+"/"+cadastro.substring(8,12)+"-"+cadastro.substring(12, 14);
//		}
//		if(cadastro.length()==11)
//		{
//			formatted = cadastro.substring(0, 3)+"."+cadastro.substring(3, 6)+"."+cadastro.substring(6,9)+"-"+cadastro.substring(9,11);
//		}
//		return formatted;		
//	}
	
	public static String formatValor(double valor)
	{
		String valorFormatado="";
		valorFormatado = fn.format(valor);
		return valorFormatado;
	}
	
	public static Date parseDateDMY(String stringDate)
	{
		// 28/12/2017
		int year=Integer.parseInt(stringDate.substring(6, 10));
		int month=Integer.parseInt(stringDate.substring(3, 5));
		int day=Integer.parseInt(stringDate.substring(0, 2));
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DAY_OF_MONTH, day);
//		System.out.println("Year: " + year + " Month: " + month + " Day: " + day);
//		System.out.println("Year: " + cal.get(Calendar.YEAR) + " Month: " + cal.get(Calendar.MONTH) + " Day: " + cal.get(Calendar.DAY_OF_MONTH));
//		System.out.println(cal.getTime());
		return cal.getTime();
	}
	
	public static void nada()
	{
		/*
		 * This is for nothing
		 */
	}
	
	public static List<File> filesInDirectory(File directory)
	{		
        List<File> files = (List<File>) FileUtils.listFiles(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        return files;
	}
	
	public static ArrayList<File> filesOnlyInDirectory(File directory)
	{		
		ArrayList<File> files = new ArrayList<File>();
        File[] filesToTest = directory.listFiles();
        for(int i=0;i<filesToTest.length;i++)
        {
        	if(!filesToTest[i].isDirectory())
        	{
        		files.add(filesToTest[i]);
        	}
        }
        return files;
	}
	
	public static List<File> foldersInDirectory(File directory)
	{		
        List<File> files = (List<File>) FileUtils.listFilesAndDirs(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        List<File> folders = new ArrayList<File>();
        for(File file:files)
        {
        	if(file.isDirectory())
        	{
        		folders.add(file);
        	}
        }
        return folders;
	}

	public static ArrayList<File> filesInDirectory(File directory, String extension)
	{		
        List<File> files = (List<File>) FileUtils.listFiles(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        ArrayList<File> filesFiltered = new ArrayList<File>();
        System.out.println("Searching " + directory.getAbsolutePath() + " for " + extension.toUpperCase() + " files");
        for(File file: files)
        {
        	if(file.getName().toLowerCase().endsWith(extension))
        	{
        		filesFiltered.add(file);
        	}
        }
        return filesFiltered;
	}
	
	public static boolean deleteFilesInDirectory(File directory)
	{		
		boolean success=false;
		System.out.println("Trying to clean files in: "+directory.getAbsolutePath());
        List<File> files = (List<File>) FileUtils.listFiles(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        
        List<File> folders = (List<File>) FileUtils.listFilesAndDirs(directory, TrueFileFilter.INSTANCE, DirectoryFileFilter.DIRECTORY);

        System.out.println("Deleting files");
        for(File file: files)
        {
        	if(file.isDirectory())
        	{
//        		deleteFilesInDirectory(file);    
        	}
        	else
        	{
	        	System.out.println("Trying to delete " + file.getAbsolutePath());
	        	file.delete();
        	}
        }
        
        for(File file: folders)
        {
        	System.out.println("Trying to delete " + file.getAbsolutePath());
        	file.delete();
        }
    	System.out.println("Trying to delete " + directory.getAbsolutePath());
        directory.delete();
        success=true;
        return success;
        
	}

	public static String readStringInFile(String pathFile)
	{
		String content="";
		File f = new File(pathFile);
		if(f.exists() && !f.isDirectory())
		{
			
		}
		else
		{
			System.out.println(f.getName() + " is a folder!");
			return content;
		}
		
		
		FileInputStream fstream=null;
		try {
			fstream = new FileInputStream(pathFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream,StandardCharsets.ISO_8859_1));
		String strLine;

		//Read File Line By Line
		try {
			while ((strLine = br.readLine()) != null)   
			{
			  content+=strLine+"\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fstream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return content;
	}

	public static String readStringInFileUTF8(String pathFile)
	{
		String content="";
		File f = new File(pathFile);
		if(f.exists() && !f.isDirectory())
		{
			
		}
		else
		{
			System.out.println(f.getName() + " is a folder!");
			return content;
		}
		
		
		FileInputStream fstream=null;
		try {
			fstream = new FileInputStream(pathFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream,StandardCharsets.UTF_8));
		String strLine;

		//Read File Line By Line
		try {
			while ((strLine = br.readLine()) != null)   
			{
			  content+=strLine+"\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fstream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return content;
	}

	

	public static ArrayList<String> readLinesInFile(String pathFile, String pattern)
	{
		ArrayList<String> lines = new ArrayList<String>();
		File f = new File(pathFile);
		if(f.exists() && !f.isDirectory())
		{
			
		}
		else
		{
			System.out.println(f.getName() + " is a folder!");
			return lines;
		}
		
		
		FileInputStream fstream=null;
		try {
			fstream = new FileInputStream(pathFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream,StandardCharsets.ISO_8859_1));
		String strLine;

		//Read File Line By Line
		try {
			while ((strLine = br.readLine()) != null)   
			{
				if(strLine.contains(pattern))
				{
					lines.add(strLine);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fstream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println(lines.size() + " lines readed!");		
		return lines;
	}

	
	public static ArrayList<String> readLinesInFile(String pathFile)
	{
		ArrayList<String> lines = new ArrayList<String>();
		File f = new File(pathFile);
		if(f.exists())
		{
			
		}
		else
		{
			 if(f.isDirectory())
			 {
				System.out.println(f.getName() + " is a folder!");
			 }
			 else
			 {
				 System.out.println(f.getName() + " does not existr!");
			 }
			return lines;		
		}
		
		
		FileInputStream fstream=null;
		try {
			fstream = new FileInputStream(pathFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream,StandardCharsets.ISO_8859_1));
		String strLine;

		//Read File Line By Line
		try {
			while ((strLine = br.readLine()) != null)   
			{
			  lines.add(strLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fstream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println(lines.size() + " lines readed!");		
		return lines;
	}
		
	public static ArrayList<String> readLinesInFileUTF8(String pathFile)
	{
		ArrayList<String> lines = new ArrayList<String>();
		File f = new File(pathFile);
		if(f.exists() && !f.isDirectory())
		{
			
		}
		else
		{
			return lines;
		}
		
		
		FileInputStream fstream=null;
		try {
			fstream = new FileInputStream(pathFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream,StandardCharsets.UTF_8));

		String strLine;

		//Read File Line By Line
		try {
			while ((strLine = br.readLine()) != null)   
			{
			  lines.add(strLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	public static ArrayList<String> reaLinesInString(String myString)
	{
		ArrayList<String> linesClean = new ArrayList<String>();
		String[] lines = myString.split(System.getProperty("line.separator"));
		for(int i=0;i<lines.length;i++)
		{
			linesClean.add(lines[i]);
		}
		return linesClean;
	}
	
	public static ArrayList<String> readLinesInFileANSI(String pathFile)
	{
		ArrayList<String> lines = new ArrayList<String>();
		File f = new File(pathFile);
		if(f.exists() && !f.isDirectory())
		{
			
		}
		else
		{
			return lines;
		}
		System.out.println("****************************************************");
		System.out.println("READING FILE " + f.getAbsolutePath());
		System.out.println("****************************************************");
		
		FileInputStream fstream=null;
		try {
			fstream = new FileInputStream(pathFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream,StandardCharsets.ISO_8859_1));

		String strLine;
		
		
		//Read File Line By Line
		try {
			while ((strLine = br.readLine()) != null)   
			{
				strLine = strLine.replaceAll("[^a-zA-Z0-9]"," ");
//				strLine = Normalizer.normalize(strLine, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", " ");
				
				//strLine = strLine.replaceAll("[\\p{InCombiningDiacriticalMarks}]", " ");
				
				lines.add(strLine);
			  	System.out.println(strLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	public static boolean copyFileAndDelete(File origin, File destiny)
	{
		boolean successful=false;
		InputStream inStream = null;
		OutputStream outStream = null;
			
	    	try{
	    		   		
	    	    inStream = new FileInputStream(origin);
	    	    outStream = new FileOutputStream(destiny);
	        	
	    	    byte[] buffer = new byte[1024];
	    		
	    	    int length;
	    	    //copy the file content in bytes 
	    	    while ((length = inStream.read(buffer)) > 0){
	    	  
	    	    	outStream.write(buffer, 0, length);
	    	 
	    	    }
	    	 
	    	    inStream.close();
	    	    outStream.close();
	    	    
	    	    //delete the original file
	    	    origin.delete();
	    	    
	    	    System.out.println("File is copied successful!");
	    	    successful=false;
	    	    
	    	}catch(IOException e){
	    	    e.printStackTrace();
	    	}
		return successful;
	}
	
	public static boolean stringToFile(String content, String fileName)
	{
		System.out.println("Filename to write: "+fileName);
		File file=new File(fileName);
		
		if(file.exists())
		{
			file.delete();
			Utils.waitv("Deleting original "+ fileName,2);
		}
		
		boolean succeed=false;
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			fw = new FileWriter(fileName);
			bw = new BufferedWriter(fw);
			bw.write(content);
			System.out.println("Done " + Calendar.getInstance().getTime());
			succeed=true;

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		System.out.println(file.getAbsolutePath());
		return succeed;
	}
	
	public static String convertToBinary(String input, String encoding) 
		      throws UnsupportedEncodingException {
		    byte[] encoded_input = Charset.forName(encoding)
		      .encode(input)
		      .array();  
		    return IntStream.range(0, encoded_input.length)
		        .map(i -> encoded_input[i])
		        .mapToObj(e -> Integer.toBinaryString(e ^ 255))
		        .map(e -> String.format("%1$" + Byte.SIZE + "s", e).replace(" ", "0"))
		        .collect(Collectors.joining(" "));
		}
	public static String decodeText(String input, Charset charset, 
			  CodingErrorAction codingErrorAction) throws IOException {
	    CharsetDecoder charsetDecoder = charset.newDecoder();
	    charsetDecoder.onMalformedInput(codingErrorAction);
	    return new BufferedReader(
	      new InputStreamReader(
	        new ByteArrayInputStream(input.getBytes()), charsetDecoder)).readLine();
	}
	public static String stringToANSI(String line)
	{
		try {
			line = decodeText(line,
				    StandardCharsets.US_ASCII,
				    CodingErrorAction.IGNORE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}
	
	public static void checkFolder(String folderPath)
	{
		File folder = new File(folderPath);
		if(!folder.exists())
        {
			System.out.println("Folder " + folderPath + " will be created!");
        	Path pathFolder=folder.toPath();
        	try {
				Files.createDirectories(pathFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		else
		{
			System.out.println("Folder " + folderPath + " exists already!");
		}
	}
	
	public static void checkFolder(File folder)
	{
		if(!folder.exists())
        {
        	Path pathFolder=folder.toPath();
        	try {
				Files.createDirectories(pathFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}

	public static boolean copyFileAndDelete(File origin, String pathDestiny, String fileNameDestiny)
	{
		boolean successful=false;
		InputStream inStream = null;
		OutputStream outStream = null;
		//create output directory if not exists
    	File folder = new File(pathDestiny);
    	if(!folder.exists()){
    		folder.mkdirs();
    	}
    	File destiny = new File(pathDestiny+File.separator+fileNameDestiny);			
    	System.out.println("---Destiny file: "+destiny.getAbsolutePath());
	    	try{
	    		if(destiny.exists())
	    		{
	    			System.out.println("File exist already!");
	    			destiny.delete();
	    			destiny = new File(pathDestiny+"\\\\"+fileNameDestiny);
	    		}
	    		else
	    		{
	    			System.out.println("Destiny does not exist!");
		    	    inStream = new FileInputStream(origin);
		    	    outStream = new FileOutputStream(destiny);
		        	
		    	    byte[] buffer = new byte[1024];
		    		
		    	    int length;
		    	    //copy the file content in bytes 
		    	    while ((length = inStream.read(buffer)) > 0){
		    	  
		    	    	outStream.write(buffer, 0, length);
		    	 
		    	    }
		    	 
		    	    inStream.close();
		    	    outStream.close();
	    		}
	    		   		
	    	    
	    	    //delete the original file
	    	    
	    	    origin.delete();
	    	    
	    	    System.out.println("File is copied successful!");
	    	    successful=false;
	    	    
	    	}catch(IOException e){
	    	    e.printStackTrace();
	    	}
		return successful;
	}

	
	public static boolean copyFile(File origin, File destiny)
	{
		boolean successful=false;
		InputStream inStream = null;
		OutputStream outStream = null;
		
		File destinyParent = destiny.getParentFile();
		destinyParent.mkdirs();
		System.out.println("Trying to copy file, from " + origin.getAbsolutePath() + " to " + destiny.getAbsolutePath());
	    	try{
	    		   		
	    	    inStream = new FileInputStream(origin);
	    	    outStream = new FileOutputStream(destiny);
	        	
	    	    byte[] buffer = new byte[1024];
	    		
	    	    int length;
	    	    //copy the file content in bytes 
	    	    while ((length = inStream.read(buffer)) > 0){
	    	  
	    	    	outStream.write(buffer, 0, length);
	    	 
	    	    }
	    	 
	    	    inStream.close();
	    	    outStream.close();
	    	    
	    	    //delete the original file
//	    	    origin.delete();
	    	    
	    	    System.out.println("File is copied successfully!");
	    	    successful=true;
	    	    
	    	}catch(IOException e){
	    	    e.printStackTrace();
	    	}
		return successful;
	}
	public static boolean copyFile(File origin, String pathDestiny, String fileDestinyName)
	{
		boolean successful=false;
		InputStream inStream = null;
		OutputStream outStream = null;
		//create output directory if not exists
    	File folder = new File(pathDestiny);
    	if(!folder.exists()){
    		folder.mkdirs();
    	}
    	File destiny = new File(pathDestiny+"\\\\"+fileDestinyName);
		
	    	try{
	    		   		
	    	    inStream = new FileInputStream(origin);
	    	    outStream = new FileOutputStream(destiny);
	        	
	    	    byte[] buffer = new byte[1024];
	    		
	    	    int length;
	    	    //copy the file content in bytes 
	    	    while ((length = inStream.read(buffer)) > 0){
	    	  
	    	    	outStream.write(buffer, 0, length);
	    	 
	    	    }
	    	 
	    	    inStream.close();
	    	    outStream.close();
	    	    
	    	    //delete the original file
//	    	    origin.delete();
	    	    
	    	    System.out.println("File is copied successful!");
	    	    successful=false;
	    	    
	    	}catch(IOException e){
	    	    e.printStackTrace();
	    	}
		return successful;
	}
	
	public static int positionNumber(String stringAlphaNumeric)
	{
		int position=0;
		for(int i=0; i<stringAlphaNumeric.length(); i++)
		{
			if(Character.isDigit(stringAlphaNumeric.charAt(i)))
			{
				position=i;
				break;
			}
		}
		return position;
	}

//	public static Email setupEmail(Connection conn, String chave)
//	{
//		Email email = new Email();
//		String query="select * from configuracoes.email_conf where chave='"+chave+"';";
//		System.out.println(query);
//		Statement st = null;
//		try {
//			st = conn.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		try {
//			ResultSet rs = st.executeQuery(query);
//			while(rs.next())
//			{
//				email.setSmtpServer(rs.getString("smtp"));
//				email.setSmtpPort(rs.getInt("porta_smtp"));
//				email.setImapServer(rs.getString("imap"));
//				email.setImapPort(rs.getInt("porta_imap"));
//				email.setUser(rs.getString("nome"));
//				email.setAddress(rs.getString("conta"));
//				email.setPassword(rs.getString("senha"));
//				email.setSecurity(rs.getString("seguranca"));
//			} 
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return email;
//	}
//	
	public static void sendKeys(Robot robot, String keys) {
	    for (char c : keys.toCharArray()) {
	    	
	        int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
	        if (KeyEvent.CHAR_UNDEFINED == keyCode) {
	            throw new RuntimeException(
	                "Key code not found for character '" + c + "'");
	        }
	        
	        System.out.println("Typing " + " char " + c + " keyCode" + keyCode);
	        robot.keyPress(keyCode);
	        robot.delay(100);
	        robot.keyRelease(keyCode);
	        robot.delay(100);
	    }
	}
	
	public static HashMap<String, File> loadKeyFilesFromDirectory(String folderPath, String extension, int sizeKey)
	{
		HashMap<String, File> foundFiles = new HashMap<String, File>();
        File folder = new File(folderPath);

        File[] files = folder.listFiles();
        
        System.out.println(files.length + " " + extension + " files found!");
        
        extension="."+extension;
        
        for(File file:files)
        {
        	if(file.isDirectory())
        	{
        		continue;
        	}
        	else
        	{
        		if(file.getName().endsWith(extension))
        		{
		        	String key = file.getName().substring(0,sizeKey);
		//        	System.out.println(key + "\t" + file.getName());
		        	foundFiles.put(key,file);
        		}
        	}
        }
        return foundFiles;
	}

	public static HashMap<String, String> loadKeysFromDirectory(String folderPath, String extension, int sizeKey)
	{
		HashMap<String, String> foundKeys = new HashMap<String, String>();
        File folder = new File(folderPath);

        File[] files = folder.listFiles();
        
        System.out.println(files.length + " " + extension + " files found!");
        
        extension="."+extension;
        
        for(File file:files)
        {
        	if(file.isDirectory())
        	{
        		continue;
        	}
        	else
        	{
        		if(file.getName().endsWith(extension))
        		{
		        	String key = file.getName().substring(0,sizeKey);
		//        	System.out.println(key + "\t" + file.getName());
		        	foundKeys.put(key,key);
        		}
        	}
        }
        return foundKeys;
	}
	
	public static HashMap<String,String> loadModelosFiscais (String filePath)
	{
		HashMap<String,String> modelosFiscais = new HashMap<String,String>();
		
		ArrayList<String> lines = Utils.readLinesInFile(filePath);
		for(String line:lines)
		{
			if(!line.startsWith("#"))
			{
				String[] fields = line.trim().split("\t");
				
				String modelo=fields[0];
				String descricao=fields[1];
				modelosFiscais.put(modelo, descricao);
			}
		}		
		return modelosFiscais;		
	}
	
	public static HashMap<String,ErrorResponse> loadErrorResponses (String errorFilePath)
	{
		HashMap<String,ErrorResponse> errorResponses = new HashMap<String,ErrorResponse>();
		
		ArrayList<String> lines = Utils.readLinesInFile(errorFilePath);
		for(String line:lines)
		{
			String[] fields = line.split("\t");
			
			String chave=fields[0];
			int responseCode=Integer.parseInt(fields[1]);
			String response="";
			if(fields.length==4)
			{
				response=fields[2];
			}
			ErrorResponse errorResponse = new ErrorResponse(chave,responseCode,response);
			errorResponses.put(chave, errorResponse);
		}
		
		return errorResponses;		
	}

	public static void deleteDirectoryRecursion(Path path) throws IOException {
		  if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
		    try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
		      for (Path entry : entries) {
		        deleteDirectoryRecursion(entry);
		      }
		    }
		  }
		  Files.delete(path);
	}
	
	public static ArrayList<File> loadFilesWithExtensionFromDirectory(String folderPath, String extension)
	{
        ArrayList<File> filesArray = new ArrayList<File>();
		
		File folder = new File(folderPath);

        File[] files = folder.listFiles();
        
        System.out.println(files.length + " " + extension +" files found!");
        
        extension="."+extension;
        
        for(File file:files)
        {
        	if(file.isDirectory())
        	{
        		continue;
        	}
        	else
        	{
        		if(file.getName().toLowerCase().endsWith(extension))
        		{
        			filesArray.add(file);
        		}
        	}
        }
        return filesArray;
	}
	
	public static ArrayList<File> loadFilesWithExtensionFromDirectory(String folderPath, ArrayList<String> extensions)
	{
        ArrayList<File> filesArray = new ArrayList<File>();
		
		File folder = new File(folderPath);

        File[] files = folder.listFiles();
        
        System.out.println(files.length + " files found!");

        for(File file:files)        
        {
        	System.out.println("Checking file: "+file.getAbsolutePath());
        	if(file.isDirectory())
        	{
        		continue;
        	}
        	else
        	{
        		for(String extension:extensions)
        		{
	        		extension="."+extension;
	        		if(file.getName().toLowerCase().endsWith(extension))
	        		{
	        			System.out.println("Adding "+file.getAbsolutePath());
	        			filesArray.add(file);
	        		}
        		}
        	}
        }
        return filesArray;
	}

	
	public static String systemSeparator()
	{
		String separator="";
		if(System.getProperty("os.name").toLowerCase().contains("windows"))
		{
			return "\\";
		}
		else
		{
			return "/";
		}
	}

}
