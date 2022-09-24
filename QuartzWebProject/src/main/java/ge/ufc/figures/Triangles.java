package ge.ufc.figures;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "triangle")
public class Triangles {

    private List<Triangle> triangles;

    public List<Triangle> getTriangles() {
        return triangles;
    }

    @XmlElement(name = "triangle")
    public void setTriangles(List<Triangle> triangles) {
        this.triangles = triangles;
    }


}
