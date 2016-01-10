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
package rcdemo.simulator;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.ode.nonstiff.HighamHall54Integrator;
import rcdemo.math.StateIntegrator;
import rcdemo.physics.CombinedForceModel;
import rcdemo.physics.ConstantForceModel;
import rcdemo.physics.FrictionForceModel;
import rcdemo.track.Track;
import rcdemo.track.TrackODE;

/**
 *
 * @author ezander
 */
public class ODESimulator extends Simulator {
    StateIntegrator stateInt;

    @Override
    protected void reset() {
        Track track = state.getTrack();
        TrackODE ode2 = new TrackODE(track, new CombinedForceModel().add(ConstantForceModel.createGravityForceModel(1, 9.81), 1).add(new FrictionForceModel(), 0 * 0.01));
        double v0 = state.getV0();
        double dxds = track.getDxDs(0).getNorm();
        double dsdt0 = v0 / dxds;
        ArrayRealVector y = new ArrayRealVector(new double[]{0, dsdt0});
        StateIntegrator.setDefaultIntegrator(new HighamHall54Integrator(1e-6, 1, 1e-8, 1e-8));
        stateInt = new StateIntegrator(ode2, y);
    }

    @Override
    double[] stepTo(double simTime) {
        stateInt.integrateTo(simTime);
        return stateInt.getY().toArray();
    }
    
    public void reverse() {
        ArrayRealVector y = stateInt.getY();
        y.setEntry(1, -y.getEntry(1));
        stateInt.setY(y);
    }
    
}
//class SimpleSimulator extends Simulator {
//        for (double s = 0; s < 10 * track.length(); s += 0.1) {
//            System.out.println(s);
//            Vector3d vector = TrackHelper.getPosition(track, s);
//            Transform3D transform = new Transform3D();
//            transform.setTranslation(vector);
//            car.setTransform(transform);
//
//            sleep(0.01);
//        }
//}
