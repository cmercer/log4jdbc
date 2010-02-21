/**
 * Copyright 2010 Tim Azzopardi
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

import java.util.List;

public interface ResultSetCollector {

  public boolean methodReturned(ResultSetSpy resultSetSpy,
      String methodCall, Object returnValue, Object targetObject,
      Object... methodParams);

  public void preMethod(ResultSetSpy resultSetSpy, String methodCall, Object... methodParams);
  
  public List<List<Object>> getRows();

  public int getColumnCount();

  public String getColumnName(int column);

  public void reset();


}