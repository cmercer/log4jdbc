/**
 * Copyright 2007-2010 Arthur Blake
 * Copyright 2010      Tim Azzopardi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.log4jdbc;

import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * RDBMS specifics for the Oracle DB.
 *
 * @author Arthur Blake
 * @author Tim Azzopardi use SQL92 syntax
 */
class OracleRdbmsSpecifics extends RdbmsSpecifics
{
  OracleRdbmsSpecifics()
  {
    super();
  }
  
  private static final DateFormat dateFormat = 
      new SimpleDateFormat("yyyy-MM-dd");

    private static final DateFormat timestampFormat = 
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    String formatParameterObject(Object object)
    {
      if (object == null)
      {
        return super.formatParameterObject(object);
      }
      else
      {
        if (object instanceof Timestamp)
        {
          // Use SQL92 standard timestamp literal which Oracle 10/11 is happy with
          return "TIMESTAMP '" + timestampFormat.format(object) + "'";
        }
        else if (object instanceof Date)
        {
          // Use SQL92 standard date literal http://sqlzoo.net/sql92.html#date_literal
          return "DATE '" + dateFormat.format(object) + "'";
        }
        else
        {
            return super.formatParameterObject(object);
        }
      }
    }

}
