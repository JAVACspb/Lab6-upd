package data;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * Class for study group coordinates
 */
public class Coordinates implements Comparable<Coordinates>, Serializable {
    private Integer x;
    private Double y;

    /**
     * Class constructor
     *
     * @param aX - first coordinate
     * @param aY - second coordinate
     */
    public Coordinates(Integer aX, Double aY) {
        x = aX;
        y = aY;
    }

    /**
     * Class constructor for Xml parser
     */
    public Coordinates() {
        x = null;
        y = null;
    }

    @XmlElement(name = "CoordinateX")
    public void setX(Integer aX) {
        x = aX;
    }

    @XmlElement(name = "CoordinateY")
    public void setY(Double aY) {
        y = aY;
    }

    public Integer getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    private double dist0(Coordinates aCoordinates) {
        return Math.sqrt(Math.pow(aCoordinates.getX(),2) + Math.pow(aCoordinates.getY(),2));
    }

    @Override
    public String toString() {
        return getX() + ", " + getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public int compareTo(Coordinates o) {
        double dist = dist0(this) - dist0(o);
        if (dist > 0) return 1;
        else if (dist ==0) return 0;
        else return -1;
    }
}