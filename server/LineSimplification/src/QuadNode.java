

import java.awt.*;


public class QuadNode extends Nokta {

    QuadNode sagUst = null,
             sagAlt=null,
             solAlt=null,
             solUst=null;

    Color color;


    public QuadNode(double x,double y,int id) {

        this.x=x;
        this.y=y;
        this.id=id;

    }
}