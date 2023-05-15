package agridrone.model;

public class Item extends ItemAbstract{
    
    private double marketValue;
    
    public Item(String name, int x, int y, int w, int l, int h, double p, double mv) {
        super(name, x, y, w, l, h, p);
        this.marketValue = mv;
    }
    

    public double getMarketValue() {
        return this.marketValue;
    }
    public void setMarketValue(double mv) {
        this.marketValue = mv;
    }
    
    public void delete() {
        this.getParentContainer().getContents().remove(this);
    }


}