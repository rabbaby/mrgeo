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

import org.geotools.referencing.CRS;
import org.mrgeo.data.GeometryCollection;
import org.mrgeo.geometry.WritableGeometry;
import org.opengis.referencing.FactoryException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Iterator;

public class ReprojectedGeometryCollection implements GeometryCollection
{
  static class LocalIterator implements Iterator<WritableGeometry>
  {
    private int currentIndex = 0;
    private final ReprojectedGeometryCollection parent;

    public LocalIterator(final ReprojectedGeometryCollection parent)
    {
      this.parent = parent;
    }

    @Override
    public boolean hasNext()
    {
      return currentIndex < parent.size();
    }

    @Override
    public WritableGeometry next()
    {
      return parent.get(currentIndex++);
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }

  private static final long serialVersionUID = 1L;

  private final String newProjection;
  private transient Reprojector reprojector;
  private GeometryCollection src;

  public ReprojectedGeometryCollection(final GeometryCollection src, final String newProjection)
  {
    this.src = src;
    this.newProjection = newProjection;

    reprojector = Reprojector.createFromWkt(src.getProjection(), newProjection);
  }

  @Override
  public void close()
  {
    if (src != null)
    {
      src.close();
    }
  }

  @Override
  public WritableGeometry get(final int index)
  {
    final WritableGeometry result = src.get(index).createWritableClone();
    result.filter(reprojector);
    return result;
  }


  public String getProjection()
  {
    return newProjection;
  }

  @Override
  public Iterator<WritableGeometry> iterator()
  {
    return new LocalIterator(this);
  }

  public int size()
  {
    return src.size();
  }

  private void readObject(final ObjectInputStream is) throws ClassNotFoundException, IOException
  {
    // always perform the default de-serialization first
    is.defaultReadObject();

    reprojector = Reprojector.createFromWkt(src.getProjection(), newProjection);
  }

}
