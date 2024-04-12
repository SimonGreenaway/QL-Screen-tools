/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ql;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author simon
 */
public class PNG2QL
{
    private static void convert(final boolean trim,final boolean ignoreGreen,final File source,final File dest) throws IOException
    {
        if(!source.exists())
        {
            System.out.println("File not found: "+source); System.exit(1);
        }
        else if(!source.canRead())
        {
            System.out.println("Cannot read from: "+source); System.exit(1);
        }

        System.out.println("Converting '"+source+"' to '"+dest+"'");
        if(ignoreGreen) System.out.println("Ignoring green!");

        final BufferedImage bufferedImage=ImageIO.read(source);

        System.out.println("\tSize: "+bufferedImage.getWidth()+" x "+bufferedImage.getHeight());

        if(bufferedImage.getWidth()>256)
        {
            System.out.println("Width is to big, please resize");
            System.exit(1);
        }
        else if(bufferedImage.getHeight()>256)
        {
            System.out.println("Height is to big, please resize");
            System.exit(1);
        }

        final boolean image[][][]=new boolean[bufferedImage.getWidth()][bufferedImage.getHeight()][3];

        int count=0;
        int start=-1,end=-1;

        for(int y=0;y<bufferedImage.getHeight();y++)
        {
            boolean empty=true;

            for(int x=0;x<bufferedImage.getWidth();x++)
            {
                final int data=bufferedImage.getRGB(x, y);

                image[x][y][2]=(data&0xFF)>=128;
                image[x][y][1]=((data>>8)&0xFF)>=128;
                image[x][y][0]=((data>>16)&0xFF)>=128;

                if(image[x][y][0]||image[x][y][1]||image[x][y][2]) empty=false;
            }

            if(empty)
                count=0;
            else
            {
                count++;

                if(start==-1) start=y;
                end=y;
            }
        }

        System.out.println((trim?"Trimming to rows ":"Data: ")+start+"-"+end+" ("+((end-start+1)*128)+" bytes).");

        //

        final int xOffset=(256-bufferedImage.getWidth())/2;
        final int yOffset=(256-bufferedImage.getHeight())/2;

        try(final DataOutputStream out=new DataOutputStream(new FileOutputStream(dest)))
        {
            if(!trim)
            {
                // Top blank filler space (if required)

                for(int y=0;y<yOffset;y++)
                {
                    for(int x=0;x<128;x++)
                        out.writeByte(0);
                }
            }

            for(int y=0;y<bufferedImage.getHeight();y++)
            {
                if(trim&&(y>=start)&&(y<=end))
                {
                    // Left filler space
                    for(int x=0;x<xOffset/4;x++) out.writeShort(0);

                    for(int x=0;x<bufferedImage.getWidth();x+=4)
                    {
                        int data1=0,data2=0;

                        if(image[x+3][y][0]) data1|=2;
                        if(image[x+2][y][0]) data1|=8;
                        if(image[x+1][y][0]) data1|=32;
                        if(image[x+0][y][0]) data1|=128;

                        if(image[x+3][y][1]) data2|=2;
                        if(image[x+2][y][1]) data2|=8;
                        if(image[x+1][y][1]) data2|=32;
                        if(image[x+0][y][1]) data2|=128;

                        if(image[x+3][y][2]) data1|=1;
                        if(image[x+2][y][2]) data1|=4;
                        if(image[x+1][y][2]) data1|=16;
                        if(image[x+0][y][2]) data1|=64;

                        if(ignoreGreen) out.writeByte(0);
                        else out.writeByte(data2&0xFF);

                        out.writeByte(data1&0xFF);
                    }

                    // Right filler space
                    while(out.size()%128!=0) out.writeByte(0);
                }
            }

            if(!trim)
            {
                // Bottom blank filler space (if required)

                for(int y=0;y<(256-bufferedImage.getHeight()-yOffset);y++)
                    for(int x=0;x<128;x++)
                        out.write(0);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            convert(true,true,new File("/home/simon/code/ql/moon.png"),new File("/home/simon/code/ql/moon.scr"));
            //convert(false,false,new File("/home/simon/code/ql/skyrim2.png"),new File("/home/simon/code/ql/skyrim.scr"));
        }
        catch(final Exception e)
        {
            e.printStackTrace(System.out);
            System.exit(2);
        }
    }
}
