package dtvm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import driver.ChromeDriverFactory;
import mysql.ConnectorMariaDB;
import portalfidc.OperacaoSimplificada;
import utils.Utils;

public class AprovadorCustodiaAposVadu {
	private WebDriver driver=null;
	private static SimpleDateFormat sdfh=new SimpleDateFormat("HHmm");
	
	public AprovadorCustodiaAposVadu()
	{
		this.driver=ChromeDriverFactory.create();
	}
	
	public static void main(String[] args) 
	{
		ConnectorMariaDB.connect();
		AprovadorCustodiaAposVadu aprovadorCustodiaAposVadu=new AprovadorCustodiaAposVadu();
		int hourMin=900;
		int hourMax=1800;
		int currentHour=Integer.parseInt(sdfh.format(Calendar.getInstance().getTime()));
		int totalAttempts=50;
		if(currentHour>hourMin && currentHour<hourMax)
		{
			if(	aprovadorCustodiaAposVadu.login()
					&& currentHour>hourMin && currentHour<hourMax
					)
			{
				int iAttempt=0;
				while(iAttempt<totalAttempts)
				{
					
					System.out.println("****************************************");
					System.out.println("Tentativa de número "+iAttempt+"/"+totalAttempts);
					System.out.println("****************************************");
	
					aprovadorCustodiaAposVadu.refresh();
					iAttempt++;
					currentHour=Integer.parseInt(sdfh.format(Calendar.getInstance().getTime()));
					System.out.println("CurrentHour: "+currentHour);
				}
			}
		}
		else
		{
			System.out.println("HourMin: "+hourMin);
			System.out.println("HourMax: "+hourMax);
			System.out.println("CurrentHour: "+currentHour);
		}
	}

	public WebDriver getDriver() {
		return this.driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	
	private void refresh()
	{
		String pathClosePopup="//*[@id=\"j_idt447\"]/span/a/span";
		List<WebElement> closePopup = this.driver.findElements(By.xpath(pathClosePopup));
		if(!closePopup.isEmpty())
		{
			if(closePopup.get(0).isDisplayed() && closePopup.get(0).isEnabled())
			{
				closePopup.get(0).click();
				Utils.waitv("Closing popup",2);
			}
		}
		WebElement pesquisar=this.driver.findElement(By.xpath("//*[@id=\"formLiquidacao:j_idt276\"]"));
		System.out.println("Click on pesquisar");
		pesquisar.click();
		Utils.waitv(8);
		WebElement tableOperacoesParaAprovar=this.driver.findElement(By.xpath("//*[@id=\"formLiquidacao:pagedDataTable:tb\"]"));
		List<WebElement> rows = tableOperacoesParaAprovar.findElements(By.tagName("tr"));
		if(rows.size()==1 && rows.get(0).getText().toLowerCase().contains("nenhum resultado encontrado"))
		{
			System.out.println("Nothing to approve!");
		}
		else
		{
			boolean hasToApprove=false;
			closePopup = this.driver.findElements(By.xpath(pathClosePopup));
			if(!closePopup.isEmpty())
			{
				if(closePopup.get(0).isDisplayed() && closePopup.get(0).isEnabled())
				{
					closePopup.get(0).click();
					Utils.waitv("Closing popup",2);
				}
			}	
			
			for(WebElement row:rows)
			{
				OperacaoSimplificada operacaoSimplificada = new OperacaoSimplificada(row);
				operacaoSimplificada.verificar();
				operacaoSimplificada.show();
				List<WebElement> cols = row.findElements(By.tagName("td"));
				int iCol=0;
				if(operacaoSimplificada.isAprovadoVadu())
				{
					for(WebElement col:cols)
					{
						if(iCol==0)
						{						
							List<WebElement> checkboxes=col.findElements(By.tagName("input"));
							if(!checkboxes.isEmpty())
							{
								checkboxes.get(0).click();
								System.out.println("Selected!");
								Utils.waitv(1);
								hasToApprove=true;
							}
							else
							{
								System.out.println("****************** No checkbox to select!");
							}
						}					
						iCol++;
						break;
					}
				}
			}

			if(hasToApprove)
			{
				System.out.println("Has to approve!");
				this.aprovar();
			}
			else
			{
				System.out.println("Nothing to approve!");
			}
		}
		Utils.waitv(5);
	}
	
	private void aprovar()
	{
	    ArrayList<String> pathAprovar = new ArrayList<>();
	    pathAprovar.add("//*[@id=\"formLiquidacao:pagedDataTable:j_idt396\"]");
	    pathAprovar.add("//*[@id=\"formLiquidacao:pagedDataTable:j_idt391\"]");
	    pathAprovar.add("//*[@id=\"formLiquidacao:pagedDataTable:j_idt394\"]");
	    pathAprovar.add("//*[@id=\"formLiquidacao:pagedDataTable:j_idt389\"]");
	    for(String path: pathAprovar)
	    {
	    	List<WebElement> aprovarSemArquivos=this.driver.findElements(By.xpath(path));
	    	if(!aprovarSemArquivos.isEmpty())
	    	{
	    		System.out.println("Using xpath: "+path);
	    		aprovarSemArquivos.get(0).click();
	    		Utils.waitv(20);	
	    		break;
	    	}			
	    }
		ArrayList<String> pathFecharPopup = new ArrayList<>();
		pathFecharPopup.add("//*[@id=\"j_idt452\"]/span/a/span");
		pathFecharPopup.add("//*[@id=\"j_idt445\"]/span/a");
		
		for(String path:pathFecharPopup)
		{
			List<WebElement> popupFechar = this.driver.findElements(By.xpath(path));
			if(!popupFechar.isEmpty())
			{
				popupFechar.get(0).click();
				Utils.waitv(2);
			}
		}
	}
	
	private boolean login()
	{
		boolean success=false;
		this.driver.get("https://limine-custodia.fromtis.com/login.xhtml");
		this.driver.findElement(By.xpath("//*[@id=\"j_username\"]")).sendKeys("duplicata.limine");
		this.driver.findElement(By.xpath("//*[@id=\"j_password\"]")).sendKeys("LTai4fin");
		Utils.waitv(4);
		this.driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/form/button")).click();
		Utils.waitv(7);
		this.driver.get("https://limine-custodia.fromtis.com/pages/novaLiquidacao.xhtml");
		Utils.waitv(7);
		WebElement selectSituacao = this.driver.findElement(By.xpath("//*[@id=\"formLiquidacao:situacao\"]"));
		Select selectorSituacaoSelect=new Select(selectSituacao);
		selectorSituacaoSelect.deselectAll();
		Utils.waitv(2);
		selectorSituacaoSelect.selectByValue("PI");
		Utils.waitv(2);
		WebElement selectTipoRecebivel=this.driver.findElement(By.xpath("//*[@id=\"formLiquidacao:tipoRecebivel\"]"));
		Select selectorTipoRecebivel = new Select(selectTipoRecebivel);
		selectorTipoRecebivel.selectByValue("1");
		Utils.waitv(2);
		success=true;
		return success;
	}
}
