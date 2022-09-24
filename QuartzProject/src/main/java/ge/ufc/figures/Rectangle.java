package ge.ufc.figures;

public class Rectangle {

    private double width;
    private double length;

    public Rectangle(double width, double length){
        this.width = width;
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public double getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "[ Rectangle width = " + width + ", Rectangle length = " + length + " ]";
    }
}
