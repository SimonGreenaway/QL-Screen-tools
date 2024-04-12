/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author simon
 */
public class ZipHeader
{
    public static void main(final String... args)
    {
        File zipFile=null;
        File imageFile=null;
        boolean qltools=false;

        // Read the command line arguments

        for(String arg : args)
        {
            if(arg.equals("-h")||arg.contains("--help")) help();
            if(arg.equals("--qltools")) qltools=true;
            else
            {
                if(zipFile==null) zipFile=new File(arg);
                else if((qltools)&&(imageFile==null)) imageFile=new File(arg);
                else help();
            }
        }

        zipFile=new File(args[0]);
        imageFile=new File(args[1]);

        // Check files

        if(!zipFile.exists()) { System.err.println("Zip file not found: '"+zipFile+"'"); System.exit(1); }
        if(!zipFile.canRead()) { System.err.println("Cannot read zip filefound: '"+zipFile+"'"); System.exit(1); }
        if(!imageFile.canWrite() ) { System.err.println("Cannot write image file: '"+imageFile+"'"); System.exit(1); }

        // Read data sizes from zip file

        try
        {
            try(final ZipInputStream in=new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile))))
            {
                ZipEntry ze;

                int fs=0,ds=0;

                while((ze=in.getNextEntry())!=null)
                {
                    final byte[] extra=ze.getExtra();

                    if(extra!=null)
                    {
                        // File size (to confirm)
                        for(int i=10;i<16;i++)
                        {
                            int d=(int)extra[i];
                            if(d<0) d+=256;

                            fs=(fs*256)+d;
                        }

                        // Data size
                        for(int i=16;i<22;i++)
                        {
                            int d=(int)extra[i];
                            if(d<0) d+=256;

                            ds=(ds*256)+d;
                        }

                        // Display results

                        if(qltools)
                            System.out.format("qltools %s -x %s %d\n",imageFile,ze.getName(),ds);
                        else
                            System.out.format("%s\t%d\t%d\n",ze.getName(),fs ,ds);
                    }
                }
            }
        }
        catch(final Exception e)
        {
            e.printStackTrace(System.err);
        }
    }

    private static void help()
    {
        System.out.println("Usage: zipheader [-h --help] zipfile [ --qltools imagefile]");
        System.exit(0);
    }
}
