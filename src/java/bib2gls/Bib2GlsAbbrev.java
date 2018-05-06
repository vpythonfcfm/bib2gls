/*
    Copyright (C) 2017 Nicola L.C. Talbot
    www.dickimaw-books.com

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/
package com.dickimawbooks.bib2gls;

import java.io.*;
import java.util.Set;
import java.util.Iterator;

import com.dickimawbooks.texparserlib.*;
import com.dickimawbooks.texparserlib.bib.*;

public class Bib2GlsAbbrev extends Bib2GlsEntry
{
   public Bib2GlsAbbrev(Bib2Gls bib2gls, String entryType)
   {
      super(bib2gls, entryType);
   }

   public void checkRequiredFields()
   {
      if (getField("short") == null)
      {
         missingFieldWarning("short");
      }

      if (getField("long") == null)
      {
         missingFieldWarning("long");
      }
   }

   public String getFallbackValue(String field)
   {
      String val;

      if (field.equals("sort"))
      {
         field = resource.getAbbrevDefaultSortField();
         val = getFieldValue(field);

         if (val != null)
         {
            return val;
         }
      }

      if (field.equals("name"))
      {
         field = resource.getAbbrevDefaultNameField();
         val = getFieldValue(field);

         if (val != null)
         {
            return val;
         }
      }

      return super.getFallbackValue(field);
   }

   public BibValueList getFallbackContents(String field)
   {
      BibValueList val;

      if (field.equals("sort"))
      {
         String fallbackField = resource.getAbbrevDefaultSortField();
         val = getField(fallbackField);

         return val == null ? getFallbackContents(fallbackField) : val;
      }
      else if (field.equals("name"))
      {
         String fallbackField = resource.getAbbrevDefaultNameField();
         val = getField(fallbackField);

         return val == null ? getFallbackContents(fallbackField) : val;
      }

      return super.getFallbackContents(field);
   }

   protected void changeNameCase(TeXParser parser)
    throws IOException
   {
      BibValueList value = getField("name");

      if (value == null)
      {
         return;
      }

      super.changeNameCase(parser);
   }

   public void writeCsDefinition(PrintWriter writer) throws IOException
   {
      // syntax: {label}{opts}{short}{long}

      writer.format("\\providecommand{\\%s}[4]{%%%n", getCsName());

      writer.format("  \\new%s[#2]{#1}{#3}{#4}%%%n", getEntryType());

      writer.println("}");
   }

   public void writeBibEntry(PrintWriter writer)
   throws IOException
   {
      writer.format("\\%s{%s}%%%n{", getCsName(), getId());

      String sep = "";
      String shortText = "";
      String longText = "";

      Set<String> keyset = getFieldSet();

      Iterator<String> it = keyset.iterator();

      while (it.hasNext())
      {
         String field = it.next();

         if (field.equals("short"))
         {
            shortText = getFieldValue(field);
         }
         else if (field.equals("long"))
         {
            longText = getFieldValue(field);
         }
         else if (bib2gls.isKnownField(field))
         {
            writer.format("%s", sep);

            sep = String.format(",%n");

            writer.format("%s={%s}", field, getFieldValue(field));
         }
         else if (bib2gls.getDebugLevel() > 0 && 
            !bib2gls.isInternalField(field))
         {
            bib2gls.debugMessage("warning.ignoring.unknown.field", field);
         }
      }

      writer.println(String.format("}%%%n{%s}%%%n{%s}",
        shortText, longText));
   }

}
