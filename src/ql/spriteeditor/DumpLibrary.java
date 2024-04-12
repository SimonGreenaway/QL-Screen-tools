/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ql.spriteeditor;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author simon
 */
public class DumpLibrary
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try(final BufferedReader in=new BufferedReader(new FileReader("/home/simon/test.lib")))
        {
            String name=in.readLine();
            final int x=Integer.parseInt(in.readLine());
            final int y=Integer.parseInt(in.readLine());

            System.out.println(name+"\t"+x+"\t"+y);

            System.out.println(" y gfgfgfgfrbrbrbrb gfgfgfgfrbrbrbrb");
            System.out.println("   3322110033221100 3322110033221100");

            for(int b=0;b<y;b++)
            {
                System.out.format("%2d",b);

                for(int a=0;a<x;a++)
                {
                    final int d=Integer.parseInt(in.readLine());

                    String z=Integer.toBinaryString(d);

                    while(z.length()<16) z="0"+z;

                    System.out.format(" %16s",z);
                }

                System.out.println();
            }

            System.out.println(" y gfgfgfgfrbrbrbrb gfgfgfgfrbrbrbrb");
            System.out.println("   3322110033221100 3322110033221100");
        }
        catch(final Exception e)
        {
            e.printStackTrace(System.out);
        }
    }
}