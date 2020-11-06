package de.rwth.visualization.car;

import de.CoordHelper;
import de.rwth.visualization.coord.Rotation;
import de.rwth.visualization.track.Track;
import de.rwth.visualization.track.Wall;
import de.rwth.visualization.track.WallCurved;
import de.rwth.visualization.track.WallLinear;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Sensor {
    private RealVector offset;
    private RealVector direction;

    public Sensor(RealVector offset, RealVector direction) {
        this.offset = offset;
        this.direction = direction;
    }

    public RealVector getDirection() {
        double degree = Car.getDegree();
        RealMatrix rotationMatrix = Rotation.getMatrix(degree);
        RealVector rotatedDirection = rotationMatrix.operate(this.direction);
        //RealVector position = this.getPosition();
        return rotatedDirection;
    }

    public RealVector getPosition() {
        double degree = Car.getDegree();
        RealMatrix rotationMatrix = Rotation.getMatrix(degree);
        RealVector position = Car.getPosition();
        RealVector offset = position.add(this.offset).subtract(position);
        RealVector rotatedOffset = rotationMatrix.operate(offset);
        return rotatedOffset.add(position);
    }

    @Override
    public String toString() {
        return String.format("Sensor - Offset: %s - Direction: %s, %s - Position: %s",
                this.offset, this.direction, this.getDirection(), this.getPosition());
    }

    /*public List<Double> getDistances(TrackPart trackPart) {
        if(trackPart instanceof TrackPartLinear) {
            return this.getDistances((TrackPartLinear)trackPart);
        } else if(trackPart instanceof TrackPartCircle) {
            return this.getDistances((TrackPartCircle)trackPart);
        }
        return new ArrayList<>();
    }

    public List<Double> getDistances(TrackPartLinear trackPartLinear) {
        List<Double> result = new ArrayList<>();

        RealVector pointUpperLeft = trackPartLinear.pointUpperLeft;
        RealVector pointUpperRight = trackPartLinear.pointUpperRight;

        double distanceUpper = CoordHelper.getDistanceLine(pointUpperLeft, pointUpperRight,
                this.getPosition(Car.position), this.getDirection(Car.degree));

        RealVector pointLowerLeft = trackPartLinear.pointLowerLeft;
        RealVector pointLowerRight = trackPartLinear.pointLowerRight;

        double distanceLower = CoordHelper.getDistanceLine(pointLowerLeft, pointLowerRight,
                this.getPosition(Car.position), this.getDirection(Car.degree));

        result.add(distanceUpper);
        result.add(distanceLower);

        return result;
    }

    public List<Double> getDistances(TrackPartCircle trackPartCircle) {
        List<Double> result = new ArrayList<>();

        RealVector point = trackPartCircle.point;
        double radiusInner = trackPartCircle.radiusInner;
        double radiusOuter = trackPartCircle.radiusOuter;

        double distanceInnerPlus =
                CoordHelper.getDistanceCircle(this.getPosition(Car.position),this.getDirection(Car.degree),
                        point, radiusInner, true);
        double distanceInnerMinus =
                CoordHelper.getDistanceCircle(this.getPosition(Car.position),this.getDirection(Car.degree),
                        point, radiusInner, false);
        double distanceOuterPlus =
                CoordHelper.getDistanceCircle(this.getPosition(Car.position),this.getDirection(Car.degree),
                        point, radiusOuter, true);
        double distanceOuterMinus =
                CoordHelper.getDistanceCircle(this.getPosition(Car.position),this.getDirection(Car.degree),
                        point, radiusOuter, false);

        result.add(distanceInnerPlus);
        result.add(distanceInnerMinus);
        result.add(distanceOuterPlus);
        result.add(distanceOuterMinus);

        return result;
    }*/

    /*public double getDistance(TrackPart trackPart) {
        if(trackPart instanceof TrackPartLinear) {
            return this.getDistance((TrackPartLinear)trackPart);
        } else if(trackPart instanceof TrackPartCircle) {
            return this.getDistance((TrackPartCircle)trackPart);
        }
        return Double.MAX_VALUE;
    }

    public double getDistance(TrackPartLinear trackPartLinear) {
        List<Double> distances = this.getDistances(trackPartLinear);

        return 0;
    }

    public double getDistance(TrackPartCircle trackPartCircle) {
        return 0;
    }

    public List<RealVector> getIntersections(TrackPart trackPart) {
        if(trackPart instanceof TrackPartLinear) {
            return this.getIntersections((TrackPartLinear)trackPart);
        } else if(trackPart instanceof TrackPartCircle) {
            return this.getIntersections((TrackPartCircle)trackPart);
        }
        return new ArrayList<>();
    }

    public List<RealVector> getIntersections(TrackPartLinear trackPartLinear) {
        List<RealVector> result = new ArrayList<>();

        RealVector pointUpperLeft = trackPartLinear.pointUpperLeft;
        RealVector pointUpperRight = trackPartLinear.pointUpperRight;

        RealVector point1 =
                CoordHelper.getIntersectionLine(pointUpperLeft, pointUpperRight, this.getPosition(Car.position),
                        this.getDirection(Car.degree));

        RealVector pointLowerLeft = trackPartLinear.pointLowerLeft;
        RealVector pointLowerRight = trackPartLinear.pointLowerRight;

        RealVector point3 =
                CoordHelper.getIntersectionLine(pointUpperLeft, pointLowerLeft, this.getPosition(Car.position),
                        this.getDirection(Car.degree));

        result.add(point1);
        result.add(point3);

        return result;
    }

    public List<RealVector> getIntersections(TrackPartCircle trackPartCircle) {
        List<RealVector> result = new ArrayList<>();

        RealVector point = trackPartCircle.point; //midpoint of both circles
        double radiusInner = trackPartCircle.radiusInner;
        double radiusOuter = trackPartCircle.radiusOuter;


        RealVector distance1InnerPlus =
                CoordHelper.getIntersectionCircle(this.getPosition(Car.position),this.getDirection(Car.degree),
                        point, radiusInner, true);
        RealVector distance1InnerMinus =
                CoordHelper.getIntersectionCircle(this.getPosition(Car.position),this.getDirection(Car.degree),
                        point, radiusInner, false);
        RealVector distance1OuterPlus =
                CoordHelper.getIntersectionCircle(this.getPosition(Car.position),this.getDirection(Car.degree),
                        point, radiusOuter, true);
        RealVector distance1OuterMinus =
                CoordHelper.getIntersectionCircle(this.getPosition(Car.position),this.getDirection(Car.degree),
                        point, radiusOuter, false);

        result.add(distance1InnerPlus);
        result.add(distance1InnerMinus);
        result.add(distance1OuterPlus);
        result.add(distance1OuterMinus);

        return result;
    }

    public Map<RealVector, Double> getParameters(TrackPart trackPart) {
        Map<RealVector, Double> result = new HashMap<>();
        List<RealVector> intersections = this.getIntersections(trackPart);
        RealVector position = this.getPosition(Car.position);
        RealVector direction = this.getDirection(Car.degree);

        for(RealVector intersection : intersections) {
            double scalar =
                    (intersection.getEntry(0) - position.getEntry(0)) / direction.getEntry(0);
            result.put(intersection, scalar);
        }

        return result;
    }

    public List<Double> getDistances(TrackPart trackPart) {
        List<Double> result = new ArrayList<>();
        RealVector position = this.getPosition(Car.position);
        Map<RealVector, Double> parameters = this.getParameters(trackPart);
        Set<Map.Entry<RealVector, Double>> entries = parameters.entrySet();

        for(Map.Entry<RealVector, Double> entry : entries) {
            if(entry.getValue() >= 0) {
                RealVector intersection = entry.getKey();
                double distance = position.getDistance(intersection);
                result.add(distance);
            }
        }

        return result;
    }

    public double getMinDistance(TrackPart trackPart) {
        List<Double> distances = this.getDistances(trackPart);
        double minDistance = Double.MAX_VALUE;

        for(Double distance : distances) {
            minDistance = Math.min(distance, minDistance);
        }

        return minDistance;
    }*/

    public List<RealVector> getIntersections(Wall wall) {
        if(wall instanceof WallLinear) {
            return this.getIntersections((WallLinear)wall);
        } else if(wall instanceof WallCurved) {
            return this.getIntersections((WallCurved)wall);
        }
        return new ArrayList<>();
    }

    public List<RealVector> getIntersections(WallLinear wall) {
        List<RealVector> result = new ArrayList<>();

        try {
            RealVector position = this.getPosition();
            RealVector direction = this.getDirection();
            RealVector intersection =
                    CoordHelper.getIntersectionLine(wall.pointLeft, wall.pointRight, position, direction);

            if(wall.inBoundaries(intersection)) {
                result.add(intersection);
            }

            return result;
        } catch(Exception exception) {
            return result;
        }
    }

    public List<RealVector> getIntersections(WallCurved wall) {
        List<RealVector> result = new ArrayList<>();

        try {
            RealVector position = this.getPosition();
            RealVector direction = this.getDirection();
            List<RealVector> intersections =
                    CoordHelper.getIntersectionCircle(position, direction, wall.pointMiddle, wall.radius);

            for(RealVector intersection : intersections) {
                if(wall.inBoundaries(intersection)) {
                    result.add(intersection);
                }
            }

            return result;
        } catch(Exception exception) {
            return result;
        }
    }

    public List<Double> getParameters(Wall wall) {
        List<Double> parameters = new ArrayList<>();
        List<RealVector> intersections = this.getIntersections(wall);

        for(RealVector intersection : intersections) {
            RealVector position = this.getPosition();
            RealVector direction = this.getDirection();

            double scalar = direction.getEntry(0) == 0.0 ?
                    (intersection.getEntry(1) - position.getEntry(1)) / direction.getEntry(1) :
                    (intersection.getEntry(0) - position.getEntry(0)) / direction.getEntry(0);

            parameters.add(scalar);
        }

        return parameters;
    }

    public List<Double> getDistances(Wall wall) {
        List<Double> distances = new ArrayList<>();
        List<RealVector> intersections = this.getIntersections(wall);

        for(RealVector intersection : intersections) {
            RealVector position = this.getPosition();
            double distance = position.getDistance(intersection);

            distances.add(distance);
        }

        return distances;
    }

    public List<Double> getAllDistances() {
        List<Double> allDistances = new ArrayList<>();
        List<Wall> walls = Track.walls;

        int wallIndex = 0;

        System.out.println("====Walls====");
        System.out.println(this);

        for(Wall wall : walls) {
            wallIndex++;
            List<Double> parameters = this.getParameters(wall);
            List<Double> distances = this.getDistances(wall);

            for(int i = 0; i < parameters.size(); i++) {
                double parameter = parameters.get(i);

                if(parameter >= 0) {
                    double distance = distances.get(i);

                    System.out.println("Index: " + wallIndex);
                    allDistances.add(distance);
                }
            }
        }

        return allDistances;
    }

    public double getMinDistance() {
        List<Double> distances = this.getAllDistances();
        double minDistance = Double.MAX_VALUE;

        for(Double distance : distances) {
            minDistance = Math.min(minDistance, distance);
        }

        System.out.println("====Min Distances====");
        System.out.println(this);
        System.out.println("minDistance: " + minDistance);
        return minDistance;
    }
}