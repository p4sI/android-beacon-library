package org.altbeacon.beacon.distance;

import org.altbeacon.beacon.logging.LogManager;

/**
 * This class estimates the distance between the mobile device and a BLE beacon based on the measured
 * RSSI and a txPower calibration value that represents the expected RSSI for an iPhone 5 receiving
 * the signal when it is 1 meter away.
 *
 * This class uses a polynomial regression model.  The coefficients must
 * be supplied by the caller and are specific to the Android device being used.  See the
 * <code>ModelSpecificDistanceCalculator</code> for more information on the coefficients.
 *
 * Created by ppflueger on 13.02.17.
 */

public class PolynomialRegressionDistanceCalculator implements DistanceCalculator {

    public static final String TAG = "PolynomialRegressionDistanceCalculator";
    private double mCoefficient0;
    private double mCoefficient1;
    private double mCoefficient2;

    /**
     * Construct a calculator with coefficients specific for the device's signal vs. distance
     *
     * @param coefficient0
     * @param coefficient1
     * @param coefficient2
     */
    public PolynomialRegressionDistanceCalculator(double coefficient0, double coefficient1, double coefficient2) {
        mCoefficient0 = coefficient0;
        mCoefficient1 = coefficient1;
        mCoefficient2 = coefficient2;
    }

    /**
     * Calculated the estimated distance in meters to the beacon based on a reference rssi at 1m
     * and the known actual rssi at the current location
     *
     * @param txPower
     * @param rssi
     * @return estimated distance
     */
    @Override
    public double calculateDistance(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        LogManager.d(TAG, "calculating distance based on mRssi of %s and txPower of %s", rssi, txPower);

        double distance;
        distance =  (mCoefficient0)*Math.pow(rssi,2) + mCoefficient1*rssi + mCoefficient2;

        LogManager.d(TAG, "avg mRssi: %s distance: %s", rssi, distance);
        return distance;
    }
}
