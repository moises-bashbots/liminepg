package cnab;

import utils.Utils;

public class Field 
{
	private String charset= "";
	private boolean mandatory=true; 
	private int size = 0;
	private String content="";
	private String name="";
	private Position position = new Position();
	private String filler="";
	private int ordinal=0;
	private int decimais=0;
	private int begin=0;
	private int end=0;
	
	public Field()
	{
		
	}

	public Field(int ordinal, int begin, int end, int size, String name, String content, String charset, Boolean mandatory, int decimais)
	{
		this.mandatory=mandatory;
//		System.out.println(this.name+ " Content: '"+content+"'");
		if(content==null)
		{
			if(this.charset=="A")
			{
				content=" ";
			}
			else
			{
				content="0";
			}
		}

		this.begin=begin;
		this.end=end;
		this.ordinal=ordinal;
		this.position = new Position(begin, end);
		this.name = name;
		this.size = size;
		this.content = Utils.normalizeUri(content.toUpperCase());
		this.charset=charset;
		if(this.charset=="N")
		{
			this.filler="0";
		}
		else if(this.charset=="A")
		{
			this.filler=" ";
		}

		if(this.content.length()<this.size)
		{
			int timesFill = this.size-this.content.length();
			for(int i=1; i<=timesFill; i++)
			{
				if(this.charset.equals("N"))
				{
					this.content=this.filler+this.content;
				}
				else
				{
					this.content=this.content+this.filler;
				}

			}
		}
		this.decimais=decimais;
	}

	
	public Field(int ordinal, int begin, int end, int size, String name, String content, String charset, Boolean mandatory)
	{
		this.mandatory=mandatory;
//		System.out.println(this.name+ " Content: '"+content+"'");
		if(content==null)
		{
			if(this.charset=="A")
			{
				content=" ";
			}
			else
			{
				content="0";
			}
		}

		this.begin=begin;
		this.end=end;
		this.ordinal=ordinal;
		this.position = new Position(begin, end);
		this.name = name;
		this.size = size;
		this.content = Utils.normalizeUri(content.toUpperCase());
		this.charset=charset;
		
		if(this.charset=="N")
		{
			this.filler="0";
		}
		else if(this.charset=="A")
		{
			this.filler=" ";
		}

		if(this.content.length()<this.size)
		{
			int timesFill = this.size-this.content.length();
			for(int i=1; i<=timesFill; i++)
			{
				if(this.charset.equals("N"))
				{
					this.content=this.filler+this.content;
				}
				else
				{
					this.content=this.content+this.filler;
				}

			}
		}
		else
		{
			this.content=content.substring(0,this.size);
		}
	}

	public Field(int ordinal, int begin, int end, String name, int size, String content, String charset, Boolean mandatory)
	{
		this.mandatory=mandatory;
//		System.out.println(this.name+ " Content: '"+content+"'");
		if(content==null)
		{
			if(this.charset=="A")
			{
				content=" ";
			}
			else
			{
				content="0";
			}
		}

		
		this.begin=begin;
		this.end=end;
		this.ordinal=ordinal;
		this.position = new Position(begin, end);
		this.name = name;
		this.size = size;
		this.content = Utils.normalizeUri(content.toUpperCase());
		this.charset=charset;
		if(this.charset=="N")
		{
			this.filler="0";
		}
		else if(this.charset=="A")
		{
			this.filler=" ";
		}

		if(this.content.length()<this.size)
		{
			int timesFill = this.size-this.content.length();
			for(int i=1; i<=timesFill; i++)
			{
				if(this.charset.equals("N"))
				{
					this.content=this.filler+this.content;
				}
				else
				{
					this.content=this.content+this.filler;
				}

			}
		}
		else
		{
			this.content=content.substring(0,this.size);
		}
	}
	
	public void extract(String line)
	{
		line = Utils.normalizeUri(line);
		if(line.length()>this.end && line.length() >= this.size)
		{
			this.content=line.substring((this.begin-1),this.end);	
		}		
	}
	
	public String replace(String line)
	{
		line = Utils.normalizeUri(line);
		String before="";
		String after="";
		int sizeLine=line.length();
		before=line.substring(0,this.begin-1);
		after=line.substring(this.end,sizeLine);
		line = before+this.content+after;
		return line;
	}
	
