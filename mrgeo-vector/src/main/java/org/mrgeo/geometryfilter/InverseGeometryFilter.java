/*
 * Copyright 2009-2015 DigitalGlobe, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.mrgeo.geometryfilter;

import org.mrgeo.geometry.WritableGeometry;

public class InverseGeometryFilter extends GeometryFilter
{
  private static final long serialVersionUID = 1L;
  String inputAttribute, outputAttribute;
  
  public InverseGeometryFilter(String inputAttribute, String outputAttribute)
  {
    this.inputAttribute = inputAttribute;
    this.outputAttribute = outputAttribute;
  }
  
  @Override
  public WritableGeometry filterInPlace(WritableGeometry g)
  {
    double v = Double.parseDouble(g.getAttribute(inputAttribute));
    g.setAttribute(outputAttribute, Double.toString(1 / v));
    return g;
  }
}
