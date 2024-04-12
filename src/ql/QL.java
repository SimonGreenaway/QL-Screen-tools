/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package ql;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 *
 * @author simon
 */
public class QL
{
    private final static File zips=new File("/emulators/ql/for_sd/zip");
    private final static File images=new File("/emulators/ql/for_sd/sd");

    public static void main(String[] args)
    {
        try
        {
            walk(zips,images);
        }
        catch(final Exception e)
        {
            e.printStackTrace(System.out);
        }
    }

    public static void walk(final File zips,final File images) throws IOException
    {
        System.out.println(zips+"\t->\t"+images);

        if(!zips.exists()) throw new RuntimeException("Directory not found: "+zips);
        else if(!zips.isDirectory()) throw new RuntimeException("Not a directory: "+zips);

        if(!images.exists()) images.mkdirs();
        if(!images.exists()) throw new RuntimeException("Cannot create directory: "+images);

        for(final File zip:zips.listFiles())
        {
            final File image=new File(images.getAbsolutePath()+"/"+zip.getName());


            if(zip.isDirectory())
                walk(zip,image);
            else if(zip.getName().endsWith(".zip"))
            {
                try
                {
                    final File image2=new File(images.getAbsolutePath()+"/"+zip.getName().substring(0,zip.getName().lastIndexOf("."))+".img");

                    System.out.println("\t"+zip+"\t->\t"+image2);
                    Runtime.getRuntime().exec(new String[]{"/home/simon/bin/qltools",image2.getAbsolutePath(),"-fdd"}).waitFor();
                    Runtime.getRuntime().exec(new String[]{"/home/simon/bin/qltools",image2.getAbsolutePath(),"-w",zip.getAbsolutePath()}).waitFor();
                }
                catch(final InterruptedException e)
                {
                    // ?
                }
            }
        }
    }
}