	public String replaceByChar(String line)
	{
		line = Utils.normalizeUri(line);
		String lineReplaced = Utils.replaceFieldContent(line, this);
//		System.out.println("Before: '" + line + "'");
//		System.out.println("After:  '" + lineReplaced + "'");
		return lineReplaced;
	}
	
	public String replaceByCharSilent(String line)
	{
		line = Utils.normalizeUri(line);
		String lineReplaced = Utils.replaceFieldContentSilent(line, this);
//		System.out.println("Before: '" + line + "'");
//		System.out.println("After:  '" + lineReplaced + "'");
		return lineReplaced;
	}
	
	
	public String replaceV(String line, Registro registro)
	{
		line = Utils.normalizeUri(line);
		char[] charLine = line.toCharArray();		
		String original=line;
		String before="";
		String after="";
		int sizeLine=original.length();
		before=original.substring(0,this.begin-1);
		after=original.substring(this.end,sizeLine);

//		System.out.println("Replacing '" + this.name + "'");
//
//		System.out.println("LineBeforeLenght: " + original.length());
//		System.out.println("Begin: " + this.begin);
//		System.out.println("End: " + this.end);
//		
//		System.out.println("ContentSize: " + this.content.length());
//		System.out.println("BeforeSize: " + before.length());
//		System.out.println("AfterSize: " + after.length());
		
		if((this.content.length() + before.length() + after.length() < line.length()))
		{
			System.out.println("Trouble! Downsized!");
		}
//		System.out.println("Before: '" + before + "'");
//		System.out.println("Content: '" + this.content+"'");
//		System.out.println("After: '" + after+"'");
//		
//		System.out.println("LineBefore: '" + line + "'");
		line = before+this.content+after;
//		System.out.println("LineAfter:  '" + line+"'");
//		
//		System.out.println("Indexes: " + 0 + " " + (this.begin-1) + " " + (this.end) + " " + sizeLine);
//		
//		System.out.println("LineAfterSize: " + line.length());
		return line;
	}
	
	public String view(String line)
	{
		line = Utils.normalizeUri(line);
		String content="";
		if(line.length()>this.end && line.length() >= this.size)
		{
			content=line.substring((this.begin-1),this.end);	
		}	
		return content;
	}
	
	public static void main(String[] args)
	{
		Field field = new Field(1,1,1,"Identificacao de registro",1,"1","N",true);
		Field field2 = new Field(2,2,6,"Agencia de debito",8,"1189","N",false);
		Field field3 = new Field(2,2,6,"Agencia de debito",15,"COBRANCA","A",true);
		System.out.println("Content1: '" + field.getContent() +"'");
		System.out.println("Content2: '" + field2.getContent() +"'");
		System.out.println("Content3: '" + field3.getContent() +"'");
	}
	
	
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getContent() {
		return content;
	}
	
	public void setContent()
	{
		this.setContent("");
	}
	
	public void setContent(String content) {
//		System.out.println(this.name+ " Content: '"+content+"'");
		if(content==null)
		{
			if(this.charset=="A")
			{
				content=" ";
			}
			else
			{
				content="0";
			}
		}

		this.content = Utils.normalizeUri(content.toUpperCase());

		if(this.charset=="N")
		{
			this.filler="0";
		}
		else if(this.charset=="A")
		{
			this.filler=" ";
		}
		
		this.content = this.content.toUpperCase();
		
		if(this.content.length()<this.size)
		{
			int timesFill = this.size-this.content.length();
			for(int i=1; i<=timesFill; i++)
			{
				if(this.charset.equals("N"))
				{
					this.content=this.filler+this.content;
				}
				else
				{
					this.content=this.content+this.filler;
				}

			}
		}
		else
		{
			this.content=this.content.substring(0,this.size);
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public String getFiller() {
		return filler;
	}
	public void setFiller(String filler) {
		this.filler = filler;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	public int getDecimais() {
		return decimais;
	}

	public void setDecimais(int decimais) {
		this.decimais = decimais;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	
	
	
}
