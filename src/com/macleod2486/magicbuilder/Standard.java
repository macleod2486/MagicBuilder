package com.macleod2486.magicbuilder;

import java.io.File;
import java.io.FileOutputStream;
//JSoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//Apache imports for writing to an excel file 
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


public class Standard 
{
	String standardSets[]=new String[30];
	String standardNames[]=new String[20];
	int pointer=0;
	int year=0;
	
	public void start()
	{
		//System.out.println("It works!");
				try
				{
					Document page;
					Elements table;
					Elements cards;
					String copy;
					for(int pages=0; pages<100; pages++)
					{
						System.out.println("***********"+pages+"**************");
						page = Jsoup.connect("http://gatherer.wizards.com/Pages/Search/Default.aspx?page="+pages+"&format=%5BStandard%5D").get();
						//System.out.println("Page "+page.text());
						table = page.select("table.cardItemTable");
						cards = table.select("span.cardTitle");
						copy=cards.first().text();

						for(Element found : cards)
						{
							if(copy==found.text())
							{
								break;
							}
							
							if(!found.text().contains("Mountain")&&!found.text().contains("Island")&&!found.text().contains("Swamp")&&!found.text().contains("Forest")&&!found.text().contains("Plains"))
							{
								System.out.println("Found "+found.text());
							}
						}
						
					} 
					
					//page = Jsoup.connect("http://magic.tcgplayer.com/db/price_guide.asp?setname=innistrad").get();
					//table=page.select(cssQuery)
					
					
				}
				
				catch(Exception e)
				{
					
					System.out.println("Error! "+e);
				}

	}

	public void tcg()
	{
		try
		{

			int rowNum;
			
			//Creates a excel file for the information to be stored
			HSSFWorkbook standard = new HSSFWorkbook();
			HSSFSheet setname;
			Row newRow;
			Cell info;
			
			//Various values to screen the data
			String clean;
			String highprice;
			String mediumPrice;
			String lowPrice;
			String highestCard=" ";
			double high =0;
			
			//Variables to take in information
			Document page;
			Element table;
			Elements row;
			Elements item;
			
			/*Grabs the modified set values to then be used for the website url format
			 * Not the most effecient for loop but will be modified as time goes on.
			*/
			for(int limit=0; limit<this.pointer; limit++)
			{
				rowNum=0;
				
				System.out.println("\nSet name: "+standardNames[limit]+"\n");
				
				//Creates a new sheet per set
				setname=standard.createSheet(standardNames[limit]);
				//Sets up the initial row in the set
				newRow = setname.createRow(0);
				info=newRow.createCell(0);
				info.setCellValue("Card Name");
				info=newRow.createCell(1);
				info.setCellValue("High Price");
				info=newRow.createCell(2);
				info.setCellValue("Medium Price");
				info=newRow.createCell(3);
				info.setCellValue("Low Price");
				
				/*Each modified string value is then put in the following url to then parse
				  the information from it. */
				
				page = Jsoup.connect("http://magic.tcgplayer.com/db/price_guide.asp?setname="+standardSets[limit]).get();
				table = page.select("table").get(2);
				row=table.select("tr");
				
				//Grabs each card that was selected
				for(Element tableRow: row)
				{
					//Gets the first row 
					item=tableRow.select("td");
					clean=item.get(0).text();
					
					
					
					//Filters out land cards
					if(!clean.contains("Forest")&&!clean.contains("Mountain")&&!clean.contains("Swamp")&&!clean.contains("Island")&&!clean.contains("Plains"))
					{
						//Creates new row in the sheet
						newRow = setname.createRow(rowNum+1);
						
						//Gets the name of the card
						clean=clean.substring(1);
						info=newRow.createCell(0);
						info.setCellValue(clean);
						
						//This gets the high price
						highprice=item.get(5).text();
						highprice=highprice.substring(1,highprice.length()-2);
						info=newRow.createCell(1);
						info.setCellValue(highprice);
						
						//This gets the medium price
						mediumPrice=item.get(6).text();
						mediumPrice=mediumPrice.substring(1,mediumPrice.length()-2);
						info=newRow.createCell(2);
						info.setCellValue(mediumPrice);
						
						//This gets the low price
						lowPrice = item.get(7).text();
						lowPrice = lowPrice.substring(1,lowPrice.length()-2);
						info=newRow.createCell(3);
						info.setCellValue(lowPrice);
						
						//Finds highest card using the high price
						if(high<Double.parseDouble(highprice))
						{	
							high=Double.parseDouble(highprice);
							highestCard=clean;
						}
						
						System.out.println(clean+"  H:$"+highprice+" M:$"+mediumPrice);
						rowNum++;
					}
					
				}
				
				//Displays
				System.out.println("**************************************");
				System.out.println("Highest Card out of set "+highestCard+" $"+high);
				System.out.println("**************************************\n");
				
				//Resets the values found for the next set
				highestCard="";
				high=0;
			}
			
			//Writes the workbook to the file and closes it
			File standardFile = new File("standard.xls");
			FileOutputStream standardOutput = new FileOutputStream(standardFile);
			standard.write(standardOutput);
			standardOutput.close();
			
		}
	
		catch(Exception e)
		{
			System.out.println("Error! "+e);
			if(e.toString().contains("Status=400"))
			{
				System.out.println("That webpage does not exist!");
			}
			else if(e.toString().contains("SocketTimeout"))
			{
				System.out.println("Your connection timed out");
			}
		}
		
	}

	public boolean standard()
	{
		String clean;
		char check;
		
		try
		{
			//Grabs the webpage then selects the list of standard sets
			Document page = Jsoup.connect("https://www.wizards.com/magic/magazine/article.aspx?x=judge/resources/sfrstandard").get();
			Elements article = page.select("div.article-content");
			Elements table = article.select("ul");
			Elements list = table.select("li");
			this.pointer = 0;
			//Loops through each item within the list of available standard sets
			for(Element item: list)
			{
				standardNames[pointer]=item.text();
				System.out.println(standardNames[pointer]);
				clean = item.text().replaceAll(" ", "%20");
				//Further processes the items within found on the site
				for(int length=0; length<clean.length(); length++)
				{
					check=clean.charAt(length);
					if(check=='(')
					{
						clean = clean.substring(0,length-3);
					}
				}
				
				//Checks to see if the standard set is a core set or not
				if(clean.matches(".*\\d\\d\\d\\d.*"))
				{
					standardSets[pointer]=coreSet(clean);
					
				}
				else
				{
					standardSets[pointer]=clean;
				}
				
				this.pointer++;
			}
			
			//System.out.println("Number of items in list "+pointer);
			return true;
		}
		catch(Exception e)
		{
			System.out.println("Error! "+e);
			return true;
		}
	}
	
	//If a core set is detected then the name is edited to fit the TCG format
	private String coreSet(String input)
	{
		String output=input+"%20(M"+input.substring(input.length()-2)+")";
		return output;
	}

}
