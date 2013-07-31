/*    Magic Builder
 * 	  A simple java program that grabs the latest prices and displays them per set.
    Copyright (C) 2013  Manuel Gonzales Jr.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see [http://www.gnu.org/licenses/].
*/

package com.macleod2486.magicbuilder;

import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BuildDatabase 
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
			PrintWriter standarddata = new PrintWriter("standard.db");
			standarddata.write("");
			//Various values to screen the data
			String clean;
			String highprice;
			String mediumPrice;
			String highestCard=" ";
			double high =0;
			//String color;
			Document page;
			Element table;
			Elements row;
			Elements item;
			//Grabs the modified set values to then be used for the website url format
			for(int limit=0; limit<this.pointer; limit++)
			{
				standarddata.append("\nSet name: "+standardNames[limit]+"\n");
				System.out.println("\nSet name: "+standardNames[limit]+"\n");
				//Each modified string value is then put in the following url to then parse
				//the information from it.
				page = Jsoup.connect("http://magic.tcgplayer.com/db/price_guide.asp?setname="+standardSets[limit]).get();
				table = page.select("table").get(2);
				row=table.select("tr");
				//Grabs each card that was selected
				for(Element tableRow: row)
				{
					item=tableRow.select("td");
					clean=item.get(0).text();
					//Filters out land cards
					if(!clean.contains("Forest")&&!clean.contains("Mountain")&&!clean.contains("Swamp")&&!clean.contains("Island")&&!clean.contains("Plains"))
					{
						//Gets the name of the card
						clean=clean.substring(1);
						
						//This gets the high price
						highprice=item.get(5).text();
						highprice=highprice.substring(1,highprice.length()-2);
						
						//This gets the medium price
						mediumPrice=item.get(6).text();
						mediumPrice=mediumPrice.substring(1,mediumPrice.length()-2);
						
						//Finds highest card
						if(high<Double.parseDouble(highprice))
						{	
							high=Double.parseDouble(highprice);
							highestCard=clean;
						}
						standarddata.append("\n"+clean+" H:$"+highprice+" M:$"+mediumPrice);
						System.out.println(clean+"  H:$"+highprice+" M:$"+mediumPrice);
					}
				}
				
				//Write to the file
				standarddata.append("\n**************************************\n");
				standarddata.append("Highest Card out of set "+highestCard+" $"+high);
				standarddata.append("\n**************************************\n");
				
				//Displays
				System.out.println("**************************************");
				System.out.println("Highest Card out of set "+highestCard+" $"+high);
				System.out.println("**************************************\n");
				
				//Resets the values found for the next set
				highestCard="";
				high=0;
			}
			//closes the file
			standarddata.close();
			
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
				
				//System.out.println(clean);
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