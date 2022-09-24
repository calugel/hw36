package ge.ufc.figures;


import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "triangle")
public class Triangle {

    private double a;
    private double b;
    private double c;

    public Triangle(){};
    public Triangle(double a, double b, double c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @XmlElement(name = "a")
    public void setA(double a) {
        this.a = a;
    }

    @XmlElement(name = "b")
    public void setB(double b) {
        this.b = b;
    }

    @XmlElement(name = "c")
    public void setC(double c) {
        this.c = c;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getPerimeter(){
        return a + b + c;
    }

    @Override
    public String toString() {
        return "[ Triangle : a = " + a + ", b = " + b + ", c = " + c + " ]";
    }
}
