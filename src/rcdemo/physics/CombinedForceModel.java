/*
 * Copyright (C) 2016 ezander
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package rcdemo.physics;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author ezander
 */
public class CombinedForceModel implements ForceModel {
    private final List<ForceModel> models;
    private final List<Double> factors;

    public CombinedForceModel() {
        factors = new ArrayList<>();
        models = new ArrayList<>();
    }

    public CombinedForceModel add(ForceModel model) {
        return add(model, 1.0);
    }

    public CombinedForceModel add(ForceModel model, double factor) {
        models.add(model);
        factors.add(factor);
        return this;
    }

    @Override
    public RealVector getForce(RealVector x, RealVector v) {
        ArrayRealVector F = new ArrayRealVector(x.getDimension());
        for (int i = 0; i < models.size(); i++) {
            ForceModel model = models.get(i);
            F.combineToSelf(1.0, factors.get(i), model.getForce(x, v));
        }
        return F;
    }

    @Override
    public double getPotentialEnergy(RealVector x, RealVector v) {
        double E = 0;
        for (ForceModel model : models) {
            E += model.getPotentialEnergy(x, v);
        }
        return E;
    }
    
}
