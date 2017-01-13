package com.dickimawbooks.bib2gls;

import java.io.*;
import java.util.Set;
import java.util.Iterator;

import com.dickimawbooks.texparserlib.*;
import com.dickimawbooks.texparserlib.bib.*;

public class Bib2GlsSymbol extends Bib2GlsEntry
{
   public Bib2GlsSymbol(String entryType)
   {
      super(entryType);
   }

   public void checkRequiredFields(TeXParser parser)
   {
      if (getField("name") == null)
      {
         missingFieldWarning(parser, "name");
      }
   }

   public String getDefaultSort()
   {
      return getId();
   }

   public void writeBibEntry(PrintWriter writer)
   throws IOException
   {
      writer.format("\\bibglsnew%s{%s}%%%n{", getEntryType(),
        getId());

      String sep = "";
      String name = "";
      String description = "";

      Set<String> keyset = getFieldSet();

      Iterator<String> it = keyset.iterator();

      while (it.hasNext())
      {
         String field = it.next();

         if (field.equals("name"))
         {
            name = getFieldValue(field);
         }
         else if (field.equals("description"))
         {
            description = getFieldValue(field);
         }
         else
         {
            writer.format("%s", sep);

            sep = String.format(",%n");

            writer.format("%s={%s}", field, getFieldValue(field));
         }
      }

      writer.println(String.format("}%%%n{%s}%%%n{%s}", name,
         description));
   }
}
