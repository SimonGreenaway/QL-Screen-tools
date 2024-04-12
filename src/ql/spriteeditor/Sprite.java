/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.spriteeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author simon
 */
public class Sprite
{
    private final String name;
    private final short x,y;
    private final int mask[][],data[][];

    public Sprite(final String name,final int x,final int y)
    {
        this.name=name;
        this.x=(short)x; this.y=(short)y;

        mask=new int[x*2][y];
        data=new int[x*2][y];
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the x
     */
    public short getXBytes()
    {
        return x;
    }

    /**
     * @return the x
     */
    public short getX()
    {
        return (short)(x*4);
    }

    /**
     * @return the y
     */
    public short getY()
    {
        return y;
    }

    /**
     * @return the mask
     */
    public boolean getMask(final int dx,final int dy)
    {
        final int maskum=mask[dx/4][dy];

        switch(dx&3)
        {
            case 3 ->
            {
                return (maskum&1)==0;
            }
            case 2 ->
            {
                return (maskum&4)==0;
            }
            case 1 ->
            {
                return (maskum&16)==0;
            }
            case 0 ->
            {
                return (maskum&64)==0;
            }
            default ->
            {
                throw new RuntimeException("Cannot happen!");
            }
        }
    }

    public void setColour(final int dx,final int dy,final Color color)
    {
        if(color==Color.BLACK) System.out.println("BLACK!");

        boolean red=(color==null)?false:color.getRed()>0;
        boolean blue=(color==null)?false:color.getBlue()>0;
        boolean green=(color==null)?false:color.getGreen()>0;

        int datum=data[dx/4][dy];
        int maskum=mask[dx/4][dy];

        switch(dx&3)
        {
            case 3 ->
            {
                datum&=0xfcfc;
                maskum&=0xfcfc;

                if(blue) datum|=1;
                if(red) datum|=2;
                if(green) datum|=512;

                if(color!=null) maskum|=1|2|256|512;
                else maskum&=~(1|2|256|512);
            }
            case 2 ->
            {
                datum&=0xf3f3;
                maskum&=0xf3f3;

                if(blue) datum|=4;
                if(red) datum|=8;
                if(green) datum|=2048;

                if(color!=null) maskum|=4|8|1024|2048;
                else maskum&=~(4|8|1024|2048);
            }
            case 1 ->
            {
                datum&=0xcfcf;
                maskum&=0xcfcf;

                if(blue) datum|=16;
                if(red) datum|=32;
                if(green) datum|=8192;

                if(color!=null) maskum|=16|32|4096|8192;
                else maskum&=~16|32|4096|8192;
            }
            case 0 ->
            {
                datum&=0x3f3f;
                maskum&=0x3f3f;

                if(blue) datum|=64;
                if(red) datum|=128;
                if(green) datum|=32768;

                if(color!=null) maskum|=64|128|16384|32768;
                else maskum&=~(64|128|16384|32768);
            }
        }

        mask[dx/4][dy]=maskum;
        data[dx/4][dy]=datum;

        System.out.println(dx+"\t"+dy+"\t"+color+"\n"+toString());
    }

    public Color getColour(final int dx,final int dy)
    {
        final Color colours[]={Color.BLACK,Color.BLUE,Color.RED,Color.MAGENTA,Color.GREEN,Color.CYAN,Color.YELLOW,Color.WHITE};

        final int datum=data[dx/4][dy];
        final int maskum=mask[dx/4][dy];
        int colour=0;
        boolean masked=true;

        switch(dx&3)
        {
            case 3 ->
            {
                if((datum&1)>0) colour=1;
                if((datum&2)>0) colour+=2;
                if((datum&512)>0) colour+=4;

                if((maskum&1)>0) masked=false;
            }
            case 2 ->
            {
                if((datum&4)>0) colour=1;
                if((datum&8)>0) colour+=2;
                if((datum&2048)>0) colour+=4;

                if((maskum&4)>0) masked=false;
            }
            case 1 ->
            {
                if((datum&16)>0) colour=1;
                if((datum&32)>0) colour+=2;
                if((datum&8192)>0) colour+=4;

                if((maskum&16)>0) masked=false;
            }
            case 0 ->
            {
                if((datum&64)>0) colour=1;
                if((datum&128)>0) colour+=2;
                if((datum&32768)>0) colour+=4;

                if((maskum&64)>0) masked=false;
            }
        }

        return masked?null:colours[colour];
    }

    private static String readLine(final BufferedReader in) throws IOException
    {
        while(true)
        {
            String buffer=in.readLine();
            if(buffer==null) return null;

            if((buffer.length()>0)&&(buffer.charAt(0)!='#'))
            {
                if(buffer.contains("#")) buffer=buffer.substring(0,buffer.indexOf("#")).trim();
                return buffer;
            }
        }
    }

    public static Sprite load(final BufferedReader in) throws IOException
    {
        final String name=readLine(in);
        if(name==null) return null;

        final Sprite s=new Sprite(name,Integer.parseInt(readLine(in)),Integer.parseInt(readLine(in)));

        // Load data

        for(int b=0;b<s.y;b++)
        {
            for(int a=0;a<s.x;a++)
            {
                s.data[a][b]=Integer.parseInt(readLine(in));
                s.mask[a][b]=Integer.parseInt(readLine(in));
            }
        }

        return s;
    }

    public void save(final PrintWriter out)
    {
        out.println("# "+getName());
        out.println(getName());
        out.println(x);
        out.println(y);

        out.println("# Data:");

        for(int b=0;b<y;b++)
        {
            out.println("# y="+b);
            for(int a=0;a<x;a++)
            {
                out.println(data[a][b]+"\t#D "+toBinaryString(data[a][b]));
                out.println(mask[a][b]+"\t#M "+toBinaryString(mask[a][b]));
            }
        }
    }

    private String toBinaryString(int a)
    {
        String s=Integer.toBinaryString(a);

        while(s.length()<16) s="0"+s;

        return s;
    }

    public void clear()
    {
        for(int a=0;a<x;a++)
            for(int b=0;b<y;b++)
            {
                data[a][b]=0;
                mask[a][b]=0;
            }
    }

    @Override public String toString()
    {
        final StringBuilder out=new StringBuilder();

        out.append(name).append("\t").append(x).append("\t").append(y).append("\n");

        out.append(" y  gfgfgfgfrbrbrbrb gfgfgfgfrbrbrbrb\n");
        out.append("    3322110033221100 3322110033221100\n");

        for(int b=0;b<y;b++)
        {
            out.append(String.format("%3d",b));

            for(int pass=0;pass<2;pass++)
            {
                if(pass==1) out.append("   ");

                for(int a=0;a<x;a++)
                {
                    String z=Integer.toBinaryString(pass==0?data[a][b]:mask[a][b]);

                    while(z.length()<16) z="0"+z;

                    out.append(String.format(" %16s",z));
                }

                out.append('\n');
            }
        }

        out.append(" y  gfgfgfgfrbrbrbrb gfgfgfgfrbrbrbrb\n");
        out.append("    3322110033221100 3322110033221100\n");

        return out.toString();
    }

    public int draw(final Graphics g,final int xc,final int yc,final int box)
    {
        for(int a=0;a<x*4;a++)
        {
            for(int b=0;b<y;b++)
            {
                g.setColor(getMask(a,b)?Color.LIGHT_GRAY:getColour(a,b));
                g.fillRect(xc+a*box,yc+b*box,box,box);
            }
        }

        return yc+getY()*box;
    }

    public void autoMask()
    {
        for(int y=0;y<getY();y++)
        {
            for(int x=0;x<getX();x++)
            {
                //if(getColour(x,y)>0) setMask(x,y)=1;
            }
        }
    }

    public Sprite duplicate()
    {
        final Sprite s=new Sprite(getName()+"_copy",getXBytes(),getY());

        for(int a=0;a<x*2;a++)
        {
            for(int b=0;b<y;b++)
            {
                s.data[a][b]=data[a][b];
                s.mask[a][b]=mask[a][b];
            }
        }

        return s;

    }
}
