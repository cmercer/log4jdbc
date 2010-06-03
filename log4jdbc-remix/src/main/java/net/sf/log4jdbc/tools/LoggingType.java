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
 *
 */
package net.sf.log4jdbc.tools;

public enum LoggingType {
    // SINGLE_LINE is good for human readable SQL
    // MULTI_LINE is better for human readable SQL
    // SINGLE_LINE_TWO_COLUMNS is good for pasting to excel to get table names in first column and full sequel in second
    DISABLED, MULTI_LINE, SINGLE_LINE, SINGLE_LINE_TWO_COLUMNS
}