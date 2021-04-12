public class Pair<U,V> {
    public U f;
    public V s;
    
    public Pair(U f, V s) {
        this.f = f;
        this.s = s;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
 
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
 
        Pair<?, ?> pair = (Pair<?, ?>) o;
 
        // call `equals()` method of the underlying objects
        if (!f.equals(pair.f)) {
            return false;
        }
        return s.equals(pair.s);
    }
}
