public class Particle {
    public Pair<Double, Double> loc;
    public double angle = Math.random() * 2 * Math.PI;
    public double vel = 0.01;
    public double senseDistance = 0.01;
    public double senseRadius = 0.005;
    public double senseAngle = 0.3;
    public double turnRate = 0.3;
    public int species = (int)(Math.random()*Constants.numSpecies);
    
    public Particle() {
        this.loc = new Pair<Double, Double>(0.0, 0.0);
    }
    
    public Particle(double x, double y) {
        this.loc = new Pair<Double, Double>(x, y);
    }
    
    public Particle(double x, double y, double angle, double vel) {
        this.loc = new Pair<Double, Double>(x, y);
        this.angle = angle;
        this.vel = vel;
    }
    
    public double vx() {
        return this.vel * Math.cos(this.angle);
    }
    
    public double vy() {
        return this.vel * Math.sin(this.angle);
    }
    
    // center of left sensor
    public Pair<Double, Double> leftSensor() {
        double ang = angle - senseAngle;
        double cx = loc.f + senseDistance * Math.cos(ang);
        cx = (cx + 1) % 1;
        double cy = loc.s + senseDistance * Math.sin(ang);
        cy = (cy + 1) % 1;
        return new Pair<Double,Double>(cx, cy);
    }
    
    public Pair<Double, Double> rightSensor() {
        double ang = angle + senseAngle;
        double cx = loc.f + senseDistance * Math.cos(ang);
        cx = (cx + 1) % 1;
        double cy = loc.s + senseDistance * Math.sin(ang);
        cy = (cy + 1) % 1;
        return new Pair<Double,Double>(cx, cy);
    }
    
    public Pair<Double, Double> midSensor() {
        double ang = angle;
        double cx = loc.f + senseDistance * Math.cos(ang);
        cx = (cx + 1) % 1;
        double cy = loc.s + senseDistance * Math.sin(ang);
        cy = (cy + 1) % 1;
        return new Pair<Double,Double>(cx, cy);
    }
}
