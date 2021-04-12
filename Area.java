import java.util.*;
import java.lang.*;

public class Area {
    public ArrayList<Particle> particles;
    public double[][] colors = new double[Constants.numSpecies][3];
    public double[][] destColors = new double[Constants.numSpecies][3];
    public double[][][] trails;
    public int gridSize;
    public double lastUpdateTime = 0;
    
    public Area() {
        this.particles = new ArrayList<>();
        this.gridSize = 100;
        this.trails = new double[Constants.numSpecies][this.gridSize][this.gridSize];
        for(int i = 0; i < Constants.numSpecies; i++) {
            colors[i] = randomColor();
        }
    }
    
    public Area(int gridSize) {
        this.particles = new ArrayList<>();
        this.gridSize = gridSize;
        this.trails = new double[Constants.numSpecies][this.gridSize][this.gridSize];
        for(int i = 0; i < Constants.numSpecies; i++) {
            colors[i] = randomColor();
        }
    }
    
    public double[] randomColor() {
        double[] res = {Math.random(), Math.random(), Math.random()};
        return res;
    }
    
    public double getTrailValue(int species, Pair<Double,Double> loc) {
        int x = (int)(loc.f * gridSize);
        int y = (int)(loc.s * gridSize);
        double res = 0;
        for(int k = 0; k < Constants.numSpecies; k++) {
            if(k == species) res += trails[k][x][y];
            else res -= trails[k][x][y];
        }
        return res;
    }
    
    public void updateParticle(Particle p) {
        if(System.currentTimeMillis() - lastUpdateTime <= 1000) {
            double r = Math.random();
            if(r < 1.0/3.0) {
                p.angle -= p.turnRate;
            } else if(r > 2.0/3.0) {
                p.angle += p.turnRate;
            }
        } else {
            double vl = getTrailValue(p.species, p.leftSensor());
            double vm = getTrailValue(p.species, p.midSensor());
            double vr = getTrailValue(p.species, p.rightSensor());
            if(vl >= vm && vl >= vr) {
                p.angle -= p.turnRate;
            } else if(vr >= vm && vr >= vl) {
                p.angle += p.turnRate;
            }
        }
        p.loc.f += p.vx();
        p.loc.s += p.vy();
        p.loc.f += 1; p.loc.f %= 1;
        p.loc.s += 1; p.loc.s %= 1;
    }
    
    public void step() {
        if (System.currentTimeMillis() - lastUpdateTime >= 45000) {
            lastUpdateTime = System.currentTimeMillis();
            for(int i = 0; i < Constants.numSpecies; i++) {
                destColors[i] = randomColor();
            }
        }
        for(int i = 0; i < Constants.numSpecies; i++) {
            for(int j = 0; j < 3; j++) {
                if(Math.abs(colors[i][j] - destColors[i][j]) > 0.01) {
                    if(colors[i][j] > destColors[i][j]) colors[i][j] -= 0.01;
                    else colors[i][j] += 0.01;
                }
            }
        }
        // update positions
        for(Particle p: particles) {
            updateParticle(p);
        }
        // evaporate trails
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                for(int k = 0; k < Constants.numSpecies; k++) {
                    trails[k][i][j] *= 0.99;
                }
            }
        }
        // create new trail heads
        for(Particle p: particles) {
            trails[p.species][(int)(p.loc.f*gridSize)][(int)(p.loc.s*gridSize)] = 1;
        }
        // diffuse trails
        double[][][] temp = new double[Constants.numSpecies][gridSize][gridSize];
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                for(int k = 0; k < Constants.numSpecies; k++) {
                    for(int dx = -1; dx <= 1; dx++) {
                        for(int dy = -1; dy <= 1; dy++) {
                            int x = (i+dx+gridSize)%gridSize;
                            int y = (j+dy+gridSize)%gridSize;
                            temp[k][i][j] += trails[k][x][y]/9.0;
                        }
                    }
                }
            }
        }
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                for(int k = 0; k < Constants.numSpecies; k++) {
                    trails[k][i][j] = temp[k][i][j];
                }
            }
        }
    }

    public void draw() {
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                double[] d = new double[3];
                for(int k = 0; k < 3; k++) {
                    for(int k1 = 0; k1 < Constants.numSpecies; k1++) {
                        d[k] += trails[k1][i][j] * colors[k1][k];
                    }
                }
                double mx = 0;
                for(int k = 0; k < 3; k++) {
                    mx = Math.max(mx, d[k]);
                }
                if(mx > 1) {
                    for(int k = 0; k < 3; k++) {
                        d[k] /= mx;
                    }
                }
                int[] c = new int[3];
                for(int k = 0; k < 3; k++) {
                    c[k] = Math.min(255, (int)(d[k]*256));
                }
                StdDraw.setPenColor(c[0],c[1],c[2]);
                
                double x = (i+0.5)/gridSize;
                double y = (j+0.5)/gridSize;
                StdDraw.filledSquare(x, y, 0.5/gridSize);
            }
        }
    }
}
