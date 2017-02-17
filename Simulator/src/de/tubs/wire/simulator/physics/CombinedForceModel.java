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
package de.tubs.wire.simulator.physics;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Combines two {@link ForceModel}s into one, summing up all forces and 
 * (potential) energies.
 * 
 * @author ezander
 */
public class CombinedForceModel implements ForceModel {
    private final List<ForceModel> models;
    private final List<Double> factors;

    /**
     * Create a new combined force model.
     */
    public CombinedForceModel() {
        factors = new ArrayList<>(2);
        models = new ArrayList<>(2);
    }

    /**
     * Add a force to the model.
     * 
     * @param model The force to add.
     * 
     * @return The combined model itself.
     */
    public CombinedForceModel add(ForceModel model) {
        return add(model, 1.0);
    }

    /**
     * Add a scaled force to the model.
     * 
     * @param model The force to add.
     * @param factor The factor.
     * @return The combined model itself.
     */
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
        for (int i = 0; i < models.size(); i++) {
            ForceModel model = models.get(i);
            E += factors.get(i) * model.getPotentialEnergy(x, v);
        }
        return E;
    }
    
}
