/*
    Copyright (C) 2018 Nicola L.C. Talbot
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
import java.util.Iterator;
import java.util.Vector;

import com.dickimawbooks.texparserlib.*;
import com.dickimawbooks.texparserlib.bib.*;
import com.dickimawbooks.texparserlib.latex.CsvList;

public class Bib2GlsSpawnSymbol extends Bib2GlsProgenitor
{
   public Bib2GlsSpawnSymbol(Bib2Gls bib2gls)
   {
      this(bib2gls, "spawnsymbol");
   }

   public Bib2GlsSpawnSymbol(Bib2Gls bib2gls, String entryType)
   {
      super(bib2gls, entryType);
   }

   protected void initMissingFields()
   {
   }

   public void checkRequiredFields()
   {
      super.checkRequiredFields();

      BibValueList name = getField("name");
      BibValueList parent = getField("parent");
      BibValueList description = getField("description");

      if (name != null || (parent != null && description != null))
      {
         return;
      }

      if (name == null && parent == null)
      {
         missingFieldWarning("name");
      }

      if (parent != null && description == null && name == null)
      {
         missingFieldWarning("description");
      }
   }

   public String getFallbackValue(String field)
   {
      if (field.equals("sort"))
      {
         field = resource.getSymbolDefaultSortField();

         if (field.equals("id"))
         {
            return getOriginalId();
         }

         String val = getFieldValue(field);

         if (val != null)
         {
            return val;
         }
      }

      return super.getFallbackValue(field);
   }

   public BibValueList getFallbackContents(String field)
   {
      if (field.equals("sort"))
      {
         field = resource.getSymbolDefaultSortField();

         if (field.equals("id"))
         {
            return getIdField();
         }

         BibValueList val = getField(field);

         if (val != null)
         {
            return val;
         }
      }

      return super.getFallbackContents(field);
   }

}
