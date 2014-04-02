/*    Magic Builder
 * 	  A simple java program that grabs the latest prices and displays them per set.
    Copyright (C) 2014  Manuel Gonzales Jr.

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

import java.util.Scanner;

public abstract class Magic 
{	
	public static void main(String args[])
	{
		
		if(args.length>0)
		{
			initialArguments(args);
		}
		else
		{
			String selection="";
			try
			{
				Scanner screen = new Scanner(System.in);
				System.out.printf("Please make your selection:\n1)Block Constructed\n2)Constructed\n");
				selection = screen.nextLine();
				
				if(selection.equals("1"))
				{
					BlockConstructed block = new BlockConstructed();
					block.gatherBlocks();
					System.out.println("Please make your selection on the block set: ");
					selection = screen.nextLine();
					block.getBlock(Integer.parseInt(selection));
				}
				else if(selection.equals("2"))
				{
					System.out.printf("Please make your selection\n1)Standard\n2)Extended\n3)Modern\n4)Legacy/Vintage\n");
					selection=screen.nextLine();
					sanctionedConstructed(selection);
				}
				
				screen.close();
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}	
		}
		
	}
	
	private static void initialArguments(String arguments[])
	{
		if(arguments.length==2)
		{
			try
			{
			
				if(arguments[0].equals("1"))
				{
					BlockConstructed block = new BlockConstructed();
					block.gatherBlocks();
					block.getBlock(Integer.parseInt(arguments[1]));
				}
				else if(arguments[0].equals("2"))
				{
					sanctionedConstructed(arguments[1]);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
			System.out.println("You need to enter at least two arguments.");
	}
	
	private static void sanctionedConstructed(String selection)
	{
		boolean executed;
		
		if(selection.equals("1"))
		{
			Gatherer gather = new Gatherer();
			executed=gather.gather(0);
			if(executed)
				gather.tcg();
		}
		else if(selection.equals("2"))
		{
			Gatherer gather = new Gatherer();
			executed=gather.gather(1);
			if(executed)
				gather.tcg();
		}
		else if(selection.equals("3"))
		{
			Gatherer gather = new Gatherer();
			executed=gather.gather(2);
			if(executed)
				gather.tcg();
		}
		else if(selection.equals("4"))
		{
			/*
			 * Haven't quite worked out how to efficiently parse the info
			 *  
			 *  */
			Gatherer gather = new Gatherer();
			executed=gather.gatherAll();
			if(executed)
				gather.tcg();
		}
	}
	
	
}
