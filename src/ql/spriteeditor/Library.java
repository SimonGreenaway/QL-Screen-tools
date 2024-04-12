/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.spriteeditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author simon
 */
public class Library
{
    private List<Sprite> sprites=new ArrayList<>();
    private boolean modified=false;

    public Library()
    {
    }

    public void setModified(final boolean m)
    {
        modified=m;
    }

    public void clear()
    {
        sprites.clear();
        modified=true;
    }

    public boolean isModified() { return modified; }

    public List<Sprite> get()
    {
        return Collections.unmodifiableList(sprites);
    }

    public int size()
    {
        return sprites.size();
    }

    public Sprite get(final int i)
    {
        return sprites.get(i);
    }

    public Sprite set(final int i,final Sprite s)
    {
        return sprites.set(i, s);
    }

    public void add(final Sprite s)
    {
        sprites.add(s);
        setModified(true);
    }

    public void delete(final int i)
    {
        sprites.remove(i);
    }

    public static Library load(final File file) throws IOException
    {
        final Library lib=new Library();

        try(final BufferedReader in=new BufferedReader(new FileReader(file)))
        {
            in.readLine(); // Number of sprites - not used here

            while(true)
            {
                final Sprite s=Sprite.load(in);
                if(s==null) break;

                lib.add(s);
            }
        }

        return lib;
    }

    public void save(final File file) throws FileNotFoundException
    {
        try(final PrintWriter out=new PrintWriter(file))
        {
            out.println(Integer.toString(size()));
            for(final Sprite s:sprites) s.save(out);
        }
    }
}
