package code;

public class Ship{
    int lastTimeStamp; //initially 0
    int remPass; //initially total passengers ,changes only when visits the ship

    public Ship(int t, int rem){
        lastTimeStamp=t;
        remPass=rem;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "lastTimeStamp=" + lastTimeStamp +
                ", remPass=" + remPass +
                '}';
    }
    public boolean exists(int time){
        return (remPass-(time-lastTimeStamp))>CoastGuard.wreckTime;
    }
    public String visualize(int time){
        int rem = remPass-(time-lastTimeStamp);
        int passengersAlive = Math.max(rem, 0);
        int boxDamage = rem>0?0:Math.abs(rem)+1;
        return "Passengers alive :"+passengersAlive+", Black Box Damage :"+boxDamage;
    }
}