package com.sem.pool.scene;

import com.badlogic.gdx.math.Vector3;

import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Utility class that allows calculating various collision related
 * variables, such as a reflected vector or new directions after
 * balls collide.
 */
public class PhysicsUtils {
    /**
     * Method which can be called to reflect a vector off another vector
     * which can be used for changing direction after collision.
     * @param directionV direction of our object.
     * @param normalV normal of the object we collide with.
     * @return a new Vector3 which is the reflected vector.
     */
    public static Vector3 reflectVector(Vector3 directionV, Vector3 normalV) {
        Vector3 direction = new Vector3(directionV);
        Vector3 normal = new Vector3(normalV);
        direction.nor();
        normal.nor();
        Vector3 reflectedVector = direction.add(normal.scl(-2 * direction.dot(normal)));
        return new Vector3(reflectedVector.nor());
    }

    public static float[] getSpeedOnCollision(Vector3 direction1, float speed1, Vector3 directionTo,
                                              Vector3 direction2, float speed2) {
        // Calculate phi, the angle of the direction of collision.
        double phi = acos(directionTo.x);

        // Calculate theta1 and theta2, the angle of the respective ball's direction.
        double theta1 = acos(direction1.x);

        double theta2 = acos(direction2.x);
        theta2 = checkAngle(direction2.x, theta2);
        phi = checkAngle(directionTo.z, phi);
        theta1 = checkAngle(direction1.z, theta1);

        // Calculate and set the speed and direction of ball 1
        // We do this using two methods now,
        // but keep the old code here in case something went wrong.
        double v1x = calculateVx(speed1, speed2, theta1, theta2, phi);
        //v2 * cos(theta2 - phi) * cos(phi) + v1 * sin(theta1 - phi)
        //* cos(phi + (PI / 2));
        double v1z = calculateVz(speed1, speed2, theta1, theta2, phi);
        // v2* cos(theta2 - phi) * sin(phi) + v1 * sin(theta1 - phi)
        //* sin(phi + (PI / 2));

        // Calculate and set the speed and direction of ball 2
        double v2x = calculateVx(speed2, speed1, theta2, theta1, phi);
        //v1 * cos(theta1 - phi) * cos(phi) + v2 * sin(theta2 - phi)
        //* cos(phi + (PI / 2));
        double v2z = calculateVz(speed2, speed1, theta2, theta1, phi);
        //v1 * cos(theta1 - phi) * sin(phi) + v2 * sin(theta2 - phi)
        //* sin(phi + (PI / 2));

        float newSpeed1 = calculateBallSpeed(v1x, v1z);
        float newSpeed2 = calculateBallSpeed(v2x, v2z);

        return new float[] {newSpeed1, newSpeed2};
    }

    /**
     * Calculates the new speed for elastic collisions given the speed
     * in the x and z direction.
     * @param speedX speed in the x direction.
     * @param speedZ speed in the z direction.
     * @return the new speed of the ball.
     */
    protected static float calculateBallSpeed(double speedX, double speedZ) {
        return (float) Math.sqrt(speedX * speedX + speedZ * speedZ);
    }

    /**
     * Calculates the speed for a ball after elastic collisions in the x direction.
     * @param v1 Our speed.
     * @param v2 Speed of other object.
     * @param theta1 Angle of our direction.
     * @param theta2 Angle of direction of other object.
     * @param phi Angle of collision between objects.
     * @return Speed in the x direction after collision.
     */
    private static double calculateVx(double v1, double v2, double theta1, double theta2, double phi) {
        return v2 * cos(theta2 - phi) * cos(phi) + v1 * sin(theta1 - phi)
                * cos(phi + (PI / 2));
    }

    /**
     * Calculates the speed for a ball after elastic collisions in the z direction.
     * @param v1 Our speed.
     * @param v2 Speed of other object.
     * @param theta1 Angle of our direction.
     * @param theta2 Angle of direction of other object.
     * @param phi Angle of collision between objects.
     * @return Speed in the z direction after collision.
     */
    private static double calculateVz(double v1, double v2, double theta1, double theta2, double phi) {
        return v2 * cos(theta2 - phi) * sin(phi) + v1 * sin(theta1 - phi)
                * sin(phi + (PI / 2));
    }

    /**
     * If the movement in the direction is negative, the angle will be multiplied by -1.
     * @param direction direction on the axis we're moving in.
     * @param angle angle.
     * @return the updated angle.
     */
    private static double checkAngle(float direction, double angle) {
        if (direction < 0) {
            angle *= -1;
        }
        return angle;
    }
}
