public class Driver
{
    public static void main(String[] args) {
        StdDraw.setScale();
        StdDraw.enableDoubleBuffering();
        
        Area area = new Area(300);
        for(int i = 0; i < 1000; i++) {
            area.particles.add(new Particle(0.5, 0.5));
        }
        while(true) {
            area.step();
            area.draw();
            StdDraw.show();
        }
    }
}
